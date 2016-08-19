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
	String gridTitle = "������Ϣ���";
	String toolBarId = "ddm.distributeinfo.edit.toolbar";
	String gridUrl = contextPath + "/ddm/distribute/recDesInfo!getAllNeddDesInfos.action?oid=" + oid;
	String commonInfoTitle = "������Ϣ";
	String disMediaTypeURL = request.getContextPath()+"/plm/common/select/getSeclect.jsp?select=disMediaType";

%>
<html >
<head>
	<title>�������б�</title>
	<script type="text/javascript"	src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
	<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath%>/plm/ui/editor/date/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
<script type="text/javascript">
function divSelected(obj){
	var tr = $(obj).parent().parent().parent();//��ǰtitle���ڵ�tr����
	//���Ʊ�����չʾ
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
	
	
	//����title��ʽ���Ӽ����л���
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
<!-- �ַ����� ��ʼ -->
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
						<th field="DISINFONAME"  width="100" tips="���յ�λ/��Ա">���յ�λ/��Ա</th>
						<th field="DISINFONUM"  width="100" tips="���ŷ���">���ŷ���</th>
						<th field="RECOVERNUM"  width="100" tips="�ѻ��շ���">�ѻ��շ���</th>
						<th field="DESTROYNUM"  width="100" tips="���ٷ���">�����ٷ���</th>
						<th field="NEEDDESTROYNUM" 	 width="80"  tips="���ٷ���" editor="{type:'text'}">���ٷ���</th>
						<th field="DISMEDIATYPE" width="100" title="" filterType="select" data="inSite" textField="id"  valueField="id">�ַ�����</th>
						<th field="NOTE" 		 width="207" tips="��ע"     editor="{type:'text'}">��ע</th>
					</thead>
				</table >
			</td></tr></table>
<!-- �ַ����� ���� -->	
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
		//�ַ������ǡ����ӡ����ַ����������޸�
		grid.on('beforecelledit', function(e){
			var DISMEDIATYPE = e.record.DISMEDIATYPE;
			var column = e.column;
			if(column.id=="NEEDRECOVERNUM" && (DISMEDIATYPE=="����" || DISMEDIATYPE=="1")){
				return false;
			}else if(column.id=="NEEDRECOVERNUM" && (DISMEDIATYPE=="����" || DISMEDIATYPE=="2")){
				return false;
			}else{
				return true;
			}
			
			
		});
		//�ַ������ǡ����ӡ����ַ�������Ϊ��1��
		grid.on('aftercelledit', function(e){
			var DISMEDIATYPE = e.record.DISMEDIATYPE;
			var column = e.column;
			var row =e.rowIndex;
			if(column.id=="DISMEDIATYPE" && (DISMEDIATYPE=="����" || DISMEDIATYPE=="1")){
				grid.setColumnValue("NEEDRECOVERNUM",1,row);
			}
			if(column.id=="DISMEDIATYPE" && (DISMEDIATYPE=="����" || DISMEDIATYPE=="2")){
				grid.setColumnValue("NEEDRECOVERNUM",1,row);
			}
			/* if(column.id=="DISINFONUM" && (DISMEDIATYPE=="ֽ��" || DISMEDIATYPE=="0")){
				return true;
			} */
		});
		
		grid.on("beforecelledit",function(obj){
			var DISMEDIATYPE = obj.record.DISMEDIATYPE;
			var column = obj.column;
			if(column.id=="DISMEDIATYPE" && ((DISMEDIATYPE=="����" || DISMEDIATYPE=="2"))){
				return false;
			}
		});
	});
</script>
<script>
	var container = {};
	
	var inSite = [
					{id: '0', name: 'ֽ��'},
					{id: '1', name: '����'}
					];
	
	function saveSubmitForm(){
		// ��֤
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
			plm. showMessage({title:'��ʾ', message:"������ѡ��1����������!", icon:'3'});
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
				alert("���ٷ�������Ϊ�գ�");
				return;
			}

			if (parseInt(needDestroyrNum) == 0) {
				alert("���ٷ�������Ϊ 0 ��");
				return;
			}

			var numReg=/^\d+$/;
			if (!numReg.test(needDestroyrNum)) {
				alert("���ٷ����������֣�");
				return;
			}

			//���ٷ���<=���ŷ���-�����ٷ�����
			if((parseInt(disInfoNum) - parseInt(destroyNum)) < parseInt(needDestroyrNum)){
				var mess = disInfoName+"-�����ٵ�ʣ�������"+(parseInt(disInfoNum) - parseInt(destroyNum))+"��";
				plm.showMessage({title:'������ʾ', message:mess, icon:'1'});
				return;
			}

			//�ַ���Ϣ���ƣ���λ/��Ա��
			if(null != disInfoName || disInfoName != undefined || disInfoName.length > 0){
				disInfoNames += disInfoName + ",";
			}else{
				disInfoNames +=" ,";
			}

			//�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
			if(null != disInfoType || disInfoType != undefined || disInfoType.length > 0){
				disInfoTypes += disInfoType + ",";
			}else{
				disInfoTypes +=" ,";
			}

			//�ַ���ϢIID����Ա����֯���ڲ���ʶ��
			if(null == disInfoId || disInfoId == undefined || disInfoId.length == 0){
				disInfoIds += " ,";
			}else{
				disInfoIds += disInfoId + ",";
			}

			//�������ٵ���ַ�����LINK�ڲ���ʶ
			if( null == disOrderObjLinkId || disOrderObjLinkId == undefined || disOrderObjLinkId.length == 0){
				disOrderObjLinkIds += " ,";
			}else{
				disOrderObjLinkIds += disOrderObjLinkId + ",";
			}

			//�������ٵ���ַ�����LINK���ʶ 
			if(null == disOrderObjLinkClassId || disOrderObjLinkClassId == undefined || disOrderObjLinkClassId.length == 0){
				disOrderObjLinkClassIds += " ,";
			}else{
				disOrderObjLinkClassIds += disOrderObjLinkClassId + ",";
			}

			//���ŷ���
			if(null == disInfoNum || disInfoNum == undefined || disInfoNum.length == 0){
				disInfoNums += " ,";
			}else{
				disInfoNums += disInfoNum + ",";
			}

			//�ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
			if(null == disMediaType || disMediaType == undefined || disMediaType.length == 0){
				disMediaTypes += " ,";
			}else{
				disMediaTypes += disMediaType + ",";
			}

			//��Ҫ���ٷ���
			if(null == needDestroyrNum || needDestroyrNum == undefined || needDestroyrNum.length == 0){
				needDestroyNums += " ,";
			}else{
				needDestroyNums += needDestroyrNum + ",";
			}

			//�����ٷ���
			if(null == destroyNum || destroyNum == undefined || destroyNum.length == 0){
				destroyNums += " ,";
			}else{
				destroyNums += destroyNum + ",";
			}

			//�ѻ��շ���
			if(null == recoverNum || recoverNum == undefined || recoverNum.length == 0){
				recoverNums += " ,";
			}else{
				recoverNums += recoverNum + ",";
			}

			//��ע
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
					//plm.showMessage({title:'��ʾ', message:"����ɹ�!", icon:'2'});
					cancleButton();
				},
				error:function(){
					plm.endWait();
					plm.showMessage({title:'������ʾ', message:"�Բ��������������", icon:'1'});
				}
			});	
		}

	// ���¼���
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