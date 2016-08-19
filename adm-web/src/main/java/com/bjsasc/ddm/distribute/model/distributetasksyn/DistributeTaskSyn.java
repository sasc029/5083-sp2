package com.bjsasc.ddm.distribute.model.distributetasksyn;

import com.bjsasc.plm.core.type.ATObject;

/**
 * 任务同步数据模型
 * 
 * @author guowei 2013-9-16
 */
public class DistributeTaskSyn extends ATObject {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 7094873539305446912L;

	/** CLASSID */
	public static final String CLASSID = DistributeTaskSyn.class.getSimpleName();

	public DistributeTaskSyn() {

	}
	/** 任务内部标识 */
	private String taskId;
	
	/** 任务对象类标识 */
	private String taskClassId;
	
	/** 同步任务内部标识 */
	private String taskSynId;

	/** 同步任务对象类标识 */
	private String taskSynClassId;
	
	/** 站点类型（0：中心，1：接收方） */
	private String siteType;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskClassId() {
		return taskClassId;
	}

	public void setTaskClassId(String taskClassId) {
		this.taskClassId = taskClassId;
	}

	public String getTaskSynId() {
		return taskSynId;
	}

	public void setTaskSynId(String taskSynId) {
		this.taskSynId = taskSynId;
	}

	public String getTaskSynClassId() {
		return taskSynClassId;
	}

	public void setTaskSynClassId(String taskSynClassId) {
		this.taskSynClassId = taskSynClassId;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
}
