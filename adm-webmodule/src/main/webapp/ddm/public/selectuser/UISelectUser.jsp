<%
	/**
	 *  ҳ��˵��
	 *  1.���ƣ�
	 *  2.���ã� ����ѡ��ҳ�����
	 *  3.������
	 *		    SelectMode ��   ѡ��ģʽ����ѡ��single�����߶�ѡ��multiple�� Ĭ��Ϊ��multiple��
	 *			callbackFun:  �ص�������
	 *			returnType:   ����ֵ��ʽ����ѡֵ��arrayObj������json����Ĭ��Ϊ��arrayObj��
	 			scope: ��Ա�б�Χ��all��ȫ���û�����self����ָ�����г������û�����ref����ָ����������û�
	 			domainRef ���innerid Ĭ��Ϊ��ǰ��¼����
	 			userStatus:�û����ͣ�A ����(Ĭ��)��F ���� ALL ȫ��
	 			IsModal �Ƿ���ģ̬
	 */
%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>


<%@page import="com.cascc.platform.aa.api.util.AADomainUtil"%>
<%@page import="com.cascc.platform.aa.api.util.AAUtil"%>
<%@page import="com.cascc.platform.aa.AAProvider"%>
<%@page import="com.cascc.avidm.login.model.PersonModel"%>
<%@page import="com.bjsasc.platform.i18n.PlatformLocaleUtility"%>
<%
	//�������
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	request.setCharacterEncoding("UTF-8");

	//�������¹���Ա������Ա��ѡ��Χ
	//ϵͳ����Ա��ֻ��ѡ���û�������ϵͳ����ԱAvidmConstDefine.SYSADM_USERTYPE_SYSMGR
	//��ȫ����Ա��ֻ��ѡ���û������ǰ�ȫ����ԱAvidmConstDefine.SYSADM_USERTYPE_SECURITYMGR
	//��ƹ���Ա��ֻ��ѡ���û���������ƹ���ԱAvidmConstDefine.SYSADM_USERTYPE_LOGMGR
	String configAdmin = request.getParameter("configAdmin");
	int gridWidth = 210 ;
	if(null != configAdmin && configAdmin.equals("configAdmin")){
		gridWidth = 300 ;
	}
	PersonModel person = (PersonModel) session.getAttribute("person");
	int loginDomainRef = person.getDomainRef();
	//���div��ֵ��ѡ��ҳ����Ⱦ��һ���´򿪵�IE�����в�����ext window�ؼ�
	String div = request.getParameter("div");
	String closable = "true";
	//�����������ͣ���ǰ����Ա����
	String userStatus = request.getParameter("userStatus");
	if (userStatus == null||userStatus.trim().length()==0||"null".equals(userStatus)) {
		userStatus = "A";
	}
	//�����������ͣ�Ĭ��ΪarrayObj��ȡֵ����{"arrayObj"��"json"}
	String returnType = request.getParameter("returnType");
	if (returnType == null||returnType.trim().length()==0||"null".equals(returnType)) {
		returnType = "arrayObj";
	}
	//�ص�������
	String callbackFun = request.getParameter("callbackFun");
	if(callbackFun==null){
		callbackFun="callbackFun";
	}
	//�û�����
	String pClass = request.getParameter("pClass");
	if(pClass==null){
		pClass="user";
	}

	//ѡ��ģʽ����ѡ���߶�ѡ��Ĭ��Ϊ��ѡ��ȡֵ����{"single"��"multiple"}
	String sSelMode = request.getParameter("SelectMode");
	if (sSelMode != null && sSelMode.trim().equalsIgnoreCase("single")) {
		sSelMode = "single";
	} else {
		sSelMode = "multiple";
	}
	String IsModal = request.getParameter("IsModal");
	if(IsModal != null && IsModal.trim().equalsIgnoreCase("true")){
		IsModal = "true";
	}else{
		IsModal = "false";
	}
	//�û��б�ķ�Χ
	String scope = request.getParameter("scope");
	String domainRef = request.getParameter("domainRef");
	//���û�и����ʶ����ȡ��ǰ��
	if(domainRef == null || "".equals(domainRef.trim()) || "null".equals(domainRef)){
		domainRef = loginDomainRef+"";
	}
	if(scope == null ||"".equals(scope.trim()) || "null".equals(scope)){
			scope = AADomainUtil.VISTYPE_SELF;
	}
	if(AADomainUtil.VISTYPE_ALL.equals(scope)){
		domainRef="-1";
	}
	//tree ����Դ
	String url_tree = request.getContextPath()
			+ "/platform/public/selectuser/UISelectUserAction.jsp?op=getTree&domainRef="
			+ domainRef + "&scope=" + scope+"&userStatus="+userStatus+"&treeNode=root";
	//grid ����Դ
	String url_grid1 = request.getContextPath()
			+ "/platform/data/SelectUserUtil.jsp?userStatus="+userStatus+"&pClass=user&domainRef="
			+ domainRef + "&scope=" + scope+"&dataFormat=jq"+"&configAdmin=" + configAdmin;
	String url_grid2 = "";

	String url_rootIcon = request.getContextPath()
			+ "/platform/images/folder/Doc_tree_root.gif";
	
	String disMediaTypeURL = request.getContextPath()+"/plm/common/select/getSeclect.jsp?select=disMediaType2";
%>


<%@page import="com.bjsasc.platform.i18n.PLU"%><html>
<head>
<title><%=PLU.getString("btn.select")%><%=PLU.getString("txt.user")%></title>
<script type="text/javascript" src="SelectData.jsp"></script>
<script type="text/javascript" src="StatusEnum.js"></script>
<script type="text/javascript">
	var contextPath = "<%=request.getContextPath()%>";
</script>
<jsp:include page="/platform/ui/Ui-js.jsp"></jsp:include>
<script type="text/javascript">
  var pt_sa_selectUser={};//�����ռ�
	var returnType = "<%=returnType %>"; //��������
	var callbackFun ="<%=callbackFun%>";

	//�������¼�
	pt_sa_selectUser.dblclick = function(node) {
		    var grid_pt_sa_selectUser = pt.ui.get('grid_pt_sa_selectUser');
			var id = node.id;
			var openUrl = node.openUrl;//�õ����ڵ��ϵ�url
			url =openUrl;
			if(url ==null || url.length==0){
				return;
			}
			grid_pt_sa_selectUser.reload({url:url,page:1});
	}

	//����ѡ����Ա��ӵ���ѡ��Ա����
	pt_sa_selectUser.addRecords = function(rowindex,rowData) {
			var grid_pt_sa_selectUser = pt.ui.get("grid_pt_sa_selectUser");
			var grid_pt_sa_alreadySelcetUser = pt.ui.get("grid_pt_sa_alreadySelcetUser");
			var selections = grid_pt_sa_selectUser.getSelections();
			if(selections.length==0){
				alert("<%=PLU.getString("pmp.noObj")%>");
				return false;
			}
			for ( var i = 0; i < selections.length; i++) {
				var idx=grid_pt_sa_alreadySelcetUser.find({"INNERID":selections[i]['INNERID']});
				if(!idx){
					selections[i]["DISMEDIATYPE"] = 1;
					selections[i]["DISINFONUM"] = 1;
					grid_pt_sa_alreadySelcetUser.appendRow(selections[i]);
				}
			}
			grid_pt_sa_selectUser.clearSelections();
			//�ַ������ǡ����ӡ����ַ����������޸�
			grid_pt_sa_alreadySelcetUser.on('beforecelledit', function(e){
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
			grid_pt_sa_alreadySelcetUser.on('aftercelledit', function(e){
				var DISMEDIATYPE = e.record.DISMEDIATYPE;
				var column = e.column;
				var row =e.rowIndex;
				if(column.id=="DISMEDIATYPE" && (DISMEDIATYPE=="����" || DISMEDIATYPE=="1")){
					grid_pt_sa_alreadySelcetUser.setColumnValue("DISINFONUM",1,row);
				}
			});
		}

	//ɾ����ѡ��Ա�б��е���Ŀ
	pt_sa_selectUser.deleteRecord = function() {
			var grid_pt_sa_alreadySelcetUser = pt.ui.get("grid_pt_sa_alreadySelcetUser");
			var selections = grid_pt_sa_alreadySelcetUser.getSelections();
			if(selections.length==0){
				alert("<%=PLU.getString("pmp.noObj")%>");
				return false;
			}
			grid_pt_sa_alreadySelcetUser.deleteRow(selections);
		}

	//��ѡ�����û�
	pt_sa_selectUser.returnOne = function(){
			var grid_pt_sa_selectUser = pt.ui.get("grid_pt_sa_selectUser");
			var selections = grid_pt_sa_selectUser.getSelections();
			if(selections.length != 1){
				alert("<%=PLU.getString("pmp.noObj")%>");
				return false;
			}
			returnSelections(selections);
		}

	// �رմ���
	pt_sa_selectUser.closeWin = function() {
		window.close();
	}

	//��ѡ����
	pt_sa_selectUser.returnMultiple = function() {
		var grid_pt_sa_alreadySelcetUser = pt.ui.get("grid_pt_sa_alreadySelcetUser");
		var selections = grid_pt_sa_alreadySelcetUser.getRows();
		if(selections.length==0){
			alert("<%=PLU.getString("pmp.noObj")%>");
			return false;
		}
		returnSelections(selections);
	}

	//������ѡ��¼
	function returnSelections(selections){
		var selUsers = "";
		var arrUserIID = new Array();
		var arrUserId = new Array();
		var arrUserName = new Array();
		var arrUserEMail = new Array();
		var arrDomainIID = new Array();
		var arrDomainName = new Array();
		var arrDisMediaType = new Array();
		var arrDisInfoNum = new Array();
		var arrNote = new Array();
		var isWorkflow = 'false';
		var ds = '@[';
		var de = ']';
		for(var i = 0 ; i < selections.length; i++){
			var domainName='';
			if(null!=selections[i]["DOMAINIID"]&&selections[i]["DOMAINIID"]!=undefined&&selections[i]["DOMAINIID"].length>0&&selections[i]["DOMAINIID"]!='-1'&&isWorkflow=='true'){
				domainName = "["+selections[i].domainName+"]";
			}
			selUsers = selUsers + selections[i]["USERID"] + "(" +  selections[i]["USERNAME"]+ domainName+"); ";
			if(null!=selections[i]["DOMAINIID"]&&selections[i]["DOMAINIID"]!=undefined&&selections[i]["DOMAINIID"].length>0&&selections[i]["DOMAINIID"]!='-1'&&isWorkflow=='true'){
				arrUserIID[i] = selections[i]["INNERID"]+ ds + selections[i]["DOMAINIID"] + de;
			}else{
				arrUserIID[i] = selections[i]["INNERID"];
			}
			arrUserId[i] = selections[i]["USERID"];
			if(null!=selections[i]["DOMAINIID"]&&selections[i]["DOMAINIID"]!=undefined&&selections[i]["DOMAINIID"].length>0&&selections[i]["DOMAINIID"]!='-1'&&isWorkflow=='true'){
				arrUserName[i] = selections[i]["USERNAME"]+"["+selections[i]["DOMAINNAME"]+"]";
			}else{
				arrUserName[i] = selections[i]["USERNAME"];
			}
			arrUserEMail[i] = selections[i]["EMAIL"];
			if(null==selections[i]["DOMAINIID"]||selections[i]["DOMAINIID"]==undefined||selections[i]["DOMAINIID"].length<1||selections[i]["DOMAINIID"]=='-1'){
				arrDomainIID[i] = "-1";
			}else{
				arrDomainIID[i] = selections[i]["DOMAINIID"];
			}
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
							selUsers :selUsers ,
							arrIID:arrUserIID,
							arrUserId:arrUserId,
							arrUserName:arrUserName,
							arrUserEMail:arrUserEMail,
							arrDomainIID:arrDomainIID,
							arrDomainName:arrDomainName,
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
				//var returnUserJson =  selections;
				window.returnValue = returnUserJson;
			}
		}else{
			if(returnType == null || returnType=='arrayObj' ){
				eval("opener."+callbackFun+"(returnObj);");
			}
			else if(returnType == 'json'){
				var returnUserJson =   pt.ui.JSON.encode(selections);
				//var returnUserJson =  selections;
				eval("opener."+callbackFun+"(returnUserJson);");
			}
		}
		pt_sa_selectUser.closeWin();
	}

	//ȷ����ť�����¼�
	pt_sa_selectUser.submit = function(){
		if("single" == "<%=sSelMode%>"){
			pt_sa_selectUser.returnOne();
		}
		else if("multiple" == "<%=sSelMode%>"){
			pt_sa_selectUser.returnMultiple();
		}
	}
	//����˫��չ���ڵ�
	pt_sa_selectUser.expandNode = function(node){
		UI.get('tree').expand(node);
	}
	
	function selectFormatter(v,r,c){
		var status=new StatusEnum(v,c.dataIndex);
		var value = status.getStatusString();
		if(value==null||value=='null'){value='';}
	    return '<div align=left>'+value+'</div>';
	}
</script>
</head>
<body class="body">
<table cellSpacing="0" cellPadding="0" width="100%" height="100%" align="center">
	<tr>
		<!-- tree begin ����Ա����ʱ����ʾ���Ľṹ-->
		<%if( null == configAdmin || !configAdmin.equals("configAdmin")){ %>
		<td valign="top" width="200px" >
			<table class="pt-grid" headerVisible="false" style="overflow:auto; " fit="true" treeColumn="text" id="tree" url="<%=url_tree%>" horizontalLine="false"  onClickRow="pt_sa_selectUser.dblclick()"  onDblclick="pt_sa_selectUser.expandNode()">
				<thead>
					<th field="text" id="text" width="198"></th>
				</thead>
			</table>
	 	</td>
	 	<%} %>
	 	<!-- tree end  -->
	 	<!-- grid begin -->
		<td  valign="top">
		<%if("multiple".equals(sSelMode)) {%>
			<TABLE cellSpacing="0" cellPadding="0" width="100%" height="100%" >
		    	<TR height="50%">
				    <TD>
						<table id="grid_pt_sa_selectUser" class="pt-grid" fit="true" singleSelect="false" pageSize="6"
							checkbox="true"  useFilter="true" url="<%=url_grid1 %>" pagination="true" onDblClickRow="pt_sa_selectUser.addRecords" rownumbers='true'>
							<thead>
								<th field="USERNAME" 		width="<%=gridWidth %>"   sortable="true"	compare="exlike" filterType="string"  ><%=PLU.getString("pt.fld.username")%></th>
								<th field="USERID" 			width="<%=gridWidth %>"   sortable="true"	compare="exlike" filterType="string" ><%=PLU.getString("pt.fld.loginName")%></th>
							</thead>
						</table>
				    </TD>
				 </TR>
				 <TR height="20px">
				    <TD>
				    <div class="pt-toolbar">
						<div class="pt-button" text="<%=PLU.getString("btn.add")%>" 	icon="<%=request.getContextPath()%>/platform/images/aa/move_in_user.gif" iconAlign="top" onClick="pt_sa_selectUser.addRecords()"></div>
						<div class="pt-button" text="<%=PLU.getString("btn.remove")%>" 	icon="<%=request.getContextPath()%>/platform/images/aa/move_out_user.gif" iconAlign="top" onClick="pt_sa_selectUser.deleteRecord()"></div>
					</div>
				    </TD>
				 </TR>
				<TR  height="50%">
				<TD width="100%" valign="top">
						<table id="grid_pt_sa_alreadySelcetUser" class="pt-grid"  singleSelect="false" checkbox="true" url="<%=url_grid2%>"  fit="true" width="100%"  checkbox="true"  onDblClickRow="pt_sa_selectUser.deleteRecord"  pagination="false" rownumbers='true'>
							<thead>
							   	<th field="USERNAME" 	 width="120" ><%=PLU.getString("pt.fld.username")%></th>
							   	<!-- <th field="DISMEDIATYPE" width="100" title="" filterType="select" data="inSite" textField="id"  valueField="id" editor="{type:'combo',data:inSite,textField:'name',valueField:'name'}">�ַ�����</th> -->
							   	<th field="DISMEDIATYPE" width="100" editor="{type:'combo',url:'<%=disMediaTypeURL %>',valueField:'value',textField:'text'}" formatter="selectFormatter()">�ַ�����</th>
							   	<th field="DISINFONUM" 	 width="80"  editor="{type:'text'}">�ַ�����</th>
							   	<th field="NOTE" 		 width="145" editor="{type:'text'}">��ע</th>
								<th field="USERID" 	     hidden="true"><%=PLU.getString("pt.fld.loginName")%></th>
							</thead>
						</table >
				     </TD>
				 </TR>
			</TABLE>
		<%}else if("single".equals(sSelMode)) {%>
			<TABLE cellSpacing="0" cellPadding="0" width="100%" height="100%">
			    	<TR height="450px">
					    <TD >
							<table id="grid_pt_sa_selectUser" class="pt-grid" fit="true" singleSelect="true" checkbox="false" pageSize="20" useFilter="true" url="<%=url_grid1 %>" pagination="true" onDblClickRow="pt_sa_selectUser.returnOne" rownumbers='true'>
								<thead>
									<th field="USERNAME" 		width="<%=gridWidth %>" 	sortable="true" 	compare="exlike" filterType="string"><%=PLU.getString("pt.fld.username")%></th>
									<th field="USERID" 			width="<%=gridWidth %>" 	sortable="true" 	compare="exlike" filterType="string"><%=PLU.getString("pt.fld.loginName")%></th>
								</thead>
							</table>
					    </TD>
					 </TR>
			</TABLE>
		<%} %>
		</td>
		<!-- grid end -->
	</tr>
	<tr height="50px">
		<td align="center" colspan="2">
			<div class="pt-formFooter">
					<div class="pt-formbutton" text="<%=PLU.getString("btn.OK")%>"  isok="true"
								onclick="pt_sa_selectUser.submit()"></div>
					<div class="pt-formbutton" id="clearForm" text="<%=PLU.getString("btn.cancel")%>"
								onclick="pt_sa_selectUser.closeWin()"></div>
			</div>
		</td>
	</tr>
</table>

</body>
<script>
var inSite = [
              {id: '0', name: 'ֽ��'},
              {id: '1', name: '����'}
          ];
</script>
</html>