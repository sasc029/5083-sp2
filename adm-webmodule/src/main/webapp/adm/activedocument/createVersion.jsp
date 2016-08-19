<%@page import="com.bjsasc.plm.grid.spot.SpotUtil"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.bjsasc.adm.common.ConstUtil"%>
<%
	String path = request.getContextPath();
	String oid = request.getParameter("OID");
	Persistable obj = Helper.getPersistService().getObject(oid);
	String spot = ConstUtil.SPOT_LISTVERSIONBROWSE;
	String gridId = "myGrid";
	String url=path+"/adm/active/ActiveOrderHandle!getIteratedList.action?OID="+oid;
%>

<html>
<head>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
</head>
  
<body>
<form method="post">
<table class="pt-grid" id="partTreeGrid" url="<%=url%>" rownumbers="true" useFilter="false"			submitedit="submitedit" singleSelect="true" fit="true" checkbox="true">
	<jsp:include page="/plm/common/grid/control/grid_of_body.jsp">
		<jsp:param name="spot" value="<%=spot%>"/>
		<jsp:param name="gridId" value="<%=gridId%>"/>
	</jsp:include>
</table>
<table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">		
	<tr>
		<td>
            <div class="pt-formbutton"   id="savebtn" text="确定" onclick="doSelect()"></div>
     	     <div class="pt-formbutton" id="cancelbtn" text="取消"  onclick="cancel()"></div> 
        </td>
	 </tr>
</table>
</body>
<script type="text/javascript">
function doSelect() {
	var callBack = "<%=request.getParameter(KeyS.CALLBACK)%>";
	var grid = pt.ui.get("myGrid");
	var data=grid.getSelections();
	if(data==null|| data.length==0 ||  data.length>1){
		var tip={title:"提示信息ʾ",message:"请选择一条数据"};
		pt.ui.alert(tip);
		return;
	}
	opener.callBack(data);
	window.close();
}

function cancel() {
	window.close();
}
</script>
</html>
