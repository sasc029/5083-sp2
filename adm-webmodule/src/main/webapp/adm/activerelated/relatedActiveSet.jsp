<%@page contentType = "text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page language = "java"%>
<%@page session = "true"%>
<%@page import="java.util.*" %>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.util.JsonUtil"%>
<%@page import="com.bjsasc.plm.core.type.TypeHelper"%>
<%@page import="com.bjsasc.plm.core.folder.Folder"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@include file="/plm/plm.jsp" %>
<%
	String gridId = "relationActiveOrderGrid";
	String title = "������е���";
	
	String data = request.getParameter(KeyS.DATA);
	List<Map<String,Object>> listMap = JsonUtil.toList(data);
	
	String oid = listMap.get(0).get(KeyS.OID).toString();

 	//׼������
 	String spot =  "ActiveDocument_RelatedObject_ActiveSet";
 	Map<String,Object> params = new HashMap<String, Object>();
 	params.put("OID", oid);
 	GridDataUtil.prepareRowQueryParams(params,  spot);
 %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><%= title %></title>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" scroll="no">
	<table height="100%" cellSpacing="0" cellPadding="0" width="100%" border="0"><tbody><tr><td valign="top">
		<table class="pt-grid" id="<%=gridId %>" autoLoad="true" 
			url="<%=Url.APP %>/plm/common/grid/control/data_get.jsp?spot=<%=spot%>" rownumbers="true" 
			pagination="false" useFilter="false" singleSelect="false" fit="true" checkbox="true" >
			<thead>
	<th field="TYPE" width="25" tips="����" sortable='true' ></th>
	<th field="NUMBER" width="150" tips="���" sortable='true' >���</th>
	<th field="NAME" width="250" tips="����" sortable='true' >����</th>
	<th field="VERSION" width="50" tips="�汾" sortable='true' >�汾</th>
	<th field="LIFECYCLE_STATE" width="100" tips="״̬" sortable='true' >״̬</th>
	<th field="NOTE" width="250" tips="��ע" sortable='true' >��ע</th>
	<th field="DATASOURCE" width="100" tips="��Դ" sortable='true' >��Դ</th>
	<th field="SECLEVEL" width="100" tips="�ܼ�" sortable='true' >�ܼ�</th>
	<th field="FOLDER" width="230" tips="λ��" sortable='true' >λ��</th>
	<th field="CONTEXT" width="100" tips="������" sortable='true' >������</th>
	<th field="DOMAIN" width="100" tips="��" sortable='true' >��</th>
	<th field="LIFECYCLE_TEMPLATE" width="100" tips="��������ģ��" sortable='true' >��������ģ��</th>
	<th field="MODIFIER" width="100" tips="�޸���" sortable='true' >�޸���</th>
	<th field="MODIFY_TIME" width="140" tips="�ϴ��޸�ʱ��" sortable='true' >�ϴ��޸�ʱ��</th>
	<th field="CREATOR" width="100" tips="������" sortable='true' >������</th>
	<th field="CREATE_TIME" width="140" tips="����ʱ��" sortable='true' >����ʱ��</th>
	<th field="UPDATE_TIME" width="100" tips="�������ʱ��" sortable='true' >�������ʱ��</th>
			</thead>
		</table>
	</td></tr></tbody></table>
</body>
<script type="text/javascript">
	var container = {};
</script>
</html>