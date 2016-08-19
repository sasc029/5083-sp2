package com.bjsasc.ddm.distribute.model.distributetasksite;

import com.bjsasc.plm.core.type.ATObject;

/**
 * 任务站点数据模型
 * 
 * @author guowei 2013-9-16
 */
public class DistributeTaskSite extends ATObject {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 7260347896163394967L;

	/** CLASSID */
	public static final String CLASSID = DistributeTaskSite.class.getSimpleName();

	public DistributeTaskSite() {

	}
	/** 任务内部标识 */
	private String taskId;
	
	/** 任务对象类标识 */
	private String taskClassId;
	
	/** 站点内部标识 */
	private String siteId;

	/** 站点对象类标识 */
	private String siteClassId;

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

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSiteClassId() {
		return siteClassId;
	}

	public void setSiteClassId(String siteClassId) {
		this.siteClassId = siteClassId;
	}
}
