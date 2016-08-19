package com.bjsasc.ddm.distribute.model.distributetaskdomainlink;

import com.bjsasc.plm.core.type.ATLink;

/**
 * 电子任务与外域分发信息link数据模型。
 * 
 * @author guowei 2013-10-15
 */
@SuppressWarnings("deprecation")
public class DistributeTaskDomainLink extends ATLink {

	/** serialVersionUID */
	private static final long serialVersionUID = -5214300708141803385L;

	/**
	 * 构造方法。
	 */
	public DistributeTaskDomainLink() {
		setClassId(CLASSID);
	}

	/** CLASSID */
	public static final String CLASSID = DistributeTaskDomainLink.class.getSimpleName();

	/** 任务类型（0：纸质任务，1：电子任务） */
	private String taskType;

	/**
	 * @return taskType
	 */
	public String getTaskType() {
		return taskType;
	}

	/**
	 * @param taskType 要设置的 taskType
	 */
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
}
