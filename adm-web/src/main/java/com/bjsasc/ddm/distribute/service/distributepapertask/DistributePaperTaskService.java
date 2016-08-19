package com.bjsasc.ddm.distribute.service.distributepapertask;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;

/**
 * 纸质任务服务接口。
 * 
 * @author guowei 2013-2-22
 *
 */
public interface DistributePaperTaskService {

	/**
	 * 获取所有纸质任务
	 */
	public List<DistributePaperTask> getAllDistributePaperTaskByAuth(String stateName);
	
	/**
	 * 重新打印页面初始加载默认显示一个月内的数据
	 * @param stateName
	 * @return
	 */
	public List<DistributePaperTask> getSearchProcessingDistributePaperTaskByTime(long time,String stateName,long currentTime );
	/**
	 * 打印加工管理模块点击“搜索”按钮
	 * @param stateName
	 * @return
	 */
	public List<DistributePaperTask> getSearchProcessingDistributePaperTaskByAuth(String stateName,String keyWords );
	
	/**
	 * 获取所有回退纸质任务
	 */
	public List<DistributePaperTask> getAllDistributePaperTaskReturnByAuth(String stateName);

	/**
	 * 创建纸质任务对象
	 * 
	 * @param disPaperTask
	 */
	public void createDistributePaperTask(DistributePaperTask disPaperTask);

	/**
	 * 创建纸质任务对象
	 * 
	 * @param id
	 * @param name
	 * @param note
	 */
	public void createDistributePaperTask(String number, String name, String note);

	/**
	 * 创建纸质任务对象
	 * 
	 * @return DistributePaperTask
	 */
	public DistributePaperTask newDistributePaperTask();

	/**
	 * 获取回退详细列表
	 * @param taskOid
	 * @return
	 */
	public List<DistributePaperTask> getDistributeTaskReturnDetail(String taskOid,String stateName);

	/**
	 * 获取纸质任务相关分发数据和分发信息
	 * 
	 * @return DistributePaperTask
	 */
	public DistributePaperTask getDistributePaperTaskProperty(String oid);


	/**
	 * 删除纸质任务
	 * 
	 * @param oid
	 */
	public void deleteDistributePaperTaskProperty(String oids);

	/**
	 * 取得附件
	 * 
	 * @param oid
	 * @return
	 */
	public List<Map<String,Object>> listDistributeObjectFiles(String oid,String contextPath);
	
	/**
	 * Excel对象生成。
	 * 
	 * @param oid String
	 * @return HSSFWorkbook
	 */
	public HSSFWorkbook getExcelObject(String oid);
	
	/**
	 * Excel对象生成。
	 * 
	 * @param oid String
	 * @return HSSFWorkbook
	 */
	public HSSFWorkbook getExcelObjectForDept7(String oid);

}
