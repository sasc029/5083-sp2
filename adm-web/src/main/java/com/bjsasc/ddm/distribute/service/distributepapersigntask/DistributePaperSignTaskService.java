package com.bjsasc.ddm.distribute.service.distributepapersigntask;

import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.common.ConstUtil.LIFECYCLE_OPT;
import com.bjsasc.ddm.distribute.model.distributepapersigntask.DistributePaperSignTask;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributetaskinfolink.DistributeTaskInfoLink;

/**
 * 纸质签收任务服务接口。
 * 
 * @author zhangguoqiang 2014-09-11
 */
public interface DistributePaperSignTaskService {

	/**
	 * 获取所有纸质签收任务
	 */
	public List<DistributePaperSignTask> getAllDistributePaperSignTask(Object stateName);
	
	/**
	 * 获取所有未签收纸质签收任务
	 */
	public List<DistributePaperSignTask> getAllNoSignDistributePaperSignTask(Object stateName);
	
	/**
	 * 获取所有回退纸质签收任务
	 */
	public List<DistributePaperSignTask> getAllReturnDistributePaperSignTask(Object stateName);

	/**
	 * 取得纸质签收任务信息。
	 * 
	 * @param distributePaperTaskOid
	 * @return
	 */
	public DistributePaperSignTask getDistributePaperSignTaskInfo(String distributePaperTaskOid);
	
	/**
	 * 通过纸质签收任务OID，取得相关分发数据信息
	 * 
	 * @param distributePaperTaskOid String
	 * @return List
	 */
	public List<DistributeObject> getDistributeObjects(String distributePaperTaskOid);
	
	/**
	 * 批量创建纸质签收任务对象
	 * 
	 * @param disPaperSignTask
	 */
	public void createDistributePaperSignTasks(Map<String, String> oidsMap);

	/**
	 * 创建纸质签收任务对象
	 * 
	 * @param disPaperSignTask
	 */
	public void createDistributePaperSignTask(DistributePaperSignTask disPaperSignTask);

	/**
	 * 创建纸质签收任务对象
	 * 
	 * @return DistributePaperSignTask
	 */
	public DistributePaperSignTask newDistributePaperSignTask();

	/**
	 * 通过分发单ID获取分发任务数据。
	 * 
	 * @param oid String
	 * @return List
	 */
	public List<DistributePaperSignTask> getDistributePaperSignTasksByDistributeOrderOid(String oid);

	/**
	 * 更新纸质签收任务
	 * 
	 * @param oid String
	 * @param opt LIFECYCLE_OPT
	 */
	public void updateDistributePaperSignTask(String oids, LIFECYCLE_OPT opt, String returnReason);

	/**
	 * 获取纸质签收任务相关分发数据和分发信息
	 * 
	 * @return DistributePaperSignTask
	 */
	public DistributePaperSignTask getDistributePaperSignTaskProperty(String oid);

	/**
	 * 删除纸质签收任务
	 * 
	 * @param oid
	 */
	public void deleteDistributePaperSignTask(String oids);
	
	/**
	 * 创建分发信息与分发任务link对象。
	 * 
	 * @return DistributeInfo
	 */
	public DistributeTaskInfoLink newDistributeTaskInfoLink();
	
	/**
	 * 已签收纸质签收任务完成
	 * 
	 * @param distributePaperTaskOid
	 */
	public void updateDistributePaperSignTaskLife(String distributePaperTaskOid);
	
	/**
	 * 根据纸质签收任务OID取得发放单对象
	 * 
	 * @param oids
	 */
	public DistributeOrder getDistributeOrderByPaperSignTaskOid(String oids);

}
