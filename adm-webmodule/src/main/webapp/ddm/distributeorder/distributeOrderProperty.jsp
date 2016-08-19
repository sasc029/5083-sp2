<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.type.TypeService"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>

<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%

	String title = "发放单信息";
	String path = request.getContextPath();
	String oid = (String) session.getAttribute(ConstUtil.DISTRIBUTE_ORDER_OID);
	Persistable obj = Helper.getPersistService().getObject(oid);
	DistributeOrder dis = (DistributeOrder)obj;
	TypeService typeManager = Helper.getTypeManager();
	Map<String, Object> map = typeManager.format(dis,KeyS.VIEW_DETAIL);
	String lc_new=ConstUtil.LC_NEW.getName();//获得生命周期是新建
	String toolbarId="ddm.OrderPropterty.list.toolbar";
	//工作流中显示对象的连接地址
	String objUrlInWorkflow = "/ddm/distributeorder/distributeOrder_tab.jsp?" + KeyS.OID + "=" + oid;
%>
<html>
<head>
	<title><%=title%></title>
	<script type="text/javascript" src="<%=request.getContextPath() %>/platform/common/js/ptutil.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
</head>
<body class="openWinPage">
<form id="orderForm" name="orderForm" action="#" method="post">
	
	<input type="hidden" value="<%=oid %>" name="<%=KeyS.OID %>" />
	<input type="hidden" name="objName" value="<%=dis.getName() %>" />
	<input type="hidden" name="objectURL" value="<%=objUrlInWorkflow %>" />
	<%
    if (lc_new.equals(dis.getLifeCycleInfo().getStateName())){
    %>
	<table cellSpacing="0" cellPadding="0" width="100%" border="0" style="border-right:#9cbdff solid 1px;border-left:#9cbdff solid 1px;"><tr class="AvidmToolbar"><td> 
				<div class="pt-toolbar">
					<%=UIHelper.getToolBar(toolbarId)%>
				</div> 
			</td></tr></table>	
	<%}%>		
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		 <tr>
	    	<td class="AvidmTitleName">
	    		<div class="imgdiv"><img src="<%=request.getContextPath() %>/plm/images/common/modify.gif"/></div>
				<div class="namediv"><%=title%></div></td>
		 </tr>
	</table>
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
		<tr>
		    <td class="left_col AvidmW150" >单据编号：</td>
		    <td width="35%" style="word-break:break-all;word-wrap:break-word;"><%=dis.getNumber()%></td>
		    <td class="left_col AvidmW150" >单据名称：</td>
		    <td width="35%" style="word-break:break-all;word-wrap:break-word;"><%=dis.getName()%></td>
		</tr>
		<tr>
		    <td class="left_col AvidmW150" >创建者：</td>
		    <td width="35%" style="word-break:break-all;word-wrap:break-word;"><%=dis.getCreateByName()%></td>
		    <td class="left_col AvidmW150" >创建时间：</td>
		    <td width="35%" style="word-break:break-all;word-wrap:break-word;"><%=dis.getCreatTime()%></td>
		</tr>
		<tr>
			<td class='left_col AvidmW150'>单据类型：</td>
			<td width="35%" style="word-break:break-all;word-wrap:break-word;"><%=map.get("ORDERTYPE") %></td>
		 	<td class="left_col AvidmW150">上下文：</td>
			<td width="35%" style="word-break:break-all;word-wrap:break-word;"><%=map.get("DDMCONTEXT")%></td>
		</tr>
		<tr>
			<td class='left_col AvidmW150'>状态：</td>
			<td width="35%" style="word-break:break-all;word-wrap:break-word;"><%=map.get("LIFECYCLE_STATE") %></td>
			<td class="left_col AvidmW150">备注：</td>
			<td width="35%" style="word-break:break-all;word-wrap:break-word;"><%=dis.getNote()%></td>
		</tr>
		<tr id="includeDynamicFields"></tr>
	</table>
	

</form>
</body>
</html>

<script type="text/javascript">
	
	//提交到工作流
	function submitToWorkflow(){
		if (!plm.confirm("确定提交到工作流吗?")) {
			return;
		}
		$("#orderForm").attr("action","<%=path%>/ddm/workflow/startWorkflow.jsp");
		$("#orderForm").submit();
	}
	
	// 提交
	function submitDistribute() {
		if (!plm.confirm("确定提交发放吗?")) {
			return;
		}
		var url = "<%=path%>/ddm/distribute/distributeOrderHandle!updateOrderLifeCycle.action";
		plm.startWait();
		$.ajax({
			url:url,
			type:"post",
			dataType:"json",
			data: "",
			success:function(result){
				plm.endWait();
				if(result.SUCCESS == "true"){
				   reloadOpener();
			  	   top.close(); 
				}else{
				   plm. showMessage({title:'提示', message:"此发放单没有发放数据,请添加发放数据后在提交!", icon:'3'});
				}
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"此发放单提交操作失败！", icon:'1'});
			}
		});		
	}
	
	function updateDistributeOrder() {
		window.location.href = "<%=path%>/ddm/distributeorder/updateDistributeOrder.jsp";
	}
	
	function deleteDistributeOrder() {
	
		if (!plm.confirm("警告,删除发放单会删除该发放单上已添加的分发信息以及相应发放数据,是否继续删除?")) {
			return;
		}
		var url = "<%=path%>/ddm/distribute/distributeOrderHandle!propertydeleteDistributeOrder.action";
		$.ajax({
			url:url,
			type:"post",
			dataType:"json",
			data:"<%=KeyS.OIDS%>=<%=oid%>",
			success:function(result){
				if (result.SUCCESS != null && result.SUCCESS =="false"){
					plm.showAjaxError(result);
				 } else {
				   reloadOpener();	
			  	   top.close(); 
				 }
			},
			error:function(){
				plm.showMessage({title:'错误提示', message:"删除发放单操作失败！", icon:'1'});
			}
		});
	}
	function reloadOpener() {
		var t = top.opener.parent;
		var f = t.frames["mainFrame"];
		if(f!=null){
			f.location.reload();
		}else{
			t.location.reload();
		}
	}
</script>
