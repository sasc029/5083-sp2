package com.bjsasc.ddm.distribute.model.distributetaskdomainlink;

import com.bjsasc.plm.core.type.ATLink;

/**
 * ��������������ַ���Ϣlink����ģ�͡�
 * 
 * @author guowei 2013-10-15
 */
@SuppressWarnings("deprecation")
public class DistributeTaskDomainLink extends ATLink {

	/** serialVersionUID */
	private static final long serialVersionUID = -5214300708141803385L;

	/**
	 * ���췽����
	 */
	public DistributeTaskDomainLink() {
		setClassId(CLASSID);
	}

	/** CLASSID */
	public static final String CLASSID = DistributeTaskDomainLink.class.getSimpleName();

	/** �������ͣ�0��ֽ������1���������� */
	private String taskType;

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
}
