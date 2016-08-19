package com.bjsasc.ddm.distribute.model.distributeinfo;

import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleInfo;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.type.ATObject;
import com.bjsasc.plm.type.restrict.select.Select;
import com.bjsasc.plm.type.restrict.select.SelectOption;

/**
 * �ַ���Ϣ����ģ�͡�
 * 
 * @author gengancong 2013-2-22
 */
@SuppressWarnings({ "deprecation", "static-access" })
public class DistributeInfo extends ATObject implements Manageable, LifeCycleManaged {

	/** serialVersionUID */
	private static final long serialVersionUID = -8890218770056415877L;

	/**
	 * ���췽����
	 */
	public DistributeInfo() {

	}

	/** CLASSID */
	public static final String CLASSID = DistributeInfo.class.getSimpleName();

	/** ��������������Ϣ���� */
	private LifeCycleInfo lifeCycleInfo = new LifeCycleInfo();

	/** ������Ϣ */
	private ManageInfo manageInfo;

	/** oids */
	private String oids;

	/** ���ŵ���ַ�����LINK�ڲ���ʶ */
	private String disOrderObjLinkId;
	/** ���ŵ���ַ�����LINK���ʶ */
	private String disOrderObjLinkClassId;
	/** �ַ���Ϣ��Ϊ�ѷַ�״̬��ʱ�� */
	private long sendTime;
	/** �ַ���Ϣ���ƣ���λ/��Ա�� */
	private String disInfoName;
	/** �ַ���ϢIID����Ա����֯���ڲ���ʶ�� */
	private String disInfoId;
	/** �ַ���Ϣ�����ʶ����Ա������֯�����ʶ�� */
	private String infoClassId;
	/** ������������ƣ���Ա�� */
	private String outSignName;
	/** ���������IID����Ա�ڲ���ʶ�� */
	private String outSignId;
	/** ��������˵����ʶ����Ա���ʶ�� */
	private String outSignClassId;
	/** �ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա�� */
	private String disInfoType;
	/** �ַ����� */
	private long disInfoNum;
	/** ���ٷ��� */
	private long destroyNum;
	/** ���շ��� */
	private long recoverNum;
	/** �ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ���� */
	private String disMediaType;
	/** �ַ���ʽ��0Ϊֱ�ӷַ���1Ϊ������2Ϊ�Ƴ���3Ϊת���� */
	private String disType;
	/** ��ע */
	private String note;
	/** �ַ����ݱ�� */
	private String number;
	/** �ַ��������� */
	private String name;
	/** ������������ */
	private String taskName;
	/** ��������OID */
	private String taskOid;
	/** ����/���ٷ��� */
	private String recoverDestroyNum = "0";
	/** �ַ���Ϣ�깤���� */
	private long disDeadLine;
	/** �����̶ȣ�0Ϊ��ͨ��1Ϊ�Ӽ��� */
	private String disUrgent;
	/** �ַ���ʽ��0��ʽ�ַ���1һ���Էַ��� */
	private String disStyle;
	/** ��֯���� (0Ϊ�ڲ�,1Ϊ�ⲿ)*/
	private String sendType;
	/** ������Ϣ */
	private String sealInfo;

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	private DistributeObject distributeObject;

	private DistributeOrderObjectLink distributeOrderObjectLink;

	public DistributeObject getDistributeObject() {
		return distributeObject;
	}

	public void setDistributeObject(DistributeObject distributeObject) {
		this.distributeObject = distributeObject;
	}

	public DistributeOrderObjectLink getDistributeOrderObjectLink() {
		return distributeOrderObjectLink;
	}

	public void setDistributeOrderObjectLink(DistributeOrderObjectLink distributeOrderObjectLink) {
		this.distributeOrderObjectLink = distributeOrderObjectLink;
	}

	public long getDisDeadLine() {
		return disDeadLine;
	}

	public void setDisDeadLine(long disDeadLine) {
		this.disDeadLine = disDeadLine;
	}

	public String getDisUrgent() {
		return disUrgent;
	}

	public void setDisUrgent(String disUrgent) {
		this.disUrgent = disUrgent;
	}

	/**
	 * @return lifeCycleInfo
	 */
	public LifeCycleInfo getLifeCycleInfo() {
		return lifeCycleInfo;
	}

	/**
	 * @param lifeCycleInfo Ҫ���õ� lifeCycleInfo
	 */
	public void setLifeCycleInfo(LifeCycleInfo lifeCycleInfo) {
		this.lifeCycleInfo = lifeCycleInfo;
	}

	/**
	 * @return manageInfo
	 */
	public ManageInfo getManageInfo() {
		return manageInfo;
	}

	/**
	 * @param manageInfo Ҫ���õ� manageInfo
	 */
	public void setManageInfo(ManageInfo manageInfo) {
		this.manageInfo = manageInfo;
	}

	/**
	 * @return sendTime
	 */
	public long getSendTime() {
		return sendTime;
	}

	/**
	 * @param sendTime Ҫ���õ� sendTime
	 */
	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * @return disInfoName
	 */
	public String getDisInfoName() {
		return disInfoName;
	}

	/**
	 * @param disInfoName Ҫ���õ� disInfoName
	 */
	public void setDisInfoName(String disInfoName) {
		this.disInfoName = disInfoName;
	}

	/**
	 * @return disInfoId
	 */
	public String getDisInfoId() {
		return disInfoId;
	}

	/**
	 * @param disInfoId Ҫ���õ� disInfoId
	 */
	public void setDisInfoId(String disInfoId) {
		this.disInfoId = disInfoId;
	}

	/**
	 * @return infoClassId
	 */
	public String getInfoClassId() {
		return infoClassId;
	}

	/**
	 * @param infoClassId Ҫ���õ� infoClassId
	 */
	public void setInfoClassId(String infoClassId) {
		this.infoClassId = infoClassId;
	}

	/**
	 * @return outSignName
	 */
	public String getOutSignName() {
		return outSignName;
	}

	/**
	 * @param outSignName
	 */
	public void setOutSignName(String outSignName) {
		this.outSignName = outSignName;
	}

	/**
	 * @return outSignId
	 */
	public String getOutSignId() {
		return outSignId;
	}

	/**
	 * @param outSignId
	 */
	public void setOutSignId(String outSignId) {
		this.outSignId = outSignId;
	}

	/**
	 * @return outSignClassId
	 */
	public String getOutSignClassId() {
		return outSignClassId;
	}

	/**
	 * @param outSignClassId
	 */
	public void setOutSignClassId(String outSignClassId) {
		this.outSignClassId = outSignClassId;
	}

	/**
	 * @return disInfoType
	 */
	public String getDisInfoType() {
		return disInfoType;
	}

	/**
	 * @param disInfoType Ҫ���õ� disInfoType
	 */
	public void setDisInfoType(String disInfoType) {
		this.disInfoType = disInfoType;
	}

	/**
	 * @return disInfoNum
	 */
	public long getDisInfoNum() {
		return disInfoNum;
	}

	/**
	 * @param disInfoNum Ҫ���õ� disInfoNum
	 */
	public void setDisInfoNum(long disInfoNum) {
		this.disInfoNum = disInfoNum;
	}

	/**
	 * @return destroyNum
	 */
	public long getDestroyNum() {
		return destroyNum;
	}

	/**
	 * @param destroyNum Ҫ���õ� destroyNum
	 */
	public void setDestroyNum(long destroyNum) {
		this.destroyNum = destroyNum;
	}

	/**
	 * @return recoverNum
	 */
	public long getRecoverNum() {
		return recoverNum;
	}

	/**
	 * @param recoverNum Ҫ���õ� recoverNum
	 */
	public void setRecoverNum(long recoverNum) {
		this.recoverNum = recoverNum;
	}

	/**
	 * @return disMediaType
	 */
	public String getDisMediaType() {
		return disMediaType;
	}

	/**
	 * @param disMediaType Ҫ���õ� disMediaType
	 */
	public void setDisMediaType(String disMediaType) {
		this.disMediaType = disMediaType;
	}

	/**
	 * @return disType
	 */
	public String getDisType() {
		return disType;
	}

	/**
	 * @param disType Ҫ���õ� disType
	 */
	public void setDisType(String disType) {
		this.disType = disType;
	}

	/**
	 * @return note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note Ҫ���õ� note
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return disOrderObjLinkId
	 */
	public String getDisOrderObjLinkId() {
		return disOrderObjLinkId;
	}

	/**
	 * @param disOrderObjLinkId Ҫ���õ� disOrderObjLinkId
	 */
	public void setDisOrderObjLinkId(String disOrderObjLinkId) {
		this.disOrderObjLinkId = disOrderObjLinkId;
	}

	/**
	 * @return disOrderObjLinkClassId
	 */
	public String getDisOrderObjLinkClassId() {
		return disOrderObjLinkClassId;
	}

	/**
	 * @param disOrderObjLinkClassId Ҫ���õ� disOrderObjLinkClassId
	 */
	public void setDisOrderObjLinkClassId(String disOrderObjLinkClassId) {
		this.disOrderObjLinkClassId = disOrderObjLinkClassId;
	}

	/* ���� Javadoc��
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		DistributeInfo dis = (DistributeInfo) obj;
		boolean result = dis.getDisInfoId().equals(this.disInfoId)
				&& dis.getDisType().equals(this.disType)
				&& dis.getDisMediaType().equals(this.disMediaType)
				&& dis.getDisInfoNum() == this.disInfoNum;

		// ��ע
		String note1 = dis.getNote();
		String note2 = this.note;
		if (note1 == null) {
			note1 = "";
		}
		if (note2 == null) {
			note2 = "";
		}
		if (note1 != null && note2 != null) {
			result = result && note1.equals(note2);
		}

		// ���������
		String outSignId1 = dis.getOutSignId();
		String outSignId2 = this.outSignId;
		if (outSignId1 == null) {
			outSignId1 = "";
		}
		if (outSignId2 == null) {
			outSignId2 = "";
		}
		if (outSignId1 != null && outSignId2 != null) {
			result = result && outSignId1.equals(outSignId2);
		}
		return result;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public String getKey() {
		String result = this.disInfoId.hashCode() + ";"
				+ this.disType.hashCode() + ";" + this.disMediaType.hashCode()
				+ ";" + String.valueOf(this.disInfoNum).hashCode();

		// ��ע
		if (this.note != null && this.note.length() > 0) {
			result = result + ";" + this.note.hashCode();
		} else {
			result = result + ";";
		}

		// ���������
		if (this.outSignId != null && this.outSignId.length() > 0) {
			result = result + ";" + this.outSignId.hashCode();
		} else {
			result = result + ";";
		}
		return result;
	}

	public void addOid(String oid) {
		if (oids != null) {
			if (oid != null) {
				oids += "," + oid;
			}
		} else {
			oids = oid;
		}
	}

	/**
	 * @return oids
	 */
	public String getOids() {
		return oids;
	}

	/**
	 * @param oids Ҫ���õ� oids
	 */
	public void setOids(String oids) {
		this.oids = oids;
	}

	/**
	 * @return oid
	 */
	public String getOid() {
		return this.CLASSID + ":" + this.getInnerId();
	}

	/**
	 * @return disTypeName
	 */
	public String getDisTypeName() {
		Select select = Helper.getRestrictManager().getSelect("disType");
		SelectOption selectOption = select.getOptionMap().get(disType);
		return selectOption.getText();
	}

	/**
	 * @return disMediaTypeName
	 */
	public String getDisMediaTypeName() {
		Select select = Helper.getRestrictManager().getSelect("disMediaType");
		SelectOption selectOption = select.getOptionMap().get(disMediaType);
		return selectOption.getText();
	}

	/**
	 * 
	 * @return number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * 
	 * @param number Ҫ���õ� number
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return taskName
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * 
	 * @param taskName Ҫ���õ� taskName
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * 
	 * @return taskOid
	 */
	public String getTaskOid() {
		return taskOid;
	}

	/**
	 * 
	 * @param taskOid Ҫ���õ� taskOid
	 */
	public void setTaskOid(String taskOid) {
		this.taskOid = taskOid;
	}

	/**
	 * 
	 * @param name Ҫ���õ� name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return recoverDestroyNum
	 */
	public String getRecoverDestroyNum() {
		return recoverDestroyNum;
	}

	/**
	 * 
	 * @param recoverDestroyNum Ҫ���õ� recoverDestroyNum
	 */
	public void setRecoverDestroyNum(String recoverDestroyNum) {
		this.recoverDestroyNum = recoverDestroyNum;
	}

	public DistributeInfo cloneDisInfo() {
		DistributeInfo info = new DistributeInfo();
		info.setInnerId(getInnerId());
		info.setClassId(getClassId());
		info.setNumber(getNumber());
		info.setNote(getNote());
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

	/**
	 * @return disStyle
	 */
	public String getDisStyle() {
		return disStyle;
	}

	/**
	 * @param disStyle Ҫ���õ� disStyle
	 */
	public void setDisStyle(String disStyle) {
		this.disStyle = disStyle;
	}
	
	/**
	 * @return sealInfo
	 */
	public String getSealInfo() {
		return sealInfo;
	}

	/**
	 * @param sealInfo Ҫ���õ� sealInfo
	 */
	public void setSealInfo(String sealInfo) {
		this.sealInfo = sealInfo;
	}

}
