<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%@include file="/ddm/public/ddm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String path = request.getContextPath()+"/plm/context/team";
	String contextOID = RequestUtil.getParamOid(request);
	String classId = DistributePaperTask.CLASSID;
	String oids = request.getParameter("OIDS");
	String spot = "ListAddDuplicateProcessor";
	String gridId = "distributePaperTaskGrid";
	String gridTitle = "¼�븴�Ƽӹ���Ϣ";
	String toolbarId = "ddm.duplicateprocess.list.toolbar";
	String gridUrl = contextPath + "/ddm/distribute/duplicateProcessHandle!getDuplicateProcessList.action?OIDS="+oids;
	String updateUrl = contextPath + "/ddm/distribute/duplicateProcessHandle!updateDuplicateProcessList.action";
	String url =contextPath + "/ddm/distribute/duplicateProcessHandle!updateDuplicateProcessor.action";
	String collator = "";
%>
<html>
	<head>
		<title><%=gridTitle%></title>
		<script type="text/javascript"	src="<%=request.getContextPath()%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
	</head>
	<body>
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
			<table height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0">
			<tr><td height="1%">
			<table cellSpacing="0" cellPadding="0" width="100%" border="0" style="border-right:#9cbdff solid 1px;border-left:#9cbdff solid 1px;"><tr class="AvidmToolbar"><td> 
				<div class="pt-toolbar">
					<%=UIHelper.getToolBar(toolbarId)%>
				</div> 
			</td></tr></table>			
			</td></tr><tr><td valign="top">
				<table id="<%=gridId%>" class="pt-grid" singleSelect="false" checkbox="false" url="<%=gridUrl%>"
						fit="true" width="100%" pagination="false" rownumbers='true'>
					<thead>
						<th field="NUMBER"  width="250" tips="���">���</th>
					   	<th field="COLLATOR" 	 width="150"  tips="������" editor="{type:'text'}">������</th>
					   	<th field="CONTRACTOR"  width="150" tips="��ӡ��" editor="{type:'text'}">��ӡ��</th>
					</thead>
				</table >
			</td></tr></table>
</td></tr></table>
	<form id='main_form'>
		<input type="hidden" name="keys" id="keys"/>
	</form>
	</body>
</html>
<script type="text/javascript">

	var container = {};
	
	// �����������Ϣ
  	function addContractor(){
		addUsersUrl = "<%=contextPath%>/plm/role/rolePrincipalsHandle!addUsers.action";
		var configObj = 
			{ 
				IsModal : "true",
				SelectMode : "single",
				returnType : "arrayObj",
				scope : "all"
			};
		var retObj = pt.sa.tools.selectUser(configObj);
		if(retObj){
			doBindContractor(retObj);
	  	} 
  	}
  	
  	function doBindContractor(retObj) {
		var arrIID = retObj.arrUserIID;
		var arrUserId = retObj.arrUserId;
		var arrUserName = retObj.arrUserName;
  		var name = $("#keys").val(arrUserName);
		$.ajax({
				type: "post",
				url: "<%=updateUrl%>",
				dataType: "json",
				data: "username="+arrUserName[0]+"&flag=1",
				success: function(result){
					if(result.SUCCESS != null &&result.SUCCESS == "false"){
						plm.showAjaxError(result);
						return;
					}
					var table = pt.ui.get("<%=gridId%>");
					table.reload();
			   },
			   error:function(){}
			});	
  	}
  	
 	// ��Ӹ�ӡ����Ϣ
  	function addCollator(){
		addUsersUrl = "<%=contextPath%>/plm/role/rolePrincipalsHandle!addUsers.action";
		var configObj = 
			{ 
				IsModal : "true",
				SelectMode : "single",
				returnType : "arrayObj",
				scope : "all"
			};
		var retObj = pt.sa.tools.selectUser(configObj);
		if(retObj){
			doBindCollator(retObj);
	  	} 
  	}
  	
  	function doBindCollator(retObj) {
		var arrIID = retObj.arrUserIID;
		var arrUserId = retObj.arrUserId;
		var arrUserName = retObj.arrUserName;
  		var name = $("#keys").val(arrUserName);
		$.ajax({
				type: "post",
				url: "<%=updateUrl%>",
				dataType: "json",
				data: "username="+arrUserName[0]+"&flag=2",
				success: function(result){
					if(result.SUCCESS != null &&result.SUCCESS == "false"){
						plm.showAjaxError(result);
						return;
					}
					var table = pt.ui.get("<%=gridId%>");
					table.reload();
			   },
			   error:function(){}
			});	
  	}
  	
  	//�ύ
  	function submitUsers(){
  		var table = pt.ui.get("<%=gridId%>");
  		table.selectAll();
  		var data = table.getSelections();
		
		if (!plm.confirm("��ȷ�������ˣ���������д��ȷ���ύ�󽫱��沢�رմ��ڣ�")) {
			plm.endWait();
			return;
		}
		$("#keys").val(data[0].COLLATOR);
		var collator = $("#keys").val();
		$("#keys").val(data[0].CONTRACTOR);
		var contractor = $("#keys").val();
		plm.startWait();
		$.ajax({
			url:"<%=url%>",
			type:"post",
			dataType:"json",
			data:"<%=KeyS.OIDS%>=<%=oids%>&collator="+collator+"&contractor="+contractor,
			success:function(result){
				plm.endWait();
				if(result.SUCCESS != null &&result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				window.close();
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'������ʾ', message:"��Ӹ�ӡ������ʧ�ܣ�", icon:'1'});
			}
		});
  	}
  	
	//����ˢ�·���
	function reload(){
		window.location.reload(true);
		parent.frames['leftFrame'].refreshTree();
	}

	//ˢ��dategrid
	function tableReload(text){
		plm.showMessage({title:'ϵͳ��ʾ', message:text, icon:'2'});
		reload();
	}
</script>
