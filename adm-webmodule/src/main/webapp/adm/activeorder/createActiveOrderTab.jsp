<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page session = "true"%>
<%@page language="java" %>
<%@page import="java.util.*"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@include file="/plm/plm.jsp" %>
<%
	//�ļ���OID
	String folderOid = request.getParameter("FOLDER_OID");
	String data = request.getParameter(KeyS.DATA);
	List list = null;
	if (folderOid == null || folderOid.length() == 0 ) {
		try {
			list = DataUtil.JsonToList(data);
			if (list != null && list.size() > 0) {
				Map map = (Map)list.get(0);
				folderOid = (String)map.get("FOLDER_OID");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>�½����е���</title>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
</head>
<body class="openWinPage" style="height:100%;overflow-x:hidden;overflow-y:auto;">
<table  ui="form" cellSpacing="0" cellPadding="0" width="100%" border="0">
	<tr>
		<td height="570px">
			<div id="tab" class="pt-tabs"  fit="true" shareIframe="false" beforeSelect="beforeSelectTabs">
				<div id="tab1" title="������Ϣ" src="addActiveOrderAttr.jsp"></div>
				<div id="tab2" title="���Ķ���" src="addActiveOrdered.jsp"></div>
			</div>
		</td>	
	</tr>
</table>		
</body>
<script type="text/javascript">
var container = {};
	$().ready(function initTab(){
		var tabs = pt.ui.get("tab");
		tabs.selectTab("���Ķ���");
		tabs.selectTab("������Ϣ");
	});
function beforeSelectTabs(title){
	var tabs = pt.ui.get("tab");
	tabs.setParams("������Ϣ",{'<%=KeyS.FOLDER_OID%>':'<%=folderOid%>'});
}
function getData(){
	return <%=data%>;
}
</script>
</html>
