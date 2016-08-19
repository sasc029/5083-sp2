<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>
<%@page import="com.bjsasc.plm.core.util.DateTimeUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo"%>

<%@page contentType = "text/html; charset=UTF-8" pageEncoding="GBK"%>
<% 
	String contextPath = request.getContextPath();
	String oid = request.getParameter("oid");
	String classId = DistributeInfo.CLASSID;
	String gridId = "distributeInfoGrid";
	String gridTitle = "销毁信息添加";
	String toolBarId = "ddm.distributeinfo.edit.toolbar";
	String gridUrl = contextPath + "/ddm/distribute/recDesInfo!getAllNeddDesInfos.action?oid=" + oid;
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
						fit="true" width="100%" pagination="false" rownumbers='true' style="scroll:auto">
					<thead>
						<th field="DISINFONAME"  width="100" tips="接收单位/人员">接收单位/人员</th>
						<th field="DISINFONUM"  width="100" tips="发放份数">发放份数</th>
						<th field="RECOVERNUM"  width="100" tips="已回收份数">已回收份数</th>
						<th field="DESTROYNUM"  width="100" tips="销毁份数">已销毁份数</th>
						<th field="NEEDDESTROYNUM" 	 width="80"  tips="销毁份数" editor="{type:'text'}">销毁份数</th>
						<th field="DISMEDIATYPE" width="100" title="" filterType="select" data="inSite" textField="id"  valueField="id">分发类型</th>
						<th field="NOTE" 		 width="207" tips="备注"     editor="{type:'text'}">备注</th>
					</thead>
				</table >
			</td></tr></table>
<!-- 分发数据 结束 -->	
</td></tr></table>
		<input type="hidden" name="disInfoNames" id="disInfoNames"/>
		<input type="hidden" name="disInfoIds" id="disInfoIds"/>
		<input type="hidden" name="disOrderObjLinkIds" id="disOrderObjLinkIds"/>
		<input type="hidden" name="disOrderObjLinkClassIds" id="disOrderObjLinkClassIds"/>
		<input type="hidden" name="disMediaTypes" id="disMediaTypes"/>
		<input type="hidden" name="disInfoNums" id="disInfoNums"/>
		<input type="hidden" name="needDestroyNums" id="needDestroyNums"/>
		<input type="hidden" name="destroyNums" id="destroyNums"/>
		<input type="hidden" name="recoverNums" id="recoverNums"/>
		<input type="hidden" name="notes" id="notes"/>
		<input type="hidden" name="disInfoTypes" id="disInfoTypes"/>
</form>
</body>
<script language="JavaScript" for="window" event="onresize" type="text/JavaScript">

	$(document).ready(function(){
		var grid = pt.ui.get("<%=gridId%>");
		//分发类型是“电子”，分发分数不可修改
		grid.on('beforecelledit', function(e){
			var DISMEDIATYPE = e.record.DISMEDIATYPE;
			var column = e.column;
			if(column.id=="NEEDRECOVERNUM" && (DISMEDIATYPE=="电子" || DISMEDIATYPE=="1")){
				return false;
			}else if(column.id=="NEEDRECOVERNUM" && (DISMEDIATYPE=="跨域" || DISMEDIATYPE=="2")){
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
				grid.setColumnValue("NEEDRECOVERNUM",1,row);
			}
			if(column.id=="DISMEDIATYPE" && (DISMEDIATYPE=="跨域" || DISMEDIATYPE=="2")){
				grid.setColumnValue("NEEDRECOVERNUM",1,row);
			}
			/* if(column.id=="DISINFONUM" && (DISMEDIATYPE=="纸质" || DISMEDIATYPE=="0")){
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
		var updateUrl = "<%=contextPath%>/ddm/distribute/recDesInfo!addNeddDesInfos.action";
		var disInfoNames = "";
		var disInfoIds = "";
		var disOrderObjLinkIds = "";
		var disOrderObjLinkClassIds = "";
		var disMediaTypes = "";
		var disInfoNums = "";
		var needDestroyNums = "";
		var recoverNums = "";
		var destroyNums = "";
		var notes = "";
		var disInfoTypes = "";

		var gridObj = pt.ui.get("<%=gridId%>");
		var selections = gridObj.getSelections();

		if (selections == null || selections.length == 0) {
			plm. showMessage({title:'提示', message:"请至少选择1个操作对象!", icon:'3'});
			return;
		}
		for(var i = 0; i < selections.length; i++){
			var disInfoName = selections[i]["DISINFONAME"];
			var disInfoId = selections[i]["DISINFOID"];
			var disOrderObjLinkId = selections[i]["DISORDEROBJECTLINKID"];
			var disOrderObjLinkClassId = selections[i]["DISORDEROBJECTLINKCLASSID"];
			var disInfoNum = selections[i]["DISINFONUM"];
			var destroyNum = selections[i]["DESTROYNUM"];
			var disMediaType = selections[i]["DISMEDIATYPE"];
			var needDestroyrNum = selections[i]["NEEDDESTROYNUM"];
			var recoverNum = selections[i]["RECOVERNUM"];
			var note = selections[i]["NOTE"];
			var disInfoType = selections[i]["DISINFOTYPE"];

			if (null == needDestroyrNum || needDestroyrNum == undefined || needDestroyrNum.length == 0) {
				alert("销毁份数不能为空！");
				return;
			}

			if (parseInt(needDestroyrNum) == 0) {
				alert("销毁份数不能为 0 ！");
				return;
			}

			var numReg=/^\d+$/;
			if (!numReg.test(needDestroyrNum)) {
				alert("销毁份数不是数字！");
				return;
			}

			//销毁份数<=发放份数-已销毁份数！
			if((parseInt(disInfoNum) - parseInt(destroyNum)) < parseInt(needDestroyrNum)){
				var mess = disInfoName+"-可销毁的剩余份数："+(parseInt(disInfoNum) - parseInt(destroyNum))+"份";
				plm.showMessage({title:'错误提示', message:mess, icon:'1'});
				return;
			}

			//分发信息名称（单位/人员）
			if(null != disInfoName || disInfoName != undefined || disInfoName.length > 0){
				disInfoNames += disInfoName + ",";
			}else{
				disInfoNames +=" ,";
			}

			//分发信息类型（0为单位，1为人员）
			if(null != disInfoType || disInfoType != undefined || disInfoType.length > 0){
				disInfoTypes += disInfoType + ",";
			}else{
				disInfoTypes +=" ,";
			}

			//分发信息IID（人员或组织的内部标识）
			if(null == disInfoId || disInfoId == undefined || disInfoId.length == 0){
				disInfoIds += " ,";
			}else{
				disInfoIds += disInfoId + ",";
			}

			//回收销毁单与分发数据LINK内部标识
			if( null == disOrderObjLinkId || disOrderObjLinkId == undefined || disOrderObjLinkId.length == 0){
				disOrderObjLinkIds += " ,";
			}else{
				disOrderObjLinkIds += disOrderObjLinkId + ",";
			}

			//回收销毁单与分发数据LINK类标识 
			if(null == disOrderObjLinkClassId || disOrderObjLinkClassId == undefined || disOrderObjLinkClassId.length == 0){
				disOrderObjLinkClassIds += " ,";
			}else{
				disOrderObjLinkClassIds += disOrderObjLinkClassId + ",";
			}

			//发放份数
			if(null == disInfoNum || disInfoNum == undefined || disInfoNum.length == 0){
				disInfoNums += " ,";
			}else{
				disInfoNums += disInfoNum + ",";
			}

			//分发介质类型（0为纸质，1为电子，2为跨域
			if(null == disMediaType || disMediaType == undefined || disMediaType.length == 0){
				disMediaTypes += " ,";
			}else{
				disMediaTypes += disMediaType + ",";
			}

			//需要销毁份数
			if(null == needDestroyrNum || needDestroyrNum == undefined || needDestroyrNum.length == 0){
				needDestroyNums += " ,";
			}else{
				needDestroyNums += needDestroyrNum + ",";
			}

			//已销毁份数
			if(null == destroyNum || destroyNum == undefined || destroyNum.length == 0){
				destroyNums += " ,";
			}else{
				destroyNums += destroyNum + ",";
			}

			//已回收份数
			if(null == recoverNum || recoverNum == undefined || recoverNum.length == 0){
				recoverNums += " ,";
			}else{
				recoverNums += recoverNum + ",";
			}

			//备注
			if(null == note || note == undefined || note.length == 0){
				notes += " ,";
			}else{
				notes += note + ",";
			}

		}
		//disInfoNames = disInfoNames.substring(0, disInfoNames.length - 1);
		//disInfoIds = disInfoIds.substring(0, disInfoIds.length - 1);
		disOrderObjLinkIds = disOrderObjLinkIds.substring(0, disOrderObjLinkIds.length - 1);
		disOrderObjLinkClassIds = disOrderObjLinkClassIds.substring(0, disOrderObjLinkClassIds.length - 1);
		disMediaTypes = disMediaTypes.substring(0, disMediaTypes.length - 1);
		disInfoNums = disInfoNums.substring(0, disInfoNums.length - 1);
		recoverNums = recoverNums.substring(0, recoverNums.length - 1);
		destroyNums = destroyNums.substring(0, destroyNums.length - 1);
		//notes = notes.substring(0, notes.length - 1);
		$("#disInfoNames").val(disInfoNames);
		$("#disInfoIds").val(disInfoIds);
		$("#disOrderObjLinkIds").val(disOrderObjLinkIds);
		$("#disOrderObjLinkClassIds").val(disOrderObjLinkClassIds);
		$("#disMediaTypes").val(disMediaTypes);
		$("#disInfoNums").val(disInfoNums);
		$("#recoverNums").val(recoverNums);
		$("#needDestroyNums").val(needDestroyNums);
		$("#destroyNums").val(destroyNums);
		$("#notes").val(notes);
		$("#disInfoTypes").val(disInfoTypes);

		plm.startWait();

		$.ajax({
				type: "post",
				url: updateUrl,
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
		opener.reload();
		window.close();
		
	}
</script>
</html>