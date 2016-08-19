package com.bjsasc.ddm.distribute.action.distributeelectask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.action.AbstractAction;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeelectask.DistributeElecTask;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.service.distributeelectask.DistributeElecTaskService;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.bjsasc.plm.type.TypeService;
import com.bjsasc.plm.ui.tree.TreeHelper;
import com.bjsasc.plm.ui.tree.TreeNode;

/**
 * 电子任务Action实现类
 * 
 * @author gengancong 2013-3-18
 */
public class DistributeElecTaskAction extends AbstractAction {

	private static final Logger LOG = Logger.getLogger(DistributeElecTaskAction.class);

	/** serialVersionUID */
	private static final long serialVersionUID = -4470328899394701957L;

	private final DistributeElecTaskService service = DistributeHelper.getDistributeElecTaskService();

	/**
	 * 取得电子任务详细信息。
	 * 
	 * @return 画面ID
	 */
	public String getDistributeElecTaskInfo() {
		try {
			// 电子任务OID
			String oid = (String) session.getAttribute(ConstUtil.DISTRIBUTE_ELECTASK_OID);

			// 取得分发数据列表
			List<DistributeObject> listDisObj = service.getDistributeObjects(oid);
			// 格式化显示数据
			GridDataUtil.prepareRowObjects(listDisObj, ConstUtil.SOPT_LISTDISTRIBUTEELECOBJECTS);
//			// 格式化显示数据
//			TypeService typeManager = Helper.getTypeManager();
//			for (DistributeObject target : listDisObj) {
//				if (target == null) {
//					continue;
//				}
//				Map<String, Object> mapAll = new HashMap<String, Object>();
//				Map<String, Object> mainMap = typeManager.format(target);
//				Map<String, Object> linkMap = typeManager.format(target.getDistributeOrderObjectLink());
//				Map<String, Object> sourceMap = new HashMap<String, Object>();
//				Map<String, Object> sourceMap1 = (Map<String, Object>) mainMap.get(KeyS.SOURCE);
//				Map<String, Object> sourceMap2 = (Map<String, Object>) linkMap.get(KeyS.SOURCE);
//				sourceMap.putAll(sourceMap1);
//				sourceMap.putAll(sourceMap2);
//				mapAll.putAll(linkMap);
//				mapAll.putAll(mainMap);
//				mapAll.put(KeyS.SOURCE, sourceMap);
//				listMap.add(mapAll);
//			}
//			LOG.debug("取得分发数据 " + getDataSize() + " 条");
//			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SOPT_LISTDISTRIBUTEELECOBJECTS);

		} catch (Exception ex) {
			error(ex);
		}
		return "detailInfo";
	}

	/**
	 * 取得转发数据
	 * 
	 * @return
	 */
	public String getDistributeElecTaskTreeObject() {
		String oid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ELECTASK_OID);
		List<DistributeObject> listDisObj = service.getDistributeObjects(oid);
		for (DistributeObject dis : listDisObj) {
			Map<String, Object> node = TreeHelper.buildTreeNode(dis).toMap();

			// 显示图片
			String dataOid = Helper.getOid(dis.getDataClassId(), dis.getDataInnerId());
			Persistable obj = Helper.getPersistService().getObject(dataOid);
			if (obj != null) {
				Map<String, Object> dataMap = TreeHelper.buildTreeNode(obj).toMap();
				node.put("iconsrc", dataMap.get("iconsrc"));
			} else {
				node.put("iconsrc", "");
			}

			node.put("Name", dis.getName());
			node.put(TreeNode.KEY_CHILDURL, "");
			node.put(TreeNode.KEY_EXPANDED, true);
			DistributeOrderObjectLink distributeOrderObjectLink = dis.getDistributeOrderObjectLink();
			node.put("DISTRIBUTEORDEROBJECTLINKOID", distributeOrderObjectLink.getClassId() + ":"
					+ distributeOrderObjectLink.getInnerId());
			listMap.add(node);
		}
		LOG.debug("取得分发数据 " + getDataSize() + " 条");
		encode();
		return OUTPUTDATA;
	}

	/**
	 * 转发电子任务详细信息
	 */
	public String getDistributeForwardElecTaskInfo() {
		// 电子任务OID
		String oid = request.getParameter("oid");
		String type = request.getParameter("type");
		try {
			// 取得分发信息列表
			List<DistributeInfo> listDis = service.getDistributeForwardInfos(oid);

			TypeService typeManager = Helper.getTypeManager();
			for (DistributeInfo target : listDis) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mainMap = typeManager.format(target);
				listMap.add(mainMap);
			}
			LOG.debug("转发电子信息取得分发信息 " + getDataSize(listDis) + " 条");
			GridDataUtil.prepareRowObjects(listMap, ConstUtil.SPOT_LISTDISTRIBUTEINFOS);

			request.setAttribute("taskOid", oid);

		} catch (Exception ex) {
			error(ex);
		}
		if (type != null && type.equals("edit")) {
			return "infoEdit";
		} else {
			return INITPAGE;
		}
	}

	public String getDistributeForwardElecTaskEditInfo() {
		// 电子任务OID
		String oid = request.getParameter("oid");
		String linkOids = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
		// 取得分发信息列表
		List<DistributeInfo> listDis = service.getDistributeForwardInfoByOids(linkOids, oid);
		LOG.debug("转发电子信息取得分发信息 " + getDataSize(listDis) + " 条");
		TypeService typeManager = Helper.getTypeManager();
		for (DistributeInfo target : listDis) {
			if (target == null) {
				continue;
			}
			Map<String, Object> mainMap = typeManager.format(target);
			listMap.add(mainMap);
		}
		request.setAttribute("taskOid", oid);
		listToJson();
		return OUTPUTDATA;
	}

	/**
	 * 取得分发信息数据。
	 * 
	 * @return JSON对象
	 */
	public String getDistributeInfosByOids() {
		try {
			String linkOids = request.getParameter("distributeOrderObjectLinkOids");
			String taskOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ELECTASK_OID);

			session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS, linkOids);

			// 取得分发信息数据。
			List<DistributeInfo> list = service.getDistributeForwardInfoByOids(linkOids, taskOid);
			TypeService typeManager = Helper.getTypeManager();
			for (DistributeInfo target : list) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mainMap = typeManager.format(target);
				listMap.add(mainMap);
			}
			LOG.debug("取得分发信息 " + getDataSize(list) + " 条");
			// 输出结果
			GridDataUtil.prepareRowObjects(listMap, ConstUtil.SPOT_LISTDISTRIBUTEINFOS);

			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 更新发放信息备注
	 */
	public String updateDistributeInfos() {
		try {
			String oids = request.getParameter(KeyS.OIDS);
			String notes = request.getParameter("notes");
			service.updateDistributeInfos(oids, notes);

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 创建转发电子任务
	 */
	public String createDistributeForwardElecTask() {
		try {
			String oids = request.getParameter("OIDS");
			String taskOid = request.getParameter("taskOid");

			service.createDistributeForwardElecTask(taskOid, oids);

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 创建电子分发信息
	 */
	public String addDistributeInfos() {
		try {

			String iids = request.getParameter("iids");
			String linkOids = request.getParameter("distributeOrderObjectLinkOids");
			String taskOid = request.getParameter("taskOid");
			String type = request.getParameter("type");

			String size = service.getExitDistributeElecInfo(linkOids, iids, type);
			if ("0".equals(size)) {
				service.createDistributeInfos(linkOids, iids, taskOid, type);
			}
			result.put("success", "true");
			result.put("flag", size);
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	public String deleteDistributeInfos() {
		try {
			//取得分发信息OIDS
			String oids = request.getParameter(KeyS.OIDS);
			String flag = service.deleteDistributeInfos(oids);

			result.put("flag", flag);
			result.put("success", "true");
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	//任务树
	public String getDistributeSonElecTask() {
		String oids = (String) session.getAttribute(ConstUtil.DISTRIBUTE_ELECTASK_OID);
		List<DistributeElecTask> listSontask = service.getDistributeSonElecTask(oids);
		TypeService typeManager = Helper.getTypeManager();

		for (DistributeElecTask target : listSontask) {
			if (target == null) {
				continue;
			}
			Map<String, Object> mapAll = new HashMap<String, Object>();
			Map<String, Object> mainMap = typeManager.format(target);
			Map<String, Object> sourceMap = new HashMap<String, Object>();
			Map<String, Object> disinfoMap = typeManager.format(target.getDisinfo());
			Map<String, Object> sourceMap1 = (Map<String, Object>) mainMap.get(KeyS.SOURCE);
			Map<String, Object> sourceMap2 = (Map<String, Object>) disinfoMap.get(KeyS.SOURCE);
			sourceMap.putAll(sourceMap1);
			sourceMap.putAll(sourceMap2);
			mapAll.putAll(disinfoMap);
			mapAll.putAll(mainMap);
			mapAll.put(KeyS.SOURCE, sourceMap);
			listMap.add(mapAll);
		}
		LOG.debug("取得电子任务的子任务 " + getDataSize(listSontask) + "条");
		// 输出结果
		GridDataUtil.prepareRowObjects(listMap, ConstUtil.SPOT_LISTDISTRIBUTEELECTASKS);
		return "dissontasks";
	}

	/**
	 * 取得发放单相关中心任务
	 * 
	 * @return
	 */
	public String getAllDistributeCenterTask() {
		// 获取发放单OID
		String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
		DistributeElecTaskService service = DistributeHelper.getDistributeElecTaskService();

		TypeService typeManager = Helper.getTypeManager();
		//0:中心任务;1:接收方任务
		List<DistributeElecTask> elecList = service.getAllDistributeSynTasks(distributeOrderOid, 0);

		LOG.debug("取得电子任务 " + getDataSize(elecList) + " 条");
		// 格式化显示数据
		for (DistributeElecTask target : elecList) {
			if (target == null) {
				continue;
			}
			target.setOperate("");
			target.setOperates("");
			Map<String, Object> mapAll = new HashMap<String, Object>();
			Map<String, Object> mainMap = typeManager.format(target);
			Map<String, Object> returnMap = typeManager.format(target.getElecReturnReason());
			if(returnMap != null){
				mapAll.putAll(returnMap);
			}
			mapAll.putAll(mainMap);
			listMap.add(mapAll);
		}
		// 输出结果
		GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTESYNTASKS);
		return "centerTask";
	}

	/**
	 * 取得发放单相关接收方任务
	 * 
	 * @return
	 */
	public String getAllDistributeReceiveTask() {
		// 获取发放单OID
		String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
		DistributeElecTaskService service = DistributeHelper.getDistributeElecTaskService();

		TypeService typeManager = Helper.getTypeManager();
		//0:中心任务;1:接收方任务
		List<DistributeElecTask> elecList = service.getAllDistributeSynTasks(distributeOrderOid, 1);

		LOG.debug("取得电子任务 " + getDataSize(elecList) + " 条");
		for (DistributeElecTask target : elecList) {
			if (target == null) {
				continue;
			}
			target.setOperate("");
			target.setOperates("");
			Map<String, Object> mapAll = new HashMap<String, Object>();
			Map<String, Object> mainMap = typeManager.format(target);
			Map<String, Object> returnMap = typeManager.format(target.getElecReturnReason());
			if(returnMap != null){
				mapAll.putAll(returnMap);
			}
			mapAll.putAll(mainMap);
			listMap.add(mapAll);
		}
		// 输出结果
		GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTESYNTASKS);
		return "synTask";
	}

}
