<%@page import="java.util.*"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String title = "�������ڸ���";
	
	List<String> stepList = new ArrayList<String>();
	stepList.add("ɾ����������״̬���塣");
	stepList.add("ɾ����������ģ�塣");
	stepList.add("������������״̬���塣");
	stepList.add("������������ģ�塣");
	stepList.add("�����������ݡ�");
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
	<form id="form_distributeorder" name="form_distributeorder" ui="form" action="" method="POST">

		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">

	<% 	
	for (int i=0; i< stepList.size(); i++) {
		int value = i+1;
		String text = stepList.get(i);
		
		
	%> 
			<TR>
				<TD class='left_col AvidmW150'><%=value %>��</TD>
				<TD>
				<span class="padding:10px;white-space:nowrap;">
					<input type='checkbox' class="pt-checkbox" name='cbStep' id='cbStep_<%=value %>'
						value="<%=value%>"/>
					<label for='cbStep_<%=value %>' style="font-size: 12px;" onClick="checkTypeCus()"><%=text%></label>
				</span>
				</TD>
			</TR>
	<%
		}
	%>
		</table>
		
		<table width="100%" height="40px" border="0" cellspacing="0" cellpadding="0">
			<tr>
			    <td>
					<div class="pt-formbutton" text="ȷ��"  onclick="updateLifeCycle();"></div>
				</td>
			</tr>
		</table>
		<div id="msgDiv"></div>
	</form>
</body>
</html>

<script type="text/javascript">
//��������
function updateLifeCycle() {
	var cbSteps = "";
	$('[name="cbStep"]:checked').each(function(){
		cbSteps += $(this).val() + ",";
	});
	msgDiv.innerHTML = "";
	if (cbSteps == "") {
		plm.showMessage({title:'������ʾ', message:"��ѡ���������", icon:'1'});
		return;
	}
	plm.startWait();
	$.ajax({
		type: "post",
		url: "<%=contextPath%>/ddm/lifeCycleUpdateAction.jsp?op=update",
		dataType: "json",
  		data: {step:cbSteps},
		success: function(result){
			plm.endWait();
			if(result.SUCCESS != null &&result.SUCCESS == "false"){
				plm.showAjaxError(result);
				return;
			} else {
				msgDiv.innerHTML = result.MM;
				plm. showMessage({title:'��ʾ', message:"����ִ�����!", icon:'3'});
			}
	   },
	   error:function(){
		   plm.endWait();
		   plm.showMessage({title:'������ʾ', message:"�Բ��������������", icon:'1'});
	   }
	});
}
</script>