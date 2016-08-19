package com.bjsasc.adm.active.service.activeorderservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.model.ActiveObjOrder;
import com.bjsasc.adm.active.model.ActiveOrdered;
import com.bjsasc.adm.active.model.activeorder.ActiveOrder;
import com.bjsasc.adm.active.model.activeorder.ActiveOrderMaster;
import com.bjsasc.adm.active.model.activeorderlink.ActiveOrderLink;
import com.bjsasc.adm.active.service.activelifecycle.ActiveLifecycleService;
import com.bjsasc.adm.active.service.activeoptvalidation.ActiveOptValidationService;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.platform.objectmodel.business.version.Mastered;
import com.bjsasc.platform.objectmodel.business.version.VersionControlUtil;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.ContextHelper;
import com.bjsasc.plm.core.folder.Folder;
import com.bjsasc.plm.core.folder.FolderHelper;
import com.bjsasc.plm.core.folder.Foldered;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.system.access.AccessControlHelper;
import com.bjsasc.plm.core.system.access.operate.Operate;
import com.bjsasc.plm.core.system.securitylevel.SecurityLevel;
import com.bjsasc.plm.core.util.DateTimeUtil;
import com.bjsasc.plm.core.util.StringUtil;
import com.cascc.avidm.util.SplitString;

/**
 * 现行文件单据服务实现类。
 * 
 * @author yanjia 2013-6-3
 */
@SuppressWarnings("deprecation")
public class ActiveOrderServiceImpl implements ActiveOrderService {

	public ActiveOrder newActiveOrder(String classId) {
		ActiveOrder document = newActiveOrder(classId,
				VersionControlUtil.CHECKOUTSTATE_IN);
		return document;
	}

	public ActiveOrder newActiveOrder(String classId, String checkoutState) {

		// 创建master对象
		ActiveOrderMaster master = (ActiveOrderMaster) PersistUtil
				.createObject(ActiveOrderMaster.CLASSID);
		// 创建版本对象
		ActiveOrder document = (ActiveOrder) VersionControlUtil.createVersion(
				classId, master, checkoutState);
		return document;
	}

	@Override
	public String createActiveOrderMaster(String number, String name,
			String secLevel) {
		ActiveOrderMaster masterObj = (ActiveOrderMaster) PersistUtil
				.createObject(ActiveOrderMaster.CLASSID);
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
	public String createActiveOrder(Map<String, String> paramMap) {
		// 类型
		String classID = paramMap.get("CLASSIDS");
		// 编号
		String number = paramMap.get("NUMBER");
		// 名称
		String name = paramMap.get("NAME");
		// 备注
		String note = paramMap.get("NOTE");

		// 来源
		String dataSource = paramMap.get("DATASOURCE");
		// 代号
		String activeCode = paramMap.get("ACTIVECODE");
		// 现行文件编号
		String activeDocumentNumber = paramMap.get("ACTIVEDOCUMENTNUMBER");

		// 页数
		String strPages = paramMap.get("PAGES");
		int pages = StringUtil.str2int(strPages);
		// 份数
		String strCount = paramMap.get("COUNT");
		int count = StringUtil.str2int(strCount);

		// 上下文
		// String contextOid = paramMap.get("CONTEXT");
		// 密集
		String secLevel = paramMap.get("SECLEVEL");
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

		// 创建版本对象
		ActiveOrder obj;
		ActiveOrder oldObj;
		if ("0".equals(versionFlag)) {
			// 现行单据主对象保存
			String masterOid = createActiveOrderMaster(number, name, secLevel);
			// 创建版本对象
			obj = (ActiveOrder) VersionControlUtil.createVersion(classID,
					(Mastered) Helper.getPersistService().getObject(masterOid));
		} else {
			oldObj = getActiveOrderByNumber(number).get(0);
			obj = (ActiveOrder) VersionControlUtil.getService().newVersion(
					oldObj, version, "1");
		}

		// 来源
		obj.setDataSource(dataSource);
		// 现行文件编号
		obj.setActiveDocumentNumber(activeDocumentNumber);
		// 代号
		obj.setActiveCode(activeCode);
		// 页数
		obj.setPages(pages);
		// 份数
		obj.setCount(count);

		// 备注
		obj.setNote(note);
		// 作者
		obj.setAuthorName(authorName);
		// 作者单位
		obj.setAuthorUnit(authorUnit);
		// 创建时间
		obj.setAuthorTime(DateTimeUtil.parseDateLong(authorTime,
				DateTimeUtil.DATE_YYYYMMDD));
		// 生命周期
		ActiveLifecycleService lifeService = AdmHelper
				.getActiveLifecycleService();
		lifeService.initLifecycle(obj);
		// 文件夹信息
		if (folderOid != null && !folderOid.equals("")) {
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
		return Helper.getOid(obj);
	}

	@Override
	public String deleteActiveOrder(String oids) {
		List<String> oidList = SplitString.string2List(oids, ",");
		for (String oid : oidList) {
			// 删除自身LINK
			deleteActiveOrderLink(oid);
			ActiveOrder obj = (ActiveOrder) Helper.getPersistService()
					.getObject(oid);
			ActiveOrderMaster masterObj = (ActiveOrderMaster) obj.getMaster();
			// 删除文件数据
			Folder folder = (Folder) obj.getFolderInfo().getFolder();
			if (folder != null) {
				FolderHelper.getService().removeFromFolder(folder,
						(Foldered) obj);
			}
			// 删除主对象
			Helper.getPersistService().delete(masterObj);
			// 删除现行单据
			Helper.getPersistService().delete(obj);
		}
		return null;
	}

	@Override
	public void updataActiveOrder(Map<String, String> paramMap) {

		// 现行单据OID
		String oid = paramMap.get("OID");
		// 备注
		String note = paramMap.get("NOTE");
		// 来源
		String dataSource = paramMap.get("DATASOURCE");
		// 代号
		String activeCode = paramMap.get("ACTIVECODE");
		// 现行文件编号
		String activeDocumentNumber = paramMap.get("ACTIVEDOCUMENTNUMBER");
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

		// 更新现行单据
		ActiveOrder obj = (ActiveOrder) Helper.getPersistService().getObject(
				oid);
		// ActiveOrderMaster masterObj = (ActiveOrderMaster) obj.getMaster();

		obj.setNote(note);

		// 来源
		obj.setDataSource(dataSource);
		// 代号
		obj.setActiveCode(activeCode);
		// 现行文件编号
		obj.setActiveDocumentNumber(activeDocumentNumber);

		// 页数
		obj.setPages(pages);
		// 份数
		obj.setCount(count);

		// 编制者
		obj.setAuthorName(authorName);
		// 作者单位
		obj.setAuthorUnit(authorUnit);
		// 编制日期
		obj.setAuthorTime(DateTimeUtil.parseDateLong(authorTime,
				DateTimeUtil.DATE_YYYYMMDD));

		// 备注
		obj.setNote(note);

		// updataActiveOrderMaster(masterObj);
		updataActiveOrder(obj);
	}

	public void updataActiveOrderObject(String oid, String beforeOids,
			String afterOids) {

		deleteActiveOrderLink(oid);

		ActiveOrder activeOrder = (ActiveOrder) Helper.getPersistService()
				.getObject(oid);
		if (beforeOids != null && beforeOids.length() > 0) {
			List<String> beforeOidList = SplitString.string2List(beforeOids,
					",");
			for (String beforeOid : beforeOidList) {
				List<String> tempList = SplitString.string2List(beforeOid, ";");
				Persistable object = Helper.getPersistService().getObject(
						tempList.get(0));
				if (object == null) {
					continue;
				}
				addBeforeItem(activeOrder, (ActiveOrdered) object);
			}

		}

		if (afterOids != null && afterOids.length() > 0) {
			List<String> afterOidList = SplitString.string2List(afterOids, ",");
			for (String afterOid : afterOidList) {
				List<String> tempList = SplitString.string2List(afterOid, ";");
				Persistable objectAfter = Helper.getPersistService().getObject(
						tempList.get(1));
				if (objectAfter == null) {
					continue;
				}
				Persistable objectBefore = Helper.getPersistService()
						.getObject(tempList.get(0));
				ActiveOrderLink link = getLinkByBeforeItem(activeOrder,
						(ActiveOrdered) objectBefore);
				if (link == null) {
					addAfterItem(activeOrder, (ActiveOrdered) objectAfter);
				} else {
					addAfterItemByBeforeItem(link, (ActiveOrdered) objectAfter);
				}
			}
		}
	}

	@Override
	public void updataActiveOrder(ActiveOrder obj) {
		Helper.getPersistService().update(obj);
	}

	@Override
	public void updataActiveOrderMaster(ActiveOrderMaster masterObj) {
		Helper.getPersistService().update(masterObj);
	}

	@Override
	public boolean isActiveOrdered(Persistable object) {
		if (object instanceof ActiveOrdered) {
			// 是现行单据数据源
			return true;
		} else {
			// 不是现行单据数据源
			return false;
		}
	}

	@Override
	public void deleteActiveOrderLink(String oid) {
		List<ActiveOrderLink> linkList = getActiveOrderLinks(oid);
		Helper.getPersistService().deleteAll(linkList);
	}

	@Override
	public List<ActiveOrderLink> getActiveOrderLinkByObject(String objectOid) {
		List<ActiveOrderLink> resultList = new ArrayList<ActiveOrderLink>();
		resultList.addAll(getActiveOrderLinkByBeforeObject(objectOid));
		resultList.addAll(getActiveOrderLinkByAfterObject(objectOid));
		return resultList;
	}

	@Override
	public List<ActiveOrderLink> getActiveOrderLinkByBeforeObject(
			String objectOid) {
		String sql = "SELECT * FROM ADM_ACTIVEORDEREDLINK WHERE ORDEREDBEFORECLASSID || ':' || ORDEREDBEFOREID =?";
		List<ActiveOrderLink> resultList = Helper.getPersistService().query(
				sql, ActiveOrderLink.class, objectOid);
		return resultList;
	}

	@Override
	public List<ActiveOrderLink> getActiveOrderLinkByAfterObject(
			String objectOid) {
		String sql = "SELECT * FROM ADM_ACTIVEORDEREDLINK WHERE TOOBJECTCLASSID || ':' || TOOBJECTID =? ";
		List<ActiveOrderLink> resultList = Helper.getPersistService().query(
				sql, ActiveOrderLink.class, objectOid);
		return resultList;
	}

	@Override
	public void deleteActiveOrderLinkByBeforeObject(ActiveOrderLink activeLink) {
		if (activeLink.getTo() == null) {
			Helper.getPersistService().delete(activeLink);
		} else {
			activeLink.setOrderedBeforeClassID("");
			activeLink.setOrderedBeforeID("");
			Helper.getPersistService().update(activeLink);
		}
	}

	@Override
	public void deleteActiveOrderLinkByAfterObject(ActiveOrderLink activeLink) {
		if (activeLink.getOrderedBeforeOid() == null) {
			Helper.getPersistService().delete(activeLink);
		} else {
			activeLink.setToObject(null);
			Helper.getPersistService().update(activeLink);
		}
	}

	@Override
	public void addAfterItem(ActiveObjOrder activeOrder,
			ActiveOrdered activeOrdered) {
		ActiveOrderLink link = (ActiveOrderLink) PersistUtil
				.createObject(ActiveOrderLink.CLASSID);
		link.setFromObject(activeOrder);
		link.setToObject(activeOrdered);
		Helper.getPersistService().save(link);
	}

	@Override
	public void addAfterItems(ActiveObjOrder activeOrder,
			List<ActiveOrdered> activeOrdereds) {
		for (ActiveOrdered activeOrdered : activeOrdereds) {
			addAfterItem(activeOrder, activeOrdered);
		}
	}

	@Override
	public void addBeforeItem(ActiveObjOrder activeOrder,
			ActiveOrdered activeOrdered) {
		ActiveOrderLink link = (ActiveOrderLink) PersistUtil
				.createObject(ActiveOrderLink.CLASSID);
		link.setFromObject(activeOrder);
		String oid = Helper.getOid(activeOrdered);
		link.setOrderedBeforeClassID(Helper.getClassId(oid));
		link.setOrderedBeforeID(Helper.getInnerId(oid));
		Helper.getPersistService().save(link);
	}

	@Override
	public void addBeforeItems(ActiveObjOrder activeOrder,
			List<ActiveOrdered> activeOrdereds) {
		for (ActiveOrdered activeOrdered : activeOrdereds) {
			addBeforeItem(activeOrder, activeOrdered);
		}
	}

	@Override
	public List<ActiveOrderLink> getActiveOrderLinks(String activeOrderOid) {
		String hql = " from ActiveOrderLink l where l.fromObjectRef.classId || ':' || l.fromObjectRef.innerId = ? ";
		List<ActiveOrderLink> linkList = Helper.getPersistService().find(hql, activeOrderOid);
		return linkList;
	}

	@Override
	public List<ActiveOrdered> getAfterItems(ActiveObjOrder activeOrder) {
		List<ActiveOrderLink> linkList = getActiveOrderLinks(Helper.getOid(activeOrder));
		List<ActiveOrdered> activeOrders = new ArrayList<ActiveOrdered>();
		for (ActiveOrderLink link : linkList) {
			ActiveOrdered temp = (ActiveOrdered) link.getTo();
			if (temp == null) {
				continue;
			}
			activeOrders.add((ActiveOrdered) link.getTo());
		}
		return activeOrders;
	}

	@Override
	public List<ActiveOrdered> getBeforeItems(String activeOrderOid) {
		List<ActiveOrderLink> linkList = getActiveOrderLinks(activeOrderOid);
		List<ActiveOrdered> activeOrders = new ArrayList<ActiveOrdered>();
		for (ActiveOrderLink link : linkList) {
			String oid = link.getOrderedBeforeOid();
			if (oid == null || oid.isEmpty()) {
				continue;
			}
			Persistable object = Helper.getPersistService().getObject(oid);
			if (object != null) {
				activeOrders.add((ActiveOrdered) object);
			}
		}
		return activeOrders;
	}

	@Override
	public List<ActiveOrdered> getBeforeItemsByOrderAndAfterItem(
			String activeOrderOid, String objectOid) {
		String sql = "SELECT * FROM ADM_ACTIVEORDEREDLINK WHERE FROMOBJECTCLASSID || ':' || FROMOBJECTID =? AND TOOBJECTCLASSID || ':' || TOOBJECTID =?";
		List<ActiveOrderLink> linkList = Helper.getPersistService().query(sql,
				ActiveOrderLink.class, activeOrderOid, objectOid);
		List<ActiveOrdered> activeOrders = new ArrayList<ActiveOrdered>();
		for (ActiveOrderLink link : linkList) {
			String oid = link.getOrderedBeforeOid();
			if (oid == null || oid.isEmpty()) {
				continue;
			}
			activeOrders.add((ActiveOrdered) Helper.getPersistService()
					.getObject(oid));
		}
		return activeOrders;
	}

	@Override
	public void addAfterItemByBeforeItem(ActiveOrderLink link,
			ActiveOrdered activeOrdered) {
		link.setToObject(activeOrdered);
		Helper.getPersistService().update(link);
	}

	@Override
	public ActiveOrderLink getLinkByBeforeItem(ActiveObjOrder activeOrder,
			ActiveOrdered activeOrdered) {
		if (activeOrdered == null) {
			return null;
		}
		String sql = "SELECT * FROM ADM_ACTIVEORDEREDLINK WHERE FROMOBJECTCLASSID || ':' || FROMOBJECTID =? AND ORDEREDBEFORECLASSID || ':' || ORDEREDBEFOREID =?";
		String activeOrderedOid = Helper.getOid(activeOrder);
		String activeOrderededOid = Helper.getOid(activeOrdered);
		List<ActiveOrderLink> resultList = Helper.getPersistService().query(
				sql, ActiveOrderLink.class, activeOrderedOid,
				activeOrderededOid);
		if (resultList == null || resultList.isEmpty()) {
			return null;
		}
		return resultList.get(0);
	}

	@Override
	public List<ActiveOrder> getActiveOrderByNumber(String number) {
		String sql = "SELECT A .* FROM ADM_ACTIVEORDER A, ADM_ACTIVEORDERMASTER B WHERE A .MASTERID = B.INNERID AND A .MASTERCLASSID = B.CLASSID AND B. ID = ?";
		List<ActiveOrder> resultList = (List<ActiveOrder>) Helper
				.getPersistService().query(sql, ActiveOrder.class, number);
		return resultList;
	}
}
