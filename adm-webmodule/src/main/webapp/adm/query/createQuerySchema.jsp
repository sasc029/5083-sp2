<%@page import="java.net.URLDecoder"%>
<%@page import="com.bjsasc.plm.core.change.ChangeKeyWords"%>
<%@page import="com.bjsasc.plm.core.change.notice.NoticeContent"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.ui.UIDataInfo"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.core.change.ChangeHelper" %>
<%@page import="com.bjsasc.plm.core.change.notice.NoticeContentMark" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>创建方案</title>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/platform/common/js/ptutil.js"
	charset="GBK"></script>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
</head>
<body  bgcolor="#E0ECFF" onload="" class="openWinPage">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="AvidmTitleName"><div class="imgdiv"><img src="<%=request.getContextPath()%>/plm/images/common/rename.gif"/></div>
    <div class="namediv">保存方案</div></td>
  </tr>
</table>
	<form name="eciForm" method="post" id="eciForm" ui="form" action="">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
			<tr>
				  <td class="left_col AvidmW150"><span>*</span>标题：</td>
				  <td colspan="3"><input type="text"  id="schemaTitle" name="schemaTitle" value=""/></td>
			</tr>
			<tr>
				<td class="left_col AvidmW150">设置为默认方案：</td>
			    <td><input type="radio" id="is_DefaultSchema" name="setDefaultSchema"  value="1">是
			      <input id="no_DefaultSchema" name="setDefaultSchema" type="radio"  value="0" checked>否 
	        	</td>
	      	</tr>
		</table>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			  <tr>
			    <td class="AvidmDecision">
			    	<div class="pt-formbutton" text="新建"	id="newBtn"  onclick="newshcame()"></div>
			    	<div class="pt-formbutton" text="保存"	id="saveBtn"  onclick="setshcame()"></div>
					<div class="pt-formbutton" text="取消"	id="closebutton" onclick="cancelSubmit()"></div>
				</td>
			  </tr>
		</table>
	</form>
</body>
<script type="text/javascript">

var title="";
var is_default="";
	
var formData=window.opener.formData;
var Oid=formData.OID;
var defaultName=formData.DEFAULTNAME;
var chked=formData.CHKED;
var is_new="add";
function newshcame(){
	document.getElementById("schemaTitle").value="";
	is_new="add";
}

if(defaultName!=""){
	var is_new="update";
	 document.getElementById("schemaTitle").value=defaultName;
	 var defaultSchema =document.getElementsByName("setDefaultSchema");
	 for(var i=0;i<defaultSchema.length;i++){
			if(defaultSchema[i].value==chked){
				defaultSchema[i].checked=true;
			}
		}
}
function setshcame(){
	 title=document.getElementById("schemaTitle").value;
	 if(title==""){
		 plm.showMessage({
	         title   : "提示",
		     message : "请填写标题",
		     icon 	 : "3"
		 });
		 return;
	 }
	 var defaultSchema =document.getElementsByName("setDefaultSchema");
	is_default="";
	for(var i=0;i<defaultSchema.length;i++){
		if(defaultSchema[i].checked==true){
			is_default=defaultSchema[i].value;
		}
	}
	 window.opener.formData.TITLE=title;
	 window.opener.formData.IS_DEFAULT=is_default;
	 $.ajax({
			url:"querySchema_save.jsp?op="+is_new,
			dataType: "json",
			data:formData,
			type:  "post",
			success:function(result){
				 plm.showMessage({
			         title   : "提示",
				     message : "处理成功",
				     icon 	 : "2"
				 });
				parent.opener.refresh(title,is_default);
				cancelSubmit();
			},
			error:function(){
				 plm.showMessage({
			         title   : "提示",
				     message : "对不起，处理失败",
				     icon 	 : "1"
				 });
				
			}
		});
 }
 
function cancelSubmit(){
		window.close();
}
 
</script>
</html>