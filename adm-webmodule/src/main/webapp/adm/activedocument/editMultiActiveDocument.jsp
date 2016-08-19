<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.adm.active.model.activedocument.ActiveDocument"%>
<%@page import="com.bjsasc.adm.active.model.activedocument.ActiveDocumentMaster"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String title ="�༭��������ļ�";
	String oids = request.getParameter("OIDS");
	String gridId ="editMultiAcitveDocument";
	String gridUrl = contextPath + "/adm/active/ActiveDocumentHandle!getActiveDoucmentByOIDS.action?OIDS="+oids;
	String updataUrl =  contextPath + "/adm/active/ActiveDocumentHandle!updataMultiActiveDocument.action";
%>
<html>
	<head>
		<title><%=title%></title>
		<script type="text/javascript"	src="<%=request.getContextPath()%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
	</head>
	<body>
	<table cellSpacing="0" cellPadding="0" height="100%" width="100%" border="0">
		<tr>
			<td height="1%">
				<div id="gridTop" name="gridTop"><div>
			</td>
		</tr>
		<tr class="AvidmToolbar">
			<td nowrap="nowrap">
				<div class="AvidmMtop5 twoTitle">
					<a class="showIcon" href="#" onclick="divSelected(this);">
					<img src="<%=request.getContextPath()%>/plm/images/common/space.gif"></a>
					<%=title%>
				</div>
			</td>
		</tr>
		<tr>
			<td valign="top">
			<form name="myForm" id="myForm">
				<table id="<%=gridId%>" class="pt-grid" singleSelect="false" checkbox="false" url="<%=gridUrl%>"
						fit="true" width="100%" pagination="false" rownumbers='true'>
					<thead>
						<th field="NUMBER"  width="250" tips="���">���</th>
					   	<th field="NAME" 	 width="150"  tips="����">����</th>
					   	<th field="NOTE"  width="150" tips="��ע" editor="{type:'text'}" <%=ValidateHelper.buildValidatorAndAssisant()%>>��ע</th>
					</thead>
				</table >
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="exegesis AvidmW150"><span>*</span> Ϊ��ѡ/������</td>
						<td>
			    			<div class="pt-formbutton" text="ȷ��"  onclick="submitForm();" <%=ValidateHelper.buildCheckAll()%>></div>
			    			<div class="pt-formbutton" text="ȡ��"  onclick="cancle();"></div>
						</td>
					</tr>
				</table>
			</form>
			</td>
		</tr>
	</table>

	</body>
</html>
<script type="text/javascript">

	var container = {};
	
	// �����޸ı�ע
  	function submitForm(){
		<%=ValidateHelper.buildCheck()%>;
  		var table = pt.ui.get("<%=gridId%>");
  		table.selectAll();
  		var data = table.getSelections();

		var oids="";
		var notes="";
		for (var i=0;i<data.length;i++){
			oids+=data[i].OID+",";
			var note =data[i].NOTE;
			if(note==null||note=="undefined"||note==""){
				notes+=",";
			}else{
				notes+=note+",";
			}
		}
		oids = oids.substring(0,oids.length-1);
		notes = notes.substring(0,notes.length-1);
		if (!plm.confirm("��ȷ�ϱ�ע��д��ȷ���ύ�󽫱��沢�رմ��ڣ�")) {
			return;
		}
		 plm.startWait();
	$.ajax({
	        type: "post",
	        url : "<%=updataUrl%>?OIDS="+oids +"&NOTES="+notes,
	        dataType:"json",
	        success: function(result){
			    plm.endWait();
				if(result.SUCCESS != null && result.SUCCESS == "false"){
					plm.showAjaxError(result);
				}else{
					plm.showMessage({title:'��ʾ', message:"�����༭�����ɹ�!", icon:'2'});
					top.opener.location.reload();
					top.close();
				}
		   },
		   error:function(){
			   plm.endWait();
			   plm.showMessage({title:'������ʾ', message:"�����༭����ʧ��!", icon:'1'});
		   }
	    });
  	}
</script>
