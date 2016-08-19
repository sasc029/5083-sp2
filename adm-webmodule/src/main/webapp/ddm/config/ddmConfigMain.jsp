<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>

<%@include file="/plm/plm.jsp" %>
<%
	String contextPath = request.getContextPath();
	String oid = RequestUtil.getParamOid(request);
	String gridId = "ddmConfigTree";
	String title = "���Ź�������";
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
		//����splitbar
		$(document).ready(function() {
			$("#table").splitter({
				splitVertical:true,				    //ˮƽ�ָ�Ǵ�ֱ�ָ�,���Ϊtrue,���Ǵ�ֱ�ָ�,Ҳ�������ҷָ�
				A:"tdLeft",						    //���������id,����
				B:"tdRight",					    //�Ҳ�������id,����
				closeableto:20,					    //�Զ����ص���С��Ȼ�߶�,����Ĭ�ϼ���
				slaveleft:"<%=gridId%>",	        //���������grid��tree�ؼ���id
				slaveright:"distributeContent",	    //�Ҳ�������grid��tree�ؼ���id
				retfunc:"plm.resizecontrol"});	    //�ص�����,����Ĭ�ϼ���
		});

		//��Ӧ�û�
		function onClickRow(nodes){
			var tree = pt.ui.get("<%=gridId%>");
			var node = tree.getSelected();
			var link = node["link"];
			showContent(link);
		}
		
		//���ұ�iframe����ʾ����ҳ��
		function showContent(link){
			var contentView = document.getElementById("rightContent");
			//��ʱ��������գ����ж�֮ǰ������
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
