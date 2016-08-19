
<%
	/**
	 *  ҳ��˵��
	 *  1.���ƣ� platform/platform/public/selectorg/SelectOrgAction.jsp
	 *  2.���ã� ����ѡ����֯����Դҳ��
	 *  3.����: op ������ʶ
				scope: ��Ա�б�Χ��all��ȫ���û�����self����ָ�����г������û�����ref����ָ����������û�
				����ƶ�������֯����Ĭ��Ϊ��ʾ������֯������ƶ�����Ӧ��������ʾ������֯
				domainRef ���innerid Ĭ��Ϊ��ǰ��¼����
	 */
%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@ page language="java"%>
<%@ page session="true"%>

<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.bjsasc.platform.webframework.tag.util.TreeUtil"%>
<%@page import="com.bjsasc.platform.webframework.util.FilterParamUtil"%>
<%@page import="com.bjsasc.platform.webframework.tag.util.JsonUtil"%>
<%@page import="com.bjsasc.platform.webframework.tag.bean.TreeBean"%>
<%@page import="com.bjsasc.platform.webframework.bean.FilterParam"%>
<%@page import="com.cascc.platform.aa.api.util.AAUtil"%>
<%@page import="com.cascc.platform.aa.api.data.AAExtAttrData"%>
<%@page import="com.cascc.platform.aa.api.data.AADomainData"%>
<%@page import="com.cascc.platform.aa.api.data.ac.ExtendDivisionData"%>
<%@page import="com.cascc.platform.aa.AAProvider"%>
<%@page import="com.cascc.platform.aa.api.data.AADivisionData"%>
<%@page import="com.cascc.platform.aa.api.util.AADomainUtil"%>
<%@page import="com.cascc.platform.aa.AAContext"%>
<%@page import="com.cascc.avidm.login.model.PersonModel"%>
<%@page import="com.cascc.avidm.util.AvidmConstDefine"%>
<%
	//�������
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	request.setCharacterEncoding("UTF-8");
	
	String sessionID = (String) request.getSession().getAttribute(
			AvidmConstDefine.SESSIONPARA_SESSIONID);
	AAContext ctx = new AAContext(sessionID);
	PersonModel person = (PersonModel) session.getAttribute("person");
	int loginDomainRef = person.getDomainRef();
	
	String scope = request.getParameter("scope");
	String domainRef = request.getParameter("domainRef");
	
	//��������
	String url_selectUserAction = request.getContextPath()
			+ "/platform/public/selectorg/SelectOrgAction.jsp?scope="+scope+"&domainRef="+domainRef;
	String url_data = request.getContextPath()
			+ "/platform/public/selectorg/SelectOrgAction.jsp?op=getOrg&scope="+scope+"&domainRef="+domainRef;

	String op = request.getParameter("op");
	if("getTree".equals(op)){
		try{
			String divIID = request.getParameter("divIID");
			if (divIID == null || divIID.trim().length() == 0) {
				divIID = " ";
			} 
			FilterParam filterParam = new FilterParam();
			filterParam.setField("WEIGHT");
			filterParam.setDirection("asc");
			filterParam.addItem("divType",AADomainUtil.PRIPTYPE_ORG);
			List list = new ArrayList();
			// scop=all ������֯
			if(AADomainUtil.VISTYPE_ALL.equals(scope)){
				List<AADivisionData> subDivList = AAProvider.getDivisionService().listSubDivisions(ctx,divIID,filterParam);
				for (AADivisionData data : subDivList) {
						TreeBean tb = new TreeBean();
						tb.setText(data.getName());
						tb.setId(data.getInnerID());
						tb.setLeaf("false");
						tb.setIcon(request.getContextPath()
								+ "/platform/images/org/organized_tree.gif");
						tb.setIconEx(request.getContextPath()
								+ "/platform/images/org/organized_tree_add.gif");
						String url_getOrgChild =url_selectUserAction
								+ "&divIID="+ data.getInnerID()+"&op=getTree";
						tb.setChildUrl(url_getOrgChild);
						Map exmap = new HashMap();
						String openUrl = url_data + "&iid=" + data.getInnerID();
						exmap.put("openUrl", openUrl);
						tb.setExpPro(exmap);
						list.add(tb);
				}
			}
			// scope=self ȡ��ָ�����������֯��һ���������ӽṹ�ķ�֧,�ƶ����������֯�Լ��������������֯������֯����ʹ�á�ͬ����
			else if(AADomainUtil.VISTYPE_SELF.equals(scope)){
				List<ExtendDivisionData> subDivList = AAProvider.getDivisionService().listTreeDivisions4Ancesstor(ctx, Integer.parseInt(domainRef),AADomainUtil.PRIPTYPE_ORG);
				for (ExtendDivisionData data : subDivList) {
						TreeBean tb = new TreeBean();
						int domIID = data.getDomainRef();
						AADomainData dom = AAProvider.getDomainService()
								.getDomain(null, domIID);
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
						String url_getOrgChild =url_selectUserAction
								+ "&divIID="+ data.getInnerID()+"&op=";
						tb.setChildUrl(url_getOrgChild);
						Map exmap = new HashMap();
						//�������ʶ���������֯��������
						String openUrl = request.getContextPath()
							+ "/platform/public/selectorg/SelectOrgAction.jsp?op=getOrg&scope="+scope+"&domainRef="+domainRef + "&iid=" + data.getInnerID();
						exmap.put("openUrl", openUrl);
						tb.setExpPro(exmap);
						list.add(tb);
				}
			}
			// scope=path ��ȡָ�������Լ�ָ������������µ���֯
			else if(AADomainUtil.VISTYPE_PATH.equals(scope)){
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
						AADomainData dom = AAProvider.getDomainService()
								.getDomain(null, domIID);
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
						String url_getOrgChild =url_selectUserAction
								+ "&divIID="+ data.getInnerID()+"&op=getTree";
						tb.setChildUrl(url_getOrgChild);
						Map exmap = new HashMap();
						String openUrl = url_data + "&iid=" + data.getInnerID();
						exmap.put("openUrl", openUrl);
						tb.setExpPro(exmap);
						list.add(tb);
					}
				}
			}
			
			//scope=bind ��ȡָ�����°���֯��һ���������ӽṹ�ķ�֧����Ӧ������ʹ�á�ͬ����
			else if(AADomainUtil.VISTYPE_BIND.equals(scope)){
				List<AADivisionData> existList = AAProvider
				.getDivisionService().listBindDivisions(null,Integer.parseInt(domainRef), filterParam);
				Map<String, String> existMap = new HashMap<String, String>();
				for (AADivisionData data : existList) {
					existMap.put(data.getInnerID(), "");
				}
				List<ExtendDivisionData> subDivList = AAProvider.getDivisionService().listTreeDivisions4Domain(ctx, Integer.parseInt(domainRef),AADomainUtil.PRIPTYPE_ORG);
				for (ExtendDivisionData data : subDivList) {
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
						String url_getOrgChild =url_selectUserAction
								+ "&divIID="+ data.getInnerID()+"&op=";
						tb.setChildUrl(url_getOrgChild);
						Map exmap = new HashMap();
						String openUrl = url_data + "&iid=" + data.getInnerID();
						exmap.put("openUrl", openUrl);
						tb.setExpPro(exmap);
						list.add(tb);
				}
			}
			//�γ�json��
			String str = TreeUtil.listToJson(list);
			out.println(str);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//��֯grid ����Դ
	else if("getOrg".equals(op)){
		try{
			String start = request.getParameter("start");
			if (start == null || start.trim().equalsIgnoreCase("")) {
				start = "0";
			}
			String limit = request.getParameter("limit");
			if (limit == null || limit.trim().equalsIgnoreCase("")) {
				limit = "10";
			}

			int count = 0;
			int pageStart = Integer.parseInt(start) + 1;
			int pageEnd = Integer.parseInt(limit) + pageStart - 1;
			List<AADivisionData> subDivList = new ArrayList<AADivisionData>();

			String parentDivIID = request.getParameter("iid");

			FilterParam filter = FilterParamUtil.requestToFilter(request);
		 	filter.addItem("divType",AADomainUtil.PRIPTYPE_ORG);
			//scopt=all ���ȫ����֯
			if(AADomainUtil.VISTYPE_ALL.equals(scope)){
				subDivList = AAProvider.getDivisionService().listRangeSubDivisions(
						ctx, parentDivIID, pageStart,pageEnd,filter);
				
				count = AAProvider.getDivisionService().countSubDivisions(ctx,parentDivIID,filter);
			}
			//scopt=self ��ó�����ָ�������֯
			else if(AADomainUtil.VISTYPE_SELF.equals(scope)){
				filter.addItem("domainRef", "=", domainRef);
				subDivList = AAProvider.getDivisionService().listRangeSubDivisions(
						ctx, parentDivIID, pageStart,pageEnd,filter);
				count = AAProvider.getDivisionService().countSubDivisions(ctx,parentDivIID,filter);
			}
			//scopt=path ��ó�����ָ�������֯
			else if(AADomainUtil.VISTYPE_PATH.equals(scope)){
				filter.addItem("domainRef", "=", domainRef);
				subDivList = AAProvider.getDivisionService().listRangeSubDivisions(
						ctx, parentDivIID, pageStart,pageEnd,filter);
				count = AAProvider.getDivisionService().countSubDivisions(ctx,parentDivIID,filter);
			}
			//scopt=bind ����ƶ���󶨵���֯
			else if(AADomainUtil.VISTYPE_BIND.equals(scope)){
				List subDivListAll = AAProvider.getDivisionService()
				.listBindSubDivisions(ctx, Integer.parseInt(domainRef), parentDivIID,
						filter);
				subDivList = AADomainUtil.listRangeData(subDivListAll,pageStart,pageEnd);
				count = subDivListAll.size();
			}
			
			List<Map<String, String>> jsonList = new ArrayList<Map<String, String>>();
			for (AADivisionData data : subDivList) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("INNERID", data.getInnerID());
				map.put("ID", data.getId());
				map.put("NAME", data.getName());
				map.put("DESCRIPTION", data.getDescription());
				map.put("DOMAINREF", data.getDomainRef() + "");
				map.put("DOMAINIID",data.getDomainRef() + "");
				AADomainData domain = AAProvider.getDomainService().getDomain(
						null, data.getDomainRef());
				if (domain != null) {
					map.put("DOMAINNAME", domain.getName());
				} else {
					map.put("DOMAINNAME", "");
				}
				map.put("JIBIE",data.getJibie()+"");
				map.put("MIJI",data.getMiji());
				
				List<AAExtAttrData> exts = AAProvider.getAAExtAttrService()
				.getAAExtAttrDatasByType(AAUtil.getAAContext(), "DIVISION");
				for (int i = 0; i < exts.size(); i++) {
					AAExtAttrData ext = (AAExtAttrData) exts.get(i);
					if (0 == ext.getStatus()) {
						String fieldName = ext.getId().toUpperCase();
						Object o = data.getExtProp(fieldName);
						String res = "";
						if (null != o) {
							res = o + "";
						}
					map.put(fieldName, res);
						}
					}
				
				map.put("pt_modify", "�༭");
				jsonList.add(map);
			}
			if(count==0){
				count = jsonList.size();
			}
			String xx = JsonUtil.listToJson(count, jsonList);
			out.println(xx);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
%>