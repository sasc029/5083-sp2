package com.bjsasc.ddm.distribute.model.distributeworkload;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.common.LifeCycleUtil;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleInfo;
import com.bjsasc.platform.objectmodel.business.persist.ObjectReference;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.type.ATObject;

/**
 * 工作量统计数据模型。
 *
 * @author gengancong 2013-3-21
 */
public class DistributeWorkload extends ATObject implements Manageable {

	/** serialVersionUID */
	private static final long serialVersionUID = -9183215143236229193L;

	/**
	 * 构造方法。
	 */
	public DistributeWorkload() {

	}

	public static final String CLASSID = DistributeWorkload.class.getSimpleName();

	/** 生命周期属性信息对象 */
	private LifeCycleInfo fromLifeCycleInfo = new LifeCycleInfo();

	/** 生命周期属性信息对象 */
	private LifeCycleInfo toLifeCycleInfo = new LifeCycleInfo();

	private ObjectReference distributeData = new ObjectReference();

	/** 管理信息 */
	private ManageInfo manageInfo;

	/** objectId */
	private String objectId;

	/** objectClassId */
	private String objectClassId;

	/** 原生命周期标识 */
	private String fromStateId;
	/** 原生命周期名称 */
	private String fromStateName;
	/** 原生命周期模板 */
	private String fromLifeCycleTemplate;
	/** 现生命周期标识 */
	private String toStateId;
	/** 现生命周期名称 */
	private String toStateName;
	/** 现生命周期模板 */
	private String toLifeCycleTemplate;
	/** 编号  */
	private String number;
	/** 名称  */
	private String name;
	/** 查询状态  */
	private String lifeCycleType;

	private String fromLifeCycleTemplateName;
	private String toLifeCycleTemplateName;

	public boolean check() {
		if (ConstUtil.LC_SCHEDULING.getName().equals(fromStateName)) {
			return true;
		} else if (ConstUtil.LC_PROCESSING.getName().equals(fromStateName)) {
			return true;
		} else if(ConstUtil.LC_DUPLICATED.getName().equals(fromStateName)){
			return true;
		} else if(ConstUtil.LC_DISTRIBUTING.getName().equals(fromStateName) && ConstUtil.LC_DISTRIBUTED.getName().equals(toStateName)){
			return true;
		}
		return false;
	}

	/**
	 * @return distributeData
	 */
	public ObjectReference getDistributeData() {
		loadDistributeData();
		return distributeData;
	}

	/**
	 * @param distributeData 要设置的 distributeData
	 */
	public void setDistributeData(ObjectReference distributeData) {
		this.distributeData = distributeData;
	}

	private void loadDistributeData() {
		if (objectClassId != null && objectId != null) {
			String oid = this.objectClassId + ":" + this.objectId;
			distributeData.setObject(Helper.getPersistService().getObject(oid));
			distributeData.setClassId(this.objectClassId);
			distributeData.setInnerId(this.objectId);
		}
	}

	/**
	 * @return fromLifeCycleInfo
	 */
	public LifeCycleInfo getFromLifeCycleInfo() {
		return fromLifeCycleInfo;
	}

	/**
	 * @param fromLifeCycleInfo 要设置的 fromLifeCycleInfo
	 */
	public void setFromLifeCycleInfo(LifeCycleInfo fromLifeCycleInfo) {
		this.fromLifeCycleInfo = fromLifeCycleInfo;
		if (fromLifeCycleInfo != null) {
			fromStateId = fromLifeCycleInfo.getStateId();
			fromStateName = fromLifeCycleInfo.getStateName();
			fromLifeCycleTemplate = fromLifeCycleInfo.getLifeCycleTemplate();
		}
	}

	/**
	 * @return toLifeCycleInfo
	 */
	public LifeCycleInfo getToLifeCycleInfo() {
		return toLifeCycleInfo;
	}

	/**
	 * @param toLifeCycleInfo 要设置的 toLifeCycleInfo
	 */
	public void setToLifeCycleInfo(LifeCycleInfo toLifeCycleInfo) {
		this.toLifeCycleInfo = toLifeCycleInfo;
		if (toLifeCycleInfo != null) {
			toStateId = toLifeCycleInfo.getStateId();
			toStateName = toLifeCycleInfo.getStateName();
			toLifeCycleTemplate = toLifeCycleInfo.getLifeCycleTemplate();
		}
	}

	/**
	 * @return manageInfo
	 */
	public ManageInfo getManageInfo() {
		return manageInfo;
	}

	/**
	 * @param manageInfo 要设置的 manageInfo
	 */
	public void setManageInfo(ManageInfo manageInfo) {
		this.manageInfo = manageInfo;
	}

	/**
	 * @return objectId
	 */
	public String getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId 要设置的 objectId
	 */
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	/**
	 * @return objectClassId
	 */
	public String getObjectClassId() {
		return objectClassId;
	}

	/**
	 * @param objectClassId 要设置的 objectClassId
	 */
	public void setObjectClassId(String objectClassId) {
		this.objectClassId = objectClassId;
	}

	/**
	 * @return fromStateId
	 */
	public String getFromStateId() {
		return fromStateId;
	}

	/**
	 * @param fromStateId 要设置的 fromStateId
	 */
	public void setFromStateId(String fromStateId) {
		this.fromStateId = fromStateId;
	}

	/**
	 * @return fromStateName
	 */
	public String getFromStateName() {
		return fromStateName;
	}

	/**
	 * @param fromStateName 要设置的 fromStateName
	 */
	public void setFromStateName(String fromStateName) {
		this.fromStateName = fromStateName;
	}

	/**
	 * @return fromLifeCycleTemplate
	 */
	public String getFromLifeCycleTemplate() {
		return fromLifeCycleTemplate;
	}

	/**
	 * @param fromLifeCycleTemplate 要设置的 fromLifeCycleTemplate
	 */
	public void setFromLifeCycleTemplate(String fromLifeCycleTemplate) {
		this.fromLifeCycleTemplate = fromLifeCycleTemplate;
	}

	/**
	 * @return toStateId
	 */
	public String getToStateId() {
		return toStateId;
	}

	/**
	 * @param toStateId 要设置的 toStateId
	 */
	public void setToStateId(String toStateId) {
		this.toStateId = toStateId;
	}

	/**
	 * @return toStateName
	 */
	public String getToStateName() {
		return toStateName;
	}

	/**
	 * @param toStateName 要设置的 toStateName
	 */
	public void setToStateName(String toStateName) {
		this.toStateName = toStateName;
	}

	/**
	 * @return toLifeCycleTemplate
	 */
	public String getToLifeCycleTemplate() {
		return toLifeCycleTemplate;
	}

	/**
	 * @param toLifeCycleTemplate 要设置的 toLifeCycleTemplate
	 */
	public void setToLifeCycleTemplate(String toLifeCycleTemplate) {
		this.toLifeCycleTemplate = toLifeCycleTemplate;
	}

	/**
	 * @return fromLifeCycleTemplateName
	 */
	public String getFromLifeCycleTemplateName() {
		if (fromLifeCycleTemplate != null) {
			setFromLifeCycleTemplateName(LifeCycleUtil.getDisplayTemplate(fromLifeCycleTemplate));
		}
		return fromLifeCycleTemplateName;
	}

	/**
	 * @param fromLifeCycleTemplateName 要设置的 fromLifeCycleTemplateName
	 */
	public void setFromLifeCycleTemplateName(String fromLifeCycleTemplateName) {
		this.fromLifeCycleTemplateName = fromLifeCycleTemplateName;
	}

	/**
	 * @return toLifeCycleTemplateName
	 */
	public String getToLifeCycleTemplateName() {
		if (toLifeCycleTemplate != null) {
			setToLifeCycleTemplateName(LifeCycleUtil.getDisplayTemplate(toLifeCycleTemplate));
		}
		return toLifeCycleTemplateName;
	}

	/**
	 * @param toLifeCycleTemplateName 要设置的 toLifeCycleTemplateName
	 */
	public void setToLifeCycleTemplateName(String toLifeCycleTemplateName) {
		this.toLifeCycleTemplateName = toLifeCycleTemplateName;
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

	public String getLifeCycleType() {
		return lifeCycleType;
	}

	public void setLifeCycleType(String lifeCycleType) {
		this.lifeCycleType = lifeCycleType;
	}

}
