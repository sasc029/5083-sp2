<%@page import="com.bjsasc.plm.core.context.rule.RuleHelper"%>
<%@page import="com.bjsasc.plm.core.context.rule.TypeBasedRule"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
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
<%@page import="com.bjsasc.adm.active.helper.AdmHelper" %>
<%@page import="com.bjsasc.adm.active.service.admmodelservice.AdmModelService" %>
<%@page import="java.util.Map" %>
<%@page import="com.bjsasc.adm.common.ConstUtil" %>
<%@page errorPage="/plm/ajaxError.jsp"%>
<%@page import="com.bjsasc.plm.core.util.DateTimeUtil"%>
<%
	//��ȡ
	String contextPath = request.getContextPath();
	String title = "com.bjsasc.adm.activedocument.create";

	AdmModelService amService = AdmHelper.getAdmModelService();
	String requestFolderOid = request.getParameter(KeyS.FOLDER_OID);
	//�ļ��в���
	Map<String,String> map = amService.getCreateActiveDocumentParam(requestFolderOid);
	//����
	String classId = map.get(ConstUtil.CLASSID_PPRAM);
	//�ļ���OID
	String folderOid = map.get(ConstUtil.FOLDEROID_PPRAM);
	//�ļ���PATH
	String folderPath= map.get(ConstUtil.FOLDERPATH_PPRAM);
	//������OID
	String contextOid = map.get(ConstUtil.CONTEXTOID_PPRAM);
	//������NAME
	String contextName = map.get(ConstUtil.CONTEXTNAME_PPRAM);
	String date = DateTimeUtil.getCurrentDate(DateTimeUtil.DATE_YYYYMMDD);
	
	//���ݳ�ʼ�������ȡ����Ļ�ȡ��ʽ	,�����ʼ��������ã���Ĭ��Ϊ���롣
	String editType = "input";
	Context context = (Context)Helper.getPersistService().getObject(contextOid);
	TypeBasedRule rule = RuleHelper.getService().findRule("ActiveDocument", context);
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
		<jsp:param name="ACTIONID" value="<%=title %>"/>
	</jsp:include>
	<form id="form_doc" ui="form" action="" method="POST" name="form_doc">
		<input type="hidden" name="CLASSID" id="CLASSID" value="<%=classId %>"/>
		<input type="hidden" name="CONTEXT" id="CONTEXT" value="<%=contextOid%>"/>
		<input type="hidden" name="VERSIONFlAG" id="VERSIONFlAG" value="0"/>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
			<tr>
				<td class='left_col AvidmW150'>�����ģ�</td>
				<td>
					<%=contextName %>
				</td>
			</tr>			
			<tr>
				<td class='left_col AvidmW150'><span>*</span>���ͣ�</td>
				<td>
					<select class="pt-select"  name='CLASSIDS' id='CLASSIDS' style="width:270px;" onchange="initActiveDocumentAttr(this)" 
						url="<%=contextPath%>/adm/activedocument/handleActiveDocument.jsp?OPERATE=getClassId&CLASSID=<%=classId%>">
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
						url="<%=contextPath%>/platform/objectmodel/param/page/SelectLookupTableItem.jsp?lookupTableId=activeCode">
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

$(document).ready(function() {
	var classId = "<%=classId%>";
	if(classId!="ActiveDocument"){getfield(classId);}
});

//�ύ��
function submitForm() {
	<%=ValidateHelper.buildCheck()%>;
	// �����ļ����ͼ��
	if(!validateClassId()){
		return;
	}

	plm.startWait();
	    $.ajax({
			url:"<%=contextPath%>/adm/active/ActiveDocumentHandle!createActiveDocument.action",
			type:"post",
			dataType:"json",
			data:$("#form_doc").serializeArray(),
			success:function(result){
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

//�ı�ģ��ʱ����̬��ȡ�����ļ�������
function initActiveDocumentAttr(object){
	var classId = $("#CLASSIDS").val();
	if(object == undefined) { // ҳ���״μ���
		if(classId == "") {
			classId = $("#CLASSIDS").val(); // ���û���������ʻ�ȡ
			if(classId == "" || classId == "more" || classId =="nothing") {
				classId = "ActiveDocument";
			}
		}
	} else {
		var selectClassId = object.options[object.selectedIndex].value;
		if(selectClassId == "more"){
			openSelectPage();
			return;
		}else if(selectClassId == "nothing"){
			return;
		}else{
			classId = selectClassId;
			$("#CLASSIDS").val(classId);
			$("#CLASSID").val(classId);
		}
	}
    getfield(classId); //������չ������
    changeFolder(classId);
}

// �����ļ�������չ����
function getfield(classId){
	plm.getExtAttr({trid:'includeDynamicFields',colnum:1,classid:classId,viewId:""});
}

// �����ļ�����ѡ�񣬿��������Ƿ�������ڵ�
function openSelectPage(){
	plm.selectTypes({callback:"doCreateType",spot:'ActiveDocument',single:'true'});
};

//�����ļ�����ѡ��ص�����
function doCreateType(result){
	var dataList = UI.JSON.decode(result);
    if(typeof(dataList) != "undefined"){
        var name = dataList[0].Name;
        var id = dataList[0].id;
        var object = document.getElementById("CLASSIDS");
        var op=document.createElement('option');
        var len = object.options.length;
         
        op.text = name;
        op.value = id;
        for(var i = 0;i < len; i++ ){
        	if(object.options[i].value == id){
        		object.options[i].selected = true;
        		break;
        	}else if(i == len-1){
        		object.add(op);
                op.selected = true;
        	}
        }
        initActiveDocumentAttr(object);
    }
}

// �����ļ����ͼ��
function validateClassId(){
	var result = true;
	var classId = $("#CLASSIDS").val();
	if(classId == "" || classId == "more"|| classId == "nothing") {
		plm.showMessage({title:'��ʾ', message:"��ѡ���ĵ�����!", icon:'1'});
		result = false;
	}
	return result;
}

//У���ļ�
function validateFile() {
	var iframes = window.frames["viewWindow"];
	var isHasFile = iframes.isHasFile(); // �ж��Ƿ����ļ�
	var isHasMainFile = iframes.isHasMainFile(); // �ж��Ƿ������ļ�
	var validateUrl = iframes.validateUrl();
	if(validateUrl) { // URL��Ч
		if(isHasFile) { // ���ļ�
			if(isHasMainFile) {
				return true;
			} else {
				return confirm("δ�������ļ�,ȷ��Ҫ���������ļ���?");
			}
		} else {
			return confirm("δ�ύ�ļ�,ȷ��Ҫ���������ļ���?");
		}
	} else {
		plm.showMessage({title:'��ʾ', message:"URL�������ݣ���ǩ��URL��������Ϊ�գ�", icon:'3'});
		return false;
	}
}

//�������������ͻ�ȡĬ���ļ���
function getCabinetByContext(contextOid){
	var tempContextOid = contextOid;
	if(typeof(tempContextOid) == "undefined" ){
		tempContextOid = "<%=contextOid%>";
	}
	var url = "<%=contextPath%>/adm/activedocument/handleActiveDocument.jsp?OPERATE=getCabinetByContext&CONTEXTOID="+tempContextOid;
	var param = {
        type: "POST",
        url : url,
        dataType:"json",
        success: function(result) {
        	document.getElementById("folderOid").value = result.FOLDEROID;
        	document.getElementById("folder").value = result.FOLDERPATH;
        }
    };
	pt.ui.ajax(param);
}
function changeFolder(classId){
	if(typeof(classId) == "undefined" || classId =="ActiveDocument"){
		return;
	}
	var url = "<%=contextPath%>/adm/activedocument/handleActiveDocument.jsp?OPERATE=getFolderByClassID&CLASSID="+classId;
	var param = {
        type: "POST",
        url : url,
        dataType:"json",
        success: function(result) {
        	document.getElementById("folderOid").value = result.FOLDEROID;
        	document.getElementById("folder").value = result.FOLDERPATH;
        }
    };
	pt.ui.ajax(param);
}
function callBack(){
	top.opener.location.reload();
	top.close();
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
		plm.showMessage({title:'��ʾ', message:"��ѡ�������ļ�����!", icon:'1'});
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