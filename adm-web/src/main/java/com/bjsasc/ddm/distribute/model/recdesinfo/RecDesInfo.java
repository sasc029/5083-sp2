package com.bjsasc.ddm.distribute.model.recdesinfo;

import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleInfo;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.type.ATObject;

/**
 * �������ٷַ���Ϣ
 * 
 * @author kangyanfei 2014-5-28
 *
 */
public class RecDesInfo extends ATObject implements Manageable, LifeCycleManaged {

	/** CLASSID */
	public static final String CLASSID = RecDesInfo.class.getSimpleName();

	/** serialVersionUID */
	private static final long serialVersionUID = 5080756332789037098L;

	/** ��������������Ϣ���� */
	private LifeCycleInfo lifeCycleInfo = new LifeCycleInfo();

	/** ������Ϣ */
	private ManageInfo manageInfo;

	/** oids */
	private String oids;

	/** �������ٷ��ŵ���ַ�����LINK�ڲ���ʶ */
	private String disOrderObjectLinkId;
	/** �������ٷ��ŵ���ַ�����LINK���ʶ  */
	private String disOrderObjectLinkClassId;
	/** ��Ҫ���ٵķ���  */
	private long needDestroyNum;
	/** ��Ҫ���յķ���  */
	private long needRecoverNum;
	/** �ַ����� */
	private long disInfoNum;
	/** ���ٷ��� */
	private long destroyNum;
	/** ���շ��� */
	private long recoverNum;
	/** �ַ���Ϣ��Ϊ�ѷַ�״̬��ʱ�� */
	private long sendTime;
	/** �ַ���Ϣ���ƣ���λ/��Ա�� */
	private String disInfoName;
	/** �ַ���ϢIID����Ա����֯���ڲ���ʶ�� */
	private String disInfoId;
	/** �ַ���Ϣ�����ʶ����Ա������֯�����ʶ�� */
	private String infoClassId;
	/** �ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա�� */
	private String disInfoType;
	/** �ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ���� */
	private String disMediaType;
	/** �ַ���ʽ��0Ϊֱ�ӷַ���1Ϊ������2Ϊ�Ƴ���3Ϊת����4Ϊ���գ�5Ϊ���٣�*/
	private String disType;
	/** ��ע */
	private String note;
	/** �ַ����ݱ�� */
	private String number;
	/** ������������ */
	private String taskName;
	/** �ַ���Ϣ�깤���� */
	private long disDeadLine;
	/** �����̶ȣ�0Ϊ��ͨ��1Ϊ�Ӽ��� */
	private String disUrgent;
	/** ��������ID*/
	private String disObjId;
	/** �ַ��������� */
	private String disObjName;
	/** �������ݶ���*/
	private DistributeObject distributeObject;
	/** �������ݶ���LINK*/
	private DistributeOrderObjectLink distributeOrderObjectLink;
	/**
	 * ���췽����
	 */
	public RecDesInfo() {

	}

	/**
	 * @return ����������Ϣ
	 */
	public LifeCycleInfo getLifeCycleInfo() {
		return lifeCycleInfo;
	}

	/**
	 * @return ������Ϣ
	 */
	public ManageInfo getManageInfo() {
		return manageInfo;
	}
	
	/**
	 * @return oids
	 */
	public String getOids() {
		return oids;
	}

	/**
	 * @return �ַ�����LINK�ڲ���ʶ 
	 */
	public String getDisOrderObjectLinkId() {
		return disOrderObjectLinkId;
	}

	/**
	 * @return �ַ�����LINK���ʶ 
	 */
	public String getDisOrderObjectLinkClassId() {
		return disOrderObjectLinkClassId;
	}

	/**
	 * @return ��Ҫ���ٵķ��� 
	 */
	public long getNeedDestroyNum() {
		return needDestroyNum;
	}

	/**
	 * @return ��Ҫ���յķ���
	 */
	public long getNeedRecoverNum() {
		return needRecoverNum;
	}

	/**
	 * @return �ַ�����
	 */
	public long getDisInfoNum() {
		return disInfoNum;
	}

	/**
	 * @return ���ٷ���
	 */
	public long getDestroyNum() {
		return destroyNum;
	}

	/**
	 * @return ���շ���
	 */
	public long getRecoverNum() {
		return recoverNum;
	}

	
	/**
	 * @return �ַ���Ϣ��Ϊ�ѷַ�״̬��ʱ��
	 */
	public long getSendTime() {
		return sendTime;
	}

	/**
	 * @return �ַ���Ϣ���ƣ���λ/��Ա��
	 */
	public String getDisInfoName() {
		return disInfoName;
	}
	
	/**
	 * @return �ַ���ϢIID����Ա����֯���ڲ���ʶ��
	 */
	public String getDisInfoId() {
		return disInfoId;
	}

	/**
	 * @return �ַ���Ϣ�����ʶ����Ա������֯�����ʶ��
	 */
	public String getInfoClassId() {
		return infoClassId;
	}

	/**
	 * @return �ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
	 */
	public String getDisInfoType() {
		return disInfoType;
	}

	/**
	 * @return �ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
	 */
	public String getDisMediaType() {
		return disMediaType;
	}

	/**
	 * @return �ַ���ʽ��0Ϊֱ�ӷַ���1Ϊ������2Ϊ�Ƴ���3Ϊת����4Ϊ���գ�5Ϊ���٣�
	 */
	public String getDisType() {
		return disType;
	}

	/**
	 * @param disType
	 * 			���÷ַ���ʽ��0Ϊֱ�ӷַ���1Ϊ������2Ϊ�Ƴ���3Ϊת����4Ϊ���գ�5Ϊ���٣�
	 */
	public void setDisType(String disType) {
		this.disType = disType;
	}

	/**
	 * @return ��ע
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return �ַ����ݱ��
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @return �ַ���������
	 */
	public String getDisObjName() {
		return disObjName;
	}

	/**
	 * @return ������������
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * @return �ַ���Ϣ�깤����
	 */
	public long getDisDeadLine() {
		return disDeadLine;
	}

	/**
	 * @return �����̶ȣ�0Ϊ��ͨ��1Ϊ�Ӽ���
	 */
	public String getDisUrgent() {
		return disUrgent;
	}


	/**
	 * @return ��������ID
	 */
	public String getDisObjId() {
		return disObjId;
	}

	/**
	 * @return �������ݶ���
	 */
	public DistributeObject getDistributeObject() {
		return distributeObject;
	}

	/**
	 * @return �������ݶ���LINK
	 */
	public DistributeOrderObjectLink getDistributeOrderObjectLink() {
		return distributeOrderObjectLink;
	}

	/**
	 * @param lifeCycleInfo
	 *			���� lifeCycleInfo
	 */
	public void setLifeCycleInfo(LifeCycleInfo lifeCycleInfo) {
		this.lifeCycleInfo = lifeCycleInfo;
	}

	/**
	 * @param manageInfo
	 *			���� manageInfo
	 */
	public void setManageInfo(ManageInfo manageInfo) {
		this.manageInfo = manageInfo;
	}

	/**
	 * @param oids
	 *			���� oids
	 */
	public void setOids(String oids) {
		this.oids = oids;
	}

	/**
	 * @param disOrderObjectLinkId
	 *			���÷ַ�����LINK�ڲ���ʶ
	 */
	public void setDisOrderObjectLinkId(String disOrderObjectLinkId) {
		this.disOrderObjectLinkId = disOrderObjectLinkId;
	}

	/**
	 * @param disOrderObjectLinkClassId
	 *			���÷ַ�����LINK���ʶ
	 */
	public void setDisOrderObjectLinkClassId(String disOrderObjectLinkClassId) {
		this.disOrderObjectLinkClassId = disOrderObjectLinkClassId;
	}

	/**
	 * @param needDestroyNum
	 *			���� ��Ҫ���ٵķ���
	 */
	public void setNeedDestroyNum(long needDestroyNum) {
		this.needDestroyNum = needDestroyNum;
	}

	/**
	 * @param needRecoverNum
	 *			���� ��Ҫ���յķ���
	 */
	public void setNeedRecoverNum(long needRecoverNum) {
		this.needRecoverNum = needRecoverNum;
	}

	/**
	 * @param disInfoNum
	 *			���÷ַ�����
	 */
	public void setDisInfoNum(long disInfoNum) {
		this.disInfoNum = disInfoNum;
	}

	/**
	 * @param destroyNum
	 *			���� ���ٷ���
	 */
	public void setDestroyNum(long destroyNum) {
		this.destroyNum = destroyNum;
	}

	/**
	 * @param recoverNum
	 *			���� ���շ���
	 */
	public void setRecoverNum(long recoverNum) {
		this.recoverNum = recoverNum;
	}

	/**
	 * @param sendTime
	 *			���÷ַ���Ϣ��Ϊ�ѷַ�״̬��ʱ��
	 */
	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * @param disInfoName
	 *			���� �ַ���Ϣ���ƣ���λ/��Ա��
	 */
	public void setDisInfoName(String disInfoName) {
		this.disInfoName = disInfoName;
	}
	
	/**
	 * @param disInfoId
	 *			���÷ַ���ϢIID����Ա����֯���ڲ���ʶ��
	 */
	public void setDisInfoId(String disInfoId) {
		this.disInfoId = disInfoId;
	}

	/**
	 * @param infoClassId
	 *			���� �ַ���Ϣ�����ʶ����Ա������֯�����ʶ��
	 */
	public void setInfoClassId(String infoClassId) {
		this.infoClassId = infoClassId;
	}


	/**
	 * @param disInfoType
	 *			���÷ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա�� 
	 */
	public void setDisInfoType(String disInfoType) {
		this.disInfoType = disInfoType;
	}

	/**
	 * @param disMediaType
	 *			���� �ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
	 */
	public void setDisMediaType(String disMediaType) {
		this.disMediaType = disMediaType;
	}

	/**
	 * @param note
	 *			���� ��ע
	 */
	public void setNote(String note) {
		this.note = note;
	}
	
	/**
	 * @param number
	 *			���� �ַ����ݱ��
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @param disObjName
	 *			���� �ַ���������
	 */
	public void setDisObjName(String disObjName) {
		this.disObjName = disObjName;
	}

	/**
	 * @param taskName
	 *			���������������� 
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * @param disDeadLine
	 *			���÷ַ���Ϣ�깤����
	 */
	public void setDisDeadLine(long disDeadLine) {
		this.disDeadLine = disDeadLine;
	}

	/**
	 * @param disUrgent
	 *			���� �����̶ȣ�0Ϊ��ͨ��1Ϊ�Ӽ���
	 */
	public void setDisUrgent(String disUrgent) {
		this.disUrgent = disUrgent;
	}


	/**
	 * @param disObjId
	 *			���� ��������ID
	 */
	public void setDisObjId(String disObjId) {
		this.disObjId = disObjId;
	}

	/**
	 * @param DistributeObject
	 *			���� �������ݶ���
	 */
	public void setDistributeObject(DistributeObject distributeObject) {
		this.distributeObject = distributeObject;
	}

	/**
	 * @param DistributeOrderObjectLink
	 *			���� �������ݶ���LINK
	 */
	public void setDistributeOrderObjectLink(
			DistributeOrderObjectLink distributeOrderObjectLink) {
		this.distributeOrderObjectLink = distributeOrderObjectLink;
	}

	/**
	 * @return oid
	 */
	public String getOid() {
		return RecDesInfo.CLASSID + ":" + this.getInnerId();
	}

	//���OID
	public void addOid(String oid) {
		if (oids != null) {
			if (oid != null) {
				oids += "," + oid;
			}
		} else {
			oids = oid;
		}
	}

	//��Ҫ����
	public String getKey() {
		String result = this.disInfoId.hashCode() + ";"
				+ this.disMediaType.hashCode() + ";"
				+ String.valueOf(this.disInfoNum).hashCode() + ";"
				+ String.valueOf(this.recoverNum).hashCode() + ";"
				+ String.valueOf(this.needRecoverNum).hashCode();

		// ��ע
		if (this.note != null && this.note.length() > 0) {
			result = result + ";" + this.note.hashCode();
		} else {
			result = result + ";";
		}

		return result;
	}

	//��Ҫ����
	public String getKey2() {
		String result = this.disInfoId.hashCode() + ";"
				+ this.disMediaType.hashCode() + ";"
				+ String.valueOf(this.disInfoNum).hashCode() +";"
				+ String.valueOf(this.destroyNum).hashCode() +";"
				+ String.valueOf(this.needDestroyNum).hashCode();
		
		// ��ע
		if (this.note != null && this.note.length() > 0) {
			result = result + ";" + this.note.hashCode();
		} else {
			result = result + ";";
		}

		return result;
	}

	public RecDesInfo cloneRecDesInfo() {
		RecDesInfo info = new RecDesInfo();
		info.setInnerId(getInnerId());
		info.setClassId(getClassId());
		info.setNumber(getNumber());
		info.setNote(getNote());
		info.setNeedDestroyNum(getNeedDestroyNum());
		info.setNeedRecoverNum(getNeedRecoverNum());
		info.setDisInfoNum(getDisInfoNum());
		info.setDestroyNum(getDestroyNum());
		info.setDisInfoId(getDisInfoId());
		info.setDisInfoNum(getDisInfoNum());
		info.setDisInfoType(getDisInfoType());
		info.setDisInfoName(getDisInfoName());
		info.setRecoverNum(getRecoverNum());
		info.setManageInfo(getManageInfo());
		info.setLifeCycleInfo(getLifeCycleInfo());
		info.setDisType(getDisType());
		info.setDisMediaType(getDisMediaType());
		return info;
	}

	
}
