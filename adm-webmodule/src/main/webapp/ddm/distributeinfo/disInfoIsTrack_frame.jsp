<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%
	String contextPath = request.getContextPath();
	String title = "修改是否跟踪信息";
	String treeUrl = contextPath + "/ddm/distributeinfo/disInfoIstrack_tree.jsp";
	String contentUrl = contextPath + "/ddm/distributeinfo/disInfoIsTrack_content_edit.jsp";
	
	//session.removeAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
%>
<html>
<head>
	<title><%=title%></title>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
	<script type="text/javascript" src="<%=Url.RPMJS%>" charset="GBK"></script>
	<script type="text/javascript"	src="<%=contextPath%>/plm/javascript/uisplitter.js" charset="UTF-8"></script>
	<script type="text/javascript">
	//增加splitbar
	$(document).ready(function() {
		$("#table").splitter({
			splitVertical:true,				    //水平分割还是垂直分割,如果为true,就是垂直分割,也就是左右分割
			A:"tdLeft",						    //左侧容器的id,必须
			B:"tdRight",					    //右侧容器的id,必须
			closeableto:20,					    //自动隐藏的最小宽度或高度,保持默认即可
			slaveleft:"disObjectTree",	//左侧容器中grid或tree控件的id
			slaveright:"disInfoContent",	//右侧容器中grid或tree控件的id
			retfunc:"plm.resizecontrol"});	    //回调函数,保持默认即可
	});
	</script>
</head>
<body>
	<table id="table" class="splittable" style="height:100%;width:100%;" cellspacing="0" cellpadding="0" border="0"> 
		<tr> 
			<td id="tdLeft" class="leftpanel" style="width:22%;"> 
				<iframe id="leftTree" name="leftTree" style="width:100%;height:99.9%;"
					src="<%=treeUrl%>"></iframe>
			</td>
			<td id="tdRight">
				<iframe id="rightContent" name="rightContent" style="width:100%;height:100%; "
					src=""></iframe>
			</td>
		</tr>
	</table>
</body>
</html>
