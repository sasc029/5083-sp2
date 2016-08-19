package com.bjsasc.adm.active.self.service;

import java.util.List;
import java.util.ArrayList;

import com.bjsasc.adm.active.self.AdmSelfDefHelper;
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
 * �������ݹ���ģ���ȡ��������ģ���Զ����ࡣ
 * �޸����£�
 * 	   1��������������������ģ��ķ�����
 *     2��ֻ�Ӹ����в�ѯ��������ģ��
 * 
 * @author yanjia, 2013-6-25
 */
public class AdmTemplateServiceImpl extends TemplateServiceImpl {

	@Override
	public List<LifeCycleTemplate> findLifeCycleTemplatesEnabled(Context context, String typeId) {
		// ��ȡ��������������
		List<Context> ancestorContexts = ContextHelper.getService().getAncestorContexts(context);

		// ��ȡ�������ģ��������
		List<Integer> domainRefs = new ArrayList<Integer>();
		domainRefs.add(DomainHelper.getService().getRootDomain().getAaDomainRef());

		for (Context c : ancestorContexts) {
			domainRefs.add(c.getDomainOrg().getAaDomainRef());
		}

		// ��ȡ�����Ϳɼ���ģ��
		List<LifeCycleTemplate> typeMasters = new ArrayList<LifeCycleTemplate>();
		for (Integer domainRef : domainRefs) {
			typeMasters.addAll(findLifeCycleTemplateMasters(typeId, domainRef));
		}

		List<LifeCycleTemplate> templates = new ArrayList<LifeCycleTemplate>();

		List<LifeCycleTemplateMasterX> lifeCycleTemplateList = AdmSelfDefHelper.getTemplateService()
				.listContextTemplates(context, LifeCycleTemplateMasterX.class);

		for (LifeCycleTemplateMasterX masterX : lifeCycleTemplateList) {
			if (masterX == null) {
				continue;
			}
			// �жϸ�ģ���Ƿ�����
			if (masterX.isEnabled()) {
				templates.add(Helper.getLifeCycleService().getLastLifeCycleTemplate(masterX.getLifeCycleTemplate()));
			}
		}

		return templates;
	}

	/**
	 * ��ȡģ�Ͱ󶨵���������ģ�壬���������Ͱ󶨵���������ģ��
	 * 
	 * @param typeId
	 * @param domainRef
	 * @return List<LifeCycleTemplate>
	 */
	private List<LifeCycleTemplate> findLifeCycleTemplateMasters(String typeId, int domainRef) {
		List<LifeCycleTemplate> masters = new ArrayList<LifeCycleTemplate>();

		masters.addAll(Helper.getLifeCycleService().findCandidateTemplates(typeId, domainRef));

		List<TypeDefinition> types = TypeHelper.getService().getAncestors(typeId);
		for (TypeDefinition type : types) {
			masters.addAll(Helper.getLifeCycleService().findCandidateTemplates(type.getId(), domainRef));
		}

		return masters;
	}
}
