<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.adm.common.ConstUtil"%>
<%@page import="com.bjsasc.adm.active.model.activedocument.ActiveDocument"%>
<%@include file="/plm/plm.jsp" %>
<%@include file="/adm/activepublic/activeError.jsp" %>
<%@page import="com.bjsasc.adm.common.ActiveInitParameter" %>
<%
 	String contextPath = request.getContextPath();
	String tempFolderOid = ActiveInitParameter.getActiveCabinetOid();
	String spot = ConstUtil.SPOT_LISTACTIVEDOCUMENT;
	String gridId = ConstUtil.GRID_LISTACTIVEDOCUMENT;
	String gridTitle = "现行文件列表";
	String toolbarId = "activedocument.operate";
	//类型参数
	String classId = "ActiveDocument"; 
%>
<html>
	<head>
		<title><%=gridTitle%></title>
		<script type="text/javascript"	src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/platform/public/selectlifecycle/LifeCycleUtil.js" charset="GBK"></script>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
	</head>
	<body>
		<form id="mainForm" name="mainForm" method="POST">
			<table height="100%" width="100%" cellSpacing="0" cellPadding="0"
			     border="0">
			    <input type="hidden" name="CLASSID" id="CLASSID" value="<%=classId %>"/>
				<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
					<jsp:param name="spot" value="<%=spot%>"/>
					<jsp:param name="gridId" value="<%=gridId%>"/>
					<jsp:param name="gridTitle" value="<%=gridTitle%>"/>
					<jsp:param name="toolbar_modelId" value="<%=toolbarId%>"/>
					<jsp:param name="operate_container" value="container"/>
				</jsp:include>
		</table>
		</form>
	</body>
</html>
<script type="text/javascript">
var container = {OID:"<%=tempFolderOid%>"};
	function reload(){
		var table = pt.ui.get("<%=gridId%>");
		table.reload();
	}
</script>
