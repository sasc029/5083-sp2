package com.bjsasc.ddm.distribute.model.distributetaskinfolink;

import com.bjsasc.plm.core.type.ATLink;

/**
 * ������ַ���Ϣlink����ģ�͡�
 * 
 * @author gengancong 2013-2-22
 */
@SuppressWarnings("deprecation")
public class DistributeTaskInfoLink extends ATLink {

	/** serialVersionUID */
	private static final long serialVersionUID = 5414526469688358794L;

	/**
	 * ���췽����
	 */
	public DistributeTaskInfoLink() {
		setClassId(CLASSID);
	}

	/** CLASSID */
	public static final String CLASSID = DistributeTaskInfoLink.class.getSimpleName();

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
