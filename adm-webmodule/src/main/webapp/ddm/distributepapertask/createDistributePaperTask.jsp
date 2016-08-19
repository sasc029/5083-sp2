<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String title = "创建纸质任务";
	String classId = "DistributePaperTask";
%>
<html>
	<head>
		<title><%=title%></title>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
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
	<form id="form_distributepapertask" name="form_distributepapertask" ui="form" action="" method="POST">
	    <!-- 配ATDocument,用于输入框描述控制所需方法 -->
	    <input type="hidden" name="CLASSID" id="CLASSID" value="<%=classId%>"/>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
			<tr>
			    <td class="left_col AvidmW150" ><span>*</span>编号：</td>
			    <td>
					<input type='text' name='id' id='id' class='pt-text pt-validatebox AvidmW270' <%=ValidateHelper.buildValidator()%>>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span>*</span>任务名称：</td>
			    <td>
					<input type='text' name='name' id='name' class='pt-text pt-validatebox AvidmW270' <%=ValidateHelper.buildValidator()%>>
			    </td>
			</tr>
			<tr>
			 	<td class="left_col AvidmW150">备注：</td>
				<td>
					<textarea name='note' id='note' class='pt-textarea' style="width:270px;height:50px;"  <%=ValidateHelper.buildValidatorAndAssisant()%>></textarea>
				</td>
			</tr>
			<tr id="includeDynamicFields"></tr>
		</table>

		<table width="100%" height="40px" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="exegesis AvidmW150"><span>*</span> 为必选/必填项</td>
			    <td>
					<div class="pt-formbutton" text="确定"  onclick="saveSubmitForm();" <%=ValidateHelper.buildCheckAll()%>></div>
					<div class="pt-formbutton" text="取消"  onclick="cancleButton();"></div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
<script type="text/javascript">

	//保存    提交表单
	function saveSubmitForm() {
		<%=ValidateHelper.buildCheck()%>
		var main_form = pt.ui.get("form_distributepapertask");
		var options = {
				url:"<%=contextPath%>/ddm/distribute/distributePaperTaskHandle!createDistributePaperTask.action",
				dataType:"json",
				type:"POST",
				success:function(result) {
					plm.endWait();
					if(result.SUCCESS !=null && result.SUCCESS == "false"){
						plm.showAjaxError(result);
						return;
					}
					onAjaxExecuted(result);
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
	function onAjaxExecuted(result){	
		opener.tableReload("创建成功");		
		window.close();
	}
</script>
