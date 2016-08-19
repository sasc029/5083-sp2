<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.core.system.access.operate.Operate"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp"%>
<%@include file="/ddm/public/ddm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String spot = "ListDistributePaperTasks";
	String gridId = "distributePaperTaskGrid";
	String gridTitle = "�ӹ�������";
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
		<%-- var url = '<%=contextPath%>/ddm/distributepapertask/distributePaperTask_tab.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + value; --%>
		var url = '<%=contextPath%>/ddm/distribute/distributePaperTaskHandle!getDistributePaperTaskList.action?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + value;
		ddm.tools.openWindow(url,900,600,"distributeOrderOpen");
	}
	
	function doCustomizeMethod_operate(value) {
		var url = '<%=contextPath%>/ddm/distribute/distributePaperTaskHandle!paperTaskExcel.action?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + value;
		$("#download").attr("action", url);
		$("#download").submit();
	}
	
	//�ύ�����Ƽӹ�
	function submitDistributePaperTask(){
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
		ddmValidation(oids,"<%=Operate.PROMOTE%>","submitDistributePaperTaskCallBack");
	}
	//ˢ�´�����������
	var nodeId="com.bjsasc.ddm.distribute.distributePaperTask";
	plm.refreshTree(nodeId);
	function submitDistributePaperTaskCallBack(oids){
		$.ajax({
			url:"<%=submitUrl%>",
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
				plm.showMessage({title:'������ʾ', message:"�ύʧ�ܣ�", icon:'1'});
			}
		});
	}

	//�ӹ����������
	function rollbackTask(){
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
				plm.showMessage({title:'������ʾ', message:"����ʧ�ܣ�", icon:'1'});
			}
		});
    }
	
	//�޸���Ŀ����
	function updateDistributePaperTask(){
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if (data == null || data.length != 1) {
			plm. showMessage({title:'��ʾ', message:"������ѡ��1����������!", icon:'3'});
			return;
		}
		
		var oid = data[0].OID;
		var url = "<%=contextPath%>/ddm/distributepapertask/updateDistributePaperTask.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=" + oid;
		plm.openWindow(url,800,600);	
	}
	
	//ɾ��ֽ������
	function deleteDistributePaperTask(){
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if(data.length == 0){
			plm.showMessage({title:'������ʾ', message:"������ѡ��һ����������", icon:'1'});
			return;
		}
		if (!plm.confirm("����,ɾ�����ŵ����ܻ�ɾ���÷��ŵ�������ӵķַ�����,�Ƿ����ɾ��?")) {
			return;
		}
		var oids = "";
		for(var i=0;i<data.length;i++){
			oids += data[i].OID + ",";
		}
		oids = oids.substring(0, oids.length - 1);
		plm.startWait();
		$.ajax({
			url:"<%=deleteUrl%>",
			type:"post",
			dataType:"json",
			data:"<%=KeyS.OIDS%>="+oids,
			success:function(result){
				plm.endWait();
				if(result.SUCCESS != null &&result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				tableReload("ɾ���ɹ�");
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'������ʾ', message:"ɾ������ʧ�ܣ�", icon:'1'});
			}
		});
	}

	//ˢ��dategrid
	function tableReload(text){
		plm.showMessage({title:'ϵͳ��ʾ', message:text, icon:'2'});
		reload();
	}
	
	// ���¼���
	function reload(text){
		if (text != undefined && text != "") {
			plm.showMessage({title:'ϵͳ��ʾ', message:text, icon:'2'});
		}
		var table = pt.ui.get("<%=gridId%>");
		table.reload();
	}
</script>
