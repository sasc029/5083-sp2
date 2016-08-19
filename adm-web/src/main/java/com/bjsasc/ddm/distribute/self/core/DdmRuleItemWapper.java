package com.bjsasc.ddm.distribute.self.core;

import com.bjsasc.ddm.distribute.self.DdmSelfDefHelper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.rule.algorithm.Algorithm;
import com.bjsasc.plm.core.context.rule.algorithm.RuleItem;
import com.bjsasc.plm.core.context.rule.algorithm.impl.LifeCycleRuleImpl;
import com.bjsasc.plm.core.persist.model.Persistable;

public class DdmRuleItemWapper {

	private final RuleItem ruleItem;
	
	public DdmRuleItemWapper(RuleItem ruleItem){
		this.ruleItem =  ruleItem;
	}
	/**
	 * ִ�й�����Ŀ��������������㷨��������
	 * 
	 * @param target
	 */
	public void execute(Persistable target, String typeId, Context context) {
		if (ruleItem.getAlgorithm() == null) {
			return;
		}

		Algorithm algorithmInstance = buildAlgorithm();
		if (algorithmInstance == null) {
			throw new RuntimeException("��ʼ�������㷨����ʧ�ܣ�ruleItem=" + this);
		} else {
			algorithmInstance.calculate(target, typeId, context,
					ruleItem.getArgs());
		}
	}

	/**
	 * �����㷨ʵ��
	 * 
	 * @return
	 */
	public Algorithm buildAlgorithm() {
		Algorithm ddmAlgorithm = ruleItem.buildAlgorithm();
		if (null != ddmAlgorithm && ddmAlgorithm instanceof LifeCycleRuleImpl) {
			ddmAlgorithm = DdmSelfDefHelper.getLifeCycleRuleImpl();
		}
		return ddmAlgorithm;
	}
}
