<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.grid.GridUtil"%>
<%@page import="com.bjsasc.plm.core.system.principal.User"%>
<%@page import="com.bjsasc.plm.core.context.ContextHelper"%>
<%@page import="com.cascc.platform.aa.api.util.AADomainUtil"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.bjsasc.plm.operate.ModelUtil"%>
<%@page import="com.bjsasc.plm.core.session.SessionHelper"%>
<%@page import="com.cascc.avidm.login.model.PersonModel"%>
<%@page language="java"%>
<%@page session="true"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@ page import="java.util.Map"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.KeyS" %>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.type.restrict.select.Select"%>
<%@page import="com.bjsasc.plm.type.restrict.select.SelectOption"%>
<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.core.folder.*" %>
<%@page import="com.bjsasc.plm.core.persist.PersistHelper"%>
<%@page import="com.bjsasc.plm.common.search.ViewProviderManager" %> 
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="java.util.*"%>
<%@page import="com.cascc.avidm.util.SplitString"%>
<%@page import="com.bjsasc.adm.common.ActiveInitParameter"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String title = "统计与输出";
	String contextPath = request.getContextPath();
	String url = request.getContextPath() + "/adm/active/ActiveStatisticsHandle!getComModelOids.action";
	//获得类型上的属性列表
	//String url_getClassAttrList=request.getContextPath()+"/platform/objectmodel/classattr/action/ClassAttrHandler.jsp?op=getClassAttrList&currInstIId="+currInstIId+"&currClassId="+currClassId;
	
	/* String oid = request.getParameter("oid");
	String classId = DistributeObject.CLASSID;
	String spot = "distributeCommonSearch";
	String gridId = "distributeCommonSearchtGrid";
	String toolbarId = "ddm.commonsearch.list.toolbar"; */
	//文件夹参数
	//Map<String,String> map = AMService.getCreateActiveDocumentParam(requestFolderOid);
	//类型
	String classId = "ActiveDocument";
	
%>	
<html>
<head>
	<title><%=title%></title>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
	<script type="text/javascript" src="<%=contextPath%>/plm/ui/editor/date/WdatePicker.js"></script>
	<script type="text/javascript"	src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
	<script type="text/javascript"	src="<%=contextPath%>/adm/javascript/active.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/plm/javascript/messager.js"></script>
<style>
.noTable{border:0px; border-collapse:collapse; font-size:12px;}
.noTable td{border:0px; padding-left:0px; height:25px; line-height:25px;}
</style>
</head>
<body>
<form name="searchForm" id="searchForm">
	<table width="100%"  cellSpacing="0" cellPadding="0" border="0">
		<input type="hidden" name="CLASSID" id="CLASSID" value="<%=classId %>"/>
		<input type="hidden" name="MODELOIDS" id="MODELOIDS"/>
		<tr><td>
		<table width="100%" class="avidmTable" border="0" cellspacing="0" cellpadding="0">
			<TR>
				<TD class="left_col AvidmW150"><span>*</span>文件夹：</TD>
				<TD>
					<table class="noTable">
					<tr><td>
						<input type='hidden' id="activeDocumentId" name="activeDocumentId"/>
						<input type='text' id="activeDocumentName" name="activeDocumentName" class='pt-textbox AvidmW270' 
							 title="选择文件夹" readonly="readonly" ontrigger="selectActiveDocument()"/>
						</td>
						<td>
						<a href="#" onclick="clearValue('activeDocumentId','activeDocumentName');">
							<img src="<%=contextPath%>/plm/images/common/clear.gif"></a>
						</td>
					</tr>
					</table>
				</TD>
			</TR>
			<TR>
				<TD class="left_col AvidmW150">附件：</TD>
				<TD>
					<table class="noTable">
					<tr><td>
							<input type="radio" name="files" id="files" value="0" checked="true"  />有原文&nbsp;&nbsp;
						</td>
						<td>&nbsp;&nbsp;
							<input type="radio" name="files" id="files" value="1" />无原文
						</td>
						<td>&nbsp;&nbsp;
							<input type="radio" name="files" id="files" value="2" />不确定
						</td>
					</tr>
					</table>
				</TD>
			</TR>
		<tr>
		<td class='left_col AvidmW150'><span>*</span>统计字段：
			<!--<div class="pt-formbutton" text="选择字段" onclick="getAttr('0');"></div>-->
			<a href="#" onclick="getAttr('0');">
			<img src="<%=contextPath%>/adm/images/activedocument/multi_add.gif"></a>
		</td>
		<td>
			<input type = "hidden" id = "attrlength" name = "attrlength"/>
		  <div id="div1">
			<table id="statistics" class="noTable" height="5%" cellSpacing="0" cellPadding="0">
			</table>
		 </div>
		</td></tr>
		<tr>
		<td class='left_col AvidmW150'>查询条件：
			<a href="#" onclick="getAttr('1');">
			<img src="<%=contextPath%>/adm/images/activedocument/multi_add.gif"></a>
		</td>
		<td>
			<input type = "hidden" id = "selectAttrlength" name = "selectAttrlength"/>
		  <div id="div2">
			<table id="selectAttr" class="noTable">
			</table>
		  </div>	
		</td></tr>
		<tr>
		<td class='left_col AvidmW150'><span>*</span>统计条件：
			<a href="#" onclick="getAttr('2');">
			<img src="<%=contextPath%>/adm/images/activedocument/multi_add.gif"></a>
	    </td>
		<td>
			<input type = "hidden" id = "statisticsAttrlength" name = "statisticsAttrlength"/>
		<div id="div3">
			<table id="statisticsAttr" class="noTable">
			</table>
		</div>	
		</td></tr>
		<tr>
			<td class="exegesis AvidmW150"><span>*</span> 为必选/必填项</td>
			<!--<table width="100%" class="avidmTable" border="0" cellspacing="0" cellpadding="0">
				<TR>
				<td class="exegesis AvidmW150"><span>*</span> 为必选/必填项</td>-->
				<td style="height:40px">
					<div class="pt-formbutton" text="搜索" id="submitbutton" onclick="searchAll();"></div>
					&nbsp;&nbsp;&nbsp;
				   <div class="pt-formbutton" text="打印" id="submitbutton" onclick="print();"></div>
				</td>
		</tr>
			</table>
		</td></tr>
	</table>
	<table id="tableList" width="100%" class="avidmTable" border="0" cellspacing="0" cellpadding="0">
		
	</table>
</form>
<form id="download" name="download" method="POST">
</form>
<script type="text/javascript">
    var at=0;
    var se=0;
    var st=0;
	$(document).ready(function() {
		var classId = "<%=classId%>";
		//getfield(classId);
	});
	var container = {};
	var attrID = [];
	var optNAME = [];
	var optSelValues = "";
	var pt_text='class=pt-textbox AvidmW150';
	
	var pt_date_onclick = "WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'2001-01-01',maxDate:'2128-03-10'})";
	
	var pt_date="class='Wdate pt-textbox' readonly='readonly'  style='width:100px;' onclick= "
			+'"'+pt_date_onclick+'"';
	var t="";
	function searchAll(){
		if($("#activeDocumentName").val() == "" || $("#activeDocumentName").val() == null){
			alert("请选择文件夹！");
			return;
		}
		var tablestatistics=document.getElementById("statistics");
		var rows=tablestatistics.rows.length;
		var tableAttr=document.getElementById("statisticsAttr");
		var rowsAttr=tableAttr.rows.length;
       if(rows==0){
			alert("请选择统计字段！");
		    return;
	   }
	    if(rowsAttr==0){
			 alert("请选择统计条件！");
			 return;
		}
		var url = "<%=contextPath%>/adm/active/ActiveStatisticsHandle!getListStatistics.action";
		plm.startWait();
		$.ajax({
			url:url,
			type:"post",
			dataType:"json",
			data:$("#searchForm").serializeArray(),
			success:function(result){
				plm.endWait();
				if(result.SUCCESS != null &&result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				var tableId = document.getElementById("tableList");
				tableId.firstChild.removeNode(true);
           for(var m=result.total-1;m<result.total;m++ ){
	            var optid=result.rows[m]["OPTID"];
	           var optname=result.rows[m]["OPTNAME"]; 
             }
             var attrID = optid.split(",");
			 var  optNAME = optname.split(",");
				var attrIds = attrID;
				var optNames = optNAME;
            for(var t=optNames.length-2;t<optNames.length-1;t++){
             if(optNames[t]=="1"){
              alert("你选择的统计条件含有相同的数据，请重新选择");
            document.getElementById("tableList").style.display = "none"
        }else{
         document.getElementById("tableList").style.display = "block"
        }
            
            }
				if(optNames.length>0){
					var tr = "";
					tr += "<tr><td class='left_col AvidmW150'>统计字段</td>";
					for(var m = 0; m < optNames.length-2; m++){
						tr += "<td class='left_col AvidmW150'>"+optNames[m]+"</td>";
					}
					tr += "</tr>";
					$("#tableList").append(tr);
					for(var i = 0; i < result.total-1; i++){
						var listTr = "<tr>";
						listTr += "<td>" + result.rows[i]["NAME"] + "</td>";
						for(var j = 0; j < attrIds.length; j++){
							//字符串中小写字母转换成大写
							var id = attrIds[j].toUpperCase();
							var value = "";
							//for(var n = 0; n < length; n++){
								//var pages = "document.forms[0].opt_"+attrIds[j]+"["+i+"].value";
								if($("#opt_"+attrIds[j]).val() == 0){
									value = "'COUNT(";
								}else{
									value = "'SUM(";
								}
								var opt = value + id;
								var optValue = eval("result.rows["+i+"]["+opt+")']");
								if(optValue != undefined){
									//var length = document.forms[0].attrIds[j].length;
									var length = eval("document.forms[0].opt_"+attrIds[j]+".length");
									for(var n = 0; n < length; n++){
										var pages = eval("document.forms[0].opt_"+attrIds[j]+"["+n+"].value");
										if(pages == 0){
											var countId = eval("result.rows["+i+"]['COUNT("+id+")']");
											if(countId != undefined){
												listTr += "<td>" + countId + "</td>";
											}
										}else{
											var sumId = eval("result.rows["+i+"]['SUM("+id+")']");
											if(sumId != undefined){
												listTr += "<td>" + sumId + "</td>";
											}
										}
									}
								}
							//}
						}
						listTr += "</tr>";
						$("#tableList").append(listTr);
					}
				}
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"操作失败！", icon:'1'});
			}
		});
	}
	
	function selectActiveDocument(){
		var url = '<%=contextPath%>/adm/admstatistics/listFolderTree.jsp?OID=<%=ActiveInitParameter.getActiveCabinetOid()%>';
		var retObj = adm.tools.showModalDialog(url);
		if (retObj!=undefined) {
		 $("#activeDocumentId").val(retObj["oids"]);
		 $("#activeDocumentName").val(retObj["names"]);
		}
	}
	
	function selectModel(){
		//	var url = '<%=contextPath%>/plm/type/visitTypeTree.jsp?<%=KeyS.CALLBACK%>=setActiveDocument&spot=ActiveDocument&single=true';
		//	plm.openWindow(url,500,400,"ActiveDocument");
		//plm.selectTypes({callback:"setActiveDocument",spot:'ActiveDocument',single:'true'});
		//plm.selectTypes({callback:"setActiveDocument",spot:'ActiveDocument',single:'true'});
	}
	
	function setActiveDocument(records) {
		var dataList = UI.JSON.decode(records);
	    if(typeof(dataList) != "undefined"){
	        var name = dataList[0].Name;
			
	    }
	}
	
	//改变模型时，动态获取现行文件关属性
	function initActiveDocumentAttr(object){
		var classId = $("#CLASSIDS").val();
		if(object == undefined) { // 页面首次加载
			if(classId == "") {
				classId = $("#CLASSIDS").val(); // 从用户的最近访问获取
				if(classId == "" || classId == "more" || classId =="nothing") {
					classId = "ActiveDocument";
				}
			}
		} else {
			var selectClassId = object.options[object.selectedIndex].value;
			if(selectClassId == "more"){
				openSelectPage();
				return;
			}else if(selectClassId == "nothing"){
				return;
			}else{
				classId = selectClassId;
				$("#CLASSIDS").val(classId);
				$("#CLASSID").val(classId);
			}
		}
	    getfield(classId); //更新扩展属性域
	}

	// 现行文件类型扩展属性
	function getfield(classId){
		plm.getExtAttr({trid:'includeDynamicFields',colnum:1,classid:classId,viewId:""});
	}
	
	// 现行文件类型选择，可以配置是否包含根节点
	function openSelectPage(){
		plm.selectTypes({callback:"doCreateType",spot:'ActiveDocument',single:'true'});
	};

	//现行文件类型选择回调函数
	function doCreateType(result){
		var dataList = UI.JSON.decode(result);
	    if(typeof(dataList) != "undefined"){
	        var name = dataList[0].Name;
	        var id = dataList[0].id;
	        var object = document.getElementById("CLASSIDS");
	        var op=document.createElement('option');
	        var len = object.options.length;
	         
	        op.text = name;
	        op.value = id;
	        for(var i = 0;i < len; i++ ){
	        	if(object.options[i].value == id){
	        		object.options[i].selected = true;
	        		break;
	        	}else if(i == len-1){
	        		object.add(op);
	                op.selected = true;
	        	}
	        }
	        initActiveDocumentAttr(object);
	    }
	}
	
	function getAttr(value){
		if($("#activeDocumentName").val() == "" || $("#activeDocumentName").val() == null){
			alert("请选择文件夹！");
			return;
		}
		var classId = $("#CLASSID").val();
		if(classId == null || classId == ""){
			classId = "ActiveDocument";
		}
		var oids = $("#activeDocumentId").val();
		$.ajax({
			url:"<%=url%>",
			type:"post",
			dataType:"json",
			data:"<%=KeyS.OIDS%>="+oids,
			success:function(result){
				plm.endWait();
				if(result.SUCCESS != null &&result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				if(value == "0"){
					getAttrList(result.MODELOIDS);
				}else if(value == "1"){
					getSelectAttrList(result.MODELOIDS);
				}else if(value == "2"){
					getStatisticsAttrList(result.MODELOIDS);
				}else{
					
				}
				$("#MODELOIDS").val(result.MODELOIDS);
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'错误提示', message:"获取属性失败！", icon:'1'});
			}
		});
	}
	
	function getAttrList(value){
		var url = '<%=contextPath%>/adm/admstatistics/listModelAttr.jsp?oids='+value;
		//plm.openWindow(url,200,400,"ActiveDocument");
		var retObj = adm.tools.showModalDialog(url);
	 if (retObj!=undefined) {
		var oids = retObj["oids"];
		var attrIds = retObj["attrIds"];
		var attrNames = retObj["attrNames"];
		var oid = oids.split(",");
		var id = attrIds.split(",");
		var name = attrNames.split(",");
		var tr = "";
		for(var i = 0; i < oid.length; i++){
        if(name[i]=='创建时间'||name[i]=='修改时间' || name[i]=='文件编制时间'){
       	t=pt_date;
        }else{
         t=pt_text;
       }
			tr += "<tr><td class='left_col AvidmW100'>" + name[i] + "：</td>";
			tr += "<td><input type='text' name='st_" + id[i] + "'  "+t+"/>"
			tr += "&nbsp;&nbsp;<img src='<%=contextPath%>/adm/images/activedocument/delete.gif' onclick='delRow(this,1)'></td>";
			tr += "<input type='hidden' id = 'attrOid' value = '" + oid[i] + "'/></tr>";
		}
		$("#attrlength").val(oid.length);
		$("#statistics").append(tr);
		at=at+oid.length;
		
	  }
	   if(at>=3){
		 	$("#div1").css({"position":"relative","height":"80px","overflow":"auto"});
	   }
		//alert(document.getElementsByName("st_note").length);
	}
	
	function getSelectAttrList(value){
		var url = '<%=contextPath%>/adm/admstatistics/listModelAttr.jsp?oids='+value;
		var retObj = adm.tools.showModalDialog(url);
		if (retObj!=undefined) {
		var oids = retObj["oids"];
		var attrIds = retObj["attrIds"];
		var attrNames = retObj["attrNames"];
		var dataTypes = retObj["dataTypes"];
		var oid = oids.split(",");
		var id = attrIds.split(",");
		var name = attrNames.split(",");
		var dataType = dataTypes.split(",");
		var tr = "";
		for(var i = 0; i < oid.length; i++){
			 if(name[i]=='创建时间'||name[i]=='修改时间'||name[i]=='文件编制时间'){
			       	t=pt_date;
			        }else{
			         t=pt_text;
			       }
			
			tr += "<tr>";
			tr += "	<td class='left_col AvidmW100'>" + name[i] + "：</td>";
			tr += "	<td>&nbsp;";
			tr += "		<select name='sel_" + id[i] + "' id='sel_" + id[i] + "' class='pt-select'>";
			tr += "			<option value='0' selected = 'selected'>等于</option>";
			if(dataType[i] == "String"){
				tr += "			<option value='1'>包含</option>";
			} else {
				tr += "			<option value='2'>小于等于</option>";
				tr += "			<option value='3'>大于等于</option>";
			}
			tr += "</select>&nbsp;";
			tr += "	</td>";
			tr += "	<td><input type='text' id='selContext_" + id[i] + "' name='selContext_" + id[i] + "' "+t+"/></td>";
			tr += "	<td><div id='divAndOr' name='divAndOr'>&nbsp;";
			tr += "		<select name='andOr_" + id[i] + "' id='andOr_" + id[i] + "' class='pt-select'>";
			tr += "			<option value='0' selected = 'selected'>并且</option>";
			tr += "			<option value='1'>或者</option>";
			tr += "		</select>&nbsp;</div>";
			tr += "	</td>";
			tr += "	<td><img src='<%=contextPath%>/adm/images/activedocument/delete.gif' onclick='delRow(this,2)'></td>";
			tr += "	<input type='hidden' id = 'attrOid" + i + "' value = '" + oid[i] + "'/>";
			tr += "</tr>";
		/*	tr += "<tr><td class='left_col AvidmW100'>" + name[i] + "：</td>";
			tr += "<td>&nbsp;<select name='selWhere"+i+"' id='selWhere"+i+"' class='pt-select'>";
			tr += "<option value='0' selected = 'selected'>等于</option>";
			if(dataType[i] == "字符串型"){
				tr += "<option value='1'>包含</option>";
			}else{
				tr += "<option value='2'>小于等于</option>";
				tr += "<option value='3'>大于等于</option>";
				tr += "</select>";
			}
			tr += "</td><td>&nbsp;<input type='text' id='" + id[i] + "' class='pt-textbox AvidmW150'/>&nbsp;&nbsp;";
			//if(i != oid.length - 1){
				tr += "<div id='divAndOr'><select name='blur"+i+"' id='blur"+i+"' class='pt-select'><option value='0' selected = 'selected'>并且</option><option value='1'>或者</option></select></div>"
			//	}
			tr += "</td><td>&nbsp;&nbsp;<img src='<%=contextPath%>/adm/images/activedocument/delete.gif' onclick='delRow(this,2)'></td>";
			
			tr += "<input type='hidden' id = 'attrOid" + i + "' value = '" + oid[i] + "'/></tr>";
			*/
			
		}
		$("#selectAttrlength").val(oid.length);
		//alert(tr);
		$("#selectAttr").append(tr);
		se=se+oid.length;
		//divs[divs.length -1].style.display = "none"
		//$("#divAndOr").hide();
		}
		if(se>=3){
		 	$("#div2").css({"position":"relative","height":"80px","overflow":"auto"});
	   }

	}
	
function getStatisticsAttrList(value){
		var url = '<%=contextPath%>/adm/admstatistics/listModelAttr.jsp?oids='+value;
		var retObj = adm.tools.showModalDialog(url);
	if (retObj!=undefined) {
		var oids = retObj["oids"];
		var attrIds = retObj["attrIds"];
		var attrNames = retObj["attrNames"];
		var dataTypes = retObj["dataTypes"];
		var oid = oids.split(",");
		var id = attrIds.split(",");
		var name = attrNames.split(",");
		var dataType = dataTypes.split(",");
		var tr = "";
		for(var i = 0; i < oid.length; i++){
			tr += "<tr><td class='left_col AvidmW100'>" + name[i] + "：<input type='hidden' name='opt_name_"+id[i]+"' value='"+name[i]+"'></td>";
			tr += "<td><select name='opt_"+id[i]+"' id='opt_"+id[i]+"' class='pt-select'><option value='0' selected = 'selected'>记数</option>";
			if(dataType[i] == "long"){
				tr += "<option value='1'>求和</option>"
			}
			tr += "</select>&nbsp;&nbsp;<img src='<%=contextPath%>/adm/images/activedocument/delete.gif' onclick='delRow(this,3)'></td>";
			tr += "</td>";
			tr += "<input type='hidden' id = 'attrOid' value = '" + oid[i] + "'/></tr>";
			optSelValues += "opt_"+id[i]+",";
		}
		$("#statisticsAttrlength").val(oid.length);
		$("#statisticsAttr").append(tr);
		st=st+oid.length;
  }
	if(st>=3){
	 	$("#div3").css({"position":"relative","height":"80px","overflow":"auto"});
   }

	}
	
	function clearValue(value1,value2){
		$("#"+value1).val("");
		$("#"+value2).val("");
	}
	
	function consClear(){
		$("#activeDocumentId").val("");
		$("#activeDocumentName").val("");
	}
	
	function delRow(obj,value){
		if(value==1){
			at=at-1;
			if(at<=3){
				$("#div1").removeAttr("style");
			}
		}if (value==2){
			se=se-1;
			if(se<=3){
				$("#div2").removeAttr("style");
			}
		}if (value==3){
			st=st-1;
			if(st<=3){
				$("#div3").removeAttr("style");
			}
		}
		//参数为对象
		var row = obj.parentNode.parentNode;
		//按钮所在行
		var tb = row.parentNode;
		//所在行下标
		var rowIndex = row.rowIndex;
		//删除当前行
		tb.deleteRow(rowIndex);
		
		var divs = document.getElementsByName("divAndOr");
		for (var i=0; i<divs.length; i++) {
			divs[i].style.display = "block";
		}
		//divs[divs.length -1].style.display = "none"
	//	$("#divAndOr").hide();
	}
	function print(){
    	var url = "<%=contextPath%>/adm/active/ActiveStatisticsHandle!dataExport.action";
    	$("#download").attr("action", url);
		$("#download").submit();	
    }
	</script>
</body>
</html>