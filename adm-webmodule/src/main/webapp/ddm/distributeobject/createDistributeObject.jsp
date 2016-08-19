<%@page import="com.bjsasc.plm.url.Url"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String title = "创建分发数据对象";
	String classId = "DistributeObject";
%>
<html>
	<head>
		<title><%=title%></title>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
	</head>
<body class="openWinPage">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
	    	<td class="AvidmTitleName">
				<div class="imgdiv"><img src="<%=contextPath%>/plm/images/project/createproject.gif"/></div>
				<div class="namediv"><%=title%></div>
			</td>
		</tr>
	</table>
	<FORM id='main_form' ui='form'>
	    <!-- 配ATDocument,用于输入框描述控制所需方法 -->
	    <input type="hidden" name="CLASSID" id="CLASSID" value="<%=classId%>"/>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
			<tr>
			    <td class="left_col AvidmW150" ><span>*</span>分发数据源内部标识：</td>
			    <td>
			    	<input type='text' id="dataInnerId" name="dataInnerId" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);"/>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span>*</span>分发数据源类名：</td>
			    <td>
			    	<input type='text' id="dataClassId" name="dataClassId" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);"/>
			    </td>
			</tr>
			<tr>
			 	<td class="left_col AvidmW150"><span>*</span>分发数据源来源：</td>
				<td>
			    	<input type='text' id="dataFrom" name="dataFrom" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);"/>
				</td>
			</tr>
			<tr>
			 	<td class="left_col AvidmW150"><span>*</span>编号：</td>
				<td>
			    	<input type='text' id="id" name="id" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);"/>
				</td>
			</tr>
			<tr>
			 	<td class="left_col AvidmW150"><span>*</span>名称：</td>
				<td>
			    	<input type='text' id="name" name="name" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);"/>
				</td>
			</tr>
			<tr>
			 	<td class="left_col AvidmW150"><span>*</span>类型：</td>
				<td>
			    	<input type='text' id="type" name="type" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);"/>
				</td>
			</tr>
			<tr>
			 	<td class="left_col AvidmW150"><span>*</span>型号：</td>
				<td>
			    	<input type='text' id="code" name="code" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);"/>
				</td>
			</tr>
			<tr>
			 	<td class="left_col AvidmW150"><span>*</span>版本：</td>
				<td>
			    	<input type='text' id="version" name="version" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);"/>
				</td>
			</tr>
			<tr>
			 	<td class="left_col AvidmW150"><span>*</span>备注：</td>
				<td>
			    	<input type='text' id="note" name="note" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);"/>
				</td>
			</tr>
			<tr id="includeDynamicFields"></tr>
		</table>

		<table width="100%" height="40px" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="exegesis AvidmW150"><span>*</span> 为必选/必填项</td>
			    <td>
					<div class="pt-formbutton" text="确定"  onclick="saveSubmitForm();"></div>
					<div class="pt-formbutton" text="取消"  onclick="cancleButton();"></div>
				</td>
			</tr>
		</table>
	</FORM>
</body>
</html>

<script type="text/javascript">
			
	getfield("<%=classId%>");
	
	function getfield(classId){
		plm.getExtAttr({trid:'includeDynamicFields',colnum:1,classid:classId});
	}

	//保存    提交表单
	function saveSubmitForm() {
		var main_form = pt.ui.get("main_form");
		var name = $("#name").val();//名称
		var options = {
				url:"<%=contextPath%>/ddm/distribute/distributeObjectHandle!createDistributeObject.action",
				dataType:"json",
				type:"POST",
				success:function(result) {
					plm.endWait();
					if(result.SUCCESS !=null && result.SUCCESS == "false"){
						plm.showAjaxError(result);
						return;
					}
					onAjaxExecuted();
				},
				error:function(a){
					plm.endWait();
					plm.showMessage({title:'错误提示', message:a.responseText, icon:'1'});
				}
			};
		plm.startWait();
		main_form.submit(options);
	}
	
	function cancleButton(){
		window.close();
	}

	//关闭修改页面并调用父页面刷新方法
	function onAjaxExecuted(){
		window.close();
	}
</script>
