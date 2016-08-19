package com.bjsasc.adm.active.service.activefolder;

import java.util.List;

import org.apache.log4j.Logger;

import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.model.activedocument.ActiveDocument;
import com.bjsasc.adm.active.service.admmodelservice.AdmModelService;
import com.bjsasc.platform.objectmodel.business.persist.ObjectReference;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.common.move.MoveHelper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.ContextHelper;
import com.bjsasc.plm.core.context.model.Contexted;
import com.bjsasc.plm.core.domain.Domain;
import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.folder.AbstractFolder;
import com.bjsasc.plm.core.folder.Folder;
import com.bjsasc.plm.core.folder.FolderHelper;
import com.bjsasc.plm.core.folder.FolderInfo;
import com.bjsasc.plm.core.folder.FolderLink;
import com.bjsasc.plm.core.folder.FolderMemberLink;
import com.bjsasc.plm.core.folder.Foldered;
import com.bjsasc.plm.core.folder.IterationFoldered;
import com.bjsasc.plm.core.folder.SubFolder;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.system.access.AccessControlHelper;
import com.bjsasc.plm.core.system.access.operate.Operate;
import com.bjsasc.plm.core.vc.model.ControlBranch;
import com.bjsasc.plm.core.vc.model.Iterated;
import com.bjsasc.plm.core.vc.VersionControlHelper;
import com.bjsasc.plm.folder.FolderManagerServiceImpl;

public class ActiveSubFolderManagerServiceImpl extends FolderManagerServiceImpl {
	private static final Logger LOGGER = Logger.getLogger(ActiveSubFolderManagerServiceImpl.class);

	public void moveSubFolder(SubFolder sub, Folder fd, List<SubFolder> list, List<Persistable> objs) {
		LOGGER.debug("entering moveSubFolder(SubFolder,Folder).sub.innerid=:" + sub.getInnerId() + ",fd.innerid:="
				+ fd.getInnerId());
		//权限检查
		AccessControlHelper.getService().checkEntityPermission(Operate.ACCESS, sub);
		AccessControlHelper.getService().checkEntityPermission(Operate.MODIFY, sub);
		AccessControlHelper.getService().checkEntityPermission(Operate.ACCESS, fd);
		AccessControlHelper.getService().checkEntityPermission(Operate.MODIFY, fd);
		//
		//获取sub的FolderLink
		FolderLink link = FolderHelper.getService().getParentFolderLink(sub);
		link.setParentFolder(fd);
		PersistHelper.getService().update(link);
		setFolderInfo((Foldered) sub, (AbstractFolder) fd);
		PersistHelper.getService().update(sub);
		// 1.更新一个文件夹的父关系
		// 2.更新文件夹的域信息--如果该文件夹是继承的父域，则在新位置继续继承新的父域，如果是单独域，则继续保持单独域－－由规划zhangmei确认。
		// 以上判断放在应用层，这里默认已经按照规则将域信息设置给了该文件夹
		// PersistHelper.getService().update(sub);
		if (sub.getDomaintype().equals(SubFolder.DOMAINTYPE_EXTENDS)) {
			AbstractFolder targetFolder = (AbstractFolder) fd;
			if (!sub.getDomainInfo().getDomain().getInnerId()
					.equals(targetFolder.getDomainInfo().getDomain().getInnerId())) {
				AccessControlHelper.getService().checkEntityPermission(Operate.CHANGE_DOMAIN, sub);
				Domain domain = targetFolder.getDomainInfo().getDomain();
				updateSubFolderDomain(sub, domain, list, objs);
			}
		}

		updateSubFolderActiveDocument(sub, list, objs);
		// 处理文件夹内业务对象的移动逻辑

		LOGGER.debug("exiting moveSubFolder(SubFolder,Folder).sub.innerid=:" + sub.getInnerId() + ",fd.innerid:="
				+ fd.getInnerId());

	}

	private void setFolderInfo(Foldered obj, AbstractFolder folder) {
		// 更新对象属性信息
		ObjectReference ref = ObjectReference.newObjectReference(folder);
		FolderInfo info = new FolderInfo();
		info.setFolderRef(ref);
		info.setFolderName(folder.getName());
		obj.setFolderInfo(info);
	}

	private void setContextInfo(Foldered obj, AbstractFolder folder) {
		// 更新对象上下文信息
		if (obj instanceof Contexted) {
			if (((AbstractFolder) folder).getContextInfo() == null) {
				return;
			}
			Context context = ((AbstractFolder) folder).getContextInfo().getContext();

			ContextHelper.getService().addToContext((Contexted) obj, context);
		}
	}

	private void setActiveDocument(Foldered obj, AbstractFolder folder) {
		// 设置类型
		if (obj instanceof ActiveDocument) {
			AdmModelService admModelService = AdmHelper.getAdmModelService();
			String folderOid = Helper.getOid(folder);
			String modelFolderOid = admModelService.getModelFolder(folderOid);
			String modelId = admModelService.getModelId(modelFolderOid);
			if (modelId != null && modelId.length() > 0) {
				obj.setClassId(modelId);
			}
		}
	}

	private void setDomainInfo(Foldered obj, AbstractFolder folder) {
		// 更新对象域信息
		if (obj instanceof Domained) {
			Domain domain = ((AbstractFolder) folder).getDomainInfo().getDomain();
			Domained domained = (Domained) obj;
			if (domained.getDomainInfo() != null && domained.getDomainInfo().getDomain() != null) {
				if (!domained.getDomainInfo().getDomain().getInnerId().equals(domain.getInnerId())) {
					AccessControlHelper.getService().checkEntityPermission(Operate.CHANGE_DOMAIN, obj);
					Helper.getDomainService().addToDomain(domained, domain);
					AccessControlHelper.getService().checkEntityPermission(Operate.MOVE_INTO_DOMAIN, obj);
				}
			} else {
				Helper.getDomainService().addToDomain(domained, domain);
			}

		}
	}

	//移动文件夹时递归更新文件的现行文件类型
	private void updateSubFolderActiveDocument(SubFolder sub, List<SubFolder> folderlist, List<Persistable> objs) {

		///////
		//获取该文件夹的子文件夹
		List<FolderLink> childfolders = FolderHelper.getService().getChildFolderLinks(sub);
		for (FolderLink link : childfolders) {
			SubFolder subfolder = (SubFolder) link.getChildFolder();
			updateSubFolderActiveDocument(subfolder, folderlist, objs);
		}
		//3.更新文件夹内对象的域信息
		List<FolderMemberLink> objlist = FolderHelper.getService().getFolderMemberLinks(sub);
		for (FolderMemberLink objlink : objlist) {
			Persistable obj = (Persistable) objlink.getFoldered();
			if (obj == null) {
				continue;
			}
			if (objlink.getObjType() == FolderMemberLink.OBJTYPE_VERSION_Y) {

				List<Iterated> iterationlist = VersionControlHelper.getService().iterationsOf((ControlBranch) obj);
				for (Iterated iterated : iterationlist) {
					IterationFoldered iteratedfoldered = (IterationFoldered) iterated;

					// 更新对象属性信息-现行文件类型
					if (iteratedfoldered instanceof ActiveDocument) {
						setActiveDocument(iteratedfoldered, (AbstractFolder) sub);
					}
					PersistHelper.getService().update(iteratedfoldered);
				}
			} else {
				// 更新对象属性信息-现行文件类型
				if (obj instanceof ActiveDocument) {
					setActiveDocument((Foldered) obj, (AbstractFolder) sub);
				}
				PersistHelper.getService().update(obj);
			}
		}
	}

	//移动文件夹时递归更新文件夹的域/上下文/移动信息
	private void updateSubFolderDomain(SubFolder sub, Domain domain, List<SubFolder> folderlist, List<Persistable> objs) {

		//权限验证
		AccessControlHelper.getService().checkEntityPermission(Operate.ACCESS, sub);
		AccessControlHelper.getService().checkEntityPermission(Operate.MODIFY, sub);
		AccessControlHelper.getService().checkEntityPermission(Operate.CHANGE_DOMAIN, sub);
		checkMoveInDomain(sub, domain);
		///////
		//获取该文件夹的子文件夹
		List<FolderLink> childfolders = FolderHelper.getService().getChildFolderLinks(sub);
		for (FolderLink link : childfolders) {
			SubFolder subfolder = (SubFolder) link.getChildFolder();
			if (subfolder.getDomaintype().equals(SubFolder.DOMAINTYPE_EXTENDS)) {//继承父域
				updateSubFolderDomain(subfolder, domain, folderlist, objs);
			}
		}

		//DomainHelper.getService().addToDomain(sub, domain);

		sub.setDomainInfo(domain.buildDomainInfo());

		PersistHelper.getService().update(sub);
		//3.更新文件夹内对象的域信息
		List<FolderMemberLink> objlist = FolderHelper.getService().getFolderMemberLinks(sub);
		for (FolderMemberLink objlink : objlist) {
			Persistable obj = (Persistable) objlink.getFoldered();
			if (obj == null) {
				continue;
			}
			if (objlink.getObjType() == FolderMemberLink.OBJTYPE_VERSION_Y) {
				if (folderlist != null) {
					MoveHelper.getService().moveObjInFolders(folderlist, obj, objs);//移动业务对象
				}
				List<Iterated> iterationlist = VersionControlHelper.getService().iterationsOf((ControlBranch) obj);
				for (Iterated iterated : iterationlist) {
					IterationFoldered iteratedfoldered = (IterationFoldered) iterated;
					//更新上下文信息
					if (iteratedfoldered instanceof Contexted) {
						setContextInfo(iteratedfoldered, (AbstractFolder) sub);
					}
					// 更新对象属性信息-域信息
					if (iteratedfoldered instanceof Domained) {
						setDomainInfo(iteratedfoldered, (AbstractFolder) sub);
					}
					PersistHelper.getService().update(iteratedfoldered);
				}
			} else {
				//更新上下文信息
				if (obj instanceof Contexted) {
					setContextInfo((Foldered) obj, (AbstractFolder) sub);
				}
				// 更新对象属性信息-域信息
				if (obj instanceof Domained) {
					setDomainInfo((Foldered) obj, (AbstractFolder) sub);
				}
				PersistHelper.getService().update(obj);
			}
		}
	}

}
