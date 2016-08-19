//现行文件操作
//创建
Plm.prototype.createActiveDocument = function(records, callback, container) {
	var param = "FOLDER_OID=";
	if (container.OID!=null&&container.OID!="undefined") {
		param += container.OID;
	}
	var url = contextPath + '/adm/activedocument/createActiveDocument.jsp?' + param;

	var windowid = "createActiveDocument" + "_" + new Date().getTime();
	// 打开窗口
	plm.openWindow(contextPath + "/plm/load.html", 900, 600, windowid);
	// 在前台中执行
	plm.openByPost(url, pt.ui.JSON.encode(records), windowid);
};
// 现行数据管理 -- 删除
Plm.prototype.deleteActiveData = function(records, callback, container) {
	var oids = "";
	var confirmMsg=[];
	for (var i = 0; i < records.length; i++) {
		//check对象是否存在于单据中
		var message = plm.existActiveOrder(records[i].OID);
		if (message != "") {
			var tip = "所选【"+records[i]["TITLE"]+"】"+message;
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
		var msg = "您确定要删除对象吗？\n";
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
				plm.showMessage({ title : '错误提示', message : result.MESSAGE, icon : '3'});
			} else {
				tipsuccess();
			}
		},
		error:function(){
			plm.endWait();
			plm.showMessage({ title : '错误提示', message : "删除操作失败！", icon : '1'});
		}
	}); 	
}

// 编辑
Plm.prototype.editActiveDocument = function(records, callback, container) {
	if(records.length>1){
		plm.showMessage({ title : '错误提示', message : "请选择一条数据！", icon : '3'});
		return;
	}
	plm.startWait();
	var oid = records[0].OID;
	
	admValidation(oid,"plm_modify","editActiveDocumentCallBack");
	
};

function editActiveDocumentCallBack(oid){
	var url = contextPath + '/adm/activedocument/editActiveDocument.jsp?OID=' + oid;
	plm.endWait();
	// 打开窗口
	plm.openWindow(url, 900, 600, "editActiveDocument");
}

//批量编辑
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
	// 打开窗口
	plm.openWindow(contextPath + "/plm/load.html", 900, 600, windowid);
	// 在前台中执行
	plm.openByPost(url, pt.ui.JSON.encode(editRecords), windowid);
}

//同步模型
Plm.prototype.admReloadModel = function(records, callback, container){
	if (!plm.confirm("确定同步模型吗?")) {
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
				plm.showMessage({title:'提示', message:"同步模型失败!", icon:'3'});
				return;	
			}

		},
		error:function(){
			plm.endWait();
			plm.showMessage({title:'错误提示', message:"操作失败！", icon:'1'});
		}
	});
};
if (!window.adm) {
	adm = function() {
	};
};

adm.tools = function() {
};

//打开窗口
adm.tools.showModalDialog = function(url) {
	var strFeatures = "dialogWidth=300px;dialogHeight=400px;center=1;middle=yes;help=no;status=no;scroll=yes;resizable=no;";
	var retObj = window.showModalDialog(url,window,strFeatures);
	return retObj;
};

//父页面刷新
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

//搜索对象(ActiveSet)
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

//移除对象(ActiveSet)
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
			plm.showMessage({ title : '错误提示', message : "删除操作失败！", icon : '1'});
		}
	};
	UI.ajax(param);
}

//check是否可以直接删除对象(ActiveOrder)
Plm.prototype.existActiveOrder = function (objectOid){
	var url = contextPath + '/adm/active/ActiveOrderHandle!hasActiveOrderLink.action?OID='+objectOid;
	var returnValue = "";
	$.ajax({
        type: "POST",
        async: false,//是否异步
        url : url,
        dataType : "json",
        success: function(result) {
        	returnValue = result.MESSAGE;
		}
	});
	return returnValue;
};

//check是否是现行套数据源
Plm.prototype.checkisActiveSeted = function (objectOid){
	var url = contextPath + '/adm/active/ActiveSetHandle!isActiveSetedObject.action?OID='+objectOid;
	var returnValue = "";
	$.ajax({
      type: "POST",
      async: false,//是否异步
      url : url,
      dataType : "json",
      success: function(result) {
      	returnValue = result.MESSAGE;
		}
	});
	return returnValue;
};
//---------------------------------------------------------------------------------
//现行单据操作
//创建
Plm.prototype.createActiveOrder = function(records, callback, container) {
	var param = "FOLDER_OID=";

	if (container != null && container.OID!=null&&container.OID!="undefined") {
		param += container.OID;
	}
	var resultTemp=[];
	var confirmMsg=[];
	var oids="";
	for ( var i = 0; i < records.length; i++) {
		//是否是现行单据数据源
		var message = plm.checkisActiveOrdered(records[i].OID);
		if(message != ""){
			var tip = "所选【"+records[i]["TITLE"]+"】"+message;
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
	// 打开窗口
	plm.openWindow(contextPath + "/plm/load.html", 900, 600, windowid);
	// 在前台中执行
	plm.openByPost(url, pt.ui.JSON.encode(resultTemp), windowid);
};

//check是否是现行单据数据源
Plm.prototype.checkisActiveOrdered = function (objectOid){
	var url = contextPath + '/adm/active/ActiveOrderHandle!isActiveOrderedObject.action?OID='+objectOid;
	var returnValue = "";
	$.ajax({
    type: "POST",
    async: false,//是否异步
    url : url,
    dataType : "json",
    success: function(result) {
    	returnValue = result.MESSAGE;
		}
	});
	return returnValue;
};
var orderCallBack = "";
//搜索对象(ActiveOrder)
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

//搜索版本对象(ActiveOrder)
var searchOid,callback2;
Plm.prototype.searchIteratedList = function (records, callback, container){
	plm.startWait();
	var data_Grid=pt.ui.get("data_TreeGrid_before");
	var records=data_Grid.getSelections();
	if(records.length==0){
		plm.endWait();
		plm.showMessage({ title : '错误提示', message : "请选择一个更改前对象进行操作！", icon : '3'});
		return;
	}
	if(records.length>1){
		plm.endWait();
		plm.showMessage({ title : '错误提示', message : "只能对单个更改前对象操作！", icon : '3'});
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
					msg1 += dataObject[i].SOURCE.NUMBER+"、";
				}
			}else{
				count+=1;
				msg2 += dataObject[i].SOURCE.NUMBER+"、";
			}
		}else
		{
			data_Grid.appendRow(dataObject[i]);
		}
	}

	if(count>0){
		if(msg1.length>0){
			msg+="编号为 ["+msg1.substring(0, msg1.length-1)+"] 的另一个版本已存在;";
		}
		if(msg2.length>0){
			msg+="编号为 ["+msg2.substring(0, msg2.length-1)+"] 的数据已存在,不能重复添加！";
		}
		plm.showMessage({ title : '错误提示', message : msg, icon : '3'});
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
//check是否是最新版本
Plm.prototype.checkLastestVersion = function (objectOid){
	var url = contextPath + '/adm/active/ActiveOrderHandle!isLastestVersion.action?OID='+objectOid;
	var returnValue = "";
	$.ajax({
    type: "POST",
    async: false,//是否异步
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
//编辑
Plm.prototype.editActiveOrderOperate = function(records, callback, container) {
	if(records.length!=1){
		plm.showMessage({ title : '错误提示', message : "只能对单个对象操作！", icon : '3'});
		return;
	}
	plm.startWait();
	var oid = records[0].OID;
	admValidation(oid,"plm_modify","editActiveOrderOperateCallBack");
	
};

function editActiveOrderOperateCallBack(oid){
	var url = contextPath + '/adm/activeorder/editActiveOrderTab.jsp?clickTab=tab1&OID=' + oid;
	plm.endWait();
	// 打开窗口
	plm.openWindow(url, 900, 600, "editActiveOrder");
}

//编辑
Plm.prototype.editActiveOrderToolbar = function(records, callback, container) {
	plm.startWait();
	admValidation(activeOrderOid,"plm_modify","editActiveOrderToolbarCallBack");
};

function editActiveOrderToolbarCallBack(oid){
	var url = contextPath + '/adm/activeorder/editActiveOrderTab.jsp?clickTab=tab2';
	plm.endWait();
	// 打开窗口
	plm.openWindow(url, 900, 600, "editActiveOrder");
}

//编辑
Plm.prototype.editActiveOrderAdd = function(records, callback, container) {
	if(records.length!=1){
		plm.showMessage({ title : '错误提示', message : "只能对单个对象操作！", icon : '3'});
		return;
	}
	var oid = records[0].OID;
	var url = contextPath + '/adm/activeorder/editActiveOrderTab.jsp?clickTab=tab1&OID=' + oid;
	// 打开窗口
	plm.openWindow(url, 900, 600, "editActiveOrder");
};
//点击直接入库按钮
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
				plm.showMessage({title:'错误提示', message:mes, icon:'1'});
				return;
			}else if(result.FLAG == "false"){
				alert("您没有操作权限！");
				return;
			}
			window.reload();
		},
		error:function(){
			plm.endWait();
			plm.showMessage({title:'错误提示', message:"操作失败！", icon:'1'});
		}
	});
	
};
//权限验证
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
			plm.showMessage({title:'错误提示', message:"获取操作权限失败！", icon:'1'});
		}
	});    	
}

// 选择现行文件编号
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

// 现行数据文件夹树-右键菜单-作为根显示
Plm.prototype.activeModelAsRootDisplay = function(records, callback, container) {
	var asRootOid =  records[0]["OID"];
	//刷新导航栏	ds
	parent.refreshNavigator("NAVIGATE_TO_OBJECT", records[0]);
	window.location.href = contextPath + "/adm/folder/visitFolder.jsp?OID="+asRootOid+"&asRootOid="+asRootOid;
}

// 现行数据文件夹树-快捷菜单-返回根目录
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

//选择文件夹
Plm.prototype.selectActiveFolder = function(treetype,context,callback){
	var folderiid = "";
	if(context!=null&&context.OID!=null){ 
		folderiid = context.OID;
	}
	var url =contextPath+"/adm/folder/folderPubTree.jsp?treeType="+treetype+"&folderiid="+folderiid+"&CALLBACK="+callback;
	plm.openWindow(url, 400, 500, "selectFolder");	
};

// 还原现行对象
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
			plm.showMessage({title:'错误提示', message:"还原操作失败！", icon:'1'});
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
		plm.showMessage({title:'错误提示', message:"文件夹类型不能执行重命名操作！", icon:'1'});
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
 * 重新指派生命周期模板。
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
		plm.showMessage({title:'错误提示', message:"文件夹类型不能执行重新指配生命周期操作！", icon:'1'});
		return;
	}	
	
	var innerId = records[0]["INNERID"];
	var classId = records[0]["CLASSID"];
	
	var parameter = {
		operation : "resetLifecycle",
		objectIId : innerId,
		classId : classId
	};
	addWFParam(parameter, records); // 添加工作流参数
	
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
			plm.showMessage({title:'错误提示', message:"文件夹类型不能执行剪切操作！", icon:'1'});
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
			plm.showMessage({title:'错误提示', message:"文件夹类型不能执行复制操作！", icon:'1'});
			return;
		}
	}
	plm.clipboard.copyToClipboard(records, callback, container);
};