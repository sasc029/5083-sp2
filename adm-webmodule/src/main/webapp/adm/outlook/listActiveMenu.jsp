<%@page import="com.bjsasc.plm.ui.tree.TreeHelper"%>
<%@page import="com.bjsasc.plm.ui.tree.TreeNode"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.core.context.model.RootContext"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.plm.core.Helper"%>
<%@page import="com.bjsasc.plm.core.context.model.ProductContext"%>
<%@page import="com.bjsasc.plm.ui.outlook.OutlookHelper"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%
	Context rootContext = Helper.getContextService().getRootContext();

	List<Map<String, Object>> list = TreeHelper.toMaps(OutlookHelper.buildGlobalMenus("com.bjsasc.adm.active"));
	
	String result = DataUtil.encode(list);
	out.print(result);
%>