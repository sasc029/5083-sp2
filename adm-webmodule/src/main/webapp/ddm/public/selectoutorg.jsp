<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@ page import="java.util.*"%>
<%@ page import="com.bjsasc.plm.ui.UIHelper"%>
<%@ page import="com.bjsasc.plm.url.Url"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.cascc.avidm.util.SplitString"%>
<%@include file="/plm/plm.jsp" %>
<%
	String root = request.getContextPath();
	String sites = (String)request.getAttribute("sites");
	String oids = request.getParameter("DATA");
	String orderOids = "";
	String linkOids = "";
	if(oids != "" && oids != null){
		oids  = URLDecoder.decode(oids, "UTF-8");
		oids = oids.replace("\"", "");
		List<String> oidsList = SplitString.string2List(oids, ":");
		if("DistributeOrder".equals(oidsList.get(0))){
			orderOids = oids;
		}else if ("DistributeOrderObjectLink".equals(oidsList.get(0))){
			linkOids = oids;
		}
	}
%>
<html>
<head>
<title></title>
<jsp:include page="/platform/ui/Ui-js.jsp"></jsp:include>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
</head>
<body class="openWinPage">
<form name="orderForm" method="post">
<table style="width:100%;height:100%;" border="0" cellspacing="0" cellpadding="0">
<tr height = "60%"><td>
<table id="site_list_" width="100%" class="pt-grid" singleSelect="false" rownumbers="true" useFilter="false"
fit="true" checkbox="true" url="<%=root%>/ddm/distribute/distributeInfoHandle!initReceiveSiteAndUser.action" pagination="false" onLoadSuccess="onLoadSuccess()" >
	<thead>
	<tr>
		<!-- <th field="INNERID" hidden="hidden"></th>
		<th field="DOMAINID" hidden="hidden"></th> -->
		<th field="SITENAME" width="220">接收站点</th>
		<!-- <th field="USERID" hidden="hidden"></th> -->
		<th field="USERNAME" width="200" editor="{type:'Search',readOnly:true,ontrigger: function(){receiveUser();}}">接收人</th>
	</tr>
	</thead>
</table>
</td></tr>
<tr height="10%"><td>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
			<div class="pt-formbutton" text="确定"  onclick="submitForm();" ></div>
			<div class="pt-formbutton" text="取消"  onclick="cancleButton();"></div>
		</td>
	</tr>
</table>
</td></tr>
<tr height="30%"><td>
</table>
</form>
</body>
<script type="text/javascript">
function onLoadSuccess(){
	var sites = "<%= sites%>";
	var grid = pt.ui.get("site_list_");
	var rows = grid.getRows();
	for(var i=0; i<rows.length; i++){
		if(sites.indexOf(rows[i]["INNERD"])>-1){
			grid.selectRow(i);
		}
	}
}
//选择站点接收人
function receiveUser(){
	var grid = pt.ui.get("site_list_");
	var selectedRecord = grid.getSelected();
	grid.data.beginChange();
	selectedRecord.USERID = '';
	selectedRecord.USERNAME = '';
	grid.data.endChange();
	var returnType = 'arrayObj';//arrayOjb(数据对象，默认)，json(json对象)
	var IsModal = 'true';//页面是否是模态
	var SelectMode = 'multiple';//multiple多选(默认)，single单选
	var userStatus = 'A';//选择用户状态(A:正常状态的用户，F:锁定状态的用户，T：临时锁定状态的用户，ALL：全部用户)
	var scope = 'self';//选择用户范围(all：所有组织域创建的用户，path:指定组织域及其所有父结点所创建的用户,self:指定组织域所创建的用户，bind:当前应用域所绑定的用户)
	var domainRef = selectedRecord.DOMAINID;
	var url = "<%=root%>/platform/public/selectuser/UISelectUser.jsp?returnType=" + returnType
		+ "&IsModal=" + IsModal + "&SelectMode=" + SelectMode + 
		"&userStatus=" + userStatus + "&scope=" + scope+"&domainRef="+domainRef;
	var strFeatures="dialogWidth=700px;dialogHeight=500px;center=yes;middle=yes;help=no;status=no;scroll=no;resizable=no;location=no;";
	var reObj = window.showModalDialog(url,window,strFeatures);
	if (reObj) {
		callbackFun(reObj);
	}
}
//回调函数
function callbackFun(arrayObj) {
	if (null == arrayObj){
		return;
	}
	var grid = pt.ui.get("site_list_");
	var selectedRecord = grid.getSelected();
	
	var arrUserIID = arrayObj.arrUserIID;
	var arrUserName = arrayObj.arrUserName;
	var userIID = '';
	var userName = '';
	for(var i=0; i<arrUserIID.length; i++){
		userIID = userIID + arrUserIID[i];
		if(i < arrUserIID.length-1){
			userIID = userIID + ',';
		}
	}
	for(var i=0; i<arrUserName.length; i++){
		userName = userName + arrUserName[i];
		if(i < arrUserName.length-1){
			userName = userName + ',';
		}
	}
	grid.data.beginChange();
	selectedRecord.USERID = userIID;
	selectedRecord.USERNAME = userName;
	grid.data.endChange();
}

function cancleButton(){
		window.close();
	}

function submitForm(){
	var linkOids = "<%=linkOids%>";
	if(linkOids != "null" && linkOids !=""){
		addOutSign_info();
	}else{
		addOutSign_order();
	}
}

function addOutSign_info(){
	var table = pt.ui.get("site_list_");
	var data = table.getSelections();
	if(data.length == 0){
		plm.showMessage({title:'错误提示', message:"请至少选择一条操作数据", icon:'1'});
		return;
	}
	var innerIds = "";
	var siteNames = "";
	var userIds = "";
	for(var i=0;i<data.length;i++){
		innerIds += data[i].INNERID + ";";
		siteNames += data[i].SITENAME + ";";
		userIds += data[i].USERID + ";";
	}
	innerIds = innerIds.substring(0, innerIds.length - 1);
	siteNames = siteNames.substring(0, siteNames.length - 1);
	userIds = userIds.substring(0, userIds.length - 1);
	var url = contextPath + "/ddm/distribute/distributeInfoHandle!createOutSignDisInfo.action";
	plm.startWait();
		$.ajax({
			url:url,
			type:"post",
			dataType:"json",
			data:"innerIds="+innerIds+"&siteNames="+siteNames+"&userIds="+userIds+"&linkOids=<%=linkOids%>",
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
				plm.showMessage({title:'错误提示', message:"添加操作失败！", icon:'1'});
			}
		});
}

function addOutSign_order(){
	var table = pt.ui.get("site_list_");
	var data = table.getSelections();
	if(data.length == 0){
		plm.showMessage({title:'错误提示', message:"请至少选择一条操作数据", icon:'1'});
		return;
	}

	var innerIds = "";
	var siteNames = "";
	var userIds = "";
	for(var i=0;i<data.length;i++){
		innerIds += data[i].INNERID + ";";
		siteNames += data[i].SITENAME + ";";
		userIds += data[i].USERID + ";";
	}
	innerIds = innerIds.substring(0, innerIds.length - 1);
	siteNames = siteNames.substring(0, siteNames.length - 1);
	userIds = userIds.substring(0, userIds.length - 1);
	var addOutPrincipalUrl = "<%=root%>/ddm/distribute/distributeOrderHandle!addOutPrincipals.action";
	plm.startWait();
	$.ajax({
		type: "post",
		url: addOutPrincipalUrl,
		dataType: "json",
		data: "innerIds="+innerIds+"&siteNames="+siteNames+"&userIds="+userIds+"&orderOids=<%=orderOids%>",
		success: function(result){
			plm.endWait();
			if(result.SUCCESS != null && result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
			}
			window.close();
	   },
	   error:function(){
		   plm.endWait();
		   plm.showMessage({title:'错误提示', message:"对不起，数据请求错误", icon:'1'});
	   }
	});
}

function reload(){	
	opener.tableReload();		
	window.close();
}
</script>
</html>
