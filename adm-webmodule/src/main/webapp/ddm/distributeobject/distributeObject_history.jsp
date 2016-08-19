<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>

<%
	String spot = "ListDistributeInfos";
	String gridId = "distributeInfoGrid";
	String gridTitle = "历史分发信息";
	String contextPath = request.getContextPath();
%>
<html>
	<head>
		<script type="text/javascript"	src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
	<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
	</head>
	<body>
	<form id='main_form'>
	   <table height="100%" width="100%" cellSpacing="0" cellPadding="0"
			border="0">
			<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
				<jsp:param name="spot" value="<%=spot%>" />
				<jsp:param name="gridId" value="<%=gridId%>" />
				<jsp:param name="operate_container" value="container" />
			</jsp:include>
		</table>
		</form>
	</body>
</html>
<script type="text/javascript">
<%-- function onLoadSuccess() {
	onLoadSuccess_ddm("<%=gridId%>");
}   --%>

var container = {};


// 重新加载
function reload(){
	var table = pt.ui.get("<%=gridId%>");
	table.reload();
}

function tableReload(){
		var table = pt.ui.get("<%=gridId%>");
		table.reload();
	}
</script>
