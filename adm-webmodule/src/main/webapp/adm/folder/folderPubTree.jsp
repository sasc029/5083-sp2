<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@ page language="java"%>
<%@ page session="true"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@page import="com.bjsasc.platform.webframework.bean.FilterParam"%>
<%@ page import="com.bjsasc.plm.core.folder.*" %>
<%@ page import="com.bjsasc.plm.folder.*" %>
<%@ page import="com.bjsasc.plm.*" %>
<%@include file="/plm/plm.jsp" %>
<%
	//�������
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	// ҳ��ʹ��UTF-8����
	request.setCharacterEncoding("UTF-8");
	//�û�ȡ�����ж��Ƿ�������ļ�������������
	String treeType = request.getParameter(FolderSelector.FOLDER_TREE_ID);
	String folderiid = request.getParameter(FolderSelector.FOLDER_IID);
	String callback = request.getParameter(KeyS.CALLBACK);
	if(folderiid==null){
		folderiid = "";
	}
%>
<html> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>�ļ�����</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/plm/javascript/plm_js.jsp" charset="GBK"></script>
</head>
<body scroll="no" class="openWinPage" >

<table width="100%" height="100%" cellSpacing="0" cellPadding="0" border="0">
	<tr class="AvidmActionTitle">
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			  <tr>
			    <td class="AvidmTitleName">
			    <div class="imgdiv"><img src="<%=request.getContextPath()%>/plm/images/doc/doc_new.gif"/></div>
			    <div class="namediv">ѡ���ļ���</div>
			    </td>
			  </tr>
			</table>
		</td>
	</tr>
	<tr>
		<td valign="top">
			<table class="pt-tree" treeColumn="<%=KeyS.FOLDER %>" autoColumns="true" id="folderTree" fit="true" url="getTreeData.jsp?treeType=<%=treeType %>&folderiid=<%=folderiid %>"  onDblClickRow="treedbclick">
				<thead>
					<tr>
						<th field="<%=KeyS.FOLDER %>" ></th>
					</tr>
				</thead>
			</table>
	 	</td>
	</tr>
	<tr class="AvidmDecision">
			   <td>
			    	<div class="pt-formbutton" text="ȷ��" id="submitbutton" onclick="submitForm();"></div>
					<div class="pt-formbutton" text="ȡ��" id="closebutton" onclick="window.close();"></div>
					<div class="pt-formbutton" text="�½�" id="closebutton" onclick="createSubFolder();"></div>
				</td>
			</tr>
</table>
</body>
<script>
function submitForm(){
	var tree = pt.ui.get("folderTree");
	var node = tree.getSelected();
	//add by tianli
	var nodes=[];
	nodes.push(node);
	var curnode=node;
	while(tree.getParent(curnode)!=null){
		curnode= tree.getParent(curnode);
		if(curnode.FOLDER!=null){
			nodes.push(curnode);
		}
	}
	var str="";
	for(var i=nodes.length-1;i>=0;i--){
		str+="/"+nodes[i].FOLDER;
	}
	node.FOLDERNAME=node.FOLDER;
	node.FOLDER=str;
	//end add by tianli
	<%
		if(callback!=null&&!callback.equals("")){
	%>
	opener.<%=callback%>(node);
	window.close();
	<%}%>
}

function createSubFolder(){
	var tree = pt.ui.get("folderTree");
	var node = tree.getSelected();
	if(node==null){
		alert("��ѡ��ĳ�ļ��д�����");
		return;
	}
	var folderoid = node.OID;
	var url = "<%=request.getContextPath()%>/adm/folder/createFolder.jsp?<%=KeyS.CONTAINER_OID%>="+folderoid +"&<%=KeyS.CALLBACK%>=refreshnode";
	window.open(url,'win' , 'toolbar=no,  menubar=no, location=no, directories=no, status=no, scrollbars=no, resizable=yes, width=550, height=260, ' + 'left=' + (screen.width - 300) / 2 + ', ' + 'top=' + (screen.height - 120) / 2);

}
function refreshnode(){
	var tree = pt.ui.get("folderTree");
	var node = tree.getSelected();
	tree.reloadNode(node);//ˢ�����ڵ�
	tree.expand(node);
}
</script>
</html>
