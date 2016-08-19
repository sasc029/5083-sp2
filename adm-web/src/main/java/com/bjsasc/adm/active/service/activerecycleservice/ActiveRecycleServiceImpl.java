package com.bjsasc.adm.active.service.activerecycleservice;

import java.util.ArrayList;
import java.util.List;

import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.model.AdmMaster;
import com.bjsasc.adm.active.model.Recycledable;
import com.bjsasc.adm.active.model.activedocument.ActiveDocument;
import com.bjsasc.adm.active.model.activeorder.ActiveOrder;
import com.bjsasc.adm.active.model.activerecycle.ActiveRecycle;
import com.bjsasc.adm.active.model.activeset.ActiveSet;
import com.bjsasc.adm.active.service.activedocumentservice.ActiveDocumentService;
import com.bjsasc.adm.active.service.activelifecycle.ActiveLifecycleService;
import com.bjsasc.adm.active.service.activeorderservice.ActiveOrderService;
import com.bjsasc.adm.active.service.activesetservice.ActiveSetService;
import com.bjsasc.adm.active.service.admmodelservice.AdmModelService;
import com.bjsasc.adm.common.AdmLifeCycleConstUtil;
import com.bjsasc.platform.objectmodel.business.persist.ObjectReference;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.platform.objectmodel.business.version.IterationInfo;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.ContextHelper;
import com.bjsasc.plm.core.context.model.Contexted;
import com.bjsasc.plm.core.domain.Domain;
import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.folder.AbstractFolder;
import com.bjsasc.plm.core.folder.Folder;
import com.bjsasc.plm.core.folder.FolderHelper;
import com.bjsasc.plm.core.folder.FolderInfo;
import com.bjsasc.plm.core.folder.FolderMemberLink;
import com.bjsasc.plm.core.folder.Foldered;
import com.bjsasc.plm.core.folder.IterationFoldered;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.system.access.AccessControlHelper;
import com.bjsasc.plm.core.system.access.operate.Operate;
import com.bjsasc.plm.core.system.securitylevel.SecurityLevel;
import com.bjsasc.plm.core.system.securitylevel.SecurityLevelInfo;
import com.bjsasc.plm.core.vc.model.Iterated;
import com.bjsasc.plm.core.vc.model.Mastered;
import com.bjsasc.plm.core.vc.VersionControlHelper;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.cascc.avidm.util.SplitString;

/**
 * 回收站服务实现类。
 * 
 * @author gengancong 2013-6-3
 */
public class ActiveRecycleServiceImpl implements ActiveRecycleService {
	private final ActiveOrderService orderService = AdmHelper.getActiveOrderService();
	private final ActiveDocumentService docService = AdmHelper.getActiveDocumentService();
	private final ActiveSetService setService = AdmHelper.getActiveSetService();

	@Override
	public List<ActiveRecycle> listItems() {

		String hql = "from ActiveRecycle ";

		List<ActiveRecycle> itemList = Helper.getPersistService().find(hql);

		return itemList;
	}

	@Override
	public void addltem(String oids) {
		List<Persistable> objects = new ArrayList<Persistable>();
		List<String> oidList = SplitString.string2List(oids, ",");
		for (String oid : oidList) {
			Persistable obj = Helper.getPersistService().getObject(oid);
			if (obj instanceof Recycledable) {
				addltem((Recycledable) obj);
			} else {
				objects.add(obj);
			}
		}
		Helper.getPersistService().deleteAll(objects);
	}	

	@Override
	public void addltem(Recycledable admRecycled) {

		IterationFoldered ifObj = (IterationFoldered) admRecycled;

		// TODO
		String propertiesUrl = "";
		admRecycled.getPropertiesUrl();

		// 回收对象ClassId 与 InnerId
		String itemClassId = ifObj.getClassId();
		String itemInnerId = ifObj.getInnerId();

		// 创建回收对象信息
		ActiveRecycle recycle = newActiveRecycle();

		recycle.setItemId(itemInnerId);
		recycle.setItemClassId(itemClassId);
		recycle.setModelId("");
		recycle.setItemPropertiesUrl(propertiesUrl);

		// 主对象信息
		Mastered master = ifObj.getMaster();
		if (master instanceof AdmMaster) {
			AdmMaster am = (AdmMaster) master;
			if (am != null) {
				String number = am.getNumber();
				String name = am.getName();
				recycle.setNumber(number);
				recycle.setName(name);

				// 密级信息
				SecurityLevelInfo sli = am.getSecurityLevelInfo();
				if (sli != null) {
					SecurityLevel sl = sli.getSecurityLevel();
					if (sl != null) {
						String securityLevel = sl.getName();
						recycle.setSecurityLevel(securityLevel);
					}
				}
			}
		}

		// 版本信息
		IterationInfo iterationInfo = ifObj.getIterationInfo();
		if (iterationInfo != null) {
			recycle.setVersionNo(iterationInfo.getVersionNo());
		}

		// 文件夹信息
		FolderInfo folderInfo = ifObj.getFolderInfo();
		if (folderInfo != null) {
			Folder folder = folderInfo.getFolder();
			if (folder != null) {
				// 文件夹PATH
				String folderPath = FolderHelper.getService().getFolderPathStr(folder);
				recycle.setFolderPath(folderPath);
				// 文件夹OID
				String folderOid = Helper.getOid(folder);
				// 模型名称
				AdmModelService modelService = AdmHelper.getAdmModelService();
				String modelFolderOid = modelService.getModelFolder(folderOid);
				String modelName = modelService.getModelName(modelFolderOid);
				recycle.setModelName(modelName);
			}
		}

		// 修改回收对象生命周期
		if (admRecycled instanceof LifeCycleManaged) {
			// 生命周期
			ActiveLifecycleService lifeService = AdmHelper.getActiveLifecycleService();
			LifeCycleManaged lcObj = (LifeCycleManaged) admRecycled;

			recycle.setLifeCycleInfo(lcObj.getLifeCycleInfo());
			Helper.getPersistService().save(recycle);

			lifeService.updateLifeCycleByStateName(lcObj, AdmLifeCycleConstUtil.LC_RECYCLE.getName());
			Helper.getPersistService().update(lcObj);
		} else {
			Helper.getPersistService().save(recycle);
		}
	}

	/**
	 * 创建回收站对象。
	 * 
	 * @return ReturnReason
	 */
	private ActiveRecycle newActiveRecycle() {
		ActiveRecycle recycle = (ActiveRecycle) PersistUtil.createObject(ActiveRecycle.CLASSID);
		return recycle;
	}

	@Override
	public void clearItems() {
		String hql = "from ActiveRecycle ";

		List<ActiveRecycle> itemList = Helper.getPersistService().find(hql);

		for (ActiveRecycle recycle : itemList) {
			String innerId = recycle.getItemId();
			String classId = recycle.getItemClassId();
			String oid = Helper.getOid(classId, innerId);
			Persistable obj = Helper.getPersistService().getObject(oid);
			if (obj instanceof ActiveDocument) {
				docService.deleteActiveDocument(oid);
			} else if (obj instanceof ActiveSet) {
				setService.deleteActiveSet(oid);
			} else if (obj instanceof ActiveOrder) {
				orderService.deleteActiveOrder(oid);
			}
			Helper.getPersistService().delete(recycle);
		}
	}

	@Override
	public void reductionItems(String recycleOids) {
		List<String> oidList = SplitString.string2List(recycleOids, ",");
		for (String recycleOid : oidList) {
			Persistable recycleObj = Helper.getPersistService().getObject(recycleOid);
			ActiveRecycle recycle = (ActiveRecycle) recycleObj;

			String activeOid = recycle.getItemClassId() + ":" + recycle.getItemId();
			Persistable activeObj = Helper.getPersistService().getObject(activeOid);
			// 修改回收对象生命周期
			if (activeObj instanceof LifeCycleManaged) {
				LifeCycleManaged lcObj = (LifeCycleManaged) activeObj;
				lcObj.setLifeCycleInfo(recycle.getLifeCycleInfo());
				Helper.getPersistService().update(lcObj);
			}
			Helper.getPersistService().delete(recycle);
		}
	}
	

	public void reductionItems(List<ActiveRecycle> recycleObjects) {
		for (int i=0; i<recycleObjects.size(); i++) {
			
			ActiveRecycle activeRecycle = recycleObjects.get(i);
			
			String innerId = activeRecycle.getItemId();
			String classId = activeRecycle.getItemClassId();
			
			//待移动的对象
			Persistable obj = PersistHelper.getService().getObject(classId, innerId);
			
			// 修改回收对象生命周期
			if (obj instanceof LifeCycleManaged) {
				LifeCycleManaged lcObj = (LifeCycleManaged) obj;
				lcObj.setLifeCycleInfo(activeRecycle.getLifeCycleInfo());
			}

			Helper.getPersistService().update(obj);
			Helper.getPersistService().delete(activeRecycle);
		}

		List<ActiveRecycle> arList = listItems();
		// 输出结果
		GridDataUtil.prepareRowObjects(arList, "ListActiveRecycles");
	}
	
	public void reductionItems(List<ActiveRecycle> recycleObjects, List<Persistable> targetFolders) {
		for (int i=0; i<recycleObjects.size(); i++) {
			
			ActiveRecycle activeRecycle = recycleObjects.get(i);
			
			String innerId = activeRecycle.getItemId();
			String classId = activeRecycle.getItemClassId();
			
			//待移动的对象
			Persistable obj = PersistHelper.getService().getObject(classId, innerId);
			
			Persistable fObj = targetFolders.get(i);
			AbstractFolder f2 = (AbstractFolder)fObj;
			
			// 修改回收对象生命周期
			if (obj instanceof LifeCycleManaged) {
				LifeCycleManaged lcObj = (LifeCycleManaged) obj;
				lcObj.setLifeCycleInfo(activeRecycle.getLifeCycleInfo());
			}
			
			FolderInfo folderInfo = ((Foldered)obj).getFolderInfo();
			Folder folder = folderInfo.getFolder();
			if (folder == null) {
				// 修改文件夹
				FolderMemberLink link = FolderHelper.getService().addToFolder(f2, (Foldered)obj);
				if(link!=null){
					ObjectReference ref = ObjectReference.newObjectReference(f2);
					FolderInfo info = new FolderInfo();
					info.setFolderRef(ref);
					info.setFolderName(f2.getName());
					//3更新对象属性信息
					if(obj instanceof IterationFoldered){
						List<Iterated> list = VersionControlHelper.getService().iterationsOf((Iterated)obj);
						for(Iterated iterated:list){
							IterationFoldered iteratedfoldered = (IterationFoldered)iterated;
							// 更新对象属性信息-文件夹信息、上下文信息、域信息
							setFolderInfo(iteratedfoldered,(AbstractFolder)f2);
							setContextInfo(iteratedfoldered,(AbstractFolder)f2);
							setDomainInfo(iteratedfoldered,(AbstractFolder)f2);
							setActiveDocumentClassId(iteratedfoldered,(AbstractFolder)f2);
						}
					}else{					
						// 更新对象属性信息-文件夹信息、上下文信息、域信息
						setFolderInfo((Foldered)obj,(AbstractFolder)f2);
						setContextInfo((Foldered)obj,(AbstractFolder)f2);
						setDomainInfo((Foldered)obj,(AbstractFolder)f2);
					}
					setActiveDocumentClassId((Foldered)obj,(AbstractFolder)f2);
				}
			}
			Helper.getPersistService().update(obj);
			Helper.getPersistService().delete(activeRecycle);
		}

		List<ActiveRecycle> arList = listItems();
		// 输出结果
		GridDataUtil.prepareRowObjects(arList, "ListActiveRecycles");
	}
	
	private void setActiveDocumentClassId(Foldered obj,AbstractFolder folder) {
		String classId = obj.getClassId();
		// 设置类型
		if (obj instanceof ActiveDocument) {
			AdmModelService admModelService = AdmHelper.getAdmModelService();
			String folderOid = Helper.getOid(folder);
			String modelFolderOid = admModelService.getModelFolder(folderOid);
			String modelId = admModelService.getModelId(modelFolderOid);
			if (modelId != null && modelId.length() > 0) {
				classId = modelId;
				obj.setClassId(classId);
				Helper.getPersistService().update(obj);
			}
		}
	}
	
	private void setFolderInfo(Foldered obj,AbstractFolder folder){
		// 更新对象属性信息
		ObjectReference ref = ObjectReference.newObjectReference(folder);
		FolderInfo info = new FolderInfo();
		info.setFolderRef(ref);
		info.setFolderName(folder.getName());
		obj.setFolderInfo(info);		
	}
	private void setContextInfo(Foldered obj,AbstractFolder folder){
		// 更新对象上下文信息
		if (obj instanceof Contexted) {
			if(((AbstractFolder) folder).getContextInfo()==null){
				return;
			}
			Context context = ((AbstractFolder) folder).getContextInfo().getContext();
			
			ContextHelper.getService().addToContext((Contexted) obj, context);
		}
	}
	private void setDomainInfo(Foldered obj,AbstractFolder folder){
		// 更新对象域信息
		if (obj instanceof Domained) {		
			Domain domain = ((AbstractFolder) folder).getDomainInfo().getDomain();		
			Domained domained = (Domained) obj;
			if(domained.getDomainInfo()!=null&&domained.getDomainInfo().getDomain()!=null){
				if(!domained.getDomainInfo().getDomain().getInnerId().equals(domain.getInnerId())){
					AccessControlHelper.getService().checkEntityPermission(Operate.CHANGE_DOMAIN, obj);
					Helper.getDomainService().addToDomain(domained, domain);
					AccessControlHelper.getService().checkEntityPermission(Operate.MOVE_INTO_DOMAIN, obj);
				}
			}
			else{
				Helper.getDomainService().addToDomain(domained, domain);
			}
			
		}
	}

	@Override
	public void deleteItems(String recycleOids) {
		List<String> oidList = SplitString.string2List(recycleOids, ",");
		for (String recycleOid : oidList) {
			ActiveRecycle recycle = (ActiveRecycle) Helper.getPersistService().getObject(recycleOid);
			String innerId = recycle.getItemId();
			String classId = recycle.getItemClassId();
			String oid = Helper.getOid(classId, innerId);
			Persistable obj = Helper.getPersistService().getObject(oid);
			if (obj instanceof ActiveDocument) {
				docService.deleteActiveDocument(oid);
			} else if (obj instanceof ActiveSet) {
				setService.deleteActiveSet(oid);
			} else if (obj instanceof ActiveOrder) {
				orderService.deleteActiveOrder(oid);
			}
			deleteRecycle(recycleOid);
		}
	}

	/**
	 * 删除现行文件信息。 删除现行文件主对象信息。
	 * 
	 * @param recycleOid
	 *            回收站OID
	 */
	private void deleteActiveDocument(String recycleOid) {
		String sql = " SELECT AD.* FROM ADM_ACTIVEDOCUMENT AD, ADM_ACTIVERECYCLE AR "
				+ "  WHERE AD.INNERID = AR.ITEMID AND AD.CLASSID = AR.ITEMCLASSID "
				+ "    AND AR.CLASSID || ':' ||AR.INNERID = ? ";
		List<ActiveDocument> adList = PersistHelper.getService().query(sql, ActiveDocument.class, recycleOid);
		for (ActiveDocument aDoc : adList) {
			Mastered master = aDoc.getMaster();
			Helper.getPersistService().delete(master);
			Helper.getPersistService().delete(aDoc);
		}
	}

	/**
	 * 删除回收站信息。
	 * 
	 * @param recycleOid
	 *            回收站OID
	 */
	private void deleteRecycle(String recycleOid) {
		Persistable recycle = Helper.getPersistService().getObject(recycleOid);
		Helper.getPersistService().delete(recycle);
	}
}
