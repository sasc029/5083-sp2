<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@ page import="com.bjsasc.platform.mainframe.console.*"%>
<%@ page import="com.cascc.avidm.login.model.PersonModel"%>
<%@ taglib uri="/WEB-INF/framework-ext.tld" prefix="ext"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<script type="text/javascript" src="<%=request.getContextPath()%>/platform/javascript/ext/adapter/ext/ext-all.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/platform/javascript/ext/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/platform/javascript/ext/ext-all-debug.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/platform/javascript/ext/CollectGarbage.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/platform/javascript/ext/source/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/platform/javascript/ext/source/calendar/calendar-setup.js"></script>
<script type="text/javascript"  src="<%=request.getContextPath()%>/platform/javascript/ext/source/calendar/calendar-zh.js" charset="GBK"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/platform/javascript/ext/source/locale/ext-lang-zh_CN.js" charset="GBK"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/platform/javascript/ext/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" media="all"   href="<%=request.getContextPath()%>/platform/javascript/ext/resources/css/calendar/calendar.css" />
 
<script type="text/javascript" src="<%=request.getContextPath()%>/platform/javascript/ext/ext-sasc.js" charset="GBK"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/platform/javascript/ext/resources/css/ext-sasc.css" />
<script type="text/javascript"  src="<%=request.getContextPath()%>/platform/common/js/ptutil.js" charset="gb2312"></script>
<script type="text/javascript"  src="<%=request.getContextPath()%>/platform/common/js/ptui/date/date.js" charset="gb2312"></script>

<title></title>
</head>
<body text="#000000" class="avidmPageBody">

<script>
    <ext:nameSpace nameSpace="pt_sa_selectuser_test" />
    //JS 方式
    pt_sa_selectuser_test.test1 = function() {
    	var form = Ext.getCmp('pt_sa_selectuser_form');
    	var div = form.getFieldValue("div");
    	var SelectMode = form.getFieldValue("SelectMode");
    	var IsModal = form.getFieldValue("IsModal");
    	var callbackFun = form.getFieldValue("callbackFun");
    	var returnType = form.getFieldValue("returnType");
    	var ParameterIn = form.getFieldValue("ParameterIn");
    	var scope = form.getFieldValue("scope");
    	var userStatus = form.getFieldValue("userStatus");
    	
        var params = {
    			callbackFun : callbackFun,
				returnType : returnType,
				ParameterIn : ParameterIn,
				IsModal : IsModal,
				SelectMode : SelectMode,
				scope:scope,
				userStatus:userStatus
        	}
		pt.sa.tools.selectUser(params);
	}

 	//JSP 方式
    pt_sa_selectuser_test.test2 = function() {
    	var form = Ext.getCmp('pt_sa_selectuser_form');
    	var div = form.getFieldValue("div");
    	var SelectMode = form.getFieldValue("SelectMode");
    	var IsModal = form.getFieldValue("IsModal");
    	var callbackFun = form.getFieldValue("callbackFun");
    	var returnType = form.getFieldValue("returnType");
    	var ParameterIn = form.getFieldValue("ParameterIn");
    	var scope = form.getFieldValue("scope");
    	var userStatus = form.getFieldValue("userStatus");
        var testUrl = "<%=request.getContextPath()%>/platform/public/selectuser/index.jsp"+"?returnType="+returnType
			+"&userStatus="+userStatus
			+"&SelectMode="+SelectMode
			+"&callbackFun="+callbackFun
			+"&scope="+scope
			+"&IsModal="+IsModal;
        var strFeatures = "dialogWidth=600px;dialogHeight=450px;center=yes;middle=yes ;help=no;status=no;scroll=yes;resizable=yes;";
		if(IsModal == "true"){
			var reObj = window.showModalDialog(testUrl,window,strFeatures);
			var showContent = pt_sa_selectuser_test.getContent(reObj);
    		form.setFieldValue("returnContent",showContent);
		}else{
			window.open(testUrl);
		}
	}

 	//回调函数
    pt_sa_selectuser_test.callback = function(arrayObj) {
    	var form = Ext.getCmp('pt_sa_selectuser_form');
    	var showContent = pt_sa_selectuser_test.getContent(arrayObj);
    	form.setFieldValue("returnContent",showContent);
	}
	//reObj 为返回数据，具体数据类型根据returnType 对应
    pt_sa_selectuser_test.getContent = function(arrayObj){
    	var form = Ext.getCmp('pt_sa_selectuser_form');
    	var returnType = form.getFieldValue("returnType");
    	var showContent = "";
    	if(returnType == null || returnType == "" || returnType == "arrayObj"){
			showContent = "arrayObj.selUsers="+arrayObj.selUsers+"\n";
	    	showContent += "arrayObj.arrUserIID="+arrayObj.arrUserIID+"\n";
	    	showContent += "arrayObj.arrUserId="+arrayObj.arrUserId+"\n";
	    	showContent += "arrayObj.arrUserName="+arrayObj.arrUserName+"\n";
	    	showContent += "arrayObj.arrUserEMail="+arrayObj.arrUserEMail+"\n";
	    	showContent += "arrayObj.arrDomainIID="+arrayObj.arrDomainIID+"\n";
	    	showContent += "arrayObj.arrDomainName="+arrayObj.arrDomainName+"\n";
    	}else{
    		showContent = arrayObj;
    	}
    	return showContent;
	}

	function returnMainTest(){
		document.location.href = '<%=request.getContextPath()%>/platform/test/test.jsp';
	}

</script>
<ext:ext>
<ext:winForm id="pt_sa_selectuser_form" title="选择用户" labelWidth="100" fieldWidth="400" width="800" storeRoot="TABLE" div="Test">
<ext:text name="div" label="div" width="300" readOnly="false"/>
<ext:select name="SelectMode" label="SelectMode" readOnly="false" width="300"
			allowBlank="true" blankText="" data="['single','single'];['multiple','multiple']" value="multiple"/>
<ext:select name="IsModal" label="IsModal" readOnly="false" width="300"
			allowBlank="true" blankText="" data="['true','true'];['false','false']" value="false"/>
<ext:select name="returnType" label="returnType" readOnly="false" width="300"
			allowBlank="true" blankText="" data="['arrayObj','arrayObj'];['json','json']" value="arrayObj"/>
<ext:text name="callbackFun" label="callbackFun" value="pt_sa_selectuser_test.callback" readOnly="false" width="300"/>
<ext:text name="ParameterIn" label="ParameterIn"  readOnly="false" width="300"/>
<ext:select name="scope" label="scope" readOnly="false" width="300"
			allowBlank="true" blankText="" data="['all','all'];['self','self'];['path','path'];['bind','bind']" value="all"/>
<ext:select name="userStatus" label="userStatus" readOnly="false" width="300"
			allowBlank="true" blankText="" data="['A','A'];['F','F'];['T','T'];['ALL','ALL']" value="all"/>
<ext:textArea name="returnContent" value="ReturnValue" readOnly="true" width="700" height="160"/>
<ext:button iconCls="yes" text="JS方式" onClick="pt_sa_selectuser_test.test1()" />
<ext:button iconCls="yes" text="JSP方式" onClick="pt_sa_selectuser_test.test2()" />
<ext:button iconCls="yes" text="返回" onClick="returnMainTest()" />
</ext:winForm>
</ext:ext>
<div id="Test"> </div>
</body>
</html>