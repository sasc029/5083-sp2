<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>

<%
	String contextPath = request.getContextPath();
	String oid = RequestUtil.getParamOid(request);
	session.setAttribute(ConstUtil.DISTRIBUTE_PAPERTASK_OID, oid);
	String title = "复制加工";
	String tabTitle1 = "未签收任务";
	String tabTitle2 = "已签收任务";
	String tabTitle3 = "被退回任务";

	String tabStr1 = contextPath + "/ddm/distribute/duplicateProcessHandle!getAllDuplicateNoReceiveTask.action?flag=noReceive";
	String tabStr2 = contextPath + "/ddm/distribute/duplicateProcessHandle!getAllDuplicateReceiveTask.action?flag=receive";
	String tabStr3 = contextPath + "/ddm/distribute/duplicateProcessHandle!getAllDistributeTaskRollBack.action";
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
		</div>
	</body>
</html>