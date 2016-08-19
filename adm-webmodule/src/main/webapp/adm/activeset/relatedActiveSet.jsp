<%@page contentType = "text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.plm.core.persist.PersistHelper"%>
<%@page import="com.bjsasc.plm.util.JsonUtil"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page language = "java"%>
<%@page import="java.util.*"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.ui.UIDataInfo"%>	
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.type.TypeManager"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.adm.common.ConstUtil"%>
<%
	// 必要参数
	String path = request.getContextPath();
	String data = request.getParameter(KeyS.DATA);
    String oid = (String)session.getAttribute("getOneOid");
	String classid = Helper.getClassId(oid);
	String contextPath = request.getContextPath();
	String addUrl= contextPath+"/adm/active/ActiveSetHandle!addActiveSetObject.action?OID="+oid;
	String loadUrl = contextPath+"/adm/active/ActiveSetHandle!getActiveSetObject.action?OID="+oid;
	//String data = request.getParameter(KeyS.DATA);
	//List<Map<String,Object>> listMap = JsonUtil.toList(data); 
	//String classId = listMap.get(0).get(KeyS.CLASSID).toString();
	//String innerId = listMap.get(0).get(KeyS.INNERID).toString();
%>

<html>
<head>  
<title>相关对象</title>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" scroll="no">
<table height="100%" cellSpacing="0" cellPadding="0" width="100%" border="0">
	<tbody>
			<tr class="AvidmToolbar">
				<td align=left width="100%">
					<%
						UIDataInfo maps = new UIDataInfo();
						maps.put(UIHelper.MENU_ACTION_RECORDS, ConstUtil.GRID_LISTACTIVEDOCUMENT+".getSelections()");
						maps.put(UIHelper.MENU_ACTION_CONTAINER, "container");
						maps.put(UIHelper.MENU_ITEM_WIDTH, "30");
						maps.put(UIHelper.MENU_TOOLBAR_IS_SHOW_TEXT, "false");
						maps.put(UIHelper.BUILD_TOOLBAR, "false");
					%> 
					<div class='pt-toolbar'><%=UIHelper.getToolBar("relatedactiveset.toolbar", maps)%>
					</div>
				</td>
			</tr>
			<tr>
				<td valign="top">
      			<table class="pt-grid" id="<%=ConstUtil.GRID_LISTACTIVEDOCUMENT%>" singleSelect="false" fit="true" autoLoad="true" checkbox="true" useFilter="false" url="<%=loadUrl%>" pagination="false">
					<thead>
						<th field="NUMBER" width="150">编号</th>
						<th field="NAME" width="150">名称</th>
						<th field="NOTE" width="150">备注</th>
						<th field="CONTEXT" width="150">上下文</th>
					</thead>
				</table>
      		</td>
    	</tr>
	</tbody>
</table>
</body>
<script type="text/javascript">
	var container = {OID:"<%=oid%>"};
	function gatherAffectobjects(objArray) {

		if(objArray.length == 0){
			plm.showMessage({title:'提示', message:'所选取的数据为空！', icon:'3'});
 			return;
		}

		var confirmMsg=[];
		var addData="";
		for(var i = 0; i < objArray.length; i++) {
		 	var objId = objArray[i]["CLASSID"] + ":" + objArray[i]["INNERID"];
			//是否是现行套数据源
			var message = plm.checkisActiveSeted(objId);
			if(message != ""){
				var tip = "所选【"+objArray[i]["RAW"]["NAME"]+"】"+message;
				confirmMsg.push(tip);
				continue;
			}
			addData+= objId + ",";
		}

		if(confirmMsg.length > 0){
			var msg = "";
			msg = confirmMsg.join("\n");
			msg += "\n";
			if(!plm.confirm(msg)){
				return;
			}
		}
		addData = addData.substring(0, addData.length - 1);
		plm.startWait();
		$.ajax({
			url:"<%=addUrl%>",
			type:"post",
			dataType:"json",
			data:"<%=KeyS.OIDS%>="+addData,
			success:function(result){
				plm.endWait();
				if (result.SUCCESS != null && result.SUCCESS =="false"){
					plm.showAjaxError(result);
				} else {
					reload();
				}

			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"添加现行套数据操作失败！", icon:'1'});
			}
		});
	}
	function reload(){
		var table = pt.ui.get("<%=ConstUtil.GRID_LISTACTIVEDOCUMENT%>");
		table.reload();
	}
</script>
</html>