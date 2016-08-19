package com.bjsasc.adm.active.self.service;

import java.util.Map;

import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.rule.RuleServiceImpl;
import com.bjsasc.plm.core.context.rule.TypeBasedRule;
import com.bjsasc.plm.core.context.rule.algorithm.RuleItem;
import com.bjsasc.plm.core.context.rule.algorithm.RuleUtil;
import com.bjsasc.plm.core.persist.model.Persistable;

/**
 * 对象绑定初始化规则
 * 
 * @author yanjia, 2013-6-25
 */
public class AdmRuleServiceImpl extends RuleServiceImpl {
	public void init(Persistable target, Context context) {
		String typeId = Helper.getTypeService().getTargetClassId(target.getClassId());
		TypeBasedRule rule = findRule(target.getClassId(), context);
		if (rule != null) {
			Map<String, RuleItem> ruleItems = RuleUtil.loadConfig(rule);
			for (RuleItem ruleItem : ruleItems.values()) {
				new AdmRuleItemWapper(ruleItem).execute(target, typeId, context);
			}
		} else {
			throw new RuntimeException("未找到该对象类型绑定的初始化规则");
		}
	}
}
