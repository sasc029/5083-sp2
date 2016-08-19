package com.bjsasc.ddm.distribute.model.convertversion;

import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.type.ATObject;

/**
 * ��汾����ǩ����Ϣ
 * @author zhangguoqiang 2014-4-30
 * ���� --DDM_DC_A3_TASK_SIGNATURE
 */
public class DcA3TaskSignature extends ATObject implements Manageable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1124964456858064721L;

	/** CLASSID */
	public static final String CLASSID = DcA3TaskSignature.class.getSimpleName();
	
	/**
	 * �����˱�ʶ
	 */
	private String dealUserIID;
	/**
	 * �����˱��
	 */
	private String dealUserID;
	/**
	 * ����������
	 */
	private String dealUserName;
	/**
	 * �����˲��ű�ʶ
	 */
	private String dealDevisionIID;
	/**
	 * �����˲��ű��
	 */
	private String dealDevisionID;
	/**
	 * �����˲�������
	 */
	private String dealDevisionName;
	/**
	 * �����˽�ɫ
	 */
	private String dealRole;
	/**
	 * �Ƿ�ͬ��
	 */
	private int isAgree;
	/**
	 * �������
	 */
	private String dealMind;
	/**
	 * �������
	 */
	private String mindType;
	/**
	 * �Ƿ���Ҫ��λ���
	 */
	private int isNeedDevisionMind;
	/**
	 * ����ʼʱ��
	 */
	private long startTime;
	/**
	 * ǩ��ʱ��
	 */
	private long signatureTime;
	/**
	 * ���ʶ
	 */
	private String domainIID;

	/**
	 * ǩ��״̬
	 */
	private int signatureState;

	/**
	 * ��������Ϣ
	 */
	private ManageInfo manageInfo;
	
	/**
	 * ���ݱ�ʶ
	 */
	private String orderIID;
	
	/**
	 * �����ʶ
	 */
	private String taskIID;

	/**
	 * ǩ�����ҵ������
	 */
	private String signatureObjLink = "�鿴ǩ�����";
	
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
