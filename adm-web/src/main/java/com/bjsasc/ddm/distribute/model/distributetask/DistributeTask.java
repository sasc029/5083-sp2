package com.bjsasc.ddm.distribute.model.distributetask;

import com.bjsasc.ddm.common.SessionDataUtil;
import com.bjsasc.plm.core.context.model.ContextInfo;
import com.bjsasc.plm.core.context.model.Contexted;
import com.bjsasc.plm.core.domain.DomainInfo;
import com.bjsasc.plm.core.identifier.Identified;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.type.ATObject;
import com.bjsasc.plm.core.util.DateTimeUtil;

/**
 * 发放任务数据模型。
 * 
 * @author guowei 2013-2-22
 *
 */
public abstract class DistributeTask extends ATObject implements Identified, Contexted, Manageable,
		LifeCycleManaged {

	/** serialVersionUID */
	private static final long serialVersionUID = -7199958720979571929L;

	/** 任务类型（0：纸质任务，1：电子任务） */
	private String taskType;
	/** 域信息 */
	private DomainInfo domainInfo;
	/** 管理信息 */
	private ManageInfo manageInfo;
	/** 编号 */
	private String number;
	/** 名称 */
	private String name;
	/** 上下文信息 */
	private ContextInfo contextInfo;
	/** 备注 */
	private String note;
	/** 创建单位 */
	private String creatUnit;
	/** 所属发放单 */
	private String orderName;
	/** 紧急程度（0为普通，1为加急） */
	private String disUrgent;
	/** 分发信息完工期限 */
	private long disDeadLine;
	/** 创建任务的单位 */
	private SessionDataUtil organization;

	public SessionDataUtil getOrganization() {
		return organization;
	}

	public void setOrganization(SessionDataUtil organization) {
		this.organization = organization;
	}

	public DomainInfo getDomainInfo() {
		return domainInfo;
	}

	public void setDomainInfo(DomainInfo domainInfo) {
		this.domainInfo = domainInfo;
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

	public ManageInfo getManageInfo() {
		return manageInfo;
	}

	public void setManageInfo(ManageInfo manageInfo) {
		this.manageInfo = manageInfo;
	}

	public String getCreateByName() {
		if (manageInfo != null) {
			User user = manageInfo.getCreateBy();
			if (user != null) {
				return user.getName();
			}
		}
		return "";
	}

	public String getCreateTime() {
		return DateTimeUtil.getDateDisplay(manageInfo.getCreateTime());
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ContextInfo getContextInfo() {
		return contextInfo;
	}

	public void setContextInfo(ContextInfo contextInfo) {
		this.contextInfo = contextInfo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCreatUnit() {
		return creatUnit;
	}

	public void setCreatUnit(String creatUnit) {
		this.creatUnit = creatUnit;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getDisUrgent() {
		return disUrgent;
	}

	public void setDisUrgent(String disUrgent) {
		this.disUrgent = disUrgent;
	}

	public long getDisDeadLine() {
		return disDeadLine;
	}

	public void setDisDeadLine(long disDeadLine) {
		this.disDeadLine = disDeadLine;
	}

	@Override
	public String toString() {
		return "DistributeTask [taskType=" + taskType + ", domainInfo=" + domainInfo + ", manageInfo=" + manageInfo
				+ ", number=" + number + ", name=" + name + ", contextInfo=" + contextInfo + ", note=" + note + ", creatUnit="
				+ creatUnit + ", orderName=" + orderName + ", disUrgent=" + disUrgent + "]" + super.toString();
	}

}
