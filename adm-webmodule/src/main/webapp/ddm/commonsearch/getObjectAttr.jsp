<%@page import="com.bjsasc.plm.common.search.advanced.ObjectAttrType"%>
<%@page import="com.bjsasc.plm.type.TypeServiceImpl"%>
<%@page import="com.bjsasc.plm.util.JsonUtil"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.bjsasc.plm.type.TypeJsonUtil"%>
<%@page import="com.bjsasc.plm.type.TypeS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.type.attr.Attr"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page language="java"%>
<%@page session="true"%>
<%@ page import="java.util.Map"%>
<%
	//�������
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	// ҳ��ʹ��UTF-8����
	request.setCharacterEncoding("UTF-8");
	request.setCharacterEncoding("UTF-8");
	//��ȡҳ�洫��Ĳ���	
	String param = request.getParameter("param");
	String callBack = request.getParameter(KeyS.CALLBACK);//�ص�����
	String typeId=request.getParameter("typeId");
	if(typeId.equals("all")){
		typeId="";
	}
%>
<html>
<head>
<title>��������</title>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
</head>
<body class="openWinPage">
    <jsp:include page="/plm/common/actionTitle.jsp">
		<jsp:param name="actionTitle" value="��������"/>
	</jsp:include>
	<table width="100%" height="450px" cellSpacing="0" cellPadding="0">
		<TR height="84%">
			<td valign="top" heigthr="90%">
				<table class="pt-grid" rownumbers="true" id="grid" checkbox="true"
					singleSelect="false" fit="true" autoLoad="false" useFilter="false"
					url="" pagination="false">
					<thead>
						<th field="NAME">��������</th>
					</thead>
				</table>
				
			</td>
		</TR>
      	<tr heigth="16%">
			<TD TD height="40px">
				<div id="submitSaveas_cmd" class="pt-formbutton" 
						text="ȷ��" 
						onclick="submitMe()"></div>
					<div id="cancelSaveas_cmd" class="pt-formbutton" 
						text="�ر�" 
						onclick="cancelMe()"></div>
			</TD>
		</tr>
	</table>
</body>
<script type="text/javascript">
	var grid = pt.ui.get("grid");
	jQuery(document).ready(function() {
		var objData = <%=new ObjectAttrType().getObjectAttr(typeId)%>;
		for ( var i = 0; i < objData.length; i++) {
			if(objData[i].ID!='FOLDER'&&objData[i].ID!='MODIFY'&&objData[i].ID!='MODIFYTIME'){
			grid.appendRow(objData[i]);
			}
		}
	});
	function submitMe() {
			griddata = grid.getSelections();
			var types = null, names = null,ids="";
			var retValue = null;
			if (griddata != null && griddata.length > 0) {
				for ( var i = 0; i < griddata.length; i++) {
					if (types != null) {
						ids   +=",";
						types += ",";
						names += ",";
					} else {
						types = "";
						names = "";
					}
					ids +=griddata[i].ID;
					types += griddata[i].TYPE;
					names += griddata[i].NAME;
				}
				retValue = {
					"IDS":ids,
					"TYPES" : types,
					"NAMES" : names
				};
			}
	
			//alert(griddata);
			<%if (callBack != null) {%>
				try {
					opener.<%=callBack%>(retValue); // �������ڻص�
				} catch (e) { 
					try {
						window.parent.dialogArguments.<%=callBack%>(retValue); // ģʽ���ڻص�
					} catch(e) {
					}
				} 
		 	<%}%> 
			window.close();
		}
		function cancelMe() {
			window.returnValue = null;
			window.close();
		}
</script>
</html>