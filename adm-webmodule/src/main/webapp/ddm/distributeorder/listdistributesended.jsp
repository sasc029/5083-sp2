<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import=" com.bjsasc.ddm.common.ConstUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>

<%
	String contextPath = request.getContextPath();
	String spot = "ListDistributeOrders";
	String gridId = "distributeOrderGrid";
	String toolbarId = "ddm.distributeorderSend.list.toolbar";
%>

<html>
	<head>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
	</head>
	<body>
		<form id="mainForm" name="mainForm" method="POST">
		<table width="100%" height="100%" cellSpacing="0" cellPadding="0"
		border="0">
		<%-- <jsp:include page="/plm/common/grid/control/grid_of_title.jsp">
			<jsp:param name="gridTitle" value="<%=title%>"/>
		</jsp:include> --%>
		<tr><td valign="top">
			<table width="100%" height="100%" cellSpacing="0" cellPadding="0" border="0">
				<tr id="searchConditionTD" height="70">
					<td valign="top">
						<table width="100%" cellSpacing="0" cellPadding="0" border="0">
							<tr>
								<td valign="top">
									<table width="100%" class="avidmTable" border="0" cellspacing="0"
										cellpadding="0">
										<TR>
											<TD class="left_col AvidmW150">关键字：</TD>
											<TD><table class="noTable">
													<tr>
														<td><input type="text" name="keywords" id="keywords"
															class="pt-textbox AvidmW270"/>
														</td>
														<td width="5"></td>
														<td><a href="#" onclick="clearValue('keywords','','');">
																<img src="<%=contextPath%>/plm/images/common/clear.gif">
														</a></td>
													</tr>
												</table>
											</TD>
										</TR>
										<TR>
											<!-- <td class="exegesis"><span>*</span>为必选/必填项</td> -->
											<TD></TD>
											<TD style="height: 40px">
												<div class="pt-formbutton" text="搜索" id="submitbutton"
													onclick="searchAll();"></div> &nbsp;&nbsp;&nbsp;
												<div class="pt-formbutton" text="清空" id="submitbutton"
													onclick="consClear();"></div>
											</TD>
										</TR>
									</table>
								</TD>
							</TR>
						</table>
					</td>
				</tr>
			<%-- <table height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0">
				<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
					<jsp:param name="spot" value="<%=spot%>"/>
					<jsp:param name="gridId" value="<%=gridId%>"/>
					<jsp:param name="toolbar_modelId" value="<%=toolbarId%>"/>
					<jsp:param name="onLoadSuccess" value="onLoadSuccess()"/>
					<jsp:param name="operate_container" value="container"/>
				</jsp:include>
			</table> --%>
				<TR>
					<TD valign="top">
						<table height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0">
							<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
							<jsp:param name="spot" value="<%=spot%>" />
							<jsp:param name="gridId" value="<%=gridId%>" />
							<jsp:param name="gridTitle" value="" />
							<jsp:param name="toolbar_modelId" value="<%=toolbarId%>" />
							<jsp:param name="onLoadSuccess" value="onLoadSuccess()"/>
							<jsp:param name="operate_container" value="container" />
							</jsp:include>
						</table>
					</TD>
				</TR>
			</table>
		</td></tr>
	</table>
		</form>
	</body>
</html>
<script type="text/javascript">
function onLoadSuccess() {
	onLoadSuccess_ddm("<%=gridId%>");
}

var container = {};



//点击搜索按钮
function searchAll(){
	var keywords=$("#keywords").val();
	var flag1="2";
	var datas = "keywords=" + keywords+"&flag1="+flag1;
	//var url = "<%=contextPath%>/ddm/distribute/distributeDestroyOrderHandle!getAllDistributeDestoryTasks.action";
	//var url = contextPath + "/ddm/distribute/distributePaperTaskHandle!getPrintProcessingDistributePaperTask.action";
	//var url = contextPath + "/ddm/distribute/distributePaperTaskHandle!getSearchPrintProcessingDistributePaperTask.action";
	//var url = contextPath + "/ddm/distribute/distributeOrderHandle!listSendedDistributeOrderAdderSearchIuput.action";
	var url = contextPath + "/ddm/distribute/distributeOrderHandle!listSendedDistributeOrderAdderSearchIuput.action";
	plm.startWait();
	$.ajax({
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		url:url,
		type:"post",
		dataType:"json",
		data:datas,
		success:function(result){
			plm.endWait();
		 	if(result.FLAG==0){
		    	messager.showTipMessager({'content':'没有数据'});
			}else{
				/*  $("#searchTab").show();
        		setVisible(this,'searchConditionTD');resizeTable(); */ 
			} 
		 	if(result.SUCCESS != null &&result.SUCCESS == "false"){
				plm.showAjaxError(result);
				return;
			} 
			var table = pt.ui.get("<%=gridId%>");
			table.reload();
		},
		error:function(){
			plm.endWait();
			plm.showMessage({title:'错误提示', message:"操作失败！", icon:'1'});
		}
	});	
	flag1="";
}

//清空
function consClear() {
	try {
	$("#keywords").val("");
	$("#taskName").val("");
	//$("#taskCode").val("");
	$("#allContext").attr("checked",false);
	$("#favoritedContext").attr("checked",false);
	$("#excludeOption").html("");
	$("#creator").val("");
	
	$('[id="receiver_person"]').removeAttr("checked");
	$('[type="radio"][id="receiver_unit"]').attr("checked", true);
	$("#flag").val("1");
	$('#disInfoName').show();
	$('#disInfoName').val("0");
	$('#disInfoNameOfUser').hide();
	
	$("#queryCreateDateFrom").val("");
	$("#queryCreateDateTo").val("");
	$("#creatorName").val("");
	$('[id="receiver_person"]').removeAttr("checked");
	$('[type="radio"][id="receiver_unit"]').attr("checked", true);
	$("#flag").val("1");
	} catch (e) {}
}
//橡皮擦
function clearValue(value1,value2,value3) {
	try {
	$("#"+value1).val("");
	$("#"+value2).val("");
	$("#"+value3).val("");
	} catch (e) {}
}


//补发
function reDistribute(){
	
	var table = pt.ui.get("<%=gridId%>");
	var data = table.getSelections();
	if(data.length == 0){
		plm.showMessage({title:'错误提示', message:"请至少选择一条操作数据", icon:'1'});
		return;
	}if(data.length > 1){
		plm.showMessage({title:'错误提示', message:"只能选择一条操作数据", icon:'1'});
		return;
	}
	var oids = "";
	//回收发放单
	var recDisType = "<%=ConstUtil.C_ORDERNAME_2%>";
	//销毁发放单
	var desDisType = "<%=ConstUtil.C_ORDERNAME_3%>";

	for(var i=0;i<data.length;i++){
		var orderType = data[i]["ORDERTYPE"];
		if(orderType == recDisType) {
			plm.showMessage({title:'错误提示', message:"回收发放单不能创建补发单！", icon:'1'});
			return;
		}else if(orderType == desDisType) {
			plm.showMessage({title:'错误提示', message:"销毁发放单不能创建补发单！", icon:'1'});
			return;
		}else{
			oids += data[i]["OID"] + ",";
		}
	}
	oids=oids.substring(0,oids.length-1);
	var url = "<%=contextPath%>/ddm/distributeorder/createReissueDistributeOrder.jsp?<%=KeyS.CALLBACK%>=reload&oid=" + oids;
	plm.OpenWindowAndReload(url);
	return; 

}

//创建回收发放单
function addRecInfo(){
	var table = pt.ui.get("<%=gridId%>");
	var data = table.getSelections();
	if(data.length == 0){
		plm.showMessage({title:'错误提示', message:"请至少选择一条操作数据", icon:'1'});
		return;
	}if(data.length > 1){
		plm.showMessage({title:'错误提示', message:"只能选择一条操作数据", icon:'1'});
		return;
	}

	//回收发放单
	var recDisType = "<%=ConstUtil.C_ORDERNAME_2%>";
	//销毁发放单
	var desDisType = "<%=ConstUtil.C_ORDERNAME_3%>";
	var oids = "";
	for(var i=0;i<data.length;i++){
		var orderType = data[i]["ORDERTYPE"];
		if(orderType == recDisType) {
			plm.showMessage({title:'错误提示', message:"回收发放单不能创建回收发放单！", icon:'1'});
			return;
		}else if(orderType == desDisType) {
			plm.showMessage({title:'错误提示', message:"销毁发放单不能创建回收发放单！", icon:'1'});
			return;
		}else{
			oids += data[i]["OID"] + ",";
		}
	}

	oids = oids.substring(0,oids.length-1);
	//验证是否存在未完成的回收发放单
	var checkIsFinished = "<%=contextPath%>/ddm/distribute/recDesInfo!checkRecDistributeOrder.action?oid=" + oids;
	$.ajax({
			type: "post",
			url: checkIsFinished,
			dataType: "json",
			data: "",
			success: function(result){
				if(result.SUCCESS != null &&result.SUCCESS == "false"){
				plm.showMessage({title:'提示', message:"您所选择的分发数据，存在未完成的回收发放单!", icon:'1'});
					return;
				}else{
					var url = "<%=contextPath%>/ddm/recdesinfo/createRecDistributeOrder.jsp?<%=KeyS.CALLBACK%>=reload&oid=" + oids;
				
					plm.OpenWindowAndReload(url); 
					return; 
				}
			},
			error:function(){
				plm.showMessage({title:'错误提示', message:"对不起，数据请求错误", icon:'1'});
				return;
			}
		});
}

//创建销毁发放单
function addDesInfo(){
	var table = pt.ui.get("<%=gridId%>");
	var data = table.getSelections();
	if(data.length == 0){
		plm.showMessage({title:'错误提示', message:"请至少选择一条操作数据", icon:'1'});
		return;
	}if(data.length > 1){
		plm.showMessage({title:'错误提示', message:"只能选择一条操作数据", icon:'1'});
		return;
	}

	//回收发放单
	var recDisType = "<%=ConstUtil.C_ORDERNAME_2%>";
	//销毁发放单
	var desDisType = "<%=ConstUtil.C_ORDERNAME_3%>";
	var oids = "";
	for(var i=0;i<data.length;i++){
		var orderType = data[i]["ORDERTYPE"];
		if(orderType == recDisType) {
			plm.showMessage({title:'错误提示', message:"回收发放单不能创建销毁发放单！", icon:'1'});
			return;
		}else if(orderType == desDisType) {
			plm.showMessage({title:'错误提示', message:"销毁发放单不能创建销毁发放单！", icon:'1'});
			return;
		}else{
			oids += data[i]["OID"] + ",";
		}
	}
	
	 oids = oids.substring(0,oids.length-1);
	//验证是否存在未完成的回收发放单
	var checkIsFinished = "<%=contextPath%>/ddm/distribute/recDesInfo!checkDesDistributeOrder.action?oid=" + oids;

	$.ajax({
			type: "post",
			url: checkIsFinished,
			dataType: "json",
			data:"",
			success: function(result){
				if(result.SUCCESS != null &&result.SUCCESS == "false"){
					plm.showMessage({title:'提示', message:"您所选择的分发数据，存在未完成的销毁发放单!", icon:'1'});
					return;
				}else{
				
					var url = "<%=contextPath%>/ddm/recdesinfo/createDesDistributeOrder.jsp?<%=KeyS.CALLBACK%>=reload&oid=" + oids;
					plm.OpenWindowAndReload(url);
					return; 

				}
			},
			error:function(){
				plm.showMessage({title:'错误提示', message:"对不起，数据请求错误", icon:'1'});
				return;
			}
		});
}


//导出瀚海之星xml文件
function hanhaiExportXml(){
	var table = pt.ui.get("<%=gridId%>");
	var data = table.getSelections();
	if(data.length == 0){
		plm.showMessage({title:'错误提示', message:"请至少选择一条操作数据", icon:'1'});
		return;
	}
	if (!plm.confirm("确定要导出所选发放单的相关信息至瀚海之星么?")) {
		return;
	}
	var oids = "";
	for(var i=0;i<data.length;i++){
			oids += data[i]["OID"] + ",";
	}
	 oids = oids.substring(0,oids.length-1);
	//瀚海之星文件导出
	var hanhaiExportXmlUrl = "<%=contextPath%>/ddm/integrate/hanhaiExportXml.jsp";
	plm.startWait();
	$.ajax({
			type: "post",
			url: hanhaiExportXmlUrl,
			dataType: "json",
			data:"<%=KeyS.OIDS%>=" + oids,
			success: function(result){
				if(result.SUCCESS == "true"){
					plm.endWait();
					plm.showMessage({title:'提示', message:"导出瀚海之星文件成功!", icon:'2'});
					return;
				}else{
					plm.endWait();
					plm.showMessage({title:'提示', message:"导出瀚海之星文件失败!", icon:'1'});
					return;
				}
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"对不起，导出瀚海之星文件失败!", icon:'1'});
				return;
			}
		});
}

</script>
