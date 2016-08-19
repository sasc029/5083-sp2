package com.bjsasc.ddm.distribute.model.distributetaskinfolink;

import com.bjsasc.plm.core.type.ATLink;

/**
 * 任务与分发信息link数据模型。
 * 
 * @author gengancong 2013-2-22
 */
@SuppressWarnings("deprecation")
public class DistributeTaskInfoLink extends ATLink {

	/** serialVersionUID */
	private static final long serialVersionUID = 5414526469688358794L;

	/**
	 * 构造方法。
	 */
	public DistributeTaskInfoLink() {
		setClassId(CLASSID);
	}

	/** CLASSID */
	public static final String CLASSID = DistributeTaskInfoLink.class.getSimpleName();

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
