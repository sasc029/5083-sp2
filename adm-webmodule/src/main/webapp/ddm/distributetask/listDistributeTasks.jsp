<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String spot = "ListDistributeTasks";
	String gridId = "distributeTaskGrid";
	String gridTitle = "分发任务";
%>
<html>
	<head>
		<title><%=gridTitle%></title>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
		<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
	</head>
	<body>
		<form id="mainForm" name="mainForm" method="POST">
			<table height="100%" width="100%" cellSpacing="0" cellPadding="0"
			border="0">
			<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
				<jsp:param name="spot" value="<%=spot%>" />
				<jsp:param name="gridId" value="<%=gridId%>" />
				<jsp:param name="gridTitle" value="" />
				<jsp:param name="toolbar_modelId" value="" />
				<jsp:param name="onLoadSuccess" value="onLoadSuccess()"/>
				<jsp:param name="operate_container" value="container" />
			</jsp:include>
		</table>
		</form>
	</body>
</html>
<script type="text/javascript">
	var container = {};

	function onLoadSuccess() {
		onLoadSuccess_ddm("<%=gridId%>");
	}
	function doCustomizeMethod_id(value) {
		var url = '<%=contextPath%>/ddm/distributepapertask/distributePaperTask_tab.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + value;
		ddm.tools.openWindow(url,900,600,"distributeOrderOpen");
	}
</script>
