<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>
<%@page import="com.bjsasc.plm.core.util.DateTimeUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder"%>
<%@page import="com.bjsasc.ddm.distribute.model.recdesinfo.RecDesInfo"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.core.system.access.operate.Operate"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@include file="/ddm/public/ddm.jsp" %>

<%@page contentType = "text/html; charset=UTF-8" pageEncoding="GBK"%>
<%
	String contextPath = request.getContextPath();
	String oid = (String) session.getAttribute(ConstUtil.DISTRIBUTE_ORDER_OID);
	String classId = RecDesInfo.CLASSID;
	String spot = "ListDesInfos";
	String gridId = "DecInfoGrid";
	String gridTitle = "销毁信息";
	String toolbarId = "ddm.desinfo.list.toolbar";
	
	String commonInfoTitle = "公共信息";
	String lc_new=ConstUtil.LC_NEW.getName();//获得生命周期是新建
	String approveTerminate=ConstUtil.LC_APPROVE_TERMINATE.getName();//获得生命周期是审批终止
	String scheduling=ConstUtil.LC_SCHEDULING.getName();//获得生命周期是调度中
	String backoff=ConstUtil.LC_BACKOFF.getName();//获得生命周期是已回退
	
	Persistable obj = Helper.getPersistService().getObject(oid);
	DistributeOrder dis = (DistributeOrder)obj;
	boolean operation = false;
	String stateName = dis.getStateName();

	if(StringUtils.isNotBlank(stateName) && "新建".equals(stateName)){
		operation = true;
	}
	String addPrincipalUrl = contextPath + "/ddm/distribute/recDesInfo!getAllDesInfo.action";
	String deadLineDate = DateTimeUtil.getCurrentDate(ConstUtil.C_DISDEADLINE_DELAY_DAY);
	String disUrgent = (String)request.getAttribute("disUrgent");
	String deadDate = (String)request.getAttribute("deadLineDate");

	if(deadDate == null){
		deadDate = DateTimeUtil.getCurrentDate(ConstUtil.C_DISDEADLINE_DELAY_DAY);
		//deadDate = "";
	}
	String qReceive_sel = "checked=\"true\"";
	String qReceive_cus = "";

	if ("1".equals(disUrgent)) {
		qReceive_sel = "";
		qReceive_cus = "checked=\"true\"";
	}

	if (!lc_new.equals(dis.getLifeCycleInfo().getStateName())
			&& !approveTerminate.equals(dis.getLifeCycleInfo().getStateName())
			&& !scheduling.equals(dis.getLifeCycleInfo().getStateName())
			&& !backoff.equals(dis.getLifeCycleInfo().getStateName())) {
		
		qReceive_sel += " disabled=\"true\"";
		qReceive_cus += " disabled=\"true\"";
	}
%>
<html>
<head>
	<title>工作区列表</title>
	<script type="text/javascript"	src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
	<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath%>/plm/ui/editor/date/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0"  scroll="no">
<form id='main_form'>
<input type="hidden" name="CLASSID" id="CLASSID" value="<%=classId%>"/>
<table cellSpacing="0" cellPadding="0" height="100%" width="100%" border="0">
	<tr><td height="1%">
		<!-- 公共信息 开始 -->
		<table width="100%" cellSpacing="0" cellPadding="0" border="0">
			<tr class="AvidmToolbar">
				<td nowrap="nowrap">
					<div class="AvidmMtop5 twoTitle">
						<a class="showIcon" href="#" onclick="divSelected(this);">
						<img src="<%=contextPath%>/plm/images/common/space.gif"></a>
						<%=commonInfoTitle%>
					</div>
				</td>
			</tr>
			<tr><td>
				<table border="0" cellspacing="0" cellpadding="0" class="avidmTable">
					<tr>
					<td class="left_col AvidmW100">完工期限：</td>
					<td class="e-checked-text">
						<INPUT type='text' id="deadLineDate"  name="deadLineDate" value="<%=deadDate%>"
			        		class="Wdate pt-textbox"  readonly="readonly"  style="width:100px;" <%=ValidateHelper.buildValidator()%>
			        			<%if(lc_new.equals(dis.getLifeCycleInfo().getStateName()) || approveTerminate.equals(dis.getLifeCycleInfo().getStateName()) || scheduling.equals(dis.getLifeCycleInfo().getStateName()) || backoff.equals(dis.getLifeCycleInfo().getStateName())){ %>
			        		onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'2128-03-10'})" onchange="updateTime()"
			        		<%}%>
			        		/>&nbsp;
					</td>
					<td class="left_col AvidmW100">紧急程度：</td>
					<td class="e-checked-text">
						<input value=0  type=radio name=disUrgent id=disUrgent0 <%=qReceive_sel%> onchange="updateDisUrgent('0')"/>
						普通&nbsp;
						<input value=1 type=radio name=disUrgent id=disUrgent1 <%=qReceive_cus%> onchange="updateDisUrgent('1')"/>
						加急&nbsp;&nbsp;
					</td>
					</tr>
				</table>
		</td></tr></table>
<!-- 公共信息 结束 -->
		<div id="gridTop" name="gridTop"><div>
	</td></tr><tr><td>
			<table height="100%" width="100%" cellSpacing="0" cellPadding="0"
			border="0">
			<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
				<jsp:param name="spot" value="<%=spot%>" />
				<jsp:param name="gridId" value="<%=gridId%>" />
				<jsp:param name="gridTitle" value="<%=gridTitle%>"/>
				<jsp:param name="toolbar_modelId" value="<%=toolbarId%>"/>
				<jsp:param name="operate_container" value="container" />
				<jsp:param name="operate_mainObjectOID" value="<%=oid%>" />
			</jsp:include>
		</table>
<!-- 分发数据 结束 -->	
</td></tr></table>
<input type="hidden" name="distributeOrderOids" id="distributeOrderOids"/>
<input type="hidden" name="updateDisUrgentValue" id="updateDisUrgentValue"/>
<input type="hidden" name="historyOids" id="historyOids"/>
<input type="hidden" name="oids" id="oids"/>
</form>
</body>
<script>
	var container = {};
	//添加销毁单信息
	function addDesInfos(){
		var treeSelections = parent.frames("leftTree").getSelections();
		if(treeSelections == null || treeSelections == ""){
			plm.showMessage({title:'错误提示', message:"请选择一条分发数据！", icon:'1'});
		}else if(treeSelections.length>1){
			plm.showMessage({title:'错误提示', message:"请只选择一条分发数据添加回收销毁信息！", icon:'1'});
		}else{
			var url = "<%=contextPath%>/ddm/recdesinfo/addDesInfos.jsp";
			var windowid = "addDesInfos" + "_" + new Date().getTime();
			plm.openWindow(url , 800, 620, windowid);
		}
	}
	
	//编辑销毁信息
	function editDesInfos(){

		var table = pt.ui.get("<%=gridId%>");
		var selections = table.getSelections();
		if (selections.length == 0) {
			plm.showMessage({title:'错误提示', message:"没有可操作的数据", icon:'1'});
			return;
		}

		var oid = "";

		for(var i=0;i<selections.length;i++){
			oid += selections[i].OIDS + "!";
		}

		oid = oid.substring(0, oid.length - 1);
		

		//window.location.href = "<%=contextPath%>/ddm/recdesinfo/desInfo_content_edit.jsp?oid="+oid;
		var url = "<%=contextPath%>/ddm/recdesinfo/desInfo_content_edit.jsp?oid="+oid;
		plm.OpenWindowAndReload(url);
	}
	// 刷新数据
	function reload() {
		/*
		var adt_grid = pt.ui.get("<%=gridId%>");
		adt_grid.reload();*/
		window.location.href = "<%=contextPath%>/ddm/distribute/recDesInfo!getDesInfoByLinkOids.action";
	}

	function deleteDesInfos(){
		var url = "<%=contextPath%>/ddm/distribute/recDesInfo!deleteRecDesInfos.action";
		var table = pt.ui.get("<%=gridId%>");
		var selections = table.getSelections();
		if (selections.length == 0) {
			plm.showMessage({title:'错误提示', message:"没有可操作的数据", icon:'1'});
			return;
		}

		if (!plm.confirm("确定删除吗?")) {
			plm.endWait();
			return;
		}else{
			plm.startWait();
		}

		var oids = "";

		for(var i=0;i<selections.length;i++){
			oids += selections[i].OIDS+",";
		}

		oids = oids.substring(0, oids.length - 1);
		$("#oids").val(oids);
		$.ajax({
			type: "post",
			url: url,
			dataType: "json",
			data: $("#main_form").serializeArray(),
			success: function(result){
				plm.endWait();
				if(result.SUCCESS != null &&result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				//plm.showMessage({title:'提示', message:"删除成功!", icon:'2'});
				reload();
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"对不起，数据请求错误", icon:'1'});
			}
		});

	}

	function updateTime(){
		plm.startWait();
		ddmValidation("<%=oid%>","<%=Operate.MODIFY%>","updateTimeCallBack");
	}
	
	function updateTimeCallBack(orderOid){
		parent.frames("leftTree").checkAll();
		var data = parent.frames("leftTree").getSelections();
		if(data.length == 0){
			plm.endWait();
			return;
		}
		var linkOids = "";
		for(var i = 0; i < data.length; i++){
			var oid = data[i]["DISTRIBUTEORDEROBJECTLINKOID"];
			if(linkOids != ""){
				linkOids += ",";
			}
			linkOids += oid;
		}
		var deadLineDate = $("#deadLineDate").val();
		var url = "<%=contextPath%>/ddm/distribute/recDesInfo!updateDistributedeadLinkDate.action";

		$.ajax({
			url:url,
			type:"post",
			dataType:"json",
			data: "<%=KeyS.OIDS%>="+linkOids+"&deadLineDate="+deadLineDate,
			success:function(result){
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

	function updateDisUrgent(value){
		plm.startWait();
		$("#updateDisUrgentValue").val(value);
		ddmValidation("<%=oid%>","<%=Operate.MODIFY%>","updateDisUrgentCallBack");
	}
	
	function updateDisUrgentCallBack(orderOid){
		var value = $("#updateDisUrgentValue").val();
		parent.frames("leftTree").checkAll();
		var data = parent.frames("leftTree").getSelections();
		if(data.length == 0){
			plm.endWait();
			return;
		}
		var linkOids = "";
		for(var i = 0; i < data.length; i++){
			var oid = data[i]["DISTRIBUTEORDEROBJECTLINKOID"];
			if(linkOids != ""){
				linkOids += ",";
			}
			linkOids += oid;
		}
		var url = "<%=contextPath%>/ddm/distribute/recDesInfo!updateDistributeObjectLink.action";

		$.ajax({
			url:url,
			type:"post",
			dataType:"json",
			data: "<%=KeyS.OIDS%>="+linkOids+"&disUrgent="+value,
			success:function(result){
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
  	// 查看历史分发信息
  	function searchHistory(){
  		var selections = parent.frames("leftTree").getSelections();
  		if(selections.length == 0){
			plm.showMessage({title:'错误提示', message:"请至少选择一条分发的数据！", icon:'1'});
			return;
		}
    	var oids = "";
    	var firstOid  = "";
    	for(var i = 0; i < selections.length; i++){
			var oid = selections[i]["OID"];
			var linkid = selections[i]["DISTRIBUTEORDEROBJECTLINKOID"];
			if(oids != ""){
				oids += ",";
			}
			oids += oid+"_"+linkid;
			firstOid = selections[0]["OID"];
		}
    	$("#historyOids").val(oids);
		//var url="<%=contextPath%>/ddm/distributeinfo/distributeInfo_history_main.jsp?oid_linkid_str="+oids;
		//plm.openWindow(url,1230,600);
    	var url="<%=contextPath%>/ddm/distributeinfo/distributeInfo_history_main.jsp?oid_linkid_str="+firstOid;
		window.showModalDialog(url,window,"dialogHeight: 600px; dialogWidth: 1230px; left: 150px; top: 150px; toolbar: no; menubar: no; resizable: yes; scrollbar: no; location: no; status: no;");
  	}
</script>
</html>