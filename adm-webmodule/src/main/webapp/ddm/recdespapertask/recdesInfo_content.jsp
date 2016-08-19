<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>
<%@page import="com.bjsasc.plm.core.util.DateTimeUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo"%>
<%@page import="com.bjsasc.ddm.distribute.model.recdesinfo.RecDesInfo"%>
<%@page import="com.bjsasc.plm.KeyS"%>

<%@page contentType = "text/html; charset=UTF-8" pageEncoding="GBK"%>
<% 
	String contextPath = request.getContextPath();
	String classId = RecDesInfo.CLASSID;
	String spot = ConstUtil.SPOT_LISTRECDESINFO;
	String gridId = "distributeInfoGrid";
	String gridTitle = "回收销毁信息";
	String toolbarId = "";
	
	String commonInfoTitle = "公共信息";
	  
	String disUrgent = (String)request.getAttribute("disUrgent");
	String deadDate = (String)request.getAttribute("deadLineDate");
	//String disStyle = (String)request.getAttribute("disStyle");
	if(deadDate == null){
		deadDate = DateTimeUtil.getCurrentDate(ConstUtil.C_DISDEADLINE_DELAY_DAY);
		//deadDate = "";
	}
	String qReceive_sel = "checked=\"true\" disabled=\"true\"";
	String qReceive_cus = "disabled=\"true\"";
	if ("1".equals(disUrgent)) {
		qReceive_sel = "disabled=\"true\"";
		qReceive_cus = "checked=\"true\" disabled=\"true\"";
	}
	//String disStyle_0 = "checked=\"true\" disabled=\"true\"";
	//String disStyle_1 = "disabled=\"true\"";
	/*
	if ("1".equals(disStyle)) {
		disStyle_0 = "disabled=\"true\"";
		disStyle_1 = "checked=\"true\" disabled=\"true\"";
	}*/
	
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
			        		class="Wdate pt-textbox"  readonly="readonly"  style="width:100px;" <%=ValidateHelper.buildValidator()%>/>&nbsp;
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
			</jsp:include>
		</table>
<!-- 分发数据 结束 -->	
</td></tr></table>
<input type="hidden" name="distributeObjectOids" id="distributeObjectOids"/>
<input type="hidden" name="distributeInfoOids" id="distributeInfoOids"/>
<input type="hidden" name="distributeOrderOids" id="distributeOrderOids"/>
<input type="hidden" name="distributeOrderObjectLinkOids" id="distributeOrderObjectLinkOids"/>
<input type="hidden" name="type" id="type"/>
<input type="hidden" name="iids" id="iids"/>
<input type="hidden" name="disMediaTypes" id="disMediaTypes"/>
<input type="hidden" name="disInfoNums" id="disInfoNums"/>
<input type="hidden" name="notes" id="notes"/>
<input type="hidden" name="disType" id="disType"/>
</form>
</body>
</html>
<script>
var container={};
// 重新加载
function reload(){
	window.location.href = "<%=contextPath%>/ddm/distribute/distributeInfoHandle!getRecDesInfosByPaperOids.action";
	/*
	var table = pt.ui.get("<%=gridId%>");
	table.reload();
	*/
}
</script>