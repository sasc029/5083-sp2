
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
<%@page import="com.cascc.avidm.login.model.PersonModel"%>
<%@page import="com.cascc.avidm.util.AvidmConstDefine"%>
<%@page import="com.bjsasc.ui.util.*"%>
<%@page import="com.cascc.platform.aa.api.data.*"%>
<%@page import="com.bjsasc.platform.webframework.util.*"%>
<%@page import="com.bjsasc.ui.json.*"%>
<%@page import="com.bjsasc.platform.i18n.*"%>
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
			+ "/platform/public/selectorg/UISelectOrgAction.jsp?scope="+scope+"&domainRef="+domainRef;
	String url_data = request.getContextPath()
			+ "/platform/public/selectorg/UISelectOrgAction.jsp?op=getOrg&scope="+scope+"&domainRef="+domainRef + "&dataFormat=jq";

	String url_baseData = request.getContextPath()
	+ "/platform/data/SelectOrgUtil.jsp?dataFormat=jq";

	String url_rootNode = request.getContextPath()
	+ "/platform/public/selectorg/UISelectOrgAction.jsp?op=getOrg&scope="+
	scope+"&domainRef="+domainRef + "&dataFormat=jq";

	String op = request.getParameter("op");
	String treeNode = request.getParameter("treeNode");

	String rootName = PLU.getString(request,"pt.common.publicselect.rootName");
	if(rootName == null){
		rootName = "�й�����";
	}

	if("getTree".equals(op)){
		try{
			List list = new ArrayList();
			if(treeNode!=null && "root".equals(treeNode)){
				Map map1 = new HashMap();
				map1.put("__viewicon", true);
				map1.put("expanded", false);
				map1.put("text", rootName);
				map1.put("id", "rootNode");
				map1.put("ChildUrl", url_selectUserAction + "&op=getTree");
				map1.put("iconsrc",request.getContextPath()
						+ "/platform/images/folder/Doc_tree_root.gif");
				String openUrl = url_rootNode;
				map1.put("openUrl", openUrl);
				list.add(map1);
				String str = DataUtil.encode(list);
				out.println(str);
				return;
			}
			String divIID = request.getParameter("divIID");
			if (divIID == null || divIID.trim().length() == 0) {
				divIID = " ";
			}
			FilterParam filterParam = new FilterParam();
			filterParam.setField("WEIGHT");
			filterParam.setDirection("asc");
			filterParam.addItem("divType",AADomainUtil.PRIPTYPE_ORG);

			// scop=all ������֯
			if(AADomainUtil.VISTYPE_ALL.equals(scope)){
				List<AADivisionData> subDivList = AAProvider.getDivisionService().listSubDivisions(ctx,divIID,filterParam);
				for (AADivisionData data : subDivList) {
					/**
						TreeBean tb = new TreeBean();
						tb.setText(data.getName());
						tb.setId(data.getInnerID());
						tb.setLeaf("false");
						String url_getOrgChild =url_selectUserAction
								+ "&divIID="+ data.getInnerID()+"&op=getTree";
						tb.setChildUrl(url_getOrgChild);
						Map exmap = new HashMap();
						String openUrl = url_data + "&iid=" + data.getInnerID();
						exmap.put("openUrl", openUrl);
						tb.setExpPro(exmap);
						list.add(tb);
					**/
						Map map1 = new HashMap();
						map1.put("__viewicon", true);
						map1.put("expanded", false);
						map1.put("text", data.getName());
						map1.put("id", data.getInnerID());
						String url_getOrgChild =url_selectUserAction
						+ "&divIID="+ data.getInnerID()+"&op=getTree";
						map1.put("ChildUrl", url_getOrgChild);
						map1.put("iconsrc",request.getContextPath()
								+ "/platform/images/org/organized_tree.gif");
						String openUrl = url_data + "&iid=" + data.getInnerID();
						map1.put("openUrl", openUrl);
						list.add(map1);
				}
			}
			// scope=self ȡ��ָ�����������֯��һ���������ӽṹ�ķ�֧,�ƶ����������֯�Լ��������������֯������֯����ʹ�á�ͬ����
			else if(AADomainUtil.VISTYPE_SELF.equals(scope)){
				List<ExtendDivisionData> subDivList = AAProvider.getDivisionService().listTreeDivisions4Ancesstor(ctx, Integer.parseInt(domainRef),AADomainUtil.PRIPTYPE_ORG);
				for (ExtendDivisionData data : subDivList) {
						int domIID = data.getDomainRef();
						AADomainData dom = AAProvider.getDomainService()
								.getDomain(null, domIID);
						boolean isAllow = true;
						if (domIID != Integer.parseInt(domainRef)) {
							isAllow = false;
						}
						String text = AADomainUtil.getFontStyle(isAllow,
								false, data.getName());
						Map map1 = new HashMap();
						map1.put("__viewicon", true);
						map1.put("text", text);
						map1.put("id", data.getInnerID());
						map1.put("pid", data.getParentref());
						String url_getOrgChild =url_selectUserAction
						+ "&divIID="+ data.getInnerID()+"&op=";
						//�������ʶ���������֯��������
						String openUrl = url_data + "&iid=" + data.getInnerID();
						map1.put("openUrl", openUrl);
						map1.put("iconsrc",request.getContextPath()
								+ "/platform/images/org/organized_tree.gif");

						list.add(map1);
				}
				String xx = DataUtil.listToJsonSync(list);
				out.println(xx);
				return;
			}
			// scope=path ��ȡָ�������Լ��ƶ�����������µ���֯
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

						int domIID = data.getDomainRef();
						AADomainData dom = AAProvider.getDomainService()
								.getDomain(null, domIID);
						boolean isAllow = true;
						if (domIID != Integer.parseInt(domainRef)) {
							isAllow = false;
						}
						String text = AADomainUtil.getFontStyle(isAllow,
								false, data.getName());

						/**
						TreeBean tb = new TreeBean();
						tb.setText(text);
						tb.setId(data.getInnerID());
						tb.setLeaf("false");
						String url_getOrgChild =url_selectUserAction
								+ "&divIID="+ data.getInnerID()+"&op=getTree";
						tb.setChildUrl(url_getOrgChild);
						Map exmap = new HashMap();
						String openUrl = url_data + "&iid=" + data.getInnerID();
						exmap.put("openUrl", openUrl);
						tb.setExpPro(exmap);
						list.add(tb);
						**/
						Map map1 = new HashMap();
						map1.put("__viewicon", true);
						map1.put("text", text);
						map1.put("id", data.getInnerID());
						String url_getOrgChild =url_selectUserAction
						+ "&divIID="+ data.getInnerID()+"&op=getTree";
						map1.put("ChildUrl", url_getOrgChild);
						String openUrl = url_data + "&iid=" + data.getInnerID();
						map1.put("openUrl", openUrl);
						map1.put("iconsrc",request.getContextPath()
								+ "/platform/images/org/organized_tree.gif");
						list.add(map1);
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

				List<Map<String,Object>> dataList = new java.util.ArrayList<Map<String,Object>>();

				for (ExtendDivisionData data : subDivList) {
					boolean isAllow = true;
					if (!existMap.containsKey(data.getInnerID())) {
						isAllow = false;
					}
					String text = AADomainUtil.getFontStyle(isAllow, false,
							data.getName());
					String parentRef = data.getParentref();
					int domIID = data.getDomainRef();

					java.util.HashMap<String,Object> tmap = new java.util.HashMap<String,Object>();
					//��ʶ
					tmap.put("id", data.getInnerID());
					tmap.put("Name", text);
					//��ʾֵ
					tmap.put("text", text);
					tmap.put("EName", data.getId());
					//�Ƿ�չʾͼ��
					tmap.put("__viewicon", true);
					tmap.put("iconsrc",request.getContextPath()
							+ "/platform/images/org/organized_tree.gif");

					tmap.put("expanded", false);

					String openUrl = url_data + "&iid=" + data.getInnerID();
					//չ������
					tmap.put("openUrl", openUrl);
					//���ڵ�
					if (parentRef == null
							|| parentRef.trim().equalsIgnoreCase("")) {
						tmap.put("pid", "");
					} else {
						tmap.put("pid", parentRef);
					}
					dataList.add(tmap);
				}
			 	String xx = DataUtil.listToJsonSync(dataList);
				out.println(xx);
				return;
			}

			//�γ�json��
			//String str = DataUtil.listTreeJson(list,"");
			String str = DataUtil.encode(list);
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
			String xx = DataUtil.listToJson(count, jsonList);
			out.println(xx);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	else if("".equals(op) || null == op){
		Map<String, String> map = new HashMap<String, String>();
		List<Map<String, String>> jsonList = new ArrayList<Map<String, String>>();
		String xx = DataUtil.listToJson(jsonList);
		out.println(xx);
	}

%>