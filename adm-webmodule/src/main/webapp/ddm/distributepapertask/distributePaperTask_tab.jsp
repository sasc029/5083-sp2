<%@page import="com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>

<%
	String contextPath = request.getContextPath();
	String oid = RequestUtil.getParamOid(request);
    session.setAttribute(ConstUtil.DISTRIBUTE_PAPERTASK_OID, oid);
    DistributePaperTaskService service = DistributeHelper.getDistributePaperTaskService();
	DistributePaperTask dis = service.getDistributePaperTaskProperty(oid);	
	String title = "纸质任务";
	String tabTitle1 = "属性信息";
	String tabTitle2 = "分发数据";
	String  tabStr2="";
	String tabStr1 = contextPath + "/ddm/distributepapertask/distributePaperTaskProperty.jsp";
	tabStr2 = contextPath + "/ddm/distribute/distributeSendOrderHandle!getDistributePaperTaskList.action";
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