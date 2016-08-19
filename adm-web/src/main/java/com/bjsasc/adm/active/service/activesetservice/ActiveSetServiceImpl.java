package com.bjsasc.adm.active.service.activesetservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.model.ActiveObjSet;
import com.bjsasc.adm.active.model.ActiveSeted;
import com.bjsasc.adm.active.model.activeorderlink.ActiveOrderLink;
import com.bjsasc.adm.active.model.activeset.ActiveSet;
import com.bjsasc.adm.active.model.activeset.ActiveSetMaster;
import com.bjsasc.adm.active.model.activesetlink.ActiveSetLink;
import com.bjsasc.adm.active.service.activelifecycle.ActiveLifecycleService;
import com.bjsasc.adm.active.service.activeoptvalidation.ActiveOptValidationService;
import com.bjsasc.adm.active.service.activeorderservice.ActiveOrderService;
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
import com.bjsasc.plm.core.util.StringUtil;
import com.cascc.avidm.util.SplitString;

/**
 * �����׷���ʵ���ࡣ
 * 
 * @author yanjia 2013-5-13
 */
@SuppressWarnings("deprecation")
public class ActiveSetServiceImpl implements ActiveSetService {
	private final ActiveOrderService orderService = AdmHelper.getActiveOrderService();
	
	public ActiveSet newActiveSet(String classId) {
		ActiveSet document = newActiveSet(classId, VersionControlUtil.CHECKOUTSTATE_IN);
		return document;
	}
	
	public ActiveSet newActiveSet(String classId, String checkoutState) {
		
		//����master����
		ActiveSetMaster master =  (ActiveSetMaster) PersistUtil.createObject(ActiveSetMaster.CLASSID);
		//�����汾����
		ActiveSet document = (ActiveSet) VersionControlUtil.createVersion(classId, master, checkoutState);
		return document;
	}
	
	@Override
	public List<ActiveSet> getAllActiveSet() {
		String sql = "SELECT * FROM ADM_ACTIVESET WHERE CLASSID || ':' || INNERID NOT IN ( SELECT (ITEMCLASSID || ':' || ITEMID) AS OID FROM ADM_ACTIVERECYCLE )";
		List<ActiveSet> resultList = (List<ActiveSet>) Helper.getPersistService().query(sql, ActiveSet.class);
		return resultList;
	}

	@Override
	public String createActiveSetMaster(String number, String name, String secLevel) {
		ActiveSetMaster masterObj = (ActiveSetMaster) PersistUtil.createObject(ActiveSetMaster.CLASSID);
		// ���
		masterObj.setNumber(number);
		// ����
		masterObj.setName(name);
		if (secLevel != null && !secLevel.isEmpty()) {// ���ܼ���Ϣ
			Persistable secPt = PersistHelper.getService().getObject(secLevel);
			if (secPt != null && secPt instanceof SecurityLevel) {
				SecurityLevel secLevelObj = (SecurityLevel) secPt;
				masterObj.setSecurityLevelInfo(secLevelObj.buildSecurityLevelInfo());
			}
		}
		Helper.getPersistService().save(masterObj);
		return Helper.getOid(masterObj);
	}

	@Override
	public String createActiveSet(Map<String, String> paramMap) {

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
		String contextOid = paramMap.get("CONTEXT");
		// �ܼ�
		String secLevel = paramMap.get("SECLEVEL");
		// �ļ���OID
		String folderOid = paramMap.get("folderOid");
		// �汾Flag
		String versionFlag = paramMap.get("VERSIONFlAG");
		// �汾
		String version = paramMap.get("VERSION");

		String masterOid = "";
		if ("0".equals(versionFlag)) {
			// �����������󱣴�
			masterOid = createActiveSetMaster(number, name, secLevel);
		}
		
		//�����汾����
		ActiveSet obj;
		ActiveSet oldObj;
		if ("0".equals(versionFlag)) {
			obj = (ActiveSet) VersionControlUtil.createVersion(classID, (Mastered) Helper.getPersistService()
					.getObject(masterOid));
		} else {
			oldObj = getActiveSetByNumber(number).get(0);
			obj = (ActiveSet) VersionControlUtil.getService().newVersion(oldObj, version, "1");
		}
		// ������
		Context context = ContextHelper.getService().getContext(contextOid);
		ContextHelper.getService().addToContext(obj, context);

		// ����Ϣ
		obj.setDomainInfo(context.getDomainInfo());
		// ��ע
		obj.setNote(note);

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

		// ��������
		ActiveLifecycleService lifeService = AdmHelper.getActiveLifecycleService();
		lifeService.initLifecycle(obj);
		// �ļ�����Ϣ
		if (folderOid != null && !folderOid.equals("")) {
			Folder folder = (Folder) Helper.getPersistService().getObject(folderOid);
			FolderHelper.getService().addToFolder(folder, (Foldered) obj);
			//setFolderInfo((Foldered) obj, (AbstractFolder) folder);
		}

		//���Ȩ��
		ActiveOptValidationService validationService = AdmHelper.getActiveOptValidationService();
		boolean flag = validationService.getCreateActiveDocumentValidation(Operate.CREATE, obj);
		if (flag == false) {
			AccessControlHelper.getService().checkEntityPermission(Operate.CREATE, obj);
		}

		if ("0".equals(versionFlag)) {
			Helper.getPersistService().save(obj);
		} else {
			Helper.getPersistService().update(obj);
		}
		return Helper.getOid(obj);
	}

	@Override
	public String deleteActiveSet(String oids) {
		// TODO
		List<String> oidList = SplitString.string2List(oids, ",");
		for (String oid : oidList) {
			// ɾ�����LINK
			deleteLink(oid);
			// ɾ������LINK
			deleteActiveSetLink(oid);
			ActiveSet obj = (ActiveSet) Helper.getPersistService().getObject(oid);
			ActiveSetMaster masterObj = (ActiveSetMaster) obj.getMaster();
			// ɾ���ļ�����
			Folder folder = (Folder) obj.getFolderInfo().getFolder();
			if (folder != null) {
				FolderHelper.getService().removeFromFolder(folder, (Foldered) obj);
			}
			// ɾ��������
			Helper.getPersistService().delete(masterObj);
			// ɾ�����е���
			Helper.getPersistService().delete(obj);
		}
		return null;
	}

	@Override
	public String updataActiveSet(Map<String, String> paramMap) {
		
		// ������OID
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
		
		ActiveSet obj = (ActiveSet) Helper.getPersistService().getObject(oid);
		ActiveSetMaster masterObj = (ActiveSetMaster) obj.getMaster();

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
		// ��ע
		obj.setNote(note);
		updataActiveSetMaster(masterObj);
		updataActiveSet(obj);
		return null;
	}

	@Override
	public String updataActiveSet(ActiveSet obj) {
		// TODO
		Helper.getPersistService().update(obj);
		return null;
	}

	@Override
	public String updataActiveSetMaster(ActiveSetMaster masterObj) {
		// TODO
		Helper.getPersistService().update(masterObj);
		return null;
	}

	@Override
	public List<ActiveSetMaster> getAllActiveSetMaster() {
		// TODO
		String sql = "SELECT * FROM ADM_ACTIVESETMASTER";
		List<ActiveSetMaster> resultList = (List<ActiveSetMaster>) Helper.getPersistService().query(sql,
				ActiveSetMaster.class);
		return resultList;
	}

	@Override
	public String deleteActiveSetLink(String oid) {
		String sql = "SELECT * FROM ADM_ACTIVESETLINK WHERE FROMOBJECTCLASSID || ':' || FROMOBJECTID =?";
		List<ActiveSetLink> list = Helper.getPersistService().query(sql, ActiveSetLink.class, oid);
		for (ActiveSetLink link : list) {
			Helper.getPersistService().delete(link);
		}
		return null;
	}

	@Override
	public List<ActiveSetLink> getActiveSetLinkByOID(String oids) {
		String sql = "SELECT * FROM ADM_ACTIVESETLINK WHERE FROMOBJECTCLASSID || ':' || FROMOBJECTID =?";
		List<ActiveSetLink> list = Helper.getPersistService().query(sql, ActiveSetLink.class, oids);
		return list;
	}

	@Override
	public String deleteActiveSetLink(String setOid, String objectOid) {
		ActiveObjSet activeSet = (ActiveObjSet) Helper.getPersistService().getObject(setOid);
		ActiveSeted activeSeted = (ActiveSeted) Helper.getPersistService().getObject(objectOid);
		removeFromActiveSet(activeSet, activeSeted);
		return null;
	}

	@Override
	public void removeActiveSetedLink(ActiveSetLink activeSetedLink) {
		Helper.getPersistService().delete(activeSetedLink);
	}

	@Override
	public List<ActiveSetLink> getActiveSetLinkByObject(String objectOid) {
		String sql = "SELECT * FROM ADM_ACTIVESETLINK WHERE TOOBJECTCLASSID || ':' || TOOBJECTID =?";
		List<ActiveSetLink> list = Helper.getPersistService().query(sql, ActiveSetLink.class, objectOid);
		return list;
	}

	@Override
	public boolean isActiveSeted(Persistable object) {
		if (object instanceof ActiveSeted) {
			// ������������Դ
			return true;
		} else {
			// ��������������Դ
			return false;
		}

	}

	@Override
	public void addToActiveSet(ActiveObjSet activeSet, ActiveSeted activeSeted) {
		ActiveSetLink link = (ActiveSetLink) PersistUtil.createObject(ActiveSetLink.CLASSID);
		link.setFromObject(activeSet);
		link.setToObject(activeSeted);
		Helper.getPersistService().save(link);
	}

	@Override
	public void addToActiveSet(ActiveObjSet activeSet, List<ActiveSeted> activeSeteds) {
		for (ActiveSeted activeSeted : activeSeteds) {
			ActiveSetLink link = (ActiveSetLink) PersistUtil.createObject(ActiveSetLink.CLASSID);
			link.setFromObject(activeSet);
			link.setToObject(activeSeted);
			Helper.getPersistService().save(link);
		}
	}

	@Override
	public List<ActiveSeted> getActiveItems(ActiveObjSet activeSet) {
		String sql = "SELECT * FROM ADM_ACTIVESETLINK WHERE FROMOBJECTCLASSID || ':' || FROMOBJECTID =?";
		List<ActiveSetLink> linkList = Helper.getPersistService().query(sql, ActiveSetLink.class,
				Helper.getOid(activeSet));
		List<ActiveSeted> activeSeteds = new ArrayList<ActiveSeted>();
		for (ActiveSetLink link : linkList) {
			activeSeteds.add((ActiveSeted) link.getTo());
		}
		return activeSeteds;
	}

	@Override
	public void removeFromActiveSet(ActiveObjSet activeSet, ActiveSeted activeSeted) {
		String sql = "SELECT * FROM ADM_ACTIVESETLINK WHERE FROMOBJECTCLASSID || ':' || FROMOBJECTID =? AND TOOBJECTCLASSID || ':' || TOOBJECTID =?";
		String activeSetOid = Helper.getOid(activeSet);
		String activeSetedOid = Helper.getOid(activeSeted);
		List<ActiveSetLink> list = Helper.getPersistService().query(sql, ActiveSetLink.class, activeSetOid,
				activeSetedOid);
		for (ActiveSetLink link : list) {
			Helper.getPersistService().delete(link);
		}
	}

	@Override
	public void removeFromActiveSet(ActiveObjSet activeSet, List<ActiveSeted> activeSeteds) {
		String sql = "SELECT * FROM ADM_ACTIVESETLINK WHERE FROMOBJECTCLASSID || ':' || FROMOBJECTID =? AND TOOBJECTCLASSID || ':' || TOOBJECTID =?";
		String activeSetOid = Helper.getOid(activeSet);
		for (ActiveSeted activeSeted : activeSeteds) {
			String activeSetedOid = Helper.getOid(activeSeted);
			List<ActiveSetLink> list = Helper.getPersistService().query(sql, ActiveSetLink.class, activeSetOid,
					activeSetedOid);
			for (ActiveSetLink link : list) {
				Helper.getPersistService().delete(link);
			}
		}
	}

	@Override
	public void deleteLink(String oid) {
		// ɾ������LINK-��ǰ
		List<ActiveOrderLink> orderbeforeLinkList = orderService.getActiveOrderLinkByBeforeObject(oid);
		for (ActiveOrderLink link : orderbeforeLinkList) {
			orderService.deleteActiveOrderLinkByBeforeObject(link);
		}
		// ɾ������LINK-�ĺ�
		List<ActiveOrderLink> orderAfterLinkList = orderService.getActiveOrderLinkByAfterObject(oid);
		for (ActiveOrderLink link : orderAfterLinkList) {
			orderService.deleteActiveOrderLinkByAfterObject(link);
		}
	}

	@Override
	public List<ActiveSet> getActiveSetByNumber(String number) {
		String sql = "SELECT A .* FROM ADM_ACTIVESET A, ADM_ACTIVESETMASTER B WHERE A .MASTERID = B.INNERID AND A .MASTERCLASSID = B.CLASSID AND B. ID = ?";
		List<ActiveSet> resultList = (List<ActiveSet>) Helper.getPersistService().query(sql, ActiveSet.class, number);
		return resultList;
	}
}
