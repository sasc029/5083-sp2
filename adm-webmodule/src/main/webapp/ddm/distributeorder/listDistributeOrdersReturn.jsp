<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.core.system.access.operate.Operate"%>

<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%@include file="/ddm/public/ddm.jsp" %>
<%
	String contextPath = request.getContextPath();
	String classId = DistributeOrder.CLASSID;
	String spot = "ListDistributeReturn";
	String gridId = "distributeOrderGrid";
	String gridTitle = "发放单回退列表";
	String toolbarId = "ddm.distributeordersReturn.list.toolbar";
	String gridUrl = contextPath + "/ddm/distribute/distributeOrderHandle!getAllDistributeOrder.action";
	String deleteUrl = contextPath + "/ddm/distribute/distributeOrderHandle!deleteDistributeOrder.action";
	String addPrincipalUrl = contextPath + "/ddm/distribute/distributeOrderHandle!addPrincipals.action";
%>
<html>
	<head>
		<title><%=gridTitle%></title>
		<script type="text/javascript"	src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
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
		    <input type="hidden" name="oids" id="oids"/>
		    <input type="hidden" name="type" id="type"/>
		    <input type="hidden" name="iids" id="iids"/>
		    <input type="hidden" name="disMediaTypes" id="disMediaTypes"/>
		    <input type="hidden" name="disInfoNums" id="disInfoNums"/>
		    <input type="hidden" name="notes" id="notes"/>
		</form>
	</body>
</html>
<script type="text/javascript">

    function onLoadSuccess() {
	  onLoadSuccess_ddm("<%=gridId%>");
  }
	var container = {};
	
	<%-- function doCustomizeMethod_id(value) {
		var url = '<%=contextPath%>/ddm/distributeorder/distributeOrder_tab.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + value;
		ddm.tools.openWindow(url,900,600,"distributeOrderOpen");
	} --%>

	function doCustomizeMethod_returnCount(value) {
		var url = '<%=contextPath%>/ddm/distribute/distributeOrderHandle!getDistributeOrderReturnDetail.action?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + value;
		ddm.tools.openWindow(url,900,600,"distributeOrderOpen");
	}

	//修改项目操作
	function updateDistributeOrder(){
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if (data == null || data.length != 1) {
			plm. showMessage({title:'提示', message:"请至少选择1个操作对象!", icon:'3'});
			return;
		}
		
		var oid = data[0].OID;
		var url = "<%=contextPath%>/ddm/distributeorder/updateDistributeOrder.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=" + oid;
		plm.openWindow(url,800,600);	
	}
	
	//删除问题
	function deleteDistributeOrder(){
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if(data.length == 0){
			plm.endWait();
			plm.showMessage({title:'错误提示', message:"请至少选择一条操作数据", icon:'1'});
			return;
		}
		if (!plm.confirm("警告,删除发放单会删除该发放单上已添加的分发信息以及相应发放数据,是否继续删除?")) {
			plm.endWait();
			return;
		}
		var oids = "";
		for(var i=0;i<data.length;i++){
			oids += data[i].OID + ",";
		}
		oids = oids.substring(0, oids.length - 1);
		ddmValidation(oids,"<%=Operate.DELETE%>","deleteDistributeOrderCallBack");
	}
	
	function deleteDistributeOrderCallBack(oids){
		var url = "<%=contextPath%>/ddm/distribute/distributeOrderHandle!deleteDistributeOrder.action";
		plm.startWait();
		$.ajax({
			url:url,
			type:"post",
			dataType:"json",
			data:"<%=KeyS.OIDS%>="+oids,
			success:function(result){
				plm.endWait();
				if(result.SUCCESS != null && result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}else{
					if(result.ERROROIDS != ""){
						var strErrMsg = "";
						var table = pt.ui.get("<%=gridId%>");
						var data = table.getSelections();
						var errorOids = result.ERROROIDS.split(",");
						for(var j=0;j<errorOids.length;j++){
							for(var i=0;i<data.length;i++){
								if (data[i].OID == errorOids[j]) {
									strErrMsg+=data[i]["NAME"].substring(data[i]["NAME"].indexOf("le='{")+5,data[i]["NAME"].indexOf("}'>")) + ",";
									continue;
								}
							}
						}
						if (strErrMsg!=""){
							alert("当前所选的发放单没有被删除\n编号：" +strErrMsg.substring(0, strErrMsg.length - 1));
							reload();
						}
					}
				}
				tableReload("删除成功");
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"删除失败！", icon:'1'});
			}
		});
	}
	
    function setDistributeOrdersOid() {
    	var oids = "";
		var table = pt.ui.get("<%=gridId%>");
		var selections = table.getSelections();
    	for(var i = 0; i < selections.length; i++){
			var oid = selections[i]["OID"];
			if(oids != ""){
				oids += ",";
			}
			oids += oid;
		}
		$("#oids").val(oids);
    }
    
 	// 批量提交发放，生成任务
    function submitDistribute() {
		setDistributeOrdersOid();
		var oids = $("#oids").val();
		ddmValidation(oids,"<%=Operate.PROMOTE%>","submitDistributeCallBack");
    }
    
    // 提交检验是否走工作流
	function submitDistributeCallBack(oids) {
		var url = "<%=contextPath%>/ddm/distribute/distributeOrderHandle!getDistributeToWorkFlow.action";
		setDistributeOrdersOid();
		$.ajax({
			url:url,
			type:"post",
			dataType:"json",
			data: $("#mainForm").serializeArray(),
			success:function(result){
				if (result.SUCCESS != null && result.SUCCESS =="false"){
					plm.endWait();
					plm.showAjaxError(result);
				} else {
					if (result.TOWORKFLOW == "true") {
						// 判断选择条数
						var table = pt.ui.get("<%=gridId%>");
						var selections = table.getSelections();
						if (selections.length > 1) {
							plm.endWait();
							if (!plm.confirm("存在提交到工作流的发放单，不可以同时操作，确定重新选择？取消则批量分发。")) {
								createTask();
							}
						} else {
							// 提交到工作流
							if (!plm.confirm("确定提交到工作流吗?")) {
								plm.endWait();
								return;
							}
							plm.endWait();
							var oids = $("#oids").val();
							var url = contextPath + "/ddm/workflow/startWorkflow_DistributeOrder.jsp?OID=" + oids + "&fromPage=listDistributeOrdersReturn";
							window.open(url,'win' , 'toolbar=no,  menubar=no, location=no, directories=no, status=no, scrollbars=no, resizable=yes, width=850, height=600, ' + 'left=' + (screen.width - 700) / 2 + ', ' + 'top=' + (screen.height - 700) / 2);
						}
					} else {
						createTask();
					}
				}
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"批量提交发放操作失败！", icon:'1'});
			}
		});
	}
	//送审后的回调函数
	function ddmApproveorderCallBack(){
		setTimeout(function(){
			window.location.href = "<%=contextPath%>/ddm/distribute/distributeOrderHandle!getAllDistributeOrderReturn.action"
		}, 3000);
	}    
    //提交生成任务
    function createTask() {
    	if (!plm.confirm("确定提交发放吗?")) {
    		plm.endWait();
			return;
		}
		var url = "<%=contextPath%>/ddm/distribute/distributeOrderHandle!createDistributeTask.action";
		var oids = $("#oids").val();
		plm.startWait();
		$.ajax({
			url:url,
			type:"post",
			dataType:"json",
			data: "flag=true&oids="+oids,
			success:function(result){
				plm.endWait();
				if (result.SUCCESS != null && result.SUCCESS =="false"){
					plm.showAjaxError(result);
				} else {
					reloadOrder();
				}
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"批量提交发放操作失败！", icon:'1'});
			}
		});
    }
	
	// 选择用户
	function addUsers(){
		plm.startWait();
		setDistributeOrdersOid();
		var oids = $("#oids").val();
		ddmValidation(oids,"<%=Operate.MODIFY%>","addUsersCallBack");
	}
	
  	function addUsersCallBack(oids){
		var configObj = 
			{ 
				IsModal : "true",
				SelectMode : "multiple",
				returnType : "arrayObj",
				scope : "<%=ConstUtil.VISTYPE_ALL%>"
			};
		var retObj = ddm.tools.selectUser(configObj);
		plm.endWait();
		if(retObj){
			doBindRole(retObj, "<%=ConstUtil.DISINFOTYPE.USER%>");
	  	}
  	}
      	
  	//选择组织
  	function addOrg(){
		plm.startWait();
  		setDistributeOrdersOid();
  		var oids = $("#oids").val();
		ddmValidation(oids,"<%=Operate.MODIFY%>","addOrgCallBack");
  	}
  	
    function addOrgCallBack(oids){
		var configObj = 
  		{ 
  			IsModal : "true",
  			SelectMode : "multiple",
  			returnType : "arrayObj",
  			scope : "<%=ConstUtil.VISTYPE_ALL%>"
  		};
		plm.endWait();
	    var retObj = ddm.tools.selectOrg(configObj);
		if(retObj){
			doBindRole(retObj, "<%=ConstUtil.DISINFOTYPE.ORG%>");
		} 
    }

 	//选择（人/组织）回调
    function doBindRole(reObj, type){
    	plm.startWait();
    	var arrIid = reObj.arrIID;
    	var arrDisMediaType = reObj.arrDisMediaType;
    	var arrDisInfoNum = reObj.arrDisInfoNum;
    	var arrNote = reObj.arrNote;
    	var iids = "";
    	var disMediaTypes = "";
    	var disInfoNums = "";
    	var notes = "";
    	var distributeObjectIids = "";
    	for(var i = 0; i < arrIid.length; i++){
    		if(iids != ""){
    			iids += ",";
    			disMediaTypes += ",";
    			disInfoNums += ",";
    			notes += ",";
    		}
    		iids += arrIid[i];
    		disMediaTypes += arrDisMediaType[i];
    		disInfoNums += arrDisInfoNum[i];
    		if (arrNote[i] == "") {
    			arrNote[i] = "null";
    		}
    		notes += arrNote[i];
    	}
		document.getElementById("type").value = type;
		document.getElementById("iids").value = iids;
		document.getElementById("disMediaTypes").value = disMediaTypes;
		document.getElementById("disInfoNums").value = disInfoNums;
		document.getElementById("notes").value = notes;

		$.ajax({
				type: "post",
				url: "<%=addPrincipalUrl%>",
				dataType: "json",
				data: $("#mainForm").serializeArray(),
				success: function(result){
					plm.endWait();
					plm.showMessage({title:'提示', message:"保存成功!", icon:'2'});
					reload("操作成功");
			   },
			   error:function(){
				   plm.endWait();
				   plm.showMessage({title:'错误提示', message:"对不起，数据请求错误", icon:'1'});
			   }
			});	
    }
	
    function addOutSignUser(){
		var oids = "";
		var table = pt.ui.get("<%=gridId%>");
		var selections = table.getSelections();
    	for(var i = 0; i < selections.length; i++){
			var oid = selections[i]["OID"];
			if(oids != ""){
				oids += ",";
			}
			oids += oid;
		}
    	plm.openWindow("<%=contextPath%>/ddm/public/selectoutorg.jsp?orderOids="+oids,500,500,'outSignUser');
    	
    }
 	
	//数据刷新方法
	 
	function reload(){
		window.location.reload(true);
		parent.frames['leftFrame'].refreshTree();
	}
  
	//数据刷新方法
	function reloadOrder(){
		window.location.reload(true);
		
	}
	

	//刷新dategrid
	function tableReload(text){
		plm.showMessage({title:'系统提示', message:text, icon:'2'});
		window.location.reload(true);
	}
</script>
