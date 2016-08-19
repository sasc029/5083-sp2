<%@page import="java.util.*" %>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>

<%@include file="/plm/plm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String contextOid = request.getParameter("contextOid");
	String pageType = request.getParameter("pageType");
	String classId = "DistributeObjectType";
	
	String spot = "ListDistributeObjectTypes";
	String gridId = "distributeObjectTypeDetail";
	String gridTitle = "����ģ������";
	String toolbar_modelId = "ddm.distributeobjecttype.addtype.toolbar";
	
	String addTypeUrl = contextPath+"/ddm/config/objecttype/addObjectTypeMain.jsp?contextOid="+contextOid;
	String actionUrl = contextPath+"/ddm/config/objecttype/ddmObjectTypeAction.jsp?op=addRoles&contextOid="+contextOid;
	String dataUrl = contextPath + "/ddm/config/objecttype/ddmObjectTypeAction.jsp?op=typeData";
	
	Map<String,Object> params = new HashMap<String, Object>();
 	params.put("contextOid", contextOid);
 	GridDataUtil.prepareRowQueryParams(params,  spot);
%>

<html>
	<head>
		<title><%=gridTitle%></title>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
		<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
	</head>
	<body>
		<form id="mainForm" name="mainForm" method="POST">
			<table height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0">
				<jsp:include page="/plm/common/grid/control/grid_of_title.jsp">
					<jsp:param name="gridTitle" value="<%=gridTitle%>"/>
				</jsp:include>
				<tr><td>
					<table cellSpacing="0" cellPadding="0" width="100%" border="0"><tr class="AvidmToolbar"><td> 
						<div class="pt-toolbar">
							<%=UIHelper.getToolBar(toolbar_modelId)%>
						</div> 
					</td></tr></table>
				</td></tr>
				<tr height="100%"><td>
					<table class="pt-grid" id="<%=gridId%>"	autoLoad="false" url="<%=dataUrl%>"
						rownumbers="true" pagination="false" useFilter="false" 
						singleSelect="false" fit="true" checkbox="true">
						<th field="DOT_DATA" align="center" width="150" tips="��ɫ/��Ա">��ɫ/��Ա</th>
						<th field="DOT_TYPE_NAME" align="center" width="100" tips="��������">��������</th>
						<th field="DOT_STATE" align="center" width="50" tips="���">���</th>
						<th field="CONTEXT" align="center" width="100" tips="������">������</th>
					</table>
				</td></tr>
				<tr><td>
				<table width="100%" border="0" cellspacing="5" cellpadding="10">
					<tr>
		     	        <td align="center">
		     	          <div class="pt-formbutton" id="savebtn"   text="ȷ��" onclick="submitForm()"></div>
		     	          <div class="pt-formbutton" id="cancelbtn" text="ȡ��" onclick="cancel()"></div> 
		     	        </td>
			         </tr>
				  </table>
				</td></tr>
			</table>
		</form>
	</body>
</html>
<script type="text/javascript">
var container = {OID:'<%=contextOid%>'};
var records = "{}";

$(document).ready(function() {
	var adt_grid = pt.ui.get("<%=gridId%>");
	adt_grid.reload();
});

// ȡ����ť����
function cancel() {
	var msg = "��ȷ��ȡ��������\n";
	if (!plm.confirm(msg)) {
		return;
	}	
	parent.close();
}

// ȷ����ť����
function submitForm() {
	var adt_grid = pt.ui.get("<%=gridId%>");
	var data = adt_grid.getRows();
	if(data.length == 0){
		plm.showMessage({title:'������ʾ', message:"��������ݺ���ȷ��", icon:'1'});
		return;
	}	
	var msg = "��ȷ���ύ������\n";
	if (!plm.confirm(msg)) {
		return;
	}		
	plm.startWait();
 	
	$.ajax({
		type: "post",
		url: "<%=contextPath%>/ddm/config/objecttype/ddmObjectTypeAction.jsp?op=addTypeData&contextOid=<%=contextOid%>",
		dataType: "json",
  		data: {pageType:"<%=pageType%>"},
		success: function(result){
			plm.endWait();
			if(result.SUCCESS != null &&result.SUCCESS == "false"){
				plm.showAjaxError(result);
				return;
			}
			parent.opener.reload();
			parent.close();
	   },
	   error:function(){
		   plm.endWait();
		   plm.showMessage({title:'������ʾ', message:"�Բ��������������", icon:'1'});
	   }
	});	
}

// �������
function addTypes() {
	//��߽�ɫ/�û�ҳ��
	var data = parent.getSelections();
	if(data.length == 0){
		plm.showMessage({title:'������ʾ', message:"��ѡ���ɫ���Ա����", icon:'1'});
		return;
	}
	plm.selectTypes({callback:"doBindTypes",spot:"Distribute",expand:"true"});	
	
}

// ������ͣ��ص�������
function doBindTypes(records){
	plm.startWait();
	var dataList = UI.JSON.decode(records);
	var typeIds = "";
	var typeNames = "";
	for(var i=0;i<dataList.length;i++){
 	 	if(i==0){
 	 		typeIds   += dataList[i]["id"];
 	 		typeNames += dataList[i]["Name"];
 	 	}else{ 	
 	 		typeIds += "#@#"+dataList[i]["id"];
 	 		typeNames += "#@#"+dataList[i]["Name"];
 	 	}
 	}	
	var data = parent.getSelections();
 	var OIDS = "";
 	for(var i=0;i<data.length;i++){
 	 	if(i==0){
 	 		OIDS += data[i]["OID"];
 	 	}else{ 	
 	 		OIDS += "#@#"+data[i]["OID"];
 	 	}
 	}	
	var adt_grid = pt.ui.get("<%=gridId%>");
	$.ajax({
		type: "post",
		url: "<%=contextPath%>/ddm/config/objecttype/ddmObjectTypeAction.jsp?op=addType&contextOid=<%=contextOid%>",
		dataType: "json",
  		data: {typeIds:typeIds,typeNames:typeNames,OIDS:OIDS},
		success: function(result){
			plm.endWait();
			if(result.SUCCESS != null &&result.SUCCESS == "false"){
				plm.showAjaxError(result);
				return;
			}
			adt_grid.reload();
	   },
	   error:function(){
		   plm.endWait();
		   plm.showMessage({title:'������ʾ', message:"�Բ��������������", icon:'1'});
	   }
	});	
}

// ɾ������
function deleteDistributeObjectType() {
	var adt_grid = pt.ui.get("<%=gridId%>");
	var data = adt_grid.getSelections();
	if(data.length == 0){
		plm.showMessage({title:'������ʾ', message:"��ѡ�������������", icon:'1'});
		return;
	}	
	var msg = "��ȷ��Ҫɾ��������\n";
	if (!plm.confirm(msg)) {
		return;
	}	
	plm.startWait();
 	var DKS = "";
 	for(var i=0;i<data.length;i++){
 	 	if(i==0){
 	 		DKS += data[i]["DK"];
 	 	}else{ 	
 	 		DKS += "#@#"+data[i]["DK"];
 	 	}
 	}	
	$.ajax({
		type: "post",
		url: "<%=contextPath%>/ddm/config/objecttype/ddmObjectTypeAction.jsp?op=deleteTypeData&contextOid=<%=contextOid%>",
		dataType: "json",
  		data: {DKS:DKS},
		success: function(result){
			plm.endWait();
			if(result.SUCCESS != null &&result.SUCCESS == "false"){
				plm.showAjaxError(result);
				return;
			}
			adt_grid.reload();
	   },
	   error:function(){
		   plm.endWait();
		   plm.showMessage({title:'������ʾ', message:"�Բ��������������", icon:'1'});
	   }
	});	
}
</script>