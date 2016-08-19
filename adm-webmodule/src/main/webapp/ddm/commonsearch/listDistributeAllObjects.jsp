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

<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String oid = request.getParameter("oid");
	String classId = DistributeObject.CLASSID;
	String spot = "distributeCommonSearch";
	String gridId = "distributeCommonSearchtGrid";
	String gridTitle = "�ۺϲ�ѯ";
	String toolbarId = "ddm.commonsearch.list.toolbar";
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
					<jsp:param name="gridTitle" value="<%=gridTitle%>"/>
					<jsp:param name="toolbar_modelId" value="<%=toolbarId%>"/>
					<jsp:param name="operate_container" value="container"/>
				</jsp:include>
			</table>
		</form>
		<form id="download" name="download" method="POST">
			<input type='hidden' name="SEARCH_TEXT" id="SEARCH_TEXT"/>
			<input type='hidden' name="<%=KeyS.OIDS%>" id="<%=KeyS.OIDS%>"/>
		</form>
	</body>
</html>

<script type="text/javascript">

	var container = {};
	
	function doCustomizeMethod_id(value) {
		var arr = value.split(":");
		if (arr[0] == "DistributeOrder") {
			var url = '<%=contextPath%>/ddm/distributeorder/distributeOrder_tab.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + value;
			ddm.tools.openWindow(url,900,600,"distributeOrderOpen");
		}
	}	
	
	// ���ؼ����������档
	function returnConditionPage() {
		window.location.href = "<%=contextPath%>/ddm/commonsearch/listDistributeConditions.jsp";
	}

	// ����
	function reDistribute() {
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if (data == null || data.length != 1) {
			plm. showMessage({title:'��ʾ', message:"��ѡ��һ����Ŀ����ִ�в�������!", icon:'3'});
			return;
		}
		
		var dataInnerId = data[0].DATAINNERID;
		var dataClassId = data[0].DATACLASSID;
		if (dataClassId == '' || dataClassId == undefined) {
			plm. showMessage({title:'��ʾ', message:"��ѡ�������ļ�����ִ�в�������!", icon:'3'});
			return;
		}
		var oid = dataClassId + ":" + dataInnerId;
		
		var url = "<%=contextPath%>/ddm/distributeorder/createDistributeOrder.jsp?<%=KeyS.CALLBACK%>=reload&oid=" + oid;
		plm.OpenWindowAndReload(url);
	}
	
	// ��ӡ�ĵ�
	function dataPrint() {
		
	}
	
	// ������ѯ���
	function dataExport() {
		var url = "<%=contextPath%>/ddm/distribute/commonSearchHandle!dataExport.action";

		var table = pt.ui.get("<%=gridId%>");
		var selections = table.getSelections();
		var oids = "";
		for(var i=0;i<selections.length;i++){
			oids += selections[i].OID + ",";
		}
		oids = oids.substring(0, oids.length - 1);
		$("#<%=KeyS.OIDS%>").val(oids);
		$("#SEARCH_TEXT").val($("#distributeCommonSearchtGrid_searchText").val());
		$("#download").attr("action", url);
		$("#download").submit();
	}
</script>