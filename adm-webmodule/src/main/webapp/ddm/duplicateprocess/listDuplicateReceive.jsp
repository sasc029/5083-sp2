<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.core.system.access.operate.Operate"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%@include file="/ddm/public/ddm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String spot = "ListDistributePaperTasks";
	String gridId = "distributePaperTaskGrid";
	String gridTitle = "复制加工任务";
	String toolbarId = "ddm.duplicatereceive.list.toolbar";
	String submitUrl = contextPath + "/ddm/distribute/distributeTaskHandle!updateDistributeTask.action"; 
%>
<html>
	<head>
		<title><%=gridTitle%></title>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
		<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
	</head>
	<body>
		<form id="mainForm" name="mainForm" method="POST" >
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
		var url = "<%=contextPath%>/ddm/distribute/duplicateProcessHandle!initDuplicateProcessInfoList.action?<%=KeyS.OID%>="+value;
		ddm.tools.openWindow(url,900,600,"distributeOrderOpen");
	}
	
	//提交到加工
	function submitToProcess(){
		plm.startWait();
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
		checkDuplicateInfo(oids,"ddmValidation");
	}
	
	function submitToProcessCallBack(oids){
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
				plm.showMessage({title:'错误提示', message:"提交失败！", icon:'1'});
			}
		});
	}

	//验证要提交的任务是否录入复制加工信息
	function checkDuplicateInfo(oids,callback){
		var operateUrl = contextPath + "/ddm/distribute/duplicateProcessHandle!checkDuplicateInfo.action";
		$.ajax({
			url:operateUrl,
			type:"post",
			dataType:"json",
			data:"OIDS="+oids,
			success:function(result){
				if(result.SUCCESS != null && result.SUCCESS == "false"){
					plm.endWait();
					plm.showAjaxError(result);
					return;
				} else if(result.SUCCESS != null && result.SUCCESS !="true"){
					plm.endWait();
					plm. showMessage({title:'提示', message:"编号是"+result.SUCCESS+"的任务录入的复制加工信息不全，不能提交！", icon:'3'});
					return;
				}
				callback = callback + "('" + oids + "','" + "<%=Operate.PROMOTE%>" + "'," + "'submitToProcessCallBack')";
				eval(callback);
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"获取提交的任务是否录入复制加工信息失败！", icon:'1'});
			}
		});    	
	}
	<%-- function addDuplicateProcessor(){
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if (data == null || data.length == 0) {
			plm. showMessage({title:'提示', message:"请选择任务!", icon:'3'});
			return;
		}
		
		var oid = data[0].OID;
		var url = "<%=contextPath%>/ddm/distribute/duplicateProcessHandle!addDuplicateProcessor.action?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=" + oid;
		plm.openWindow(url,800,600);	
	} --%>
	
	//选择复制加工人员
	function addDuplicateProcessor(){
		plm.startWait();
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if(data.length == 0){
			plm.endWait();
			plm.showMessage({title:'错误提示', message:"请至少选择一条操作数据", icon:'1'});
			return;
		}
		
		var oids = "";
		for(var i=0;i<data.length;i++){
			oids += data[i].OID + ",";
		}
		oids = oids.substring(0, oids.length - 1);
		var url = "<%=contextPath%>/ddm/distribute/duplicateProcessHandle!initDuplicateProcessList.action?<%=KeyS.OIDS%>="+oids;
		plm.endWait();
		var result = plm.openWindow(url,900,600,"distributePaperTaskOpen");	
	}
	
	//数据刷新方法
	function reload(){
		window.location.reload(true);
		//parent.frames['leftFrame'].refreshTree();
	}

	//刷新dategrid
	function tableReload(text){
		plm.showMessage({title:'系统提示', message:text, icon:'2'});
		reload();
	}
</script>
