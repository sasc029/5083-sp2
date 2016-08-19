package com.bjsasc.ddm.distribute.model.distributeelectask;
import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributetask.DistributeTask;
import com.bjsasc.ddm.distribute.model.returnreason.ReturnReason;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleInfo;
import com.bjsasc.platform.objectmodel.business.persist.ObjectReference;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.system.principal.User;

/**
 * 电子任务数据模型。
 * 
 * @author gengancong 2013-3-18
 */
@SuppressWarnings("deprecation")
public class DistributeElecTask extends DistributeTask{

	/** serialVersionUID */
	private static final long serialVersionUID = 3905059021527421394L;

	/**
	 * 构造方法。
	 */
	public DistributeElecTask() {

	}

	private String taskType = ConstUtil.C_TASKTYPE_ELEC;

	public static final String CLASSID = DistributeElecTask.class.getSimpleName();

	/** 生命周期属性信息对象 */
	private LifeCycleInfo lifeCycleInfo = new LifeCycleInfo();
	/** 操作 */
	private String operates = "签收,拒签";
	
	/** 操作 */
	private String operate = "转发";

	/** 型号 */
	private String dataFrom;

	/** 紧急程度（0为普通，1为加急） */
	private String disUrgent;

	/** 分发信息变为已分发状态的时间 */
	private long sendTime;

	/** 回退原因 */
	private String returnReason;

	/** 接受人 */
	private ObjectReference receiveByRef;

	/** 接受时间 */
	private long receiveTime;
	
	/** 父任务内部标识 */
	private String fromTaskId;
	
	/** 父任务内部表示 */
	private String fromTaskClassId;
	
    /** 所属发放单 */
	private String orderName;
	
	/** 所属发放单OID */
	private String orderOid;
	
	/** 分发信息 */
	private DistributeInfo disinfo;
	
	/** 任务类型（0为域内，1为域外,2为反馈） */
	private String elecTaskType;
	
	/** 外域（0为数据中心，1为接收方） */
	private String outSiteType;
	
	/** 源站点内部标识 */
	private String sourceSiteId;
	
	/** 源站点类标识 */
	private String sourceSiteClassId;
	
	/** 源站点名称 */
	private String sourceSiteName;
	
	/** 目标站点内部标识 */
	private String targetSiteId;
	
	/** 目标站点类标识 */
	private String targetSiteClassId;
	
	/** 目标站点名称 */
	private String targetSiteName;
	
	/** 中心站点内部标识 */
	private String centerSiteId;
	
	/** 中心站点类标识 */
	private String centerSiteClassId;
	
	/** 接收者名称 */
	private String receiveByName;
	
	/** 回退理由 */
	private ReturnReason elecReturnReason;
	
	/** 分发信息名称（单位/人员） */
	private String disInfoName;
	
	/**分发信息的innerid */
	private String disInfoId;
	
	/**分发信息的classid */
	private String infoClassId;
	
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

	public String getDisInfoName() {
		return disInfoName;
	}

	public void setDisInfoName(String disInfoName) {
		this.disInfoName = disInfoName;
	}
	
	public ReturnReason getElecReturnReason() {
		return elecReturnReason;
	}

	public void setElecReturnReason(ReturnReason elecReturnReason) {
		this.elecReturnReason = elecReturnReason;
	}

	public String getReceiveByName() {
		return receiveByName;
	}

	public void setReceiveByName(String receiveByName) {
		this.receiveByName = receiveByName;
	}

	public String getCenterSiteId() {
		return centerSiteId;
	}

	public void setCenterSiteId(String centerSiteId) {
		this.centerSiteId = centerSiteId;
	}

	public String getCenterSiteClassId() {
		return centerSiteClassId;
	}

	public void setCenterSiteClassId(String centerSiteClassId) {
		this.centerSiteClassId = centerSiteClassId;
	}

	public String getSourceSiteId() {
		return sourceSiteId;
	}

	public void setSourceSiteId(String sourceSiteId) {
		this.sourceSiteId = sourceSiteId;
	}

	public String getSourceSiteClassId() {
		return sourceSiteClassId;
	}

	public void setSourceSiteClassId(String sourceSiteClassId) {
		this.sourceSiteClassId = sourceSiteClassId;
	}

	public String getSourceSiteName() {
		return sourceSiteName;
	}

	public void setSourceSiteName(String sourceSiteName) {
		this.sourceSiteName = sourceSiteName;
	}

	public String getTargetSiteId() {
		return targetSiteId;
	}

	public void setTargetSiteId(String targetSiteId) {
		this.targetSiteId = targetSiteId;
	}

	public String getTargetSiteClassId() {
		return targetSiteClassId;
	}

	public void setTargetSiteClassId(String targetSiteClassId) {
		this.targetSiteClassId = targetSiteClassId;
	}

	public String getTargetSiteName() {
		return targetSiteName;
	}

	public void setTargetSiteName(String targetSiteName) {
		this.targetSiteName = targetSiteName;
	}

	public String getOutSiteType() {
		return outSiteType;
	}

	public void setOutSiteType(String outSiteType) {
		this.outSiteType = outSiteType;
	}

	public String getElecTaskType() {
		return elecTaskType;
	}

	public void setElecTaskType(String elecTaskType) {
		this.elecTaskType = elecTaskType;
	}

	public DistributeInfo getDisinfo() {
		return disinfo;
	}

	public void setDisinfo(DistributeInfo disinfo) {
		this.disinfo = disinfo;
	}

	public String getOrderOid() {
		return orderOid;
	}

	public void setOrderOid(String orderOid) {
		this.orderOid = orderOid;
	}

	public String getOrderName() {
		return orderName;
	}
	
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	
	public String getStateName() {
		if (lifeCycleInfo == null) {
			return "";
		}
		String stateName = lifeCycleInfo.getStateName();
		if (stateName == null) {
			return "";
		}
		return stateName;
	}



	public String getDisUrgent() {
		return disUrgent;
	}

	public void setDisUrgent(String disUrgent) {
		this.disUrgent = disUrgent;
	}

	public String getOperates() {
		return operates;
	}

	public void setOperates(String operates) {
		this.operates = operates;
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

	public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
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
	 * @return fromTaskId
	 */
	public String getFromTaskId() {
		return fromTaskId;
	}

	/**
	 * @param fromTaskId 要设置的 fromTaskId
	 */
	public void setFromTaskId(String fromTaskId) {
		this.fromTaskId = fromTaskId;
	}

	/**
	 * @return fromTaskClassId
	 */
	public String getFromTaskClassId() {
		return fromTaskClassId;
	}

	/**
	 * @param fromTaskClassId 要设置的 fromTaskClassId
	 */
	public void setFromTaskClassId(String fromTaskClassId) {
		this.fromTaskClassId = fromTaskClassId;
	}

	/**
	 * @return returnReason
	 */
	public String getReturnReason() {
		if (lifeCycleInfo != null && ConstUtil.LC_REFUSE_SIGNED.getName().equals(lifeCycleInfo.getStateName())) {
			Object oid = getOid();
			String sql = "SELECT MAX (T.RETURNREASON) AS RETURNREASON FROM DDM_DIS_RETURN T WHERE T.TASKCLASSID || ':' || T.TASKID = ?";
			List<Map<String, Object>> reasonList = Helper.getPersistService().query(sql, oid);
			if (reasonList == null || reasonList.isEmpty()) {
				return returnReason;
			}
			Map<String, Object> map = reasonList.get(0);
			if (map != null) {
				Object object = map.get("RETURNREASON");
				if (object != null) {
					returnReason = object.toString();
				}
			}
		}
		return returnReason;
	}

	/**
	 * @param returnReason 要设置的 returnReason
	 */
	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	/**
	 * @return receiveByRef
	 */
	public ObjectReference getReceiveByRef() {
		return receiveByRef;
	}

	/**
	 * @param receiveByRef 要设置的 receiveByRef
	 */
	public void setReceiveByRef(ObjectReference receiveByRef) {
		this.receiveByRef = receiveByRef;
	}

	/**
	 * @return receiveTime
	 */
	public long getReceiveTime() {
		return receiveTime;
	}

	/**
	 * @param receiveTime 要设置的 receiveTime
	 */
	public void setReceiveTime(long receiveTime) {
		this.receiveTime = receiveTime;
	}

	public User getReceiveBy() {
		return (User) PersistHelper.getService().getObject(receiveByRef);
	}

	public void setReceiveBy(User receiveBy) {
		this.receiveByRef = ObjectReference.newObjectReference(receiveBy);
	}

	/**
	 * @return taskType
	 */
	public String getTaskType() {
		return taskType;
	}

	/**
	 * @param taskType 要设置的 taskType
	 */
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getOid() {
		return this.getClassId() + ":" + this.getInnerId();
	}
	
	public DistributeElecTask clone(DistributeElecTask task) {
		task.setNumber(getNumber());
		task.setName(getName());
		task.setNote(getNote());
		task.setManageInfo(getManageInfo());
		task.setContextInfo(getContextInfo());
		task.setDomainInfo(getDomainInfo());
		task.setLifeCycleInfo(getLifeCycleInfo());
		task.setPersistInfo(getPersistInfo());
		return task;
	}

	@Override
	public String toString() {
		return "DistributeElecTask [elecTaskType=" + elecTaskType + ", taskType=" + taskType + ", lifeCycleInfo="
				+ lifeCycleInfo + ", operates=" + operates + ", operate="
				+ operate + ", dataFrom=" + dataFrom + ", disUrgent="
				+ disUrgent + ", sendTime=" + sendTime + ", returnReason="
				+ returnReason + ", receiveByRef=" + receiveByRef
				+ ", receiveTime=" + receiveTime + ", fromTaskId=" + fromTaskId
				+ ", receiveByName=" + receiveByName
				+ ", fromTaskClassId=" + fromTaskClassId + "]"+super.toString();
	}

}
