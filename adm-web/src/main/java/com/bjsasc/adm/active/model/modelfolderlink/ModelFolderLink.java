package com.bjsasc.adm.active.model.modelfolderlink;

import com.bjsasc.plm.core.type.ATLink;

/**
 * �����ļ�ģ���������ļ��й���ģ�͡�
 * 
 * @author yanjia 2013-5-13
 */
@SuppressWarnings("deprecation")
public class ModelFolderLink extends ATLink {

	/** serialVersionUID */
	private static final long serialVersionUID = -3912612163392510817L;

	/**
	 * ���췽����
	 */
	public ModelFolderLink() {
		setClassId(CLASSID);
	}

	/** CLASSID */
	public static final String CLASSID = ModelFolderLink.class.getSimpleName();

}
