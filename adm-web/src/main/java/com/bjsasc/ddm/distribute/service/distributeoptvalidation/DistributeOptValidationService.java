package com.bjsasc.ddm.distribute.service.distributeoptvalidation;

import com.bjsasc.plm.core.domain.Domained;

/**
 * 操作权限服务接口
 * 
 * @author guowei 2013-6-9
 */
public interface DistributeOptValidationService {

	/**
	 * 操作权限
	 * 
	 * @param orderOids
	 * @return
	 */
	public boolean getDistributeOperateValidation(String operateId, String oids);
	
	/**
	 * 创建发放单权限
	 * 
	 * @param operateId
	 * @param domained
	 * @return
	 */
	public boolean getCreateDistributeOrderValidation(String operateId, Domained domained);
}
