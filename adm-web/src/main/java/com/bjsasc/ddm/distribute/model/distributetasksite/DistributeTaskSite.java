package com.bjsasc.ddm.distribute.model.distributetasksite;

import com.bjsasc.plm.core.type.ATObject;

/**
 * ����վ������ģ��
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
	/** �����ڲ���ʶ */
	private String taskId;
	
	/** ����������ʶ */
	private String taskClassId;
	
	/** վ���ڲ���ʶ */
	private String siteId;

	/** վ��������ʶ */
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
