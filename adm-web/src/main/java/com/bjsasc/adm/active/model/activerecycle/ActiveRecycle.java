package com.bjsasc.adm.active.model.activerecycle;

import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleInfo;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.type.ATObject;

/**
 * 回收站模型。
 * 
 * @author gengancong 2013-2-22
 */
public class ActiveRecycle extends ATObject implements LifeCycleManaged {

	/** serialVersionUID */
	private static final long serialVersionUID = 85440203708682482L;

	/** CLASSID */
	public static final String CLASSID = ActiveRecycle.class.getSimpleName();

	/** 生命周期  */
	private LifeCycleInfo lifeCycleInfo;
	/** 回收对象的id */
	private String itemId;
	/** 回收对象类型 */
	private String itemClassId;
	/** 浏览对象属性页面的url */
	private String itemPropertiesUrl;
	/** 模型id */
	private String modelId;
	/** 模型类型 */
	private String modelClassId;
	/** 编号  */
	private String number;
	/** 名称  */
	private String name;
	/** 密级 */
	private String securityLevel;
	/** 版本  */
	private String versionNo;
	/** 位置 */
	private String folderPath;
	/** 模型 */
	private String modelName;

	/**
	 * 取得生命周期
	 * @return lifeCycleInfo
	 */
	public LifeCycleInfo getLifeCycleInfo() {
		return lifeCycleInfo;
	}

	/**
	 * 设置生命周期
	 * @param lifeCycleInfo 要设置的 lifeCycleInfo
	 */
	public void setLifeCycleInfo(LifeCycleInfo lifeCycleInfo) {
		this.lifeCycleInfo = lifeCycleInfo;
	}

	/**
	 * @return itemId
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId 要设置的 itemId
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return itemClassId
	 */
	public String getItemClassId() {
		return itemClassId;
	}

	/**
	 * @param itemClassId 要设置的 itemClassId
	 */
	public void setItemClassId(String itemClassId) {
		this.itemClassId = itemClassId;
	}

	/**
	 * @return modelId
	 */
	public String getModelId() {
		return modelId;
	}

	/**
	 * @param modelId 要设置的 modelId
	 */
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	/**
	 * @return modelClassId
	 */
	public String getModelClassId() {
		return modelClassId;
	}

	/**
	 * @param modelClassId 要设置的 modelClassId
	 */
	public void setModelClassId(String modelClassId) {
		this.modelClassId = modelClassId;
	}

	/**
	 * @return itemPropertiesUrl
	 */
	public String getItemPropertiesUrl() {
		return itemPropertiesUrl;
	}

	/**
	 * @param itemPropertiesUrl 要设置的 itemPropertiesUrl
	 */
	public void setItemPropertiesUrl(String itemPropertiesUrl) {
		this.itemPropertiesUrl = itemPropertiesUrl;
	}

	/**
	 * @return versionNo
	 */
	public String getVersionNo() {
		return versionNo;
	}

	/**
	 * @param versionNo 要设置的 versionNo
	 */
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	/**
	 * @return number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number 要设置的 number
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 要设置的 name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return securityLevel
	 */
	public String getSecurityLevel() {
		return securityLevel;
	}

	/**
	 * @param securityLevel 要设置的 securityLevel
	 */
	public void setSecurityLevel(String securityLevel) {
		this.securityLevel = securityLevel;
	}

	/**
	 * @return folderPath
	 */
	public String getFolderPath() {
		return folderPath;
	}

	/**
	 * @param folderPath 要设置的 folderPath
	 */
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	/**
	 * @return modelName
	 */
	public String getModelName() {
		return modelName;
	}

	/**
	 * @param modelName 要设置的 modelName
	 */
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

}
