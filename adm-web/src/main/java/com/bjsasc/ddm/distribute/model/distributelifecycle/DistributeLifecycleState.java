package com.bjsasc.ddm.distribute.model.distributelifecycle;

import java.io.Serializable;

/**
 * 发放管理生命周期状态
 * 
 * @author gaolingjie, 2013-3-6
 */
public class DistributeLifecycleState implements Serializable{

	private static final long serialVersionUID = 8396886511912919238L;

	/**
	 * 生命周期名称
	 */
	private String name;
	
	/**
	 * 下一状态节点的名称
	 */
	private String nextStateName;

	/**
	 * 上一状态节点的id
	 */
	private String previousStateName;

	/**
	 * "拒绝"操作以后的生命周期状态id
	 */
	private String rejectiveStateName;

	public DistributeLifecycleState() {

	}
	
	public DistributeLifecycleState(String name, String nextStateName, String previousStateName,
			String rejectiveStateName) {
		super();
		this.setName(name);
		this.nextStateName = nextStateName;
		this.previousStateName = previousStateName;
		this.rejectiveStateName = rejectiveStateName;
	}

	/**
	 * 
	 * @return previousStateName
	 */
	public String getNextStateName() {
		return nextStateName;
	}

	/**
	 * 
	 * @param nextStateName
	 */
	public void setNextStateName(String nextStateName) {
		this.nextStateName = nextStateName;
	}

	/**
	 * 
	 * @return previousStateName
	 */
	public String getPreviousStateName() {
		return previousStateName;
	}

	/**
	 * 
	 * @param previousStateName
	 */
	public void setPreviousStateName(String previousStateName) {
		this.previousStateName = previousStateName;
	}

	/**
	 * 
	 * @return rejectiveStateName
	 */
	public String getRejectiveStateName() {
		return rejectiveStateName;
	}

	/**
	 * 
	 * @param rejectiveStateName
	 */
	public void setRejectiveStateName(String rejectiveStateName) {
		this.rejectiveStateName = rejectiveStateName;
	}
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "DistributeLifecycleState [name="
				+ getName() + ",nextStateName=" + nextStateName
				+ ", previousStateName=" + previousStateName
				+ ", rejectiveStateName=" + rejectiveStateName + "]";
	}

}
