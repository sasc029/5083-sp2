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
	String gridTitle = "������Ϣ�޸�";
	String toolBarId = "ddm.distributeinfo.edit.toolbar";
	String gridUrl = contextPath + "/ddm/distribute/recDesInfo!getRecInfos.action?oid=" + oid;
	String updateUrl = contextPath + "/ddm/distribute/recDesInfo!updateRecInfos.action?oid=" + oid;
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
	var tr = $(obj).parent();//��ǰtitle���ڵ�tr����
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
						<th field="DISINFONAME"  width="110" tips="���յ�λ/��Ա">���յ�λ/��Ա</th>
						<th field="DISMEDIATYPE" width="100" title="" filterType="select" data="inSite" textField="id"  valueField="id">�ַ�����</th>
						<th field="DISINFONUM"  width="100" tips="�ַ�����">�ַ�����</th>
						<th field="RECOVERNUM"  width="100" tips="�ѻ��շ���">�ѻ��շ���</th>
						<th field="NEEDRECOVERNUM" 	 width="100"  tips="���շ���" editor="{type:'text'}">���շ���</th>
						<th field="NOTE" 		 width="241" tips="��ע"     editor="{type:'text'}">��ע</th>
					</thead>
				</table >
			</td></tr></table>
<!-- �ַ����� ���� -->	
</td></tr></table>
<input type="hidden" name="recInfoOids" id="recInfoOids"/>
<input type="hidden" name="needRecoverNums" id="needRecoverNums"/>
<input type="hidden" name="notes" id="notes"/>
<input type="hidden" name="dismediatypes" id="dismediatypes"/>
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
		plm.startWait();
			
    	var oids = "";
    	var needRecoverNums = "";
    	var notes = "";
    	var dismediatypes="";
    	
		var gridObj = pt.ui.get("<%=gridId%>");
		var selections = gridObj.getSelections();
		if (selections == null || selections.length == 0) {
			plm.endWait();
			plm. showMessage({title:'��ʾ', message:"������ѡ��1����������!", icon:'3'});
			return;
		}
		for(var i = 0; i < selections.length; i++){
			var oidArr = selections[i]["OIDS"];
			var disInfoName = selections[i]["DISINFONAME"];
			var needRecoverNum = selections[i]["NEEDRECOVERNUM"];
			var note = selections[i]["NOTE"];
			var dismediatype=selections[i]["DISMEDIATYPE"];
			var disInfoNum=selections[i]["DISINFONUM"];
			var recoverNum=selections[i]["RECOVERNUM"];

			if(note == ""){
				note = " ,";
			}
			if(oids != ""){
				oids += "!";
				needRecoverNums += ",";
				notes += ",";
				dismediatypes += ",";
			}
			oids += oidArr;
			needRecoverNums += needRecoverNum;
			notes += note;
			dismediatypes += dismediatype;
			if (null == needRecoverNum || needRecoverNum == undefined || needRecoverNum.length == 0) {
				plm.endWait();
				alert("���շ�������Ϊ�գ�");
				return false;
			}

			if (parseInt(needRecoverNum) == 0) {
				plm.endWait();
				alert("���շ�������Ϊ 0 ��");
				return false;
			}

			var numReg=/^\d+$/;
			if (!numReg.test(needRecoverNum)) {
				plm.endWait();
				alert("���շ����������֣�");
				return false;
			} 

			//��Ҫ���շ���<=�ܷ��ŷ���-�ѻ��շ���
			if((parseInt(disInfoNum)-parseInt(recoverNum))<parseInt(needRecoverNum)){
				plm.endWait();
				var mess = disInfoName+"-�ɻ��յ�ʣ�������"+(parseInt(disInfoNum)-parseInt(recoverNum))+"��";
				plm.showMessage({title:'������ʾ', message:mess, icon:'1'});
				return false;
			}
		}
		oids = oids.substring(0, oids.length);
		$("#recInfoOids").val(oids);
		needRecoverNums = needRecoverNums.substring(0, needRecoverNums.length);
		$("#needRecoverNums").val(needRecoverNums);
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
  		//window.location.href = "<%=contextPath%>/ddm/distribute/recDesInfo!getAllRecInfo.action";
		opener.reload();
		window.close();
  	}
</script>
</html>