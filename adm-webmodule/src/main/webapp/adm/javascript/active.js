//�����ļ�����
//����
Plm.prototype.createActiveDocument = function(records, callback, container) {
	var param = "FOLDER_OID=";
	if (container.OID!=null&&container.OID!="undefined") {
		param += container.OID;
	}
	var url = contextPath + '/adm/activedocument/createActiveDocument.jsp?' + param;

	var windowid = "createActiveDocument" + "_" + new Date().getTime();
	// �򿪴���
	plm.openWindow(contextPath + "/plm/load.html", 900, 600, windowid);
	// ��ǰ̨��ִ��
	plm.openByPost(url, pt.ui.JSON.encode(records), windowid);
};
// �������ݹ��� -- ɾ��
Plm.prototype.deleteActiveData = function(records, callback, container) {
	var oids = "";
	var confirmMsg=[];
	for (var i = 0; i < records.length; i++) {
		//check�����Ƿ�����ڵ�����
		var message = plm.existActiveOrder(records[i].OID);
		if (message != "") {
			var tip = "��ѡ��"+records[i]["TITLE"]+"��"+message;
			confirmMsg.push(tip);
		}
		oids += records[i].OID + ",";
	}

	if (confirmMsg.length > 0) {
		var msg = confirmMsg.join("\n") + "\n";
		if (!plm.confirm(msg)) {
			return;
		}
	} else {
		var msg = "��ȷ��Ҫɾ��������\n";
		if (!plm.confirm(msg)) {
			return;
		}
	}
	plm.startWait();
	if (oids.length>0) {
		oids=oids.substring(0, oids.length-1);
	}
	admValidation(oids, "plm_delete", "deleteActiveDataCallBack");
};

function deleteActiveDataCallBack(oids){
	var url = contextPath + '/adm/active/ActiveRecycleHandle!addRecycle.action?OIDS=' + oids;
	$.ajax({
		url:url,
		type:"post",
		dataType:"json",
		data:"",
		success:function(result){
			plm.endWait();
			if (result.SUCCESS != null && result.SUCCESS == "false") {
				// plm.showAjaxError(result);
				plm.showMessage({ title : '������ʾ', message : result.MESSAGE, icon : '3'});
			} else {
				tipsuccess();
			}
		},
		error:function(){
			plm.endWait();
			plm.showMessage({ title : '������ʾ', message : "ɾ������ʧ�ܣ�", icon : '1'});
		}
	}); 	
}

// �༭
Plm.prototype.editActiveDocument = function(records, callback, container) {
	if(records.length>1){
		plm.showMessage({ title : '������ʾ', message : "��ѡ��һ�����ݣ�", icon : '3'});
		return;
	}
	plm.startWait();
	var oid = records[0].OID;
	
	admValidation(oid,"plm_modify","editActiveDocumentCallBack");
	
};

function editActiveDocumentCallBack(oid){
	var url = contextPath + '/adm/activedocument/editActiveDocument.jsp?OID=' + oid;
	plm.endWait();
	// �򿪴���
	plm.openWindow(url, 900, 600, "editActiveDocument");
}

//�����༭
var editRecords = {};
Plm.prototype.editMultiActiveDocument = function(records, callback, container) {
	plm.startWait();
	var oids = "";
	for ( var i = 0; i < records.length; i++) {
		oids += records[i].OID + ",";
	}
	if(oids.length>0){
		oids=oids.substring(0,oids.length-1);
	}
	editRecords = records;
	admValidation(oids,"plm_modify","editMultiActiveDocumentCallBack");
};

function editMultiActiveDocumentCallBack(oids){
	//distributeCommonSearch
	var url = contextPath + '/adm/active/ActiveDocumentHandle!getParam.action?OIDS=' + oids+"&spot=ACTIVEDOCUMENT";
	var windowid = "editMulti";
	plm.endWait();
	// �򿪴���
	plm.openWindow(contextPath + "/plm/load.html", 900, 600, windowid);
	// ��ǰ̨��ִ��
	plm.openByPost(url, pt.ui.JSON.encode(editRecords), windowid);
}

//ͬ��ģ��
Plm.prototype.admReloadModel = function(records, callback, container){
	if (!plm.confirm("ȷ��ͬ��ģ����?")) {
		return;
	}
	var oid =  records[0]["OID"];
	var url = contextPath + "/adm/active/ActiveModelHandle!getActiveDocumentFolder.action";
	plm.startWait();
	$.ajax({
		url:url,
		type:"post",
		dataType:"json",
		data:"OID="+oid,
		success:function(result){
			if(result.SUCCESS == "true"){
				plm.endWait();
				window.location.href = contextPath + "/plm/common/visit.jsp?OID="+oid;
			}else{
				plm.endWait();
				plm.showMessage({title:'��ʾ', message:"ͬ��ģ��ʧ��!", icon:'3'});
				return;	
			}

		},
		error:function(){
			plm.endWait();
			plm.showMessage({title:'������ʾ', message:"����ʧ�ܣ�", icon:'1'});
		}
	});
};
if (!window.adm) {
	adm = function() {
	};
};

adm.tools = function() {
};

//�򿪴���
adm.tools.showModalDialog = function(url) {
	var strFeatures = "dialogWidth=300px;dialogHeight=400px;center=1;middle=yes;help=no;status=no;scroll=yes;resizable=no;";
	var retObj = window.showModalDialog(url,window,strFeatures);
	return retObj;
};

//��ҳ��ˢ��
Plm.prototype.pageReload=function (){
	try{
		var tempCommonSearch = contextPath +"/ddm/distribute";
		var temprelation = contextPath +"/ddm/distributeorder/relatedDistributeOrders.jsp";
		var tempfolder = contextPath +"/plm/folder/folderlist.jsp";
		var t1=top.opener.location.href;
		if(t1.indexOf(temprelation) > 0){
			top.opener.parent.location.href = top.opener.parent.location.href;
			top.opener.parent.location.reload();
		}else if(t1.indexOf(tempCommonSearch) > 0){
			top.opener.reloadOrder();
		}else if(t1.indexOf(tempfolder) > 0){
			top.opener.reload();
		}else {
			top.opener.location.reload();
		}
	}catch(e){
	}
	top.close();
};

//��������(ActiveSet)
Plm.prototype.searchObject = function (records, callback, container){
	var oid = container.OID;
	plm.startWait();
	admValidation(oid,"plm_modify","searchObjectCallBack");
	
};

function searchObjectCallBack(oid){
	var params="CALLBACK=gatherAffectobjects&single=false&types=ActiveDocument";
	var url = commonSearchPath+"?"+params;
	plm.endWait();
	window.showModalDialog(url,self,"dialogHeight: 480px; dialogWidth: 800px;  center: Yes; help: Yes; resizable: Yes; status: no;");
};

//�Ƴ�����(ActiveSet)
var ASObjectOids = "";
Plm.prototype.removeObject = function (records, callback, container){
	var ASOid="";
	if (container.OID!=null&&container.OID!="undefined") {
		ASOid += container.OID;
	}
	//var ASObjectOids = "";
	for ( var i = 0; i < records.length; i++) {
		ASObjectOids += records[i].OID + ",";
	}
	
	if(ASObjectOids.length>0){
		ASObjectOids = ASObjectOids.substring(0, ASObjectOids.length - 1);
	}
	plm.startWait();
	admValidation(ASOid,"plm_modify","removeObjectCallBack");
};

function removeObjectCallBack(ASOid){
	var url = contextPath + '/adm/active/ActiveSetHandle!deleteActiveSetObject.action?OIDS=' + ASObjectOids+"&OID="+ASOid;
	var param = {
		type : "POST",
		url : url,
		dataType : "json",
		success : function(result) {
			plm.endWait();
			if (result.SUCCESS != null && result.SUCCESS == "false") {
				plm.showAjaxError(result);
			} else {
				window.parent.reload();
			}
		},
		error : function() {
			plm.endWait();
			plm.showMessage({ title : '������ʾ', message : "ɾ������ʧ�ܣ�", icon : '1'});
		}
	};
	UI.ajax(param);
}

//check�Ƿ����ֱ��ɾ������(ActiveOrder)
Plm.prototype.existActiveOrder = function (objectOid){
	var url = contextPath + '/adm/active/ActiveOrderHandle!hasActiveOrderLink.action?OID='+objectOid;
	var returnValue = "";
	$.ajax({
        type: "POST",
        async: false,//�Ƿ��첽
        url : url,
        dataType : "json",
        success: function(result) {
        	returnValue = result.MESSAGE;
		}
	});
	return returnValue;
};

//check�Ƿ�������������Դ
Plm.prototype.checkisActiveSeted = function (objectOid){
	var url = contextPath + '/adm/active/ActiveSetHandle!isActiveSetedObject.action?OID='+objectOid;
	var returnValue = "";
	$.ajax({
      type: "POST",
      async: false,//�Ƿ��첽
      url : url,
      dataType : "json",
      success: function(result) {
      	returnValue = result.MESSAGE;
		}
	});
	return returnValue;
};
//---------------------------------------------------------------------------------
//���е��ݲ���
//����
Plm.prototype.createActiveOrder = function(records, callback, container) {
	var param = "FOLDER_OID=";

	if (container != null && container.OID!=null&&container.OID!="undefined") {
		param += container.OID;
	}
	var resultTemp=[];
	var confirmMsg=[];
	var oids="";
	for ( var i = 0; i < records.length; i++) {
		//�Ƿ������е�������Դ
		var message = plm.checkisActiveOrdered(records[i].OID);
		if(message != ""){
			var tip = "��ѡ��"+records[i]["TITLE"]+"��"+message;
			confirmMsg.push(tip);
			continue;
		}
		resultTemp.push(records[i]);
		oids += records[i].OID + ",";
	}

	if(confirmMsg.length > 0){
		var msg = confirmMsg.join("\n") + "\n";
		if(!plm.confirm(msg)){
			return;
		}
	}
	if(oids.length>0){
		oids = oids.substring(0, oids.length - 1);
	}
	var url = contextPath + '/adm/activeorder/createActiveOrderTab.jsp?' + param +"&addAfterOids="+oids;

	var windowid = "createActiveOrder" + "_" + new Date().getTime();
	// �򿪴���
	plm.openWindow(contextPath + "/plm/load.html", 900, 600, windowid);
	// ��ǰ̨��ִ��
	plm.openByPost(url, pt.ui.JSON.encode(resultTemp), windowid);
};

//check�Ƿ������е�������Դ
Plm.prototype.checkisActiveOrdered = function (objectOid){
	var url = contextPath + '/adm/active/ActiveOrderHandle!isActiveOrderedObject.action?OID='+objectOid;
	var returnValue = "";
	$.ajax({
    type: "POST",
    async: false,//�Ƿ��첽
    url : url,
    dataType : "json",
    success: function(result) {
    	returnValue = result.MESSAGE;
		}
	});
	return returnValue;
};
var orderCallBack = "";
//��������(ActiveOrder)
Plm.prototype.searchObjectActiveOrder = function (records, callback, container){
	plm.startWait();
	orderCallBack = callback;
	admValidation(activeOrderOid,"plm_modify","searchObjectActiveOrderCallBack");
	
};

function searchObjectActiveOrderCallBack(oid){
	//var params="single=false&types=ActiveDocument,ActiveSet&CALLBACK="+callback;
	var params="single=false&types=ActiveDocument&CALLBACK="+orderCallBack;
	var url = commonSearchPath+"?"+params;
	plm.endWait();
	window.showModalDialog(url,self,"dialogHeight: 480px; dialogWidth: 800px;  center: Yes; help: Yes; resizable: Yes; status: no;");
}

//�����汾����(ActiveOrder)
var searchOid,callback2;
Plm.prototype.searchIteratedList = function (records, callback, container){
	plm.startWait();
	var data_Grid=pt.ui.get("data_TreeGrid_before");
	var records=data_Grid.getSelections();
	if(records.length==0){
		plm.endWait();
		plm.showMessage({ title : '������ʾ', message : "��ѡ��һ������ǰ������в�����", icon : '3'});
		return;
	}
	if(records.length>1){
		plm.endWait();
		plm.showMessage({ title : '������ʾ', message : "ֻ�ܶԵ�������ǰ���������", icon : '3'});
		return;
	}
	var message = plm.checkLastestVersion(records[0].OID);
	if(message != ""){
		plm.endWait();
		var callback3 = "addAfterDatas";
		var oid = records[0]["OID"];
		var url = contextPath+"/plm/common/versionControl/Revise.jsp?OID=" + oid + "&CALLBACK=addAfterDatas";
		plm.openWindow(contextPath+'/plm/load.html', 800, 400,'createRevise');
		plm.openByPost(url, pt.ui.JSON.encode(records), 'createRevise');
		return;
	}
	searchOid = records[0].OID;
	callback2 = callback;
	admValidation(activeOrderOid,"plm_modify","searchIteratedListCallBack");
	
};

function searchIteratedListCallBack(oid){
	var url = contextPath+"/adm/activeorder/searchIteratedList.jsp?OID="+searchOid+"&CALLBACK=addAfterDatas";
	plm.endWait();
	window.showModalDialog(url,self,"dialogHeight: 480px; dialogWidth: 800px;  center: Yes; help: Yes; resizable: Yes; status: no;");
}

Plm.prototype.addDataToGrid= function add_object_data(gridId,dataObject){
	var msg="";
	var msg1="";
	var msg2="";
	var count=0;
	var data_Grid=pt.ui.get(gridId);
	var data_old=data_Grid.getRows();
	for(var i=0;i< dataObject.length;i++){
		if(data_old.length!=0){
			if(!plm.checkDataExist(dataObject[i],data_old)){
				if(!plm.checkDataVersion(dataObject[i],data_old)){
					data_Grid.appendRow(dataObject[i]); 
				}else{
					count+=1;
					msg1 += dataObject[i].SOURCE.NUMBER+"��";
				}
			}else{
				count+=1;
				msg2 += dataObject[i].SOURCE.NUMBER+"��";
			}
		}else
		{
			data_Grid.appendRow(dataObject[i]);
		}
	}

	if(count>0){
		if(msg1.length>0){
			msg+="���Ϊ ["+msg1.substring(0, msg1.length-1)+"] ����һ���汾�Ѵ���;";
		}
		if(msg2.length>0){
			msg+="���Ϊ ["+msg2.substring(0, msg2.length-1)+"] �������Ѵ���,�����ظ���ӣ�";
		}
		plm.showMessage({ title : '������ʾ', message : msg, icon : '3'});
	}
	data_Grid.data.refresh();
};
Plm.prototype.checkDataExist =function (newData,oldData){
	for(var i=0;i< oldData.length;i++){
		if(oldData[i] && newData.OID==oldData[i].OID){
			return true;
		}
	}
	return false;
};
Plm.prototype.checkDataVersion=function (newData,oldData){
	for(var i=0;i< oldData.length;i++){
		if(oldData[i] && newData.MASTER_OID!=undefined && newData.MASTER_OID==oldData[i].MASTER_OID){
			return true;
		}
	}
	return false;
};
//check�Ƿ������°汾
Plm.prototype.checkLastestVersion = function (objectOid){
	var url = contextPath + '/adm/active/ActiveOrderHandle!isLastestVersion.action?OID='+objectOid;
	var returnValue = "";
	$.ajax({
    type: "POST",
    async: false,//�Ƿ��첽
    url : url,
    dataType : "json",
    success: function(result) {
    	returnValue = result.MESSAGE;
		}
	});
	return returnValue;
};
Plm.prototype.getPagesData=function (gridId){
	var data_table = pt.ui.get(gridId);
	var records = data_table.getRows();
	//return pt.ui.JSON.encode(records);
	return  records;
};
//�༭
Plm.prototype.editActiveOrderOperate = function(records, callback, container) {
	if(records.length!=1){
		plm.showMessage({ title : '������ʾ', message : "ֻ�ܶԵ������������", icon : '3'});
		return;
	}
	plm.startWait();
	var oid = records[0].OID;
	admValidation(oid,"plm_modify","editActiveOrderOperateCallBack");
	
};

function editActiveOrderOperateCallBack(oid){
	var url = contextPath + '/adm/activeorder/editActiveOrderTab.jsp?clickTab=tab1&OID=' + oid;
	plm.endWait();
	// �򿪴���
	plm.openWindow(url, 900, 600, "editActiveOrder");
}

//�༭
Plm.prototype.editActiveOrderToolbar = function(records, callback, container) {
	plm.startWait();
	admValidation(activeOrderOid,"plm_modify","editActiveOrderToolbarCallBack");
};

function editActiveOrderToolbarCallBack(oid){
	var url = contextPath + '/adm/activeorder/editActiveOrderTab.jsp?clickTab=tab2';
	plm.endWait();
	// �򿪴���
	plm.openWindow(url, 900, 600, "editActiveOrder");
}

//�༭
Plm.prototype.editActiveOrderAdd = function(records, callback, container) {
	if(records.length!=1){
		plm.showMessage({ title : '������ʾ', message : "ֻ�ܶԵ������������", icon : '3'});
		return;
	}
	var oid = records[0].OID;
	var url = contextPath + '/adm/activeorder/editActiveOrderTab.jsp?clickTab=tab1&OID=' + oid;
	// �򿪴���
	plm.openWindow(url, 900, 600, "editActiveOrder");
};
//���ֱ����ⰴť
Plm.prototype.putIntoStorage = function(records, callback, container){
	if(!plm.checkSelect(records)){
		return;
	}
	var oid =  records[0]["OID"];
	plm.startWait();
	var url = contextPath + '/adm/active/ActiveDocumentHandle!putIntoStorage.action';
	$.ajax({
		url:url,
		type:"post",
		dataType:"json",
		data:"OID="+oid,
		success:function(result){
			plm.endWait();
			if(result.SUCCESS != null &&result.SUCCESS == "false"){
				plm.showAjaxError(result);
				return;
			}else if(result.SUCCESS == "message"){
				var mes=result.MESSAGE;
				plm.showMessage({title:'������ʾ', message:mes, icon:'1'});
				return;
			}else if(result.FLAG == "false"){
				alert("��û�в���Ȩ�ޣ�");
				return;
			}
			window.reload();
		},
		error:function(){
			plm.endWait();
			plm.showMessage({title:'������ʾ', message:"����ʧ�ܣ�", icon:'1'});
		}
	});
	
};
//Ȩ����֤
function admValidation(oids,operate,callback){
	//plm.startWait();
	var operateUrl = contextPath + "/adm/active/activeOperateValidationHandle!activeOperateValidation.action";
	$.ajax({
		url:operateUrl,
		type:"post",
		dataType:"json",
		data:"OIDS="+oids+"&optId="+operate,
		success:function(result){
			//plm.endWait();
			if (result.SUCCESS != null && result.SUCCESS =="false"){
				plm.endWait();
				plm.showAjaxError(result);
				return;
			}
			callback = callback + "('"+oids+"')";
			eval(callback);
		},
		error:function(){
			plm.endWait();
			plm.showMessage({title:'������ʾ', message:"��ȡ����Ȩ��ʧ�ܣ�", icon:'1'});
		}
	});    	
}

// ѡ�������ļ����
function selectActiveDocument(inputId, oid, ads) {
	var url =  contextPath + "/adm/activerelated/relatedActiveDocument.jsp?OID="+oid+"&ads="+ads;
	var t = (window.screen.availHeight - 30 - 500) / 2;
	var l = (window.screen.availWidth - 10 - 700) / 2;
	var strFeatures = "dialogWidth=705px;dialogHeight=505px;dialogLeft="+l+"px;dialogTop="+
			t+"px;center=1;middle=yes;help=no;status=no;scroll=yes;resizable=no;";
	var retObj = window.showModalDialog(url,window,strFeatures);
	if (retObj) {
		var record =  pt.ui.JSON.decode(retObj);
		$("#" + inputId).val(record[0]["SOURCE"]["NUMBER"]);
	}
}

// ���������ļ�����-�Ҽ��˵�-��Ϊ����ʾ
Plm.prototype.activeModelAsRootDisplay = function(records, callback, container) {
	var asRootOid =  records[0]["OID"];
	//ˢ�µ�����	ds
	parent.refreshNavigator("NAVIGATE_TO_OBJECT", records[0]);
	window.location.href = contextPath + "/adm/folder/visitFolder.jsp?OID="+asRootOid+"&asRootOid="+asRootOid;
}

// ���������ļ�����-��ݲ˵�-���ظ�Ŀ¼
Plm.prototype.activeModelReturnRootDisplay = function(records, callback, container) {
	var asRootOid =  records[0]["OID"];
	window.frames["view"].location.href = contextPath + "/adm/folder/visitFolder.jsp?rootDisplay=true&OID="+asRootOid;
}

Plm.prototype.saveasActiveObjects = function(records, callback, container){
	if(!plm.checkSelect(records)){
		return;
	}
	var appendParam="";
	if(container!=null){
		appendParam="&CONTAINER_OID="+container.OID;
	}
	var url = contextPath+"/adm/folder/SaveasObjects.jsp?CALLBACK="+callback+appendParam;
	
	plm.openWindow(contextPath+"/plm/load.html", 800, 500,'saveasObjects');
	
	plm.openByPost(url, pt.ui.JSON.encode(records), 'saveasObjects');
};

Plm.prototype.moveActiveObjects = function(records, callback, container){
	if(!plm.checkSelect(records)){
		return;
	}
	var appendParam="";
	if(container!=null){
		appendParam="&CONTAINER_OID="+container.OID;
	}
	var url = contextPath+"/adm/folder/moveObjects.jsp?CALLBACK="+callback+appendParam;
	
	plm.openWindow(contextPath+"/plm/load.html", 800, 400,'moveObjects');
	
	plm.openByPost(url, pt.ui.JSON.encode(records), 'moveObjects');
};

//ѡ���ļ���
Plm.prototype.selectActiveFolder = function(treetype,context,callback){
	var folderiid = "";
	if(context!=null&&context.OID!=null){ 
		folderiid = context.OID;
	}
	var url =contextPath+"/adm/folder/folderPubTree.jsp?treeType="+treetype+"&folderiid="+folderiid+"&CALLBACK="+callback;
	plm.openWindow(url, 400, 500, "selectFolder");	
};

// ��ԭ���ж���
Plm.prototype.reductionActiveObjects = function(records, callback, container){
	if(!plm.checkSelect(records)){
		return;
	}
	var appendParam="";
	if(container!=null && container.OID != undefined){
		appendParam="&CONTAINER_OID="+container.OID;
	}
	
	var operateUrl = contextPath + "/adm/folder/reductionHandle.jsp?op=isFolder";
	$.ajax({
		url:operateUrl,
		type:"post",
		dataType:"json",
		data:"DATA="+pt.ui.JSON.encode(records),
		success:function(result){
			if (result.SUCCESS != null && result.SUCCESS =="false"){
				plm.showAjaxError(result);
				return;
			}
			if (result.SUCCESS =="true" && result.COUNT > 0) {
				var url = contextPath+"/adm/folder/reductionObjects.jsp?CALLBACK="+callback+appendParam;
				plm.openWindow(contextPath+"/plm/load.html", 800, 500,'reductionObjects');
				plm.openByPost(url, pt.ui.JSON.encode(records), 'reductionObjects');
			} else {
				reload();
			}
		},
		error:function(){
			plm.showMessage({title:'������ʾ', message:"��ԭ����ʧ�ܣ�", icon:'1'});
		}
	});
};

Plm.prototype.activeRenameObjects = function(records, callback, container){
	if(!plm.checkSelect(records)){
		return;
	}
	var checkError = false;
	for ( var i = 0; i < records.length; i++) {
		if (records[i].CLASSID.indexOf("Folder") > 0) {
			checkError = true;
			break;
		}
	}
	if (checkError) {
		plm.showMessage({title:'������ʾ', message:"�ļ������Ͳ���ִ��������������", icon:'1'});
		return;
	}
	
	var appendParam="";
	if(container!=null){
		appendParam="&CONTAINER_OID="+container.OID;
	}
	var url = contextPath+"/plm/common/identifier/RenameObjects.jsp?CALLBACK="+callback+appendParam;
	
	plm.openWindow(contextPath+"/plm/load.html", 800, 400,'renameObjects');
	
	plm.openByPost(url, pt.ui.JSON.encode(records), 'renameObjects');
};

/**
 * 
 * ����ָ����������ģ�塣
 * 
 * @param records
 * @param callback
 * @param container
 */
Plm.prototype.activeReasignLifeCycleTemplateSimple = function(records, callback, container){
	
	if(!plm.checkSelect(records)){
		return;
	}

	var checkError = false;
	for ( var i = 0; i < records.length; i++) {
		if (records[i].CLASSID.indexOf("Folder") > 0) {
			checkError = true;
			break;
		}
	}
	if (checkError) {
		plm.showMessage({title:'������ʾ', message:"�ļ������Ͳ���ִ������ָ���������ڲ�����", icon:'1'});
		return;
	}	
	
	var innerId = records[0]["INNERID"];
	var classId = records[0]["CLASSID"];
	
	var parameter = {
		operation : "resetLifecycle",
		objectIId : innerId,
		classId : classId
	};
	addWFParam(parameter, records); // ��ӹ���������
	
	//pt.lifecycle.completeLifeCycleOperation(parameter,callback);
	plm.completeLifeCycleOperation(parameter,callback);
};

Plm.prototype.activeCutToClipboard = function(records, callback, container){
	if(records != null && records.length > 0){
		var checkError = false;
		for ( var i = 0; i < records.length; i++) {
			if (records[i].CLASSID.indexOf("Folder") > 0) {
				checkError = true;
				break;
			}
		}
		if (checkError) {
			plm.showMessage({title:'������ʾ', message:"�ļ������Ͳ���ִ�м��в�����", icon:'1'});
			return;
		}
	}
	plm.clipboard.cutToClipboard(records, callback, container);
};

Plm.prototype.activeCopyToClipboard = function(records, callback, container){
	if(records != null && records.length > 0){
		var checkError = false;
		for ( var i = 0; i < records.length; i++) {
			if (records[i].CLASSID.indexOf("Folder") > 0) {
				checkError = true;
				break;
			}
		}
		if (checkError) {
			plm.showMessage({title:'������ʾ', message:"�ļ������Ͳ���ִ�и��Ʋ�����", icon:'1'});
			return;
		}
	}
	plm.clipboard.copyToClipboard(records, callback, container);
};