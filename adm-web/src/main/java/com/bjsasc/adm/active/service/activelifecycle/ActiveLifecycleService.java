package com.bjsasc.adm.active.service.activelifecycle;

import com.bjsasc.platform.objectmodel.business.lifeCycle.State;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;

/**
 * 现行数据管理生命周期服务类接口
 * 
 * @author yanjia, 2013-6-25
 */
public interface ActiveLifecycleService {

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
	public void updateLifeCycleByStateName(LifeCycleManaged target, String stateName);

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
}
