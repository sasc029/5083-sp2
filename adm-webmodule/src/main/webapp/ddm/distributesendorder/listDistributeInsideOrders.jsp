<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@ page import="java.util.Map"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp"%>

<%
	String contextPath = request.getContextPath();
	Map<String,String> map = null;
	map = (Map<String,String>) session.getAttribute("DDM_DISTRIBUTE_INSIDE");
	String spot = "ListDistributeInsideOrders";
	String gridId = "distributeInsideOrderGrid";
	String sendType = map.get("sendType");
	String gridTitle  = "";
	if("0".equals(sendType)){
		gridTitle = "内发单";
	}else{
		gridTitle = "外发单";
	}
	
	String toolbarId = "ddm.distributeinsideOrder.list.toolbar";
	String submitUrl = contextPath + "/ddm/distribute/distributeSendOrderHandle!updateDistributeInsideCycles.action";
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
				<jsp:param name="gridTitle" value="<%=gridTitle%>" />
				<jsp:param name="toolbar_modelId" value="<%=toolbarId%>" />
				<jsp:param name="onLoadSuccess" value="onLoadSuccess()"/>
				<jsp:param name="operate_container" value="container" />
			</jsp:include>
		</table>
	</form>
</body>
<form id="download" name="download" method="POST"></form>
</html>
<script type="text/javascript">

	var container = {};

	function onLoadSuccess() {
		onLoadSuccess_ddm("<%=gridId%>");
	}
	
	function doCustomizeMethod_operate(value) {
		plm.startWait();
		$.ajax({
			url:"<%=submitUrl%>",
			type:"post",
			dataType:"text",
			data:"<%=KeyS.OIDS%>="+value,
			success:function(result){
				plm.endWait();
				setTimeout("initPage()",500);
				var url = '<%=contextPath %>/ddm/distribute/distributeSendOrderHandle!paperTaskExcel.action?<%=KeyS.OIDS%>=' + value;
				$("#download").attr("action", url);
				$("#download").submit();
			},
			error:function(){
				plm.showMessage({title:'错误提示', message:"提交失败！", icon:'1'});
			}
		});
		
	}

	function reload(){
		var table = pt.ui.get("<%=gridId%>");
		table.reload();
	}
	
	function dataPrint(){
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if (data == null || data.length == 0) {
			plm. showMessage({title:'提示', message:"请至少选择1个操作对象!", icon:'3'});
			return;
		}
		var oids = "";
		for(var i=0;i<data.length;i++){
			oids += data[i].OID + ",";
		}
		oids = oids.substring(0, oids.length - 1);
		plm.startWait();
		$.ajax({
			url:"<%=submitUrl%>",
			type:"post",
			dataType:"text",
			data:"<%=KeyS.OIDS%>="+oids,
			success:function(result){
				plm.endWait();
				setTimeout("initPage()",500);
				var url = '<%=contextPath %>/ddm/distribute/distributeSendOrderHandle!paperTaskExcel.action?<%=KeyS.OIDS%>=' + oids;
				$("#download").attr("action", url);
				$("#download").submit();
			},
			error:function(){
				plm.showMessage({title:'错误提示', message:"提交失败！", icon:'1'});
			}
		});
	}
	function initPage(){
		window.location.href = "<%=contextPath%>/ddm/distributesendorder/listDistributeInsideSearch.jsp?sendType=<%=sendType%>";
	}

	
	// 返回检索条件画面。
	function returnSearchPage() {
		window.location.href = "<%=contextPath%>/ddm/distributesendorder/listDistributeInsideSearch.jsp?sendType=<%=sendType%>";
	}
	
	
	//修改项目操作
	function updateDistributePaperTask(){
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if (data == null || data.length != 1) {
			plm. showMessage({title:'提示', message:"请至少选择1个操作对象!", icon:'3'});
			return;
		}
		
		var oid = data[0].OID;
		var url = "<%=contextPath%>/ddm/distributepapertask/updateDistributePaperTask.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=" + oid;
		plm.openWindow(url,800,600);	
	}
</script>
