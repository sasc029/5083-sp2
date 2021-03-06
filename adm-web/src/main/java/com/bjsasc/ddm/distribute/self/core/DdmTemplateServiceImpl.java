package com.bjsasc.ddm.distribute.self.core;

import java.util.ArrayList;
import java.util.List;

import com.bjsasc.ddm.distribute.self.DdmSelfDefHelper;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleTemplate;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.ContextHelper;
import com.bjsasc.plm.core.context.template.LifeCycleTemplateMasterX;
import com.bjsasc.plm.core.context.template.TemplateServiceImpl;
import com.bjsasc.plm.core.domain.DomainHelper;
import com.bjsasc.plm.core.type.TypeDefinition;
import com.bjsasc.plm.core.type.TypeHelper;

/**
 * 发放管理模块获取生命周期模板自定义类。修改了如下：1、父类获得所有生命周期模板的方法。2、只从根域中查询生命周期模板
 * 
 * @author gaolingjie, 2013-4-13
 */
public class DdmTemplateServiceImpl extends TemplateServiceImpl {

	@Override
	public List<LifeCycleTemplate> findLifeCycleTemplatesEnabled(
			Context context, String typeId) {
		// 获取所有祖先上下文
		List<Context> ancestorContexts = ContextHelper.getService()
				.getAncestorContexts(context);

		// 获取所有相关模板隶属域
		List<Integer> domainRefs = new ArrayList<Integer>();
		domainRefs.add(DomainHelper.getService().getRootDomain()
				.getAaDomainRef());
		
		//gaolingjie 注释 
		//domainRefs.add(SessionHelper.getService().getDomainRef());
		
		for (Context c : ancestorContexts) {
			domainRefs.add(c.getDomainOrg().getAaDomainRef());
		}

		// 获取对类型可见的模板
		List<LifeCycleTemplate> typeMasters = new ArrayList<LifeCycleTemplate>();
		for (Integer domainRef : domainRefs) {
			typeMasters.addAll(findLifeCycleTemplateMasters(typeId, domainRef));
		}

		List<LifeCycleTemplate> templates = new ArrayList<LifeCycleTemplate>();

		// gaolingjie modify 2013-03-13
		List<LifeCycleTemplateMasterX> lifeCycleTemplateList = DdmSelfDefHelper
				.getTemplateService().listContextTemplates(context,
						LifeCycleTemplateMasterX.class);

		for (LifeCycleTemplateMasterX masterX : lifeCycleTemplateList) {
			// 判断该模板是否启用
			if (masterX.isEnabled()) {
				templates.add(Helper.getLifeCycleService().getLastLifeCycleTemplate(masterX.getLifeCycleTemplate()));
			}
		}
		// gaolingjie modify 2013-03-13 end

		return templates;
	}

	/**
	 * 获取模型绑定的生命周期模板，包括父类型绑定的生命周期模板
	 * 
	 * @author linjinzhi, 2012-9-10
	 * @param typeId
	 * @param domainRef
	 * @return
	 */
	private List<LifeCycleTemplate> findLifeCycleTemplateMasters(
			String typeId, int domainRef) {
		List<LifeCycleTemplate> masters = new ArrayList<LifeCycleTemplate>();

		masters.addAll(Helper.getLifeCycleService().findCandidateTemplates(typeId,
				domainRef));

		List<TypeDefinition> types = TypeHelper.getService().getAncestors(
				typeId);
		for (TypeDefinition type : types) {
			masters.addAll(Helper.getLifeCycleService().findCandidateTemplates(
					type.getId(), domainRef));
		}

		return masters;
	}
}
