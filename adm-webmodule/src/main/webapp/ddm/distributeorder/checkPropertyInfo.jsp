<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>

<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String path = request.getContextPath();
	String oid = request.getParameter("oid");
	
	Persistable obj = Helper.getPersistService().getObject(oid);
	DistributeOrder dis = (DistributeOrder)obj;
	String orderType = dis.getOrderType();
	if(("0").equals(orderType)){
		orderType = "���ŵ�";
	}else{
		orderType = "�������ŵ�";
	}
%>
<html>
<head>
	<title>���ŵ���Ϣ�鿴</title>
	<script type="text/javascript" src="<%=request.getContextPath() %>/platform/common/js/ptutil.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
</head>
<body class="openWinPage">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		 <tr>
	    	<td class="AvidmTitleName">
	    		<div class="imgdiv"><img src="<%=request.getContextPath() %>/plm/images/common/modify.gif"/></div>
				<div class="namediv">���ŵ���Ϣ�鿴</div></td>
		 </tr>
	</table>
	
	<input type="hidden" value="<%=oid %>" name="<%=KeyS.OID %>" />
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
			<tr>
			    <td class="left_col AvidmW150" >���ݱ�ţ�</td>
			    <td>
			    	<%=dis.getId()%>
			    </td>
			    <td class="left_col AvidmW150" >�������ƣ�</td>
			    <td>
			    	<%=dis.getName()%>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" >�����ߣ�</td>
			    <td>
			    	<%=dis.getManageInfo().getCreateBy().getName()%>
			    </td>
			    <td class="left_col AvidmW150" >����ʱ�䣺</td>
			    <td>
			    	<%=dis.getManageInfo().getCreateTimeDisplay()%>
			    </td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'>�������ͣ�</td>
				<td>
					<%=orderType %>
				</td>
			 	<td class="left_col AvidmW150">��ע��</td>
				<td>
					<%=dis.getNote()%>
				</td>
			</tr>
			<tr id="includeDynamicFields"></tr>
		</table>
</body>
</html>
<script type="text/javascript">
	
	//�ر��޸�ҳ�沢���ø�ҳ��ˢ�·���
	function onAjaxExecuted(result){	
		opener.tableReload("�޸ĳɹ�");		
		window.close();
	}
	
	function cancleButton(){
		window.close();
	}

</script>
