<%@page import="com.bjsasc.plm.core.util.ListUtil"%>
<%@page import="com.bjsasc.plm.util.JsonUtil"%>
<%@page import="com.bjsasc.plm.type.type.Type"%>
<%@page import="com.bjsasc.plm.type.attr.Attr"%>
<%@page import="com.bjsasc.plm.type.TypeS"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="java.util.HashMap"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.adm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@include file="/plm/plm.jsp" %>
<%
	//ģ������
	String typeIdsStr = (String)request.getAttribute("typeIdsStr");
	List<String> typeIds = ListUtil.toList(typeIdsStr);
	//plm����չʾģ��
	Map<String, Object> types = (Map<String, Object>)request.getAttribute("types");
	String oids =(String)request.getAttribute("oids");
	String spot = ConstUtil.SPOT_LISTACTIVEDOCUMENT;
	String gridId = ConstUtil.GRID_LISTACTIVEDOCUMENT;
	String gridTitle = "�����ļ��������б�";
	String toolbarId = "activedocument.operate";
%>
<html>
<head>
<title>�����༭�����ļ�</title>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/plm/ui/editor/date/WdatePicker.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/plm/ui/editor/date/WdatePicker.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/plm/javascript/messager.js"></script>
</head>
<body class="openWinPage" onload="true">
<form name="mainForm" method="POST">
<input type='hidden' id='OIDS' name='OIDS' value='<%=oids%>'/>
<input type='hidden' id='ATTRIDS' name='ATTRIDS'/>
<table width=100% cellSpacing="0" cellPadding="0" border="0">
<tr>
	<td>
		<jsp:include page="/plm/common/actionTitle.jsp">
			<jsp:param name="ACTIONID" value="com.bjsasc.adm.activedocument.editmulti"/>
		</jsp:include>
	</td>
</tr>
<tr id="searchConditionTD"><td valign="top">
<table width=100% cellSpacing="0" cellPadding="0" border="0">
<tr> <!--��ע��1��������50�ߣ� 2��������80�ߣ��������ƣ�ÿ��һ�м�30�� -->
	<td>
		<table class="tableBorderNone AvidmMtop">
		<tr>
			<td class="font_normal">ģ�ͣ�</td>
			<td>
				<select id="type" class="pt-select"  name='CLASSIDS' id='CLASSIDS' style="width:270px;"  onchange="onTypeSelected()">
	    		</select>		
	    	</td>
		</tr>
		<tr>
			<td class="font_normal">���ԣ�</td>
			<td>
				<select id="attr" class="pt-select"  name='CLASSIDS' id='CLASSIDS' style="width:270px;"  >
	    		</select>
	    		<div tips='����޸�����' width='30' iconAlign='center' icon='<%=request.getContextPath()%>/plm/images/common/add.gif' onclick='onAddFilter()'  class='pt-button' ></div>
	    	</td>
		</tr>
		</table>
	</td>
</tr>

<tr class="AvidmToolbar">
	<td>		
		<div class='pt-toolbar'>
    		<div tips='ɾ���޸�����' width='30' iconAlign='center' icon='<%=request.getContextPath()%>/plm/images/common/delete.gif' onclick='onDelete()'  class='pt-button'></div>
    		<input name="allFlag" id="ALL" value="0" type="radio" title="ȫ���޸�" checked><font class="font_normal" >&nbsp;ȫ���޸�</font>
			<input name="allFlag" id="NOTALL"  value="1" type="radio" title="�����޸�"><font class="font_normal">&nbsp;�����޸�</font>
    	</div>	    		
   	</td>
</tr>

<tr>
	<td>	
		<table id="grid" class="avidmTable" width=100% cellSpacing="0" cellPadding="0" border="3">
		<tr>
			<td align="center" width=30>
				<input id="checkAll" type="checkbox" onclick="onCheckAll()"></td>
			<td align="center" width=120><B>����</B></td>
			<td align="left"><B>ֵ</B></td>
		</tr>
		</table>
   	</td>
</tr>

<tr class="AvidmDecision">
	<td>
		<div class="pt-formbutton" text="ȷ��" id="submitbutton" onclick="onOk();"></div>
		<div class="pt-formbutton" text="ȡ��" id="closebutton" onclick="onCancel()"></div>
 	</td>
</tr>
</table>
</td></tr>
<tr height="10">
	<td class="splitButton"> 
		<img onClick="setVisible(this,'searchConditionTD');resizeTable();" src="<%=request.getContextPath()%>/plm/images/common/splitTop.gif"/> 
	</td>
</tr>
<table height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0">
	<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
		<jsp:param name="spot" value="<%=spot%>"/>
		<jsp:param name="gridId" value="<%=gridId%>"/>
		<jsp:param name="gridTitle" value="<%=gridTitle%>"/>
		<jsp:param name="toolbar_modelId" value="<%=toolbarId%>"/>
		<jsp:param name="operate_container" value="container"/>
		</jsp:include>
	</table>
</table>
</form>
</body>
</html>

<script type="text/javascript">
var grid;
var container = {};
//ģ��ѡ������ID
var typeIds = <%=JsonUtil.toJsonArray(typeIds)%>;
//ģ��ѡ������
var types = <%=DataUtil.encode(types)%>;

$(document).ready(function(){
	grid = document.getElementById("grid");
	init();
});

function init(){
	//ģ�������б�
	for(var i=0; i<typeIds.length; i++){
		var typeId = typeIds[i];
		var option = "<option value='"+typeId+"'>"+types[typeId].TYPENAME+"</option>";
		$("#type").append(option);		
	}

	//�������������б�
	resetSelectAttr(typeIds[0]);
}

function resetSelectAttr(typeId){
	var attr = document.getElementById("attr");
	attr.length = 0;
	
	var attrIds = types[typeId].ATTRIDS;
	var attrs = types[typeId].ATTRS;
	for(var i=0; i<attrIds.length; i++){
		var attrId = attrIds[i];
		if(isEditAttr(attrId)=="false"){
			continue;
		}
		var attrName = attrs[attrId].ATTRNAME;
		var comparorIds = attrs[attrId].COMPARORIDS;
		
		//�бȽ��������Բ�����Ϊ��ѡ����
		if(comparorIds.length > 0){
			var attrOption = "<option value='"+attrId+"'>" + attrName + "</option>";
			$("#attr").append(attrOption);
		}
	}
}
function isEditAttr(object){
	var attrs=[];
	attrs.push("NUMBER");
	attrs.push("NAME");
	attrs.push("CREATE_TIME");
	attrs.push("MODIFY_TIME");
	attrs.push("LIFECYCLE_STATE");
	attrs.push("CONTEXT");
	attrs.push("FOLDER");
	for(var i=0;i<attrs.length;i++){
		if(object==attrs[i]){
			return "false";
		}
	}
	return "ture";
}
function onTypeSelected(){
	resetSelectAttr(document.getElementById("type").value);
	var checks = document.getElementsByName("check");
	for(var i=checks.length-1; i>=0; i--){
		$(checks[i]).parent().parent().remove();
	}
}

function onCheckAll(){
	var checkAll = document.getElementById("checkAll");
	var checked = checkAll.checked;
	var checks = document.getElementsByName("check");
	for(var i=0; i<checks.length; i++){
		checks[i].checked = checked;
	}
}
function onAddFilter(){
	var typeId = document.getElementById("type").value;
	var attrId = document.getElementById("attr").value;
	var objs = document.getElementsByName("allFlag");
	var allFlag;
	for ( var j = 0; j < objs.length; j++) {
		if (objs[j].checked == true) {
			allFlag = objs[j].value;
			break;
		}
	}
	//�ж�Ҫ��ӵ������Ƿ���������ӵ��޸������б���
	var isExiting = false;
	var attrIds = document.getElementsByName("attrId");
	var typeIds = document.getElementsByName("typeId");
	for(var i=0; i<attrIds.length; i++){
		if(typeId==typeIds[i].value && attrId==attrIds[i].value){
			isExiting=true;
			break;
		}
	}
	if(!isExiting){
		addFilter(typeId, attrId, allFlag,"","");
	}
}

var rowIndex = 0;

function addFilter(typeId, attrId, allFlag, valueString, valueText){
	var type = types[typeId];
	var attrs = type["ATTRS"];
	var attr = attrs[attrId];
	
	var attrName = attr.ATTRNAME;
	var editorId = attr.EDITORID;
	
	var tr = "<tr>";
	
	//checkbox
	tr += "<td align='center'><input id='"+rowIndex+"' name='check' type='checkbox'></td>";
	
	//����
	tr += "<td align='center'><nobr>"+attrName+"</nobr></td>";
	tr += "<input type='hidden' id='attrId"+rowIndex+"' name='attrId' value='"+attrId+"'>";
	tr += "<input type='hidden' id='typeId"+rowIndex+"' name='typeId' value='"+typeId+"'>";
	tr += "<input type='hidden' id='allFlag"+rowIndex+"' name='allFlag' value='"+allFlag+"'>";
	//�༭��
	if (allFlag=="0"){
		tr += "<td><nobr>";
		tr += "<table><tr><td>�޸ĺ�</td>";
		tr += "<td><div id='div"+rowIndex+"'></div></td>";	
		tr += "</tr></table>";
		tr += "</nobr></td>";
	} else {
		tr += "<td><nobr>";
		tr += "<table><tr><td>�޸�ǰ</td>";
		tr += "<td><div id='beforediv"+rowIndex+"'></div></td>";
		tr += "</tr>";
		tr += "<tr><td>�޸ĺ�</td>";
		tr += "<td><div id='afterdiv"+rowIndex+"'></div></td>";
		tr += "</tr></table>";
		tr += "</nobr></td>";
	}

	tr += "</tr>";
	
	$("#grid").append(tr);	
	
	if (allFlag=="0"){
		//ajax���ɱ༭�������첽����
		plm.buildEditorAsync(editorId, "editor"+rowIndex, valueString, valueText, null, "div"+rowIndex);	
	} else {
		//ajax���ɱ༭�������첽����
		plm.buildEditorAsync(editorId, "editor"+rowIndex+"before", valueString, valueText, null, "beforediv"+rowIndex);	
		plm.buildEditorAsync(editorId, "editor"+rowIndex+"after", valueString, valueText, null, "afterdiv"+rowIndex);	
	}
	rowIndex++;	
}

function onDelete(){
	var checks = document.getElementsByName("check");
	for(var i=0; i<checks.length; i++){
		if(checks[i].checked){
			$(checks[i]).parent().parent().remove();
			onDelete();
			break;
		}
	}
}

function onOk(){	
	var editMutilConditions = [];
	var inputs = document.getElementsByName("check");
	var errorMsg=[];
	for(var i=0; i<inputs.length; i++){
		var rowIndex = inputs[i].id;
		var attrId = document.getElementById("attrId"+rowIndex).value;
		var typeId = document.getElementById("typeId"+rowIndex).value;
		var type = types[typeId];
		var attrs = type["ATTRS"];
		var attr = attrs[attrId];
		var allFlag = document.getElementById("allFlag"+rowIndex).value;
		var editValue="";
		var mes="";
		if(allFlag=="0"){
			var valueString = document.getElementById("editor"+rowIndex).value;
			//var valueText = document.getElementById("editor"+rowIndex+"_displayText").value;
			if(valueString==""){
				mes+="����"+attr.ATTRNAME+"�޸ĺ�Ϊ�գ�������Ϊ�༭�޸�����";
				errorMsg.push(mes);
				continue;
			}
			editValue=valueString;
		} else {
			var valueStringBefore = document.getElementById("editor"+rowIndex+"before").value;
			//var valueTextBefore = document.getElementById("editor"+rowIndex+"before_displayText").value;
			var valueStringAfter = document.getElementById("editor"+rowIndex+"after").value;
			//var valueTextAfter = document.getElementById("editor"+rowIndex+"after_displayText").value;
			if(valueStringAfter==""){
				mes+="����"+attr.ATTRNAME+"�޸ĺ�Ϊ�գ�������Ϊ�༭�޸�����";
				errorMsg.push(mes);
				continue;
			}
			editValue=valueStringBefore+","+valueStringAfter;
		}
	
		var editMutilCondition = {};
		editMutilCondition.typeId = typeId;
		editMutilCondition.attrId = attrId;
		editMutilCondition.attrFieldType = attr.FIELDTYPE;
		editMutilCondition.allFlag = allFlag;	
		editMutilCondition.editValue = editValue;		
		editMutilConditions.push(editMutilCondition);		
	}
	if(errorMsg.length > 0){
	 	var msg = "";
 		msg = errorMsg.join("! ");
 		plm.showMessage({title:'��ʾ', message:msg, icon:'3'});
		return;
 	}
	var url ="<%=request.getContextPath()%>/adm/active/ActiveDocumentHandle!updataMultiActiveDocument.action?OIDS=<%=oids%>&CONDITIONS="+pt.ui.JSON.encode(editMutilConditions);
	plm.startWait();
	$.ajax({
	        type: "post",
	        url : url,
	        dataType:"json",
	        success: function(result){
			    plm.endWait();
				if(result.SUCCESS != null && result.SUCCESS == "false"){
					plm.showAjaxError(result);
				}else{
					//plm.showMessage({title:'��ʾ', message:"�����༭�����ɹ�!", icon:'2'});
					messager.showTipMessager({'content':'�����༭�����ɹ�!','top':false});
					top.opener.location.reload();
					var table = pt.ui.get("<%=ConstUtil.GRID_LISTACTIVEDOCUMENT%>");
					table.reload();
				}
		   },
		   error:function(){
			   plm.endWait();
			   plm.showMessage({title:'������ʾ', message:"�����༭����ʧ��!", icon:'1'});
		   }
	});
}
	
// �ָ���
function resizeTable(){
	var grid = pt.ui.get("<%=gridId%>"); 
	grid.set({width :$(grid.renderTo).width(),height :$(grid.renderTo).height()-22});
	grid.pagingbar.set( {width :$(grid.renderTo).width(),height :22});
};

function onCancel(){
	window.close();
}
</script>