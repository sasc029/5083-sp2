<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@ taglib uri="/WEB-INF/framework-ext.tld" prefix="ext" %> 
<%@include file="/plm/plm.jsp" %>
<%
	String contextPath = request.getContextPath();
    String spot = "ListDistributeTaskPropertys";
    String gridId = "DistributeObject";
    String gridTitle = "分发数据";
	String gridUrl = "";
	String paperTaskOid = RequestUtil.getParamOid(request);
	String toolbarId="ddm.distributeobject.list.toolbar2";
	//String toolbarId="ddm.distributeobject.list.toolbar3";
%>
<html>
	<head>
		<title><%=gridTitle%></title>
		<script type="text/javascript"	src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
		<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
	</head>
	<body>
	<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
					<jsp:param name="spot" value="<%=spot%>"/>
					<jsp:param name="gridId" value="<%=gridId%>"/>
					<jsp:param name="gridTitle" value=""/>
					<jsp:param name="toolbar_modelId" value="<%=toolbarId%>"/>
					<jsp:param name="onLoadSuccess" value="onLoadSuccess()"/>
					<jsp:param name="operate_container" value="container"/>
					<jsp:param name="operate_mainObjectOID" value="<%=paperTaskOid%>" />
				</jsp:include>
<form action="" method="post" name="infoForm" target="hideFrame" >
		<input type="hidden" name="oids"/>
		<input type="hidden" name="CALLBACK" value="afterDownload"/>
</form>
<iframe id="hideFrame" name="hideFrame" src="" width="0px" height="0px"></iframe>
	</body>
</html>
<script type="text/javascript">
	var container={};
    function onLoadSuccess() {
	   onLoadSuccess_ddm("<%=gridId%>");
     }
    
	function doCustomizeMethod_operate(value) {
		var url = '<%=contextPath%>/ddm/distributeobject/listDistributeFiles.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + value;
		ddm.tools.openWindow(url,900,600,"distributeObjectOpen");
		
	}
	//下载选中记录的文件
	function downloadSelectFiles(){
		var grid = pt.ui.get("<%=gridId%>");
		var select  = grid.getSelections();
		if(select.length<1){
			plm.showMessage({"title":"提示","message":"请至少选择1条记录进行操作！",icon:"3"});
			return ;
		}
		var oids = new Array();
		for(var i=0; i<select.length; i++){
			var oid = select[i].OID;
			var obj = {
					oid:select[i].OID
			};
			oids.push(obj);
		}
		var url = "<%=request.getContextPath() %>/ddm/distributeobject/downloadFilesByOrder.jsp";
		if(oids.length > 0) {
			document.infoForm.action = url;
			document.infoForm.oids.value = pt.ui.JSON.encode(oids);
			document.infoForm.submit();
		}
	}
	
	//标记打印按钮
	function signPrint(){
		var urlPrint = "<%=contextPath%>/ddm/distribute/distributeSendOrderHandle!InsertDistributeObjectPrint.action";
		var grid = pt.ui.get("<%=gridId%>");
		var select  = grid.getSelections();
		if(select.length<1){
			plm.showMessage({"title":"提示","message":"请至少选择1条记录进行操作！",icon:"3"});
			return ;
		}
		var oids = new Array();
		for(var i=0; i<select.length; i++){
			var oid = select[i].OID;
			var obj = {
				oid:select[i].OID
			};
			oids.push(obj);
		}
		oids=pt.ui.JSON.encode(oids);
		 $.ajax({
			url:urlPrint,
			type:"post",
			dataType:"json",
			data:"oids="+oids,
			success:function(result){
				plm.endWait();
				var url1 = "<%=contextPath%>/ddm/distribute/distributeSendOrderHandle!getDistributePaperTaskList.action";
				window.location.href = url1;
				if(result.SUCCESS != null && result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"操作失败！", icon:'1'});
			}
		}); 
	
	}
	
	function afterDownload(successObjJson, faliedObjJson){
		if(successObjJson == null && faliedObjJson == null){
			plm.showMessage({title:'提示', message:'未找到要下载的文件!', icon:'1'});
			//plm.endWait();
			return;
		}
		
		var faliedObjArray = eval(faliedObjJson), names = "";
		if(faliedObjArray.length > 0){
			for(index = 0; index < faliedObjArray.length; index++){
				names += "[" + faliedObjArray[index].fileName + "]";
			}
			plm.showMessage({title:'提示', message:names + '下载失败!', icon:'3'});
		}else{
			plm.showMessage({title:'提示', message:'下载成功!', icon:'2'});
		}
		//plm.endWait();
	}
</script>