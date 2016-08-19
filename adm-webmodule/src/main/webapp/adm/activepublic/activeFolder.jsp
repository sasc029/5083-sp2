<%@page import="com.bjsasc.adm.common.ActiveInitParameter"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%
String activeCabinetOid = ActiveInitParameter.getActiveCabinetOid();
if (activeCabinetOid != null) {
	response.sendRedirect(request.getContextPath() + "/plm/common/visit.jsp?OID=" + activeCabinetOid);
}
%>