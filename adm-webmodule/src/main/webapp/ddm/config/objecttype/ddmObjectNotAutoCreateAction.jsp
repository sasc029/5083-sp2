<%@page import="java.util.*"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.cascc.avidm.util.SplitString"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.core.system.principal.Role"%>
<%@page import="com.bjsasc.plm.core.system.principal.Principal"%>
<%@page import="com.bjsasc.plm.core.system.principal.RoleHelper"%>
<%@page import="com.bjsasc.plm.core.system.principal.UserHelper"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeobjectnotautocreate.DistributeObjectNotAutoCreate"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeobjectnotautocreate.DistributeObjectNotAutoCreateService"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%
	String contextPath = request.getContextPath();
	String op = request.getParameter("op");
	String contextOid = request.getParameter("contextOid");
	if (op == null) {
		op = "getRoot";
	}
	String results = "";
	try {
		
	// 删除数据
	if (op.equals("addTypeData")) {
		Context context = (Context) Helper.getPersistService().getObject(contextOid);
		
		String typeIds = request.getParameter("typeIds");
		String typeNames = request.getParameter("typeNames");
		
		List<String> typeIdList = SplitString.string2List(typeIds, "#@#");
		List<String> typeNameList = SplitString.string2List(typeNames, "#@#");
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		Map<String, String> typeMap = new HashMap<String, String>();
		int typeCount = typeIdList.size();
		for (int i=0; i<typeCount; i++) {
			String typeId = typeIdList.get(i);
			String typeName = typeNameList.get(i);	
			Map<String, String> map = new HashMap<String, String>();
			map.put("DOT_TYPE_ID", typeId);
			map.put("DOT_TYPE_NAME", typeName);
			map.put("CONTEXT", context.getName());
					
			listMap.add(map);
		}
		
		DistributeObjectNotAutoCreateService service = DistributeHelper.getDistributeObjectNotAutoCreateService();
		
		service.addTypeData(contextOid, listMap);
		
		//在session上缓存查询结果，以便高效排序
		List<Map<String, Object>> listData = service.findByContextOid(contextOid);
		String spot = "ListDistributeObjectNotAutoCreate";
		GridDataUtil.prepareRowQueryResult(listData, spot);
		
		Map<String, String> result = new HashMap<String, String>();
		result.put("success", "true");
		results = DataUtil.mapToSimpleJson(result);
	} 
	// 删除数据
	else if (op.equals("deleteObjectTypeData")) {
		String typeNames = request.getParameter("NAMES");
		
		DistributeObjectNotAutoCreateService service = DistributeHelper.getDistributeObjectNotAutoCreateService();
		service.deleteTypeData(contextOid, typeNames);
		
		Map<String, String> result = new HashMap<String, String>();
		
		List<Map<String, Object>> listData = service.findByContextOid(contextOid);
		//在session上缓存查询结果，以便高效排序
		String spot = "ListDistributeObjectNotAutoCreate";
		GridDataUtil.prepareRowQueryResult(listData, spot);
		
		result.put("success", "true");
		results = DataUtil.mapToSimpleJson(result);
	} 
	
	// 错误处理
	} catch (Exception ex) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("success", "false");
		result.put("message", ex.getMessage());
		results = DataUtil.mapToSimpleJson(result);
	}
	out.print(results);
%>