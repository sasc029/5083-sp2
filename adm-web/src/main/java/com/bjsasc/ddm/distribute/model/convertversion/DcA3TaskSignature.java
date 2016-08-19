package com.bjsasc.ddm.distribute.model.convertversion;

import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.type.ATObject;

/**
 * 跨版本任务签署信息
 * @author zhangguoqiang 2014-4-30
 * 表名 --DDM_DC_A3_TASK_SIGNATURE
 */
public class DcA3TaskSignature extends ATObject implements Manageable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1124964456858064721L;

	/** CLASSID */
	public static final String CLASSID = DcA3TaskSignature.class.getSimpleName();
	
	/**
	 * 处理人标识
	 */
	private String dealUserIID;
	/**
	 * 处理人编号
	 */
	private String dealUserID;
	/**
	 * 处理人名称
	 */
	private String dealUserName;
	/**
	 * 处理人部门标识
	 */
	private String dealDevisionIID;
	/**
	 * 处理人部门编号
	 */
	private String dealDevisionID;
	/**
	 * 处理人部门名称
	 */
	private String dealDevisionName;
	/**
	 * 处理人角色
	 */
	private String dealRole;
	/**
	 * 是否同意
	 */
	private int isAgree;
	/**
	 * 处理意见
	 */
	private String dealMind;
	/**
	 * 意见类型
	 */
	private String mindType;
	/**
	 * 是否需要单位意见
	 */
	private int isNeedDevisionMind;
	/**
	 * 任务开始时间
	 */
	private long startTime;
	/**
	 * 签署时间
	 */
	private long signatureTime;
	/**
	 * 域标识
	 */
	private String domainIID;

	/**
	 * 签署状态
	 */
	private int signatureState;

	/**
	 * 管理者信息
	 */
	private ManageInfo manageInfo;
	
	/**
	 * 单据标识
	 */
	private String orderIID;
	
	/**
	 * 任务标识
	 */
	private String taskIID;

	/**
	 * 签署对象业务数据
	 */
	private String signatureObjLink = "查看签署对象";
	
	public DcA3TaskSignature(){
		setClassId(CLASSID);
	}
	
	public String getDealUserIID() {
		return dealUserIID;
	}
	public void setDealUserIID(String dealUserIID) {
		this.dealUserIID = dealUserIID;
	}
	public String getDealUserID() {
		return dealUserID;
	}
	public void setDealUserID(String dealUserID) {
		this.dealUserID = dealUserID;
	}
	public String getDealUserName() {
		return dealUserName;
	}
	public void setDealUserName(String dealUserName) {
		this.dealUserName = dealUserName;
	}
	public String getDealDevisionIID() {
		return dealDevisionIID;
	}
	public void setDealDevisionIID(String dealDevisionIID) {
		this.dealDevisionIID = dealDevisionIID;
	}
	public String getDealDevisionID() {
		return dealDevisionID;
	}
	public void setDealDevisionID(String dealDevisionID) {
		this.dealDevisionID = dealDevisionID;
	}
	public String getDealDevisionName() {
		return dealDevisionName;
	}
	public void setDealDevisionName(String dealDevisionName) {
		this.dealDevisionName = dealDevisionName;
	}
	public String getDealRole() {
		return dealRole;
	}
	public void setDealRole(String dealRole) {
		this.dealRole = dealRole;
	}
	public int getIsAgree() {
		return isAgree;
	}
	public void setIsAgree(int isAgree) {
		this.isAgree = isAgree;
	}
	public String getDealMind() {
		return dealMind;
	}
	public void setDealMind(String dealMind) {
		this.dealMind = dealMind;
	}
	public String getMindType() {
		return mindType;
	}
	public void setMindType(String mindType) {
		this.mindType = mindType;
	}
	public int getIsNeedDevisionMind() {
		return isNeedDevisionMind;
	}
	public void setIsNeedDevisionMind(int isNeedDevisionMind) {
		this.isNeedDevisionMind = isNeedDevisionMind;
	}
	public long getSignatureTime() {
		return signatureTime;
	}
	public void setSignatureTime(long signatureTime) {
		this.signatureTime = signatureTime;
	}

	public String getDomainIID() {
		return domainIID;
	}

	public void setDomainIID(String domainIID) {
		this.domainIID = domainIID;
	}

	public String getOrderIID() {
		return orderIID;
	}

	public void setOrderIID(String orderIID) {
		this.orderIID = orderIID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ManageInfo getManageInfo() {
		return manageInfo;
	}

	public void setManageInfo(ManageInfo manageInfo) {
		this.manageInfo = manageInfo;
	}

	public int getSignatureState() {
		return signatureState;
	}

	public void setSignatureState(int signatureState) {
		this.signatureState = signatureState;
	}

	public String getTaskIID() {
		return taskIID;
	}

	public void setTaskIID(String taskIID) {
		this.taskIID = taskIID;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public String getSignatureObjLink() {
		return signatureObjLink;
	}

	public void setSignatureObjLink(String signatureObjLink) {
		this.signatureObjLink = signatureObjLink;
	}

}
