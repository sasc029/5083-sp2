<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.bjsasc.adm.active.model.query.ActiveQuerySchema"%>
<%@page import="java.util.List"%> 
<%@page import="com.bjsasc.adm.active.helper.AdmHelper"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="GBK"%>
<%
    String classid = request.getParameter(KeyS.CLASSID);
    String innerid=request.getParameter(KeyS.INNERID);
   //默认方案ID
	String defaultSchemaClassid="";
	//方案标题是否显示选中
	String isSelected="";
	//得到所有方案列表
	List<ActiveQuerySchema> listSchema = AdmHelper.getActiveChangeService().getActiveQuerySchemaList();
	List<Map<String, String>> listSchemaMap = new ArrayList<Map<String, String>>();
		//方案默认状态
	int defaultSchema=0;
	//动态生成统计方案下拉列表
	String str_ul="";
	String str_ul1="";
	String str_ul2="";
	//当前选中方案
	String selectValue="";
	String schename="";
	String schenames="";
    if(listSchema.size()>0){
     schenames=listSchema.get(0).getSchemaName();
	};
	int flat=0;
	//循环遍历得到所有方案的各个值 并动态生赋值成下拉列表
	 for (ActiveQuerySchema schema : listSchema) {
		    String chk="0";
		    Map<String, String> tempMap = new HashMap<String, String>();
            String key=schema.getSchemaName();
			String value =schema.getInnerId();
			defaultSchema=schema.getDefaultSchema();
			String activename=schema.getActivename();
			if(defaultSchema==1){
				selectValue=schema.getInnerId();
				defaultSchemaClassid=schema.getActivename();
				chk="1";
				key+="(默认)";
			  schename=schema.getSchemaName();
			 flat=1;
			str_ul1="<li id='l1' cid="+activename+" innerid='"+value+"' chk="+chk+" title='"+key+"'><font style='float:left;margin-left:5px;' size='2px'>"+key+"</font><img src='"+request.getContextPath()+"/plm/images/common/delete.gif'  title='删除' width='12' height='12'/></li>";
			}else{
			str_ul2+="<li id='l1' cid="+activename+" innerid='"+value+"' chk="+chk+" title='"+key+"'><font style='float:left;margin-left:5px;' size='2px'>"+key+"</font><img src='"+request.getContextPath()+"/plm/images/common/delete.gif'  title='删除' width='12' height='12'/></li>";
			}
			str_ul=str_ul1+str_ul2;
			tempMap.put("text",key);
			tempMap.put("value",value);
			listSchemaMap.add(tempMap);
	 }
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
<title>变更统计</title>
<style>
	.indexMenu{
	     border:1px solid #a4bfe4;
	     cursor:hand ;		 
		 width:180px;
		 height:18px; height:20px \9;
		 background: #fff;
		 margin-top:4px;
	}
	.indexMenu img{
		width:10px;
	    height:10px;
	    float:right;
	    margin:4px 3px 0 0;
	}
	ul,li{
		 list-style-type:none;
		 list-style-position : outside;
	     overflow:hidden;                                                                                   
         text-overflow: ellipsis;
         white-space:nowrap; 
       	}
	.menu li
	{
		 height:20px;
	     cursor:hand ;
	     margin-left:18px;	
	     margin-top:1px;
	     width:154px;
	     display: none;
	     
	}
	.menu li font
	{
	     width:130px;
	     overflow:hidden;                                                                                   
         text-overflow: ellipsis;
         white-space:nowrap;
	}
	.menu li img
	{
	  float:right;		    
	}
</style>
<script language="javascript" type="text/javascript">
	$(function(){
		var a=<%=listSchema.size()%>;
		var flat=<%=flat%>;
		var sname='<%=schename%>';
		var schenames='<%=schenames%>';
      if(a==0){
    	  document.getElementById("titleText").innerHTML="----请设置方案----";	
       }
      else{
         if(flat==0){
          document.getElementById("titleText").innerHTML=schenames;	
           }
         if(flat==1){
         document.getElementById("titleText").innerHTML=sname;		
          }
       }
      $(".menu li").first().css("list-style-image","url(<%=request.getContextPath()%>/plm/images/common/ok.png)");
	  $(".menu li").first().siblings().css("list-style-image","none"); 
	});
	function show(){
		var dis=$(".menu li").css("display");
		if(dis=="none"){
			   $(".indexMenu img").first().attr("src","<%=request.getContextPath()%>/plm/images/common/move_up.gif");
			   $(".menu li").css("display","block");
			   $(".menu").css("display","block");
		}else{
				$(".indexMenu img").first().attr("src","<%=request.getContextPath()%>/plm/images/common/move_down.gif");
				$(".menu li").css("display","none");
				$(".menu").css("display","none");
		}
	}
</script>
</head>

<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr class="AvidmNavigator">
			<td colspan="2">
				<jsp:include page="/plm/common/navigator.jsp">
					<jsp:param name="FLAG" value="NAVIGATE_TO_HOME"/>
				</jsp:include>
			</td>
		</tr>
  <tr>
    <td class="twoTitleOnLink">
    <div class="name">统计方案</div>
	<div style="position:relative;z-index:10000;float:left;">
	    <div class="indexMenu" id="indexMenu" onClick="show()"><span id="titleText" style="float:left;font-size:13px;margin-left:20px;"></span><img src="<%=request.getContextPath()%>/plm/images/common/move_down.gif" /></div>
	    <div  style="position:absolute; left:0; top:24px; z-index:10000">
	        <ul id="menu" class="menu" style="z-index:10000;display:none;">
	         <%=str_ul %>
	        </ul>
	    </div>
	</div>
	</td>
	<td style="width:200px;background:#dbe5f1;text-align:right; padding-top:5px">
	 	<div  class="pt-formbutton" text="保存方案" id="saveForm" onclick="saveForm()"></div>
		<div  class="pt-formbutton" text="查询"  id="closebutton" onclick="querySubmit()"></div>
	</td>
  </tr>
   <TR>
	<TD class="splitButton" colspan="2"> 
		<img id="splitimage" onClick="setVisible(this,'searchConditionTD')" src="<%=request.getContextPath()%>/plm/images/common/splitTop.gif"/> 
	</TD>
  </TR>
  <tr id="searchConditionTD">
	  <td colspan=2>
	  	<iframe id="iframKeysTab" frameborder="1"  name="iframKeysTab"  width="100%" height="250"  src="queryKeysTable.jsp"></iframe>
	  </td>
  </tr>
</table>
<table width="100%">
	<tr>
		<td align="center">
			<iframe  name="chartFrame" frameborder="0" style="margin-top: 1px;border:1px;" width="1000" height="450" src=""></iframe>
		</td>
	</tr>
</table>

<form id="formData" action="barChartView.jsp" target="chartFrame" method="post">
</form>
</body>
</html>
<script type="text/javascript">
var mainFrom = pt.ui.get("mainFrom");
var formData="";
var showType="";
var data="";
var isSelected="<%=isSelected%>";
var cid="<%=classid%>";
var innerid="<%=innerid%>";
var selectValue="<%=selectValue%>";
var iframes = window.frames;

function hideKeys(){
	$("#tabKeys").css("display","none");
} 
function showKeys(){
	$("#clickShow").css("display","none");
	$("#tabKeys").css("display","block");
}
function  querySubmit(){
	var objs  = window.frames["iframKeysTab"].getValue();
	if(objs.activeModelName == "" || objs.activeModelName == null){
		alert("请选择文件夹！");
		return;
	}
	var sttime = objs.dateTo;
	var edtime = objs.dateEnd;
	if (sttime != "") {
		if (edtime != "" && sttime > edtime) {
			alert("开始时间应小于结束时间！");
			return;
		}
	}
	$("#searchConditionTD").css("display","none");
	$("#indexMenu").css("enable","true");
    $("#splitimage").attr("src",contextPath+"/plm/images/common/splitBottom.gif");
	window.frames["iframKeysTab"].querySubmit();
}
function  saveForm(){
	var objs  = window.frames["iframKeysTab"].getValue();
	if(objs.activeModelName == "" || objs.activeModelName == null){
		alert("请选择文件夹！");
		return;
	}
	var sttime = objs.dateTo;
	var edtime = objs.dateEnd;
	if (sttime != "") {
		if (edtime != "" && sttime > edtime) {
			alert("开始时间应小于结束时间！");
			return;
		}
	}
  var a= document.getElementById("titleText").innerHTML;
  window.frames["iframKeysTab"].saveForm(a);
}
function refreshUl(){
	$.ajax({
		url:"<%=request.getContextPath()%>/adm/query/getSchemaTypeList.jsp",
		dataType: "json",
		type:  "post",
		success:function(result){
			result = eval(result);
			plm.showMessage({
		         title   : "提示",
			     message : "处理成功",
			     icon 	 : "2"
			 });
			var ul=$("#menu");
			$("#menu li").remove();
			if(result.length>0){
				for(var i=0;i<result.length;i++){
					var str_li = "<li id='l1' cid="+result[i].classid+" innerid='"+result[i].value+"' title='"+result[i].text+"'><font style='float:left;' size='2px'>"+result[i].text+"</font><img src='<%=request.getContextPath() %>/plm/images/common/delete.gif'  title='删除' width='12' height='12'/></li>";
					ul.append(str_li);
				}
				document.getElementById("titleText").innerHTML="----请选择----";
			}else{
				document.getElementById("titleText").innerHTML="----请设置方案----";
			}
			$(".menu li").css("display","none");
			$(".menu").css("display","none");
		},
		error:function(){
			plm.showMessage({
		         title   : "提示", 
			     message : "对不起，处理失败",
			     icon 	 : "1"
			 });
		}
	});
}


$(function(){
	$(".menu li>img").live('click',function(){
		var  inId=$(this).parent().attr("innerid");
		var  schemaData=[{"OID":"ActiveQuerySchema:"+inId}];
		plm.deleteObjects(schemaData,"refreshUl");
	
	});
$('.menu li>font').live('click',function(){
		$(this).parent().css("list-style-image","url(<%=request.getContextPath()%>/plm/images/common/ok.png)");
		$(this).parent().siblings().css("list-style-image","none");
		$(".menu li").css("display","none");
		$(".menu").css("display","none");
 		document.getElementById("titleText").innerHTML=$(this).text();
		var cid = $(this).parent().attr("cid");
		var inid = $(this).parent().attr("innerid");
		var url = "queryKeysTable.jsp?<%=KeyS.CLASSID%>="+cid+"&<%=KeyS.INNERID%>="+inid;
		document.getElementById("iframKeysTab").src=url;
	});
});

  
</script>