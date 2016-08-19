<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.plm.core.system.access.operate.Operate"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp"%>
<%@include file="/ddm/public/ddm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String spot = "ListDistributePaperTaskReturn";
	String gridId = "distributePaperTaskGrid";
	String gridTitle = "被退回任务";
	String toolbarId = "ddm.distributepapertask.list.toolbar";
	String deleteUrl = contextPath + "/ddm/distribute/distributePaperTaskHandle!deleteDistributePaperTask.action";
	String submitUrl = contextPath + "/ddm/distribute/distributeTaskHandle!updateDistributeTask.action";
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
	
	function doCustomizeMethod_id(value) {
		var url = '<%=contextPath%>/ddm/distribute/distributePaperTaskHandle!getDistributePaperTaskList.action?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + value;
		ddm.tools.openWindow(url,900,600,"distributeOrderOpen");
	}
	
	function doCustomizeMethod_operate(value) {
		var url = '<%=contextPath%>/ddm/distribute/distributePaperTaskHandle!paperTaskExcel.action?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + value;
		$("#download").attr("action", url);
		$("#download").submit();
	}
	
	function doCustomizeMethod_returnCount(value) {
		var url = '<%=contextPath%>/ddm/distribute/distributePaperTaskHandle!getDistributePaperTaskReturnDetail.action?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + value +'&flag=duplicateReturn';
		ddm.tools.openWindow(url,900,600,"distributeOrderOpen");
	}
	
	//提交到复制加工
	function submitDistributePaperTask(){
		plm.startWait();
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if (data == null || data.length != 1) {
			plm.endWait();
			plm. showMessage({title:'提示', message:"请至少选择1个操作对象!", icon:'3'});
			return;
		}
		var oids = "";
		for(var i=0;i<data.length;i++){
			oids += data[i].OID + ",";
		}
		oids = oids.substring(0, oids.length - 1);
		ddmValidation(oids,"<%=Operate.PROMOTE%>","submitDistributePaperTaskCallBack");
	}
	
	function submitDistributePaperTaskCallBack(oids){
		$.ajax({
			url:"<%=submitUrl%>",
			type:"post",
			dataType:"json",
			data:"<%=KeyS.OIDS%>="+oids+"&flag=promote",
			success:function(result){
				plm.endWait();
				if(result.SUCCESS != null && result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				window.location.reload(true);
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"提交失败！", icon:'1'});
			}
		});
	}
	
	//加工单任务回退
	function rollbackTask(){
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if (data == null || data.length == 0) {
			plm.endWait();
			plm. showMessage({title:'提示', message:"请至少选择1个操作对象!", icon:'3'});
			return;
		}
		var oids = "";
		for(var i=0;i<data.length;i++){
			oids += data[i].OID + ",";
		}
		oids = oids.substring(0, oids.length - 1);
		ddmValidation(oids,"<%=Operate.PROMOTE%>","rollbackTaskCallBack");
	}
	
	function rollbackTaskCallBack(oids){
		var url = '<%=contextPath%>/ddm/distributetask/distributeReturnReason.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OIDS%>=' + oids + '&type=returnTask&flag=reject';
	    var retObj = ddm.tools.showModalDialog(url);	
		if(retObj){
			doBindRole(retObj, oids);
		} 
	}
	
    function doBindRole(returnReason, oids){
    	plm.startWait();
		$.ajax({
			url:"<%=submitUrl%>",
			type:"post",
			dataType:"text",
			data:"<%=KeyS.OIDS%>="+oids+"&flag=reject&returnReason="+returnReason+"&type=returnTask",
			success:function(result){
				plm.endWait();
				window.location.reload(true);
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"提交失败！", icon:'1'});
			}
		});
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
