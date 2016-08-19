<%@page import="com.cascc.platform.aa.api.util.AADomainUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.bjsasc.plm.core.session.SessionHelper"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page language="java"%>
<%@page session="true"%>
<%@ page import="java.util.Map"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@include file="/plm/plm.jsp" %>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.core.system.principal.User"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.core.context.ContextHelper"%>

<%
	request.setCharacterEncoding("UTF-8");
	String title = "外发单";
	String contextPath = request.getContextPath();
	String init = request.getParameter("init");
	Map<String,String> map = null;
	if ("true".equals(init)) {
		session.removeAttribute("DDM_DISTRIBUTE_INSIDE");
		
	} else {
		map = (Map<String,String>) session.getAttribute("DDM_DISTRIBUTE_INSIDE");
	}
	if (map == null) {
		map = new HashMap<String, String>();
		map.put("keywords", "");
		map.put("taskName", "");
		map.put("taskCode", "");
		map.put("creator", "");
		map.put("creatorName", "");
		map.put("queryCreateDateFrom", "");
		map.put("queryCreateDateTo", "");
		map.put("disInfo", "");
		map.put("disInfoName", "");
		map.put("receiver", "");
		map.put("flag", "1");
		
	}
	
	String qReceive_sel = "checked=\"true\"";
	String qReceive_cus = "";
	if ("2".equals(map.get("flag"))) {
		qReceive_sel = "";
		qReceive_cus = "checked=\"true\"";
	}
	
%>	
<html>
<head>
	<title><%=title%></title>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
	<script type="text/javascript" src="<%=contextPath%>/plm/ui/editor/date/WdatePicker.js"></script>
	<script type="text/javascript"	src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
<script type="text/javascript">

</script>
<style>
.noTable{border:0px; border-collapse:collapse; font-size:12px;}
.noTable td{border:0px; padding-left:0px; height:25px; line-height:25px;}
</style>		
</head>
<form name="searchForm" id="searchForm">
<table width="100%" height="100%" cellSpacing="0" cellPadding="0" border="0"><tr><td valign="top" height="5">
		<table width="100%" cellSpacing="0" cellPadding="0" border="0">
			<tr class="AvidmToolbar">
				<td nowrap="nowrap">
					<div class="AvidmMtop5 twoTitle">
						<a class="showIcon" href="#" onclick="divSelected(this);">
						<img src="<%=contextPath%>/plm/images/common/space.gif"></a>
						<%=title%>
					</div>
	</td></tr>
	<tr><td valign="top">
	<table width="100%" class="avidmTable" border="0" cellspacing="0" cellpadding="0">
		<%-- <TR>
			<td class="left_col AvidmW150"><span>*</span>关键字： </td>
			<td><input type="text" class="pt-textbox AvidmW270" name="keywords" id="keywords" value="<%=map.get("keywords")%>"/></td>
		</TR> --%>
		<TR>
			<TD class="left_col AvidmW150">任务名称：</TD>
			<TD><table class="noTable"><tr><td>
				<input type="text" name="taskName" id="taskName" class="pt-textbox AvidmW270" value="<%=map.get("taskName")%>" />	
				</td><td width="5"></td><td>
				<a href="#" onclick="clearValue('taskName','','');">
					<img src="<%=contextPath%>/plm/images/common/clear.gif">
				</a>
				</td></tr></table>
			</TD>
		</TR>
		<TR>
			<TD class="left_col AvidmW150">任务发起者：</TD>
			<TD><table class="noTable"><tr><td>
			<TD><input type='hidden' id="creator" name="creator" value="<%=map.get("creator")%>"/>
				<input type='text' id="creatorName" name="creatorName" class="pt-textbox AvidmW270"
					 value="<%=map.get("creatorName")%>" title="选择任务发起者" readonly="readonly" ontrigger="selectUser('creator')" />
				</td><td width="5"></td><td>
				<a href="#" onclick="clearValue('creatorName','creator','');">
					<img src="<%=contextPath%>/plm/images/common/clear.gif">
				</a>
				</td></tr></table>
			</TD>
		</TR>
		
		<TR>
			<TD class="left_col AvidmW150">任务型号：</TD>
			<%-- <TD><input type="text" name="taskCode" id="taskCode" class="pt-textbox AvidmW270" value="<%=map.get("taskCode")%>" /></TD> --%>
			<TD><select name="taskCode" id="taskCode">
							<option value="">所有型号</option>
							<%
							//从所有上下文中过滤出当前用户参加的上下文
							List<Context> contextList = Helper.getContextService().findAllContexts();
							User user = SessionHelper.getService().getUser();
							//List<Context> contexts= ContextHelper.getService().filterContextsByUserPrincipal(contextList, user);
							List<Context> contexts = ContextHelper.getService().getAllProducts();
							for(Context c:contexts){
							%>
							<option value="<%=c.getOid()%>"<%if((c.getOid()).equals(map.get("taskCode"))){ %>selected ="selected" <%} %>><%=c.getName()%></option>
							<%} %>
							 
					</select>
					</TD>
		</TR>
		<TR>
			<TD class="left_col AvidmW150"><span>*</span>接收单位/人员：</TD>
			<TD><table class="noTable"><tr><td>
				<input type="radio" name="receiver" id="receiver_unit" value="1" <%=qReceive_sel%> onclick="selectRadio(this)"/>单位&nbsp;&nbsp;
				<input type="radio" name="receiver" id="receiver_person" value="2" <%=qReceive_cus%> onclick="selectRadio(this)"/>人员&nbsp;&nbsp;
				<input type='text' id="disInfoName" name="disInfoName" class='pt-textbox AvidmW270' 
					 value="<%=map.get("disInfoName")%>" title="选择接收单位/人员" readonly="readonly" ontrigger="selectReceiver('disInfo')" />
				<input type='hidden' id="disInfo" name="disInfo" value="<%=map.get("disInfo")%>"/>
				<input type='hidden' id="infoClassId" name="infoClassId" value="<%=map.get("infoClassId")%>"/>
				<input type='hidden' id="flag" name="flag" value="<%=map.get("flag")%>"/>
				</td><td width="5"></td><td>
				<a href="#" onclick="clearValue('disInfoName','disInfo','infoClassId');">
					<img src="<%=contextPath%>/plm/images/common/clear.gif">
				</a>
				</td></tr></table>
			</TD>
		</TR>
		<TR>
			<TD class="left_col AvidmW150">创建时间：</TD>
			<TD>
				<table  class="tableBorderNone">
					<TR>
						<TD>&nbsp;自：</TD>
						<TD>
						<INPUT type='text' id="queryCreateDateFrom" name="queryCreateDateFrom" value="<%=map.get("queryCreateDateFrom")%>"
							class="Wdate pt-textbox" readonly="readonly"  style="width:100px;"
			        		onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'2001-01-01',maxDate:'2128-03-10'})" onchange="compareTime()"/>			
						</TD>
						<TD>&nbsp;到：</TD>
						<TD>
						<INPUT type='text' id="queryCreateDateTo" name="queryCreateDateTo" value="<%=map.get("queryCreateDateTo")%>"
							class="Wdate pt-textbox" readonly="readonly"  style="width:100px;"
			        		onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'2001-01-01',maxDate:'2128-03-10'})"  onchange="compareTime()"/>			
						</TD>
					</TR>
				</table></TD>
		</TR>
</table>
<table>
		<TR>
			<td class="exegesis"><span>*</span>为必选/必填项</td>
			<TD style="height:40px">&nbsp;&nbsp;&nbsp;&nbsp;
				<div class="pt-formbutton" text="搜索" id="submitbutton" onclick="searchAll();"></div>
				&nbsp;&nbsp;&nbsp;
				<div class="pt-formbutton" text="清空" id="submitbutton" onclick="consClear();"></div>
			</TD>
		</TR>
</table>
</TD></TR></table>
<input type='hidden' name="hidDistributeTypes" id="hidDistributeTypes"/>
<input type='hidden' name="hidDistributeStates" id="hidDistributeStates"/>
</form>
<script type="text/javascript">
	//延迟初始化页面
	setTimeout("initPage()",200);
	
	function initPage(){
		pt.ui.get("taskCode").setValue("<%=map.get("taskCode")%>");
	}
	
	function searchAll(){
		if($("#disInfoName").val()==""){
			plm. showMessage({title:'提示', message:"请先选择接收单位/人员!", icon:'3'});
			return false;
		}
		var url = "<%=contextPath%>/ddm/distribute/distributeSendOrderHandle!getAllDistributeOutsideTasks.action";
		$("#searchForm").attr("action", url);
		$("#searchForm").submit();
	}
	
	function selectRadio(obj){
		$("#disInfoName").val("");
		if(obj.value == "1"){
			$('[id="receiver_person"]').removeAttr("checked");
			$('[type="radio"][id="receiver_unit"]').attr("checked", true);
			$("#flag").val("1");
		}
		if(obj.value == "2"){
			$('[id="receiver_unit"]').removeAttr("checked");
			$('[type="radio"][id="receiver_person"]').attr("checked", true);
			$("#flag").val("2");
		}
	}
	
	function consClear() {
		try {
		$("#keywords").val("");
		$("#taskName").val("");
		$("#taskCode").val("");
		$("#creator").val("");
		$("#disInfoName").val("");
		$("#queryCreateDateFrom").val("");
		$("#queryCreateDateTo").val("");
		$("#creatorName").val("");
		$('[id="receiver_person"]').removeAttr("checked");
		$('[type="radio"][id="receiver_unit"]').attr("checked", true);
		$("#flag").val("1");
		} catch (e) {}
	}
	
	function clearValue(value1,value2,value3) {
		try {
		$("#"+value1).val("");
		$("#"+value2).val("");
		$("#"+value3).val("");
		} catch (e) {}
	}
	
	function selectReceiver(disInfoId){
		var flag=$("#flag").val();
		/* if($("#receiver").attr("checked")=="checked"){
			receiver = $("#receiver").val();
			$("#flag").val(receiver);
			alert(receiver);
		} */
		if(flag=="1"){
			selectOrg(disInfoId);
		}else if(flag=="2"){
			selectUser(disInfoId);
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
		if(retObj){
			$("#"+disInfoId).val(retObj.arrDivIID[0]);
			$("#"+disInfoId+"Name").val(retObj.arrDivName);
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
	</script>
</body>
</html>