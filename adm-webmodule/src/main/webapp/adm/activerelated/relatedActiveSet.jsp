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
	String title = "相关现行单据";
	
	String data = request.getParameter(KeyS.DATA);
	List<Map<String,Object>> listMap = JsonUtil.toList(data);
	
	String oid = listMap.get(0).get(KeyS.OID).toString();

 	//准备数据
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
	<th field="TYPE" width="25" tips="类型" sortable='true' ></th>
	<th field="NUMBER" width="150" tips="编号" sortable='true' >编号</th>
	<th field="NAME" width="250" tips="名称" sortable='true' >名称</th>
	<th field="VERSION" width="50" tips="版本" sortable='true' >版本</th>
	<th field="LIFECYCLE_STATE" width="100" tips="状态" sortable='true' >状态</th>
	<th field="NOTE" width="250" tips="备注" sortable='true' >备注</th>
	<th field="DATASOURCE" width="100" tips="来源" sortable='true' >来源</th>
	<th field="SECLEVEL" width="100" tips="密级" sortable='true' >密级</th>
	<th field="FOLDER" width="230" tips="位置" sortable='true' >位置</th>
	<th field="CONTEXT" width="100" tips="上下文" sortable='true' >上下文</th>
	<th field="DOMAIN" width="100" tips="域" sortable='true' >域</th>
	<th field="LIFECYCLE_TEMPLATE" width="100" tips="生命周期模板" sortable='true' >生命周期模板</th>
	<th field="MODIFIER" width="100" tips="修改者" sortable='true' >修改者</th>
	<th field="MODIFY_TIME" width="140" tips="上次修改时间" sortable='true' >上次修改时间</th>
	<th field="CREATOR" width="100" tips="创建者" sortable='true' >创建者</th>
	<th field="CREATE_TIME" width="140" tips="创建时间" sortable='true' >创建时间</th>
	<th field="UPDATE_TIME" width="100" tips="物理更新时间" sortable='true' >物理更新时间</th>
			</thead>
		</table>
	</td></tr></tbody></table>
</body>
<script type="text/javascript">
	var container = {};
</script>
</html>