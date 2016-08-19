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
 * 现行文件服务实现类。
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

		// 创建master对象
		ActiveDocumentMaster master = (ActiveDocumentMaster) PersistUtil
				.createObject(ActiveDocumentMaster.CLASSID);
		// 创建版本对象
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
		// 编号
		masterObj.setNumber(number);
		// 名称
		masterObj.setName(name);
		if (secLevel != null && !secLevel.isEmpty()) {// 有密级信息
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

		// 编号
		String number = paramMap.get("NUMBER");
		// 名称
		String name = paramMap.get("NAME");
		// 来源
		String dataSource = paramMap.get("DATASOURCE");
		// 代号
		String activeCode = paramMap.get("ACTIVECODE");
		// 备注
		String note = paramMap.get("NOTE");
		// 类型
		String classID = paramMap.get("CLASSIDS");
		// 上下文
		// String contextOid = paramMap.get("CONTEXT");
		// 密集
		String secLevel = paramMap.get("SECLEVEL");
		// 页数
		String strPages = paramMap.get("PAGES");
		int pages = StringUtil.str2int(strPages);
		// 份数
		String strCount = paramMap.get("COUNT");
		int count = StringUtil.str2int(strCount);
		// 作者
		String authorName = paramMap.get("AUTHORNAME");
		// 作者单位
		String authorUnit = paramMap.get("AUTHORUNIT");
		// 创建时间
		String authorTime = paramMap.get("AUTHORTIME");
		// 文件夹OID
		String folderOid = paramMap.get("folderOid");
		// 版本Flag
		String versionFlag = paramMap.get("VERSIONFlAG");
		// 版本
		String version = paramMap.get("VERSION");

		String masterOid = "";
		if ("0".equals(versionFlag)) {
			// 现行文件主对象保存
			masterOid = createActiveDocumentMaster(number, name, secLevel);
		}

		// 创建版本对象
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

		// 备注
		obj.setNote(note);
		// 来源
		obj.setDataSource(dataSource);
		// 代号
		obj.setActiveCode(activeCode);
		// 页数
		obj.setPages(pages);
		// 份数
		obj.setCount(count);
		// 作者
		obj.setAuthorName(authorName);
		// 作者单位
		obj.setAuthorUnit(authorUnit);
		// 创建时间
		obj.setAuthorTime(DateTimeUtil.parseDateLong(authorTime,
				DateTimeUtil.DATE_YYYYMMDD));
		// 类型
		obj.setClassId(classID);
		// 生命周期
		ActiveLifecycleService lifeService = AdmHelper
				.getActiveLifecycleService();
		lifeService.initLifecycle(obj);
		// 文件夹信息
		if (folderOid != null && folderOid.length() > 0) {
			Folder folder = (Folder) Helper.getPersistService().getObject(
					folderOid);

			// 上下文
			Context context = folder.getContextInfo().getContext();
			ContextHelper.getService().addToContext(obj, context);

			// 域信息
			obj.setDomainInfo(folder.getDomainInfo());
			FolderHelper.getService().addToFolder(folder, (Foldered) obj);
		}

		// 检查权限
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
		 * //设置锁 if(isLocked){ Helper.getLockService().lock(baseline); }else{
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
			// 删除相关LINK
			deleteLink(oid);
			// 删除文件数据
			Folder folder = (Folder) object.getFolderInfo().getFolder();
			if (folder != null) {
				FolderHelper.getService().removeFromFolder(folder,
						(Foldered) object);

			}
			// 删除主对象-如果没有其他版本则删除
			List<ActiveDocument> list = getActiveDocumentByNumber(masterObj
					.getNumber());
			if (list == null || list.size() <= 1) {
				Helper.getPersistService().delete(masterObj);
			}
			// 删除现行文件对象
			Helper.getPersistService().delete(object);
		}
		return null;
	}

	@Override
	public String putIntoStorage(String oid) {

		String flag = "fail";
		Persistable obj = Helper.getPersistService().getObject(oid);
		LifeCycleService service = LifeCycleHelper.getService();
		// // 修改现行对象生命周期
		// if (obj instanceof LifeCycleManaged) {
		// // 生命周期
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
					.getAfterItems(activeOrder);// 取得改后对象
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
						.getActiveOrderService().getAfterItems(activeOrder);// 取得改后对象
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

		// 现行文件OID
		String oid = paramMap.get("OID");
		// 来源
		String dataSource = paramMap.get("DATASOURCE");
		// 代号
		String activeCode = paramMap.get("ACTIVECODE");

		// 页数
		String strPages = paramMap.get("PAGES");
		int pages = StringUtil.str2int(strPages);
		// 份数
		String strCount = paramMap.get("COUNT");
		int count = StringUtil.str2int(strCount);
		// 作者
		String authorName = paramMap.get("AUTHORNAME");
		// 作者单位
		String authorUnit = paramMap.get("AUTHORUNIT");
		// 创建时间
		String authorTime = paramMap.get("AUTHORTIME");
		// 备注
		String note = paramMap.get("NOTE");

		ActiveDocument obj = (ActiveDocument) Helper.getPersistService()
				.getObject(oid);
		ActiveDocumentMaster masterObj = (ActiveDocumentMaster) obj.getMaster();

		// 来源
		obj.setDataSource(dataSource);
		// 代号
		obj.setActiveCode(activeCode);
		// 页数
		obj.setPages(pages);
		// 份数
		obj.setCount(count);
		// 作者
		obj.setAuthorName(authorName);
		// 作者单位
		obj.setAuthorUnit(authorUnit);
		// 创建时间
		obj.setAuthorTime(DateTimeUtil.parseDateLong(authorTime,
				DateTimeUtil.DATE_YYYYMMDD));
		// 备注
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

			String sheetName = "统计查询";

			ew.init(sheetName);

			// 创建单元格样式
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

				// 场景自定义信息，包括名称、宽度、编辑
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
				// 定义数组获取Object对象所有值
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

					// 获取单元格值
					arrStr[columnIndex] = StringUtil.getString(cellValue);

					ew.setCell(rowIndex + 2, columnIndex + 1, cellValue,
							detailStyle);
				}

				// 排序,获取长度最大的值
				String maxLength = StringUtil.maxValue(arrStr);

				// 根据长度最大字符串设置单元格拉伸高度
				ew.setCellHeightStyle(rowIndex + 2, 3, maxLength, 7);
			}

			// 设置打印版式
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
		// 删除单据LINK-改前
		List<ActiveOrderLink> orderbeforeLinkList = orderService
				.getActiveOrderLinkByBeforeObject(oid);
		for (ActiveOrderLink link : orderbeforeLinkList) {
			orderService.deleteActiveOrderLinkByBeforeObject(link);
		}
		// 删除单据LINK-改后
		List<ActiveOrderLink> orderAfterLinkList = orderService
				.getActiveOrderLinkByAfterObject(oid);
		for (ActiveOrderLink link : orderAfterLinkList) {
			orderService.deleteActiveOrderLinkByAfterObject(link);
		}
		// 删除套图LINK
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
