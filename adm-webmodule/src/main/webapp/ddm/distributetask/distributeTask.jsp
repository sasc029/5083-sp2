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
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.core.system.access.operate.Operate"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%@include file="/ddm/public/ddm.jsp" %>

<%	
    String toolbarId = "ddm.distributeExecttask.list.toolbar";
	String contextPath = request.getContextPath();
	String spot = "ListDistributeElecTasks_notSigned";
	String gridTitle = "δǩ�������б�";
	//String flag = request.getParameter("flag");
	String type = request.getParameter("type");
	if ("notSigned".equals(request.getParameter("type"))) {
		gridTitle = "δǩ�������б�";
		toolbarId="ddm.distributeelectasknosigned.list.toolbar";
		spot = "ListDistributeElecTasks_notSigned";
		
	} else if ("signed".equals(request.getParameter("type"))) {
		gridTitle = "��ǩ�������б�";
		toolbarId = "ddm.distributeExecttask.list.toolbar";
		spot = "ListDistributeElecTasks_signed";
	} else if ("completed".equals(request.getParameter("type"))) {
		toolbarId="";
		gridTitle = "����������б�";
		spot = "ListDistributeElecTasks_completed";
	} else if ("refuseSigned".equals(request.getParameter("type"))) {
		gridTitle = "�Ѿ�ǩ�����б�";
		toolbarId="";
		spot = "ListDistributeElecTasks_refuseSigned";
	}
	String gridId = "distributeElecTaskGrid";
	String signedUrl = contextPath + "/ddm/distribute/distributeTaskHandle!updateDistributeElecTask.action";
	String refuseSignedUrl = contextPath + "/ddm/distribute/distributeTaskHandle!refuseSigned.action";
	String updateURL = contextPath + "/ddm/distribute/distributeTaskHandle!updateDistributeElecTaskLife.action";
%>
<html>
	<head>
		<title><%=gridTitle%></title>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
		<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
		<script type="text/javascript"	src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
	</head>
	<body>
		<form id="mainForm" name="mainForm" method="POST">
			<table height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0">
				<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
					<jsp:param name="spot" value="<%=spot%>"/>
					<jsp:param name="gridId" value="<%=gridId%>"/>
					<jsp:param name="toolbar_modelId" value="<%=toolbarId%>" />
					<jsp:param name="gridTitle" value=""/>
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
	
	// ����ǩ�� signed
	function doCustomizeMethod_operates_0(value) {
		plm.startWait();
		//ddmValidation(value,"<%=Operate.PROMOTE%>","singnedReceive");
		singnedReceive(value);
	}
	
	function singnedReceive(value){
		$.ajax({
			url:"<%=signedUrl%>",
			type:"post",
			dataType:"json",
			data:"<%=KeyS.OIDS%>="+value+"&operate=signed",
			success:function(result){
				plm.endWait();
				if(result.SUCCESS != null && result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				reload();
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'������ʾ', message:"����ʧ�ܣ�", icon:'1'});
			}
		});
	}

	
	// ����ǩ�� signed
	function submit(){
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
		//ddmValidation(oids,"<%=Operate.PROMOTE%>","submitCallBack");
		submitCallBack(oids);
	}
	
	function submitCallBack(oids) {
		$.ajax({
			url:"<%=signedUrl%>",
			type:"post",
			dataType:"json",
			data:"<%=KeyS.OIDS%>="+oids+"&operate=signed",
			success:function(result){
				plm.endWait();
				if(result.SUCCESS != null && result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				reload();
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'������ʾ', message:"ǩ��ʧ�ܣ�", icon:'1'});
			}
		});
	}
	
	// ������ǩ refuse_signed
	function doCustomizeMethod_operates_1(value) {
		plm.startWait();
		//ddmValidation(value,"<%=Operate.PROMOTE%>","refuse_signed");
		refuse_signed(value);
	}
	
	function refuse_signed(value){
		var url = '<%=contextPath%>/ddm/distributetask/distributeReturnReason.jsp?flag=elecTaskRefuse';
		plm.endWait();
	    var retObj = ddm.tools.showModalDialog(url);	
		if(retObj){
			doReturn(retObj, value);	
		}
	}
	
	//������ǩ
	function rollback(){
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
		//ddmValidation(oids,"<%=Operate.PROMOTE%>","rollbackCallBack");
		rollbackCallBack(oids);
	}
	
	function rollbackCallBack(oids) {
		var url = '<%=contextPath%>/ddm/distributetask/distributeReturnReason.jsp?flag=elecTaskRefuse';
		plm.endWait();
		var retObj = ddm.tools.showModalDialog(url);	
		if(retObj){
			doReturn(retObj, oids);	
		}
	}
	
	// ��ǩ refuse_signed �ص�����
	function doReturn(retObj, oids) {
		plm.startWait();
		$.ajax({
			url:"<%=signedUrl%>",
			type:"post",
			dataType:"json",
			data:"<%=KeyS.OIDS%>="+oids+"&operate=refuse&returnReason="+retObj,
			success:function(result){
				plm.endWait();
				if(result.SUCCESS != null && result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				reload();
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'������ʾ', message:"��ǩʧ�ܣ�", icon:'1'});
			}
		});
	}
	
	// ת��
	function doCustomizeMethod_operate(value){
		var params = "dialogHeight:900px;dialogWidth:900px;center:Yes;help:no;resizable:NO;status:no;";
		<%-- var url = '<%=contextPath%>/ddm/distribute/distributeElecTaskHandle!getDistributeForwardElecTaskDetail.action?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OIDS%>=' + value; --%>
		var url = '<%=contextPath%>/ddm/distributeelectask/distributeElecForwardProperty_tab.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + value;
		ddm.tools.openWindow(url,900,400,"distributeElecTaskOpen");
	}
	
	
  //ˢ��dategrid
	function reload(){
		var table = pt.ui.get("<%=gridId%>");
		table.reload();
	}
	
	//��ǩ�յ�����������
	function submitToDistribute(){
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
		//ddmValidation(oids,"<%=Operate.PROMOTE%>","submitToDistributeCallBack");
		submitToDistributeCallBack(oids);
	}
	
	function submitToDistributeCallBack(oids){
		$.ajax({
			url:"<%=updateURL %>",
			type:"post",
			dataType:"json",
			data:"<%=KeyS.OIDS%>="+oids,
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
				plm.showMessage({title:'������ʾ', message:"�ύʧ��", icon:'1'});
			}
		});
		
	}

	function reloadOrder(){
		var tabStr = "";
		if("notSigned" == "<%=type%>"){
		tabStr = contextPath + "/ddm/distribute/distributeTaskHandle!getAllDistributeElecTaskNotSigned.action";
		}else if("signed" == "<%=type%>"){
			tabStr = contextPath + "/ddm/distribute/distributeTaskHandle!getAllDistributeElecTaskSigned.action";
		}else if("completed" == "<%=type%>"){
			tabStr = contextPath + "/ddm/distribute/distributeTaskHandle!getAllDistributeElecTaskCompleted.action";
		}else if("signed" == "<%=type%>"){
			tabStr = contextPath + "/ddm/distribute/distributeTaskHandle!getAllDistributeElecTaskRefuseSigned.action";
		}
		if(tabStr != ""){
			window.location.href = tabStr;
		}
	}
</script>
