package com.bjsasc.adm.active.service.activeoptvalidation;

import com.bjsasc.plm.core.domain.Domained;

/**
 * 操作权限服务接口
 * 
 * @author guowei 2013-7-10
 */
public interface ActiveOptValidationService {

	/**
	 * 操作权限
	 * 
	 * @param orderOids
	 * @return
	 */
	public boolean getActiveOperateValidation(String operateId, String oids);
	
	/**
	 * 创建发放单权限
	 * 
	 * @param operateId
	 * @param domained
	 * @return
	 */
	public boolean getCreateActiveDocumentValidation(String operateId, Domained domained);
}
