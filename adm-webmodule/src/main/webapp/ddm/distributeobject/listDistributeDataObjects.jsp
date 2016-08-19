<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.ui.UIDataInfo"%>	

<%@page import="com.bjsasc.plm.type.TypeService"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.context.ContextManager"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="com.bjsasc.plm.navigator.NavigatorUtil"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.Helper"%>

<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder"%>
<%@page import="com.bjsasc.plm.core.system.access.operate.Operate"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%@include file="/ddm/public/ddm.jsp" %>

<%
	String spot = "ListDistributeDataObject";
	String gridId = "distributeObjectGrid";
	String gridTitle = "任务相关业务对象";
	String toolbarId = "";
	String oid = request.getParameter("oid");
	String url = request.getContextPath() + "/ddm/distributeobject/listDistributeDataObjects_get.jsp?op=getRefObjectTop"
			+ "&oid=" + oid ;
	
%>
<html>
	<head>
		<title><%=gridTitle%></title>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
	</head>
	<body>
		<form id="mainForm" name="mainForm" method="POST">
			<table height="100%" width="100%" cellSpacing="0" cellPadding="0"
			     border="0">
				<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
					<jsp:param name="spot" value="<%=spot%>"/>
					<jsp:param name="gridId" value="<%=gridId%>"/>
					<jsp:param name="gridTitle" value=""/>
					<jsp:param name="gridUrl" value="<%=url%>"/>
					<jsp:param name="treeColumn" value="NUMBER"/>
					<jsp:param name="toolbar_modelId" value="<%=toolbarId%>"/>
					<jsp:param name="operate_container" value="container"/>
					<jsp:param name="onLoadSuccess" value="loadSucess()"/>
				</jsp:include>
				<%-- </td></tr>--%>
		</table>
		</form>
	</body>
</html>
<script type="text/javascript">
	var container = {};
	function getSelections(){
		var grid = pt.ui.get("<%=gridId%>");
		var data =grid.getSelections();
		if(data.length == 0){
			return null;
		}else{
			var oids = "";
			for(var i=0;i<data.length;i++){
				oids += data[i].OID + ",";
			}
			oids = oids.substring(0, oids.length - 1);
			return oids;
		}
		
	}
	function loadSucess(){
		 var grid = pt.ui.get("<%=gridId%>");
	 		var rows=grid.getRows();
			$(rows).each(function(i){
					rows[i].expanded = false;
					grid.expand(rows[i],true,expand4Children);
					
			});
	}
	
function expand4Children(records){
	var grid = pt.ui.get("<%=gridId%>");
	if(records!=null&&records.length>0){
		for(var j = records.length-1;j >=0; j--){
			grid.enableEdit(records[j],false);
			grid.enableSelect(records[j],false);
			grid.expand(records[j], true,expand4Children);
		}
	}
}
</script>
