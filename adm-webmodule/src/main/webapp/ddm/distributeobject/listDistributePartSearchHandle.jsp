<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.bjsasc.plm.util.Ajax"%>
<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page language="java"%>
<%@page session="true"%>

<%@page import="com.bjsasc.ddm.common.CommonSearcher"%>
<%
	//清除缓存
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	// 页面使用UTF-8编码
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html;charset=UTF-8");
	//获取页面传输的参数	
	String param = request.getParameter("param");
	String[] keys = CommonSearcher.CONDITIONS;
	
	CommonSearcher searcher = new CommonSearcher();
	searcher.setContextPath(request.getContextPath());
	for (String key : keys) {
		String value = request.getParameter(key);
		searcher.addQueryCondition(key, value);
	}
	String oid=request.getParameter("oid");
	String spot="DistributePartSearch";
	String spotConditions = request.getParameter(CommonSearcher.SPOT_CONDITIONS);
	searcher.setSpotConditions(spotConditions);
	//在搜索之前把场景信息放入到搜索中
	boolean flag = true;
	List<Persistable> list=searcher.getResult(oid,flag);
	String basePath=request.getContextPath();
	List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
	for (Persistable Persistable : list) {
		Map<String, Object> map  =  Helper.getTypeManager().getAttrValues(Persistable);
		Map<String, String> objMap=(Map<String, String> )map.get("SOURCE");
		map.put("NAME", "<a href=\""+basePath+"/plm/common/visit.jsp?OID="+objMap.get("OID")+"\" target='_blank'>"+objMap.get("NAME")+"</a>");
		map.put("NUMBER", "<a href=\""+basePath+"/plm/common/visit.jsp?OID="+objMap.get("OID")+"\" target='_blank'>"+objMap.get("NUMBER")+"</a>");
		maps.add(map);
	}
	
	GridDataUtil.prepareRowObjects(maps,  spot);
	Map<String, String> result = new HashMap<String, String>();
	if(list!=null&&list.size()>0){ 
		result.put(Ajax.SUCCESS, "true");
	}else{
		result.put(Ajax.SUCCESS, "false");
	} 
	String json = DataUtil.mapToSimpleJson(result);
	out.write(json); 
%>