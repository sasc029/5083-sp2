package com.bjsasc.ddm.distribute.service.distributeworkload;

import java.util.List;
import com.bjsasc.ddm.distribute.model.distributeworkload.DistributeWorkload;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;

/**
 * 工作量统计服务接口。
 * 
 * @author gengancong 2013-3-21
 */
public interface DistributeWorkloadService {

	/**
	 * 获取工作量统计数据。
	 * 
	 * @return List
	 */
	public List<DistributeWorkload> getDistributeWorkloads(String where);

	/**
	 * 创建工作量统计。
	 * 
	 * @param dis DistributeWorkload
	 */
	public void createDistributeWorkload(DistributeWorkload dis, LifeCycleManaged object);

	/**
	 * 创建工作量统计。
	 * 
	 * @return DistributeWorkload
	 */
	public DistributeWorkload newDistributeWorkload(LifeCycleManaged object);

}