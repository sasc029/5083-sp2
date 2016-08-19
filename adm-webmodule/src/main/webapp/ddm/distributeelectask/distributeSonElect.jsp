<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
    String toolbarId="";
	String spot = "ListDistributeElectasks";
	String gridTitle = "分发任务树";
	String gridId = "distributeSonElecTask";
	String contextPath = request.getContextPath();
%>
<html>
	<head>
		<title><%=gridTitle%></title>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
		<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>

	</head>
	<body>
		<form id="mainForm" name="mainForm" method="POST">
			<table height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0">
				<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
					<jsp:param name="spot" value="<%=spot%>"/>
					<jsp:param name="gridId" value="<%=gridId%>"/>
					<jsp:param name="toolbar_modelId" value="<%=toolbarId%>" />
					<jsp:param name="onLoadSuccess" value="onLoadSuccess()"/>
					<jsp:param name="gridTitle" value=""/>
					<jsp:param name="operate_container" value="container"/>
				</jsp:include>
			</table>
		</form>
	</body>
</html>
<script type="text/javascript">

	function onLoadSuccess() {
		onLoadSuccess_ddm("<%=gridId%>");
	}

</script>
