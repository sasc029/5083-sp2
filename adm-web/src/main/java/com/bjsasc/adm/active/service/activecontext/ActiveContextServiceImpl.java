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
 * 现行文件服务实现类。
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

		//确定default的父域
		Domain defaultParentDomain = parentContext.getDomainOrg();
		if (parentContext instanceof RootContext) {
			defaultParentDomain = rootContext.getDomain();
		} else if (parentContext instanceof OrgContext) {
			OrgContext orgContext = (OrgContext) parentContext;
			if (sharedTeam != null) {
				//使用共享团队，父域为共享团队同名域
				defaultParentDomain = sharedTeam.getTeamDomain();
				defaultDomainAclFolderName = AclFolder.DOMAIN_DEFAULT_SHARED_TEAM;
			} else {
				if (isPrivate) {
					//专有产品，父域为组织private域
					defaultParentDomain = orgContext.getDomainPrivate();
					defaultDomainAclFolderName = AclFolder.DOMAIN_DEFAULT_PRIVATE;
				} else {
					//共享产品，父域为组织PDM域
					if (orgContext.getDomainPDM() != null) {
						defaultParentDomain = orgContext.getDomainPDM();
					} else {
						defaultParentDomain = orgContext.getDomainDefault();
					}
				}
			}
		}

		//确定system的父域
		Domain systemParentDomain = parentContext.getDomainOrg();
		if (parentContext instanceof RootContext) {
			systemParentDomain = rootContext.getDomain();
		} else if (parentContext instanceof OrgContext) {
			OrgContext orgContext = (OrgContext) parentContext;
			systemParentDomain = orgContext.getDomainPrivate();
		}

		//1、创建上下文隶属域
		Domain domainDefault = DomainUtil.createDomainDefault(context, defaultParentDomain);
		Domain domainSystem = DomainUtil.createDomainSystem(context, systemParentDomain);

		//2、创建上下文
		context.setIsPrivate(isPrivate);
		context.setContextInfo(parentContext.buildContextInfo());
		context.setDomainInfo(parentContext.getDomainSystem().buildDomainInfo());
		context.setDefaultDomainRef(Helper.getReference(domainDefault));
		context.setSystemDomainRef(Helper.getReference(domainSystem));

		if (sharedTeam != null) {
			//使用共享团队
			context.setSharedTeamRef(Helper.getReference(sharedTeam));
		}

		//SessionHelper.getService().setCheckPermission(temp);
		Helper.getPersistService().save(context);
		//SessionHelper.getService().setCheckPermission(false);

		//3、创建文件柜
		Cabinet cabinetDefault = ActiveCabinetUtil.createCabinetDefault(context, domainDefault);
		Cabinet cabinetSystem = ActiveCabinetUtil.createCabinetSystem(context, domainSystem);

		context.setDefaultCabinetRef(Helper.getReference(cabinetDefault));
		context.setSystemCabinetRef(Helper.getReference(cabinetSystem));
		Helper.getPersistService().update(context);

		//4、创建同名本地团队
		if (sharedTeam == null || (sharedTeam != null && sharedTeamExtended)) {
			//不使用共享团队时；使用共享团队，并且允许扩展共享团队
			Team localTeam = TeamHelper.getService().newTeam();
			localTeam.setNumber(context.getInnerId());
			localTeam.setName(context.getName());
			localTeam.setNote("本地团队");
			TeamHelper.getService().createTeam(localTeam, context);
			context.setLocalTeamRef(Helper.getReference(localTeam));
		}

		Helper.getPersistService().update(context);

		//5、最后为每个域注册默认的acl列表		
		AclRegisterUtil.registerDomainDefaultAcls(domainDefault, defaultDomainAclFolderName);
		AclRegisterUtil.registerDomainDefaultAcls(domainSystem, domainSystem.getName());

		SessionHelper.getService().setCheckPermission(temp);

		return context;
	}

	//这个方法不能被并行执行，整个系统只能有一个现行文件上下文
	@Override
	public synchronized ActiveContext createActiveContext() {

		RootContext parentContext = ContextHelper.getService().getRootContext();

		// 创建现行数据管理员角色
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
