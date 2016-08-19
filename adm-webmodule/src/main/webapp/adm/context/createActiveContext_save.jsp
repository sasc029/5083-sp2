<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.plm.core.favorite.FavoriteHelper"%>
<%@page import="com.bjsasc.plm.core.context.team.TeamHelper"%>
<%@page import="com.bjsasc.adm.active.model.activecontext.ActiveContext"%>
<%@page import="com.bjsasc.plm.core.context.model.ProductContext"%>
<%@page import="com.bjsasc.adm.active.helper.AdmHelper"%>
<%@page import="com.bjsasc.plm.core.context.ContextHelper"%>
<%@page import="com.bjsasc.plm.core.context.team.Team"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.plm.util.Ajax"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page language="java" %>
<%@page session ="true" %>
<%@page errorPage="/plm/ajaxError.jsp"%>

<%

	Map<String, String> map = new HashMap<String, String>();
	String contextNumber = request.getParameter("NUMBER");
	String contextName = request.getParameter("NAME");
	String contextType = request.getParameter("contextType");
	String contextNote = request.getParameter("NOTE");
	String parentContextOid = request.getParameter("parentContextOid");
	String teamOid = request.getParameter("teamOid");
	String isPrivate = request.getParameter("isPrivate");
	String extended = request.getParameter("extended");
	if(extended==null){
		extended = "true";
	}
	boolean flag = false;
	if("isPrivate".equals(isPrivate)){
		flag = true;
	}
	Context parentContext = ContextHelper.getService().getContext(parentContextOid);
	Team team = null;
	if(!"".equals(teamOid) && teamOid != null){
		team = TeamHelper.getService().getTeam(teamOid);
	}
	ActiveContext activeContext = AdmHelper.getActiveContextService().newActiveContext();
	activeContext.setNumber(contextNumber);
	activeContext.setName(contextName);
	activeContext.setNote(contextNote);
	//ActiveContext currentContext = (ActiveContext)ContextHelper.getService().createAppContext(activeContext,parentContext, team,new Boolean(extended), flag);
	ActiveContext currentContext = (ActiveContext)AdmHelper.getActiveContextService().createAppContext(activeContext,parentContext, team,new Boolean(extended), flag);

/*
ProductContext productContext = ContextHelper.getService().newProductContext();
productContext.setNumber(contextNumber);
productContext.setName(contextName);
productContext.setNote(contextNote);
ProductContext currentContext = (ProductContext)ContextHelper.getService().createAppContext(productContext,parentContext, team,new Boolean(extended), flag);
*/
	//end add by tianli
	Map<String, Object> mapObject = Helper.getTypeManager().getAttrValues(currentContext);
	String jsonObject = DataUtil.mapToSimpleJson(mapObject);
	Map<String, Object> result = new HashMap<String, Object>();
	result.put(Ajax.SUCCESS, jsonObject);
	String json = DataUtil.mapToSimpleJson(result);
	out.write(json);

%>