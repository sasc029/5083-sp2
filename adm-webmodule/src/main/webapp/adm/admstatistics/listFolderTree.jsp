<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.core.folder.Cabinet"%>
<%@page import="com.bjsasc.plm.core.Helper"%>
<%@page import="com.bjsasc.plm.core.folder.Folder"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@include file="/plm/plm.jsp" %>
<%
	String contextPath=request.getContextPath();
	String oid = request.getParameter(KeyS.OID);
	String singleSelect = request.getParameter("singleSelect");
	if (singleSelect == null || singleSelect.length() == 0) {
		singleSelect = "true";
	}
	String folderStr = "{OID:'"+oid+"'}";
	String url = contextPath + "/plm/folder/visitFolder_get.jsp?OID="+oid;
	
%>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              

<head>  
<title>请选择文件夹</title>  
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>	
<link rel="stylesheet" type="text/css" href="/platform/plm/css/plm.css">
<script type="text/javascript"	src="<%=contextPath%>/plm/javascript/uisplitter.js" charset="UTF-8"></script>
<script type="text/javascript"	src="<%=contextPath%>/plm/javascript/visitFolder.js" charset="GBK"></script>

</head>

<body scroll="no">
<form method="post" name="myform" id="myform" ui='form'>
<table id="table" height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0" style="margin:0px;">
<tr class="AvidmActionTitle">
	<td>
		<jsp:include page="/plm/common/actionTitle.jsp">
			<jsp:param name="ACTIONID" value="com.bjsasc.adm.type.select"/>
		</jsp:include>
	</td>
</tr>
<tr height=100%>
<td>	
	<table id="table" height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0" style="margin:0;" scrolling="yes" >
		<tr>
			<td>
				<table class="pt-grid" url="<%=url%>" treeColumn="Name" id="folderTree" fit="true" singleSelect="<%=singleSelect%>" checkbox="true" scrolling="yes"  >
					<thead>
						<th field="Name" width="200">文件夹</th>
					</thead>
				</table>		
			</td>
		</tr>
		<tr class="AvidmDecision">
		<td>
			<div class="pt-formbutton" text="确定" id="submitbutton" onclick="onOk();"></div>
			<div class="pt-formbutton" text="取消" id="closebutton" onclick="window.close();"></div>		
		</td>
		</tr>
	</table>
</td>
</tr>
</table>
<script type="text/javascript">
var folder = <%=folderStr%>;
	
	function onLoad(){
		//var tree = pt.ui.get("folderTree");
		//使目标节点处于选中状态
		//var node = tree.find(folder);
		//tree.selectRow(tree.getRowIndex(node));	

	}
	
	//响应用户
	function onOk(){
		var data = folderTree.getSelections();
		if (data == null || data.length == 0) {
			plm. showMessage({title:'提示', message:"请至少选择1个操作对象!", icon:'3'});
			return;
		}
		var obj = pt.ui.JSON.encode(data);
		
		var oids = "";
		var names = "";
		for(var i=0;i<data.length;i++){
			oids += data[i].OID + ",";
			names += data[i].Name + ",";
		}
		oids = oids.substring(0, oids.length - 1);
		names = names.substring(0, names.length - 1);
		var returnObj = {
				oids:oids ,
				names:names
			};
		window.returnValue = returnObj;
		window.close();
	}
	</script>
</body>
</html>
