<%@page import="java.util.Map"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.type.TypeService"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String path = request.getContextPath();
    String oid=(String)session.getAttribute(ConstUtil.DISTRIBUTE_PAPERTASK_OID);
	DistributePaperTaskService service = DistributeHelper.getDistributePaperTaskService();
	DistributePaperTask dis = service.getDistributePaperTaskProperty(oid);	
	TypeService typeManager = Helper.getTypeManager();
	Map<String, Object> map = typeManager.format(dis,KeyS.VIEW_DETAIL);
	String title="纸质任务信息";

	
	String contextPath = request.getContextPath();
	String classId = DistributePaperTask.CLASSID;
	String gridId = "";
	String gridTitle = "";
	String toolbarId = "";

%>
<html>
<head>
	<title>属性信息</title>
	<script type="text/javascript" src="<%=request.getContextPath() %>/platform/common/js/ptutil.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
	<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
</head>
<body class="openWinPage">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		 <tr>
	    	<td class="AvidmTitleName">
	    		<div class="imgdiv"><img src="<%=request.getContextPath() %>/plm/images/common/modify.gif"/></div>
				<div class="namediv"><%=title%></div></td>
		 </tr>
	</table>
	<input type="hidden" value="<%=oid %>" name="<%=KeyS.OID %>" />
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
			<tr>
			    <td class="left_col AvidmW150" >任务编号：</td>
			    <td width="35%" style="word-break:break-all;word-wrap:break-word;">
			    	<%=dis.getNumber()%>
			    </td>
			    <td class="left_col AvidmW150">上下文：</td>
				<td width="35%" style="word-break:break-all;word-wrap:break-word;">
					<%=map.get("DDMCONTEXT")%>
				</td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" >创建人：</td>
			    <td width="35%" style="word-break:break-all;word-wrap:break-word;">
			    	<%=dis.getCreateByName()%>
			    </td>
			    <td class="left_col AvidmW150" >创建时间：</td>
			    <td width="35%" style="word-break:break-all;word-wrap:break-word;">
			    	<%=dis.getCreateTime()%>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" >创建单位：</td>
			    <td width="35%" style="word-break:break-all;word-wrap:break-word;">
			    	 <%=dis.getOrganization().getUserOrgName() %>
			    </td>
			    <td class="left_col AvidmW150">所属发放单：</td>
			    <td width="35%" style="word-break:break-all;word-wrap:break-word;">
			    	<a href ="#" onclick="javascript:selectOrderProperty('<%=dis.getOrderOid() %>')"><%=dis.getOrderName() %></a>	
			    </td>
			</tr>
			<tr>
			<td class='left_col AvidmW150'>状态：</td>
			<td width="35%" style="word-break:break-all;word-wrap:break-word;">
				<%=map.get("LIFECYCLE_STATE") %></td>
			<td class="left_col AvidmW150">紧急程度：</td>
			<td width="35%" style="word-break:break-all;word-wrap:break-word;">
				<%=map.get("DISURGENT") %>
			</td>
		</tr>
		<tr id="includeDynamicFields"></tr>
		</table></td></tr>
		<tr><td>
		
		</td></tr></table>
</body>
</html>
<script type="text/javascript">

	function selectOrderProperty(value){
		var url = '<%=contextPath%>/ddm/distributeorder/distributeOrder_tab.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + value;
		ddm.tools.openWindow(url,800,600,"distributeOrderOpen");
	}
	
	//关闭修改页面并调用父页面刷新方法
	function onAjaxExecuted(result){	
		opener.tableReload("修改成功");		
		window.close();
	}
	
	function cancleButton(){
		window.close();
	}

</script>
