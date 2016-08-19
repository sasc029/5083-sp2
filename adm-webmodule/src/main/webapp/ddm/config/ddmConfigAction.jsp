<%@page import="java.util.*"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.plm.ui.tree.TreeNode"%>
<%@page import="com.bjsasc.plm.core.Helper"%>
<%@page import="com.bjsasc.plm.core.context.model.RootContext"%>

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
	if (op.equals("getRoot")) {
		List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
		
		/** 
		 * ������β˵�
		 * 1, ���ĵ���������
		 */
		String url = contextPath + "/ddm/config/objecttype/ddmObjectTypeList.jsp?contextOid=" + contextOid;
		Map node = new HashMap();
		node.put("NAME", "��������������ҵ����Ա");
		node.put(TreeNode.KEY_ICONSRC, contextPath + "/plm/images/common/type.gif");
		node.put(TreeNode.KEY_VIEWICON, true);
		node.put(TreeNode.KEY_EXPANDED, true);
		node.put(TreeNode.KEY_CHILDURL, "");
		node.put(TreeNode.KEY_LINK, url);
		nodeList.add(node);
		/** 
		 * ������β˵�
		 * 2, �������������ò��Զ�����
		 */
		
		url = contextPath + "/ddm/config/objecttype/ddmObjectNotAutoCreate.jsp?contextOid=" + contextOid;
		node = new HashMap();
		node.put("NAME", "�������������ò��Զ�����");
		node.put(TreeNode.KEY_ICONSRC, contextPath + "/plm/images/common/type.gif");
		node.put(TreeNode.KEY_VIEWICON, true);
		node.put(TreeNode.KEY_EXPANDED, true);
		node.put(TreeNode.KEY_CHILDURL, "");
		node.put(TreeNode.KEY_LINK, url);
		nodeList.add(node);
		
		Object object = Helper.getPersistService().getObject(contextOid);
		if (object instanceof RootContext) {
			url = contextPath + "/ddm/config/distributeinfoconfig/distributeInfoConfigList.jsp?contextOid=" + contextOid;
			node = new HashMap();
			node.put("NAME", "Ĭ�Ϸַ���Ϣ����");
			node.put(TreeNode.KEY_ICONSRC, contextPath + "/plm/images/common/type.gif");
			node.put(TreeNode.KEY_VIEWICON, true);
			node.put(TreeNode.KEY_EXPANDED, true);
			node.put(TreeNode.KEY_CHILDURL, "");
			node.put(TreeNode.KEY_LINK, url);
			nodeList.add(node);
			
			/** 
			 *  ���Ź���ͨ������
			 */
			url = contextPath + "/ddm/config/distributecommonconfig/distributeCommonConfig.jsp?";
			node = new HashMap();
			node.put("NAME", "���Ź���ͨ������");
			node.put(TreeNode.KEY_ICONSRC, contextPath + "/plm/images/common/type.gif");
			node.put(TreeNode.KEY_VIEWICON, true);
			node.put(TreeNode.KEY_EXPANDED, true);
			node.put(TreeNode.KEY_CHILDURL, "");
			node.put(TreeNode.KEY_LINK, url);
			nodeList.add(node);

		}
		// ��ʽ����List -> JSON��
		results = DataUtil.encode(nodeList);
	}
	out.print(results);
%>