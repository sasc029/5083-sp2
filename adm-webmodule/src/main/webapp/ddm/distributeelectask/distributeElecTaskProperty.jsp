<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.type.TypeService"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.core.util.DateTimeUtil"%>

<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeelectask.DistributeElecTaskService"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeelectask.DistributeElecTask"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
try {
	String title = "电子任务属性信息";
	String path = request.getContextPath();
	String oid = (String) session.getAttribute(ConstUtil.DISTRIBUTE_ELECTASK_OID);
	DistributeElecTaskService service = DistributeHelper.getDistributeElecTaskService();
	DistributeElecTask disTask = service.getDistributeElecTaskInfo(oid);
	TypeService typeManager = Helper.getTypeManager();
	Map<String, Object> map = typeManager.format(disTask,KeyS.VIEW_DETAIL);

%>
<html>
<head>
	<title><%=title%></title>
	<script type="text/javascript" src="<%=request.getContextPath() %>/platform/common/js/ptutil.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
	<script type="text/javascript"	src="<%=request.getContextPath()%>/ddm/javascript/ddmutil.js"></script>
</head>
<body class="openWinPage">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		 <tr>
	    	<td class="AvidmTitleName">
	    		<div class="imgdiv"><img src="<%=request.getContextPath() %>/plm/images/common/modify.gif"/></div>
				<div class="namediv"><%=title%></div></td>
		 </tr>
	</table>
	
	<input type="hidden" value="<%=oid %>" name="<%=KeyS.OID %>" />
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
		<tr>
			<td class="left_col AvidmW150">任务编号：</td>
			<td class="e-checked-text" width="35%"><%= disTask.getNumber() %></td>
			<td class="left_col AvidmW150">发送人：</td>
			<td class="e-checked-text" width="35%"><%= disTask.getCreateByName() %></td>
		</tr>
		<tr>
			<td class="left_col AvidmW150">发送时间：</td>
			<td class="e-checked-text"><%= DateTimeUtil.getDateDisplay(disTask.getSendTime()) %></td>
			<td class="left_col AvidmW150">上下文：</td>
			<td><%=map.get("DDMCONTEXT")%></td>
		</tr>
		<tr>
			<td class='left_col AvidmW150'>生命周期模板：</td>
			<td><%=map.get("LIFECYCLE_TEMPLATE") %></td>
			<td class='left_col AvidmW150'>状态：</td>
			<td><%=map.get("LIFECYCLE_STATE") %></td>
		</tr>
			<tr>
			<td class='left_col AvidmW150'>所属发放单：</td>
			<td><a href ="#" onclick="javascript:selectOrderProperty('<%=disTask.getOrderOid() %>')"><%=disTask.getOrderName() %></a></td>
			<td class='left_col AvidmW150'></td>
			<td></td>
		</tr>
	</table>
	<br>
</body>
</html>
<% 
}catch (Exception ex) {
	ex.printStackTrace();
} 
%>
<script type="text/javascript">

function selectOrderProperty(value){
	var url = '<%=request.getContextPath()%>/ddm/distributeorder/distributeOrder_tab.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + value;
	ddm.tools.openWindow(url,800,600,"distributeOrderOpen");
}
</script>
