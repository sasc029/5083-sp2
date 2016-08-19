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
<%@page import="com.cascc.avidm.util.AvidmConstDefine"%>
<%@page import="com.cascc.platform.aa.api.util.*"%>
<%@page import="com.cascc.platform.aa.api.data.ac.*"%>
<%@page import="com.bjsasc.ui.json.*"%>
<%@page import="com.bjsasc.ui.util.TreeBean"%>
<%@page import="com.bjsasc.platform.i18n.*"%>


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
			+ "/platform/public/selectuser/UISelectUserAction.jsp?scope="
			+ scope + "&domainRef=" + domainRef + "&userStatus="
			+ userStatus;
	//grid数据
	String url_data = request.getContextPath()
			+ "/platform/data/SelectUserUtil.jsp?userStatus="
			+ userStatus + "&scope=" + scope + "&domainRef="
			+ domainRef + "&dataFormat=jq";
	//grid数据
	String url_baseData = request.getContextPath()
			+ "/platform/data/SelectUserUtil.jsp?dataFormat=jq";
	String sessionID = (String) request.getSession().getAttribute(
			AvidmConstDefine.SESSIONPARA_SESSIONID);
	AAContext ctx = new AAContext(sessionID);
	String treeNode = request.getParameter("treeNode");
	String rootName = PLU.getString(request,"pt.common.publicselect.rootName");
	if(rootName == null){
		rootName = "中国航天";
	}

	//得到树节点
	if ("getTree".equals(op)) {
		List list = new ArrayList();
		List childList = new ArrayList();
		if(treeNode!=null && "root".equals(treeNode)){
			Map map1 = new HashMap();
			map1.put("__viewicon", true);
			map1.put("expanded", true);
			map1.put("text", rootName);
			map1.put("id", "rootNode");
			map1.put("ChildUrl", url_selectUserAction + "&op=getTree");
			map1.put("iconsrc",request.getContextPath()
					+ "/platform/images/folder/Doc_tree_root.gif");
			String openUrl = url_baseData + "&userStatus="
					+ userStatus + "&scope="
					+ scope + "&domainRef="
					+ domainRef + "&pClass=user";
			map1.put("openUrl", openUrl);
			
			//行政组织
			Map map2 = new HashMap();
			map2.put("__viewicon", true);
			map2.put("expanded", false);
			map2.put("text", AATreeNodeSet.TREE_XZZZ_NAME);
			map2.put("id", AATreeNodeSet.TREE_XZZZ_ID);
			map2.put("ChildUrl", url_selectUserAction + "&op=org");
			map2.put("iconsrc",request.getContextPath()
					+ "/platform/images/org/organized_tree.gif");

			//系统用户
			Map map3 = new HashMap();
			map3.put("__viewicon", false);
			map3.put("text", AATreeNodeSet.TREE_XTYH_NAME);
			map3.put("id", AATreeNodeSet.TREE_XTYH_ID);
			String openUrl4 = url_data + "&pClass=user";
			map3.put("openUrl", openUrl4);
			map3.put("iconsrc",request.getContextPath()
					+ "/platform/images/people/user_tree.gif");
			
			childList.add(map2);
			childList.add(map3);
			map1.put("children",childList);
			list.add(map1);
		}else{
			//行政组织
			Map map1 = new HashMap();
			map1.put("__viewicon", true);
			map1.put("expanded", false);
			map1.put("text", AATreeNodeSet.TREE_XZZZ_NAME);
			map1.put("id", AATreeNodeSet.TREE_XZZZ_ID);
			map1.put("ChildUrl", url_selectUserAction + "&op=org");
			map1.put("iconsrc",request.getContextPath()
					+ "/platform/images/org/organized_tree.gif");
			list.add(map1);

			//系统用户
			Map map2 = new HashMap();
			map2.put("__viewicon", false);
			map2.put("text", AATreeNodeSet.TREE_XTYH_NAME);
			map2.put("id", AATreeNodeSet.TREE_XTYH_ID);
			String openUrl4 = url_data + "&pClass=user";
			map2.put("openUrl", openUrl4);
			map2.put("iconsrc",request.getContextPath()
					+ "/platform/images/people/user_tree.gif");
			list.add(map2);
		}
		String xx = DataUtil.encode(list);
		out.println(xx);
	}

	//获取行政组织tree数据源页面
	else if ("org".equals(op)) {
		DivisionService divisionService = AAProvider
				.getDivisionService();
		String divIID = request.getParameter("divIID");
		FilterParam filterParam = new FilterParam();
		filterParam.setField("WEIGHT");
		filterParam.setDirection("asc");
		filterParam.addItem("divType", AADomainUtil.PRIPTYPE_ORG);
		if (divIID == null || divIID.trim().length() == 0) {
			divIID = " ";
		}
		List allDivisionsList = new ArrayList();

		//scope=all 获取所有组织
		if (AADomainUtil.VISTYPE_ALL.equals(scope)) {
			allDivisionsList = divisionService.listSubDivisions(ctx,
					divIID, filterParam);
			List list = new ArrayList();
			for (int i = 0; i < allDivisionsList.size(); i++) {
				AADivisionData data = (AADivisionData) allDivisionsList
						.get(i);
				Map map1 = new HashMap();
				map1.put("__viewicon", true);
				map1.put("expanded", false);
				map1.put("text", data.getName());
				map1.put("id", data.getInnerID());
				String url_getOrgChild = url_selectUserAction
						+ "&divIID=" + data.getInnerID() + "&op=org";
				map1.put("ChildUrl", url_getOrgChild);
				String openUrl = url_baseData + "&userStatus="
						+ userStatus + "&scope="
						+ AADomainUtil.VISTYPE_BIND + "&domainRef="
						+ domainRef + "&pClass=org&pValue2="
						+ data.getInnerID();
				map1.put("openUrl", openUrl);
				map1.put("iconsrc",request.getContextPath()
						+ "/platform/images/org/organized_tree.gif");
				list.add(map1);
			}
			//形成json串
			String str = DataUtil.encode(list);
			out.println(str);
			return;
		}
		//scope=self取得指定域出生的组织（一个包括父子结构的分支）【组织域下使用】同步树
		else if (AADomainUtil.VISTYPE_SELF.equals(scope)) {
			List dataList = new ArrayList();
			List<ExtendDivisionData> divs = new ArrayList<ExtendDivisionData>();
			divs = AAProvider.getDivisionService()
					.listTreeDivisions4Ancesstor(ctx,
							Integer.parseInt(domainRef),
							AADomainUtil.PRIPTYPE_ORG);
			for (ExtendDivisionData data : divs) {
				int domIID = data.getDomainRef();
				boolean isAllow = true;
				if (domIID != Integer.parseInt(domainRef)) {
					isAllow = false;
				}
				String text = AADomainUtil.getFontStyle(isAllow, false,
						data.getName());
				Map map1 = new HashMap();
				map1.put("text", text);
				map1.put("id", data.getInnerID());
				if (data.isLeaf()) {
					map1.put("__viewicon", false);
				} else {
					map1.put("__viewicon", true);
					map1.put("expanded", false);
				}
				String url_getOrgChild = url_selectUserAction
						+ "&divIID=" + data.getInnerID() + "";
				//map1.put("ChildUrl", url_getOrgChild);
				String parentRef = data.getParentref();
				String openUrl = url_baseData + "&userStatus="
						+ userStatus + "&scope=" + scope
						+ "&domainRef=" + domainRef
						+ "&pClass=org&pValue2=" + data.getInnerID();
				map1.put("openUrl", openUrl);
				map1.put("iconsrc",request.getContextPath()
						+ "/platform/images/org/organized_tree.gif");
				map1.put("pid", parentRef);
				dataList.add(map1);
			}
			String str = DataUtil.listToJsonSync(dataList);
			response.getWriter().write(str);
			return;
		}
		// scope=path 获取指定域下以及制定域的祖先域下的组织
		else if (AADomainUtil.VISTYPE_PATH.equals(scope)) {
			List dataList = new ArrayList();
			List<AADivisionData> ancestorList = AAProvider
					.getDivisionService().listAncestorDivisions(ctx,
							Integer.parseInt(domainRef), filterParam);
			Map<String, String> ancestorMap = new HashMap<String, String>();
			for (AADivisionData data : ancestorList) {
				ancestorMap.put(data.getInnerID(), "");
			}
			List<AADivisionData> subDivList = AAProvider
					.getDivisionService().listSubDivisions(ctx, divIID,
							filterParam);
			for (AADivisionData data : subDivList) {
				if (ancestorMap.containsKey(data.getInnerID())) {
					int domIID = data.getDomainRef();
					boolean isAllow = true;
					if (domIID != Integer.parseInt(domainRef)) {
						isAllow = false;
					}
					String text = AADomainUtil.getFontStyle(isAllow,
							false, data.getName());
					Map map1 = new HashMap();
					map1.put("text", text);
					map1.put("id", data.getInnerID());
					map1.put("__viewicon", true);
					map1.put("expanded", false);

					String url_getOrgChild = url_selectUserAction
							+ "&divIID=" + data.getInnerID()
							+ "&op=org";
					map1.put("ChildUrl", url_getOrgChild);
					String openUrl = url_baseData + "&userStatus="
							+ userStatus + "&scope=" + scope
							+ "&domainRef=" + domainRef
							+ "&pClass=org&pValue2="
							+ data.getInnerID();
					map1.put("openUrl", openUrl);
					map1.put("iconsrc",request.getContextPath()
							+ "/platform/images/org/organized_tree.gif");
					dataList.add(map1);
				}
			}
			String str = DataUtil.encode(dataList);
			response.getWriter().write(str);
			return;
		}
		//scope=bind获取指定域下绑定组织（一个包括父子结构的分支）【应用域下使用】同步树
		else if (AADomainUtil.VISTYPE_BIND.equals(scope)) {
			List<AADivisionData> existList = AAProvider
					.getDivisionService().listBindDivisions(null,
							Integer.parseInt(domainRef), filterParam);
			Map<String, String> existMap = new HashMap<String, String>();
			for (AADivisionData data : existList) {
				existMap.put(data.getInnerID(), "");
			}
			List dataList = new ArrayList();
			List<ExtendDivisionData> divs = new ArrayList<ExtendDivisionData>();
			divs = AAProvider.getDivisionService()
					.listTreeDivisions4Domain(null,
							Integer.parseInt(domainRef),
							AADomainUtil.PRIPTYPE_ORG);
			for (ExtendDivisionData data : divs) {
				int domIID = data.getDomainRef();
				boolean isAllow = true;
				if (!existMap.containsKey(data.getInnerID())) {
					isAllow = false;
				}
				String text = AADomainUtil.getFontStyle(isAllow, false,
						data.getName());
				Map map1 = new HashMap();
				map1.put("text", text);
				map1.put("id", data.getInnerID());
				if (data.isLeaf()) {
					map1.put("__viewicon", false);
				} else {
					map1.put("__viewicon", true);
					map1.put("expanded", false);
				}

				String url_getOrgChild = url_selectUserAction
						+ "&divIID=" + data.getInnerID() + "&op=";
				//map1.put("ChildUrl", url_getOrgChild);
				String openUrl = url_baseData + "&userStatus="
						+ userStatus + "&scope=" + scope
						+ "&domainRef=" + domainRef
						+ "&pClass=org&pValue2=" + data.getInnerID();
				map1.put("openUrl", openUrl);
				map1.put("iconsrc",request.getContextPath()
						+ "/platform/images/org/organized_tree.gif");
				String parentRef = data.getParentref();
				map1.put("pid", parentRef);
				dataList.add(map1);
			}
			String str = DataUtil.listToJsonSync(dataList);
			response.getWriter().write(str);
			return;
		}
	}//end for getOrg
%>