package com.bjsasc.adm.active.model.activemodel;

import com.bjsasc.plm.core.type.ATObject;

/**
 * 现行模型。
 * 
 * @author yanjia 2013-5-13
 */
public class ActiveModel extends ATObject{
	
	/** serialVersionUID */
	private static final long serialVersionUID = 3002696125279457040L;

	/** CLASSID */
	public static final String CLASSID = ActiveModel.class.getSimpleName();
	
	/** 模型编号 */
	private String modelId;
	
	/** 模型名称 */
	private String modelName;

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
}
