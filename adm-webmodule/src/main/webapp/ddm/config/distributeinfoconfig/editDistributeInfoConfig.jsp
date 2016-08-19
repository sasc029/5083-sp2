<%@page import="com.bjsasc.plm.core.persist.PersistHelper"%>
<%@page import="java.util.*"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.cascc.avidm.util.SplitString"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.core.system.principal.Role"%>
<%@page import="com.bjsasc.plm.core.system.principal.Principal"%>
<%@page import="com.bjsasc.plm.core.system.principal.OrganizationHelper"%>
<%@page import="com.bjsasc.plm.core.system.principal.UserHelper"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeinfoconfig.DistributeInfoConfig"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeinfoconfig.DistributeInfoConfigService"%>
<%@page import="com.bjsasc.plm.core.system.principal.Organization"%>
<%@page import="com.bjsasc.plm.core.system.principal.User"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	// ����ת������
	String contextPath = request.getContextPath();
	String title = "Ĭ�Ϸַ���Ϣ�޸�";
	String classId = "DistributeInfoConfig";
	String oid = request.getParameter("oid");
	Persistable persist=Helper.getPersistService().getObject(oid);
	DistributeInfoConfig infoConfig=(DistributeInfoConfig)persist;
	
	
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
	<form id="edit_infoconfig_form" name="edit_infoconfig_form" ui="form" action="" method="POST">
	  <input type="hidden" name="oid" id="oid" value="<%=oid%>"/>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
			
			<tr>
			    <td class="left_col AvidmW150" >��λ/��Ա��</td>
			   <TD cols="1" rows="2">
					<input type='text' name='disInfoName' id='disInfoName' class='pt-text pt-validatebox AvidmW270' disabled="disabled" value="<%=infoConfig.getDisInfoName() %>">
				</TD>
			</tr>
			<TR>
				<TD class='left_col AvidmW150'>�ַ����ͣ�</TD>
				
				 <td>
					<input type='text' name='disInfoType' id='disInfoType' class='pt-text pt-validatebox AvidmW270' disabled="disabled" 
					<%
					
					if(infoConfig.getDisInfoType().equals("0")) {
						%>	
						value="��λ"
						<%	
					}if(infoConfig.getDisInfoType().equals("1")) {
						%>	
						value="��Ա"
					<%	
					}
					
					%> >
			    </td>
			</TR>
			<TR>
				<TD class='left_col AvidmW150'>�ַ���ʽ��</TD>
				<TD cols="1" rows="2">
				<SELECT id="disMediaType" name='disMediaType' class='pt-select AvidmW270' onchange="disableNum()">
					<%
					
					if(infoConfig.getDisMediaType().equals("0")) {
						%>	
						<option value="0" selected="selected">ֽ�� </option>	
						<option value="1">���� </option>		
						<%	
					}if(infoConfig.getDisMediaType().equals("1")) {
						%>	
						<option value="1" selected="selected">���� </option>	
						<option value="0">ֽ�� </option>		
					<%	
					}
					
					%> >
					  
				</SELECT>
				</TD>
			</TR>
			
			<TR>
				<TD class='left_col AvidmW150'>�ַ�������</TD>
				<TD cols="1" rows="2">
				<%
					if(infoConfig.getDisMediaType().equals("1")) {
						%>
						<input type='text' name='disInfoNum' id='disInfoNum' class='pt-text pt-validatebox AvidmW270' value="<%=infoConfig.getDisInfoNum() %>"  disabled="disabled" >		
					<%	
					}else{
					%>
					<input type='text' name='disInfoNum' id='disInfoNum' class='pt-text pt-validatebox AvidmW270' value="<%=infoConfig.getDisInfoNum() %>" >
					<%	
					}
					
					%>
				</TD>
			</TR>
			
			<tr>
			 	<td class="left_col AvidmW150">��ע��</td>
				<td>
					<textarea name='note' id='note' class='pt-textarea' style="width:270px;height:50px;" value="<%=infoConfig.getNote()%>"></textarea>
				</td>
			</tr>
		</table>

		<table width="100%" height="40px" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="exegesis AvidmW150"><span>*</span> Ϊ��ѡ/������</td>
			    <td>
					<div class="pt-formbutton" text="ȷ��"  onclick="saveSubmitForm();" ></div>
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
	
	 var oid=$("#oid").val();
	 var disMediaType=$("#disMediaType").val();
	 var disInfoNum=$("#disInfoNum").val();
	 var note=$("#note").val();
	 $.ajax({
		type: "post",
		url: "<%=contextPath%>/ddm/config/distributeinfoconfig/distributeInfoConfigAction.jsp?op=editUserOrOrg",
		dataType: "json",
		data: {oid:oid,disMediaType:disMediaType,disInfoNum:disInfoNum,note:note},
		success: function(result){
			if(result.SUCCESS != null && result.SUCCESS == "false"){
				plm.showAjaxError(result);
				return;
			}
			opener.reload();
			window.close();
			
	    },
	    error:function(){
			plm.showMessage({title:'������ʾ', message:"����ʧ�ܣ�", icon:'1'});
	    }
	});
}

function disableNum(){
	var disMediaType=$("#disMediaType").val();
	if(disMediaType==1){
		$("#disInfoNum").attr("disabled","disabled");
		$("#disInfoNum").val("1");
	}else{
		$("#disInfoNum").removeAttr("disabled");
	}
}

function cancleButton(){
	window.close();
}
</script>
