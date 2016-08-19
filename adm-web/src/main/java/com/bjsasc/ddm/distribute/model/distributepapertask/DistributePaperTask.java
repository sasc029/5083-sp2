package com.bjsasc.ddm.distribute.model.distributepapertask;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.distributetask.DistributeTask;
import com.bjsasc.ddm.distribute.model.returnreason.ReturnReason;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleInfo;
import com.bjsasc.plm.core.domain.Domained;

/**
 * ֽ����������ģ�͡�
 * 
 * @author guowei 2013-2-22
 */
public class DistributePaperTask extends DistributeTask implements Domained{

	/** serialVersionUID */
	private static final long serialVersionUID = -1849905188033282014L;

	/**
	 * ���췽����
	 */
	public DistributePaperTask() {

	}

	public static final String CLASSID = DistributePaperTask.class.getSimpleName();
	
	private String taskType = ConstUtil.C_TASKTYPE_PAPER;

	/** ��������������Ϣ���� */
	private LifeCycleInfo lifeCycleInfo = new LifeCycleInfo();

	/** ���� */
	private String operate = "<img src='../images/p_xls.gif' alt='��ӡ'/>";

	/** �ַ���Ϣ���ƣ���λ/��Ա�� */
	private String disInfoName;

	/** �ַ���ϢIID����Ա����֯���ڲ���ʶ�� */
	private String disInfoId;

	/** �ַ���Ϣ�����ʶ����Ա������֯�����ʶ�� */
	private String infoClassId;

	/** �ַ���ϢID */
	private String distributeInfoId;

	/** �ַ���ϢCLASSID */
	private String distributeInfoClassId;

	/** ���� */
	private String operater = "<img src='../images/p_xls.gif' alt='��ӡ'/>";
	
	/** �������ŵ�OID */
	private String orderOid;

	/** ���˴��� */
	private long returnCount;

	/** ����ʱ�� */
	private long returnTime;
	
	/** ������ */
	private String userName;
	
	/** �ַ���Ϣ */
	private DistributeOrderObjectLink distributeOrderObjectLink;
	
	/** �������� */
	private ReturnReason returnReason;
	
	/** �������� */
	private DistributeInfo distributeInfo;
	
	/** �����̶ȣ�0Ϊ��ͨ��1Ϊ�Ӽ��� */
	private String disUrgent;
	
	/**���ŵ������ߣ�ForDept7��*/
	private String disOrderCreator;
	
	/**ֽ�������µķַ������Ƿ��Ѵ�ӡ*/
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
