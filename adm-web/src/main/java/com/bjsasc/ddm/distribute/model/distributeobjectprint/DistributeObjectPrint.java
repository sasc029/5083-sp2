package com.bjsasc.ddm.distribute.model.distributeobjectprint;

import com.bjsasc.plm.core.type.ATObject;

/**
 * �����������Ƿ��Ѵ�ӡ
 * @author yangzhenzhou
 *
 */
public class DistributeObjectPrint extends ATObject  {
	/**
	 * ��������µķַ������Ƿ��Ѵ�ӡ
	 */
	private static final long serialVersionUID = 2676777501077154869L;
	/** CLASSID */
	public static final String CLASSID = DistributeObjectPrint.class
			.getSimpleName();
	/** �ַ������ڲ���ʶ  */
	private String objInnerid;
	/** �ַ���������  */
	private String objClassid;
	/** �ַ������ڲ���ʶ  */
	private String taskInnerid;
	/** �ַ���������  */
	private String taskClassid;
	/**�ַ������Ƿ��ӡ */
	private String isprint;
	public String getObjInnerid() {
		return objInnerid;
	}
	public void setObjInnerid(String objInnerid) {
		this.objInnerid = objInnerid;
	}
	public String getObjClassid() {
		return objClassid;
	}
	public void setObjClassid(String objClassid) {
		this.objClassid = objClassid;
	}
	public String getTaskInnerid() {
		return taskInnerid;
	}
	public void setTaskInnerid(String taskInnerid) {
		this.taskInnerid = taskInnerid;
	}
	public String getTaskClassid() {
		return taskClassid;
	}
	public void setTaskClassid(String taskClassid) {
		this.taskClassid = taskClassid;
	}
	public String getIsprint() {
		return isprint;
	}
	public void setIsprint(String isprint) {
		this.isprint = isprint;
	}
}
