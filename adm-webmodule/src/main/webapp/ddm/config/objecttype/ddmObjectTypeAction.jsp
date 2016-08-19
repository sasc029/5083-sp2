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
<%@page import="com.bjsasc.ddm.distribute.model.distributeobjecttype.DistributeObjectType"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeobjecttype.DistributeObjectTypeService"%>
<%@page import="com.bjsasc.ddm.common.DdmTypePermissionCache"%>


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
	// 取得角色/成员数据
	if (op.equals("data")) {
		List<Map<String, String>> listMap = (List<Map<String, String>>)session.getAttribute("DOT_DATA_LIST");
		if (listMap == null) {
			listMap = new ArrayList<Map<String, String>>();
		}
		results = DataUtil.listToJson(listMap);
	}
	// 取得角色/成员--类型数据
	else if (op.equals("typeData")) {
		List<Map<String, String>> listMap = (List<Map<String, String>>)session.getAttribute("DOT_TYPE_DATA_LIST");
		if (listMap == null) {
			listMap = new ArrayList<Map<String, String>>();
		}
		results = DataUtil.listToJson(listMap);
	} 
	// 添加角色
	else if (op.equals("addRole")) {
		String roleIIDs = request.getParameter("SelRoleIID");
		List<String> roleIIDList = SplitString.string2List(roleIIDs, ";");

		List<Map<String, String>> listMap = (List<Map<String, String>>)session.getAttribute("DOT_DATA_LIST");
		if (listMap == null) {
			listMap = new ArrayList<Map<String, String>>();
		}
		for (String roleIID : roleIIDList) {
			boolean cc = true;
			for (Map<String, String> mm : listMap) {
				String aaInnerId = mm.get("AAINNERID");
				if (roleIID.equals(aaInnerId)) {
					cc = false;
					break;
				}
			}
			if (cc) {
				Principal principal = RoleHelper.getService().getRole(roleIID);
				Map<String, String> map = new HashMap<String, String>();
				map.put("AAINNERID", roleIID);
				map.put("STATE", "ROLE");
				map.put("OID", principal.getClassId() + ":" + principal.getInnerId());
				map.put("NAME", principal.getName());
				map.put("TYPE", "<img src='"+contextPath + "/plm/images/system/role.gif'>");
				map.put("DK", principal.getClassId() + ":" + principal.getInnerId());
				listMap.add(map);
			}
		}
		session.setAttribute("DOT_DATA_LIST", listMap);
		
		Map<String, String> result = new HashMap<String, String>();
		result.put("success", "true");
		results = DataUtil.mapToSimpleJson(result);
	} 
	// 添加用户
	else if (op.equals("addUser")) {
		String userIIDs = request.getParameter("SelUserIID");
		List<String> userIIDList = SplitString.string2List(userIIDs, ";");

		List<Map<String, String>> listMap = (List<Map<String, String>>)session.getAttribute("DOT_DATA_LIST");
		if (listMap == null) {
			listMap = new ArrayList<Map<String, String>>();
		}
		for (String userIID : userIIDList) {
			boolean cc = true;
			for (Map<String, String> mm : listMap) {
				String aaInnerId = mm.get("AAINNERID");
				if (userIID.equals(aaInnerId)) {
					cc = false;
					break;
				}
			}
			if (cc) {
				Principal principal = UserHelper.getService().getUser(userIID);
				Map<String, String> map = new HashMap<String, String>();
				map.put("AAINNERID", userIID);
				map.put("STATE", "USER");
				map.put("OID", principal.getClassId() + ":" + principal.getInnerId());
				map.put("NAME", principal.getName());
				map.put("TYPE", "<img src='"+contextPath + "/plm/images/system/user.gif'>");
				map.put("DK", principal.getClassId() + ":" + principal.getInnerId());
				listMap.add(map);
			}
		}
		session.setAttribute("DOT_DATA_LIST", listMap);
		
		Map<String, String> result = new HashMap<String, String>();
		result.put("success", "true");
		results = DataUtil.mapToSimpleJson(result);
	} 
	// 添加类型
	else if (op.equals("addType")) {
		Context context = (Context) Helper.getPersistService().getObject(contextOid);
		
		String typeIds = request.getParameter("typeIds");
		String typeNames = request.getParameter("typeNames");
		String OIDS = request.getParameter("OIDS");
		
		List<String> typeIdList = SplitString.string2List(typeIds, "#@#");
		List<String> typeNameList = SplitString.string2List(typeNames, "#@#");
		List<String> OIDList = SplitString.string2List(OIDS, "#@#");
		
		Map<String, String> typeMap = new HashMap<String, String>();
		int typeCount = typeIdList.size();
		for (int i=0; i<typeCount; i++) {
			String typeId = typeIdList.get(i);
			String typeName = typeNameList.get(i); 
			typeMap.put(typeId, typeName);
		}		

		List<Map<String, String>> listMap = (List<Map<String, String>>)session.getAttribute("DOT_TYPE_DATA_LIST");
		if (listMap == null) {
			listMap = new ArrayList<Map<String, String>>();
		}
		
		for (String oid : OIDList) {
			Principal principal = (Principal)Helper.getPersistService().getObject(oid);
			for (String typeId : typeIdList) {
				boolean cc = true;
				for (Map<String, String> mm : listMap) {
					String toid = mm.get("OID");
					String dot_type_id = mm.get("DOT_TYPE_ID");
					if (oid.equals(toid) && typeId.equals(dot_type_id)) {
						cc = false;
						break;
					}
				}
				if (cc) {
					String state = "用户";
					if (principal instanceof Role) {
						state = "角色";
					}
					Map<String, String> map = new HashMap<String, String>();
					map.put("DOT_DATA", principal.getName());
					map.put("DOT_TYPE_ID", typeId);
					map.put("DOT_TYPE_NAME", typeMap.get(typeId));
					map.put("DOT_STATE", state);
					map.put("CONTEXT", context.getName());
					
					map.put("OID", principal.getClassId() + ":" + principal.getInnerId());
					map.put("DK", principal.getClassId() + ":" + principal.getInnerId() + ":" + typeId);
					listMap.add(map);
				}
			}
		}
		session.setAttribute("DOT_TYPE_DATA_LIST", listMap);
		
		Map<String, String> result = new HashMap<String, String>();
		result.put("success", "true");
		results = DataUtil.mapToSimpleJson(result);
	} 
	// 删除数据
	else if (op.equals("deleteData")) {
		String DKS = request.getParameter("DKS");
		
		List<String> DKList = SplitString.string2List(DKS, "#@#");

		List<Map<String, String>> listMap = (List<Map<String, String>>)session.getAttribute("DOT_DATA_LIST");
		if (listMap == null) {
			listMap = new ArrayList<Map<String, String>>();
		}
		
		for (String dk : DKList) {
			for (Map<String, String> mm : listMap) {
				String tdk = mm.get("DK");
				if (dk.equals(tdk)) {
					listMap.remove(mm);
					break;
				}
			}
		}
		session.setAttribute("DOT_DATA_LIST", listMap);
		
		Map<String, String> result = new HashMap<String, String>();
		result.put("success", "true");
		results = DataUtil.mapToSimpleJson(result);
	} 
	// 删除数据
	else if (op.equals("deleteTypeData")) {
		String DKS = request.getParameter("DKS");
		
		List<String> DKList = SplitString.string2List(DKS, "#@#");

		List<Map<String, String>> listMap = (List<Map<String, String>>)session.getAttribute("DOT_TYPE_DATA_LIST");
		if (listMap == null) {
			listMap = new ArrayList<Map<String, String>>();
		}
		
		for (String dk : DKList) {
			for (Map<String, String> mm : listMap) {
				String tdk = mm.get("DK");
				if (dk.equals(tdk)) {
					listMap.remove(mm);
					break;
				}
			}
		}
		session.setAttribute("DOT_TYPE_DATA_LIST", listMap);
		
		Map<String, String> result = new HashMap<String, String>();
		result.put("success", "true");
		results = DataUtil.mapToSimpleJson(result);
	} else if (op.equals("addTypeData")) {
		String pageType = request.getParameter("pageType");
		List<Map<String, String>> listMap = (List<Map<String, String>>)session.getAttribute("DOT_DATA_LIST");
		List<Map<String, String>> listTypeMap = (List<Map<String, String>>)session.getAttribute("DOT_TYPE_DATA_LIST");
		
		DistributeObjectTypeService service = DistributeHelper.getDistributeObjectTypeService();
		
		if ("edit".equals(pageType)) {
			service.editTypeData(contextOid, listMap, listTypeMap);
			//添加类型到缓存
			DdmTypePermissionCache.clearTypeMap();
			DdmTypePermissionCache.loadDataSource();
		} else {
			service.addTypeData(contextOid, listTypeMap);
			//添加类型到缓存
			DdmTypePermissionCache.clearTypeMap();
			DdmTypePermissionCache.loadDataSource();
			
		}
		
		Map<String, String> result = new HashMap<String, String>();
		result.put("success", "true");
		results = DataUtil.mapToSimpleJson(result);
	} 
	// 删除数据
	else if (op.equals("deleteObjectTypeData")) {
		String oids = request.getParameter("OIDS");
		
		DistributeObjectTypeService service = DistributeHelper.getDistributeObjectTypeService();
		service.deleteTypeData(contextOid, oids);
		
		Map<String, String> result = new HashMap<String, String>();
		
		List<Map<String, Object>> listData = service.findByContextOid(contextOid);
		//在session上缓存查询结果，以便高效排序
		String spot = "ListDistributeObjectTypes";
		GridDataUtil.prepareRowQueryResult(listData, spot);
		
		result.put("success", "true");
		results = DataUtil.mapToSimpleJson(result);
		//添加类型到缓存
		DdmTypePermissionCache.clearTypeMap();
		DdmTypePermissionCache.loadDataSource();
	} 
	// 编辑数据
	else if (op.equals("editData")) {
		String oids = request.getParameter("OIDS");
		Context context = (Context) Helper.getPersistService().getObject(contextOid);
		
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();	
		List<Map<String, String>> listTypeMap = new ArrayList<Map<String, String>>();

		String sql = "select * from DDM_DIS_OBJECT_TYPE t "
				+ " where t.contextid=? and t.contextclassid=? "
				+ "and t.dataId=? and t.dataClassId=? ";
		String cid = Helper.getInnerId(contextOid);
		String ccd = Helper.getClassId(contextOid);

		List<String> oidList = SplitString.string2List(oids, "#@#");
		for (String oid : oidList) {
			Principal principal = (Principal) Helper.getPersistService().getObject(oid);
			Map<String, String> map = new HashMap<String, String>();
			map.put("AAINNERID", "");
			map.put("OID", oid);
			map.put("NAME", principal.getName());
			if (principal instanceof Role) {
				map.put("STATE", "ROLE");
				map.put("TYPE", "<img src='"+contextPath + "/plm/images/system/role.gif'>");
			} else {
				map.put("STATE", "USER");
				map.put("TYPE", "<img src='"+contextPath + "/plm/images/system/user.gif'>");
			}			
			map.put("DK", principal.getClassId() + ":" + principal.getInnerId());
			listMap.add(map);

			String did = Helper.getInnerId(oid);
			String dcd = Helper.getClassId(oid);
			List<DistributeObjectType> dotList = Helper.getPersistService().query(
					sql, DistributeObjectType.class, cid, ccd, did, dcd);
			
			for (DistributeObjectType dot : dotList) {
				String state = "用户";
				if (principal instanceof Role) {
					state = "角色";
				}
				String typeId = dot.getTypeId();
				Map<String, String> tpmap = new HashMap<String, String>();
				tpmap.put("DOT_DATA", principal.getName());
				tpmap.put("DOT_TYPE_ID", typeId);
				tpmap.put("DOT_TYPE_NAME", dot.getTypeName());
				tpmap.put("DOT_STATE", state);
				tpmap.put("CONTEXT", context.getName());
				
				tpmap.put("OID", oid);
				tpmap.put("DK", oid + ":" + typeId);
				listTypeMap.add(tpmap);
			}
		}
		
		session.setAttribute("DOT_DATA_LIST", listMap);
		session.setAttribute("DOT_TYPE_DATA_LIST", listTypeMap);

		Map<String, String> result = new HashMap<String, String>();
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