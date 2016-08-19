<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page language="java"%>
<%@page session="true"%>

<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.util.Ajax"%>
<%@page import="com.bjsasc.plm.doc.HistoryEnum"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.core.type.TypeHelper"%>
<%@page import="com.bjsasc.plm.core.type.TypeDefinition"%>
<%@page import="com.bjsasc.plm.core.attachment.FileHolder"%>
<%@page import="com.bjsasc.plm.core.context.model.LibraryContext"%>
<%@page import="com.bjsasc.plm.core.context.model.ProductContext"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.core.persist.PersistHelper"%>
<%@page import="com.bjsasc.plm.core.system.principal.User"%>
<%@page import="com.bjsasc.plm.core.session.SessionHelper"%>
<%@page import="com.bjsasc.plm.core.context.ContextHelper"%>
<%@page import="com.bjsasc.plm.core.system.securitylevel.SecurityLevel"%>
<%@page import="com.bjsasc.plm.core.system.securitylevel.ContextUserHelper" %>
<%@page import="com.bjsasc.plm.core.folder.Cabinet" %>
<%@page import="com.bjsasc.plm.core.folder.FolderHelper" %>
<%@page import="com.bjsasc.plm.common.visit.VisitHistoryHelper"%>
<%@page import="com.bjsasc.platform.objectmodel.business.lifeCycle.*"%>
<%@page import="com.bjsasc.platform.objectmodel.managed.external.data.ClassInfoDef"%>
<%@page import="com.bjsasc.platform.objectmodel.managed.external.util.ModelInfoUtil"%>
<%@page import="com.bjsasc.platform.objectmodel.managed.external.service.ModelInfoService"%>
<%@page import="com.bjsasc.platform.webframework.bean.FilterParam"%>
<%@page import="com.bjsasc.platform.webframework.tag.util.JsonUtil"%>
<%@page import="com.bjsasc.platform.modelmgr.lookuptable.model.LookupItemBean"%>
<%@page import="com.bjsasc.platform.modelmgr.lookuptable.service.LookupItemService"%>
<%@page import="com.bjsasc.platform.modelmgr.lookuptable.util.LookupTableServiceUtil"%>
<%@page import="com.bjsasc.platform.modelmgr.lookuptable.util.LookupTableExternalUtil"%>
<%@page import="com.bjsasc.adm.active.helper.AdmHelper" %>
<%@page import="com.bjsasc.adm.active.service.admmodelservice.AdmModelService" %>
<%@page import="com.bjsasc.adm.active.service.activedocumentservice.ActiveDocumentService" %>
<%@page import="com.bjsasc.adm.active.service.activeorderservice.ActiveOrderService" %>
<%@page import="com.bjsasc.adm.active.service.activesetservice.ActiveSetService" %>
<%@page import="com.bjsasc.adm.active.model.activeset.ActiveSet"%>
<%@page import="com.bjsasc.adm.active.model.activeorder.ActiveOrder"%>
<%@page import="com.bjsasc.adm.active.manage.ActiveDocumentManage"%>
<%@page import="com.bjsasc.adm.active.model.activedocument.ActiveDocument"%>

<%@page import="com.cascc.avidm.util.SplitString" %>
<%@page import="com.cascc.avidm.util.AvidmConstDefine"%>
<%@page import="com.cascc.avidm.login.model.PersonModel"%>
<%@page import="com.cascc.platform.aa.api.util.AAUtil"%>

<%@page errorPage="/plm/ajaxError.jsp"%>
<%
	//清除缓存
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	// 页面使用UTF-8编码
	request.setCharacterEncoding("UTF-8");
	String contextPath = request.getContextPath();
	String operate = request.getParameter("OPERATE");

	if("getContext".equals(operate)){
		String contextoid = request.getParameter("CONTEXTOID");
		Persistable pt = Helper.getPersistService().getObject(contextoid);
		List<Context> contexts = null;
		if(pt instanceof ProductContext){
			contexts= ContextHelper.getService().getAllProducts(); // 获取产品上下文列表,工作区创建使用	
		}else if(pt instanceof LibraryContext){
			contexts= ContextHelper.getService().getAllLibraryContexts();//获取基础库上下文列表
		}
		
		User user = SessionHelper.getService().getUser();
		List<Context> contextList = ContextHelper.getService().filterContextsByUserPrincipal(contexts, user);

		//获取上下文
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		for (Context prodContext : contextList) {
			String contextOid = Helper.getOid(prodContext);
			Map<String,String> map = new HashMap<String,String>();
			if (contextOid.equals(contextoid)) {
				map.put("text", prodContext.getName());
				map.put("value", contextOid);
				map.put("selected", "true");
				result.add(map);
			} else {
				map.put("text", prodContext.getName());
				map.put("value", contextOid);
				result.add(map);
			}
		}
		out.print(DataUtil.encode(result));
		out.flush();
	}/* else if ("getActiveDocumentType".equals(operate)) {
		String classId = request.getParameter(KeyS.CLASSID);
		List<TypeDefinition>  classDefList = TypeHelper.getService().getDescendants(classId);
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		Map<String,Object> blankMap = new HashMap<String,Object>();
		blankMap.put("text","请选择");
		blankMap.put("value","nothing");
		result.add(blankMap);
		TypeDefinition sourceDef = TypeHelper.getService().getType(classId);
		
		Map<String,Object> sourceMap = new HashMap<String,Object>();
		
		sourceMap.put("text",sourceDef.getName());
		sourceMap.put("value",sourceDef.getId());
		result.add(sourceMap);
		
		for(TypeDefinition temp:classDefList){
			Map<String,Object> map = new HashMap<String,Object>();
			
			map.put("text",temp.getName());
			map.put("value",temp.getId());
			result.add(map);
		}
		out.print(DataUtil.encode(result));
		out.flush();
	}*/ else if("getSecLevel".equals(operate)) {
		String contextOID = request.getParameter("CONTEXTOID");
		Context context = ContextHelper.getService().getContext(contextOID);
		List<Map<String,String>> lists = new ArrayList<Map<String,String>>();
		List<SecurityLevel> levels = ContextUserHelper.getUserSecurityService().getUserPermittedSecurityLevel(SessionHelper.getService().getUser(), context); 
		Map<String,String> map = null;
		if(levels != null && levels.size() > 0){
			for(SecurityLevel level : levels){
				map = new HashMap<String,String>();
				map.put("text", level.getName());
				map.put("value", Helper.getOid(level));
				lists.add(map);
			}
		}
		out.print(DataUtil.listToJson(lists));
		out.flush();
	}  else if ("getClassId".equals(operate)) {
		String classId = request.getParameter("CLASSID");
	/*	List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		Map<String,String> selectMap = new HashMap<String,String>();
		ModelInfoService moService = ModelInfoUtil.getService();

		ClassInfoDef def = moService.getClassInfo(classId);
		List<ClassInfoDef> classDefList = moService.getDescendants(ActiveDocument.CLASSID);
		boolean containsSelf = false;//classDefList是否包含def

		for (ClassInfoDef clazzDef : classDefList) {
			if(clazzDef.getId().equals(def.getId())){
				containsSelf = true;
			}

			Map<String,String> map = new HashMap<String,String>();
			if (classId.equals(clazzDef.getId())) {
				map.put("text", clazzDef.getName());
				map.put("value", clazzDef.getId());
				result.add(map);
			}
		}
			
		if(!containsSelf){
			Map<String,String> map = new HashMap<String,String>();
			map.put("text", def.getName());
			map.put("value",def.getId());
			result.add(0, map);
		}
		selectMap.put("text", "更多...");
		selectMap.put("value", "more");
		result.add(selectMap);
		out.print(DataUtil.encode(result));
		out.flush();
		*/
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		// 根模型，类型可以选择但是不包括它本身
		if("ActiveDocument".equals(classId)){
			Map<String,Object> blankMap = new HashMap<String,Object>();
			blankMap.put("text","请选择");
			blankMap.put("value","nothing");
			//blankMap.put("text","现行文件");
			//blankMap.put("value","ActiveDocument");
			result.add(blankMap);
			Map<String,Object> moreMap = new HashMap<String,Object>();
			moreMap.put("text", "更多...");
			moreMap.put("value", "more");
			result.add(moreMap);
			out.print(DataUtil.encode(result));
			out.flush();
		} else {
		// 非根模型，类型可以选择它本身和它的子类
			TypeDefinition sourceDef = TypeHelper.getService().getType(classId);
			Map<String,Object> sourceMap = new HashMap<String,Object>();
			sourceMap.put("text",sourceDef.getName());
			sourceMap.put("value",sourceDef.getId());
			result.add(sourceMap);

			List<TypeDefinition>  classDefList = TypeHelper.getService().getDescendants(classId);
			for(TypeDefinition temp:classDefList){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("text",temp.getName());
				map.put("value",temp.getId());
				result.add(map);
			}
			out.print(DataUtil.encode(result));
			out.flush();
		/*
		Map<String,Object> moreMap = new HashMap<String,Object>();
		moreMap.put("text", "更多...");
		moreMap.put("value", "more");
		result.add(moreMap);
		*/

		}
	} else if ("getCabinetByContext".equals(operate)) {
		String contextOid = request.getParameter("CONTEXTOID");
		Cabinet cabinet = ((Context) Helper.getPersistService().getObject(contextOid)).getCabinetDefault();
		String folerOid = Helper.getOid(cabinet);
		String folderPath = FolderHelper.getService().getFolderPathStr(cabinet);
		Map<String, String> result = new HashMap<String, String>();
		result.put("success", "true");
		result.put("FOLDEROID", folerOid);
		result.put("FOLDERPATH", folderPath);
		out.print(DataUtil.mapToSimpleJson(result));
		out.flush();
	} else if ("getFolderByClassID".equals(operate)) {
		String classId = request.getParameter("CLASSID");
		AdmModelService AMService = AdmHelper.getAdmModelService();
		//文件夹参数
		Map<String,String> result = AMService.getFolderByModel(classId);
		result.put("success", "true");
		out.print(DataUtil.mapToSimpleJson(result));
		out.flush();
	}else if ("getVersion".equals(operate)) {
		String number = request.getParameter("NUMBER");
		ActiveDocumentService servie = AdmHelper.getActiveDocumentService();
		List<ActiveDocument> list = servie.getActiveDocumentByNumber(number);
		String item = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
		List<String> itemList = SplitString.string2List(item, ",");
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		if(list==null||list.isEmpty()){

		}else{
			for(ActiveDocument obj:list){
				String versionNo = obj.getIterationInfo().getVersionNo();
				if(itemList.contains(versionNo)){
					itemList.remove(versionNo);
				}
			}
		}
		Map<String,Object> blankMap = new HashMap<String,Object>();
		blankMap.put("text","请选择");
		blankMap.put("value","nothing");
		result.add(blankMap);
		for(String temp:itemList){
			Map<String,Object> sourceMap = new HashMap<String,Object>();
			sourceMap.put("text",temp);
			sourceMap.put("value",temp);
			result.add(sourceMap);
		}
		out.print(DataUtil.encode(result));
		out.flush();
	}else if ("getVersionSet".equals(operate)) {
		String number = request.getParameter("NUMBER");
		ActiveSetService servie = AdmHelper.getActiveSetService();
		List<ActiveSet> list = servie.getActiveSetByNumber(number);
		String item = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
		List<String> itemList = SplitString.string2List(item, ",");
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		if(list==null||list.isEmpty()){

		}else{
			for(ActiveSet obj:list){
				String versionNo = obj.getIterationInfo().getVersionNo();
				if(itemList.contains(versionNo)){
					itemList.remove(versionNo);
				}
			}
		}
		Map<String,Object> blankMap = new HashMap<String,Object>();
		blankMap.put("text","请选择");
		blankMap.put("value","nothing");
		result.add(blankMap);
		for(String temp:itemList){
			Map<String,Object> sourceMap = new HashMap<String,Object>();
			sourceMap.put("text",temp);
			sourceMap.put("value",temp);
			result.add(sourceMap);
		}
		out.print(DataUtil.encode(result));
		out.flush();
	} else if ("getVersionOrder".equals(operate)) {
		String number = request.getParameter("NUMBER");
		ActiveOrderService servie = AdmHelper.getActiveOrderService();
		List<ActiveOrder> list = servie.getActiveOrderByNumber(number);
		String item = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
		List<String> itemList = SplitString.string2List(item, ",");
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		if(list==null||list.isEmpty()){

		}else{
			for(ActiveOrder obj:list){
				String versionNo = obj.getIterationInfo().getVersionNo();
				if(itemList.contains(versionNo)){
					itemList.remove(versionNo);
				}
			}
		}
		Map<String,Object> blankMap = new HashMap<String,Object>();
		blankMap.put("text","请选择");
		blankMap.put("value","nothing");
		result.add(blankMap);
		for(String temp:itemList){
			Map<String,Object> sourceMap = new HashMap<String,Object>();
			sourceMap.put("text",temp);
			sourceMap.put("value",temp);
			result.add(sourceMap);
		}
		out.print(DataUtil.encode(result));
		out.flush();
	} else if ("getActiveCode".equals(operate)) {

		String activeCode = "activeCode";
		
		PersonModel person =(PersonModel)session.getAttribute(AvidmConstDefine.SESSIONPARA_USERINFO);
		//获取数据字典定义数据
		List<LookupItemBean> listItem = LookupTableExternalUtil
		.getLookupExternalIfc().getLookupItemByLookupId(activeCode, person.getDomainRef(),1);
		//获取级别定义数据
		List<Map<String, String>> applylist = new ArrayList<Map<String, String>>();
		for (LookupItemBean itemBean : listItem) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("value", itemBean.getItemId());
			map.put("text",  itemBean.getItemValue());
			applylist.add(map);
		}
		String dataString = DataUtil.encode(applylist);
		out.println(dataString);
		out.flush();
		
	} else if ("getActiveDocumentExtAttr".equals(operate)) {
		
		// 扩展属性登录页面展示
		String oid = request.getParameter("oid");
		Map docMap = new HashMap();
		if (oid != null && !oid.equals("")) {
			String innerId = Helper.getInnerId(oid);
			String classId = Helper.getClassId(oid);
			ActiveDocument docTemplate = (ActiveDocument)Helper.getPersistService().getByClassId(classId, innerId);

			//添加扩展属性
			Map<String, Object> attrContainer = docTemplate.getExtAttrs();
			for (Entry<String, Object> attr : attrContainer.entrySet()) {
				if (attr.getValue() != null) {
					docMap.put(attr.getKey(), attr.getValue());
				} else {
					docMap.put(attr.getKey(), "");
				}
			}
		}
		String json = DataUtil.encode(docMap);
		out.print(json);
	}

%>