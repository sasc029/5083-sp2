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
 * ������������ģ�͡�
 * 
 * @author guowei 2013-2-22
 *
 */
public abstract class DistributeTask extends ATObject implements Identified, Contexted, Manageable,
		LifeCycleManaged {

	/** serialVersionUID */
	private static final long serialVersionUID = -7199958720979571929L;

	/** �������ͣ�0��ֽ������1���������� */
	private String taskType;
	/** ����Ϣ */
	private DomainInfo domainInfo;
	/** ������Ϣ */
	private ManageInfo manageInfo;
	/** ��� */
	private String number;
	/** ���� */
	private String name;
	/** ��������Ϣ */
	private ContextInfo contextInfo;
	/** ��ע */
	private String note;
	/** ������λ */
	private String creatUnit;
	/** �������ŵ� */
	private String orderName;
	/** �����̶ȣ�0Ϊ��ͨ��1Ϊ�Ӽ��� */
	private String disUrgent;
	/** �ַ���Ϣ�깤���� */
	private long disDeadLine;
	/** ��������ĵ�λ */
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
	 * @param taskType Ҫ���õ� taskType
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
