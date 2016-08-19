package com.bjsasc.adm.active.self.service;

import com.bjsasc.adm.active.self.helper.AdmSelfHelper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.rule.algorithm.Algorithm;
import com.bjsasc.plm.core.context.rule.algorithm.RuleItem;
import com.bjsasc.plm.core.context.rule.algorithm.impl.LifeCycleRuleImpl;
import com.bjsasc.plm.core.persist.model.Persistable;

/**
 * ����󶨳�ʼ������
 * 
 * @author yanjia, 2013-6-25
 */
public class AdmRuleItemWapper {

	private final RuleItem ruleItem;

	public AdmRuleItemWapper(RuleItem ruleItem) {
		this.ruleItem = ruleItem;
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
			algorithmInstance.calculate(target, typeId, context, ruleItem.getArgs());
		}
	}

	/**
	 * �����㷨ʵ��
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
