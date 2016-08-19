<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>
<%@page import="com.bjsasc.plm.util.JsonUtil"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.adm.active.model.activedocument.ActiveDocument"%>
<%@page import="com.bjsasc.adm.active.model.activedocument.ActiveDocumentMaster"%>
<%@page import="com.bjsasc.platform.objectmodel.business.persist.ContainerIncluded"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String title ="�༭�����ļ�";
	String oid = (String) request.getParameter("OID");
	String classId = Helper.getClassId(oid);
	String innerId = Helper.getInnerId(oid);
	Persistable obj = Helper.getPersistService().getObject(oid);
	ActiveDocument adObj = (ActiveDocument)obj;
	ActiveDocumentMaster ADMasterObj =(ActiveDocumentMaster) adObj.getMaster();
	
	String extAttrJson = DataUtil.encode(Helper.getPersistService().getExtAttrs((ContainerIncluded)adObj));
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
			    <td class="left_col AvidmW150" ><span></span>�����ģ�</td>
			    <td>
			    	<%=adObj.getContextName()==null ? "" : adObj.getContextName()%>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span></span>���ͣ�</td>
			    <td>
			    	<%=adObj.getModelName()==null ? "" : adObj.getModelName()%>
			    </td>
			</tr>
			<tr id="iframeShow">
				<td class='left_col AvidmW150'>�ļ���</td>
				<td>
					<div>
		   				 <iframe id="viewWindow" name="viewWindow" scrolling="no" frameborder="0" src="<%=request.getContextPath() %>/plm/docman/FileOperate.jsp?oid=<%=oid%>"   style="width: 100%; height:80;"> </iframe>
		    		</div>
		   	    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span></span>��ţ�</td>
			    <td >
					<%=ADMasterObj.getNumber()%>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span></span>���ƣ�</td>
			    <td>
			    	<%=ADMasterObj.getName()%>
			    </td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span>*</span>��Դ��</td>
				<td> 
					<input type='text' name='DATASOURCE' id='DATASOURCE' class='pt-text pt-validatebox' style="width:270px" value='<%=adObj.getDataSource()==null ? "" : adObj.getDataSource()%>' <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>ҳ����</td>
				<td> 
					<input type='text' name='PAGES' id='PAGES' class='pt-text pt-validatebox' style="width:270px" value='<%=adObj.getPages()%>' <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>������</td>
				<td> 
					<input type='text' name='COUNT' id='COUNT' class='pt-text pt-validatebox' style="width:270px" value='<%=adObj.getCount()%>' <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>�����ߣ�</td>
				<td> 
					<input type='text' name='AUTHORNAME' id='AUTHORNAME' class='pt-text pt-validatebox' style="width:270px" value='<%=adObj.getAuthorName()==null ? "" : adObj.getAuthorName()%>' <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>���Ƶ�λ��</td>
				<td> 
					<input type='text' name='AUTHORUNIT' id='AUTHORUNIT' class='pt-text pt-validatebox' style="width:270px" value='<%=adObj.getAuthorUnit()==null ? "" : adObj.getAuthorUnit()%>'  <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>�������ڣ�</td>
				<td> 
				<input type='text' id="AUTHORTIME" name="AUTHORTIME" value='<%=adObj.getDisAuthorTime()%>'
						class="Wdate pt-textbox" readonly="readonly"  style="width:100px;"
				        onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'1970-01-01',maxDate:'2128-03-10'})"/>			
				</td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span></span>�ܼ���</td>
			    <td>
			    	<%=adObj.getSecLevelName()==null ? "" : adObj.getSecLevelName()%>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span></span>���ţ�</td>
			    <td>
					<SELECT name='ACTIVECODE' id='ACTIVECODE' class='pt-select' style="width: 270px;"
						url="<%=contextPath%>/platform/objectmodel/param/page/SelectLookupTableItem.jsp?lookupTableId=activeCode">
					</SELECT>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span></span>�汾��</td>
			    <td>
			    	<%=adObj.getVersionName()==null ? "" : adObj.getVersionName()%>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span></span>λ�ã�</td>
			    <td>
			    	<%=adObj.getFolderPath()==null ? "" : adObj.getFolderPath()%>
			    </td>
			</tr>
			<tr>
			 	<td class="left_col AvidmW150">��ע��</td>
				<td>
					<TEXTAREA id='NOTE' name='NOTE' class='pt-textarea' style='width:270px;height:60px;' <%=ValidateHelper.buildValidatorAndAssisant()%>><%=adObj.getNote()==null ? "" : adObj.getNote()%></TEXTAREA>
				</td>
			</tr>
			<tr id="includeDynamicFields"></tr>
		</table>
	
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
			    <td class="exegesis AvidmW150"><span>*</span> Ϊ��ѡ/������</td>
			    <td>
			    	<div class="pt-formbutton" text="ȷ��"  onclick="submitForm();" <%=ValidateHelper.buildCheckAll()%>></div>
			    	<div class="pt-formbutton" text="ȡ��"  onclick="cancle();"></div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
<script type="text/javascript">
var extJson = <%=extAttrJson%>;
$(document).ready(function() {
	setTimeout("initPage()",300);
	plm.getExtAttr({trid:'includeDynamicFields',colnum:1,classid:'<%=classId%>',viewId:'',oid:'<%=oid%>',callback:setExtData});
});

function initPage() {
	pt.ui.get("ACTIVECODE").setValue("<%=adObj.getActiveCode()%>");
}
function setExtData(){
	var myform = pt.ui.get("myform");
	myform.setData(extJson);
}
// �ύ��
function submitForm(){
	<%=ValidateHelper.buildCheck()%>;
	plm.startWait();
	var innerId = "<%=innerId%>";
	var classId = "<%=classId%>";
	$.ajax({
		   type: "post",
		   url: "<%=contextPath%>/adm/active/ActiveDocumentHandle!updataActiveDocument.action",
		   dataType:"json",
		   data: $("#myform").serializeArray(),
		   success: function(result){
			    plm.endWait();
				if(result.SUCCESS != null && result.SUCCESS == "false"){
					plm.showAjaxError(result);
				}else{
					//plm.showMessage({title:'��ʾ', message:"�༭�����ɹ�!", icon:'2'});
					
					var iframes = window.frames["viewWindow"];
	        		//������ļ����ϴ��ļ�
	        		if(iframes.isHasFile()){
	        			iframes.submitFile(innerId, classId);
	        		}else{	
	        			callBack();
	        		}
				}
		   },
		   error:function(){
			   plm.endWait();
			   plm.showMessage({title:'������ʾ', message:"�༭����ʧ��!", icon:'1'});
		   }
		});
}
	
// ȡ��
function cancle(){
	window.close();
}

function callBack(){
	top.opener.location.reload();
	top.close();
}
</script>
