package com.bjsasc.ddm.distribute.model.convertversion;

import com.bjsasc.plm.core.type.ATLink;

/**
 * 跨版本任务签署信息与分发业务对象link。
 * 
 * @author zhangguoqiang 2014-4-30
 * 表名 --DDM_DC_A3_SIGOBJLINK
 */
public class DcA3SigObjectLink extends ATLink {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4674667010395687048L;

	/** CLASSID */
	public static final String CLASSID = DcA3SigObjectLink.class.getSimpleName();
	/**
	 * 构造方法。
	 */
	public DcA3SigObjectLink() {
		setClassId(CLASSID);
	}

}
