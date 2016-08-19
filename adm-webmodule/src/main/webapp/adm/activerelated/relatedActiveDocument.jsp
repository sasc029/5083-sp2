<%@page contentType = "text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page language = "java"%>
<%@page session = "true"%>
<%@page import="java.util.*" %>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.util.JsonUtil"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.core.type.TypeHelper"%>
<%@page import="com.bjsasc.plm.core.folder.Folder"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@include file="/plm/plm.jsp" %>
<%
	String contextPath = request.getContextPath();
	String gridId = "relationActiveDocumentGrid";
	String title = "选择现行文件编号";
	
	String oid = request.getParameter(KeyS.OID);
	String ads = request.getParameter("ads");
 	String spot =  "RelatedObject_ActiveDocument";
	if (oid != null && oid.length() > 0) {
	 	//准备数据
	 	Map<String,Object> params = new HashMap<String, Object>();
	 	params.put("OID", oid);
	 	GridDataUtil.prepareRowQueryParams(params,  spot);
	} else if (ads != null && ads.length() > 0) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<String> numberList = new ArrayList<String>();
		String[] adarr = ads.split(",");
		for (String activeOid : adarr) {
			if (activeOid == null || activeOid.length() == 0) {
				continue;
			}
			Persistable object = Helper.getPersistService().getObject(activeOid);
			Map<String, Object> orderMap = Helper.getTypeManager().format(object);
			Object sObject = orderMap.get("SOURCE");
			Map sMap = (Map) sObject;
			Object number = sMap.get("NUMBER");
			if (number != null && !numberList.contains(number.toString())) {
				numberList.add(number.toString());
				list.add(orderMap);
			}
		}
		GridDataUtil.prepareRowObjects(list, spot);
	} else {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		GridDataUtil.prepareRowObjects(list, spot);
	}
 %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><%= title %></title>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" scroll="no">
	<table height="90%" cellSpacing="0" cellPadding="0" width="100%" border="0"><tbody><tr><td valign="top">
		<table class="pt-grid" id="<%=gridId %>" autoLoad="true" 
			url="<%=contextPath%>/plm/common/grid/control/data_get.jsp?spot=<%=spot%>" rownumbers="true" 
			pagination="false" useFilter="false" singleSelect="true" fit="true" checkbox="true" >
			<thead>
	<th field="TYPE" width="25" tips="类型" sortable='true' ></th>
	<th field="NUMBER" width="150" tips="编号" sortable='true' >编号</th>
	<th field="NAME" width="250" tips="名称" sortable='true' >名称</th>
			</thead>
		</table>
	</td></tr></tbody></table>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">		
		<tr>      
			<td align="center">
			<div class="pt-formbutton"   id="savebtn" text="确定" onclick="submitForm()"></div>
			<div class="pt-formbutton" text="取消"  onclick="cancle();"></div>
			</td>
	    </tr>
	</table>
</body>
<script type="text/javascript">
	var container = {};
	function submitForm(){
		var gridObj = pt.ui.get("<%=gridId %>");
		var selections = gridObj.getSelections();
		if(selections.length != 1){
			alert("请选择一条数据。");
			return false;
		}
		var returnJson =  pt.ui.JSON.encode(selections);		
		window.returnValue = returnJson;
		window.close();
	}	
	// 取消
	function cancle(){
		window.close();
	}
</script>
</html>