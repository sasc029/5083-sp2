<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>

<%@page import="com.bjsasc.plm.type.TypeService"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.context.ContextManager"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="com.bjsasc.plm.navigator.NavigatorUtil"%>
<%@page import="com.bjsasc.plm.core.util.DateTimeUtil"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>
	
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeelectask.DistributeElecTask"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String commonInfoTitle = "�����������";
	DistributeElecTask disTask = (DistributeElecTask) request.getAttribute("distributeElecTaskInfo");
	TypeService typeManager = Helper.getTypeManager();
	Map<String, Object> map = typeManager.format(disTask);
	
%>
<html>
	<head>
		<title><%=commonInfoTitle%></title>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
		<!-- ������ -->
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
		<script type="text/javascript"	src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
		<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
	</head>
	<body>
		<form id="mainForm" name="mainForm" method="POST">
<table cellSpacing="0" cellPadding="0" width="100%" border="0">
	<tr><td height="1%">	
		<!-- ������Ϣ ��ʼ -->
		<table width="100%" cellSpacing="0" cellPadding="0" border="0">
			<tr class="AvidmToolbar">
				<td nowrap="nowrap">
					<div class="AvidmMtop5 twoTitle">
						<a class="showIcon" href="#" onclick="divSelected(this)">
						<img src="<%=contextPath%>/plm/images/common/space.gif"></a>
						<%=commonInfoTitle%>
					</div>
				</td>
			</tr>
			<tr><td>
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
					<tr>
						<td class="left_col AvidmW150">�����ţ�</td>
						<td class="e-checked-text" width="35%"><%= disTask.getNumber() %></td>
						<td class="left_col AvidmW150">�����ˣ�</td>
						<td class="e-checked-text" width="35%"><%= disTask.getCreateByName() %></td>
					</tr>
					<tr>
						<td class="left_col AvidmW150">����ʱ�䣺</td>
						<td class="e-checked-text"><%= DateTimeUtil.getDateText(disTask.getSendTime()) %></td>
					 	<td class="left_col AvidmW150">�����ģ�</td>
						<td><%=map.get("DDMCONTEXT")%></td>
					</tr>
					<tr>
						<td class='left_col AvidmW150'>��������ģ�壺</td>
						<td><%=map.get("LIFECYCLE_TEMPLATE") %></td>
						<td class='left_col AvidmW150'>״̬��</td>
						<td><%=map.get("LIFECYCLE_STATE_ALL") %></td>
					</tr>
					<tr>
						<td class='left_col AvidmW150'>�������ŵ���</td>
						<td><a href ="#" onclick="javascript:selectOrderProperty('<%=disTask.getOrderOid() %>')"><%=disTask.getOrderName() %></a></td>
						<td class='left_col AvidmW150'></td>
						<td></td>
					</tr>
					<input type="hidden" id="classId" name="classId" value="<%=disTask.getClassId() %>">
					<input type="hidden" id="innerId" name="innerId" value="<%=disTask.getInnerId() %>">
				</table>
		</td></tr></table>
	</td></tr><tr><td>	
<%
	String spot = "ListDistributeObjects";
	String gridId = "distributeObjectGrid";
	String gridTitle = "�ַ������б�";
	String gridUrl = "";
%>
		<table width="100%" cellSpacing="0" cellPadding="0" border="0">
			<tr class="AvidmToolbar">
				<td nowrap="nowrap">
					<div class="AvidmMtop5 twoTitle">
						<a class="showIcon" href="#" onclick="divSelected(this)">
						<img src="<%=contextPath%>/plm/images/common/space.gif"></a>
						<%=gridTitle%>
					</div>
				</td>
			</tr>
			<tr><td>
				<table class="pt-grid" id="<%=gridId%>" autoLoad="true"
						url="<%=contextPath%>/plm/common/grid/control/data_get.jsp?spot=ListDistributeObjects&spotInstance=ListDistributeObjects" 
					rownumbers="true" pagination="false" useFilter="false" singleSelect="false" fit="true" checkbox="true" onClickRow="getDistributeInfos()" >
					<thead>
						<th field="NUMBER" width="100" tips="���" sortable='true' >���</th>
						<th field="NAME" width="250" tips="����" sortable='true' >����</th>
						<th field="DISTRIBUTEDATA" width="150" tips="�ַ�����" sortable='true' >�ַ�����</th>
						<th field="DATAFROM" width="250" tips="�ַ�����Դ��Դ" sortable='true' >�ַ�����Դ��Դ</th>
						<th field="STATENAME" width="100" tips="��״̬" sortable='true' >��״̬</th>
						<th field="LIFECYCLE_TEMPLATE_NAME" width="100" tips="��������ģ��" sortable='true' >��������ģ��</th>
						<th field="DISTRIBUTEORDEROBJECTLINKOID" hidden='true' >DISTRIBUTEORDEROBJECTLINKOID</th>
					</thead>
				</table>
		</td></tr></table>
	</td></tr><tr><td>	
<%
	spot = "ListDistributeInfos";
	String infogridId = "distributeInfoGrid";
	gridTitle = "ת����Ϣ�б�";
	String toolbarId = "ddm.distributeinfo.edit.toolbar";
	String addPrincipalUrl = contextPath + "/ddm/distribute/distributeElecTaskHandle!addDistributeInfos.action";
	String updateUrl = contextPath + "/ddm/distribute/distributeElecTaskHandle!updateDistributeInfos.action";
	
%>
		<table width="100%" cellSpacing="0" cellPadding="0" border="0">
			<tr class="AvidmToolbar">
				<td nowrap="nowrap">
					<div class="AvidmMtop5 twoTitle">
						<a class="showIcon" href="#" onclick="divSelected(this)">
						<img src="<%=contextPath%>/plm/images/common/space.gif"></a>
						<%=gridTitle%>
					</div>
				</td>
			</tr>
			<tr><td height="1%">
			<table cellSpacing="0" cellPadding="0" width="100%" border="0" style="border-right:#9cbdff solid 1px;border-left:#9cbdff solid 1px;"><tr class="AvidmToolbar"><td> 
				<div class="pt-toolbar">
					<%=UIHelper.getToolBar(toolbarId)%>
				</div> 
			</td></tr></table>			
			</td></tr>
			<tr><td>
				<table class="pt-grid" id="<%=infogridId%>" autoLoad="true"
						url="<%=contextPath%>/plm/common/grid/control/data_get.jsp?spot=ListDistributeInfos&spotInstance=ListDistributeInfos" 
					rownumbers="true" pagination="false" useFilter="false" singleSelect="false" fit="true" checkbox="true" >
					<th field="DISINFONAME" width="100" tips="���յ�λ/��Ա" sortable='true' >���յ�λ/��Ա</th>
					<th field="DISINFOTYPE" width="100" tips="�ַ���Ϣ����" sortable='true' >�ַ���Ϣ����</th>
					<th field="DISINFONUM" width="100" tips="����" sortable='true' >����</th>
					<th field="DISMEDIATYPE" width="100" tips="�ַ�����" sortable='true' >�ַ�����</th>
					<th field="DISTYPE" width="100" tips="�ַ���ʽ" sortable='true' >�ַ���ʽ</th>
					<th field="NOTE" width="250" tips="��ע" editor="{type:'text'}" >��ע</th>
					<th field="SENDTIME" width="100" tips="����ʱ��" sortable='true' >����ʱ��</th>
					<th field="LIFECYCLE_STATE" width="100" tips="״̬" sortable='true' >״̬</th>
					<th field="LIFECYCLE_STATEID" width="100" tips="״̬ID" sortable='true' >״̬ID</th>
					<th field="LIFECYCLE_TEMPLATE" width="100" tips="��������ģ��" sortable='true' >��������ģ��</th>
					<th field="CREATOR" width="100" tips="������" sortable='true' >������</th>
					<th field="MODIFIER" width="100" tips="�޸���" sortable='true' >�޸���</th>
					<th field="CREATE_TIME" width="140" tips="����ʱ��" sortable='true' >����ʱ��</th>
					<th field="MODIFY_TIME" width="140" tips="�ϴ��޸�ʱ��" sortable='true' >�ϴ��޸�ʱ��</th>
					<th field="INNERID"  hidden="true">INNERID</th>
					<th field="CLASSID"  hidden="true">CLASSID</th>
				</table>
		</td></tr></table>
	</table>
	<input type="hidden" name="distributeOrderObjectLinkOids" id="distributeOrderObjectLinkOids"/>
	<input type="hidden" name="taskOid" id="taskOid" value="<%=request.getParameter("taskOid") %>"/>
	</form>
	</body>
</html>
<script type="text/javascript">

	var container = {};
	
	function saveSubmitForm(){
  		// ��֤
		<%=ValidateHelper.buildCheck()%>
			
    	var oids = "";
    	var notes = "";
    	
		var gridObj = pt.ui.get("<%=infogridId%>");
		gridObj.selectAll();
		var selections = gridObj.getRows();
    	for(var i = 0; i < selections.length; i++){
			var innerId = selections[i]["INNERID"];
			var classId = selections[i]["CLASSID"];
			var oid = classId + ":" + innerId;
			var note = selections[i]["NOTE"];
			if(note == ""){
				note = null;
			}
			oids += oid + ",";
			notes += note + ",";
    	}
		$.ajax({
				type: "post",
				url: "<%=updateUrl%>",
				dataType: "json",
				data: "<%= KeyS.OIDS%>="+oids+"&notes="+notes,
				success: function(result){
					cancleButton();
			   },
			   error:function(){
				   plm.showMessage({title:'������ʾ', message:"�Բ��������������", icon:'1'});
			   }
			});	
  	}
	
	function selectOrderProperty(value){
		var url = '<%=contextPath%>/ddm/distributeorder/distributeOrder_tab.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + value;
		ddm.tools.openWindow(url,800,600,"distributeOrderOpen");
	}
    
    // ���¼���
	function reload(text){
		var table = pt.ui.get("<%=gridId%>");
		table.reload();
	}
  	function cancleButton(){
  		var taskOid = $("#classId").val()+":"+$("#innerId").val();
  		window.location.href = "<%=contextPath%>/ddm/distribute/distributeElecTaskHandle!getDistributeForwardElecTaskDetail.action?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OIDS%>="+taskOid;
  	}

</script>
