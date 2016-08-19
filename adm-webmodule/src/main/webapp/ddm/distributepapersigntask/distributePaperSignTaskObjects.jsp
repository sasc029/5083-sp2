<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%
	String contextPath = request.getContextPath();
	String oid = (String) session.getAttribute(ConstUtil.DISTRIBUTE_PAPERSIGNTASK_OID);
	String title = "分发数据";
	String spot = "ListDistributePaperSignObjects";
	String gridId = "distributeObjectGrid";
	//String toolbarId = "ddm.distributeTaskObject.list.toolbar";
%>
<html>
	<head>
		<title><%=title%></title>
		<script type="text/javascript"	src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
		<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
	</head>
	<body>
		<form id="mainForm" name="mainForm" method="POST">
			<table height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0">
				<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
					<jsp:param name="spot" value="<%=spot%>"/>
					<jsp:param name="gridId" value="<%=gridId%>"/>
					<jsp:param name="gridTitle" value=""/>
					<jsp:param name="toolbar_modelId" value=""/>
					<jsp:param name="onLoadSuccess" value="onLoadSuccess()"/>
					<jsp:param name="operate_container" value="container"/>
					<jsp:param name="operate_mainObjectOID" value="<%=oid%>" />
				</jsp:include>
			</table>
		</form>
	</body>
	
	<script>
	var container = {};
	
	function onLoadSuccess() {
		onLoadSuccess_ddm("<%=gridId%>");
	}
	
	function distirbuteObjectArticulate(){
		var url = contextPath + "/ddm/distributeobject/distributeObject_frame.jsp?OID=<%=oid%>";
		window.open(url,900,600,"distributeObjectArticulate");
	}
	</script>
</html>