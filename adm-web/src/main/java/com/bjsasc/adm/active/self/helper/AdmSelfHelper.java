package com.bjsasc.adm.active.self.helper;

import com.bjsasc.plm.core.context.rule.RuleService;
import com.bjsasc.plm.core.context.rule.algorithm.Algorithm;
import com.bjsasc.plm.core.context.template.TemplateService;
import com.bjsasc.plm.core.util.SpringUtil;

/**
 * �������ݹ����Զ��������helper
 * 
 * @author yanjia, 2013-6-25
 */
public class AdmSelfHelper {
	
	private AdmSelfHelper() {
		
	}
	
	/** ��������ģ����� */
	private static TemplateService templdateSerivce;
	/** �������ڳ�ʼ��������� */
	private static Algorithm lifeCycleRuleImpl;
	/** �����ʼ��������� */
	private static RuleService admRuleServiceImpl;

	public synchronized static TemplateService getTemplateService() {
		if(null == templdateSerivce){
			templdateSerivce = (TemplateService) SpringUtil.getBean("adm_template_service");
		}
		
		return templdateSerivce;
	}
	
	public synchronized static Algorithm getLifeCycleRuleImpl(){
		if(null == lifeCycleRuleImpl){
			lifeCycleRuleImpl = (Algorithm)SpringUtil.getBean("adm_lifeCycle_rule");
		}
		return lifeCycleRuleImpl;
	}
	
	public synchronized static RuleService getRuleService(){
		if(null == admRuleServiceImpl){
			admRuleServiceImpl = (RuleService)SpringUtil.getBean("adm_rule_service");
		}
		return admRuleServiceImpl;
	}
}
