<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.ui.UIDataInfo"%>
<%@page import="com.bjsasc.plm.change.ChangeBaseS"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="java.util.*"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page session = "true"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page language="java" %>
<%@page import="com.bjsasc.adm.common.ConstUtil" %>
<%
	String path = request.getContextPath();
	String oid = (String)session.getAttribute("getOneOid");
	String q_url = path+"/adm/active/ActiveOrderHandle!getBeforeObject.action?OID=" + oid;
	String h_url = path+"/adm/active/ActiveOrderHandle!getAfterObject.action?OID=" + oid;
	 
%>
<html>
<head>  
	<title>更改前/更改后</title>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
</head>

<body   bgcolor="#E0ECFF">
<form name="mainForm" method="POST">
	<div style="width:100%;">
		<div class="AvidmMtop twoTitle" >
			<a id="a_1" class="showIcon" onclick="divSelect_1(this)" href="javascript:void(0)"><img src="<%=request.getContextPath() %>/plm/images/common/space.gif"></a>
					更改前对象
			</div>
		</div>
		<div id="div_1" style="width:100%;" >
			<table id="iframe1" width="100%" cellSpacing="0" cellPadding="0" border="0">
				<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
				<jsp:param name="spot" value="<%=ConstUtil.SPOT_LISTACTIVEORDEROBJECT%>"/>
				<jsp:param name="gridUrl" value="<%=q_url%>"/>
				<jsp:param name="gridId" value="data_TreeGrid_before"/>
				<jsp:param name="toolbar_modelId" value="relatedactiveorder.browse.before.toolbar"/>
			</jsp:include>
			</table>   
		</div>
	   	<div style="width:100%;">
			<div class="AvidmMtop twoTitle" >
				<a id="a_2" class="showIcon" onclick="divSelect_2(this)" href="javascript:void(0)"><img src="<%=request.getContextPath() %>/plm/images/common/space.gif"></a>
					更改后对象
				</div>
			</div>
		<div id="div_2" style="width:100%;">
			<table id="iframe2" width="100%" cellSpacing="0" cellPadding="0" border="0">
				<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
					<jsp:param name="spot" value="<%=ConstUtil.SPOT_LISTACTIVEORDEROBJECT%>"/>
					<jsp:param name="gridUrl" value="<%=h_url%>"/>
					<jsp:param name="gridId" value="data_TreeGrid_after"/>
					<jsp:param name="toolbar_modelId" value="relatedactiveorder.browse.after.toolbar"/>
				</jsp:include>
			</table>  
		</div> 
	</div>  
</form>  
</body>  
</html>
<script type="text/javascript">
var activeOrderOid = "<%=oid%>";
	//刷新页面
	function reload(){
		var grid_a = pt.ui.get("data_TreeGrid_after");
		var grid_b = pt.ui.get("data_TreeGrid_before");
		grid_a.reload();
		grid_b.reload();
	}	
	var bodyH = document.body.clientHeight;
	$().ready(function(){
		$("#iframe1").attr("height",(bodyH-80)/2+"px");
		$("#iframe2").attr("height",(bodyH-80)/2+"px");
	});

	function divSelect_1(obj){
		var icon1 =$("#a_1").attr("class");
		var icon2 =$("#a_2").attr("class");
		if(icon1 == "showIcon"){
			$("#a_1").attr("class","hideIcon");		
			$("#div_1").hide();
			if(icon2 == "showIcon"){
				$("#iframe2").attr("height",(bodyH-80)+"px");
				
				setGridH("data_TreeGrid_after",bodyH-90);
			}
		}else{
			$("#a_1").attr("class","showIcon");
			$("#div_1").show();
			if(icon2 == "showIcon"){
				$("#iframe1").attr("height",(bodyH-80)/2+"px");
				$("#iframe2").attr("height",(bodyH-80)/2+"px");
				setGridH("data_TreeGrid_before",(bodyH-90)/2);
				setGridH("data_TreeGrid_after",(bodyH-90)/2);
			}else{
				$("#iframe1").attr("height",(bodyH-80)+"px");
				setGridH("data_TreeGrid_before",bodyH-90);
			}
		}
	}
	function divSelect_2(obj){
		var icon1 =$("#a_1").attr("class");
		var icon2 =$("#a_2").attr("class");
		if(icon2 == "showIcon"){
			$("#a_2").attr("class","hideIcon");		
			$("#div_2").hide();
			if(icon1 == "showIcon"){
				$("#iframe1").attr("height",(bodyH-80)+"px");
				setGridH("data_TreeGrid_before",bodyH-90);
			}
		}else{
			$("#a_2").attr("class","showIcon");
			$("#div_2").show();
			if(icon1 == "showIcon"){
				$("#iframe1").attr("height",(bodyH-80)/2+"px");
				$("#iframe2").attr("height",(bodyH-80)/2+"px");
				setGridH("data_TreeGrid_before",(bodyH-90)/2);
				setGridH("data_TreeGrid_after",(bodyH-90)/2);
			}else{
				$("#iframe2").attr("height",(bodyH-80)+"px");
				setGridH("data_TreeGrid_after",bodyH-90);
			}
		}
	}
	function setGridH(gridId,height){
		var grid = pt.ui.get(gridId);
		grid.set( {
			//width :size.width,
			height :height-30
		});
	}
</script>