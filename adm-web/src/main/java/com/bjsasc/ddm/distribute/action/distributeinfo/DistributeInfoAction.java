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
 * �ַ���ϢActionʵ���ࡣ
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
	 * ȡ�÷ַ���Ϣ���ݡ�
	 * 
	 * @return JSON����
	 */
	public String getAllDistributeInfo() {
		try {
			// ȡ�÷ַ���Ϣ���ݡ�
			getDistributeInfos(DISPLAY_TYPE.DISPLAY);

			// ������
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTEINFOS);

		} catch (Exception ex) {
			error(ex);
		}
		return INITPAGE;
	}
	
	/**
	 * ���ݵ�������ȡ�÷ַ���Ϣ���ݡ�
	 * 
	 * @return JSON����
	 */
	public String getAllDistributeElecTaskInfo() {
		try {
			// ȡ�÷ַ���Ϣ���ݡ�
			getDistributeElecTaskInfos(DISPLAY_TYPE.DISPLAY);

			// ������
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTEINFOS);

		} catch (Exception ex) {
			error(ex);
		}
		return "infoContextPage";
	}
	
	/**
	 * ����ֽ��ǩ������ȡ�÷ַ���Ϣ���ݡ�
	 * 
	 * @return JSON����
	 * @author zhangguoqiang 2014-09-11
	 */
	public String getAllDistributePaperSignTaskInfo() {
		try {
			// ȡ�÷ַ���Ϣ���ݡ�
			getDistributePaperSignTaskInfos(DISPLAY_TYPE.DISPLAY);

			// ������
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTEINFOS);

		} catch (Exception ex) {
			error(ex);
		}
		return "paperSignTaskInfo";
	}

	/**
	 * ȡ�÷ַ���Ϣ���ݡ�
	 * 
	 * @return JSON����
	 */
	public String getAllDistributePaperrInfo() {
		// ֽ������OID
		String oid = request.getParameter("oid");
		try {
			// ȡ�÷ַ���Ϣ�б�
			List<DistributeInfo> listDis = service.getpapertaskinfo(null,oid);

			TypeService typeManager = Helper.getTypeManager();
			for (DistributeInfo target : listDis) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mainMap = typeManager.format(target);
				listMap.add(mainMap);
			}
			LOG.debug("ֽ������ȡ�÷ַ���Ϣ " + getDataSize(listDis) + " ��");
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
	 * ȡ�û�����������Ļ���������Ϣ���ݡ�
	 * 
	 * @return JSON����
	 * @author kangyanfei
	 * @date 2014/08/08
	 */
	public String getRecDesInfosByPaperOids() {
		// ֽ������OID
		String taskOid = getAttributeFromSession(ConstUtil.SPOT_RECDESPAPERTASK_OID);
		String linkOids = (String) session.getAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
		try {
			// ȡ�÷ַ���Ϣ�б�
			List<RecDesInfo> listDis = recDesService.getpapertaskinfo(linkOids,taskOid);

			TypeService typeManager = Helper.getTypeManager();
			for (RecDesInfo target : listDis) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mainMap = typeManager.format(target);
				listMap.add(mainMap);
			}
			LOG.debug("ֽ������ȡ�÷ַ���Ϣ " + getDataSize(listDis) + " ��");
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
	 * ȡ�û�����������Ļ���������Ϣ���ݡ�
	 * 
	 * @return JSON����
	 * @author Sun Zongqing
	 * @date 2014/7/2
	 */
	public String getAllRecDesPaperInfo() {
		// ֽ������OID
		String oid = request.getParameter("oid");
		String linkOids = (String) session.getAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
		try {
			// ȡ�÷ַ���Ϣ�б�
			List<RecDesInfo> listDis = recDesService.getpapertaskinfo(null,oid);

			TypeService typeManager = Helper.getTypeManager();
			for (RecDesInfo target : listDis) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mainMap = typeManager.format(target);
				listMap.add(mainMap);
			}
			LOG.debug("ֽ������ȡ�÷ַ���Ϣ " + getDataSize(listDis) + " ��");
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
	 * ȡ�÷ַ���Ϣ���ݡ�
	 * 
	 * @return JSON����
	 */
	public String getDistributeInfosByOids() {
		try {
			String linkOids = request.getParameter("distributeOrderObjectLinkOids");

			session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS, linkOids);

			// ȡ�÷ַ���Ϣ���ݡ�
			getDistributeInfos(DISPLAY_TYPE.DISPLAY);
			// ������
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTEINFOS);

			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}
	
	
	/**
	 * ȡ�÷ַ����ݶ���
	 * 
	 * @return JSON����
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
			// ȡ�÷ַ���Ϣ���ݡ�

			/**
			 * kangyanfei
			 * modify start
			 */
			//getDistributeInfos(DISPLAY_TYPE.DISPLAY);
			getHistoryDistributeInfos(DISPLAY_TYPE.DISPLAY);
			/**
			 * modify end
			 */

			// ������
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
			// ���ݷַ�����oidȡ�÷��ŵ���ַ�����link�б�
			List<DistributeOrderObjectLink> listdisOrderObjectLink =  
					disOrdObjLinkService.getDistributeOrderObjectLinkListByDisObjOidAndDisOrderOids(disObjOid,disOrderOids);
			
			TypeService typeManager = Helper.getTypeManager();
			// ȡ�÷ַ���Ϣ���ݡ�
			List<DistributeInfo> listDisInfo = service.getDistributeInfosByOrderObjectLinkList(listdisOrderObjectLink);
			for (DistributeInfo target : listDisInfo) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mainMap = typeManager.format(target);
				listMap.add(mainMap);
			}
			
			// ȡ�÷ַ���Ϣ���ݡ�
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
			// ���ݷַ�����oidȡ�÷��ŵ���ַ�����link�б�
			List<DistributeOrderObjectLink> listdisOrderObjectLink =  
					disOrdObjLinkService.getDistributeOrderObjectLinkListByDisObjOidAndDisOrderOids(disObjOid,disOrderOids);
			
			TypeService typeManager = Helper.getTypeManager();
			// ȡ�÷ַ���Ϣ���ݡ�
			List<DistributeInfo> listDisInfo = service.getDistributeInfosByOrderObjectLinkList(listdisOrderObjectLink);
			for (DistributeInfo target : listDisInfo) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mainMap = typeManager.format(target);
				listMap.add(mainMap);
			}
			
			// ȡ�÷ַ���Ϣ���ݡ�
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
	 * ��������ȡ�÷ַ���Ϣ���ݡ�
	 * 
	 * @return JSON����
	 */
	public String getDistributeInfosByElecTaskOids() {
		try {
			String linkOids = request.getParameter("distributeOrderObjectLinkOids");

			session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS, linkOids);

			// ȡ�÷ַ���Ϣ���ݡ�
			getDistributeElecTaskInfos(DISPLAY_TYPE.DISPLAY);
			// ������
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTEINFOS);

			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}
	
	/**
	 * ֽ��ǩ������ȡ�÷ַ���Ϣ���ݡ�
	 * 
	 * @return JSON����
	 * @author zhangguoqiang 2014-09-11
	 */
	public String getDistributeInfosByDisOrderObjectLinkOids() {
		try {
			String linkOids = request.getParameter("distributeOrderObjectLinkOids");

			session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS, linkOids);

			// ȡ�÷ַ���Ϣ���ݡ�
			getDistributePaperSignTaskInfos(DISPLAY_TYPE.DISPLAY);
			// ������
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTEINFOS);

			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * ȡ�÷ַ���Ϣ���ݡ�
	 * 
	 * @return JSON����
	 */
	public String getDistributeInfosByPaperOids() {
		try {
			String linkOids = request.getParameter("distributeOrderObjectLinkOids");
			String taskOid = getAttributeFromSession(ConstUtil.SPOT_DISTRIBUTEPAPERTASK_OID);

			session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS, linkOids);

			// ȡ�÷ַ���Ϣ���ݡ�
			List<DistributeInfo> list = service.getpapertaskinfo(linkOids, taskOid);
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
	 * ȡ�û�������ֽ������Ļ���������Ϣ��
	 * 
	 * @return JSON����
	 */
	public String setDistributeOrderObjectLinkOids() {
		try {
			String linkOids = request.getParameter("distributeOrderObjectLinkOids");
			//String taskOid = getAttributeFromSession(ConstUtil.SPOT_RECDESPAPERTASK_OID);

			session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS, linkOids);
/*
			// ȡ�÷ַ���Ϣ���ݡ�
			List<RecDesInfo> list = recDesService.getpapertaskinfo(linkOids, taskOid);
			TypeService typeManager = Helper.getTypeManager();
			for (RecDesInfo target : list) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mainMap = typeManager.format(target);
				listMap.add(mainMap);
			}
			LOG.debug("ȡ�û���������Ϣ��Ϣ " + getDataSize(list) + " ��");
			// ������
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
	 * ��֤�Ƿ��зַ���Ϣ��
	 * 
	 * @return JSON����
	 */
	public String checkDisInfo() {
		try {
			String distributeOrderOid = request.getParameter("OID");
			Persistable obj = Helper.getPersistService().getObject(distributeOrderOid);
			DistributeOrder dis = (DistributeOrder) obj;
			
			String innerId = Helper.getInnerId(distributeOrderOid);
			/** ��������(2���շ��ŵ���3���ٷ��ŵ�) */
			if(ConstUtil.C_ORDERTYPE_2.equals(dis.getOrderType())||ConstUtil.C_ORDERTYPE_3.equals(dis.getOrderType())){
				List<RecDesInfo> list = recDesService.getRecDesInfosByDistributeOrderInnerId(innerId);
				if (list.size() == 0) {
					result.put("success", "false");
				} else {
					result.put("success", "true");
				}
			} else {/** ��������(0���ŵ���1�������ŵ�) */
				// ȡ�÷ַ���Ϣ���ݡ�
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
	 * ȡ�÷ַ���Ϣ���ݡ�
	 * 
	 * @return JSON����
	 */
	public String editDistributeInfos() {
		try {
			String linkOid = request.getParameter("linkOid");
			if (linkOid !=null) {
				session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS, linkOid);
			}
			// ȡ�÷ַ���Ϣ���ݡ�
			getDistributeInfos(DISPLAY_TYPE.EDIT);
			LOG.debug("ȡ�÷ַ���Ϣ" + getDataSize() + " ��,�ַ�����Ϊ" + DISPLAY_TYPE.EDIT);

		} catch (Exception ex) {
			error(ex);
		}
		// ������
		listToJson();
		return OUTPUTDATA;
	}

	/**
	 * ȡ�÷ַ���Ϣ���ݡ�
	 */
	private void getDistributeInfos(DISPLAY_TYPE type) {

		String linkOids = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
		List<String> linkOidList = SplitString.string2List(linkOids, ",");

		// ��ȡ���ŵ�OIDS
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

		LOG.debug("ȡ�÷ַ���Ϣ" + getDataSize() + " ��,�ַ�����Ϊ" + type);

	}

	/*private void getDistributeObj(DISPLAY_TYPE type) {

		String linkOids = getAttributeFromSession(ConstUtil.DISTRIBUTEINFO_OID);
		List<String> linkOidList = SplitString.string2List(linkOids, ",");

		// ��ȡ���ŵ�OIDS
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

		LOG.debug("ȡ�÷ַ���Ϣ" + getDataSize() + " ��,�ַ�����Ϊ" + type);

	}*/

	/**
	 * kangyanfei
	 * 2014-08-19
	 * ȡ����ʷ�ַ���Ϣ���ݡ�
	 */
	private void getHistoryDistributeInfos(DISPLAY_TYPE type) {

		//���
		String linkOids = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
		List<String> linkOidList = new ArrayList<String>();
		//1.���ŵ� 2.���յ� 3.���ٵ�
		String disInfoType = request.getParameter("orderType");

		// ��ȡ���ŵ�OIDS
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
		//����������Ϊ�������ٵ�ʱ
		if(disInfoType.equals(ConstUtil.C_ORDERTYPE_2)||disInfoType.equals(ConstUtil.C_ORDERTYPE_3)) { //���յ�,����
			listRes=service.getRecDesInfosByOid(distributeOrderOid, linkOidList, type);
			for (RecDesInfo target : listRes) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mainMap = typeManager.format(target);
				listMap.add(mainMap);
			}
		} else{//����������Ϊ""�� ���ŵ�ʱ
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

		LOG.debug("ȡ�÷ַ���Ϣ" + getDataSize() + " ��,�ַ�����Ϊ" + type);

	}
	
	/**
	 * ���ݵ�������ȡ�÷ַ���Ϣ���ݡ�
	 */
	private void getDistributeElecTaskInfos(DISPLAY_TYPE type) {

		String linkOids = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
		List<String> linkOidList = SplitString.string2List(linkOids, ",");

		// ��ȡ���ŵ�OIDS
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

		LOG.debug("ȡ�÷ַ���Ϣ" + getDataSize() + " ��,�ַ�����Ϊ" + type);

	}

	/**
	 * ����ֽ��ǩ������ȡ�÷ַ���Ϣ���ݡ�
	 * @author zhangguoqiang 2014-09-11
	 */
	private void getDistributePaperSignTaskInfos(DISPLAY_TYPE type) {

		String linkOids = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
		List<String> linkOidList = SplitString.string2List(linkOids, ",");

		// ��ȡ���ŵ�OIDS
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

		LOG.debug("ȡ�÷ַ���Ϣ" + getDataSize() + " ��,�ַ�����Ϊ" + type);

	}

	/**
	 * �����û�/��֯��
	 * 
	 * @return JSON����
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
	 * ���·ַ���Ϣ��
	 * 
	 * @return JSON����
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
	 * ���·ַ���Ϣ��
	 * 
	 * @return JSON����
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
	 * �����������
	 * 
	 * @return JSON����
	 */
	public String createDistributeTask() {
		try {
			// ��ȡ���ŵ�OID
			String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
			DistributeTaskService taskService = DistributeHelper.getDistributeTaskService();
			taskService.createDistributeTask(distributeOrderOid, "true", null);
			success();
			Persistable obj = Helper.getPersistService().getObject(distributeOrderOid);
			DistributeOrder order = (DistributeOrder) obj;
			Context context = order.getContextInfo().getContext();
			//�ж��Ƿ��ǲ������ŵ�
			if(ConstUtil.C_ORDERTYPE_1.equals(order.getOrderType())){
				// �������ŵ��Ƿ����ֽ�ʴ���(������ѡ������þ����Ƿ���)
				OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disFlowConfig_whetherPaperProcessing");
				if ("false".equals(value.getValue())) {
					//���ַ����ݡ��ַ���Ϣ���ַ����񡢷��ŵ����ó������
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
	 * ɾ���ַ���Ϣ��
	 * 
	 * @return JSON����
	 */
	public String deleteDistributeInfo() {
		try {
			// ��ȡ�ַ���ϢOIDS
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
	 * ��ʼ������վ�㼰������
	 */
	public void initReceiveSiteAndUser() throws Exception{
		SiteService siteService = SiteHelper.getSiteService();
		List<Site> list = siteService.findAllOutSite();
		List<Map<String, String>> receiveSiteMapList = new ArrayList<Map<String, String>>();
		for(int i=0; i<list.size(); i++){
			Site site = list.get(i);
			//���˵�����վ��
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
