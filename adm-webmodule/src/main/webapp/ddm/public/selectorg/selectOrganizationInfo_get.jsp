<%@page import="com.cascc.platform.aa.api.data.AADivisionData"%>
<%@page import="com.cascc.platform.aa.AAProvider"%>
<%@page import="com.cascc.platform.aa.api.util.AADomainUtil"%>
<%@page import="com.bjsasc.platform.webframework.bean.FilterParam"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.bjsasc.plm.foundation.Helper"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.bjsasc.plm.core.system.principal.OrganizationHelper"%>
<%@page import="com.bjsasc.plm.core.system.principal.Organization"%>
<%@page import="com.bjsasc.plm.core.context.ContextHelper"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@ page language="java"%>
<%@ page session="true"%>
<%!
	//所有的子组织信息
	private void getAllChildOrganizationInfo(Map<String,Object> map,Organization org,String searchText,String ids,String path, boolean isOutOrg){
	    // Map<String,Object> map = new LinkedHashMap<String,Object>();
		if(path==null){
			path = org.getAaOrgInnerId();
		}else{
			path = path + "/" + org.getAaOrgInnerId();
		}
		if(searchText!=null&&!searchText.trim().equals("")&&org.getName().contains(searchText.trim())){
			map.put("PATH"+map.size(), path);
		}
		if(ids!=null&&!ids.trim().equals("")&&ids.contains(org.getAaOrgInnerId())){
			map.put("PATH"+map.size(), path);
		}
		if(!isOutOrg) {
			List<Organization> orgs = OrganizationHelper.getService().getChildOrganizations(org);
			for(Organization o:orgs){
				getAllChildOrganizationInfo(map,o,searchText,ids,path,isOutOrg);
			}
		} else {
			String domainRef = "10000";
			FilterParam filter = new FilterParam();;
			filter.addItem("divType",AADomainUtil.PRIPTYPE_ORG);
			filter.setField("WEIGHT,ID");
			filter.setDirection("ASC,ASC");
			String parentDivIID = org.getAaOrgInnerId();
			List<AADivisionData> subDivList = AAProvider.getDivisionService().listSubDivisions(null, parentDivIID, Integer.parseInt(domainRef), filter);
			for(AADivisionData data : subDivList) {
				Organization subOrg = new Organization();
				subOrg.setName(data.getName());
				subOrg.setAaOrgInnerId(data.getInnerId());
				getAllChildOrganizationInfo(map,subOrg,searchText,ids,path, true);
			}
		}
	}
%>
<%
	String type = request.getParameter("type");
	String searchText = request.getParameter("searchText");
	String ids = request.getParameter("ids");
	Map<String,Object> map = new LinkedHashMap<String,Object>();
	List<Context> contexts = null;
	//if(type==null||type.equals("1")){//所有组织上下文
	//	contexts = ContextHelper.getService().getAllOrgContext(true);
	//}else if(type.equals("2")){//不包含外域上下文
		contexts = ContextHelper.getService().getAllOrgContext(false);
	//}else if(type.equals("3")){//仅仅外域上下文
	//	contexts = ContextHelper.getService().getAllOrgContextOnlyOuterDomainOrg();
	//}
	Context rootContext = ContextHelper.getService().getRootContext();
	contexts.add(rootContext);
	for(Context context:contexts){
		List<Organization> orgs = new ArrayList<Organization>();
		//if(type==null||type.equals("1")){ //所有组织机构
			orgs = OrganizationHelper.getService().getAllRootOrganizations(context);
		//}else if(type.equals("4")){ //不包含外部组织机构
		//	orgs = OrganizationHelper.getService().getRootOnlyOuterOrganizations(context);
		//}else{ //仅仅外部组织机构
		//	orgs = OrganizationHelper.getService().getRootOrganizations(context);
		//}
		for(Organization org:orgs){
			//if(org.getName().contains(searchText.trim())){
			getAllChildOrganizationInfo(map,org,searchText,ids,null, false);
			//}
		}
	}
	/**
	String domainRef = "10000";
	FilterParam filterParam = new FilterParam();
	filterParam.setField("WEIGHT,ID");
	filterParam.setDirection("ASC,ASC");
	filterParam.addItem("divType",AADomainUtil.PRIPTYPE_ORG);
	List<AADivisionData> subDivList = AAProvider.getDivisionService().listSubDivisions(null, null,Integer.parseInt(domainRef), filterParam);
	for (AADivisionData data : subDivList) {
		Organization org = new Organization();
		org.setName(data.getName());
		org.setAaOrgInnerId(data.getInnerId());
		getAllChildOrganizationInfo(map,org,searchText,ids,null, true);
	}
	*/
	map.put("SIZE", map.size());
	String json= DataUtil.mapToSimpleJson(map); 
    out.write(json);
%>