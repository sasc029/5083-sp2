package com.bjsasc.ddm.distribute.service.distributetaskinfolink;

import com.bjsasc.ddm.distribute.model.distributetaskinfolink.DistributeTaskInfoLink;

/**
 *  任务与分发信息link服务接口。
 * 
 * @author gengancong 2013-2-22
 */
public interface DistributeTaskInfoLinkService {

	/**
	 * 创建任务与分发信息link对象。
	 * 
	 * @param disObj DistributeTaskInfoLink
	 */
	public void createDistributeTaskInfoLink(DistributeTaskInfoLink disObj);

	/**
	 * 创建任务与分发信息link对象。
	 * 
	 * @return DistributeTaskInfoLink
	 */
	public DistributeTaskInfoLink newDistributeTaskInfoLink();
}
