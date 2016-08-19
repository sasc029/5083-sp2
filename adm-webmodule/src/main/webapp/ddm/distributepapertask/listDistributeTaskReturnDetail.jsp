<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>

<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%@include file="/ddm/public/ddm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String classId = DistributeOrder.CLASSID;
	String spot = "ListDistributePaperTaskReturnDetail";
	String gridId = "";
	String gridTitle = "回退列表";
	String toolbarId = "";
	String gridUrl = contextPath + "/ddm/distribute/distributeOrderHandle!getAllDistributeOrder.action";
	String addPrincipalUrl = contextPath + "/ddm/distribute/distributeOrderHandle!addPrincipals.action";
%>
<html>
	<head>
		<title><%=gridTitle%></title>
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
					<jsp:param name="gridTitle" value="<%=gridTitle%>"/>
					<jsp:param name="toolbar_modelId" value="<%=toolbarId%>"/>
					<jsp:param name="onLoadSuccess" value="onLoadSuccess()"/>
					<jsp:param name="operate_container" value="container"/>
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
	//数据刷新方法
	function reload(){
		window.location.reload(true);
		parent.frames['leftFrame'].refreshTree();
	}

	//刷新dategrid
	function tableReload(text){
		plm.showMessage({title:'系统提示', message:text, icon:'2'});
		reload();
	}
</script>
