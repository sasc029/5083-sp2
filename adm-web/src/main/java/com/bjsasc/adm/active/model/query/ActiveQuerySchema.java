package com.bjsasc.adm.active.model.query;


import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.type.ATObject;

/**
 * 现行统计查询模型。
 * 
 * @author yanjia 2013-5-13
 */
public class ActiveQuerySchema extends ATObject  implements Manageable{

	/** serialVersionUID */
	private static final long serialVersionUID = 7276840297608674130L;
	
	public static final String CLASSID = ActiveQuerySchema.class.getSimpleName();


	// 方案名称
	private String schemaName;
	// 现行对象
	private String activename;
	// 开始时间
	private String beginTime;

	// 结束时间
	private String endTime;

	// 生命周期
	private String lifeCycle;

	// 子模型ID
	private String activeModelId;
	//子模型名称
	private String activeModelname;

	public String getActiveModelname() {
		return activeModelname;
	}

	public void setActiveModelname(String activeModelname) {
		this.activeModelname = activeModelname;
	}

	// 是否是默认方案
	private int defaultSchema;


	// 展示条件
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
	 * 基础管理信息
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
