<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.adm.active.model.activeset.ActiveSet"%>
<%@page import="com.bjsasc.adm.active.model.activeset.ActiveSetMaster"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String title ="编辑现行套";
	String oid = (String) request.getParameter("OID");
	String classId = Helper.getClassId(oid);
	Persistable obj = Helper.getPersistService().getObject(oid);
	ActiveSet asObj = (ActiveSet)obj;
	ActiveSetMaster asmObj =(ActiveSetMaster) asObj.getMaster();
%>
<html>
<head>
	<title><%=title%></title>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
</head>
<body class="openWinPage">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		 <tr>
	    	<td class="AvidmTitleName">
	    		<div class="imgdiv"><img src="<%=contextPath%>/plm/images/common/modify.gif"/></div>
				<div class="namediv"><%=title%></div></td>
		 </tr>
	</table>
	<form method="post" name="myform" id="myform" ui='form'>
		<input type="hidden"  name="OID" value="<%=oid %>"/>
	    <input type="hidden" name="CLASSID" id="CLASSID" value="<%=classId%>"/>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
			<tr>
			    <td class="left_col AvidmW150" ><span></span>上下文：</td>
			    <td>
			    	<%=asObj.getContextName()==null ? "" : asObj.getContextName()%>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span></span>类型：</td>
			    <td>
			    	<%=asObj.getModelName()==null ? "" : asObj.getModelName()%>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span>*</span>编号：</td>
			    <td >
					<%=asmObj.getNumber()%>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span>*</span>名称：</td>
			    <td>
			    	<%=asmObj.getName()%>
			    </td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span>*</span>来源：</td>
				<td> 
					<input type='text' name='DATASOURCE' id='DATASOURCE' class='pt-text pt-validatebox' style="width:270px" value='<%=asObj.getDataSource()==null ? "" : asObj.getDataSource()%>' <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>现行文件编号：</td>
				<td>
					<input type='text' id="ACTIVEDOCUMENTNUMBER" name="ACTIVEDOCUMENTNUMBER" class='pt-textbox' style="width:270px" value="<%=asObj.getActiveDocumentNumber()==null ? "" : asObj.getActiveDocumentNumber()%>" title="选择现行文件编号" readonly="readonly" ontrigger="selectActiveDocument('ACTIVEDOCUMENTNUMBER', '<%=oid%>')" />
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>页数：</td>
				<td> 
					<input type='text' name='PAGES' id='PAGES' class='pt-text pt-validatebox' style="width:270px" value='<%=asObj.getPages()%>' <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>份数：</td>
				<td> 
					<input type='text' name='COUNT' id='COUNT' class='pt-text pt-validatebox' style="width:270px" value='<%=asObj.getCount()%>' <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span></span>密级：</td>
			    <td>
			    	<%=asObj.getSecLevelName()==null ? "" : asObj.getSecLevelName()%>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span></span>代号：</td>
			    <td>
					<SELECT name='ACTIVECODE' id='ACTIVECODE' class='pt-select' style="width: 270px;"
						url="<%=contextPath%>/platform/objectmodel/param/page/SelectLookupTableItem.jsp?lookupTableId=activeCode">
					</SELECT>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span></span>版本：</td>
			    <td>
			    	<%=asObj.getVersionName()==null ? "" : asObj.getVersionName()%>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span></span>位置：</td>
			    <td>
			    	<%=asObj.getFolderPath()==null ? "" : asObj.getFolderPath()%>
			    </td>
			</tr>			
			<tr>
			 	<td class="left_col AvidmW150">备注：</td>
				<td>
					<TEXTAREA id='NOTE' name='NOTE' class='pt-textarea' style='width:270px;height:60px;' <%=ValidateHelper.buildValidatorAndAssisant()%>><%=asObj.getNote()==null ? "" : asObj.getNote()%></TEXTAREA>
				</td>
			</tr>
			<tr id="includeDynamicFields"></tr>
		</table>
	
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
			    <td class="exegesis AvidmW150"><span>*</span> 为必选/必填项</td>
			    <td>
			    	<div class="pt-formbutton" text="确定"  onclick="submitForm();" <%=ValidateHelper.buildCheckAll()%>></div>
			    	<div class="pt-formbutton" text="取消"  onclick="cancle();"></div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
<script type="text/javascript">

$(document).ready(function() {
	setTimeout("initPage()",300);
});

function initPage() {
	pt.ui.get("ACTIVECODE").setValue("<%=asObj.getActiveCode()%>");
}

// 提交表单
function submitForm(){
	<%=ValidateHelper.buildCheck()%>;
	plm.startWait();
	$.ajax({
		   type: "post",
		   url: "<%=contextPath%>/adm/active/ActiveSetHandle!updataActiveSet.action",
		   dataType:"json",
		   data: $("#myform").serializeArray(),
		   success: function(result){
			    plm.endWait();
				if(result.SUCCESS != null && result.SUCCESS == "false"){
					plm.showAjaxError(result);
				}else{
					//plm.showMessage({title:'提示', message:"编辑操作成功!", icon:'2'});
					top.opener.location.reload();
					top.close();
				}
		   },
		   error:function(){
			   plm.endWait();
			   plm.showMessage({title:'错误提示', message:"编辑操作失败!", icon:'1'});
		   }
		});
}
	
// 取消
function cancle(){
	window.close();
}
</script>
