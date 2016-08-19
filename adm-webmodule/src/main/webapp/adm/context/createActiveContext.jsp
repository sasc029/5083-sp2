<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.adm.active.model.activecontext.ActiveContext"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.adm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@include file="/plm/plm.jsp" %>
<%@page language="java"%>
<%@page session="true"%>
<%
	String contextPath = request.getContextPath();
	String classId = request.getParameter(KeyS.CONTAINER_OID);
	String parentContext = contextPath+"/plm/context/context/productContext/createProductContext_getParentContexts.jsp";
	String callback = request.getParameter(KeyS.CALLBACK);
	String title = "�½������ļ�������";
	String number = ConstUtil.ID_ACTIVE_CONTEXT;
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
	<FORM id='form_assetplace' ui='form'>
	
	<input type="hidden" name="CLASSID" value="ProductContext">
	
		<TABLE class="avidmTable" width="100%" border="0" cellspacing="0" cellpadding="0">
			<TR>
				<TD class='left_col AvidmW150'><span>*</span>��ţ�</TD>
				<TD>
					<INPUT type='text' name='NUMBER' readonly class='pt-textbox AvidmW270' value="<%=number%>" <%=ValidateHelper.buildValidator()%>>
				</TD>
			</TR>
			<TR>
				<TD class='left_col AvidmW150'><span>*</span>���ƣ�</TD>
				<TD>
					<INPUT type='text' name='NAME' class='pt-textbox AvidmW270' <%=ValidateHelper.buildValidator()%>>
				</TD>
			</TR>
			<TR>
				<TD class='left_col AvidmW150'><span>*</span>�������ģ�</TD>
				<TD cols="1" rows="2">
					<SELECT id='parentContextOid' name='parentContextOid' onchange="getTeamList(this)" class='pt-select' style='width:270px;' url='<%=parentContext%>'>
						<option id="please_select_template" value="">��ѡ��</option>
					</SELECT>
				</TD>
			</TR>
			<TR>
				<TD class='left_col AvidmW150'>�����Ŷӣ�</TD>
				<TD cols="1" rows="2">
					<SELECT id='teamOid' name='teamOid' class='pt-select' onchange="getTeam(this)" style='width:270px;'>
						<option id="please_select_template" value="">��ѡ��</option>
					</SELECT>
				</TD>
			</TR>
			<TR id="userShareTeam" style="display:none;">
				<TD class='left_col AvidmW150'>������չ��</TD>
				<TD>
					&nbsp;<INPUT type='radio' name='extended' id="extended_true" class='pt-radio' value='true'>&nbsp;��
					&nbsp;<INPUT type='radio' name='extended' id="extended_false" class='pt-radio' value='false'>&nbsp;��<br/>
					&nbsp;(������ɫ�� / ���Ա�Ա��ط�ʽ��ӵ���������)
				</TD>
			</TR>
			<TR>
				<TD class='left_col AvidmW150'>�Ƿ�Ϊר�ã�</TD>
				<TD>
					&nbsp;<INPUT type='radio' name='isPrivate' class='pt-radio' value='isPrivate' >&nbsp;��
					&nbsp;<INPUT type='radio' name='isPrivate' class='pt-radio' value='noPrivate' checked="checked">&nbsp;��
				</TD>
			</TR>
			<TR>
				<TD class='left_col AvidmW150'>��ע��</TD>
				<TD>
					<TEXTAREA name='NOTE' class='pt-textarea' style='width:270px;height:60px;' <%=ValidateHelper.buildValidatorAndAssisant()%>></TEXTAREA>
				</TD>
   			</TR>
   		</table>
   		<table width="100%" border="0" cellspacing="0" cellpadding="0">
   			<TR>
   				<TD class="exegesis AvidmW150"><span>*</span> Ϊ��ѡ/������</TD>
   				<TD>
   					<div class="pt-formbutton" text="ȷ��" onclick="doCreateProductContext()" <%=ValidateHelper.buildCheckAll()%>></div>
					<div class="pt-formbutton" text="ȡ��" onclick="plm.closeWin()"></div>
   				</TD>
   			</TR>
   		</table>
		<INPUT ui='textField' type='hidden' name='classIID' value='<%=classId%>'>
   	</FORM>
</body>
</html>
<script type="text/javascript">
	var form_assetplace = pt.ui.get("form_assetplace");

	function doCreateProductContext(){
		
		<%=ValidateHelper.buildCheck()%>
		
		var parentContextOid = form_assetplace.getFieldValue("parentContextOid");
		if(parentContextOid == ''){
			plm.showMessage({title:'��ʾ', message:"�������Ĳ���Ϊ�ա�", icon:'1'});
			return ;
		}
		var options = {
			url:contextPath + "/adm/context/createActiveContext_save.jsp",
			dataType:"json",
			type:"POST",
			success:function(result){
				plm.endWait();
				if(result.SUCCESS !=null && result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				onAjaxExecuted(result);
			},
			error:function(a){
				plm.showAjaxError(a.responseText);
			}
		};
		plm.startWait();
		form_assetplace.submit(options);
	}
	
	function onAjaxExecuted(result){	
		<%if(callback != null){%>
			opener.<%=callback%>(result);
		<%}%>
		window.close();
	}
	
	function getTeamList(obj){
		var contextOid = obj.options[obj.selectedIndex].value;
		var contextClassId = contextOid.split(":")[0];
		if(contextClassId == 'RootContext'){
			document.getElementById("teamOid").disabled = true;
			document.getElementById("isPrivate").disabled = true;
		}else{
			document.getElementById("teamOid").disabled = false;
			document.getElementById("isPrivate").disabled = false;
		}
		document.getElementById("userShareTeam").style.display="none";
		var param = {
			url : contextPath + "/plm/context/context/productContext/productContextHandle.jsp?op=getTeamList&contextOid="+contextOid,
        	type: "POST",
        	success: function(result) {
        		var select = document.getElementById("teamOid");
				var len = select.options.length;
	        	for(var i=0;i<len;i++){
	        		select.remove(0);
	        	}
	       		var evalResult = eval('('+result+')');
	        	for(var j=0;j<evalResult.length;j++){
	        		select.options[j] = new Option(evalResult[j].text,evalResult[j].value);
	        	}
        	}
	    };
		UI.ajax(param);
	}
	
	function getTeam(obj){
		if(obj.options[obj.selectedIndex].text != '��ѡ��'){
			document.getElementById("isPrivate").disabled = true;
			var isPrivateObj = document.getElementsByName("isPrivate");
			isPrivateObj.item(1).checked = true;
		}else{
			document.getElementById("isPrivate").disabled = false;
		}
		/**
		*yinjiubo modify by yinjiubo   2012-8-30 18:58
		*/
		if(obj.options[obj.selectedIndex].value != ''){
			document.getElementById("userShareTeam").style.display="";
			var oid = obj.options[obj.selectedIndex].value;
			var url = contextPath + "/plm/context/team/shareTeamInfo_get.jsp?OID="+oid;
			var data = {};
		 	var options = {};
			options.success = function(data){
				document.getElementById("extended_true").disabled = false;
				document.getElementById("extended_false").disabled = false;
				if(data.trim()=="false"){
					document.getElementById("extended_true").disabled = true;
					document.getElementById("extended_false").disabled = false;
					document.getElementById("extended_true").checked = false;
					document.getElementById("extended_false").checked = true;
					document.getElementById("extended_true").onclick = function(){
						
						document.getElementById("extended_true").checked = false;
						document.getElementById("extended_false").checked = true;
						//plm.showMessage({title:'��ʾ', message:"����ʹ�ô�ѡ���Ϊѡ���ŶӲ�����չ����ѡȡһ������չ���¹����Ŷӡ�", icon:'1'});
						//pt.ui.alert({title:"��ʾ",message:"����ʹ�ô�ѡ���Ϊѡ���ŶӲ�����չ����ѡȡһ������չ���¹����Ŷӡ�"});
						
					}
					document.getElementById("extended_false").onclick = function(){
						
						document.getElementById("extended_false").checked = false;
						document.getElementById("extended_false").checked = true;
						//plm.showMessage({title:'��ʾ', message:"����ʹ�ô�ѡ���Ϊѡ���ŶӲ�����չ����ѡȡһ������չ���¹����Ŷӡ�", icon:'1'});
						//pt.ui.alert({title:"��ʾ",message:"����ʹ�ô�ѡ���Ϊѡ���ŶӲ�����չ����ѡȡһ������չ���¹����Ŷӡ�"});
						
					}
				}else{
					document.getElementById("extended_true").checked = true;
					document.getElementById("extended_true").onclick = function(){
					}
					document.getElementById("extended_false").onclick = function(){
					}
				}
			}
			options.failure = function(data){
				plm.showMessage({title:'��ʾ', message:data.INFO, icon:'1'});
				//pt.ui.alert({title:"��ʾ",message:data.INFO});
			}
			options.data = data;    
		    options.url = url;
		    pt.ui.ajax(options);
		}else{
			document.getElementById("userShareTeam").style.display="none";
		}
	}
</script>