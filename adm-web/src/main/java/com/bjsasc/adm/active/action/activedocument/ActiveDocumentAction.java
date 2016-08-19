package com.bjsasc.adm.active.action.activedocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.adm.active.action.AbstractAction;
import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.model.activedocument.ActiveDocument;
import com.bjsasc.adm.active.model.activedocument.ActiveDocumentMaster;
import com.bjsasc.adm.active.service.activedocumentservice.ActiveDocumentService;
import com.bjsasc.adm.common.ConstUtil;
import com.bjsasc.adm.common.EditMultiUtil;
import com.bjsasc.platform.objectmodel.managed.external.data.ClassAttrInfoDef;
import com.bjsasc.platform.objectmodel.managed.external.service.ModelInfoService;
import com.bjsasc.platform.objectmodel.managed.external.util.ModelInfoUtil;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.grid.data.GridDataAccessUtil;
import com.bjsasc.plm.grid.data.GridDataFilterUtil;
import com.bjsasc.plm.grid.data.GridDataMarkUtil;
import com.bjsasc.plm.grid.data.GridDataSearchUtil;
import com.bjsasc.plm.grid.data.GridDataSortUtil;
import com.bjsasc.plm.grid.data.GridDataStyleUtil;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.bjsasc.plm.grid.spot.Spot;
import com.bjsasc.plm.grid.spot.SpotUtil;
import com.bjsasc.plm.type.TypeService;
import com.bjsasc.plm.type.attr.Attr;
import com.bjsasc.plm.type.type.Type;
import com.bjsasc.plm.type.type.TypeUtil;
import com.cascc.avidm.util.SplitString;

/**
 * 现行文件Action实现类。
 * 
 * @author yanjia 2013-5-15
 */
@SuppressWarnings("deprecation")
public class ActiveDocumentAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = -4131327954360034679L;

	/** 现行文件服务 */
	ActiveDocumentService service = AdmHelper.getActiveDocumentService();

	private static final Logger LOG = Logger.getLogger(ActiveDocumentAction.class);
	Map<String, String> map = new HashMap<String, String>();

	/**
	 * 取得现行文件。
	 * 
	 * @return JSON对象
	 */
	public String getAllActiveDocument() {
		try {
			List<ActiveDocument> list = service.getAllActiveDocument();
			logPrint(list);
			// 输出结果
			GridDataUtil.prepareRowObjects(list,
					ConstUtil.SPOT_LISTACTIVEDOCUMENT);
		} catch (Exception ex) {
			error(ex);
		}
		return INITPAGE;
	}

	/**
	 * 取得现行文件主对象。
	 * 
	 * @return JSON对象
	 */
	public String getAllActiveDocumentMaster() {
		try {
			List<ActiveDocumentMaster> list = service
					.getAllActiveDocumentMaster();
			logPrint(list);
			// 输出结果
			GridDataUtil.prepareRowObjects(list,
					ConstUtil.SPOT_LISTACTIVEDOCUMENTMASTER);
		} catch (Exception ex) {
			error(ex);
		}
		return INITPAGE;
	}

	/**
	 * 取得现行文件主对象。
	 * 
	 * @return JSON对象
	 */
	public String getActiveDocumentMasterByOid() {
		try {
			// 编号
			String number = request.getParameter("NUMBER");
			List<ActiveDocument> list = service
					.getActiveDocumentByNumber(number);
			logPrint(list);
			// 输出结果
			GridDataUtil.prepareRowObjects(list,
					ConstUtil.SPOT_LISTVERSIONBROWSE);
			success();
			result.put("flag", String.valueOf(getDataSize(list)));
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 创建现行文件。
	 * 
	 * @return JSON对象
	 */
	public String createActiveDocument() {
		try {
			String[] keys = {
					// 类型
					"CLASSIDS",
					// 编号
					"NUMBER",
					// 名称
					"NAME",
					// 来源
					"DATASOURCE",
					// 代号
					"ACTIVECODE",
					// 页数
					"PAGES",
					// 份数
					"COUNT",
					// 作者
					"AUTHORNAME",
					// 作者单位
					"AUTHORUNIT",
					// 创建时间
					"AUTHORTIME",
					// 备注
					"NOTE",
					// 上下文
					"CONTEXT",
					// 密集
					"SECLEVEL",
					// 文件夹OID
					"folderOid",
					// 版本Flag
					"VERSIONFlAG",
					// 版本
					"VERSION" };
			
			Map<String, String> paramMap = getParams(keys);

			String oid = "";
			String name = paramMap.get("NAME");
			int q = 1;
			if (name.startsWith("test") && name.indexOf("_") > 0) {
				try {
					String qStr = name.substring(name.indexOf("_") + 1);
					q = Integer.parseInt(qStr);

					for (int i = 0; i < q; i++) {
						paramMap.put("NUMBER", name + i);
						paramMap.put("NAME", name + i);
						// 现行文件保存
						oid = service.createActiveDocument(paramMap);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {

				// 现行文件保存
				oid = service.createActiveDocument(paramMap);

				// 设置版本扩展属性
				setExtAttr(oid);
			}
			result.put("innerId", Helper.getInnerId(oid));
			result.put("classId", Helper.getClassId(oid));
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 删除现行文件。
	 * 
	 * @return JSON对象
	 */
	public String deleteActiveDocument() {
		try {
			// 现行文件OIDS
			String oids = request.getParameter(KeyS.OIDS);

			// 删除现行文件
			service.deleteActiveDocument(oids);

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 更新现行文件。
	 * 
	 * @return JSON对象
	 */
	public String updataActiveDocument() {
		try {

			String[] keys = {
					// 现行文件OID
					"OID",
					// 类型
					"CLASSIDS",
					// 编号
					"NUMBER",
					// 名称
					"NAME",
					// 来源
					"DATASOURCE",
					// 代号
					"ACTIVECODE",
					// 页数
					"PAGES",
					// 份数
					"COUNT",
					// 作者
					"AUTHORNAME",
					// 作者单位
					"AUTHORUNIT",
					// 创建时间
					"AUTHORTIME",
					// 备注
					"NOTE",
					// 上下文
					"CONTEXT",
					// 密集
					"SECLEVEL",
					// 文件夹OID
					"folderOid",
					// 版本Flag
					"VERSIONFlAG",
					// 版本
					"VERSION" };

			Map<String, String> paramMap = getParams(keys);

			// 更新现行文件
			service.updataActiveDocument(paramMap);

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 根据OIDS取得现行文件
	 * 
	 * @return JSON对象
	 */
	public void getActiveDoucmentByOIDS() {
		String oids = request.getParameter(KeyS.OIDS);
		List<String> oidList = SplitString.string2List(oids, ",");
		List<ActiveDocument> list = new ArrayList<ActiveDocument>();
		for (String oid : oidList) {
			ActiveDocument adObject = (ActiveDocument) Helper
					.getPersistService().getObject(oid);
			if (adObject == null) {
				continue;
			}
			list.add(adObject);
		}
		logPrint(list);
		// 输出结果
		GridDataUtil.prepareRowObjects(list, ConstUtil.SPOT_LISTACTIVEDOCUMENT);
	}

	/**
	 * 批量更新现行文件。
	 * 
	 * @return JSON对象
	 */
	public String updataMultiActiveDocument() {
		try {
			String oids = request.getParameter(KeyS.OIDS);
			String conditions = request.getParameter("CONDITIONS");

			List<String> oidList = SplitString.string2List(oids, ",");
			List<Persistable> list = new ArrayList<Persistable>();
			for (String oid : oidList) {
				Persistable obj = Helper.getPersistService().getObject(oid);
				list.add(obj);
			}
			EditMultiUtil editMulti = new EditMultiUtil();
			editMulti.updataEditMulti(list, conditions);

			// 格式化显示数据
			TypeService typeManager = Helper.getTypeManager();
			for (String oid : oidList) {
				ActiveDocument obj = (ActiveDocument) Helper
						.getPersistService().getObject(oid);
				if (obj == null) {
					continue;
				}
				listMap.add(typeManager.format(obj));
			}
			LOG.debug("取得现行文件数据: " + getDataSize() + " 条");
			// 输出结果
			GridDataUtil.prepareRowObjectMaps(listMap,
					ConstUtil.SPOT_LISTACTIVEDOCUMENT);
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	public String getParam() {
		try {
			request.setAttribute("typeIdsStr", packageTypeId());
			request.setAttribute("types", packageTypes());
			request.setAttribute("oids", (String) request.getParameter(KeyS.OIDS));
			getActiveDoucmentByOIDS();
		} catch (Exception ex) {
			error(ex);
		}
		return "editMulti";
	}

	private String packageTypeId() {
		String spot = (String) request.getParameter("spot");
		// 场景定义的硬模型id
		List<String> spotTypeIds = new ArrayList<String>();
		for (Type type : Helper.getGridManager().getSpotTypes(spot)) {
			spotTypeIds.add(type.getId());
		}
		return SplitString.list2Str(spotTypeIds, ",");
	}

	private Map<String, Object> packageTypes() {
		Map<String, Object> typesMap = new HashMap<String, Object>();

		List<Type> types = TypeUtil.getFilterNullViewTypes(KeyS.VIEW_SIMPLE);
		for (Type type : types) {
			typesMap.put(type.getId(), packageType(type));
		}

		return typesMap;
	}

	private Map<String, Object> packageType(Type type) {
		Map<String, Object> typeMap = new HashMap<String, Object>();
		typeMap.put("TYPEID", type.getId());
		typeMap.put("TYPENAME", type.getName());
		typeMap.put("TYPEICON", type.getIcon());

		List<Attr> attrs = Helper.getTypeManager().getTypeAttrs(type.getId(),
				KeyS.VIEW_SIMPLE);

		List<String> attrIds = new ArrayList<String>();
		for (Attr attr : attrs) {
			attrIds.add(attr.getId());
		}
		typeMap.put("ATTRIDS", attrIds);

		typeMap.put("ATTRS", packageAttrs(attrs));

		return typeMap;
	}

	private Map<String, Object> packageAttrs(List<Attr> attrs) {
		Map<String, Object> attrsMap = new HashMap<String, Object>();

		for (Attr attr : attrs) {
			attrsMap.put(attr.getId(), packageAttr(attr));
		}

		return attrsMap;
	}

	private Map<String, Object> packageAttr(Attr attr) {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put("ATTRID", attr.getId());
		attrMap.put("ATTRNAME", attr.getName());		

		return attrMap;
	}

	/**
	 * 初始化页面
	 * 
	 * @return
	 */
	public String initActiveDocumentCondition() {
		initParam();
		try {
			// 输出结果
			GridDataUtil.prepareRowObjectMaps(listMap,
					ConstUtil.SPOT_LISTACTIVEDOCUMENTCONDITION);
		} catch (Exception ex) {
			error(ex);
		}
		return "initpageCondition";
	}

	/**
	 * 直接入库处理
	 * 
	 * @return
	 */
	public String putIntoStorage() {
		try {

			String oid = request.getParameter("OID");

			String flag = service.putIntoStorage(oid);

			if ("success".equals(flag)) {
				success();
			} else if("createError".equals(flag)){
				result.put("success", "message");
				result.put("message", ConstUtil.CREATE_DISTRIBUTEORDER_ERROE);
			} else if("putError".equals(flag)){
				result.put("success", "message");
				result.put("message", ConstUtil.PUT_STORAGE_ERROE);
			} else {
				result.put("success", "message");
				result.put("message", ConstUtil.PUTSTORAGE_OPERATE_ERROE);
			} 
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	public String getAllactionDocumentObject() {
		initParam();
		session.setAttribute("ADM_ACTIVEDOCUMENT_INSIDE", map);
		try {
			List<ActiveDocument> listAdmDocument = service
					.getAllActiveDocumentquery(map);

			// log.debug("取得现行文件 " + getDataSize(listAdmDocument) + " 条");

			// GridDataUtil.prepareRowObjects(listAdmDocument,
			// ConstUtil.SPOT_LISTACTIVEDOCUMENTCONDITION);
			// List<ActiveDocument> listAdmDocument =
			// service.getAllActiveDocument();
			LOG.debug("取得现行文件数据: " + getDataSize(listAdmDocument) + " 条");
			TypeService typeManager = Helper.getTypeManager();
			for (ActiveDocument target : listAdmDocument) {
				listMap.add(typeManager.format(target));
			}
			GridDataUtil.prepareRowObjectMaps(listMap,
					ConstUtil.SPOT_LISTACTIVEDOCUMENTCONDITION);

			result.put("success", "true");
			result.put("flag", listAdmDocument.size() + "");

			// success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	public void dataExport() {
		String filename = "现行数据统计";
		String changeClassId = request.getParameter("changeClassId");
		String spot = "";
		String spotInstance = "";
		if ("doc".equals(changeClassId)) {
			spot = "QUERYACTIVEDOCUMENTS";
			spotInstance = "QUERYACTIVEDOCUMENTS";
		} else if ("set".equals(changeClassId)) {
			spot = "QUERYACTIVESET";
			spotInstance = "QUERYACTIVESET";
		} else {
			spot = "QUERYACTIVEORDER";
			spotInstance = "QUERYACTIVEORDER";
		}
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> objectMaps = GridDataUtil
				.getRowObjectMaps(spot);

		result = GridDataFilterUtil.filterMaps(request, spot, spotInstance,
				objectMaps);

		Spot spt = SpotUtil.getSpot(spot);

		// 读取权限过滤
		result = GridDataAccessUtil.accessFilter(result);

		// 关键字过滤
		if (spt.hasSearch()) {
			result = GridDataSearchUtil.search(request, spot, spotInstance,
					result);
		}

		// 最近创建、最近更新标记
		result = GridDataMarkUtil.markByTime(spot, spotInstance,result);

		// 排序
		if (spt.hasSort()) {
			result = GridDataSortUtil.sort(request, spot, spotInstance, result);
		}

		// 关键字添加特殊标记
		GridDataStyleUtil.keyWordStyling(result);

		try {
			// 取得对象。
			HSSFWorkbook wBook = service.getExcelObject(result, changeClassId);

			// Excel文件输出。
			exportExcel(wBook, filename);
		} catch (Exception ex) {
			LOG.debug("Excel导出异常", ex);
			throw new RuntimeException(ex);
		}

	}

	private void initParam() {
		map.put("qCreateDate_sel", getParameter("qCreateDate_sel"));
		map.put("activeDocumentName", getParameter("activeDocumentName"));
		map.put("activeDocumentId", getParameter("activeDocumentId"));
		map.put("queryName", getParameter("queryName"));
		map.put("queryNumber", request.getParameter("queryNumber"));
		map.put("queryCreateDateFrom",
				request.getParameter("queryCreateDateFrom"));
		map.put("queryCreateDateTo", request.getParameter("queryCreateDateTo"));
		map.put("creator", getParameter("creator"));
		map.put("creatorName", request.getParameter("creatorName"));

	}

	private void setExtAttr(String oid) {
		ActiveDocument obj = (ActiveDocument) Helper.getPersistService()
				.getObject(oid);
		// 设置版本扩展属性
		ModelInfoService moExternalService = ModelInfoUtil.getService();
		List<ClassAttrInfoDef> classDefList = moExternalService
				.getExtClassAttrInfos(obj.getClassId());
		for (ClassAttrInfoDef bean : classDefList) {
			String attrId = bean.getAttrId();
			String attrValue = request.getParameter(attrId);
			if (attrValue != null) {
				obj.setExtAttr(attrId, attrValue);
			}
		}
		Helper.getPersistService().update(obj);
	}
	
	private void logPrint(List<?> list) {
		LOG.debug("取得现行文件数据: " + getDataSize(list) + " 条");		
	}

}
