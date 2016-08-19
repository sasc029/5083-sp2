<%@page import="java.net.URLEncoder"%>
<%@page import="jmathlib.toolbox.jmathlib.system.foreach"%>
<%@page import="com.bjsasc.plm.util.JsonUtil"%>
<%@page import="com.bjsasc.plm.core.view.ViewManageable"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.bjsasc.plm.core.system.principal.User"%>
<%@page import="com.bjsasc.plm.core.context.ContextHelper"%>
<%@page import="com.bjsasc.plm.grid.GridUtil"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Map"%>
<%@page import="com.bjsasc.plm.core.session.SessionHelper"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page language="java"%>
<%@page session="true"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="java.util.List"%>
<%@page import="com.cascc.avidm.login.model.PersonModel"%>
<%@page import="com.cascc.avidm.util.AvidmConstDefine"%>
<%@page import="com.bjsasc.plm.core.folder.*"%>
<%@page import="com.bjsasc.plm.core.persist.PersistHelper"%>
<%@page import="com.bjsasc.plm.common.search.ViewProviderManager"%>
<%@page import="com.bjsasc.plm.core.workspace.Workspace"%>
<%@page import="com.bjsasc.plm.workspace.util.WorkspaceUtil"%>
<%
	//�������
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	// ҳ��ʹ��UTF-8����
	request.setCharacterEncoding("UTF-8");
	String data=null;
	if(request.getParameter(KeyS.DATA)!=null){
		data = request.getParameter(KeyS.DATA);
		data  = URLDecoder.decode(data, "UTF-8");
	}
	if(data==""){
		data=null;
	}
	String condition="";
	if(request.getParameter("Record")!="" && request.getParameter("Record")!=null){
		String database=request.getParameter("Record");
		condition= URLEncoder.encode(database);
	}
	else{
		
	}
	//��ȡҳ�洫��Ĳ���	
	String param = request.getParameter("param");
	String getOneOid = session.getAttribute("getOneOid")+"";
	String callBack = request.getParameter(KeyS.CALLBACK);//�ص�����
	Context ctx = null;
	Persistable pt = PersistHelper.getService().getObject(getOneOid);
	
	String innerId=request.getParameter("INNERID");
	String viewoid=request.getParameter("viewOID");
	if(viewoid==null){
		viewoid="";
	}
	if(innerId!=null){
		Workspace workspace = WorkspaceUtil.getManager().getWorkspace(innerId);
		ctx = workspace.getContextInfo().getContext();
	}
	
	//�������������й��˳���ǰ�û��μӵ�������
	List<Context> contexts = Helper.getContextService().findAllAppContexts();
	User user = SessionHelper.getService().getUser();
	List<Context> contextList = ContextHelper.getService().filterContextsByUserPrincipal(contexts, user);
	
	String path = request.getContextPath();
	String singleSelect = request.getParameter("single");
	boolean single = false;
	if (singleSelect != null && "true".equals(singleSelect)) {
		single = true;
	}

	String keyword = request.getParameter("keyword");
	boolean initsearch = true;
	if (keyword == null || keyword=="" ||keyword.equals("*")) {
		initsearch = false;
		keyword = "";
	}
	boolean isRequest = false;
	String types = request.getParameter("types");
	String typeNames = null;
	if (types != null) {
		isRequest = true;
		typeNames = ViewProviderManager.getInstance().getTypeNames(types);
	} else {
		Map<String,String> tm=ViewProviderManager.getInstance().getSearchTypeNames();
		typeNames="ȫ��";
		types=tm.get("types");
		 
	}

	PersonModel person = (PersonModel) session
			.getAttribute(AvidmConstDefine.SESSIONPARA_USERINFO);
	//ģ��id
	String nodeIID = "";
	String classid = "ECI";
	String contextiid = "ECI";

	String filteriid = request.getParameter("filteriid");
	Folder tfolder = null;
	String foldername = "";
	if (nodeIID != null && !nodeIID.equals("")) {
		tfolder = (Folder) PersistHelper.getService().getObject(
				classid, nodeIID);
	}
	//����
	String spot="DistributePartSearch";
		 //���ݸ������û�ȡ�����ľ�������
	String spotInstance=PersistHelper.getService().getOid((SessionHelper.getService().getUser()));
	List list = new Vector();
	GridDataUtil.prepareRowObjects(list,  spot);
	String gridUrl = GridUtil.getDataUrl(spot, spot);
	String viewid=request.getParameter("viewid");
	//String viewid="87279d723119ba734ab0aa25ff3daa20";
	if(viewid==null){
		viewid="";
	}
	//�������Ĺ����ֶ�ֵ"addfield","isequal","fieldvalue"
	String addfield=request.getParameter("addfield");
	String isequal=request.getParameter("isequal");
	String filedvalue=request.getParameter("filedvalue");
%>
<html>
<head>
<title>����</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/plm/javascript/messager.js"></script>
</head>
<body class="openWinPage" onload="<%if(initsearch==true){%>Search()<%}%>"> 
	
<TABLE width=100% height="100%" cellSpacing="0" cellPadding="0" border="0">
	<TR><TD>
		<jsp:include page="/plm/common/actionTitle.jsp">
			<jsp:param name="actionTitle" value="����"/>
		</jsp:include>
	</TD></TR>
	<TR ><TD>
	<form name="searchForm" id="searchForm">
		<table width="100%" class="avidmTable"  cellSpacing="0" cellPadding="0" border="0">
				<TR>
					<td class="left_col AvidmW90">�ؼ��֣�</td>
					<td valign="top">
						<input type="text" class="pt-text" name="keyword" id="keyword" value="<%=keyword%>" style='width: 152px;'/>
					</td>
				</TR>
				<TR>
					<TD class="left_col AvidmW90">������Χ��</TD>
					<TD>
						    <SELECT name="scope" style="width:153px" id="scope">
								<option value="">����������</option>
							  <%
							      for(int i=0; i<contextList.size(); i++){
							    	  Context context = contextList.get(i);
							    	  if(ctx!=null&&ctx.getInnerId().equals(context.getInnerId())){
							  %>
							             <option value="<%=context.getOid()%>" selected><%=context.getName()%></option>
							  <%
							    	  }else{
							  %>
							           <option value="<%=context.getOid()%>"><%=context.getName()%></option>
							  <% 
							    	  }
							     }
							  %> 
						  </SELECT>
						  
					</TD>
				</TR>
				<TR>
					<TD class="left_col AvidmW90">�汾��Χ��</TD>
					<TD><select name="latestinbranch">  
							<option value="">ȫ��</option>
							<option value="1" selected>����</option>
					</select></TD>

				</TR>
				<input type="hidden" name="types" value="Part" />
				
			</table>
		</form>
	</TD></TR>
	
	<TR ><TD >
		<div class="pt-formbutton" text="����" id="submitbutton" onclick="Search();"></div>
		<div class="pt-formbutton" text="���" id="submitbutton" onclick="Clear();"></div>
	</TD></TR>
	<tr><td>&nbsp;</td></tr>
	<TR height="100%"><TD  valign="top">
			<jsp:include page="/plm/common/grid/control/grid_of_table.jsp">
				<jsp:param name="toolbar_modelId" value=""/>
				<jsp:param name="spot" value="<%=spot%>"/>
				<jsp:param name="spotInstance" value="<%=spotInstance%>"/>
				<jsp:param name="gridId" value="myGrid"/>
			</jsp:include> 
	</TD></TR>
</TABLE>
		<script type="text/javascript">
		function selectUser(type){
			//ѡ������
			 var configObj =
			{
			IsModal : "true",
			SelectMode : "multiple",
			returnType : "arrayObj"
			};
			//reObj Ϊ�������ݣ������������͸���returnType ��Ӧ����
			var returnObj = pt.sa.tools.selectUser(configObj);
			if(returnObj){
				var iid=returnObj.arrUserIID;//ѡ���˵�����
				var name=returnObj.arrUserName;
				$("#"+type+"Name").val(name);
				$("#"+type).val(iid);
			}
			
		}
		/**
		 * ��һ���ռ��ֵƴ��һ����������ԣ��Ա��ڰѽ���ֵ���Ϊjson�ַ����ύ����̨
		 */
		function Search() {
			var grid = pt.ui.get("myGrid");
			var keyWord=$("#keyword").val();
			
            if(keyWord=="" || keyWord=="*" ||$.trim(keyWord)==""){
            	messager.showTipMessager({'content':'�������뾫ȷ���������������Ч��','top':false});
            }
			var ids = [ "keyword", "scope", "types", "qname", "qnumber",
					"modifypastdays", "createpastdays","latestinbranch","versionno" ,"modifier","creator","viewid","condition","addfield","isequal","filedvalue"];
            
			var radioIds = [ "blur", "qcreatedate", "qmodifydate" ];
			var dateIds = [ "qmodifydatefrom", "qmodifydateto",
					"qcreatedatefrom", "qcreatedateto" ];
			var types=document.forms["searchForm"].elements["types"].value;
			if(types==null||types==""){
				var tip={title:"��ʾ",message:"ѡ�������Ķ���"};
				pt.ui.alert(tip);
				return;
			}
			var o = {};
			var hasCondition=false;
			for ( var i = 0; i < ids.length; i++) {
				if (document.forms["searchForm"].elements[ids[i]] != null) {
					o[ids[i]] = document.forms["searchForm"].elements[ids[i]].value;
					if(ids[i]=="keyword"||ids[i]=="qname"||ids[i]=="qnumber"){
						if(o[ids[i]]!="")
						hasCondition=true;
					}
				}
			}
			for ( var i = 0; i < radioIds.length; i++) {
				var objs = document.getElementsByName(radioIds[i]);
				if (objs != null) {
					for ( var j = 0; j < objs.length; j++) {
						if (objs[j].checked == true) {
							o[radioIds[i]] = objs[j].value;
						}
					}
				}
			}
			for ( var i = 0; i < dateIds.length; i++) {
				if (document.getElementById(dateIds[i]) != null) {
					o[dateIds[i]] = document.getElementById(dateIds[i]).text;
				}
			}
			//��Ϊ���ò������¼������� 
			//if(hasCondition==false){
			//	plm.showTip(document.getElementById("keyword"), "�����ѯ�������о�ȷ������");
			//	return;
			//}
			//��Ϊ���ò������¼�������
			//grid.reload(o, false);
			plm.startWait();
			$.ajax({
		        type:"post", 
		        data:o,
				url:contextPath+"/ddm/distributeobject/listDistributePartSearchHandle.jsp?oid=<%=getOneOid%>",
		        async: true,
		        dataType:'json',
		        success: function(result){
		        	if(result.SUCCESS != "true"){
		        		plm.showMessage({title:"��ʾ",message:"û������������!"});
		        	};
		        	var newo={start:0,limit:20,url:"<%=gridUrl%>"};
		        	plm.endWait();
	    			grid.reload(newo,false);
			    },         
		        error: function(code){ 
		        	logined = false;
		        } 
			}); 
		}
		function selectTypes() {
			//ǰ̨ url
			var url = "<%=request.getContextPath()%>/plm/search/commonsearch/TypesList.jsp?CALLBACK=afterSelectTypes";
			
			//�򿪴���
			plm.openWindow("<%=request.getContextPath()%>/plm/load.html", 200, 400,'T');
			
			//��ǰ̨��ִ��
			plm.openByPost(url, null, 'T');
			
		}
		function afterSelectTypes(retValue){
			if (retValue != null) {
				document.getElementById("typeNames").innerText = retValue.NAMES;
				document.getElementById("types").innerText = retValue.TYPES;
			}
		}
		function doSelect() { 
			//window.returnValue = grid.getSelections(); 
			<%if (callBack != null && !"".equals(callBack)) {%>
			var grid = pt.ui.get("myGrid");
			var data=grid.getSelections();
			if(data==null|| data.length==0){
				var tip={title:"��ʾ",message:"��ѡ�����ݣ�"};
				pt.ui.alert(tip);
				return;
			}
			try {
				opener.<%=callBack%>(data); // �������ڻص�
			} catch (e) {
				try {
					window.parent.dialogArguments.<%=callBack%>(data); // ģʽ���ڻص�
				} catch(e) {
				
				}
			} 
			<%}%>
			window.close();
			}

			function cancelMe() {
				window.close();
			}
			function clearValue(){
				$("#creator").val("");
				$("#creatorName").val("");
			}
			function clearValue2(){
				$("#modifier").val("");
				$("#modifierName").val("");
			}
			
			function Clear(){
				$("#keyword").val("");
			}
			
			function getSelections(){
				var grid = pt.ui.get("myGrid");
				var data=grid.getSelections();
				if(data.length == 0){
					return null;
				}else{
					var oid = data[0].OID;
					return oid;
				}
				
			}
		</script>
</body>
</html>