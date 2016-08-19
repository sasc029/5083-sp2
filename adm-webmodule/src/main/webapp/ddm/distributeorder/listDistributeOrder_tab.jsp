<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>

<%
	String contextPath = request.getContextPath();
	//String oid = RequestUtil.getParamOid(request);
	//session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OID, oid);
	
	String title = "发放单";
	String tabTitle1 = "待办任务";
	String tabTitle2 = "被退回任务";

	String tabStr1 = contextPath + "/ddm/distribute/distributeOrderHandle!getAllDistributeOrder.action";
	String tabStr2 = contextPath + "/ddm/distribute/distributeOrderHandle!getAllDistributeOrderReturn.action";
%>
	
<html>
	<head>
		<title><%=title%></title>
		<jsp:include page="/platform/ui/Ui-js.jsp"></jsp:include>
	</head>
	<body scroll="no" class="body">
		<div ui="tabs" id="RunningTask" class="pt-tabs" shareIframe="false" fit="true">
			<div id="disTab1" title="<%=tabTitle1%>" src="<%=tabStr1%>" style="padding:0px;" cache="false"></div>
			<div id="disTab2" title="<%=tabTitle2%>" src="<%=tabStr2%>" style="padding:0px;overflow: hidden;" cache="false"></div>
		</div>
	</body>
</html>