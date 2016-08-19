//�ַ�����Դ����ת��Ϊ�ַ�����
Plm.prototype.transferToDistributeObject = function(records, callback, container){
	if(!plm.checkSelect(records)){
		return;
	}
	var oid =  records[0]["OID"];
	var url = contextPath+"/ddm/distributeorder/createDistributeOrder.jsp?oid=" + oid;
	plm.openWindow(url,800,600);
	
};

//�ļ��и����������������ר��
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
		plm.showMessage({title:'������ʾ', message:"��������ֻ��ѡ��һ���ͻ����ĵ��Ͳ��������", icon:'1'});
		return;
	}else if(objClassArr.length == 2){
		for(i=0; i<objClassArr.length; i++){
			if(objClassArr[i].indexOf("Part")>=0 || objClassArr[i].indexOf("Document")>=0){
				continue;
			}else{
				plm.showMessage({title:'������ʾ', message:"��������ֻ��ѡ��һ���ͻ����ĵ��Ͳ��������", icon:'1'});
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
		   plm.showMessage({title:'������ʾ', message:"���ݴ���", icon:'1'});
	   }
	});
};

//�½����ύ��������
Plm.prototype.submitToWorkflow = function(records, callback, container){
	if (!plm.confirm("ȷ���ύ����������?")) {
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

//���Ⱥ��ύ��������
Plm.prototype.submitDisToWorkflow = function(records, callback, container){
	if (!plm.confirm("ȷ���ύ����������?")) {
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
				plm.showMessage({title:'������ʾ', message:"�ַ���ϢΪ�գ������ύ!", icon:'1'});
				return;
			}
			if(result.SUCCESS != null && result.SUCCESS == "secrityNotCanDis"){
				plm.endWait();
				plm.showMessage({title:'������ʾ', message:"�ַ����ݵ��ܼ����ڷַ��˵��ܼ������ܷ��ţ�", icon:'2'});
				return;
			}
			plm.endWait();
			window.location.href = contextPath + "/ddm/workflow/startWorkflow_DistributeOrder.jsp?OID=" + oid;
		},
		error:function(){
			plm.endWait();
			plm.showMessage({title:'������ʾ', message:"����ʧ�ܣ�", icon:'1'});
		}
	});
	
}

// �ύ���½������ȣ�
Plm.prototype.submitDistribute = function(records, callback, container) {
	if (!plm.confirm("ȷ���ύ������?")) {
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
				plm.showMessage({title:'��ʾ', message:"�˷��ŵ�û�з�������,����ӷ������ݺ����ύ!", icon:'3'});
				return;	
			}else if(result.SUCCESS == "infoFalse"){
				plm.showMessage({title:'��ʾ', message:"�ַ���ϢΪ�գ������ύ!", icon:'3'});
				return;	
			}else{
				plm.showAjaxError(result);
			}
		
		},
		error:function(){
			plm.endWait();
			plm.showMessage({title:'������ʾ', message:"����ʧ�ܣ�", icon:'1'});
		}
	});
}

// �ύ����(���ȵ��ַ���)
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
				plm.showMessage({title:'������ʾ', message:"û�пɲ��������ݣ�", icon:'1'});
				return;
			}
			if(result.SUCCESS != null && result.SUCCESS == "secrityNotCanDis"){
				plm.endWait();
				plm.showMessage({title:'������ʾ', message:"�ַ����ݵ��ܼ����ڷַ��˵��ܼ������ܷ��ţ�", icon:'2'});
				return;
			}
			if (!plm.confirm("ȷ���ύ������?")) {
				plm.endWait();
				return;
			}
			submitDistributeCallBack(orderOid);
		},
		error:function(){
			plm.endWait();
			plm.showMessage({title:'������ʾ', message:"����ʧ�ܣ�", icon:'1'});
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
			plm.showMessage({title:'������ʾ', message:"����ʧ�ܣ�", icon:'1'});
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

//�޸�
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

//ɾ��
Plm.prototype.deleteDistributeOrder = function(records, callback, container) {
	if(!plm.checkSelect(records)){
		return;
	}
	if (!plm.confirm("����,ɾ�����ŵ���ɾ���÷��ŵ�������ӵķַ���Ϣ�Լ���Ӧ��������,�Ƿ����ɾ��?")) {
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
				plm.showMessage({title:'��ʾ', message:"ɾ�����ŵ�����ʧ��!", icon:'1'});
				return;	
			}
		},
		error:function(){
			plm.endWait();
			plm.showMessage({title:'������ʾ', message:"ɾ�����ŵ�����ʧ�ܣ�", icon:'1'});
		}
	});
}

Plm.prototype.OpenWindowAndReload = function(url) {
	//plm.openWindow(contextPath+"/plm/load.html", 800, 800,'blank');
	//��ǰ̨��ִ��
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

//Ȩ����֤
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
			plm.showMessage({title:'������ʾ', message:"��ȡ����Ȩ��ʧ�ܣ�", icon:'1'});
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

// �������񵥸���ǩ plm.singleRefuse
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

//���������ڲ�������ť��������ת��
Plm.prototype.singleForward = function(records){
	var oid =  records[0]["OID"];
	var url = contextPath+"/ddm/distributeelectask/distributeElecForwardProperty_tab.jsp?OID=" + oid;
	ddm.tools.openWindow(url,900,400,"distributeElecTaskOpen");
};

//���������ڲ�������ť���
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
			plm.showMessage({title:'������ʾ', message:"�ύʧ��", icon:'1'});
		}
	});
};
// ��ǩ refuse_signed �ص�����
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
			plm.showMessage({title:'������ʾ', message:"��ǩʧ�ܣ�", icon:'1'});
		}
	});
}
//�������񵥸�ǩ�� plm.singleSubmit
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
			plm.showMessage({title:'������ʾ', message:"����ʧ�ܣ�", icon:'1'});
		}
	});
}
//ˢ�µ��ȡ��ӹ��������Ƽӹ��Ĵ�����������ʱ�����
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

