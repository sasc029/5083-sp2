<%@page import="com.bjsasc.ddm.distribute.model.recdesinfo.RecDesInfo"%>
<%@page import="java.util.Map"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.type.TypeService"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@ page import="java.util.*"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributesendorder.DistributeSendOrderService"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo"%>
<%@page import="com.bjsasc.ddm.distribute.model.recdesinfo.RecDesInfo"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%@include file="/ddm/public/ddm.jsp" %>

<%
	String path = request.getContextPath();
	String oid = request.getParameter("OIDS");

	String contextPath = request.getContextPath();
	String classId = DistributePaperTask.CLASSID;
	String spot = "ListDistributeDestoryDetails";
	String gridId = "ListDistributeDestoryDetail";
	String gridTitle = "";
	String toolbarId = "ddm.distributeDestroyOrder.list.toolbar";
	String gridUrl = contextPath + "/ddm/distribute/distributeDestroyOrderHandle!getDistributeDestroyDetails.action?OIDS="+oid;
	//Map<String,String> map = (Map<String,String>) session.getAttribute("DDM_DISTRIBUTE_INSIDE");
	//String destroyType = map.get("destroyType");
	String destroyType = request.getParameter("destroyType");
	String title = "";
	String destroynums = "";
	
	String recDesClassId = RecDesInfo.class.getSimpleName();
	
	if("0".equals(destroyType)){
		title="���յ�";
		destroynums = "���շ���";
	}else{
		title="���ٵ�";
		destroynums = "���ٷ���";
	}
	
	String destroyNumURL = request.getContextPath()+"/ddm/public/getSelect.jsp?select=";
	String updateURL = contextPath + "/ddm/distribute/distributeDestroyOrderHandle!updateDestroyNum.action";

%>
<html>
<head>
	<title>������Ϣ</title>
	<script type="text/javascript" src="destroyNumSelect.jsp"></script>
	<script type="text/javascript" src="destroyNumStatusEnum.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/platform/common/js/ptutil.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0"  scroll="no">
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><td height="1%">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		 <tr>
	    	<td class="AvidmTitleName">
	    		<div class="imgdiv"><img src="<%=request.getContextPath() %>/plm/images/common/modify.gif"/></div>
				<div class="namediv"><%=title %></div></td>
		 </tr>
	</table>
	</td></tr>
	<input type="hidden" value="<%=oid %>" name="<%=KeyS.OID %>" />
		<tr><td>
		<table height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0">
			<tr><td height="1%">
			<table cellSpacing="0" cellPadding="0" width="100%" border="0" style="border-right:#9cbdff solid 1px;border-left:#9cbdff solid 1px;"><tr class="AvidmToolbar"><td> 
				<div class="pt-toolbar">
					<%=UIHelper.getToolBar(toolbarId)%>
				</div> 
			</td></tr></table>			
			</td></tr><tr><td valign="top">
				<table id="<%=gridId%>" class="pt-grid" singleSelect="false" checkbox="true" url="<%=gridUrl%>"
						fit="true" height="100%" width="100%" pagination="false" rownumbers='true'>
					<thead>
						<th field="NUMBER"  width="200" tips="���">���</th>
						<th field="NAME"  width="200" tips="����">����</th>
					   	<th field="DISINFONUM" 	 width="100"  tips="�ַ�����">�ַ�����</th>
					   	<th field="RECOVERDESTROYNUM"  width="100" editor="{type:'text'}" tips="����/���ٷ���" ><%= destroynums %></th>
					   	<th field="DESTROYNUM"  width="100" tips="�����ٷ���" >�����ٷ���</th>
					   	<th field="RECOVERNUM" width="100" tips="�ѻ��շ���">�ѻ��շ���</th>
					   	<th field="DISINFONAME"  width="100" tips="���յ�λ/��Ա" >���յ�λ/��Ա</th>
					   	<th field="NOTE" width="150" tips="��ע">��ע</th>
					</thead>
				</table >
			</td></tr></table>
			</td></tr></table>
			<form id="download" name="download" method="POST"></form>
</body>
</html>
<script type="text/javascript">
           
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
		var nums = "";
		var flag="";
		var destroyType = "<%=destroyType%>";
		var alertContent = "";
		var alertFlag = "";
		var type="";
		for(var i=0;i<data.length;i++){
			var numReg=/^\d+$/;
			var dataNumber = "���\""+data[i].NUMBER.substring(data[i].NUMBER.indexOf("title='")+7,data[i].NUMBER.indexOf("'>"))+"\"";
			if (!numReg.test(data[i].RECOVERDESTROYNUM)) {
				alertFlag = 1;
				alertContent += ("���"+dataNumber +"������ȷ������\n");
    		}
			if(destroyType == 0){
				type = "NEEDRECOVERNUM";
				var recoverCount = data[i].DISINFONUM - data[i].RECOVERNUM;
				if(recoverCount < data[i].RECOVERDESTROYNUM){
					alertFlag = 1;
					alertContent += (dataNumber+"���ɻ���"+recoverCount+"�ݣ�\n");
				}
			}else if(destroyType == 1){
				type = "NEEDDESTROYNUM";
				var desCount = data[i].DISINFONUM - data[i].DESTROYNUM;
				if(desCount < data[i].RECOVERDESTROYNUM){
					alertFlag = 1;
					alertContent += (dataNumber+"��������"+desCount+"�ݣ�\n");
				}
			}
		   if(data[i].RECOVERDESTROYNUM==0){
		         flag=1;
		   }else{
			   	oids += data[i].OID + ",";
				nums += data[i].RECOVERDESTROYNUM + ",";
		   }
		   
			if(data[i].OID.indexOf("<%=recDesClassId %>")>=0){
				if(data[i].RECOVERDESTROYNUM != data[i][type])
				{
					alertContent +=(dataNumber+"���ɸ��ķ���");
					alertFlag = 1;		
				}
				
			}
		}
		if(alertFlag ==1){
			plm.endWait();
			alert(alertContent);
			return;
		}
		if(oids.length==0){
	   		alert("���κοɻ��ջ������ٵ���Ϣ������������");
	   		plm.endWait();
	   		return;
	   	}
	   	if(flag==1){
		    if(!plm.confirm('�в������ݣ�����/���ٷ���Ϊ0��ȷ��Ҫ����������')) {
		       plm.endWait();
			   return;
			}
		}
		oids = oids.substring(0, oids.length - 1);
		nums = nums.substring(0, nums.length - 1);
		
		$.ajax({
			url:"<%=updateURL%>",
			type:"post",
			dataType:"json",
			data:"<%=KeyS.OIDS%>="+oids + '&destroyNums='+nums+'&taskOids=<%=oid%>',
			success:function(result){
				plm.endWait();
				if(result.SUCCESS != null &&result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				top.opener.reloadOrder(oids,nums,destroyType);
				onAjaxExecuted();
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'������ʾ', message:"�ύʧ�ܣ�", icon:'1'});
			}
		});
		//setTimeout("onAjaxExecuted()",1000);
	}

	function reload(){
		var table = pt.ui.get("<%=gridId%>");
		table.reload();
	}

	function selectFormatter(v,r,c){
		var status=new StatusEnum(v,c.dataIndex);
		var value = status.getStatusString();
		if(value==null||value=='null'){value='';}
	    return '<div align=left>'+value+'</div>';
	}
	
	//�ر��޸�ҳ�沢���ø�ҳ��ˢ�·���
	function onAjaxExecuted(){
		//window.location.href = "<%=contextPath%>/ddm/distribute/distributeDestroyOrderHandle!getAllDistributeDestoryTasks.action";
		window.close();
	}
	
	function cancleButton(){
		window.close();
	}

</script>