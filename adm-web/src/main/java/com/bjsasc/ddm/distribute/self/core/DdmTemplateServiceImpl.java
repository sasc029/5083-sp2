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
 * ���Ź���ģ���ȡ��������ģ���Զ����ࡣ�޸������£�1��������������������ģ��ķ�����2��ֻ�Ӹ����в�ѯ��������ģ��
 * 
 * @author gaolingjie, 2013-4-13
 */
public class DdmTemplateServiceImpl extends TemplateServiceImpl {

	@Override
	public List<LifeCycleTemplate> findLifeCycleTemplatesEnabled(
			Context context, String typeId) {
		// ��ȡ��������������
		List<Context> ancestorContexts = ContextHelper.getService()
				.getAncestorContexts(context);

		// ��ȡ�������ģ��������
		List<Integer> domainRefs = new ArrayList<Integer>();
		domainRefs.add(DomainHelper.getService().getRootDomain()
				.getAaDomainRef());
		
		//gaolingjie ע�� 
		//domainRefs.add(SessionHelper.getService().getDomainRef());
		
		for (Context c : ancestorContexts) {
			domainRefs.add(c.getDomainOrg().getAaDomainRef());
		}

		// ��ȡ�����Ϳɼ���ģ��
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
			// �жϸ�ģ���Ƿ�����
			if (masterX.isEnabled()) {
				templates.add(Helper.getLifeCycleService().getLastLifeCycleTemplate(masterX.getLifeCycleTemplate()));
			}
		}
		// gaolingjie modify 2013-03-13 end

		return templates;
	}

	/**
	 * ��ȡģ�Ͱ󶨵���������ģ�壬���������Ͱ󶨵���������ģ��
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
