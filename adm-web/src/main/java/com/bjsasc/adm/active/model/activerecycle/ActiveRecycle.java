package com.bjsasc.adm.active.model.activerecycle;

import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleInfo;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.type.ATObject;

/**
 * ����վģ�͡�
 * 
 * @author gengancong 2013-2-22
 */
public class ActiveRecycle extends ATObject implements LifeCycleManaged {

	/** serialVersionUID */
	private static final long serialVersionUID = 85440203708682482L;

	/** CLASSID */
	public static final String CLASSID = ActiveRecycle.class.getSimpleName();

	/** ��������  */
	private LifeCycleInfo lifeCycleInfo;
	/** ���ն����id */
	private String itemId;
	/** ���ն������� */
	private String itemClassId;
	/** �����������ҳ���url */
	private String itemPropertiesUrl;
	/** ģ��id */
	private String modelId;
	/** ģ������ */
	private String modelClassId;
	/** ���  */
	private String number;
	/** ����  */
	private String name;
	/** �ܼ� */
	private String securityLevel;
	/** �汾  */
	private String versionNo;
	/** λ�� */
	private String folderPath;
	/** ģ�� */
	private String modelName;

	/**
	 * ȡ����������
	 * @return lifeCycleInfo
	 */
	public LifeCycleInfo getLifeCycleInfo() {
		return lifeCycleInfo;
	}

	/**
	 * ������������
	 * @param lifeCycleInfo Ҫ���õ� lifeCycleInfo
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
	 * @param itemId Ҫ���õ� itemId
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
	 * @param itemClassId Ҫ���õ� itemClassId
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
	 * @param modelId Ҫ���õ� modelId
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
	 * @param modelClassId Ҫ���õ� modelClassId
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
	 * @param itemPropertiesUrl Ҫ���õ� itemPropertiesUrl
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
	 * @param versionNo Ҫ���õ� versionNo
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
	 * @param number Ҫ���õ� number
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
	 * @param name Ҫ���õ� name
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
	 * @param securityLevel Ҫ���õ� securityLevel
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
	 * @param folderPath Ҫ���õ� folderPath
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
	 * @param modelName Ҫ���õ� modelName
	 */
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

}
