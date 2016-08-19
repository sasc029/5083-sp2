package com.bjsasc.ddm.distribute.service.distributeelectask;

import java.util.List;

import com.bjsasc.ddm.common.ConstUtil.LIFECYCLE_OPT;
import com.bjsasc.ddm.distribute.model.distributeelectask.DistributeElecTask;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributetaskinfolink.DistributeTaskInfoLink;

/**
 * 电子任务服务接口。
 * 
 * @author gengancong 2013-3-18
 */
public interface DistributeElecTaskService {

	/**
	 * 获取所有电子任务
	 */
	public List<DistributeElecTask> getAllDistributeElecTask(Object stateName);

	/**
	 * 获取二级调度员的电子任务
	 */
	public List<DistributeElecTask> getDistributeElecTaskForSecondSche(String stateName, long from, long to);
	
	/**
	 * 获取跨域协管员的电子任务(目前只针对已拒绝和已完成的状态,待办任务还是沿用以前的逻辑)
	 */
	public List<DistributeElecTask> getDistributeElecTaskForCrossDomain(String stateName, long from, long to);
	
	/**
	 * 获取所有未签收电子任务
	 */
	public List<DistributeElecTask> getAllNoSignDistributeElecTask(Object stateName);
	
	/**
	 * 获取所有回退电子任务
	 */
	public List<DistributeElecTask> getAllReturnDistributeElecTask(Object stateName);

	/**
	 * 取得电子任务信息。
	 * 
	 * @param distributeElecOid
	 * @return
	 */
	public DistributeElecTask getDistributeElecTaskInfo(String distributeElecOid);

	/**
	 * 通过电子任务OID，取得相关分发信息
	 * 
	 * @param distributeElecOid String
	 * @return List
	 */
//	public List<DistributeInfo> getDistributeInfos(String distributeElecOid);
	
	/**
	 * 通过电子任务OID，取得相关转发信息
	 * 
	 * @param distributeElecOid String
	 * @return List
	 */
	public List<DistributeInfo> getDistributeForwardInfos(String distributeElecOid);
	
	/**
	 * 取得分发数据对应的分发信息
	 * 
	 * @param orderObjectLinkOids
	 * @return
	 */
	public List<DistributeInfo> getDistributeForwardInfoByOids(String orderObjectLinkOids,String taskOid);

	/**
	 * 通过电子任务OID，取得相关分发数据信息
	 * 
	 * @param distributeElecOid String
	 * @return List
	 */
	public List<DistributeObject> getDistributeObjects(String distributeElecOid);
	
	/**
	 * 通过电子任务OID，取得相关分发数据信息
	 * 
	 * @param distributeElecOid String
	 * @return List
	 */
	public List<DistributeObject> getDistributeObject(String distributeElecOid);

	/**
	 * 创建电子任务对象
	 * 
	 * @param disElecTask
	 */
	public void createDistributeElecTask(DistributeElecTask disElecTask);
	
	/**
	 * 更新转发信息备注
	 * 
	 * @param oids
	 * @param notes
	 */
	public void updateDistributeInfos(String oids,String notes);
	
	/**
	 * 创建转发的电子任务
	 * 
	 * @param taskOid
	 * @param oids
	 */
	public void createDistributeForwardElecTask(String taskOid,String oids);

	/**
	 * 创建电子任务对象
	 * 
	 * @param id
	 * @param name
	 * @param note
	 */
	public void createDistributeElecTask(String number, String name, String note);

	/**
	 * 创建电子任务对象
	 * 
	 * @return DistributeElecTask
	 */
	public DistributeElecTask newDistributeElecTask();

	/**
	 * 更新电子任务
	 * 
	 * @param oid String
	 * @param opt LIFECYCLE_OPT
	 */
	public void updateDistributeElecTask(String oids, LIFECYCLE_OPT opt, String returnReason);

	/**
	 * 获取电子任务相关分发数据和分发信息
	 * 
	 * @return DistributeElecTask
	 */
	public DistributeElecTask getDistributeElecTaskProperty(String oid);

	/**
	 * 删除电子任务
	 * 
	 * @param oid
	 */
	public void deleteDistributeElecTask(String oids);
	
	/**
	 * 创建电子信息
	 * 
	 * @param linkOids
	 * @param iids
	 */
	public void createDistributeInfos(String linkOids, String iids,String taskOid,String type);
	
	/**
	 * 创建分发信息对象。
	 * 
	 * @return DistributeInfo
	 */
	public DistributeInfo newDistributeInfo();
	
	/**
	 * 创建分发信息与分发任务link对象。
	 * 
	 * @return DistributeInfo
	 */
	public DistributeTaskInfoLink newDistributeTaskInfoLink();
	
	/**
	 * 已签收电子任务完成
	 * 
	 * @param distributeElecOid
	 */
	public void updateDistributeElecTaskLife(String distributeElecOid);
	
	/**
	 * 删除分发信息
	 * 
	 * @param oid
	 */
	public String deleteDistributeInfos(String oids);


	/**
	 * 取的任务树
	 * 
	 * @param oid
	 */
	public List<DistributeElecTask> getDistributeSonElecTask(String oids);
	
	/**
	 * 取得存在电子转发信息
	 * 
	 * @param linkOids
	 * @param iids
	 * @param taskOid
	 * @param type
	 * @return
	 */
	public String getExitDistributeElecInfo(String linkOids, String iids,String type);
	
	/**
	 * 判断用户是否有转发权限
	 */
    public 	boolean isUserlimit();
    
	/**
	 * 判断用户是否是二级调度员
	 */
    public 	boolean isSecondScheUser();
    
	/**
	 * 判断用户是否是跨域协管员
	 */
    public 	boolean isCrossDomainUser();
    
	/**
	 * 电子任务发送到外站点
	 * 
	 * @param oids
	 */
	public void sendDistributeToOutSign(String oids);
	
	/**
	 * 根据电子任务OID取得发放单对象
	 * 
	 * @param oids
	 */
	public DistributeOrder getDistributeOrderByElecTaskOid(String oids);

	/**
	 * 获取外站点同步任务
	 * 
	 * @return
	 */
	public List<DistributeElecTask> getAllDistributeSynTasks(String orderOid, int siteType);
	
	/**
	 * 判断中心模式分发方式（链接分发还是实体分发）
	 * 
	 * @return
	 */
	public Boolean getDistributeElecTaskIsEntity(DistributeElecTask elecTask);

}
