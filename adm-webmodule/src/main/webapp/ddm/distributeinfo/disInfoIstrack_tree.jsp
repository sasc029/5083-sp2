<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@include file="/ddm/public/ddm.jsp" %>

<%@page contentType = "text/html; charset=UTF-8" pageEncoding="GBK"%>
	
<% 
	String contextPath = request.getContextPath();
	String title = "分发信息";
	String gridId = "disObjTree";
	String toolBarId = "ddm.distributeinfo.tree.toolbar";
	String treeUrl = contextPath + "/ddm/distribute/distributeObjectHandle!getDisObjectTree.action";
%>
<html >
<head>
<title><%=title%></title>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
<script type="text/javascript">
function divSelected(obj){
	var tr = $(obj).parent().parent().parent();//当前title所在的tr对象
	//控制表格对象展示
	var tr1 = $(tr).next();
	var tr2 = $(tr).next().next();
	addSign(tr1);
	addSign(tr2);

	var show = $(tr1).attr("show");
	if(show == "true"){
		var str1 = tr1.clone();
		var str2 = tr2.clone();
		
		str1 = $(str1).attr("id",$(str1).attr("sid"));
		str2 = $(str2).attr("id",$(str2).attr("sid"));
		
		$(tr1).css("display","none").attr("show","false");
		$(tr2).css("display","none").attr("show","false");
		
		$(str1).empty();
		$(str2).empty();
		
		$(tr).parent().append(str1).append(str2);
	}else{
		var sid1 = $(tr1).attr("sid");
		var sid2 = $(tr2).attr("sid");
		
		$(document.getElementById(sid1)).remove();
		$(document.getElementById(sid2)).remove();
		
		$(tr1).css("display","block").attr("show","true");
		$(tr2).css("display","block").attr("show","true");
	}
	
	
	//控制title样式（加减号切换）
	var icon = $(obj).attr("class");
	if(icon == "showIcon"){
		$(obj).attr("class","hideIcon");		
	}else{
		$(obj).attr("class","showIcon");
	}
}

function addSign(obj){
	var sid =  $(obj).attr("sid");
	if(sid == null || sid == ""){
		$(obj).attr({"sid":Math.random()*10000,"show":"true"});
	}
}
</script>
 <style type="text/css">
	.icon_check_clear{
		background:url('<%=contextPath%>/plm/images/common/check_clear.gif') no-repeat;
	}
	.icon_check_all{
		background:url('<%=contextPath%>/plm/images/common/check_all.gif') no-repeat;
	}
</style>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0"  scroll="no">
<table cellSpacing="0" cellPadding="0" height="100%" width="100%" border="0">
	<tr>
		<td height="100%">
			<table cellSpacing="0" cellPadding="0" width="100%" height="100%" border="0" style="border-right:#9cbdff solid 1px;border-left:#9cbdff solid 1px;">
				<tr class="AvidmToolbar">
					<td nowrap="nowrap">
					<div class='AvidmMtop5 twoTitle'>
						<a class="showIcon" href="#" onclick="divSelected(this);">
						<img src="<%=request.getContextPath()%>/plm/images/common/space.gif"></a>
						更改单列表
					</div> 
				</td></tr>
				<tr><td>
					<table class="pt-tree" url="<%=treeUrl%>" height="100%" width="100%" checkbox="true" treeColumn="Name"  id="<%=gridId%>" fit="true" singleSelect="true" onDblclick="" onDblClickRow="" onClickRow="getDistributeInfos()" scroll="yes" onLoadSuccess="onLoadSuccess()">
						<thead>
							<tr>
								<th field="Name" width="200" tips="名称"></th>
							</tr>
						</thead>
					</table>
				</td></tr>
				</table>
		</td></tr>
</table>

</body>
<script language="JavaScript" for="window" event="onresize" type="text/JavaScript">
</script>
<script>
	function onLoadSuccess(){
		var grid = pt.ui.get("<%=gridId%>");
		var rows = grid.getRows();
		var linkOid = rows[0]["DISTRIBUTEORDEROBJECTLINKOID"];
		var isTrack = rows[0]["ISTRACK"];
		grid.selectRow(0);
		parent.frames["rightContent"].location=contextPath + "/ddm/distributeinfo/disInfoIsTrack_content_edit.jsp?linkOid=" + linkOid + "&isTrack=" + isTrack;
	}

	function reload(){
		var table = pt.ui.get("<%=gridId%>");
		table.reload();
	}
	
	function getDistributeInfos() {
		var selections = getSelections();
	    var linkOid = selections[0]["DISTRIBUTEORDEROBJECTLINKOID"];
		var isTrack = selections[0]["ISTRACK"];
		parent.frames["rightContent"].location=contextPath + "/ddm/distributeinfo/disInfoIsTrack_content_edit.jsp?linkOid=" + linkOid + "&isTrack=" + isTrack;
	}
	
	function getSelections() {
		var table = pt.ui.get("<%=gridId%>");
		var selections = table.getSelections();
		return selections;
	}
</script>
</html>