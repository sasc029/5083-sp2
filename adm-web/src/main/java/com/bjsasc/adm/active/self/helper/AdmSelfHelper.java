package com.bjsasc.adm.active.self.helper;

import com.bjsasc.plm.core.context.rule.RuleService;
import com.bjsasc.plm.core.context.rule.algorithm.Algorithm;
import com.bjsasc.plm.core.context.template.TemplateService;
import com.bjsasc.plm.core.util.SpringUtil;

/**
 * 现行数据管理自定义服务类helper
 * 
 * @author yanjia, 2013-6-25
 */
public class AdmSelfHelper {
	
	private AdmSelfHelper() {
		
	}
	
	/** 生命周期模板服务 */
	private static TemplateService templdateSerivce;
	/** 生命周期初始化规则服务 */
	private static Algorithm lifeCycleRuleImpl;
	/** 对象初始化规则服务 */
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
