package com.bjsasc.adm.active.model;

import java.io.Serializable;

import com.bjsasc.plm.core.identifier.Identified;
import com.bjsasc.plm.core.system.securitylevel.SecurityLevelInfo;
import com.bjsasc.plm.core.system.securitylevel.SecurityLeveled;
import com.bjsasc.plm.core.vc.model.Master;

/**
 * 现行文件主对象模型
 * 
 * @author yanjia 2013-5-13
 *
 */
public class AdmMaster extends Master implements SecurityLeveled, Identified, Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 3287588128579998513L;

	/** 密集信息 */
	private SecurityLevelInfo securityLevelInfo;
	/** 编号  */
	private String number;
	/** 名称  */
	private String name;

	/**
	 * 取得密集信息
	 * @return securityLevelInfo
	 */
	public SecurityLevelInfo getSecurityLevelInfo() {
		return securityLevelInfo;
	}

	/**
	 * 设置密集信息
	 * @param securityLevelInfo 要设置的 securityLevelInfo
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
