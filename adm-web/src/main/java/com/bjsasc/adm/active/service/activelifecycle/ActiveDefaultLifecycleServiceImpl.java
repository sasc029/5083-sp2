package com.bjsasc.adm.active.service.activelifecycle;

import java.util.List;

import com.bjsasc.adm.active.self.helper.AdmSelfHelper;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleTemplate;
import com.bjsasc.platform.objectmodel.business.lifeCycle.State;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.ContextHelper;
import com.bjsasc.plm.core.lifecycle.LifeCycleHelper;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.util.PlmException;
/**
 * 生命周期管理服务类（该类是默认实现，工作流状态按照工作流模板一步一步往下走，不存在跳跃设置生命周期状态的情况）
 * 
 * @author yanjia, 2013-6-25
 */
public class ActiveDefaultLifecycleServiceImpl implements ActiveLifecycleService {

	@Override
	public void initLifecycle(LifeCycleManaged target) {
		Context rootContext = ContextHelper.getService().getRootContext();
		AdmSelfHelper.getRuleService().init((Persistable) target, rootContext);
	}

	@Override
	public void updateLifeCycle(LifeCycleManaged target, State lifecycleState) {
		LifeCycleHelper.getService().setLifeCycleState(target, lifecycleState);
	}

	@Override
	public void updateLifeCycleByStateName(LifeCycleManaged target, String stateName) {
		// 获取生命周期模板
		LifeCycleTemplate lifeCycleTemplate = LifeCycleHelper.getService().getLifeCycleTemplate(target);

		List<State> stateList = LifeCycleHelper.getService().findStates(lifeCycleTemplate);
		State lifecycleState = null;
		for (State state : stateList) {
			if ((state.getName()).equals(stateName)) {
				lifecycleState = state;
			}
		}
		if (null == lifecycleState) {
			throw new PlmException("没有找到名称为：" + stateName + "的生命周期状态,根据生命周期名称设置生命周期模板失败！");
		}

		this.updateLifeCycle(target, lifecycleState);
	}

	@Override
	public void updateLifeCycleByStateId(LifeCycleManaged target, String stateId) {
		// 获取生命周期模板
		LifeCycleTemplate lifeCycleTemplate = LifeCycleHelper.getService().getLifeCycleTemplate(target);

		List<State> stateList = LifeCycleHelper.getService().findStates(lifeCycleTemplate);
		State lifecycleState = null;
		for (State state : stateList) {
			if ((state.getId()).equals(stateId)) {
				lifecycleState = state;
			}
		}
		if (null == lifecycleState) {
			throw new PlmException("没有找到id为：" + stateId + "的生命周期模板,根据生命周期id设置生命周期模板失败！");
		}

		this.updateLifeCycle(target, lifecycleState);
	}

	@Override
	public void promoteLifeCycle(LifeCycleManaged target) {
		LifeCycleHelper.getService().promote(target);
	}

	@Override
	public void demoteLifeCycle(LifeCycleManaged target) {
		LifeCycleHelper.getService().demote(target);
	}

	@Override
	public void rejectLifeCycle(LifeCycleManaged target) {

	}
}
