package com.bjsasc.ddm.distribute.service.distributetask;

import java.util.List;

import com.bjsasc.avidm.core.site.Site;
import com.bjsasc.ddm.common.ConstUtil.LIFECYCLE_OPT;
import com.bjsasc.ddm.distribute.model.distributeelectask.DistributeElecTask;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;

/**
 * 分发任务服务接口。
 * 
 * @author gengancong 2013-2-22
 */
public interface DistributeTaskService {

	/**
	 * 通过分发单ID获取分发任务数据。
	 * 
	 * @param oid String
	 * @return List
	 */
	public List<DistributePaperTask> getDistributePaperTasksByDistributeOrderOid(String oid);

	/**
	 * 通过分发单ID获取分发任务数据。
	 * 
	 * @param oid String
	 * @return List
	 */
	public List<DistributeElecTask> getDistributeElecTasksByDistributeOrderOid(String oid);

	/**
	 * 创建分发任务。
	 * 
	 * @param distributeOrderOids 分发单的oid，里面可以有分发单和回收销毁单
	 * @param flag 标签，true表示不走工作流
	 * @modify Sun Zongqing
	 */
	public void createDistributeTask(String distributeOrderOids, String flag,String modify);
	/**
	 * 根据首选项的设置，当进行调度时将分发单、分发任务、分发信息以及分发数据设置成已分发状态
	 * 
	 * @param distributeOrderOids 分发单的oid
	 */
	public void setAllSended(String distributeOrderOids);

	/**
	 * 修改生命周期
	 * 
	 * @param oids
	 * @param opt
	 * @param returnReason
	 */
	public void updateDistributeTask(String oids, LIFECYCLE_OPT opt, String returnReason);
	
	/**
	 * 发送相关信息
	 * 
	 * @param outSignInfoOids
	 */
	public void sendToOutSign(String outSignInfoOids, boolean flag);
	
	/**
	 * 数据中心发送发放相关信息到接收方
	 * 
	 * @param outSiteInfos
	 */
	public void sendToReceiveSite(List<DistributeInfo> disInfoList, String outSiteInfos, DistributeOrder order);
	
	/**
	 * 创建外站点任务
	 * 
	 * @param orderOids
	 * @param isDcDeployModel
	 */
	public List<String> createOutSiteDistributeTask(String orderOid, String disInfoOids, Site site, boolean flag,
			boolean centerFlag, Site centerSite);
	
	/**
	 * 创建中心站点任务
	 * 
	 * @param orderOids
	 * @param isDcDeployModel
	 */
	public List<String> createCenterSiteDistributeTask(String orderOid, String disInfoOids, Site site, boolean flag,
			boolean centerFlag, Site centerSite);
}