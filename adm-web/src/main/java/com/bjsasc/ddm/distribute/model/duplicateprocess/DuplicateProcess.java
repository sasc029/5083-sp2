package com.bjsasc.ddm.distribute.model.duplicateprocess;

import com.bjsasc.plm.core.type.ATObject;

/** 
 * ���Ƽӹ�����ģ��
 * 
 * @author guowei 2013-3-8
 */
public class DuplicateProcess extends ATObject {

	/** serialVersionUID */
	private static final long serialVersionUID = 2962901155298212270L;

	/** CLASSID */
	public static final String CLASSID = DuplicateProcess.class.getSimpleName();

	public DuplicateProcess() {

	}

	/** �ַ������ڲ���ʶ */
	private String disObjectInnerId;

	/** �ַ������ڲ����ʶ */
	private String disObjectClassId;

	/** ����ֽ�ʷַ������ڲ���ʶ */
	private String disPaperTaskInnerId;

	/** ����ֽ�ʷַ������ڲ����ʶ */
	private String disPaperTaskClassId;

	/** ��ӡ�� */
	private String contractor;

	/** ������ */
	private String collator;
	
	/** ���ʱ�� */
	private long finishTime;

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

	public String getDisObjectInnerId() {
		return disObjectInnerId;
	}

	public void setDisObjectInnerId(String disObjectInnerId) {
		this.disObjectInnerId = disObjectInnerId;
	}

	public String getDisObjectClassId() {
		return disObjectClassId;
	}

	public void setDisObjectClassId(String disObjectClassId) {
		this.disObjectClassId = disObjectClassId;
	}

	public String getDisPaperTaskInnerId() {
		return disPaperTaskInnerId;
	}

	public void setDisPaperTaskInnerId(String disPaperTaskInnerId) {
		this.disPaperTaskInnerId = disPaperTaskInnerId;
	}

	public String getDisPaperTaskClassId() {
		return disPaperTaskClassId;
	}

	public void setDisPaperTaskClassId(String disPaperTaskClassId) {
		this.disPaperTaskClassId = disPaperTaskClassId;
	}

	public String getContractor() {
		return contractor;
	}

	public void setContractor(String contractor) {
		this.contractor = contractor;
	}

	public String getCollator() {
		return collator;
	}

	public void setCollator(String collator) {
		this.collator = collator;
	}
}
