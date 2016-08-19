package com.bjsasc.ddm.distribute.service.distributesendorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;

/**
 * 内发单和外发单服务接口。
 * 
 * @author yanjia 2013-03-21
 *
 */
@SuppressWarnings("rawtypes")
public interface DistributeSendOrderService {

	/**
	 * Excel对象生成。
	 * 
	 * @param mapObject Map
	 * @return HSSFWorkbook
	 */
	public HSSFWorkbook getExcelObject(Map mapObject);
	
	/**
	 * 根据查询条件查询相应任务。
	 * 
	 * @param stateName
	 * @return
	 */
     public List<DistributePaperTask> getAllDistributePaperTask(Map<String, String> map);
	/**
	 * 根据编号查询相应的任务的信息。
	 * 
	 * @param stateName
	 * @return
	 */
	public List<DistributeObject> getDistributePaperTaskPropertyList(Map<String, String> map,String oid); 
	/**
	 * 根据查询条件查询回收销毁单任务详细。
	 * 
	 * @param stateName
	 * @return
	 */
	public List<DistributeInfo> getDistributeDestroyDetails(Map<String, String> map,Object taskOid);
	
	/**
	 * 更新生命周期状态并打印
	 * 
	 * @param map
	 */
	public void updateDistributeInsideCycles(Map<String, String> map, String taskOid);
	
	/**
	 * 根据状态查询内外发放单的单位/人员
	 * @param disInfoType 
	 * @return List<Map<String, Object>> listMap
	 */
	public List<Map<String, Object>> getDisInfoNames(String disInfoType,String sendtype);

	/**kangyanfei
	 * 根据发放信息类型查询回收销毁发放单的单位/人员
	 * @param disInfoType
	 *			分发信息类型（0为单位，1为人员）
	 * @param destroyType
	 *			回收销毁信息（0回收信息，1销毁信息）
	 * @return List<Map<String, Object>> listMap
	 */
	public List<Map<String, Object>> getDisInfoNamesByDisIntfoTypeAndDestroyType(String disInfoType, String destroyType);

}
