<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>

<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String title = "修改分发数据对象";
	String contextPath = request.getContextPath();
	String oid = RequestUtil.getParamOid(request);
	Persistable obj = Helper.getPersistService().getObject(oid);
	DistributeObject dis = (DistributeObject)obj;
%>
<html>
<head>
	<title><%=title%></title>
	<script type="text/javascript" src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
</head>
<body class="openWinPage">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		 <tr>
	    	<td class="AvidmTitleName">
	    		<div class="imgdiv"><img src="<%=contextPath%>/plm/images/common/modify.gif"/></div>
				<div class="namediv"><%=title%></div></td>
		 </tr>
	</table>
	<form action="updateProject_save.jsp" method="post" name="main_form" id="main_form" ui='form'>
		<input type="hidden" value="<%=oid %>" name="<%=KeyS.OID %>" />
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
			<tr>
			    <td class="left_col AvidmW150" ><span>*</span>分发数据源内部标识：</td>
			    <td>
			    	<input type='text' id="dataInnerId" name="dataInnerId" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);" value="<%=dis.getDataInnerId()%>"/>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span>*</span>分发数据源类名：</td>
			    <td>
			    	<input type='text' id="dataClassId" name="dataClassId" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);" value="<%=dis.getDataClassId()%>"/>
			    </td>
			</tr>
			<tr>
			 	<td class="left_col AvidmW150"><span>*</span>分发数据源来源：</td>
				<td>
					<input type='text' id="dataFrom" name="dataFrom" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);" value="<%=dis.getDataFrom()%>"/>
				</td>
			</tr>
			<tr id="includeDynamicFields"></tr>
		</table>
	
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
			    <td class="exegesis AvidmW150"><span>*</span> 为必选/必填项</td>
			    <td>
			    	<div class="pt-formbutton" text="确定"  onclick="saveSubmitForm();"></div>
			    	<div class="pt-formbutton" text="取消"  onclick="cancleButton();"></div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
<script type="text/javascript">
	function saveSubmitForm(){
		var dataInnerId = $("#dataInnerId").val();
		var dataClassId = $("#dataClassId").val();
		var dataFrom = $("#dataFrom").val();
		
		if(dataInnerId == ""){
			plm.showMessage({title:'错误提示', message:"请输入分发数据源内部标识", icon:'1'});
			return;
		}
		
		$.ajax({
			   type: "post",
			   url: "<%=contextPath%>/ddm/distribute/distributeObjectHandle!updateDistributeObject.action",
			   dataType:"json",
			   data: $("#main_form").serializeArray(),
			   success: function(result){
				   if (result.SUCCESS != null && result.SUCCESS =="false"){
						plm.showAjaxError(result);
				   } else {
				  		onAjaxExecuted();
			   	   }
			   },
			   error:function(){
				   plm.showMessage({title:'错误提示', message:"对不起，数据请求错误", icon:'1'});
			   }
			});
	}
	
	//关闭修改页面并调用父页面刷新方法
	function onAjaxExecuted(){
		window.returnValue = "success";
		window.close();
	}
	
	function cancleButton(){
		window.close();
	}

</script>
