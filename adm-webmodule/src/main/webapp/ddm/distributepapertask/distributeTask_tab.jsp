<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>

<%
	String contextPath = request.getContextPath();
	String title = "加工单";
	String tabTitle1 = "待办任务";
	String tabTitle2 = "被退回任务";
	String tabTitle3 = "复制完成任务";
	String tabTitle4 = "重新打印";
	
	String tabStr1 = contextPath + "/ddm/distribute/distributePaperTaskHandle!getAllDistributePaperTask.action";
	String tabStr2 = contextPath + "/ddm/distribute/distributePaperTaskHandle!getAllDistributeReturnTask.action";
	String tabStr3 = contextPath + "/ddm/distribute/distributePaperTaskHandle!getAllDuplicateSubmitTask.action";
	//String tabStr4 = contextPath + "/ddm/distribute/distributePaperTaskHandle!getAllDistributePaperTask.action";
	//String tabStr4 = contextPath + "/ddm/distributepapertask/NewFile.jsp";
	//getSearchPrintProcessingDistributePaperTask
	String tabStr4 = contextPath + "/ddm/distribute/distributePaperTaskHandle!getSearchPrintProcessingDistributePaperTask.action";
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