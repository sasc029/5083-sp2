package com.bjsasc.adm.active.model.query;


import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.type.ATObject;

/**
 * ����ͳ�Ʋ�ѯģ�͡�
 * 
 * @author yanjia 2013-5-13
 */
public class ActiveQuerySchema extends ATObject  implements Manageable{

	/** serialVersionUID */
	private static final long serialVersionUID = 7276840297608674130L;
	
	public static final String CLASSID = ActiveQuerySchema.class.getSimpleName();


	// ��������
	private String schemaName;
	// ���ж���
	private String activename;
	// ��ʼʱ��
	private String beginTime;

	// ����ʱ��
	private String endTime;

	// ��������
	private String lifeCycle;

	// ��ģ��ID
	private String activeModelId;
	//��ģ������
	private String activeModelname;

	public String getActiveModelname() {
		return activeModelname;
	}

	public void setActiveModelname(String activeModelname) {
		this.activeModelname = activeModelname;
	}

	// �Ƿ���Ĭ�Ϸ���
	private int defaultSchema;


	// չʾ����
	private String groupCondition;


	/**
	 * @return the beginTime
	 */
	public String getBeginTime() {
		return beginTime;
	}

	/**
	 * @param beginTime
	 *            the beginTime to set
	 */
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the lifeCycle
	 */
	public String getLifeCycle() {
		return lifeCycle;
	}

	/**
	 * @param lifeCycle
	 *            the lifeCycle to set
	 */
	public void setLifeCycle(String lifeCycle) {
		this.lifeCycle = lifeCycle;
	}



	public int getDefaultSchema() {
		return defaultSchema;
	}

	public void setDefaultSchema(int defaultSchema) {
		this.defaultSchema = defaultSchema;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getGroupCondition() {
		return groupCondition;
	}

	public String getActiveModelId() {
		return activeModelId;
	}

	public void setActiveModelId(String activeModelId) {
		this.activeModelId = activeModelId;
	}

	public void setGroupCondition(String groupCondition) {
		this.groupCondition = groupCondition;
	}

	public String getActivename() {
		return activename;
	}

	/**
	 * ����������Ϣ
	 */
	private ManageInfo manageInfo ;

	public ManageInfo getManageInfo() {
		return manageInfo;
	}

	public void setManageInfo(ManageInfo manageInfo) {
		this.manageInfo = manageInfo;
	}

	public void setActivename(String activename) {
		this.activename = activename;
	}
}
