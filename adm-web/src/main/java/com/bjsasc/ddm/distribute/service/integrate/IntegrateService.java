package com.bjsasc.ddm.distribute.service.integrate;

/**
 * 瀚海之星系统集成服务接口
 * 
 * @author zhangguoqiang 2014-07-09
 */
public interface IntegrateService {

	/**
	 * 输出xml文件
	 * 
	 * @param 
	 *         orderOid   发放单Oid 
	 */
	public void  exportXml(String orderOid);
}
