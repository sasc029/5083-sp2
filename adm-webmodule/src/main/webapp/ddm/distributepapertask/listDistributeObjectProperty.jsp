<%@page import="java.util.Map"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.type.TypeService"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%@include file="/ddm/public/ddm.jsp" %>

<%
	String path = request.getContextPath();
	//String oid = request.getParameter("OID");
	//String oid = request.getParameter("oid");
	
	String contextPath = request.getContextPath();
	String classId = DistributePaperTask.CLASSID;
	String spot = "ListDistributeTaskPropertys";
	String gridId = "";
	String gridTitle = "";
	String toolbarId = "";

%>
<html>
<head>
	<title>属性信息</title>
	<script type="text/javascript" src="<%=request.getContextPath() %>/platform/common/js/ptutil.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
	<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0"  scroll="no">
<table cellSpacing="0" cellPadding="0" height="100%" width="100%" border="0">
	<tr><td>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		 <tr>
	    	<td class="AvidmTitleName">
	    		<div class="imgdiv"><img src="<%=request.getContextPath() %>/plm/images/common/modify.gif"/></div>
				<div class="namediv">纸质任务信息</div></td>
		 </tr>
	</table>
	</td></tr>
		<tr><td>
		<table height="437" width="100%" cellSpacing="0" cellPadding="0" border="0">
		<tr><td>
		<form id="mainForm" name="mainForm" method="POST">
			<table height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0">
				<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
					<jsp:param name="spot" value="<%=spot%>"/>
					<jsp:param name="gridId" value="<%=gridId%>"/>
					<jsp:param name="gridTitle" value="<%=gridTitle%>"/>
					<jsp:param name="toolbar_modelId" value="<%=toolbarId%>"/>
					<jsp:param name="operate_container" value="container"/>
				</jsp:include>
			</table>
		</form>
		</td></tr></table>
		</td></tr></table>
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
