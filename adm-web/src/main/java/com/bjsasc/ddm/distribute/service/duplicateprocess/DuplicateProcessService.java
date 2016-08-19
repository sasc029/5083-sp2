package com.bjsasc.ddm.distribute.service.duplicateprocess;

import java.util.List;

import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;

/**
 * 复制加工服务接口。
 * 
 * @author guowei 2013-3-11
 */
public interface DuplicateProcessService {
	
	/**
	 * 获取所有纸质任务
	 * @return
	 */
	public List<DistributePaperTask> getAllDistributePaperTaskByAuth(String stateName);
	
	/**
	 * 获取所有回退纸质任务
	 * @return
	 */
	public List<DistributePaperTask> getAllDistributePaperTaskReturnByAuth(String stateName);
	
	/**
	 * 获取选中的任务信息
	 * @param oids
	 * @return
	 */
	public List<DistributePaperTask> getDuplicateProcessInfo(String oids);
	
	/**
	 * 更新整理人和复印人
	 * @param oids
	 */
	public void updateDuplicateProcessor(String oids,String collator,String contractor);
	
	/**
	 * 录入加工信息
	 * @param oid
	 * @return
	 */
	public List<DistributeObject> listDuplicateProcessInfo(String oid);
	
	/**
	 * 录入加工信息（同意）
	 * 
	 * @param oids
	 * @param collator
	 * @param contractor
	 */
	public void updateProcessInfo(String collator,String contractor,String oids,String taskOid);
	
	/**
	 * 更新加工信息（不同意）
	 * @param oids
	 */
	public void updateDisAgreeInfo(String oids,String returnReason,String taskOid);

}
