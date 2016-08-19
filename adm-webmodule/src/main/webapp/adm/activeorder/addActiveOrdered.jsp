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
<%
	String path = request.getContextPath();
	String oid = (String)session.getAttribute("getOneOid");
	UIDataInfo beforeMap=new UIDataInfo();
	beforeMap.put(UIHelper.MENU_ACTION_RECORDS, "data_TreeGrid_before.getSelections()");
	beforeMap.put(UIHelper.MENU_ACTION_CONTAINER,"beforeContext");
	beforeMap.put(UIHelper.MENU_ITEM_WIDTH,"30");
	beforeMap.put(UIHelper.MENU_TOOLBAR_IS_SHOW_TEXT, "false");
	
	UIDataInfo afterMap=new UIDataInfo();
	afterMap.put(UIHelper.MENU_ACTION_RECORDS, "data_TreeGrid_after.getSelections()");
	afterMap.put(UIHelper.MENU_ACTION_CONTAINER,"afterContext");
	afterMap.put(UIHelper.MENU_ITEM_WIDTH,"30");
	afterMap.put(UIHelper.MENU_TOOLBAR_IS_SHOW_TEXT, "false");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>添加更改对象</title>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
<script type="text/javascript">
	var beforeContext = {};
	var afterContext = {};
</script>
</head>
<body   bgcolor="#E0ECFF">
<form name="newForm" id="newForm">
	<div style="width:100%;">
		<div class="AvidmMtop twoTitle" >
			<a id="a_1" class="showIcon" onclick="divSelect_1(this)" href="javascript:void(0)"><img src="<%=path %>/plm/images/common/space.gif"></a>
			改前对象
		</div>
	</div>
	<div id="div_1" style="width:100%;height:50%"> 
	<TABLE id="iframe1" cellSpacing="0" cellPadding="0" width="100%" border="0"  height="100%">
	  <TBODY>
	    <TR class="AvidmToolbar">
	      <td>  
				<%=UIHelper.getToolBar("relatedactiveorder.create.before.toolbar",beforeMap) %>
		  </td>
	    </TR>
	     <TR>
	      <TD valign="top">
			<table class="pt-grid" id="data_TreeGrid_before" rownumbers="true" fit="true" pagination="false" checkbox="true"  singleSelect="false" autoLoad="false">
				<thead>
					<tr>
						<th field="TYPE" width="30px"></th>
						<th field="NUMBER" >编号</th>
						<th field="NAME" >名称</th>
						<th field="VERSION" >版本</th>
						<th field="CONTEXT" >上下文</th>
						<th field="NOTE" width='120px' editor="{type:'textarea'}">备注&nbsp;<font size="1" ></font></th>
						<th field="MODIFY_TIME">上次修改时间</th>
						<th field="MODIFIER">修改者</th>	
					</tr>
				</thead>
			</table>
	      </TD>
	    </TR>
	  </TBODY>
	</TABLE>
	</div>
	<div style="width:100%;">
	<div class="AvidmMtop twoTitle" >
		<a id="a_2" class="showIcon" onclick="divSelect_2(this)" href="javascript:void(0)"><img src="<%=path %>/plm/images/common/space.gif"></a>
		改后对象
	</div>
	</div>
	<div id="div_2" style="width:100%;height:50%">
	<TABLE id="iframe2" cellSpacing="0" cellPadding="0" width="100%" border="0" height="100%">
	  <TBODY>
	    <TR class="AvidmToolbar">
	      <TD valign="top" >
			<%=UIHelper.getToolBar("relatedactiveorder.create.after.toolbar",afterMap) %>
	      </TD>
	    </TR>
	     <TR>
	      <TD valign="top">
			<table class="pt-grid" id="data_TreeGrid_after" rownumbers="true" fit="true" pagination="false" checkbox="true" singleSelect="false" autoLoad="false">
				<thead>
					<tr>
						<th field="TYPE" width="30px"></th>
						<th field="NUMBER" >编号</th>
						<th field="NAME" >名称</th>
						<th field="VERSION" >版本</th>
						<th field="CONTEXT" >上下文</th>
						<th field="NOTE" width='120px' editor="{type:'textarea'}">备注&nbsp;<font size="1" ></font></th>
						<th field="MODIFY_TIME">上次修改时间</th>
						<th field="MODIFIER">修改者</th>	
					</tr>
				</thead>
			</table>
	      </TD>
	    </TR>
	  </TBODY>
	</TABLE>
	</div>
</form>	
</body>
</html>
<script type="text/javascript">
	var activeOrderOid = "<%=oid%>";
	$(document).ready(function(){
		var bodyH = 500;
		$("#iframe1").attr("height",((bodyH-80)/2-15)+"px");
		$("#iframe2").attr("height",((bodyH-80)/2-20)+"px");
		initParentData();
	});
	function getBeforeDataParam(){
		var datas="";
		try {
			var dataGrid=pt.ui.get("data_TreeGrid_before");
			var dataslist = dataGrid.getRows();
			for(var i=0;i<dataslist.length;i++){
				datas+=dataslist[i].OID+",";
			}
			if(datas.length>0){
				datas= datas.substring(0,datas.length-1);
			}
		} catch (e) {}
		return datas;
	}
	function getAfterDataParam(){
		var datas="";
		try {
			var dataGrid=pt.ui.get("data_TreeGrid_after");
			var dataslist = dataGrid.getRows();
			for(var i=0;i<dataslist.length;i++){
				datas+=dataslist[i].OID+",";
			}
			if(datas.length>0){
				datas= datas.substring(0,datas.length-1);
			}
		} catch (e) {}
		return datas;
	}
	function addAfterDatas(dataObjects){

		var data_TreeGrid_after=pt.ui.get("data_TreeGrid_after");
		var data_TreeGrid_before=pt.ui.get("data_TreeGrid_before");
		var records=data_TreeGrid_before.getSelections();
		// alert(records[0]["OID"] + ";" + dataObjects[0]["OID"]);
		
		records[0]["OID"] = records[0]["OID"] + ";" + dataObjects[0]["OID"];
		records[0]["TYPE"] = dataObjects[0]["TYPE"];
		records[0]["NUMBER"] = dataObjects[0]["NUMBER"];
		records[0]["NAME"] = dataObjects[0]["NAME"];
		records[0]["VERSION"] = dataObjects[0]["VERSION"];
		records[0]["CONTEXT"] = dataObjects[0]["CONTEXT"];
		records[0]["NOTE"] = dataObjects[0]["NOTE"];
		records[0]["MODIFY_TIME"] = dataObjects[0]["MODIFY_TIME"];
		records[0]["MODIFIER"] = dataObjects[0]["MODIFIER"];
		data_TreeGrid_after.appendRow(records[0]);
	}	
	//初始化数据
	function initParentData(){
		var parentData = window.parent.getData();
		if(parentData != ""){
			addBeforeData(parentData);
		}
	}
	//添加收集器数据Before
	function addBeforeData(dataObject){
		plm.addDataToGrid("data_TreeGrid_before",dataObject);
	}
	//删除方法Before
	function deleteBeforeData(records){
		var data_table = pt.ui.get("data_TreeGrid_before");
		if(!plm.checkSelect(records)){
			return;
		}
		data_table.deleteRow(records);
		data_table.data.refresh();
	}

	//添加收集器数据After
	function addAfterData(data_new){
		plm.addDataToGrid("data_TreeGrid_after",dataObject);
	}

	//删除方法After
	function deleteAfterData(records){
		var data_table = pt.ui.get("data_TreeGrid_after");
		if(!plm.checkSelect(records)){
			return;
		}
		data_table.deleteRow(records);
		data_table.data.refresh();
	}

	function getPagesData_before(){
		return plm.getPagesData("data_TreeGrid_before");
	}	
	function getPagesData_after(){
		return plm.getPagesData("data_TreeGrid_after");
	}
	function divSelect_1(obj){
	var icon1 =$("#a_1").attr("class");
	var icon2 =$("#a_2").attr("class");
	if(icon1 == "showIcon"){
		$("#a_1").attr("class","hideIcon");		
		$("#div_1").hide();
		if(icon2 == "showIcon"){
			$("#iframe2").attr("height",(bodyH-80)+"px");
		}
	}else{
		$("#a_1").attr("class","showIcon");
		$("#div_1").show();
		if(icon2 == "showIcon"){
			$("#iframe1").attr("height",(bodyH-80)/2+"px");
			$("#iframe2").attr("height",(bodyH-80)/2+"px");
		}else{
			$("#iframe1").attr("height",(bodyH-80)+"px");
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
		}
	}else{
		$("#a_2").attr("class","showIcon");
		$("#div_2").show();
		if(icon1 == "showIcon"){
			$("#iframe1").attr("height",(bodyH-80)/2+"px");
			$("#iframe2").attr("height",(bodyH-80)/2+"px");
		}else{
			$("#iframe2").attr("height",(bodyH-80)+"px");
		}
	}
}
</script>