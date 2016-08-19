<%@page import="java.util.*" %>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>

<%@include file="/plm/plm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String contextOid = request.getParameter("contextOid");
	String pageType = request.getParameter("pageType");
	String classId = "DistributeObjectType";
	
	String spot = "ListDistributeObjectTypes";
	String gridId = "distributeObjectTypeDetail";
	String gridTitle = "对象模型配置";
	String toolbar_modelId = "ddm.distributeobjecttype.addtype.toolbar";
	
	String addTypeUrl = contextPath+"/ddm/config/objecttype/addObjectTypeMain.jsp?contextOid="+contextOid;
	String actionUrl = contextPath+"/ddm/config/objecttype/ddmObjectTypeAction.jsp?op=addRoles&contextOid="+contextOid;
	String dataUrl = contextPath + "/ddm/config/objecttype/ddmObjectTypeAction.jsp?op=typeData";
	
	Map<String,Object> params = new HashMap<String, Object>();
 	params.put("contextOid", contextOid);
 	GridDataUtil.prepareRowQueryParams(params,  spot);
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
				<jsp:include page="/plm/common/grid/control/grid_of_title.jsp">
					<jsp:param name="gridTitle" value="<%=gridTitle%>"/>
				</jsp:include>
				<tr><td>
					<table cellSpacing="0" cellPadding="0" width="100%" border="0"><tr class="AvidmToolbar"><td> 
						<div class="pt-toolbar">
							<%=UIHelper.getToolBar(toolbar_modelId)%>
						</div> 
					</td></tr></table>
				</td></tr>
				<tr height="100%"><td>
					<table class="pt-grid" id="<%=gridId%>"	autoLoad="false" url="<%=dataUrl%>"
						rownumbers="true" pagination="false" useFilter="false" 
						singleSelect="false" fit="true" checkbox="true">
						<th field="DOT_DATA" align="center" width="150" tips="角色/成员">角色/成员</th>
						<th field="DOT_TYPE_NAME" align="center" width="100" tips="类型名称">类型名称</th>
						<th field="DOT_STATE" align="center" width="50" tips="类别">类别</th>
						<th field="CONTEXT" align="center" width="100" tips="上下文">上下文</th>
					</table>
				</td></tr>
				<tr><td>
				<table width="100%" border="0" cellspacing="5" cellpadding="10">
					<tr>
		     	        <td align="center">
		     	          <div class="pt-formbutton" id="savebtn"   text="确定" onclick="submitForm()"></div>
		     	          <div class="pt-formbutton" id="cancelbtn" text="取消" onclick="cancel()"></div> 
		     	        </td>
			         </tr>
				  </table>
				</td></tr>
			</table>
		</form>
	</body>
</html>
<script type="text/javascript">
var container = {OID:'<%=contextOid%>'};
var records = "{}";

$(document).ready(function() {
	var adt_grid = pt.ui.get("<%=gridId%>");
	adt_grid.reload();
});

// 取消按钮操作
function cancel() {
	var msg = "您确定取消操作吗？\n";
	if (!plm.confirm(msg)) {
		return;
	}	
	parent.close();
}

// 确定按钮操作
function submitForm() {
	var adt_grid = pt.ui.get("<%=gridId%>");
	var data = adt_grid.getRows();
	if(data.length == 0){
		plm.showMessage({title:'错误提示', message:"请添加数据后，再确定", icon:'1'});
		return;
	}	
	var msg = "您确定提交操作吗？\n";
	if (!plm.confirm(msg)) {
		return;
	}		
	plm.startWait();
 	
	$.ajax({
		type: "post",
		url: "<%=contextPath%>/ddm/config/objecttype/ddmObjectTypeAction.jsp?op=addTypeData&contextOid=<%=contextOid%>",
		dataType: "json",
  		data: {pageType:"<%=pageType%>"},
		success: function(result){
			plm.endWait();
			if(result.SUCCESS != null &&result.SUCCESS == "false"){
				plm.showAjaxError(result);
				return;
			}
			parent.opener.reload();
			parent.close();
	   },
	   error:function(){
		   plm.endWait();
		   plm.showMessage({title:'错误提示', message:"对不起，数据请求错误", icon:'1'});
	   }
	});	
}

// 添加类型
function addTypes() {
	//左边角色/用户页面
	var data = parent.getSelections();
	if(data.length == 0){
		plm.showMessage({title:'错误提示', message:"请选择角色或成员数据", icon:'1'});
		return;
	}
	plm.selectTypes({callback:"doBindTypes",spot:"Distribute",expand:"true"});	
	
}

// 添加类型（回调函数）
function doBindTypes(records){
	plm.startWait();
	var dataList = UI.JSON.decode(records);
	var typeIds = "";
	var typeNames = "";
	for(var i=0;i<dataList.length;i++){
 	 	if(i==0){
 	 		typeIds   += dataList[i]["id"];
 	 		typeNames += dataList[i]["Name"];
 	 	}else{ 	
 	 		typeIds += "#@#"+dataList[i]["id"];
 	 		typeNames += "#@#"+dataList[i]["Name"];
 	 	}
 	}	
	var data = parent.getSelections();
 	var OIDS = "";
 	for(var i=0;i<data.length;i++){
 	 	if(i==0){
 	 		OIDS += data[i]["OID"];
 	 	}else{ 	
 	 		OIDS += "#@#"+data[i]["OID"];
 	 	}
 	}	
	var adt_grid = pt.ui.get("<%=gridId%>");
	$.ajax({
		type: "post",
		url: "<%=contextPath%>/ddm/config/objecttype/ddmObjectTypeAction.jsp?op=addType&contextOid=<%=contextOid%>",
		dataType: "json",
  		data: {typeIds:typeIds,typeNames:typeNames,OIDS:OIDS},
		success: function(result){
			plm.endWait();
			if(result.SUCCESS != null &&result.SUCCESS == "false"){
				plm.showAjaxError(result);
				return;
			}
			adt_grid.reload();
	   },
	   error:function(){
		   plm.endWait();
		   plm.showMessage({title:'错误提示', message:"对不起，数据请求错误", icon:'1'});
	   }
	});	
}

// 删除数据
function deleteDistributeObjectType() {
	var adt_grid = pt.ui.get("<%=gridId%>");
	var data = adt_grid.getSelections();
	if(data.length == 0){
		plm.showMessage({title:'错误提示', message:"请选择操作对象数据", icon:'1'});
		return;
	}	
	var msg = "您确定要删除对象吗？\n";
	if (!plm.confirm(msg)) {
		return;
	}	
	plm.startWait();
 	var DKS = "";
 	for(var i=0;i<data.length;i++){
 	 	if(i==0){
 	 		DKS += data[i]["DK"];
 	 	}else{ 	
 	 		DKS += "#@#"+data[i]["DK"];
 	 	}
 	}	
	$.ajax({
		type: "post",
		url: "<%=contextPath%>/ddm/config/objecttype/ddmObjectTypeAction.jsp?op=deleteTypeData&contextOid=<%=contextOid%>",
		dataType: "json",
  		data: {DKS:DKS},
		success: function(result){
			plm.endWait();
			if(result.SUCCESS != null &&result.SUCCESS == "false"){
				plm.showAjaxError(result);
				return;
			}
			adt_grid.reload();
	   },
	   error:function(){
		   plm.endWait();
		   plm.showMessage({title:'错误提示', message:"对不起，数据请求错误", icon:'1'});
	   }
	});	
}
</script>