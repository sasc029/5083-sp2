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
		title="回收单";
		destroynums = "回收份数";
	}else{
		title="销毁单";
		destroynums = "销毁份数";
	}
	
	String destroyNumURL = request.getContextPath()+"/ddm/public/getSelect.jsp?select=";
	String updateURL = contextPath + "/ddm/distribute/distributeDestroyOrderHandle!updateDestroyNum.action";

%>
<html>
<head>
	<title>属性信息</title>
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
						<th field="NUMBER"  width="200" tips="编号">编号</th>
						<th field="NAME"  width="200" tips="名称">名称</th>
					   	<th field="DISINFONUM" 	 width="100"  tips="分发份数">分发分数</th>
					   	<th field="RECOVERDESTROYNUM"  width="100" editor="{type:'text'}" tips="回收/销毁份数" ><%= destroynums %></th>
					   	<th field="DESTROYNUM"  width="100" tips="已销毁份数" >已销毁份数</th>
					   	<th field="RECOVERNUM" width="100" tips="已回收份数">已回收份数</th>
					   	<th field="DISINFONAME"  width="100" tips="接收单位/人员" >接收单位/人员</th>
					   	<th field="NOTE" width="150" tips="备注">备注</th>
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
			plm. showMessage({title:'提示', message:"请至少选择1个操作对象!", icon:'3'});
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
			var dataNumber = "编号\""+data[i].NUMBER.substring(data[i].NUMBER.indexOf("title='")+7,data[i].NUMBER.indexOf("'>"))+"\"";
			if (!numReg.test(data[i].RECOVERDESTROYNUM)) {
				alertFlag = 1;
				alertContent += ("请给"+dataNumber +"输入正确份数。\n");
    		}
			if(destroyType == 0){
				type = "NEEDRECOVERNUM";
				var recoverCount = data[i].DISINFONUM - data[i].RECOVERNUM;
				if(recoverCount < data[i].RECOVERDESTROYNUM){
					alertFlag = 1;
					alertContent += (dataNumber+"最多可回收"+recoverCount+"份！\n");
				}
			}else if(destroyType == 1){
				type = "NEEDDESTROYNUM";
				var desCount = data[i].DISINFONUM - data[i].DESTROYNUM;
				if(desCount < data[i].RECOVERDESTROYNUM){
					alertFlag = 1;
					alertContent += (dataNumber+"最多可销毁"+desCount+"份！\n");
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
					alertContent +=(dataNumber+"不可更改份数");
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
	   		alert("无任何可回收或者销毁的信息，请重新输入");
	   		plm.endWait();
	   		return;
	   	}
	   	if(flag==1){
		    if(!plm.confirm('有部分数据，回收/销毁份数为0，确定要回收销毁吗？')) {
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
				plm.showMessage({title:'错误提示', message:"提交失败！", icon:'1'});
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
	
	//关闭修改页面并调用父页面刷新方法
	function onAjaxExecuted(){
		//window.location.href = "<%=contextPath%>/ddm/distribute/distributeDestroyOrderHandle!getAllDistributeDestoryTasks.action";
		window.close();
	}
	
	function cancleButton(){
		window.close();
	}

</script>