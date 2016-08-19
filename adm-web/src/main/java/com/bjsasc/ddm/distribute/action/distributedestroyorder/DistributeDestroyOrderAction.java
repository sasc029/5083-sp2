package com.bjsasc.ddm.distribute.action.distributedestroyorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.action.AbstractAction;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.model.recdesinfo.RecDesInfo;
import com.bjsasc.ddm.distribute.model.recdespapertask.RecDesPaperTask;
import com.bjsasc.ddm.distribute.service.distributedestroyorder.DistributeDestroyOrderService;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.bjsasc.plm.type.TypeService;
import com.cascc.avidm.util.SplitString;

/**
 * 回收销毁单Action实现类
 * 
 * @author guowei 2013-4-10
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DistributeDestroyOrderAction extends AbstractAction {
	/** serialVersionUID */
	private static final long serialVersionUID = -2453469342445459220L;
	private static final Logger LOG = Logger.getLogger(DistributeDestroyOrderAction.class);
	private final DistributeDestroyOrderService service = DistributeHelper.getDistributeDestroyOrderService();
	Map<String, String> map = new HashMap<String, String>();

	/**
	 * 初始化页面
	 * @return
	 */
	public String initDistributeDestroyLaod() {
		initParam();
		try {
			// 输出结果
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SOPT_LISTDISTRIBUTEDESTORYORDERS);
			request.setAttribute("init", "true");
			request.setAttribute("destroyType", request.getParameter("destroyType"));
			
		} catch (Exception ex) {
			error(ex);
		}
		return INITPAGE;
	}

	/**
	 * 发放管理--回收单/销毁单菜单
	 * 页面数据一栏显示。
	 * 取得纸质任务生命周期为【已发送】的数据。
	 * /ddm/distributesendorder/listDistributeDestorySearch.jsp
	 * 去的回收销毁纸质任务，生命周期为【分发中】的数据。
	 * @return struts页面参数
	 * 
	 * @modify Sun Zongqing
	 */
	public String getAllDistributeDestoryTasks() {
		initParam();
		session.setAttribute("DDM_DISTRIBUTE_INSIDE", map);
		try {
			
			String lc = ConstUtil.LC_DISTRIBUTED.getName();
			List<DistributePaperTask> listDis = service.getAllDistributePaperTask(map, lc);
			LOG.debug("取得回收销毁单纸质任务 " + getDataSize(listDis) + " 条，生命周期状态：" + lc);
			
			String lcRecDes = ConstUtil.LC_DISTRIBUTING.getName();
			List<RecDesPaperTask> listRecDesPaperTask = service.getAllRecDesPaperTask(map, lcRecDes);
			LOG.debug("取得回收销毁单纸质任务 " + getDataSize(listRecDesPaperTask) + " 条，生命周期状态：" + lcRecDes);

			String spot = ConstUtil.SOPT_LISTDISTRIBUTEDESTORYORDERS;
			List<Map<String, Object>> listDatas = new ArrayList<Map<String, Object>>();
			
			// 批量验证权限
			List<Map<String, Object>> listRecDesDatas = checkPermissionAF(listRecDesPaperTask, spot, spot);
			listDatas.addAll(listRecDesDatas);

			//利用service中写好的方法，屏蔽掉产生过回收销毁任务的纸质任务
			List<DistributePaperTask> disPaperTaskList = service.removeDisTaskConflictWithRecDesTask(listDis, listRecDesPaperTask);
			// 批量验证权限
			List<Map<String, Object>> listPaperTaskDatas = checkPermissionAF(disPaperTaskList, spot, spot);
			listDatas.addAll(listPaperTaskDatas);

			// 输出结果
			GridDataUtil.prepareRowObjects(listDatas, spot);

			result.put("success","true");
			result.put("flag", listDis.size()+"");
			//success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 获得详细的回收销毁任务的详细信息（回收销毁信息和数据）
	 * 与分发任务的详细信息（分发信息和数据）
	 * 
	 * @return 被Strust捕捉的返回信息
	 * @modify Sun Zongqing
	 */
	public String getDistributeDestroyDetails() {
		try {
			map = (Map<String, String>) session.getAttribute("DDM_DISTRIBUTE_INSIDE");
			String destroyType = map.get("destroyType");
			String taskOid = request.getParameter(KeyS.OIDS);
			List<String> oids = SplitString.string2List(taskOid, ",");
			
			String disOids = "";
			String recDesOids = "";
			//从传进来的oid中，拆分分发信息与回收销毁信息的oid
			for(String oid:oids){
				if(DistributePaperTask.CLASSID.equals(Helper.getClassId(oid))){
					disOids += (oid+",");
				}else if(RecDesPaperTask.CLASSID.equals(Helper.getClassId(oid))){
					recDesOids += (oid+",");
				}
			}
			if(disOids != ""){
				disOids = disOids.substring(0,disOids.length()-1);
			}else if(recDesOids!=""){
				recDesOids = recDesOids.substring(0,recDesOids.length()-1);
			}
			
			List<DistributeInfo> listDis = service.getDistributeDestroyDetails(map, disOids);
			//获得回收销毁信息
			List<RecDesInfo> listRecDes = service.getRecDesDetails(map, recDesOids);
			//屏蔽掉产生过回收销毁信息的分发信息
			List<DistributeInfo> listDisAfterRemove = service.removeDisInfoConflictWithRecDesInfo(listDis, listRecDes);
			// 格式化显示数据
			TypeService typeManager = Helper.getTypeManager();
			
			//格式化分发信息
			for (DistributeInfo target : listDisAfterRemove) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mapAll = new HashMap<String, Object>();
				Map<String, Object> mainMap = typeManager.format(target);
				Map<String, Object> objMap = typeManager.format(target.getDistributeObject());
				mapAll.putAll(objMap);
				mainMap.put("NUMBER", objMap.get("NUMBER"));
				mainMap.put("NAME", objMap.get("NAME"));
				mapAll.putAll(mainMap);
				listMap.add(mapAll);
			}
			
			//格式化回收销毁信息
			for (RecDesInfo target : listRecDes) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mapAll = new HashMap<String, Object>();
				Map<String, Object> mainMap = typeManager.format(target);
				Map<String, Object> objMap = typeManager.format(target.getDistributeObject());
				mapAll.putAll(objMap);
				mainMap.put("NUMBER", objMap.get("NUMBER"));
				mainMap.put("NAME", objMap.get("NAME"));
				if("0".equals(destroyType)){
					mainMap.put("RECOVERDESTROYNUM", mainMap.get("NEEDRECOVERNUM"));
					mainMap.put("NEEDRECOVERNUM", mainMap.get("NEEDRECOVERNUM"));
				}else if("1".equals(destroyType)){
					mainMap.put("RECOVERDESTROYNUM", mainMap.get("NEEDDESTROYNUM"));
					mainMap.put("NEEDDESTROYNUM", mainMap.get("NEEDDESTROYNUM"));
				}
				mapAll.putAll(mainMap);
				listMap.add(mapAll);
			}
			
			LOG.debug("取得回收销毁单发送信息 " + getDataSize() + " 条");
			
			
			
			success();
		} catch (Exception ex) {
			error(ex);
		}
		listToJson();
		return OUTPUTDATA;
	}

	/**
	 * 更改分发信息的回收销毁数目，更改相关数据的
	 * 生命周期（分发信息，回收销毁信息，纸质任务、
	 * 回收销毁纸质任务、回收单、销毁单）
	 * 
	 * @return 被Strust捕捉的返回信息
	 * @modify Sun Zongqing
	 */
	public String updateDestroyNum() {
		try {
			map = (Map<String, String>) session.getAttribute("DDM_DISTRIBUTE_INSIDE");
			String taskOids = request.getParameter("taskOids");
			String oids = request.getParameter(KeyS.OIDS);
			String destroyNums = request.getParameter("destroyNums");
			
			List<String> oidList = SplitString.string2List(oids, ",");
			List<String> numList = SplitString.string2List(destroyNums, ",");
			List<String> taskList = SplitString.string2List(taskOids, ",");
			String disOids = "";
			String recDesOids = "";
			String disNums="";
			String recDesNums="";
			String disTask = "";
			String recDesTask = "";
			//从传进来的oid中，拆分分发信息与回收销毁信息的oid
			for(int i =0; i < oidList.size();i++){
				if(DistributeInfo.CLASSID.equals(Helper.getClassId(oidList.get(i)))){
					disOids += (oidList.get(i)+",");
					disNums += (numList.get(i)+",");
				}else if(RecDesInfo.CLASSID.equals(Helper.getClassId(oidList.get(i)))){
					recDesOids += (oidList.get(i)+",");
					recDesNums += (numList.get(i)+",");
				}
			}
			for(String oid:taskList){
				if(DistributePaperTask.CLASSID.equals(Helper.getClassId(oid))){
					disTask += (oid+",");
				}else if(RecDesPaperTask.CLASSID.equals(Helper.getClassId(oid))){
					recDesTask += (oid+",");
				}
			}
			
			//判断oid是否为空
			if(!StringUtil.isStringEmpty(disOids)){
				disOids = disOids.substring(0,disOids.length()-1);
				disNums = disNums.substring(0,disNums.length()-1);
				disTask = disTask.substring(0,disTask.length()-1);
			}else if(!StringUtil.isStringEmpty(recDesOids)){
				recDesOids = recDesOids.substring(0,recDesOids.length()-1);
				recDesNums = recDesNums.substring(0,recDesNums.length()-1);
				recDesTask = recDesTask.substring(0,recDesTask.length()-1);
			}
			service.updateDestroyNum(map, disTask, disOids, disNums);
			service.updateDestroyNumByRecDesTask(map, recDesTask, recDesOids, recDesNums);

			//重新取得最新数据列表
			String lc = ConstUtil.LC_DISTRIBUTED.getName();
			List<DistributePaperTask> listDis = service.getAllDistributePaperTask(map, lc);
			LOG.debug("取得回收销毁单纸质任务 " + getDataSize(listDis) + " 条，生命周期状态：" + lc);
			
			String lcRecDes = ConstUtil.LC_DISTRIBUTING.getName();
			List<RecDesPaperTask> listRecDesPaperTask = service.getAllRecDesPaperTask(map, lcRecDes);
			LOG.debug("取得回收销毁单纸质任务 " + getDataSize(listRecDesPaperTask) + " 条，生命周期状态：" + lcRecDes);

			String spot = ConstUtil.SOPT_LISTDISTRIBUTEDESTORYORDERS;
			List<Map<String, Object>> listDatas = new ArrayList<Map<String, Object>>();
			
			// 批量验证权限
			List<Map<String, Object>> listRecDesDatas = checkPermissionAF(listRecDesPaperTask, spot, spot);
			listDatas.addAll(listRecDesDatas);

			//利用service中写好的方法，屏蔽掉产生过回收销毁任务的纸质任务
			List<DistributePaperTask> disPaperTaskList = service.removeDisTaskConflictWithRecDesTask(listDis, listRecDesPaperTask);
			// 批量验证权限
			List<Map<String, Object>> listPaperTaskDatas = checkPermissionAF(disPaperTaskList, spot, spot);
			listDatas.addAll(listPaperTaskDatas);

			// 输出结果
			GridDataUtil.prepareRowObjects(listDatas, spot);
			
			success();

		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 取得所有的回收销毁纸质任务
	 * 
	 * @return 被strust捕捉的返回信息
	 * @author Sun Zongqing
	 */
	public String getAllRecDesPaperTask(){
		try{
		String spot = ConstUtil.SPOT_LISTDISTRIBUTETASKS;
		//生命周期状态
		String lc = ConstUtil.LC_DISTRIBUTING.getName();
		//获得指定生命周期状态的回收销毁纸质任务
		List<RecDesPaperTask> taskList = service.getAllRecDesPaperTask(lc);
		
		LOG.debug("取得发放单 " + getDataSize(taskList) + " 条，生命周期状态：" + lc);
		
		// 批量验证权限
		List<Map<String, Object>> listDatas = checkPermissionAF(taskList, spot, spot);
		// 输出结果
		GridDataUtil.prepareRowObjects(listDatas, spot);
		}catch(Exception ex){
			error(ex);
		}
		return "listRecDesPaperTask";
	}
	
	private void initParam() {
		map.put("taskName", request.getParameter("taskName"));
		map.put("disObj", request.getParameter("disObj"));
		map.put("taskCode", getParameter("taskCode"));
		map.put("creator", getParameter("creator"));
		map.put("creatorName", request.getParameter("creatorName"));
		map.put("disInfo", getParameter("disInfo"));
		map.put("infoClassId", getParameter("infoClassId"));
		map.put("disInfoName", request.getParameter("disInfoName"));
		map.put("flag", getParameter("flag"));
		map.put("queryCreateDateFrom", getParameter("queryCreateDateFrom"));
		map.put("queryCreateDateTo", getParameter("queryCreateDateTo"));
		map.put("destroyType", getParameter("destroyType"));
		map.put("oid", getParameter("oid"));
	}

	/**
	 * Excel输出调用。
	 */
	public void paperTaskExcel() {
		map = (Map<String, String>) session.getAttribute("DDM_DISTRIBUTE_INSIDE");
		String oids = request.getParameter(KeyS.OIDS);
		String destroyType = request.getParameter("destroyType");
		LOG.debug("执行paperTaskExcel() 请求参数 OID = " + oids);
		String destroyNums = request.getParameter("destroyNums");
		LOG.debug("paperTaskExcel() 请求参数[回收/销毁份数] : destroyNums = " + destroyNums);
		Map mapObject = new HashMap();
		mapObject.put("destroyNums", destroyNums);

		mapObject.put("oids", oids);
		mapObject.putAll(map);
		try {
			// 取得对象。
			HSSFWorkbook wBook = service.getExcelObject(mapObject);
			// Excel文件输出。
			String file_name = "";
			//1:销毁单；0:回收单
			if("0".equals(destroyType)){
				file_name = ConstUtil.REC_TITLE_NAME;
			}else{
				file_name = ConstUtil.DES_TITLE_NAME;
			}
			exportExcel(wBook, file_name);
		} catch (Exception ex) {
			LOG.debug("Excel导出异常", ex);
			throw new RuntimeException(ex);
		}
	}
}
