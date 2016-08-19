package com.bjsasc.ddm.distribute.service.distributeprincipal;

import java.util.List;

import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.privilege.acl.ACLData;
import com.bjsasc.plm.core.system.principal.User;

/**
 * 发放授权服务类接口
 * 
 * @author guowei 2014-03-05
 *
 */
public interface DistributePrincipalService {

	/**
	 * 为业务对象授权
	 * 
	 * @param domained
	 * @param currentUser
	 */
	public void setPriviledge(Domained domained, User currentUser);
	
	/**
	 * 获得为业务对象所赋予的权限
	 * 
	 * @param domained 分发数据中的业务对象
	 * @param currentUser 当前的用户
	 * @return 设置好权限的参数
	 * @author Sun Zongqing
	 * @date 2014/7/4
	 */
	public List<ACLData> getPriviledgeData(Domained domained, User currentUser);
	
}
