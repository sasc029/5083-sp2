package com.bjsasc.ddm.distribute.self.core;

import java.util.Map;

import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.rule.TypeBasedRule;
import com.bjsasc.plm.core.context.rule.algorithm.RuleItem;
import com.bjsasc.plm.core.context.rule.algorithm.RuleUtil;
import com.bjsasc.plm.core.persist.model.Persistable;

public class DdmRuleUtil {
	
	private DdmRuleUtil() {
		
	}
	
	/**
	 * 在目标对象上执行初始化规则
	 * 
	 * @param rule
	 * @param target
	 */
	public static void executeRule(TypeBasedRule rule, Persistable target,
			String typeId, Context context) {
		Map<String, RuleItem> ruleItems = RuleUtil.loadConfig(rule);
		for (RuleItem ruleItem : ruleItems.values()) {
			
			//ruleItem.execute(target, typeId, context);
			
			// gaolingjie modify
			new DdmRuleItemWapper(ruleItem).execute(target, typeId, context);
		}
	}
}
