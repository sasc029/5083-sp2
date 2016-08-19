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
 * �����ļ�Actionʵ���ࡣ
 * 
 * @author yanjia 2013-5-15
 */
@SuppressWarnings("deprecation")
public class ActiveDocumentAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = -4131327954360034679L;

	/** �����ļ����� */
	ActiveDocumentService service = AdmHelper.getActiveDocumentService();

	private static final Logger LOG = Logger.getLogger(ActiveDocumentAction.class);
	Map<String, String> map = new HashMap<String, String>();

	/**
	 * ȡ�������ļ���
	 * 
	 * @return JSON����
	 */
	public String getAllActiveDocument() {
		try {
			List<ActiveDocument> list = service.getAllActiveDocument();
			logPrint(list);
			// ������
			GridDataUtil.prepareRowObjects(list,
					ConstUtil.SPOT_LISTACTIVEDOCUMENT);
		} catch (Exception ex) {
			error(ex);
		}
		return INITPAGE;
	}

	/**
	 * ȡ�������ļ�������
	 * 
	 * @return JSON����
	 */
	public String getAllActiveDocumentMaster() {
		try {
			List<ActiveDocumentMaster> list = service
					.getAllActiveDocumentMaster();
			logPrint(list);
			// ������
			GridDataUtil.prepareRowObjects(list,
					ConstUtil.SPOT_LISTACTIVEDOCUMENTMASTER);
		} catch (Exception ex) {
			error(ex);
		}
		return INITPAGE;
	}

	/**
	 * ȡ�������ļ�������
	 * 
	 * @return JSON����
	 */
	public String getActiveDocumentMasterByOid() {
		try {
			// ���
			String number = request.getParameter("NUMBER");
			List<ActiveDocument> list = service
					.getActiveDocumentByNumber(number);
			logPrint(list);
			// ������
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
	 * ���������ļ���
	 * 
	 * @return JSON����
	 */
	public String createActiveDocument() {
		try {
			String[] keys = {
					// ����
					"CLASSIDS",
					// ���
					"NUMBER",
					// ����
					"NAME",
					// ��Դ
					"DATASOURCE",
					// ����
					"ACTIVECODE",
					// ҳ��
					"PAGES",
					// ����
					"COUNT",
					// ����
					"AUTHORNAME",
					// ���ߵ�λ
					"AUTHORUNIT",
					// ����ʱ��
					"AUTHORTIME",
					// ��ע
					"NOTE",
					// ������
					"CONTEXT",
					// �ܼ�
					"SECLEVEL",
					// �ļ���OID
					"folderOid",
					// �汾Flag
					"VERSIONFlAG",
					// �汾
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
						// �����ļ�����
						oid = service.createActiveDocument(paramMap);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {

				// �����ļ�����
				oid = service.createActiveDocument(paramMap);

				// ���ð汾��չ����
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
	 * ɾ�������ļ���
	 * 
	 * @return JSON����
	 */
	public String deleteActiveDocument() {
		try {
			// �����ļ�OIDS
			String oids = request.getParameter(KeyS.OIDS);

			// ɾ�������ļ�
			service.deleteActiveDocument(oids);

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * ���������ļ���
	 * 
	 * @return JSON����
	 */
	public String updataActiveDocument() {
		try {

			String[] keys = {
					// �����ļ�OID
					"OID",
					// ����
					"CLASSIDS",
					// ���
					"NUMBER",
					// ����
					"NAME",
					// ��Դ
					"DATASOURCE",
					// ����
					"ACTIVECODE",
					// ҳ��
					"PAGES",
					// ����
					"COUNT",
					// ����
					"AUTHORNAME",
					// ���ߵ�λ
					"AUTHORUNIT",
					// ����ʱ��
					"AUTHORTIME",
					// ��ע
					"NOTE",
					// ������
					"CONTEXT",
					// �ܼ�
					"SECLEVEL",
					// �ļ���OID
					"folderOid",
					// �汾Flag
					"VERSIONFlAG",
					// �汾
					"VERSION" };

			Map<String, String> paramMap = getParams(keys);

			// ���������ļ�
			service.updataActiveDocument(paramMap);

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * ����OIDSȡ�������ļ�
	 * 
	 * @return JSON����
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
		// ������
		GridDataUtil.prepareRowObjects(list, ConstUtil.SPOT_LISTACTIVEDOCUMENT);
	}

	/**
	 * �������������ļ���
	 * 
	 * @return JSON����
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

			// ��ʽ����ʾ����
			TypeService typeManager = Helper.getTypeManager();
			for (String oid : oidList) {
				ActiveDocument obj = (ActiveDocument) Helper
						.getPersistService().getObject(oid);
				if (obj == null) {
					continue;
				}
				listMap.add(typeManager.format(obj));
			}
			LOG.debug("ȡ�������ļ�����: " + getDataSize() + " ��");
			// ������
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
		// ���������Ӳģ��id
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
	 * ��ʼ��ҳ��
	 * 
	 * @return
	 */
	public String initActiveDocumentCondition() {
		initParam();
		try {
			// ������
			GridDataUtil.prepareRowObjectMaps(listMap,
					ConstUtil.SPOT_LISTACTIVEDOCUMENTCONDITION);
		} catch (Exception ex) {
			error(ex);
		}
		return "initpageCondition";
	}

	/**
	 * ֱ����⴦��
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

			// log.debug("ȡ�������ļ� " + getDataSize(listAdmDocument) + " ��");

			// GridDataUtil.prepareRowObjects(listAdmDocument,
			// ConstUtil.SPOT_LISTACTIVEDOCUMENTCONDITION);
			// List<ActiveDocument> listAdmDocument =
			// service.getAllActiveDocument();
			LOG.debug("ȡ�������ļ�����: " + getDataSize(listAdmDocument) + " ��");
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
		String filename = "��������ͳ��";
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

		// ��ȡȨ�޹���
		result = GridDataAccessUtil.accessFilter(result);

		// �ؼ��ֹ���
		if (spt.hasSearch()) {
			result = GridDataSearchUtil.search(request, spot, spotInstance,
					result);
		}

		// ���������������±��
		result = GridDataMarkUtil.markByTime(spot, spotInstance,result);

		// ����
		if (spt.hasSort()) {
			result = GridDataSortUtil.sort(request, spot, spotInstance, result);
		}

		// �ؼ������������
		GridDataStyleUtil.keyWordStyling(result);

		try {
			// ȡ�ö���
			HSSFWorkbook wBook = service.getExcelObject(result, changeClassId);

			// Excel�ļ������
			exportExcel(wBook, filename);
		} catch (Exception ex) {
			LOG.debug("Excel�����쳣", ex);
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
		// ���ð汾��չ����
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
		LOG.debug("ȡ�������ļ�����: " + getDataSize(list) + " ��");		
	}

}
