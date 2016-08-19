<%@page import="com.bjsasc.plm.core.context.rule.RuleHelper"%>
<%@page import="com.bjsasc.plm.core.context.rule.TypeBasedRule"%>
<%@page import="com.bjsasc.plm.core.session.SessionHelper"%>
<%@page import="com.bjsasc.plm.core.option.OptionValue"%>
<%@page import="com.bjsasc.plm.core.option.OptionManager"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="GBK"%>
<%@page session="true"%>
<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.operate.ActionUtil"%>
<%@page import="java.util.*"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.folder.FolderSelector"%>
<%@page import="java.util.Map" %>
<%@page import="com.bjsasc.adm.common.ConstUtil" %>
<%@page import="com.bjsasc.plm.core.folder.FolderHelper"%>
<%@page import="com.bjsasc.plm.core.type.TypeDefinition"%>
<%@page import="com.bjsasc.adm.active.model.activeorder.ActiveOrder" %>
<%@page import="com.bjsasc.plm.core.folder.Folder"%>
<%@page import="com.bjsasc.plm.core.Helper"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.platform.webframework.tag.util.JsonUtil"%>
<%@page import="com.bjsasc.plm.core.util.DateTimeUtil"%>
<%
	//��ȡ
	String contextPath = request.getContextPath();
	String title = "com.bjsasc.adm.activeorder.create";
	//�ļ���OID
	String folderOid = request.getParameter("FOLDER_OID");
	//����
	String classId = ActiveOrder.CLASSID;
	//�ļ���PATH
	Folder folder = (Folder) Helper.getPersistService().getObject(folderOid);
	String folderPath= FolderHelper.getService().getFolderPathStr(folder);
	Context context = folder.getContextInfo().getContext();
	//������OID
	String contextOid = context.getOid();
	//������NAME
	String contextName = context.getName();
	TypeDefinition baseModel =  Helper.getTypeService().getType(classId);
	String date = DateTimeUtil.getCurrentDate(DateTimeUtil.DATE_YYYYMMDD);
	
	//���ݳ�ʼ�������ȡ����Ļ�ȡ��ʽ	,�����ʼ��������ã���Ĭ��Ϊ���롣
	String editType = "input";
	TypeBasedRule rule = RuleHelper.getService().findRule("ActiveOrder", context);
	if (null != rule) {
		editType = rule.getRuleItemArg("number", "editType");
	}		
%>
<html>
<head>
<title><%=ActionUtil.getTitle(title)%></title>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath%>/plm/ui/editor/date/WdatePicker.js"></script>
</head>
<body  class="openWinPage" onload="">
	<jsp:include page="/plm/common/actionTitle.jsp">
		<jsp:param name="ACTIONID" value="<%=title%>"/>
	</jsp:include>
	<form id="form_doc" ui="form" action="" method="POST" name="form_doc">
		<input type="hidden" name="CLASSID" id="CLASSID" value="<%=classId %>"/>
		<input type="hidden" name="FOLDEROID" id="FOLDEROID" value="<%=folderOid%>"/>
		<input type="hidden" name="CONTEXT" id="CONTEXT" value="<%=contextOid%>"/>
		<input type="hidden" name="VERSIONFlAG" id="VERSIONFlAG" value="0"/>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
			<tr>
				<td class='left_col AvidmW150'>�����ģ�</td>
				<td><%=contextName %></td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span>*</span>���ͣ�</td>
				<td>
					<select class="pt-select"  name='CLASSIDS' id='CLASSIDS' style="width:270px;" 
						url="<%=contextPath%>/adm/activeorder/handleActiveOrder.jsp?OPERATE=getClassId&CLASSID=<%=classId%>">
					</select>
				</td>
			</tr>
			<tr id="iframeShow">
				<td class='left_col AvidmW150'>�ļ���</td>
				<td>
					<div>
		   				 <iframe id="viewWindow" name="viewWindow" scrolling="no" frameborder="0" src="<%=request.getContextPath() %>/plm/docman/FileOperate.jsp"   style="width: 100%; height:80;"> </iframe>
		    		</div>
		   	    </td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span>*</span>��ţ�</td>
				<td>  				
			    	<jsp:include page="/plm/code/numberinput/NumberInput.jsp">
						<jsp:param name="CLASSID" value="ActiveDocument"/>
						<jsp:param name="CONTEXT_OID" value="<%=contextOid %>"/>
					</jsp:include>	
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span>*</span>���ƣ�</td>
				<td><input type='text' name='NAME' id='NAME' class='pt-text pt-validatebox' style="width:270px" <%=ValidateHelper.buildValidator()%>></td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span>*</span>��Դ��</td>
				<td> 
					<input type='text' name='DATASOURCE' id='DATASOURCE' class='pt-text pt-validatebox' style="width:270px" <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>�����ļ���ţ�</td>
				<td> 
					<input type='text' name='ACTIVEDOCUMENTNUMBER' id='ACTIVEDOCUMENTNUMBER' class='pt-text pt-validatebox' style="width:270px"  readonly="readonly" ontrigger="selectActiveDocumentTemp()" >
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>ҳ����</td>
				<td> 
					<input type='text' name='PAGES' id='PAGES' class='pt-text pt-validatebox' style="width:270px" <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>������</td>
				<td> 
					<input type='text' name='COUNT' id='COUNT' class='pt-text pt-validatebox' style="width:270px" <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>�����ߣ�</td>
				<td> 
					<input type='text' name='AUTHORNAME' id='AUTHORNAME' class='pt-text pt-validatebox' style="width:270px" <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>���Ƶ�λ��</td>
				<td> 
					<input type='text' name='AUTHORUNIT' id='AUTHORUNIT' class='pt-text pt-validatebox' style="width:270px" <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>�������ڣ�</td>
				<td> 
				<input type='text' id="AUTHORTIME" name="AUTHORTIME" value="<%=date%>"
						class="Wdate pt-textbox" readonly="readonly"  style="width:100px;"
				        onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'1970-01-01',maxDate:'2128-03-10'})"/>			
				</td>
			</tr>
			<tr id="secLevelView" >
				<td class='left_col AvidmW150'>�ܼ���</td>
				<td>
					<SELECT name='SECLEVEL' id='SECLEVEL' class='pt-select' style="width: 270px;"
						url="<%=contextPath%>/adm/activedocument/handleActiveDocument.jsp?OPERATE=getSecLevel&CONTEXTOID=<%=contextOid%>">
					</SELECT>
				</td>
			</tr>
			<tr id="secLevelView" >
				<td class='left_col AvidmW150'>���ţ�</td>
				<td>
					<SELECT name='ACTIVECODE' id='ACTIVECODE' class='pt-select' style="width: 270px;"
						url="<%=contextPath%>/adm/activedocument/handleActiveDocument.jsp?OPERATE=getActiveCode&CONTEXTOID=<%=contextOid%>">
					</SELECT>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'>λ�ã�</td>
				<td>
					<input type="hidden" name="folderOid" id="folderOid" value="<%=folderOid%>"/>
		            <input type='text' name='folder' id='folder' class='pt-text'  value='<%=folderPath%>' readonly style="width:270px" >
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'>��������ģ�壺</td>
				<td>(�Զ�����)</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'>��ע��</td>
				<td><textarea name='NOTE' id='NOTE'  class='pt-textarea' style="width:270px;height:50px;" <%=ValidateHelper.buildValidatorAndAssisant()%>></textarea></td>
			</tr>
			<tr id="includeDynamicFields"></tr>
		</table>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">		
			<tr>
		        <td class="exegesis AvidmW150"><span>*</span> Ϊ��ѡ/������</td>	      
     	        <td>
     	          <div class="pt-formbutton"   id="savebtn" text="ȷ��" onclick="submitForm()" <%=ValidateHelper.buildCheckAll()%>></div>
     	          <div class="pt-formbutton" id="cancelbtn" text="ȡ��"  onclick="cancel()"></div> 
     	        </td>
	         </tr>
		  </table>
	</form>
</body>

<script type="text/javascript">

//�ύ��
function submitForm() {
	<%=ValidateHelper.buildCheck()%>;

	var beforeOid = getBeforeDataParam();
	var afterOid = getAfterDataParam();

	try {
	var beforeOids=top.window.frames["tab2"].getBeforeDataParam();
	var afterOids=top.window.frames["tab2"].getAfterDataParam();
	} catch (e) {
		beforeOids = "";
		afterOids = "";
	}

	plm.startWait();
	$.ajax({
		type: "post",
        url : "<%=contextPath%>/adm/active/ActiveOrderHandle!createActiveOrder.action?"
			+"beforeOids="+beforeOids
			+"&afterOids="+afterOids,
        dataType:"json",
    	data:$("#form_doc").serializeArray(),
        success: function(result) {
			plm.endWait();
			if (result.SUCCESS != null && result.SUCCESS =="false"){
				plm.showAjaxError(result);
			} else {
	          	var innerId = result.INNERID;
            	var classId = result.CLASSID;
				var iframes = window.frames["viewWindow"];
        		//������ļ����ϴ��ļ�
        		if(iframes.isHasFile()){
        			iframes.submitFile(innerId, classId);
        		}else{
        			callBack();
        		}
			}
        },
		error:function(){
			plm.endWait();
			plm.showMessage({title:'������ʾ', message:"���������ļ�����ʧ�ܣ�", icon:'1'});
		}
	});
}

function cancel(){
	window.close();
}

//function callBack(){
//	top.opener.location.reload();
//	top.close();
//}

function callBack(){
	try {
		var lu = top.opener.location.href;
		if (lu.indexOf("visit.jsp") > 0 || lu.indexOf("folderlist.jsp") > 0) {
			top.opener.location.reload();			
		} else {
			top.opener.parent.location.reload();
		}
	} catch (e) {alert(e.message);}
	top.close();
}

function getBeforeDataParam(){
	var beforeData="";
	try {
	var dataslist_before = top.window.frames["tab2"].getPagesData_before();
	for(var i=0;i<dataslist_before.length;i++){
		beforeData+=dataslist_before[i].OID+",";
	}
	if(beforeData.length>0){
		beforeData= beforeData.substring(0,beforeData.length-1);
	}
	} catch (e) {}
	return beforeData;
}
function getAfterDataParam(){
	var afterData="";
	try {
	var dataslist_after = top.window.frames["tab2"].getPagesData_after();
	for(var i=0;i<dataslist_after.length;i++){
		afterData+=dataslist_after[i].BEFOREOID+"/"+dataslist_after[i].OID+",";
	}
	if(afterData.length>0){
		afterData= afterData.substring(0,afterData.length-1);
	}
	} catch (e) {}
	return afterData;
}

function selectActiveDocumentTemp() {
	var beforeOid = getBeforeDataParam();
	selectActiveDocument('ACTIVEDOCUMENTNUMBER', '', beforeOid);
}
//�������
function applyCode(){
	//������ñ�ź���ѡ����ʾ��ʧ
	var num = document.getElementById("NUMBER");
	plm.hideTip(num);
	var number = $("#NUMBER").val();
	var classId = $("#CLASSIDS").val();
	//��ʱ��ȡĬ�ϵ�������oid
	var contextOid = "<%=contextOid %>";
	var applyType = "<%=editType%>"; 
	if(classId == "" || classId == "more"|| classId == "nothing") {
		plm.showMessage({title:'��ʾ', message:"��ѡ�����е�������!", icon:'1'});
		return;
	}
	var data ={
			CLASSID : classId,
			CONTEXT_OID :contextOid,
			NUMBER : number
	};
	var callback = "getCode";
	plm.getNumber(data,callback);
}
//��ȡ����
function  getCode(value){
	var number = value;
	if(number == undefined){
		number = "";
	}
	document.getElementById("NUMBER").value = number ;
};
</script>
</html>