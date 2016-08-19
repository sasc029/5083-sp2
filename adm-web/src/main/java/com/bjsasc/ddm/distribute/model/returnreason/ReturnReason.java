package com.bjsasc.ddm.distribute.model.returnreason;

import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleInfo;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.type.ATObject;

/**
 * 回退原因数据模型
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

	/** 生命周期属性信息对象 */
	private LifeCycleInfo lifeCycleInfo = new LifeCycleInfo();

	public LifeCycleInfo getLifeCycleInfo() {
		return lifeCycleInfo;
	}

	public void setLifeCycleInfo(LifeCycleInfo lifeCycleInfo) {
		this.lifeCycleInfo = lifeCycleInfo;
	}

	/** 编号 */
	private String number;
	
	/** 名称 */
	private String name;
	
	/** 对象内部标识 */
	private String objectId;

	/** 对象类标识 */
	private String objectClassId;

	/** 任务内部标识 */
	private String taskId;

	/** 任务类标识 */
	private String taskClassId;

	/** 回退次数 */
	private long returnCount;

	/** 回退原因 */
	private String returnReason;
	
	/** 回退人内部标识 */
	private String userId;
	
	/** 回退人类标识 */
	private String userClassId;
	
	/** 回退人名称 */
	private String userName;

	/** 纸质任务对象 */
	private DistributePaperTask distributePaperTask;
	
	/** 发放单对象 */
	private DistributeOrder distributeOrder;
	
	/** 分发数据 */
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
	 * @param taskId 要设置的 taskId
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
	 * @param taskClassId 要设置的 taskClassId
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
