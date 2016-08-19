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
 * 分发单Action实现类。
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
	 * 发放管理--调度菜单
	 * 代办任务页面数据一栏显示。
	 * 取得发放单生命周期为【调度中,审批终止,审批被退回】的数据。
	 * /ddm/distributeorder/listDistributeOrders.jsp
	 * 
	 * @return struts页面参数
	 */
	public String getAllDistributeOrder() {
		try {
			
			String[] schStateArray = new String[]{ConstUtil.LC_SCHEDULING.getName(),ConstUtil.LC_APPROVE_TERMINATE.getName(),ConstUtil.LC_APPROVE_REJECT.getName()};
			List<DistributeOrder> listDis = service.listDiffStatesDistributeOrder(schStateArray);
			LOG.debug("取得发放单 " + getDataSize(listDis) + " 条，生命周期状态：" + schStateArray);

			String spot = ConstUtil.SPOT_LISTDISTRIBUTEORDERS;
			
			// 批量验证权限
			List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
			
			// 输出结果
			GridDataUtil.prepareRowObjects(listDatas, spot);
		} catch (Exception ex) {
			error(ex);
		}
		return INITPAGE;
	}
	
	/**
	 * 发放管理--发放单
	 * 代办任务页面数据一栏显示。
	 * 取得发放单生命周期为【分发中】的数据。
	 * /ddm/distributeorder/listdistributesending.jsp
	 * 
	 * @return struts页面参数
	 */
	public String listSendingDistributeOrder() {
		try {
			String[] notInStateArray = new String[]{ConstUtil.LC_DISTRIBUTED.getName(),ConstUtil.LC_COMPLETED.getName()};
			List<DistributeOrder> listDis = service
					.listNotInStatesDistributeOrder(notInStateArray);
			LOG.debug("取得发放单 " + getDataSize(listDis) + " 条，生命周期状态："
					+ notInStateArray + "以外的状态");

			String spot = ConstUtil.SPOT_LISTDISTRIBUTEORDERS;
			
			List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
			GridDataUtil.prepareRowObjects(listDatas, spot);
		} catch (Exception ex) {
			error(ex);
		}
		return "listdistributesending";
	}
	/**
	 * 发放管理--发放单
	 * 代办任务页面数据一栏显示。
	 * 取得发放单生命周期为【已分发】的数据。
	 * /ddm/distributeorder/listdistributesended.jsp
	 * 
	 * @return struts页面参数
	 */
	public String listSendedDistributeOrder(){
		try {
			String [] stateArray=new String[]{ConstUtil.LC_DISTRIBUTED.getName(),ConstUtil.LC_COMPLETED.getName()};
			List<DistributeOrder> listDis = service.listDiffStatesDistributeOrder(stateArray);
			LOG.debug("取得发放单 " + getDataSize(listDis) + " 条，生命周期状态：" + stateArray);
			
			String spot = ConstUtil.SPOT_LISTDISTRIBUTEORDERS;
			List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
			GridDataUtil.prepareRowObjects(listDatas, spot);
		} catch (Exception ex) {
			error(ex);
		}
		return "listdistributesended";
	}
	
	
	/**
	 * 发放管理--发放单
	 * 代办任务页面数据一栏显示。
	 * 取得发放单生命周期为【已分发】的数据。
	 * 第一次加载页面显示一个月之前月的数据
	 * /ddm/distributeorder/listdistributesended.jsp
	 * 
	 * @return struts页面参数
	 */
	public String listSendedDistributeOrderOnload(){
		try {
			long currentTime = System.currentTimeMillis();
			long monthtime = 30*24*60*60*1000L;
			long time = currentTime-monthtime;
			String [] stateArray=new String[]{ConstUtil.LC_DISTRIBUTED.getName(),ConstUtil.LC_COMPLETED.getName()};
			List<DistributeOrder> listDis = service.listDiffStatesDistributeOrderOnload(stateArray,time,currentTime);
			LOG.debug("取得发放单 " + getDataSize(listDis) + " 条，生命周期状态：" + stateArray);
			String spot = ConstUtil.SPOT_LISTDISTRIBUTEORDERS;
			List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
			GridDataUtil.prepareRowObjects(listDatas, spot);
		} catch (Exception ex) {
			error(ex);
		}
		return "listdistributesended";
	}
	
	
	/**
	 * 发放管理--发放单
	 * 代办任务页面数据一栏显示。
	 * 取得发放单生命周期为【已分发】的数据。
	 * /ddm/distributeorder/listdistributesended.jsp
	 * 在页面添加搜索关键字的搜索框
	 * author：杨振州
	 * @return struts页面参数
	 */
	public String listSendedDistributeOrderAdderSearchIuput(){
		String ret="";
		try {
			//搜索框中的搜索字段
			String keywords=request.getParameter("keywords").trim();
			String [] stateArray=new String[]{ConstUtil.LC_DISTRIBUTED.getName(),ConstUtil.LC_COMPLETED.getName()};
			List<DistributeOrder> listDis = service.listSendedDistributeOrderAdderSearchIuput(stateArray,keywords);
			LOG.debug("取得发放单 " + getDataSize(listDis) + " 条，生命周期状态：" + stateArray);
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
	 * 发放管理--发放单
	 * 已分发--【补发】
	 * @return struts页面参数
	 */
	
	public String reissueDistributeOrder(){
		
		try {
			String number = request.getParameter("NUMBER");// 编号
			String name = request.getParameter("NAME");// 名称
			String orderType = request.getParameter("orderType");// 单据类型
			String note = request.getParameter("NOTE");// 备注

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
	 * 发放管理--调度菜单
	 * 被退回任务页面数据一栏显示。
	 * 取得发放单生命周期为【已退回】的数据。
	 * /ddm/distributeorder/listDistributeOrdersReturn.jsp
	 * 
	 * @return struts页面参数
	 */
	public String getAllDistributeOrderReturn() {
		try {
			String lc = ConstUtil.LC_BACKOFF.getName();
			List<DistributeOrder> listDis = service.getAllDistributeOrderReturnByAuth(lc);
			LOG.debug("取得发放单 " + getDataSize(listDis) + " 条，生命周期状态：" + lc);
						
			String spot = ConstUtil.SOPT_LISTDISTRIBUTERETURN;
			
			// 批量验证权限
			List<Object> checkList = checkPermission(listDis, spot, spot);
			
			List<String> keys = GridHelper.getService().getMyLatestGridViewColumnIds(spot, spot);
			
			// 格式化显示数据
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
			// 输出结果
			GridDataUtil.prepareRowObjectMaps(listMap, spot);
		} catch (Exception ex) {
			error(ex);
		}
		return "listDisOrderReturn";
	}

	/**
	 * 取得分发被回退列表详细。
	 * 
	 * @return JSON对象
	 */
	public String getDistributeOrderReturnDetail() {
		try {
			String oid = request.getParameter(KeyS.OID);
			List<DistributeOrder> listDis = service.getDistributeOrderReturnDetail(ConstUtil.LC_BACKOFF.getName(), oid);
			// 格式化显示数据
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
			LOG.debug("取得发放单被回退 " + getDataSize() + " 条，生命周期状态：" + ConstUtil.LC_BACKOFF.getName());
			// 输出结果
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTERETURNDETAIL);
		} catch (Exception ex) {
			error(ex);
		}
		return "listDisOrderReturnDetail";
	}

	/**
	 * 取得相关发放单。
	 * 
	 * @return JSON对象
	 */
	public String getRelatedDistributeOrder() {
		String oid = request.getParameter(KeyS.OID);
		List<DistributeOrder> listDis = service.getRelatedDistributeOrder(oid);
		TypeService typeManager = Helper.getTypeManager();
		for (DistributeOrder disOrder : listDis) {
			listMap.add(typeManager.getAttrValues(disOrder));
		}
		LOG.debug("取得发放单" + getDataSize() + " 条");

		listToJson();
		return OUTPUTDATA;
	}
	
	/**
	 * 取得纸质任务的发放单
	 * 
	 * @return JSON对象
	 */
	public String getRelatetaskdDistributeOrder() {
		String oid = request.getParameter(KeyS.OID);
		List<DistributeOrder> listDis = service.getRelatetaskdDistributeOrder(oid);
		TypeService typeManager = Helper.getTypeManager();
		for (DistributeOrder disOrder : listDis) {
			listMap.add(typeManager.getAttrValues(disOrder));
		}
		LOG.debug("取得发放单" + getDataSize() + " 条");
		listToJson();
		return OUTPUTDATA;
	}

	/**
	 * 创建发放单对象
	 * 
	 * @return JSON对象
	 */

	//创建发放单
	public String createDistributeOrder() {
		try {
			String number = request.getParameter("NUMBER");// 编号
			String name = request.getParameter("NAME");// 名称
			String orderType = request.getParameter("orderType");// 单据类型
			String note = request.getParameter("NOTE");// 备注

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

	//更多操作中批量发放创建发放单
	public String createDistributeOrderMulti() {
		try {
			String number = request.getParameter("NUMBER");// 编号
			String name = request.getParameter("NAME");// 名称
			String orderType = request.getParameter("orderType");// 单据类型
			String note = request.getParameter("NOTE");// 备注

			String oids = request.getParameter("oids");
			//取第一个分发对象创建发放单，其余在发放单创建成功后添加到分发数据里面去，参考DistributeObjectAction.java中addDistributeObject
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
	 * 校验工作流开关是否启动且发放单提交个数。
	 * 
	 * @return JSON对象
	 */
	public String getDistributeToWorkFlow() {
		try {
			// 获取发放单OIDS
			String distributeOrderOids = request.getParameter("oids");
			String flag = "false";
			List<String> oidList = SplitString.string2List(distributeOrderOids, ",");
			for(String orderOid : oidList){
				Persistable obj = Helper.getPersistService().getObject(orderOid);
				DistributeOrder order = (DistributeOrder) obj;
				Context context = order.getContextInfo().getContext();
				OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disFlowConfig_check");
				if ("true".equals(value.getValue())) {
					//启动工作流
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
	 * 创建任务对象。
	 * 
	 * @return JSON对象
	 */
	public String createDistributeTask() {
		try {
//			boolean flag = TokenHelper.validToken();
			// 获取发放单OIDS
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
				//判断是否是补发发放单
				if(ConstUtil.C_ORDERTYPE_1.equals(order.getOrderType())){
					// 补发发放单是否进行纸质处理(根据首选项的配置决定是否开启)
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
	 * 修改发放单对象
	 * 
	 * @return JSON对象
	 */
	public String updateDistributeOrder() {
		try {
			String oid = request.getParameter(KeyS.OID);
			String name = request.getParameter("NAME");// 名称
			String note = request.getParameter("NOTE");// 备注

			service.updateDistributeOrder(oid, name, note);
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 关联用户/组织。
	 * 
	 * @return JSON对象
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
	 * 删除发放单对象。
	 * 
	 * @return JSON对象
	 */
	public String deleteDistributeOrder() {
		try {
			// 获取发放单的OIDS
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
			// 获取发放单的OIDS
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
