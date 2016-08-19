<%@page import="com.bjsasc.plm.core.session.SessionHelper"%>
<%@page import="com.bjsasc.plm.core.option.OptionValue"%>
<%@page import="com.bjsasc.plm.core.option.OptionManager"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="GBK"%>
<%@page session="true"%>
<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.operate.ActionUtil"%>
<%@page import="java.util.*"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.folder.FolderSelector"%>
<%@page import="java.util.Map" %>
<%@page import="com.bjsasc.adm.common.ConstUtil" %>
<%@page import="com.bjsasc.plm.core.folder.FolderHelper"%>
<%@page import="com.bjsasc.plm.core.type.TypeDefinition"%>
<%@page import="com.bjsasc.adm.active.model.activeset.ActiveSet" %>
<%@page import="com.bjsasc.plm.core.folder.Folder"%>
<%@page import="com.bjsasc.plm.core.Helper"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.platform.webframework.tag.util.JsonUtil"%>
<%
	//获取
	String contextPath = request.getContextPath();
	String title = "com.bjsasc.adm.activeset.create";
	//文件夹OID
	String folderOid = request.getParameter(KeyS.FOLDER_OID);
	String data = request.getParameter(KeyS.DATA);
	List list = null;
	if (folderOid == null || folderOid.length() == 0 ) {
		try {
			list = DataUtil.JsonToList(data);
			if (list != null && list.size() > 0) {
				Map map = (Map)list.get(0);
				folderOid = (String)map.get("FOLDER_OID");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}	
	//类型
	String classId = ActiveSet.CLASSID;
	//文件夹PATH
	Folder folder = (Folder) Helper.getPersistService().getObject(folderOid);
	String folderPath= FolderHelper.getService().getFolderPathStr(folder);
	Context context = folder.getContextInfo().getContext();
	//上下文OID
	String contextOid = context.getOid();
	//上下文NAME
	String contextName = context.getName();
	TypeDefinition baseModel =  Helper.getTypeService().getType(classId);

	String addOids =  (String) request.getParameter("addOids");
	/*String data = request.getParameter(KeyS.DATA);
	List<Map<String,Object>> listMap = null;
	if(data != null && !"".equals(data)){
		listMap = JsonUtil.JsonToList(data);
		if(listMap != null && listMap.size() > 0){	
			for(Map<String,Object> map:listMap){
				String oid = (String)map.get(KeyS.OID);
				addOids += oid + ",";
			}
			if(addOids.endsWith(",")){
				addOids = addOids.substring(0,addOids.length() - 1);
			}
		}
	}*/
%>
<html>
<head>
<title><%=ActionUtil.getTitle(title)%></title>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath%>/plm/ui/editor/date/WdatePicker.js"></script>
</head>
<body  class="openWinPage" onload="">
	<jsp:include page="/plm/common/actionTitle.jsp">
		<jsp:param name="ACTIONID" value="<%=title %>"/>
	</jsp:include>
	<form id="form_doc" ui="form" action="" method="POST" name="form_doc">
		<input type="hidden" name="CLASSID" id="CLASSID" value="<%=classId %>"/>
		<input type="hidden" name="FOLDEROID" id="FOLDEROID" value="<%=folderOid%>"/>
		<input type="hidden" name="CONTEXT" id="CONTEXT" value="<%=contextOid%>"/>
		<input type="hidden" name="addOids" id="addOids" value="<%=addOids%>"/>
		<input type="hidden" name="VERSIONFlAG" id="VERSIONFlAG" value="0"/>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
			<tr>
				<td class='left_col AvidmW150'>上下文：</td>
				<td><%=contextName %></td>
			</tr>			
			<tr>
				<td class='left_col AvidmW150'><span>*</span>类型：</td>
				<td>
					<select class="pt-select"  name='CLASSIDS' id='CLASSIDS' style="width:270px;" 
						url="<%=contextPath%>/adm/activedocument/handleActiveDocument.jsp?OPERATE=getClassId&CLASSID=<%=classId%>">
					</select>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span>*</span>编号：</td>
				<td> 
					<input type='text' name='<%=KeyS.NUMBER %>' id='<%=KeyS.NUMBER %>' class='pt-text pt-validatebox' style="width:270px" <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span>*</span>名称：</td>
				<td><input type='text' name='NAME' id='NAME' class='pt-text pt-validatebox' style="width:270px" ></td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span>*</span>来源：</td>
				<td> 
					<input type='text' name='DATASOURCE' id='DATASOURCE' class='pt-text pt-validatebox' style="width:270px" <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>现行文件编号：</td>
				<td> 
					<input type='text' name='ACTIVEDOCUMENTNUMBER' id='ACTIVEDOCUMENTNUMBER' class='pt-text pt-validatebox' style="width:270px" <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>页数：</td>
				<td> 
					<input type='text' name='PAGES' id='PAGES' class='pt-text pt-validatebox' style="width:270px" <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'><span></span>份数：</td>
				<td> 
					<input type='text' name='COUNT' id='COUNT' class='pt-text pt-validatebox' style="width:270px" <%=ValidateHelper.buildValidator()%>>
				</td>
			</tr>
			<tr id="secLevelView" >
				<td class='left_col AvidmW150'>密级：</td>
				<td>
					<SELECT name='SECLEVEL' id='SECLEVEL' class='pt-select' style="width: 270px;"
						url="<%=contextPath%>/adm/activedocument/handleActiveDocument.jsp?OPERATE=getSecLevel&CONTEXTOID=<%=contextOid%>">
					</SELECT>
				</td>
			</tr>
			<tr id="secLevelView" >
				<td class='left_col AvidmW150'>代号：</td>
				<td>
					<SELECT name='ACTIVECODE' id='ACTIVECODE' class='pt-select' style="width: 270px;"
						url="<%=contextPath%>/adm/activedocument/handleActiveDocument.jsp?OPERATE=getActiveCode&CONTEXTOID=<%=contextOid%>">
					</SELECT>
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'>位置：</td>
				<td>
					<input type="hidden" name="folderOid" id="folderOid" value="<%=folderOid%>"/>
		            <input type='text' name='folder' id='folder' class='pt-text'  value='<%=folderPath%>' readonly style="width:270px" >
				</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'>生命周期模板：</td>
				<td>(自动生成)</td>
			</tr>
			<tr>
				<td class='left_col AvidmW150'>备注：</td>
				<td><textarea name='NOTE' id='NOTE'  class='pt-textarea' style="width:270px;height:50px;" ></textarea></td>
			</tr>
			<tr id="includeDynamicFields"></tr>
		</table>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">		
			<tr>
		        <td class="exegesis AvidmW150"><span>*</span> 为必选/必填项</td>	      
     	        <td>
     	          <div class="pt-formbutton"   id="savebtn" text="确定" onclick="submitForm()" ></div>
     	          <div class="pt-formbutton" id="cancelbtn" text="取消"  onclick="cancel()"></div> 
     	        </td>
	         </tr>
		  </table>
	</form>
</body>

<script type="text/javascript">

//提交表单
function submitForm() {
	<%=ValidateHelper.buildCheck()%>;

	plm.startWait();
	$.ajax({
		type: "post",
        url : "<%=contextPath%>/adm/active/ActiveSetHandle!createActiveSet.action",
        dataType:"json",
    	data:$("#form_doc").serializeArray(),
        success: function(result) {
			plm.endWait();
			if (result.SUCCESS != null && result.SUCCESS =="false"){
				plm.showAjaxError(result);
			} else {
        		callBack();
			}
        },
		error:function(){
			plm.endWait();
			plm.showMessage({title:'错误提示', message:"创建现行套操作失败！", icon:'1'});
		}
	});
}

function callBack(){
	top.opener.location.reload();
	top.close();
}

function cancel(){
	window.close();
}
</script>
</html>