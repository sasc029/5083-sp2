<%@page import="java.util.*"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%
	String contextPath = request.getContextPath();
	String op = request.getParameter("op");
	if (op == null) {
		op = "setMaster";
	}
	String results = "";
	try {
	if (op.equals("setMaster")) {
		String distributeOrderOid = request.getParameter("distributeOrderOid");
		String distributeObjectOid = request.getParameter("distributeObjectOid");
		
		DistributeObjectService service = DistributeHelper.getDistributeObjectService();
		// 更新数据
		service.setMaster(distributeOrderOid, distributeObjectOid);
		
		// 重新取得数据
		service.getAllDistributeObject(distributeOrderOid);
		
		Map<String, String> result = new HashMap<String, String>();
		
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