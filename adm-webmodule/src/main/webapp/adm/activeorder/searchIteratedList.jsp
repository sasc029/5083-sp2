<%@page import="com.bjsasc.plm.grid.spot.SpotUtil"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.adm.common.ConstUtil"%>
<%@include file="/adm/activepublic/activeError.jsp" %>
<%
	String path = request.getContextPath();
	String spot = ConstUtil.SPOT_LISTVERSIONBROWSE;
	String gridId = "searchgrid";
	String gridTitle="检索版本";
	String oid = (String) request.getParameter("OID");
	String gridUrl = path+"/adm/active/ActiveOrderHandle!getIteratedList.action?OID="+oid;
	String callBack = (String)request.getParameter("CALLBACK");
%>

<html>
<head>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
<title><%=gridTitle %></title>
</head>
<body  class="openWinPage" onload="">
<jsp:include page="/plm/common/actionTitle.jsp">
	<jsp:param name="actionTitle" value="<%=gridTitle%>"/>
</jsp:include>
<form method="POST" name="myForm">
<div style="width:100%;">
	<div class="AvidmMtop twoTitle" >
		<a id="a_1" class="showIcon" href="javascript:void(0)"><img src="<%=path %>/plm/images/common/space.gif"></a>
		<%=gridTitle%>
	</div>
</div>
	<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0"  height="80%">
	  <TBODY>
	     <TR>
	      <TD valign="top">
			<table height="100%" class="pt-grid" id="<%=gridId%>" url="<%=gridUrl%>" singleSelect="true" rownumbers="true" useFilter="false" submitedit="submitedit" fit="true" checkbox="true" width="100%" border="0" cellspacing="0" cellpadding="0" >
			<jsp:include page="/plm/common/grid/control/grid_of_body.jsp">
				<jsp:param name="spot" value="<%=spot%>"/>
				<jsp:param name="spotInstance" value="<%=spot%>"/>
				<jsp:param name="gridId" value="<%=gridId%>"/>		
				<jsp:param name="operate_container" value="container"/>
			</jsp:include>
			</table>
	      </TD>
	    </TR>
	  </TBODY>
	</TABLE>
<table width="100%" border="0" cellspacing="0" cellpadding="0">		
	<tr>
		<td>
			<div class="pt-formbutton" id="savebtn" text="确定" onclick="doSelect()"></div>
     		<div class="pt-formbutton" id="cancelbtn" text="取消"  onclick="cancel()"></div> 
		 </td>
	</tr>
</table>
</form>
</body>
<script type="text/javascript">
var container = {};
var bodyH = document.body.clientHeight;
function doSelect() {
	var grid = pt.ui.get("<%=gridId%>");
	var data=grid.getSelections();
	if(data==null || data.length==0 ||  data.length>1){
		var tip={title:"提示信息ʾ",message:"请选择一条数据"};
		pt.ui.alert(tip);
		return;
	}
	try {
		opener.<%=callBack%>(data); // 弹出窗口回调
		window.close();
	} catch (e) {
		try {
			window.parent.dialogArguments.<%=callBack%>(data); // 模式窗口回调
			window.close();
		} catch(e) {}
	} 

}
function cancel() {
	window.close();
}
</script>
</html>
