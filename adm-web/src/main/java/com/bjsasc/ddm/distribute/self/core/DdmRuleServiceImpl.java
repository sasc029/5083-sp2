package com.bjsasc.ddm.distribute.self.core;

import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.rule.RuleServiceImpl;
import com.bjsasc.plm.core.context.rule.TypeBasedRule;
import com.bjsasc.plm.core.persist.model.Persistable;

public class DdmRuleServiceImpl extends RuleServiceImpl {
	public void init(Persistable target, Context context){
		
		String typeId = Helper.getTypeService().getTargetClassId(target.getClassId());
		
		TypeBasedRule rule = findRule(target.getClassId(), context);
		if(rule != null){
			//rule.execute(target, typeId, context);
			
			//gaolingjie modify
			new DdmTypeBasedRuleWapper(rule).execute(target, typeId, context);
		}else{
			throw new RuntimeException("未找到该对象类型绑定的初始化规则");
		}
	}
}
