<%@page import="com.bjsasc.adm.active.model.query.ActiveQuerySchema"%>
<%@page import="com.bjsasc.adm.active.helper.AdmHelper"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.util.JsonUtil"%>
<%@page import="com.bjsasc.plm.util.Ajax"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page language="java" %>

<%	
	request.setCharacterEncoding("UTF-8");
    String method=request.getParameter("op");
    String activename=request.getParameter("ACTIVENAME");
    String begintime=request.getParameter("BEGINTIME");
    String endtime=request.getParameter("ENDTIME");
    String activeModelId=request.getParameter("ACTIVEMODELID");
    String activeModelName=request.getParameter("activeModelName");
    String showdate=request.getParameter("SHOWDATE");
	String schemaName =request.getParameter("TITLE");
	String Oid=request.getParameter("OID");
	int defaultSchema =Integer.parseInt(request.getParameter("IS_DEFAULT"));
	ActiveQuerySchema querySchema=null;
	if(method.equals("update")){
		querySchema=AdmHelper.getActiveChangeService().modifyQuerySchema(Oid,activename,schemaName,defaultSchema,begintime,endtime,activeModelId,activeModelName,showdate);
	}else if(method.equals("add")){
		querySchema=AdmHelper.getActiveChangeService().createQuerySchema(activename,schemaName,defaultSchema,begintime,endtime,activeModelId,activeModelName,showdate);
	}
	
	//封装刚创建对象成json格式
	Map<String, Object> mapObject = Helper.getTypeManager().getAttrValues(querySchema);
	
	String jsonObject = DataUtil.mapToJson(mapObject);

	out.write(jsonObject);
%>
