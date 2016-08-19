<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String title = "����ֽ������";
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
	    <!-- ��ATDocument,��������������������跽�� -->
	    <input type="hidden" name="CLASSID" id="CLASSID" value="<%=classId%>"/>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
			<tr>
			    <td class="left_col AvidmW150" ><span>*</span>��ţ�</td>
			    <td>
					<input type='text' name='id' id='id' class='pt-text pt-validatebox AvidmW270' <%=ValidateHelper.buildValidator()%>>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span>*</span>�������ƣ�</td>
			    <td>
					<input type='text' name='name' id='name' class='pt-text pt-validatebox AvidmW270' <%=ValidateHelper.buildValidator()%>>
			    </td>
			</tr>
			<tr>
			 	<td class="left_col AvidmW150">��ע��</td>
				<td>
					<textarea name='note' id='note' class='pt-textarea' style="width:270px;height:50px;"  <%=ValidateHelper.buildValidatorAndAssisant()%>></textarea>
				</td>
			</tr>
			<tr id="includeDynamicFields"></tr>
		</table>

		<table width="100%" height="40px" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="exegesis AvidmW150"><span>*</span> Ϊ��ѡ/������</td>
			    <td>
					<div class="pt-formbutton" text="ȷ��"  onclick="saveSubmitForm();" <%=ValidateHelper.buildCheckAll()%>></div>
					<div class="pt-formbutton" text="ȡ��"  onclick="cancleButton();"></div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
<script type="text/javascript">

	//����    �ύ��
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
					plm.showMessage({title:'������ʾ', message:a.responseText, icon:'1'});
				}
			};
		plm.startWait();
		main_form.submit(options);
	}
	
	function cancleButton(){
		window.close();
	}

	//�ر��޸�ҳ�沢���ø�ҳ��ˢ�·���
	function onAjaxExecuted(result){	
		opener.tableReload("�����ɹ�");		
		window.close();
	}
</script>
