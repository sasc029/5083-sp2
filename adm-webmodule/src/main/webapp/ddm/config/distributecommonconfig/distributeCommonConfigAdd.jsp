<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@ page import="com.bjsasc.platform.i18n.PLU"%>
<%@ page import="com.bjsasc.plm.url.Url"%>
<%@ page import="com.bjsasc.plm.ui.ValidateHelper"%>
<html>
<%
	String basePath = request.getContextPath();
%>
<head>
<jsp:include page="/platform/ui/Ui-js.jsp"></jsp:include>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
</head>
<body class="openWinPage">
<form id="addForm" ui="form" method="post">
<table class="pt-formtable" col="1">
	<tr>
		<td colspan="2" class="pt-formHeader">
			<div class="imgdiv">
				<img src="<%=basePath%>/platform/ui/images/add.gif" />
			</div>
			<div class="namediv">���ӷ��Ź���ͨ�����ò���</div>
		</td>
	</tr>
	<tr>
		<td class="pt-label"><font style="color: #FF0000">&nbsp;*</font>���ñ�ţ�</td>
		<td class="pt-field">
			<INPUT type='text' name="configID" id='configID' class="pt-text" <%=ValidateHelper.buildValidator()%>/>
			<input type="hidden" name="distributeCommonConfig.configID" id="distributeCommonConfig_configID"/>
		</td>
	</tr>
	<tr>
		<td class='pt-label'><font style="color: #FF0000">&nbsp;*</font>�������ƣ�</td>
		<td class='pt-field'>
			<INPUT type="text" name="configName" id='configName' class="pt-text" <%=ValidateHelper.buildValidator()%>/>
			<input type="hidden" name="distributeCommonConfig.configName" id="distributeCommonConfig_configName"/>
		</td>
	</tr>
	<tr>
		<td class='pt-label'><font style="color: #FF0000">&nbsp;*</font>����ֵ��</td>
		<td class='pt-field'>
			<input type="text" name="configValue" id="configValue" class="pt-text" <%=ValidateHelper.buildValidator()%>/>
			<input type="hidden" name="distributeCommonConfig.configValue" id="distributeCommonConfig_configValue"/>
		</td>
	</tr>
	<tr>
		<td class='pt-label'><font style="color: #FF0000">&nbsp;*</font>����Ĭ��ֵ��</td>
		<td class='pt-field'>
			<input type="text" name="configDefaultValue" id="configDefaultValue" class="pt-text" <%=ValidateHelper.buildValidator()%>/>
			<input type="hidden" name="distributeCommonConfig.configDefaultValue" id="distributeCommonConfig_configDefaultValue"/>
		</td>
	</tr>
	<tr>
		<td class="pt-label">�Ƿ�����ɾ����</td>
		<td>
			<input type="radio" name="distributeCommonConfig.isPermitDelete"checked="checked" value="1">��
			<input type="radio" name="distributeCommonConfig.isPermitDelete" value="0">��
			<font style="color: #FF0000">ע�⣺��ѡ��ѡ���������޸�</font>
		</td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td style="font-size: 12px;color: #999;line-height: 36px;width: 146px;">&nbsp;&nbsp;<span style="color:#F00; padding-right:3px;">*</span> Ϊ��ѡ/������</td>
   		<td>
			<div class="pt-formbutton"  text="����" onclick="configAdd()" <%=ValidateHelper.buildCheckAll()%>></div>
			<div class="pt-formbutton" text="<%=PLU.getString("btn.cancel")%>" onclick="window.close()"></div>
		</td>
	</tr>
</table>
</form>
</body>
<script type="text/javascript">
function configAdd(){
	
	<%=ValidateHelper.buildCheck()%>
	
	var configID = document.getElementById("configID").value;
	$("#distributeCommonConfig_configID").val(configID);
	
	var configName = document.getElementById("configName").value;
	$("#distributeCommonConfig_configName").val(configName);
	
	var configValue = document.getElementById("configValue").value;
	$("#distributeCommonConfig_configValue").val(configValue);
	
	var configDefaultValue = document.getElementById("configDefaultValue").value;
	$("#distributeCommonConfig_configDefaultValue").val(configDefaultValue);
	
	if(configID == null || configID == ""){
		plm.showMessage({title:'��ʾ', message:"���ñ�Ų���Ϊ��!", icon:'1'});
		return;
	}
	if(configName == null || configName == ""){
		plm.showMessage({title:'��ʾ', message:"�������Ʋ���Ϊ��", icon:'1'});
		return;
	}
	if(configValue == null || configValue == ""){
		plm.showMessage({title:'��ʾ', message:"����ֵ����Ϊ��!", icon:'1'});
		return;
	}
	if(configDefaultValue == null || configDefaultValue == ""){
		plm.showMessage({title:'��ʾ', message:"Ĭ�����ò���Ϊ��!", icon:'1'});
		return;
	}
	if(checkId()){
		plm.showMessage({title:'��ʾ', message:"���ñ���Ѵ���", icon:"1"});
		return;
	}
	var options={
		url: "<%=basePath%>/ddm/distribute/distributeCommonConfigHandle!addDistributeCommonConfig.action",
		type : "post",
		success : function(result) {
			if (result == 'success') {
				plm.showMessage({title:'��ʾ', message:"���óɹ�!", icon:"2"});
				window.close();
				opener.reload();
			}
		}
	};
	pt.ui.get("addForm").submit(options);
}
//���ñ���Ƿ��Ѿ�����
function checkId(){
	var configID = $("#configID").val();
	var result = false;
	//true �Ѵ���  false ������
	$.ajax({
		type: "POST",
		url: "<%=basePath%>/ddm/distribute/distributeCommonConfigHandle!checkConfigID.action?configID="+configID,
		async: false,
		success: function(msg){
			if(msg == 'true'){
				result = true;
			}else{
				result = false;
			}
		}
	});
	return result;
}
</script>
</html>
