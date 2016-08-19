<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%
	String contextPath = request.getContextPath();
	String oid = request.getParameter("OID");
	String title = "ҵ�����ҽ�";
	String treeUrl = "listDistributePartSearch.jsp";
	String contentUrl="listDistributeDataObjects.jsp?oid="+oid;
	//String contentUrl = contextPath + "/ddm/distribute/distributeObjectHandle!getAllDistributeDataObject.action?OID=" + oid;

	String url = contextPath + "/ddm/distribute/distributeObjectHandle!addDistributeDataObjectLink.action";
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
			splitVertical:true,				    //ˮƽ�ָ�Ǵ�ֱ�ָ�,���Ϊtrue,���Ǵ�ֱ�ָ�,Ҳ�������ҷָ�
			A:"tdLeft",						    //���������id,����
			B:"tdRight",					    //�Ҳ�������id,����
			closeableto:20,					    //�Զ����ص���С��Ȼ�߶�,����Ĭ�ϼ���
			slaveleft:"distributeObjectTree",	//���������grid��tree�ؼ���id
			slaveright:"distributeInfoContent",	//�Ҳ�������grid��tree�ؼ���id
			retfunc:"plm.resizecontrol"});	    //�ص�����,����Ĭ�ϼ���
	});
	</script>
</head>
<body>
	<table id="table" class="splittable" style="height:95%;width:100%;" cellspacing="0" cellpadding="0" border="0"> 
		<tr> 
			<td id="tdLeft" class="leftpanel" style="width:40%;"> 
				<iframe id="leftTree" name="leftTree" style="width:100%;height:100%;"
					src="<%=treeUrl%>"></iframe>
			</td>
			<td id="tdRight">
				<iframe id="rightContent" name="rightContent" style="width:100%;height:100%; "
					src="<%=contentUrl%>"></iframe>
			</td>
		</tr>
	</table>
	<table id="Articulate" class="splittable" style="height:5%;width:100%;" cellspacing="0" cellpadding="0" border="0"> 
		<tr>
			<td align="center">
				<div class="pt-formbutton" text="ȷ��" id="submitbutton" onclick="onOk();"></div>
				<div class="pt-formbutton" text="ȡ��" id="closebutton" onclick="window.close();"></div>		
			</td>
		</tr>
	</table>
</body>
<script>
	function onOk(){
		var partOid = document.frames("leftTree").getSelections();
		if(partOid == null){
			plm.showMessage({title:'������ʾ', message:"��ѡ�񲿼���", icon:'1'});
			return;
		}
		var objectOids = document.frames("rightContent").getSelections();
		if(objectOids == null){
			plm.showMessage({title:'������ʾ', message:"��ѡ����Ҫ�ҽӵĶ���", icon:'1'});
			return;
		}
		
		$.ajax({
			url:"<%=url%>",
			type:"post",
			dataType:"json",
			data:"partOid="+partOid+"&objectOids="+objectOids,
			success:function(result){
				plm.endWait();
				if (result.SUCCESS != null && result.SUCCESS =="false"){
					plm.showAjaxError(result);
				} else {
					alert("����ҽӳɹ���");
					window.close();
				}
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'������ʾ', message:"����ҽ�ʧ�ܣ�", icon:'1'});
			}
		});
	}
	</script>
</html>
