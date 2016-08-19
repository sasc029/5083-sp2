package com.bjsasc.adm.active.model;

import java.io.Serializable;

import com.bjsasc.plm.core.identifier.Identified;
import com.bjsasc.plm.core.system.securitylevel.SecurityLevelInfo;
import com.bjsasc.plm.core.system.securitylevel.SecurityLeveled;
import com.bjsasc.plm.core.vc.model.Master;

/**
 * �����ļ�������ģ��
 * 
 * @author yanjia 2013-5-13
 *
 */
public class AdmMaster extends Master implements SecurityLeveled, Identified, Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 3287588128579998513L;

	/** �ܼ���Ϣ */
	private SecurityLevelInfo securityLevelInfo;
	/** ���  */
	private String number;
	/** ����  */
	private String name;

	/**
	 * ȡ���ܼ���Ϣ
	 * @return securityLevelInfo
	 */
	public SecurityLevelInfo getSecurityLevelInfo() {
		return securityLevelInfo;
	}

	/**
	 * �����ܼ���Ϣ
	 * @param securityLevelInfo Ҫ���õ� securityLevelInfo
	 */
	public void setSecurityLevelInfo(SecurityLevelInfo securityLevelInfo) {
		this.securityLevelInfo = securityLevelInfo;
	}

	@Override
	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getNumber() {
		return number;
	}

	@Override
	public String getName() {
		return name;
	}
}
