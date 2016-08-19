<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.core.system.access.operate.Operate"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder"%>
<%@page import="com.bjsasc.ddm.distribute.model.recdespapertask.RecDesPaperTask"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%@include file="/ddm/public/ddm.jsp" %>

<% 
String contextPath = request.getContextPath();
String classId = RecDesPaperTask.CLASSID;
String spot = "ListDistributeTasks";
String gridId = "recDesTaskGrid";
String gridTitle = "回收销毁任务";
String toolbarId = "";
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
		<table table height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0">
			<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
				<jsp:param name="spot" value="<%=spot%>"/>
				<jsp:param name="gridId" value="<%=gridId%>"/>
				<jsp:param name="gridTitle" value=""/>
				<jsp:param name="toolbar_modelId" value="<%=toolbarId%>"/>
				<jsp:param name="onLoadSuccess" value="onLoadSuccess()"/>
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

var container = {};
    
	function dataPrint(){
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		alert("Number: "+data[0].NUMBER+".\n Name: "+data[0].NAME);
	}

</script>