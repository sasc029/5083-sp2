package com.bjsasc.ddm.distribute.action.distributeinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bjsasc.avidm.core.site.Site;
import com.bjsasc.avidm.core.site.SiteHelper;
import com.bjsasc.avidm.core.site.SiteService;
import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.common.ConstUtil.DISPLAY_TYPE;
import com.bjsasc.ddm.distribute.action.AbstractAction;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.disinfoistrack.DisInfoIsTrack;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.service.disinfoistrack.DisInfoIsTrackService;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.model.recdesinfo.RecDesInfo;
import com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DefaultLifecycleServiceImpl;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService;
import com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService;
import com.bjsasc.ddm.distribute.service.distributetask.DistributeTaskService;
import com.bjsasc.ddm.distribute.service.recdesinfo.RecDesInfoService;
import com.bjsasc.platform.sitemgr.bean.SiteData;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.collaboration.site.model.DCSiteAttribute;
import com.bjsasc.plm.collaboration.site.service.DCSiteAttributeHelper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.option.OptionHelper;
import com.bjsasc.plm.core.option.OptionValue;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.util.DateTimeUtil;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.bjsasc.plm.type.TypeService;
import com.bjsasc.plm.util.RequestUtil;
import com.bjsasc.ui.json.DataUtil;
import com.cascc.avidm.util.SplitString;

/**
 * 分发信息Action实现类。
 * 
 * @author gengancong 2013/02/20
 */
public class DistributeInfoAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = -6547602656486307894L;

	/** DistributeInfoService */
	private final DistributeInfoService service = DistributeHelper.getDistributeInfoService();
	/** RecDesInfoService*/
	private final RecDesInfoService recDesService = DistributeHelper.getRecDesInfoService();
	/** DistributeOrderObjectLinkService*/
	private final DistributeOrderObjectLinkService disOrdObjLinkService = DistributeHelper.getDistributeOrderObjectLinkService();
	
	private static final Logger LOG = Logger.getLogger(DistributeInfoAction.class);
	
	DistributeObjectService objService = DistributeHelper.getDistributeObjectService();

	/**
	 * 取得分发信息数据。
	 * 
	 * @return JSON对象
	 */
	public String getAllDistributeInfo() {
		try {
			// 取得分发信息数据。
			getDistributeInfos(DISPLAY_TYPE.DISPLAY);

			// 输出结果
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTEINFOS);

		} catch (Exception ex) {
			error(ex);
		}
		return INITPAGE;
	}
	
	/**
	 * 根据电子任务取得分发信息数据。
	 * 
	 * @return JSON对象
	 */
	public String getAllDistributeElecTaskInfo() {
		try {
			// 取得分发信息数据。
			getDistributeElecTaskInfos(DISPLAY_TYPE.DISPLAY);

			// 输出结果
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTEINFOS);

		} catch (Exception ex) {
			error(ex);
		}
		return "infoContextPage";
	}
	
	/**
	 * 根据纸质签收任务取得分发信息数据。
	 * 
	 * @return JSON对象
	 * @author zhangguoqiang 2014-09-11
	 */
	public String getAllDistributePaperSignTaskInfo() {
		try {
			// 取得分发信息数据。
			getDistributePaperSignTaskInfos(DISPLAY_TYPE.DISPLAY);

			// 输出结果
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTEINFOS);

		} catch (Exception ex) {
			error(ex);
		}
		return "paperSignTaskInfo";
	}

	/**
	 * 取得分发信息数据。
	 * 
	 * @return JSON对象
	 */
	public String getAllDistributePaperrInfo() {
		// 纸质任务OID
		String oid = request.getParameter("oid");
		try {
			// 取得分发信息列表
			List<DistributeInfo> listDis = service.getpapertaskinfo(null,oid);

			TypeService typeManager = Helper.getTypeManager();
			for (DistributeInfo target : listDis) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mainMap = typeManager.format(target);
				listMap.add(mainMap);
			}
			LOG.debug("纸质任务取得分发信息 " + getDataSize(listDis) + " 条");
			GridDataUtil.prepareRowObjects(listMap, ConstUtil.SPOT_LISTDISTRIBUTEINFOS);

			request.setAttribute("taskOid", oid);
			if (!listDis.isEmpty()) {
				request.setAttribute("disUrgent", listDis.get(0).getDisUrgent());
				request.setAttribute("deadLineDate", DateTimeUtil.getDateDisplay(listDis.get(0).getDisDeadLine()));
				request.setAttribute("disStyle", listDis.get(0).getDisStyle());
			}
		} catch (Exception ex) {
			error(ex);
		}

		return "Paperinfo";
	}
	
	/**
	 * 取得回收销毁任务的回收销毁信息数据。
	 * 
	 * @return JSON对象
	 * @author kangyanfei
	 * @date 2014/08/08
	 */
	public String getRecDesInfosByPaperOids() {
		// 纸质任务OID
		String taskOid = getAttributeFromSession(ConstUtil.SPOT_RECDESPAPERTASK_OID);
		String linkOids = (String) session.getAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
		try {
			// 取得分发信息列表
			List<RecDesInfo> listDis = recDesService.getpapertaskinfo(linkOids,taskOid);

			TypeService typeManager = Helper.getTypeManager();
			for (RecDesInfo target : listDis) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mainMap = typeManager.format(target);
				listMap.add(mainMap);
			}
			LOG.debug("纸质任务取得分发信息 " + getDataSize(listDis) + " 条");
			GridDataUtil.prepareRowObjects(listMap, ConstUtil.SPOT_LISTRECDESINFO);

			//request.setAttribute("taskOid", oid);
			request.setAttribute("taskOid", taskOid);
			if (!listDis.isEmpty()) {
				request.setAttribute("disUrgent", listDis.get(0).getDisUrgent());
				request.setAttribute("deadLineDate", DateTimeUtil.getDateDisplay(listDis.get(0).getDisDeadLine()));
//				request.setAttribute("disStyle", listDis.get(0).getDisStyle());
			}
		} catch (Exception ex) {
			error(ex);
		}

		return "recDesPaperInfo";
	}

	/**
	 * 取得回收销毁任务的回收销毁信息数据。
	 * 
	 * @return JSON对象
	 * @author Sun Zongqing
	 * @date 2014/7/2
	 */
	public String getAllRecDesPaperInfo() {
		// 纸质任务OID
		String oid = request.getParameter("oid");
		String linkOids = (String) session.getAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
		try {
			// 取得分发信息列表
			List<RecDesInfo> listDis = recDesService.getpapertaskinfo(null,oid);

			TypeService typeManager = Helper.getTypeManager();
			for (RecDesInfo target : listDis) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mainMap = typeManager.format(target);
				listMap.add(mainMap);
			}
			LOG.debug("纸质任务取得分发信息 " + getDataSize(listDis) + " 条");
			GridDataUtil.prepareRowObjects(listMap, ConstUtil.SPOT_LISTRECDESINFO);

			request.setAttribute("taskOid", oid);
			if (!listDis.isEmpty()) {
				request.setAttribute("disUrgent", listDis.get(0).getDisUrgent());
				request.setAttribute("deadLineDate", DateTimeUtil.getDateDisplay(listDis.get(0).getDisDeadLine()));
//				request.setAttribute("disStyle", listDis.get(0).getDisStyle());
			}
		} catch (Exception ex) {
			error(ex);
		}

		return "recDesPaperInfo";
	}

	/**
	 * 取得分发信息数据。
	 * 
	 * @return JSON对象
	 */
	public String getDistributeInfosByOids() {
		try {
			String linkOids = request.getParameter("distributeOrderObjectLinkOids");

			session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS, linkOids);

			// 取得分发信息数据。
			getDistributeInfos(DISPLAY_TYPE.DISPLAY);
			// 输出结果
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTEINFOS);

			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}
	
	
	/**
	 * 取得分发数据对象。
	 * 
	 * @return JSON对象
	 */
	public String getDistributeObjsByInfoOids() {
		try {
			String disinfoname = request.getParameter("disinfoname");
			String oid = request.getParameter("oid");
			List<String> disinfonamesList = SplitString.string2List(disinfoname, ",");
			List<DistributeObject> listDis = objService.getDistributeObjByInfoOid(disinfonamesList,oid);
				String oids="";
				for(DistributeObject obj:listDis){
					//oid=Helper.getInnerId(obj.getOid())+",";
					oid=obj.getOid()+",";
					oids+=oid;
				}
			if(oids!=null&&!"".equals(oids)){
			oids = oids.substring(0,oids.length()-1);
				result.put("oid", oids);
				success();
			}
			} catch (Exception ex) {
				error(ex);
			}
			mapToSimpleJson();
			return OUTPUTDATA;
	}
	

	public String getHistoryDistributeInfosByOids() {
		
		try {
			
			String linkOids = request.getParameter("distributeOrderObjectLinkOids");
			
			session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS, linkOids);
			// 取得分发信息数据。

			/**
			 * kangyanfei
			 * modify start
			 */
			//getDistributeInfos(DISPLAY_TYPE.DISPLAY);
			getHistoryDistributeInfos(DISPLAY_TYPE.DISPLAY);
			/**
			 * modify end
			 */

			// 输出结果
			//GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTEINFOS);
			
			success();

		} catch (Exception ex) {
			error(ex);
		}
		
		listToJson();
		
		return OUTPUTDATA;
	}
	
	public String getHistoryDistributeInfosByDisObjOidAndDisOrderOids() {
		
		try {
			
			String disObjOid = request.getParameter("distributeObjectOid");
			String disOrderOids = request.getParameter("order_oids");
			//TODO
			// 根据分发数据oid取得发放单与分发数据link列表。
			List<DistributeOrderObjectLink> listdisOrderObjectLink =  
					disOrdObjLinkService.getDistributeOrderObjectLinkListByDisObjOidAndDisOrderOids(disObjOid,disOrderOids);
			
			TypeService typeManager = Helper.getTypeManager();
			// 取得分发信息数据。
			List<DistributeInfo> listDisInfo = service.getDistributeInfosByOrderObjectLinkList(listdisOrderObjectLink);
			for (DistributeInfo target : listDisInfo) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mainMap = typeManager.format(target);
				listMap.add(mainMap);
			}
			
			// 取得分发信息数据。
			List<RecDesInfo> listRecDesInfo = recDesService.getRecDesInfosByOrderObjectLinkList(listdisOrderObjectLink);
			for (RecDesInfo target : listRecDesInfo) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mainMap = typeManager.format(target);
				listMap.add(mainMap);
			}
			
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTEINFOS);
			success();

		} catch (Exception ex) {
			error(ex);
		}
		
		//listToJson();
		mapToSimpleJson();
		return OUTPUTDATA;
	}
	
	public String getHistoryDistributeInfos() {
		
		try {
			
			String disObjOid = request.getParameter("distributeObjectOid");
			String disOrderOids = request.getParameter("order_oids");
			//TODO
			// 根据分发数据oid取得发放单与分发数据link列表。
			List<DistributeOrderObjectLink> listdisOrderObjectLink =  
					disOrdObjLinkService.getDistributeOrderObjectLinkListByDisObjOidAndDisOrderOids(disObjOid,disOrderOids);
			
			TypeService typeManager = Helper.getTypeManager();
			// 取得分发信息数据。
			List<DistributeInfo> listDisInfo = service.getDistributeInfosByOrderObjectLinkList(listdisOrderObjectLink);
			for (DistributeInfo target : listDisInfo) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mainMap = typeManager.format(target);
				listMap.add(mainMap);
			}
			
			// 取得分发信息数据。
			List<RecDesInfo> listRecDesInfo = recDesService.getRecDesInfosByOrderObjectLinkList(listdisOrderObjectLink);
			for (RecDesInfo target : listRecDesInfo) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mainMap = typeManager.format(target);
				listMap.add(mainMap);
			}
			
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTEINFOS);
			success();

		} catch (Exception ex) {
			error(ex);
		}
		
		//listToJson();
		mapToSimpleJson();
		return "historyDistributeInfos";
	}
	
	/**
	 * 电子任务取得分发信息数据。
	 * 
	 * @return JSON对象
	 */
	public String getDistributeInfosByElecTaskOids() {
		try {
			String linkOids = request.getParameter("distributeOrderObjectLinkOids");

			session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS, linkOids);

			// 取得分发信息数据。
			getDistributeElecTaskInfos(DISPLAY_TYPE.DISPLAY);
			// 输出结果
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTEINFOS);

			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}
	
	/**
	 * 纸质签收任务取得分发信息数据。
	 * 
	 * @return JSON对象
	 * @author zhangguoqiang 2014-09-11
	 */
	public String getDistributeInfosByDisOrderObjectLinkOids() {
		try {
			String linkOids = request.getParameter("distributeOrderObjectLinkOids");

			session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS, linkOids);

			// 取得分发信息数据。
			getDistributePaperSignTaskInfos(DISPLAY_TYPE.DISPLAY);
			// 输出结果
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTEINFOS);

			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 取得分发信息数据。
	 * 
	 * @return JSON对象
	 */
	public String getDistributeInfosByPaperOids() {
		try {
			String linkOids = request.getParameter("distributeOrderObjectLinkOids");
			String taskOid = getAttributeFromSession(ConstUtil.SPOT_DISTRIBUTEPAPERTASK_OID);

			session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS, linkOids);

			// 取得分发信息数据。
			List<DistributeInfo> list = service.getpapertaskinfo(linkOids, taskOid);
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
	 * 取得回收销毁纸质任务的回收销毁信息。
	 * 
	 * @return JSON对象
	 */
	public String setDistributeOrderObjectLinkOids() {
		try {
			String linkOids = request.getParameter("distributeOrderObjectLinkOids");
			//String taskOid = getAttributeFromSession(ConstUtil.SPOT_RECDESPAPERTASK_OID);

			session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS, linkOids);
/*
			// 取得分发信息数据。
			List<RecDesInfo> list = recDesService.getpapertaskinfo(linkOids, taskOid);
			TypeService typeManager = Helper.getTypeManager();
			for (RecDesInfo target : list) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mainMap = typeManager.format(target);
				listMap.add(mainMap);
			}
			LOG.debug("取得回收销毁信息信息 " + getDataSize(list) + " 条");
			// 输出结果
			GridDataUtil.prepareRowObjects(listMap, ConstUtil.SPOT_RECDESPAPERTASK_OID);
*/
			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}
	
	/**
	 * 验证是否有分发信息。
	 * 
	 * @return JSON对象
	 */
	public String checkDisInfo() {
		try {
			String distributeOrderOid = request.getParameter("OID");
			Persistable obj = Helper.getPersistService().getObject(distributeOrderOid);
			DistributeOrder dis = (DistributeOrder) obj;
			
			String innerId = Helper.getInnerId(distributeOrderOid);
			/** 单据类型(2回收发放单，3销毁发放单) */
			if(ConstUtil.C_ORDERTYPE_2.equals(dis.getOrderType())||ConstUtil.C_ORDERTYPE_3.equals(dis.getOrderType())){
				List<RecDesInfo> list = recDesService.getRecDesInfosByDistributeOrderInnerId(innerId);
				if (list.size() == 0) {
					result.put("success", "false");
				} else {
					result.put("success", "true");
				}
			} else {/** 单据类型(0发放单、1补发发放单) */
				// 取得分发信息数据。
				List<DistributeInfo> list = service.getDistributeInfosByDistributeOrderInnerId(innerId);
				boolean canDisFlag = DistributeHelper.getDistributeInfoService().canDisBySecurityLevel(distributeOrderOid);
				if (list.size() == 0) {
					result.put("success", "false");
				} else if(!canDisFlag){
					result.put("success", "secrityNotCanDis");
				} else{
					result.put("success", "true");
				}
			}
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 取得分发信息数据。
	 * 
	 * @return JSON对象
	 */
	public String editDistributeInfos() {
		try {
			String linkOid = request.getParameter("linkOid");
			if (linkOid !=null) {
				session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS, linkOid);
			}
			// 取得分发信息数据。
			getDistributeInfos(DISPLAY_TYPE.EDIT);
			LOG.debug("取得分发信息" + getDataSize() + " 条,分发类型为" + DISPLAY_TYPE.EDIT);

		} catch (Exception ex) {
			error(ex);
		}
		// 输出结果
		listToJson();
		return OUTPUTDATA;
	}

	/**
	 * 取得分发信息数据。
	 */
	private void getDistributeInfos(DISPLAY_TYPE type) {

		String linkOids = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
		List<String> linkOidList = SplitString.string2List(linkOids, ",");

		// 获取发放单OIDS
		String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
		if(distributeOrderOid == null){
			distributeOrderOid = RequestUtil.getParamOid(request);
		}
		List<DistributeInfo> listDis = service.getDistributeInfosByOid(distributeOrderOid, linkOidList, type);

		TypeService typeManager = Helper.getTypeManager();
		for (DistributeInfo disInfo : listDis) {
			Map<String, Object> mapValue = new HashMap<String, Object>();
			DisInfoIsTrackService service = DistributeHelper.getDisInfoIsTrackService();
			List<DisInfoIsTrack> disInfoIsTrackList = service.getDisInfoIsTrackByDisInfoOid(disInfo.getOid());
			if (disInfoIsTrackList.size() != 0) {
				Map<String, Object> istMap = typeManager.getAttrValues(disInfoIsTrackList.get(0));
				mapValue.putAll(istMap);
			}
			Map<String, Object> attrValues = typeManager.getAttrValues(disInfo);
			if(disInfo.getNote() == null){
				attrValues.put("NOTE", "");
			}
			mapValue.putAll(attrValues);
			listMap.add(mapValue);
		}

		if (!listDis.isEmpty()) {
			request.setAttribute("disUrgent", listDis.get(0).getDisUrgent());
			request.setAttribute("deadLineDate", DateTimeUtil.getDateDisplay(listDis.get(0).getDisDeadLine()));
			request.setAttribute("disStyle", listDis.get(0).getDisStyle());
		}

		LOG.debug("取得分发信息" + getDataSize() + " 条,分发类型为" + type);

	}

	/*private void getDistributeObj(DISPLAY_TYPE type) {

		String linkOids = getAttributeFromSession(ConstUtil.DISTRIBUTEINFO_OID);
		List<String> linkOidList = SplitString.string2List(linkOids, ",");

		// 获取发放单OIDS
		//String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
		if(distributeOrderOid == null){
			distributeOrderOid = RequestUtil.getParamOid(request);
		}
		//List<DistributeInfo> listDis = service.getDistributeInfosByOid(distributeOrderOid, linkOidList, type);
		List<DistributeObject> listDis = objService.getDistributeObjByInfoOid(linkOidList, type);

		TypeService typeManager = Helper.getTypeManager();
		for (DistributeObject disInfo : listDis) {
			Map<String, Object> mapValue = new HashMap<String, Object>();
			DisInfoIsTrackService service = DistributeHelper.getDisInfoIsTrackService();
			List<DisInfoIsTrack> disInfoIsTrackList = service.getDisInfoIsTrackByDisInfoOid(disInfo.getOid());
			if (disInfoIsTrackList.size() != 0) {
				Map<String, Object> istMap = typeManager.getAttrValues(disInfoIsTrackList.get(0));
				mapValue.putAll(istMap);
			}
			Map<String, Object> attrValues = typeManager.getAttrValues(disInfo);
			if(disInfo.getNote() == null){
				attrValues.put("NOTE", "");
			}
			mapValue.putAll(attrValues);
			listMap.add(mapValue);
		}

		if (!listDis.isEmpty()) {
			request.setAttribute("disUrgent", listDis.get(0).getDisUrgent());
			request.setAttribute("deadLineDate", DateTimeUtil.getDateDisplay(listDis.get(0).getDisDeadLine()));
			request.setAttribute("disStyle", listDis.get(0).getDisStyle());
		}

		LOG.debug("取得分发信息" + getDataSize() + " 条,分发类型为" + type);

	}*/

	/**
	 * kangyanfei
	 * 2014-08-19
	 * 取得历史分发信息数据。
	 */
	private void getHistoryDistributeInfos(DISPLAY_TYPE type) {

		//获得
		String linkOids = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
		List<String> linkOidList = new ArrayList<String>();
		//1.发放单 2.回收单 3.销毁单
		String disInfoType = request.getParameter("orderType");

		// 获取发放单OIDS
		String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
		if(distributeOrderOid == null){
			distributeOrderOid = RequestUtil.getParamOid(request);
		}
		if (StringUtil.isStringEmpty(linkOids)) {
			List<DistributeOrderObjectLink> distributeOrderObjectLink = disOrdObjLinkService.getDistributeOrderObjectLinkListByDistributeOrderOid(distributeOrderOid);
			for(DistributeOrderObjectLink link : distributeOrderObjectLink){
				String innerId = link.getInnerId();
				String classId = link.getClassId();
				linkOidList.add(Helper.getOid(classId, innerId));
			}
		} else {
			linkOidList = SplitString.string2List(linkOids, ",");
		}
		List<DistributeInfo> listDis =new ArrayList<DistributeInfo>();
		List<RecDesInfo> listRes =new ArrayList<RecDesInfo>();
		
		
		TypeService typeManager = Helper.getTypeManager();
		//当所传参数为回收销毁单时
		if(disInfoType.equals(ConstUtil.C_ORDERTYPE_2)||disInfoType.equals(ConstUtil.C_ORDERTYPE_3)) { //回收单,销毁
			listRes=service.getRecDesInfosByOid(distributeOrderOid, linkOidList, type);
			for (RecDesInfo target : listRes) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mainMap = typeManager.format(target);
				listMap.add(mainMap);
			}
		} else{//当所传参数为""或 发放单时
			listDis = service.getDistributeInfosByOid(distributeOrderOid, linkOidList, type);
			
			for (DistributeInfo disInfo : listDis) {
				Map<String, Object> attrValues = typeManager.getAttrValues(disInfo);
				if(disInfo.getNote() == null){
					attrValues.put("NOTE", "");
				}
				listMap.add(attrValues);
			}
		}

		if (!listDis.isEmpty()) {
			request.setAttribute("disUrgent", listDis.get(0).getDisUrgent());
			request.setAttribute("deadLineDate", DateTimeUtil.getDateDisplay(listDis.get(0).getDisDeadLine()));
			request.setAttribute("disStyle", listDis.get(0).getDisStyle());
		}

		LOG.debug("取得分发信息" + getDataSize() + " 条,分发类型为" + type);

	}
	
	/**
	 * 根据电子任务取得分发信息数据。
	 */
	private void getDistributeElecTaskInfos(DISPLAY_TYPE type) {

		String linkOids = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
		List<String> linkOidList = SplitString.string2List(linkOids, ",");

		// 获取发放单OIDS
		String distributeElecTaskOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ELECTASK_OID);

		List<DistributeInfo> listDis = service.getDistributeInfosByTaskOid(distributeElecTaskOid, linkOidList, type);

		TypeService typeManager = Helper.getTypeManager();
		for (DistributeInfo disInfo : listDis) {
			listMap.add(typeManager.getAttrValues(disInfo));
		}

		if (!listDis.isEmpty()) {
			request.setAttribute("disUrgent", listDis.get(0).getDisUrgent());
			request.setAttribute("deadLineDate", DateTimeUtil.getDateDisplay(listDis.get(0).getDisDeadLine()));
			request.setAttribute("disStyle", listDis.get(0).getDisStyle());
		}

		LOG.debug("取得分发信息" + getDataSize() + " 条,分发类型为" + type);

	}

	/**
	 * 根据纸质签收任务取得分发信息数据。
	 * @author zhangguoqiang 2014-09-11
	 */
	private void getDistributePaperSignTaskInfos(DISPLAY_TYPE type) {

		String linkOids = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
		List<String> linkOidList = SplitString.string2List(linkOids, ",");

		// 获取发放单OIDS
		String distributePaperSignTaskOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_PAPERSIGNTASK_OID);

		List<DistributeInfo> listDis = service.getDistributeInfosByPaperSignTaskOid(distributePaperSignTaskOid, linkOidList, type);

		TypeService typeManager = Helper.getTypeManager();
		for (DistributeInfo disInfo : listDis) {
			listMap.add(typeManager.getAttrValues(disInfo));
		}

		if (!listDis.isEmpty()) {
			request.setAttribute("disUrgent", listDis.get(0).getDisUrgent());
			request.setAttribute("deadLineDate", DateTimeUtil.getDateDisplay(listDis.get(0).getDisDeadLine()));
			request.setAttribute("disStyle", listDis.get(0).getDisStyle());
		}

		LOG.debug("取得分发信息" + getDataSize() + " 条,分发类型为" + type);

	}

	/**
	 * 关联用户/组织。
	 * 
	 * @return JSON对象
	 */
	public String addPrincipals() {
		try {

			String type = request.getParameter("type");
			String iids = request.getParameter("iids");
			String disMediaTypes = request.getParameter("disMediaTypes");
			String disInfoNums = request.getParameter("disInfoNums");
			String notes = request.getParameter("notes");
			String distributeOrderObjectLinkOids = request.getParameter("distributeOrderObjectLinkOids");
			String deleterows = request.getParameter("deleterows");

			service.createDistributeInfo(type, iids, disMediaTypes, disInfoNums, notes, distributeOrderObjectLinkOids, deleterows);

			getAllDistributeInfo();

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 更新分发信息。
	 * 
	 * @return JSON对象
	 */
	public String updateDistributeInfo() {
		try {
			String oids = request.getParameter("distributeInfoOids");
			String disInfoNums = request.getParameter("disInfoNums");
			String notes = request.getParameter("notes");
			String dismediatypes = request.getParameter("dismediatypes");
			service.updateDistributeInfos(oids, disInfoNums, notes, dismediatypes);
			getAllDistributeInfo();

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 更新分发信息。
	 * 
	 * @return JSON对象
	 */
	public String updateDisInfoIsTrack() {
		try {
			String oids = request.getParameter("distributeInfoOids");
			String isTracks = request.getParameter("isTracks");
			service.updateDisInfoIsTrack(oids, isTracks);
			getAllDistributeInfo();
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 创建任务对象。
	 * 
	 * @return JSON对象
	 */
	public String createDistributeTask() {
		try {
			// 获取发放单OID
			String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
			DistributeTaskService taskService = DistributeHelper.getDistributeTaskService();
			taskService.createDistributeTask(distributeOrderOid, "true", null);
			success();
			Persistable obj = Helper.getPersistService().getObject(distributeOrderOid);
			DistributeOrder order = (DistributeOrder) obj;
			Context context = order.getContextInfo().getContext();
			//判断是否是补发发放单
			if(ConstUtil.C_ORDERTYPE_1.equals(order.getOrderType())){
				// 补发发放单是否进行纸质处理(根据首选项的配置决定是否开启)
				OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disFlowConfig_whetherPaperProcessing");
				if ("false".equals(value.getValue())) {
					//将分发数据、分发信息、分发任务、发放单设置成已完成
						taskService.setAllSended(distributeOrderOid);
					}
			}
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 删除分发信息。
	 * 
	 * @return JSON对象
	 */
	public String deleteDistributeInfo() {
		try {
			// 获取分发信息OIDS
			String oids = request.getParameter("distributeInfoOids");

			service.deleteDistributeInfos(oids);

			getAllDistributeInfo();

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}
	
	public String createOutSignDisInfo(){
		try {
			
			String innerIds = request.getParameter("innerIds");
			String siteNames = request.getParameter("siteNames");
			String userIds = request.getParameter("userIds");
			String linkOids = request.getParameter("linkOids");

			service.createOutSignDisInfo(innerIds,siteNames,userIds,linkOids,false);

			getAllDistributeInfo();

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}
	
	/**
	 * 初始化接收站点及接收人
	 */
	public void initReceiveSiteAndUser() throws Exception{
		SiteService siteService = SiteHelper.getSiteService();
		List<Site> list = siteService.findAllOutSite();
		List<Map<String, String>> receiveSiteMapList = new ArrayList<Map<String, String>>();
		for(int i=0; i<list.size(); i++){
			Site site = list.get(i);
			//过滤掉中心站点
			DCSiteAttribute dcSite = DCSiteAttributeHelper.getDCSiteAttrService().findDcSiteAttrByDTSiteId(site.getInnerId());
			if(dcSite.getIsSiteControl().equals("true")){
				continue;
			}
			SiteData siteData = site.getSiteData();
			Map<String, String> map = new HashMap<String, String>();
			map.put("innerId", site.getInnerId());
			map.put("siteName", siteData.getSiteName());
			map.put("domainID", String.valueOf((site.getSiteData().getDomainIId())));
			map.put("userId", "");
			map.put("userName", "");
			receiveSiteMapList.add(map);
		}
		response.getWriter().print(DataUtil.listToJson(receiveSiteMapList.size(), receiveSiteMapList));
	}
}
