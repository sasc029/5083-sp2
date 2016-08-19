<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.bjsasc.adm.active.model.query.ActiveQuerySchema"%>
<%@page import="java.util.List"%>
<%@page import="com.bjsasc.adm.active.helper.AdmHelper"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="java.util.Date"%>
<%@page import="com.bjsasc.adm.common.ActiveInitParameter"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="GBK"%>
<%	
    String contextPath = request.getContextPath();
	String classid = request.getParameter(KeyS.CLASSID);
	if(classid==null){
		classid="doc,";
	}
	String innerid=request.getParameter(KeyS.INNERID); 
	//得到当前时间为开始时间，并通过字符串的处理得到默认的结束时间为开始时间的两个月后
	Date d=new Date();
	SimpleDateFormat myFmt1=new SimpleDateFormat("yyyy-MM-dd"); 
	String now=myFmt1.format(d);
	String [] beginTimes=now.split("-");
	String beginTime="";
	String group="";
	String activemodelid="";
	String activemodelname="";
	int defaultSchema=0;
	String schename="";
	int bYear=0;
	int bMonth=0;
	bYear=Integer.parseInt(beginTimes[0]);
	bMonth=Integer.parseInt(beginTimes[1]);
	if(bMonth<3){
		bYear-=1;
		bMonth=12-(2-bMonth);
		beginTime=bYear+"-"+bMonth+"-01";
	}else{
		bMonth-=2;
		if(bMonth>9){
			beginTime=bYear+"-"+bMonth+"-01";
		}else{
			beginTime=bYear+"-0"+bMonth+"-01";
		}
	}
	if(innerid!=null){
	List<ActiveQuerySchema> listSchema = AdmHelper.getActiveChangeService().findActiveQuerySchemaList(innerid);
	ActiveQuerySchema  schema=listSchema.get(0);
	 beginTime=schema.getBeginTime();
	 now=schema.getEndTime();
	 activemodelid=schema.getActiveModelId();
     group=schema.getGroupCondition();
     activemodelname=schema.getActiveModelname();
     defaultSchema=schema.getDefaultSchema();
     schename=schema.getSchemaName();
	}
	else{
	//默认方案ID
	//方案标题是否显示选中
	//得到所有方案列表
	List<ActiveQuerySchema> listSchema = AdmHelper.getActiveChangeService().getActiveQuerySchemaList();
	List<Map<String, String>> listSchemaMap = new ArrayList<Map<String, String>>();
	
	int flat=0;
	//循环遍历得到所有方案的各个值 并动态生赋值成下拉列表
	 for (ActiveQuerySchema schema : listSchema) {
		int	ds=schema.getDefaultSchema();
			if(ds==1){
			      flat=1;
			     classid=schema.getActivename();
			     beginTime=schema.getBeginTime();
				 now=schema.getEndTime();
				 activemodelid=schema.getActiveModelId();
			     group=schema.getGroupCondition();
			     activemodelname=schema.getActiveModelname(); 
			     innerid=schema.getInnerId();
			     defaultSchema=schema.getDefaultSchema();
			     schename=schema.getSchemaName();
			     
			}
	 }
	if(flat==0){
		if(listSchema.size()>0){
		ActiveQuerySchema  schema=listSchema.get(0);
		 innerid=schema.getInnerId();
		 classid=schema.getActivename();
		 beginTime=schema.getBeginTime();
		 now=schema.getEndTime();
		 activemodelid=schema.getActiveModelId();
	     group=schema.getGroupCondition();
	     activemodelname=schema.getActiveModelname();
	     defaultSchema=schema.getDefaultSchema();
	     schename=schema.getSchemaName();
		}
	}
	}
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/platform/common/js/ptutil.js"	charset="GBK"></script>
<script type="text/javascript" src="<%=contextPath%>/plm/ui/editor/date/WdatePicker.js"></script>
<title>变更统计</title>
</head>
<body scroll=no>
<form ui="form" id="mainFrom">
<table  width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
	<tr>
		  <td class="left_col AvidmW150">现行对象：</td>
		  <td>
		  <input type="hidden" name="activename" id="activename" value="">
		  <input type="hidden" name="changeClassText" id="changeClassText" value="">
		    <SELECT name='CHANGECLASSTYPE' id='CHANGECLASSTYPE' style='float:left;' width='154'  class='pt-multicombo' onSelect="refreshByClassId()">
		       <option value="doc">现行文档</option>
		       <!--<option value="set">现行套图</option>-->
		       <option value="order">现行单据</option> 
		     </SELECT>
		  </td>
		<TD class="left_col AvidmW150"><span>*</span>文件夹：</TD>
				<TD>
						<input type='hidden' id="activeModelId" name="activeModelId"/>
						<input type='text' id="activeModelName" name="activeModelName" class='pt-textbox AvidmW270' 
							 title="选择文件夹" readonly="readonly" ontrigger="selectActiveDocument()"/>
		</TD>
	</tr>
	<tr>
	 <td class='left_col AvidmW150'>开始时间：</td>
	  <TD>
							<INPUT type='text' id="queryCreateDateTo" name="queryCreateDateTo" 
								class="Wdate pt-textbox" readonly="readonly"  style="width:100px;"  value=<%=beginTime%> 
				        		onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'2001-01-01',maxDate:'2128-03-10'})"/>			
	</TD>
	  <td class='left_col AvidmW150'>结束时间：</td>
	 <TD>
							<INPUT type='text' id="queryCreateDateEnd" name="queryCreateDateEnd" 
								class="Wdate pt-textbox" readonly="readonly"  style="width:100px;"  value=<%=now%> 
				        		onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'2001-01-01',maxDate:'2128-03-10'})" />			
	</TD>
	</tr>
	<tr>
	    <td class="left_col AvidmW150">展现方式：</td>
	    <td width="330px">
	    		<SELECT  name='SHOWDATE' id='SHOWDATE' style='width:153px;' class='pt-select'>
					<option value="YEAR">年</option>
					<option value="QUARTER">季度</option>
					<option value="MONTH">月</option>
				</SELECT></td>
	</tr>
</table>
</form>
</body>
</html>
  <script type="text/javascript">
   var schemaname='<%=schename%>';
   var  chked='<%=defaultSchema%>';
   $(function(){
		  var anames='<%=classid%>';
		  var begintime='<%=beginTime%>';
		  var endtime='<%=now%>';
		  var activemodelid="<%=activemodelid%>";
		  var group='<%=group%>';
		  var activemodelname='<%=activemodelname%>';
		  pt.ui.get("CHANGECLASSTYPE").setValue(anames);
		   var gridChangeType = pt.ui.get("CHANGECLASSTYPE");
	       var gridChangeType=gridChangeType.text;
	      $("#changeClassText").val(gridChangeType);
		  $("#activename").val(anames);
		  $("#queryCreateDateTo").val(begintime);
		  $("#queryCreateDateEnd").val(endtime);
		  $("#activeModelId").val(activemodelid);
		  if(activemodelname=="null"){
			   $("#activeModelName").val("");
		}else{
		  $("#activeModelName").val(activemodelname);
		}
		  var dateTypes =document.getElementById("SHOWDATE").options;
			for(var k=0;k<dateTypes.length;k++){
				if(dateTypes[k].value==group){
					dateTypes[k].selected="selected";
				}	
	  }
	});
	
        function selectActiveDocument(){
		    var url = '<%=contextPath%>/adm/admstatistics/listFolderTree.jsp?OID=<%=ActiveInitParameter.getActiveCabinetOid()%>';
		    var retObj = adm.tools.showModalDialog(url);
		  if (retObj!=undefined) {
		    $("#activeModelId").val(retObj["oids"]);
		    $("#activeModelName").val(retObj["names"]);
		   }

	     }
        
        function getValue(){
        	var objs ={};
        	objs.activeModelName = $("#activeModelName").val();
        	objs.dateTo = $("#queryCreateDateTo").val();
        	objs.dateEnd = $("#queryCreateDateEnd").val();
        	return objs;
	     }
        
		//现行文件类型选择回调函数
		function doCreateType(result){
			var dataList = UI.JSON.decode(result);
	    	var names="";
	    	var ids="";
		    if(typeof(dataList) != "undefined"){
            for(var i=0;i<dataList.length;i++){
            	names+=dataList[i].Name+",";
	            ids+="<'"+dataList[i].id+"'<,";
              }  
		  ids = ids.substring(0, ids.length - 1);
		  names = names.substring(0, names.length - 1);
		 $("#activeModelId").val(ids);
		 $("#activeModelName").val(names);
		  }
		}
	function refreshByClassId(){ 
		 var gridChangeType = pt.ui.get("CHANGECLASSTYPE");
	     var changCType = gridChangeType.getValue();
	     var changeClassText=gridChangeType.text;
	     $("#changeClassText").val(changeClassText);
	     $("#activename").val(changCType);
	}

	function querySubmit(){
		parent.$("#formData").empty();
		parent.document.getElementById("chartFrame").src="";
		var activename=$("#activename").val();
		var activeModelId=$("#activeModelId").val();
		var begintime=$("#queryCreateDateTo").val();
		var endtime=$("#queryCreateDateEnd").val();
	    var	changeClassText=$("#changeClassText").val();
		var gridShow = pt.ui.get("SHOWDATE");
		var showDate=gridShow.getValue();
		if(activename==""){
			changeClassText="现行文档";
			activename="doc";
		}
		parent.$("#formData").append("<input type='hidden' name='activename' value='"+activename+"'>");
		parent.$("#formData").append("<input type='hidden' name='activeModelId' value="+activeModelId+">");
		parent.$("#formData").append("<input type='hidden' name='begintime' value='"+begintime+"'>");
		parent.$("#formData").append("<input type='hidden' name='endtime' value='"+endtime+"'>");
		parent.$("#formData").append("<input type='hidden' name='showDate' value='"+showDate+"'>");
		parent.$("#formData").append("<input type='hidden' name='changeClassText' value='"+changeClassText+"'>");
		parent.$("#formData").submit();	
	}
	function saveForm(a){
	    var activename=$("#activename").val();
		var activeModelId=$("#activeModelId").val();
	    var activeModelName=$("#activeModelName").val();
		var begintime=$("#queryCreateDateTo").val();
		var endtime=$("#queryCreateDateEnd").val();
		var gridShow = pt.ui.get("SHOWDATE");
	    var showDate=gridShow.getValue();
	    var oid="ActiveQuerySchema"+":"+'<%=innerid%>';
	    if(activename==""){
	    	activename="doc";
	    }
	    if(a=='----请选择----'||a=='----请设置方案----'){
	    	a="";
	    	chked=0;
	    }if(a.indexOf("(默认)")>0){
	    	a=a.substring(0, a.indexOf("(默认)"));
	    }
	    var defaultName="";
		var width = 450;
		var height = 300;
		var left = (window.screen.width - width)/2;
		var top = (window.screen.height - height)/2;
		var param = "height="+height+",width="+width+",left="+left+",top="+top;
		formData={"ACTIVENAME":activename,"BEGINTIME":begintime,"ENDTIME":endtime,
        "SHOWDATE":showDate,"ACTIVEMODELID":activeModelId,"activeModelName":activeModelName,"DEFAULTNAME":a,"CHKED":chked,"OID":oid};
		        window.open("createQuerySchema.jsp","createSchema","menubar=no,toolbar=no,location=no,scrollbars=no,status=yes,directories=no,resizable=no,copyhistory=yes,"+param+"");
};
/**
 * 回掉函数  选中添加方案
 */
function refresh(title,is_default){
	  parent.document.getElementById("titleText").innerHTML=title;
	  schemaname=title;
	  check=is_default;
	 $.ajax({
			url:"<%=request.getContextPath()%>/adm/query/getSchemaTypeList.jsp",
			dataType: "json",
			type:  "post",
			success:function(result){
				result = eval(result);
				var ul=parent.$("#menu");
				parent.$("#menu li").remove();
				for(var i=0;i<result.length;i++){
					var str_li = "<li id='l1' cid="+result[i].classid+" innerid='"+result[i].value+"' title='"+result[i].text+"'><font style='float:left;' size='2px'>"+result[i].text+"</font><img src='<%=request.getContextPath() %>/plm/images/common/delete.gif'  title='删除' width='12' height='12'/></li>";
					ul.append(str_li);
				}
				var lis=parent.$(".menu li");
				for(var j=0;j<lis.length+1;j++){
					var $li=parent.$("ul li:eq("+j+")");
					var txt=$li.text();
					if(txt==title||txt==title+"(默认)"){
					    setTimeout('settime()',2000);
						$li.css("list-style-image","url(<%=request.getContextPath()%>/plm/images/common/ok.png)");
						$li.siblings().css("list-style-image","none");
						parent.$(".menu li").css("display","none");
 						$li.attr("chk","1");
					}
				}
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
    function settime(){
	
	
    }
    

</script>
