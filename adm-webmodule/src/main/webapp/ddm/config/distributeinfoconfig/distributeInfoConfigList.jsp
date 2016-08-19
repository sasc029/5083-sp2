<%@page import="java.util.*" %>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>

<%@include file="/plm/plm.jsp" %>

<%
	String contextPath = request.getContextPath();
	
	String spot = "ListDistributeInfoConfig";
	String gridId = "distributeInfoConfigGrid";
	String gridTitle = "默认分发信息配置";
	String toolbar_modelId = "ddm.distributeinfoconfig.list.toolbar";
	
	
	Map<String,Object> params = new HashMap<String, Object>();
 	params.put("OID", "");
 	GridDataUtil.prepareRowQueryParams(params,  spot);
 	
%>

<html>
	<head>
		<title><%=gridTitle%></title>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
	    <script type="text/javascript"	src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
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
var container = "";
var records = "{}";

// 刷新数据
function reload() {
	var adt_grid = pt.ui.get("<%=gridId%>");
	adt_grid.reload();
}	
//添加用户
function  addUsers(){
	  var configObj = { 
			IsModal : "true",
			SelectMode : "multiple",
			returnType : "arrayObj",
			scope : "<%=ConstUtil.VISTYPE_ALL%>"};
	 var retObj = ddm.tools.selectUser(configObj);
	 if(retObj){
		doBindRole(retObj, "<%=ConstUtil.DISINFOTYPE.USER%>");
  	 } 
  }
  //添加组织
function addOrg(){
	 var configObj = 
		{ 
			IsModal : "true",
			SelectMode : "multiple",
			returnType : "arrayObj",
			scope : "<%=ConstUtil.VISTYPE_ALL%>"
		};
	var retObj = ddm.tools.selectOrg(configObj); 
	if(retObj){
		doBindRole(retObj, "<%=ConstUtil.DISINFOTYPE.ORG%>");
  	} 
}
 //选择（人/组织）回调
function doBindRole(reObj, type){
	plm.startWait();
	var arrIid = reObj.arrIID;
	var arrDisMediaType = reObj.arrDisMediaType;
	var arrDisInfoNum = reObj.arrDisInfoNum;
	var arrNote = reObj.arrNote;
	
	var iids = "";
	var disMediaTypes = "";
	var disInfoNums = "";
	var notes = "";
	for(var i = 0; i < arrIid.length; i++){
		if(iids != ""){
			iids += ",";
			disMediaTypes += ",";
			disInfoNums += ",";
			notes += ",";
		}
		iids += arrIid[i];
		disMediaTypes += arrDisMediaType[i];
		disInfoNums += arrDisInfoNum[i];
		if (arrNote[i] == "") {
			arrNote[i] = "&nbsp;";
		}
		notes += arrNote[i];
	}
	
	$.ajax({
			type: "post",
			url: "<%=contextPath%>/ddm/config/distributeinfoconfig/distributeInfoConfigAction.jsp?op=addRoleOrOrg",
			dataType: "json",
			data: {type:type,iids:iids,disMediaTypes:disMediaTypes,disInfoNums:disInfoNums,notes:notes},
			success: function(result){
				 plm.endWait();
				if(result.SUCCESS != null && result.SUCCESS == "false"){
					plm.showMessage({title:'错误提示', message:"请勿对同一个用户或组织添加相同的分发信息！", icon:'1'});
					return;
				}
				reload(); 
		    },
		    error:function(){
		    	plm.endWait();
				plm.showMessage({title:'错误提示', message:"操作失败！", icon:'1'});
		    }
		});	
} 
 //删除用户或者组织
function delUserOrOrg(){
	var adt_grid = pt.ui.get("<%=gridId%>");
	var data = adt_grid.getSelections();
	if(data.length == 0){
		plm.showMessage({title:'错误提示', message:"请选择操作对象数据", icon:'1'});
		return;
	}	
	var msg = "您确定要删除此条分发信息吗？\n";
	if (!plm.confirm(msg)) {
		return;
	}	
	plm.startWait();
 	var OIDS = "";
 	for(var i=0;i<data.length;i++){
 		OIDS += data[i]["OID"]+",";
 	}
	$.ajax({
		type: "post",
		url: "<%=contextPath%>/ddm/config/distributeinfoconfig/distributeInfoConfigAction.jsp?op=deleteUserOrOrg",
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
		   plm.showMessage({title:'错误提示', message:"对不起，数据请求错误", icon:'1'});
	   }
	});	
}


// 更新用户或组织
function editUserOrOrg() {

	var adt_grid = pt.ui.get("<%=gridId%>");
	var data = adt_grid.getSelections();
 	var oid = data[0]["OID"];
 	 plm.openWindow("<%=contextPath%>/ddm/config/distributeinfoconfig/editDistributeInfoConfig.jsp?oid="+oid,800,600,"dot_edit_type");
}

</script>