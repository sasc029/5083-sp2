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
 * �ַ�����Actionʵ���ࡣ
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
	 * ȡ�÷ַ�����
	 * 
	 * @return JSON����
	 */
	public String getAllDistributeTask() {
		// ��ȡ���ŵ�OID
		String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
		if(distributeOrderOid == null){
			distributeOrderOid = RequestUtil.getParamOid(request);
		}
		DistributeTaskService service = DistributeHelper.getDistributeTaskService();

		TypeService typeManager = Helper.getTypeManager();
		DistributeOrder disOrder = (DistributeOrder) Helper.getPersistService().getObject(distributeOrderOid);
		//0���ŵ���1�������ŵ���2���շ��ŵ���3���ٷ��ŵ�
		String orderType = disOrder.getOrderType();
		if(orderType.equals(ConstUtil.C_ORDERTYPE_3) || orderType.equals(ConstUtil.C_ORDERTYPE_2)){
			List<RecDesPaperTask> paperList = recDesPaperTaskService.getRecDesPaperTasksByDistributeOrderOid(distributeOrderOid);
			LOG.debug("ȡ��ֽ������ " + getDataSize(paperList) + " ��");
			for (RecDesPaperTask recDespaperTask : paperList) {
				listMap.add(typeManager.format(recDespaperTask));
			}
		} else {
			List<DistributePaperTask> paperList = service.getDistributePaperTasksByDistributeOrderOid(distributeOrderOid);

			LOG.debug("ȡ��ֽ������ " + getDataSize(paperList) + " ��");
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

			LOG.debug("ȡ�õ������� " + getDataSize(elecList) + " ��");
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

			LOG.debug("ȡ��ֽ��ǩ������ " + getDataSize(paperSignList) + " ��");
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

		// ������
		GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTETASKS);
		return INITPAGE;
	}

	/**
	 * �޸�ֽ��������������״̬
	 * 
	 * @return JSON����
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
	 * ȡ��δǩ�յ�������
	 * 
	 * @return ����ID
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
	 * ȡ��δǩ�յ�������
	 * 
	 * @param state
	 *            String
	 */
	private void getAllNoSignDistributeElecTask(String state, String spot) {
		List<Map<String, Object>> listDis = new ArrayList<Map<String, Object>>();
//		String spot = ConstUtil.SPOT_LISTDISTRIBUTEELECTASKSNOTSIGNED;
		
		//��������
		List<DistributeElecTask> disElecTasklistDis = disElecTaskService.getAllNoSignDistributeElecTask(state);
		// ������֤Ȩ��
		List<Map<String, Object>> disElecTasklistDatas = checkPermissionAF(disElecTasklistDis, spot, spot);
		listDis.addAll(disElecTasklistDatas);
		
		//ֽ��ǩ������
//		String spot = ConstUtil.SPOT_LISTDISTRIBUTEELECTASKSNOTSIGNED;
		List<DistributePaperSignTask> disPaperSignTasklistDis = disPaperSignTaskService.getAllNoSignDistributePaperSignTask(state);
		// ������֤Ȩ��
		List<Map<String, Object>> disPaperSignTasklistDatas = checkPermissionAF(disPaperSignTasklistDis, spot, spot);
		listDis.addAll(disPaperSignTasklistDatas);
		LOG.debug("ȡ�õ������� " + getDataSize(listDis) + " ������������״̬��" + state + "���û���Ϣ��" + getCurrentUser());

		
		// ������
		GridDataUtil.prepareRowObjects(listDis, spot);
	}

	/**
	 * ȡ�õ�������
	 * 
	 * @param state
	 *            String
	 */
	private void getAllDistributeElecTask(String state, String spot) {
		List<DistributeElecTask> listDis = disElecTaskService.getAllDistributeElecTask(state);
		boolean flat = disElecTaskService.isUserlimit();
		
		// ������֤Ȩ��
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
		LOG.debug("ȡ�õ������� " + getDataSize(listMap) + " ������������״̬��" + state + "���û���Ϣ��" + getCurrentUser());
		
		List<DistributePaperSignTask> listPaperSignDis = disPaperSignTaskService.getAllDistributePaperSignTask(state);
		
		// ������֤Ȩ��
		List<Object> checkPaperSignList = checkPermission(listPaperSignDis, spot, spot);
		
		List<String> keysPaperSign = GridHelper.getService().getMyLatestGridViewColumnIds(spot, spot);
		
		TypeService typeManagerPaperSign = Helper.getTypeManager();

		for (Object obj : checkPaperSignList) {
			if (obj == null) {
				continue;
			}
			DistributePaperSignTask target = (DistributePaperSignTask)obj;
//			//ֽ��ǩ�������漰ת��
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
		LOG.debug("ȡ��ֽ��ǩ������͵�������ϼ� " + getDataSize(listMap) + " ������������״̬��" + state + "���û���Ϣ��" + getCurrentUser());
		
		
		GridDataUtil.prepareRowObjects(listMap, spot);
	}

	/**
	 * ȡ����ǩ�յ�������
	 * 
	 * @return ����ID
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
	 * ȡ������ɵ�������
	 * 
	 * @return ����ID
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
	 * ȡ�þܾ�ǩ������
	 * 
	 * @return ����ID
	 */
	public String getAllDistributeElecTaskRefuseSigned() {
		try {
			String lc = ConstUtil.LC_REFUSE_SIGNED.getName();
			
			List<DistributeElecTask> listDis = disElecTaskService.getAllReturnDistributeElecTask(lc);
			LOG.debug("ȡ�õ������� " + getDataSize(listDis) + " ������������״̬��" + lc + "���û���Ϣ��" + getCurrentUser());
			
			String spot = ConstUtil.SPOT_LISTDISTRIBUTEELECTASKSREFUSESIGNED;
			
			// ������֤Ȩ��
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
			LOG.debug("ȡ��ֽ��ǩ������ " + getDataSize(listDis) + " ������������״̬��" + lc + "���û���Ϣ��" + getCurrentUser());
			
//			String spot = ConstUtil.SPOT_LISTDISTRIBUTEELECTASKSREFUSESIGNED;
			
			// ������֤Ȩ��
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
	 * �޸ĵ���������������״̬
	 * 
	 * @return JSON����
	 */
	public String updateDistributeElecTask() {
		try {
			String oids = request.getParameter(KeyS.OIDS);
			String operate = request.getParameter("operate");
			String returnReason = request.getParameter("returnReason");

			//�ж��û��Ƿ�������Ȩ��
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
	 * ��ǩ�յ�������������������
	 * 
	 * @return
	 */
	public String updateDistributeElecTaskLife() {
		try {
			// ��ȡ���������OIDS
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
						//��������
						disElecTaskService.sendDistributeToOutSign(elecOids);
					}
				}
				//��������
				disElecTaskService.updateDistributeElecTaskLife(elecOids);
			}

			if(!StringUtil.isStringEmpty(paperSignOids)){
//				//ֽ��ǩ��������Ҫ��������
//				Site selfSite = SiteHelper.getSiteService().findLocalSiteInfo();
//				if (selfSite != null) {
//					DCSiteAttribute dcSiteAttr = DCSiteAttributeHelper.getDCSiteAttrService().findDcSiteAttrByDTSiteId(
//							selfSite.getInnerId());
//					if (dcSiteAttr != null && "true".equals(dcSiteAttr.getIsSiteControl())) {
//						//��������
//						disPaperSignTaskService.sendDistributeToOutSign(paperSignOids);
//					}
//				}
				//��������
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
