<%@page import="com.cascc.platform.aa.api.util.AADomainUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.plm.core.session.SessionHelper"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page language="java"%>
<%@page session="true"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@include file="/plm/plm.jsp"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.core.system.principal.User"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.core.context.ContextHelper"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.grid.GridUtil"%>
<%@include file="/ddm/public/ddm.jsp" %>

<%
	String contextPath = request.getContextPath();
	request.setCharacterEncoding("UTF-8");
	String spot = "ListDistributeDestoryOrders";
	String gridId = "distributeDestoryOrderGrid";
	String destroyType = (String)request.getAttribute("destroyType");
	String init = (String)request.getAttribute("init");
	if ("true".equals(init)) {
		session.removeAttribute("DDM_DISTRIBUTE_INSIDE");
	}
	String title = "";
	if("0".equals(destroyType)){
		title="回收单";
	}else{
		title="销毁单";
	}
	List<Map<String, Object>> listInfoNamesOfOrg=DistributeHelper.getDistributeSendOrderService().getDisInfoNamesByDisIntfoTypeAndDestroyType("0",destroyType);
	List<Map<String, Object>> listInfoNamesOfUser=DistributeHelper.getDistributeSendOrderService().getDisInfoNamesByDisIntfoTypeAndDestroyType("1",destroyType);
	String toolbarId = "ddm.distributeinsideOrder.list.toolbar";
%>
<html>
<head>
<title><%=title%></title>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
<script type="text/javascript" src="<%=contextPath%>/plm/ui/editor/date/WdatePicker.js"></script>
<script type="text/javascript" src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/plm/javascript/messager.js"></script>
<style>
.noTable {
	border: 0px;
	border-collapse: collapse;
	font-size: 12px;
}

.noTable td {
	border: 0px;
	padding-left: 0px;
	height: 25px;
	line-height: 25px;
}
</style>
</head>
<body>
<form name="searchForm" id="searchForm">
	<table width="100%" height="100%" cellSpacing="0" cellPadding="0"
		border="0">
		<%-- <jsp:include page="/plm/common/grid/control/grid_of_title.jsp">
			<jsp:param name="gridTitle" value="<%=title%>"/>
		</jsp:include> --%>
		<tr><td valign="top">
			<table width="100%" height="100%" cellSpacing="0" cellPadding="0" border="0">
				<tr id="searchConditionTD" height="196">
					<td valign="top">
						<table width="100%" cellSpacing="0" cellPadding="0" border="0">
							<tr>
								<td valign="top">
									<table width="100%" class="avidmTable" border="0" cellspacing="0"
										cellpadding="0">
										<TR>
											<TD class="left_col AvidmW150">任务名称：</TD>
											<TD><table class="noTable">
													<tr>
														<td><input type="text" name="taskName" id="taskName"
															class="pt-textbox AvidmW270"/>
														</td>
														<td width="5"></td>
														<td><a href="#" onclick="clearValue('taskName','','');">
																<img src="<%=contextPath%>/plm/images/common/clear.gif">
														</a></td>
													</tr>
												</table>
											</TD>
										</TR>
										<TR>
											<TD class="left_col AvidmW150">分发数据：</TD>
											<TD><table class="noTable">
													<tr>
														<td><input type="text" name="disObj" id="disObj"
															class="pt-textbox AvidmW270"/>
														</td>
														<td width="5"></td>
														<td><a href="#" onclick="clearValue('disObj','','');">
																<img src="<%=contextPath%>/plm/images/common/clear.gif">
														</a></td>
													</tr>
												</table>
											</TD>
										</TR>
										<TR>
											<TD class="left_col AvidmW150">任务发起者：</TD>
											<TD><table class="noTable">
													<tr>
														<td>
														<TD><input type='hidden' id="creator" name="creator"/>
															<input type='text'id="creatorName" name="creatorName"
															class="pt-textbox AvidmW270"
															title="选择任务发起者"
															readonly="readonly" ontrigger="selectUser('creator')" /></td>
														<td width="5"></td>
														<td><a href="#"
															onclick="clearValue('creatorName','creator','');"> <img
																src="<%=contextPath%>/plm/images/common/clear.gif">
														</a></td>
													</tr>
												</table>
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
											<TD class="left_col AvidmW150"><span>*</span>接收单位/人员：</TD>
											<TD><table class="noTable">
													<tr>
														<td><input type="radio" name="receiver"
															id="receiver_unit" value="1" onclick="selectRadio(this)" checked="true"/>单位&nbsp;&nbsp; 
															<input type="radio" name="receiver" id="receiver_person" value="2" onclick="selectRadio(this)" />人员&nbsp;&nbsp;
														</td>
													</tr>
													<tr>
														<td>
															<select id="disInfoName" name="disInfoName" onchange='initInfoNames(this)' style="display:block;width:150px;">
															<option value="0"></option>
																<% 
																	for(Map<String,Object> map:listInfoNamesOfOrg){	
																%>
																<option value="<%=map.get("INFOCLASSID")%>:<%=map.get("DISINFOID")%>"><%=map.get("DISINFONAME")%></option>
																<%
																	}
																%>
																<option value="more" id="more">更多</option>
															</select>
															<select id="disInfoNameOfUser" name="disInfoNameOfUser" onchange='initInfoNames(this)' style="display:none;width:150px;">
															<option value="0"></option>
																<% 
																	for(Map<String,Object> map:listInfoNamesOfUser){
																%>
																<option value="<%=map.get("INFOCLASSID")%>:<%=map.get("DISINFOID")%>"><%=map.get("DISINFONAME")%></option>
																<%
																	}
																%>
																<option value="more" id="more">更多</option>
															</select>
															<input type='hidden' id="disInfo" name="disInfo" />
															<input type='hidden' id="infoClassId" name="infoClassId"/>
															<input type='hidden' id="flag" name="flag"/>
														</td>
														<td width="5"></td>
														<td></td>
													</tr>
												</table></TD>
										</TR>
										<TR>
											<TD class="left_col AvidmW150">创建时间：</TD>
											<TD>
												<table class="tableBorderNone">
													<TR>
														<TD>&nbsp;自：</TD>
														<TD><INPUT type='text' id="queryCreateDateFrom"
															name="queryCreateDateFrom" 
															class="Wdate pt-textbox" readonly="readonly"
															style="width: 100px;" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'2001-01-01',maxDate:'2128-03-10'})" 
															onchange="compareTime()" /></TD>
														<TD>&nbsp;到：</TD>
														<TD><INPUT type='text' id="queryCreateDateTo"
															name="queryCreateDateTo"
															
															class="Wdate pt-textbox" readonly="readonly"
															style="width: 100px;"
															onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'2001-01-01',maxDate:'2128-03-10'})"
															onchange="compareTime()" /></TD>
													</TR>
												</table>
											</TD>
										</TR>
										<TR>
											<td class="exegesis"><span>*</span>为必选/必填项</td>
											<TD style="height: 40px">
												<div class="pt-formbutton" text="搜索" id="submitbutton"
													onclick="searchAll();"></div> &nbsp;&nbsp;&nbsp;
												<div class="pt-formbutton" text="清空" id="submitbutton"
													onclick="consClear();"></div>
											</TD>
										</TR>
									</table>
								</TD>
							</TR>
						</table>
					</td>
				</tr>
				<TR>
					<TD class="splitButton" valign="top"> 
					<img onClick="setVisible(this,'searchConditionTD');resizeTable();" src="<%=request.getContextPath()%>/plm/images/common/splitTop.gif"/> 
					</TD>
				</TR>
				<TR>
					<TD valign="top">
						<table height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0">
							<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
							<jsp:param name="spot" value="<%=spot%>" />
							<jsp:param name="gridId" value="<%=gridId%>" />
							<jsp:param name="gridTitle" value="" />
							<jsp:param name="toolbar_modelId" value="<%=toolbarId%>" />
							<jsp:param name="onLoadSuccess" value="onLoadSuccess()"/>
							<jsp:param name="operate_container" value="container" />
							</jsp:include>
						</table>
					</TD>
				</TR>
			</table>
		</td></tr>
	</table>
</form>
<form id="download" name="download" method="POST"></form>
</body>
</html>
<script type="text/javascript">
	var container = {};

	function onLoadSuccess() {
		onLoadSuccess_ddm("<%=gridId%>");
	}
	
	function searchAll(){
		var taskName=$("#taskName").val();
		//分发数据名称或者编号
		var disObj=$("#disObj").val();
		var creator=$("#creator").val();
		var creatorName=$("#creatorName").val();
		var taskCode=getSearchScope();
		var receiver=$("#receiver").val();
		var queryCreateDateFrom=$("#queryCreateDateFrom").val();
		var queryCreateDateTo=$("#queryCreateDateTo").val();
		var disInfoName="";
		var oid=null;
		//单位
		if($("#disInfoName").is(":visible")==true){
			disInfoName=$("#disInfoName").find("option:selected").text();
			oid=$("#disInfoName").val();
			$("#flag").val("1");
			if(oid!="temp"){
				oid=$("#disInfoName").val();
			}else{
				oid="";
			}
		//人员
		}else{
			disInfoName=$("#disInfoNameOfUser").find("option:selected").text();
			oid=$("#disInfoNameOfUser").val();
			$("#flag").val("2");
			if(oid!="temp"){
				oid=$("#disInfoNameOfUser").val();
			}else{
				oid="";
			}
		}
		var disInfo="";
		var infoClassId="";
		var flag=$("#flag").val();
		if(oid==null||oid==""||oid=="temp"){
			infoClassId = $("#infoClassId").val();
			disInfo = $("#disInfo").val();
		}
		var destroyType=<%=destroyType%>;
		if(disInfoName==""){
			plm. showMessage({title:'提示', message:"请先选择接收单位/人员!", icon:'3'});
			return false;
		}
		var datas = "taskName=" + taskName
					+"&disObj=" + disObj
					+"&creator=" + creator
					+"&creatorName=" + creatorName
					+"&taskCode=" + taskCode
					+"&receiver=" + receiver
					+"&queryCreateDateFrom=" + queryCreateDateFrom
					+"&queryCreateDateTo=" + queryCreateDateTo
					+"&disInfoName=" + disInfoName
					+"&infoClassId=" + infoClassId
					+"&disInfo=" + disInfo
					+"&flag=" + flag
					+"&destroyType=" + destroyType
					+"&oid=" + oid;
		var url = "<%=contextPath%>/ddm/distribute/distributeDestroyOrderHandle!getAllDistributeDestoryTasks.action";
		plm.startWait();
		$.ajax({
			contentType:"application/x-www-form-urlencoded;charset=UTF-8",
			url:url,
			type:"post",
			dataType:"json",
			data:datas,
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

	function consClear() {
		try {
		$("#keywords").val("");
		$("#taskName").val("");
		$("#disObj").val("");
		//$("#taskCode").val("");
		$("#allContext").attr("checked",false);
		$("#favoritedContext").attr("checked",false);
		$("#excludeOption").html("");
		$("#creator").val("");
		
		$('[id="receiver_person"]').removeAttr("checked");
		$('[type="radio"][id="receiver_unit"]').attr("checked", true);
		$("#flag").val("1");
		$('#disInfoName').show();
		$('#disInfoName').val("0");
		$('#disInfoNameOfUser').hide();
		
		$("#queryCreateDateFrom").val("");
		$("#queryCreateDateTo").val("");
		$("#creatorName").val("");
		$('[id="receiver_person"]').removeAttr("checked");
		$('[type="radio"][id="receiver_unit"]').attr("checked", true);
		$("#flag").val("1");
		} catch (e) {}
	}

	
	function selectUser(disInfoId){
		var configObj = 
			{ 
				IsModal : "true",
				SelectMode : "single",
				returnType : "arrayObj",
				scope : "<%=AADomainUtil.VISTYPE_ALL%>"
			};
		var retObj = pt.sa.tools.selectUser(configObj);
		if(retObj){
			$("#" + disInfoId).val(retObj.arrUserIID[0]);
			$("#" + disInfoId + "Name").val(retObj.arrUserName);
	  	};
	}
	
	function selectOrg(disInfoId){
		var configObj = 
			{ 
				IsModal : "true",
				SelectMode : "single",
				returnType : "arrayObj",
				scope : "<%=AADomainUtil.VISTYPE_ALL%>"
			};
		var retObj = pt.sa.tools.selectOrg(configObj);
		if (retObj) {
			var optiontext=null;
			$("#disInfoName option[value='temp']").remove();
			$("#disInfoName option").each(function(){
				if($(this).text()==retObj.arrDivName[0]){
					$(this).attr("selected","selected");
					optiontext=retObj.arrDivName[0];
					return;
				}
			});
			if(optiontext==null){
				var option=$("<option>").val("temp").text(retObj.arrDivName[0]);
				$("#disInfoName option[value='0']").after(option);
				$("#disInfoName").find("option:contains("+retObj.arrDivName[0]+")").attr("selected","selected");
				$("#" + disInfoId).val(retObj.arrDivIID[0]);
			}
		};
	}

	
	function compareTime(){
		var sttime = $("#queryCreateDateFrom").val();
		var edtime = $("#queryCreateDateTo").val();
		
		if(sttime!=""){
			  if(edtime!="" && sttime>edtime ){
			    alert("开始时间应小于结束时间！");
			    document.getElementById("enddate").value = "";
			    return;
			  }
		}
		if(edtime!=""){
			if(sttime!="" && sttime>edtime){
			  alert("请选择开始时间！");
			  document.getElementById("startdate").value = "";
			  return;
			}
		}
	}
	
	// 子页面打印方法的调用
	function reloadOrder(oids,nums,destroyType){
		var table = pt.ui.get("<%=gridId%>");
		table.reload();
		var url = '<%=contextPath %>/ddm/distribute/distributeDestroyOrderHandle!paperTaskExcel.action?<%=KeyS.OIDS%>=' + oids + '&destroyNums='+nums+ '&destroyType='+destroyType;
		$("#download").attr("action", url);
		$("#download").submit();
	}

	// 分割线
	function resizeTable(){
		var grid = pt.ui.get("<%=gridId%>"); 
		grid.set({width :$(grid.renderTo).width(),height :$(grid.renderTo).height()-22});
		grid.pagingbar.set( {width :$(grid.renderTo).width(),height :22});
	};

	function doCustomizeMethod_operate(value) {
		var url = '<%=contextPath%>/ddm/distributesendorder/listDistributeDestroyDetail.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OIDS%>=' + value + '&destroyType=<%=destroyType%>';
		ddm.tools.openWindow(url,900,600,"distributeOrderOpen");
	}
	
	function dataPrint(){
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if (data == null || data.length == 0) {
			plm. showMessage({title:'提示', message:"请至少选择1个操作对象!", icon:'3'});
			return;
		}
		var oids = "";
		for(var i=0;i<data.length;i++){
			oids += data[i].OID + ",";
		}
		oids = oids.substring(0, oids.length - 1);
		var url = '<%=contextPath%>/ddm/distributesendorder/listDistributeDestroyDetails.jsp?<%=KeyS.OIDS%>=' + oids+ '&destroyType=<%=destroyType%>';
		ddm.tools.openWindow(url,900,600,"distributeOrderOpen");
	}

	// 返回检索条件画面。
	function returnSearchPage() {
		window.location.href = "<%=contextPath%>/ddm/distributesendorder/listDistributeDestorySearch.jsp";
	}

	//修改项目操作
	function updateDistributePaperTask(){
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if (data == null || data.length != 1) {
			plm. showMessage({title:'提示', message:"请至少选择1个操作对象!", icon:'3'});
			return;
		}
		
		var oid = data[0].OID;
		var url = "<%=contextPath%>/ddm/distributepapertask/updateDistributePaperTask.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=" + oid;
		plm.openWindow(url,800,600);	
	}

	//刷新dategrid
	function tableReload(){
		//plm.showMessage({title:'系统提示', message:text, icon:'2'});
		//window.location.reload(true);
	}
	
	// 重新加载
	function reload(text){
		if (text != undefined && text != "") {
			plm.showMessage({title:'系统提示', message:text, icon:'2'});
		}

		var table = pt.ui.get("<%=gridId%>");
		table.reload();
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
	//搜索范围    选择对象
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
	//kangyanfei 2014-07-09	
	function initInfoNames(object){	
		var disInfo='disInfo';
		var selectClassId = object.options[object.selectedIndex].value;
		if(selectClassId == "more"){
			selectReceiver(disInfo);	
			return;
		}else{
			$("#disInfoNameOfUser option[value='temp']").remove();
			$("#disInfoName option[value='temp']").remove();
		}
	}
	
	function selectReceiver(disInfoId){
		var flag=$("input[name='receiver'][checked]").val();
		if(flag=="1"){
			selectOrg(disInfoId);
		}else if(flag=="2"){
			selectUser(disInfoId);
		}
	}
	
	function selectRadio(obj){
		$("#disInfoName").val("");
		if(obj.value == "1"){
			$('[id="receiver_person"]').removeAttr("checked");
			$('[type="radio"][id="receiver_unit"]').attr("checked", true);
			$("#flag").val("1");
			$('#disInfoName').show();
			$('#disInfoName').val("0");
			$('#disInfoNameOfUser').hide();
		}
		if(obj.value == "2"){
			$('[id="receiver_unit"]').removeAttr("checked");
			$('[type="radio"][id="receiver_person"]').attr("checked", true);
			$("#flag").val("2");
			$('#disInfoName').hide();
			$('#disInfoNameOfUser').show();
			$('#disInfoNameOfUser').val("0");
		}
	}

	function selectUser(disInfoId){
		var configObj = 
			{ 
				IsModal : "true",
				SelectMode : "single",
				returnType : "arrayObj",
				scope : "<%=AADomainUtil.VISTYPE_ALL%>"
			};
		var retObj = pt.sa.tools.selectUser(configObj);
		if(retObj){
			var optiontext=null;
			$("#disInfoNameOfUser option[value='temp']").remove();
			$("#disInfoNameOfUser option").each(function(){
				if($(this).text()==retObj.arrUserName[0]){
					$(this).attr("selected","selected");
					optiontext=retObj.arrUserName[0];
					return;
				}
			});
			if(optiontext==null){
				var option=$("<option>").val("temp").text(retObj.arrUserName[0]);
				$("#disInfoNameOfUser option[value='0']").after(option);
				$("#disInfoNameOfUser").find("option:contains("+retObj.arrUserName[0]+")").attr("selected","selected");
				$("#" + disInfoId).val(retObj.arrUserIID[0]);
			};
			if(disInfoId=="creator"){
				$("#" + disInfoId).val(retObj.arrUserIID[0]);
				$("#" + disInfoId+"Name").val(retObj.arrUserName[0]);
			}
	  	};
	}
	function clearId(idValue){
		$("#" +idValue).val("");
	}
	
	function clearValue(value1,value2,value3) {
		try {
		$("#"+value1).val("");
		$("#"+value2).val("");
		$("#"+value3).val("");
		} catch (e) {}
	}
	
	function selectOrg(disInfoId){
		var configObj = 
			{ 
				IsModal : "true",
				SelectMode : "single",
				returnType : "arrayObj",
				scope : "<%=AADomainUtil.VISTYPE_ALL%>"
			};
		var retObj = pt.sa.tools.selectOrg(configObj);
		if (retObj) {
			var optiontext=null;
			$("#disInfoName option[value='temp']").remove();
			$("#disInfoName option").each(function(){
				if($(this).text()==retObj.arrDivName[0]){
					$(this).attr("selected","selected");
					optiontext=retObj.arrDivName[0];
					return;
				}
			});
			if(optiontext==null){
				var option=$("<option>").val("temp").text(retObj.arrDivName[0]);
				$("#disInfoName option[value='0']").after(option);
				$("#disInfoName").find("option:contains("+retObj.arrDivName[0]+")").attr("selected","selected");
				$("#" + disInfoId).val(retObj.arrDivIID[0]);
			}
		};
	}

</script>