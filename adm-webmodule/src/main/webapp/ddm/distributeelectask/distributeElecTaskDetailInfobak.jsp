<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.type.TypeService"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.context.ContextManager"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="com.bjsasc.plm.navigator.NavigatorUtil"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeelectask.DistributeElecTask"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String title = "分发数据";
%>
<html>
	<head>
		<title><%=title%></title>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
		<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js"></script>
	</head>
	<body>
		<form id="mainForm" name="mainForm" method="POST">
<table cellSpacing="0" cellPadding="0" height="100%" width="100%" border="0">
	<tr><td height="1%">	
		<!-- 公共信息 开始 -->
		<table width="100%" cellSpacing="0" cellPadding="0" border="0">
			<tr class="AvidmToolbar">
				<td nowrap="nowrap">
					<div class="AvidmMtop5 twoTitle">
						<a class="showIcon" href="#" onclick="divSelected(this)">
						<img src="<%=contextPath%>/plm/images/common/space.gif"></a>
						<%=title%>
					</div>
				</td>
			</tr>
		</table></td></tr>

	<tr><td>	
<%
	String spot = "ListDistributeElecObjects";
	String gridId = "distributeObjectGrid";
	String gridTitle = "分发数据列表";
	String gridUrl = "";
%>
	
		<table height="460" width="100%" cellSpacing="0" cellPadding="0"
			border="0">
			<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
				<jsp:param name="spot" value="<%=spot%>" />
				<jsp:param name="gridId" value="<%=gridId%>" />
				<jsp:param name="gridTitle" value="<%=gridTitle%>" />
				<jsp:param name="toolbar_modelId" value="" />
				<jsp:param name="operate_container" value="container" />
			</jsp:include>
		</table>
	</td></tr>
	</table>
	</form>
	</body>
</html>
<script type="text/javascript">

	var container = {};
	
	function selectOrderProperty(value){
		var url = '<%=contextPath%>/ddm/distributeorder/distributeOrder_tab.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + value;
		ddm.tools.openWindow(url,800,600,"distributeOrderOpen");
	}

</script>
