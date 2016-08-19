<%@page import="com.cascc.platform.aa.api.data.AADivisionData"%>
<%@page import="com.bjsasc.platform.webframework.bean.FilterParam"%>
<%@page import="com.cascc.platform.aa.api.data.ac.ExtendDivisionData"%>
<%@page import="com.cascc.platform.aa.AAProvider"%>
<%@page import="com.cascc.platform.aa.api.util.AADomainUtil"%>
<%@page import="com.bjsasc.platform.objectmodel.business.persist.ObjectReference"%>
<%@page import="com.bjsasc.plm.core.system.sort.SortHelper"%>
<%@page import="com.bjsasc.plm.core.context.model.RootContext"%>
<%@page import="java.util.Set"%>
<%@page import="com.bjsasc.plm.core.context.model.AppContext"%>
<%@page import="com.bjsasc.plm.core.context.util.RoleUtil"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.bjsasc.plm.core.system.principal.PrincipalHelper"%>
<%@page import="com.bjsasc.plm.core.context.team.Team"%>
<%@page import="com.bjsasc.plm.core.context.team.TeamHelper"%>
<%@page import="com.bjsasc.plm.core.context.model.OrgContext"%>
<%@page import="com.bjsasc.plm.core.system.principal.Principal"%>
<%@page import="com.bjsasc.plm.core.context.ContextHelper"%>
<%@page import="com.bjsasc.plm.core.system.principal.RoleHelper"%>
<%@page import="com.bjsasc.plm.core.system.principal.OrganizationHelper"%>
<%@page import="com.bjsasc.plm.core.system.principal.Role"%>
<%@page import="com.bjsasc.plm.core.system.principal.Organization"%>
<%@page import="com.bjsasc.plm.core.foundation.Helper"%>
<%@page import="com.bjsasc.plm.ui.tree.TreeHelper"%>
<%@page import="com.bjsasc.plm.ui.tree.TreeNode"%>
<%@page import="com.bjsasc.plm.core.system.principal.User"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.plm.core.system.principal.Group"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@ page language="java"%>
<%@ page session="true"%>
<%
	String op = request.getParameter("op");
	String type = request.getParameter("type");
	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	if ("Root".equals(op)) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", "root");
		map.put("text", "站点");
		map.put("__viewicon", true);
		map.put("expanded", false);
		map.put("iconsrc", request.getContextPath()+ "/platform/images/folder/Doc_tree_root.gif");
		String url = request.getContextPath()+"/ddm/public/selectorg/selectOrganizationTree_get.jsp?op=members&type="+type;
		map.put("ChildUrl",url);
		list.add(map);
	}else if ("members".equals(op)){
		List<Context> contexts = null;
	/* 	if(type==null||type.equals("1")){//所有组织上下文
			contexts = ContextHelper.getService().getAllOrgContext(true);
		}else if(type.equals("2")){ *///不包含外域上下文
			contexts = ContextHelper.getService().getAllOrgContext(false);
		/* }else if(type.equals("3")){//仅仅外域上下文
			contexts = ContextHelper.getService().getAllOrgContextOnlyOuterDomainOrg();
		} */
		Context rootContext = ContextHelper.getService().getRootContext();
		contexts.add(rootContext);
		for(Context context:contexts){
			List<Organization> orgs = new ArrayList<Organization>();
			/* if(type==null||type.equals("1")){  *///所有组织机构
			orgs = OrganizationHelper.getService().getAllRootOrganizations(context);
		/* 	}else if(type.equals("4")){ //不包含外部组织机构
				orgs = OrganizationHelper.getService().getRootOnlyOuterOrganizations(context);
			}else{ //仅仅外部组织机构
				orgs = OrganizationHelper.getService().getRootOrganizations(context);
			} */
			for(Organization org:orgs){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("__viewicon", true);
				map.put("expanded", false);
				map.put("AAORGINNERID", org.getAaOrgInnerId());
				map.put("NAME", org.getName());
				map.put("NUMBER", org.getId());
				map.put("text", org.getName());
				map.put("id", Helper.getOid(org.getClassId(), org.getInnerId()));
				map.put("iconsrc", request.getContextPath()+ "/plm/images/system/organization.gif");
				String url = request.getContextPath()+"/ddm/public/selectorg/selectOrganizationTree_get.jsp?op=childOrg&type="+type+"&orgOid="+Helper.getOid(org.getClassId(), org.getInnerId());
				map.put("ChildUrl",url);			
				list.add(map);
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
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("__viewicon", true);
			map.put("expanded", false);
			map.put("AAORGINNERID", data.getInnerId());
			map.put("NAME", data.getName());
			map.put("NUMBER", data.getId());
			map.put("text", data.getName());
			map.put("id", Helper.getOid(Organization.CLASSID, data.getInnerId()));
			map.put("iconsrc", request.getContextPath()+ "/plm/images/system/organization.gif");
			String url = request.getContextPath()+"/ddm/public/selectorg/selectOrganizationTree_get.jsp?op=childOrg&type="+type+"&orgOid="+Helper.getOid(Organization.CLASSID, data.getInnerId() + "&isOutOrg=true");
			map.put("ChildUrl",url);			
			list.add(map);
		}
		*/
	}else if("childOrg".equals(op)){
		String orgOid = request.getParameter("orgOid");
		String isOutOrg = request.getParameter("isOutOrg");
		if(isOutOrg != null && isOutOrg.equals("true")) {
			String domainRef = "10000";
			FilterParam filter = new FilterParam();;
			filter.addItem("divType",AADomainUtil.PRIPTYPE_ORG);
			filter.setField("WEIGHT,ID");
			filter.setDirection("ASC,ASC");
			String parentDivIID = Helper.getInnerId(orgOid);
			List<AADivisionData> subDivList = AAProvider.getDivisionService().listSubDivisions(null, parentDivIID, Integer.parseInt(domainRef), filter);
			for(AADivisionData data : subDivList) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("__viewicon", true);
				map.put("expanded", false);
				map.put("AAORGINNERID", data.getInnerId());
				map.put("NAME", data.getName());
				map.put("NUMBER", data.getId());
				map.put("text", data.getName());
				map.put("id", Helper.getOid(Organization.CLASSID, data.getInnerId()));
				map.put("iconsrc", request.getContextPath()+ "/plm/images/system/organization.gif");
				String url = request.getContextPath()+"/ddm/public/selectorg/selectOrganizationTree_get.jsp?op=childOrg&type="+type+"&orgOid="+Helper.getOid(Organization.CLASSID, data.getInnerId() + "&isOutOrg=true");
				map.put("ChildUrl",url);			
				list.add(map);
			}
		} else {
			Organization organization = (Organization)Helper.getPersistService().getObject(orgOid);
			List<Organization> orgs = OrganizationHelper.getService().getChildOrganizations(organization);
			for(Organization org:orgs){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("__viewicon", true);
				map.put("expanded", false);
				map.put("AAORGINNERID", org.getAaOrgInnerId());
				map.put("NAME", org.getName());
				map.put("NUMBER", org.getId());
				map.put("text", org.getName());
				map.put("id", Helper.getOid(org.getClassId(), org.getInnerId()));
				map.put("iconsrc", request.getContextPath()+ "/plm/images/system/organization.gif");
				String url = request.getContextPath()+"/ddm/public/selectorg/selectOrganizationTree_get.jsp?op=childOrg&type="+type+"&orgOid="+Helper.getOid(org.getClassId(), org.getInnerId());
				map.put("ChildUrl",url);
				list.add(map);
			}
		}
	}
	String json = DataUtil.encode(list);
	out.println(json);
%>
