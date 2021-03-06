<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>
<%@page import="com.bjsasc.plm.core.util.DateTimeUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstLifeCycle"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.core.system.access.operate.Operate"%>
<%@include file="/ddm/public/ddm.jsp" %>


<%@page contentType = "text/html; charset=UTF-8" pageEncoding="GBK"%>
<%
	String contextPath = request.getContextPath();
    String oid = (String) session.getAttribute(ConstUtil.DISTRIBUTE_ORDER_OID);
    String disorderoid = (String) session.getAttribute(ConstUtil.DISTRIBUTE_ORDER_OID);
    if(oid == null){
		oid = RequestUtil.getParamOid(request);
	}
	String classId = DistributeInfo.CLASSID;
	String spot = "ListDistributeInfos";
	String gridId = "distributeInfoGrid";
	String gridIdleft = "disTree";
	String gridTitle = "分发信息";
	String toolbarId = "ddm.distributeinfo.list.toolbar";
	
	String commonInfoTitle = "公共信息";
	String lc_new=ConstUtil.LC_NEW.getName();//获得生命周期是新建
	String approveTerminate=ConstUtil.LC_APPROVE_TERMINATE.getName();//获得生命周期是审批终止
	String scheduling=ConstUtil.LC_SCHEDULING.getName();//获得生命周期是调度中
	String backoff=ConstUtil.LC_BACKOFF.getName();//获得生命周期是已回退
	
	Persistable obj = Helper.getPersistService().getObject(oid);
	DistributeOrder dis = (DistributeOrder)obj;
	
	String addPrincipalUrl = contextPath + "/ddm/distribute/distributeInfoHandle!addPrincipals.action";
	
	
	//String contentUrl = contextPath + "/ddm/distribute/distributeInfoHandle!getDistributeInfosByOids.action";
	String contentUrl = contextPath + "/ddm/distribute/distributeInfoHandle!getDistributeObjsByInfoOids.action";
	
	
	String deadLineDate = DateTimeUtil.getCurrentDate(ConstUtil.C_DISDEADLINE_DELAY_DAY);
	String disUrgent = (String)request.getAttribute("disUrgent");
	String deadDate = (String)request.getAttribute("deadLineDate");
	String disStyle = (String)request.getAttribute("disStyle");
	if(deadDate == null){
		deadDate = DateTimeUtil.getCurrentDate(ConstUtil.C_DISDEADLINE_DELAY_DAY);
		//deadDate = "";
	}
	String qReceive_sel = "checked=\"true\"";
	String qReceive_cus = "";
	String disStyle_0 = "checked=\"true\"";
	String disStyle_1 = "";
	if ("1".equals(disUrgent)) {
		qReceive_sel = "";
		qReceive_cus = "checked=\"true\"";
	}
	if ("1".equals(disStyle)) {
		disStyle_0 = "";
		disStyle_1 = "checked=\"true\"";
	}
	if (!lc_new.equals(dis.getLifeCycleInfo().getStateName())
			&& !approveTerminate.equals(dis.getLifeCycleInfo().getStateName())
			&& !scheduling.equals(dis.getLifeCycleInfo().getStateName())
			&& !backoff.equals(dis.getLifeCycleInfo().getStateName())) {
		
		qReceive_sel += " disabled=\"true\"";
		qReceive_cus += " disabled=\"true\"";
		disStyle_0 += " disabled=\"true\"";
		disStyle_1 += " disabled=\"true\"";
	}
	String stateOfSended=ConstLifeCycle.LC_DISTRIBUTED.getName();
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
						<input value=0  type=radio name=disUrgent id=disUrgent0 <%=qReceive_sel%> onclick="updateDisUrgent('0')"/>
						普通&nbsp;
						<input value=1 type=radio name=disUrgent id=disUrgent1 <%=qReceive_cus%> onclick="updateDisUrgent('1')"/>
						加急&nbsp;&nbsp;
					</td>
					<td class="left_col AvidmW100">分发方式：</td>
					<td class="e-checked-text">
						<input value=0  type="radio" name="disStyle" id="disStyle0" <%=disStyle_0%>  onclick="updateDisstyle('0')"/>
						正式分发&nbsp;
						<input value=1 type="radio" name="disStyle" id="disStyle1" <%=disStyle_1%>  onclick="updateDisstyle('1')"/>
						一次性分发&nbsp;&nbsp;
					</td>
					</tr>
				</table>
		</td></tr></table>
<!-- 公共信息 结束 -->
		<div id="gridTop" name="gridTop"><div>
	</td></tr><tr><td>
	 	 <table id="aaa" height="100%" width="100%" cellSpacing="0" cellPadding="0"
			border="0"   onClick="getDistributeInfosByclick()"  >

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

<input type="hidden" name="distributeObjectOids" id="distributeObjectOids"/>
<input type="hidden" name="distributeInfoOids" id="distributeInfoOids"/>
<input type="hidden" name="distributeOrderOids" id="distributeOrderOids"/>
<input type="hidden" name="distributeOrderObjectLinkOids" id="distributeOrderObjectLinkOids"/>
<input type="hidden" name="deleterows" id="deleterows"/>
<input type="hidden" name="type" id="type"/>
<input type="hidden" name="iids" id="iids"/>
<input type="hidden" name="disMediaTypes" id="disMediaTypes"/>
<input type="hidden" name="disInfoNums" id="disInfoNums"/>
<input type="hidden" name="notes" id="notes"/>
<input type="hidden" name="disType" id="disType"/>
<input type="hidden" name="updateDisUrgentValue" id="updateDisUrgentValue"/>
<input type="hidden" name="updateDisstylValue" id="updateDisstylValue"/>
<input type="hidden" name="outSignInfoOids" id="outSignInfoOids"/>
<input type="hidden" name="historyOids" id="historyOids"/>
</form>
</body>
<script>
/**
 *点击分发信息显示对应的分发数据
 */
function getDistributeInfosByclick(node) {
	var selections = getSelections();
	var disinfoname="";
	var disinfonames="";
	if(selections.length>0){
	for(var i = 0; i < selections.length; i++){
		var oid = selections[i].OID;
		var disinfoname="'"+selections[i].DISINFONAME+"'";
		disinfoname+= ",";
		disinfonames += disinfoname;
	}
		$.ajax({
		type:"POST",
		dataType:"json",
		data:"disinfoname="+disinfonames+"&oid="+"<%=oid%>",
		url: "<%=contentUrl%>",
		success: function(result){
			parent.frames("leftTree").selectdataFromDisInfo(result.OID);
		},
		error:function(){}
	});
	}
/* 	else
		{
		//parent.frames("leftTree").reload("");
		// parent.frames("leftTree").clearAll();
		
		}  */
	
}

function getSelections() {
	var table = pt.ui.get("<%=gridId%>");
	var selections = table.getSelections();
	return selections;  
}

	var container = {};
	
	// 提交发放
	function submitDistribute(){
		ddmValidation("<%=oid%>","<%=Operate.MODIFY%>","submitDistributeCallBack");
	}
	
	function submitDistributeCallBack(orderOid) {
		var table = pt.ui.get("<%=gridId%>");
		var selections = table.getRows();
		if (selections.length == 0) {
			plm.endWait();
			plm.showMessage({title:'错误提示', message:"没有可操作的数据", icon:'1'});
			return;
		}
		if (!plm.confirm("确定提交发放吗?")) {
			plm.endWait();
			return;
		}
		var url = "<%=contextPath%>/ddm/distribute/distributeInfoHandle!createDistributeTask.action";
		var oids = "";
		var outSignInfoOids = "";
		for(var i=0;i<selections.length;i++){
			oids += selections[i].OID + ",";
			if(selections[i].DISMEDIATYPE == '跨域'){
				outSignInfoOids += selections[i].OID + ",";
			}
		}
		oids = oids.substring(0, oids.length - 1);
		$("#distributeInfoOids").val(oids);
		$("#outSignInfoOids").val(outSignInfoOids);
		plm.startWait();
		$.ajax({
			url:url,
			type:"post",
			dataType:"json",
			data: $("#main_form").serializeArray(),
			success:function(result){
				plm.endWait();
				if(result.SUCCESS != null && result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				pageReload();
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"操作失败！", icon:'1'});
			}
		});
	}
	
	function gatherAffectobjects() {
		plm.showMessage({title:'系统提示', message:"正在建设中。。。", icon:'2'});
	}

	// 选择用户
	function addUsers(){
		plm.startWait();
		ddmValidation("<%=oid%>","<%=Operate.MODIFY%>","addUsersCallBack");
	}
	
  	function addUsersCallBack(orderOid){
  		// 验证
		<%=ValidateHelper.buildCheck()%>
			
  		clearDistributeObjectsIid();
		//左边导航树页面
		var data = parent.frames("leftTree").getSelections();
		if(data.length == 0){
			plm.endWait();
			plm.showMessage({title:'错误提示', message:"请选择需要分发的数据", icon:'1'});
			return;
		}
		setDistributeObjectsIid(data);
		var configObj = 
			{ 
				IsModal : "true",
				SelectMode : "multiple",
				returnType : "arrayObj",
				scope : "<%=ConstUtil.VISTYPE_ALL%>"
			};
		plm.endWait();
		var retObj = ddm.tools.selectUser(configObj);
		if(retObj){
			doBindRole(retObj, "<%=ConstUtil.DISINFOTYPE.USER%>");
	  	}
  	}
  	
  	function addOutSignUsers(){
  		plm.startWait();
		ddmValidation("<%=oid%>","<%=Operate.MODIFY%>","addOutUsersCallBack");
		
	}
  	
  	function addOutUsersCallBack(){
  		var linkData = parent.frames("leftTree").getSelections();
  		plm.endWait();
		if(linkData.length == 0){
			plm.showMessage({title:'错误提示', message:"请选择需要分发的数据", icon:'1'});
			return;
		}
		setDistributeObjectsIid(linkData);
		var linkOids = $("#distributeOrderObjectLinkOids").val();
  		<%-- plm.openWindow("<%=contextPath%>/ddm/public/selectoutorg.jsp?linkOids="+linkOids,500,500,'outSignUser'); --%>
  		var url = contextPath + '/ddm/public/selectoutorg.jsp?';

  		var windowid = "addOutUsers" + "_" + new Date().getTime();
  		// 打开窗口
  		plm.openWindow(contextPath + "/plm/load.html", 500, 500, windowid);
  		// 在前台中执行
  		plm.openByPost(url, pt.ui.JSON.encode(linkOids), windowid);
  	}
      	
  	//选择组织
  	function addOrg(){
  		plm.startWait();
		ddmValidation("<%=oid%>","<%=Operate.MODIFY%>","addOrgCallBack");
	}
	
    function addOrgCallBack(orderOid){
  		// 验证
		<%=ValidateHelper.buildCheck()%>
			
  		clearDistributeObjectsIid();
		//左边导航树页面
		var data = parent.frames("leftTree").getSelections();
		if(data.length == 0){
			plm.endWait();
			plm.showMessage({title:'错误提示', message:"请选择需要分发的数据", icon:'1'});
			return;
		}
		setDistributeObjectsIid(data);
		var configObj = 
  		{ 
  			IsModal : "true",
  			SelectMode : "multiple",
  			returnType : "arrayObj",
  			scope : "<%=ConstUtil.VISTYPE_ALL%>"
  		};
		plm.endWait();
	    var retObj = ddm.tools.selectOrg(configObj);
		if(retObj){
			doBindRole(retObj, "<%=ConstUtil.DISINFOTYPE.ORG%>");
		} 
    }
    
    function clearDistributeObjectsIid() {
		$("#distributeOrderObjectLinkOids").val("");
    }
    
    function setDistributeObjectsIid(selections) {
    	var oids = "";
    	var deleterows = "";
    	for(var i = 0; i < selections.length; i++){
			var oid = selections[i]["DISTRIBUTEORDEROBJECTLINKOID"];
			var deleterow = selections[i]["deleterow"];
			if(oids != ""){
				oids += ",";
			}
			oids += oid;
			
			if(deleterows != ""){
				deleterows += ",";
			}
			deleterows += deleterow;
		}
		$("#distributeOrderObjectLinkOids").val(oids);
		$("#deleterows").val(deleterows);
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
    	var distributeObjectIids = "";
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
    			arrNote[i] = "null";
    		}
    		notes += arrNote[i];
    	}
		$("#type").val(type);
		$("#iids").val(iids);
		$("#disMediaTypes").val(disMediaTypes);
		$("#disInfoNums").val(disInfoNums);
		$("#notes").val(notes);
		$("#disType").val("1");

		$.ajax({
				type: "post",
				url: "<%=addPrincipalUrl%>",
				dataType: "json",
				data: $("#main_form").serializeArray(),
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
    
    // 删除分发信息
    function deleteDistributeInfo(){
		ddmValidation("<%=oid%>","<%=Operate.MODIFY%>","deleteDistributeInfoCallBack");
	}
    
	function deleteDistributeInfoCallBack(orderOid){
		var table = pt.ui.get("<%=gridId%>");
		var selections = table.getSelections();
		if (selections.length == 0) {
			plm.endWait();
			plm.showMessage({title:'错误提示', message:"请至少选择1个操作对象!", icon:'1'});
			return;
		}
		var oids = "";
		var life = new Array();
		for(var i=0;i<selections.length;i++){
			oids += selections[i].OIDS + ",";
			life.add(selections[i].LIFECYCLE_STATE);
		}
		oids = oids.substring(0, oids.length - 1);
		if(life.length != 0){
			for(var j = 0; j < life.length;j++){
				var stateName="<%=stateOfSended%>";
				if(stateName== life[j]){
					plm.endWait();
					plm.showMessage({title:'错误提示', message:"不能删除已发送的分发信息!", icon:'1'});
					return;
				}
			}
		}

		if (!plm.confirm("确定删除吗?")) {
			plm.endWait();
			return;
		} 
		var url = "<%=contextPath%>/ddm/distribute/distributeInfoHandle!deleteDistributeInfo.action?oid=<%=oid%>";
		plm.startWait();
		$("#distributeInfoOids").val(oids);
		$.ajax({
			url:url,
			type:"post",
			dataType:"json",
			data: $("#main_form").serializeArray(),
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
    	var url = "<%=contextPath%>/ddm/distribute/distributeObjectHandle!updateDistributeObjectLink.action";
    	
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
    	var url = "<%=contextPath%>/ddm/distribute/distributeObjectHandle!updateDistributedeadLinkDate.action";
    	
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
	
    // 更新分发信息
    function updateDistributeInfo(){
    	plm.startWait();
		ddmValidation("<%=oid%>","<%=Operate.MODIFY%>","updateDistributeInfoCallBack");
	}
	
  	function updateDistributeInfoCallBack(Orderoid){
    	var style = $("#disStyle0").attr("checked")?0:1;
    	var urgent= $("#disUrgent0").attr("checked")?0:1;
    	var deadDate = $("#deadLineDate").val();
    	plm.endWait();
  		window.location.href = "<%=contextPath%>/ddm/distributeinfo/distributeInfo_content_edit.jsp?oid=<%=oid%>&disUrgent="+urgent+"&deadLineDate="+deadDate+"&disStyle="+style;
  	}
	
    // 更新是否跟踪信息
    function updateDisInfoIsTrack(){
    	plm.startWait();
		ddmValidation("<%=oid%>","<%=Operate.MODIFY%>","checkDisOrdersHaveEco");
	}
    function checkDisOrdersHaveEco(oid){
    	var url = contextPath + "/ddm/distribute/distributeObjectHandle!checkDisOrdersHaveEco.action";
    	$.ajax({
    		url:url,
    		type:"post",
    		dataType:"json",
    		data:"OID="+oid,
    		success:function(result){
    			if(result.SUCCESS == "false"){
        			plm.endWait();
    				plm.showMessage({title:'提示', message:"发放单中无更改单或者技术通知单的对象,不用修改是否跟踪信息!", icon:'3'});
    				return;	
    			} else {
    				updateDisInfoIsTrackCallBack(oid);
    			}
    		
    		},
    		error:function(){
    			plm.endWait();
    			plm.showMessage({title:'错误提示', message:"操作失败！", icon:'1'});
    		}
    	});
    }	
  	function updateDisInfoIsTrackCallBack(orderoid){
		var url = "<%=contextPath%>/ddm/distributeinfo/disInfoIsTrack_frame.jsp";
		plm.endWait();
		window.showModalDialog(url,self,"dialogHeight: 512px; dialogWidth: 710px;  center: Yes; help: Yes; resizable: no; status: no;");
  	}
  	
  	// 查看历史分发信息
  	function searchHistory(){
  		
  		var selections = parent.frames("leftTree").getSelections();
  		//var linkData = parent.frames("leftTree").getSelections();
  		//plm.endWait();
 
  		//kangyanfei 2014-08-19
  		
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
    	var url="<%=contextPath%>/ddm/distributeinfo/distributeInfo_history_main.jsp?oid_linkid_str="+firstOid;
		window.showModalDialog(url,window,"dialogHeight: 600px; dialogWidth: 1230px; left: 150px; top: 150px; toolbar: no; menubar: no; resizable: yes; scrollbar: no; location: no; status: no;");
  	}
    
    // 重新加载
	function reload(){
		var hideIcon = $(".hideIcon").attr("class");
		if(hideIcon == "hideIcon"){
			 divSelected($(".hideIcon"));
		}
		var table = pt.ui.get("<%=gridId%>");
		table.reload();
	}
 
	//更新分发方式
	function updateDisstyle(value){
		plm.startWait();
		$("#updateDisstylValue").val(value);
		ddmValidation("<%=oid%>","<%=Operate.MODIFY%>","updateDisstyleCallBack");
	}
	
	function updateDisstyleCallBack(orderOid){
		var value = $("#updateDisstylValue").val();
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
    	var url = "<%=contextPath%>/ddm/distribute/distributeObjectHandle!updateDistributeObjectLinkByDisStyle.action";
    	
		$.ajax({
			url:url,
			type:"post",
			dataType:"json",
			data: "<%=KeyS.OIDS%>="+linkOids+"&disStyle="+value,
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

	function tableReload(){
		var table = pt.ui.get("<%=gridId%>");
		table.reload();
	}
</script>
</html>
