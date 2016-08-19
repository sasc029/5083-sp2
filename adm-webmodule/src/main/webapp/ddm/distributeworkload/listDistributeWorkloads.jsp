<%@page import="java.util.*"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.cascc.avidm.util.SplitString"%>
<%@page import="com.bjsasc.plm.type.restrict.select.Select"%>
<%@page import="com.bjsasc.plm.type.restrict.select.SelectOption"%>

<%@page import="com.bjsasc.plm.type.TypeService"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.context.ContextManager"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="com.bjsasc.plm.navigator.NavigatorUtil"%>
<%@page import="com.cascc.platform.aa.api.util.AADomainUtil"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/ddm/public/ddm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String spot = "ListDistributeWorkloads";
	String gridId = "distributeWorkloadGrid";
	String gridTitle = "工作量统计";
	String commonTitle = "查询条件";
	
	String searchUrl = contextPath + "/ddm/distribute/distributeWorkloadHandle!getDistributeWorkload.action";
	
	String rdoDistributeState_all = "checked=\"true\"";
	String rdoDistributeState_cus = "";
	String lifeCycleType = "0";
	String init = (String)request.getAttribute("init");
	if ("true".equals(init)) {
		session.removeAttribute("DDM_DISTRIBUTE_INSIDE");
	}
%>
<html>
	<head>
		<title><%=gridTitle%></title>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
		<script type="text/javascript" src="<%=contextPath%>/plm/ui/editor/date/WdatePicker.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/ddm/javascript/ddmutil.js"></script>
		<script type="text/javascript"	src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
			<script type="text/javascript" src="<%=request.getContextPath()%>/plm/javascript/messager.js"></script>
		<style>
		.noTable{border:0px; border-collapse:collapse; font-size:12px;}
		.noTable td{border:0px; padding-left:0px; height:25px; line-height:25px;}
		</style>
	</head>
	<body>
	<form id="mainForm" name="mainForm" method="POST">
		<table height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0">
			<%-- <jsp:include page="/plm/common/grid/control/grid_of_title.jsp">
				<jsp:param name="gridTitle" value="<%=commonTitle%>"/>
			</jsp:include> --%>	
			<tr id="searchConditionTD"><td height="124" valign="top">
				<table width="100%" class="avidmTable" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="left_col AvidmW150">操作人：</td>
					<td>
						<table class="noTable">
						<tr>
							<td>
								<input type='hidden' id="creator" name="creator" value=""/>
								<input type='text' id="creatorName" name="creatorName" class='pt-textbox AvidmW270' value="" title="选择操作人" readonly="readonly" ontrigger="selectUser('creator')" />
							</td>
							<td>
								<a href="#" onclick="clearValue('creatorName,creator');">
								<img src="<%=contextPath%>/plm/images/common/clear.gif"></a>
							</td>
						</tr>
						</table>
					</td>
				</tr>
	
				<tr>
					<td class="left_col AvidmW150"><span>*</span>状态：</td>
					<td>
						<SELECT id="lifeCycleType" name='lifeCycleType' class='pt-select AvidmW120'  url="<%=contextPath%>/plm/common/select/getSelect.jsp?select=lifeCycleType" >
						</SELECT>
					</td>
				</tr> 		
				<TR>
					<TD class="left_col AvidmW150">操作时间：</TD>
					<TD>
						<table  class="tableBorderNone">
							<TR>
								<TD><input type="radio" name="qCreateDate" id="qCreateDate_sel" value="past" checked="true" />
									<select name="createPastDays" id="createPastDays" class='pt-select AvidmW100' url="<%=contextPath%>/plm/common/select/getSelect.jsp?select=pastDays">
									</select>
								</TD>
								<TD>&nbsp;<input type="radio" name="qCreateDate" value="select"/></TD>
								<TD>&nbsp;自：</TD>
								<TD>
								<INPUT type='text' id="queryCreateDateFrom" name="queryCreateDateFrom" value=""
									class="Wdate pt-textbox" readonly="readonly"  style="width:100px;"
					        		onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'2001-01-01',maxDate:'2128-03-10'})"/>			
								</TD>
								<TD>&nbsp;到：</TD>
								<TD>
								<INPUT type='text' id="queryCreateDateTo" name="queryCreateDateTo" value=""
									class="Wdate pt-textbox" readonly="readonly"  style="width:100px;"
					        		onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'2001-01-01',maxDate:'2128-03-10'})"/>			
								</TD>
							</TR>
						</table>
					</TD>
				</TR>
				<TR>
					<td class="exegesis"><span>*</span>为必选/必填项</td>
					<TD style="height: 40px">
						<div class="pt-formbutton" text="搜索" id="submitbutton" onclick="searchAll();"></div>
						&nbsp;&nbsp;&nbsp;
						<div class="pt-formbutton" text="清空" id="submitbutton" onclick="consClear();"></div>
					</TD>
				</TR>
			</table></td></tr>
				<tr>
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
							<jsp:param name="onLoadSuccess" value="onLoadSuccess()"/>
							<jsp:param name="operate_container" value="container"/>
						</jsp:include>
					</table>
				</td></tr>
			</table>
		</form>
	</body>
</html>
<script type="text/javascript">
	var container = {};
	
	function onLoadSuccess() {
		onLoadSuccess_ddm("<%=gridId%>");
	}
	
	function checkStateCus() {
		$("#rdoDistributeState_cus").attr("checked", true);
	}
			
	function clearValue(fields) {
		var arrField = fields.split(",");
        $.each(arrField,function(n,value) {  
			eval("$('#"+value+"').val('');");
        });
	}	
	
	function displayDistributeState(obj) {
		if (obj.value == "ALL") {
			$('[name="cbDistributeStates"]').removeAttr("checked");
			//$('[name="cbDistributeStates"]').attr("disabled", true);
			//$("#divDistributeState").css("display","none");
		} else {
			//$('[name="cbDistributeStates"]').removeAttr("disabled");
			//$("#divDistributeState").css("display","block");
		}
	}	
	
	function consClear () {
		$("#creator").val("");
		$("#creatorName").val("");
		$("#queryCreateDateFrom").val("");
		$("#queryCreateDateTo").val("");
		$("#qCreateDate_sel").attr("checked", true);
		$("#rdoDistributeState_all").attr("checked", true);
		$('[name="cbDistributeStates"]').removeAttr("checked");
		pt.ui.get("createPastDays").setValue("0");
		pt.ui.get("lifeCycleType").setValue("0");
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
			$("#" + inputId).val(retObj.arrUserIID[0]);
			$("#" + inputId + "Name").val(retObj.arrUserName);
	  	};
	}
	
	

	function searchAll() {
		var creator = $("#creator").val();
		var createPastDays = $("#createPastDays").val();
		var queryCreateDateFrom = $("#queryCreateDateFrom").val();
		var queryCreateDateTo = $("#queryCreateDateTo").val();
		var lifeCycleType = $("#lifeCycleType").val();
		// 状态
		/* var rdoDistributeState = $('input[name="rdoDistributeState"]:checked').val();
		var cbDistributeStates = "";
		$('[name="cbDistributeStates"]:checked').each(function(){
			cbDistributeStates += $(this).val() + ",";
		}) */
		// 创建时间
		var qCreateDate = $('input[name="qCreateDate"]:checked').val();
		var datas = "creator=" + creator
				 /* + "&rdoDistributeState=" + rdoDistributeState
				 + "&cbDistributeStates=" + cbDistributeStates */
				 + "&qCreateDate=" + qCreateDate
				 + "&createPastDays=" + createPastDays
				 + "&queryCreateDateFrom=" + queryCreateDateFrom
				 + "&queryCreateDateTo=" + queryCreateDateTo
				 + "&lifeCycleType=" + lifeCycleType;
		plm.startWait();
		$.ajax({
			url:"<%=searchUrl%>",
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
	
	// 分割线
	function resizeTable(){
		var grid = pt.ui.get("<%=gridId%>"); 
		grid.set({width :$(grid.renderTo).width(),height :$(grid.renderTo).height()-22});
		grid.pagingbar.set( {width :$(grid.renderTo).width(),height :22});
	};
</script>

