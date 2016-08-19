<%@page import="com.bjsasc.plm.url.Url"%>
<%@page session = "true"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page language="java" %>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@include file="/plm/plm.jsp" %>
<%
	String oid = request.getParameter("OID");
	if(oid==null){
		oid = (String)session.getAttribute("getOneOid");
	}
	String clickTab = request.getParameter("clickTab");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>编辑现行单据</title>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
</head>
<body class="openWinPage" style="height:100%;overflow-x:hidden;overflow-y:auto;">
<table  ui="form" cellSpacing="0" cellPadding="0" width="100%" border="0">
	<tr>
		<td height="570px">
			<div id="tab" class="pt-tabs"  fit="true" shareIframe="false" beforeSelect="beforeSelectTabs">
				<div id="tab3" title="编辑基本信息" src="editActiveOrderAttr.jsp"></div>
				<div id="tab4" title="编辑更改对象" src="editActiveOrdered.jsp"></div>
			</div>
		</td>	
	</tr>
</table>		
</body>
<script type="text/javascript">
var container = {};
$(document).ready(function(){
	var ckTab ="<%=clickTab %>";
	var tabObjName="编辑基本信息";
	if(ckTab =="tab1"){
		tabObjName="编辑基本信息";
	}else if(ckTab == "tab2"){
		tabObjName="编辑更改对象";
	}
	beforeSelectTabs(tabObjName);
});
function beforeSelectTabs(title){
	var tabs = pt.ui.get("tab");
	tabs.setParams(title,{'<%=KeyS.OID%>':'<%=oid%>'});
}
</script>
</html>
