<%@page import="com.bjsasc.plm.ui.tree.TreeHelper"%>
<%@page import="com.bjsasc.plm.ui.tree.TreeNode"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.core.context.model.RootContext"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.plm.core.Helper"%>
<%@page import="com.bjsasc.plm.core.context.model.ProductContext"%>
<%@page import="com.bjsasc.plm.ui.outlook.OutlookHelper"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstLifeCycle"%>
<%@page import="com.bjsasc.platform.objectmodel.business.lifeCycle.State"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder"%>
<%@page import="com.bjsasc.ddm.distribute.action.AbstractAction"%>
<%@page import="com.bjsasc.ddm.common.CheckPermission"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask"%> 
<%@page import="com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService"%>
<%@page import="com.bjsasc.ddm.common.ConstLifeCycle"%> 
<%@page import="com.bjsasc.ddm.distribute.service.duplicateprocess.DuplicateProcessService"%> 

<%
	String nodeId=request.getParameter("nodeId");
	Context rootContext = Helper.getContextService().getRootContext();
	List<Map<String, Object>> list = TreeHelper.toMaps(OutlookHelper.buildGlobalMenus("com.bjsasc.ddm.distribute"));
	int num1=0;
	int num2=0;
	if(nodeId=="com.bjsasc.ddm.distribute.dispatchManager"){
		DistributeOrderService service1 = DistributeHelper.getDistributeOrderService();
		String[] schStateArray = new String[]{ConstUtil.LC_SCHEDULING.getName(),ConstUtil.LC_APPROVE_TERMINATE.getName(),ConstUtil.LC_APPROVE_REJECT.getName()};
		List<DistributeOrder> listDis1 = service1.listDiffStatesDistributeOrder(schStateArray);
		String spot1 =ConstUtil.SPOT_LISTDISTRIBUTEORDERS;
		List<Map<String, Object>> listDatas1 = CheckPermission.checkPermissionAF(listDis1, spot1, spot1);
		num1=listDatas1.size();
		//为调度节点添加代办任务数量
		list.get(1).put("Name","调度(<font color='red' style='font-weight:bold'>"+num1+"</font>)");
	}else if(nodeId=="com.bjsasc.ddm.distribute.distributePaperTask"){
		DistributePaperTaskService service2 = DistributeHelper.getDistributePaperTaskService();
		String lc = ConstUtil.LC_PROCESSING.getName();
		List<DistributePaperTask> listDis2 = service2.getAllDistributePaperTaskByAuth(lc);
		String spot2 = ConstUtil.SPOT_LISTDISTRIBUTEPAPERTASKS;
		List<Map<String, Object>> listDatas2 = CheckPermission.checkPermissionAF(listDis2, spot2, spot2);
		num2=listDatas2.size();
		//为加工单增加代办任务数量
		list.get(2).put("Name","加工单(<font color='red' style='font-weight:bold'>"+num2+"</font>)");
		//为复制加工节点增加未签收任务数量
		//list.get(3).put("Name","复制加工(<font color='red' style='font-weight:bold'>"+num3+"</font>)");
	}else{
		DistributeOrderService service1 = DistributeHelper.getDistributeOrderService();
		String[] schStateArray = new String[]{ConstUtil.LC_SCHEDULING.getName(),ConstUtil.LC_APPROVE_TERMINATE.getName(),ConstUtil.LC_APPROVE_REJECT.getName()};
		List<DistributeOrder> listDis1 = service1.listDiffStatesDistributeOrder(schStateArray);
		String spot1 =ConstUtil.SPOT_LISTDISTRIBUTEORDERS;
		List<Map<String, Object>> listDatas1 = CheckPermission.checkPermissionAF(listDis1, spot1, spot1);
		num1=listDatas1.size();
		//为调度节点添加代办任务数量
		list.get(1).put("Name","调度(<font color='red' style='font-weight:bold'>"+num1+"</font>)");
		DistributePaperTaskService service2 = DistributeHelper.getDistributePaperTaskService();
		String lc = ConstUtil.LC_PROCESSING.getName();
		List<DistributePaperTask> listDis2 = service2.getAllDistributePaperTaskByAuth(lc);
		String spot2 = ConstUtil.SPOT_LISTDISTRIBUTEPAPERTASKS;
		List<Map<String, Object>> listDatas2 = CheckPermission.checkPermissionAF(listDis2, spot2, spot2);
		num2=listDatas2.size();
		//为加工单增加代办任务数量
		list.get(2).put("Name","加工单(<font color='red' style='font-weight:bold'>"+num2+"</font>)");
		
	} 
	String result = DataUtil.encode(list);
	out.print(result);
%>


