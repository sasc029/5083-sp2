package com.bjsasc.ddm.distribute.model.returnreason;

import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleInfo;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.type.ATObject;

/**
 * ����ԭ������ģ��
 * 
 * @author guowei 2013-3-15
 */
public class ReturnReason extends ATObject implements LifeCycleManaged {

	/** serialVersionUID */
	private static final long serialVersionUID = 5969583591399917316L;

	/** CLASSID */
	public static final String CLASSID = ReturnReason.class.getSimpleName();

	public ReturnReason() {

	}

	/** ��������������Ϣ���� */
	private LifeCycleInfo lifeCycleInfo = new LifeCycleInfo();

	public LifeCycleInfo getLifeCycleInfo() {
		return lifeCycleInfo;
	}

	public void setLifeCycleInfo(LifeCycleInfo lifeCycleInfo) {
		this.lifeCycleInfo = lifeCycleInfo;
	}

	/** ��� */
	private String number;
	
	/** ���� */
	private String name;
	
	/** �����ڲ���ʶ */
	private String objectId;

	/** �������ʶ */
	private String objectClassId;

	/** �����ڲ���ʶ */
	private String taskId;

	/** �������ʶ */
	private String taskClassId;

	/** ���˴��� */
	private long returnCount;

	/** ����ԭ�� */
	private String returnReason;
	
	/** �������ڲ���ʶ */
	private String userId;
	
	/** ���������ʶ */
	private String userClassId;
	
	/** ���������� */
	private String userName;

	/** ֽ��������� */
	private DistributePaperTask distributePaperTask;
	
	/** ���ŵ����� */
	private DistributeOrder distributeOrder;
	
	/** �ַ����� */
	private DistributeObject distributeObject;
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DistributeObject getDistributeObject() {
		return distributeObject;
	}

	public void setDistributeObject(DistributeObject distributeObject) {
		this.distributeObject = distributeObject;
	}

	public DistributeOrder getDistributeOrder() {
		return distributeOrder;
	}

	public void setDistributeOrder(DistributeOrder distributeOrder) {
		this.distributeOrder = distributeOrder;
	}

	public DistributePaperTask getDistributePaperTask() {
		return distributePaperTask;
	}

	public void setDistributePaperTask(DistributePaperTask distributePaperTask) {
		this.distributePaperTask = distributePaperTask;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectClassId() {
		return objectClassId;
	}

	public void setObjectClassId(String objectClassId) {
		this.objectClassId = objectClassId;
	}

	public long getReturnCount() {
		return returnCount;
	}

	public void setReturnCount(long returnCount) {
		this.returnCount = returnCount;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	/**
	 * @return taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId Ҫ���õ� taskId
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return taskClassId
	 */
	public String getTaskClassId() {
		return taskClassId;
	}

	/**
	 * @param taskClassId Ҫ���õ� taskClassId
	 */
	public void setTaskClassId(String taskClassId) {
		this.taskClassId = taskClassId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserClassId() {
		return userClassId;
	}

	public void setUserClassId(String userClassId) {
		this.userClassId = userClassId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
