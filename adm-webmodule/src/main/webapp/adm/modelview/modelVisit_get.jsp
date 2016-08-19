<%@page import="java.net.URLDecoder"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.*"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.ui.tree.TreeNode"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.plm.grid.spot.Spot"%>
<%@page import="com.bjsasc.plm.grid.spot.SpotUtil"%>
<%@page import="com.bjsasc.plm.grid.GridUtil"%>
<%@page import="com.bjsasc.plm.grid.GridView"%>
<%@page import="com.bjsasc.plm.grid.GridColumn"%>
<%@page import="com.cascc.avidm.util.SplitString"%>
<%@page import="com.bjsasc.plm.core.util.StringUtil"%>
<%@page import="com.bjsasc.adm.active.helper.AdmHelper"%>
<%@page import="com.bjsasc.adm.active.service.modelviewservice.ModelViewService"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%
	String contextPath = request.getContextPath();
	String op = request.getParameter("op");
	String keys = request.getParameter("keys");
	if(op == null){
		op = "getCabinet";
	}
	if (keys == null) {
		keys = "";
	} else {
		keys = StringUtil.encoding(keys, "GBK");
	}

	List<String> columnList = new ArrayList<String>();
	List<String> keyList = SplitString.string2List(keys, ";");
	
	for (String temp0 : keyList) {
		List<String> temp1 = SplitString.string2List(temp0, ",");
		List<String> temp2 = SplitString.string2List(temp1.get(0), ":");
		columnList.add(temp2.get(1));
	}
	List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
	if (op.equals("getCabinet")) {
		String spotId = "ActiveDocumentView";
		GridView view = Helper.getGridManager().getMyLatestGridView(spotId, spotId);
		Spot spot = SpotUtil.getSpot(spotId);
		for (GridColumn column:GridUtil.getColumns(spotId, spotId)) {
			String columnId = column.getAttrId();
			if (columnList.contains(columnId)) {
				continue;
			}
			String name = column.getAttrName();
			Map node = new HashMap();
			node.put("NAME", name);
			node.put("columnId", columnId);
			node.put(TreeNode.KEY_ICONSRC, contextPath + "/plm/images/common/type.gif");
			node.put(TreeNode.KEY_VIEWICON, true);
			node.put(TreeNode.KEY_EXPANDED, false);
			
			String key = "columnId:" + columnId + ",";
			String params = "op=getSubFolders&columnId=" + columnId + "&keys=" + keys + key;
			String url = contextPath + "/adm/modelview/modelVisit_get.jsp?" + params;
			node.put(TreeNode.KEY_CHILDURL, url);
			url = contextPath + "/adm/active/ModelViewHandle!getActiveDocuments.action?" + params;
			node.put(TreeNode.KEY_LINK, url);
			nodes.add(node);
		}
	} else if(op.equals("getSubFolders")) {
		String columnId = request.getParameter("columnId");
		ModelViewService service = AdmHelper.getModelViewService();
		List<Map<String, Object>> subMap = service.getSubObject(columnId, keys);
		for (Map<String, Object> map : subMap) {
			Object object = map.get("KEYNAME");
			if (object == null) {
				object = "NULL";
			}
			map.put(TreeNode.KEY_ICONSRC, contextPath + "/plm/images/common/all.gif");
			map.put(TreeNode.KEY_VIEWICON, true);
			map.put(TreeNode.KEY_EXPANDED, false);
			String key = "";
			try {
				key = URLEncoder.encode(object.toString()) + ";";
			} catch (Exception ex) {
				
			}
			String params = "op=getCabinet&columnId=" + columnId + "&keys=" + keys + key;
			String url = contextPath + "/adm/modelview/modelVisit_get.jsp?" + params;
			map.put(TreeNode.KEY_CHILDURL, url);
			url = contextPath + "/adm/active/ModelViewHandle!getActiveDocuments.action?" + params;
			map.put(TreeNode.KEY_LINK, url);
			nodes.add(map);
		}
	}
	out.print(DataUtil.encode(nodes));
%>