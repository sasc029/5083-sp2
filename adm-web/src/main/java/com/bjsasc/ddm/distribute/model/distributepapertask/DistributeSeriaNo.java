package com.bjsasc.ddm.distribute.model.distributepapertask;

import com.bjsasc.plm.core.type.ATObject;

public class DistributeSeriaNo extends ATObject {
	/** serialVersionUID */
	private static final long serialVersionUID = 7939965883192112239L;

	public static final String CLASSID = DistributeSeriaNo.class.getSimpleName();

	/** ��ˮ��*/
	private long seriaNo;
	/** ������ˮ��ʱ��*/
	private long createSerianoTime;
	/** ��������*/
	private int orderType;

	/** 
	 * ȡ����ˮ��
	 * @return seriaNo
	 */
	public long getSeriaNo() {
		return seriaNo;
	}

	/** 
	 * ������ˮ��
	 * @param seriaNo Ҫ���õ�seriaNo
	 */
	public void setSeriaNo(long seriaNo) {
		this.seriaNo = seriaNo;
	}

	/**
	 * ȡ�ô�����ˮ��ʱ��
	 * @return createSerianoTime
	 */
	public long getCreateSerianoTime() {
		return createSerianoTime;
	}

	/**
	 * ���ô�����ˮ��ʱ��
	 * @param createSerianoTime Ҫ���õ�createSerianoTime
	 */
	public void setCreateSerianoTime(long createSerianoTime) {
		this.createSerianoTime = createSerianoTime;
	}

	/**
	 * ȡ�õ�������
	 * @return orderType
	 */
	public int getOrderType() {
		return orderType;
	}

	/** 
	 * ���õ�������
	 * @param orderType Ҫ���õ�orderType
	 */
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	/** 
	 * ȡ����ˮ��
	 * @return seriaNo+1
	 */
	public long getNextSeriaNo() {
		return seriaNo + 1;
	}
}
