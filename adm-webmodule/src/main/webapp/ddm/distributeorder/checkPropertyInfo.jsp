<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>

<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String path = request.getContextPath();
	String oid = request.getParameter("oid");
	
	Persistable obj = Helper.getPersistService().getObject(oid);
	DistributeOrder dis = (DistributeOrder)obj;
	String orderType = dis.getOrderType();
	if(("0").equals(orderType)){
		orderType = "发放单";
	}else{
		orderType = "补发发放单";
	}
%>
<html>
<head>
	<title>发放单信息查看</title>
	<script type="text/javascript" src="<%=request.getContextPath() %>/platform/common/js/ptutil.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
</head>
<body class="openWinPage">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		 <tr>
	    	<td class="AvidmTitleName">
	    		<div class="imgdiv"><img src="<%=request.getContextPath() %>/plm/images/common/modify.gif"/></div>
				<div class="namediv">发放单信息查看</div></td>
		 </tr>
	</table>
	
	<input type="hidden" value="<%=oid %>" name="<%=KeyS.OID %>" />
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
			<tr>
			    <td class="left_col AvidmW150" >单据编号：</td>
			    <td>
			    	<%=dis.getId()%>
			    </td>
			    <td class="left_col AvidmW150" >单据名称：</td>
			    <td>
			    	<%=dis.getName()%>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" >创建者：</td>
			    <td>
			    	<%=dis.getManageInfo().getCreateBy().getName()%>
			    </td>
			    <td class="left_col AvidmW150" >创建时间：</td>
			    <td>
			    	<%=dis.getManageInfo().getCreateTimeDisplay()%>
			    </td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'>单据类型：</td>
				<td>
					<%=orderType %>
				</td>
			 	<td class="left_col AvidmW150">备注：</td>
				<td>
					<%=dis.getNote()%>
				</td>
			</tr>
			<tr id="includeDynamicFields"></tr>
		</table>
</body>
</html>
<script type="text/javascript">
	
	//关闭修改页面并调用父页面刷新方法
	function onAjaxExecuted(result){	
		opener.tableReload("修改成功");		
		window.close();
	}
	
	function cancleButton(){
		window.close();
	}

</script>
