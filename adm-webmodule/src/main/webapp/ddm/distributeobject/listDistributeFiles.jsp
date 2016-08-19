<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.util.JsonUtil"%>
<%@page import="com.bjsasc.plm.core.attachment.AttachHelper"%>
<%@page import="com.bjsasc.plm.core.attachment.FileHolder"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.core.Helper"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%@page import="com.bjsasc.platform.filecomponent.model.PtFileStorageObj"%>
<%@page import="com.bjsasc.platform.filecomponent.util.PtFileCptServiceProvider"%>
<%@page import="com.bjsasc.platform.filecomponent.service.PtStorageService"%>
<%@page import="java.net.URLEncoder"%>
<%
	String contextPath = request.getContextPath();
	String oid = request.getParameter(KeyS.OID);
    String spot = "ListDistributeTaskPropertys";
    String gridId = "DistributeObject";
    String gridTitle = "分发数据";
	String gridUrl = contextPath + "/ddm/distribute/distributePaperTaskHandle!listDistributeObjectFiles.action?OID="+oid;
	/* String url_base = contextPath+"/plm/docman/action/FileMgrAction.jsp?refId="+"71fe718aae4dbdb20c313efc0d397e72"+"&refType="+"Document";
 	String gridUrl = url_base+"&op=getFileInfoList"; */
	
	//String localUrl ="http://"+request.getServerName()+":"+request.getLocalPort()+contextPath;
 	String localUrl = "http://"+request.getServerName()+":"+ request.getLocalPort() + request.getContextPath();
%>
<html>
	<head>
		<title><%=gridTitle%></title>
		<script type="text/javascript"	src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
		<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
	</head>
	<body>
	<table cellSpacing="0" cellPadding="0" width="100%" height="100%" border="0" scroll="no">
		<tr><td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				 <tr>
			    	<td class="AvidmTitleName">
			    		<div class="imgdiv"><img src="<%=request.getContextPath() %>/plm/images/common/modify.gif"/></div>
						<div class="namediv">文档文件列表</div></td>
				 </tr>
			</table>
		</td></tr>
		<tr><td>
			<form id="mainForm" name="mainForm" method="POST">
			<input type="hidden" name="downloadUrl" id="downloadUrl">
				<%-- <table  class="pt-grid" id="grid_fileList" width="100%" submitedit="fun1" checkbox="false" fit="true" singleSelect="false" rownumbers="true"  url="<%=gridUrl%>" pagination="false" onLoadSuccess="havingMainFile()"> --%>
		        <%-- <table id="<%=gridId%>" class="pt-grid" singleSelect="false" checkbox="false" url="<%=gridUrl%>"
						fit="true" width="100%" pagination="false" rownumbers='true'> --%>
					<table  height="575" class="pt-grid" id="<%=gridId%>" width="100%" heigth="500" submitedit="fun1" checkbox="false" fit="true" singleSelect="false" rownumbers="true"  url="<%=gridUrl%>" pagination="false">
		         	<thead>
		         		 <tr>
							<!-- <th  field="FILENAME"	width="200">文件名称</th>
							<th  field="FILEEXTENDNAME"  		width="150">文件类型</th>
							<th  field="FILESIZE"  	width="150" >文件大小</th>
							<th  field="MAINFILE"  		width="150">主文件</th>
				  			<th  field="OPERATE"   link="openFile"  width="150">操作</th> -->
				  			<th  field="FILENAME"	width="200">文件名称</th>
							<th  field="FILEEXTENDNAME"  		width="150">文件类型</th>
							<th  field="FILESIZE"  	width="150" >文件大小</th>
							<th  field="MAINFILE"  		width="150">主文件</th>
				  			<th  field="OPERATE"   link="openFile"  width="150">操作</th> 
						</tr>
					</thead>
				</table>
			</form>
		</td></tr>
	</table>
	</body>
</html>
<script type="text/javascript">
	function openFile(){
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		var innerId = data[0].INNERID;
		var refId = data[0].REFID;
		var refType = data[0].REFTYPE;
		var fileName = data[0].FILENAME;
		var downloadUrl = ""+"<%=localUrl%>/PLMFileDownloadServlet?refType="+refType+"&refId="+refId+"&itemId="+innerId;
		downloadUrl = encodeURIComponent(downloadUrl);
		fileName = encodeURI(encodeURI(fileName));
		var actionPath = "<%=contextPath%>/ddm/common/openFileByDocControl.jsp?downloadURL="+downloadUrl + "&fileName="+fileName;
		window.showModalDialog(actionPath,window,"dialogWidth=400px;dialogHeight=250px;resizable=yes;center=1");
	}
</script>