<%@page import="java.util.Map"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.type.TypeService"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask"%>
<%@page import="com.bjsasc.plm.core.system.access.operate.Operate"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%@include file="/ddm/public/ddm.jsp" %>

<%
	String path = request.getContextPath();
	String oid = request.getParameter("OID");
	
	DistributePaperTaskService service = DistributeHelper.getDistributePaperTaskService();
	DistributePaperTask dis = service.getDistributePaperTaskProperty(oid);	
	TypeService typeManager = Helper.getTypeManager();
	Map<String, Object> map = typeManager.format(dis,KeyS.VIEW_DETAIL);
	
	String contextPath = request.getContextPath();
	String classId = DistributePaperTask.CLASSID;
	String spot = "ListAddProcessInfos";
	String gridId = "listAddProcessInfo";
	String updateUrl = contextPath + "/ddm/distribute/duplicateProcessHandle!updateProcessList.action";
	String url = contextPath + "/ddm/distribute/duplicateProcessHandle!updateProcessInfoList.action";
	String gridUrl = contextPath + "/ddm/distribute/duplicateProcessHandle!listDuplicateProcessInfo.action?OID="+oid;
	String gridTitle = "";
	String toolbarId = "ddm.duplicateprocessInfo.list.toolbar";
	String disagreeUrl = contextPath + "/ddm/distribute/distributeTaskHandle!updateDistributeTask.action";

%>
<html>
<head>
	<title>录入复制加工信息</title>
	<script type="text/javascript" src="<%=request.getContextPath() %>/platform/common/js/ptutil.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
	<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" scroll="no">
<table cellSpacing="0" cellPadding="0" height="100%" width="100%" border="0" scroll="no">
	<tr><td>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		 <tr>
	    	<td class="AvidmTitleName">
	    		<div class="imgdiv"><img src="<%=request.getContextPath() %>/plm/images/common/modify.gif"/></div>
				<div class="namediv">录入加工信息</div></td>
		 </tr>
	</table>
	</td></tr>
	<tr><td>

    <input type="hidden" value="<%=oid %>" name="<%=KeyS.OID %>" />
    
		<table width="100%" border="0" cellspacing="0" cellpadding="0" scroll="no" class="avidmTable">
			<tr>
			    <td class="left_col AvidmW150" >任务编号：</td>
			    <td>
			    	<%=dis.getNumber()%>
			    </td>
			    <td class="left_col AvidmW150" >上下文：</td>
			    <td>
			    	<%=map.get("DDMCONTEXT")%>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" >创建人：</td>
			    <td>
			    	<%=dis.getCreateByName()%>
			    </td>
			    <td class="left_col AvidmW150" >创建时间：</td>
			    <td>
			    	<%=dis.getCreateTime()%>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" >创建单位：</td>
			    <td>
			    	
			    </td>
			    <td class="left_col AvidmW150" >所属发放单：</td>
			    <td>
			    	<a href ="#" onclick="javascript:selectOrderProperty('<%=dis.getOrderOid() %>')"><%=dis.getOrderName() %></a>	
			    </td>
			</tr>
			<tr>
			<td class='left_col AvidmW150'>状态：</td>
			<td width="35%"><%=map.get("LIFECYCLE_STATE") %></td>
		 	<td class="left_col AvidmW150"></td>
		</tr>
			<tr id="includeDynamicFields"></tr>
		</table>
		</td></tr>
		<tr><td>
		<table height="437" width="100%" cellSpacing="0" cellPadding="0" border="0" scroll="no">
			<tr><td height="1%">
			<table cellSpacing="0" cellPadding="0" width="100%" border="0" scroll="no" style="border-right:#9cbdff solid 1px;border-left:#9cbdff solid 1px;"><tr class="AvidmToolbar"><td> 
				<div class="pt-toolbar">
					<%=UIHelper.getToolBar(toolbarId)%>
				</div> 
			</td></tr></table></td></tr>			
			<tr><td valign="top">
				<table id="<%=gridId%>" class="pt-grid" singleSelect="false" checkbox="true" url="<%=gridUrl%>"
						fit="true" width="100%" pagination="false" rownumbers='true'>
					<thead>
						<th field="NUMBER"  width="200" tips="编号">编号</th>
						<th field="DDMCONTEXT"  width="150" tips="上下文">上下文</th>
						<th field="VERSION"  width="80" tips="版本">版本</th>
						<!-- <th field="DISINFONAME"  width="150" tips="发往单位（份数）">发往单位（份数）</th> -->
					   	<th field="COLLATOR" 	 width="150"  tips="整理人" editor="{type:'text',readOnly:'readOnly',onclick:addContractor}" >整理人</th>
					   	<th field="CONTRACTOR"  width="150" tips="复印人" editor="{type:'text',readOnly:'readOnly',onclick:addCollator}">复印人</th>
					   	<th field="FINISHTIME"  width="150" tips="完成时间">完成时间</th>
					   	<th field="OPENCOUNT"  width="150" tips="回退次数">回退次数</th>
					</thead>
				</table >
			</td></tr></table>
			</td></tr>
			<form id='main_form'>
				<input type="hidden" name="keys" id="keys"/>
			</form>
		</table>
</body>
</html>

<script type="text/javascript">
	
	function selectOrderProperty(value){
		var url = '<%=contextPath%>/ddm/distributeorder/distributeOrder_tab.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + value;
		window.open(url,900,600,"distributeOrderOpen");
	}
	
	function doCustomizeMethod_returnCount(value) {
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		var oid="";
		for(var i=0;i<data.length;i++){
			oid += data[i].OID;
		}
		var url = '<%=contextPath%>/ddm/distribute/distributeObjectHandle!getDistributeObjectReturnDetail.action?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + oid + '&taskOid=<%=oid%>';
		window.open(url,900,600,"distributeOrderOpen");
	}
	
	//添加整理人信息
	function addContractor(){
	var table = pt.ui.get("<%=gridId%>");
	var data = table.getSelections();
	if(data.length == 0){
		plm.endWait();
		plm.showMessage({title:'错误提示', message:"请至少选择一条操作数据", icon:'1'});
		return;
	}
	addUsersUrl = "<%=contextPath%>/plm/role/rolePrincipalsHandle!addUsers.action";
	var configObj = 
		{ 
			IsModal : "true",
			SelectMode : "single",
			returnType : "arrayObj",
			scope : "all"
		};
	plm.endWait();
	var retObj = pt.sa.tools.selectUser(configObj);
	if(retObj){
		doBindContractor(retObj);
  	} 
	}
	
	function doBindContractor(retObj) {
		plm.startWait();
		var arrUserName = retObj.arrUserName;
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		for ( var i = 0; i < data.length; i++) {
			var rindex = table.getRowIndex(data[i]);
			table.setColumnValue("COLLATOR",arrUserName[0],rindex);
		}
		plm.endWait();
	}
	
	// 添加复印人信息
	function addCollator(){
	var table = pt.ui.get("<%=gridId%>");
	var data = table.getSelections();
	if(data.length == 0){
		plm.endWait();
		plm.showMessage({title:'错误提示', message:"请至少选择一条操作数据", icon:'1'});
		return;
	}
	addUsersUrl = "<%=contextPath%>/plm/role/rolePrincipalsHandle!addUsers.action";
	var configObj = 
		{ 
			IsModal : "true",
			SelectMode : "single",
			returnType : "arrayObj",
			scope : "all"
		};
	plm.endWait();
	var retObj = pt.sa.tools.selectUser(configObj);
	if(retObj){
		doBindCollator(retObj);
  	} 
	}
	
	function doBindCollator(retObj) {
		plm.startWait();
		var arrUserName = retObj.arrUserName;
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		for ( var i = 0; i < data.length; i++) {
			var rindex = table.getRowIndex(data[i]);
			table.setColumnValue("CONTRACTOR",arrUserName[0],rindex);
		}
		plm.endWait();
	}
	
	//同意
	function agree(){
  		ddmValidation("<%=oid%>","<%=Operate.PROMOTE%>","agreeCallBack");
  	}
	
  	function agreeCallBack(taskOid){
  		var table = pt.ui.get("<%=gridId%>");
  		var data = table.getSelections();
  		
  		if(data.length == 0){
  			plm.endWait();
			plm.showMessage({title:'错误提示', message:"请至少选择一条操作数据", icon:'1'});
			return;
		}
		
		if (!plm.confirm("请确认整理人，复制人填写正确，提交后将保存并关闭窗口！")) {
			plm.endWait();
			return;
		}
		
		var oids = "";
		var collators = "";
		var contractors = "";
		for(var i=0;i<data.length;i++){
			oids += data[i].OID + ",";
			$("#keys").val(data[i].COLLATOR);
			var collator = $("#keys").val();
			collators += collator + ",";
			
			$("#keys").val(data[i].CONTRACTOR);
			var contractor = $("#keys").val();
			contractors += contractor + ",";
		}
		oids = oids.substring(0, oids.length - 1);
		collators = collators.substring(0, collators.length - 1);
		contractors = contractors.substring(0, contractors.length - 1);
		plm.startWait();
		$.ajax({
			url:"<%=url%>",
			type:"post",
			dataType:"json",
			data:"<%=KeyS.OIDS%>="+oids+"&collator="+collators+"&contractor="+contractors+"&taskOid=<%=oid %>",
			success:function(result){
				plm.endWait();
				if(result.SUCCESS != null &&result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				window.close();
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"添加复制加工者失败！", icon:'1'});
			}
		});
  	}
  	
  	//不同意
  	function disagree(){
  		plm.startWait();
  		ddmValidation("<%=oid%>","<%=Operate.PROMOTE%>","disagreeCallBack");
  	}
  	
  	function disagreeCallBack(taskOid){
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		if (data == null || data.length == 0) {
			plm.endWait();
			plm. showMessage({title:'提示', message:"请至少选择1个操作对象!", icon:'3'});
			return;
		}
		var oids = "";
		for(var i=0;i<data.length;i++){
			oids += data[i].OID + ",";
			$("#keys").val(data[i].FINISHTIME);
			var finishTime = $("#keys").val();
			if(finishTime != ""){
				plm.endWait();
				plm. showMessage({title:'提示', message:"包含已同意的分发数据，请重新选择后提交！", icon:'3'});
				return;
			}
		}
		oids = oids.substring(0, oids.length - 1);
		var url = '<%=contextPath%>/ddm/distributetask/distributeReturnReason.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OIDS%>=' + oids + '&type=disagree&taskOid=<%=oid%>';
		plm.endWait();
	    var retObj = ddm.tools.showModalDialog(url);
		if(retObj){
			doBindRole(retObj, oids);
		} 
	}
	
    function doBindRole(returnReason, oids){
    	plm.startWait();
		$.ajax({
			url:"<%=disagreeUrl%>",
			type:"post",
			dataType:"text",
			data:"<%=KeyS.OIDS%>="+oids+"&returnReason="+returnReason+"&type=disagree&taskOid=<%=oid%>",
			success:function(result){
				plm.endWait();
				window.location.reload(true);
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"提交失败！", icon:'1'});
			}
		});
    }
  	
	//数据刷新方法
	function reload(){
		window.location.reload(true);
		parent.frames['leftFrame'].refreshTree();
	}

	//刷新dategrid
	function tableReload(text){
		plm.showMessage({title:'系统提示', message:text, icon:'2'});
		reload();
	}

</script>
