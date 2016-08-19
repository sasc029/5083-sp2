<%@page import="java.util.*" %>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="com.bjsasc.plm.core.domain.Domain"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.core.context.ContextHelper"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>

<%@include file="/plm/plm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String contextOid = request.getParameter("contextOid");
	String classId = "DistributeObjectType";
	
	String spot = "ListDistributeObjectTypes";
	String gridId = "distributeObjectTypeGrid";
	String gridTitle = "����ģ������";
	String toolbar_modelId = "ddm.distributeobjecttype.list.toolbar";
	
	String addTypeUrl = contextPath+"/ddm/config/objecttype/addObjectTypeMain.jsp?op=add&contextOid="+contextOid;
	String editTypeUrl = contextPath+"/ddm/config/objecttype/addObjectTypeMain.jsp?op=edit&contextOid="+contextOid;
	String actionUrl = contextPath+"/ddm/config/objecttype/ddmObjectTypeAction.jsp?op=addRoles&contextOid="+contextOid;
	
	Map<String,Object> params = new HashMap<String, Object>();
 	params.put("contextOid", contextOid);
 	GridDataUtil.prepareRowQueryParams(params,  spot);
 	
	Context context = ContextHelper.getService().getContext(contextOid);
	
	Domain domain = context.getDomain();
	Context orgContext = domain.getContextInfo().getContext();
	String orgOid = orgContext.getOid();
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
				<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
					<jsp:param name="spot" value="<%=spot%>"/>
					<jsp:param name="gridId" value="<%=gridId%>"/>
					<jsp:param name="gridTitle" value="<%=gridTitle%>"/>
					<jsp:param name="toolbar_modelId" value="<%=toolbar_modelId%>"/>
					<jsp:param name="operate_container" value="container"/>
				</jsp:include>
			</table>
		</form>
	</body>
</html>
<script type="text/javascript">
var container = {OID:'<%=contextOid%>'};
var records = "{}";

// ˢ������
function reload() {
	var adt_grid = pt.ui.get("<%=gridId%>");
	adt_grid.reload();
}	

// �������
function addData() {
	var ret = plm.openWindow("<%=addTypeUrl%>",800,600,"dot_add_type");
}

// ��������
function updateDistributeObjectType() {

	var adt_grid = pt.ui.get("<%=gridId%>");
	var data = adt_grid.getSelections();
	plm.startWait();
 	var OIDS = "";
 	for(var i=0;i<data.length;i++){
 	 	if(i==0){
 	 		OIDS += data[i]["DOT_DATA_OID"];
 	 	}else{ 	
 	 		OIDS += "#@#"+data[i]["DOT_DATA_OID"];
 	 	}
 	}
	$.ajax({
		type: "post",
		url: "<%=contextPath%>/ddm/config/objecttype/ddmObjectTypeAction.jsp?op=editData&contextOid=<%=contextOid%>",
		dataType: "json",
  		data: {OIDS:OIDS},
		success: function(result){
			plm.endWait();
			if(result.SUCCESS != null &&result.SUCCESS == "false"){
				plm.showAjaxError(result);
				return;
			}
			var ret = plm.openWindow("<%=editTypeUrl%>",800,600,"dot_edit_type");
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
 	var OIDS = "";
 	for(var i=0;i<data.length;i++){
 	 	if(i==0){
 	 		OIDS += data[i]["DOT_DATA_OID"];
 	 	}else{ 	
 	 		OIDS += "#@#"+data[i]["DOT_DATA_OID"];
 	 	}
 	}
	$.ajax({
		type: "post",
		url: "<%=contextPath%>/ddm/config/objecttype/ddmObjectTypeAction.jsp?op=deleteObjectTypeData&contextOid=<%=contextOid%>",
		dataType: "json",
  		data: {OIDS:OIDS},
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