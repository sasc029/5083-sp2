<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page language="java"%>
<%@page session="true"%>

<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
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

	if ("getClassId".equals(operate)) {
		String classId = request.getParameter("CLASSID");
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
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
	} else if ("getActiveCode".equals(operate)) {

		String activeCode = "A001";
		
		PersonModel person =(PersonModel)session.getAttribute(AvidmConstDefine.SESSIONPARA_USERINFO);
		//获取数据字典定义数据
		List<LookupItemBean> listItem = LookupTableExternalUtil
		.getLookupExternalIfc().getLookupItemByLookupId(activeCode, person.getDomainRef(),5);
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
	}

%>