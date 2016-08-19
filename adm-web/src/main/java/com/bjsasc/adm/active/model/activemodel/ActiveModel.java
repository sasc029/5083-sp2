package com.bjsasc.adm.active.model.activemodel;

import com.bjsasc.plm.core.type.ATObject;

/**
 * ����ģ�͡�
 * 
 * @author yanjia 2013-5-13
 */
public class ActiveModel extends ATObject{
	
	/** serialVersionUID */
	private static final long serialVersionUID = 3002696125279457040L;

	/** CLASSID */
	public static final String CLASSID = ActiveModel.class.getSimpleName();
	
	/** ģ�ͱ�� */
	private String modelId;
	
	/** ģ������ */
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
