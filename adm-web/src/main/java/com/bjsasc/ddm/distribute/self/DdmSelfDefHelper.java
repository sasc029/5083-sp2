package com.bjsasc.ddm.distribute.self;

import com.bjsasc.plm.core.context.rule.RuleService;
import com.bjsasc.plm.core.context.rule.algorithm.Algorithm;
import com.bjsasc.plm.core.context.template.TemplateService;
import com.bjsasc.plm.core.util.SpringUtil;

/**
 * 自定义服务类helper
 * 
 * @author gaolingjie, 2013-4-13
 */
public class DdmSelfDefHelper {
	
	private DdmSelfDefHelper() {
		
	}
	
	private static TemplateService templdateSerivce;
	
	private static Algorithm lifeCycleRuleImpl;
	
	private static RuleService ddmRuleServiceImpl;

	public synchronized static TemplateService getTemplateService() {
		if(null == templdateSerivce){
			templdateSerivce = (TemplateService) SpringUtil.getBean("ddm_template_ervice");
		}
		
		return templdateSerivce;
	}
	
	public synchronized static Algorithm getLifeCycleRuleImpl(){
		if(null == lifeCycleRuleImpl){
			lifeCycleRuleImpl = (Algorithm)SpringUtil.getBean("ddm_lifeCycle_rule");
		}
		return lifeCycleRuleImpl;
	}
	
	public synchronized static RuleService getRuleService(){
		if(null == ddmRuleServiceImpl){
			ddmRuleServiceImpl = (RuleService)SpringUtil.getBean("ddm_rule_service");
		}
		return ddmRuleServiceImpl;
	}
}
