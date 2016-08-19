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
 * 现行套服务实现类。
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
		
		//创建master对象
		ActiveSetMaster master =  (ActiveSetMaster) PersistUtil.createObject(ActiveSetMaster.CLASSID);
		//创建版本对象
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
		// 编号
		masterObj.setNumber(number);
		// 名称
		masterObj.setName(name);
		if (secLevel != null && !secLevel.isEmpty()) {// 有密级信息
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
		String contextOid = paramMap.get("CONTEXT");
		// 密集
		String secLevel = paramMap.get("SECLEVEL");
		// 文件夹OID
		String folderOid = paramMap.get("folderOid");
		// 版本Flag
		String versionFlag = paramMap.get("VERSIONFlAG");
		// 版本
		String version = paramMap.get("VERSION");

		String masterOid = "";
		if ("0".equals(versionFlag)) {
			// 现行套主对象保存
			masterOid = createActiveSetMaster(number, name, secLevel);
		}
		
		//创建版本对象
		ActiveSet obj;
		ActiveSet oldObj;
		if ("0".equals(versionFlag)) {
			obj = (ActiveSet) VersionControlUtil.createVersion(classID, (Mastered) Helper.getPersistService()
					.getObject(masterOid));
		} else {
			oldObj = getActiveSetByNumber(number).get(0);
			obj = (ActiveSet) VersionControlUtil.getService().newVersion(oldObj, version, "1");
		}
		// 上下文
		Context context = ContextHelper.getService().getContext(contextOid);
		ContextHelper.getService().addToContext(obj, context);

		// 域信息
		obj.setDomainInfo(context.getDomainInfo());
		// 备注
		obj.setNote(note);

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

		// 生命周期
		ActiveLifecycleService lifeService = AdmHelper.getActiveLifecycleService();
		lifeService.initLifecycle(obj);
		// 文件夹信息
		if (folderOid != null && !folderOid.equals("")) {
			Folder folder = (Folder) Helper.getPersistService().getObject(folderOid);
			FolderHelper.getService().addToFolder(folder, (Foldered) obj);
			//setFolderInfo((Foldered) obj, (AbstractFolder) folder);
		}

		//检查权限
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
			// 删除相关LINK
			deleteLink(oid);
			// 删除自身LINK
			deleteActiveSetLink(oid);
			ActiveSet obj = (ActiveSet) Helper.getPersistService().getObject(oid);
			ActiveSetMaster masterObj = (ActiveSetMaster) obj.getMaster();
			// 删除文件数据
			Folder folder = (Folder) obj.getFolderInfo().getFolder();
			if (folder != null) {
				FolderHelper.getService().removeFromFolder(folder, (Foldered) obj);
			}
			// 删除主对象
			Helper.getPersistService().delete(masterObj);
			// 删除现行单据
			Helper.getPersistService().delete(obj);
		}
		return null;
	}

	@Override
	public String updataActiveSet(Map<String, String> paramMap) {
		
		// 现行套OID
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
		
		ActiveSet obj = (ActiveSet) Helper.getPersistService().getObject(oid);
		ActiveSetMaster masterObj = (ActiveSetMaster) obj.getMaster();

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
		// 备注
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
			// 是现行套数据源
			return true;
		} else {
			// 不是现行套数据源
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
		// 删除单据LINK-改前
		List<ActiveOrderLink> orderbeforeLinkList = orderService.getActiveOrderLinkByBeforeObject(oid);
		for (ActiveOrderLink link : orderbeforeLinkList) {
			orderService.deleteActiveOrderLinkByBeforeObject(link);
		}
		// 删除单据LINK-改后
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
