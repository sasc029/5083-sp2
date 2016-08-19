package com.bjsasc.ddm.distribute.pojo;

import com.bjsasc.plm.core.type.ATObject;

/**
 * 画面模型父类。
 * 
 * @author gengancong 2013-2-22
 */
public class BasePOJO extends ATObject {

	/** serialVersionUID */
	private static final long serialVersionUID = 7855021340482649507L;
	/** oid */
	private String oid;
	/** oids */
	private String oids;

	/**
	 * @return oid
	 */
	public String getOid() {
		return oid;
	}

	/**
	 * @param oid 要设置的 oid
	 */
	public void setOid(String oid) {
		this.oid = oid;
	}

	/**
	 * @return oids
	 */
	public String getOids() {
		return oids;
	}

	/**
	 * @param oids 要设置的 oids
	 */
	public void setOids(String oids) {
		this.oids = oids;
	}

}