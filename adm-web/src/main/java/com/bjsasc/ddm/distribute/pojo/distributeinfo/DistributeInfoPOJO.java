package com.bjsasc.ddm.distribute.pojo.distributeinfo;

import com.bjsasc.ddm.distribute.pojo.BasePOJO;

/**
 * 分发信息画面模型。
 * 
 * @author gengancong 2013-2-22
 */
public class DistributeInfoPOJO extends BasePOJO {

	/** serialVersionUID */
	private static final long serialVersionUID = 6880675729975864012L;
	/** distributeObjectOids */
	private String distributeObjectOids;
	/** type */
	private String type;
	/** iids */
	private String iids;
	/** disMediaTypes */
	private String disMediaTypes;
	/** disInfoNums */
	private String disInfoNums;
	/** notes */
	private String notes;

	/**
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type 要设置的 type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return iids
	 */
	public String getIids() {
		return iids;
	}

	/**
	 * @param iids 要设置的 iids
	 */
	public void setIids(String iids) {
		this.iids = iids;
	}

	/**
	 * @return disMediaTypes
	 */
	public String getDisMediaTypes() {
		return disMediaTypes;
	}

	/**
	 * @param disMediaTypes 要设置的 disMediaTypes
	 */
	public void setDisMediaTypes(String disMediaTypes) {
		this.disMediaTypes = disMediaTypes;
	}

	/**
	 * @return disInfoNums
	 */
	public String getDisInfoNums() {
		return disInfoNums;
	}

	/**
	 * @param disInfoNums 要设置的 disInfoNums
	 */
	public void setDisInfoNums(String disInfoNums) {
		this.disInfoNums = disInfoNums;
	}

	/**
	 * @return notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes 要设置的 notes
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return distributeObjectOids
	 */
	public String getDistributeObjectOids() {
		return distributeObjectOids;
	}

	/**
	 * @param distributeObjectOids 要设置的 distributeObjectOids
	 */
	public void setDistributeObjectOids(String distributeObjectOids) {
		this.distributeObjectOids = distributeObjectOids;
	}

}
