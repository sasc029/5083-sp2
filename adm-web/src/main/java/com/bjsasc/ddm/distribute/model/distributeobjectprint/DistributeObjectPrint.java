package com.bjsasc.ddm.distribute.model.distributeobjectprint;

import com.bjsasc.plm.core.type.ATObject;

/**
 * 任务下数据是否已打印
 * @author yangzhenzhou
 *
 */
public class DistributeObjectPrint extends ATObject  {
	/**
	 * 标记任务下的分发数据是否已打印
	 */
	private static final long serialVersionUID = 2676777501077154869L;
	/** CLASSID */
	public static final String CLASSID = DistributeObjectPrint.class
			.getSimpleName();
	/** 分发数据内部标识  */
	private String objInnerid;
	/** 分发数据类名  */
	private String objClassid;
	/** 分发任务内部标识  */
	private String taskInnerid;
	/** 分发任务类名  */
	private String taskClassid;
	/**分发数据是否打印 */
	private String isprint;
	public String getObjInnerid() {
		return objInnerid;
	}
	public void setObjInnerid(String objInnerid) {
		this.objInnerid = objInnerid;
	}
	public String getObjClassid() {
		return objClassid;
	}
	public void setObjClassid(String objClassid) {
		this.objClassid = objClassid;
	}
	public String getTaskInnerid() {
		return taskInnerid;
	}
	public void setTaskInnerid(String taskInnerid) {
		this.taskInnerid = taskInnerid;
	}
	public String getTaskClassid() {
		return taskClassid;
	}
	public void setTaskClassid(String taskClassid) {
		this.taskClassid = taskClassid;
	}
	public String getIsprint() {
		return isprint;
	}
	public void setIsprint(String isprint) {
		this.isprint = isprint;
	}
}
