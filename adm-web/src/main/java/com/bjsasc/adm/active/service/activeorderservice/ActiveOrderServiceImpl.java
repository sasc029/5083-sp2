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
 * �����ļ����ݷ���ʵ���ࡣ
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

		// ����master����
		ActiveOrderMaster master = (ActiveOrderMaster) PersistUtil
				.createObject(ActiveOrderMaster.CLASSID);
		// �����汾����
		ActiveOrder document = (ActiveOrder) VersionControlUtil.createVersion(
				classId, master, checkoutState);
		return document;
	}

	@Override
	public String createActiveOrderMaster(String number, String name,
			String secLevel) {
		ActiveOrderMaster masterObj = (ActiveOrderMaster) PersistUtil
				.createObject(ActiveOrderMaster.CLASSID);
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
	public String createActiveOrder(Map<String, String> paramMap) {
		// ����
		String classID = paramMap.get("CLASSIDS");
		// ���
		String number = paramMap.get("NUMBER");
		// ����
		String name = paramMap.get("NAME");
		// ��ע
		String note = paramMap.get("NOTE");

		// ��Դ
		String dataSource = paramMap.get("DATASOURCE");
		// ����
		String activeCode = paramMap.get("ACTIVECODE");
		// �����ļ����
		String activeDocumentNumber = paramMap.get("ACTIVEDOCUMENTNUMBER");

		// ҳ��
		String strPages = paramMap.get("PAGES");
		int pages = StringUtil.str2int(strPages);
		// ����
		String strCount = paramMap.get("COUNT");
		int count = StringUtil.str2int(strCount);

		// ������
		// String contextOid = paramMap.get("CONTEXT");
		// �ܼ�
		String secLevel = paramMap.get("SECLEVEL");
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

		// �����汾����
		ActiveOrder obj;
		ActiveOrder oldObj;
		if ("0".equals(versionFlag)) {
			// ���е��������󱣴�
			String masterOid = createActiveOrderMaster(number, name, secLevel);
			// �����汾����
			obj = (ActiveOrder) VersionControlUtil.createVersion(classID,
					(Mastered) Helper.getPersistService().getObject(masterOid));
		} else {
			oldObj = getActiveOrderByNumber(number).get(0);
			obj = (ActiveOrder) VersionControlUtil.getService().newVersion(
					oldObj, version, "1");
		}

		// ��Դ
		obj.setDataSource(dataSource);
		// �����ļ����
		obj.setActiveDocumentNumber(activeDocumentNumber);
		// ����
		obj.setActiveCode(activeCode);
		// ҳ��
		obj.setPages(pages);
		// ����
		obj.setCount(count);

		// ��ע
		obj.setNote(note);
		// ����
		obj.setAuthorName(authorName);
		// ���ߵ�λ
		obj.setAuthorUnit(authorUnit);
		// ����ʱ��
		obj.setAuthorTime(DateTimeUtil.parseDateLong(authorTime,
				DateTimeUtil.DATE_YYYYMMDD));
		// ��������
		ActiveLifecycleService lifeService = AdmHelper
				.getActiveLifecycleService();
		lifeService.initLifecycle(obj);
		// �ļ�����Ϣ
		if (folderOid != null && !folderOid.equals("")) {
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
		return Helper.getOid(obj);
	}

	@Override
	public String deleteActiveOrder(String oids) {
		List<String> oidList = SplitString.string2List(oids, ",");
		for (String oid : oidList) {
			// ɾ������LINK
			deleteActiveOrderLink(oid);
			ActiveOrder obj = (ActiveOrder) Helper.getPersistService()
					.getObject(oid);
			ActiveOrderMaster masterObj = (ActiveOrderMaster) obj.getMaster();
			// ɾ���ļ�����
			Folder folder = (Folder) obj.getFolderInfo().getFolder();
			if (folder != null) {
				FolderHelper.getService().removeFromFolder(folder,
						(Foldered) obj);
			}
			// ɾ��������
			Helper.getPersistService().delete(masterObj);
			// ɾ�����е���
			Helper.getPersistService().delete(obj);
		}
		return null;
	}

	@Override
	public void updataActiveOrder(Map<String, String> paramMap) {

		// ���е���OID
		String oid = paramMap.get("OID");
		// ��ע
		String note = paramMap.get("NOTE");
		// ��Դ
		String dataSource = paramMap.get("DATASOURCE");
		// ����
		String activeCode = paramMap.get("ACTIVECODE");
		// �����ļ����
		String activeDocumentNumber = paramMap.get("ACTIVEDOCUMENTNUMBER");
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

		// �������е���
		ActiveOrder obj = (ActiveOrder) Helper.getPersistService().getObject(
				oid);
		// ActiveOrderMaster masterObj = (ActiveOrderMaster) obj.getMaster();

		obj.setNote(note);

		// ��Դ
		obj.setDataSource(dataSource);
		// ����
		obj.setActiveCode(activeCode);
		// �����ļ����
		obj.setActiveDocumentNumber(activeDocumentNumber);

		// ҳ��
		obj.setPages(pages);
		// ����
		obj.setCount(count);

		// ������
		obj.setAuthorName(authorName);
		// ���ߵ�λ
		obj.setAuthorUnit(authorUnit);
		// ��������
		obj.setAuthorTime(DateTimeUtil.parseDateLong(authorTime,
				DateTimeUtil.DATE_YYYYMMDD));

		// ��ע
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
			// �����е�������Դ
			return true;
		} else {
			// �������е�������Դ
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
