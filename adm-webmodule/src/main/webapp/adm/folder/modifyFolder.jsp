<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>
<%@page import="com.bjsasc.plm.core.system.access.operate.Operate"%>
<%@page import="com.bjsasc.plm.core.system.access.AccessControlHelper"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.operate.ActionUtil"%>
<%@page import="com.bjsasc.plm.core.folder.SubFolder"%>
<%@page import="com.bjsasc.plm.core.Helper"%>
<%@page import="com.bjsasc.plm.core.domain.Domain"%>
<%@page import="com.bjsasc.plm.core.domain.DomainInfo"%>
<%@page import="com.bjsasc.plm.core.folder.AbstractFolder"%>
<%@page import="com.bjsasc.plm.core.folder.Folder"%>
<%@page import="com.bjsasc.plm.core.persist.PersistHelper"%>
<%@page contentType = "text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page language = "java"%>
<%@page session = "true"%>
<%@page import="com.cascc.avidm.util.UUIDService"%>
<%@include file="/plm/plm.jsp" %>
<%
	
	String folderoid = RequestUtil.getParamOid(request);
	String callback = request.getParameter(KeyS.CALLBACK);
	SubFolder folder = (SubFolder)PersistHelper.getService().getObject(folderoid);
	AccessControlHelper.getService().checkEntityPermission(Operate.ACCESS, folder);
	String contextoid = Helper.getOid(folder.getContextInfo().getContextRef());
    //获取父权限域
	DomainInfo domaininfo = folder.getDomainInfo();
	String domainname = "";
	String domainoid = "";
	if(domaininfo!=null){
		domainoid = Helper.getOid(domaininfo.getDomainRef());
		domainname = domaininfo.getDomainName();
	}
	boolean modifyname = false; 
			//AccessControlHelper.getService().hasEntityPermission(Operate.RENAME, folder);
	boolean modifynote = AccessControlHelper.getService().hasEntityPermission(Operate.MODIFY, folder);
	boolean modifydomain = AccessControlHelper.getService().hasEntityPermission(Operate.CHANGE_DOMAIN, folder);
	
	boolean inherit = SubFolder.DOMAINTYPE_EXTENDS.equals(folder.getDomaintype());
%>

<html>
<head>
<base target="_self"/>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title><%=ActionUtil.getTitle("com.bjsasc.plm.folder.edit")%></title>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>	
</head>
<body class="openWinPage">

<jsp:include page="/plm/common/actionTitle.jsp">
	<jsp:param name="ACTIONID" value="com.bjsasc.plm.folder.edit"/>
</jsp:include>

<form id="mainFrom" name="mainFrom" action="createFolder_save.jsp" ui="form" method="post">
<input type="hidden" name="CLASSID" value="<%=folder.getClassId()%>">
<input type="hidden" name="domainoid" value="<%=domainoid%>">
<input type="hidden" name="folderoid" value="<%=folderoid%>">
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">	
<tr>
    <td class="left_col AvidmW150"><span>*</span>名称：</td>
    <td><INPUT type='text' name='NAME'  style="width:270px" class='pt-textbox' <%=ValidateHelper.buildValidator()%> <%if(!modifyname){ %> readonly <%} %>   required='true' value="<%=folder.getName()%>"/></td>
</tr>
	<%if(modifydomain){ %>
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
				<input id='isinherit' value='true' type='checkbox' class=”pt-checkbox” name='isinherit' <%if(inherit){ %> checked <%} %> onclick="selectdomain()">
				<label for='groupby1_01'>从父项继承域</LABEL>
				<input type="button" id="getdomain" value="查找" <%if(inherit){%> disabled <%}%> <%=UIHelper.ATBUTTON%> onclick="getDomain();">
			</td>
		</tr>							
		</table>
	</td>
</tr>	
	<%} %>			
<tr id="includeDynamicFields">
    <td class="left_col AvidmW150">备注：</td>
    <td>
    <textarea name='NOTE'  style="width:270px;height:60px;"  <%if(!modifynote){ %> readonly <%} %> class='pt-textarea' <%=ValidateHelper.buildValidatorAndAssisant()%>><%=folder.getNote()==null?"":folder.getNote() %></textarea>
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
		
		//检查
		var folderInput = $("#NAME").val();
		if(folderInput == "" || folderInput == null){
			alert("请输入名称!");
			return;
		}
		
		//定义ajax参数
		var url = "<%=request.getContextPath()%>/plm/folder/modifyFolder_update.jsp";           
		var options = {
				dataType:"json",
				url:url,
				type:"post" ,
				success:function(result){
					plm.endWait();
					if(result.SUCCESS  == "true"){
						onAjaxExecuted(result);
					}
					else{
						plm.showAjaxError(result);
					}
				},
				error:function(a){
					alert(a.responseText);
				}
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
			opener.reload(result);
		<%}%>
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
			btn.disabled = true;
		}else{
			btn.disabled = false;
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

