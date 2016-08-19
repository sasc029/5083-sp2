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
 * ��������Actionʵ����
 * 
 * @author gengancong 2013-3-18
 */
public class DistributeElecTaskAction extends AbstractAction {

	private static final Logger LOG = Logger.getLogger(DistributeElecTaskAction.class);

	/** serialVersionUID */
	private static final long serialVersionUID = -4470328899394701957L;

	private final DistributeElecTaskService service = DistributeHelper.getDistributeElecTaskService();

	/**
	 * ȡ�õ���������ϸ��Ϣ��
	 * 
	 * @return ����ID
	 */
	public String getDistributeElecTaskInfo() {
		try {
			// ��������OID
			String oid = (String) session.getAttribute(ConstUtil.DISTRIBUTE_ELECTASK_OID);

			// ȡ�÷ַ������б�
			List<DistributeObject> listDisObj = service.getDistributeObjects(oid);
			// ��ʽ����ʾ����
			GridDataUtil.prepareRowObjects(listDisObj, ConstUtil.SOPT_LISTDISTRIBUTEELECOBJECTS);
//			// ��ʽ����ʾ����
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
//			LOG.debug("ȡ�÷ַ����� " + getDataSize() + " ��");
//			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SOPT_LISTDISTRIBUTEELECOBJECTS);

		} catch (Exception ex) {
			error(ex);
		}
		return "detailInfo";
	}

	/**
	 * ȡ��ת������
	 * 
	 * @return
	 */
	public String getDistributeElecTaskTreeObject() {
		String oid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ELECTASK_OID);
		List<DistributeObject> listDisObj = service.getDistributeObjects(oid);
		for (DistributeObject dis : listDisObj) {
			Map<String, Object> node = TreeHelper.buildTreeNode(dis).toMap();

			// ��ʾͼƬ
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
		LOG.debug("ȡ�÷ַ����� " + getDataSize() + " ��");
		encode();
		return OUTPUTDATA;
	}

	/**
	 * ת������������ϸ��Ϣ
	 */
	public String getDistributeForwardElecTaskInfo() {
		// ��������OID
		String oid = request.getParameter("oid");
		String type = request.getParameter("type");
		try {
			// ȡ�÷ַ���Ϣ�б�
			List<DistributeInfo> listDis = service.getDistributeForwardInfos(oid);

			TypeService typeManager = Helper.getTypeManager();
			for (DistributeInfo target : listDis) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mainMap = typeManager.format(target);
				listMap.add(mainMap);
			}
			LOG.debug("ת��������Ϣȡ�÷ַ���Ϣ " + getDataSize(listDis) + " ��");
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
		// ��������OID
		String oid = request.getParameter("oid");
		String linkOids = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
		// ȡ�÷ַ���Ϣ�б�
		List<DistributeInfo> listDis = service.getDistributeForwardInfoByOids(linkOids, oid);
		LOG.debug("ת��������Ϣȡ�÷ַ���Ϣ " + getDataSize(listDis) + " ��");
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
	 * ȡ�÷ַ���Ϣ���ݡ�
	 * 
	 * @return JSON����
	 */
	public String getDistributeInfosByOids() {
		try {
			String linkOids = request.getParameter("distributeOrderObjectLinkOids");
			String taskOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ELECTASK_OID);

			session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS, linkOids);

			// ȡ�÷ַ���Ϣ���ݡ�
			List<DistributeInfo> list = service.getDistributeForwardInfoByOids(linkOids, taskOid);
			TypeService typeManager = Helper.getTypeManager();
			for (DistributeInfo target : list) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mainMap = typeManager.format(target);
				listMap.add(mainMap);
			}
			LOG.debug("ȡ�÷ַ���Ϣ " + getDataSize(list) + " ��");
			// ������
			GridDataUtil.prepareRowObjects(listMap, ConstUtil.SPOT_LISTDISTRIBUTEINFOS);

			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * ���·�����Ϣ��ע
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
	 * ����ת����������
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
	 * �������ӷַ���Ϣ
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
			//ȡ�÷ַ���ϢOIDS
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

	//������
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
		LOG.debug("ȡ�õ�������������� " + getDataSize(listSontask) + "��");
		// ������
		GridDataUtil.prepareRowObjects(listMap, ConstUtil.SPOT_LISTDISTRIBUTEELECTASKS);
		return "dissontasks";
	}

	/**
	 * ȡ�÷��ŵ������������
	 * 
	 * @return
	 */
	public String getAllDistributeCenterTask() {
		// ��ȡ���ŵ�OID
		String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
		DistributeElecTaskService service = DistributeHelper.getDistributeElecTaskService();

		TypeService typeManager = Helper.getTypeManager();
		//0:��������;1:���շ�����
		List<DistributeElecTask> elecList = service.getAllDistributeSynTasks(distributeOrderOid, 0);

		LOG.debug("ȡ�õ������� " + getDataSize(elecList) + " ��");
		// ��ʽ����ʾ����
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
		// ������
		GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTESYNTASKS);
		return "centerTask";
	}

	/**
	 * ȡ�÷��ŵ���ؽ��շ�����
	 * 
	 * @return
	 */
	public String getAllDistributeReceiveTask() {
		// ��ȡ���ŵ�OID
		String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
		DistributeElecTaskService service = DistributeHelper.getDistributeElecTaskService();

		TypeService typeManager = Helper.getTypeManager();
		//0:��������;1:���շ�����
		List<DistributeElecTask> elecList = service.getAllDistributeSynTasks(distributeOrderOid, 1);

		LOG.debug("ȡ�õ������� " + getDataSize(elecList) + " ��");
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
		// ������
		GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTESYNTASKS);
		return "synTask";
	}

}
