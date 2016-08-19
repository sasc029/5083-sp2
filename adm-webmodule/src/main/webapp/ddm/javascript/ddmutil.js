if (!window.ddm) {
	ddm = function() {
	};
};

ddm.tools = function() {
};

ddm.tools.showModalDialog = function(url,width,height) {
	var strFeatures = "dialogWidth=500px;dialogHeight=180px;center=1;middle=yes;help=no;status=no;scroll=yes;resizable=no;";
	if (undefined != width && undefined != height) {
		strFeatures = "dialogWidth="+width+"px;dialogHeight="+height+"px;center=1;middle=yes;help=no;status=no;scroll=yes;resizable=no;";
	}
	var retObj = window.showModalDialog(url,window,strFeatures);
	return retObj;
};

ddm.tools.openWindow = function(url,width,height,name) {
	setTimeout("plm.openWindow('"+url+"','"+width+"','"+height+"','"+name+"');",100);
};

ddm.tools.showModalDialogReload = function(url) {
	var strFeatures = "dialogHeight: 550px; dialogWidth: 1000px;  center: Yes; help: no; resizable: Yes; status: no; scroll: Yes";
	var retObj = window.showModalDialog(url,"self",strFeatures);
	if (retObj) {
		try {
			reloadOrder();
		} catch (e) {}
	}
};

ddm.tools.showModalDialogReloadTaskBox = function(url) {
	var strFeatures = "dialogHeight: 550px; dialogWidth: 1000px;  center: Yes; help: no; resizable: Yes; status: no; scroll: Yes";
	var retObj = window.showModalDialog(url,"self",strFeatures);
	if (retObj) {
		try {
			reload();
		} catch (e) {}
	}
};
/**
 * 公共选人页面 
 * 参数: Parameter : 传递参数
 */
ddm.tools.selectUser = function(parameter) {
	var t = (window.screen.availHeight - 30 - 500) / 2;
	var l = (window.screen.availWidth - 10 - 700) / 2;
	var IsModal = parameter.IsModal;
	if(!IsModal){
		IsModal = "false";
	}
	var returnType = parameter.returnType;
	if(!returnType){
		returnType = "arrayObj";
	}
	var userStatus = parameter.userStatus;
	if(!userStatus){
		userStatus ="A";
	}
	var SelectMode = parameter.SelectMode;
	if(!SelectMode){
		SelectMode = "multiple";
	}
	var callbackFun = parameter.callbackFun;
	var scope = parameter.scope;
	if(!scope){
		scope = "all";
	}
	var projectName = "/" + pt.sa.tools.getProjectName();
    var testUrl = projectName + "/ddm/public/selectuser/index.jsp"+
    	"?returnType="+returnType
		+"&userStatus="+ userStatus
		+"&SelectMode="+SelectMode
		+"&callbackFun="+callbackFun
		+"&scope="+ scope
		+"&IsModal="+IsModal;
    var domainRef = parameter.domainRef;
    if(domainRef != undefined){
    	testUrl = testUrl+"&domainRef="+ parameter.domainRef;
    }
    var strFeatures;
	if(IsModal == "true"){
		strFeatures = "dialogWidth=700px;dialogHeight=500px;dialogLeft="+l+"px;dialogTop="+
			t+"px;center=1;middle=yes;help=no;status=no;scroll=yes;resizable=no;";
		var reObj = window.showModalDialog(testUrl,window,strFeatures);
		return reObj;
	}else{
		strFeatures ='width=700,height=500,left='+l+',top='+t+',toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no';
		window.open(testUrl,"",strFeatures);
	}
};


/**
 * 选组织页面 
 * 参数: Parameter.params传参
 */
ddm.tools.selectOrg = function(parameter) {
	var t = (window.screen.availHeight - 30 - 500) / 2;
	var l = (window.screen.availWidth - 10 - 700) / 2;
	var IsModal = parameter.IsModal;
	if(!IsModal){
		IsModal = "false";
	}
	var SelectMode = parameter.SelectMode;
	if(!SelectMode){
		SelectMode = "multiple";
	}
	var returnType = parameter.returnType;
	if(!returnType){
		returnType = "arrayObj";
	}
	var scope = parameter.scope;
	if(!scope){
		scope = "all";
	}
	var callbackFun = parameter.callbackFun;
	var projectName = "/" + pt.sa.tools.getProjectName();
    var testUrl = projectName + "/ddm/public/selectorg/index.jsp"+
    	"?returnType="+returnType
		+"&SelectMode="+SelectMode
		+"&callbackFun="+callbackFun
		+"&scope="+scope
		+"&IsModal="+IsModal;
    var domainRef = parameter.domainRef;
    if(domainRef != undefined){
    	testUrl = testUrl+"&domainRef="+ parameter.domainRef;
    }
    var strFeatures;
    if(IsModal == "true"){
		strFeatures = "dialogWidth=705px;dialogHeight=505px;dialogLeft="+l+"px;dialogTop="+
			t+"px;center=1;middle=yes;help=no;status=no;scroll=yes;resizable=no;";
		var reObj = window.showModalDialog(testUrl,window,strFeatures);
		return reObj;
	}else{
		strFeatures ='width=705,height=505,left='+l+',top='+t+',toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no';
		window.open(testUrl,"",strFeatures);
	}
};
