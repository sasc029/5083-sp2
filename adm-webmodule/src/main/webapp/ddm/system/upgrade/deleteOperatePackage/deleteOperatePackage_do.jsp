
<%@page import="com.bjsasc.plm.core.system.access.operate.register.OperateRegisterUtil"%>
<%@page import="com.bjsasc.plm.core.system.access.operate.OperatePackageUtil"%>
<%@page import="com.bjsasc.plm.core.system.access.operate.OperatePackage"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.plm.core.Helper" %>
<%@page import="java.util.List" %>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%
	Map<String,Object> result = new HashMap<String,Object>();
	
	String typeRef = request.getParameter("typeRef");
	if(typeRef != null && typeRef != ""){
		//�жϸò������Ƿ��ѱ������
		OperatePackage operatePackageExiting = OperatePackageUtil.allOperatePackages.get(typeRef);
		if(operatePackageExiting != null){
			OperateRegisterUtil.deleteOperatePackage(operatePackageExiting);
			result.put("SUCCESS", "true");
		}else{
			result.put("FALSE", "�����������ڣ�"+typeRef);
		}
	}else{
		result.put("FALSE", "������Ҫɾ����ģ��ID");
	}
	out.print(DataUtil.mapToSimpleJson(result));	
%>