<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@ page language="java"%>
<%@ page session="true"%>

<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@page import="com.cascc.platform.aa.AAContext"%>
<%@page import="com.cascc.platform.aa.AAProvider"%>
<%@page import="com.cascc.platform.aa.api.util.RoleUtil"%>
<%@page import="com.cascc.platform.aa.api.data.AADivisionData"%>
<%@page import="com.cascc.platform.aa.api.DivisionService"%>
<%@page import="com.cascc.platform.aa.api.util.AAConstants"%>
<%@page import="com.cascc.platform.aa.api.util.AATreeNodeSet"%>
<%@page import="com.bjsasc.platform.webframework.bean.FilterParam"%>
<%@page import="com.bjsasc.platform.webframework.tag.util.TreeUtil"%>
<%@page import="com.bjsasc.platform.webframework.tag.bean.TreeBean"%>
<%@page import="com.cascc.avidm.util.AvidmConstDefine"%>
<%@page import="com.cascc.platform.aa.api.util.*"%>
<%@page import="com.cascc.platform.aa.api.data.ac.*"%>


<%
	//清除缓存
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	request.setCharacterEncoding("UTF-8");

	//操作类型
	String op = request.getParameter("op");
	//三员管理增加用户的过滤条件
	String userStatus = request.getParameter("userStatus");
	//用户范围
	String scope = request.getParameter("scope");
	//选择域
	String domainRef = request.getParameter("domainRef");
	//行政组织tree childUrl
	String url_selectUserAction = request.getContextPath()
			+ "/platform/public/selectuser/SelectUserAction.jsp?scope="+scope+"&domainRef="+domainRef+"&userStatus="+userStatus;
	//grid数据
	String url_data = request.getContextPath()
			+ "/platform/data/SelectUserUtil.jsp?userStatus="+userStatus+"&scope="+scope+"&domainRef="+domainRef;

	String sessionID = (String) request.getSession().getAttribute(
			AvidmConstDefine.SESSIONPARA_SESSIONID);
	AAContext ctx = new AAContext(sessionID);

	//得到树节点
	if ("getTree".equals(op)) {
		//行政组织
		List list = new ArrayList();
		TreeBean tb = new TreeBean();
		tb.setText(AATreeNodeSet.TREE_XZZZ_NAME);
		tb.setId(AATreeNodeSet.TREE_XZZZ_ID);
		tb.setIconCls("org_tree");
		tb.setLeaf("false");
		tb.setChildUrl(url_selectUserAction + "&op=org");
		list.add(tb);

		//系统用户
		TreeBean tb4 = new TreeBean();
		tb4.setText(AATreeNodeSet.TREE_XTYH_NAME);
		tb4.setId(AATreeNodeSet.TREE_XTYH_ID);
		tb4.setLeaf("true");
		tb4.setIcon(request.getContextPath()
				+ "/platform/images/people/user_tree.gif");
		tb4.setIconEx(request.getContextPath()
				+ "/platform/images/people/user_tree_add.gif");

		Map exmap4 = new HashMap();
		String openUrl4 = url_data + "&pClass=user";
		exmap4.put("openUrl", openUrl4);
		tb4.setExpPro(exmap4);

		list.add(tb4);

		String str = TreeUtil.beanToJson(list, "");
		out.println(str);
	}

	//获取行政组织tree数据源页面
	else if ("org".equals(op)) {
		DivisionService divisionService = AAProvider
				.getDivisionService();
		String divIID = request.getParameter("divIID");
		FilterParam filterParam = new FilterParam();
		filterParam.setField("WEIGHT");
		filterParam.setDirection("asc");
		filterParam.addItem("divType",AADomainUtil.PRIPTYPE_ORG);
		if (divIID == null || divIID.trim().length() == 0) {
			divIID = " ";
		}
		List allDivisionsList = new ArrayList();
		
		//scope=all 获取所有组织
		if(AADomainUtil.VISTYPE_ALL.equals(scope)){
			allDivisionsList = divisionService.listSubDivisions(ctx,divIID,filterParam);
			List list = new ArrayList();
			for (int i = 0; i < allDivisionsList.size(); i++) {
				AADivisionData data = (AADivisionData) allDivisionsList
						.get(i);
				TreeBean tb = new TreeBean();
				tb.setText(data.getName());
				tb.setId(data.getInnerID());
				tb.setLeaf("false");
				tb.setIcon(request.getContextPath()
						+ "/platform/images/org/organized_tree.gif");
				tb.setIconEx(request.getContextPath()
						+ "/platform/images/org/organized_tree_add.gif");
				String url_getOrgChild = url_selectUserAction
						+ "&divIID="
						+ data.getInnerID()+"&op=org";
				tb.setChildUrl(url_getOrgChild);
				Map exmap = new HashMap();
				String openUrl = request.getContextPath()
					+ "/platform/data/SelectUserUtil.jsp?userStatus="+userStatus
					+"&scope="+AADomainUtil.VISTYPE_ALL
					+"&domainRef="+data.getDomainRef()
					+"&pClass=org&pValue2="+ data.getInnerID();
				exmap.put("openUrl", openUrl);
				tb.setExpPro(exmap);
				list.add(tb);
			}
			//形成json串
			String str = TreeUtil.beanToJson(list, "");
			out.println(str);
			return;
		}
		//scope=self取得指定域出生的组织（一个包括父子结构的分支）【组织域下使用】同步树
		else if(AADomainUtil.VISTYPE_SELF.equals(scope)){
			List dataList = new ArrayList();
			List<ExtendDivisionData> divs = new ArrayList<ExtendDivisionData>();
				divs = AAProvider.getDivisionService()
						.listTreeDivisions4Ancesstor(ctx, Integer.parseInt(domainRef),AADomainUtil.PRIPTYPE_ORG);
			for (ExtendDivisionData data : divs) {
				TreeBean tb = new TreeBean();
				int domIID = data.getDomainRef();
				boolean isAllow = true;
				if (domIID != Integer.parseInt(domainRef)) {
					isAllow = false;
				}
				String text = AADomainUtil.getFontStyle(isAllow,
						false, data.getName());
				tb.setText(text);
				tb.setId(data.getInnerID());
				tb.setLeaf(data.isLeaf() + "");
				String parentRef = data.getParentref();
				if(parentRef == null || parentRef.trim().equalsIgnoreCase("")){
					tb.setPid("");
				}else{
					tb.setPid(parentRef);
				}
				tb.setIcon(request.getContextPath()
						+ "/platform/images/org/organized_tree.gif");
				tb.setIconEx(request.getContextPath()
						+ "/platform/images/org/organized_tree_add.gif");
				String url_getOrgChild = url_selectUserAction
						+ "&divIID="
						+ data.getInnerID()+"";
				tb.setChildUrl(url_getOrgChild);
				Map exmap = new HashMap();
				String openUrl = request.getContextPath()
					+ "/platform/data/SelectUserUtil.jsp?userStatus="+userStatus
					+"&scope="+scope
					+"&domainRef="+domainRef
					+"&pClass=org&pValue2="+ data.getInnerID();
				exmap.put("openUrl", openUrl);
				tb.setExpPro(exmap);
				
				dataList.add(tb);
			}
			String str = TreeUtil.listToJson(dataList);
			response.getWriter().write(str);
			return;
		}
		// scope=path 获取指定域下以及制定域的祖先域下的组织
		else if(AADomainUtil.VISTYPE_PATH.equals(scope)){
			List dataList = new ArrayList();
			List<AADivisionData> ancestorList = AAProvider.getDivisionService().listAncestorDivisions(ctx,
					Integer.parseInt(domainRef), filterParam);
			Map<String, String> ancestorMap = new HashMap<String, String>();
			for (AADivisionData data : ancestorList) {
				ancestorMap.put(data.getInnerID(), "");
			}
			List<AADivisionData> subDivList	 = AAProvider.getDivisionService().listSubDivisions(
					ctx, divIID, filterParam);
			for (AADivisionData data : subDivList) {
				if (ancestorMap.containsKey(data.getInnerID())) {
					TreeBean tb = new TreeBean();
					int domIID = data.getDomainRef();
					boolean isAllow = true;
					if (domIID != Integer.parseInt(domainRef)) {
						isAllow = false;
					}
					String text = AADomainUtil.getFontStyle(isAllow,
							false, data.getName());
					tb.setText(text);
					tb.setId(data.getInnerID());
					tb.setLeaf("false");
					tb.setIcon(request.getContextPath()
							+ "/platform/images/org/organized_tree.gif");
					tb.setIconEx(request.getContextPath()
							+ "/platform/images/org/organized_tree_add.gif");
					String url_getOrgChild = url_selectUserAction
							+ "&divIID="
							+ data.getInnerID()+"&op=org";
					tb.setChildUrl(url_getOrgChild);
					Map exmap = new HashMap();
					String openUrl = request.getContextPath()
						+ "/platform/data/SelectUserUtil.jsp?userStatus="+userStatus
						+"&scope="+scope
						+"&domainRef="+domainRef
						+"&pClass=org&pValue2="+ data.getInnerID();
					exmap.put("openUrl", openUrl);
					tb.setExpPro(exmap);
					dataList.add(tb);
				}
			}
			String str = TreeUtil.listToJson(dataList);
			response.getWriter().write(str);
			return;
		}
		//scope=bind获取指定域下绑定组织（一个包括父子结构的分支）【应用域下使用】同步树
		else if(AADomainUtil.VISTYPE_BIND.equals(scope)){
			List<AADivisionData> existList = AAProvider
			.getDivisionService().listBindDivisions(null,Integer.parseInt(domainRef), filterParam);
			Map<String, String> existMap = new HashMap<String, String>();
			for (AADivisionData data : existList) {
				existMap.put(data.getInnerID(), "");
			}
			List dataList = new ArrayList();
			List<ExtendDivisionData> divs = new ArrayList<ExtendDivisionData>();
				divs = AAProvider.getDivisionService()
						.listTreeDivisions4Domain(null, Integer.parseInt(domainRef),AADomainUtil.PRIPTYPE_ORG);
			for (ExtendDivisionData data : divs) {
				TreeBean tb = new TreeBean();
				int domIID = data.getDomainRef();
				boolean isAllow = true;
				if (!existMap.containsKey(data.getInnerID())) {
					isAllow = false;
				}
				String text = AADomainUtil.getFontStyle(isAllow,
						false, data.getName());
				tb.setText(text);
				tb.setId(data.getInnerID());
				tb.setLeaf(data.isLeaf() + "");
				String parentRef = data.getParentref();
				if(parentRef == null || parentRef.trim().equalsIgnoreCase("")){
					tb.setPid("");
				}else{
					tb.setPid(parentRef);
				}
				tb.setIcon(request.getContextPath()
						+ "/platform/images/org/organized_tree.gif");
				tb.setIconEx(request.getContextPath()
						+ "/platform/images/org/organized_tree_add.gif");
				String url_getOrgChild = url_selectUserAction
						+ "&divIID="
						+ data.getInnerID()+"&op=";
				tb.setChildUrl(url_getOrgChild);
				Map exmap = new HashMap();
				String openUrl = request.getContextPath()
					+ "/platform/data/SelectUserUtil.jsp?userStatus="+userStatus
					+"&scope="+scope
					+"&domainRef="+domainRef
					+"&pClass=org&pValue2="+ data.getInnerID();
				exmap.put("openUrl", openUrl);
				tb.setExpPro(exmap);
				
				dataList.add(tb);
			}
			String str = TreeUtil.listToJson(dataList);
			response.getWriter().write(str);
			return;
		}
	}//end for getOrg
%>