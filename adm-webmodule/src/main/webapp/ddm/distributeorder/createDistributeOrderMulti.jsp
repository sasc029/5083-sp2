<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.plm.core.doc.Document"%>
<%@page import="com.bjsasc.plm.core.doc.DocumentMaster"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.core.context.rule.RuleHelper"%>
<%@page import="com.bjsasc.plm.core.context.rule.TypeBasedRule"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.core.context.model.Contexted"%>
<%@page import="com.bjsasc.plm.core.context.model.ContextInfo"%>
	
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService"%>
<%@page import="com.bjsasc.ddm.transfer.service.DdmDataTransferService"%>
<%@page import="com.bjsasc.ddm.transfer.helper.DdmDataTransferHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService"%>
<%@page import="com.cascc.avidm.util.SplitString"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%@include file="/plm/code/numberinput/NumberInput_include.jsp"%>

<%
	// ����ת������
	String contextPath = request.getContextPath();
	String title = "�������ŵ�";
	String classId = "DistributeOrder";
	String oids = request.getParameter("oids");
	List<String> oidList = SplitString.string2List(oids, ",");
	DistributeObjectService disObjService = DistributeHelper.getDistributeObjectService();
    String orderTypeName= "";
	DistributeObject disObject;
	String disObjVersion;
	String orderType= "";
	String number = "";
	String name= "";
	//String note = "";
	//List<DistributeObject> list = new ArrayList<DistributeObject>();
	//disObjList��ֻ��һ������
	String oidForOrder = "";
	
	for (String oid : oidList) {
		List<DistributeObject> disObjList = disObjService.getDistributeObjectsByDataOid(oid);
		if (disObjList == null || disObjList.isEmpty()) {
			if("".equals(oidForOrder)){
				orderType = ConstUtil.C_ORDERTYPE_0;
				orderTypeName="���ŵ�";
				oidForOrder = oid;
			}
			//note= disObject.getNote();
			continue;
		} else {
			orderType = ConstUtil.C_ORDERTYPE_1;
			orderTypeName="�������ŵ�";
			disObject = disObjList.get(0);
			disObjVersion=disObjService.getCurrentDisObjectVersion(oid);
			
			number = disObject.getNumber() + ConstUtil.C_DISTRIBUTEORDER_1+disObjVersion;
			name= disObject.getName() + ConstUtil.C_DISTRIBUTEORDER_1+disObjVersion;
			/** ���ŵ���ַ�����Link���� */
			DistributeOrderObjectLinkService linkService = DistributeHelper.getDistributeOrderObjectLinkService();
			int seriaNo = linkService.getDistributeOrderObjectLinkListByDistributeObjectOid(Helper.getOid(disObject))
					.size();
			number = number + "-" + String.valueOf(seriaNo);
			name = name + "-" + String.valueOf(seriaNo);
			//note= disObject.getNote();
			break;
		}
	}
	//��������ڲ��������ݵ����
	if(ConstUtil.C_ORDERTYPE_0.equals(orderType)){
		// ����ת������
		DdmDataTransferService tranService = DdmDataTransferHelper.getDistributeObjectService();
		Persistable obj = Helper.getPersistService().getObject(oidForOrder);
		disObject = tranService.transferToDdmData(obj);
		disObjVersion=disObjService.getCurrentDisObjectVersion(oidForOrder);	
		number = disObject.getNumber() + ConstUtil.C_DISTRIBUTEORDER_0+disObjVersion;
		name= disObject.getName() + ConstUtil.C_DISTRIBUTEORDER_0+disObjVersion;
	}
	
		
	//���ݳ�ʼ�������ȡ����Ļ�ȡ��ʽ	,�����ʼ��������ã���Ĭ��Ϊ���롣
	String oid = oidList.get(0);
	String editType = "input";
	String contextOid = "";
	Persistable obj = Helper.getPersistService().getObject(oid);
	if (obj instanceof Contexted) {
		ContextInfo contextInfo = ((Contexted)obj).getContextInfo();
		Context context = contextInfo.getContext();
		contextOid = context.getOid();
		TypeBasedRule rule = RuleHelper.getService().findRule("DistributeOrder", context);
		if (null != rule) {
			editType = rule.getRuleItemArg("number", "editType");
		}
	}
	//�����  "select"�Զ�������룻"mix"�Զ�������룬�������ֹ��޸ģ���ʱ��
	//���Զ����� number,��Ҫ��ǰ�߱༭�õ�number�������óɿ�
	if("select".equals(editType) || "mix".equals(editType)){
		number = "";
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
				<div class="imgdiv"><img src="<%=contextPath%>/plm/images/project/createproject.gif"/></div>
				<div class="namediv"><%=title%></div>
			</td>
		</tr>
	</table>
	<form id="form_distributeorder" name="form_distributeorder" ui="form" action="" method="POST">
	    <input type="hidden" name="CLASSID" id="CLASSID" value="<%=classId%>"/>
	    <input type="hidden" name="oids" id="oids" value="<%=oids%>"/>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
			<tr>
			    <td class="left_col AvidmW150" ><span>*</span>��ţ�</td>
			    <td>
    	<jsp:include page="/plm/code/numberinput/NumberInput.jsp">
			<jsp:param name="CLASSID" value="DistributeOrder"/>
			<jsp:param name="CONTEXT_OID" value="<%=contextOid %>"/>
		</jsp:include>	
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span>*</span>���ŵ����ƣ�</td>
			    <td>
					<input type='text' name='NAME' id='NAME' class='pt-text pt-validatebox AvidmW270' value="<%=name%>" <%=ValidateHelper.buildValidator()%>>
			    </td>
			</tr>
			<TR>
				<TD class='left_col AvidmW150'>�������ͣ�</TD>
				<TD cols="1" rows="2">
				<!--  
					<SELECT id="orderType" name='orderType' class='pt-select AvidmW270'  url="<%=contextPath%>/plm/common/select/getSelect.jsp?select=orderType" >
					</SELECT>
					-->
					<input type='text' name='type' id='type' class='pt-text pt-validatebox AvidmW270' value="<%=orderTypeName%>" readonly = "readonly">
					<input type='hidden' name='orderType' id='orderType' class='pt-text pt-validatebox AvidmW270' value="<%=orderType%>">
				</TD>
			</TR>
			<tr>
			 	<td class="left_col AvidmW150">��ע��</td>
				<td>
					<textarea name='NOTE' id='NOTE' class='pt-textarea' style="width:270px;height:50px;" <%=ValidateHelper.buildValidatorAndAssisant()%>></textarea>
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
	var createOrderFlag = "false";// ���ڷ��ŵ��Ƿ񴴽��ɹ��ı�ʾ������ȷ���Ƿ������ա�//�رմ���ʱ���ձ���
	window.onbeforeunload = function(){
		codeRecycle();
		//window.close();
	};
	window.onload = function(){
		codeRecycle();
	};
	
	// �������Ĭ��ֵ��
	$(document).ready(function(){
		if ($("#NUMBER").val() == "") {
			$("#NUMBER").val("<%=number%>");
		}
	});
	
	//�����ύ��
	function saveSubmitForm() {
		<%=ValidateHelper.buildCheck()%>
		plm.startWait();
		var main_form = pt.ui.get("form_distributeorder");
		var options = {
				
				url:"<%=contextPath%>/ddm/distribute/distributeOrderHandle!createDistributeOrderMulti.action",
				dataType:"json",
				type:"POST",
				success:function(result) {
					plm.endWait();
					if(result.SUCCESS != null &&result.SUCCESS == "false"){
						plm.showAjaxError(result);
					}else if(result.SUCCESS == "message"){
						var mes=result.MESSAGE;
						plm.showMessage({title:'������ʾ', message:mes, icon:'1'});
					}else if(result.FLAG == "false"){
						alert("��û�в���Ȩ�ޣ�");
						return;
					}else{
						createOrderFlag = "true";//������ʾ���ŵ������ɹ�
						setState();
						var tempUrl = '<%=contextPath%>/ddm/public/visitObject.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + result.DISORDEROID;
						top.location.href = tempUrl;

						//plm.showMessage({title:'��ʾ', message:"�������ŵ������ɹ�!", icon:'2'});
						//top.opener.location.reload();
						plm.ddmReload();
					}
				},
				error:function(a){
					plm.endWait();
					plm.showMessage({title:'������ʾ', message:"����ʧ�ܣ�", icon:'1'});
				}
			};
		main_form.submit(options);
	};
	
	function cancleButton(){
		codeRecycle();
		window.close();
	};

	//�ر��޸�ҳ�沢���ø�ҳ��ˢ�·���
	function onAjaxExecuted(result){
		window.close();
	};

	function setState(){
		var num = document.getElementById("NUMBER");
		num.onblur=function(){};
		//$("#NUMBER").unbind();
	};
	
	//�������
	function applyCode(){
		//������ñ�ź���ѡ����ʾ��ʧ
		var num = document.getElementById("NUMBER");
		plm.hideTip(num);
		var number = $("#NUMBER").val();
		var classId = "DistributeOrder";
		//��ʱ��ȡĬ�ϵ�������oid
		var contextOid = "<%=contextOid %>";
		var applyType = "<%=editType%>"; 
		var data ={
				CLASSID : classId,
				CONTEXT_OID :contextOid,
				NUMBER : number
		};
		var callback = "getCode";
		plm.getNumber(data,callback);
	};
	//��ȡ����
	function  getCode(value){
		var number = value;
		if(number == undefined){
			number = "";
		}
		document.getElementById("NUMBER").value = number ;
	};	
	//�������
	function codeRecycle(){
		var code = document.getElementById("NUMBER").value;
		if(code != null && code != "" && createOrderFlag != "true"){
			plm.codeRecycle(code);
		}
	};
</script>
