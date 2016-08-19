<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>

<%
	String contextPath = request.getContextPath();
	String title = "电子任务纸质签收任务";
	String tabTitle1 = "未签收任务";
	String tabTitle2 = "已签收任务";
	String tabTitle3 = "已完成任务";
	String tabTitle4 = "已拒绝任务";

	String tabStr1 = contextPath + "/ddm/distribute/distributeTaskHandle!getAllDistributeElecTaskNotSigned.action";
	String tabStr2 = contextPath + "/ddm/distribute/distributeTaskHandle!getAllDistributeElecTaskSigned.action";
	String tabStr3 = contextPath + "/ddm/distribute/distributeTaskHandle!getAllDistributeElecTaskCompleted.action";
	String tabStr4 = contextPath + "/ddm/distribute/distributeTaskHandle!getAllDistributeElecTaskRefuseSigned.action";
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
			<div id="disTab3" title="<%=tabTitle3%>" src="<%=tabStr3%>" style="padding:0px;overflow: hidden;" cache="false"></div>
			<div id="disTab4" title="<%=tabTitle4%>" src="<%=tabStr4%>" style="padding:0px;overflow: hidden;" cache="false"></div>
		</div>
	</body>
</html>