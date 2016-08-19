<%@page import="com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder"%>
<%@page import="com.bjsasc.plm.KeyS" %>
<%@page import="com.bjsasc.plm.core.Helper"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%
	
	String objName = request.getParameter("objName");
	
	String objIID = request.getParameter(KeyS.OID);
	
	String objectURL = request.getParameter("objectURL");
	
	String actionUrl = request.getContextPath()
			+ "/platform/workflow/instance/CheckIfObjHasInstanceEdo.jsp";
	
	String isMultiObj = "0";

	// 这个参数需要设置，在根域-->配置中心-->参数设置-->底层配置信息集合-->工作流配置中心-->流程模板注入中配置的参数，这个参数指定的对应的类
	String module = "approve";
	
	String callbackURL = "";
	
	String productIID = "";
	
	String mainType = "";
	
	String subType = "";
	
	String items = "";
	
	String callBackFunc = null;
%>

<script type="text/javascript">
</script>
<html>
<body>
	<form name="wftestForm" id="wftestForm" action="<%=actionUrl%>" method="post">
		<input type="hidden" name="ObjectInnerID" value="<%=objIID%>"/>
		<input type="hidden" name="MainType" value="<%=mainType%>"/>
		<input type="hidden" name="ObjectName" value="<%=objName%>"/>
		<input type="hidden" name="SubType" value="<%=subType%>"/>
		<input type="hidden" name="Module" value="<%=module%>"/>
		<input type="hidden" name="productIID" value="<%=productIID%>"/>
		<input type="hidden" name="ObjectURL" value="<%=objectURL%>"/>
		<input type="hidden" name="CallbackURL" value="<%=callbackURL%>"/>
		<input type="hidden" name="isMultiObj" value="<%=isMultiObj%>"/>
		<input type="hidden" name="items" value="<%=items%>"/>
		<input type="hidden" name="callBackFunc" value="<%=callBackFunc%>"/>
	</form>
</body>
<script type="text/javascript">
	document.getElementById("wftestForm").submit();
</script>
</html>