<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="GBK"%>
<%@page import="com.bjsasc.plm.util.Ajax"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.bjsasc.ddm.common.CheckPermission"%>
<%@page import="com.bjsasc.ddm.common.ConstLifeCycle"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.platform.objectmodel.business.lifeCycle.State"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributetask.DistributeTask"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeelectask.DistributeElecTask"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributepapersigntask.DistributePaperSignTask"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeelectask.DistributeElecTaskService"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributepapersigntask.DistributePaperSignTaskService"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%
	int rowss = Integer.parseInt(request.getParameter("rowss"));
	String contextPath = request.getContextPath();
	String stateId = request.getParameter("stateId");
	State objState = Helper.getLifeCycleService().getState(stateId);
	String state = objState.getName();
	String spot = "ListDistributeElecTasks_notSigned";
	DistributeElecTaskService disElecTaskService = DistributeHelper.getDistributeElecTaskService();
	DistributePaperSignTaskService disPaperSignTaskService = DistributeHelper.getDistributePaperSignTaskService();
	
	List<Map<String, Object>> listDis = new ArrayList<Map<String, Object>>();
	
	//电子任务
	List<DistributeElecTask> disElecTasklistDis = disElecTaskService.getAllNoSignDistributeElecTask(state);
	// 批量验证权限
	List<Map<String, Object>> disElecTasklistDatas = CheckPermission.checkPermissionAF(disElecTasklistDis, spot, spot);
	listDis.addAll(disElecTasklistDatas);
	//纸质签收任务
	List<DistributePaperSignTask> disPaperSignTasklistDis = disPaperSignTaskService.getAllNoSignDistributePaperSignTask(state);
	// 批量验证权限
	List<Map<String, Object>> disPaperSignTasklistDatas = CheckPermission.checkPermissionAF(disPaperSignTasklistDis, spot, spot);
	listDis.addAll(disPaperSignTasklistDatas);
	
	List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
	Map<String, Object> result = new HashMap<String, Object>();
	int i = 0;
	if(listDis != null){
		for (Map<String,Object> defaultmap:listDis) {
			if(defaultmap != null){
				if(i>=rowss){
					break;	
				}
				Map<String,Object> map = new  HashMap<String,Object>();
				String title = (String)defaultmap.get("NAME");
				title = title.replace("/plm/common/visit/VisitObject.jsp", "/ddm/public/visitObject.jsp");
				map.put("TITLE", title);
				map.put("IMAGE", defaultmap.get("TYPE"));
				map.put("EXTINFO", defaultmap.get("CREATE_TIME")); 
				lists.add(map);
				i++;
			}
		}
	}
	result.put("COUNT", listDis.size());
	result.put(Ajax.OBJECTS, (Object)lists);
	result.put(Ajax.SUCCESS, Ajax.TRUE);
	String json = DataUtil.mapToSimpleJson(result);
	out.print(json); 
%>
