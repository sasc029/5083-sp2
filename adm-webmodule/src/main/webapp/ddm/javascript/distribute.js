//分发数据源对象转换为分发对象
Plm.prototype.transferToDistributeObject = function(records, callback, container){
	if(!plm.checkSelect(records)){
		return;
	}
	var oid =  records[0]["OID"];
	var url = contextPath+"/ddm/distributeorder/createDistributeOrder.jsp?oid=" + oid;
	plm.openWindow(url,800,600);
	
};

//文件夹更多操作中批量发放专用
Plm.prototype.transferToDistributeObjectMulti = function(records, callback, container){
	var oids = "";
	if(!plm.checkSelect(records)){
		return;
	}
	var objClass = "";
	for(var i=0;i<records.length;i++){
		oids += records[i]["OID"] + ",";
		var str = records[i]["OID"].split(":");
		if(objClass.indexOf(str[0]) < 0){
			objClass = objClass + str[0] + ",";
		}
	}
	
	objClass = objClass.substring(0,objClass.length-1);
	var objClassArr = objClass.split(",");
	if(objClassArr.length > 2){
		plm.showMessage({title:'错误提示', message:"批量发放只能选择单一类型或者文档和部件的组合", icon:'1'});
		return;
	}else if(objClassArr.length == 2){
		for(i=0; i<objClassArr.length; i++){
			if(objClassArr[i].indexOf("Part")>=0 || objClassArr[i].indexOf("Document")>=0){
				continue;
			}else{
				plm.showMessage({title:'错误提示', message:"批量发放只能选择单一类型或者文档和部件的组合", icon:'1'});
				return;
			}
		}
	}
	
	
	oids = oids.substring(0, oids.length - 1);
	var url = contextPath+"/ddm/distributeorder/createDistributeOrderMulti.jsp?oids=" + oids;
	plm.openWindow(url,800,600);
	
};


Plm.prototype.toSubmit = function(records){
	var url=contextPath+"/ddm/toDistributeOrWorkflow.jsp";
	var oid = records[0]["OID"];
	$.ajax({
		type: "post",
		url: url,
		dataType: "json",
		data:"OID="+oid,
		success: function(result){
			if(result.SUCCESS == "submitDistribute"){
				plm.submitDistribute(records);
			}else if(result.SUCCESS == "submitToWorkflow"){
				plm.submitToWorkflow(records);
			}else if(result.SUCCESS == "submitDis"){
				plm.submitDis(records);
			}else if(result.SUCCESS == "submitDisToWorkflow"){
				plm.submitDisToWorkflow(records);
			}
	   },
	   error:function(){
		   plm.endWait();
		   plm.showMessage({title:'错误提示', message:"数据错误", icon:'1'});
	   }
	});
};

//新建后提交到工作流
Plm.prototype.submitToWorkflow = function(records, callback, container){
	if (!plm.confirm("确定提交到工作流吗?")) {
		return;
	}
	if(!plm.checkSelect(records)){
		return;
	}
	plm.startWait();
	var oid = records[0]["OID"];
	ddmValidation(oid,"plm_promote","submitToWorkflowCallBack");
};

function submitToWorkflowCallBack(oid){
	window.location.href = contextPath + "/ddm/workflow/startWorkflow_DistributeOrder.jsp?OID=" + oid;
}

//调度后提交到工作流
Plm.prototype.submitDisToWorkflow = function(records, callback, container){
	if (!plm.confirm("确定提交到工作流吗?")) {
		return;
	}
	if(!plm.checkSelect(records)){
		return;
	}
	plm.startWait();
	var oid = records[0]["OID"];
	ddmValidation(oid,"plm_promote","submitDisToWorkflowCallBack");
};

function submitDisToWorkflowCallBack(oid){
	var url = contextPath + "/ddm/distribute/distributeInfoHandle!checkDisInfo.action";
	$.ajax({
		url:url,
		type:"post",
		dataType:"json",
		data:"OID="+oid+"&flag=true",
		success:function(result){
			if(result.SUCCESS != null && result.SUCCESS == "false"){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"分发信息为空，不能提交!", icon:'1'});
				return;
			}
			if(result.SUCCESS != null && result.SUCCESS == "secrityNotCanDis"){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"分发数据的密级高于分发人的密级，不能发放！", icon:'2'});
				return;
			}
			plm.endWait();
			window.location.href = contextPath + "/ddm/workflow/startWorkflow_DistributeOrder.jsp?OID=" + oid;
		},
		error:function(){
			plm.endWait();
			plm.showMessage({title:'错误提示', message:"操作失败！", icon:'1'});
		}
	});
	
}

// 提交（新建到调度）
Plm.prototype.submitDistribute = function(records, callback, container) {
	if (!plm.confirm("确定提交发放吗?")) {
		return;
	}
	if(!plm.checkSelect(records)){
		return;
	}
	plm.startWait();
	var oid =  records[0]["OID"];
	ddmValidation(oid,"plm_promote","submitDistributeToCreate");	
	
};

function submitDistributeToCreate(oid){
	var url = contextPath + "/ddm/distribute/distributeOrderHandle!updateOrderLifeCycle.action";
	$.ajax({
		url:url,
		type:"post",
		dataType:"json",
		data:"OID="+oid,
		success:function(result){
			plm.endWait();
			if(result.SUCCESS == "true"){
				setTimeout("pageReload();",500);
				window.returnValue=true;
				window.close();
				top.opener.location.reload();
			}else if(result.SUCCESS == "dataFalse"){
				plm.showMessage({title:'提示', message:"此发放单没有发放数据,请添加发放数据后再提交!", icon:'3'});
				return;	
			}else if(result.SUCCESS == "infoFalse"){
				plm.showMessage({title:'提示', message:"分发信息为空，不能提交!", icon:'3'});
				return;	
			}else{
				plm.showAjaxError(result);
			}
		
		},
		error:function(){
			plm.endWait();
			plm.showMessage({title:'错误提示', message:"操作失败！", icon:'1'});
		}
	});
}

// 提交发放(调度到分发中)
Plm.prototype.submitDis = function(records, callback, container) {
	if(!plm.checkSelect(records)){
		return;
	}
	var oid =  records[0]["OID"];
    ddmValidation(oid,"plm_promote","checkDisInfo");
};

function checkDisInfo(orderOid) {
	var url = contextPath + "/ddm/distribute/distributeInfoHandle!checkDisInfo.action";
	$.ajax({
		url:url,
		type:"post",
		dataType:"json",
		data:"OID="+orderOid+"&flag=true",
		success:function(result){
			if(result.SUCCESS != null && result.SUCCESS == "false"){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"没有可操作的数据！", icon:'1'});
				return;
			}
			if(result.SUCCESS != null && result.SUCCESS == "secrityNotCanDis"){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"分发数据的密级高于分发人的密级，不能发放！", icon:'2'});
				return;
			}
			if (!plm.confirm("确定提交发放吗?")) {
				plm.endWait();
				return;
			}
			submitDistributeCallBack(orderOid);
		},
		error:function(){
			plm.endWait();
			plm.showMessage({title:'错误提示', message:"操作失败！", icon:'1'});
		}
	});
}

function submitDistributeCallBack(orderOid) {
	var url = contextPath + "/ddm/distribute/distributeInfoHandle!createDistributeTask.action";
	plm.startWait();
	$.ajax({
		url:url,
		type:"post",
		data:"OID="+orderOid,
		success:function(result){
			plm.endWait();
			if(result.SUCCESS != null && result.SUCCESS == "false"){
				plm.showAjaxError(result);
				return;
			}
			pageReload();
		},
		error:function(){
			plm.endWait();
			plm.showMessage({title:'错误提示', message:"操作失败！", icon:'1'});
		}
	});
}

function pageReload(){
	try{
		var tempCommonSearch = contextPath +"/ddm/distribute";
		var temprelation = contextPath +"/ddm/distributeorder/relatedDistributeOrders.jsp";
		var tempfolder = contextPath +"/plm/folder/folderlist.jsp";
		var tempSendedDistributeOrder = contextPath + "/ddm/distribute/distributeOrderHandle!listSendedDistributeOrder.action";
		if(top.opener==undefined){
			window.returnValue=true;
			window.close();
		}else{
			var t1=top.opener.location.href;
			if(t1.indexOf(temprelation) > 0){
				top.opener.parent.location.href = top.opener.parent.location.href;
				top.opener.parent.location.reload();
			}else if(t1.indexOf(tempSendedDistributeOrder) > 0){
				top.opener.location.reload();
			}else if(t1.indexOf(tempCommonSearch) > 0){
				top.opener.reloadOrder();
			}else if(t1.indexOf(tempfolder) > 0){
				top.opener.reload();
			}else {
				top.opener.location.reload();
			}	
			top.close();
		}
	}catch(e){
	}
};

//修改
Plm.prototype.updateDistributeOrder = function(records, callback, container) {
	if(!plm.checkSelect(records)){
		return;
	}
	plm.startWait();
	var oid =  records[0]["OID"];
	ddmValidation(oid,"plm_modify","updateDistributeOrderCallBack");
	
};

function updateDistributeOrderCallBack(oid){
	plm.endWait();
	var url = contextPath + "/ddm/distributeorder/updateDistributeOrder.jsp?OID=" + oid;
	plm.showModalDialog(url, 640, 480);
}

//删除
Plm.prototype.deleteDistributeOrder = function(records, callback, container) {
	if(!plm.checkSelect(records)){
		return;
	}
	if (!plm.confirm("警告,删除发放单会删除该发放单上已添加的分发信息以及相应发放数据,是否继续删除?")) {
		return;
	}
	plm.startWait();
	var oid = records[0]["OID"];
	ddmValidation(oid,"plm_delete","deleteDistributeOrderCallBack");
	
};

function deleteDistributeOrderCallBack(oid){
	var url = contextPath + "/ddm/distribute/distributeOrderHandle!propertydeleteDistributeOrder.action";
	$.ajax({
		url:url,
		type:"post",
		dataType:"json",
		data:"OIDS=" + oid,
		success:function(result){
			plm.endWait();
			if(result.SUCCESS == "true"){
				setTimeout("pageReload();",500);
			}else{
				plm.showMessage({title:'提示', message:"删除发放单操作失败!", icon:'1'});
				return;	
			}
		},
		error:function(){
			plm.endWait();
			plm.showMessage({title:'错误提示', message:"删除发放单操作失败！", icon:'1'});
		}
	});
}

Plm.prototype.OpenWindowAndReload = function(url) {
	//plm.openWindow(contextPath+"/plm/load.html", 800, 800,'blank');
	//在前台中执行
	//plm.openByPost(url, pt.ui.JSON.encode(""), 'blank');
	setTimeout(function(){plm.openWindow(url, 800, 600,"newWindow");},300);
};

Plm.prototype.ddmReload= function(){
	try{
		var tempCommonSearch = contextPath +"/ddm/distribute";
		var temprelation = contextPath +"/ddm/distributeorder/relatedDistributeOrders.jsp";
		var tempfolder = contextPath +"/plm/folder/folderlist.jsp";
		var tempSendedDistributeOrder = contextPath + "/ddm/distribute/distributeOrderHandle!listSendedDistributeOrder.action";
		var t1=top.opener.location.href;

		if(t1.indexOf(temprelation) > 0){
			top.opener.parent.location.href = top.opener.parent.location.href;
			top.opener.parent.location.reload();
		}else if(t1.indexOf(tempSendedDistributeOrder) > 0){
			top.opener.location.reload();
		}else if(t1.indexOf(tempCommonSearch) > 0){
			top.opener.reloadOrder();
		}else if(t1.indexOf(tempfolder) > 0){
			top.opener.reload();
		}else {
			top.opener.location.reload();
		}
	}catch(e){
	}
};

//权限验证
function ddmValidation(oids,operate,callback){
	//plm.startWait();
	var operateUrl = contextPath + "/ddm/distribute/distributeOperateValidationHandle!distributeOperateValidation.action";
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

function onLoadSuccess_ddm(gridId) {
	var grid = pt.ui.get(gridId);
	var records = grid.getRows(); 
	for ( var i = 0; i < records.length; i++) {
		var number = records[i].SOURCE.NUMBER;
		var name = records[i].SOURCE.NAME;
		var classId = records[i].CLASSID;
		var innerId = records[i].INNERID;
		var oid = classId + ":" + innerId;

		if (number.indexOf("href") >=0 ) {
			continue;
		}
		var url = contextPath + "/ddm/public/visitObject.jsp?OID=" + oid + "&classId=" + classId;
		var fNumber = "<a href='#' onclick=\"ddm.tools.showModalDialogReload('"+url+"')\" title='{"+number+"}'>"+number+"</a>";
		var fName = "<a href='#' onclick=\"ddm.tools.showModalDialogReload('"+url+"')\" title='{"+name+"}'>"+name+"</a>";
		//grid.setColumnValue("NUMBER", fNumber, i);
		//grid.setColumnValue("NAME", fName, i);

		records[i].NUMBER = fNumber;
		records[i].NAME = fName;
	}
}

// 电子任务单个拒签 plm.singleRefuse
Plm.prototype.singleRefuse= function(records){
	var oid = records[0]["OID"];
	plm.startWait();
	//ddmValidation(oid,"plm_promote","refuse_signedForElec");
	refuse_signedForElec(oid);
};

function refuse_signedForElec(value){

	var url = contextPath+'/ddm/distributetask/distributeReturnReason.jsp?flag=elecTaskRefuse';
	plm.endWait();
    var retObj = ddm.tools.showModalDialog(url);
	if(retObj){
		doReturn(retObj, value);	
	}
}

//电子任务内部操作按钮二级调度转发
Plm.prototype.singleForward = function(records){
	var oid =  records[0]["OID"];
	var url = contextPath+"/ddm/distributeelectask/distributeElecForwardProperty_tab.jsp?OID=" + oid;
	ddm.tools.openWindow(url,900,400,"distributeElecTaskOpen");
};

//电子任务内部操作按钮完成
Plm.prototype.submitToDistributeOperate = function(records){
	var oid = records[0]["OID"];
	var updateURL = contextPath + "/ddm/distribute/distributeTaskHandle!updateDistributeElecTaskLife.action";	
	plm.startWait();
	$.ajax({
		url:updateURL,
		type:"post",
		dataType:"json",
		data:"OIDS="+oid,
		success:function(result){
			plm.endWait();
			if(result.SUCCESS != null && result.SUCCESS == "false"){
				plm.showAjaxError(result);
				return;
			}
			window.returnValue=true;
            window.close();
		},
		error:function(){
			plm.endWait();
			plm.showMessage({title:'错误提示', message:"提交失败", icon:'1'});
		}
	});
};
// 拒签 refuse_signed 回调函数
function doReturn(retObj, value) {
	var signedUrl = contextPath + "/ddm/distribute/distributeTaskHandle!updateDistributeElecTask.action";
	plm.startWait();
	$.ajax({
		url:signedUrl,
		type:"post",
		dataType:"json",
		data:"OIDS="+value+"&operate=refuse&returnReason="+retObj,
		success:function(result){
			plm.endWait();
			if(result.SUCCESS != null && result.SUCCESS == "false"){
				plm.showAjaxError(result);
				return;
			}
			var tempvisit = "/plm/common/visit.jsp";
			var t0=top.parent.location.href ;
            if(t0.indexOf(tempvisit) > 0){
            	window.returnValue=true;
            	window.close();
            }else{
            	window.location.href = contextPath + "/plm/system/task/taskbox.jsp?folder=" + "daiban";
            }
		},
		error:function(){
			plm.endWait();
			plm.showMessage({title:'错误提示', message:"拒签失败！", icon:'1'});
		}
	});
}
//电子任务单个签收 plm.singleSubmit
Plm.prototype.singleSubmit= function(records){
	try{
        var oid = records[0]["OID"];
		plm.startWait();
		//ddmValidation(oid,"plm_promote","singnedReceiveForElec");
		singnedReceiveForElec(oid);
	}catch(e){
	}
};

function singnedReceiveForElec(value){
	var signedUrl = contextPath + "/ddm/distribute/distributeTaskHandle!updateDistributeElecTask.action";
	$.ajax({
		url:signedUrl,
		type:"post",
		dataType:"json",
		data:"OIDS="+value+"&operate=signed",
		success:function(result){
			plm.endWait();
			if(result.SUCCESS != null && result.SUCCESS == "false"){
				plm.showAjaxError(result);
				return;
			}
			var tempvisit = "/plm/common/visit.jsp";
			var t0=top.parent.location.href ;
            if(t0.indexOf(tempvisit) > 0){
            	window.returnValue=true;
            	window.close();
            }else{
            	window.location.href = contextPath + "/plm/system/task/taskbox.jsp?folder=" + "daiban";
            }
		},
		error:function(){
			plm.endWait();
			plm.showMessage({title:'错误提示', message:"操作失败！", icon:'1'});
		}
	});
}
//刷新调度、加工单、复制加工的待办任务数量时候调用
Plm.prototype.refreshTree = function(nodeId){
	var leftFrame = parent.parent.frames['leftFrame'];
	if(leftFrame != null){
		//var nodeId1="com.bjsasc.ddm.distribute.dispatchManager";
		//var nodeId2="com.bjsasc.ddm.distribute.distributePaperTask";
		//var nodeId3="com.bjsasc.ddm.distribute.duplicate";
		//var tree = getTree();
		var tree =leftFrame.getTree();
		if(tree != null && tree.data.find != null){
			var record = tree.data.find({id:nodeId});
			//var record2 = tree.data.find({id:nodeId2});
			//var record3 = tree.data.find({id:nodeId3});
			if(record != null && record.refreshUrl != null){
				$.ajax({
			        type:"post",            
					url:contextPath+"/ddm/outlook/listDistributeMenu.jsp?nodeId="+nodeId, 
			        async: false,
			        data: {},  
			        dataType:'json',
			        success: function(result){
			        	tree.data.beginChange();
			        	if(nodeId=='com.bjsasc.ddm.distribute.dispatchManager'){
							record.Name =result[1].Name;
					    	record.iconsrc = result[1].iconsrc;
			        	}else if(nodeId=='com.bjsasc.ddm.distribute.distributePaperTask'){
							record.Name =result[2].Name;
					    	record.iconsrc = result[2].iconsrc;
			        	}
			        	tree.data.endChange();
				    	
				    },
			        error: function(code){
			        	alert(code);
			        }
				});				
			} 
		}
}
};

