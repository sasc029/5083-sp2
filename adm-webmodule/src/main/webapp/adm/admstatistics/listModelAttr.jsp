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
	String oids = request.getParameter("oids");
	String url_getClassAttrList = contextPath + "/adm/active/ActiveStatisticsHandle!getClassAttrList.action?OIDS="+oids;
	
%>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              

<head>  
<title>��ѡ������</title>  
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
			<jsp:param name="ACTIONID" value="com.bjsasc.adm.attr.select"/>
		</jsp:include>
	</td>
</tr>
<tr height=100%>
<td>	
	<table id="table" height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0" style="margin:0;" scrolling="yes" >
		<tr>
			<td>
				<table class="pt-grid" id="grid_classAttr" singleSelect="false"
						fit="true" autoLoad="true" checkbox="true" useFilter="false" url="<%=url_getClassAttrList %>" pagination="false">
						<thead>
							<th field="ATTRNAME" width="100">����</th>						
						</thead>
				</table>		
			</td>
		</tr>
		<tr class="AvidmDecision">
		<td>
			<div class="pt-formbutton" text="ȷ��" id="submitbutton" onclick="onOk();"></div>
			<div class="pt-formbutton" text="ȡ��" id="closebutton" onclick="window.close();"></div>		
		</td>
		</tr>
	</table>
</td>
</tr>
</table>
<script type="text/javascript">
	function onOk(){
		var data = grid_classAttr.getSelections();
		if (data == null || data.length == 0) {
			plm. showMessage({title:'��ʾ', message:"������ѡ��1����������!", icon:'3'});
			return;
		}
		var obj = pt.ui.JSON.encode(data);
		var oids = "";
		var attrIds = "";
		var attrNames = "";
		var dataTypes = "";
		for(var i=0;i<data.length;i++){
			oids += data[i].CLASSID + ":" + data[i].INNERID + ",";
			attrIds += data[i].ID + ",";
			attrNames += data[i].ATTRNAME + ",";
			dataTypes += data[i].DATATYPE + ",";
		}
		oids = oids.substring(0, oids.length - 1);
		attrIds = attrIds.substring(0, attrIds.length - 1);
		attrNames = attrNames.substring(0, attrNames.length - 1);
		dataTypes = dataTypes.substring(0, dataTypes.length - 1);
		var returnObj = {
			oids:oids ,
			attrIds:attrIds ,
			attrNames:attrNames,
			dataTypes:dataTypes
		};
		window.returnValue = returnObj;
		window.close();
	}
</script>
</body>
</html>
