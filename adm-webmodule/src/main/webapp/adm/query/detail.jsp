
<%@page import="com.bjsasc.plm.grid.GridHelper"%>
<%@page import="com.bjsasc.platform.objectmodel.business.persist.PTFactor"%>
<%@page import="com.bjsasc.plm.change.query.ToObjectUtil"%>
<%@page import="com.bjsasc.adm.active.helper.AdmHelper"%>
<%@page import="com.bjsasc.plm.grid.GridUtil"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="com.bjsasc.plm.collector.CollectorUtil"%>
<%@page import="com.bjsasc.plm.url.ContextPath"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@ page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
String contextPath = request.getContextPath();
request.setCharacterEncoding("UTF-8");
String data=request.getParameter(KeyS.DATA);
if(data == null)return;
Map<String,String> map = DataUtil.JsonToMap(data);
String activeids=map.get("activeids"); 
String showDate=map.get("showdate");
Long btime=Long.parseLong(map.get("btime"));
Long etime= Long.parseLong(map.get("etime"));
String changeClassId =new String(request.getParameter("series").getBytes("ISO-8859-1"),"UTF-8");
String titleContent=changeClassId;
String spot="";
String gridid="";
if(changeClassId.equals("现行文档")){
	changeClassId="doc";
	 spot="QUERYACTIVEDOCUMENTS";
	 gridid="activedocuments";
}else if(changeClassId.equals("现行套图")){
	changeClassId="set";
    spot="QUERYACTIVESET";
    gridid="activeset";
}else if(changeClassId.equals("现行单据")){
	changeClassId="order";
	 spot="QUERYACTIVEORDER";
	 gridid="activeorder";
}
  List<String> fkeys = GridHelper.getService().getMyLatestGridViewColumnIds(spot, spot);
  String time =new String(request.getParameter("category").getBytes("ISO-8859-1"),"UTF-8");
  List<Object> getDetailResultData=AdmHelper.getActiveChangeService().listdetail(changeClassId,time,activeids,showDate,btime,etime) ;
  List<Map<String, Object>> mapList=AdmHelper.getActiveChangeService().listMap(fkeys,getDetailResultData);
  GridDataUtil.prepareRowObjectMaps(mapList,  spot);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script> 
</head>
<body class="openWinPage">
<form name="mainForm" method="POST">
<table width="100%"  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="AvidmTitleName"><div class="imgdiv"><img src="<%=contextPath%>/plm/images/common/rename.gif"/></div>
    <div class="namediv"><%=titleContent %></div></td>
  </tr>
</table>
<table width=100% height="96%" cellSpacing="0" cellPadding="0" border="0">
<tr>
<td>
	 <jsp:include page="/plm/common/grid/control/grid.jsp">
		<jsp:param name="spot" value="<%=spot%>"/>
		<jsp:param name="gridId" value="mygrid"/>
		<jsp:param name="operate_container" value=""/>
	</jsp:include>
	</td>	
</tr>
<tr class="AvidmDecision">
	<td>
	<table>
		<tr class="AvidmDecision">
			<td>
				<div class="pt-formbutton" text="确定" id="submitbutton" onclick="onOk();"></div>
				<div class="pt-formbutton" text="导出Excel" id="submitbutton" onclick="outExcel();"></div>
			</td>
	    </tr>
	</table>
 	</td>
</tr>
</table>
</form>
<form id="download" name="download" method="POST">
</form>
</body>
<script type="text/javascript">
function onOk(){
	window.close();
};
function outExcel(){
	var url = '<%=contextPath%>/adm/active/ActiveDocumentHandle!dataExport.action?changeClassId='+'<%=changeClassId%>';
	$("#download").attr("action", url);
	$("#download").submit();
};
</script> 
</html>