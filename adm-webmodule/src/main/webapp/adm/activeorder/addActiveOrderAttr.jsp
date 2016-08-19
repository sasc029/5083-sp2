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
	//获取
	String contextPath = request.getContextPath();
	String title = "com.bjsasc.adm.activeorder.create";
	//文件夹OID
	String folderOid = request.getParameter("FOLDER_OID");
	//类型
	String classId = ActiveOrder.CLASSID;
	//文件夹PATH
	Folder folder = (Folder) Helper.getPersistService().getObject(folderOid);
	String folderPath= FolderHelper.getService().getFolderPathStr(folder);
	Context context = folder.getContextInfo().getContext();
	//上下文OID
	String contextOid = context.getOid();
	//上下文NAME
	String contextName = context.getName();
	TypeDefinition baseModel =  Helper.getTypeService().getType(classId);
	String date = DateTimeUtil.getCurrentDate(DateTimeUtil.DATE_YYYYMMDD);
	
	//根据初始化规则获取编码的获取方式	,如果初始化规则禁用，则默认为输入。
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
				<td class='left_col AvidmW150'>上下文：</td>
				<td><%=contextName %></td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span>*</span>类型：</td>
				<td>
					<select class="pt-select"  name='CLASSIDS' id='CLASSIDS' style="width:270px;" 
						url="<%=contextPath%>/adm/activeorder/handleActiveOrder.jsp?OPERATE=getClassId&CLASSID=<%=classId%>">
					</select>
				</td>
			</tr>
			<tr id="iframeShow">
				<td class='left_col AvidmW150'>文件：</td>
				<td>
					<div>
		   				 <iframe id="viewWindow" name="viewWindow" scrolling="no" frameborder="0" src="<%=request.getContextPath() %>/plm/docman/FileOperate.jsp"   style="width: 100%; height:80;"> </iframe>
		    		</div>
		   	    </td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span>*</span>编号：</td>
				<td>  				
			    	<jsp:include page="/plm/code/numberinput/NumberInput.jsp">
						<jsp:param name="CLASSID" value="ActiveDocument"/>
						<jsp:param name="CONTEXT_OID" value="<%=contextOid %>"/>
					</jsp:include>	
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span>*</span>名称：</td>
				<td><input type='text' name='NAME' id='NAME' class='pt-text pt-validatebox' style="width:270px" <%=ValidateHelper.buildValidator()%>></td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span>*</span>来源：</td>
				<td> 
					<input type='text' name='DATASOURCE' id='DATASOURCE' class='pt-text pt-validatebox' style="width:270px" <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>现行文件编号：</td>
				<td> 
					<input type='text' name='ACTIVEDOCUMENTNUMBER' id='ACTIVEDOCUMENTNUMBER' class='pt-text pt-validatebox' style="width:270px"  readonly="readonly" ontrigger="selectActiveDocumentTemp()" >
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>页数：</td>
				<td> 
					<input type='text' name='PAGES' id='PAGES' class='pt-text pt-validatebox' style="width:270px" <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>份数：</td>
				<td> 
					<input type='text' name='COUNT' id='COUNT' class='pt-text pt-validatebox' style="width:270px" <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>编制者：</td>
				<td> 
					<input type='text' name='AUTHORNAME' id='AUTHORNAME' class='pt-text pt-validatebox' style="width:270px" <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>编制单位：</td>
				<td> 
					<input type='text' name='AUTHORUNIT' id='AUTHORUNIT' class='pt-text pt-validatebox' style="width:270px" <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>编制日期：</td>
				<td> 
				<input type='text' id="AUTHORTIME" name="AUTHORTIME" value="<%=date%>"
						class="Wdate pt-textbox" readonly="readonly"  style="width:100px;"
				        onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'1970-01-01',maxDate:'2128-03-10'})"/>			
				</td>
			</tr>
			<tr id="secLevelView" >
				<td class='left_col AvidmW150'>密级：</td>
				<td>
					<SELECT name='SECLEVEL' id='SECLEVEL' class='pt-select' style="width: 270px;"
						url="<%=contextPath%>/adm/activedocument/handleActiveDocument.jsp?OPERATE=getSecLevel&CONTEXTOID=<%=contextOid%>">
					</SELECT>
				</td>
			</tr>
			<tr id="secLevelView" >
				<td class='left_col AvidmW150'>代号：</td>
				<td>
					<SELECT name='ACTIVECODE' id='ACTIVECODE' class='pt-select' style="width: 270px;"
						url="<%=contextPath%>/adm/activedocument/handleActiveDocument.jsp?OPERATE=getActiveCode&CONTEXTOID=<%=contextOid%>">
					</SELECT>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'>位置：</td>
				<td>
					<input type="hidden" name="folderOid" id="folderOid" value="<%=folderOid%>"/>
		            <input type='text' name='folder' id='folder' class='pt-text'  value='<%=folderPath%>' readonly style="width:270px" >
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'>生命周期模板：</td>
				<td>(自动生成)</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'>备注：</td>
				<td><textarea name='NOTE' id='NOTE'  class='pt-textarea' style="width:270px;height:50px;" <%=ValidateHelper.buildValidatorAndAssisant()%>></textarea></td>
			</tr>
			<tr id="includeDynamicFields"></tr>
		</table>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">		
			<tr>
		        <td class="exegesis AvidmW150"><span>*</span> 为必选/必填项</td>	      
     	        <td>
     	          <div class="pt-formbutton"   id="savebtn" text="确定" onclick="submitForm()" <%=ValidateHelper.buildCheckAll()%>></div>
     	          <div class="pt-formbutton" id="cancelbtn" text="取消"  onclick="cancel()"></div> 
     	        </td>
	         </tr>
		  </table>
	</form>
</body>

<script type="text/javascript">

//提交表单
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
        		//如果有文件则上传文件
        		if(iframes.isHasFile()){
        			iframes.submitFile(innerId, classId);
        		}else{
        			callBack();
        		}
			}
        },
		error:function(){
			plm.endWait();
			plm.showMessage({title:'错误提示', message:"创建现行文件操作失败！", icon:'1'});
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
//申请编码
function applyCode(){
	//点击后让编号后面选项提示消失
	var num = document.getElementById("NUMBER");
	plm.hideTip(num);
	var number = $("#NUMBER").val();
	var classId = $("#CLASSIDS").val();
	//暂时获取默认的上下文oid
	var contextOid = "<%=contextOid %>";
	var applyType = "<%=editType%>"; 
	if(classId == "" || classId == "more"|| classId == "nothing") {
		plm.showMessage({title:'提示', message:"请选择现行单据类型!", icon:'1'});
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
//获取编码
function  getCode(value){
	var number = value;
	if(number == undefined){
		number = "";
	}
	document.getElementById("NUMBER").value = number ;
};
</script>
</html>