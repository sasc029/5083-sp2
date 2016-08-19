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
 * ������������ģ�͡�
 * 
 * @author gengancong 2013-3-18
 */
@SuppressWarnings("deprecation")
public class DistributeElecTask extends DistributeTask{

	/** serialVersionUID */
	private static final long serialVersionUID = 3905059021527421394L;

	/**
	 * ���췽����
	 */
	public DistributeElecTask() {

	}

	private String taskType = ConstUtil.C_TASKTYPE_ELEC;

	public static final String CLASSID = DistributeElecTask.class.getSimpleName();

	/** ��������������Ϣ���� */
	private LifeCycleInfo lifeCycleInfo = new LifeCycleInfo();
	/** ���� */
	private String operates = "ǩ��,��ǩ";
	
	/** ���� */
	private String operate = "ת��";

	/** �ͺ� */
	private String dataFrom;

	/** �����̶ȣ�0Ϊ��ͨ��1Ϊ�Ӽ��� */
	private String disUrgent;

	/** �ַ���Ϣ��Ϊ�ѷַ�״̬��ʱ�� */
	private long sendTime;

	/** ����ԭ�� */
	private String returnReason;

	/** ������ */
	private ObjectReference receiveByRef;

	/** ����ʱ�� */
	private long receiveTime;
	
	/** �������ڲ���ʶ */
	private String fromTaskId;
	
	/** �������ڲ���ʾ */
	private String fromTaskClassId;
	
    /** �������ŵ� */
	private String orderName;
	
	/** �������ŵ�OID */
	private String orderOid;
	
	/** �ַ���Ϣ */
	private DistributeInfo disinfo;
	
	/** �������ͣ�0Ϊ���ڣ�1Ϊ����,2Ϊ������ */
	private String elecTaskType;
	
	/** ����0Ϊ�������ģ�1Ϊ���շ��� */
	private String outSiteType;
	
	/** Դվ���ڲ���ʶ */
	private String sourceSiteId;
	
	/** Դվ�����ʶ */
	private String sourceSiteClassId;
	
	/** Դվ������ */
	private String sourceSiteName;
	
	/** Ŀ��վ���ڲ���ʶ */
	private String targetSiteId;
	
	/** Ŀ��վ�����ʶ */
	private String targetSiteClassId;
	
	/** Ŀ��վ������ */
	private String targetSiteName;
	
	/** ����վ���ڲ���ʶ */
	private String centerSiteId;
	
	/** ����վ�����ʶ */
	private String centerSiteClassId;
	
	/** ���������� */
	private String receiveByName;
	
	/** �������� */
	private ReturnReason elecReturnReason;
	
	/** �ַ���Ϣ���ƣ���λ/��Ա�� */
	private String disInfoName;
	
	/**�ַ���Ϣ��innerid */
	private String disInfoId;
	
	/**�ַ���Ϣ��classid */
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
	 * @param sendTime Ҫ���õ� sendTime
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
	 * @param fromTaskId Ҫ���õ� fromTaskId
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
	 * @param fromTaskClassId Ҫ���õ� fromTaskClassId
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
	 * @param returnReason Ҫ���õ� returnReason
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
	 * @param receiveByRef Ҫ���õ� receiveByRef
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
	 * @param receiveTime Ҫ���õ� receiveTime
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
	 * @param taskType Ҫ���õ� taskType
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
