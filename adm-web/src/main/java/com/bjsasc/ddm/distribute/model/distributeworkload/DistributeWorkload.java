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
 * ������ͳ������ģ�͡�
 *
 * @author gengancong 2013-3-21
 */
public class DistributeWorkload extends ATObject implements Manageable {

	/** serialVersionUID */
	private static final long serialVersionUID = -9183215143236229193L;

	/**
	 * ���췽����
	 */
	public DistributeWorkload() {

	}

	public static final String CLASSID = DistributeWorkload.class.getSimpleName();

	/** ��������������Ϣ���� */
	private LifeCycleInfo fromLifeCycleInfo = new LifeCycleInfo();

	/** ��������������Ϣ���� */
	private LifeCycleInfo toLifeCycleInfo = new LifeCycleInfo();

	private ObjectReference distributeData = new ObjectReference();

	/** ������Ϣ */
	private ManageInfo manageInfo;

	/** objectId */
	private String objectId;

	/** objectClassId */
	private String objectClassId;

	/** ԭ�������ڱ�ʶ */
	private String fromStateId;
	/** ԭ������������ */
	private String fromStateName;
	/** ԭ��������ģ�� */
	private String fromLifeCycleTemplate;
	/** ���������ڱ�ʶ */
	private String toStateId;
	/** �������������� */
	private String toStateName;
	/** ����������ģ�� */
	private String toLifeCycleTemplate;
	/** ���  */
	private String number;
	/** ����  */
	private String name;
	/** ��ѯ״̬  */
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
	 * @param distributeData Ҫ���õ� distributeData
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
	 * @param fromLifeCycleInfo Ҫ���õ� fromLifeCycleInfo
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
	 * @param toLifeCycleInfo Ҫ���õ� toLifeCycleInfo
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
	 * @param manageInfo Ҫ���õ� manageInfo
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
	 * @param objectId Ҫ���õ� objectId
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
	 * @param objectClassId Ҫ���õ� objectClassId
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
	 * @param fromStateId Ҫ���õ� fromStateId
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
	 * @param fromStateName Ҫ���õ� fromStateName
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
	 * @param fromLifeCycleTemplate Ҫ���õ� fromLifeCycleTemplate
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
	 * @param toStateId Ҫ���õ� toStateId
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
	 * @param toStateName Ҫ���õ� toStateName
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
	 * @param toLifeCycleTemplate Ҫ���õ� toLifeCycleTemplate
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
	 * @param fromLifeCycleTemplateName Ҫ���õ� fromLifeCycleTemplateName
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
	 * @param toLifeCycleTemplateName Ҫ���õ� toLifeCycleTemplateName
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
