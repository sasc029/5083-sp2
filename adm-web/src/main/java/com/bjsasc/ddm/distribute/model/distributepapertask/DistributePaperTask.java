package com.bjsasc.ddm.distribute.model.distributepapertask;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.distributetask.DistributeTask;
import com.bjsasc.ddm.distribute.model.returnreason.ReturnReason;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleInfo;
import com.bjsasc.plm.core.domain.Domained;

/**
 * 纸质任务数据模型。
 * 
 * @author guowei 2013-2-22
 */
public class DistributePaperTask extends DistributeTask implements Domained{

	/** serialVersionUID */
	private static final long serialVersionUID = -1849905188033282014L;

	/**
	 * 构造方法。
	 */
	public DistributePaperTask() {

	}

	public static final String CLASSID = DistributePaperTask.class.getSimpleName();
	
	private String taskType = ConstUtil.C_TASKTYPE_PAPER;

	/** 生命周期属性信息对象 */
	private LifeCycleInfo lifeCycleInfo = new LifeCycleInfo();

	/** 操作 */
	private String operate = "<img src='../images/p_xls.gif' alt='打印'/>";

	/** 分发信息名称（单位/人员） */
	private String disInfoName;

	/** 分发信息IID（人员或组织的内部标识） */
	private String disInfoId;

	/** 分发信息的类标识（人员或者组织的类标识） */
	private String infoClassId;

	/** 分发信息ID */
	private String distributeInfoId;

	/** 分发信息CLASSID */
	private String distributeInfoClassId;

	/** 操作 */
	private String operater = "<img src='../images/p_xls.gif' alt='打印'/>";
	
	/** 所属发放单OID */
	private String orderOid;

	/** 回退次数 */
	private long returnCount;

	/** 回退时间 */
	private long returnTime;
	
	/** 回退人 */
	private String userName;
	
	/** 分发信息 */
	private DistributeOrderObjectLink distributeOrderObjectLink;
	
	/** 回退理由 */
	private ReturnReason returnReason;
	
	/** 回退理由 */
	private DistributeInfo distributeInfo;
	
	/** 紧急程度（0为普通，1为加急） */
	private String disUrgent;
	
	/**发放单创建者（ForDept7）*/
	private String disOrderCreator;
	
	/**纸质任务下的分发数据是否已打印*/
	private String isprint;
	

	public String getIsprint() {
		return isprint;
	}

	public void setIsprint(String isprint) {
		this.isprint = isprint;
	}

	public String getDisOrderCreator() {
		return disOrderCreator;
	}

	public void setDisOrderCreator(String disOrderCreator) {
		this.disOrderCreator = disOrderCreator;
	}

	public String getDisUrgent() {
		return disUrgent;
	}

	public void setDisUrgent(String disUrgent) {
		this.disUrgent = disUrgent;
	}

	public DistributeInfo getDistributeInfo() {
		return distributeInfo;
	}

	public void setDistributeInfo(DistributeInfo distributeInfo) {
		this.distributeInfo = distributeInfo;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public LifeCycleInfo getLifeCycleInfo() {
		return lifeCycleInfo;
	}

	public void setLifeCycleInfo(LifeCycleInfo lifeCycleInfo) {
		this.lifeCycleInfo = lifeCycleInfo;
	}

	public String getDisInfoName() {
		return disInfoName;
	}

	public void setDisInfoName(String disInfoName) {
		this.disInfoName = disInfoName;
	}

	public String getDisInfoId() {
		return disInfoId;
	}

	public void setDisInfoId(String disInfoId) {
		this.disInfoId = disInfoId;
	}

	public String getInfoClassId() {
		return infoClassId;
	}

	public void setInfoClassId(String infoClassId) {
		this.infoClassId = infoClassId;
	}

	public String getDistributeInfoId() {
		return distributeInfoId;
	}

	public void setDistributeInfoId(String distributeInfoId) {
		this.distributeInfoId = distributeInfoId;
	}

	public String getDistributeInfoClassId() {
		return distributeInfoClassId;
	}

	public void setDistributeInfoClassId(String distributeInfoClassId) {
		this.distributeInfoClassId = distributeInfoClassId;
	}

	public String getOperater() {
		return operater;
	}

	public void setOperater(String operater) {
		this.operater = operater;
	}

	public String getOrderOid() {
		return orderOid;
	}

	public void setOrderOid(String orderOid) {
		this.orderOid = orderOid;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public DistributeOrderObjectLink getDistributeOrderObjectLink() {
		return distributeOrderObjectLink;
	}

	public void setDistributeOrderObjectLink(DistributeOrderObjectLink distributeOrderObjectLink) {
		this.distributeOrderObjectLink = distributeOrderObjectLink;
	}

	public ReturnReason getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(ReturnReason returnReason) {
		this.returnReason = returnReason;
	}

	public long getReturnCount() {
		return returnCount;
	}

	public void setReturnCount(long returnCount) {
		this.returnCount = returnCount;
	}

	public long getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(long returnTime) {
		this.returnTime = returnTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public DistributePaperTask cloneDisTask() {
		DistributePaperTask task = new DistributePaperTask();
		task.setInnerId(getInnerId());
		task.setClassId(getClassId());
		task.setNumber(getNumber());
		task.setName(getName());
		task.setNote(getNote());
		task.setDisUrgent(getDisUrgent());
		task.setManageInfo(getManageInfo());
		task.setContextInfo(getContextInfo());
		task.setDomainInfo(getDomainInfo());
		task.setLifeCycleInfo(getLifeCycleInfo());
		task.setDisOrderCreator(getDisOrderCreator());
		return task;
	}

	@Override
	public String toString() {
		return "DistributePaperTask [lifeCycleInfo=" + lifeCycleInfo + ", operate=" + operate + ", disInfoName="
				+ disInfoName + ", disInfoId=" + disInfoId + ", infoClassId=" + infoClassId + ", distributeInfoId="
				+ distributeInfoId + ", distributeInfoClassId=" + distributeInfoClassId + ", operater=" + operater
				+ "]" + super.toString();
	}

}
