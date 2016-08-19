<%@page import="java.net.URLDecoder"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.bjsasc.plm.core.folder.Folder"%>
<%@page import="com.bjsasc.plm.core.workspace.Workspace"%>
<%@page import="com.bjsasc.plm.workspace.util.WorkspaceUtil"%>
<%@page import="com.bjsasc.plm.type.TypeManager"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
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
<%@page import="com.bjsasc.plm.ui.UIDataInfo"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%> 
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.folder.FolderSelector"%>
<%@page import="com.bjsasc.adm.common.FolderTreeUtil"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@include file="/plm/plm.jsp"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.plm.common.CommonUtil"%>
<%@page import="com.bjsasc.plm.core.util.PlmException"%>
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
		 contextOid=FolderTreeUtil.getContextOidByJsonData(data);
	}

	//��������Ϣ��ӵ�Data
	data = CommonUtil.addContainerInfoToJSon(data);
	
	String spot = "moveCollector";
	String gridId = "grid_files";
	String tbar = "collector.move.toolbar";
	CollectorScope scope = CollectorScope.PUBLIC;//������Χ
	CollectorType collectorType = CollectorType.MOVECOLLECTOR;//��������:�ƶ������Ϊ����
%>
  
<html>
<head>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>�ƶ�����</title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/platform/fileserver/res/bjsasc-ui.css">
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
			<td><jsp:include page="/plm/common/actionTitle.jsp">
					<jsp:param name="ACTIONID" value="com.bjsasc.plm.move" />
				</jsp:include></td>
		</tr>
		
		<tr height="50px">
			<!--��ע��1��������50�ߣ� 2��������80�ߣ��������ƣ�ÿ��һ�м�30�� -->
			<td>
				<form id="infoForm" name="infoForm" action="" ui="form"
					method="post">
					<table width="100%" class="pt-table">
						<tr>
							<td class='pt-label' align="right" width="100">��λ�ã� <font
								size=2 color=#FF0000>*</font></td>
							<td>
								<INPUT type='text' class="pt-text" id="FolderName" name='<%=KeyS.FOLDER%>' style="width: 270px" value="" ontrigger="plm.selectActiveFolder(treeType,context,'onFolderSelected');">
							</td>
						</tr>
						<TR><TD colspan="2">��һ���ƶ���������а汾��С�汾��</TD></TR>
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
			    <div class="pt-formbutton" text="ȷ��"	id="submitbutton" onclick="submitMe();"></div>
				<div class="pt-formbutton" text="ȡ��"	id="closebutton" onclick="cancelMe()"></div>
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
$(document).ready(function(){
	//listObjects();
	
});
//�����ĵ�ID����һ���ƶ������У�ֻ�ܴ�һ���ļ�����������ƶ������context��ͬһ����
//��context�����ٴδ��ļ���ѡ��ؼ�ʱ��λ�ļ���
//��ǰһҳ���ȡѡ�еĴ��ƶ����󣬲��ڱ�ҳ����ʾ

function collectorCallBack(obj){ 
	var result = pt.ui.JSON.decode(obj);
	
	if (null != result && 0 < result.length) { 
		listObjects(result); 
	}
}
/**
 * �б�ʱ���˵���ͬmasterid�Ķ���,ͬʱ��¼��classId��innerid�Ա������ύʱ���Ӹ���
 */
 var repeatedObjs=[];
 var recordCount=0;
function listObjects(objsSel){ 
	grid = pt.ui.get("<%=gridId%>");
	var masterIds=new Array();
	var recordCount=0;
	for(var i=0;i<objsSel.length;i++)
	{	 
		
		var canAdd=true; 
		/*if(objsSel[i].MASTERREF_INNERID!=null){
			var curMasterId=objsSel[i].MASTERREF_INNERID;
			//����Ƿ��Ѿ�������ͬ��Master
			for(var j=0;j<masterIds.length;j++){
				if(curMasterId==masterIds[j]){
					canAdd=false;
					//���Ӽ�¼
					repeatedObjs.push(objsSel[i]);
					break;
				};
			}
			//���û�й��˵�������MasterId
			if(canAdd){
				masterIds.push(curMasterId);
			};
		}
		*/
		if(canAdd){
			objsSel[i].NEWFOLDER_NAME=objsSel[i].FOLDERPATH;
			objsSel[i].NEWFOLDER_OID=objsSel[i].FOLDER_CLASSID+":"+objsSel[i].FOLDER_INNERID;
		
			//���ظ�������ݽ����ж�
			var oldData = grid.getRows();
			if(!collector.check_data(objsSel[i],oldData)){
				grid.appendRow(objsSel[i]);
				recordCount++;
			}
		};
 	};
 	//���������,���������������ռ�����
 	collector.setAdjustDatas(grid.getRows());
}

function submitMe(){
	var records = grid.findCancelDeletelines();
	var puregrid=[];
	//ѭ����ӹ��˵��Ķ���
	//alert("records.length:"+records.length);
	for(var x=0;x<records.length;x++){
		if(records[x]["NEWFOLDER_OID"]==undefined){
			plm.alertMsg("��ѡ���µ�λ�ã�");
			return;
		}
		var r={};
		r.CLASSID=records[x]["CLASSID"];
		r.INNERID=records[x]["INNERID"];
		r.NEWFOLDER_OID=records[x]["NEWFOLDER_OID"];
		r.FOLDER_OID=records[x]["<%=KeyS.FOLDER_OID%>"];
		
		puregrid.push(r);
	
		
		//���Ϊ�а汾���󣬸��� masterId���Ҳ���ӹ��˵��ķ�֧,������ӹ��˵ķ�֧
		var curMasterId=records[x].MASTERREF_INNERID;
		if(curMasterId!=null){
			for(var j=0;j<repeatedObjs.length;j++){
				//alert("curMasterId:"+curMasterId+" repeatedObjs[j].MASTERREF_INNERID:"+repeatedObjs[j].MASTERREF_INNERID);
				if(curMasterId==repeatedObjs[j].MASTERREF_INNERID){
					//�ҵ�ͬһ��������Ĳ�ͬ��֧�������ӵ�����ύ��������
					var b={};
					b.CLASSID=repeatedObjs[j]["CLASSID"];
					b.INNERID=repeatedObjs[j]["INNERID"];
					b.NEWFOLDER_OID=records[x]["NEWFOLDER_OID"];
					b.<%=KeyS.FOLDER_OID%>=records[x]["<%=KeyS.FOLDER_OID%>"];

					puregrid.push(b);
				};
			};
		};
	}
	//alert("puregrid.length:"+puregrid.length);
	//griddata =grid.getChanges("updated","INNERID,CLASSID,NEWNUMBER,NEWNAME");
	griddata = UI.JSON.encode(puregrid);
	//alert(griddata);
	var param ={
			type: "POST",
			url :"<%=request.getContextPath()%>/plm/common/move/MoveHandle.jsp",
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
				title : "��ʾ",
				message : o.msg
			};
			pt.ui.alert(tip);
		} else {
			<%if (callBack != null) {%>
			var srcFolder=<%=folderJson%>;
		try {
				opener.<%=callBack%>(srcFolder); // �������ڻص�
			} catch (e) {
				try {
					window.parent.dialogArguments.<%=callBack%>(srcFolder); // ģʽ���ڻص�
				} catch (e) {
				}
			}
			<%}%>

	//�رյ�ǰ����
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
