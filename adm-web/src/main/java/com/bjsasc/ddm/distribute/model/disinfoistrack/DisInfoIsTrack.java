package com.bjsasc.ddm.distribute.model.disinfoistrack;

import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.type.ATObject;

/**
 * �Ƿ��������ģ�͡�
 * 
 * @author xuhuiling 2013-9-12
 */
@SuppressWarnings({ "deprecation", "static-access" })
public class DisInfoIsTrack extends ATObject implements Manageable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1927543367270215009L;

	/**
	 * ���췽����
	 */
	public DisInfoIsTrack() {

	}

	/** CLASSID */
	public static final String CLASSID = DisInfoIsTrack.class.getSimpleName();

	/** ������Ϣ */
	private ManageInfo manageInfo;

	/** �Ƿ������Ϣ */
	private int istrack;

	/** �ַ���ϢID */
	private String disInfoId;
	/** �ַ���Ϣ�����ʶ  */
	private String infoClassId;

	/**
	 * @return oid
	 */
	public String getOid() {
		return this.CLASSID + ":" + this.getInnerId();
	}

	public ManageInfo getManageInfo() {
		return manageInfo;
	}

	public void setManageInfo(ManageInfo manageInfo) {
		this.manageInfo = manageInfo;
	}

	public int getIstrack() {
		return istrack;
	}

	public void setIstrack(int istrack) {
		this.istrack = istrack;
	}

	public String getDisInfoId() {
		return disInfoId;
	}

	public void setDisInfoId(String disInfoId) {
		this.disInfoId = disInfoId;
	}

	public String getInfoClassId() {
		return infoClassId;
	}

	public void setInfoClassId(String infoClassId) {
		this.infoClassId = infoClassId;
	}

}
