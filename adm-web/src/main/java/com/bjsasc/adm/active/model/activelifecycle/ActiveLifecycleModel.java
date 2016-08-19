package com.bjsasc.adm.active.model.activelifecycle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * �������ݹ�����������ģ��
 * 
 * @author yanjia, 2013-6-25
 */
public class ActiveLifecycleModel implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 2969918962086795765L;

	private final Map<String, ActiveLifecycleState> lifecycleStateMap = new HashMap<String, ActiveLifecycleState>();

	public ActiveLifecycleModel() {

	}

	public ActiveLifecycleModel(List<ActiveLifecycleState> stateList) {
		if (null != stateList) {
			for (ActiveLifecycleState state : stateList) {
				lifecycleStateMap.put(state.getName(), state);
			}
		}
	}

	/**
	 * ������������״̬
	 * 
	 * @param stateList
	 */
	public void initLifecycleStates(List<ActiveLifecycleState> stateList) {
		clearDataModelMap();
		if (null != stateList) {
			for (ActiveLifecycleState state : stateList) {
				lifecycleStateMap.put(state.getName(), state);
			}
		}
	}

	/**
	 * ����������ݹ�����������״̬
	 * 
	 * @param stateName
	 *            ��������״̬����
	 * @return ��������״̬
	 */
	public ActiveLifecycleState getActiveLifeCycleStateByName(String stateName) {
		return lifecycleStateMap.get(stateName);
	}

	/**
	 * �������
	 */
	private void clearDataModelMap() {
		if (!lifecycleStateMap.isEmpty()) {
			lifecycleStateMap.clear();
		}
	}

	@Override
	public String toString() {
		return "ActiveLifecycleModel [dataModelMap=" + lifecycleStateMap + "]";
	}

}
