package com.bjsasc.ddm.distribute.self.core;

import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.rule.TypeBasedRule;
import com.bjsasc.plm.core.persist.model.Persistable;

public class DdmTypeBasedRuleWapper {
	private final TypeBasedRule typeBasedRule;
	
	public DdmTypeBasedRuleWapper(TypeBasedRule typeBasedRule){
		this.typeBasedRule = typeBasedRule;
	}
	
	/**
	 * 执行初始化规则
	 * @param target
	 */
	public void execute(Persistable target, String typeId, Context context){
		DdmRuleUtil.executeRule(typeBasedRule, target, typeId, context);
	}
}
