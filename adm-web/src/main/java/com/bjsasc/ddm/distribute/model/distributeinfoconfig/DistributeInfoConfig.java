package com.bjsasc.ddm.distribute.model.distributeinfoconfig;

import com.bjsasc.plm.core.type.ATObject;
/**
 * 分发信息数据配置模型。
 * 
 * @author yangqun 2014-05-09
 */
public class DistributeInfoConfig  extends ATObject{

	
	private static final long serialVersionUID = 2842082782351633176L;
	/** CLASSID */
	public static final String CLASSID = DistributeInfoConfig.class.getSimpleName();

	/** 分发信息名称（单位/人员） */
	private String disInfoName;
	/** 分发信息IID（人员或组织的内部标识） */
	private String disInfoId;
	/** 分发信息类型（0为单位，1为人员） */
	private String disInfoType;
	/** 分发份数 */
	private long disInfoNum;
	/** 分发介质类型（0为纸质，1为电子，2为跨域） */
	private String disMediaType;
	/** 备注 */
	private String note;

	
	public String getDisInfoName() {
		return disInfoName;
	}


	public void setDisInfoName(String disInfoName) {
		this.disInfoName = disInfoName;
	}


	public String getDisInfoId() {
		return disInfoId;
	}


	public void setDisInfoId(String disInfoId) {
		this.disInfoId = disInfoId;
	}


	public String getDisInfoType() {
		return disInfoType;
	}


	public void setDisInfoType(String disInfoType) {
		this.disInfoType = disInfoType;
	}


	public long getDisInfoNum() {
		return disInfoNum;
	}


	public void setDisInfoNum(long disInfoNum) {
		this.disInfoNum = disInfoNum;
	}


	public String getDisMediaType() {
		return disMediaType;
	}


	public void setDisMediaType(String disMediaType) {
		this.disMediaType = disMediaType;
	}


	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}
	
	
	
	
	
	
}
