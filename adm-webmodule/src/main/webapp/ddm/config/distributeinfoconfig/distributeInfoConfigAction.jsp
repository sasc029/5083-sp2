<%@page import="com.bjsasc.plm.core.util.StringUtil"%>
<%@page import="com.bjsasc.plm.core.persist.PersistHelper"%>
<%@page import="java.util.*"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.cascc.avidm.util.SplitString"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.core.system.principal.Role"%>
<%@page import="com.bjsasc.plm.core.system.principal.Principal"%>
<%@page import="com.bjsasc.plm.core.system.principal.OrganizationHelper"%>
<%@page import="com.bjsasc.plm.core.system.principal.UserHelper"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeinfoconfig.DistributeInfoConfig"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeinfoconfig.DistributeInfoConfigService"%>
<%@page import="com.bjsasc.plm.core.system.principal.Organization"%>
<%@page import="com.bjsasc.plm.core.system.principal.User"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%
	String contextPath = request.getContextPath();
	String op = request.getParameter("op");
	
	String results = "";
	
	try {
	// 添加用户或组织
	 if (op.equals("addRoleOrOrg")) {
		 
		 String type = request.getParameter("type");
		 String iids=request.getParameter("iids");
		 String disMediaTypes=request.getParameter("disMediaTypes");
		 String disInfoNums=request.getParameter("disInfoNums");
		 String notes=request.getParameter("notes");
		 
		 DistributeInfoConfigService infoConfigservice = DistributeHelper.getDistributeInfoConfigService();
		 boolean flag=infoConfigservice.addDistributeInfoConfig(type, iids, disMediaTypes, disInfoNums, notes);
		
		 if(flag){
		  Map<String, String> result = new HashMap<String, String>();
		  result.put("SUCCESS", "false");
		  results = DataUtil.mapToSimpleJson(result);
			  //return ;
		 }else{
		   String spot = "ListDistributeInfoConfig";
		   Map<String, String> result = new HashMap<String, String>();
		   List<Map<String, Object>> listData = infoConfigservice.listDistributeInfoConfig(spot,spot);
			//在session上缓存查询结果，以便高效排序
		   GridDataUtil.prepareRowQueryResult(listData, spot);
		   result.put("success", "true");
		   results = DataUtil.mapToSimpleJson(result);
		 }
	} 
	// 删除数据
	else if (op.equals("deleteUserOrOrg")) {
		
		 String oids = request.getParameter("OIDS");
		 List<String> oidsList = SplitString.string2List(oids, ",");
		 
		 DistributeInfoConfigService infoConfigservice = DistributeHelper.getDistributeInfoConfigService();
		 for(int i=0;i<oidsList.size();i++){
			 DistributeInfoConfig distributeInfoConfig=(DistributeInfoConfig) Helper.getPersistService().getObject(oidsList.get(i));
			 infoConfigservice.delDistributeInfoConfig(distributeInfoConfig);
		 }
	    String spot = "ListDistributeInfoConfig";
	    Map<String, String> result = new HashMap<String, String>();
	    List<Map<String, Object>> listData = infoConfigservice.listDistributeInfoConfig(spot,spot);
		//在session上缓存查询结果，以便高效排序
	    GridDataUtil.prepareRowQueryResult(listData, spot);
	    result.put("success", "true");
	    results = DataUtil.mapToSimpleJson(result);
		 
	} 
	// 编辑数据
	else if (op.equals("editUserOrOrg")) {
		
		 String oid = request.getParameter("oid");
		 String disMediaType = request.getParameter("disMediaType");
		 String disInfoNum = request.getParameter("disInfoNum");
		 String note = request.getParameter("note");
		 
		 DistributeInfoConfigService infoConfigservice = DistributeHelper.getDistributeInfoConfigService();
		 Persistable persist=Helper.getPersistService().getObject(oid);
		 DistributeInfoConfig infoConfig=(DistributeInfoConfig)persist;
		
		 infoConfig.setDisInfoNum(Long.parseLong(disInfoNum));
		 infoConfig.setDisMediaType(disMediaType);
		 infoConfig.setNote(note.trim());
		 
		 infoConfigservice.updateDistributeInfoConfig(infoConfig);
		 
		 String spot = "ListDistributeInfoConfig";
	     Map<String, String> result = new HashMap<String, String>();
	     List<Map<String, Object>> listData = infoConfigservice.listDistributeInfoConfig(spot,spot);
		 //在session上缓存查询结果，以便高效排序
	     GridDataUtil.prepareRowQueryResult(listData, spot);
	     result.put("success", "true");
	     results = DataUtil.mapToSimpleJson(result);
	}
	// 错误处理
	} catch (Exception ex) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("success", "false");
		result.put("message", ex.getMessage());
		results = DataUtil.mapToSimpleJson(result);
	}
	out.print(results);
%>