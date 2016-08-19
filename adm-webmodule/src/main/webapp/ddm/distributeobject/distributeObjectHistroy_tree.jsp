<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>

<%
	String contextPath = request.getContextPath();
	//String oid_linkid_str = request.getParameter("oid_linkid_str");
	String title = "历史信息";
	String gridId = "disTree";
	String treeUrl = contextPath + "/ddm/distribute/distributeObjectHandle!getDistributeObjectHistoryTree.action";
	String contentUrl = contextPath + "/ddm/distribute/distributeInfoHandle!getHistoryDistributeInfosByDisObjOidAndDisOrderOids.action";
%>
<html>
<head>
<title><%=title%></title>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0"  scroll="no" onload="querySubmit()">
<table cellSpacing="0" cellPadding="0" height="100%" width="100%" border="0">
			<table class="pt-tree" border="0" url="" height="100%" width="100%" checkbox="false" treeColumn="Name"  id="<%=gridId%>" fit="true" singleSelect="false" onLoadSuccess="" onDblclick="" onDblClickRow="" onClickRow="getDistributeInfos()" scroll="yes">
				<thead>
					<tr>
						<th field="Name" width="200" tips="名称"></th>
					</tr>
				</thead>
			</table>
		</td>
	</tr>
</table>
</body>
<script>

	function querySubmit(){
		var oid_linkid_str = parent.document.getElementById("oid_linkid_str").value;
		var tree = pt.ui.get("<%=gridId%>");
		plm.startMiniWait();
		$.ajax({
    		url:"<%=treeUrl%>",
    		type:"post",
    		dataType:"json",
    		data:"oid_linkid_str="+oid_linkid_str,
    		success:function(result){
    			plm.endWait();
    			tree.data.addRange(result);
    		},
    		error:function(){
    			plm.showMessage({title:'错误提示', message:"操作失败！", icon:'1'});
    		}
		});
	}
	
	function reload(){
		var table = pt.ui.get("<%=gridId%>");
		table.reload();
	}
	
	function getSelections() {
		var table = pt.ui.get("<%=gridId%>");
		var selections = table.getSelections();
		return selections;
	}

	var getedNode = null;
	function selectChild() {
  	var tree = pt.ui.get("<%=gridId%>");
  	var selections = getSelections();
  	//var oids = "";
  	var order_oids = parent.document.getElementById("order_oids").value;
  	for(var i = 0; i < selections.length; i++){
  		var oid = selections[i]["DISTRIBUTEOBJECTOID"];
		//if(oids != ""){
		//	oids += ",";
		//}
			//oids += oid;
	}
		$.ajax({
		type:"POST",
		dataType:"json",
		data:"distributeObjectOid="+oid+"&order_oids="+order_oids,
		url: "<%=contentUrl%>",
		success: function(result){
			parent.frames["rightContent"].reload("");
		},
		error:function(){}
  	});	
	}

	function getDistributeInfos(node) {
		getedNode = node;
		var tree = pt.ui.get("<%=gridId%>");
		if (node == undefined || !node.expanded) {
			tree.expand(node, 1, selectChild);
		} else {
			selectChild();
		}
	}
</script>
</html>