package com.bjsasc.adm.active.service.activestatisticsservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.adm.active.model.activemodel.ActiveModel;
//import com.bjsasc.platform.objectmodel.managed.constants.AttrType;
import com.bjsasc.platform.objectmodel.managed.external.util.ModelInfoUtil;
import com.bjsasc.platform.objectmodel.managed.modelattr.data.CommModelAttr;
import com.bjsasc.platform.objectmodel.managed.modelattr.data.CompModelAttr;
import com.bjsasc.platform.objectmodel.managed.modelattr.data.ModelAttr;
import com.bjsasc.platform.objectmodel.managed.modelattr.data.RefModelAttr;
import com.bjsasc.platform.objectmodel.managed.modelattr.data.VirtualModelAttr;
import com.bjsasc.platform.objectmodel.managed.modelattr.service.ModelAttrService;
import com.bjsasc.platform.objectmodel.managed.modelattr.util.ModelAttrUtil;
//import com.bjsasc.platform.objectmodel.managed.pagemgt.util.ObjectModelPageUtil;
import com.bjsasc.platform.objectmodel.util.ObjectModelUtil;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.folder.Folder;
import com.bjsasc.plm.core.util.ExcelUtil;
import com.bjsasc.plm.core.util.StringUtil;
import com.cascc.avidm.util.SplitString;

/**
 * 统计与输出服务实现类。
 * 
 * @author guowei 2013-5-30
 */
public class ActiveStatisticsServiceImpl implements ActiveStatisticsService {
	String param = "";

	@Override
	public String getModelId(String oids) {
		List<String> folderOidsList = SplitString.string2List(oids, ",");
		String comModelOid = "";
		for (String folderOid : folderOidsList) {
			String classId = Helper.getClassId(folderOid);
			if (!"Cabinet".equals(classId)) { 
				String sql = "select * from ADM_ACTIVEMODEL m,ADM_MODELFOLDERLINK l"
						+ " where m.innerId = l.fromObjectId and m.classId = l.fromObjectClassId"
						+ " and l.toObjectClassId || ':' || l.toObjectId = ?";
				List<ActiveModel> modelList = Helper.getPersistService().query(sql, ActiveModel.class, folderOid);
				for (ActiveModel model : modelList) {
					String modelSql = "select innerId as innerId,classId as classId from PT_OM_MODEL where id = ?";
					List<Map<String, Object>> list = Helper.getPersistService().query(modelSql, model.getModelId());
					Map<String, Object> tempMap = list.get(0);
					comModelOid += tempMap.get("CLASSID").toString() + ":" + tempMap.get("INNERID").toString() + ",";
				}
			}
		}
		return comModelOid;
	}

	public List<Map<String, Object>> getClassAttrList(String oids) {
		List<String> modelOidsList = SplitString.string2List(oids, ",");
		List tempMapList = new ArrayList();
		for (String oid : modelOidsList) {
			//获取当前模型的模型标识
			String currClassId = Helper.getClassId(oid);
			//当前模型的唯一标识
			String currInstIId = Helper.getInnerId(oid);
			List mapList = new ArrayList();
			List<ModelAttr> classAttrs = new ArrayList<ModelAttr>();
			//获取类型属性service实例
			ModelAttrService classAttrService = ModelAttrUtil.getService();

			//classAttrs = classAttrService.getSelfModelAttrs(currClassId, currInstIId);
			classAttrs = classAttrService.getModelAttrs(currClassId, currInstIId);

			for (ModelAttr classAttr : classAttrs) {
				//不显示displayName属性
				if (!ModelInfoUtil.DISPLAY_NAME.equals(classAttr.getAttrId())) {
					Map classAttrMap = new HashMap();
					classAttrMap.put("ID", classAttr.getAttrId());
					classAttrMap.put("INNERID", classAttr.getInnerId());
					classAttrMap.put("CLASSID", classAttr.getClassId());
					classAttrMap.put("ATTRNAME", classAttr.getAttrName());
					//若为普通类型属性：
					if (classAttr instanceof CommModelAttr) {
						CommModelAttr commonClassAttr = (CommModelAttr) classAttr;
//						if (null != commonClassAttr.getAttrType()) {
//							//classAttrMap.put("ATTRTYPE", commonClassAttr.getAttrType().getDisplayName()); //存储类型
//						}
					}
					//若为引用类型属性:
					else if (classAttr instanceof RefModelAttr) {
						//classAttrMap.put("ATTRTYPE", AttrType.HARD.getDisplayName()); //引用、组合类型的存储类型都默认为“硬属性”	  
					}
					//若为组合类型属性:
					else if (classAttr instanceof CompModelAttr) {
						//classAttrMap.put("ATTRTYPE", AttrType.HARD.getDisplayName()); //引用、组合类型的存储类型都默认为“硬属性”
					} else if (classAttr instanceof VirtualModelAttr) {
						classAttrMap.put("ATTRTYPE", "虚拟属性"); //存储类型
					}

					String displayName = "";//ObjectModelPageUtil.getAttrDataTypeDispName(classAttr);
					classAttrMap.put("DATATYPE", displayName); //数据类型格式为：引用[应用-类]

					String privateAttr = null;
					if (classAttr.isPrivateAttr()) {
						privateAttr = "是";
					} else {
						privateAttr = "否";
					}
					classAttrMap.put("PRIVATEATTR", privateAttr);
					classAttrMap.put("SORTNO", classAttr.getSortNo());
					classAttrMap.put("INFOSOURCE", classAttr.getInfoSource().name());
					//classAttrMap.put("OPLEVEL", classAttr.getOpLevel());

					//自添加的属性才可以编辑，比当前系统级别高的信息不显示编辑按钮
					if (classAttr.getModelRef().getInnerId().equals(currInstIId)) {
						String id = classAttr.getAttrId();
//						if (!ModelInfoUtil.CLASSID.equals(id) && !ModelInfoUtil.INNERID.equals(id)
//								&& !ModelInfoUtil.FROM_OBJECT_REF.equals(id) && !ModelInfoUtil.TO_OBJECT_REF.equals(id)
//								&& classAttr.getOpLevel() >= ObjectModelUtil.getOpLevel()) {
//							String operation = null;
//							operation = "<a href='#'  onclick='editClassAttr()'>编辑</a>";
//							classAttrMap.put("OPERATION", operation);
//						}
						classAttrMap.put("INHERIT", "false");
					} else {
						classAttrMap.put("INHERIT", "true");
					}

					//					String icon = ObjectModelPageUtil.getIconBySource(classAttr.getInfoSource(), inherit,
					//							request.getContextPath());
					//					if (!AssertUtil.assertNull(icon)) {
					//						classAttrMap.put("ICON", icon); //图标
					//					}
					mapList.add(classAttrMap);
				}
			}
			tempMapList.addAll(mapList);
		}
		return tempMapList;
	}

	public List<Map<String, Object>> getActiveDocumentlist(String folderOids, String file,
			List<Map<String, Object>> stList, List<Map<String, String>> selList, List<Map<String, Object>> optList) {
		String sql = getSql(folderOids, file, stList, selList, optList);
		List<Map<String, Object>> list;
		if (param != null && param.length() > 0) {
			String[] params = param.split(",");
			list = Helper.getPersistService().query(sql, params);
		} else {
			list = Helper.getPersistService().query(sql);
		}
		return list;
	}

	private String getSql(String folderOids, String file, List<Map<String, Object>> stList,
			List<Map<String, String>> selList, List<Map<String, Object>> optList) {
		String sql = "";
		for (Map<String, Object> map : stList) {
			String id = map.get("id").toString();
			String name = map.get("name").toString();
			String[] idValue = (String[]) map.get(id + "_value");
			for (String value : idValue) {
				sql += "select {0} from {1} where {2} group by {3} union all ";
				//取得where条件sql
				String whereSql = getWhereSql(folderOids, file, selList);
				//取得要查询字段的sql
				String selSql = getSelSql(stList, optList);
				sql = sql.replace("{0}", "'" + name + "-' || " + id + " as name " + selSql);
				sql = sql.replace("{1}", "V_ADM_STATISTICS");
				sql = sql.replace("{2}", whereSql);
				sql = sql.replace("{3}", id + " having " + id + " in ('" + value + "')");
			}
		}
		sql = sql.substring(0, sql.length() - 10);
		return sql;
	}

	private String getWhereSql(String folderOids, String file,
			List<Map<String, String>> selList) {
		param = "";
		String sql = "";
		String oids = getOids(folderOids);
		oids = oids.substring(0, oids.length() - 1);
		StringBuffer selSbf = new StringBuffer();
		selSbf.append("folderClassId || ':' || folderId in(" + oids + ")");
		if (!selList.isEmpty()) {
			selSbf.append(" and ");
			int i = 0;
			for (Map<String, String> map : selList) {
				i = i + 1;
				String id = map.get("id");
				String selValue = map.get("sel_" + id);
				String andOr = map.get("andOr");
				String sign = "";
				if ("0".equals(selValue)) {
					sign = " = ";
				} else if ("1".equals(selValue)) {
					sign = " like ";
				} else if ("2".equals(selValue)) {
					sign = " <= ";
				} else if ("3".equals(selValue)) {
					sign = " >= ";
				}
				if ("0".equals(andOr)) {
					andOr = " and ";
				} else {
					andOr = " or ";
				}
				if ("1".equals(selValue)) {
					if (i == selList.size()) {
						selSbf.append(id + sign + "'%" + map.get("context") + "%'");
					} else {
						selSbf.append(id + sign + "'%" + map.get("context") + "%'" + andOr);
					}
				} else {

					if (i == selList.size()) {
						if (map.get("context") == "" || map.get("context") == null) {
							selSbf.append(id + sign + "'" + map.get("context") + "'");
						} else {
							selSbf.append(id + sign + "?");
							param += map.get("context") + ",";
						}
					} else {
						if (map.get("context") == "" || map.get("context") == null) {
							selSbf.append(id + sign + "'" + map.get("context") + "'" + andOr);
						} else {
							selSbf.append(id + sign + "?" + andOr);
							param += map.get("context") + ",";
						}

					}
				}
			}
			//sql = selSbf.toString();
		}
		if ("0".equals(file)) {
			selSbf.append(" and fileId != 'null' and fileClassId != 'null'");
		} else if ("1".equals(file)) {
			selSbf.append(" and fileId is null and fileClassId is null");
		}
		sql = selSbf.toString();
		return sql;
	}

	private String getSelSql(List<Map<String, Object>> stList, List<Map<String, Object>> optList) {
		//统计字段
		StringBuffer sbf = new StringBuffer();
		if (!stList.isEmpty()) {
			sbf.append(",");
		}
		//统计条件
		for (Map<String, Object> map : optList) {
			String id = (String) map.get("id");
			String[] opts = (String[]) map.get("opt");
			for (String opt : opts) {
				String optValue = "count";
				if ("1".equals(opt)) {
					optValue = "sum";
					sbf.append(optValue + "(" + id + "),");
				} else {
					sbf.append(optValue + "(" + id + "),");
				}
			}
		}
		String sql = sbf.toString();
		sql = sql.substring(0, sql.length() - 1);
		return sql;
	}

	public HSSFWorkbook getExcelObject(List<Map<String, Object>> listname, List<Map<String, Object>> listStatistic) {
		List columns = new ArrayList();
		List listopt = new ArrayList();
		List listid = new ArrayList();
		columns.add("统计字段");
		for (int i = 0; i < listname.size(); i++) {
			String[] strname = (String[]) listname.get(i).get("name");
			String[] stropt = (String[]) listname.get(i).get("opt");
			String id = (String) listname.get(i).get("id");
			String idUpper = id.toUpperCase();

			if (strname.length == 2) {
				for (int p = 0; p < 2; p++) {
					listid.add(idUpper);
				}
			} else {
				listid.add(idUpper);
			}
			for (int c = 0; c < strname.length; c++) {
				if (!strname[c].equals("0") || !strname[c].equals("1")) {
					columns.add(strname[c]);
				}
			}
			for (int d = 0; d < stropt.length; d++) {
				listopt.add(stropt[d]);
			}
		}
		int num = 0;
		try {
			ExcelUtil ew = new ExcelUtil();

			String sheetName = "统计查询";

			ew.init(sheetName);
			// 创建单元格样式
			HSSFCellStyle headStyle = ew.getCellStyle((short) 200, true, false, true);
			HSSFCellStyle detailStyle = ew.getCellStyle((short) 200, true, false, false);

			int columnSize = columns.size();
			int rowSize = listStatistic.size() - 1;
			ew.createRow(rowSize + 1, columnSize + 1);
			ew.setRowHeight(1, (short) 300);

			ew.setCell(1, 1, "No.", headStyle);

			ew.setColumnWidth(1, 1500);
			ew.setColumnWidth(2, 3700);
			for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
				ew.setCell(1, columnIndex + 2, columns.get(columnIndex), headStyle);
				ew.setColumnWidth(columnIndex + 2, 3700);
			}

			for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
				Object[] arrStr = new Object[columnSize];
				ew.setCell(rowIndex + 2, 1, rowIndex + 1, detailStyle);
				ew.setCell(rowIndex + 2, 2, listStatistic.get(rowIndex).get("NAME"), detailStyle);
				arrStr[rowIndex] = StringUtil.getString(listStatistic.get(rowIndex).get("NAME"));
				for (int columnIndex = 0; columnIndex < columnSize - 1; columnIndex++) {
					if (listopt.get(columnIndex).equals("0")) {
						num = Integer.parseInt(listStatistic.get(rowIndex)
								.get("COUNT(" + listid.get(columnIndex) + ")") + "");
					} else {
						num = Integer.parseInt(listStatistic.get(rowIndex).get("SUM(" + listid.get(columnIndex) + ")")
								+ "");
					}
					//arrStr[columnIndex+1] = StringUtil.getString(num);
					ew.setCell(rowIndex + 2, columnIndex + 3, num, detailStyle);
				}
				//排序,获取长度最大的值
				String maxLength = StringUtil.maxValue(arrStr);
				// 根据长度最大字符串设置单元格拉伸高度
				ew.setCellHeightStyle(rowIndex + 2, 2, maxLength, 7);
			}
			ew.setColumnWidth(2, 5000);
			// 设置打印版式
			ew.setPrintStyle(true);
			ew.close();

			return ew.getCurrentWorkbook();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//递归累加文件夹的oid

	public String getOids(String folderOids) {
		String oids = "";
		String oidss = "";
		oids = "'" + folderOids + "',";
		Folder folder = (Folder) Helper.getPersistService().getObject(folderOids);
		List<Folder> subFolders = Helper.getFolderService().getChildFolders(folder);
		for (int j = 0; j < subFolders.size(); j++) {
			Folder folders = subFolders.get(j);
			String id = folders.getClassId() + ":" + folders.getInnerId();
			oidss += this.getOids(id);
		}
		oids += oidss;
		return oids;
	}
}
