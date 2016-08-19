package com.bjsasc.adm.active.model.activelifecycle;

import java.io.Serializable;

/**
 * �������ݹ�����������״̬
 * 
 * @author yanjia, 2013-6-25
 */
public class ActiveLifecycleState implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -5208660099911431245L;

	/** ������������ */
	private String name;

	/** ��һ״̬�ڵ������ */
	private String nextStateName;

	/** ��һ״̬�ڵ������ */
	private String previousStateName;

	/** "�ܾ�"�����Ժ����������״̬���� */
	private String rejectiveStateName;

	public ActiveLifecycleState() {

	}

	public ActiveLifecycleState(String name, String nextStateName, String previousStateName, String rejectiveStateName) {
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

	/**
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ActiveLifecycleState [name=" + getName() + ",nextStateName=" + nextStateName + ", previousStateName="
				+ previousStateName + ", rejectiveStateName=" + rejectiveStateName + "]";
	}

}
