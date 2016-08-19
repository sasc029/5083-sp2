<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page language="java"%>
<%@page session="true"%>

<%@page import="java.util.*"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.common.CommonUtil"%>
<%@page import="com.bjsasc.adm.common.FolderTreeUtil"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="com.bjsasc.plm.folder.FolderSelector"%>
<%@page import="com.bjsasc.plm.core.folder.Folder"%>
<%@page import="com.bjsasc.plm.core.folder.Foldered"%>
<%@page import="com.bjsasc.plm.core.folder.FolderInfo"%>
<%@page import="com.bjsasc.plm.core.folder.AbstractFolder"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.core.persist.PersistHelper"%>
<%@page import="com.bjsasc.adm.active.model.activerecycle.ActiveRecycle"%>

<%@include file="/plm/plm.jsp"%>
<%
	String griddata = request.getParameter(KeyS.DATA);
	griddata  = URLDecoder.decode(griddata, "UTF-8");
	String callBack = request.getParameter(KeyS.CALLBACK);//回调函数
	String custom_table = request.getParameter("custom_table");
	String folderJson=null;  
	
	String contextOid = request.getParameter(KeyS.CONTAINER_OID);
	if(contextOid!=null){
		contextOid=CommonUtil.getContextOidByContainer(contextOid);
	}else{
		 contextOid=FolderTreeUtil.getContextOidByJsonData(griddata);
	}

	//把容器信息添加到Data
	griddata = CommonUtil.addContainerInfoToJSon(griddata);
	
	String spot = "moveActiveRecycle";
	String gridId = "grid_files";

	//将数据转换为list
	List<Map<String, Object>> dataList = DataUtil.JsonToList(griddata);
	List<ActiveRecycle> recycleObjects = new ArrayList<ActiveRecycle>();
	
	for (int i = 0; i < dataList.size(); i++) {
		Map<String, Object> data = dataList.get(i);
		String rid = (String) data.get("INNERID");
		String rcid = (String) data.get("CLASSID");
		//待还原的对象
		Persistable recycleObject = PersistHelper.getService().getObject(rcid, rid);
		if (recycleObject == null) {
			continue;
		}
		ActiveRecycle activeRecycle = (ActiveRecycle)recycleObject;
		String innerId = activeRecycle.getItemId();
		String classId = activeRecycle.getItemClassId();
		
		//待还原的对象
		Persistable obj = PersistHelper.getService().getObject(classId, innerId);
		FolderInfo folderInfo = ((Foldered)obj).getFolderInfo();
		Folder folder = folderInfo.getFolder();
		if (folder == null) {
			//添加待还原对象
			recycleObjects.add(activeRecycle);
		}
	}	
	// 输出结果
	GridDataUtil.prepareRowObjects(recycleObjects, spot);
%>
  
<html>
<head>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>还原对象</title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/platform/fileserver/res/bjsasc-ui.css">
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
</head>
<body class="openWinPage">
<form id="mainForm" name="mainForm" ui="form" method="post">

	<input type="hidden" id="<%=KeyS.DATA %>" name="<%=KeyS.DATA %>" value="<%=URLEncoder.encode(griddata, "UTF-8")%>">
	<input type="hidden" id="<%=KeyS.CALLBACK %>" name="<%=KeyS.CALLBACK %>" value="<%=callBack%>">
	<input type="hidden" id="<%=KeyS.CONTAINER_OID %>" name="<%=KeyS.CONTAINER_OID %>" value="<%=contextOid%>">
	<input type="hidden" id="custom_table" name="custom_table" value="<%=custom_table%>">
	
	<table width=100% height="100%" cellSpacing="0" cellPadding="0" border="0"> 
		<tr class="AvidmActionTitle">
			<td><jsp:include page="/plm/common/actionTitle.jsp">
					<jsp:param name="ACTIONID" value="com.bjsasc.adm.activerecycle.reduction" />
				</jsp:include></td>
		</tr>
		
		<tr height="50px">
			<!--备注：1行输入域50高， 2行输入域80高，依次类推，每多一行加30高 -->
			<td>
				<form id="infoForm" name="infoForm" action="" ui="form"
					method="post">
					<table width="100%" class="pt-table">
						<tr>
							<td class='pt-label' align="right" width="100">新位置： <font
								size=2 color=#FF0000>*</font></td>
							<td>
								<INPUT type='text' class="pt-text" id="FolderName" name='<%=KeyS.FOLDER%>' style="width: 270px" value="" ontrigger="plm.selectActiveFolder(treeType,context,'onFolderSelected');">
							</td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
		<TR><td height="100%">
			<table height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0">
			<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
				<jsp:param name="spot" value="<%=spot%>"/>
				<jsp:param name="gridId" value="<%=gridId%>"/>
				<jsp:param name="operate_container" value="container"/>
			</jsp:include>
			</table>
		</td></tr>
		<TR><TD>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
			 <td class="exegesis AvidmW150"><span>*</span> 为必选/必填项</td>
			 <td>
			    <div class="pt-formbutton" text="确定"	id="submitbutton" onclick="submitMe();"></div>
				<div class="pt-formbutton" text="取消"	id="closebutton" onclick="cancelMe()"></div>
			</td>
		</tr>
	</table>
		</TD>
		</tr> 
	</table>
</form>	
</body>
</html>
<script type="text/javascript">
	var context = {OID:"<%=contextOid%>"};
	var treeType = '<%=FolderSelector.FOLDERTREE_TYPE_WHOLE%>';
	var grid = pt.ui.get("<%=gridId%>");

	function submitMe(){
		try {
		var griddata = UI.JSON.encode(grid.getRows());
		var param ={
			type: "POST",
			url :"<%=request.getContextPath()%>/adm/folder/reductionHandle.jsp",
			data : "griddata=" + griddata,
			success : function(result) { 
				afterMove(result);
			},
	 		failure : function(result){
	 			afterMove(result);
	 		}			
		};
		plm.startWait();
		if(plm.checkLogin()){
			UI.ajax(param);
		}else{ 
			window.location.reload();
		}
		return;
		} catch (e) {
			plm.showMessage({title:'错误提示', message:"请选择还原文件夹", icon:'1'});
		}
	}
	function afterMove(o) {
		plm.endWait();
		if(o.SUCCESS == "false"){
    		plm.showAjaxError(o);
    		return;
	    }
		//alert(o);
		if (o.result == "checkFail") {
			var tip = {
				title : "提示",
				message : o.msg
			};
			pt.ui.alert(tip);
		} else {
			<%if (callBack != null) {%>
			var srcFolder=<%=folderJson%>;
		try {
				opener.<%=callBack%>(srcFolder); // 弹出窗口回调
			} catch (e) {
				try {
					window.parent.dialogArguments.<%=callBack%>(srcFolder); // 模式窗口回调
				} catch (e) {
				}
			}
			<%}%>

	//关闭当前窗口
			window.close();
		}
	}

	function cancelMe() {
		window.close();
	}
	 

	function onFolderSelected(folder) {
		grid = pt.ui.get("<%=gridId%>");
		var newFolderOid = folder.<%=KeyS.OID%>; 	
		var pathName = folder.<%=KeyS.FOLDER%>;

		document.getElementById("FolderName").value = pathName;
		//alert(pathName);
		var records = grid.getSelections(); 
		if(records.length==0){
			records=grid.getRows();
		} 
		for ( var i = 0; i < records.length; i++) { 
			grid.setColumnValue("NEWFOLDER_NAME", pathName, i);
			grid.setColumnValue("NEWFOLDER_OID", newFolderOid, i);
		}
	} 
	function selectFolderCallBack(folder){
		grid = pt.ui.get("<%=gridId%>");
		var selectedRecord = grid.getSelected();
		grid.data.beginChange();
		selectedRecord.NEWFOLDER_OID = folder.<%=KeyS.OID%>;
  		selectedRecord.NEWFOLDER_NAME = plm.getFolderPath(folder);
  		grid.data.endChange();
  	}

	//刷新页面,视图控件重新加载
	function reload(){
		mainForm.submit();
	}
</script>
