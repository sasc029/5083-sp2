package com.bjsasc.ddm.distribute.service.distributeprincipal;

import java.util.List;

import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.privilege.acl.ACLData;
import com.bjsasc.plm.core.system.principal.User;

/**
 * ������Ȩ������ӿ�
 * 
 * @author guowei 2014-03-05
 *
 */
public interface DistributePrincipalService {

	/**
	 * Ϊҵ�������Ȩ
	 * 
	 * @param domained
	 * @param currentUser
	 */
	public void setPriviledge(Domained domained, User currentUser);
	
	/**
	 * ���Ϊҵ������������Ȩ��
	 * 
	 * @param domained �ַ������е�ҵ�����
	 * @param currentUser ��ǰ���û�
	 * @return ���ú�Ȩ�޵Ĳ���
	 * @author Sun Zongqing
	 * @date 2014/7/4
	 */
	public List<ACLData> getPriviledgeData(Domained domained, User currentUser);
	
}
