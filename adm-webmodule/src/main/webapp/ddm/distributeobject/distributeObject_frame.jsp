<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%
	String contextPath = request.getContextPath();
	String oid = request.getParameter("OID");
	String title = "业务对象挂接";
	String treeUrl = "listDistributePartSearch.jsp";
	String contentUrl="listDistributeDataObjects.jsp?oid="+oid;
	//String contentUrl = contextPath + "/ddm/distribute/distributeObjectHandle!getAllDistributeDataObject.action?OID=" + oid;

	String url = contextPath + "/ddm/distribute/distributeObjectHandle!addDistributeDataObjectLink.action";
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
			slaveleft:"distributeObjectTree",	//左侧容器中grid或tree控件的id
			slaveright:"distributeInfoContent",	//右侧容器中grid或tree控件的id
			retfunc:"plm.resizecontrol"});	    //回调函数,保持默认即可
	});
	</script>
</head>
<body>
	<table id="table" class="splittable" style="height:95%;width:100%;" cellspacing="0" cellpadding="0" border="0"> 
		<tr> 
			<td id="tdLeft" class="leftpanel" style="width:40%;"> 
				<iframe id="leftTree" name="leftTree" style="width:100%;height:100%;"
					src="<%=treeUrl%>"></iframe>
			</td>
			<td id="tdRight">
				<iframe id="rightContent" name="rightContent" style="width:100%;height:100%; "
					src="<%=contentUrl%>"></iframe>
			</td>
		</tr>
	</table>
	<table id="Articulate" class="splittable" style="height:5%;width:100%;" cellspacing="0" cellpadding="0" border="0"> 
		<tr>
			<td align="center">
				<div class="pt-formbutton" text="确定" id="submitbutton" onclick="onOk();"></div>
				<div class="pt-formbutton" text="取消" id="closebutton" onclick="window.close();"></div>		
			</td>
		</tr>
	</table>
</body>
<script>
	function onOk(){
		var partOid = document.frames("leftTree").getSelections();
		if(partOid == null){
			plm.showMessage({title:'错误提示', message:"请选择部件！", icon:'1'});
			return;
		}
		var objectOids = document.frames("rightContent").getSelections();
		if(objectOids == null){
			plm.showMessage({title:'错误提示', message:"请选择需要挂接的对象！", icon:'1'});
			return;
		}
		
		$.ajax({
			url:"<%=url%>",
			type:"post",
			dataType:"json",
			data:"partOid="+partOid+"&objectOids="+objectOids,
			success:function(result){
				plm.endWait();
				if (result.SUCCESS != null && result.SUCCESS =="false"){
					plm.showAjaxError(result);
				} else {
					alert("对象挂接成功！");
					window.close();
				}
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"对象挂接失败！", icon:'1'});
			}
		});
	}
	</script>
</html>
