<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.plm.core.domain.Domain"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.core.context.ContextHelper"%>
<%@page import="com.cascc.platform.aa.api.util.AADomainUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.plm.core.context.model.LibraryContext"%>
<%@page import="com.bjsasc.plm.core.context.model.OrgContext"%>
<%@page import="com.bjsasc.plm.core.context.model.ProductContext"%>
<%@page import="com.bjsasc.plm.core.foundation.Helper"%>
<%@include file="/plm/plm.jsp" %>
<%
	String contextPath = request.getContextPath();
	String contextOid = request.getParameter("contextOid");
	String op = request.getParameter("op");
	String gridId = "ddmObjectTypeList";
	String title = "�༭����ģ������";
	String spot = "ListDistributeObjectTypes";
	String toolbar_modelId = "ddm.distributeobjecttype.adddata.toolbar";
	String contentUrl = contextPath + "/ddm/config/objecttype/ddmAddTypeList.jsp?contextOid="+contextOid+"&pageType="+op;
	String dataUrl = contextPath + "/ddm/config/objecttype/ddmObjectTypeAction.jsp?op=data";
	
	Context context = ContextHelper.getService().getContext(contextOid);
	Domain domain = context.getDomain();
	Context orgContext = domain.getContextInfo().getContext();
	String orgOid = orgContext.getOid();

	if ("add".equals(op)) {
		session.setAttribute("DOT_DATA_LIST", null);
		session.setAttribute("DOT_TYPE_DATA_LIST", null);
		title = "��Ӷ���ģ������";
	}
%>
<html>
<head>
	<title><%=title%></title>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>	
	<script type="text/javascript"	src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
	<script type="text/javascript"	src="<%=contextPath%>/plm/javascript/uisplitter.js" charset="UTF-8"></script>
	
	<script type="text/javascript">
		var container = {OID:'<%=contextOid%>'};
		//����splitbar
		$(document).ready(function() {
			$("#table").splitter({
				splitVertical:true,				    //ˮƽ�ָ�Ǵ�ֱ�ָ�,���Ϊtrue,���Ǵ�ֱ�ָ�,Ҳ�������ҷָ�
				A:"tdLeft",						    //���������id,����
				B:"tdRight",					    //�Ҳ�������id,����
				closeableto:20,					    //�Զ����ص���С��Ȼ�߶�,����Ĭ�ϼ���
				slaveleft:"<%=gridId%>",	        //���������grid��tree�ؼ���id
				slaveright:"distributeContent",	    //�Ҳ�������grid��tree�ؼ���id
				retfunc:"plm.resizecontrol"});	    //�ص�����,����Ĭ�ϼ���
		});

		//��Ӧ�û�
		function onClickRow(nodes){
			var tree = pt.ui.get("<%=gridId%>");
			var node = tree.getSelected();
			var link = node["link"];
			showContent(link);
		}
		
		//���ұ�iframe����ʾ����ҳ��
		function showContent(link){
			var contentView = document.getElementById("rightContent");
			//��ʱ��������գ����ж�֮ǰ������
			contentView.contentWindow.location.href="about:blank";
			contentView.contentWindow.location.href = link;
		}
		function onLoad(){
		}
	</script>
</head>
<body class="openWinPage">
<form name="grid_assetplace" method="POST">
	<table width="100%" height="100%" cellSpacing="0" cellPadding="0" border="0">
		<tr class="AvidmActionTitle">
			<td>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="AvidmTitleName"><div class="imgdiv"><img src="<%=contextPath%>/plm/images/common/type.gif"/></div>
    <div class="namediv"><%=title%></div></td>
  </tr>
</table>
			</td>
		</tr>
		<tr height="100%"><td>
			<table width="100%" height="100%" cellSpacing="0" cellPadding="0" border="0">		
			<tr> 
			<td id="tdLeft" class="leftpanel" style="width:25%;">
				<table  style="width:100%;height:100%;" cellSpacing="0" cellPadding="0" border="0">
				<tr><td>
					<table cellSpacing="0" cellPadding="0" width="100%" border="0" style="border-right:#9cbdff solid 1px;border-left:#9cbdff solid 1px;"><tr class="AvidmToolbar"><td> 
						<div class="pt-toolbar">
				<% if ("add".equals(op)) { %>
							<%=UIHelper.getToolBar(toolbar_modelId)%>
				<% } %>
						</div> 
					</td></tr></table>
				</td></tr>
				<tr><td height="100%">
					<table class="pt-grid" id="<%=gridId%>" style="width:100%;height:100%;"
						url="<%=dataUrl%>" pagination="false" useFilter="false" 
						singleSelect="false" fit="true" checkbox="true"	autoLoad="false" rownumbers="true">
						<thead>
							<th field="TYPE" align="center" width="25" tips="����" ></th>
							<th field="NAME" align="center" width="130" tips="��ɫ/��Ա">��ɫ/��Ա</th>
						</thead>
					</table>
				</td></tr></table>
			</td>
			<td id="tdRight">
				<iframe id="rightContent" name="rightContent" style="width:100%;height:100%;"
					src="<%=contentUrl%>"></iframe>
			</td>
			</tr>
			</table>
		</td></tr>
	</table>
</body>
</form>
</html>

	<script type="text/javascript">
	var records = "{}";	
	
	$(document).ready(function() {
		var adt_grid = pt.ui.get("<%=gridId%>");
		adt_grid.reload();
	});
	
	// ȡ��ѡ������
	function getSelections() {
		var table = pt.ui.get("<%=gridId%>");
		var selections = table.getSelections();
		return selections;
	}	
	
	// ��ӽ�ɫ
	function addRoles() {
		var container = {'OID':'<%=orgOid%>'};
		plm.selectRole(records,"doBindRoles",container);
	}

	// ��ӽ�ɫ���ص�������
	function doBindRoles(records){
		if(records==null||records.length<=0){
			plm.showMessage({title:'��ʾ', message:'��ѡ��һ������!', icon:'1'});
			return;
		}
		plm.startWait();
		var roleIIDs = "";
		for(var i = 0; i < records.length; i++){
			if(roleIIDs != ""){
				roleIIDs += ";";
			}
			roleIIDs += records[i]["AAROLEINNERID"];
		}
		var adt_grid = pt.ui.get("<%=gridId%>");
		
		$.ajax({
				type: "post",
				url: "<%=contextPath%>/ddm/config/objecttype/ddmObjectTypeAction.jsp?op=addRole",
				dataType: "json",
		  		data: {SelRoleIID:roleIIDs},
				success: function(result){
					plm.endWait();
					if(result.SUCCESS != null &&result.SUCCESS == "false"){
						plm.showAjaxError(result);
						return;
					}
					adt_grid.reload();
			   },
			   error:function(){
				   plm.endWait();
				   plm.showMessage({title:'������ʾ', message:"�Բ��������������", icon:'1'});
			   }
			});
	}
	
	// ��ӳ�Ա
	function addUsers() {
		<%
		String typeName = "";	
		Object object = Helper.getPersistService().getObject(contextOid);
		if (object instanceof OrgContext || object instanceof LibraryContext || object instanceof ProductContext) {
			typeName = "all";
		}else{
			typeName = "org";
		}
		%>
		var url = "<%=request.getContextPath()%>/plm/system/principal/selectUserTree.jsp?type=<%=typeName%>&singleSelect=false&contextOid=<%=contextOid%>&callback=doBindUsers&userType=3";
		plm.openWindow(url,900,600,"selectUser");
	}
	
  	// ��ӳ�Ա���ص�������
    function doBindUsers(reObj){
    	var objs =eval(reObj);
		plm.startWait();
    	var userIIDs = "";
		var userNames = "";
	for(var i = 0; i < objs.length; i++){
		if(userIIDs != ""){
			userIIDs += ";";
			userNames += ";";
		}
		userIIDs += objs[i].AAINNERID;
		userNames += objs[i].NAME;
	}
    	var adt_grid = pt.ui.get("<%=gridId%>");
    	
		$.ajax({
			type: "post",
			url: "<%=contextPath%>/ddm/config/objecttype/ddmObjectTypeAction.jsp?op=addUser",
			dataType: "json",
	  		data: {SelUserIID:userIIDs},
			success: function(result){
				plm.endWait();
				if(result.SUCCESS != null &&result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				adt_grid.reload();
		   },
		   error:function(){
			   plm.endWait();
			   plm.showMessage({title:'������ʾ', message:"�Բ��������������", icon:'1'});
		   }
		});
    }	
	
  	// ɾ������
	function deleteDistributeObjectType() {
		var adt_grid = pt.ui.get("<%=gridId%>");
		var data = adt_grid.getSelections();
		if(data.length == 0){
			plm.showMessage({title:'������ʾ', message:"��ѡ�������������", icon:'1'});
			return;
		}
		var msg = "��ȷ��Ҫɾ��������\n";
		if (!plm.confirm(msg)) {
			return;
		}
		plm.startWait();
	 	var DKS = "";
	 	for(var i=0;i<data.length;i++){
	 	 	if(i==0){
	 	 		DKS += data[i]["DK"];
	 	 	}else{ 	
	 	 		DKS += "#@#"+data[i]["DK"];
	 	 	}
	 	}	
		$.ajax({
			type: "post",
			url: "<%=contextPath%>/ddm/config/objecttype/ddmObjectTypeAction.jsp?op=deleteData&contextOid=<%=contextOid%>",
			dataType: "json",
	  		data: {DKS:DKS},
			success: function(result){
				plm.endWait();
				if(result.SUCCESS != null &&result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				adt_grid.reload();
		   },
		   error:function(){
			   plm.endWait();
			   plm.showMessage({title:'������ʾ', message:"�Բ��������������", icon:'1'});
		   }
		});			
	}
	</script>