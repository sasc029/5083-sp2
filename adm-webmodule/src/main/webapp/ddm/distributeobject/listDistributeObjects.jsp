<%@page import="com.bjsasc.plm.util.RequestUtil"%>
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
	String contextPath = request.getContextPath();
	String oid = (String) session.getAttribute(ConstUtil.DISTRIBUTE_ORDER_OID);
	if(oid == null){
		oid = RequestUtil.getParamOid(request);
	}
	String classId = DistributeObject.CLASSID;
	String spot = "ListDistributeObjects";
	String gridId = "distributeObjectGrid";
	String gridTitle = "分发数据";
	String toolbarId = "";
	String lc_new=ConstUtil.LC_NEW.getName();//获得生命周期是新建
	String lc_ddm_approver_terminate=ConstUtil.LC_DDM_APPROVE_TERMINATE.getName();//获得生命周期是分发审批被终止
	String lc_ddm_approver_reject=ConstUtil.LC_DDM_APPROVE_REJECT.getName();//获得生命周期是分发审批被退回
	String updateUrl = contextPath + "/ddm/distributeobject/updateDistributeObject.jsp?"+KeyS.CALLBACK+"=reload&"+KeyS.OID+"=";
	String deleteUrl = contextPath + "/ddm/distribute/distributeObjectHandle!deleteDistributeObject.action";
	String addUrl = contextPath + "/ddm/distribute/distributeObjectHandle!addDistributeObject.action";
	
	Persistable obj = Helper.getPersistService().getObject(oid);
	DistributeOrder disOrder = (DistributeOrder)obj;
	
	String contextOid = Helper.getOid(disOrder.getContextInfo().getContextRef());
	String approveOid = "";
	  if (lc_new.equals(disOrder.getLifeCycleInfo().getStateName())
		  ||lc_ddm_approver_terminate.equals(disOrder.getLifeCycleInfo().getStateName())
		  ||lc_ddm_approver_reject.equals(disOrder.getLifeCycleInfo().getStateName())){
		  toolbarId ="ddm.distributeobject.list.toolbar";   
	  }else{
		  toolbarId ="ddm.distributeobject.list.toolbar2";  
	  }
%>
<html>
	<head>
		<title><%=gridTitle%></title>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
	</head>
	<body>
	<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
					<jsp:param name="spot" value="<%=spot%>"/>
					<jsp:param name="gridId" value="<%=gridId%>"/>
					<jsp:param name="gridTitle" value=""/>
					<jsp:param name="toolbar_modelId" value="<%=toolbarId%>"/>
					<jsp:param name="onLoadSuccess" value="onLoadSuccess()"/>
					<jsp:param name="operate_container" value="container"/>
					<jsp:param name="operate_mainObjectOID" value="<%=oid%>" />
				</jsp:include>
<form action="" method="post" name="infoForm" target="hideFrame" >
		<input type="hidden" name="oids"/>
		<input type="hidden" name="CALLBACK" value="afterDownload"/>
</form>
<iframe id="hideFrame" name="hideFrame" src="" width="0px" height="0px"></iframe>

	</body>
</html>
<script type="text/javascript">
    function onLoadSuccess() {
	  onLoadSuccess_ddm("<%=gridId%>");
    }
	var container = {OID:"<%=contextOid%>",PARAM_DistributeOrderOid:"<%=oid%>"};
	
	var recordsData;

	//修改项目操作
	function updateDistributeObject(){
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if (data == null || data.length != 1) {
			plm. showMessage({title:'提示', message:"请至少选择1个操作对象!", icon:'3'});
			return;
		}
		
		var oid = data[0].OID;
		var url = "<%=updateUrl%>" + oid;
		var retObj = pt.ui.showModalDialog(url,"","normal");
		if(retObj){
			reload();
		}
	}
	
	//删除问题
	function deleteDistributeObject(){
		plm.startWait();
		ddmValidation("<%=oid%>","plm_modify_content","deleteDistributeObjectCallBack");
	}
	
	function deleteDistributeObjectCallBack(orderOids){
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if(data.length == 0){
			plm.endWait();
			plm.showMessage({title:'错误提示', message:"请至少选择一条操作数据", icon:'1'});
			return;
		}
		if (!plm.confirm("警告,删除数据会删除该数据上已添加的分发信息,是否继续删除?")) {
			plm.endWait();
			return;
		}
		var oids = "";
		for(var i=0;i<data.length;i++){
			oids += data[i]["DISTRIBUTEORDEROBJECTLINKOID"] + ",";
		}
		oids = oids.substring(0, oids.length - 1);
		
		$.ajax({
			url:"<%=deleteUrl%>",
			type:"post",
			dataType : "json",
			data:"<%=KeyS.OIDS%>="+oids,
			success:function(result){
				plm.endWait();
				if (result.SUCCESS != null && result.SUCCESS =="false"){
					plm.showAjaxError(result);
				} else {
					reload();
				}
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"删除分发数据操作失败！", icon:'1'});
			}
		});
	}
	
	function gatherAffectobjects(objArray) {
		var addData="";
		var errorMsg=[];
		
		if(objArray.length == 0){
			plm.showMessage({title:'提示', message:'所选取的数据为空！', icon:'3'});
 			return;
		}

		for(var i = 0; i < objArray.length; i++) {
	 		var objId = objArray[i]["CLASSID"] + ":" + objArray[i]["INNERID"];
	 		var flag = isTransferControlling(objArray[i]);
	 		if(flag != "yes"){
	 			var name = objArray[i]["NAME"].split(">")[1].slice(0,-3);
	 			var tips = "所选【"+name+"】"+flag ;
	 			errorMsg.push(tips);
	 			continue;
	 		}
	 		addData+= objId + ",";
	 	}

	 	if(errorMsg.length > 0){
		 	var msg = "";
	 		msg = errorMsg.join("! ");
	 		//plm.showMessage({title:'提示', message:msg, icon:'3'});
	 		plm.showMessage({title:'提示', message:"您所选择的部分数据不满足条件，添加失败！", icon:'3'});
	 	}

		addData = addData.substring(0, addData.length - 1);
		plm.startWait();
		$.ajax({
			url:"<%=addUrl%>",
			type:"post",
			dataType:"json",
			data:"<%=KeyS.OIDS%>="+addData,
			success:function(result){
				plm.endWait();
				if (result.SUCCESS != null && result.SUCCESS =="false"){
					plm.showAjaxError(result);
				} else {
					reload();
				}

			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"添加分发数据操作失败！", icon:'1'});
			}
		});
	}
	//判断是否可转换且受控中。
	function isTransferControlling(obj){
		var innerId = obj["INNERID"];
		var classId = obj["CLASSID"];
		var url = "<%=contextPath%>/ddm/distribute/distributeObjectHandle!isTransferControlling.action?innerId="+innerId+"&classId="+classId;
		var returnValue = "yes";
		$.ajax({
	        type: "POST",
	        async: false,//是否异步
	        url : url,
	        dataType : "json",
	        success: function(result) {
	        	returnValue = result.FLAG;
			}
		});
		return returnValue;
	}
	//刷新dategrid
	function reload(){
		var table = pt.ui.get("<%=gridId%>");
		table.reload();
	}
	function search(){
		plm.startWait();
		ddmValidation("<%=oid%>","plm_modify_content","searchCallBack");
	}
	
	function searchCallBack(oid){
		var params="CALLBACK=gatherAffectobjects&single=false";
		var url = commonSearchPath+"?"+params;
		plm.endWait();
		window.showModalDialog(url,self,"dialogHeight: 630px; dialogWidth: 800px;  center: Yes; help: Yes; resizable: Yes; status: no;");
	}
	
	Plm.prototype.distributeObjectCollector = function(records, callback,container){
		plm.startWait();
		recordsData = records;
		ddmValidation("<%=oid%>","plm_modify_content","distributeObjectCollectorCallBack");
		
	}
	
	function distributeObjectCollectorCallBack(oid){
		var url = contextPath+'/ddm/distributeobject/distributeObjectCollector.jsp?<%=KeyS.CONTAINER_OID%>=<%=contextOid%>&PARAM_DistributeOrderOid=<%=oid%>&CALLBACK=gatherAffectobjects';
		var windowid = "CREATEDOCUMENT"+ "_" + new Date().getTime();
		plm.endWait();
		//打开窗口
		plm.openWindow(contextPath+"/plm/load.html", 900, 600,windowid);
		//在前台中执行
		plm.openByPost(url, pt.ui.JSON.encode(recordsData), windowid);
	}
	
	// 设置主对象
	function setMaster() {
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if(data.length != 1){
			plm.showMessage({title:'错误提示', message:"请选择一条操作数据", icon:'1'});
			return;
		}
		$.ajax({
			url:"<%=contextPath%>/ddm/distributeobject/distributeObjectAction.jsp?op=setMaster",
			type:"post",
			dataType : "json",
			data:{distributeOrderOid:"<%=oid%>",distributeObjectOid:data[0].OID},
			success:function(result){
				plm.endWait();
				if (result.SUCCESS != null && result.SUCCESS =="false"){
					plm.showAjaxError(result);
				} else {
					reload();
				}
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"删除分发数据操作失败！", icon:'1'});
			}
		});		
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
