<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>

<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String title = "�޸ķ��ŵ�";
	String contextPath = request.getContextPath();
	String oid = (String) session.getAttribute(ConstUtil.DISTRIBUTE_ORDER_OID);
	String classId = "DistributeOrder";
	Persistable obj = Helper.getPersistService().getObject(oid);
	DistributeOrder dis = (DistributeOrder)obj;
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
				<div class="namediv"><%=title%></div></td>
		 </tr>
	</table>
	<form method="post" name="myform" id="myform" ui='form'>
		<input type="hidden" value="<%=oid %>" name="<%=KeyS.OID %>" />
	    <input type="hidden" name="CLASSID" id="CLASSID" value="<%=classId%>"/>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
			<tr>
			    <td class="left_col AvidmW150" ><span>*</span>��ţ�</td>
			    <td>
					<input type="hidden" name="NUMBER" id="NUMBER" value="<%=dis.getNumber()%>"/>
			    	<%=dis.getNumber() %>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span>*</span>���ŵ����ƣ�</td>
			    <td>
			    	<input type='text' id="NAME" name="NAME" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);" value="<%=dis.getName()%>" <%=ValidateHelper.buildValidator()%>/>
			    </td>
			</tr>
			
			<tr>
			 	<td class="left_col AvidmW150">��ע��</td>
				<td>
				    <%if(dis.getNote() == null){%>
						<TEXTAREA id='NOTE' name='NOTE' class='pt-textarea' style='width:270px;height:60px;' <%=ValidateHelper.buildValidatorAndAssisant()%>></TEXTAREA>
				    <%} else {%>
						<TEXTAREA id='NOTE' name='NOTE' class='pt-textarea' style='width:270px;height:60px;' <%=ValidateHelper.buildValidatorAndAssisant()%>><%=dis.getNote()%></TEXTAREA>
					<%}%>
				</td>
			</tr>
			<tr id="includeDynamicFields"></tr>
		</table>
	
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
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


	// ��ť������ȷ����
	function saveSubmitForm(){

		<%=ValidateHelper.buildCheck()%>
		plm.startWait();
		$.ajax({
			   type: "post",
			   url: "<%=contextPath%>/ddm/distribute/distributeOrderHandle!updateDistributeOrder.action",
			   dataType:"json",
			   data: $("#myform").serializeArray(),
			   success: function(result){
				    plm.endWait();
					if(result.SUCCESS != null && result.SUCCESS == "false"){
						plm.showAjaxError(result);
					}else{
						window.close();
						//visit.jsp��ר��Ϊ��ҳ��Ϊģ̬���ڵ�����ṩ��ˢ�¸�ҳ��ķ���
						window.dialogArguments.reloadInModalDialog(); 
					}
			   },
			   error:function(){
				   plm.endWait();
				   plm.showMessage({title:'������ʾ', message:"�޸ķ��ŵ�����ʧ��!", icon:'1'});
			   }
			});
	}
	
	// ��ť������ȡ����
	function cancleButton(){
		//window.location.href = "<%=contextPath%>/ddm/public/visitObject.jsp?OID=<%=oid%>";
		window.close();
	}

	//�ӳٳ�ʼ��ҳ��
	//setTimeout("initPage()",200);
</script>
