<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>

<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String title = "�޸�ֽ������";
	String contextPath = request.getContextPath();
	String oid = RequestUtil.getParamOid(request);
	Persistable obj = Helper.getPersistService().getObject(oid);
	DistributePaperTask dis = (DistributePaperTask)obj;
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
	<form action="updatePaperTask_save.jsp" method="post" name="myform" id="myform" ui='form'>
		<input type="hidden" value="<%=oid %>" name="<%=KeyS.OID %>" />
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
			<tr>
			    <td class="left_col AvidmW150" ><span>*</span>��ţ�</td>
			    <td>
			    	<input type='text' id="id" name="id" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);" value="<%=dis.getNumber()%>"/>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span>*</span>���ƣ�</td>
			    <td>
			    	<input type='text' id="name" name="name" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);" value="<%=dis.getName()%>"/>
			    </td>
			</tr>
			
			<tr>
			 	<td class="left_col AvidmW150">��ע��</td>
				<td>
					<TEXTAREA id='note' name='note' class='pt-textarea' style='width:270px;height:60px;'><%=dis.getNote()%></TEXTAREA>
				</td>
			</tr>
			<tr id="includeDynamicFields"></tr>
		</table>
	
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
			    <td class="exegesis AvidmW150"><span>*</span> Ϊ��ѡ/������</td>
			    <td>
			    	<div class="pt-formbutton" text="ȷ��"  onclick="saveSubmitForm();"></div>
			    	<div class="pt-formbutton" text="ȡ��"  onclick="cancleButton();"></div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
<script type="text/javascript">

	// ��Ŀ��ʼ��
	<%-- function initPage(){
		pt.ui.get("orderType").setValue("<%=dis.getOrderType()%>");
	} --%>

	// ��ť������ȷ����
	function saveSubmitForm(){
		var id = $("#id").val();
		var name = $("#name").val();
		var note = $("#note").val();
		
		if(name == ""){
			plm.showMessage({title:'������ʾ', message:"����������", icon:'1'});
			return;
		}
		
		$.ajax({
			   type: "post",
			   url: "<%=contextPath%>/ddm/distribute/distributePaperTaskHandle!updateDistributePaperTask.action",
			   dataType:"json",
			   data: $("#myform").serializeArray(),
			   success: function(result){
				   plm.showMessage({title:'��ʾ', message:"����ɹ�!", icon:'2'});
				   onAjaxExecuted();
			   },
			   error:function(){
				   plm.showMessage({title:'������ʾ', message:"�Բ��������������", icon:'1'});
			   }
			});
	}
	
	//�رձ�ҳ�沢���ø�ҳ��ˢ�·���
	function onAjaxExecuted(result){	
		opener.tableReload("�޸ĳɹ�");		
		window.close();
	}
	
	// ��ť������ȡ����
	function cancleButton(){
		window.close();
	}

	//�ӳٳ�ʼ��ҳ��
	setTimeout("initPage()",200);
</script>
