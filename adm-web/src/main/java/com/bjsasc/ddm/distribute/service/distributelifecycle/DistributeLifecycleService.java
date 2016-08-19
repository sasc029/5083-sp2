package com.bjsasc.ddm.distribute.service.distributelifecycle;

import com.bjsasc.platform.objectmodel.business.lifeCycle.State;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;

/**
 * 发放管理生命周期服务类接口
 * 
 * @author gaolingjie, 2013-2-28
 */
public interface DistributeLifecycleService {

	/**
	 * 初始上下文规则信息
	 * 
	 * @param target
	 */
	public void initLifecycle(LifeCycleManaged target);

	/**
	 * 更新生命周期状态
	 * 
	 * @param target
	 *            目标对象
	 * @param lifecycleState
	 *            生命周期状态
	 */
	public void updateLifeCycle(LifeCycleManaged target, State lifecycleState);

	/**
	 * 根据生命周期状态名称名称设置生命周期
	 * 
	 * @param target
	 *            目标对象
	 * @param stateName
	 *            生命周期状态名称
	 */
	public void updateLifeCycleByStateName(LifeCycleManaged target,
			String stateName);

	/**
	 * 根据生命周期状态id设置生命周期模板
	 * 
	 * @param target
	 *            目标对象
	 * @param stateId
	 *            生命周期id
	 */
	public void updateLifeCycleByStateId(LifeCycleManaged target, String stateId);

	/**
	 * 升级生命周期状态
	 * 
	 * @param target
	 *            目标对象
	 */
	public void promoteLifeCycle(LifeCycleManaged target);

	/**
	 * 降级生命周期状态
	 * 
	 * @param target
	 *            目标对象
	 */
	public void demoteLifeCycle(LifeCycleManaged target);

	/**
	 * 拒绝生命周期状态
	 * 
	 * @param target
	 *            目标对象
	 */
	public void rejectLifeCycle(LifeCycleManaged target);
	
	/**
	 * 根据生命周期状态id设置生命周期模板(只适用于分发数据生命周期联动情况)
	 * 
	 * @param target
	 *            目标对象
	 * @param stateId
	 *            生命周期id
	 */
	public void updateLifeCycleByStateIdNew(LifeCycleManaged target, String stateId);
}
