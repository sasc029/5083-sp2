<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@include file="/ddm/public/ddm.jsp" %>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder"%>
<%@page contentType = "text/html; charset=UTF-8" pageEncoding="GBK"%>
	
<%
	String contextPath = request.getContextPath();
	String oid = request.getParameter("OID");
	String title = "�ַ���Ϣ";
	String gridId = "disTree";
	String toolBarId = "ddm.distributeinfo.tree.toolbar";
	String treeUrl = contextPath + "/ddm/distribute/distributeObjectHandle!getDistributeObjectTree.action?OID=" + oid;
	String contentUrl = "";
	//��÷��ŵ�OID
	String distributeOrderOid = null;
	if(oid == null){
		distributeOrderOid = (String)request.getSession().getAttribute(ConstUtil.DISTRIBUTE_ORDER_OID);
	}else{
		distributeOrderOid = oid;
	}
	//��ѯ���ŵ�
	Persistable obj = Helper.getPersistService().getObject(distributeOrderOid);
	DistributeOrder dis = (DistributeOrder)obj;
	boolean flag = false;
	//���յ����ٵ���Ϣ
		
	if(null != dis.getOrderType() && ( ConstUtil.C_ORDERTYPE_2.equals(dis.getOrderType())
			|| ConstUtil.C_ORDERTYPE_3.equals(dis.getOrderType()))){
		contentUrl = contextPath + "/ddm/distribute/recDesInfo!setLinkOids.action";
		flag = true;
	}else{//���ŵ�
		contentUrl = contextPath + "/ddm/distribute/distributeInfoHandle!getDistributeInfosByOids.action";
	}
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
			<table class="pt-grid" url="<%=treeUrl%>" height="100%" width="100%" checkbox="true" treeColumn="Number"  id="<%=gridId%>"  fit="true" singleSelect="false" onLoadSuccess="" onDblclick="" onDblClickRow="" onClickRow="getDistributeInfos()" scroll="yes">
				<thead>
					<tr>
					    <th field="Number" width="190" tips="���" >���</th>
						<th field="Name" width="190" tips="����">����</th>
						<th field="Version" width="90" tips="�汾">�汾</th>
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
	
	<%-- function selOrUnselChild(children,flag) {
		
		var tree = pt.ui.get("<%=gridId%>");
		if(flag=="sel"){
			
			for(var i=0;i<children.length;i++){
			
				var rowIndex = tree.getRowIndex(children[i]);
				var k;
				var str="";
				for(k in children[i]){
					//alert(typeof k);
				//	alert(children[i][k]);
					//str=str+k+"--";
				}
				
				//alert("11111==="+str);
				
				tree.selectRow(rowIndex);
			}
		}else{
			for(var j=0;j<children.length;j++){
				var rowIndex = tree.getRowIndex(children[j]);
				tree.unselectRow(rowIndex);
			}
		}
    } --%>
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
    //����ַ���Ϣ��������ʾ��Ӧ�ķַ�����
   function selectdataFromDisInfo(oids){
	   	clearAll();
    	var tree = pt.ui.get("<%=gridId%>");
    	var oida=oids.split(",");
    	var select = tree.getRows();
  		for(var i=0;i<oida.length;i++){
   			for(var j=0;j<select.length;j++){
   				var oid=select[j].OID;
   				/* for(k in select[i]){
   					alert(select[i][k]);
				} */
    			if(oida[i]==oid){
    				tree.selectRow(select[j]["__index"]);
    			};
			};
    	}; 
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
	
	// ȫѡ
	function checkAll() {
		var tree = pt.ui.get("<%=gridId%>");
	     tree.selectAll();
	     if(<%=flag%>){
			getDistributeInfos();
			setAllSelect();
	     }else {
			getDistributeInfos();
	     }
	
	}
	function selectAllData() {
		var table = pt.ui.get("<%=gridId%>");
		var selections = table.getRows();
		return selections;
	
	
	}
	
	// ȫȡ��
	function clearAll() {
		var tree = pt.ui.get("<%=gridId%>");
	    tree.unselectAll();
	     if(<%=flag%>){
			getDistributeInfos();
			removeAllSelect();
	     }else {
			getDistributeInfos();
	     }
	
	}
	
	// չ��
	function expandAll() {
		var tree = pt.ui.get("<%=gridId%>");
		var rows = tree.getRows();
		for(var i = 0; i < rows.length; i++){
			var row= rows[i];
			tree.expand(row,true);
		}
	}
	
	// �۵�
	function collapseAll() {
		var tree = pt.ui.get("<%=gridId%>");
		var rows = tree.getRows();
		for(var i = 0; i < rows.length; i++){
			var row= rows[i];
			tree.collapse(row,true);
		}
	}
	//kangyanfei 2014-07-08
	//session����linksOids�ڻ���������Ϣʹ��
	function setAllSelect() {
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
			},
			error:function(){}
		});	
	}
	//session�Ƴ�linksOids
	function removeAllSelect() {
		var oids = "";
			$.ajax({
			type:"POST",
			dataType:"json",
			data:"distributeOrderObjectLinkOids="+oids,
			url: "<%=contentUrl%>",
			success: function(result){
			},
			error:function(){}
		});	
	}
</script>
</html>
