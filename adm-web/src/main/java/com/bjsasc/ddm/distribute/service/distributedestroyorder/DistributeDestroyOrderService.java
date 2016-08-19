package com.bjsasc.ddm.distribute.service.distributedestroyorder;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.model.recdesinfo.RecDesInfo;
import com.bjsasc.ddm.distribute.model.recdespapertask.RecDesPaperTask;

/**
 * 回收单和销毁单服务接口。
 * 
 * @author yanjia 2013-3-21
 * 
 */
@SuppressWarnings("rawtypes")
public interface DistributeDestroyOrderService {
	
	/**
	 * 根据查询条件查询相应任务。
	 * 
	 * @param stateName
	 * @return
	 */
	public List<DistributePaperTask> getAllDistributePaperTask(Map<String, String> map,Object stateName);
	
	/**
	 * 根据MAP里面提供的查询条件，返回所得的回收销毁纸质任务
	 * 
	 * @param map 存储查询条件的map
	 * @param stateName 回收销毁纸质任务的生命周期状态
	 * @return List<RecDesPaperTask> 根据查询条件返回的回收销毁纸质任务
	 * @author Sun Zongqing
	 */
	public List<RecDesPaperTask> getAllRecDesPaperTask(Map<String, String> map, Object stateName);
	
	/**
	 * 根据查询条件查询回收销毁单任务详细。
	 * 
	 * @param stateName
	 * @return
	 */
	public List<DistributeInfo> getDistributeDestroyDetails(Map<String, String> map,Object taskOids);
	
	/**
	 * 根据map提供的查询条件和回收销毁任务OID，查询与回收销毁任务相关的回收销毁信息
	 * 
	 * @param map 提供查询条件的map参数，用于创建sql语句
	 * @param taskOids 回收销毁任务的OID
	 * @return 根据查询条件获得的回收销毁信息
	 * @author Sun Zongqing
	 */
	public List<RecDesInfo> getRecDesDetails(Map<String, String> map, Object taskOids);
	
	/**
	 * 更新回收销毁份数
	 * 
	 * @param oids
	 * @param destroyNums
	 */
	public void updateDestroyNum(Map<String, String> map, String taskOids, String oids, String destroyNums);
	
	/**
	 * 根据回收销毁任务提供的参数，更新分发信息的回收销毁份数、纸质分发任务的生命周期、
	 * 回收销毁任务的生命周期、回收销毁信息的生命周期、回收/销毁单生命周期
	 * 
	 * @param map 提供查询条件的map参数，提供destroyType参数
	 * @param taskOids 需要更改生命周期的回收销毁纸质任务
	 * @param oids 需要更改生命周期的回收销毁信息
	 * @param destroyNums 回收/销毁的份数
	 * 
	 * @author Sun Zongqing
	 */
	public void updateDestroyNumByRecDesTask(Map<String, String> map, String taskOids, String oids, String destroyNums);
	
	/**
	 * Excel对象生成。
	 * 
	 * @param map Map
	 * @return HSSFWorkbook
	 */
	public HSSFWorkbook getExcelObject(Map mapObject);
	
	/**
	 * 根据提供的分发纸质任务与回收销毁纸质任务，返回一个含有未生成回收销毁信息的分发信息的纸质分发任务列表
	 * (该分发信息与回收销毁信息的接收人/单位信息相同，因为是通过同一搜索条件得出)
	 * 
	 * @param dis 纸质分发任务列表
	 * @param rec 回收销毁信息列表
	 * @return List<DistributePaperTask> 得到的新分发任务列表
	 * @author Sun Zongqing
	 */
	public List<DistributePaperTask> removeDisTaskConflictWithRecDesTask(List<DistributePaperTask> dis, List<RecDesPaperTask> rec);
	
	/**
	 * 根据提供的分发信息与回收销毁信息，移除产生过回收销毁信息的分发信息
	 * 
	 * @param dis 分发信息列表
	 * @param recDes 回收销毁信息列表
	 * @return 移除回收销毁信息后的分发信息列表
	 * @author Sun Zongqing
	 */
	public List<DistributeInfo> removeDisInfoConflictWithRecDesInfo(List<DistributeInfo> dis, List<RecDesInfo> recDes);
	
	/**
	 * 取得所有的回收销毁纸质任务
	 * 
	 * @return List<RecDesPaperTask> 得到的所有回收销毁纸质任务
	 * @author Sun Zongqing
	 */
	public List<RecDesPaperTask> getAllRecDesPaperTask(String lc);

}
