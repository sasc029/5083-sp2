<%@page import="com.bjsasc.adm.active.service.activechangeservice.ActiveJfreeChartBase"%>
<%@page import="com.bjsasc.plm.core.util.DateTimeUtil"%>
<%@page import="com.bjsasc.adm.active.helper.AdmHelper"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="org.jfree.chart.servlet.ServletUtilities"%>
<%@page import="org.jfree.chart.ChartUtilities"%>
<%@page import="org.jfree.chart.ChartRenderingInfo"%>
<%@page import="org.jfree.chart.entity.StandardEntityCollection"%>
<%@page import="org.jfree.chart.JFreeChart"%>
<%@page import="org.jfree.data.category.DefaultCategoryDataset"%>
<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.core.change.ChangeKeyWords"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.bjsasc.plm.change.ChangeManager"%>
<%@page import="java.io.PrintWriter"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.awt.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
request.setCharacterEncoding("UTF-8");
String activename=request.getParameter("activename");
String activeModelId=request.getParameter("activeModelId");
String activeModelIds[]=activeModelId.split("<");
String activeids="";
for(int i=0;i<activeModelIds.length;i++){
	activeids+=activeModelIds[i];
}
String showDate=request.getParameter("showDate");
String changeClassText=request.getParameter("changeClassText");
String begintime=request.getParameter("begintime");
String endtime = request.getParameter("endtime");
Long btime = DateTimeUtil.parseDateLong(begintime, DateTimeUtil.DATE_YYYYMMDD);
Long etime = DateTimeUtil.parseDateLong(endtime, DateTimeUtil.DATE_YYYYMMDD) + 86399000L;
//根据分组（展示）条件得到统计结果
List<Map<String,Object>>   getStatResultData=new ArrayList<Map<String,Object>>();
List<Map<String,Object>>   getStatResultDatas=new ArrayList<Map<String,Object>>();
DefaultCategoryDataset dataset = new DefaultCategoryDataset();
JFreeChart jfreechart =null; 
String activenames[]=activename.split(",");
String changeClassTexts[]=changeClassText.split(",");
      for(int i=0;i<activenames.length;i++){
		getStatResultDatas=AdmHelper.getActiveChangeService().getStatResultByGroup(activenames[i],activeids,btime,etime,showDate);
		getStatResultData.addAll(getStatResultDatas);
		getStatResultData.get(i).put("name",changeClassTexts[i]);	
}

jfreechart = ActiveJfreeChartBase.createChart(ActiveJfreeChartBase.getDatasetByGroup(getStatResultData),"数量（个）",changeClassText+"统计",request.getContextPath()+"/adm/query/detail.jsp",true);

	StandardEntityCollection sec = new StandardEntityCollection();
	ChartRenderingInfo info = new ChartRenderingInfo(sec);
	PrintWriter w = new PrintWriter(out);//输出MAP信息 
	//500是图片长度，300是图片高度 
	String filename = ServletUtilities.saveChartAsPNG(jfreechart, 980, 430, info, session);
	ChartUtilities.writeImageMap(w, "map0", info, false);

	String graphURL = request.getContextPath() + "/DisplayChart?filename=" + filename;
%>
<script type="text/javascript" src="<%=request.getContextPath()%>/platform/common/js/ptutil.js" charset="GBK"></script>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script> 
<P ALIGN="CENTER"> 
	<img src="<%=graphURL%>" width=980 height=430 border=0 usemap="#map0"> 
</P> 
</body>
</html>
<script type="text/javascript">
$(document).ready(function(){
	formData={activeids:"<%=activeids%>",showdate:"<%=showDate%>",btime:"<%=btime%>",etime:"<%=etime%>"};

	var are = $("area");
	for(var i=0;i<are.length;i++){
		$(are[i]).attr("url",$(are[i]).attr("href")).attr("href","#");
		
	}
	$("area").bind("click",function(){
		var width = 1050;
		var height = 700;
		var left = (window.screen.width - width)/2;
		var top = (window.screen.height - height)/2;
		var param = "height="+height+",width="+width+",left="+left+",top="+top;
		var url=$(this).attr("url");
		window.open(url,"detail","menubar=no,toolbar=no,location=no,scrollbars=no,status=yes,directories=no,resizable=no,copyhistory=yes,"+param+"");
		plm.openByPost(url,pt.ui.JSON.encode(formData),"detail");
	});
});
</script> 