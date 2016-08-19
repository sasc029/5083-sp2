package com.bjsasc.ddm.distribute.action.distributetask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.avidm.core.site.Site;
import com.bjsasc.avidm.core.site.SiteHelper;
import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.common.ConstUtil.LIFECYCLE_OPT;
import com.bjsasc.ddm.distribute.action.AbstractAction;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeelectask.DistributeElecTask;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributepapersigntask.DistributePaperSignTask;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.model.recdespapertask.RecDesPaperTask;
import com.bjsasc.ddm.distribute.service.distributeelectask.DistributeElecTaskService;
import com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService;
import com.bjsasc.ddm.distribute.service.distributepapersigntask.DistributePaperSignTaskService;
import com.bjsasc.ddm.distribute.service.distributetask.DistributeTaskService;
import com.bjsasc.ddm.distribute.service.duplicateprocess.DuplicateProcessService;
import com.bjsasc.ddm.distribute.service.recdespapertask.RecDesPaperTaskService;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.collaboration.site.model.DCSiteAttribute;
import com.bjsasc.plm.collaboration.site.service.DCSiteAttributeHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.grid.GridHelper;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.bjsasc.plm.type.TypeService;
import com.bjsasc.plm.util.RequestUtil;

import org.apache.log4j.Logger;

/**
 * 分发任务Action实现类。
 * 
 * @author gengancong 2013-2-27
 */
public class DistributeTaskAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = 7281038450079134336L;

	private final DistributeTaskService service = DistributeHelper.getDistributeTaskService();
	private final RecDesPaperTaskService recDesPaperTaskService = DistributeHelper.getRecDesPaperTaskService();
	private final DistributeElecTaskService disElecTaskService = DistributeHelper.getDistributeElecTaskService();
	private final DistributePaperSignTaskService disPaperSignTaskService = DistributeHelper.getDistributePaperSignTaskService();

	private static final Logger LOG = Logger.getLogger(DistributeTaskAction.class);

	/**
	 * 取得分发任务。
	 * 
	 * @return JSON对象
	 */
	public String getAllDistributeTask() {
		// 获取发放单OID
		String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
		if(distributeOrderOid == null){
			distributeOrderOid = RequestUtil.getParamOid(request);
		}
		DistributeTaskService service = DistributeHelper.getDistributeTaskService();

		TypeService typeManager = Helper.getTypeManager();
		DistributeOrder disOrder = (DistributeOrder) Helper.getPersistService().getObject(distributeOrderOid);
		//0发放单，1补发发放单，2回收发放单，3销毁发放单
		String orderType = disOrder.getOrderType();
		if(orderType.equals(ConstUtil.C_ORDERTYPE_3) || orderType.equals(ConstUtil.C_ORDERTYPE_2)){
			List<RecDesPaperTask> paperList = recDesPaperTaskService.getRecDesPaperTasksByDistributeOrderOid(distributeOrderOid);
			LOG.debug("取得纸质任务 " + getDataSize(paperList) + " 条");
			for (RecDesPaperTask recDespaperTask : paperList) {
				listMap.add(typeManager.format(recDespaperTask));
			}
		} else {
			List<DistributePaperTask> paperList = service.getDistributePaperTasksByDistributeOrderOid(distributeOrderOid);

			LOG.debug("取得纸质任务 " + getDataSize(paperList) + " 条");
			Map<String, Object> mapAll = null;
			for (DistributePaperTask paperTask : paperList) {
				mapAll = new HashMap<String, Object>();
				String stateName = paperTask.getLifeCycleInfo().getStateName();
				if(ConstUtil.LC_PROCESSING_BACKOFF.getName().equals(stateName)
						|| ConstUtil.LC_DUPLICATE_BACKOFF.getName().equals(stateName)){
					mapAll.putAll(typeManager.format(paperTask.getReturnReason()));
					mapAll.putAll(typeManager.format(paperTask));
				}else{
					mapAll.putAll(typeManager.format(paperTask));
				}
				listMap.add(mapAll);
			}
			List<DistributeElecTask> elecList = service.getDistributeElecTasksByDistributeOrderOid(distributeOrderOid);

			LOG.debug("取得电子任务 " + getDataSize(elecList) + " 条");
			for (DistributeElecTask elecTask : elecList) {
				//DistributeElecTask target = (DistributeElecTask)obj;
				Map<String, Object> mainMap = typeManager.format(elecTask);
				String returnReason = elecTask.getReturnReason();
				if (returnReason == null || "null".equals(returnReason)) {
					returnReason = "";
				}
				mainMap.put("RETURNREASON", returnReason);
				mainMap.put(KeyS.ACCESS, true);
				listMap.add(mainMap);
				
				//listMap.add(typeManager.format(elecTask));
			}
			
			List<DistributePaperSignTask> paperSignList = disPaperSignTaskService.getDistributePaperSignTasksByDistributeOrderOid(distributeOrderOid);

			LOG.debug("取得纸质签收任务 " + getDataSize(paperSignList) + " 条");
			for (DistributePaperSignTask paperSignTask : paperSignList) {
				Map<String, Object> mainMap = typeManager.format(paperSignTask);
				String returnReason = paperSignTask.getReturnReason();
				if (returnReason == null || "null".equals(returnReason)) {
					returnReason = "";
				}
				mainMap.put("RETURNREASON", returnReason);
				mainMap.put(KeyS.ACCESS, true);
				listMap.add(mainMap);
				//listMap.add(typeManager.format(paperSignTask));
			}
		}

		// 输出结果
		GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTETASKS);
		return INITPAGE;
	}

	/**
	 * 修改纸质任务生命周期状态
	 * 
	 * @return JSON对象
	 */
	public String updateDistributeTask() {
		try {
			String oids = request.getParameter(KeyS.OIDS);
			String flag = request.getParameter("flag");
			String type = request.getParameter("type");
			String returnReason = request.getParameter("returnReason");
			if ("null".equals(returnReason)) {
				returnReason = "";
			}
			String taskOid = request.getParameter("taskOid");
			LIFECYCLE_OPT opt = null;
			if ("promote".equals(flag)) {
				opt = LIFECYCLE_OPT.PROMOTE;
			} else if ("demote".equals(flag)) {
				opt = LIFECYCLE_OPT.DEMOTE;
			} else if ("reject".equals(flag)) {
				opt = LIFECYCLE_OPT.REJECT;
			} else {
				opt = null;
			}

			if ("returnTask".equals(type)) {
				DistributeOrderService service = DistributeHelper.getDistributeOrderService();
				service.updateDisOrderLifeCycle(oids, opt, returnReason);
			} else if ("disagree".equals(type)) {
				DuplicateProcessService service = DistributeHelper.getDuplicateProcessService();
				service.updateDisAgreeInfo(oids, returnReason, taskOid);
			} else {
				service.updateDistributeTask(oids, opt, returnReason);
			}
			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 取得未签收电子任务。
	 * 
	 * @return 画面ID
	 */
	public String getAllDistributeElecTaskNotSigned() {
		try {
			getAllNoSignDistributeElecTask(ConstUtil.LC_NOT_SIGNED.getName(),
					ConstUtil.SPOT_LISTDISTRIBUTEELECTASKSNOTSIGNED);

		} catch (Exception ex) {
			error(ex);
		}
		return "notSigned";
	}

	/**
	 * 取得未签收电子任务。
	 * 
	 * @param state
	 *            String
	 */
	private void getAllNoSignDistributeElecTask(String state, String spot) {
		List<Map<String, Object>> listDis = new ArrayList<Map<String, Object>>();
//		String spot = ConstUtil.SPOT_LISTDISTRIBUTEELECTASKSNOTSIGNED;
		
		//电子任务
		List<DistributeElecTask> disElecTasklistDis = disElecTaskService.getAllNoSignDistributeElecTask(state);
		// 批量验证权限
		List<Map<String, Object>> disElecTasklistDatas = checkPermissionAF(disElecTasklistDis, spot, spot);
		listDis.addAll(disElecTasklistDatas);
		
		//纸质签收任务
//		String spot = ConstUtil.SPOT_LISTDISTRIBUTEELECTASKSNOTSIGNED;
		List<DistributePaperSignTask> disPaperSignTasklistDis = disPaperSignTaskService.getAllNoSignDistributePaperSignTask(state);
		// 批量验证权限
		List<Map<String, Object>> disPaperSignTasklistDatas = checkPermissionAF(disPaperSignTasklistDis, spot, spot);
		listDis.addAll(disPaperSignTasklistDatas);
		LOG.debug("取得电子任务 " + getDataSize(listDis) + " 条，生命周期状态：" + state + "，用户信息：" + getCurrentUser());

		
		// 输出结果
		GridDataUtil.prepareRowObjects(listDis, spot);
	}

	/**
	 * 取得电子任务。
	 * 
	 * @param state
	 *            String
	 */
	private void getAllDistributeElecTask(String state, String spot) {
		List<DistributeElecTask> listDis = disElecTaskService.getAllDistributeElecTask(state);
		boolean flat = disElecTaskService.isUserlimit();
		
		// 批量验证权限
		List<Object> checkList = checkPermission(listDis, spot, spot);
		
		List<String> keys = GridHelper.getService().getMyLatestGridViewColumnIds(spot, spot);
		
		TypeService typeManager = Helper.getTypeManager();

		for (Object obj : checkList) {
			if (obj == null) {
				continue;
			}
			DistributeElecTask target = (DistributeElecTask)obj;
			if (flat == false) {
				target.setOperate("");
			}
			Map<String, Object> mapAll = new HashMap<String, Object>();
			Map<String, Object> mainMap = typeManager.format(target, keys, false);
			//			Map<String, Object> disinfoMap = typeManager.format(target.getDisinfo());
			//			mapAll.putAll(disinfoMap);
			mapAll.putAll(mainMap);
			mapAll.put(KeyS.ACCESS, true);
			listMap.add(mapAll);
		}
		LOG.debug("取得电子任务 " + getDataSize(listMap) + " 条，生命周期状态：" + state + "，用户信息：" + getCurrentUser());
		
		List<DistributePaperSignTask> listPaperSignDis = disPaperSignTaskService.getAllDistributePaperSignTask(state);
		
		// 批量验证权限
		List<Object> checkPaperSignList = checkPermission(listPaperSignDis, spot, spot);
		
		List<String> keysPaperSign = GridHelper.getService().getMyLatestGridViewColumnIds(spot, spot);
		
		TypeService typeManagerPaperSign = Helper.getTypeManager();

		for (Object obj : checkPaperSignList) {
			if (obj == null) {
				continue;
			}
			DistributePaperSignTask target = (DistributePaperSignTask)obj;
//			//纸质签收任务不涉及转发
//			if (flat == false) {
//				target.setOperate("");
//			}
			Map<String, Object> mapAll = new HashMap<String, Object>();
			Map<String, Object> mainMap = typeManagerPaperSign.format(target, keysPaperSign, false);
			//			Map<String, Object> disinfoMap = typeManager.format(target.getDisinfo());
			//			mapAll.putAll(disinfoMap);
			mapAll.putAll(mainMap);
			mapAll.put(KeyS.ACCESS, true);
			listMap.add(mapAll);
		}
		LOG.debug("取得纸质签收任务和电子任务合计 " + getDataSize(listMap) + " 条，生命周期状态：" + state + "，用户信息：" + getCurrentUser());
		
		
		GridDataUtil.prepareRowObjects(listMap, spot);
	}

	/**
	 * 取得已签收电子任务。
	 * 
	 * @return 画面ID
	 */
	public String getAllDistributeElecTaskSigned() {
		try {
			getAllDistributeElecTask(ConstUtil.LC_SIGNED.getName(), ConstUtil.SPOT_LISTDISTRIBUTEELECTASKSSIGNED);

		} catch (Exception ex) {
			error(ex);
		}
		return "signed";
	}

	/**
	 * 取得已完成电子任务。
	 * 
	 * @return 画面ID
	 */
	public String getAllDistributeElecTaskCompleted() {
		try {
			getAllDistributeElecTask(ConstUtil.LC_COMPLETED.getName(), ConstUtil.SPOT_LISTDISTRIBUTEELECTASKSCOMPLETED);

		} catch (Exception ex) {
			error(ex);
		}
		return "completed";
	}

	/**
	 * 取得拒绝签收任务。
	 * 
	 * @return 画面ID
	 */
	public String getAllDistributeElecTaskRefuseSigned() {
		try {
			String lc = ConstUtil.LC_REFUSE_SIGNED.getName();
			
			List<DistributeElecTask> listDis = disElecTaskService.getAllReturnDistributeElecTask(lc);
			LOG.debug("取得电子任务 " + getDataSize(listDis) + " 条，生命周期状态：" + lc + "，用户信息：" + getCurrentUser());
			
			String spot = ConstUtil.SPOT_LISTDISTRIBUTEELECTASKSREFUSESIGNED;
			
			// 批量验证权限
			List<Object> checkList = checkPermission(listDis, spot, spot);
			
			List<String> keys = GridHelper.getService().getMyLatestGridViewColumnIds(spot, spot);
			
			TypeService typeManager = Helper.getTypeManager();
			for (Object obj : checkList) {
				if (obj == null) {
					continue;
				}
				DistributeElecTask target = (DistributeElecTask)obj;
				Map<String, Object> mainMap = typeManager.format(target, keys, false);
				String returnReason = target.getReturnReason();
				if (returnReason == null || "null".equals(returnReason)) {
					returnReason = "";
				}
				mainMap.put("RETURNREASON", returnReason);
				mainMap.put(KeyS.ACCESS, true);
				listMap.add(mainMap);
			}
			
			List<DistributePaperSignTask> listPaperSignDis = disPaperSignTaskService.getAllReturnDistributePaperSignTask(lc);
			LOG.debug("取得纸质签收任务 " + getDataSize(listDis) + " 条，生命周期状态：" + lc + "，用户信息：" + getCurrentUser());
			
//			String spot = ConstUtil.SPOT_LISTDISTRIBUTEELECTASKSREFUSESIGNED;
			
			// 批量验证权限
			List<Object> checkPaperSignList = checkPermission(listPaperSignDis, spot, spot);
			
			List<String> keysPaperSign = GridHelper.getService().getMyLatestGridViewColumnIds(spot, spot);
			
			TypeService typeManagerPaperSign = Helper.getTypeManager();
			for (Object obj : checkPaperSignList) {
				if (obj == null) {
					continue;
				}
				DistributePaperSignTask target = (DistributePaperSignTask)obj;
				Map<String, Object> mainMap = typeManagerPaperSign.format(target, keysPaperSign, false);
				String returnReason = target.getReturnReason();
				if (returnReason == null || "null".equals(returnReason)) {
					returnReason = "";
				}
				mainMap.put("RETURNREASON", returnReason);
				mainMap.put(KeyS.ACCESS, true);
				listMap.add(mainMap);
			}
			
			GridDataUtil.prepareRowObjects(listMap, spot);
		} catch (Exception ex) {
			error(ex);
		}
		return "refuseSigned";
	}

	/**
	 * 修改电子任务生命周期状态
	 * 
	 * @return JSON对象
	 */
	public String updateDistributeElecTask() {
		try {
			String oids = request.getParameter(KeyS.OIDS);
			String operate = request.getParameter("operate");
			String returnReason = request.getParameter("returnReason");

			//判断用户是否有下载权限
			//boolean flag = service.getUserPrivilege(oids);

			LIFECYCLE_OPT opt = null;
			if ("signed".equals(operate)) {
				opt = LIFECYCLE_OPT.PROMOTE;
			} else if ("refuse".equals(operate)) {
				opt = LIFECYCLE_OPT.REJECT;
			}
			String elecOids = "";
			String paperSignOids = "";
			String taskOid[] = oids.split(",");
			for (String oid : taskOid) {
				Persistable taskobj = Helper.getPersistService().getObject(oid);
				if (taskobj instanceof DistributeElecTask) {
					elecOids = elecOids + oid + ",";
				}else if (taskobj instanceof DistributePaperSignTask) {
					paperSignOids = paperSignOids + oid + ",";
				}
			}
			
			if(!StringUtil.isStringEmpty(elecOids)){
				elecOids = elecOids.substring(0,elecOids.length()-1);
				disElecTaskService.updateDistributeElecTask(elecOids, opt, returnReason);
			}

			if(!StringUtil.isStringEmpty(paperSignOids)){
				paperSignOids = paperSignOids.substring(0,paperSignOids.length()-1);
				disPaperSignTaskService.updateDistributePaperSignTask(paperSignOids, opt, returnReason);
			}
			
			getAllNoSignDistributeElecTask(ConstUtil.LC_NOT_SIGNED.getName(),
					ConstUtil.SPOT_LISTDISTRIBUTEELECTASKSNOTSIGNED);

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 已签收电子任务生命周期升级
	 * 
	 * @return
	 */
	public String updateDistributeElecTaskLife() {
		try {
			// 获取电子任务的OIDS
			String oids = request.getParameter(KeyS.OIDS);
			
			String elecOids = "";
			String paperSignOids = "";
			String taskOid[] = oids.split(",");
			for (String oid : taskOid) {
				Persistable taskobj = Helper.getPersistService().getObject(oid);
				if (taskobj instanceof DistributeElecTask) {
					elecOids = elecOids + oid + ",";
				}else if (taskobj instanceof DistributePaperSignTask) {
					paperSignOids = paperSignOids + oid + ",";
				}
			}

			if(!StringUtil.isStringEmpty(elecOids)){
				Site selfSite = SiteHelper.getSiteService().findLocalSiteInfo();
				if (selfSite != null) {
					DCSiteAttribute dcSiteAttr = DCSiteAttributeHelper.getDCSiteAttrService().findDcSiteAttrByDTSiteId(
							selfSite.getInnerId());
					if (dcSiteAttr != null && "true".equals(dcSiteAttr.getIsSiteControl())) {
						//向外域发送
						disElecTaskService.sendDistributeToOutSign(elecOids);
					}
				}
				//任务升级
				disElecTaskService.updateDistributeElecTaskLife(elecOids);
			}

			if(!StringUtil.isStringEmpty(paperSignOids)){
//				//纸质签收任务不需要向外域发送
//				Site selfSite = SiteHelper.getSiteService().findLocalSiteInfo();
//				if (selfSite != null) {
//					DCSiteAttribute dcSiteAttr = DCSiteAttributeHelper.getDCSiteAttrService().findDcSiteAttrByDTSiteId(
//							selfSite.getInnerId());
//					if (dcSiteAttr != null && "true".equals(dcSiteAttr.getIsSiteControl())) {
//						//向外域发送
//						disPaperSignTaskService.sendDistributeToOutSign(paperSignOids);
//					}
//				}
				//任务升级
				disPaperSignTaskService.updateDistributePaperSignTaskLife(paperSignOids);
			}
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

}
