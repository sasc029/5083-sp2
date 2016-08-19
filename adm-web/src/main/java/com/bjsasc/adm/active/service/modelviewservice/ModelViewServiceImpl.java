package com.bjsasc.adm.active.service.modelviewservice;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bjsasc.adm.active.model.activedocument.ActiveDocument;
import com.bjsasc.adm.common.ActiveInitParameter;
import com.bjsasc.adm.common.AdmLifeCycleConstUtil;
import com.bjsasc.platform.objectmodel.business.persist.PTFactor;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.access.AccessControlHelper;
import com.bjsasc.plm.core.system.access.PtPermS;
import com.bjsasc.plm.core.system.access.operate.Operate;
import com.bjsasc.plm.core.system.config.ConfigHelper;
import com.bjsasc.plm.core.type.TypeDefinition;
import com.bjsasc.plm.core.type.TypeHelper;
import com.bjsasc.plm.core.util.DateTimeUtil;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.grid.GridHelper;
import com.bjsasc.plm.grid.GridRowType;
import com.bjsasc.plm.grid.GridView;
import com.bjsasc.plm.util.JsonUtil;
import com.cascc.avidm.util.SplitString;


/**
 * 模型视图服务实现类。
 * 
 * @author gengancong 2013-6-3
 */
public class ModelViewServiceImpl implements ModelViewService {

	// 根据参数配置确定在获取文件夹内容列表时是否验证权限
	private static final String CHECKACCESSINFOLDER = ConfigHelper.getService().getParameter("checkAccessInFolder",
			"/plm/system");
	/** SPOT */
	private static final String C_SPOT = "ActiveDocumentView";
	/** WHERE */
	private static final String C_WHERE = " where 1=1 ";

	private static final List<String> VIEWFIELDS = initViewFields();

	private static Map<String, String> admMap = new HashMap<String, String>();

	private final List<String> fkeys = new ArrayList<String>();

	@Override
	public List<Object> getActiveDocuments(String keys) {

		// 用于校验权限的集合
		List<Object> forCheckObj = new ArrayList<Object>();

		List<ActiveDocument> result = getAllActiveDocument(keys);
		forCheckObj.addAll(result);

		// 把对象存入缓存，准备没有权限对象的列表
		Map<String, Object> objMap = new HashMap<String, Object>();
		for (Object obj : forCheckObj) {
			objMap.put(Helper.getOid((Persistable) obj), obj);
		}
		List<Object> noAccessObjs = new ArrayList<Object>();
		if (!"false".equals(CHECKACCESSINFOLDER)) {
			Context context = ActiveInitParameter.getActiveContext();
			// 批量校验权限
			List<Map<String, Object>> permRS = AccessControlHelper.getService().hasEntityPermission(
					SessionHelper.getService().getUser(), Operate.ACCESS, context, forCheckObj);

			for (Map<String, Object> temp : permRS) {
				Object obj = temp.get("OBJ");
				String oid = null;
				if (obj instanceof Persistable) {
					Persistable obj2 = (Persistable) obj;
					oid = com.bjsasc.plm.core.Helper.getOid(obj2);
				} else if (obj instanceof PtPermS) {
					PtPermS ptObj = (PtPermS) obj;
					oid = com.bjsasc.plm.core.Helper.getOid(ptObj.getClassId(), ptObj.getInnerId());
				}

				// flag>0则说明对象标示为oid的对象具有权限，否则，应该过滤掉
				// 如果没有权限,把该对象加入待移除列表
				if ((Integer) temp.get("FLAG") <= 0) {
					noAccessObjs.add(objMap.get(oid));
				}
			}
		}
		// 移除没有权限的对象
		forCheckObj.removeAll(noAccessObjs);
		return forCheckObj;
	}

	@Override
	public List<Map<String, Object>> getSubObject(String attr, String keys) {
		List<Object> valueList = new ArrayList<Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 取得现行文件数据
		List<ActiveDocument> adList = getAllActiveDocument(keys);
		// 模型初始化
		getActiveDocumentModel();

		NameComparator nameComp = new NameComparator();
		List<String> keyList = SplitString.string2List(keys, ";");
		String recycleName = AdmLifeCycleConstUtil.LC_RECYCLE.getName();

		for (ActiveDocument obj : adList) {
			if (valueList.size() > 50) {
				break;
			}

			Map<String, Object> dataMap = Helper.getTypeManager().format((PTFactor) obj, fkeys, false);
			Map<String, Object> raw = JsonUtil.toMap(dataMap.get(KeyS.RAW).toString());

			if (recycleName.equals(obj.getLifeCycleInfo().getStateName())) {
				continue;
			}
			if (checkData(raw, obj, keyList)) {
				Map<String, Object> source = (Map<String, Object>) dataMap.get(KeyS.SOURCE);
				String attRUpper = attr.toUpperCase();
				Object value = raw.get(attRUpper);
				Object disValue = source.get(attRUpper);

				if (StringUtil.isNull(value)) {
					value = raw.get(attr);
					disValue = source.get(attr);
					if (StringUtil.isNull(value)) {
						value = "NULL";
						disValue = "NULL";
					}
				} else if ("FOLDER".equalsIgnoreCase(attRUpper)) {
					value = raw.get("FOLDER_INNERID");
				} else if ("CONTEXT".equalsIgnoreCase(attRUpper)) {
					value = raw.get("CONTEXT_INNERID");
				} else if ("DOMAIN".equalsIgnoreCase(attRUpper)) {
					value = raw.get("DOMAIN_INNERID");
				} else if ("TYPE".equalsIgnoreCase(attRUpper)) {
					value = obj.getClassId();
					disValue = admMap.get(value);
				} else if ("LIFECYCLE_TEMPLATE".equalsIgnoreCase(attRUpper)) {
					value = obj.getLifeCycleInfo().getLifeCycleTemplate();
				} else if ("NEW_MARK".equals(attRUpper)) {
					value = raw.get("NEW_MARK");
				} else if ("HINT".equals(attRUpper)) {
					value = raw.get("HINT");
				} else if ("LATEST_MARK".equals(attRUpper)) {
					value = raw.get("LATEST_MARK");
				}
				if (valueList.contains(disValue)) {
					continue;
				} else {
					Map<String, Object> valueMap = new HashMap<String, Object>();
					valueMap.put("NAME", disValue);
					valueMap.put("KEYNAME", value);
					if (isDate(value, disValue)) {
						valueMap.put("KEYNAME", disValue);
					}
					list.add(valueMap);
					valueList.add(disValue);
				}
			}
		}
		Collections.sort(list, nameComp);
		return list;
	}

	private void getActiveDocumentModel() {
		if (admMap == null || admMap.isEmpty()) {
			String classId = "ActiveDocument";
			TypeDefinition sourceDef = TypeHelper.getService().getType(classId);
			admMap.put(sourceDef.getId(), sourceDef.getName());

			List<TypeDefinition> classDefList = TypeHelper.getService().getDescendants(classId);
			for (TypeDefinition temp : classDefList) {
				admMap.put(temp.getId(), temp.getName());
			}
		}
	}

	/**
	 * 取得现行文件对象。
	 * 
	 * @return List
	 */
	private List<ActiveDocument> getAllActiveDocument(String keys) {

		List<ActiveDocument> result;
		// String where = getWhere(keys);
		WhereParams wps = getWhereParams(keys);
		String where = wps.getWhereSql();
		String typeIn = getTypeInSql();
		Object[] params = wps.getParams();
		
		String sql = "select ad.* from ADM_ACTIVEDOCUMENT ad, "
				+ "(select (CLASSID || ':' || INNERID) OID from V_ADM_STATISTICS " + where + " and " + typeIn
				+ " ) vt where (AD.CLASSID || ':' || AD.INNERID) = VT.OID ORDER BY 	ad.UPDATETIME DESC ";

		if (params.length > 0) {
			result = Helper.getPersistService().query(sql, ActiveDocument.class, params);
		} else {
			result = Helper.getPersistService().query(sql, ActiveDocument.class);			
		}
		return result;
	}

	private String getTypeInSql() {
		Set<String> viewTypeIds = getViewTypes();

		String typeIn = "''";
		for (String typeId : viewTypeIds) {
			typeIn = typeIn + ", '" + typeId + "' ";
		}
		String result = " classId in (" + typeIn + ") ";
		return result;
	}

	/**
	 * 取得默认（被选中）视图的模型类型。
	 * 
	 * @return Set
	 */
	private Set<String> getViewTypes() {
		Set<String> result = new HashSet<String>();
		GridView gridView = GridHelper.getService().getMyLatestGridView(C_SPOT, C_SPOT);
		//视图定义的模型
		List<GridRowType> viewTypes = Helper.getGridManager().getGridRowTypes(gridView.getOid());
		Set<String> viewTypeIds = new HashSet<String>();
		for (GridRowType type : viewTypes) {
			if (type.getIsHardTypeIncluded()) {
				//用户选中了硬类型
				viewTypeIds.add(type.getTypeId());
			}
			viewTypeIds.addAll(type.getSoftTypeIds());
		}

		for (String typeId : viewTypeIds) {
			result.add(typeId);
			result.addAll(getChildrens(typeId));
		}
		return result;
	}

	/**
	 * 取得指定模型的子类型。
	 * 如果子模型还有子的话，也把它取出来。
	 * 
	 * @return Set
	 */
	private Set<String> getChildrens(String typeId) {
		Set<String> result = new HashSet<String>();
		List<TypeDefinition> childrenList = TypeHelper.getService().getChildren(typeId);

		for (TypeDefinition type : childrenList) {
			result.add(type.getId());
			result.addAll(getChildrens(type.getId()));
		}
		return result;
	}

	/**
	 * 判断属性是否是日期类型。
	 * 
	 * @param value
	 *            Object
	 * @param disValue
	 *            Object
	 * @return boolean
	 */
	private boolean isDate(Object value, Object disValue) {
		boolean dateFlag = false;
		int length = disValue.toString().length();
		if (value instanceof Long && (length == 10 || length == 19)) {
			dateFlag = true;
		}
		return dateFlag;
	}

	/**
	 * 通过值属性，取得日期格式。
	 * 
	 * @param value
	 *            String
	 * @return String
	 */
	private String getDateFormat(String value) {
		String format = "";
		if (value.length() == 10) {
			format = DateTimeUtil.DATE_YYYYMMDD;
		} else if (value.length() == 19) {
			format = DateTimeUtil.DATE_YYYYMMDDHHMMSS_2;
		}
		return format;
	}

	/**
	 * NameComparator比较器。
	 */
	private class NameComparator implements Comparator<Map<String, Object>> {
		@Override
		public int compare(Map<String, Object> map1, Map<String, Object> map2) {
			Object object1 = map1.get("NAME");
			Object object2 = map2.get("NAME");
			if (object1 == null || object2 == null) {
				return 0;
			}
			String name1 = object1.toString().toUpperCase();
			String name2 = object2.toString().toUpperCase();
			return name1.compareTo(name2);
		}
	}

	/**
	 * 验证数据有效性。
	 * 
	 * @param dataMap
	 *            Map
	 * @param keyList
	 *            List
	 * @return boolean
	 */
	private boolean checkData(Map<String, Object> dataMap, ActiveDocument obj, List<String> keyList) {
		boolean checkFlag = true;
		for (String key : keyList) {
			List<String> cv = SplitString.string2List(key, ",");
			if (cv.size() < 2) {
				continue;
			}

			String column = cv.get(0);
			String value = cv.get(1);

			List<String> cl = SplitString.string2List(column, ":");
			String columnId = cl.get(1);
			Object object = dataMap.get(columnId.toUpperCase());

			if ("FOLDER".equals(columnId)) {
				object = dataMap.get("FOLDER_INNERID");
			} else if ("CONTEXT".equals(columnId)) {
				object = dataMap.get("CONTEXT_INNERID");
			} else if ("DOMAIN".equals(columnId)) {
				object = dataMap.get("DOMAIN_INNERID");
			} else if ("TYPE".equals(columnId)) {
				object = obj.getClassId();
			} else if ("LIFECYCLE_TEMPLATE".equalsIgnoreCase(columnId)) {
				object = obj.getLifeCycleInfo().getLifeCycleTemplate();
			} else if ("NEW_MARK".equals(columnId)) {
				object = dataMap.get("NEW_MARK");
			} else if ("HINT".equals(columnId)) {
				object = dataMap.get("HINT");
			} else if ("LATEST_MARK".equals(columnId)) {
				object = dataMap.get("LATEST_MARK");
			}
			if (value != null && "NULL".equalsIgnoreCase(value) && object == null) {
				checkFlag = true;
			} else if ((value == null || value.length() == 0) && (object != null && object.toString().length() > 0)) {
				checkFlag = false;
			} else {
				if (object instanceof Long) {
					object = DateTimeUtil.dateDisplay((Long) object, getDateFormat(value));
				} else if (object instanceof Integer) {
					object = object.toString();
				}
				if (!value.equals(object)) {
					checkFlag = false;
				}
			}
		}
		return checkFlag;
	}
	
	class WhereParams {
		private String whereSql;
		private Object[] params;
		public String getWhereSql() {
			return whereSql;
		}
		public void setWhereSql(String whereSql) {
			this.whereSql = whereSql;
		}
		public Object[] getParams() {
			return params;
		}
		public void setParams(Object[] params) {
			this.params = params;
		}
	}
	
	/**
	 * 检索条件做成。
	 * 
	 * @param keys
	 *            String
	 * @return String
	 */
	private WhereParams getWhereParams(String keys) {
		String where = C_WHERE;
		List<String> listParams = new ArrayList<String>();
		WhereParams wps = new WhereParams();
		if (keys == null) {
			wps.setWhereSql(where);
			wps.setParams(null);
			return wps;
		}

		List<String> keyList = SplitString.string2List(keys, ";");
		for (String key : keyList) {
			List<String> cv = SplitString.string2List(key, ",");
			if (cv.size() == 0) {
				continue;
			}
			String column = cv.get(0);
			List<String> cl = SplitString.string2List(column, ":");
			if (cl.size() < 2) {
				continue;
			}
			String columnId = cl.get(1);
			if (!fkeys.contains(columnId)) {
				fkeys.add(columnId);
			}
			if (cv.size() < 2) {
				continue;
			}
			if ("NEW_MARK".equals(columnId) || "HINT".equals(columnId) || "LATEST_MARK".equals(columnId)) {
				continue;
			}
			String value = cv.get(1);
			if (StringUtil.isNull(value)) {
				where += " and " + columnId + " is null ";
				continue;
			} else if ("TYPE".equals(columnId)) {
				where += " and classId = ?";
			} else if ("NUMBER".equals(columnId)) {
				where += " and id = ?";
			} else if ("FOLDER".equals(columnId)) {
				where += " and FOLDER_INNERID = ?";
			} else if ("CONTEXT".equals(columnId)) {
				where += " and CONTEXT_INNERID = ?";
			} else if ("DOMAIN".equals(columnId)) {
				where += " and DOMAIN_INNERID = ?";
			} else if ("ADM_COUNT".equals(columnId)) {
				where += " and COUNT = ?";
			} else if (VIEWFIELDS.contains(columnId)) {
				where += " and " + columnId + " = ?";
			}
			listParams.add(value);
		}
		wps.setWhereSql(where);
		wps.setParams(listParams.toArray());
		return wps;
	}

	private static List<String> initViewFields() {
		List<String> fields = new ArrayList<String>();
		fields.add("TYPE");
		fields.add("NUMBER");
		fields.add("NAME");
		fields.add("DATASOURCE");
		fields.add("PAGES");
		fields.add("ADM_COUNT");
		fields.add("AUTHORNAME");
		fields.add("AUTHORUNIT");
		fields.add("AUTHORTIME");
		fields.add("ACTIVECODE");
		fields.add("NOTE");
		fields.add("LIFECYCLE_STATE");
		fields.add("CREATOR");
		fields.add("LIFECYCLE_TEMPLATE");
		fields.add("CREATE_TIME");
		fields.add("MODIFIER");
		fields.add("MODIFY_TIME");
		fields.add("UPDATE_TIME");
		fields.add("FOLDER");
		fields.add("CONTEXT");
		fields.add("DOMAIN");
		fields.add("SECLEVEL");
		fields.add("VERSION");
		return fields;
	}

}
