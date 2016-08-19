
<%
	/**
	 *  页面说明
	 *  1.名称： 
	 *  2.作用： 公共选择组织页面入口
	 *  3.参数： div：  如果div有值则选人页面渲染到一个新打开的IE窗口中不再是ext window控件
	 *		    SelectMode ：   选择模式，单选“single”或者多选“multiple” 默认为“multiple”
	 *			callbackFun:  回调函数名
	 *			returnType:   返回值样式，可选值“arrayObj”，“json”，默认为“arrayObj”
			 	scope: 组织列表范围【all】全域组织；【self】在指定域中出生的组织；【ref】和指定域关联的组织；【path】指定域以及其祖先出生的角色
			 			如果制定域是组织域则默认为显示出生组织，如果制定域是应用域则显示关联组织
				domainRef 域的innerid 默认为当前登录的域
	 
	 */
%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@ page import="com.bjsasc.platform.mainframe.console.*"%>
<%@ page import="com.cascc.avidm.login.model.PersonModel"%>
<%@ taglib uri="/WEB-INF/framework-ext.tld" prefix="ext"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	//直接跳转到新界面库中
    request.getRequestDispatcher("UISelectOrg.jsp").forward(request,response);
	
	String returnType = request.getParameter("returnType");
	String ParameterIn = request.getParameter("ParameterIn");
	String SelectMode = request.getParameter("SelectMode");
	String callbackFun = request.getParameter("callbackFun");
	String IsModal = request.getParameter("IsModal");
	if(IsModal != null && IsModal.trim().equalsIgnoreCase("true")){
		IsModal = "true";
	}else{
		IsModal = "false";
	}
	String scope = request.getParameter("scope");
	String domainRef = request.getParameter("domainRef");
%>
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
<ext:nameSpace nameSpace="pt_sa_selectorg_index" />
Ext.onReady(function(){
	pt_sa_selectorg_index.setOrg = function(){
		var p = {
			callbackFun : 'pt_sa_selectorg_index.callBackFunc',
			div :"pt_sa_selectorg_index_div",
			returnType : '<%=returnType%>',
			ParameterIn : '<%=ParameterIn%>',
			SelectMode : '<%=SelectMode%>',
			scope : '<%=scope%>',
			domainIID : '<%=domainRef%>'
		}
		pt.sa.tools.selectOrg(p);
	}
	pt_sa_selectorg_index.setOrg();

	pt_sa_selectorg_index.callBackFunc = function(arrayObj){
		var callb = "<%=callbackFun%>";
		var IsModal = "<%=IsModal%>";
		if(IsModal == 'true'){
			window.returnValue = arrayObj;
		}else{
			eval("opener."+callb+"(arrayObj);");
		}
		window.close();
	}
});

</script>
<div id="pt_sa_selectorg_index_div" style="height:100%;width:100%;" ></div>
</body>
</html>