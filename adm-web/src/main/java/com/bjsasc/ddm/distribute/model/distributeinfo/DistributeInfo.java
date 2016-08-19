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
 * 分发信息数据模型。
 * 
 * @author gengancong 2013-2-22
 */
@SuppressWarnings({ "deprecation", "static-access" })
public class DistributeInfo extends ATObject implements Manageable, LifeCycleManaged {

	/** serialVersionUID */
	private static final long serialVersionUID = -8890218770056415877L;

	/**
	 * 构造方法。
	 */
	public DistributeInfo() {

	}

	/** CLASSID */
	public static final String CLASSID = DistributeInfo.class.getSimpleName();

	/** 生命周期属性信息对象 */
	private LifeCycleInfo lifeCycleInfo = new LifeCycleInfo();

	/** 管理信息 */
	private ManageInfo manageInfo;

	/** oids */
	private String oids;

	/** 发放单与分发数据LINK内部标识 */
	private String disOrderObjLinkId;
	/** 发放单与分发数据LINK类标识 */
	private String disOrderObjLinkClassId;
	/** 分发信息变为已分发状态的时间 */
	private long sendTime;
	/** 分发信息名称（单位/人员） */
	private String disInfoName;
	/** 分发信息IID（人员或组织的内部标识） */
	private String disInfoId;
	/** 分发信息的类标识（人员或者组织的类标识） */
	private String infoClassId;
	/** 外域接收人名称（人员） */
	private String outSignName;
	/** 外域接收人IID（人员内部标识） */
	private String outSignId;
	/** 外域接收人的类标识（人员类标识） */
	private String outSignClassId;
	/** 分发信息类型（0为单位，1为人员） */
	private String disInfoType;
	/** 分发份数 */
	private long disInfoNum;
	/** 销毁份数 */
	private long destroyNum;
	/** 回收份数 */
	private long recoverNum;
	/** 分发介质类型（0为纸质，1为电子，2为跨域） */
	private String disMediaType;
	/** 分发方式（0为直接分发，1为补发，2为移除，3为转发） */
	private String disType;
	/** 备注 */
	private String note;
	/** 分发数据编号 */
	private String number;
	/** 分发数据名称 */
	private String name;
	/** 所属任务名称 */
	private String taskName;
	/** 所属任务OID */
	private String taskOid;
	/** 回收/销毁份数 */
	private String recoverDestroyNum = "0";
	/** 分发信息完工期限 */
	private long disDeadLine;
	/** 紧急程度（0为普通，1为加急） */
	private String disUrgent;
	/** 分发方式（0正式分发，1一次性分发） */
	private String disStyle;
	/** 组织类型 (0为内部,1为外部)*/
	private String sendType;
	/** 盖章信息 */
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
	 * @param lifeCycleInfo 要设置的 lifeCycleInfo
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
	 * @param manageInfo 要设置的 manageInfo
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
	 * @param sendTime 要设置的 sendTime
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
	 * @param disInfoName 要设置的 disInfoName
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
	 * @param disInfoId 要设置的 disInfoId
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
	 * @param infoClassId 要设置的 infoClassId
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
	 * @param disInfoType 要设置的 disInfoType
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
	 * @param disInfoNum 要设置的 disInfoNum
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
	 * @param destroyNum 要设置的 destroyNum
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
	 * @param recoverNum 要设置的 recoverNum
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
	 * @param disMediaType 要设置的 disMediaType
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
	 * @param disType 要设置的 disType
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
	 * @param note 要设置的 note
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
	 * @param disOrderObjLinkId 要设置的 disOrderObjLinkId
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
	 * @param disOrderObjLinkClassId 要设置的 disOrderObjLinkClassId
	 */
	public void setDisOrderObjLinkClassId(String disOrderObjLinkClassId) {
		this.disOrderObjLinkClassId = disOrderObjLinkClassId;
	}

	/* （非 Javadoc）
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		DistributeInfo dis = (DistributeInfo) obj;
		boolean result = dis.getDisInfoId().equals(this.disInfoId)
				&& dis.getDisType().equals(this.disType)
				&& dis.getDisMediaType().equals(this.disMediaType)
				&& dis.getDisInfoNum() == this.disInfoNum;

		// 备注
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

		// 外域接收人
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

		// 备注
		if (this.note != null && this.note.length() > 0) {
			result = result + ";" + this.note.hashCode();
		} else {
			result = result + ";";
		}

		// 外域接收人
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
	 * @param oids 要设置的 oids
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
	 * @param number 要设置的 number
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
	 * @param taskName 要设置的 taskName
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
	 * @param taskOid 要设置的 taskOid
	 */
	public void setTaskOid(String taskOid) {
		this.taskOid = taskOid;
	}

	/**
	 * 
	 * @param name 要设置的 name
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
	 * @param recoverDestroyNum 要设置的 recoverDestroyNum
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
	 * @param disStyle 要设置的 disStyle
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
	 * @param sealInfo 要设置的 sealInfo
	 */
	public void setSealInfo(String sealInfo) {
		this.sealInfo = sealInfo;
	}

}
