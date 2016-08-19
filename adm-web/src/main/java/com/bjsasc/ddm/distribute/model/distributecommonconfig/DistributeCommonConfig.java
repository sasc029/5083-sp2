package com.bjsasc.ddm.distribute.model.distributecommonconfig;

import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.type.ATObject;

/**
 * ϵͳ���������������ģ�͡�
 * 
 * @author zhangguoqiang 2014-7-11
 */
public class DistributeCommonConfig extends ATObject implements Manageable{

	/** serialVersionUID */
	private static final long serialVersionUID = -360222856983018114L;

	/** CLASSID */
	public static final String CLASSID = DistributeCommonConfig.class.getSimpleName();

	/**
	 * ���ñ��
	 */
	private String configID;
	
	/**
	 * ��������
	 */
	private String configName;
	
	/**
	 * ����ֵ
	 */
	private String configValue;
	
	/**
	 * ����Ĭ��ֵ
	 */
	private String configDefaultValue;
	
	/**
	 * �Ƿ�����ɾ��
	 */
	private int isPermitDelete;
    
	/**
	 * �������
	 */
	private ManageInfo manageInfo;
	
	
	public DistributeCommonConfig() {
		setClassId(DistributeCommonConfig.class.getSimpleName());
	}
	
	/**
	 * ��ȡ���ñ��
	 */
	public String getConfigID() {
		return configID;
	}

	/**
	 * �������ñ��
	 */
	public void setConfigID(String configID) {
		this.configID = configID;
	}

	/**
	 * ��ȡ��������
	 */
	public String getConfigName() {
		return configName;
	}

	/**
	 * ������������
	 */
	public void setConfigName(String configName) {
		this.configName = configName;
	}

	/**
	 * ��ȡ����ֵ
	 */
	public String getConfigValue() {
		return configValue;
	}

	/**
	 * ��������ֵ
	 */
	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	/**
	 * ��ȡ����Ĭ��ֵ
	 */
	public String getConfigDefaultValue() {
		return configDefaultValue;
	}

	/**
	 * ��������Ĭ��ֵ
	 */
	public void setConfigDefaultValue(String configDefaultValue) {
		this.configDefaultValue = configDefaultValue;
	}

	/**
	 * ��ȡ�Ƿ�����ɾ��
	 */
	public int getIsPermitDelete() {
		return isPermitDelete;
	}

	/**
	 * �����Ƿ�����ɾ��
	 */
	public void setIsPermitDelete(int isPermitDelete) {
		this.isPermitDelete = isPermitDelete;
	}

	/**
	 * �����������
	 */
	public void setManageInfo(ManageInfo manageInfo) {
		this.manageInfo = manageInfo;

	}
	
	/**
	 * ��ȡ�������
	 */
	public ManageInfo getManageInfo() {
		return this.manageInfo;
	}
}
