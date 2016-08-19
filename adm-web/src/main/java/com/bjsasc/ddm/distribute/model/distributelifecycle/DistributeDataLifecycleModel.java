package com.bjsasc.ddm.distribute.model.distributelifecycle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发放管理对象生命周期模型
 * 
 * @author gaolingjie, 2013-3-7
 */
public class DistributeDataLifecycleModel implements Serializable {

	private static final long serialVersionUID = 6004343933171533233L;

	private final Map<String, DistributeLifecycleState> lifecycleStateMap = new HashMap<String, DistributeLifecycleState>();

	public DistributeDataLifecycleModel() {

	}

	public DistributeDataLifecycleModel(List<DistributeLifecycleState> stateList) {
		if (null != stateList) {
			for (DistributeLifecycleState state : stateList) {
				lifecycleStateMap.put(state.getName(), state);
			}
		}
	}

	/**
	 * 设置生命周期状态
	 * 
	 * @param stateList
	 */
	public void initLifecycleStates(List<DistributeLifecycleState> stateList) {
		clearDataModelMap();
		if (null != stateList) {
			for (DistributeLifecycleState state : stateList) {
				lifecycleStateMap.put(state.getName(), state);
			}
		}
	}

	/**
	 * 获得发放管理生命周期状态
	 * 
	 * @param stateName
	 *            生命周期状态名称
	 * @return 生命周期状态
	 */
	public DistributeLifecycleState getDdmLifeCycleStateByName(String stateName) {
		return lifecycleStateMap.get(stateName);
	}

	/**
	 * 清空数据
	 */
	private void clearDataModelMap() {
		if (!lifecycleStateMap.isEmpty()) {
			lifecycleStateMap.clear();
		}
	}

	@Override
	public String toString() {
		return "DistributeDataLifecycleModel [dataModelMap=" + lifecycleStateMap
				+ "]";
	}

}
