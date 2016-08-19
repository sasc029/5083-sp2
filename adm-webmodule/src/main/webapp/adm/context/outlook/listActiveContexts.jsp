<%@page import="com.bjsasc.adm.active.model.activecontext.ActiveContext"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.context.ContextManager"%>
<%@page import="com.bjsasc.plm.ui.tree.TreeNode"%>
<%@page import="com.bjsasc.plm.core.favorite.FavoriteHelper"%>
<%@page import="com.bjsasc.plm.ui.tree.TreeHelper"%>
<%@include file="/plm/plm.jsp" %>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.plm.core.Helper"%>
<%@page import="com.bjsasc.plm.ui.outlook.OutlookHelper"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	
	String scope = ContextManager.getService().getDefaultContextScope();
	List<Context> favorites = ContextManager.getService().getContexts("ActiveContext", scope);
	List<TreeNode> favoritesTreeNodes = OutlookHelper.buildTreeNodes(favorites);
	//list.addAll(TreeHelper.toMaps(favoritesTreeNodes));
	
	//虚拟节点“最近访问的”
	TreeNode recentlyVisitedNode = new TreeNode();
	recentlyVisitedNode.setNodeName("最近访问");
	recentlyVisitedNode.setNodeIconSrc("/plm/images/common/recently_visited.gif");
	recentlyVisitedNode.setExpanded(true);
	recentlyVisitedNode.setNodeLink("/adm/context/listActiveContexts.jsp");
	recentlyVisitedNode.getChildren().addAll(favoritesTreeNodes);
	
	list.add(recentlyVisitedNode.toMap());	
	
	TreeNode theAllTreeNode = TreeHelper.buildTreeNode(ActiveContext.class.getName(), "com.bjsasc.adm.context.active.all"); 
	if(theAllTreeNode != null){
		
		//theAllTreeNode.setNodeViewIcon(false);
		theAllTreeNode.setExpanded(true);
		//theAllTreeNode.setChildUrl("/plm/context/outlook/getOutlookChildren_getClazzAllObjects.jsp?CLAZZ=ProductContext");
		list.add(theAllTreeNode.toMap());	
	}
	
	String result = DataUtil.encode(list);
	out.print(result);
%>