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
	//获取
	String contextPath = request.getContextPath();
	String title = "com.bjsasc.adm.activedocument.create";

	AdmModelService amService = AdmHelper.getAdmModelService();
	String requestFolderOid = request.getParameter(KeyS.FOLDER_OID);
	//文件夹参数
	Map<String,String> map = amService.getCreateActiveDocumentParam(requestFolderOid);
	//类型
	String classId = map.get(ConstUtil.CLASSID_PPRAM);
	//文件夹OID
	String folderOid = map.get(ConstUtil.FOLDEROID_PPRAM);
	//文件夹PATH
	String folderPath= map.get(ConstUtil.FOLDERPATH_PPRAM);
	//上下文OID
	String contextOid = map.get(ConstUtil.CONTEXTOID_PPRAM);
	//上下文NAME
	String contextName = map.get(ConstUtil.CONTEXTNAME_PPRAM);
	String date = DateTimeUtil.getCurrentDate(DateTimeUtil.DATE_YYYYMMDD);
	
	//根据初始化规则获取编码的获取方式	,如果初始化规则禁用，则默认为输入。
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
				<td class='left_col AvidmW150'>上下文：</td>
				<td>
					<%=contextName %>
				</td>
			</tr>			
			<tr>
				<td class='left_col AvidmW150'><span>*</span>类型：</td>
				<td>
					<select class="pt-select"  name='CLASSIDS' id='CLASSIDS' style="width:270px;" onchange="initActiveDocumentAttr(this)" 
						url="<%=contextPath%>/adm/activedocument/handleActiveDocument.jsp?OPERATE=getClassId&CLASSID=<%=classId%>">
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
						url="<%=contextPath%>/platform/objectmodel/param/page/SelectLookupTableItem.jsp?lookupTableId=activeCode">
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

$(document).ready(function() {
	var classId = "<%=classId%>";
	if(classId!="ActiveDocument"){getfield(classId);}
});

//提交表单
function submitForm() {
	<%=ValidateHelper.buildCheck()%>;
	// 现行文件类型检测
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

//改变模型时，动态获取现行文件关属性
function initActiveDocumentAttr(object){
	var classId = $("#CLASSIDS").val();
	if(object == undefined) { // 页面首次加载
		if(classId == "") {
			classId = $("#CLASSIDS").val(); // 从用户的最近访问获取
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
    getfield(classId); //更新扩展属性域
    changeFolder(classId);
}

// 现行文件类型扩展属性
function getfield(classId){
	plm.getExtAttr({trid:'includeDynamicFields',colnum:1,classid:classId,viewId:""});
}

// 现行文件类型选择，可以配置是否包含根节点
function openSelectPage(){
	plm.selectTypes({callback:"doCreateType",spot:'ActiveDocument',single:'true'});
};

//现行文件类型选择回调函数
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

// 现行文件类型检测
function validateClassId(){
	var result = true;
	var classId = $("#CLASSIDS").val();
	if(classId == "" || classId == "more"|| classId == "nothing") {
		plm.showMessage({title:'提示', message:"请选择文档类型!", icon:'1'});
		result = false;
	}
	return result;
}

//校验文件
function validateFile() {
	var iframes = window.frames["viewWindow"];
	var isHasFile = iframes.isHasFile(); // 判断是否有文件
	var isHasMainFile = iframes.isHasMainFile(); // 判断是否有主文件
	var validateUrl = iframes.validateUrl();
	if(validateUrl) { // URL有效
		if(isHasFile) { // 有文件
			if(isHasMainFile) {
				return true;
			} else {
				return confirm("未设置主文件,确认要创建现行文件吗?");
			}
		} else {
			return confirm("未提交文件,确认要创建现行文件吗?");
		}
	} else {
		plm.showMessage({title:'提示', message:"URL类型数据：标签和URL均不允许为空！", icon:'3'});
		return false;
	}
}

//根据上下文类型获取默认文件夹
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
		plm.showMessage({title:'提示', message:"请选择现行文件类型!", icon:'1'});
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