<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>
<%@page import="com.bjsasc.plm.core.domain.DomainInfo"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.core.folder.Folder"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.operate.PlmAction"%>
<%@page import="com.bjsasc.plm.operate.ActionUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page session = "true"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="java.util.List"%>
<%@include file="/plm/plm.jsp" %>

<%
	Folder parentFolder = (Folder)RequestUtil.getTargetFolder(request);
    String parentFolderOid = Helper.getOid(parentFolder);
	String callback = request.getParameter(KeyS.CALLBACK);
	String contextoid = Helper.getOid(parentFolder.getContextInfo().getContextRef());
	//获取父权限域
	DomainInfo domaininfo = parentFolder.getDomainInfo();
	String domainname = "";
	String domainoid = "";
	if(domaininfo!=null){
		domainoid = Helper.getOid(domaininfo.getDomainRef());
		domainname = domaininfo.getDomainName();
	}
%>

<html>
<head>
<base target="_self"/>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title><%=ActionUtil.getTitle("com.bjsasc.plm.folder.create")%></title>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>	
</head>
<body class="openWinPage">

<jsp:include page="/plm/common/actionTitle.jsp">
	<jsp:param name="ACTIONID" value="com.bjsasc.plm.folder.create"/>
</jsp:include>

<form id="mainFrom" name="mainFrom" action="createFolder_save.jsp" ui="form" method="post">
<input type="hidden" name="CLASSID" value="SubFolder">
<input type="hidden" name="parentFolderOid" value="<%=parentFolderOid%>">
<input type="hidden" name="domainoid" value="<%=domainoid%>">
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
<tr>
    <td class="left_col AvidmW150"><span>*</span>名称：</td>
    <td><INPUT type='text' name='NAME'  style="width:270px" class='pt-textbox' required='true' <%=ValidateHelper.buildValidator()%>/></td>
</tr>
<tr>
	<td class='left_col AvidmW150'>域：</td>
	<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tableBorderNone2">
		<tr>
			<td>
				<INPUT ui='textField' type='text' name='domainname' value="<%=domainname %>" style="width:270px" 
				readonly class='pt-textbox pt-validatebox ' id='domain' required='true'>
			</td>
		</tr>
		<tr>
			<td>
				<input id='isinherit' value='true' type='checkbox' class=”pt-checkbox” name='isinherit' checked  onclick="selectdomain()"> 
				<label for='groupby1_01'>从父项继承域</LABEL>
				<input type="button" id="getdomain" value="查找" onclick="getDomain()" disabled="disabled" <%=UIHelper.ATBUTTON%>>				
			</td>
		</tr>
		</table>	
	</td>
</tr>
<tr id="includeDynamicFields">
    <td class="left_col AvidmW150">备注：</td>
    <td>
    	<textarea name='NOTE'  style="width:270px;height:60px;" class='pt-textarea' <%=ValidateHelper.buildValidatorAndAssisant()%>></textarea>
    </td>
</tr>

</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
    <td class="exegesis AvidmW150"><span>*</span> 为必选/必填项</td>
    <td>
    	<div class="pt-formbutton" text="确定" id="submitbutton" onclick="onOk();" <%=ValidateHelper.buildCheckAll()%>></div>
		<div class="pt-formbutton" text="取消" id="closebutton" onclick="onCancelButton();"></div>
	</td>
</tr>
</table>

</form>
</body>
</html>

<script type="text/javascript">
function onOk(){
	<%=ValidateHelper.buildCheck()%>
	
	//定义ajax参数
	var url = "<%=request.getContextPath()%>/adm/folder/createFolder_save.jsp";           
	var options = {
			success:function(result){
				plm.endWait();
				if(result.SUCCESS !=null && result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				
				onAjaxExecuted(result);
			},
			error:function(a){alert(a.responseText);},
			dataType:"json",
			url:url,
			type:"post"   
		};
	
	//提交执行
	var form = pt.ui.get("mainFrom");
	plm.startWait();
	form.submit(options);
}

//ajax执行成功
function onAjaxExecuted(result){	
	//执行回调
	<%if(callback != null){%>
		opener.<%=callback%>(result);
	<%}%>
	//弹出创建成功的提示
	opener.messager.showTipMessager({"content":"文件夹创建成功！"});
	//关闭当前窗口
	window.close();
}

function onCancelButton(){
	window.close();
}

function selectdomain(){
	var cd = document.getElementById("isinherit");
	var btn = document.getElementById("getdomain");
	
	if(cd.checked){
		mainFrom.domainoid.value="<%=domainoid%>";
		mainFrom.domainname.value = "<%=domainname%>";
		btn.disabled="disabled";
	}else{
		btn.disabled="";
	}
}
function getDomain(){
	plm.selectDomain("setDomain","<%=contextoid%>");
	
}
function setDomain(jsonstr){
	var domainJson = eval("("+jsonstr+")");
	mainFrom.domainoid.value=domainJson[0]["OID"];
	mainFrom.domainname.value = domainJson[0]["NAME"];
}
</script>
