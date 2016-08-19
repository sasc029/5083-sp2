<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.adm.active.model.activeorder.ActiveOrder"%>
<%@page import="com.bjsasc.adm.active.model.activeorder.ActiveOrderMaster"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String title ="编辑现行单据";
	String oid = (String) request.getParameter("OID");
	String innerId = Helper.getInnerId(oid);
	String classId = Helper.getClassId(oid);
	Persistable obj = Helper.getPersistService().getObject(oid);
	ActiveOrder aoObj = (ActiveOrder)obj;
	ActiveOrderMaster AOMasterObj =(ActiveOrderMaster) aoObj.getMaster();
%>
<html>
<head>
	<title><%=title%></title>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
	<script type="text/javascript" src="<%=contextPath%>/plm/ui/editor/date/WdatePicker.js"></script>
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
			    	<%=aoObj.getContextName()==null ? "" : aoObj.getContextName()%>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span></span>类型：</td>
			    <td>
			    	<%=aoObj.getModelName()==null ? "" : aoObj.getModelName()%>
			    </td>
			</tr>
			<tr id="iframeShow">
				<td class='left_col AvidmW150'>文件：</td>
				<td>
					<div>
		   				 <iframe id="viewWindow" name="viewWindow" scrolling="no" frameborder="0" src="<%=request.getContextPath() %>/plm/docman/FileOperate.jsp?oid=<%=oid%>"   style="width: 100%; height:80;"> </iframe>
		    		</div>
		   	    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span></span>编号：</td>
			    <td >
					<%=AOMasterObj.getNumber()%>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span></span>名称：</td>
			    <td>
			    	<%=AOMasterObj.getName()%>
			    </td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span>*</span>来源：</td>
				<td> 
					<input type='text' name='DATASOURCE' id='DATASOURCE' class='pt-text pt-validatebox' style="width:270px" value='<%=aoObj.getDataSource()==null ? "" : aoObj.getDataSource()%>' <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>现行文件编号：</td>
				<td>
					<input type='text' id="ACTIVEDOCUMENTNUMBER" name="ACTIVEDOCUMENTNUMBER" class='pt-textbox' style="width:270px" value="<%=aoObj.getActiveDocumentNumber()==null ? "" : aoObj.getActiveDocumentNumber()%>" title="选择现行文件编号" readonly="readonly" ontrigger="selectActiveDocument('ACTIVEDOCUMENTNUMBER', '<%=oid%>')" />
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>页数：</td>
				<td> 
					<input type='text' name='PAGES' id='PAGES' class='pt-text pt-validatebox' style="width:270px" value='<%=aoObj.getPages()%>' <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>份数：</td>
				<td> 
					<input type='text' name='COUNT' id='COUNT' class='pt-text pt-validatebox' style="width:270px" value='<%=aoObj.getCount()%>' <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>编制者：</td>
				<td> 
					<input type='text' name='AUTHORNAME' id='AUTHORNAME' class='pt-text pt-validatebox' style="width:270px" value='<%=aoObj.getAuthorName()==null ? "" : aoObj.getAuthorName()%>' <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>编制单位：</td>
				<td> 
					<input type='text' name='AUTHORUNIT' id='AUTHORUNIT' class='pt-text pt-validatebox' style="width:270px" value='<%=aoObj.getAuthorUnit()==null ? "" : aoObj.getAuthorUnit()%>' <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>编制日期：</td>
				<td> 
				<input type='text' id="AUTHORTIME" name="AUTHORTIME" value="<%=aoObj.getDisAuthorTime()%>"
						class="Wdate pt-textbox" readonly="readonly"  style="width:100px;"
				        onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'1970-01-01',maxDate:'2128-03-10'})"/>			
				</td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span></span>密级：</td>
			    <td>
			    	<%=aoObj.getSecLevelName()==null ? "" : aoObj.getSecLevelName()%>
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
			    	<%=aoObj.getVersionName()==null ? "" : aoObj.getVersionName()%>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span></span>位置：</td>
			    <td>
			    	<%=aoObj.getFolderPath()==null ? "" : aoObj.getFolderPath()%>
			    </td>
			</tr>
			<tr>
			 	<td class="left_col AvidmW150">备注：</td>
				<td>
					<TEXTAREA id='NOTE' name='NOTE' class='pt-textarea' style='width:270px;height:60px;' <%=ValidateHelper.buildValidatorAndAssisant()%>><%=aoObj.getNote()==null ? "" : aoObj.getNote()%></TEXTAREA>
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
	pt.ui.get("ACTIVECODE").setValue("<%=aoObj.getActiveCode()%>");
}

// 提交表单
function submitForm(){
	var innerId = "<%=innerId%>";
	var classId = "<%=classId%>";
	<%=ValidateHelper.buildCheck()%>;

	try {
		var beforeOids=top.window.frames["tab4"].getBeforeDataParam();
		var afterOids=top.window.frames["tab4"].getAfterDataParam();
	} catch (e) {
		beforeOids = "";
		afterOids = "";
	}
	
	plm.startWait();
	$.ajax({
		   type: "post",
		   url: "<%=contextPath%>/adm/active/ActiveOrderHandle!updataActiveOrder.action?"
				+"beforeOids="+beforeOids
				+"&afterOids="+afterOids,
		   dataType:"json",
		   data: $("#myform").serializeArray(),
		   success: function(result){
			    plm.endWait();
				if(result.SUCCESS != null && result.SUCCESS == "false"){
					plm.showAjaxError(result);
				}else{
					// plm.showMessage({title:'提示', message:"编辑操作成功!", icon:'2'});
					
					var iframes = window.frames["viewWindow"];
	        		//如果有文件则上传文件
	        		if(iframes.isHasFile()){
	        			iframes.submitFile(innerId, classId);
	        		}else{	
	        			callBack();
	        		}					
				}
		   },
		   error:function(){
			   plm.endWait();
			   plm.showMessage({title:'错误提示', message:"编辑操作失败!", icon:'1'});
		   }
		});
}


function callBack(){
	try {
		var lu = top.opener.location.href;
		if (lu.indexOf("visit.jsp") > 0 || lu.indexOf("folderlist.jsp") > 0) {
			top.opener.location.reload();			
		} else {
			top.opener.parent.location.reload();
		}
	} catch (e) {alert(e.message);}
	top.close();
}
	
// 取消
function cancle(){
	window.close();
}

</script>
