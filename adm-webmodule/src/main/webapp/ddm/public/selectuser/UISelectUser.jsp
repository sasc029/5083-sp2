<%
	/**
	 *  页面说明
	 *  1.名称：
	 *  2.作用： 公共选人页面入口
	 *  3.参数：
	 *		    SelectMode ：   选择模式，单选“single”或者多选“multiple” 默认为“multiple”
	 *			callbackFun:  回调函数名
	 *			returnType:   返回值样式，可选值“arrayObj”，“json”，默认为“arrayObj”
	 			scope: 人员列表范围【all】全域用户；【self】在指定域中出生的用户；【ref】和指定域关联的用户
	 			domainRef 域的innerid 默认为当前登录的域
	 			userStatus:用户类型，A 激活(默认)，F 冻结 ALL 全部
	 			IsModal 是否是模态
	 */
%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>


<%@page import="com.cascc.platform.aa.api.util.AADomainUtil"%>
<%@page import="com.cascc.platform.aa.api.util.AAUtil"%>
<%@page import="com.cascc.platform.aa.AAProvider"%>
<%@page import="com.cascc.avidm.login.model.PersonModel"%>
<%@page import="com.bjsasc.platform.i18n.PlatformLocaleUtility"%>
<%
	//清除缓存
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	request.setCharacterEncoding("UTF-8");

	//控制域下管理员配置人员的选择范围
	//系统管理员：只能选择用户类型是系统管理员AvidmConstDefine.SYSADM_USERTYPE_SYSMGR
	//安全管理员：只能选择用户类型是安全管理员AvidmConstDefine.SYSADM_USERTYPE_SECURITYMGR
	//审计管理员：只能选择用户类型是审计管理员AvidmConstDefine.SYSADM_USERTYPE_LOGMGR
	String configAdmin = request.getParameter("configAdmin");
	int gridWidth = 210 ;
	if(null != configAdmin && configAdmin.equals("configAdmin")){
		gridWidth = 300 ;
	}
	PersonModel person = (PersonModel) session.getAttribute("person");
	int loginDomainRef = person.getDomainRef();
	//如果div有值则选人页面渲染到一个新打开的IE窗口中不再是ext window控件
	String div = request.getParameter("div");
	String closable = "true";
	//返回数据类型，当前管理员类型
	String userStatus = request.getParameter("userStatus");
	if (userStatus == null||userStatus.trim().length()==0||"null".equals(userStatus)) {
		userStatus = "A";
	}
	//返回数据类型，默认为arrayObj，取值集合{"arrayObj"，"json"}
	String returnType = request.getParameter("returnType");
	if (returnType == null||returnType.trim().length()==0||"null".equals(returnType)) {
		returnType = "arrayObj";
	}
	//回调函数名
	String callbackFun = request.getParameter("callbackFun");
	if(callbackFun==null){
		callbackFun="callbackFun";
	}
	//用户分类
	String pClass = request.getParameter("pClass");
	if(pClass==null){
		pClass="user";
	}

	//选择模式：单选或者多选，默认为多选，取值集合{"single"，"multiple"}
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
	//用户列表的范围
	String scope = request.getParameter("scope");
	String domainRef = request.getParameter("domainRef");
	//如果没有给域标识，则取当前域
	if(domainRef == null || "".equals(domainRef.trim()) || "null".equals(domainRef)){
		domainRef = loginDomainRef+"";
	}
	if(scope == null ||"".equals(scope.trim()) || "null".equals(scope)){
			scope = AADomainUtil.VISTYPE_SELF;
	}
	if(AADomainUtil.VISTYPE_ALL.equals(scope)){
		domainRef="-1";
	}
	//tree 数据源
	String url_tree = request.getContextPath()
			+ "/platform/public/selectuser/UISelectUserAction.jsp?op=getTree&domainRef="
			+ domainRef + "&scope=" + scope+"&userStatus="+userStatus+"&treeNode=root";
	//grid 数据源
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
  var pt_sa_selectUser={};//命名空间
	var returnType = "<%=returnType %>"; //返回类型
	var callbackFun ="<%=callbackFun%>";

	//树单击事件
	pt_sa_selectUser.dblclick = function(node) {
		    var grid_pt_sa_selectUser = pt.ui.get('grid_pt_sa_selectUser');
			var id = node.id;
			var openUrl = node.openUrl;//得到树节点上的url
			url =openUrl;
			if(url ==null || url.length==0){
				return;
			}
			grid_pt_sa_selectUser.reload({url:url,page:1});
	}

	//将已选的人员添加到已选人员栏中
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
			//分发类型是“电子”，分发分数不可修改
			grid_pt_sa_alreadySelcetUser.on('beforecelledit', function(e){
				var DISMEDIATYPE = e.record.DISMEDIATYPE;
				var column = e.column;
				if(column.id=="DISINFONUM" && (DISMEDIATYPE=="电子" || DISMEDIATYPE=="1")){
					return false;
				}
				else{
					return true;
				}
			});
			//分发类型是“电子”，分发分数设为“1”
			grid_pt_sa_alreadySelcetUser.on('aftercelledit', function(e){
				var DISMEDIATYPE = e.record.DISMEDIATYPE;
				var column = e.column;
				var row =e.rowIndex;
				if(column.id=="DISMEDIATYPE" && (DISMEDIATYPE=="电子" || DISMEDIATYPE=="1")){
					grid_pt_sa_alreadySelcetUser.setColumnValue("DISINFONUM",1,row);
				}
			});
		}

	//删除已选人员列表中的条目
	pt_sa_selectUser.deleteRecord = function() {
			var grid_pt_sa_alreadySelcetUser = pt.ui.get("grid_pt_sa_alreadySelcetUser");
			var selections = grid_pt_sa_alreadySelcetUser.getSelections();
			if(selections.length==0){
				alert("<%=PLU.getString("pmp.noObj")%>");
				return false;
			}
			grid_pt_sa_alreadySelcetUser.deleteRow(selections);
		}

	//单选返回用户
	pt_sa_selectUser.returnOne = function(){
			var grid_pt_sa_selectUser = pt.ui.get("grid_pt_sa_selectUser");
			var selections = grid_pt_sa_selectUser.getSelections();
			if(selections.length != 1){
				alert("<%=PLU.getString("pmp.noObj")%>");
				return false;
			}
			returnSelections(selections);
		}

	// 关闭窗口
	pt_sa_selectUser.closeWin = function() {
		window.close();
	}

	//多选返回
	pt_sa_selectUser.returnMultiple = function() {
		var grid_pt_sa_alreadySelcetUser = pt.ui.get("grid_pt_sa_alreadySelcetUser");
		var selections = grid_pt_sa_alreadySelcetUser.getRows();
		if(selections.length==0){
			alert("<%=PLU.getString("pmp.noObj")%>");
			return false;
		}
		returnSelections(selections);
	}

	//返回所选记录
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
				alert("第（"+chkIndex+"）行的分发介质类型不能为空！");
				return false;
			}
			
			var numReg=/^\d+$/;
			if (null == arrDisInfoNum[i] || arrDisInfoNum[i] == undefined || arrDisInfoNum[i].length == 0) {
				alert("第（"+chkIndex+"）行的分发份数不能为空！");
				return false;
			}
			if (!numReg.test(arrDisInfoNum[i])) {
				alert("第（"+chkIndex+"）行的分发份数不是数字！");
    	    	return false;
    		} else if (parseInt(arrDisInfoNum[i]) <= 0) {
				alert("第（"+chkIndex+"）行的分发份数不能为 0 ！");
    	    	return false;
    		}else if(parseInt(arrDisInfoNum[i])>= 9999999999999999999999999){
    			alert("第（"+chkIndex+"）行的分发份数太大！");
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

	//确定按钮触发事件
	pt_sa_selectUser.submit = function(){
		if("single" == "<%=sSelMode%>"){
			pt_sa_selectUser.returnOne();
		}
		else if("multiple" == "<%=sSelMode%>"){
			pt_sa_selectUser.returnMultiple();
		}
	}
	//树上双击展开节点
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
		<!-- tree begin 管理员配置时不显示树的结构-->
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
							   	<!-- <th field="DISMEDIATYPE" width="100" title="" filterType="select" data="inSite" textField="id"  valueField="id" editor="{type:'combo',data:inSite,textField:'name',valueField:'name'}">分发类型</th> -->
							   	<th field="DISMEDIATYPE" width="100" editor="{type:'combo',url:'<%=disMediaTypeURL %>',valueField:'value',textField:'text'}" formatter="selectFormatter()">分发类型</th>
							   	<th field="DISINFONUM" 	 width="80"  editor="{type:'text'}">分发份数</th>
							   	<th field="NOTE" 		 width="145" editor="{type:'text'}">备注</th>
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
              {id: '0', name: '纸质'},
              {id: '1', name: '电子'}
          ];
</script>
</html>