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
		//Ȩ�޼��
		AccessControlHelper.getService().checkEntityPermission(Operate.ACCESS, sub);
		AccessControlHelper.getService().checkEntityPermission(Operate.MODIFY, sub);
		AccessControlHelper.getService().checkEntityPermission(Operate.ACCESS, fd);
		AccessControlHelper.getService().checkEntityPermission(Operate.MODIFY, fd);
		//
		//��ȡsub��FolderLink
		FolderLink link = FolderHelper.getService().getParentFolderLink(sub);
		link.setParentFolder(fd);
		PersistHelper.getService().update(link);
		setFolderInfo((Foldered) sub, (AbstractFolder) fd);
		PersistHelper.getService().update(sub);
		// 1.����һ���ļ��еĸ���ϵ
		// 2.�����ļ��е�����Ϣ--������ļ����Ǽ̳еĸ���������λ�ü����̳��µĸ�������ǵ�������������ֵ����򣭣��ɹ滮zhangmeiȷ�ϡ�
		// �����жϷ���Ӧ�ò㣬����Ĭ���Ѿ����չ�������Ϣ���ø��˸��ļ���
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
		// �����ļ�����ҵ�������ƶ��߼�

		LOGGER.debug("exiting moveSubFolder(SubFolder,Folder).sub.innerid=:" + sub.getInnerId() + ",fd.innerid:="
				+ fd.getInnerId());

	}

	private void setFolderInfo(Foldered obj, AbstractFolder folder) {
		// ���¶���������Ϣ
		ObjectReference ref = ObjectReference.newObjectReference(folder);
		FolderInfo info = new FolderInfo();
		info.setFolderRef(ref);
		info.setFolderName(folder.getName());
		obj.setFolderInfo(info);
	}

	private void setContextInfo(Foldered obj, AbstractFolder folder) {
		// ���¶�����������Ϣ
		if (obj instanceof Contexted) {
			if (((AbstractFolder) folder).getContextInfo() == null) {
				return;
			}
			Context context = ((AbstractFolder) folder).getContextInfo().getContext();

			ContextHelper.getService().addToContext((Contexted) obj, context);
		}
	}

	private void setActiveDocument(Foldered obj, AbstractFolder folder) {
		// ��������
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
		// ���¶�������Ϣ
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

	//�ƶ��ļ���ʱ�ݹ�����ļ��������ļ�����
	private void updateSubFolderActiveDocument(SubFolder sub, List<SubFolder> folderlist, List<Persistable> objs) {

		///////
		//��ȡ���ļ��е����ļ���
		List<FolderLink> childfolders = FolderHelper.getService().getChildFolderLinks(sub);
		for (FolderLink link : childfolders) {
			SubFolder subfolder = (SubFolder) link.getChildFolder();
			updateSubFolderActiveDocument(subfolder, folderlist, objs);
		}
		//3.�����ļ����ڶ��������Ϣ
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

					// ���¶���������Ϣ-�����ļ�����
					if (iteratedfoldered instanceof ActiveDocument) {
						setActiveDocument(iteratedfoldered, (AbstractFolder) sub);
					}
					PersistHelper.getService().update(iteratedfoldered);
				}
			} else {
				// ���¶���������Ϣ-�����ļ�����
				if (obj instanceof ActiveDocument) {
					setActiveDocument((Foldered) obj, (AbstractFolder) sub);
				}
				PersistHelper.getService().update(obj);
			}
		}
	}

	//�ƶ��ļ���ʱ�ݹ�����ļ��е���/������/�ƶ���Ϣ
	private void updateSubFolderDomain(SubFolder sub, Domain domain, List<SubFolder> folderlist, List<Persistable> objs) {

		//Ȩ����֤
		AccessControlHelper.getService().checkEntityPermission(Operate.ACCESS, sub);
		AccessControlHelper.getService().checkEntityPermission(Operate.MODIFY, sub);
		AccessControlHelper.getService().checkEntityPermission(Operate.CHANGE_DOMAIN, sub);
		checkMoveInDomain(sub, domain);
		///////
		//��ȡ���ļ��е����ļ���
		List<FolderLink> childfolders = FolderHelper.getService().getChildFolderLinks(sub);
		for (FolderLink link : childfolders) {
			SubFolder subfolder = (SubFolder) link.getChildFolder();
			if (subfolder.getDomaintype().equals(SubFolder.DOMAINTYPE_EXTENDS)) {//�̳и���
				updateSubFolderDomain(subfolder, domain, folderlist, objs);
			}
		}

		//DomainHelper.getService().addToDomain(sub, domain);

		sub.setDomainInfo(domain.buildDomainInfo());

		PersistHelper.getService().update(sub);
		//3.�����ļ����ڶ��������Ϣ
		List<FolderMemberLink> objlist = FolderHelper.getService().getFolderMemberLinks(sub);
		for (FolderMemberLink objlink : objlist) {
			Persistable obj = (Persistable) objlink.getFoldered();
			if (obj == null) {
				continue;
			}
			if (objlink.getObjType() == FolderMemberLink.OBJTYPE_VERSION_Y) {
				if (folderlist != null) {
					MoveHelper.getService().moveObjInFolders(folderlist, obj, objs);//�ƶ�ҵ�����
				}
				List<Iterated> iterationlist = VersionControlHelper.getService().iterationsOf((ControlBranch) obj);
				for (Iterated iterated : iterationlist) {
					IterationFoldered iteratedfoldered = (IterationFoldered) iterated;
					//������������Ϣ
					if (iteratedfoldered instanceof Contexted) {
						setContextInfo(iteratedfoldered, (AbstractFolder) sub);
					}
					// ���¶���������Ϣ-����Ϣ
					if (iteratedfoldered instanceof Domained) {
						setDomainInfo(iteratedfoldered, (AbstractFolder) sub);
					}
					PersistHelper.getService().update(iteratedfoldered);
				}
			} else {
				//������������Ϣ
				if (obj instanceof Contexted) {
					setContextInfo((Foldered) obj, (AbstractFolder) sub);
				}
				// ���¶���������Ϣ-����Ϣ
				if (obj instanceof Domained) {
					setDomainInfo((Foldered) obj, (AbstractFolder) sub);
				}
				PersistHelper.getService().update(obj);
			}
		}
	}

}
