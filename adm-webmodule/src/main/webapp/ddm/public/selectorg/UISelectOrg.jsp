<%@page import="com.bjsasc.plm.core.context.model.RootContext"%>
<%@page import="com.bjsasc.plm.core.system.discipline.PrincipalDisciplineLink"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.core.util.JsonUtil"%>
<%@page import="com.bjsasc.plm.core.system.principal.RoleHelper"%>
<%@page import="com.bjsasc.plm.core.context.model.OrgContext"%>
<%@page import="com.bjsasc.plm.core.foundation.Helper"%>
<%@page import="com.bjsasc.platform.i18n.PLU"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="java.net.URLDecoder"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%
  /**
	* ҳ��˵����
	* 
	* callback:�ص���������
	* singleSelect  true:��ѡ ��false:��ѡ 
	* ids �Ѿ�ѡȡ��֯����AAORGINNERID���ϣ��Զ��Ÿ�����
	* type 1:��ʾ������֯����  2:����ʾ��ǰϵͳ�û���������������֯�������ⲿ��֯������3:����ʾ������֯���� 
	*      4:����ʾ�ⲿ��֯���� 
	*/
%>
<%
	String callback = request.getParameter("callback");	
	callback="doBindOrgs";
	String singleSelect = request.getParameter("singleSelect");
	String type = request.getParameter("type");
	type="2";
	if(singleSelect==null||singleSelect.trim().equals("")){
		singleSelect = "false";
	}
	String ids = request.getParameter("ids");
	String url = request.getContextPath()+"/ddm/public/selectorg/selectOrganizationTree_get.jsp?op=Root&type="+type;
	String disMediaTypeURL = request.getContextPath()+"/plm/common/select/getSeclect.jsp?select=disMediaType2";
	//ѡ��ģʽ����ѡ���߶�ѡ��Ĭ��Ϊ��ѡ��ȡֵ����{"single"��"multiple"}
		String sSelMode = request.getParameter("SelectMode");
		if (sSelMode != null && sSelMode.trim().equalsIgnoreCase("single")) {
			sSelMode = "single";
		} else {
			sSelMode = "multiple";
		}
		String IsModal = request.getParameter("IsModal");
		IsModal = "true";
		/* if(IsModal != null && IsModal.trim().equalsIgnoreCase("true")){
			IsModal = "true";
		}else{
			IsModal = "false";
		} */
		//�����������ͣ�Ĭ��ΪarrayObj��ȡֵ����{"arrayObj"��"json"}
		String returnType = request.getParameter("returnType");
			returnType="arrayObj";
	/* 	if (returnType == null || returnType.trim().length()==0||"null".equals(returnType)) {
			returnType = "arrayObj";
		} */
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ѡ����֯����</title>
<jsp:include page="/platform/ui/Ui-js.jsp"></jsp:include>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
<script type="text/javascript">

</script>
</head>
<body> 
	<table cellSpacing="0" cellPadding="0" width="100%" height="100%" align="center">
		<tr>
			<td valign="top" width="205px">
				<table cellSpacing="0" cellPadding="0" width="100%" height="100%" align="center">
				    <tr>
					    <td valign="top" height="20px">
					    	<div id="searchTextDiv"><input style="width:100%" type="text" id="searchText" value="" class='pt-textbox' onkeyup="plm_search.searchOkEnterKey();" ontrigger="plm_search.search(0);"></div>
							<div id="searchTextsDiv" style="display: none;" >
								<input id="searchTexts" style=" width:75%; height:25px;float: left;" type="text" value="" onkeyup="plm_search.closeBackGoBar();" class='pt-textbox'/>
								<input class="buttontreeDefault" onmouseover="this.className='buttontreeHover'" onmouseout="this.className='buttontreeDefault'" style="border: #CCCCCC solid 1px; float: left; width:7.2%; height:25px"  type="button" value="X" onclick="plm_search.closeBackGoBar();"/>
								<input class="buttontreeDefault" onmouseover="this.className='buttontreeHover'" onmouseout="this.className='buttontreeDefault'"  id="treeBack" style="border: #CCCCCC solid 1px;float: left; width: 7.2%; height:25px"   type="button" value="<" onclick="plm_search.backSearch();"/>
								<input class="buttontreeDefault" onmouseover="this.className='buttontreeHover'" onmouseout="this.className='buttontreeDefault'" id="treeInput" style="border: #CCCCCC solid 1px; float: left; width: 7.2%; height:25px"    type="button" value=">" onclick="plm_search.goSearch();"/>
							</div>	
					    </td>
				    </tr>
					<tr>
						<td valign="top">
							<table id="tree" class="pt-grid" headerVisible="false" fit="true" treeColumn="text" url="<%=url%>" horizontalLine="false" singleSelect="<%=singleSelect %>" pagination="false" <%if(singleSelect.equals("false")){%>checkbox="true"<%}%> onClickRow="onClickRow" onDblClickRow="pt_sa_selectOrg.addRecord()" onLoadSuccess="onLoadSuccess">
								<thead>
									<th field="text" id="text" width="240px" ></th>
								</thead>
							</table>
						</td>
					</tr>
				</table>
			</td>
			<td valign="top">
				<TABLE cellSpacing="0" cellPadding="0" width="100%" height="100%">	
					<TR height="20px">
						<TD>
							<div class="pt-toolbar">
								<div class="pt-button" text="<%=PLU.getString("btn.remove")%>" icon="<%=request.getContextPath()%>/platform/images/aa/move_out_org.gif" iconAlign="top" onClick="pt_sa_selectOrg.deleteRecord()"></div>
							</div>
						</TD>
					</TR>
					<TR height="95%">
						<TD width="100%" valign="top">
							<table id="grid_pt_sa_alreadySelcetOrg" class="pt-grid" singleSelect="false" checkbox="true" fit="true" width="100%" checkbox="true" onDblClickRow="pt_sa_selectOrg.deleteRecord()" pagination="false" rownumbers='true'>
								<thead>
									<!-- <th field="NUMBER" width="150">���</th>
									<th field="NAME" width="150">����</th> -->
									<th field="NAME"         width="120"><%=PLU.getString("pt.fld.name")%></th>
								   	<th field="DISMEDIATYPE" width="100" editor="{type:'combo',url:'<%=disMediaTypeURL %>',valueField:'value',textField:'text'}" formatter="selectFormatter()">�ַ�����</th>
								   	<th field="DISINFONUM" 	 width="80"  editor="{type:'text'}">�ַ�����</th>
								   	<th field="NOTE" 		 width="145" editor="{type:'text'}">��ע</th>
									<th field="ID"           hidden="true"><%=PLU.getString("pt.fld.id")%></th>
								</thead>
							</table>
						</TD>
					</TR>
				</TABLE>
			</td>
		</tr>
		<tr height="50px">
			<td align="center" colspan="2">
				<div class="pt-formFooter">
					<%-- <div class="pt-formbutton" text="<%=PLU.getString("btn.OK")%>" isok="true" onclick="onOk()"></div> --%>
					<div class="pt-formbutton" text="<%=PLU.getString("btn.OK")%>" isok="true" onclick="pt_sa_selectOrg.submit()"></div>
					<div class="pt-formbutton" id="clearForm" text="<%=PLU.getString("btn.cancel")%>" onclick="window.close();"></div>
				</div>
			</td>
		</tr>
	</table>
	
</body>
</html>
<script type="text/javascript">
var returnType = "<%=returnType %>" ; //��������
var callbackFun ="<%=callback%>";
var pt_sa_selectUser={};//�����ռ�
//����˫��չ���ڵ�
pt_sa_selectUser.expandNode = function(node,expandAll,fn){
	UI.get('tree').expand(node,expandAll,fn);
}
pt_sa_selectUser.unselectRow = function(index){
	UI.get('tree').unselectRow(index);
}
function onLoadSuccess(node){
	if(node.length>0){
		if(node[0]["id"]=="root"){
			pt_sa_selectUser.expandNode(node[0],false,loadSuccessBack);
		}
	}
}

var pt_sa_selectOrg={};//�����ռ�
//����ѡ����֯������ӵ���ѡ��֯��������
pt_sa_selectOrg.addRecord = function() {
		var grid_orgTree = pt.ui.get("tree");
		var grid_pt_sa_alreadySelcetOrg = pt.ui.get("grid_pt_sa_alreadySelcetOrg");
		var selections = grid_orgTree.getSelections();
		if(selections.length==0){
			alert("<%=PLU.getString("pmp.noObj")%>");
			return false;
		}
		var dataNodes = new Array();
		for(var i = 0; i < selections.length; i++) {
			var dataNode = new Object();
			dataNode.AAORGINNERID = selections[i].AAORGINNERID;
			dataNode.id = selections[i].id;
			dataNode.NUMBER = selections[i].NUMBER;
			dataNode.NAME = selections[i].NAME;
			/* selections[i]["DISMEDIATYPE"] = 0;
			selections[i]["DISINFONUM"] = 1; */
			dataNode["DISMEDIATYPE"]=0;
			dataNode["DISINFONUM"]=1;
			dataNodes[i] = dataNode;
		
		} 
		for(var i = 0; i < dataNodes.length; i++) {
			var idx=grid_pt_sa_alreadySelcetOrg.find({"AAORGINNERID":dataNodes[i]['AAORGINNERID']});
			if(!idx){
				var tempSelection = dataNodes[i];
				<%if(singleSelect.equals("true")){%>
				grid_pt_sa_alreadySelcetOrg.deleteRow(grid_pt_sa_alreadySelcetOrg.getRows());
				<%}%>
				grid_pt_sa_alreadySelcetOrg.appendRow(tempSelection);
			}
		}
		grid_orgTree.clearSelections();
}

//ɾ����ѡ��֯�����б��е���Ŀ
pt_sa_selectOrg.deleteRecord = function() {
		var grid_pt_sa_alreadySelcetOrg = pt.ui.get("grid_pt_sa_alreadySelcetOrg");
		var selections = grid_pt_sa_alreadySelcetOrg.getSelections();
		if(selections.length==0){
			alert("<%=PLU.getString("pmp.noObj")%>");
			return false;
		}
		grid_pt_sa_alreadySelcetOrg.deleteRow(selections);
}


/**
 * �������ݳɹ���ص�
 */
function loadSuccessBack(){
	/**
	*��ѡ������
	*/
	<%if(ids!=null&&!ids.trim().equals("")&&!ids.trim().equals("null")){%>
	var ids = "<%=ids%>";
	plm_search.selected(ids);
	<%}%>
}
//----------------------------------------����-----------------------------------------
var treeInnerIdPaths = new Array();//�����������������·������(��innerId��ɣ���/�ָ�)
var searchMarkIndex = 0;//�������
var plm_search = {};
plm_search.search = function(index){
	var tree = pt.ui.get("tree");
	plm_search.dosearch();
	searchMarkIndex = index;
	setTimeout(function(){
		if(treeInnerIdPaths.length==0){
			return;
		}
		tree.clearSelections();//���ѡ��
		plm_search.searchInTree(tree);
		document.getElementById("searchTexts").value = document.getElementById("searchText").value;
		document.getElementById("searchTextDiv").style.display="none";
		document.getElementById("searchTextsDiv").style.display="block";
	},100);
}
plm_search.searchOkEnterKey = function(){
	if (event.keyCode == 13){
		plm_search.search(0);
	}
}
plm_search.backSearch = function(){
	if(searchMarkIndex>0){
		searchMarkIndex--;//��������
		plm_search.search(searchMarkIndex);
	}else{
		alert("�����������Ѿ��ǵ�һ����");
	}
}
plm_search.goSearch = function(){
	if(searchMarkIndex==(treeInnerIdPaths.length-1)){
		alert("�����������Ѿ������һ����");
	}else{
		searchMarkIndex++;//����ǰ��
		plm_search.search(searchMarkIndex);
	}
}
plm_search.closeBackGoBar = function(){
	document.getElementById("searchTextDiv").style.display="block";
	document.getElementById("searchTextsDiv").style.display="none";
}
//�ں�̨����
plm_search.dosearch = function(){
	//���ü��ϴ�С
    treeInnerIdPaths.length=0;
    //����������
    var searchText=document.getElementById("searchText").value;
    $.ajax({ 
		type: "post",
        url: "<%=request.getContextPath()%>/ddm/public/selectorg/selectOrganizationInfo_get.jsp", 
        dataType: "json", 
        data:{"searchText":searchText,type:"<%=type%>"},
        async:false,
        success: function (data){ 
              var size = parseInt(data.SIZE);
              for(var i = 0 ; i < size ; i++){
            	  treeInnerIdPaths.push(data['PATH'+i]);
              }             	                  
        }
   });
}
//�����ڵ�������
plm_search.searchInTree = function(tree){
	//var records=tree.getRows();
	var noFindNode = false;
	var jsonTree=treeInnerIdPaths[searchMarkIndex].split("/");
	for(var i=0;i<jsonTree.length;i++){
		//���ҽڵ�
		var options = {AAORGINNERID:jsonTree[i]};
		var n = tree.find(options);
		if(n){
			if(i==(jsonTree.length-1)){//���һ���ڵ�ѡ��
				//tree.clearSelections();
	    		tree.selectRow(n);
			}else{
				if(n.expanded){//Ŀ¼�Ƿ�չ��
					continue;
				}else{
					tree.expand(n,true);
					setTimeout(function(){
						plm_search.searchInTree(tree);
					},100);
					break;
				}
			}
		}else{
			noFindNode = true;
			break;
		}
	}
	if(noFindNode){//�����һ��ƥ��·����Ч��������һ����
		searchMarkIndex ++;
		plm_search.searchInTree(tree);
	}
}
/**
 * ��ѡ����
 */
plm_search.selected = function(ids){
	var tree = pt.ui.get("tree");
	plm_search.doSelected(ids);
	searchMarkIndex = 0;
	setTimeout(function(){
		if(treeInnerIdPaths.length==0){
			return;
		}
		tree.clearSelections();//���ѡ��
		for(var i=0;i<treeInnerIdPaths.length;i++){
			plm_search.searchInTree(tree);
			searchMarkIndex ++;
	    }
		setTimeout(function(){
			//ѡ�в���
			var ids_array = ids.split(",");
			for(var i=0;i<ids_array.length;i++){
				var options = {AAORGINNERID:ids_array[i]};
				var n = tree.find(options);
				tree.selectRow(n);
			}
		},200);
	},100);
}
/**
 * �Ӻ�̨��������
 */
plm_search.doSelected = function(ids){
	//���ü��ϴ�С
    treeInnerIdPaths.length=0;
    $.ajax({ 
		type: "post",
        url: "<%=request.getContextPath()%>/ddm/public/selectorg/selectOrganizationInfo_get.jsp", 
        dataType: "json", 
        data:{"ids":ids,type:"<%=type%>"},
        async:false,
        success: function (data){ 
              var size = parseInt(data.SIZE);
              for(var i = 0 ; i < size ; i++){
            	  treeInnerIdPaths.push(data['PATH'+i]);
              }             	                  
        }
   });
}
//----------------------------------------end-----------------------------------------
function onClickRow(node){
	pt_sa_selectOrg.addRecords();
	if(node["id"]=="root"){
		pt_sa_selectUser.unselectRow(0);
	}
}
function onOk(){
	var grid_pt_sa_alreadySelcetOrg = pt.ui.get("grid_pt_sa_alreadySelcetOrg");
	var rows = grid_pt_sa_alreadySelcetOrg.getRows();
	if(rows.length==0){
		alert("<%=PLU.getString("pmp.noObj")%>");
		return false;
	}
	<%if(callback != null){%>
	<%if(singleSelect.equals("false")){%>
	var records = grid_pt_sa_alreadySelcetOrg.getRows();
	<%}else{%>
	var records = grid_pt_sa_alreadySelcetOrg.getRows()[0];
	<%}%>
	opener.<%=callback%>(records);
	<%}%>
	window.close();
}


//ȷ����ť�����¼�
pt_sa_selectOrg.submit = function(){
	if("single" == "<%=sSelMode%>"){
		pt_sa_selectOrg.returnOne();
	}
	else if("multiple" == "<%=sSelMode%>"){
		pt_sa_selectOrg.returnMultiple();
	}
}
// ��ѡ������Ա
pt_sa_selectOrg.returnMultiple = function() {
	//var grid_pt_sa_alreadySelcet = pt.ui.get("grid_pt_sa_alreadySelcet");
	var grid_pt_sa_alreadySelcet = pt.ui.get("grid_pt_sa_alreadySelcetOrg");
	var selections = grid_pt_sa_alreadySelcet.getRows();
	if(selections.length==0){
		alert("<%=PLU.getString("pmp.noObj")%>");
		return false;
	}
	returnSelections(selections);
	
}
//������ѡ��¼
function returnSelections(selections){
	var arrDivIID = new Array();
	var arrDivID = new Array();
	var arrDivName = new Array();
	var arrDomainIID = new Array();
	var arrDomainName = new Array();
	var arrDisMediaType = new Array();
	var arrDisInfoNum = new Array();
	var arrNote = new Array();		
	 for(var i = 0 ; i < selections.length; i++){
		 arrDivIID[i] = selections[i]["AAORGINNERID"];
		 arrDivID[i] = selections[i]["ID"];
		 arrDivName[i] = selections[i]["NAME"];
		 arrDomainIID[i] = selections[i]["DOMAINIID"];
		 arrDomainName[i] = selections[i]["DOMAINNAME"];
		 arrDisMediaType[i] = selections[i]["DISMEDIATYPE"];
		 arrDisInfoNum[i] = selections[i]["DISINFONUM"];
		arrNote[i] = selections[i]["NOTE"];
		if (null == arrNote[i] || arrNote[i] == undefined || arrNote[i].length == 0) {
			arrNote[i] = "";
		}
		var chkIndex = parseInt(i) + 1;
		if (null == arrDisMediaType[i] || arrDisMediaType[i] == undefined || arrDisMediaType[i].length == 0) {
			alert("�ڣ�"+chkIndex+"���еķַ��������Ͳ���Ϊ�գ�");
			return false;
		}
		
		var numReg=/^\d+$/;
		if (null == arrDisInfoNum[i] || arrDisInfoNum[i] == undefined || arrDisInfoNum[i].length == 0) {
			alert("�ڣ�"+chkIndex+"���еķַ���������Ϊ�գ�");
			return false;
		}
		if (!numReg.test(arrDisInfoNum[i])) {
			alert("�ڣ�"+chkIndex+"���еķַ������������֣�");
	    	return false;
		} else if (parseInt(arrDisInfoNum[i]) <= 0) {
			alert("�ڣ�"+chkIndex+"���еķַ���������Ϊ 0 ��");
	    	return false;
		}else if(parseInt(arrDisInfoNum[i])>= 9999999999999999999999999){
			alert("�ڣ�"+chkIndex+"���еķַ�����̫��");
	    	return false;
		}			 		 
	}
	var returnObj = {
			arrIID:arrDivIID ,
			arrDivID: arrDivID,
			arrDivName: arrDivName,
			arrDomainIID: arrDomainIID,
			arrDomainName: arrDomainName,
			arrDisMediaType:arrDisMediaType,
			arrDisInfoNum:arrDisInfoNum,
			arrNote:arrNote
	}
	
	var IsModal = "<%=IsModal%>";
	if(IsModal == 'true'){
		if(returnType == null || returnType=='arrayObj' ){
		
			window.returnValue = returnObj;
		}
		else if(returnType == 'json'){
			var returnUserJson =  pt.ui.JSON.encode(selections);
			window.returnValue = returnUserJson;
		}
	}else{
		if(returnType == null || returnType=='arrayObj' ){
			eval("opener."+callbackFun+"(returnObj);");
		}
		else if(returnType == 'json'){
			var returnOrgJson =   pt.ui.JSON.encode(selections);
			//var returnOrgJson = selections;
			eval("opener."+callbackFun+"(returnOrgJson);");
		}
	}
	pt_sa_selectOrg.closeWin();

}
//�رմ���
pt_sa_selectOrg.closeWin = function() {
	window.close();
}
//����ѡ����Ա��ӵ���ѡ��Ա����
pt_sa_selectOrg.addRecords = function() {
		var grid_orgTree = pt.ui.get("tree");
		var selections = grid_orgTree.getSelections();
		var grid_pt_sa_alreadySelcetOrg =pt.ui.get("grid_pt_sa_alreadySelcetOrg");
		if(selections.length==0){
			alert("<%=PLU.getString("pmp.noObj")%>");
			return false;
		}
		for ( var i = 0; i < selections.length; i++) {
			var idx=grid_pt_sa_alreadySelcetOrg.find({"AAORGINNERID":selections[i]['AAORGINNERID']});
			if(!idx){
				selections[i]["DISMEDIATYPE"] = 0;
				selections[i]["DISINFONUM"] = 1;
				//grid_pt_sa_alreadySelcetOrg.appendRow(selections[i]);
			}
		}
		//grid_pt_sa_selectOrg.clearSelections();
		//�ַ������ǡ����ӡ����ַ����������޸�
		grid_pt_sa_alreadySelcetOrg.on('beforecelledit', function(e){
			var DISMEDIATYPE = e.record.DISMEDIATYPE;
			var column = e.column;
			if(column.id=="DISINFONUM" && (DISMEDIATYPE=="����" || DISMEDIATYPE=="1")){
				return false;
			}
			else{
				return true;
			}
		});
		//�ַ������ǡ����ӡ����ַ�������Ϊ��1��
		grid_pt_sa_alreadySelcetOrg.on('aftercelledit', function(e){
			var DISMEDIATYPE = e.record.DISMEDIATYPE;
			var column = e.column;
			var row =e.rowIndex;
			if(column.id=="DISMEDIATYPE" && (DISMEDIATYPE=="����" || DISMEDIATYPE=="1")){
				grid_pt_sa_alreadySelcetOrg.setColumnValue("DISINFONUM",1,row);
			}
		});
	}
</script>