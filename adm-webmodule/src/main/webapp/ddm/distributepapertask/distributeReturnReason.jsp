<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp"%>

<%
	String contextPath = request.getContextPath();
	String gridId = "distributePaperTaskGrid";
	String gridTitle = "����ԭ��";
	String submitUrl = contextPath + "/ddm/distribute/distributePaperTaskHandle!updateDistributePaperTask.action";
%>
<html>
<head>
<title><%=gridTitle%></title>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
</head>
<body>
	<form id="mainForm" name="mainForm" method="POST">
		<table align="center">
		<tr>
			<td>
				����д�˻�ԭ��<br>
				<textarea name="returnReason" id="returnReason" rows="5"></textarea>
				<input type="hidden" name="oids" value="<%=request.getParameter("OIDS") %>">
				<input type="hidden" name="type" value="<%=request.getParameter("type")%>">
				<input type="button" name="save" value="ȷ��" onclick="saveDisagree()">
			</td>
		</tr>
		</table>
	</form>
</body>
<form id="download" name="download" method="POST"></form>
</html>
<script type="text/javascript">

	function saveDisagree(){
		var returnReason = document.getElementById("returnReason").value;
		var oids = document.getElementById("oids").value;
		var type = document.getElementById("type").value;

		if(returnReason.length>=70){
			alert("����ԭ��������������");
			return;
		}
		$.ajax({
			url:"<%=submitUrl%>",
			type:"post",
			dataType:"text",
			data:"<%=KeyS.OIDS%>="+oids+"&flag=reject&returnReason="+returnReason+"&type="+type,
			success:function(result){
				plm.endWait();
				onAjaxExecuted();
				reload();
			},
			error:function(){
				plm.showMessage({title:'������ʾ', message:"�ύʧ�ܣ�", icon:'1'});
			}
		});
	}
	//�رմ��ڲ�ˢ��
	function onAjaxExecuted(){
		window.returnValue = "success";
		window.close();
		reload();
	}
	//ˢ��dategrid
	function tableReload(text){
		plm.showMessage({title:'ϵͳ��ʾ', message:text, icon:'2'});
		reload();
	}
</script>
