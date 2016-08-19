<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.grid.spot.SpotUtil"%>
<%@page import="com.bjsasc.plm.grid.spot.Spot"%>
<%@page import="com.bjsasc.plm.grid.GridView"%>
<%@page import="com.bjsasc.plm.Helper"%>
	
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%
	String contextPath = request.getContextPath();
	String gridId = "attrTree";
	String spot = "ActiveDocumentView";
	String spotInstance = "ActiveDocumentView";
	String toolbarSearcher = "none";
	String url = contextPath + "/adm/modelview/modelVisit_get.jsp";
	String contentUrl = contextPath + "/adm/active/ModelViewHandle!getActiveDocuments.action?init=true";

	//用户选中某个视图时，记住用户的选择
	String oid = request.getParameter(gridId+"_viewOid");
	if(oid != null && !oid.equals("ManageView")){
		Helper.getGridManager().setMyLatestGridView(spot, spotInstance, oid);
	}
	//用户自定义的视图
	List<GridView> views = Helper.getGridManager().getMyGridViews(spot, spotInstance);
	//系统内置的与类型同名的视图
	views.addAll(Helper.getGridManager().getMyTypeGridViews(spot));
	//系统内置的视图
	GridView systemView = Helper.getGridManager().getMyDefaultGridView(spot);
	//当前应显示的视图oid
	oid = Helper.getGridManager().getMyLatestGridView(spot, spotInstance).getOid();
	if(oid == null){
		oid = Helper.getOid(views.get(0));
	}
	Spot spt = SpotUtil.getSpot(spot);
%>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
<html>
<head>  
	<link rel="stylesheet" type="text/css" href="/platform/plm/css/plm.css">
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>	
	<script type="text/javascript" src="<%=contextPath%>/plm/javascript/uisplitter.js" charset="UTF-8"></script>
	<script type="text/javascript">

		$(document).ready(function() {
			$("#table").splitter({
				splitVertical:true,				//水平分割还是垂直分割,如果为true,就是垂直分割,也就是左右分割
				A:"tdLeft",						//左侧容器的id,必须
				B:"tdRight",					//右侧容器的id,必须
				closeableto:20,					//自动隐藏的最小宽度或高度,保持默认即可
				slaveleft:"attrTree",			//左侧容器中grid或tree控件的id
				//slaveright:"grid_assetplace",	//右侧容器中grid或tree控件的id
				retfunc:"plm.resizecontrol"});	//回调函数,保持默认即可
		});

		//响应用户
		function onClickRow(nodes){
			var tree = pt.ui.get("attrTree");
			var node = tree.getSelected();
			//document.getElementById("searchTexts").value=node.Name;
			var link = node["link"];
			showContent(link);
		}
		
		//在右边iframe内显示内容页面
		function showContent(link){
			var contentView = document.getElementById("contentIFrame");
			//暂时把内容清空，以中断之前的请求
			contentView.contentWindow.location.href="about:blank";
			contentView.contentWindow.location.href = link;
		}
		
		function onLoad(){
		}
	</script>
</head>
<body style="margin:0px;" scroll="no">
<form id="mainForm" name="mainForm" method="POST">
	<table id="table" height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0" style="margin:0px;">
	<tr height=100%>
		<td id="tdLeft" width="20%">
			<table id="table" height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0" style="margin:0;" scrolling="yes" >
				<jsp:include page="/plm/common/grid/control/grid_of_title.jsp">
					<jsp:param name="gridTitle" value="视图"/>
				</jsp:include>
				<tr class="AvidmToolbar"><td align="right">
					<table id="<%=gridId%>_toolbar" width="100%" cellSpacing="0" cellPadding="0" border="0" class="toolbar_table">
					<tr><td width="90%"></td>
					<!-- 视图切换下拉框 -->
					<td width="10" style="padding-left:5px;padding-right:5px;" align="right">
						<select id="<%=gridId%>_viewOid" name="<%=gridId%>_viewOid" onchange="onViewChanged(this, '<%=spot%>', '<%=spotInstance%>');" title="请选择视图" style="font-size:13px;">
							<% for(GridView view:views){ %>
								<option value="<%=view.getOid()%>" <%=view.getOid().equals(oid)?"selected='selected'":""%> 
									title="<%=view.getNote()!=null?view.getNote():""%>"><%=view.getName()%>
								</option>
							<% } %>
							<option value="<%=systemView.getOid()%>" <%=oid.equals(systemView.getOid())?"selected='selected'":""%> title="<%=systemView.getNote()!=null?systemView.getNote():""%>"><%=systemView.getName()%></option>	
							<option value="ManageView" title="定制视图">定制...</option>
						</select>
					</td></tr>
					</table>
				</td></tr>
				<tr><td>
					<table class="pt-tree" url="<%=url%>" treeColumn="NAME" autoColumns="true"
							id="attrTree" fit="true" singleSelect="false" onLoadSuccess="onLoad"
							onDblClickRow="onClickRow" onClickRow="onClickRow" scrolling="yes">
						<thead><th field="NAME"></th></thead>
					</table>
				</td></tr>
			</table>
		</td>
		<td id="tdRight" width="80%">
			<iframe id="contentIFrame" name="contentIFrame" frameborder="0" style="overflow: hidden;" 
				scrolling="no" src="<%=contentUrl%>" height=100% width=100%/>
		</td>
	</tr></table>
</form>
</body>
</html>
