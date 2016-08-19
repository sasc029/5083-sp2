package com.bjsasc.ddm.distribute.model.duplicateprocess;

import com.bjsasc.plm.core.type.ATObject;

/** 
 * 复制加工数据模型
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

	/** 分发数据内部标识 */
	private String disObjectInnerId;

	/** 分发数据内部类标识 */
	private String disObjectClassId;

	/** 关联纸质分发任务内部标识 */
	private String disPaperTaskInnerId;

	/** 关联纸质分发任务内部类标识 */
	private String disPaperTaskClassId;

	/** 复印人 */
	private String contractor;

	/** 整理人 */
	private String collator;
	
	/** 完成时间 */
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
