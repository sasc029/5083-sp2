<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%
	//测试首页
	String testIndexPage = request.getContextPath()+"/platform/inspect/debugTools/PublicSelectTest.jsp";
%>
<html>
<head>
	<script type="text/javascript"	src="<%=request.getContextPath() %>/platform/ui/ui.js"	charset="UTF-8"></script>
	<script type="text/javascript"	src="<%=request.getContextPath() %>/platform/common/js/ptutil.js" charset="UTF-8"></script>
</head>
<body>	
<table width="100%">
	<tr>
	<form action="" id="selectConfig" ui="form"  >
		<table width="100%" style="margin-top: 10px;" class="pt-table" >
		<TR>
     	<TD colSpan=3 style='BORDER-BOTTOM: #aecaf0 1px solid; PADDING-BOTTOM: 5px; BACKGROUND-COLOR: #e0edff;  PADDING-LEFT: 6px; PADDING-RIGHT: 6px; 	 BACKGROUND-REPEAT: repeat-x; WHITE-SPACE: nowrap;  BACKGROUND-POSITION: left center; HEIGHT: 1%; VERTICAL-ALIGN: top; PADDING-TOP: 3px'  class='c'>
    	<SPAN style='FONT-WEIGHT: bold'>公共选择用户</SPAN></TD>
    	</TR>
		<tr>
			<td class='pt-label' style="text-align:center; width: 200px" >字段名 </td>
			<td class='pt-label' style="text-align:center;">可选值</td>
			<td class='pt-label' style="text-align:center;">意义 </td>
		</tr>
		<tr  align="center">
			<td class='pt-label' >SelectMode</td>
			<td class='pt-field'>
			<SELECT name='SelectMode' id='SelectMode' class='pt-select' style="width: 200px">
				<option value="multiple">multiple</option>
			    <option value="single">single</option>
			</SELECT>
			</td>
			<td class='pt-label' style="text-align:center;"	>选择模式，multiple多选（默认）和single单选</td>
		</tr>
		
		<tr align="center">
			<td class='pt-label' >IsModal</td>
			<td class='pt-field'>
			<SELECT name='IsModal' id='IsModal' class='pt-select' style="width: 200px">
				<option value="true">true</option>
			    <option value="false">false</option>
			</SELECT>
			</td>
			<td class='pt-label' style="text-align:center;"	>页面是否是模态</td>
		</tr>
		
		<tr align="center">
			<td class='pt-label' >returnType</td>
			<td class='pt-field'>
			<SELECT name='returnType' id='returnType' class='pt-select' style="width: 200px">
				<option value="arrayObj" selected="selected">arrayObj</option>
			    <option value="json">json</option>
			</SELECT>
			</td>
			<td class='pt-label' style="text-align:center;"	>返回数据类型arrayObj(数组对象,默认),json(json对象)</td>
		</tr>
		
		<tr align="center">
			<td class='pt-label' >scope</td>
			<td class='pt-field'>
			<SELECT name='scope' id='scope' class='pt-select' style="width: 200px">
				<option value="all" selected="selected">all</option>
			    <option value="self">self</option>
			    <option value="path">path</option>
			    <option value="bind">bind</option>
			</SELECT>
			</td>
			<td class='pt-label' style="text-align:center;"	>选择用户范围</td>
		</tr>
		
		<tr align="center">
			<td class='pt-label'>userStatus</td>
			<td class='pt-field'>
				<SELECT name='userStatus' id='userStatus' class='pt-select' style="width: 200px">
					<option value="A" selected="selected">A</option>
				    <option value="F">F</option>
				    <option value="T">T</option>
				    <option value="ALL">ALL</option>
				</SELECT>	
			</td>
			<td class='pt-label' style="text-align:center;"	>选择用户状态</td>
		</tr>
		<tr align="center">
			<td class='pt-label' id="id1">callbackFun</font></td>
			<td class='pt-field'>
			<INPUT ui='textField' type='text' name='callbackFun' 
				class='pt-textbox pt-validatebox ' id='callbackFun' required='true' 
				maxLength='20' value="callbackFun" style="width:200px;">
			</td>
			<td class='pt-label' style="text-align:center;"	>非模态方式，选择用户回调函数</td>
		</tr>
		
		<tr>
			<td class='pt-label' id="id1"  align="center">返回值</font></td>
			<td class='pt-field' colspan="2">
			<textarea ui='textarea' name='returnContent'
			class='pt-textarea  ' id='returnContent' style="height:130px;" cols="100"></textarea></td>
		</tr>
		</table>
	</form> 
	</tr>
	
</table>
<table width="100%" > 
	<tr align="center">
		<td colspan="6" align="center">
			<div class="pt-formbutton" text="打开 " iconCls="icon-ok"
						onclick="openSelectPage()"></div>
			<div class="pt-formbutton" id="clearForm" text="返回"" iconCls="icon-cancel"
						onclick="back()"></div>
		</td>
	</tr>
</table>
	
<script>
	//打开选择页面
	function openSelectPage(){
    	var SelectMode = document.getElementById("SelectMode").value;
    	var IsModal = $("#IsModal").val();
    	var callbackFun = $("#callbackFun").val();
    	var returnType = $("#returnType").val();
    	var scope = $("#scope").val();
    	var userStatus = $("#userStatus").val();
    	var paras = 
		{ 
			userStatus : userStatus,
			IsModal : IsModal,
			SelectMode : SelectMode,
			callbackFun : callbackFun,
			returnType : returnType,
			scope : scope
		};
    	if(IsModal == "true"){
    		var reObj =	pt.sa.tools.selectUser(paras);
    		if(reObj){
    			var showContent = getContent(reObj);
    			document.getElementById("returnContent").value = showContent;
    		}
    	}else{
    		pt.sa.tools.selectUser(paras);
        }
	}

	//回调函数
    function callbackFun (arrayObj) {
    	var showContent = getContent(arrayObj);
    	document.getElementById("returnContent").value = showContent;
	}
	//reObj 为返回数据，具体数据类型根据returnType 对应
     function getContent (arrayObj){
    	var returnType = $("#returnType").val();
    	var showContent = "";
    	if(returnType == null || returnType == "" || returnType == "arrayObj"){
			showContent = "arrayObj.selUsers="+arrayObj.selUsers+"\n";
	    	showContent += "arrayObj.arrUserIID="+toString(arrayObj.arrUserIID)+"\n";
	    	showContent += "arrayObj.arrUserId="+toString(arrayObj.arrUserId)+"\n";
	    	showContent += "arrayObj.arrUserName="+toString(arrayObj.arrUserName)+"\n";
	    	showContent += "arrayObj.arrUserEMail="+toString(arrayObj.arrUserEMail)+"\n";
	    	showContent += "arrayObj.arrDomainIID="+toString(arrayObj.arrDomainIID)+"\n";
	    	showContent += "arrayObj.arrDomainName="+toString(arrayObj.arrDomainName)+"\n";
    	}else{
    		showContent = arrayObj;
    	}
    	return showContent;
	}
 	//返回
 	function back(){
 		//window.history.back()
 		location.href='<%=testIndexPage%>';
 	}
 	//toString
 	function toString(str){
 	 	var toStr=""
		if(typeof str == "object"){
			for(var i=0;i<str.length;i++){
				toStr +=str[i]
			    if(i!=str.length-1){
			    	toStr+=","
				 }
			}
		}
		return toStr;
 	 }

</script>
</body>
</html>