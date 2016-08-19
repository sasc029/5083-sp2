<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%
	//������ҳ
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
    	<SPAN style='FONT-WEIGHT: bold'>����ѡ���û�</SPAN></TD>
    	</TR>
		<tr>
			<td class='pt-label' style="text-align:center; width: 200px" >�ֶ��� </td>
			<td class='pt-label' style="text-align:center;">��ѡֵ</td>
			<td class='pt-label' style="text-align:center;">���� </td>
		</tr>
		<tr  align="center">
			<td class='pt-label' >SelectMode</td>
			<td class='pt-field'>
			<SELECT name='SelectMode' id='SelectMode' class='pt-select' style="width: 200px">
				<option value="multiple">multiple</option>
			    <option value="single">single</option>
			</SELECT>
			</td>
			<td class='pt-label' style="text-align:center;"	>ѡ��ģʽ��multiple��ѡ��Ĭ�ϣ���single��ѡ</td>
		</tr>
		
		<tr align="center">
			<td class='pt-label' >IsModal</td>
			<td class='pt-field'>
			<SELECT name='IsModal' id='IsModal' class='pt-select' style="width: 200px">
				<option value="true">true</option>
			    <option value="false">false</option>
			</SELECT>
			</td>
			<td class='pt-label' style="text-align:center;"	>ҳ���Ƿ���ģ̬</td>
		</tr>
		
		<tr align="center">
			<td class='pt-label' >returnType</td>
			<td class='pt-field'>
			<SELECT name='returnType' id='returnType' class='pt-select' style="width: 200px">
				<option value="arrayObj" selected="selected">arrayObj</option>
			    <option value="json">json</option>
			</SELECT>
			</td>
			<td class='pt-label' style="text-align:center;"	>������������arrayObj(�������,Ĭ��),json(json����)</td>
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
			<td class='pt-label' style="text-align:center;"	>ѡ���û���Χ</td>
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
			<td class='pt-label' style="text-align:center;"	>ѡ���û�״̬</td>
		</tr>
		<tr align="center">
			<td class='pt-label' id="id1">callbackFun</font></td>
			<td class='pt-field'>
			<INPUT ui='textField' type='text' name='callbackFun' 
				class='pt-textbox pt-validatebox ' id='callbackFun' required='true' 
				maxLength='20' value="callbackFun" style="width:200px;">
			</td>
			<td class='pt-label' style="text-align:center;"	>��ģ̬��ʽ��ѡ���û��ص�����</td>
		</tr>
		
		<tr>
			<td class='pt-label' id="id1"  align="center">����ֵ</font></td>
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
			<div class="pt-formbutton" text="�� " iconCls="icon-ok"
						onclick="openSelectPage()"></div>
			<div class="pt-formbutton" id="clearForm" text="����"" iconCls="icon-cancel"
						onclick="back()"></div>
		</td>
	</tr>
</table>
	
<script>
	//��ѡ��ҳ��
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

	//�ص�����
    function callbackFun (arrayObj) {
    	var showContent = getContent(arrayObj);
    	document.getElementById("returnContent").value = showContent;
	}
	//reObj Ϊ�������ݣ������������͸���returnType ��Ӧ
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
 	//����
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