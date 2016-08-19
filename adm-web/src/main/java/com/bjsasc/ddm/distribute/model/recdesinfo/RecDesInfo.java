package com.bjsasc.ddm.distribute.model.recdesinfo;

import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleInfo;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.type.ATObject;

/**
 * 回收销毁分发信息
 * 
 * @author kangyanfei 2014-5-28
 *
 */
public class RecDesInfo extends ATObject implements Manageable, LifeCycleManaged {

	/** CLASSID */
	public static final String CLASSID = RecDesInfo.class.getSimpleName();

	/** serialVersionUID */
	private static final long serialVersionUID = 5080756332789037098L;

	/** 生命周期属性信息对象 */
	private LifeCycleInfo lifeCycleInfo = new LifeCycleInfo();

	/** 管理信息 */
	private ManageInfo manageInfo;

	/** oids */
	private String oids;

	/** 回收销毁发放单与分发数据LINK内部标识 */
	private String disOrderObjectLinkId;
	/** 回收销毁发放单与分发数据LINK类标识  */
	private String disOrderObjectLinkClassId;
	/** 需要销毁的份数  */
	private long needDestroyNum;
	/** 需要回收的份数  */
	private long needRecoverNum;
	/** 分发份数 */
	private long disInfoNum;
	/** 销毁份数 */
	private long destroyNum;
	/** 回收份数 */
	private long recoverNum;
	/** 分发信息变为已分发状态的时间 */
	private long sendTime;
	/** 分发信息名称（单位/人员） */
	private String disInfoName;
	/** 分发信息IID（人员或组织的内部标识） */
	private String disInfoId;
	/** 分发信息的类标识（人员或者组织的类标识） */
	private String infoClassId;
	/** 分发信息类型（0为单位，1为人员） */
	private String disInfoType;
	/** 分发介质类型（0为纸质，1为电子，2为跨域） */
	private String disMediaType;
	/** 分发方式（0为直接分发，1为补发，2为移除，3为转发，4为回收，5为销毁）*/
	private String disType;
	/** 备注 */
	private String note;
	/** 分发数据编号 */
	private String number;
	/** 所属任务名称 */
	private String taskName;
	/** 分发信息完工期限 */
	private long disDeadLine;
	/** 紧急程度（0为普通，1为加急） */
	private String disUrgent;
	/** 发放数据ID*/
	private String disObjId;
	/** 分发数据名称 */
	private String disObjName;
	/** 发放数据对象*/
	private DistributeObject distributeObject;
	/** 发放数据对象LINK*/
	private DistributeOrderObjectLink distributeOrderObjectLink;
	/**
	 * 构造方法。
	 */
	public RecDesInfo() {

	}

	/**
	 * @return 生命周期信息
	 */
	public LifeCycleInfo getLifeCycleInfo() {
		return lifeCycleInfo;
	}

	/**
	 * @return 管理信息
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
	 * @return 分发数据LINK内部标识 
	 */
	public String getDisOrderObjectLinkId() {
		return disOrderObjectLinkId;
	}

	/**
	 * @return 分发数据LINK类标识 
	 */
	public String getDisOrderObjectLinkClassId() {
		return disOrderObjectLinkClassId;
	}

	/**
	 * @return 需要销毁的份数 
	 */
	public long getNeedDestroyNum() {
		return needDestroyNum;
	}

	/**
	 * @return 需要回收的份数
	 */
	public long getNeedRecoverNum() {
		return needRecoverNum;
	}

	/**
	 * @return 分发份数
	 */
	public long getDisInfoNum() {
		return disInfoNum;
	}

	/**
	 * @return 销毁份数
	 */
	public long getDestroyNum() {
		return destroyNum;
	}

	/**
	 * @return 回收份数
	 */
	public long getRecoverNum() {
		return recoverNum;
	}

	
	/**
	 * @return 分发信息变为已分发状态的时间
	 */
	public long getSendTime() {
		return sendTime;
	}

	/**
	 * @return 分发信息名称（单位/人员）
	 */
	public String getDisInfoName() {
		return disInfoName;
	}
	
	/**
	 * @return 分发信息IID（人员或组织的内部标识）
	 */
	public String getDisInfoId() {
		return disInfoId;
	}

	/**
	 * @return 分发信息的类标识（人员或者组织的类标识）
	 */
	public String getInfoClassId() {
		return infoClassId;
	}

	/**
	 * @return 分发信息类型（0为单位，1为人员）
	 */
	public String getDisInfoType() {
		return disInfoType;
	}

	/**
	 * @return 分发介质类型（0为纸质，1为电子，2为跨域）
	 */
	public String getDisMediaType() {
		return disMediaType;
	}

	/**
	 * @return 分发方式（0为直接分发，1为补发，2为移除，3为转发，4为回收，5为销毁）
	 */
	public String getDisType() {
		return disType;
	}

	/**
	 * @param disType
	 * 			设置分发方式（0为直接分发，1为补发，2为移除，3为转发，4为回收，5为销毁）
	 */
	public void setDisType(String disType) {
		this.disType = disType;
	}

	/**
	 * @return 备注
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return 分发数据编号
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @return 分发数据名称
	 */
	public String getDisObjName() {
		return disObjName;
	}

	/**
	 * @return 所属任务名称
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * @return 分发信息完工期限
	 */
	public long getDisDeadLine() {
		return disDeadLine;
	}

	/**
	 * @return 紧急程度（0为普通，1为加急）
	 */
	public String getDisUrgent() {
		return disUrgent;
	}


	/**
	 * @return 发放数据ID
	 */
	public String getDisObjId() {
		return disObjId;
	}

	/**
	 * @return 发放数据对象
	 */
	public DistributeObject getDistributeObject() {
		return distributeObject;
	}

	/**
	 * @return 发放数据对象LINK
	 */
	public DistributeOrderObjectLink getDistributeOrderObjectLink() {
		return distributeOrderObjectLink;
	}

	/**
	 * @param lifeCycleInfo
	 *			设置 lifeCycleInfo
	 */
	public void setLifeCycleInfo(LifeCycleInfo lifeCycleInfo) {
		this.lifeCycleInfo = lifeCycleInfo;
	}

	/**
	 * @param manageInfo
	 *			设置 manageInfo
	 */
	public void setManageInfo(ManageInfo manageInfo) {
		this.manageInfo = manageInfo;
	}

	/**
	 * @param oids
	 *			设置 oids
	 */
	public void setOids(String oids) {
		this.oids = oids;
	}

	/**
	 * @param disOrderObjectLinkId
	 *			设置分发数据LINK内部标识
	 */
	public void setDisOrderObjectLinkId(String disOrderObjectLinkId) {
		this.disOrderObjectLinkId = disOrderObjectLinkId;
	}

	/**
	 * @param disOrderObjectLinkClassId
	 *			设置分发数据LINK类标识
	 */
	public void setDisOrderObjectLinkClassId(String disOrderObjectLinkClassId) {
		this.disOrderObjectLinkClassId = disOrderObjectLinkClassId;
	}

	/**
	 * @param needDestroyNum
	 *			设置 需要销毁的份数
	 */
	public void setNeedDestroyNum(long needDestroyNum) {
		this.needDestroyNum = needDestroyNum;
	}

	/**
	 * @param needRecoverNum
	 *			设置 需要回收的份数
	 */
	public void setNeedRecoverNum(long needRecoverNum) {
		this.needRecoverNum = needRecoverNum;
	}

	/**
	 * @param disInfoNum
	 *			设置分发份数
	 */
	public void setDisInfoNum(long disInfoNum) {
		this.disInfoNum = disInfoNum;
	}

	/**
	 * @param destroyNum
	 *			设置 销毁份数
	 */
	public void setDestroyNum(long destroyNum) {
		this.destroyNum = destroyNum;
	}

	/**
	 * @param recoverNum
	 *			设置 回收份数
	 */
	public void setRecoverNum(long recoverNum) {
		this.recoverNum = recoverNum;
	}

	/**
	 * @param sendTime
	 *			设置分发信息变为已分发状态的时间
	 */
	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * @param disInfoName
	 *			设置 分发信息名称（单位/人员）
	 */
	public void setDisInfoName(String disInfoName) {
		this.disInfoName = disInfoName;
	}
	
	/**
	 * @param disInfoId
	 *			设置分发信息IID（人员或组织的内部标识）
	 */
	public void setDisInfoId(String disInfoId) {
		this.disInfoId = disInfoId;
	}

	/**
	 * @param infoClassId
	 *			设置 分发信息的类标识（人员或者组织的类标识）
	 */
	public void setInfoClassId(String infoClassId) {
		this.infoClassId = infoClassId;
	}


	/**
	 * @param disInfoType
	 *			设置分发信息类型（0为单位，1为人员） 
	 */
	public void setDisInfoType(String disInfoType) {
		this.disInfoType = disInfoType;
	}

	/**
	 * @param disMediaType
	 *			设置 分发介质类型（0为纸质，1为电子，2为跨域）
	 */
	public void setDisMediaType(String disMediaType) {
		this.disMediaType = disMediaType;
	}

	/**
	 * @param note
	 *			设置 备注
	 */
	public void setNote(String note) {
		this.note = note;
	}
	
	/**
	 * @param number
	 *			设置 分发数据编号
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @param disObjName
	 *			设置 分发数据名称
	 */
	public void setDisObjName(String disObjName) {
		this.disObjName = disObjName;
	}

	/**
	 * @param taskName
	 *			设置所属任务名称 
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * @param disDeadLine
	 *			设置分发信息完工期限
	 */
	public void setDisDeadLine(long disDeadLine) {
		this.disDeadLine = disDeadLine;
	}

	/**
	 * @param disUrgent
	 *			设置 紧急程度（0为普通，1为加急）
	 */
	public void setDisUrgent(String disUrgent) {
		this.disUrgent = disUrgent;
	}


	/**
	 * @param disObjId
	 *			设置 发放数据ID
	 */
	public void setDisObjId(String disObjId) {
		this.disObjId = disObjId;
	}

	/**
	 * @param DistributeObject
	 *			设置 发放数据对象
	 */
	public void setDistributeObject(DistributeObject distributeObject) {
		this.distributeObject = distributeObject;
	}

	/**
	 * @param DistributeOrderObjectLink
	 *			设置 发放数据对象LINK
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

	//添加OID
	public void addOid(String oid) {
		if (oids != null) {
			if (oid != null) {
				oids += "," + oid;
			}
		} else {
			oids = oid;
		}
	}

	//需要回收
	public String getKey() {
		String result = this.disInfoId.hashCode() + ";"
				+ this.disMediaType.hashCode() + ";"
				+ String.valueOf(this.disInfoNum).hashCode() + ";"
				+ String.valueOf(this.recoverNum).hashCode() + ";"
				+ String.valueOf(this.needRecoverNum).hashCode();

		// 备注
		if (this.note != null && this.note.length() > 0) {
			result = result + ";" + this.note.hashCode();
		} else {
			result = result + ";";
		}

		return result;
	}

	//需要销毁
	public String getKey2() {
		String result = this.disInfoId.hashCode() + ";"
				+ this.disMediaType.hashCode() + ";"
				+ String.valueOf(this.disInfoNum).hashCode() +";"
				+ String.valueOf(this.destroyNum).hashCode() +";"
				+ String.valueOf(this.needDestroyNum).hashCode();
		
		// 备注
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
