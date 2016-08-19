package com.bjsasc.adm.active.service.activedocumentservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.model.ActiveOrdered;
import com.bjsasc.adm.active.model.activedocument.ActiveDocument;
import com.bjsasc.adm.active.model.activedocument.ActiveDocumentMaster;
import com.bjsasc.adm.active.model.activeorder.ActiveOrder;
import com.bjsasc.adm.active.model.activeorderlink.ActiveOrderLink;
import com.bjsasc.adm.active.model.activeset.ActiveSet;
import com.bjsasc.adm.active.service.PutIntoStorageService;
import com.bjsasc.adm.active.service.activelifecycle.ActiveLifecycleService;
import com.bjsasc.adm.active.service.activeoptvalidation.ActiveOptValidationService;
import com.bjsasc.adm.active.service.activeorderservice.ActiveOrderService;
import com.bjsasc.adm.common.AdmLifeCycleConstUtil;
import com.bjsasc.platform.objectmodel.business.lifeCycle.State;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.platform.objectmodel.business.version.Mastered;
import com.bjsasc.platform.objectmodel.business.version.VersionControlUtil;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.ContextHelper;
import com.bjsasc.plm.core.folder.Folder;
import com.bjsasc.plm.core.folder.FolderHelper;
import com.bjsasc.plm.core.folder.Foldered;
import com.bjsasc.plm.core.lifecycle.LifeCycleHelper;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.lifecycle.LifeCycleService;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.system.access.AccessControlHelper;
import com.bjsasc.plm.core.system.access.operate.Operate;
import com.bjsasc.plm.core.system.principal.Principal;
import com.bjsasc.plm.core.system.principal.UserHelper;
import com.bjsasc.plm.core.system.securitylevel.SecurityLevel;
import com.bjsasc.plm.core.util.DateTimeUtil;
import com.bjsasc.plm.core.util.ExcelUtil;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.grid.GridColumn;
import com.bjsasc.plm.grid.GridUtil;
import com.bjsasc.plm.grid.spot.Column;
import com.bjsasc.plm.grid.spot.Spot;
import com.bjsasc.plm.grid.spot.SpotUtil;
import com.cascc.avidm.util.SplitString;

/**
 * �����ļ�����ʵ���ࡣ
 * 
 * @author yanjia 2013-5-13
 */
@SuppressWarnings("deprecation")
public class ActiveDocumentServiceImpl implements ActiveDocumentService {

	private final ActiveOrderService orderService = AdmHelper
			.getActiveOrderService();

	// private final ActiveSetService setService =
	// AdmHelper.getActiveSetService();

	public ActiveDocument newActiveDocument(String classId) {
		ActiveDocument document = newActiveDocument(classId,
				VersionControlUtil.CHECKOUTSTATE_IN);
		return document;
	}

	public ActiveDocument newActiveDocument(String classId, String checkoutState) {

		// ����master����
		ActiveDocumentMaster master = (ActiveDocumentMaster) PersistUtil
				.createObject(ActiveDocumentMaster.CLASSID);
		// �����汾����
		ActiveDocument document = (ActiveDocument) VersionControlUtil
				.createVersion(classId, master, checkoutState);
		return document;
	}

	@Override
	public List<ActiveDocument> getAllActiveDocument() {
		String sql = "SELECT * FROM ADM_ACTIVEDOCUMENT WHERE CLASSID || ':' || INNERID NOT IN ( SELECT (ITEMCLASSID || ':' || ITEMID) AS OID FROM ADM_ACTIVERECYCLE )";
		List<ActiveDocument> resultList = (List<ActiveDocument>) Helper
				.getPersistService().query(sql, ActiveDocument.class);
		return resultList;
	}

	@Override
	public String createActiveDocumentMaster(String number, String name,
			String secLevel) {
		ActiveDocumentMaster masterObj = (ActiveDocumentMaster) PersistUtil
				.createObject(ActiveDocumentMaster.CLASSID);
		// ���
		masterObj.setNumber(number);
		// ����
		masterObj.setName(name);
		if (secLevel != null && !secLevel.isEmpty()) {// ���ܼ���Ϣ
			Persistable secPt = PersistHelper.getService().getObject(secLevel);
			if (secPt instanceof SecurityLevel && secPt != null) {
				SecurityLevel secLevelObj = (SecurityLevel) secPt;
				masterObj.setSecurityLevelInfo(secLevelObj
						.buildSecurityLevelInfo());
			}
		}
		Helper.getPersistService().save(masterObj);
		return Helper.getOid(masterObj);
	}

	@Override
	public String createActiveDocument(Map<String, String> paramMap) {

		// ���
		String number = paramMap.get("NUMBER");
		// ����
		String name = paramMap.get("NAME");
		// ��Դ
		String dataSource = paramMap.get("DATASOURCE");
		// ����
		String activeCode = paramMap.get("ACTIVECODE");
		// ��ע
		String note = paramMap.get("NOTE");
		// ����
		String classID = paramMap.get("CLASSIDS");
		// ������
		// String contextOid = paramMap.get("CONTEXT");
		// �ܼ�
		String secLevel = paramMap.get("SECLEVEL");
		// ҳ��
		String strPages = paramMap.get("PAGES");
		int pages = StringUtil.str2int(strPages);
		// ����
		String strCount = paramMap.get("COUNT");
		int count = StringUtil.str2int(strCount);
		// ����
		String authorName = paramMap.get("AUTHORNAME");
		// ���ߵ�λ
		String authorUnit = paramMap.get("AUTHORUNIT");
		// ����ʱ��
		String authorTime = paramMap.get("AUTHORTIME");
		// �ļ���OID
		String folderOid = paramMap.get("folderOid");
		// �汾Flag
		String versionFlag = paramMap.get("VERSIONFlAG");
		// �汾
		String version = paramMap.get("VERSION");

		String masterOid = "";
		if ("0".equals(versionFlag)) {
			// �����ļ������󱣴�
			masterOid = createActiveDocumentMaster(number, name, secLevel);
		}

		// �����汾����
		ActiveDocument obj;
		ActiveDocument oldObj;
		if ("0".equals(versionFlag)) {
			obj = (ActiveDocument) VersionControlUtil.createVersion(
					ActiveDocument.CLASSID, (Mastered) Helper
							.getPersistService().getObject(masterOid));
		} else {
			oldObj = getActiveDocumentByNumber(number).get(0);
			obj = (ActiveDocument) VersionControlUtil.getService().newVersion(
					oldObj, version, "1");
		}

		// ��ע
		obj.setNote(note);
		// ��Դ
		obj.setDataSource(dataSource);
		// ����
		obj.setActiveCode(activeCode);
		// ҳ��
		obj.setPages(pages);
		// ����
		obj.setCount(count);
		// ����
		obj.setAuthorName(authorName);
		// ���ߵ�λ
		obj.setAuthorUnit(authorUnit);
		// ����ʱ��
		obj.setAuthorTime(DateTimeUtil.parseDateLong(authorTime,
				DateTimeUtil.DATE_YYYYMMDD));
		// ����
		obj.setClassId(classID);
		// ��������
		ActiveLifecycleService lifeService = AdmHelper
				.getActiveLifecycleService();
		lifeService.initLifecycle(obj);
		// �ļ�����Ϣ
		if (folderOid != null && folderOid.length() > 0) {
			Folder folder = (Folder) Helper.getPersistService().getObject(
					folderOid);

			// ������
			Context context = folder.getContextInfo().getContext();
			ContextHelper.getService().addToContext(obj, context);

			// ����Ϣ
			obj.setDomainInfo(folder.getDomainInfo());
			FolderHelper.getService().addToFolder(folder, (Foldered) obj);
		}

		// ���Ȩ��
		ActiveOptValidationService validationService = AdmHelper
				.getActiveOptValidationService();
		boolean flag = validationService.getCreateActiveDocumentValidation(
				Operate.CREATE, obj);
		if (flag == false) {
			AccessControlHelper.getService().checkEntityPermission(
					Operate.CREATE, obj);
		}

		if ("0".equals(versionFlag)) {
			Helper.getPersistService().save(obj);
		} else {
			Helper.getPersistService().update(obj);
		}

		/*
		 * //������ if(isLocked){ Helper.getLockService().lock(baseline); }else{
		 * Helper.getLockService().unlock(baseline); }
		 */
		return Helper.getOid(obj);
	}

	@Override
	public String deleteActiveDocument(String oids) {
		List<String> oidList = SplitString.string2List(oids, ",");
		for (String oid : oidList) {
			ActiveDocument object = (ActiveDocument) Helper.getPersistService()
					.getObject(oid);
			ActiveDocumentMaster masterObj = (ActiveDocumentMaster) object
					.getMaster();
			// ɾ�����LINK
			deleteLink(oid);
			// ɾ���ļ�����
			Folder folder = (Folder) object.getFolderInfo().getFolder();
			if (folder != null) {
				FolderHelper.getService().removeFromFolder(folder,
						(Foldered) object);

			}
			// ɾ��������-���û�������汾��ɾ��
			List<ActiveDocument> list = getActiveDocumentByNumber(masterObj
					.getNumber());
			if (list == null || list.size() <= 1) {
				Helper.getPersistService().delete(masterObj);
			}
			// ɾ�������ļ�����
			Helper.getPersistService().delete(object);
		}
		return null;
	}

	@Override
	public String putIntoStorage(String oid) {

		String flag = "fail";
		Persistable obj = Helper.getPersistService().getObject(oid);
		LifeCycleService service = LifeCycleHelper.getService();
		// // �޸����ж�����������
		// if (obj instanceof LifeCycleManaged) {
		// // ��������
		// ActiveLifecycleService lifeService =
		// AdmHelper.getActiveLifecycleService();
		// LifeCycleManaged lcObj = (LifeCycleManaged) obj;
		//
		// lifeService.updateLifeCycleByStateName(lcObj,
		// AdmLifeCycleConstUtil.LC_CONTROLLING.getName());
		// Helper.getPersistService().update(lcObj);
		// }
		if (obj instanceof ActiveSet) {
			ActiveSet activeSet = (ActiveSet) obj;
			if (AdmLifeCycleConstUtil.LC_PROVIDED.getName().equals(
					activeSet.getLifeCycleInfo().getStateName())) {
				flag = "putError";
				return flag;
			}
		}
		if (obj instanceof ActiveOrder) {
			ActiveOrder activeOrder = (ActiveOrder) obj;
			List<ActiveOrdered> resultList = AdmHelper.getActiveOrderService()
					.getAfterItems(activeOrder);// ȡ�øĺ����
			for (ActiveOrdered activeOrdered : resultList) {
				if (activeOrdered instanceof ActiveDocument) {
					ActiveDocument activeDoc = (ActiveDocument) activeOrdered;
					if (!AdmLifeCycleConstUtil.LC_CONTROLLING.getName().equals(
							activeDoc.getLifeCycleInfo().getStateName())) {
						State state = AdmLifeCycleConstUtil.LC_CONTROLLING;
						LifeCycleManaged lifeCycleManagedObj = (LifeCycleManaged) activeDoc;
						LifeCycleManaged lifeCycleManage = service
								.setLifeCycleState(lifeCycleManagedObj, state);
						if (lifeCycleManage == null) {
							flag = "fail";
							return flag;
						}
					}
				}
			}
		}

		PutIntoStorageService putStorageService = AdmHelper
				.getPutIntoStorageService();
		flag = putStorageService.putIntoStorage(oid);
		if ("success".equals(flag)) {
			State state = AdmLifeCycleConstUtil.LC_PROVIDED;
			LifeCycleManaged lifeCycleManagedObj = (LifeCycleManaged) obj;
			LifeCycleManaged lifeCycleManage = service.setLifeCycleState(
					lifeCycleManagedObj, state);
			if (lifeCycleManage == null) {
				flag = "fail";
			}
			if (obj instanceof ActiveOrder) {
				ActiveOrder activeOrder = (ActiveOrder) obj;
				List<ActiveOrdered> resultList = AdmHelper
						.getActiveOrderService().getAfterItems(activeOrder);// ȡ�øĺ����
				for (ActiveOrdered activeOrdered : resultList) {
					if (activeOrdered instanceof ActiveDocument) {
						ActiveDocument activeDoc = (ActiveDocument) activeOrdered;
						LifeCycleManaged lifeCycleManagedActiveDoc = (LifeCycleManaged) activeDoc;
						LifeCycleManaged lifeCycleManageActiveDoc = service
								.setLifeCycleState(lifeCycleManagedActiveDoc,
										state);
						if (lifeCycleManageActiveDoc == null) {
							flag = "fail";
							return flag;
						}
					}
				}
			}
		}
		return flag;
	}

	@Override
	public String updataActiveDocument(Map<String, String> paramMap) {

		// �����ļ�OID
		String oid = paramMap.get("OID");
		// ��Դ
		String dataSource = paramMap.get("DATASOURCE");
		// ����
		String activeCode = paramMap.get("ACTIVECODE");

		// ҳ��
		String strPages = paramMap.get("PAGES");
		int pages = StringUtil.str2int(strPages);
		// ����
		String strCount = paramMap.get("COUNT");
		int count = StringUtil.str2int(strCount);
		// ����
		String authorName = paramMap.get("AUTHORNAME");
		// ���ߵ�λ
		String authorUnit = paramMap.get("AUTHORUNIT");
		// ����ʱ��
		String authorTime = paramMap.get("AUTHORTIME");
		// ��ע
		String note = paramMap.get("NOTE");

		ActiveDocument obj = (ActiveDocument) Helper.getPersistService()
				.getObject(oid);
		ActiveDocumentMaster masterObj = (ActiveDocumentMaster) obj.getMaster();

		// ��Դ
		obj.setDataSource(dataSource);
		// ����
		obj.setActiveCode(activeCode);
		// ҳ��
		obj.setPages(pages);
		// ����
		obj.setCount(count);
		// ����
		obj.setAuthorName(authorName);
		// ���ߵ�λ
		obj.setAuthorUnit(authorUnit);
		// ����ʱ��
		obj.setAuthorTime(DateTimeUtil.parseDateLong(authorTime,
				DateTimeUtil.DATE_YYYYMMDD));
		// ��ע
		obj.setNote(note);
		updataActiveDocumentMaster(masterObj);
		updataActiveDocument(obj);
		return null;
	}

	@Override
	public String updataActiveDocument(ActiveDocument obj) {
		Helper.getPersistService().update(obj);
		return null;
	}

	@Override
	public String updataActiveDocumentMaster(ActiveDocumentMaster masterObj) {
		Helper.getPersistService().update(masterObj);
		return null;
	}

	@Override
	public List<ActiveDocumentMaster> getAllActiveDocumentMaster() {
		String sql = "SELECT * FROM ADM_ACTIVEDOCUMENTMASTER";
		List<ActiveDocumentMaster> resultList = (List<ActiveDocumentMaster>) Helper
				.getPersistService().query(sql, ActiveDocumentMaster.class);
		return resultList;
	}

	@Override
	public List<ActiveDocument> getAllActiveDocumentquery(
			Map<String, String> map) {
		String mainSql = getSql(map);
		List<ActiveDocument> admdocumentList = (List<ActiveDocument>) Helper
				.getPersistService().query(mainSql, ActiveDocument.class);
		return admdocumentList;

	}

	private String getSql(Map<String, String> map) {
		String activeDocumentId = map.get("activeDocumentId");
		String queryCreateDateFrom = map.get("queryCreateDateFrom");
		String queryCreateDateTo = map.get("queryCreateDateTo");
		String creatorID = map.get("creator");
		String queryName = map.get("queryName");
		String queryNumber = map.get("queryNumber");
		long createDateFrom = 0;
		long createDateTo = 0;
		String activeInnerId = Helper.getInnerId(activeDocumentId);
		createDateFrom = DateTimeUtil.parseDateLong(queryCreateDateFrom,
				DateTimeUtil.DATE_YYYYMMDD);
		createDateTo = DateTimeUtil.parseDateLong(queryCreateDateTo,
				DateTimeUtil.DATE_YYYYMMDD) + 86399000L;

		StringBuffer sbSql = new StringBuffer();
		Principal creator = null;
		sbSql.append(" select a.* from adm_activedocument a,adm_activedocumentmaster b where a.masterid=b.innerid and a.masterclassid=b.classid");
		if (activeInnerId != null && activeInnerId.length() > 0) {
			sbSql.append(" and a.folderid = '" + activeInnerId + "'");
		}
		if (queryName != null && queryName.length() > 0) {
			sbSql.append(" and b.name like '%" + queryName + "%'");
		}
		if (queryNumber != null && queryNumber.length() > 0) {
			sbSql.append(" and b.id like '%" + queryNumber + "%'");
		}
		if (StringUtil.isNull(queryCreateDateTo)
				&& queryCreateDateTo.length() == 0) {
			sbSql.append(" and a.authortime>= '" + createDateFrom + "'");
		} else if (StringUtil.isNull(queryCreateDateFrom)
				&& queryCreateDateFrom.length() == 0) {
			sbSql.append(" and a.authortime <= '" + createDateTo + "'");
		} else if (!StringUtil.isNull(queryCreateDateFrom)
				&& !StringUtil.isNull(queryCreateDateTo)) {
			sbSql.append(" and a.authortime between '" + createDateFrom
					+ "' and '" + createDateTo + "'");
		}
		if (creatorID != null && creatorID.length() > 0) {
			creator = UserHelper.getService().getUser(creatorID);
			sbSql.append(" and  a.createbyid = '" + creator.getInnerId()
					+ "' and a.createbyclassid = '" + creator.getClassId()
					+ "'");
		}

		return sbSql.toString();
	}

	public HSSFWorkbook getExcelObject(List<Map<String, Object>> result,
			String changeClassId) {
		String spotId = "";
		String spotInstance = "";
		if ("doc".equals(changeClassId)) {
			spotId = "QUERYACTIVEDOCUMENTS";
			spotInstance = "QUERYACTIVEDOCUMENTS";
		} else if ("set".equals(changeClassId)) {
			spotId = "QUERYACTIVESET";
			spotInstance = "QUERYACTIVESET";
		} else {
			spotId = "QUERYACTIVEORDER";
			spotInstance = "QUERYACTIVEORDER";
		}
		Spot spot = SpotUtil.getSpot(spotId);
		List<GridColumn> columns = GridUtil.getColumns(spotId, spotInstance);

		List<String> headers = new ArrayList<String>();

		try {
			ExcelUtil ew = new ExcelUtil();

			String sheetName = "ͳ�Ʋ�ѯ";

			ew.init(sheetName);

			// ������Ԫ����ʽ
			HSSFCellStyle headStyle = ew.getCellStyle((short) 200, true, false,
					true);
			HSSFCellStyle detailStyle = ew.getCellStyle((short) 200, true,
					false, false);

			int columnSize = columns.size();
			int rowSize = result.size();
			ew.createRow(rowSize + 1, columnSize - 1);
			ew.setRowHeight(1, (short) 300);

			// ew.setCell(1, 1, "No.", headStyle);
			ew.setColumnWidth(1, 1500);
			ew.setColumnWidth(2, 3700);
			for (int columnIndex = 1; columnIndex < columnSize; columnIndex++) {
				GridColumn column = columns.get(columnIndex);
				String columnId = column.getAttrId();

				// int width = column.getWidth();

				String name = column.getAttrName();

				// �����Զ�����Ϣ���������ơ���ȡ��༭
				Column customColumn = spot.getCustomColumns().get(columnId);
				if (customColumn != null
						&& customColumn.getColumnName() != null) {
					name = customColumn.getColumnName();
				}
				headers.add(columnId);

				ew.setCell(1, columnIndex, name, headStyle);
				ew.setColumnWidth(columnIndex, 3700);
			}

			for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
				// ew.setCell(rowIndex + 2, 1, rowIndex + 1, detailStyle);
				Map<String, Object> map = result.get(rowIndex);
				// ���������ȡObject��������ֵ
				Object[] arrStr = new Object[columnSize];
				for (int columnIndex = 0; columnIndex < columnSize - 1; columnIndex++) {
					String key = headers.get(columnIndex);
					Object object = map.get(key);
					String cellValue = "";
					if (!StringUtil.isNull(object)) {
						cellValue = object.toString();
						if (StringUtil.isHtmlTag(cellValue)) {
							cellValue = StringUtil.delHtmlTag(cellValue);
						}
					}

					// ��ȡ��Ԫ��ֵ
					arrStr[columnIndex] = StringUtil.getString(cellValue);

					ew.setCell(rowIndex + 2, columnIndex + 1, cellValue,
							detailStyle);
				}

				// ����,��ȡ��������ֵ
				String maxLength = StringUtil.maxValue(arrStr);

				// ���ݳ�������ַ������õ�Ԫ������߶�
				ew.setCellHeightStyle(rowIndex + 2, 3, maxLength, 7);
			}

			// ���ô�ӡ��ʽ
			ew.setPrintStyle(true);
			ew.close();

			return ew.getCurrentWorkbook();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ActiveDocument> getActiveDocumentByNumber(String number) {
		String sql = "SELECT A .* FROM ADM_ACTIVEDOCUMENT A, ADM_ACTIVEDOCUMENTMASTER B WHERE A .MASTERID = B.INNERID AND A .MASTERCLASSID = B.CLASSID AND B. ID = ?";
		List<ActiveDocument> resultList = (List<ActiveDocument>) Helper
				.getPersistService().query(sql, ActiveDocument.class, number);
		return resultList;
	}

	@Override
	public void deleteLink(String oid) {
		// ɾ������LINK-��ǰ
		List<ActiveOrderLink> orderbeforeLinkList = orderService
				.getActiveOrderLinkByBeforeObject(oid);
		for (ActiveOrderLink link : orderbeforeLinkList) {
			orderService.deleteActiveOrderLinkByBeforeObject(link);
		}
		// ɾ������LINK-�ĺ�
		List<ActiveOrderLink> orderAfterLinkList = orderService
				.getActiveOrderLinkByAfterObject(oid);
		for (ActiveOrderLink link : orderAfterLinkList) {
			orderService.deleteActiveOrderLinkByAfterObject(link);
		}
		// ɾ����ͼLINK
		// List<ActiveSetLink> setLinkList = setService
		// .getActiveSetLinkByObject(oid);
		// for (ActiveSetLink link : setLinkList) {
		// setService.removeActiveSetedLink(link);
		// }
	}

	@Override
	public void updateActiveLife(List<LifeCycleManaged> list) {
		for (LifeCycleManaged active : list) {
			Helper.getPersistService().update(active);
		}
	}
}
