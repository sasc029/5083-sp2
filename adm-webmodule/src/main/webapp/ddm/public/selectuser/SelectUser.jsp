
<%
	/**
	 *  ҳ��˵��
	 *  1.���ƣ� 
	 *  2.���ã� ����ѡ��ҳ�����
	 *  3.������ div��  ���div��ֵ��ѡ��ҳ����Ⱦ��һ���´򿪵�IE�����в�����ext window�ؼ�
	 *		    SelectMode ��   ѡ��ģʽ����ѡ��single�����߶�ѡ��multiple�� Ĭ��Ϊ��multiple��
	 *			callbackFun:  �ص�������
	 *			returnType:   ����ֵ��ʽ����ѡֵ��arrayObj������json����Ĭ��Ϊ��arrayObj��
	 			scope: ��Ա�б�Χ��all��ȫ���û�����self����ָ�����г������û�����ref����ָ����������û�
	 			domainRef ���innerid Ĭ��Ϊ��ǰ��¼����
	 			userStatus:�û����ͣ�A ����(Ĭ��)��F ���� ALL ȫ��
	 */
%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@ page language="java"%>
<%@ page session="true"%>
<%@ page import="com.cascc.avidm.util.*"%>
<%@ page import="com.cascc.platform.aa.api.util.AATreeNodeSet"%>
<%@ page import="com.bjsasc.platform.i18n.PlatformLocaleUtility"%>
<%@ page import="com.cascc.platform.aa.api.util.AAUtil"%>
<%@ page import="com.cascc.avidm.login.model.*"%>
<%@ page import="com.cascc.platform.aa.api.util.*"%>
<%@ page import="com.cascc.platform.aa.api.data.*"%>
<%@ page import="com.cascc.platform.aa.*"%>
 <%@page import="com.cascc.platform.aa.api.util.AAMappingType"%>
<%@page import="com.cascc.platform.aa.AAConstDefine"%>

<%@ taglib uri="/WEB-INF/framework-ext.tld" prefix="ext"%>

<%
	//�������
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	request.setCharacterEncoding("UTF-8");
	
	PersonModel person = (PersonModel) session.getAttribute("person");
	int loginDomainRef = person.getDomainRef();
	//���div��ֵ��ѡ��ҳ����Ⱦ��һ���´򿪵�IE�����в�����ext window�ؼ�
	String div = request.getParameter("div");
	String closable = "true";
	String draggable="true";
	if (div == null || div.trim().length() == 0) {
		div = "";
		closable = "true";
	} else {
		closable = "false";
		draggable = "false";
	}
	//�����������ͣ���ǰ����Ա����
	String userStatus = request.getParameter("userStatus");
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
	//�û��б�ķ�Χ
	String scope = request.getParameter("scope");
	String domainRef = request.getParameter("domainIID");
	//���û�и����ʶ����ȡ��ǰ��
	if(domainRef == null || "".equals(domainRef.trim()) || "null".equals(domainRef)){
		domainRef = loginDomainRef+"";
	}
	if(scope == null ||"".equals(scope.trim()) || "null".equals(scope)){
			scope = AADomainUtil.VISTYPE_SELF;
	}
	String sRootName = AAUtil.getTreeRootGlossary(null);
	//tree ����Դ
	String url_tree = request.getContextPath()
			+ "/platform/public/selectuser/SelectUserAction.jsp?op=getTree&domainRef="
			+ domainRef + "&scope=" + scope+"&userStatus="+userStatus;
	//grid ����Դ
	String url_grid1 = request.getContextPath()
			+ "/platform/data/SelectUserUtil.jsp?userStatus="+userStatus+"&pClass=user&domainRef="
			+ domainRef + "&scope=" + scope;
	String url_grid2 = "";
	
	String url_rootIcon = request.getContextPath()
			+ "/platform/images/folder/Doc_tree_root.gif";
%>



<script type="text/javascript">
	Ext.namespace("pt_sa_selectUser");//�����ռ�
	var selectedArray = new Array();  //��ѡ��¼
	var returnType = "<%=returnType %>" ; //��������
	var callbackFun ="<%=callbackFun%>";
	//��Ա��Ϣ����
	pt_sa_selectUser.recordData = function(userName, userID, innerID, email, domainIID,
			domainName) {
		this.userName = userName;
		this.userID = userID
		this.innerID = innerID;
		this.email = email;
		this.domainIID = domainIID;
		this.domainName = domainName;
	}
	
	//�������¼�
	pt_sa_selectUser.dblclick = function(node) {
		    var grid_pt_sa_selectUser = Ext.getCmp('grid_pt_sa_selectUser');
		    var limit = grid_pt_sa_selectUser_pageToolBar.pageSize;
			var id = node.id;
			var openUrl = node.attributes.openUrl;//�õ����ڵ��ϵ�url
			url =openUrl;
			if(url ==null || url.length==0){
				return;
			}
			grid_pt_sa_selectUser.getStore().proxy = new Ext.data.HttpProxy(
					{
						method :"POST",
						url :url
					});
			grid_pt_sa_selectUser.getStore().load( {
				params : {
					start :0,
					limit :limit	
				}
			});
	}
	
	//����ѡ����Ա��ӵ���ѡ��Ա����
	pt_sa_selectUser.addRecords = function() {
			var grid_pt_sa_selectUser = Ext.getCmp("grid_pt_sa_selectUser");
			var grid_pt_sa_alreadySelcetUser = Ext.getCmp("grid_pt_sa_alreadySelcetUser");
			var selections = grid_pt_sa_selectUser.getSelectedRow();
			if(selections.length==0){
				pt.ui.quickTips.msg('<%=PlatformLocaleUtility.getLocaleString(request,"common.prompt")%>','<%=PlatformLocaleUtility.getLocaleString(request,"pt.prip.err.selMultiple")%>');
				return false;
			}
			var records = new Array();
			for ( var i = 0; i < selections.length; i++) {
				var newRecord = selections[i].copy();
				records[i] = newRecord;
			}
			grid_pt_sa_selectUser.clearSelections();
			for ( var i = 0; i < records.length; i++) {
				var id = records[i].get("INNERID");
				var number = grid_pt_sa_alreadySelcetUser.indexOf("INNERID", id);
				records[i].id = id;
				if (number == -1) {
					selectedArray[selectedArray.length] = new pt_sa_selectUser.recordData(
							records[i].get("USERNAME"),
							records[i].get("USERID"),
							records[i].get("INNERID"),
							records[i].get("EMAIL"),
							records[i].get("DOMAINIID"),
							records[i].get("DOMAINNAME"));
					var a = new Ext.data.Record.create( 
							[ 
								{name :'USERNAME'},
								{name :'USERID'}, 
								{name :'INNERID'}, 
								{name :'EMAIL'}, 
								{name :'DOMAINIID'}, 
								{name :'DOMAINNAME'} 
							], 
								{id :id}
					);
					var record = new a( {
						USERNAME :records[i].get("USERNAME"),
						USERID :records[i].get("USERID"),
						INNERID :records[i].get("INNERID"),
						EMAIL :records[i].get("EMAIL"),
						DOMAINIID :records[i].get("DOMAINIID"),
						DOMAINNAME :records[i].get("DOMAINNAME")
					});
					grid_pt_sa_alreadySelcetUser.addRow(record);
				}
			}
			grid_pt_sa_alreadySelcetUser.refreshView();
		}
	
	//ɾ����ѡ��Ա�б��е���Ŀ
	pt_sa_selectUser.deleteRecord = function() {
			var grid_pt_sa_alreadySelcetUser = Ext.getCmp("grid_pt_sa_alreadySelcetUser");
			var selections = grid_pt_sa_alreadySelcetUser.getSelectedRow();
			if(selections.length==0){
				pt.ui.quickTips.msg('<%=PlatformLocaleUtility.getLocaleString(request,"common.prompt")%>','<%=PlatformLocaleUtility.getLocaleString(request,"pt.prip.err.selMultiple")%>');
				return false;
			}
			var temp = selectedArray;
			for ( var i = 0; i < selections.length; i++) {
				for ( var j = 0; j < temp.length; j++) {
					if (temp[j].innerID == selections[i].get("INNERID")) {
						selectedArray.removeAt(j);
					}
				}
				grid_pt_sa_alreadySelcetUser.deleteRow(selections[i]);
			}
			grid_pt_sa_alreadySelcetUser.refreshView();
		}
	
	//��ѡ�����û� 
	pt_sa_selectUser.returnOne = function(){
			var grid_pt_sa_selectUser = Ext.getCmp("grid_pt_sa_selectUser");
			var selections = grid_pt_sa_selectUser.getSelectedRow();
				if(selections.length != 1){
					pt.ui.quickTips.msg('<%=PlatformLocaleUtility.getLocaleString(request,"common.prompt")%>','<%=PlatformLocaleUtility.getLocaleString(request,"pt.prip.err.selSingle")%>');
					return false;	
				}
				var records = new Array();
				for ( var i = 0; i < selections.length; i++) {
					var newRecord = selections[i].copy();
					records[i] = newRecord;
				}
				grid_pt_sa_selectUser.clearSelections();
				for ( var i = 0; i < records.length; i++) {
						selectedArray[selectedArray.length] = new pt_sa_selectUser.recordData(
								records[i].get("USERNAME"),
								records[i].get("USERID"),
								records[i].get("INNERID"),
								records[i].get("EMAIL"),
								records[i].get("DOMAINIID"),
								records[i].get("DOMAINNAME"));
				}
				pt_sa_selectUser.returnMultiple();
		}
	
	// �رմ���
	pt_sa_selectUser.closeWin = function() {
		var div="<%=div%>";
		if(div != ""){
			window.close();
		}else{
			var win1 = Ext.getCmp("pt_sa_SelectUser");
			win1.close();
		}
	}
	
	// ������ѡ��¼
	pt_sa_selectUser.returnMultiple = function() {
		if(selectedArray.length==0){
			pt.ui.quickTips.msg('<%=PlatformLocaleUtility.getLocaleString(request,"common.prompt")%>','<%=PlatformLocaleUtility.getLocaleString(request,"pt.prip.err.selMultiple")%>');
			return false;
		}
		var selUsers = "";
		var arrUserIID = new Array();
		var arrUserId = new Array();
		var arrUserName = new Array();
		var arrUserEMail = new Array();
		var arrDomainIID = new Array();
		var arrDomainName = new Array();
		var isWorkflow = 'false';
		if(selectedArray.length<1){
			pt.ui.quickTips.msg('<%=PlatformLocaleUtility.getLocaleString(request,"common.prompt")%>','<%=PlatformLocaleUtility.getLocaleString(request,"pt.prip.err.selMultiple")%>');
			return false;
		}
		 var ds = '@[';
		 var de = ']';
		 for(var i = 0 ; i < selectedArray.length; i++){
			var domainName='';
			if(null!=selectedArray[i].domainIID&&selectedArray[i].domainIID!=undefined&&selectedArray[i].domainIID.length>0&&selectedArray[i].domainIID!='-1'&&isWorkflow=='true'){
				domainName = "["+selectedArray[i].domainName+"]";
			}
			selUsers = selUsers + selectedArray[i].userID + "(" +  selectedArray[i].userName + domainName+"); ";
			if(null!=selectedArray[i].domainIID&&selectedArray[i].domainIID!=undefined&&selectedArray[i].domainIID.length>0&&selectedArray[i].domainIID!='-1'&&isWorkflow=='true'){
				arrUserIID[i] = selectedArray[i].innerID+ ds + selectedArray[i].domainIID + de;
			}else{
				arrUserIID[i] = selectedArray[i].innerID;
			}
			arrUserId[i] = selectedArray[i].userID;
			if(null!=selectedArray[i].domainIID&&selectedArray[i].domainIID!=undefined&&selectedArray[i].domainIID.length>0&&selectedArray[i].domainIID!='-1'&&isWorkflow=='true'){
				arrUserName[i] = selectedArray[i].userName+"["+selectedArray[i].domainName+"]";
			}else{
				arrUserName[i] = selectedArray[i].userName;
			}
			arrUserEMail[i] = selectedArray[i].email;
			if(null==selectedArray[i].domainIID||selectedArray[i].domainIID==undefined||selectedArray[i].domainIID.length<1||selectedArray[i].domainIID=='-1'){
				arrDomainIID[i] = "-1";
			}else{
			arrDomainIID[i] = selectedArray[i].domainIID;
			}
			arrDomainName[i] = selectedArray[i].domainName;
		}
				
		var returnObj = {
							selUsers :selUsers ,
							arrUserIID:arrUserIID,
							arrUserId:arrUserId,
							arrUserName:arrUserName,
							arrUserEMail:arrUserEMail,
							arrDomainIID:arrDomainIID,
							arrDomainName:arrDomainName
						}
		if(returnType == null || returnType=='arrayObj' ){
			var cfn =callbackFun+'(returnObj)';
			eval(cfn);
		}
		else if(returnType == 'json'){
			var returnUserJson =  Ext.util.JSON.encode(selectedArray);
			var cfn =callbackFun+'(returnUserJson)';
			eval(cfn);
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
</script>
<ext:ext newRoot="true">
<ext:window id="pt_sa_SelectUser" title='<%=PlatformLocaleUtility.getLocaleString(request,"pt.prip.title.selUser")%>' height="500" width="600" layout="border" modal="true" resizable="true" closable="<%=closable %>" div="<%=div %>" draggable="<%=draggable %>">
	<ext:tree   rootID="root" rootText="<%=sRootName%>" 
				rootIcon="<%=url_rootIcon%>"  
				treeID="tree" treeUrl="<%=url_tree%>" isItem="true"
	            parentCheckbox="false" checkMode="cascade" region="west" onClick="pt_sa_selectUser.dblclick(node)" width="180" >
	</ext:tree>
	<ext:panel id="pt_sa_selectUser_rightPanel" region="center" layout="border">
	<%if("multiple".equals(sSelMode)){%>
		<ext:grid id="grid_pt_sa_selectUser" url="<%=url_grid1%>" orderField="UserName" checkbox="true" pageSize="10" useFilter="true" filterPrefix="true"
			 region="north" height="300" onDblClick="pt_sa_selectUser.addRecords" isItem="true"
			 > 
			<ext:column field="INNERID" title="INNERID" hidden="true" />
			<ext:column field="USERNAME" width="100" title='<%=PlatformLocaleUtility.getLocaleString(request,"pt.common.username")%>'  compare="exlike" filterType="string"  />
			<ext:column field="USERID" width="100" title='<%=PlatformLocaleUtility.getLocaleString(request,"pt.common.loginname")%>'  compare="exlike" filterType="string" />
			<ext:column field="EMAIL" width="120" hidden="true" title='<%=PlatformLocaleUtility.getLocaleString(request,"pt.common.emailaddress")%>'/>
			<ext:column field="DOMAINIID"  sortable="false" title="DOMAINIID" hidden="true" />
			<ext:column field="DOMAINNAME"  sortable="false" title="��" />
		</ext:grid>
		<ext:toolBar id="bar_pt_sa_addUser" >
			<ext:button onClick="pt_sa_selectUser.addRecords" iconCls="add" text='<%=PlatformLocaleUtility.getLocaleString(request,"pt.common.add")%>' />
			<ext:button onClick="pt_sa_selectUser.deleteRecord" iconCls="delete" text='<%=PlatformLocaleUtility.getLocaleString(request,"pt.common.remove")%>'/>
		</ext:toolBar>
		<ext:grid id="grid_pt_sa_alreadySelcetUser"  url="<%=url_grid2%>" orderField="UserName" checkbox="true" autoLoad="false"
			region="center" height="220" topBar="bar_pt_sa_addUser" onDblClick="pt_sa_selectUser.deleteRecord" pageButtonAlign="none" isItem="true"
			 > 
			<ext:column field="INNERID"  sortable="false" title="INNERID" hidden="true" />
			<ext:column field="USERNAME"  sortable="false" width="100" title='<%=PlatformLocaleUtility.getLocaleString(request,"pt.common.username")%>' />
			<ext:column field="USERID"  sortable="false" width="100" title='<%=PlatformLocaleUtility.getLocaleString(request,"pt.common.loginname")%>' />
			<ext:column field="EMAIL" hidden="true" sortable="false" width="120" title='<%=PlatformLocaleUtility.getLocaleString(request,"pt.common.emailaddress")%>'/>
			<ext:column field="DOMAINIID"  sortable="false" title="DOMAINIID" hidden="true" />
			<ext:column field="DOMAINNAME"  sortable="false" title="��" />
		</ext:grid>
	<%}else if("single".equals(sSelMode)){ %>
		<ext:grid id="grid_pt_sa_selectUser" url="<%=url_grid1%>" useFilter="true" filterPrefix="true" checkbox="false" region="center" onDblClick="pt_sa_selectUser.returnOne " isItem="true" > 
			<ext:column field="INNERID" title="INNERID" hidden="true" />
			<ext:column field="USERNAME" width="100" title='<%=PlatformLocaleUtility.getLocaleString(request,"pt.common.username")%>'   filterType="string"  />
			<ext:column field="USERID" width="100" title='<%=PlatformLocaleUtility.getLocaleString(request,"pt.common.loginname")%>'  filterType="string" />
			<ext:column field="EMAIL" hidden="true" width="120" title='<%=PlatformLocaleUtility.getLocaleString(request,"pt.common.emailaddress")%>'/>
			<ext:column field="DOMAINIID"  sortable="false" title="DOMAINIID" hidden="true" />
			<ext:column field="DOMAINNAME"  sortable="false" title="��" />
		</ext:grid>
	<%} %>
	</ext:panel>
			<ext:button onClick="pt_sa_selectUser.submit" iconCls="yes" text='<%=PlatformLocaleUtility.getLocaleString(request,"common.OK")%>' />
			<ext:button onClick="pt_sa_selectUser.closeWin" iconCls="cancel" text='<%=PlatformLocaleUtility.getLocaleString(request,"common.cancel")%>' />
</ext:window>
pt.sa.showWindow('pt_sa_SelectUser');
</ext:ext>
