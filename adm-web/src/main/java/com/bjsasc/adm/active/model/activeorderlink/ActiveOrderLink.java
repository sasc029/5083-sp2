package com.bjsasc.adm.active.model.activeorderlink;

import com.bjsasc.plm.core.type.ATLink;

/**
 * 现行单据与文件关联模型。
 * 
 * @author yanjia 2013-5-13
 */
public class ActiveOrderLink extends ATLink {

	/** serialVersionUID */
	private static final long serialVersionUID = 7426527124624259782L;

	public static final String CLASSID = ActiveOrderLink.class.getSimpleName();

	/** 是否主对像（1：是，0：不是）*/
	private int mainOrdered;
	/** 改前数据classid */
	private String orderedBeforeClassID;
	/** 改前数据innerid */
	private String orderedBeforeID;
	/** 改前数据版本classid */
	private String orderedBeforeVersionClassID;
	/** 改前数据版本innerid */
	private String orderedBeforeVersionID;

	public int getMainOrdered() {
		return mainOrdered;
	}

	public void setMainOrdered(int mainOrdered) {
		this.mainOrdered = mainOrdered;
	}

	public String getOrderedBeforeClassID() {
		return orderedBeforeClassID;
	}

	public void setOrderedBeforeClassID(String orderedBeforeClassID) {
		this.orderedBeforeClassID = orderedBeforeClassID;
	}

	public String getOrderedBeforeID() {
		return orderedBeforeID;
	}

	public void setOrderedBeforeID(String orderedBeforeID) {
		this.orderedBeforeID = orderedBeforeID;
	}

	public String getOrderedBeforeVersionClassID() {
		return orderedBeforeVersionClassID;
	}

	public void setOrderedBeforeVersionClassID(String orderedBeforeVersionClassID) {
		this.orderedBeforeVersionClassID = orderedBeforeVersionClassID;
	}

	public String getOrderedBeforeVersionID() {
		return orderedBeforeVersionID;
	}

	public void setOrderedBeforeVersionID(String orderedBeforeVersionID) {
		this.orderedBeforeVersionID = orderedBeforeVersionID;
	}

	public String getOrderedBeforeOid() {
		return orderedBeforeClassID + ":" + orderedBeforeID;
	}
}
