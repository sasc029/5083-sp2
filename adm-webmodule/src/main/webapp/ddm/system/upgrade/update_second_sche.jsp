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
		throw new RuntimeException("初始化工具不存在:clazz="+clazz);
	}
	
	Inited toolInstance = tool.getInstance();
	if(toolInstance == null){
		throw new RuntimeException("初始化工具无法实例化:title="+tool.getTitle()+",clazz="+clazz);
	}
	
	//关闭权限检查
	boolean temp = SessionHelper.getService().isCheckPermission();
	SessionHelper.getService().setCheckPermission(false);
	
	System.out.println("\r\n正在【"+tool.getTitle()+"】......\r\ntool="+tool);
	
	//执行初始化
	toolInstance.init();
	
	System.out.println("\r\n初始化成功："+tool);
	
	//恢复权限检查
	SessionHelper.getService().setCheckPermission(temp);
	
	//输出反馈信息
	Map<String, String> result = new HashMap<String, String>();
	result.put(Ajax.SUCCESS, "true");
	result.put("CLAZZ", tool.getClazz());
	result.put("TITLE", tool.getTitle());

	String json = DataUtil.mapToSimpleJson(result);
	out.write(json);
%>