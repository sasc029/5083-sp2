package com.bjsasc.adm.active.self.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bjsasc.adm.active.self.helper.AdmSelfHelper;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleTemplate;
import com.bjsasc.platform.objectmodel.business.lifeCycle.State;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.rule.algorithm.impl.LifeCycleRuleImpl;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.util.LogUtil;
import com.cascc.avidm.util.UUIDService;

/**
 * ����󶨳�ʼ������
 * 
 * @author yanjia, 2013-6-25
 */
public class AdmLifeCycleRuleImpl extends LifeCycleRuleImpl {
	
	private static final Logger LOG = Logger.getLogger(LifeCycleRuleImpl.class);

	public static final String ARG_TEMPLATE_NAME = LifeCycleRuleImpl.ARG_TEMPLATE_NAME;

	public static final String ARG_STATE_NAME = LifeCycleRuleImpl.ARG_STATE_NAME;

	public void calculate(Persistable target, String typeId, Context context, Map<String, String> args) {
		LOG.debug(LogUtil.beginMethod("����������������ģ��", "Ŀ�����=" + target, "typeId=" + typeId, "context=" + context,
				"args=" + args));

		String templateName = args.get(ARG_TEMPLATE_NAME);
		String stateName = args.get(ARG_STATE_NAME);

		LOG.debug("��ʼ������ָ������������ģ��=" + templateName + ",״̬=" + stateName);

		if (!(target instanceof LifeCycleManaged)) {
			LOG.error("����û��ʵ�ֽӿ�LifeCycleManaged");
			return;
		}

		LifeCycleManaged lifeCycleManaged = (LifeCycleManaged) target;

		LifeCycleTemplate lifeCycleTemplate = null;

		//�޸Ĵ˴�  ���Զ���ķ����л����������ģ��
		List<LifeCycleTemplate> templates = AdmSelfHelper.getTemplateService().findLifeCycleTemplatesEnabled(context,
				target.getClassId());
		if (templates.isEmpty()) {
			throw new RuntimeException("δ�ҵ��ö������Ͱ󶨵��������õ���������ģ��");
		}

		for (LifeCycleTemplate template : templates) {
			if (template.getName().equals(templateName)) {
				lifeCycleTemplate = template;
				break;
			}
		}

		if (lifeCycleTemplate == null) {
			throw new RuntimeException("�ڶ���󶨵���������ģ����,δ�ҵ���ʼ������ָ����ģ��:" + templateName);
		}

		State state = null;
		if (stateName != null) {
			// ��Ŀ��ģ�����Ҹ�����״̬
			List<State> templateStates = Helper.getLifeCycleService().findStates(lifeCycleTemplate);

			for (State s : templateStates) {
				if (s.getName().equals(stateName)) {
					state = s;
					break;
				}
			}
		}

		if (target.getInnerId() == null) {
			target.setInnerId(UUIDService.getUUID());
		}

		boolean temp = SessionHelper.getService().isCheckPermission();
		SessionHelper.getService().setCheckPermission(false);

		if (state != null) {
			Helper.getLifeCycleService().setLifeCycleTemplate(lifeCycleManaged, lifeCycleTemplate, state);
		} else {
			Helper.getLifeCycleService().setLifeCycleTemplate(lifeCycleManaged, lifeCycleTemplate);
		}

		SessionHelper.getService().setCheckPermission(temp);
	}
}
