<%@page import="java.net.URLDecoder"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.bjsasc.plm.workspace.util.WorkspaceUtil"%>
<%@page import="com.bjsasc.plm.core.folder.Folder"%>
<%@page import="com.bjsasc.plm.core.context.model.Contexted"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.plm.core.workspace.Workspace"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.core.util.PlmException"%>
<%@page import="com.bjsasc.plm.core.folder.AbstractFolder"%>
<%@page import="com.bjsasc.plm.core.persist.PersistHelper"%>
<%@page language="java"%>
<%@page session="true"%>
<%@page import="com.bjsasc.plm.collector.CollectorScope"%>
<%@page import="com.bjsasc.plm.collector.CollectorType"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.util.JsonUtil"%>
<%@page import="java.util.*"%>
<%@page import="com.bjsasc.plm.collector.CollectorKeyWords"%>
<%@page import="com.bjsasc.adm.common.FolderTreeUtil"%>
<%@page import="com.bjsasc.plm.ui.UIDataInfo"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.folder.FolderSelector"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@include file="/plm/plm.jsp"%>
<%@page import="com.bjsasc.plm.common.CommonUtil"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%
	String data = request.getParameter(KeyS.DATA);
	data  = URLDecoder.decode(data, "UTF-8");
	String callBack = request.getParameter(KeyS.CALLBACK);//�ص�����
	String custom_table = request.getParameter("custom_table");
	String folderJson=null;
	
	String contextOid = request.getParameter(KeyS.CONTAINER_OID);
	if(contextOid!=null){
		contextOid=CommonUtil.getContextOidByContainer(contextOid);
	}else{
		//�ò��費Ӧ�õ���
		contextOid=FolderTreeUtil.getContextOidByJsonData(data);
		//throw new PlmException("�봫��������Ϣ!");
	}
	//��������Ϣ��ӵ�Data
	data = CommonUtil.addContainerInfoToJSon(data);
	
	String spot = "saveAsCollector";
	String gridId = "grid_files";
	String tbar = "collector.saveas.toolbar";
	CollectorScope scope = CollectorScope.PUBLIC;//������Χ
	CollectorType collectorType = CollectorType.SAVEASCOLLECTOR;//��������:�ƶ������Ϊ����
%>

<html>
<head>
<base target="_self"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>���Ϊ����</title>
<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/platform/fileserver/res/bjsasc-ui.css">
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
</head>
<body class="openWinPage">
<form id="mainForm" name="mainForm" ui="form" method="post">

	<input type="hidden" id="<%=KeyS.DATA %>" name="<%=KeyS.DATA %>" value="<%=URLEncoder.encode(data, "UTF-8")%>">
	<input type="hidden" id="<%=KeyS.CALLBACK %>" name="<%=KeyS.CALLBACK %>" value="<%=callBack%>">
	<input type="hidden" id="<%=KeyS.CONTAINER_OID %>" name="<%=KeyS.CONTAINER_OID %>" value="<%=contextOid%>">
	<input type="hidden" id="custom_table" name="custom_table" value="<%=custom_table%>">
	
	<table width=100% height="100%" cellSpacing="0" cellPadding="0" border="0">
	<tr class="AvidmActionTitle">
		<td>
			<jsp:include page="/plm/common/actionTitle.jsp">
				<jsp:param name="ACTIONID" value="com.bjsasc.plm.saveas"/>
			</jsp:include>
		</td>
	</tr>
	<tr height=50> <!--��ע��1��������50�ߣ� 2��������80�ߣ��������ƣ�ÿ��һ�м�30�� -->
			<td>
			<form id="infoForm" name="infoForm" action="" ui="form" method="post">
				<table width="100%" class="pt-table">
					<tr>
						<td class='pt-label' align="right" width="100"><span>*</span>��λ�ã�</td>
						<td>
							<INPUT type='text' class="pt-text" id="FolderName" name='<%=KeyS.FOLDER%>' style="width: 270px" value="" ontrigger="plm.selectActiveFolder(treeType,context,'onFolderSelected');">
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
	<jsp:include page="/plm/collector/collector_with_view.jsp">
		<jsp:param name="spot" value="<%=spot %>" />
		<jsp:param name="scope" value="<%=scope%>" />
		<jsp:param name="gridId" value="<%=gridId%>" />
		<jsp:param name="collectorType" value="<%=collectorType%>" />
		<jsp:param name="toolbar_modelId" value="<%=tbar%>" />
		<jsp:param name="operate_contextOID" value="<%=contextOid%>" />
		<jsp:param name="operate_initData" value="<%=data%>" />
		<jsp:param name="custom_table" value="<%=custom_table %>" />
		<jsp:param name="callback" value="collectorCallBack" />
		<jsp:param name="params" value="" />
	</jsp:include>
	<TR><TD>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			 <td class="exegesis AvidmW150"><span>*</span> Ϊ��ѡ/������</td>
			 <td>
			   <div class="pt-formbutton" text="ȷ��" id="submitbutton" onclick="submitMe();"></div>
			   <div class="pt-formbutton" text="ȡ��" id="closebutton" onclick="cancelMe()"></div>
			</td>
		</tr>
	</table>
	</TD></TR>
</table>
</form>
</body>
</html>
<script type="text/javascript">
var context = {OID:"<%=contextOid%>"};
var treeType = '<%=FolderSelector.FOLDERTREE_TYPE_WHOLE%>';
$(document).ready(function(){
	//listObjects();
	
});

var grid = pt.ui.get("<%=gridId%>"); 
function collectorCallBack(obj){ 
	var result = pt.ui.JSON.decode(obj); 
	if (null != result && 0 < result.length) { 
		listObjects(result); 
	}
}

function listObjects(objsSel){
	grid = pt.ui.get("<%=gridId%>"); 
	for(var i=0;i<objsSel.length;i++)
	{	
		objsSel[i].PATHID=objsSel[i].<%=KeyS.FOLDER_OID%>;
		objsSel[i].PATHNAME=objsSel[i].<%=KeyS.CONTAINER_NAME%>;
		objsSel[i].NEWNUMBER=plm.filterLink(objsSel[i]["<%=KeyS.NUMBER%>"])+"_";
		objsSel[i].NEWNAME=plm.filterLink(objsSel[i]["<%=KeyS.NAME%>"])+"_"; 
		objsSel[i].NEWFOLDER_NAME=objsSel[i].FOLDERPATH;
		objsSel[i].NEWFOLDER_OID=objsSel[i].FOLDER_CLASSID+":"+objsSel[i].FOLDER_INNERID;		
		//���ظ�������ݽ����ж�
		var oldData = grid.getRows();
		if(!collector.check_data(objsSel[i],oldData)){
			grid.appendRow(objsSel[i]);
		}
 	}
	//���������,���������������ռ�����
 	collector.setAdjustDatas(grid.getRows());
}

function submitMe(){
	var records = grid.findCancelDeletelines();
	var puregrid=[];
	
	for(var x=0;x<records.length;x++){
		var r={};
		r.CLASSID=records[x]["CLASSID"];
		r.INNERID=records[x]["INNERID"];
		r.NEWNUMBER=records[x]["NEWNUMBER"];
		r.NEWNAME=records[x]["NEWNAME"];
		r.NEWFOLDER_OID=records[x]["NEWFOLDER_OID"];
		r.FOLDER_OID=records[x]["<%=KeyS.FOLDER_OID%>"];
		puregrid.push(r);
	}
	//griddata =grid.getChanges("updated","INNERID,CLASSID,NEWNUMBER,NEWNAME");
	griddata = UI.JSON.encode(puregrid);
	//alert(griddata);
	var param ={
			type: "POST",
		url :"<%=request.getContextPath()%>/plm/common/saveas/SaveasHandle.jsp",
		 
		data : "griddata=" + griddata, 
		success : function(text) { 
			afterSaveas(text);
		},
		failure : function(result){
			afterSaveas(result);
 		}	
	};
	plm.startWait();
	if(plm.checkLogin()){
		UI.ajax(param);
	}else{ 
		window.location.reload();
	}
	return;
	
}

function afterSaveas(o){
	plm.endWait();
	if(o.SUCCESS == "false"){
		plm.showAjaxError(o);
		return;
    }
	if(o.result=="checkFail"){
		var tip={title:"��ʾ",message:o.msg};
		plm.showMessage(tip);
	}else{
	<%if(callBack != null&&!"".equals(callBack)){%> 
		try {
			opener.<%=callBack%>(o); // �������ڻص�
		} catch (e) {
			try {
				window.parent.dialogArguments.<%=callBack%>(o); // ģʽ���ڻص�
			} catch(e) {
			}
		}
	<%}%>
	//�رյ�ǰ����
	window.close();
	}
}
function cancelMe(){
	window.close();
}
function selectFolder(){ 
	if(contextId==null)contextId="";
	var context = {
			INNERID:contextId
	};
	plm.selectFolder("<%=FolderSelector.FOLDERTREE_TYPE_WHOLE%>",context,"onFolderSelected");
} 
	
function onFolderSelected(folder){	
	grid = pt.ui.get("<%=gridId%>");
	var newFolderOid = folder.<%=KeyS.OID%>; 	
	var pathName = folder.<%=KeyS.FOLDER%>;

	document.getElementById("FolderName").value = pathName;
	//alert(pathName);
	var records = grid.getRows();
	for ( var i = 0; i < records.length; i++) { 
		grid.setColumnValue("NEWFOLDER_NAME", pathName, i);
		grid.setColumnValue("NEWFOLDER_OID", newFolderOid, i);
	}
	reSetDataToSession();
} 
function selectFolderCallBack(folder){
	grid = pt.ui.get("<%=gridId%>");
	var selectedRecord = grid.getSelected();
	grid.data.beginChange();
	selectedRecord.NEWFOLDER_OID = folder.<%=KeyS.OID%>;
		selectedRecord.NEWFOLDER_NAME = plm.getFolderPath(folder);
		grid.data.endChange();
		reSetDataToSession();
}
//������ͼ������ݵ�session,��֤�������ݲ���ʧ
function reSetDataToSession() {
	collector.reSetData(grid.getRows());
}
//ˢ��ҳ��,��ͼ�ؼ����¼���
function reload(){
	mainForm.submit();
}
</script>
