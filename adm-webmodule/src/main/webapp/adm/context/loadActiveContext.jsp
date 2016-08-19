<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.adm.common.ActiveInitParameter"%>
<%@include file="/plm/plm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String op = request.getParameter("op");
	String tabId = request.getParameter("TABID");
	String contextOid = ActiveInitParameter.getActiveContext().getOid();
	String params = "";
	String url = "";
	String tp = "&appIID=undefined&appID=undefined&moduleID=undefined&moduleIID=undefined&currentNodeIID=undefined&displayType=grid";
	if ("context".equals(op)) {
		params = "?OID=" + contextOid;
		url = contextPath + "/plm/common/visit/VisitObject.jsp" + params;
	} else if ("folder".equals(op)) {
		String cabinetOid = ActiveInitParameter.getActiveCabinetOid();
		params = "?OID=" + cabinetOid;
		url = contextPath + "/plm/common/visit/VisitObject.jsp" + params;
	} else if ("detail".equals(op)) {
		params = "?OID=" + contextOid + "&TABID=com.bjsasc.adm.context.detail" + tp;
		url = contextPath + "/plm/common/visit/VisitObject.jsp" + params;
	} else if ("template".equals(op)) {
		params = "?OID=" + contextOid + "&TABID=com.bjsasc.adm.context.template" + tp;
		url = contextPath + "/plm/common/visit/VisitObject.jsp" + params;
	} else if ("team".equals(op)) {
		params = "?OID=" + contextOid + "&TABID=com.bjsasc.adm.context.team" + tp;
		url = contextPath + "/plm/context/team/listTeamMember.jsp" + params;
	} else if ("tool".equals(op)) {
		params = "?OID=" + contextOid + "&TABID=com.bjsasc.adm.context.tool" + tp;
		url = contextPath + "/plm/context/managertoolframe.jsp" + params;
	}
	response.sendRedirect(url);
%>
