package com.bjsasc.ddm.distribute.model.distributepapertask;

import com.bjsasc.plm.core.type.ATObject;

public class DistributeSeriaNo extends ATObject {
	/** serialVersionUID */
	private static final long serialVersionUID = 7939965883192112239L;

	public static final String CLASSID = DistributeSeriaNo.class.getSimpleName();

	/** 流水号*/
	private long seriaNo;
	/** 创建流水号时间*/
	private long createSerianoTime;
	/** 单据类型*/
	private int orderType;

	/** 
	 * 取得流水号
	 * @return seriaNo
	 */
	public long getSeriaNo() {
		return seriaNo;
	}

	/** 
	 * 设置流水号
	 * @param seriaNo 要设置的seriaNo
	 */
	public void setSeriaNo(long seriaNo) {
		this.seriaNo = seriaNo;
	}

	/**
	 * 取得创建流水号时间
	 * @return createSerianoTime
	 */
	public long getCreateSerianoTime() {
		return createSerianoTime;
	}

	/**
	 * 设置创建流水号时间
	 * @param createSerianoTime 要设置的createSerianoTime
	 */
	public void setCreateSerianoTime(long createSerianoTime) {
		this.createSerianoTime = createSerianoTime;
	}

	/**
	 * 取得单据类型
	 * @return orderType
	 */
	public int getOrderType() {
		return orderType;
	}

	/** 
	 * 设置单据类型
	 * @param orderType 要设置的orderType
	 */
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	/** 
	 * 取得流水号
	 * @return seriaNo+1
	 */
	public long getNextSeriaNo() {
		return seriaNo + 1;
	}
}
