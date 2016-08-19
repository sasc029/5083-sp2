<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@include file="/ddm/public/ddm.jsp" %>

<%@page contentType = "text/html; charset=UTF-8" pageEncoding="GBK"%>
	
<% 
	String contextPath = request.getContextPath();
	String oid = request.getParameter("oid");
	String title = "分发信息";
	String gridId = "disTree";
	String toolBarId = "ddm.distributeinfo.tree.toolbar";
	String treeUrl = contextPath + "/ddm/distribute/distributeObjectHandle!getDistributePaperObjectTree.action?oid=" + oid;
	String contentUrl = contextPath + "/ddm/distribute/distributeInfoHandle!getDistributeInfosByPaperOids.action";
%>
<html >
<head>
<title><%=title%></title>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
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
		<td height="1%">
			<table cellSpacing="0" cellPadding="0" width="100%" border="0" style="border-right:#9cbdff solid 1px;border-left:#9cbdff solid 1px;"><tr class="AvidmToolbar"><td> 
				<div class="pt-toolbar">
					<%=UIHelper.getToolBar(toolBarId)%>
				</div> 
			</td></tr></table>
			<div id="gridTop" name="gridTop"><div>
		</td></tr>
		<tr><td>
			<table class="pt-tree" url="<%=treeUrl%>" height="100%" width="100%" checkbox="true" treeColumn="Name"  id="<%=gridId%>" fit="true" singleSelect="false" onLoadSuccess="" onDblclick="" onDblClickRow="" onClickRow="getDistributeInfos()" scroll="yes">
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

	function reload(){
		var table = pt.ui.get("<%=gridId%>");
		table.reload();
	}
	
	var getedNode = null;
	
	function selectChild() {
	
		  var tree = pt.ui.get("<%=gridId%>");
		  if(!tree.isLeaf(getedNode)){
				
		  	var selections = getSelections();
		    var children = tree.getChildren(getedNode,1);
		  
		    if(selections == null || selections == ""){
		    	selOrUnselChild(children,"unsel");
		    }
		    else{
		    
		    	//var i=0;
		    	for(var i = 0;i<selections.length;i++){
		    		
		    		if(getedNode == selections[i]){
		    			
		    	    	selOrUnselChild(children,"sel");
		    			break;
		    		}
		    	}
		    	if(i==selections.length){
		    		selOrUnselChild(children,"unsel");
		    	}
		    }
		  }

		  var selections = getSelections();
		  var oids = "";
		  for(var i = 0; i < selections.length; i++){
		  	var oid = selections[i]["DISTRIBUTEORDEROBJECTLINKOID"];
			if(oids != ""){
				oids += ",";
			}
				oids += oid;
		}
			$.ajax({
			type:"POST",
			dataType:"json",
			data:"distributeOrderObjectLinkOids="+oids,
			url: "<%=contentUrl%>",
			success: function(result){
				parent.frames["rightContent"].reload("");
			},
			error:function(){}
		  });	
	    }
    function selOrUnselChild(children,flag) {
		var tree = pt.ui.get("<%=gridId%>");
		if(flag=="sel"){
			for(var i=0;i<children.length;i++){
				tree.selectRow(children[i]["__index"]);
			}
		}else{
			for(var j=0;j<children.length;j++){
				tree.unselectRow(children[j]["__index"]);
			}
		}
    }
	
	
	function getDistributeInfos(node) {
		getedNode = node;
		var tree = pt.ui.get("<%=gridId%>");
		if(node==undefined||!node.expanded){
			tree.expand(node,1,selectChild);
		} else {
			selectChild();
		}
	}
	
	function getSelections() {
		var table = pt.ui.get("<%=gridId%>");
		var selections = table.getSelections();
		return selections;
	}
	
	// 全选
	function checkAll() {
		var tree = pt.ui.get("<%=gridId%>");
	    tree.selectAll();
	    getDistributeInfos();
	}
	
	// 全取消
	function clearAll() {
		var tree = pt.ui.get("<%=gridId%>");
	     tree.unselectAll();
	     getDistributeInfos();
	}
	
	// 展开
	function expandAll() {
		var tree = pt.ui.get("<%=gridId%>");
		var rows = tree.getRows();
		for(var i = 0; i < rows.length; i++){
			var row= rows[i];
			tree.expand(row,true);
		}
	}
	
	// 折叠
	function collapseAll() {
		var tree = pt.ui.get("<%=gridId%>");
		var rows = tree.getRows();
		for(var i = 0; i < rows.length; i++){
			var row= rows[i];
			tree.collapse(row,true);
		}
	}
</script>
</html>