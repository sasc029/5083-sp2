package com.bjsasc.adm.active.service.activecontext;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.bjsasc.adm.active.model.activecontext.ActiveContext;
import com.bjsasc.adm.common.ActiveCabinetUtil;
import com.bjsasc.adm.common.ConstUtil;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.context.model.AppContext;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.ContextHelper;
import com.bjsasc.plm.core.context.ContextServiceImpl;
import com.bjsasc.plm.core.context.model.OrgContext;
import com.bjsasc.plm.core.context.model.RootContext;
import com.bjsasc.plm.core.context.team.Team;
import com.bjsasc.plm.core.context.team.TeamHelper;
import com.bjsasc.plm.core.context.util.DomainUtil;
import com.bjsasc.plm.core.context.util.RoleUtil;
import com.bjsasc.plm.core.domain.Domain;
import com.bjsasc.plm.core.folder.Cabinet;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.access.acl.AclFolder;
import com.bjsasc.plm.core.system.access.acl.register.AclRegisterUtil;
import com.cascc.avidm.util.UUIDService;

/**
 * �����ļ�����ʵ���ࡣ
 * 
 * @author gengancong 2013-07-01
 */
public class ActiveContextServiceImpl extends ContextServiceImpl implements ActiveContextService {

	@Override
	public List<ActiveContext> getAllActiveContext() {
		String hql = "from ActiveContext ";

		List<ActiveContext> itemList = Helper.getPersistService().find(hql);

		return itemList;
	}

	public ActiveContext getActiveContext() {
		String hql = "from ActiveContext where number = ?";

		List<ActiveContext> itemList = Helper.getPersistService().find(hql, ConstUtil.ID_ACTIVE_CONTEXT);
		if (itemList == null || itemList.isEmpty()) {
			return createActiveContext();
		}
		return itemList.get(0);
	}

	@Override
	public ActiveContext newActiveContext() {
		ActiveContext activeContext = new ActiveContext();
		activeContext.setClassId(ActiveContext.CLASSID);
		activeContext.setInnerId(UUIDService.getUUID());
		return activeContext;
	}

	public AppContext createAppContext(AppContext context, Context parentContext, Team sharedTeam,
			boolean sharedTeamExtended, boolean isPrivate) {
		RootContext rootContext = (RootContext) getRootContext();

		boolean temp = SessionHelper.getService().isCheckPermission();
		SessionHelper.getService().setCheckPermission(false);

		String defaultDomainAclFolderName = "Default";

		//ȷ��default�ĸ���
		Domain defaultParentDomain = parentContext.getDomainOrg();
		if (parentContext instanceof RootContext) {
			defaultParentDomain = rootContext.getDomain();
		} else if (parentContext instanceof OrgContext) {
			OrgContext orgContext = (OrgContext) parentContext;
			if (sharedTeam != null) {
				//ʹ�ù����Ŷӣ�����Ϊ�����Ŷ�ͬ����
				defaultParentDomain = sharedTeam.getTeamDomain();
				defaultDomainAclFolderName = AclFolder.DOMAIN_DEFAULT_SHARED_TEAM;
			} else {
				if (isPrivate) {
					//ר�в�Ʒ������Ϊ��֯private��
					defaultParentDomain = orgContext.getDomainPrivate();
					defaultDomainAclFolderName = AclFolder.DOMAIN_DEFAULT_PRIVATE;
				} else {
					//�����Ʒ������Ϊ��֯PDM��
					if (orgContext.getDomainPDM() != null) {
						defaultParentDomain = orgContext.getDomainPDM();
					} else {
						defaultParentDomain = orgContext.getDomainDefault();
					}
				}
			}
		}

		//ȷ��system�ĸ���
		Domain systemParentDomain = parentContext.getDomainOrg();
		if (parentContext instanceof RootContext) {
			systemParentDomain = rootContext.getDomain();
		} else if (parentContext instanceof OrgContext) {
			OrgContext orgContext = (OrgContext) parentContext;
			systemParentDomain = orgContext.getDomainPrivate();
		}

		//1������������������
		Domain domainDefault = DomainUtil.createDomainDefault(context, defaultParentDomain);
		Domain domainSystem = DomainUtil.createDomainSystem(context, systemParentDomain);

		//2������������
		context.setIsPrivate(isPrivate);
		context.setContextInfo(parentContext.buildContextInfo());
		context.setDomainInfo(parentContext.getDomainSystem().buildDomainInfo());
		context.setDefaultDomainRef(Helper.getReference(domainDefault));
		context.setSystemDomainRef(Helper.getReference(domainSystem));

		if (sharedTeam != null) {
			//ʹ�ù����Ŷ�
			context.setSharedTeamRef(Helper.getReference(sharedTeam));
		}

		//SessionHelper.getService().setCheckPermission(temp);
		Helper.getPersistService().save(context);
		//SessionHelper.getService().setCheckPermission(false);

		//3�������ļ���
		Cabinet cabinetDefault = ActiveCabinetUtil.createCabinetDefault(context, domainDefault);
		Cabinet cabinetSystem = ActiveCabinetUtil.createCabinetSystem(context, domainSystem);

		context.setDefaultCabinetRef(Helper.getReference(cabinetDefault));
		context.setSystemCabinetRef(Helper.getReference(cabinetSystem));
		Helper.getPersistService().update(context);

		//4������ͬ�������Ŷ�
		if (sharedTeam == null || (sharedTeam != null && sharedTeamExtended)) {
			//��ʹ�ù����Ŷ�ʱ��ʹ�ù����Ŷӣ�����������չ�����Ŷ�
			Team localTeam = TeamHelper.getService().newTeam();
			localTeam.setNumber(context.getInnerId());
			localTeam.setName(context.getName());
			localTeam.setNote("�����Ŷ�");
			TeamHelper.getService().createTeam(localTeam, context);
			context.setLocalTeamRef(Helper.getReference(localTeam));
		}

		Helper.getPersistService().update(context);

		//5�����Ϊÿ����ע��Ĭ�ϵ�acl�б�		
		AclRegisterUtil.registerDomainDefaultAcls(domainDefault, defaultDomainAclFolderName);
		AclRegisterUtil.registerDomainDefaultAcls(domainSystem, domainSystem.getName());

		SessionHelper.getService().setCheckPermission(temp);

		return context;
	}

	//����������ܱ�����ִ�У�����ϵͳֻ����һ�������ļ�������
	@Override
	public synchronized ActiveContext createActiveContext() {

		RootContext parentContext = ContextHelper.getService().getRootContext();

		// �����������ݹ���Ա��ɫ
		RoleUtil.createRoles(parentContext);

		ActiveContext activeContext = newActiveContext();
		activeContext.setNumber(ConstUtil.ID_ACTIVE_CONTEXT);
		activeContext.setName(ConstUtil.NAME_ACTIVE_CONTEXT);
		activeContext.setNote(ConstUtil.NAME_ACTIVE_CONTEXT);
		ActiveContext currentContext = (ActiveContext) createAppContext(activeContext, parentContext, null, false,
				false);
		return currentContext;
	}

	public ActiveContext findActiveContext() {
		DetachedCriteria dc = DetachedCriteria.forClass(ActiveContext.class);

		@SuppressWarnings("unchecked")
		List<ActiveContext> list = PersistHelper.getService().findByCriteria(dc);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
}
