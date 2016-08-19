<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.core.folder.Cabinet"%>
<%@page import="com.bjsasc.plm.core.Helper"%>
<%@page import="com.bjsasc.plm.core.folder.Folder"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@include file="/plm/plm.jsp" %>
<%
	Object asRootOidses = session.getAttribute("asRootOid");
	String asRootOidreq = request.getParameter("asRootOid");
	if (asRootOidreq != null && asRootOidreq.length() > 0) {
		session.setAttribute("asRootOid", asRootOidreq);
	}
	String rootDisplay = request.getParameter("rootDisplay");
	if (rootDisplay != null && "true".equals(rootDisplay)) {
		session.removeAttribute("asRootOid");
	}
	String oid = request.getParameter(KeyS.OID);
	
    Folder folder = (Folder)Helper.getPersistService().getObject(oid);	
    Cabinet cabinet = folder.getContextInfo().getContext().getCabinetDefault();
	String folderStr = "{OID:'"+oid+"'}";
	String url = request.getContextPath() + "/adm/folder/visitFolder_get.jsp?OID="+oid;
	String contextPath=request.getContextPath();
	String contentUrl = request.getContextPath() + "/plm/folder/folderlist.jsp?OID="+oid;
	// request.setAttribute("id","SubFolder:07973b7a54b0870a45c63dafcecdbe2a");
	
	Object asRootOid = session.getAttribute("asRootOid");
	if (asRootOid != null && asRootOid.toString().length() > 0) {
		folderStr = "{OID:'"+asRootOid+"'}";
		contentUrl = request.getContextPath() + "/plm/folder/folderlist.jsp?OID="+asRootOid;
	}
%>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
<html>
<head>
	<script type="text/javascript" src="<%=request.getContextPath()%>/platform/common/js/ptutil.js" charset="GBK"></script>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>	
	<link rel="stylesheet" type="text/css" href="/platform/plm/css/plm.css">	
	<script type="text/javascript"	src="<%=contextPath%>/plm/javascript/uisplitter.js" charset="UTF-8"></script>
	<script type="text/javascript"	src="<%=contextPath%>/plm/javascript/visitFolder.js" charset="GBK"></script>
<script type="text/javascript">
//����splitbar
$(document).ready(function() {
	var tree = pt.ui.get("folderTree");
	var node=tree.getRows();
	tree.set({
		autoColumns:true,
		    treeColumn: 'name',
		    columns: [
		        {header: '�ļ���', dataIndex: 'name', id: 'name',minWidth: 200,
		        	renderer: function(value, node, column, rowIndex, data, treegrid){
		                var treeName=document.getElementById("searchText").value;
		             if(treeName==""){
		            		 if(node.children)return ''+node.Name+'';
		            		 return node.Name; 
		            	    }else{
		                	   if(node.children){
		                		   var treeNameOfNode = node.Name.indexOf(treeName);
		                		   if(treeNameOfNode>=0){
		                			   var nodeOfname=node.Name.replace(treeName,"<span style=' background:#C0C0C0;width:4px;top:0px; '>"+treeName+"</span>");
		                	
		                			   return nodeOfname;
		                		   } 
		                		   return ''+node.Name+'';
		                	   } else{
		                		   var treeNameOfNode = node.Name.indexOf(treeName);
		                		   if(treeNameOfNode>=0){
		                			   var nodeOfname=node.Name.replace(treeName,"<span style='width:4px; top:0px; background:#C0C0C0;'>"+treeName+"</span>");
		                			   return nodeOfname;
		                		   }
		                		   return ''+node.Name+''; 
		                	   }  
		                  }
		             
		          	  }
		         }   
		   ]
		});
	$("#table").splitter({
		splitVertical:true,				//ˮƽ�ָ�Ǵ�ֱ�ָ�,���Ϊtrue,���Ǵ�ֱ�ָ�,Ҳ�������ҷָ�
		A:"tdLeft",						//���������id,����
		B:"tdRight",					//�Ҳ�������id,����
		closeableto:20,					//�Զ����ص���С��Ȼ�߶�,����Ĭ�ϼ���
		slaveleft:"folderTree",			//���������grid��tree�ؼ���id
		//slaveright:"grid_assetplace",	//�Ҳ�������grid��tree�ؼ���id
		retfunc:"plm.resizecontrol"});	//�ص�����,����Ĭ�ϼ���
		
	<%	if (asRootOid != null && asRootOid.toString().length() > 0) {
	%>

	//ˢ�µ�����	ds
	var foldert = <%=folderStr%>;
	parent.refreshNavigator("NAVIGATE_TO_OBJECT", foldert);
	<%}%>
});

</script>
<script type="text/javascript">
var folder = <%=folderStr%>;
var treeNames=Array();
//����ǰ̨������������ͨ�� ajax��ȡ���ص�·�� 
function treePush(){
	    treeNames.length=0;
	    var treeName=document.getElementById("searchText").value;
	    var cabinetinnerId="<%=cabinet.getInnerId() %>";
	  $.ajax({ 
		  type: "post",
          url: "<%=request.getContextPath()%>/plm/folder/visitFolder_set.jsp", 
          dataType: "json", 
          data:{"treeName":treeName,"cabinetinnerId":cabinetinnerId},
          success: function (data) { 
                 	var size = parseInt(data.SIZE);
                 	for(var i = 0 ; i < size ; i++){
                 		 treeNames.push(data['NAME'+i]);
                 	}             	                  
          }
     }); 
}
function onLoad(){
	var tree = pt.ui.get("folderTree");
	//ʹĿ��ڵ㴦��ѡ��״̬
	var node = tree.find(folder);
	tree.selectRow(tree.getRowIndex(node));	

}

function refreshTheNodeTree(node){
	var oid = node.OID;
	parent.location = "<%=request.getContextPath()%>/plm/common/visit.jsp?<%=KeyS.OID%>="+oid;
}
//���ұ�iframe����ʾ����ҳ��
function showContent(link){
	var contentView = document.getElementById("contentIFrame");
	//��ʱ��������գ����ж�֮ǰ������
	contentView.contentWindow.location.href="about:blank";
	contentView.contentWindow.location.href = '<%=request.getContextPath()%>'+link;
}
//��Ӧ�û�
function onClickRow(nodes){
	var tree = pt.ui.get("folderTree");
	var node = tree.getSelected();
	document.getElementById("searchTexts").value=node.Name;
	var link = node["link"];
	//ˢ�µ�����	ds
	parent.refreshNavigator("NAVIGATE_TO_OBJECT", node);
	showContent(link);
}

//ˢ�µ�ǰѡ�еĽڵ�
function refreshTree(){
	  var tree = pt.ui.get("folderTree");
	  var node = tree.getSelected();
	  
	  refreshNode(node);
}
//ˢ��������
function refreshAllTree(){
	var tree = pt.ui.get("folderTree");
	tree.selectRow(0);
	var node = tree.getSelected();
	refreshNode(node);
}

//ˢ��ָ�����ڵ�
function refreshNode(node){	
	node.ChildUrl = "<%=request.getContextPath()%>/adm/folder/visitFolder_get.jsp?op=getSubFolders&OID="+node.OID;
	node.expanded = true;	
	//ˢ�����ڵ�
	var tree = pt.ui.get("folderTree");
	tree.reloadNode(node);
}
//�����ڵ�
function search(flag){
	tree = pt.ui.get("folderTree");
	treeMove(flag);
}
//�س������ڵ�
function searchUp(flag){
    tree = pt.ui.get("folderTree");
    treeBackIndex=0;
	treeNames.length=0;
	//var tree = pt.ui.get("folderTree");
	treePush();
	tree.refresh();
	var event=arguments.callee.caller.arguments[0]||window.event;
	if (event.keyCode == 13){  folderTree
		 setTimeout(function(){
			 treeMove(flag);
			 },100);	
		 }
	}
//���˵������ڵ�
function treeClose(obj){
	tree = pt.ui.get("folderTree");
	onSearchTextKey(obj);
}

// �Ҽ��˵��ű�
var folderTree_records = [];
var folderTree_container = null;

function on_folderTree_showContextMenu(){
	var grid = pt.ui.get("folderTree");

	var target = grid.getSelected();	
	if(target == null){
		return;
	}	
		
	//Ŀ���¼
	folderTree_records = [];
	folderTree_records.push(target);
			
	//ajax���ز˵�
	var url = "<%=contextPath%>/plm/common/grid/control/contextMenu_get.jsp?oid="+target.OID+"&spot=rightmenu.ActiveContext&gridId=folderTree";	
	
	pt.ui.loadContextMenu(url);
}
</script>
</head>

<body style="margin:0px;" scroll="no">

<table id="table" height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0" style="margin:0px;">
<tr height=100%>
<td id="tdLeft" width="20%">	
	<table id="table" height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0" style="margin:0;" scrolling="yes" >
		
		<jsp:include page="/plm/common/grid/control/grid_of_title.jsp">
			<jsp:param name="gridTitle" value="�ļ���"/>
		</jsp:include>
		<tr style="height:30px">
			<td align=right>		    
				<div id="searchTextDiv"><input style="width:100%" type="text" id="searchText" value="" class='pt-textbox' onkeyup="searchUp('serch')" ontrigger="search('serch')"></div>
				<div id="searchTextsDiv" style="display: none;" ><input id="searchTexts" onmouseover="this.select();"   style=" width:76.3%; height:25px;float: left;" type="text" value="" onkeyup="treeClose('true')" class='pt-textbox'/><input class="buttontreeDefault" onmouseover="this.className='buttontreeHover'" onmouseout="this.className='buttontreeDefault'" style="border: #CCCCCC solid 1px; float: left; width:7.2%; height:25px"  type="button" value="X" onclick="treeClose('false')"/><input class="buttontreeDefault" onmouseover="this.className='buttontreeHover'" onmouseout="this.className='buttontreeDefault'"  id="treeBack" style="border: #CCCCCC solid 1px;float: left; width: 7.2%; height:25px"   type="button" value="<" onclick="search('go')"/><input class="buttontreeDefault" onmouseover="this.className='buttontreeHover'" onmouseout="this.className='buttontreeDefault'" id="treeInput" style="border: #CCCCCC solid 1px; float: left; width: 7.2%; height:25px"    type="button" value=">" onclick="search('back')"/></div>	
				
			</td>
		</tr>
		<tr>
			<td>	
					<table class="pt-tree" url="<%=url%>" treeColumn="Name" autoColumns="true" id="folderTree" fit="true" contextmenu="on_folderTree_showContextMenu" singleSelect="false" onLoadSuccess="onLoad" onDblClickRow="onClickRow" onClickRow="onClickRow" scrolling="yes"  >
						<thead>
							<tr >
								<th field="Name" width="200"></th>
							</tr>
						</thead>
					</table>		
			</td>
		</tr>
	</table>	
</td>
<td id="tdRight" width="80%">
	<iframe id="contentIFrame" name="contentIFrame" frameborder="0" style="overflow: hidden;" scrolling="no" src="<%=contentUrl%>" height=100% width=100%/>
</td>
</tr>
</table>
</body>
</html>
