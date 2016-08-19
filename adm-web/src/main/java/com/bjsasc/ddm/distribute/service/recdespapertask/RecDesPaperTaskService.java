package com.bjsasc.ddm.distribute.service.recdespapertask;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.model.recdespapertask.RecDesPaperTask;



/**
 * 回收销毁纸质任务的服务接口
 * 
 * @author sunzongqing
 */
public interface RecDesPaperTaskService {
	
	/**
	 * 获取所有指定状态的回收销毁任务
	 * 
	 * @param stateName 纸质任务状态名称
	 * @return List<RecDesPaperTask> 所查询状态的所有回收销毁纸质任务
	 */
	public List<RecDesPaperTask> getAllRecDesPaperTaskByAuth(String stateName);
	
	/**
	 * 获取所有指定状态的回退回收销毁纸质任务
	 * 
	 * @param stateName 回退回收销毁纸质任务的状态名称
	 * @return 所查询状态的所有回退回收销毁纸质任务
	 */
	public List<RecDesPaperTask> getAllRecDesPaperTaskReturnByAuth(String stateName);
	
	/**
	 * 根据所传入的回收销毁纸质任务，分配相应的生命周期模型
	 * 
	 * @param recDesPaperTask 作为参数传入的回收销毁纸质任务
	 */
	public void setRecDesPaperTaskLifecycle(RecDesPaperTask recDesPaperTask);
	
	/**
	 * 根据所传入的编号、名称、备注来创建回收销毁纸质任务
	 * 
	 * @param number 回收销毁纸质任务的编号
	 * @param name	回收销毁纸质任务的名称
	 * @param note	回收销毁纸质任务的备注
	 */
	public void createRecDesPaperTask(String number, String name, String note);
	
	/**
	 * 通过调用PersistUtil类的方法，创建一个新的回收销毁纸质任务，放弃使用构造方法创建
	 * 
	 * @return RecDesPaperTask 新创建的回收销毁纸质任务
	 */
	public RecDesPaperTask newRecDesPaperTask();
	
	/**
	 * 获取回退回收销毁纸质任务详细列表
	 * 
	 * @param taskOid 任务的OID
	 * @param stateName 状态名称
	 * @return list<RecDesPaperTask> 详细回退回收销毁纸质任务列表
	 */
	public List<RecDesPaperTask> getRecDesPaperTaskReturnDetail(String taskOid, String stateName);
	
	/**
	 * 通过OID获取回收销毁纸质任务的OrderName与OrderOid
	 * 
	 * @param oid 回收销毁纸质任务的OID
	 * @return 相对应的回收销毁纸质任务
	 */
	public RecDesPaperTask getRecDesPaperTaskProperty(String oid);
	
	/**
	 * 删除给定OID的回收销毁纸质任务
	 * 
	 * @param oid 需要删除的回收销毁纸质任务的OID
	 */
	public void deleteRecDesPaperTaskProperty(String oid);

	/**
	 * 2014-08-12
	 * @author kangyanfei
	 * 通过分发单ID获取分发任务数据。
	 * 
	 * @param oid String
	 * @return List
	 */
	public List<RecDesPaperTask> getRecDesPaperTasksByDistributeOrderOid(String oid);
}
