
<%
	/**
	 *  ҳ��˵��
	 *  1.���ƣ� 
	 *  2.���ã� ����ѡ����֯ҳ�����
	 *  3.������ div��  ���div��ֵ��ѡ��ҳ����Ⱦ��һ���´򿪵�IE�����в�����ext window�ؼ�
	 *		    SelectMode ��   ѡ��ģʽ����ѡ��single�����߶�ѡ��multiple�� Ĭ��Ϊ��multiple��
	 *			callbackFun:  �ص�������
	 *			returnType:   ����ֵ��ʽ����ѡֵ��arrayObj������json����Ĭ��Ϊ��arrayObj��
			 	scope: ��֯�б�Χ��all��ȫ����֯����self����ָ�����г�������֯����ref����ָ�����������֯����path��ָ�����Լ������ȳ����Ľ�ɫ
			 			����ƶ�������֯����Ĭ��Ϊ��ʾ������֯������ƶ�����Ӧ��������ʾ������֯
				domainRef ���innerid Ĭ��Ϊ��ǰ��¼����
	 
	 */
%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@ page language="java"%>
<%@ page session="true"%>
<%@ page import="com.cascc.avidm.util.*"%>
<%@ page import="com.bjsasc.platform.i18n.*"%>
<%@ page import="com.cascc.platform.aa.api.util.AAUtil"%>
<%@ page import="com.cascc.avidm.login.model.*"%>
<%@ page import="com.cascc.platform.aa.api.util.*"%>
<%@ page import="com.cascc.platform.aa.*"%>
<%@ page import="com.cascc.platform.aa.api.data.*"%>
<%@page import="com.cascc.platform.aa.api.util.AATreeNodeSet"%>
<%@page import="com.cascc.platform.aa.api.util.AAMappingType"%>

<%@ taglib uri="/WEB-INF/framework-ext.tld" prefix="ext"%>
<%
	//�������
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	request.setCharacterEncoding("UTF-8");

	PersonModel person = (PersonModel) session.getAttribute("person");
	int domainIID = person.getDomainRef();
	//���div��ֵ��ѡ��ҳ����Ⱦ��һ���´򿪵�IE�����в�����ext window�ؼ�
	String div = request.getParameter("div");
	String closable = "true";
	if (div == null || div.trim().length() == 0) {
		div = "";
		closable = "true";
	} else {
		closable = "false";
	}

	//�����������ͣ�Ĭ��ΪarrayObj��ȡֵ����{"arrayObj"��"json"}
	String returnType = request.getParameter("returnType");
	if (returnType == null || returnType.trim().length()==0||"null".equals(returnType)) {
		returnType = "arrayObj";
	}
	//�ص�������
	String callbackFun = request.getParameter("callbackFun");
	if (callbackFun == null) {
		callbackFun = "callbackFun";
	}

	//ѡ��ģʽ����ѡ���߶�ѡ��Ĭ��Ϊ��ѡ��ȡֵ����{"single"��"multiple"}
	String sSelMode = request.getParameter("SelectMode");
	if (sSelMode != null && sSelMode.trim().equalsIgnoreCase("single")) {
		sSelMode = "single";
	} else {
		sSelMode = "multiple";
	}
	int loginDomainRef = person.getDomainRef();
	//��֯�б�ķ�Χ
	String scope = request.getParameter("scope");
	String domainRef = request.getParameter("domainIID");
	//���û�и����ʶ����ȡ��ǰ��
	if(domainRef == null || "".equals(domainRef.trim()) || "null".equals(domainRef)){
		domainRef = domainIID+"";
	}
	
	//����ƶ�������֯����Ĭ��Ϊ��ʾ������֯������ƶ�����Ӧ��������ʾ������֯
	if(scope == null ||"".equals(scope.trim()) || "null".equals(scope)){
			scope = AADomainUtil.VISTYPE_SELF;
	}
	
	String sRootName = AAUtil.getTreeRootGlossary(null);
	String url_tree = request.getContextPath()
			+ "/platform/public/selectorg/SelectOrgAction.jsp?op=getTree&scope="+scope+"&domainRef="+domainRef;
	String url_grid1 = request.getContextPath()
			+ "/platform/public/selectorg/SelectOrgAction.jsp?op=getOrg&scope="+scope+"&domainRef="+domainRef;
	String url_grid2 = "";
	String url_rootIcon = request.getContextPath()
			+ "/platform/images/folder/Doc_tree_root.gif";
%>


<script type="text/javascript">
	Ext.namespace("pt_sa_selectOrg");//�����ռ�
	var selectedArray = new Array(); //��ѡ����
	var returnType = "<%=returnType %>" ; //��������
	var callbackFun ="<%=callbackFun%>";
	//��Ա��Ϣ����
	pt_sa_selectOrg.recordData = function(INNERID, ID, NAME, DOMAINIID, DOMAINNAME) {
		this.INNERID = INNERID;
		this.ID = ID
		this.NAME = NAME;
		this.DOMAINIID = DOMAINIID;
		this.DOMAINNAME = DOMAINNAME;
	}
	//�������¼�
	pt_sa_selectOrg.dblclick = function(node) {
		    var grid_pt_sa_selectOrg = Ext.getCmp('grid_pt_sa_selectOrg');
		    var limit = grid_pt_sa_selectOrg_pageToolBar.pageSize;
			var id = node.id;
			var openUrl = node.attributes.openUrl;//�õ����ڵ��ϵ�url
			url = openUrl;
			if(url == null || url.length==0){
				url = '<%=url_grid1%>';
			}
			grid_pt_sa_selectOrg.getStore().proxy = new Ext.data.HttpProxy(
					{
						method :"POST",
						url :url
					});
			grid_pt_sa_selectOrg.getStore().load( {
				params : {
					start :0,
					limit :limit
				}
			});
	}
	
	//����ѡ����Ա��ӵ���ѡ��Ա����
	pt_sa_selectOrg.addRecords = function() {
			var grid_pt_sa_selectOrg = Ext.getCmp("grid_pt_sa_selectOrg");
			var grid_pt_sa_alreadySelcetOrg = Ext.getCmp("grid_pt_sa_alreadySelcetOrg");
			var selections = grid_pt_sa_selectOrg.getSelectedRow();
			if(selections.length==0){
				pt.ui.quickTips.msg('<%=PlatformLocaleUtility.getLocaleString(request,"common.prompt")%>','<%=PlatformLocaleUtility.getLocaleString(request,"pt.prip.err.selMultiple")%>');
				return false;
			}
			var records = new Array();
			for ( var i = 0; i < selections.length; i++) {
				var newRecord = selections[i].copy();
				records[i] = newRecord;
			}
			grid_pt_sa_selectOrg.clearSelections();
			for ( var i = 0; i < records.length; i++) {
				var id = records[i].get("INNERID");
				var number = grid_pt_sa_alreadySelcetOrg.indexOf("INNERID", id);
				records[i].id = id;
				if (number == -1) {
					selectedArray[selectedArray.length] = new pt_sa_selectOrg.recordData(
							records[i].get("INNERID"),
							records[i].get("ID"),
							records[i].get("NAME"),
							records[i].get("DOMAINIID"),
							records[i].get("DOMAINNAME"));
					var a = new Ext.data.Record.create( 
							[ 
								{name :'INNERID'},
								{name :'ID'}, 
								{name :'NAME'}, 
								{name :'DOMAINIID'}, 
								{name :'DOMAINNAME'} 
							], 
								{id :id}
					);
					var record = new a( {
						INNERID :records[i].get("INNERID"),
						ID :records[i].get("ID"),
						NAME :records[i].get("NAME"),
						DOMAINIID :records[i].get("DOMAINIID"),
						DOMAINNAME :records[i].get("DOMAINNAME")
					});
					grid_pt_sa_alreadySelcetOrg.addRow(record);
				}
			}
			grid_pt_sa_alreadySelcetOrg.refreshView();
		}
	
	//ɾ����ѡ��Ա�б��е���Ŀ
	pt_sa_selectOrg.deleteRecord = function() {
			var grid_pt_sa_alreadySelcetOrg = Ext.getCmp("grid_pt_sa_alreadySelcetOrg");
			var selections = grid_pt_sa_alreadySelcetOrg.getSelectedRow();
			if(selections.length==0){
				pt.ui.quickTips.msg('<%=PlatformLocaleUtility.getLocaleString(request,"common.prompt")%>','<%=PlatformLocaleUtility.getLocaleString(request,"pt.prip.err.selMultiple")%>');
				return false;
			}
			var temp = selectedArray;
			for ( var i = 0; i < selections.length; i++) {
				for ( var j = 0; j < temp.length; j++) {
					if (temp[j].INNERID == selections[i].get("INNERID")) {
						selectedArray.removeAt(j);
					}
				}
				grid_pt_sa_alreadySelcetOrg.deleteRow(selections[i]);
			}
			grid_pt_sa_alreadySelcetOrg.refreshView();
		}
	
	//��ѡ�����û� 
	pt_sa_selectOrg.returnOne = function(){
			var grid_pt_sa_selectOrg = Ext.getCmp("grid_pt_sa_selectOrg");
			var selections = grid_pt_sa_selectOrg.getSelectedRow();
				if(selections.length != 1){
					pt.ui.quickTips.msg('<%=PlatformLocaleUtility.getLocaleString(request,"common.prompt")%>','<%=PlatformLocaleUtility.getLocaleString(request,"pt.prip.err.selSingle")%>');
					return false;	
				}
				var records = new Array();
				for ( var i = 0; i < selections.length; i++) {
					var newRecord = selections[i].copy();
					records[i] = newRecord;
				}
				grid_pt_sa_selectOrg.clearSelections();
				for ( var i = 0; i < records.length; i++) {
						selectedArray[selectedArray.length] = new pt_sa_selectOrg.recordData(
								records[i].get("INNERID"),
								records[i].get("ID"),
								records[i].get("NAME"),
								records[i].get("DOMAINIID"),
								records[i].get("DOMAINNAME"));
				}
				pt_sa_selectOrg.returnMultiple();
		}
	// �رմ���
	pt_sa_selectOrg.closeWin = function() {
		var div="<%=div%>";
		if(div != ""){
			window.close();
		}else{
			var win1 = Ext.getCmp("pt_sa_selectOrg_win");
			win1.close();
		}
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
	
	
	// ������ѡ��Ա
	pt_sa_selectOrg.returnMultiple = function() {
			if(selectedArray.length==0){
				pt.ui.quickTips.msg('<%=PlatformLocaleUtility.getLocaleString(request,"common.prompt")%>','<%=PlatformLocaleUtility.getLocaleString(request,"pt.prip.err.selMultiple")%>');
				return false;
			}
		var arrDivIID = new Array();	
		var arrDivID = new Array();
		var arrDivName = new Array();
		var arrDomainIID = new Array();
		var arrDomainName = new Array();
		if(selectedArray.length<1){
			pt.ui.quickTips.msg('<%=PlatformLocaleUtility.getLocaleString(request,"common.prompt")%>','<%=PlatformLocaleUtility.getLocaleString(request,"pt.prip.err.selMultiple")%>');
			return false;
		}
		 for(var i = 0 ; i < selectedArray.length; i++){
			 arrDivIID[i] = selectedArray[i].INNERID;
			 arrDivID[i] = selectedArray[i].ID;
			 arrDivName[i] = selectedArray[i].NAME;
			 arrDomainIID[i] = selectedArray[i].DOMAINIID;
			 arrDomainName[i] = selectedArray[i].DOMAINNAME;
		}
				
		var returnObj = {
				arrDivIID:arrDivIID ,
				arrDivID: arrDivID,
				arrDivName: arrDivName,
				arrDomainIID: arrDomainIID,
				arrDomainName: arrDomainName
		}
		if(returnType == null || returnType=='arrayObj' ){
			var cfn =callbackFun+'(returnObj)';
			eval(cfn);
		}
		else if(returnType == 'json'){
			var returnOrgJson =  Ext.util.JSON.encode(selectedArray);
			var cfn =callbackFun+'(returnOrgJson)';
			eval(cfn);
		}
		pt_sa_selectOrg.closeWin();
	}
</script>
<ext:ext newRoot="true">
<ext:window id="pt_sa_selectOrg_win" title='<%=PlatformLocaleUtility.getLocaleString(request,"pt.prip.title.selOrg")%>' height="500" width="600" layout="border" modal="true" resizable="true" closable="<%=closable %>" div="<%=div %>" >
	<ext:tree   rootID="root" rootText="<%=sRootName%>" 
				rootIcon="<%=url_rootIcon%>"  
				treeID="tree" treeUrl="<%=url_tree%>" isItem="true"
	            parentCheckbox="false" checkMode="cascade" region="west" onClick="pt_sa_selectOrg.dblclick(node)" width="180" >
	</ext:tree>
	<ext:panel id="pt_sa_selectOrg_rightPanel" region="center" layout="border">
	<%if("multiple".equals(sSelMode)){%>
		<ext:toolBar id="bar_pt_sa_addOrg" >
			<ext:button onClick="pt_sa_selectOrg.addRecords" iconCls="add" text='<%=PlatformLocaleUtility.getLocaleString(request,"pt.common.add")%>' />
			<ext:button onClick="pt_sa_selectOrg.deleteRecord" iconCls="delete" text='<%=PlatformLocaleUtility.getLocaleString(request,"pt.common.remove")%>'/>
		</ext:toolBar>
		<ext:grid id="grid_pt_sa_selectOrg" url="<%=url_grid1%>"  checkbox="true" pageSize="10" useFilter="true" filterPrefix="true" orderField="WEIGHT" orderDirection="ASC"
			 region="north" height="300" onDblClick="pt_sa_selectOrg.addRecords" isItem="true"
			 > 
			<ext:column field="INNERID"  title="INNERID" hidden="true" />
			<ext:column field="ID" title="ID" hidden="true" />
			<ext:column field="NAME" title='<%=PlatformLocaleUtility.getLocaleString(request,"common.name")%>' compare="exlike" filterType="string" width="230"/>
			<ext:column field="DOMAINIID" title="DOMAINIID" hidden="true" />
			<ext:column field="DOMAINNAME" title="��" sortable="false"/>
		</ext:grid>
		<ext:grid id="grid_pt_sa_alreadySelcetOrg"  url="<%=url_grid2%>"  checkbox="true" autoLoad="false"
			region="center" height="220" topBar="bar_pt_sa_addOrg"  onDblClick="pt_sa_selectOrg.deleteRecord" pageButtonAlign="none" isItem="true"
			 > 
			<ext:column field="INNERID"   sortable="false" title="INNERID" hidden="true" />
			<ext:column field="ID"  sortable="false" title="ID" hidden="true" />
			<ext:column field="NAME"  sortable="false" title='<%=PlatformLocaleUtility.getLocaleString(request,"common.name")%>' width="230"/>
			<ext:column field="DOMAINIID"  sortable="false" title="DOMAINIID" hidden="true" />
			<ext:column field="DOMAINNAME"  sortable="false" title="��" />
		</ext:grid>
	<%}else if("single".equals(sSelMode)){ %>
		<ext:grid id="grid_pt_sa_selectOrg" url="<%=url_grid1%>" useFilter="true" filterPrefix="true" checkbox="false" region="center" orderField="WEIGHT" orderDirection="ASC"  onDblClick="pt_sa_selectOrg.returnOne" isItem="true" > 
			<ext:column field="INNERID"  title="INNERID" hidden="true" />
			<ext:column field="ID" title="ID" hidden="true" />
			<ext:column field="NAME" title='<%=PlatformLocaleUtility.getLocaleString(request,"common.name")%>' compare="exlike" filterType="string" width="230"/>
			<ext:column field="DOMAINIID" title="DOMAINIID" hidden="true" />
			<ext:column field="DOMAINNAME" title="��" sortable="false"/>
		</ext:grid>
	<%} %>
	</ext:panel>
			<ext:button onClick="pt_sa_selectOrg.submit" iconCls="yes" text='<%=PlatformLocaleUtility.getLocaleString(request,"common.OK")%>' />
			<ext:button onClick="pt_sa_selectOrg.closeWin" iconCls="cancel" text='<%=PlatformLocaleUtility.getLocaleString(request,"common.cancel")%>' />
</ext:window>
pt.sa.showWindow('pt_sa_selectOrg_win');
</ext:ext>
