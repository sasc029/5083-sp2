<%@page import="com.cascc.platform.aa.api.util.AADomainUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.bjsasc.plm.core.session.SessionHelper"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page language="java"%>
<%@page session="true"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@include file="/plm/plm.jsp"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.plm.core.persist.PersistHelper"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.core.system.principal.User"%>
<%@page import="com.bjsasc.plm.core.system.principal.Principal"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.core.context.ContextHelper"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="com.bjsasc.plm.operate.ModelUtil"%>
<%@page import="com.bjsasc.plm.grid.GridUtil"%>
<%@page import="com.bjsasc.plm.KeyS"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>

<%@include file="/ddm/public/ddm.jsp" %>

<%
	request.setCharacterEncoding("UTF-8");
	String title = "";
	String receiverName = "";
	//spot
	String spot = "ListDistributeInsideOrders";
	//gridId
	String gridId = "distributeInsideOrderGrid";
	//������
	String toolbarId = "ddm.distributeinsideOrder.list.toolbar";
	String init = (String)request.getAttribute("init");
	String sendType = (String)request.getAttribute("sendType");
	if ("true".equals(init)) {
		session.removeAttribute("DDM_DISTRIBUTE_INSIDE");
	}
	List<Map<String, Object>> listInfoNamesOfOrg=null;
	if("0".equals(sendType)){
		title = "�ڷ���";
		receiverName = "���յ�λ/��Ա��";
		listInfoNamesOfOrg=DistributeHelper.getDistributeSendOrderService().getDisInfoNames("0","0");
	}else{
		title = "�ⷢ��";
		receiverName = "���յ�λ��";
		listInfoNamesOfOrg=DistributeHelper.getDistributeSendOrderService().getDisInfoNames("0","1");
	}
    List<Map<String, Object>> listInfoNamesOfUser=DistributeHelper.getDistributeSendOrderService().getDisInfoNames("1",null);
	String contextPath = request.getContextPath();
	String submitUrl = contextPath + "/ddm/distribute/distributeSendOrderHandle!updateDistributeInsideCycles.action";
%>
<html>
<head>
<title><%=title%></title>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
<script type="text/javascript"
	src="<%=contextPath%>/plm/ui/editor/date/WdatePicker.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
<script type="text/javascript"
	src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/plm/javascript/messager.js"></script>
<script type="text/javascript">

</script>
<style>
.noTable {
	border: 0px;
	border-collapse: collapse;
	font-size: 12px;
}

.noTable td {
	border: 0px;
	padding-left: 0px;
	height: 25px;
	line-height: 25px;
}
</style>
</head>
<body>
<form name="searchForm" id="searchForm">
	<table width="100%" height="100%" cellSpacing="0" cellPadding="0"
		border="0">
		<%-- <jsp:include page="/plm/common/grid/control/grid_of_title.jsp">
			<jsp:param name="gridTitle" value="<%=title%>"/>
		</jsp:include> --%>
		<tr><td valign="top">
		<table width="100%" height="100%" cellSpacing="0" cellPadding="0" border="0">
		<tr id="searchConditionTD" height="225">
			<td valign="top">
				<table width="100%" cellSpacing="0" cellPadding="0" border="0">
					<tr>
						<td valign="top">
							<table width="100%" class="avidmTable" border="0" cellspacing="0"
								cellpadding="0">
								<TR>
									<TD class="left_col AvidmW150">�������ƣ�</TD>
									<TD><table class="noTable">
											<tr>
												<td><input type="text" name="taskName" id="taskName"
													class="pt-textbox AvidmW270"/>
												</td>
												<td width="5"></td>
												<td><a href="#" onclick="clearValue('taskName','','');">
														<img src="<%=contextPath%>/plm/images/common/clear.gif">
												</a></td>
											</tr>
										</table>
									</TD>
								</TR>
								<TR>
									<TD class="left_col AvidmW150">�������ߣ�</TD>
									<TD><table class="noTable">
											<tr>
												<td>
												<TD><input type='hidden' id="creator" name="creator"/>
													<input type='text'id="creatorName" name="creatorName"
													class="pt-textbox AvidmW270"
													title="ѡ����������"
													readonly="readonly" ontrigger="selectUser('creator')" /></td>
												<td width="5"></td>
												<td><a href="#"
													onclick="clearValue('creatorName','creator','');"> <img
														src="<%=contextPath%>/plm/images/common/clear.gif">
												</a></td>
											</tr>
										</table>
									</TD>
								</TR>

								<TR>
									<TD class="left_col AvidmW150" >������Χ��</TD>
									<TD>
									     <input type="radio" name="searchScope" id="allContext" onclick="searchScopeClick();"/>����������
									    <input type="checkbox" id="favoritedContext" onclick="searchScopeClick2();">���ղص�������&nbsp;&nbsp;<a name="a" href="#" onclick="pt.ui.showMenu('submenu_com_bjsasc_plm_operate_more2')">ѡ��</a><br>
									     <div id="excludeOption">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
									     <div id='submenu_com_bjsasc_plm_operate_more2' class='pt-menu'>                                        
									    	<div  text='����������' tips='����������'  width='60'  iconAlign='left' onclick='excludeOption(this,"AllContext")'  class='pt-menuitem' >    </div>
				 						</div>
									</TD>
								</TR>
								<TR>
									<TD class="left_col AvidmW150"><span>*</span><%=receiverName%></TD>
									<TD><table class="noTable">
											<tr>
												<td><input type="radio" name="receiver"
													id="receiver_unit" value="1" onclick="selectRadio(this)" checked="true"/>��λ&nbsp;&nbsp;
													<%
														if("0".equals(sendType)){
													%>
													<input type="radio" name="receiver" id="receiver_person" value="2" onclick="selectRadio(this)" />��Ա&nbsp;&nbsp;
													<%}%>
												</td>
											</tr>
											<tr>
												<td>
													<select id="disInfoName" name="disInfoName" onchange='initInfoNames(this)' style="display:block;width:150px;">
													<option value="0"></option>
														<% 
															for(Map<String,Object> map:listInfoNamesOfOrg){	
														%>
														<option value="<%=map.get("INFOCLASSID")%>:<%=map.get("DISINFOID")%>"><%=map.get("DISINFONAME")%></option>
														<%
															}
														%>
														<option value="more" id="more">����</option>
													</select>
													<select id="disInfoNameOfUser" name="disInfoNameOfUser" onchange='initInfoNames(this)' style="display:none;width:150px;">
													<option value="0"></option>
														<% 
															for(Map<String,Object> map:listInfoNamesOfUser){
														%>
														<option value="<%=map.get("INFOCLASSID")%>:<%=map.get("DISINFOID")%>"><%=map.get("DISINFONAME")%></option>
														<%
															}
														%>
														<option value="more" id="more">����</option>
													</select>
													<input type='hidden' id="disInfo" name="disInfo" />
													<input type='hidden' id="infoClassId" name="infoClassId"/>
													<input type='hidden' id="flag" name="flag"/>
												</td>
											</tr>
										</table></TD>
								</TR>
								<TR>
									<TD class="left_col AvidmW150">����ʱ�䣺</TD>
									<TD>
										<table class="tableBorderNone">
											<TR>
												<TD>&nbsp;�ԣ�</TD>
												<TD><INPUT type='text' id="queryCreateDateFrom"
													name="queryCreateDateFrom" 
													class="Wdate pt-textbox" readonly="readonly"
													style="width: 100px;" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'2001-01-01',maxDate:'2128-03-10'})" 
													onchange="compareTime()" /></TD>
												<TD>&nbsp;����</TD>
												<TD><INPUT type='text' id="queryCreateDateTo"
													name="queryCreateDateTo"
													
													class="Wdate pt-textbox" readonly="readonly"
													style="width: 100px;"
													onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'2001-01-01',maxDate:'2128-03-10'})"
													onchange="compareTime()" /></TD>
											</TR>
										</table>
									</TD>
								</TR>
								<TR>
									<TD class="left_col AvidmW150">�Ƿ��ѯ�ѷ��ͣ�</TD>
									<TD>
										<table class="tableBorderNone">
											<TR>
												<TD>&nbsp;�ԣ�</TD>
												<TD><INPUT type='text' id="querySendDateFrom"
													name="querySendDateFrom" class="Wdate pt-textbox" readonly="readonly" 
													style="width: 100px;" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'2001-01-01',maxDate:'2128-03-10'})" 
													onchange="compareSendTime()" /></TD>
												<TD>&nbsp;����</TD>
												<TD>
													<INPUT type='text' id="querySendDateTo" name="querySendDateTo" 
													class="Wdate pt-textbox" readonly="readonly" style="width: 100px;" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'2001-01-01',maxDate:'2128-03-10'})" 
													onchange="compareSendTime()" /></TD>
											</TR>
										</table>
									</TD>
								</TR>
								<TR>
									<td class="exegesis"><span>*</span>Ϊ��ѡ/������</td>
									<TD style="height: 40px">
										<div class="pt-formbutton" text="����" id="submitbutton"
											onclick="searchAll();"></div> &nbsp;&nbsp;&nbsp;
										<div class="pt-formbutton" text="���" id="submitbutton"
											onclick="consClear();"></div>
									</TD>
								</TR>
							</table>
						</TD>
					</TR>
				</table>
			</td>
		</tr>
		<TR>
			<TD class="splitButton"> 
			<img onClick="setVisible(this,'searchConditionTD');resizeTable();" src="<%=request.getContextPath()%>/plm/images/common/splitTop.gif"/> 
			</TD>
		</TR>
		<TR>
			<TD valign="top">
				<table height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0">
					<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
					<jsp:param name="spot" value="<%=spot%>" />
					<jsp:param name="gridId" value="<%=gridId%>" />
					<jsp:param name="gridTitle" value="" />
					<jsp:param name="toolbar_modelId" value="<%=toolbarId%>" />
					<jsp:param name="onLoadSuccess" value="onLoadSuccess()"/>
					<jsp:param name="operate_container" value="container" />
					</jsp:include>
				</table>
			<TD>
		</TR>
		</table>
	</td></tr>
	</table>
</form>
</body>
<form id="download" name="download" method="POST"></form>
</html>
<script type="text/javascript">
	var container = {};
	
	function onLoadSuccess() {
		onLoadSuccess_ddm("<%=gridId%>");
	}
	
	function initInfoNames(object){	
		var disInfo='disInfo';
		var selectClassId = object.options[object.selectedIndex].value;
		if(selectClassId == "more"){
			selectReceiver(disInfo);	
			return;
		}else{
			$("#disInfoNameOfUser option[value='temp']").remove();
			$("#disInfoName option[value='temp']").remove();
		}
	}


	function searchAll(){
		var taskName=$("#taskName").val();
		var creator=$("#creator").val();
		var creatorName=$("#creatorName").val();
		var taskCode=getSearchScope();
		var receiver=$("#receiver").val();
		var queryCreateDateFrom=$("#queryCreateDateFrom").val();
		var queryCreateDateTo=$("#queryCreateDateTo").val();
		var querySendDateFrom=$("#querySendDateFrom").val();
		var querySendDateTo=$("#querySendDateTo").val();
		var sendType=<%=sendType%>;
		var disInfoName=null;
		var temp=null;
		var oid=null;
		if($("#disInfoName").is(":visible")==true){
			disInfoName=$("#disInfoName").find("option:selected").text();
			oid=$("#disInfoName").val();
			if(oid!="temp"){
			oid=$("#disInfoName").val();
			}else{
				oid="";
			}
		}else{
			disInfoName=$("#disInfoNameOfUser").find("option:selected").text();
			oid=$("#disInfoNameOfUser").val();
			if(oid!="temp"){
			oid=$("#disInfoNameOfUser").val();
			}else{
				oid="";
			}
		}
		var disInfo=null;
		var infoClassId=$("#infoClassId").val();
		if(oid==null||oid==""||oid=="temp"){
			disInfo=$("#disInfo").val();

		}
		var flag=$("#flag").val();
		
		if(flag == null || flag == ""){
			flag = 1;
		}

		if(disInfoName == ""){
			plm. showMessage({title:'��ʾ', message:"����ѡ����յ�λ/��Ա!", icon:'3'});
			return false;
		}

		var datas = "taskName=" + taskName
					+"&creator=" + creator
					+"&creatorName=" + creatorName
					+"&taskCode=" + taskCode
					+"&receiver=" + receiver
					+"&queryCreateDateFrom=" + queryCreateDateFrom
					+"&queryCreateDateTo=" + queryCreateDateTo
					+"&querySendDateFrom=" + querySendDateFrom
					+"&querySendDateTo=" + querySendDateTo
					+"&disInfoName=" + disInfoName
					+"&oid="+oid
					+"&infoClassId=" + infoClassId
					+"&disInfo=" + disInfo
					+"&flag=" + flag
					+"&sendType=" + sendType;

		var url = "<%=contextPath%>/ddm/distribute/distributeSendOrderHandle!getAllDistributeInsideTasks.action";
		plm.startWait();
		$.ajax({
			url:url,
			type:"post",
			dataType:"json",
			data:datas,
			success:function(result){
				plm.endWait();
				if(result.FLAG==0){
			    	messager.showTipMessager({'content':'û������'});	 
				}else{
					$("#searchTab").show();
	        		setVisible(this,'searchConditionTD');resizeTable();	
				}
				if(result.SUCCESS != null &&result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
			  }
				var table = pt.ui.get("<%=gridId%>");
				table.reload();
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'������ʾ', message:"����ʧ�ܣ�", icon:'1'});
			}
		});	
	}

	function selectRadio(obj){
		$("#disInfoName").val("");
		if(obj.value == "1"){
			$('[id="receiver_person"]').removeAttr("checked");
			$('[type="radio"][id="receiver_unit"]').attr("checked", true);
			$("#flag").val("1");
			$('#disInfoName').show();
			$('#disInfoName').val("0");
			$('#disInfoNameOfUser').hide();
		}
		if(obj.value == "2"){
			$('[id="receiver_unit"]').removeAttr("checked");
			$('[type="radio"][id="receiver_person"]').attr("checked", true);
			$("#flag").val("2");
			$('#disInfoName').hide();
			$('#disInfoNameOfUser').show();
			$('#disInfoNameOfUser').val("0");
		}
	}
	
	function consClear() {
		try {
		$("#keywords").val("");
		$("#taskName").val("");
		//$("#taskCode").val("");
		$("#allContext").attr("checked",false);
		$("#favoritedContext").attr("checked",false);
		$("#excludeOption").html("");
		$("#creator").val("");
		
		$('[id="receiver_person"]').removeAttr("checked");
		$('[type="radio"][id="receiver_unit"]').attr("checked", true);
		$("#flag").val("1");
		$('#disInfoName').show();
		$('#disInfoName').val("0");
		$('#disInfoNameOfUser').hide();
		
		$("#queryCreateDateFrom").val("");
		$("#queryCreateDateTo").val("");
		$("#querySendDateFrom").val("");
		$("#querySendDateTo").val("");
		$("#creatorName").val("");
		$('[id="receiver_person"]').removeAttr("checked");
		$('[type="radio"][id="receiver_unit"]').attr("checked", true);
		$("#flag").val("1");
		} catch (e) {}
	}
	
	function clearValue(value1,value2,value3) {
		try {
		$("#"+value1).val("");
		$("#"+value2).val("");
		$("#"+value3).val("");
		} catch (e) {}
	}
	
	function selectReceiver(disInfoId){
		var flag=$("input[name='receiver'][checked]").val();
		if(flag=="1"){
			selectOrg(disInfoId);
		}else if(flag=="2"){
			selectUser(disInfoId);
		}
	}
	
	function selectUser(disInfoId){
		var configObj = 
			{ 
				IsModal : "true",
				SelectMode : "single",
				returnType : "arrayObj",
				scope : "<%=AADomainUtil.VISTYPE_ALL%>"
			};
		var retObj = pt.sa.tools.selectUser(configObj);
		if(retObj){
			var optiontext=null;
			$("#disInfoNameOfUser option[value='temp']").remove();
			$("#disInfoNameOfUser option").each(function(){
				if($(this).text()==retObj.arrUserName[0]){
					$(this).attr("selected","selected");
					optiontext=retObj.arrUserName[0];
					return;
				}
			});
			if(optiontext==null){
				var option=$("<option>").val("temp").text(retObj.arrUserName[0]);
				$("#disInfoNameOfUser option[value='0']").after(option);
				$("#disInfoNameOfUser").find("option:contains("+retObj.arrUserName[0]+")").attr("selected","selected");
				$("#" + disInfoId).val(retObj.arrUserIID[0]);
			};
			if(disInfoId=="creator"){
				$("#" + disInfoId).val(retObj.arrUserIID[0]);
				$("#" + disInfoId+"Name").val(retObj.arrUserName[0]);
			}
	  	};
	}
	function clearId(idValue){
		$("#" +idValue).val("");
	}
	
	function selectOrg(disInfoId){
		var configObj = 
			{ 
				IsModal : "true",
				SelectMode : "single",
				returnType : "arrayObj",
				scope : "<%=AADomainUtil.VISTYPE_ALL%>"
			};
		var retObj = pt.sa.tools.selectOrg(configObj);
		if (retObj) {
			var optiontext=null;
			$("#disInfoName option[value='temp']").remove();
			$("#disInfoName option").each(function(){
				if($(this).text()==retObj.arrDivName[0]){
					$(this).attr("selected","selected");
					optiontext=retObj.arrDivName[0];
					return;
				}
			});
			if(optiontext==null){
				var option=$("<option>").val("temp").text(retObj.arrDivName[0]);
				$("#disInfoName option[value='0']").after(option);
				$("#disInfoName").find("option:contains("+retObj.arrDivName[0]+")").attr("selected","selected");
				$("#" + disInfoId).val(retObj.arrDivIID[0]);
			}
		};
	}

	function compareTime() {
		var sttime = $("#queryCreateDateFrom").val();
		var edtime = $("#queryCreateDateTo").val();
		if (sttime != "" || sttime != null || edtime != ""|| edtime != null) {
			document.getElementById("querySendDateFrom").value = "";
			document.getElementById("querySendDateTo").value = "";
			}
		if (sttime != "") {
			if (edtime != "" && sttime > edtime) {
				alert("��ʼʱ��ӦС�ڽ���ʱ�䣡");
				return;
			}
		}
	}

	function compareSendTime() {
		var sttime = $("#querySendDateFrom").val();
		var edtime = $("#querySendDateTo").val();
		if (sttime != "" || sttime != null || edtime != ""|| edtime != null) {
			document.getElementById("queryCreateDateFrom").value = "";
			document.getElementById("queryCreateDateTo").value = "";
		}
		if (sttime != "") {
			if (edtime != "" && sttime > edtime) {
				alert("��ʼʱ��ӦС�ڽ���ʱ�䣡");
				return;
			}
		}
	}
	
	// �ָ���
	function resizeTable(){
		var grid = pt.ui.get("<%=gridId%>"); 
		grid.set({width :$(grid.renderTo).width(),height :$(grid.renderTo).height()-22});
		grid.pagingbar.set( {width :$(grid.renderTo).width(),height :22});
	};

	function doCustomizeMethod_id(value) {
		var url = '<%=contextPath%>/ddm/distribute/distributeSendOrderHandle!getDistributePaperTaskList.action?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + value;
		ddm.tools.openWindow(url,900,600,"distributeOrderOpen");
	}
	
	// �����ӡ
	function doCustomizeMethod_operate(value) {
		plm.startWait();
		$.ajax({
			url:"<%=submitUrl%>",
			type:"post",
			dataType:"json",
			data:"<%=KeyS.OIDS%>="+value,
			success:function(result){
				plm.endWait();
				if(result.SUCCESS != null &&result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				reload();
				var url = '<%=contextPath %>/ddm/distribute/distributeSendOrderHandle!paperTaskExcel.action?<%=KeyS.OIDS%>=' + value;
				$("#download").attr("action", url);
				$("#download").submit();

			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'������ʾ', message:"�ύʧ�ܣ�", icon:'1'});
			}
		});
	}

	function reload(){
		var table = pt.ui.get("<%=gridId%>");
		table.reload();
	}

	// �����ӡ
	function dataPrint(){
		plm.startWait();
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if (data == null || data.length == 0) {
			plm.endWait();
			plm. showMessage({title:'��ʾ', message:"������ѡ��1����������!", icon:'3'});
			return;
		}
		var oids = "";
		for(var i=0;i<data.length;i++){
			oids += data[i].OID + ",";
		}
		oids = oids.substring(0, oids.length - 1);
		
		$.ajax({
			url:"<%=submitUrl%>",
			type:"post",
			dataType:"json",
			data:"<%=KeyS.OIDS%>="+oids,
			success:function(result){
				plm.endWait();
				if(result.SUCCESS != null &&result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				reload();
				var url = '<%=contextPath %>/ddm/distribute/distributeSendOrderHandle!paperTaskExcel.action?<%=KeyS.OIDS%>=' + oids;
				$("#download").attr("action", url);
				$("#download").submit();
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'������ʾ', message:"�ύʧ�ܣ�", icon:'1'});
			}
		});
	}
	
	//�޸���Ŀ����
	function updateDistributePaperTask(){
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if (data == null || data.length != 1) {
			plm. showMessage({title:'��ʾ', message:"������ѡ��1����������!", icon:'3'});
			return;
		}
		
		var oid = data[0].OID;
		var url = "<%=contextPath%>/ddm/distributepapertask/updateDistributePaperTask.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=" + oid;
		plm.openWindow(url,800,600);	
	}

    var constant_="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    
	//ɾ������
	function del(a){
		//ɾ�����ڵ�
		a.parentNode.removeNode(true);

		if($("#excludeOption").html()==constant_){
			$("#favoritedContext").attr("checked",false);
		}
	}

	function addDel(type){
		if($(type).children().next().attr("src")==undefined){
      		   $(type).append("<img src='<%=contextPath%>/plm/images/common/delete.gif'  onclick='del(this)'/>");
		}
	}
	//������������Ƿ��ظ���δ�ظ������
	function checkIsExist(type,o){
		var result=true;
		if(type=="excludeOption"){
			$("#excludeOption").children().each(function(){
				var name=$(this).attr("name");
				if(name=="span"){
				 var id=$(this).attr("id");
				 if(o==id){
					result=false;
				 }
				}
			});
		}
		return result;
	}
	//������Χ    ѡ�����
	function excludeOption(obj,a){
        var url = "<%=contextPath%>/ddm/commonsearch/excludeType.jsp?CALLBACK=afterexcludeType&excludeType="+a;

		//�򿪴���
		plm.openWindow("<%=contextPath%>/plm/load.html", 500, 500,'T');
		
		//��ǰ̨��ִ��
		plm.openByPost(url, null, 'T');
	}
	function afterexcludeType(o){
		if (o != null) {
			var name=o.NAMES.split(",");
			var type=o.TYPES.split(",");
			for(var i=0;i<name.length;i++){
				if(checkIsExist("excludeOption",type[i])){
				$("#excludeOption").append("<span name='span' id='"+type[i]+"'  onmouseover='addDel(this)'><input type='checkbox' name='"+name[i]+"' id='"+name[i]+"' checked='checked'/>"+name[i]+"</span>");
			  }
			}
		}
		searchScopeClick2();
	}
	function searchScopeClick(){
		$("#favoritedContext").attr("checked",false);
	}
	function searchScopeClick2(){
		if($("#excludeOption").html()!=constant_){
			$("#favoritedContext").attr("checked",true);
			var searchScope=$("input[name='searchScope']:checked").attr("id");
			if(searchScope!=undefined){
				$("#"+searchScope).attr("checked",false);
			}
		}
		else{
			$("#favoritedContext").attr("checked",false);
			plm.showMessage({title:'��ʾ', message:"��������ղص�������!", icon:'3'});
		}
	}
	//�õ�������Χ
	function getSearchScope(){
		//�����ķ�Χ
		var isFirst=true;
		var containerScope="";
		var favoriteScope="";
		var scopeId=$("input[name='searchScope']:checked").attr("id");
		if(scopeId!=undefined){
			containerScope=scopeId;
		}
		if($("#favoritedContext").attr("checked")=="checked"){
			$("#excludeOption").children().each(function(){
				var check=$(this).children().attr("checked");
				var name=$(this).attr("name");
				if(name=="span" && check=="checked"){
				 var id=$(this).attr("id");
				 if(isFirst){
					 favoriteScope=id;
					 isFirst=false;
				 }
				 else{
					 favoriteScope=favoriteScope+","+id;
				 }
				}
			});
		}
		var searchScope={"containerScope":containerScope,"favoriteScope":favoriteScope};
		return pt.ui.JSON.encode(searchScope);
	}
</script>