package com.bjsasc.adm.active.model.modelfolderlink;

import com.bjsasc.plm.core.type.ATLink;

/**
 * 现行文件模型与现行文件夹关联模型。
 * 
 * @author yanjia 2013-5-13
 */
@SuppressWarnings("deprecation")
public class ModelFolderLink extends ATLink {

	/** serialVersionUID */
	private static final long serialVersionUID = -3912612163392510817L;

	/**
	 * 构造方法。
	 */
	public ModelFolderLink() {
		setClassId(CLASSID);
	}

	/** CLASSID */
	public static final String CLASSID = ModelFolderLink.class.getSimpleName();

}
