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

	//�û�ѡ��ĳ����ͼʱ����ס�û���ѡ��
	String oid = request.getParameter(gridId+"_viewOid");
	if(oid != null && !oid.equals("ManageView")){
		Helper.getGridManager().setMyLatestGridView(spot, spotInstance, oid);
	}
	//�û��Զ������ͼ
	List<GridView> views = Helper.getGridManager().getMyGridViews(spot, spotInstance);
	//ϵͳ���õ�������ͬ������ͼ
	views.addAll(Helper.getGridManager().getMyTypeGridViews(spot));
	//ϵͳ���õ���ͼ
	GridView systemView = Helper.getGridManager().getMyDefaultGridView(spot);
	//��ǰӦ��ʾ����ͼoid
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
				splitVertical:true,				//ˮƽ�ָ�Ǵ�ֱ�ָ�,���Ϊtrue,���Ǵ�ֱ�ָ�,Ҳ�������ҷָ�
				A:"tdLeft",						//���������id,����
				B:"tdRight",					//�Ҳ�������id,����
				closeableto:20,					//�Զ����ص���С��Ȼ�߶�,����Ĭ�ϼ���
				slaveleft:"attrTree",			//���������grid��tree�ؼ���id
				//slaveright:"grid_assetplace",	//�Ҳ�������grid��tree�ؼ���id
				retfunc:"plm.resizecontrol"});	//�ص�����,����Ĭ�ϼ���
		});

		//��Ӧ�û�
		function onClickRow(nodes){
			var tree = pt.ui.get("attrTree");
			var node = tree.getSelected();
			//document.getElementById("searchTexts").value=node.Name;
			var link = node["link"];
			showContent(link);
		}
		
		//���ұ�iframe����ʾ����ҳ��
		function showContent(link){
			var contentView = document.getElementById("contentIFrame");
			//��ʱ��������գ����ж�֮ǰ������
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
					<jsp:param name="gridTitle" value="��ͼ"/>
				</jsp:include>
				<tr class="AvidmToolbar"><td align="right">
					<table id="<%=gridId%>_toolbar" width="100%" cellSpacing="0" cellPadding="0" border="0" class="toolbar_table">
					<tr><td width="90%"></td>
					<!-- ��ͼ�л������� -->
					<td width="10" style="padding-left:5px;padding-right:5px;" align="right">
						<select id="<%=gridId%>_viewOid" name="<%=gridId%>_viewOid" onchange="onViewChanged(this, '<%=spot%>', '<%=spotInstance%>');" title="��ѡ����ͼ" style="font-size:13px;">
							<% for(GridView view:views){ %>
								<option value="<%=view.getOid()%>" <%=view.getOid().equals(oid)?"selected='selected'":""%> 
									title="<%=view.getNote()!=null?view.getNote():""%>"><%=view.getName()%>
								</option>
							<% } %>
							<option value="<%=systemView.getOid()%>" <%=oid.equals(systemView.getOid())?"selected='selected'":""%> title="<%=systemView.getNote()!=null?systemView.getNote():""%>"><%=systemView.getName()%></option>	
							<option value="ManageView" title="������ͼ">����...</option>
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
