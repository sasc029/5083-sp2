<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.grid.GridUtil"%>
<%@page import="com.bjsasc.plm.core.system.principal.User"%>
<%@page import="com.bjsasc.plm.core.context.ContextHelper"%>
<%@page import="com.cascc.platform.aa.api.util.AADomainUtil"%>
<%@page import="java.util.Vector"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.bjsasc.plm.operate.ModelUtil"%>
<%@page import="com.bjsasc.plm.core.session.SessionHelper"%>
<%@page import="com.cascc.avidm.login.model.PersonModel"%>
<%@page language="java"%>
<%@page session="true"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@ page import="java.util.Map"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.KeyS" %>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.type.restrict.select.Select"%>
<%@page import="com.bjsasc.plm.type.restrict.select.SelectOption"%>
<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.core.folder.*" %>
<%@page import="com.bjsasc.plm.core.persist.PersistHelper"%>
<%@page import="com.bjsasc.plm.common.search.ViewProviderManager" %> 
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="java.util.*"%>
<%@page import="com.cascc.avidm.util.SplitString"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject"%>
<%@page import="com.bjsasc.avidm.core.site.SiteHelper"%>
<%@ page import="com.bjsasc.avidm.core.site.Site"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String title = "综合查询";
	String contextPath = request.getContextPath();
	String init = (String)request.getAttribute("init");
	//String init = request.getParameter("init");
	
	String oid = request.getParameter("oid");
	String classId = DistributeObject.CLASSID;
	String spot = "distributeCommonSearch";
	String gridId = "distributeCommonSearchtGrid";
	String toolbarId = "ddm.commonsearch.list.toolbar";
	
	if ("true".equals(init)) {
		session.removeAttribute("DDM_DISTRIBUTE_INSIDE");
	}
	List allSiteList = SiteHelper.getSiteService().findAllSite();
%>	
<html>
<head>
	<title><%=title%></title>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
	<script type="text/javascript" src="<%=contextPath%>/plm/ui/editor/date/WdatePicker.js"></script>
	<script type="text/javascript"	src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
	<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/plm/javascript/messager.js"></script>
<style>
.noTable{border:0px; border-collapse:collapse; font-size:12px;}
.noTable td{border:0px; padding-left:0px; height:25px; line-height:25px;}
</style>
</head>
<body>
<form name="searchForm" id="searchForm">
	<table width="100%" height="100%" cellSpacing="0" cellPadding="0" border="0">
		<%-- <jsp:include page="/plm/common/grid/control/grid_of_title.jsp">
			<jsp:param name="gridTitle" value="<%=title%>"/>
		</jsp:include> --%>		
		<tr><td>
			<table width="100%" height="100%" cellSpacing="0" cellPadding="0" border="0">
		<tr id="searchConditionTD"><td valign="top" height="300">
		<table width="100%" height="100%" class="avidmTable" border="0" cellspacing="0" cellpadding="0">
			<TR>
				<TD class="left_col AvidmW150">关键字： </TD>
				<TD>
					<table class="noTable">
					<TR><TD>
					<input type="text" class="pt-textbox AvidmW270" name="keywords" id="keywords" />
					</TD>
					<TD width="5"></TD>
					<TD>
					<a href="#" onclick="clearValue('keywords');">
						<img src="<%=contextPath%>/plm/images/common/clear.gif">
					</a>
					</TD></TR></table>
				</TD>
			</TR>
				<tr>
						<td class='left_col AvidmW150'>跨域发起单位：</td>
						<td class='pt-field'><select id="sourceDomainIID"
							name="sourceDomainIID" style="width: 340px">
								<option value="">所有单位</option>
								<%
				           		    int domainLength = allSiteList.size();
				          		    for(int i = 0; i < domainLength; i++){
				          				Site site = (Site)allSiteList.get(i);
				          		%>
								<option value="<%=site.getInnerId() %>"><%=site.getSiteData().getSiteName() %></option>
								<% 
				          			}
				          		%>
						</select></td>
					</tr>
					<tr>
						<td class='left_col AvidmW150'>跨域接收单位：</td>
						<td class='pt-field'><select id="receiveDomainIID"
							name="receiveDomainIID" style="width: 340px">
								<option value="">所有单位</option>
								<%
				           		    int length = allSiteList.size();
				          		    for(int i = 0; i < length; i++){
				          				Site site = (Site)allSiteList.get(i);
				          		%>
								<option value="<%=site.getInnerId() %>"><%=site.getSiteData().getSiteName() %></option>
								<% 
				          			}
				          		%>
						</select></td>
					</tr>
			<TR><TD class="left_col AvidmW150" >搜索条件：</TD>
					<TD><span id="typeNames"></span>
						<div class='pt-button' width='43' id='com_bjsasc_plm_operate_more', tips='添加' text='<img src="<%=request.getContextPath() %>/plm/images/common/add.gif">' icon='' iconAlign='left'  onclick="selectObjectAttr()">
						</div>查找：<input type="radio" name="blur" value="0" id="all" checked/>符合所有条件
						<!--  <input type="radio" name="blur" value="1" id="any"/>符合任一条件-->
					</TD>
			</TR>
			<TR style="display:none" id="selfTr">
				<TD class="left_col AvidmW150" >&nbsp;</TD>
					<TD>
					<div id="addOption">
					    <table id="searchCondition" style="font-size:12px" border="0">
					     <%--    <tr>
					            <td><div id="NAME" name="名称" style="height:20px;"optionType="input"/>
							         <div style="float:left;align="left">名称</div> </td>
					            <td><select style="float:left">
										<option  value="=">等于</option>
										<!-- <option  value="!=">不等于</option> -->
							  		 </select>
									<input type="text" name="queryName" class="pt-text" value="" style="float:left;margin-left:3px;" />
									<img src='<%=request.getContextPath() %>/plm/images/common/delete.gif'  onclick="delObjAttr(this)"/>
								</td>
					        </tr>
						    <tr>
							    <td>
							   	 	<div id="ID" name="编号" style="height:20px;"optionType="input"/>
									<div  style="float:left; align="left" >编号</div>
							    </td>
							    <td>
							        <select style="float:left">
										<option  value="=">等于</option>
										<!--  <option  value="!=">不等于</option>-->
									</select>
									<input type="text" name="queryNumber" class="pt-text" value="" style="float:left;margin-left:3px;"/>
									<img src='<%=request.getContextPath() %>/plm/images/common/delete.gif'  onclick="delObjAttr(this)"/>
							    </td>
						    </tr> --%>
						</table>				
					</div>
					</TD>
			</TR>
			<TR>
				<TD class="left_col AvidmW150" >搜索范围：</TD>
				<TD>
				     <input type="radio" name="searchScope" id="allContext" onclick="searchScopeClick();"/>所有上下文
				    <input type="checkbox" id="favoritedContext" onclick="searchScopeClick2();">我收藏的上下文&nbsp;&nbsp;<a name="a" href="#" onclick="pt.ui.showMenu('submenu_com_bjsasc_plm_operate_more2')">选择</a><br>
				     <div id="excludeOption">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
				     <div id='submenu_com_bjsasc_plm_operate_more2' class='pt-menu'>                                        
				    	<div  text='所有上下文' tips='所有上下文'  width='60'  iconAlign='left' onclick='excludeOption(this,"AllContext")'  class='pt-menuitem' >    </div>
						</div>
				</TD>
			</TR>
			<TR>
				<TD class="left_col AvidmW150">搜索对象：</TD>
				<TD>
				<input type="radio" name="rdoDistributeType" id="rdoDistributeType_all" value="ALL" onclick="displayDistributeType(this)" checked="true"/>
				<label for='rdoDistributeType_all' style="font-size: 12px;">全部</label>
				<input type="radio" name="rdoDistributeType" id="rdoDistributeType_cus" value="CUS" onclick="displayDistributeType(this)"/>
				<label for='rdoDistributeType_cus' style="font-size: 12px;">自定义</label>
				<div id="divDistributeType" style="display:block;">
	<% 
	Collection<SelectOption> distributeTypes = ConstUtil.DISTRIBUTETYPES.getOptionMap().values();
	
	for (SelectOption option : distributeTypes) {
		String value = option.getValue();
		String text = option.getText();	
	%> 
				<span class="padding:10px;white-space:nowrap;">
					<input type='checkbox' class="pt-checkbox" name='cbDistributeTypes' id='cbDistributeTypes_<%=value %>'
						value="<%=value%>" onClick="checkTypeCus()"/>
					<label for='cbDistributeTypes_<%=value %>' style="font-size: 12px;" onClick="checkTypeCus()"><%=text%></label>
				</span>
	<%
		}
	%>
					</div>
				</TD>
			</TR> 
			<TR>
				<td class="exegesis"></td>
				<TD style="height:40px">
					<div class="pt-formbutton" text="搜索" id="submitbutton" onclick="searchAll();"></div>
					&nbsp;&nbsp;&nbsp;
					<div class="pt-formbutton" text="清空" id="submitbutton" onclick="consClear();"></div>
				</TD>
			</TR>
		</table>
	</TD></TR>
	<tr height="10">
		<td class="splitButton"> 
			<img onClick="setVisible(this,'searchConditionTD');resizeTable();" src="<%=request.getContextPath()%>/plm/images/common/splitTop.gif"/> 
		</td>
	</tr>
	<tr><td valign="top">
		<table height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0">
			<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
				<jsp:param name="spot" value="<%=spot%>"/>
				<jsp:param name="gridId" value="<%=gridId%>"/>
				<jsp:param name="gridTitle" value=""/>
				<jsp:param name="toolbar_modelId" value="<%=toolbarId%>"/>
				<jsp:param name="onLoadSuccess" value="onLoadSuccess()"/>
				<jsp:param name="operate_container" value="container"/>
			    <jsp:param name="toolbarViewChange" value="false" />
			</jsp:include>
		</table>
	</td></tr>
			</table>
		</td></tr>
	</table>
<input type='hidden' name="queryCode" id="queryCode"/>
<input type='hidden' name="hidDistributeTypes" id="hidDistributeTypes"/>
<input type='hidden' name="hidDistributeStates" id="hidDistributeStates"/>
<input type='hidden' name="rdoDistributeState" id="rdoDistributeState"/>
</form>
<form id="download" name="download" method="POST">
	<input type='hidden' name="SEARCH_TEXT" id="SEARCH_TEXT"/>
	<input type='hidden' name="<%=KeyS.OIDS%>" id="<%=KeyS.OIDS%>"/>
</form>
<script type="text/javascript">
//$("#sourceDomainIID").attr("disabled", true);
	var container = {};
	var creatorOID="";
	var constant_="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	function onLoadSuccess() {
		onLoadSuccess_ddm("<%=gridId%>");
	}
				
	function checkStateCus() {
		$("#rdoDistributeState_cus").attr("checked", true);
	}
	
	function checkTypeCus() {
		var cbDistributeTypes = "";
		$('[name="cbDistributeTypes"]:checked').each(function(){
			cbDistributeTypes += $(this).val() + ",";
		})
			if(cbDistributeTypes.indexOf("DistributeElecTask")>-1){
				document.getElementById("sourceDomainIID").disabled = false;
				document.getElementById("receiveDomainIID").disabled = false;
			}else{
				document.getElementById("sourceDomainIID").disabled = true;
				document.getElementById("receiveDomainIID").disabled = true;
			}
		
		$("#rdoDistributeType_cus").attr("checked", true);
	}
	function clearValue(fields) {
		var arrField = fields.split(",");
        $.each(arrField,function(n,value) {  
			eval("$('#"+value+"').val('');");
        });
	}
	
	//延迟初始化页面
	setTimeout("initPage()",200);
					
	// 项目初始化
	function initPage(){
					
	}
	
	function reloadOrder(){
		searchAll();
	}
	
	function changeModifyDate(obj) {
		if (obj.value == "past") {
			pt.ui.get("qCreateDateFrom").setEnable(false);
			pt.ui.get("qCreateDateTo").setEnable(false);
		}
	}
					
	function displayDistributeType(obj) {
		if (obj.value == "ALL") {
			document.getElementById("sourceDomainIID").disabled = false;
			document.getElementById("receiveDomainIID").disabled = false;
			//$("#divDistributeType").css("display","none");
			$('[name="cbDistributeTypes"]').removeAttr("checked");
		} else {
			//$("#divDistributeType").css("display","block");
		}
	}
	
	function displayDistributeState(obj) {
		if (obj.value == "ALL") {
			//$("#divDistributeState").css("display","none");
			$('[name="cbDistributeStates"]').removeAttr("checked");
		} else {
			//$("#divDistributeState").css("display","block");
		}
	}	

	function changeDistributeType(obj) {
		var checked = obj.checked;
		$('input[type="checkbox"][name="cbDistributeTypes"]').each(
			function() {
            	$(this).attr("checked", checked);
            }
        );
	}
	function changeDistributeState(obj) {
		var checked = obj.checked;
		$('input[type="checkbox"][name="distributeStates"]').each(
			function() {
            	$(this).attr("checked", checked); 
            }
        );
	}	

	function consClear() {
		try {
		$("#keywords").val("");
		$("#sourceDomainIID").val("");
		$("#receiveDomainIID").val("");
		$("#queryName").val("");
		$("#queryNumber").val("");
		//$("#queryCode").val("");
		$("#allContext").attr("checked",false);
		$("#favoritedContext").attr("checked",false);
		$("#excludeOption").html("");
		$("#creator").val("");
		$("#creatorName").val("");
		$("#modifier").val("");
		$("#modifierName").val("");
		$("#queryCreateDateFrom").val("");
		$("#queryCreateDateTo").val("");
		$("#queryModifyDateFrom").val("");
		$("#queryModifyDateTo").val("");
		$("#qModifyDate_sel").attr("checked", true);
		$("#qCreateDate_sel").attr("checked", true);
		$("#blur_and").attr("checked", true);
		$("#rdoDistributeType_all").attr("checked", true);
		//$("#rdoDistributeState_all").attr("checked", true);
		//$("#divDistributeType").css("display","none");
		//$("#divDistributeState").css("display","none");
		$('[name="cbDistributeTypes"]').removeAttr("checked");
		//$('[name="cbDistributeStates"]').removeAttr("checked");
		$("[name='cbDistributeStates']").val("rdoDistributeState_all");
		pt.ui.get("createPastDays").setValue("0");
		pt.ui.get("modifyPastDays").setValue("0");
		} catch (e) {}
	}
	/**
	 * 把一个空间的值拼成一个对象的属性，
	 * 以便于把界面值组件为json字符串提交到后台
	 */
	function searchAll() {
		// 搜索对象
		var cbDistributeTypes = "";
		$('[name="cbDistributeTypes"]:checked').each(function(){
			cbDistributeTypes += $(this).val() + ",";
		})
		// 状态
		var cbDistributeStates = "";
		var rdoDistributeState="";
		cbDistributeStates=$("[name='cbDistributeStates']").find("option:selected").val();
		if(cbDistributeStates!="rdoDistributeState_all"){
			rdoDistributeState="CUS";
		};
		$("#hidDistributeTypes").val(cbDistributeTypes);
		$("#hidDistributeStates").val(cbDistributeStates);
		$("#rdoDistributeState").val(rdoDistributeState);
		$("#queryCode").val(getSearchScope());
		if($("#CREATETIME").length>0){
			var fromTime =$("#queryCreateDateFrom").val();
			var toTime=$("#queryCreateDateTo").val();
			 if(fromTime==""){
				 plm.showMessage({title:'错误提示', message:"开始时间不能为空!", icon:'3'});
				 return;
			 }
			 if(toTime==""){
				 plm.showMessage({title:'错误提示', message:"结束时间不能为空!", icon:'3'});
				 return;
			 }
			 if(fromTime!="" && toTime!=""){
					if(fromTime>toTime){
						plm.showMessage({title:'错误提示', message:"开始日期不能大于结束日期!", icon:'3'});
						return;
					}	
				}
		}
		var url = "<%=contextPath%>/ddm/distribute/commonSearchHandle!getAllDistributeObjects.action";
		plm.startWait();
		$.ajax({
			url:url,
			type:"post",
			dataType:"json",
			data:$("#searchForm").serializeArray(),
			success:function(result){
				plm.endWait();
				if(result.FLAG==0){
			    	messager.showTipMessager({'content':'没有数据'});
				}else{
					$("#searchTab").show();
	        		setVisible(this,'searchConditionTD');resizeTable();
				}
				if(result.SUCCESS != null &&result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				var table = pt.ui.get("<%=gridId%>");
				table.reload();
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"操作失败！", icon:'1'});
			}
		});
	}
	
	function selectTypes() {
		var selectedTypes="types";
		//前台 url
		var url = "<%=contextPath%>/plm/search/commonsearch/TypesList.jsp?CALLBACK=afterSelectTypes&selectedTypes="+selectedTypes;
		
		//打开窗口
		plm.openWindow("<%=contextPath%>/plm/load.html", 500, 500,'T');
		
		//在前台中执行
		plm.openByPost(url, null, 'T');
	}
		
	//如果初始化页面时需要执行搜索，则调用Search
	function afterSelectTypes(o){ 
		if (o != null) {
			document.getElementById("typeNames").innerText = o.NAMES;
			//document.getElementById("types").value = o.TYPES;
		}
	}

	function selectUser(inputId){
		var configObj = 
			{ 
				IsModal : "true",
				SelectMode : "single",
				returnType : "arrayObj",
				scope : "<%=AADomainUtil.VISTYPE_ALL%>"
			};
		var retObj = pt.sa.tools.selectUser(configObj);
		if(retObj){
			$("#creator").val(retObj.arrUserIID[0]);
			$("#creatorName").val(retObj.arrUserName[0]);
	  	};
	}
	
	// 分割线
	function resizeTable(){
		var grid = pt.ui.get("<%=gridId%>"); 
		grid.set({width :$(grid.renderTo).width(),height :$(grid.renderTo).height()-22});
		grid.pagingbar.set( {width :$(grid.renderTo).width(),height :22});
	};
	
	// 补发
	function reDistribute() {
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if (data == null || data.length != 1) {
			plm. showMessage({title:'提示', message:"请选择一个项目后再执行补发操作!", icon:'3'});
			return;
		}
		
		var dataInnerId = data[0].DATAINNERID;
		var dataClassId = data[0].DATACLASSID;
		
		var oid = dataClassId + ":" + dataInnerId;
		if(data[0].CLASSID=="DistributeOrder"){
			var url = "<%=contextPath%>/ddm/distributeorder/createReissueDistributeOrder.jsp?<%=KeyS.CALLBACK%>=reload&oid=" + data[0].OID;
			plm.OpenWindowAndReload(url);
		} else if(data[0].CLASSID=="DistributeObject"){
			var url = "<%=contextPath%>/ddm/distributeorder/createDistributeOrder.jsp?<%=KeyS.CALLBACK%>=reload&oid=" + oid;
			plm.OpenWindowAndReload(url);
		}else {
			plm. showMessage({title:'提示', message:"请选择现行文件或发放单后再执行补发操作!", icon:'3'});
			return;
		}
		
		
		
		//判断是现行文件还是发放单
		
		
		
		
	}
	
	// 打印文档
	function dataPrint() {
		
	}
	
	// 导出查询结果
	function dataExport() {
		var url = "<%=contextPath%>/ddm/distribute/commonSearchHandle!dataExport.action";

		var table = pt.ui.get("<%=gridId%>");
		var selections = table.getSelections();
		var oids = "";
		for(var i=0;i<selections.length;i++){
			oids += selections[i].OID + ",";
		}
		oids = oids.substring(0, oids.length - 1);
		$("#<%=KeyS.OIDS%>").val(oids);
		$("#SEARCH_TEXT").val($("#distributeCommonSearchtGrid_searchText").val());
		$("#download").attr("action", url);
		$("#download").submit();
	}


	//得到对象类型    的值
	function getObjectType(){
		var str="";
		var isFirst=true;
		$("#typeNames").children().children().each(function(){
			if($(this).attr("checked")=="checked"){
				if(isFirst){
					str=$(this).attr("name");
					isFirst=false;
				}
				else{
					str=str+","+$(this).attr("name");
				}
			}
		});
		if($("#all").attr("checked")=="checked"){
			str="all";
		}
		return str;
	}

	function selectObjectAttr() {
		var typeId="all";
		//前台 url
		var url = "<%=request.getContextPath()%>/ddm/commonsearch/getObjectAttr.jsp?CALLBACK=afterselectObjectAttr&typeId="+typeId;
		//打开窗口
		plm.openWindow("<%=request.getContextPath()%>/plm/load.html", 500, 500,'T');
		//在前台中执行
		plm.openByPost(url, null, 'T');
		
	}

	function afterselectObjectAttr(o){
		$("#selfTr").css('display','');
		if (o != null) {
			var name=o.NAMES.split(",");
			var id=o.IDS.split(",")
			var type=o.TYPES.split(",");
			for(var i=0;i<name.length;i++){
						if(type[i]=='time'){
							if($("#queryCreateDateFrom").length>0){
								plm. showMessage({title:'提示', message:"该搜索条件已存在!",icon:'3'});
								return;
							}else{
							$("#searchCondition").append("<tr><td><div id='"+id[i]+"' name='"+name[i]+"' optionType='"+type[i]+"'/>"
							+"<div style='float:left;' align='left' >"+name[i]+"</div></td><td><input type='hidden' id='qCreateDate' name='qCreateDate' value='qCreateDate'><select style='float:left'><option  value='='>等于</option></select><input class='AvidmW150 Wdate' type='text' id='queryCreateDateFrom' name='queryCreateDateFrom' onfocus='WdatePicker({skin:\"whyGreen\",dateFmt:\"yyyy-MM-dd\"})' />&nbsp;&nbsp;到：<input class='AvidmW150 Wdate'type='text' id='queryCreateDateTo' name='queryCreateDateTo' onfocus='WdatePicker({skin:\"whyGreen\",dateFmt:\"yyyy-MM-dd\"})' /><img src='<%=request.getContextPath() %>/plm/images/common/delete.gif'  onclick='delObjAttr(this)'/></td></tr>");
							}
						}
						else if(type[i]=='input'){
							if(id[i]=="NAME"){
								if($("#queryName").length>0){
								plm.showMessage({title:'提示', message:"该搜索条件已存在!",icon:'3'});
								return;
							}else{
							 $("#searchCondition").append("<tr><td><div id='"+id[i]+"' name='"+name[i]+"' style='' optionType='"+type[i]+"'/><div style='float:left;' align='left' >"+name[i]+"</div></td><td><select style='float:left'><option  value='='>等于</option></select><input type='text' name='queryName' id='queryName' class='pt-text' value='' style='float:left;margin-left:3px;'/><img src='<%=request.getContextPath() %>/plm/images/common/delete.gif' onclick='delObjAttr(this)'/></td></tr>");
							}
							}else if(id[i]=="ID"){
								if($("#queryNumber").length>0){
								plm. showMessage({title:'提示', message:"该搜索条件已存在!",icon:'3'});
								return;
							}else{
								$("#searchCondition").append("<tr><td><div id='"+id[i]+"' name='"+name[i]+"' style='' optionType='"+type[i]+"'/><div style='float:left;' align='left' >"+name[i]+"</div></td><td><select style='float:left'><option  value='='>等于</option></select><input type='text' name='queryNumber' id='queryNumber' class='pt-text' value='' style='float:left;margin-left:3px;'/><img src='<%=request.getContextPath() %>/plm/images/common/delete.gif' onclick='delObjAttr(this)'/></td></tr>");
							}
							}
						}
						else if(type[i]=='select'){
							var option="";
							//更改原因
							if(id[i]=='CHANGEREASON'){
								getChangeReasonSelect();					
							}
							//更改类别
							else if(id[i]=='CHANGETYPE'){
								gettypeSelectUrlSelect();
							}
							//提出单位
							else if(id[i]=='DEPARTMENT'){
								getdepartmentUrlSelect();
							}
							//所属上下文
							else if(id[i]=='CONTEXT'){
								option=selectDomainInfo("");
							}
							//状态
							
							else if(id[i]=='STATENAME'){
								selectStateInfo();
							}
							//工作状态
							else if(id[i]=='WORKSTATE'){
							    option="<option value='checkin'>检入</option><option value='checkout'>检出</option><option value='new'>新建</option><option value='working'>工作副本</option>"
							}
							//密级
							else if(id[i]=='SECURITY_LEVEL_INFO'){
								option=selectSecurityLevelInfo();
							}
							else if(ID=="PROFESSIONNAME"){
								option=selectDisciplineInfo();
							}
							//阶段
							else if(id[i]=='PHASE_NAME'){
								option=selectPhaseInfo();
							}
							//超差类型
							else if(id[i]=='VARIANCETYPE'){
								getvarianceTypeUrlSelect();
							}
							//域
							else if(id[i]=='DOMAIN'){
								option=selectContextInfo("");
							}
							//视图
							else if(id[i]=='VIEW'){
								option=selectViewInfo();
							}
							//来源
							else if(id[i]=='PARTSOURCE'){
								getsourceUrlSelect();
							}
							//默认单位
							else if(id[i]=='DEFAULTUNIT'){
								getquantityUnitUrlSelect();
							}
							//超差原因
							else if(id[i]=='VARIANCEREASON'){
								getreasonUrlSelect();
							}
							if($("select[name='cbDistributeStates']").length>0){
								plm.showMessage({title:'提示', message:"该搜索条件已存在!",icon:'3'});
								return;
							}else{
							$("#searchCondition").append("<tr><td><div id='"+id[i]+"' name='"+name[i]+"' optionType='"+type[i]+"'/><div style='float:left;' align='left' >"+name[i]+"</div></td><td><select style='float:left'><option  value='='>等于</option></select><SELECT name='cbDistributeStates' id='"+id[i]+"s' style='width:180px'>'"+option+"'</SELECT><img src='<%=request.getContextPath() %>/plm/images/common/delete.gif' onclick='delObjAttr(this)'/></td></tr>");
							}
						}
						else if(type[i]=="radio"){
							 $("#searchCondition").append("<tr><td><div id='"+id[i]+"' name='"+name[i]+"'  optionType='"+type[i]+"'/><div style='float:left;' align='left' >"+name[i]+"</div></td><td><select style='float:left'><option  value='='>等于</option></select><input type='radio' name="+id[i]+"s' value='1' checked/>是<input type='radio'name="+id[i]+"s' value='0'/>否<img src='<%=request.getContextPath() %>/plm/images/common/delete.gif' onclick='delObjAttr(this)'/></td></tr>");
						}
						else if(type[i]=='event'){
							if(id[i]=='FOLDER'){
								$("#searchCondition").append("<tr><td><div id='"+id[i]+"' name='"+name[i]+"'  optionType='"+type[i]+"'/><div style='float:left;' align='left' >"+name[i]+"</div></td><td><select style='float:left'><option  value='='>等于</option></select><input type='text' name='"+id[i]+"' id='"+id[i]+"s' class='pt-text' value='' style='float:left;margin-left:3px;width: 270px' readonly='readonly'/><a href='#' onclick='plm.selectFolder(treeType,context,\"onFolderSelected\")'>选择</a><img src='<%=request.getContextPath() %>/plm/images/common/delete.gif' onclick='delObjAttr(this)'/></td></tr>");
							}
							else if(id[i]=='CREATOR'){
								if($("#creatorName").length>0){
								plm.showMessage({title:'提示', message:"该搜索条件已存在!",icon:'3'});
								return;
								}else{
								$("#searchCondition").append("<tr><td><div id='"+id[i]+"' name='"+name[i]+"'  optionType='"+type[i]+"'/><div style='float:left;' align='left' >"+name[i]+"</div></td><td><select style='float:left'><option  value='='>等于</option></select><input type='hidden' name='creator' id='creator'><input type='text' name='creatorName' id='creatorName' class='pt-text' value='' style='float:left;margin-left:3px;'  readonly='readonly'/><a href='#' onclick='selectUser()'>选择</a><img src='<%=request.getContextPath() %>/plm/images/common/delete.gif' onclick='delObjAttr(this)'/></td></tr>");
								}
							}
						}
			   }
		} 
	}

	 function selectStateInfo(value){
	   var str=""
			$.ajax({
				url : "<%=request.getContextPath() %>/ddm/commonsearch/selectObjectInfo.jsp?op=selectState",
				type : "post",
				dataType : 'json',
				success : function(result) {
					for(var i=0;i<result.length;i++){
						if(value==result[i].value){
							str+="<option value=\""+result[i].value+"\" selected>"+result[i].text+"</option>";
						}
						else{
							str+="<option value=\""+result[i].value+"\">"+result[i].text+"</option>";
						}
					}
					var select = document.getElementById("STATENAME");
					select.length=0;
					$(select).attr("option",str);
					$(select).append(str);
				}
			});
  }

  //单击删除按钮移除搜索条件
	function delObjAttr(o){
		$(o).prev().parent().parent().remove();
	}


    var constant_="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    
	//删除条件
	function del(a){
		//删除父节点
		a.parentNode.removeNode(true);

		if($("#excludeOption").html()==constant_){
			$("#favoritedContext").attr("checked",false);
		}
	}

	function addDel(type){
		if($(type).children().next().attr("src")==undefined){
      		   $(type).append("<img src='<%=contextPath%>/plm/images/common/delete.gif'  onclick='del(this)'/>");
		}
	}
	//检查搜索条件是否重复，未重复则添加
	function checkIsExist(type,o){
		var result=true;
		if(type=="excludeOption"){
			$("#excludeOption").children().each(function(){
				var name=$(this).attr("name");
				if(name=="span"){
				 var id=$(this).attr("id");
				 if(o==id){
					result=false;
				 }
				}
			});
		}
		return result;
	}
	//搜索范围    选择选项
	function excludeOption(obj,a){
        var url = "<%=contextPath%>/ddm/commonsearch/excludeType.jsp?CALLBACK=afterexcludeType&excludeType="+a;

		//打开窗口
		plm.openWindow("<%=contextPath%>/plm/load.html", 500, 500,'T');
		
		//在前台中执行
		plm.openByPost(url, null, 'T');
	}
	function afterexcludeType(o){
		if (o != null) {
			var name=o.NAMES.split(",");
			var type=o.TYPES.split(",");
			for(var i=0;i<name.length;i++){
				if(checkIsExist("excludeOption",type[i])){
				$("#excludeOption").append("<span name='span' id='"+type[i]+"'  onmouseover='addDel(this)'><input type='checkbox' name='"+name[i]+"' id='"+name[i]+"' checked='checked'/>"+name[i]+"</span>");
			  }
			}
		}
		searchScopeClick2();
	}
	function searchScopeClick(){
		$("#favoritedContext").attr("checked",false);
	}
	function searchScopeClick2(){
		if($("#excludeOption").html()!=constant_){
			$("#favoritedContext").attr("checked",true);
			var searchScope=$("input[name='searchScope']:checked").attr("id");
			if(searchScope!=undefined){
				$("#"+searchScope).attr("checked",false);
			}
		}
		else{
			$("#favoritedContext").attr("checked",false);
			plm.showMessage({title:'提示', message:"请先添加收藏的上下文!", icon:'3'});
		}
	}
	//得到搜索范围
	function getSearchScope(){
		//包含的范围
		var isFirst=true;
		var containerScope="";
		var favoriteScope="";
		var scopeId=$("input[name='searchScope']:checked").attr("id");
		if(scopeId!=undefined){
			containerScope=scopeId;
		}
		if($("#favoritedContext").attr("checked")=="checked"){
			$("#excludeOption").children().each(function(){
				var check=$(this).children().attr("checked");
				var name=$(this).attr("name");
				if(name=="span" && check=="checked"){
				 var id=$(this).attr("id");
				 if(isFirst){
					 favoriteScope=id;
					 isFirst=false;
				 }
				 else{
					 favoriteScope=favoriteScope+","+id;
				 }
				}
			});
		}
		var searchScope={"containerScope":containerScope,"favoriteScope":favoriteScope};
		return pt.ui.JSON.encode(searchScope);
	}
  
	</script>
</body>
</html>