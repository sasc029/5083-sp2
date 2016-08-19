<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>
<%@page import="com.bjsasc.plm.core.util.DateTimeUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo"%>

<%@page contentType = "text/html; charset=UTF-8" pageEncoding="GBK"%>
<% 
	String contextPath = request.getContextPath();
	String oid = (String) session.getAttribute(ConstUtil.DISTRIBUTE_ELECTASK_OID);
	String classId = DistributeInfo.CLASSID;
	String gridId = "distributeInfoGrid";
	String gridTitle = "分发信息";
	String toolBarId = "ddm.distributeinfo.edit.toolbar";
	String updateUrl = contextPath + "/ddm/distribute/distributeElecTaskHandle!updateDistributeInfos.action";
	String spot = "ListDistributeInfos";
	String gridUrl = contextPath + "/ddm/distribute/distributeElecTaskHandle!getDistributeForwardElecTaskEditInfo.action?oid="+oid+"&type=edit";
%>
<html >
<head>
	<title>工作区列表</title>
	<script type="text/javascript"	src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
	<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath%>/plm/ui/editor/date/WdatePicker.js"></script>
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
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0"  scroll="no">
<form id='main_form'>
<input type="hidden" name="CLASSID" id="CLASSID" value="<%=classId%>"/>
<table cellSpacing="0" cellPadding="0" height="100%" width="100%" border="0">
	<tr><td height="1%">
		<div id="gridTop" name="gridTop"><div>
	</td></tr>
			<tr class="AvidmToolbar">
				<td nowrap="nowrap">
					<div class="AvidmMtop5 twoTitle">
						<a class="showIcon" href="#" onclick="divSelected(this);">
						<img src="<%=request.getContextPath()%>/plm/images/common/space.gif"></a>
						<%=gridTitle%>
					</div>
				</td>
			</tr>
			<tr><td>
			<table  height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0">
			<tr><td height="1%">
			<table cellSpacing="0" cellPadding="0" width="100%" border="0" style="border-right:#9cbdff solid 1px;border-left:#9cbdff solid 1px;"><tr class="AvidmToolbar"><td> 
				<div class="pt-toolbar">
					<%=UIHelper.getToolBar(toolBarId)%>
				</div> 
			</td></tr></table>			
			</td></tr><tr><td valign="top">
				<table id="<%=gridId%>" class="pt-grid" singleSelect="false" checkbox="true" url="<%=gridUrl%>"
						fit="true" width="100%" pagination="false" rownumbers='true'>
					<thead>
					<th field="DISINFONAME" width="100" tips="接收单位/人员" sortable='true' >接收单位/人员</th>
					<th field="DISINFOTYPE" width="100" tips="分发信息类型" sortable='true' >分发信息类型</th>
					<th field="DISINFONUM" width="80" tips="份数" sortable='true' >份数</th>
					<th field="DISMEDIATYPE" width="80" tips="分发类型" sortable='true' >分发类型</th>
					<th field="DISTYPE" width="80" tips="分发方式" sortable='true' >分发方式</th>
					<th field="NOTE" width="250" tips="备注" editor="{type:'text'}" >备注</th>
					</thead>
				</table >
			</td></tr></table>
<!-- 分发数据 结束 -->	
</td></tr></table>
<input type="hidden" name="notes" id="notes"/>
<input type="hidden" name="distributeInfoOids" id="distributeInfoOids"/>
<input type="hidden" name="distributeOrderObjectLinkOids" id="distributeOrderObjectLinkOids"/>
</form>
</body>
<script>
	var container = {};

	function saveSubmitForm(){
		// 验证
		<%=ValidateHelper.buildCheck()%>
			
    	var oids = "";
    	var notes = "";
    	
		var gridObj = pt.ui.get("<%=gridId%>");
		//gridObj.selectAll();
		//var selections = gridObj.getRows();
		var selections = gridObj.getSelections();
		if (selections == null || selections.length == 0) {
			plm. showMessage({title:'提示', message:"请至少选择1个操作对象!", icon:'3'});
			return;
		}
		for(var i = 0; i < selections.length; i++){
    		var oid = selections[i]["OIDS"];
			var note = selections[i]["NOTE"];
			if(note == "" || note===undefined){
				note = "null";
			}
			if(oids != ""){
				oids += ";";
				notes += ",";
			}
			oids += oid;
			notes += note;
    	}
    	plm.startWait();
    	$.ajax({
			type: "post",
			url: "<%=updateUrl%>",
			dataType: "json",
			data: "<%= KeyS.OIDS%>="+oids+"&notes="+notes,
			success: function(result){
				plm.endWait();
				if(result.SUCCESS != null &&result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				cancleButton();
		   },
		   error:function(){
			   plm.endWait();
			   plm.showMessage({title:'错误提示', message:"修改分发信息失败！", icon:'1'});
		   }
		});
  	}
    
    // 重新加载
	function reload(text){
		var table = pt.ui.get("<%=gridId%>");
		table.reload();
	}
  	function cancleButton(){
  		window.location.href = "<%=contextPath%>/ddm/distribute/distributeElecTaskHandle!getDistributeForwardElecTaskInfo.action?&oid=<%=oid%>&type=0";
  	}
</script>
</html>