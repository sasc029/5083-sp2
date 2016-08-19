<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%
	String contextPath = request.getContextPath();
	String title = "�ۺϲ�ѯ";
	String conditionsUrl = contextPath + "/ddm/commonsearch/listDistributeConditions.jsp";
	String detailListUrl = contextPath + "/ddm/commonsearch/listDistributeAllObjects.jsp";
%>
<html>
<head>
	<title><%=title%></title>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
	<script type="text/javascript" src="<%=Url.RPMJS%>" charset="GBK"></script>
	<script type="text/javascript"	src="<%=contextPath%>/plm/javascript/uisplitter.js" charset="UTF-8"></script>
	<script type="text/javascript">
	//����splitbar
	$(document).ready(function() {
		$("#table").splitter({
			splitVertical:false,					    //ˮƽ�ָ�Ǵ�ֱ�ָ�,���Ϊtrue,���Ǵ�ֱ�ָ�,Ҳ�������ҷָ�
			A:"tdTop",								    //���������id,����
			B:"tdBottom",					 		    //�Ҳ�������id,����
			closeableto:20,					  		    //�Զ����ص���С��Ȼ�߶�,����Ĭ�ϼ���
			slaveleft:"",								//���������grid��tree�ؼ���id
			slaveright:"distributeCommonSearchtGrid",	//�Ҳ�������grid��tree�ؼ���id
			retfunc:"plm.resizecontrol"});	 		    //�ص�����,����Ĭ�ϼ���
	});
	</script>
</head>
<body>
	<table id="table" class="splittable" style="height:100%;width:100%;" cellspacing="0" cellpadding="0" border="0"> 
		<tr> 
			<td id="tdTop" valign="top" style="width:100%;"> 
				<iframe id="conditions" name="conditions" style="width:100%;height:70%"
					src="<%=conditionsUrl%>"></iframe>
			</td>
		</tr>
		<tr> 
			<td id="tdBottom">
				<iframe id="detailList" name="detailList" style="width:100%;height:100%;"
					src="<%=detailListUrl%>"></iframe>
			</td>
		</tr>
	</table>
</body>
</html>
