<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp"%>

<%
	String contextPath = request.getContextPath();
	String title = "";
	String flag = request.getParameter("flag");
	if("elecTaskRefuse".equals(flag)){
		title = "��ǩ����";
	}else{
		title = "���˴���";
	}
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
	    		<div class="imgdiv"><img src="<%=contextPath%>/plm/images/common/modify.gif"/></div>
				<div class="namediv"><%=title%></div>
			</td>
		 </tr>
	</table>
	<form method="post" name="myform" id="myform" ui='form'>
	    <input type="hidden" name="CLASSID" id="CLASSID" value="ReturnReason"/>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
			<tr>
			<%
				if("elecTaskRefuse".equals(flag)){
			%>
			<td class="left_col AvidmW150">��ǩԭ��</td>
			<%} else{%>
			 	<td class="left_col AvidmW150">����ԭ��</td>
			 <%} %>
				<td>
					<TEXTAREA id='returnReason' name='returnReason' class='pt-textarea' style='width:270px;height:60px;' <%=ValidateHelper.buildValidatorAndAssisant()%>></TEXTAREA>
				</td>
			</tr>
		</table>
		<table width="100%" border="0" cellspacing="5" cellpadding="0">
			<tr>
				<td width="160">&nbsp;</td>
			    <td>
			    	<div class="pt-formbutton" text="ȷ��"  onclick="saveDisagree();"></div>
			    	<div class="pt-formbutton" text="ȡ��"  onclick="cancleButton();"></div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
	
<script type="text/javascript">

	// ��ť������ȷ����
	function saveDisagree(){
		<%=ValidateHelper.buildCheck()%>
		/* if($("#returnReason").val()==""){
			$("#returnReason").val("null");
		} */
		if($("#returnReason").val()==""){
			window.returnValue = "null";
		}else{
			window.returnValue = $("#returnReason").val();
		}
		window.close();
	}
	
	// ��ť������ȡ����
	function cancleButton(){
		window.close();
	}
</script>
