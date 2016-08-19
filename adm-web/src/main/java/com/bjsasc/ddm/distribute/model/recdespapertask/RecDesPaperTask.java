package com.bjsasc.ddm.distribute.model.recdespapertask;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributetask.DistributeTask;
import com.bjsasc.ddm.distribute.model.returnreason.ReturnReason;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleInfo;

/**
 * ��������ֽ������ģ��
 * 
 * @author sunzongqing
 */
public class RecDesPaperTask extends DistributeTask {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1202970310680403542L;
	
	/**
	 * ���췽��
	 */
	public RecDesPaperTask(){
		
	}
	
	public static final String CLASSID=RecDesPaperTask.class.getSimpleName();
	
	/** ���õ������ͣ�Ϊ���л�������ֽ������ֱ�Ӹ�ֵ*/
	private String taskType=ConstUtil.C_TASKTYPE_PAPER;
	
	/** ��������������Ϣ���� */
	private LifeCycleInfo lifeCycleInfo = new LifeCycleInfo();
	
	/** ����*/
	private String operate = "<img src='../images/p_xls.gif' alt='��ӡ'/>";
	
	/** �ַ���Ϣ���ƣ���λ/��Ա��*/
	private String disInfoName;
	
	/** �ַ���ϢInnerID����Ա/��֯���ڲ���ʶ��*/
	private String disInfoId;
	
	/** �ַ���ϢClassID����Ա/��֯�����ʶ��*/
	private String infoClassId;
	
	/** ����������ϢInnerID*/
	private String recDesInfoId;
	
	/** ����������ϢClassID*/
	private String recDesInfoClassId;
	
	/** ������*/
	private String operater ="<img src='../images/p_xls.gif' alt='��ӡ'/>";
	
	/** �������յ�/���ٵ�OID*/
	private String orderOid;
	
	/** ���˴��� */
	private long returnCount;

	/** ����ʱ�� */
	private long returnTime;
	
	/** ������ */
	private String userName;
	
	/** �������� */
	private ReturnReason returnReason;
	
	/** �������� */
	private DistributeInfo distributeInfo;
	
	/** �����̶ȣ�0Ϊ��ͨ��1Ϊ�Ӽ��� */
	private String disUrgent;
	@Override
	public LifeCycleInfo getLifeCycleInfo() {
		return lifeCycleInfo;
	}

	@Override
	public void setLifeCycleInfo(LifeCycleInfo lifeCycleInfo) {
		this.lifeCycleInfo=lifeCycleInfo;
	}
	
	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
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

	public String getRecDesInfoId() {
		return recDesInfoId;
	}

	public void setRecDesInfoId(String recDesInfoId) {
		this.recDesInfoId = recDesInfoId;
	}

	public String getRecDesInfoClassId() {
		return recDesInfoClassId;
	}

	public void setRecDesInfoClassId(String recDesInfoClassId) {
		this.recDesInfoClassId = recDesInfoClassId;
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

	public ReturnReason getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(ReturnReason returnReason) {
		this.returnReason = returnReason;
	}

	public DistributeInfo getDistributeInfo() {
		return distributeInfo;
	}

	public void setDistributeInfo(DistributeInfo distributeInfo) {
		this.distributeInfo = distributeInfo;
	}
	
	public void setTaskType(String taskType){
		this.taskType = taskType;
	}
	
	public String getTaskType(){
		return taskType;
	}
	
	public void setDisUrgent(String disUrgent){
		this.disUrgent = disUrgent;
	}
	
	public String getDisUrgent(){
		return disUrgent;
	}
	
	public RecDesPaperTask cloneRecDesPaperTask(){
		RecDesPaperTask cloneTask = new RecDesPaperTask();
		cloneTask.setInnerId(getInnerId());
		cloneTask.setClassId(getClassId());
		cloneTask.setNumber(getNumber());
		cloneTask.setName(getName());
		cloneTask.setNote(getNote());
		cloneTask.setDisUrgent(getDisUrgent());
		cloneTask.setManageInfo(getManageInfo());
		cloneTask.setContextInfo(getContextInfo());
		cloneTask.setDomainInfo(getDomainInfo());
		cloneTask.setLifeCycleInfo(getLifeCycleInfo());
		return cloneTask;
	}
	
	public String toString(){
		return "RecDesPaperTask [lifeCycleInfo="+lifeCycleInfo+
				", operate="+operate+", disInfoName="+disInfoName+
				", disInfoId="+disInfoId+", infoClassId"+infoClassId+
				", recDesInfoId"+recDesInfoId+", recDesInfoClassId"+
				recDesInfoClassId+", operater="+operater+"]"+super.toString();
	}
}
