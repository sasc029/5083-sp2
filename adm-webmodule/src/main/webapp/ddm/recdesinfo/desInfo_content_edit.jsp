<%@page import="com.bjsasc.ddm.distribute.model.recdesinfo.RecDesInfo"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>
<%@page import="com.bjsasc.plm.core.util.DateTimeUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>

<%@page contentType = "text/html; charset=UTF-8" pageEncoding="GBK"%>
<% 
	String contextPath = request.getContextPath();
	String oid = request.getParameter("oid");
	String classId = RecDesInfo.CLASSID;
	String gridId = "distributeInfoGrid";
	String gridTitle = "销毁信息修改";
	String toolBarId = "ddm.distributeinfo.edit.toolbar";
	String gridUrl = contextPath + "/ddm/distribute/recDesInfo!getDesInfos.action?oid=" + oid;
	String updateUrl = contextPath + "/ddm/distribute/recDesInfo!updateDesInfos.action?oid=" + oid;
	String commonInfoTitle = "公共信息";
	String disMediaTypeURL = request.getContextPath()+"/plm/common/select/getSeclect.jsp?select=disMediaType";
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
<!-- 公共信息 结束 -->
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
<!-- 分发数据 开始 -->
			<table  height="564" width="100%" cellSpacing="0" cellPadding="0" border="0">
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
						<th field="DISINFONAME"  width="100" tips="接收单位/人员">接收单位/人员</th>
						<th field="DISMEDIATYPE" width="100" title="" filterType="select" data="inSite" textField="id"  valueField="id">分发类型</th>
						<th field="DISINFONUM"  width="100" tips="接收单位/人员">发放分数</th>
						<th field="RECOVERNUM"  width="100" tips="接收单位/人员">已回收份数</th>
						<th field="DESTROYNUM"  width="100" tips="接收单位/人员">已销毁份数</th>
						<th field="NEEDDESTROYNUM" 	 width="80"  tips="需要销毁份数" editor="{type:'text'}">销毁份数</th>
						<th field="NOTE" 		 width="207" tips="备注"     editor="{type:'text'}">备注</th>
					</thead>
				</table >
			</td></tr></table>
<!-- 分发数据 结束 -->	
</td></tr></table>
<input type="hidden" name="desInfoOids" id="desInfoOids"/>
<input type="hidden" name="needDestroyNums" id="needDestroyNums"/>
<input type="hidden" name="notes" id="notes"/>
<input type="hidden" name="dismediatypes" id="dismediatypes"/>
</form>
</body>
<script language="JavaScript" for="window" event="onresize" type="text/JavaScript">

	$(document).ready(function(){
		var grid = pt.ui.get("<%=gridId%>");
		//分发类型是“电子”，分发分数不可修改
		grid.on('beforecelledit', function(e){
			var DISMEDIATYPE = e.record.DISMEDIATYPE;
			var column = e.column;
			if(column.id=="needDestroyNum" && (DISMEDIATYPE=="电子" || DISMEDIATYPE=="1")){
				return false;
			}else if(column.id=="needDestroyNum" && (DISMEDIATYPE=="跨域" || DISMEDIATYPE=="2")){
				return false;
			}else{
				return true;
			}
			
			
		});
		//分发类型是“电子”，分发分数设为“1”
		grid.on('aftercelledit', function(e){
			var DISMEDIATYPE = e.record.DISMEDIATYPE;
			var column = e.column;
			var row =e.rowIndex;
			if(column.id=="DISMEDIATYPE" && (DISMEDIATYPE=="电子" || DISMEDIATYPE=="1")){
				grid.setColumnValue("needDestroyNum",1,row);
			}
			if(column.id=="DISMEDIATYPE" && (DISMEDIATYPE=="跨域" || DISMEDIATYPE=="2")){
				grid.setColumnValue("needDestroyNum",1,row);
			}
			/* if(column.id=="needDestroyNum" && (DISMEDIATYPE=="纸质" || DISMEDIATYPE=="0")){
				return true;
			} */
		});
		
		grid.on("beforecelledit",function(obj){
			var DISMEDIATYPE = obj.record.DISMEDIATYPE;
			var column = obj.column;
			if(column.id=="DISMEDIATYPE" && ((DISMEDIATYPE=="跨域" || DISMEDIATYPE=="2"))){
				return false;
			}
		});
	});
</script>
<script>
	var container = {};
	
	var inSite = [
	                {id: '0', name: '纸质'},
	                {id: '1', name: '电子'}
	            ];
	
  	function saveSubmitForm(){
  		// 验证
		<%=ValidateHelper.buildCheck()%>
		plm.startWait();
			
    	var oids = "";
    	var needDestroyNums = "";
    	var notes = "";
    	var dismediatypes="";
    	
		var gridObj = pt.ui.get("<%=gridId%>");
		/* var selections = gridObj.getRows(); */
		var selections = gridObj.getSelections();
		if (selections == null || selections.length == 0) {
			plm.endWait();
			plm. showMessage({title:'提示', message:"请至少选择1个操作对象!", icon:'3'});
			return;
		}
		for(var i = 0; i < selections.length; i++){
			var oidArr = selections[i]["OIDS"];
			var disInfoName = selections[i]["DISINFONAME"];
			var needDestroyNum = selections[i]["NEEDDESTROYNUM"];
			var note = selections[i]["NOTE"];
			var dismediatype=selections[i]["DISMEDIATYPE"];
			//var recoverNum = selections[i]["RECOVERNUM"];
			var disInfoNum = selections[i]["DISINFONUM"];
			var destroyNum = selections[i]["DESTROYNUM"];
			if(note == ""){
				notes += " ,";
			}else{
				notes += note +=",";
			}
			if(oids != ""){
				oids += "!";
				needDestroyNums += ",";
				dismediatypes += ",";
			}
			oids += oidArr;
			needDestroyNums += needDestroyNum;

			dismediatypes+=dismediatype;
			if (null == needDestroyNum || needDestroyNum == undefined || needDestroyNum.length == 0) {
				plm.endWait();
				alert("分发份数不能为空！");
				return false;
			}
			if (parseInt(needDestroyNum) == 0) {
				plm.endWait();
				alert("分发份数不能为 0 ！");
				return false;
			}
			var numReg=/^\d+$/;
			if (!numReg.test(needDestroyNum)) {
				plm.endWait();
				alert("分发份数不是数字！");
				return false;
			} 
			
			//验证需要销毁份数<=已发放份数-已销毁份数
			if((parseInt(disInfoNum) - parseInt(destroyNum)) < parseInt(needDestroyNum)){
				var mess = disInfoName+"-可销毁的剩余份数："+(parseInt(disInfoNum) - parseInt(destroyNum))+"份";
				plm.showMessage({title:'错误提示', message:mess, icon:'1'});
				return false;
			}
		}
		oids = oids.substring(0, oids.length);
		$("#desInfoOids").val(oids);
		needDestroyNums = needDestroyNums.substring(0, needDestroyNums.length);
		$("#needDestroyNums").val(needDestroyNums);
		notes = notes.substring(0, notes.length);
		$("#notes").val(notes);
		dismediatypes = dismediatypes.substring(0, dismediatypes.length);
		$("#dismediatypes").val(dismediatypes);
		$.ajax({
				type: "post",
				url: "<%=updateUrl%>",
				dataType: "json",
				data: $("#main_form").serializeArray(),
				success: function(result){
					plm.endWait();
					if(result.SUCCESS != null &&result.SUCCESS == "false"){
						plm.showAjaxError(result);
						return;
					}
					//plm.showMessage({title:'提示', message:"保存成功!", icon:'2'});
					cancleButton();
				},
				error:function(){
					plm.endWait();
					plm.showMessage({title:'错误提示', message:"对不起，数据请求错误", icon:'1'});
				}
			});	
  	}

	// 重新加载
	function reload(text){
		var table = pt.ui.get("<%=gridId%>");
		table.reload();
	}
		function cancleButton(){
		//window.location.href = "<%=contextPath%>/ddm/distribute/recDesInfo!getAllDesInfo.action";
		opener.reload();
		window.close();
  	}
</script>
</html>