package com.bjsasc.ddm.distribute.model.distributecommonconfig;

import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.type.ATObject;

/**
 * 系统集成相关配置数据模型。
 * 
 * @author zhangguoqiang 2014-7-11
 */
public class DistributeCommonConfig extends ATObject implements Manageable{

	/** serialVersionUID */
	private static final long serialVersionUID = -360222856983018114L;

	/** CLASSID */
	public static final String CLASSID = DistributeCommonConfig.class.getSimpleName();

	/**
	 * 配置编号
	 */
	private String configID;
	
	/**
	 * 配置名称
	 */
	private String configName;
	
	/**
	 * 配置值
	 */
	private String configValue;
	
	/**
	 * 配置默认值
	 */
	private String configDefaultValue;
	
	/**
	 * 是否允许删除
	 */
	private int isPermitDelete;
    
	/**
	 * 组合属性
	 */
	private ManageInfo manageInfo;
	
	
	public DistributeCommonConfig() {
		setClassId(DistributeCommonConfig.class.getSimpleName());
	}
	
	/**
	 * 获取配置编号
	 */
	public String getConfigID() {
		return configID;
	}

	/**
	 * 设置配置编号
	 */
	public void setConfigID(String configID) {
		this.configID = configID;
	}

	/**
	 * 获取配置名称
	 */
	public String getConfigName() {
		return configName;
	}

	/**
	 * 设置配置名称
	 */
	public void setConfigName(String configName) {
		this.configName = configName;
	}

	/**
	 * 获取配置值
	 */
	public String getConfigValue() {
		return configValue;
	}

	/**
	 * 设置配置值
	 */
	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	/**
	 * 获取配置默认值
	 */
	public String getConfigDefaultValue() {
		return configDefaultValue;
	}

	/**
	 * 设置配置默认值
	 */
	public void setConfigDefaultValue(String configDefaultValue) {
		this.configDefaultValue = configDefaultValue;
	}

	/**
	 * 获取是否允许删除
	 */
	public int getIsPermitDelete() {
		return isPermitDelete;
	}

	/**
	 * 设置是否允许删除
	 */
	public void setIsPermitDelete(int isPermitDelete) {
		this.isPermitDelete = isPermitDelete;
	}

	/**
	 * 设置组合属性
	 */
	public void setManageInfo(ManageInfo manageInfo) {
		this.manageInfo = manageInfo;

	}
	
	/**
	 * 获取组合属性
	 */
	public ManageInfo getManageInfo() {
		return this.manageInfo;
	}
}
