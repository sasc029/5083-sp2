<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%@include file="/ddm/public/ddm.jsp" %>
<%@page import="com.bjsasc.plm.core.system.access.operate.Operate"%>

<%
	String contextPath = request.getContextPath();
	String spot = "ListDistributePaperTasks";
	String gridId = "distributePaperTaskGrid";
	String gridTitle = "���Ƽӹ�����";
	String toolbarId = "ddm.duplicatenoreceive.list.toolbar";
	String deleteUrl = contextPath + "/ddm/distribute/duplicateProcessHandle!getAllDistributePaperTask.action";
	String receiveUrl = contextPath + "/ddm/distribute/distributeTaskHandle!updateDistributeTask.action";
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

	var container = {};
	
	function onLoadSuccess() {
		onLoadSuccess_ddm("<%=gridId%>");
	}
	
	function doCustomizeMethod_id(value) {
		var url = '<%=contextPath%>/ddm/distribute/distributePaperTaskHandle!getDistributePaperTaskList.action?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + value;
		ddm.tools.openWindow(url,900,600,"distributeOrderOpen");
	}
	
	function doCustomizeMethod_operate(value) {
		var params = "dialogHeight:600px;dialogWidth:900px;center:Yes;help:no;resizable:NO;status:no;";
		var url = '<%=contextPath%>/ddm/distribute/distributePaperTaskHandle!paperTaskExcel.action?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + value;
		var result = plm.openWindow(url,900,600,"distributePaperTaskOpen");	
	}
	
	//����
	function receiveTask(){
		plm.startWait();
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if (data == null || data.length == 0) {
			plm.endWait();
			plm. showMessage({title:'��ʾ', message:"������ѡ��1����������!", icon:'3'});
			return;
		}
		var oids = "";
		for(var i=0;i<data.length;i++){
			oids += data[i].OID + ",";
		}
		oids = oids.substring(0, oids.length - 1);
		ddmValidation(oids,"<%=Operate.PROMOTE%>","receiveTaskCallBack");
	}
	
	function receiveTaskCallBack(oids){
		$.ajax({
			url:"<%=receiveUrl%>",
			type:"post",
			dataType:"json",
			data:"<%=KeyS.OIDS%>="+oids+"&flag=promote",
			success:function(result){
				plm.endWait();
				if(result.SUCCESS != null &&result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				window.location.reload(true);
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'������ʾ', message:"����ʧ�ܣ�", icon:'1'});
			}
		});
	}
	
	//�˻ص��ӹ�����
	function rollbackToProcess(){
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if (data == null || data.length == 0) {
			plm. showMessage({title:'��ʾ', message:"������ѡ��1����������!", icon:'3'});
			return;
		}
		var oids = "";
		for(var i=0;i<data.length;i++){
			oids += data[i].OID + ",";
		}
		oids = oids.substring(0, oids.length - 1);
		ddmValidation(oids,"<%=Operate.PROMOTE%>","rollbackToProcessCallBack");
	}
	
	function rollbackToProcessCallBack(oids){
		var url = '<%=contextPath%>/ddm/distributetask/distributeReturnReason.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OIDS%>=' + oids+'&flag=reject';
	    var retObj = ddm.tools.showModalDialog(url);	
		if(retObj){
			doBindRole(retObj, oids);
		} 
	}
	
    function doBindRole(returnReason, oids){
    	plm.startWait();
		$.ajax({
			url:"<%=receiveUrl%>",
			type:"post",
			dataType:"text",
			data:"<%=KeyS.OIDS%>="+oids+"&flag=reject&returnReason="+returnReason,
			success:function(result){
				plm.endWait();
				window.location.reload(true);
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'������ʾ', message:"�ύʧ�ܣ�", icon:'1'});
			}
		});
    }
	
	//����ˢ�·���
	function reload(){
		window.location.reload(true);
		parent.frames['leftFrame'].refreshTree();
	}

	//ˢ��dategrid
	function tableReload(text){
		plm.showMessage({title:'ϵͳ��ʾ', message:text, icon:'2'});
		reload();
	}
</script>
