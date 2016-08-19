<%@page import="com.bjsasc.plm.url.Url"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String title = "�����ַ����ݶ���";
	String classId = "DistributeObject";
%>
<html>
	<head>
		<title><%=title%></title>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
	</head>
<body class="openWinPage">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
	    	<td class="AvidmTitleName">
				<div class="imgdiv"><img src="<%=contextPath%>/plm/images/project/createproject.gif"/></div>
				<div class="namediv"><%=title%></div>
			</td>
		</tr>
	</table>
	<FORM id='main_form' ui='form'>
	    <!-- ��ATDocument,��������������������跽�� -->
	    <input type="hidden" name="CLASSID" id="CLASSID" value="<%=classId%>"/>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
			<tr>
			    <td class="left_col AvidmW150" ><span>*</span>�ַ�����Դ�ڲ���ʶ��</td>
			    <td>
			    	<input type='text' id="dataInnerId" name="dataInnerId" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);"/>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span>*</span>�ַ�����Դ������</td>
			    <td>
			    	<input type='text' id="dataClassId" name="dataClassId" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);"/>
			    </td>
			</tr>
			<tr>
			 	<td class="left_col AvidmW150"><span>*</span>�ַ�����Դ��Դ��</td>
				<td>
			    	<input type='text' id="dataFrom" name="dataFrom" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);"/>
				</td>
			</tr>
			<tr>
			 	<td class="left_col AvidmW150"><span>*</span>��ţ�</td>
				<td>
			    	<input type='text' id="id" name="id" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);"/>
				</td>
			</tr>
			<tr>
			 	<td class="left_col AvidmW150"><span>*</span>���ƣ�</td>
				<td>
			    	<input type='text' id="name" name="name" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);"/>
				</td>
			</tr>
			<tr>
			 	<td class="left_col AvidmW150"><span>*</span>���ͣ�</td>
				<td>
			    	<input type='text' id="type" name="type" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);"/>
				</td>
			</tr>
			<tr>
			 	<td class="left_col AvidmW150"><span>*</span>�ͺţ�</td>
				<td>
			    	<input type='text' id="code" name="code" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);"/>
				</td>
			</tr>
			<tr>
			 	<td class="left_col AvidmW150"><span>*</span>�汾��</td>
				<td>
			    	<input type='text' id="version" name="version" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);"/>
				</td>
			</tr>
			<tr>
			 	<td class="left_col AvidmW150"><span>*</span>��ע��</td>
				<td>
			    	<input type='text' id="note" name="note" class="pt-textbox AvidmW270" onfocus="plm.hideTip(this);"/>
				</td>
			</tr>
			<tr id="includeDynamicFields"></tr>
		</table>

		<table width="100%" height="40px" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="exegesis AvidmW150"><span>*</span> Ϊ��ѡ/������</td>
			    <td>
					<div class="pt-formbutton" text="ȷ��"  onclick="saveSubmitForm();"></div>
					<div class="pt-formbutton" text="ȡ��"  onclick="cancleButton();"></div>
				</td>
			</tr>
		</table>
	</FORM>
</body>
</html>

<script type="text/javascript">
			
	getfield("<%=classId%>");
	
	function getfield(classId){
		plm.getExtAttr({trid:'includeDynamicFields',colnum:1,classid:classId});
	}

	//����    �ύ��
	function saveSubmitForm() {
		var main_form = pt.ui.get("main_form");
		var name = $("#name").val();//����
		var options = {
				url:"<%=contextPath%>/ddm/distribute/distributeObjectHandle!createDistributeObject.action",
				dataType:"json",
				type:"POST",
				success:function(result) {
					plm.endWait();
					if(result.SUCCESS !=null && result.SUCCESS == "false"){
						plm.showAjaxError(result);
						return;
					}
					onAjaxExecuted();
				},
				error:function(a){
					plm.endWait();
					plm.showMessage({title:'������ʾ', message:a.responseText, icon:'1'});
				}
			};
		plm.startWait();
		main_form.submit(options);
	}
	
	function cancleButton(){
		window.close();
	}

	//�ر��޸�ҳ�沢���ø�ҳ��ˢ�·���
	function onAjaxExecuted(){
		window.close();
	}
</script>
