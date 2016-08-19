<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.plm.util.Ajax"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.bjsasc.plm.core.system.init.Inited"%>
<%@page import="com.bjsasc.plm.core.system.init.InitTool"%>
<%@page import="com.bjsasc.plm.core.system.init.InitToolUtil"%>
<%@page import="com.bjsasc.plm.core.system.init.UpgradeToolUtil"%>
<%@page import="com.bjsasc.plm.core.session.SessionHelper"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page session="true"%>
<%@page errorPage="/plm/ajaxError.jsp"%>

<%
     String clazz = "com.bjsasc.ddm.common.DdmInitTool";
	InitTool tool = UpgradeToolUtil.getInitTool(clazz);
	if(tool == null){		
		throw new RuntimeException("��ʼ�����߲�����:clazz="+clazz);
	}
	
	Inited toolInstance = tool.getInstance();
	if(toolInstance == null){
		throw new RuntimeException("��ʼ�������޷�ʵ����:title="+tool.getTitle()+",clazz="+clazz);
	}
	
	//�ر�Ȩ�޼��
	boolean temp = SessionHelper.getService().isCheckPermission();
	SessionHelper.getService().setCheckPermission(false);
	
	System.out.println("\r\n���ڡ�"+tool.getTitle()+"��......\r\ntool="+tool);
	
	//ִ�г�ʼ��
	toolInstance.init();
	
	System.out.println("\r\n��ʼ���ɹ���"+tool);
	
	//�ָ�Ȩ�޼��
	SessionHelper.getService().setCheckPermission(temp);
	
	//���������Ϣ
	Map<String, String> result = new HashMap<String, String>();
	result.put(Ajax.SUCCESS, "true");
	result.put("CLAZZ", tool.getClazz());
	result.put("TITLE", tool.getTitle());

	String json = DataUtil.mapToSimpleJson(result);
	out.write(json);
%>