package com.bjsasc.adm.active.model.activeorderlink;

import com.bjsasc.plm.core.type.ATLink;

/**
 * ���е������ļ�����ģ�͡�
 * 
 * @author yanjia 2013-5-13
 */
public class ActiveOrderLink extends ATLink {

	/** serialVersionUID */
	private static final long serialVersionUID = 7426527124624259782L;

	public static final String CLASSID = ActiveOrderLink.class.getSimpleName();

	/** �Ƿ�������1���ǣ�0�����ǣ�*/
	private int mainOrdered;
	/** ��ǰ����classid */
	private String orderedBeforeClassID;
	/** ��ǰ����innerid */
	private String orderedBeforeID;
	/** ��ǰ���ݰ汾classid */
	private String orderedBeforeVersionClassID;
	/** ��ǰ���ݰ汾innerid */
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
