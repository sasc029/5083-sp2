package com.bjsasc.adm.active.self.service;

import com.bjsasc.adm.active.self.helper.AdmSelfHelper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.rule.algorithm.Algorithm;
import com.bjsasc.plm.core.context.rule.algorithm.RuleItem;
import com.bjsasc.plm.core.context.rule.algorithm.impl.LifeCycleRuleImpl;
import com.bjsasc.plm.core.persist.model.Persistable;

/**
 * 对象绑定初始化规则
 * 
 * @author yanjia, 2013-6-25
 */
public class AdmRuleItemWapper {

	private final RuleItem ruleItem;

	public AdmRuleItemWapper(RuleItem ruleItem) {
		this.ruleItem = ruleItem;
	}

	/**
	 * 执行规则条目，具体就是启动算法进行运算
	 * 
	 * @param target
	 */
	public void execute(Persistable target, String typeId, Context context) {
		if (ruleItem.getAlgorithm() == null) {
			return;
		}

		Algorithm algorithmInstance = buildAlgorithm();
		if (algorithmInstance == null) {
			throw new RuntimeException("初始化规则算法生成失败，ruleItem=" + this);
		} else {
			algorithmInstance.calculate(target, typeId, context, ruleItem.getArgs());
		}
	}

	/**
	 * 构造算法实例
	 * 
	 * @return
	 */
	public Algorithm buildAlgorithm() {
		Algorithm admAlgorithm = ruleItem.buildAlgorithm();
		if (admAlgorithm instanceof LifeCycleRuleImpl && admAlgorithm != null) {
			admAlgorithm = AdmSelfHelper.getLifeCycleRuleImpl();
		}
		return admAlgorithm;
	}
}
