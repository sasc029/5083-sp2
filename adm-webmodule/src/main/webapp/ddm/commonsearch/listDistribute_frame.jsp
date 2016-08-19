<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%
	String contextPath = request.getContextPath();
	String title = "综合查询";
	String conditionsUrl = contextPath + "/ddm/commonsearch/listDistributeConditions.jsp";
	String detailListUrl = contextPath + "/ddm/commonsearch/listDistributeAllObjects.jsp";
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
			splitVertical:false,					    //水平分割还是垂直分割,如果为true,就是垂直分割,也就是左右分割
			A:"tdTop",								    //左侧容器的id,必须
			B:"tdBottom",					 		    //右侧容器的id,必须
			closeableto:20,					  		    //自动隐藏的最小宽度或高度,保持默认即可
			slaveleft:"",								//左侧容器中grid或tree控件的id
			slaveright:"distributeCommonSearchtGrid",	//右侧容器中grid或tree控件的id
			retfunc:"plm.resizecontrol"});	 		    //回调函数,保持默认即可
	});
	</script>
</head>
<body>
	<table id="table" class="splittable" style="height:100%;width:100%;" cellspacing="0" cellpadding="0" border="0"> 
		<tr> 
			<td id="tdTop" valign="top" style="width:100%;"> 
				<iframe id="conditions" name="conditions" style="width:100%;height:70%"
					src="<%=conditionsUrl%>"></iframe>
			</td>
		</tr>
		<tr> 
			<td id="tdBottom">
				<iframe id="detailList" name="detailList" style="width:100%;height:100%;"
					src="<%=detailListUrl%>"></iframe>
			</td>
		</tr>
	</table>
</body>
</html>
