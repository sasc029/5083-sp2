package com.bjsasc.adm.active.service.activeoptvalidation;

import com.bjsasc.plm.core.domain.Domained;

/**
 * ����Ȩ�޷���ӿ�
 * 
 * @author guowei 2013-7-10
 */
public interface ActiveOptValidationService {

	/**
	 * ����Ȩ��
	 * 
	 * @param orderOids
	 * @return
	 */
	public boolean getActiveOperateValidation(String operateId, String oids);
	
	/**
	 * �������ŵ�Ȩ��
	 * 
	 * @param operateId
	 * @param domained
	 * @return
	 */
	public boolean getCreateActiveDocumentValidation(String operateId, Domained domained);
}
