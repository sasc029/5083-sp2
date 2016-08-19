package com.bjsasc.ddm.distribute.model.distributeinfoconfig;

import com.bjsasc.plm.core.type.ATObject;
/**
 * �ַ���Ϣ��������ģ�͡�
 * 
 * @author yangqun 2014-05-09
 */
public class DistributeInfoConfig  extends ATObject{

	
	private static final long serialVersionUID = 2842082782351633176L;
	/** CLASSID */
	public static final String CLASSID = DistributeInfoConfig.class.getSimpleName();

	/** �ַ���Ϣ���ƣ���λ/��Ա�� */
	private String disInfoName;
	/** �ַ���ϢIID����Ա����֯���ڲ���ʶ�� */
	private String disInfoId;
	/** �ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա�� */
	private String disInfoType;
	/** �ַ����� */
	private long disInfoNum;
	/** �ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ���� */
	private String disMediaType;
	/** ��ע */
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
