package com.bjsasc.ddm.distribute.service.distributeoptvalidation;

import com.bjsasc.plm.core.domain.Domained;

/**
 * ����Ȩ�޷���ӿ�
 * 
 * @author guowei 2013-6-9
 */
public interface DistributeOptValidationService {

	/**
	 * ����Ȩ��
	 * 
	 * @param orderOids
	 * @return
	 */
	public boolean getDistributeOperateValidation(String operateId, String oids);
	
	/**
	 * �������ŵ�Ȩ��
	 * 
	 * @param operateId
	 * @param domained
	 * @return
	 */
	public boolean getCreateDistributeOrderValidation(String operateId, Domained domained);
}
