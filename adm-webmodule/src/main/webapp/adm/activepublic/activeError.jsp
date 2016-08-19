<%@page language="java" %>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.core.util.PlmException"%>
<%@page errorPage="/plm/error.jsp"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%
	Object ex = request.getAttribute("javax_servlet_error_exception");
	
	if (ex instanceof Exception) {
		throw new Exception((Exception)ex);
	}
%>