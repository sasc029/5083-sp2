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
	String classId = "DistributeObjectNotAutoCreate";
	
	String spot = "ListDistributeObjectNotAutoCreate";
	String gridId = "distributeObjectTypeGrid";
	String gridTitle = "����ģ�����ò��Զ�����";
	String toolbar_modelId = "ddm.distributeobjecttype.addtype.toolbar";
		
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
function addTypes() {
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
	$.ajax({
		type: "post",
		url: "<%=contextPath%>/ddm/config/objecttype/ddmObjectNotAutoCreateAction.jsp?op=addTypeData&contextOid=<%=contextOid%>",
		dataType: "json",
  		data: {typeIds:typeIds,typeNames:typeNames},
		success: function(result){
			plm.endWait();
			if(result.SUCCESS != null &&result.SUCCESS == "false"){
				plm.showAjaxError(result);
				return;
			}
			reload();
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
 	var NAMES = "";
 	for(var i=0;i<data.length;i++){
 	 	if(i==0){
 	 		NAMES += data[i]["DOT_TYPE_NAME"];
 	 	}else{ 	
 	 		NAMES += "#@#"+data[i]["DOT_TYPE_NAME"];
 	 	}
 	}
	$.ajax({
		type: "post",
		url: "<%=contextPath%>/ddm/config/objecttype/ddmObjectNotAutoCreateAction.jsp?op=deleteObjectTypeData&contextOid=<%=contextOid%>",
		dataType: "json",
  		data: {NAMES:NAMES},
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