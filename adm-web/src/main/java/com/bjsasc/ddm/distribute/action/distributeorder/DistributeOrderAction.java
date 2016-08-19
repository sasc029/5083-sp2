package com.bjsasc.ddm.distribute.action.distributeorder;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.action.AbstractAction;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DefaultLifecycleServiceImpl;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService;
import com.bjsasc.ddm.distribute.service.distributetask.DistributeTaskService;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.option.OptionHelper;
import com.bjsasc.plm.core.option.OptionValue;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.grid.GridHelper;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.bjsasc.plm.type.TypeService;
import com.cascc.avidm.util.SplitString;

/**
 * �ַ���Actionʵ���ࡣ
 * 
 * @author gengancong 2013-2-22
 */
public class DistributeOrderAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = 5314037457036540586L;

	/** DistributeOrderService */
	private final DistributeOrderService service = DistributeHelper.getDistributeOrderService();
	
	private static final Logger LOG = Logger.getLogger(DistributeOrderAction.class);

	/**
	 * ���Ź���--���Ȳ˵�
	 * ��������ҳ������һ����ʾ��
	 * ȡ�÷��ŵ���������Ϊ��������,������ֹ,�������˻ء������ݡ�
	 * /ddm/distributeorder/listDistributeOrders.jsp
	 * 
	 * @return strutsҳ�����
	 */
	public String getAllDistributeOrder() {
		try {
			
			String[] schStateArray = new String[]{ConstUtil.LC_SCHEDULING.getName(),ConstUtil.LC_APPROVE_TERMINATE.getName(),ConstUtil.LC_APPROVE_REJECT.getName()};
			List<DistributeOrder> listDis = service.listDiffStatesDistributeOrder(schStateArray);
			LOG.debug("ȡ�÷��ŵ� " + getDataSize(listDis) + " ������������״̬��" + schStateArray);

			String spot = ConstUtil.SPOT_LISTDISTRIBUTEORDERS;
			
			// ������֤Ȩ��
			List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
			
			// ������
			GridDataUtil.prepareRowObjects(listDatas, spot);
		} catch (Exception ex) {
			error(ex);
		}
		return INITPAGE;
	}
	
	/**
	 * ���Ź���--���ŵ�
	 * ��������ҳ������һ����ʾ��
	 * ȡ�÷��ŵ���������Ϊ���ַ��С������ݡ�
	 * /ddm/distributeorder/listdistributesending.jsp
	 * 
	 * @return strutsҳ�����
	 */
	public String listSendingDistributeOrder() {
		try {
			String[] notInStateArray = new String[]{ConstUtil.LC_DISTRIBUTED.getName(),ConstUtil.LC_COMPLETED.getName()};
			List<DistributeOrder> listDis = service
					.listNotInStatesDistributeOrder(notInStateArray);
			LOG.debug("ȡ�÷��ŵ� " + getDataSize(listDis) + " ������������״̬��"
					+ notInStateArray + "�����״̬");

			String spot = ConstUtil.SPOT_LISTDISTRIBUTEORDERS;
			
			List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
			GridDataUtil.prepareRowObjects(listDatas, spot);
		} catch (Exception ex) {
			error(ex);
		}
		return "listdistributesending";
	}
	/**
	 * ���Ź���--���ŵ�
	 * ��������ҳ������һ����ʾ��
	 * ȡ�÷��ŵ���������Ϊ���ѷַ��������ݡ�
	 * /ddm/distributeorder/listdistributesended.jsp
	 * 
	 * @return strutsҳ�����
	 */
	public String listSendedDistributeOrder(){
		try {
			String [] stateArray=new String[]{ConstUtil.LC_DISTRIBUTED.getName(),ConstUtil.LC_COMPLETED.getName()};
			List<DistributeOrder> listDis = service.listDiffStatesDistributeOrder(stateArray);
			LOG.debug("ȡ�÷��ŵ� " + getDataSize(listDis) + " ������������״̬��" + stateArray);
			
			String spot = ConstUtil.SPOT_LISTDISTRIBUTEORDERS;
			List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
			GridDataUtil.prepareRowObjects(listDatas, spot);
		} catch (Exception ex) {
			error(ex);
		}
		return "listdistributesended";
	}
	
	
	/**
	 * ���Ź���--���ŵ�
	 * ��������ҳ������һ����ʾ��
	 * ȡ�÷��ŵ���������Ϊ���ѷַ��������ݡ�
	 * ��һ�μ���ҳ����ʾһ����֮ǰ�µ�����
	 * /ddm/distributeorder/listdistributesended.jsp
	 * 
	 * @return strutsҳ�����
	 */
	public String listSendedDistributeOrderOnload(){
		try {
			long currentTime = System.currentTimeMillis();
			long monthtime = 30*24*60*60*1000L;
			long time = currentTime-monthtime;
			String [] stateArray=new String[]{ConstUtil.LC_DISTRIBUTED.getName(),ConstUtil.LC_COMPLETED.getName()};
			List<DistributeOrder> listDis = service.listDiffStatesDistributeOrderOnload(stateArray,time,currentTime);
			LOG.debug("ȡ�÷��ŵ� " + getDataSize(listDis) + " ������������״̬��" + stateArray);
			String spot = ConstUtil.SPOT_LISTDISTRIBUTEORDERS;
			List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
			GridDataUtil.prepareRowObjects(listDatas, spot);
		} catch (Exception ex) {
			error(ex);
		}
		return "listdistributesended";
	}
	
	
	/**
	 * ���Ź���--���ŵ�
	 * ��������ҳ������һ����ʾ��
	 * ȡ�÷��ŵ���������Ϊ���ѷַ��������ݡ�
	 * /ddm/distributeorder/listdistributesended.jsp
	 * ��ҳ����������ؼ��ֵ�������
	 * author��������
	 * @return strutsҳ�����
	 */
	public String listSendedDistributeOrderAdderSearchIuput(){
		String ret="";
		try {
			//�������е������ֶ�
			String keywords=request.getParameter("keywords").trim();
			String [] stateArray=new String[]{ConstUtil.LC_DISTRIBUTED.getName(),ConstUtil.LC_COMPLETED.getName()};
			List<DistributeOrder> listDis = service.listSendedDistributeOrderAdderSearchIuput(stateArray,keywords);
			LOG.debug("ȡ�÷��ŵ� " + getDataSize(listDis) + " ������������״̬��" + stateArray);
			String spot = ConstUtil.SPOT_LISTDISTRIBUTEORDERS;
			List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
			GridDataUtil.prepareRowObjects(listDatas, spot);
			String flag1=getParameter("flag1");
			if(""!=flag1&&flag1!=null&&"2".equals(flag1)){
			result.put("success","true");
			result.put("flag", listDatas.size()+"");
			mapToSimpleJson();
			ret=OUTPUTDATA;
			}else{
				ret="listdistributesended";
			}
		} catch (Exception ex) {
			error(ex);
		}
		//return "listdistributesended";
		return ret;
	}
	
	
	/**
	 * ���Ź���--���ŵ�
	 * �ѷַ�--��������
	 * @return strutsҳ�����
	 */
	
	public String reissueDistributeOrder(){
		
		try {
			String number = request.getParameter("NUMBER");// ���
			String name = request.getParameter("NAME");// ����
			String orderType = request.getParameter("orderType");// ��������
			String note = request.getParameter("NOTE");// ��ע

			String orderOidsStr = request.getParameter("orderOidsStr");
			DistributeOrder disOrder = service.reissueDistributeOrder(
					orderOidsStr, number, name, orderType, note);

			if (disOrder != null) {
				result.put("disOrderOid", disOrder.getOid());
				success();
			} else {
				result.put("success", "message");
				result.put("message",
						ConstUtil.CREATE_REISSUE_DISTRIBUTEORDER_ERROE);
			}

		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
		
	}
	
	
	
	

	/**
	 * ���Ź���--���Ȳ˵�
	 * ���˻�����ҳ������һ����ʾ��
	 * ȡ�÷��ŵ���������Ϊ�����˻ء������ݡ�
	 * /ddm/distributeorder/listDistributeOrdersReturn.jsp
	 * 
	 * @return strutsҳ�����
	 */
	public String getAllDistributeOrderReturn() {
		try {
			String lc = ConstUtil.LC_BACKOFF.getName();
			List<DistributeOrder> listDis = service.getAllDistributeOrderReturnByAuth(lc);
			LOG.debug("ȡ�÷��ŵ� " + getDataSize(listDis) + " ������������״̬��" + lc);
						
			String spot = ConstUtil.SOPT_LISTDISTRIBUTERETURN;
			
			// ������֤Ȩ��
			List<Object> checkList = checkPermission(listDis, spot, spot);
			
			List<String> keys = GridHelper.getService().getMyLatestGridViewColumnIds(spot, spot);
			
			// ��ʽ����ʾ����
			TypeService typeManager = Helper.getTypeManager();
			Map<String, Object> mapAll = null;
			for (Object obj : checkList) {
				if (obj == null) {
					continue;
				}
				mapAll = new HashMap<String, Object>();
				DistributeOrder target = (DistributeOrder)obj;
				Map<String, Object> mainMap = typeManager.format(target, keys, false);
				Map<String, Object> retMap = typeManager.format(target.getRetReason(), keys, false);
				mapAll.putAll(retMap);
				mapAll.putAll(mainMap);
				mapAll.put(KeyS.ACCESS, true);
				listMap.add(mapAll);
			}
			// ������
			GridDataUtil.prepareRowObjectMaps(listMap, spot);
		} catch (Exception ex) {
			error(ex);
		}
		return "listDisOrderReturn";
	}

	/**
	 * ȡ�÷ַ��������б���ϸ��
	 * 
	 * @return JSON����
	 */
	public String getDistributeOrderReturnDetail() {
		try {
			String oid = request.getParameter(KeyS.OID);
			List<DistributeOrder> listDis = service.getDistributeOrderReturnDetail(ConstUtil.LC_BACKOFF.getName(), oid);
			// ��ʽ����ʾ����
			TypeService typeManager = Helper.getTypeManager();
			for (DistributeOrder target : listDis) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mapAll = new HashMap<String, Object>();
				Map<String, Object> mainMap = typeManager.format(target);
				Map<String, Object> returnMap = typeManager.format(target.getRetReason());
				mapAll.putAll(returnMap);
				mapAll.putAll(mainMap);
				listMap.add(mapAll);
			}
			LOG.debug("ȡ�÷��ŵ������� " + getDataSize() + " ������������״̬��" + ConstUtil.LC_BACKOFF.getName());
			// ������
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTERETURNDETAIL);
		} catch (Exception ex) {
			error(ex);
		}
		return "listDisOrderReturnDetail";
	}

	/**
	 * ȡ����ط��ŵ���
	 * 
	 * @return JSON����
	 */
	public String getRelatedDistributeOrder() {
		String oid = request.getParameter(KeyS.OID);
		List<DistributeOrder> listDis = service.getRelatedDistributeOrder(oid);
		TypeService typeManager = Helper.getTypeManager();
		for (DistributeOrder disOrder : listDis) {
			listMap.add(typeManager.getAttrValues(disOrder));
		}
		LOG.debug("ȡ�÷��ŵ�" + getDataSize() + " ��");

		listToJson();
		return OUTPUTDATA;
	}
	
	/**
	 * ȡ��ֽ������ķ��ŵ�
	 * 
	 * @return JSON����
	 */
	public String getRelatetaskdDistributeOrder() {
		String oid = request.getParameter(KeyS.OID);
		List<DistributeOrder> listDis = service.getRelatetaskdDistributeOrder(oid);
		TypeService typeManager = Helper.getTypeManager();
		for (DistributeOrder disOrder : listDis) {
			listMap.add(typeManager.getAttrValues(disOrder));
		}
		LOG.debug("ȡ�÷��ŵ�" + getDataSize() + " ��");
		listToJson();
		return OUTPUTDATA;
	}

	/**
	 * �������ŵ�����
	 * 
	 * @return JSON����
	 */

	//�������ŵ�
	public String createDistributeOrder() {
		try {
			String number = request.getParameter("NUMBER");// ���
			String name = request.getParameter("NAME");// ����
			String orderType = request.getParameter("orderType");// ��������
			String note = request.getParameter("NOTE");// ��ע

			String oid = request.getParameter("oid");

			DistributeOrder disOrder = service
					.createDistributeOrderAndObject(number, name, orderType, note, oid, false, null);

			if (disOrder != null) {
				result.put("disOrderOid", disOrder.getOid());
				success();
			} else {
				result.put("success", "message");
				result.put("message", ConstUtil.CREATE_DISTRIBUTEORDER_ERROE);
			}

		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	//����������������Ŵ������ŵ�
	public String createDistributeOrderMulti() {
		try {
			String number = request.getParameter("NUMBER");// ���
			String name = request.getParameter("NAME");// ����
			String orderType = request.getParameter("orderType");// ��������
			String note = request.getParameter("NOTE");// ��ע

			String oids = request.getParameter("oids");
			//ȡ��һ���ַ����󴴽����ŵ��������ڷ��ŵ������ɹ�����ӵ��ַ���������ȥ���ο�DistributeObjectAction.java��addDistributeObject
			DistributeOrder disOrder = service
					.createDistributeOrderAndObjectMulti(number, name, orderType, note, oids, false, null);

			if (disOrder != null) {
				result.put("disOrderOid", disOrder.getOid());
				success();
			} else {
				result.put("success", "message");
				result.put("message", ConstUtil.CREATE_DISTRIBUTEORDER_ERROE);
			}

		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}
	/**
	 * У�鹤���������Ƿ������ҷ��ŵ��ύ������
	 * 
	 * @return JSON����
	 */
	public String getDistributeToWorkFlow() {
		try {
			// ��ȡ���ŵ�OIDS
			String distributeOrderOids = request.getParameter("oids");
			String flag = "false";
			List<String> oidList = SplitString.string2List(distributeOrderOids, ",");
			for(String orderOid : oidList){
				Persistable obj = Helper.getPersistService().getObject(orderOid);
				DistributeOrder order = (DistributeOrder) obj;
				Context context = order.getContextInfo().getContext();
				OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disFlowConfig_check");
				if ("true".equals(value.getValue())) {
					//����������
					flag = "true";
					break;
				}
			}
			result.put("toworkflow", flag);
			result.put("success", "true");
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
//			boolean flag = TokenHelper.validToken();
			// ��ȡ���ŵ�OIDS
			String distributeOrderOids = request.getParameter("oids");
			String flag = request.getParameter("flag");
			DistributeTaskService taskService = DistributeHelper.getDistributeTaskService();
			taskService.createDistributeTask(distributeOrderOids,flag, null);
			success();
			List<String> distributeOrderOidList = SplitString.string2List(distributeOrderOids, ",");
			for(String distributeOrderOid:distributeOrderOidList){
				Persistable obj = Helper.getPersistService().getObject(distributeOrderOid);
				DistributeOrder order = (DistributeOrder) obj;
				Context context = order.getContextInfo().getContext();
				//�ж��Ƿ��ǲ������ŵ�
				if(ConstUtil.C_ORDERTYPE_1.equals(order.getOrderType())){
					// �������ŵ��Ƿ����ֽ�ʴ���(������ѡ������þ����Ƿ���)
					OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disFlowConfig_whetherPaperProcessing");
					if ("false".equals(value.getValue())) {
						taskService.setAllSended(distributeOrderOid);
					}
				}
			}
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * �޸ķ��ŵ�����
	 * 
	 * @return JSON����
	 */
	public String updateDistributeOrder() {
		try {
			String oid = request.getParameter(KeyS.OID);
			String name = request.getParameter("NAME");// ����
			String note = request.getParameter("NOTE");// ��ע

			service.updateDistributeOrder(oid, name, note);
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * �����û�/��֯��
	 * 
	 * @return JSON����
	 */
	public String addPrincipals() {
		try {
			DistributeInfoService infoService = DistributeHelper.getDistributeInfoService();

			String oids = request.getParameter("oids");
			String type = request.getParameter("type");
			String iids = request.getParameter("iids");
			String disMediaTypes = request.getParameter("disMediaTypes");
			String disInfoNums = request.getParameter("disInfoNums");
			String notes = request.getParameter("notes");

			List<String> oidList = SplitString.string2List(oids, ",");
			List<DistributeOrderObjectLink> distributeOrderObjectLinks = service
					.getDistributeOrderObjectLinksByOids(oidList);
			String distributeOrderObjectLinkOids = "";
			for (DistributeOrderObjectLink linkObj : distributeOrderObjectLinks) {
				String linkOid = Helper.getOid(linkObj);
				distributeOrderObjectLinkOids += linkOid + ",";
			}
			if (distributeOrderObjectLinkOids.endsWith(",")) {
				distributeOrderObjectLinkOids = distributeOrderObjectLinkOids.substring(0,
						distributeOrderObjectLinkOids.length() - 1);
			}
			infoService.createDistributeInfo(type, iids, disMediaTypes, disInfoNums, notes,
					distributeOrderObjectLinkOids, "null");

			getAllDistributeOrder();
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * ɾ�����ŵ�����
	 * 
	 * @return JSON����
	 */
	public String deleteDistributeOrder() {
		try {
			// ��ȡ���ŵ���OIDS
			String oids = request.getParameter(KeyS.OIDS);
			List<String> oidList = SplitString.string2List(oids, ",");
			String errorOids = "";
			String okOids = "";
			for (String orderOid : oidList) {
				if(service.getExistDistributeElecTask(orderOid) == "true"){
					errorOids += orderOid + "," ;
				}else{
					okOids += orderOid + "," ;
				}
			}
			if(!errorOids.isEmpty()){
				errorOids = errorOids.substring(0, errorOids.length() - 1);
			}
			if(!okOids.isEmpty()){
				okOids = okOids.substring(0, okOids.length() - 1);
				service.deleteDistributeOrderByOid(okOids);
			}
			result.put("erroroids", errorOids);
			result.put("success", "true");
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	public String propertydeleteDistributeOrder() {
		try {
			// ��ȡ���ŵ���OIDS
			String oids = request.getParameter(KeyS.OIDS);
			service.deleteDistributeOrderByOid(oids);
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	public String updateOrderLifeCycle() {
		try {
			//String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
			String oid = request.getParameter("OID");
			String flag = service.updateOrderLifeCycle(oid);
			result.put("success", flag);
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}
	
	public String addOutPrincipals(){
		try{
			String innerIds = request.getParameter("innerIds");
			String siteNames = request.getParameter("siteNames");
			String userIds = request.getParameter("userIds");
			String orderOids = request.getParameter("orderOids");
			
			List<String> oidList = SplitString.string2List(orderOids, ",");
			List<DistributeOrderObjectLink> distributeOrderObjectLinks = service.getDistributeOrderObjectLinksByOids(oidList);
			String distributeOrderObjectLinkOids = "";
			for (DistributeOrderObjectLink linkObj : distributeOrderObjectLinks) {
				String linkOid = Helper.getOid(linkObj);
				distributeOrderObjectLinkOids += linkOid + ",";
			}
			
			DistributeInfoService infoService = DistributeHelper.getDistributeInfoService();
			infoService.createOutSignDisInfo(innerIds,siteNames,userIds,distributeOrderObjectLinkOids,true);
			
			getAllDistributeOrder();
			
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

}
