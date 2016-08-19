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
	String gridTitle = "�ַ���Ϣ";
	String toolBarId = "ddm.distributeinfo.edit.toolbar";
	String gridUrl = contextPath + "/ddm/distribute/distributeInfoHandle!editDistributeInfos.action?oid=" + oid;
	String updateUrl = contextPath + "/ddm/distribute/distributeInfoHandle!updateDistributeInfo.action?oid=" + oid;
	String commonInfoTitle = "������Ϣ";
	String disMediaTypeURL = request.getContextPath()+"/plm/common/select/getSeclect.jsp?select=disMediaType";
	String deadLineDate = DateTimeUtil.getCurrentDate(ConstUtil.C_DISDEADLINE_DELAY_DAY);
	String disUrgent = (String)request.getParameter("disUrgent");
	String deadDate = (String)request.getParameter("deadLineDate");
	String disStyle = (String)request.getParameter("disStyle");
	if(deadDate == null){
		deadDate = DateTimeUtil.getCurrentDate(ConstUtil.C_DISDEADLINE_DELAY_DAY);
		//deadDate = "";
	}
	String qReceive_sel = "checked=\"true\"";
	String qReceive_cus = "";
	String disStyle_0 = "checked=\"true\"";
	String disStyle_1 = "";
	if ("1".equals(disUrgent)) {
		qReceive_sel = "";
		qReceive_cus = "checked=\"true\"";
	}
	if ("1".equals(disStyle)) {
		disStyle_0 = "";
		disStyle_1 = "checked=\"true\"";
	}
	qReceive_sel += " disabled=\"true\"";
	qReceive_cus += " disabled=\"true\"";
	disStyle_0 += " disabled=\"true\"";
	disStyle_1 += " disabled=\"true\"";
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
<table cellSpacing="0" cellPadding="0" width="100%" border="0">
	<tr><td>
		<!-- ������Ϣ ��ʼ -->
		<table width="100%" cellSpacing="0" cellPadding="0" border="0">
			<tr class="AvidmToolbar">
				<td nowrap="nowrap">
					<div class="AvidmMtop5 twoTitle">
						<a class="showIcon" href="#" onclick="divSelected(this);">
						<img src="<%=request.getContextPath()%>/plm/images/common/space.gif"></a>
						<%=commonInfoTitle%>
					</div>
				</td>
			</tr>
			<tr><td>
				<table border="0" cellspacing="0" cellpadding="0" class="avidmTable">
					<tr>
					<td class="left_col AvidmW100">�깤���ޣ�</td>
					<td class="e-checked-text">
						<INPUT type='text' id="deadLineDate"  name="deadLineDate" value="<%=deadLineDate%>"
			        		class="Wdate pt-textbox"  readonly="readonly"  style="width:100px;" <%=ValidateHelper.buildValidator()%>
			        		onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'2128-03-10'})"/>&nbsp;
					</td>
					<td class="left_col AvidmW100">�����̶ȣ�</td>
					<td class="e-checked-text">
						<input value=0  type=radio name=disUrgent id=disUrgent0  <%=qReceive_sel%>/>
						��ͨ&nbsp;
						<input value=1 type=radio name=disUrgent id=disUrgent1 <%=qReceive_cus%> />
						�Ӽ�&nbsp;&nbsp;
					</td>
					<td class="left_col AvidmW100">�ַ���ʽ��</td>
					<td class="e-checked-text">
						<input value=0  type="radio" name="disStyle" id="disStyle0" <%=disStyle_0%>/>
						��ʽ�ַ�&nbsp;
						<input value=1 type="radio" name="disStyle" id="disStyle1" <%=disStyle_1%>/> 
						һ���Էַ�&nbsp;&nbsp;
					</td>
					</tr>
				</table>
		</td></tr></table>
<!-- ������Ϣ ���� -->
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
			<table height="482" width="100%" cellSpacing="0" cellPadding="0" border="0">
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
						<th field="DISINFONAME"  width="100" tips="���յ�λ/��Ա">���յ�λ/��Ա</th>
						<th field="DISTYPE"      width="100" tips="�ַ���ʽ">�ַ���ʽ</th>
				         <!-- 
				         <th field="DISMEDIATYPE" width="100" tips="�ַ�����">�ַ�����</th>
				         -->
						<%-- <th field="DISMEDIATYPE" width="100" editor="{type:'combo',url:'<%=disMediaTypeURL%>',valueField:'value',textField:'text'}" formatter="selectFormatter()">�ַ�����</th> --%>
					   	<th field="DISMEDIATYPE" width="100" title="" filterType="select" data="inSite" textField="id"  valueField="id" editor="{type:'combo',data:inSite,textField:'name',valueField:'name'}">�ַ�����</th>
					   	<th field="DISINFONUM" 	 width="80"  tips="�ַ�����" editor="{type:'text'}">�ַ�����</th>
					   	<th field="NOTE" 		 width="207" tips="��ע"     editor="{type:'text'}">��ע</th>
					   	<!-- <th field="OID"  hidden="true">OID</th>
					   	<th field="OIDS" hidden="true">OIDS</th> -->
					</thead>
				</table >
			</td></tr></table>
<!-- �ַ����� ���� -->	
</td></tr></table>
<input type="hidden" name="distributeInfoOids" id="distributeInfoOids"/>
<input type="hidden" name="disInfoNums" id="disInfoNums"/>
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
			if(column.id=="DISINFONUM" && (DISMEDIATYPE=="����" || DISMEDIATYPE=="1")){
				return false;
			}else if(column.id=="DISINFONUM" && (DISMEDIATYPE=="����" || DISMEDIATYPE=="2")){
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
				grid.setColumnValue("DISINFONUM",1,row);
			}
			if(column.id=="DISMEDIATYPE" && (DISMEDIATYPE=="����" || DISMEDIATYPE=="2")){
				grid.setColumnValue("DISINFONUM",1,row);
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
    	var disInfoNums = "";
    	var notes = "";
    	var dismediatypes="";
    	
		var gridObj = pt.ui.get("<%=gridId%>");
		var selections = gridObj.getRows(); 
	
    	for(var i = 0; i < selections.length; i++){
			var oid = selections[i]["OIDS"];
			var disInfoNum = selections[i]["DISINFONUM"];
			var note = selections[i]["NOTE"];
			var dismediatype=selections[i]["DISMEDIATYPE"];
			if(note == ""){
				note = "null";
			}
			if(oids != ""){
				oids += ";";
				disInfoNums += ",";
				notes += ",";
				dismediatypes += ",";
			}
			oids += oid;
			disInfoNums += disInfoNum;
			notes += note;
			dismediatypes+=dismediatype;
			if (null == disInfoNum || disInfoNum == undefined || disInfoNum.length == 0) {
				plm.endWait();
				alert("�ַ���������Ϊ�գ�");
				return false;
			}
			if (parseInt(disInfoNum) == 0) {
				plm.endWait();
				alert("�ַ���������Ϊ 0 ��");
    	    	return false;
    		}
			var numReg=/^\d+$/;
			if (!numReg.test(disInfoNum)) {
				plm.endWait();
				alert("�ַ������������֣�");
    	    	return false;
    		} 
    	}
		$("#distributeInfoOids").val(oids);
		$("#disInfoNums").val(disInfoNums);
		$("#notes").val(notes);
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
  		window.location.href = "<%=contextPath%>/ddm/distribute/distributeInfoHandle!getAllDistributeInfo.action";
  	}
</script>
</html>