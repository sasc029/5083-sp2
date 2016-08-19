<%@page import="com.bjsasc.plm.Helper"%>
<%@page contentType = "text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.plm.core.persist.PersistHelper"%>
<%@page import="com.bjsasc.plm.util.JsonUtil"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.ui.UIDataInfo"%>	
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributepapersigntask.DistributePaperSignTaskService"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeelectask.DistributeElecTaskService"%>
<%@page language = "java"%>
<%@page import="java.util.*"%>

<%
	String gridId = "relationDistributeOrderGrid";
	String title = "相关发放单";
	String toolbarId = "ddm.relateddistributeorder.list.toolbar";
	// 必要参数
	String path = request.getContextPath();
	String data = request.getParameter(KeyS.DATA);
	List<Map<String,Object>> listMap = JsonUtil.toList(data);
    
	String classId = listMap.get(0).get(KeyS.CLASSID).toString();

	String innerId = listMap.get(0).get(KeyS.INNERID).toString();
	
	
	String oid = listMap.get(0).get(KeyS.OID).toString();
	String classid = Helper.getClassId(oid);

	String contextPath = request.getContextPath();
	String loadUrl="";
	
	if(classid.equals("DistributeElecTask") ||classid.equals("DistributePaperTask")
			|| classid.equals("DistributePaperSignTask")){
     loadUrl = contextPath + "/ddm/distribute/distributeOrderHandle!getRelatetaskdDistributeOrder.action?OID="+oid;	
	}
	else{
	 loadUrl = contextPath + "/ddm/distribute/distributeOrderHandle!getRelatedDistributeOrder.action?OID="+oid;
	}
	
	//发放单与分发数据关联的服务
	List<DistributeObject> listDistributeObject = null;
	DistributeObjectService disObjService = DistributeHelper.getDistributeObjectService();
	DistributePaperSignTaskService pageSignTaskservice = DistributeHelper.getDistributePaperSignTaskService();
	DistributeElecTaskService elecTaskservice = DistributeHelper.getDistributeElecTaskService();
	if(classId.equals("RecDesPaperTask")||classId.equals("DistributePaperTask")){
		listDistributeObject = disObjService.getDistributeObjectsByDistributePaper(oid);
	}else if(classId.equals("DistributePaperSignTask")){
		listDistributeObject = pageSignTaskservice.getDistributeObjects(oid);
	}else if(classId.equals("DistributeElecTask")){
		listDistributeObject = elecTaskservice.getDistributeObjects(oid);
	}else{
		listDistributeObject = disObjService.getDistributeObjectsByDataOid(oid);
	}
	String oid_linkid_str = "";
	String firstObjId = "";
	for(DistributeObject disObj : listDistributeObject){
		//分发数据oid和分发对象link的oid拼接
		oid_linkid_str = oid_linkid_str + disObj.getOid() + "_" + disObj.getDistributeOrderObjectLinkOid() + ",";
		firstObjId = disObj.getOid() ;
	}
%>

<html>
<head>  
<title><%=title%></title>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" scroll="no">
<table height="100%" cellSpacing="0" cellPadding="0" width="100%" border="0">
	<tbody>
			<tr class="AvidmToolbar">
				<TD colspan='2' align='left'>
					<%
						UIDataInfo maps = new UIDataInfo();
						String strMenuActionRecords = gridId + ".getSelections()";
						maps.put(UIHelper.MENU_ACTION_RECORDS, strMenuActionRecords);
						maps.put(UIHelper.MENU_ACTION_CONTAINER, "container");
						maps.put(UIHelper.MENU_ITEM_WIDTH, "30");
						maps.put(UIHelper.MENU_TOOLBAR_IS_SHOW_TEXT, "false");
						maps.put(UIHelper.BUILD_TOOLBAR, "false");
					%> 
					<div class='pt-toolbar'><%=UIHelper.getToolBar("ddm.relateddistributeorder.list.toolbar", maps)%>
					</div></TD>
			</tr> 
			<tr>
				<td valign="top">
      			<table class="pt-grid" id="<%=gridId%>" singleSelect="false" fit="true" autoLoad="true" checkbox="true" 
      				useFilter="false" url="<%=loadUrl%>" pagination="false" onloadsuccess="onLoadSuccess()" >
					<thead>
	<th field="NUMBER" width="100" tips="编号" sortable='true' >编号</th>
	<th field="NAME" width="250" tips="名称" sortable='true' >名称</th>
	<th field="ORDERTYPE" width="100" tips="单据类型" sortable='true' >单据类型</th>
	<th field="LIFECYCLE_STATE" width="100" tips="状态" sortable='true' >状态</th>
	<th field="CONTEXT" width="100" tips="上下文" sortable='true' >上下文</th>
	<th field="CREATOR" width="100" tips="创建者" sortable='true' >创建者</th>
	<th field="CREATE_TIME" width="140" tips="创建时间" sortable='true' >创建时间</th>
	<th field="NOTE" width="250" tips="备注" sortable='true' >备注</th>
	<th field="SUBMITUSERNAME" width="150" tips="发起人" sortable='true' >发起人</th>
	<th field="SUBMITSITENAME" width="150" tips="发起站点" sortable='true' >发起站点</th>
					</thead>
				</table>
      		</td>
    	</tr>
	</tbody>
</table>
<input type="hidden" name="distributeOrderOids" id="distributeOrderOids"/>
<input type="hidden" name="historyOids" id="historyOids"/>
</body> 
<script type="text/javascript">
	var container = {};
	function onLoadSuccess() {
		onLoadSuccess_ddm("<%=gridId%>");
	}
	function doCustomizeMethod_id(value) {
		var url = '<%=contextPath%>/ddm/distributeorder/distributeOrder_tab.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + value;
		ddm.tools.openWindow(url,900,600,"distributeOrderOpen");
	}
	function reloadOrder() {
    	plm.startWait();
		$.ajax({
				type: "post",
				url: "<%=loadUrl%>",
				success: function(result){
					plm.endWait();
					if(result.SUCCESS != null && result.SUCCESS == "false"){
						plm.showAjaxError(result);
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
    // 重新加载
	function reload(){
		var table = pt.ui.get("<%=gridId%>");
		table.reload();
	}
	 // 查看当前业务对象的历史分发信息
  	function searchHistory(){
  		var grid = pt.ui.get("<%=gridId%>");
  		var selections = grid.getSelections();

    	var oids = "";
    	var firstOid  = "<%=firstObjId%>";
    	//还没有生成分发数据对象DistributeObject
    	if(firstOid == ""){
			plm.showMessage({title:'错误提示', message:"当前对象还没有发放过，没有历史分发信息！", icon:'1'});
    		return;
    	}    	
    	for(var i = 0; i < selections.length; i++){
			var oid = selections[i]["OID"];
			if(oids != ""){
				oids += ",";
			}
			oids += oid;
		}
    	$("#distributeOrderOids").val(oids);
    	$("#historyOids").val("<%=oid_linkid_str%>");
    	//var strFeatures ='width=1300,height=600,left=150,top=150,toolbar=no,menubar=no,resizable=yes, scrollbar=no,location=no,status=no';
    	var url = "";
		url="<%=contextPath%>/ddm/distributeinfo/distributeInfo_history_main.jsp?oid_linkid_str="+firstOid;
		//plm.openWindow(url,1230,600);
		window.showModalDialog(url,window,"dialogHeight: 600px; dialogWidth: 1230px; left: 150px; top: 150px; toolbar: no; menubar: no; resizable: yes; scrollbar: no; location: no; status: no;");
  	}
</script>

</html>