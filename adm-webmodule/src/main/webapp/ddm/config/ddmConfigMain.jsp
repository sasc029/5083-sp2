<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>

<%@include file="/plm/plm.jsp" %>
<%
	String contextPath = request.getContextPath();
	String oid = RequestUtil.getParamOid(request);
	String gridId = "ddmConfigTree";
	String title = "发放管理配置";
	String treeUrl = contextPath + "/ddm/config/ddmConfigAction.jsp?op=getRoot&contextOid="+oid;
	String contentUrl = contextPath + "/ddm/config/objecttype/ddmObjectTypeList.jsp?contextOid="+oid;
%>
<html>
<head>
	<title><%=title%></title>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>	
	<script type="text/javascript"	src="<%=contextPath%>/plm/javascript/uisplitter.js" charset="UTF-8"></script>
	
	<script type="text/javascript">
		var container = {OID:'<%=oid%>'};
		//增加splitbar
		$(document).ready(function() {
			$("#table").splitter({
				splitVertical:true,				    //水平分割还是垂直分割,如果为true,就是垂直分割,也就是左右分割
				A:"tdLeft",						    //左侧容器的id,必须
				B:"tdRight",					    //右侧容器的id,必须
				closeableto:20,					    //自动隐藏的最小宽度或高度,保持默认即可
				slaveleft:"<%=gridId%>",	        //左侧容器中grid或tree控件的id
				slaveright:"distributeContent",	    //右侧容器中grid或tree控件的id
				retfunc:"plm.resizecontrol"});	    //回调函数,保持默认即可
		});

		//响应用户
		function onClickRow(nodes){
			var tree = pt.ui.get("<%=gridId%>");
			var node = tree.getSelected();
			var link = node["link"];
			showContent(link);
		}
		
		//在右边iframe内显示内容页面
		function showContent(link){
			var contentView = document.getElementById("rightContent");
			//暂时把内容清空，以中断之前的请求
			contentView.contentWindow.location.href="about:blank";
			contentView.contentWindow.location.href = link;
		}
		function onLoad(){
		}
	</script>
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >

<form name="grid_assetplace" method="POST">
	<table width="100%" height="100%" cellSpacing="0" cellPadding="0" border="0">
		<% if (oid!=null) { %>
		<tr class="AvidmNavigator">
			<td>
				<jsp:include page="/plm/common/navigator.jsp">
					<jsp:param name="FLAG" value="NAVIGATE_TO_CONTEXT"/>
					<jsp:param name="OID" value="<%=oid%>"/>
				</jsp:include>
			</td>
		</tr>
		<% } %>
		<jsp:include page="/plm/common/grid/control/grid_of_title.jsp">
			<jsp:param name="gridTitle" value="<%=title%>"/>
		</jsp:include>
		<tr height="100%"><td>
			<table width="100%" height="100%" cellSpacing="0" cellPadding="0" border="0">
			<tr> 
			<td id="tdLeft" class="leftpanel" style="width:20%;">
				<table width="100%" class="pt-tree" url="<%=treeUrl%>" treeColumn="NAME" autoColumns="true"
						id="<%=gridId%>" fit="true" singleSelect="false" onLoadSuccess="onLoad"
						onDblClickRow="onClickRow" onClickRow="onClickRow" scrolling="yes">
					<thead><th field="NAME"></th></thead>
				</table>
			</td>
			<td id="tdRight">
				<iframe id="rightContent" name="rightContent" style="width:100%;height:100%; "
					src="<%=contentUrl%>"></iframe>
			</td>
			</tr>
			</table>
		</td></tr>
	</table>
</body>
</form>
</html>
