<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>

<%@page import="com.bjsasc.plm.core.*"%>
<%@page import="com.bjsasc.plm.core.context.*"%>
<%@page import="com.bjsasc.plm.core.context.team.*"%>
<%@page import="com.bjsasc.plm.core.context.util.*"%>
<%@page import="com.bjsasc.plm.core.system.application.*"%>
<%@page import="com.bjsasc.adm.active.model.activecontext.*"%>
<%@page import="com.bjsasc.adm.common.ActiveInitParameter"%>


<%@page import="com.bjsasc.plm.core.session.*"%>
<%@page import="com.bjsasc.plm.core.domain.*"%>
<%@page import="com.bjsasc.plm.core.context.*"%>

<%
	try {
		out.println("ע��Ӧ����˵�>>>>>  ��ʼ");

		//RootContext parentContext = ContextHelper.getService().getRootContext();

		// �����������ݹ���Ա��ɫ
		//RoleUtil.createRoles(parentContext);
		new ApplicationInitTool().init();
		out.println("<br>");
		out.println("ע��Ӧ����˵�>>>>>  �ɹ�");
	} catch (Exception ex) {
		out.println("<br>");
		out.println("ע��Ӧ����˵�>>>>>  ʧ��");
		ex.printStackTrace();
	}
%>
