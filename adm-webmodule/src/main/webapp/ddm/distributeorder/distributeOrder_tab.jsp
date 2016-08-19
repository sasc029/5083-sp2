<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>

<%
	String contextPath = request.getContextPath();
	String oid = RequestUtil.getParamOid(request);
	session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OID, oid);
	
	String title = "发放单";
	String tabTitle1 = "属性信息";
	String tabTitle2 = "分发数据";
	String tabTitle3 = "分发信息";
	String tabTitle4 = "分发任务";

	String tabStr1 = contextPath + "/ddm/distributeorder/distributeOrderProperty.jsp?oid=" + oid;
	String tabStr2 = contextPath + "/ddm/distribute/distributeObjectHandle!getAllDistributeObject.action?oid=" + oid;
	String tabStr3 = contextPath + "/ddm/distributeinfo/distributeInfo_frame.jsp?oid=" + oid;
	String tabStr4 = contextPath + "/ddm/distribute/distributeTaskHandle!getAllDistributeTask.action";
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