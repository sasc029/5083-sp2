<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>
<%@page import="com.bjsasc.plm.core.util.DateTimeUtil"%>
<%@page import="com.bjsasc.plm.core.system.access.operate.Operate"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributepapersigntask.DistributePaperSignTaskService"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@include file="/ddm/public/ddm.jsp" %>

<%@page contentType = "text/html; charset=UTF-8" pageEncoding="GBK"%>
<%
	String contextPath = request.getContextPath();
    String oid = (String) session.getAttribute(ConstUtil.DISTRIBUTE_PAPERSIGNTASK_OID);
	String classId = DistributeInfo.CLASSID;
	String spot = "ListDistributeInfos";
	String gridId = "distributeInfoGrid";
	String gridTitle = "�ַ���Ϣ";
	String toolbarId = "ddm.distributetaskinfo.list.toolbar";
	
	String commonInfoTitle = "������Ϣ";
	String lc_new=ConstUtil.LC_NEW.getName();//��������������½�
	String approveTerminate=ConstUtil.LC_APPROVE_TERMINATE.getName();//�������������������ֹ
	String scheduling=ConstUtil.LC_SCHEDULING.getName();//������������ǵ�����
	String backoff=ConstUtil.LC_BACKOFF.getName();//��������������ѻ���
	
	/* Persistable obj = Helper.getPersistService().getObject(oid);
	DistributeOrder dis = (DistributeOrder)obj; */
	DistributePaperSignTaskService service = DistributeHelper.getDistributePaperSignTaskService();
	DistributeOrder dis = service.getDistributeOrderByPaperSignTaskOid(oid);
	
	String addPrincipalUrl = contextPath + "/ddm/distribute/distributeInfoHandle!addPrincipals.action";
	String deadLineDate = DateTimeUtil.getCurrentDate(ConstUtil.C_DISDEADLINE_DELAY_DAY);
	String disUrgent = (String)request.getAttribute("disUrgent");
	String deadDate = (String)request.getAttribute("deadLineDate");
	String disStyle = (String)request.getAttribute("disStyle");
	if(deadDate == null){
		deadDate = DateTimeUtil.getCurrentDate(ConstUtil.C_DISDEADLINE_DELAY_DAY);
		//deadDate = "";
	}
	String qReceive_sel = "checked=\"true\"";
	String qReceive_cus = "";
	String disStyle_0 = "checked=\"true\"";
	String disStyle_1 = "";
	if ("1".equals(disUrgent)) {
		qReceive_sel = "";
		qReceive_cus = "checked=\"true\"";
	}
	if ("1".equals(disStyle)) {
		disStyle_0 = "";
		disStyle_1 = "checked=\"true\"";
	}
	if (!lc_new.equals(dis.getLifeCycleInfo().getStateName())
			&& !approveTerminate.equals(dis.getLifeCycleInfo().getStateName())
			&& !scheduling.equals(dis.getLifeCycleInfo().getStateName())
			&& !backoff.equals(dis.getLifeCycleInfo().getStateName())) {
		
		qReceive_sel += " disabled=\"true\"";
		qReceive_cus += " disabled=\"true\"";
		disStyle_0 += " disabled=\"true\"";
		disStyle_1 += " disabled=\"true\"";
	}
%>
<html>
<head>
	<title>�������б�</title>
	<script type="text/javascript"	src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
	<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath%>/plm/ui/editor/date/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0"  scroll="no">
<form id='main_form'>
<input type="hidden" name="CLASSID" id="CLASSID" value="<%=classId%>"/>
<table cellSpacing="0" cellPadding="0" height="100%" width="100%" border="0">
	<tr><td height="1%">
		<!-- ������Ϣ ��ʼ -->
		<table width="100%" cellSpacing="0" cellPadding="0" border="0">
			<tr class="AvidmToolbar">
				<td nowrap="nowrap">
					<div class="AvidmMtop5 twoTitle">
						<a class="showIcon" href="#" onclick="divSelected(this);">
						<img src="<%=contextPath%>/plm/images/common/space.gif"></a>
						<%=commonInfoTitle%>
					</div>
				</td>
			</tr>
			<tr><td>
				<table border="0" cellspacing="0" cellpadding="0" class="avidmTable">
					<tr>
					<td class="left_col AvidmW100">�깤���ޣ�</td>
					<td class="e-checked-text">
						<INPUT type='text' id="deadLineDate"  name="deadLineDate" value="<%=deadDate%>"
			        		class="Wdate pt-textbox"  readonly="readonly"  style="width:100px;" <%=ValidateHelper.buildValidator()%>
			        			<%if(lc_new.equals(dis.getLifeCycleInfo().getStateName()) || approveTerminate.equals(dis.getLifeCycleInfo().getStateName()) || scheduling.equals(dis.getLifeCycleInfo().getStateName()) || backoff.equals(dis.getLifeCycleInfo().getStateName())){ %>
			        		onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'2128-03-10'})" onchange="updateTime()"
			        		<%}%>
			        		/>&nbsp;
					</td>
					<td class="left_col AvidmW100">�����̶ȣ�</td>
					<td class="e-checked-text">
						<input value=0  type=radio name=disUrgent id=disUrgent0 <%=qReceive_sel%> onchange="updateDisUrgent('0')"/>
						��ͨ&nbsp;
						<input value=1 type=radio name=disUrgent id=disUrgent1 <%=qReceive_cus%> onchange="updateDisUrgent('1')"/>
						�Ӽ�&nbsp;&nbsp;
					</td>
					<td class="left_col AvidmW100">�ַ���ʽ��</td>
					<td class="e-checked-text">
						<input value=0  type="radio" name="disStyle" id="disStyle0" <%=disStyle_0%>  onclick="updateDisstyle('0')"/>
						��ʽ�ַ�&nbsp;
						<input value=1 type="radio" name="disStyle" id="disStyle1" <%=disStyle_1%>  onclick="updateDisstyle('1')"/>
						һ���Էַ�&nbsp;&nbsp;
					</td>
					</tr>
				</table>
		</td></tr></table>
<!-- ������Ϣ ���� -->
		<div id="gridTop" name="gridTop"><div>
	</td></tr><tr><td>
			<table height="100%" width="100%" cellSpacing="0" cellPadding="0"
			border="0">
			<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
				<jsp:param name="spot" value="<%=spot%>" />
				<jsp:param name="gridId" value="<%=gridId%>" />
				<jsp:param name="gridTitle" value="<%=gridTitle%>"/>
        	    <jsp:param name="toolbar_modelId" value="<%=toolbarId%>"/>      
				<jsp:param name="operate_container" value="container" />
				<jsp:param name="operate_mainObjectOID" value="<%=oid%>" />
			</jsp:include>
		</table>
<!-- �ַ����� ���� -->	
</td></tr></table>
<input type="hidden" name="distributeObjectOids" id="distributeObjectOids"/>
<input type="hidden" name="distributeInfoOids" id="distributeInfoOids"/>
<input type="hidden" name="distributeOrderOids" id="distributeOrderOids"/>
<input type="hidden" name="distributeOrderObjectLinkOids" id="distributeOrderObjectLinkOids"/>
<input type="hidden" name="type" id="type"/>
<input type="hidden" name="iids" id="iids"/>
<input type="hidden" name="disMediaTypes" id="disMediaTypes"/>
<input type="hidden" name="disInfoNums" id="disInfoNums"/>
<input type="hidden" name="notes" id="notes"/>
<input type="hidden" name="disType" id="disType"/>
<input type="hidden" name="updateDisUrgentValue" id="updateDisUrgentValue"/>
<input type="hidden" name="updateDisstylValue" id="updateDisstylValue"/>
<input type="hidden" name="outSignInfoOids" id="outSignInfoOids"/>
<input type="hidden" name="historyOids" id="historyOids"/>
</form>
</body>
<script>
	var container = {};
	// �ύ����
	function submitDistribute(){
		plm.startWait();
		//ddmValidation("<%=oid%>","<%=Operate.MODIFY%>","submitDistributeCallBack");
		submitDistributeCallBack("<%=oid%>");
	}
	
	function submitDistributeCallBack(orderOid) {
		var table = pt.ui.get("<%=gridId%>");
		var selections = table.getRows();
		if (selections.length == 0) {
			plm.endWait();
			plm.showMessage({title:'������ʾ', message:"û�пɲ���������", icon:'1'});
			return;
		}
		if (!plm.confirm("ȷ���ύ������?")) {
			plm.endWait();
			return;
		}
		var url = "<%=contextPath%>/ddm/distribute/distributeInfoHandle!createDistributeTask.action";
		var oids = "";
		var outSignInfoOids = "";
		for(var i=0;i<selections.length;i++){
			oids += selections[i].OID + ",";
			if(selections[i].DISMEDIATYPE == '����'){
				outSignInfoOids += selections[i].OID + ",";
			}
		}
		oids = oids.substring(0, oids.length - 1);
		$("#distributeInfoOids").val(oids);
		$("#outSignInfoOids").val(outSignInfoOids);
		$.ajax({
			url:url,
			type:"post",
			dataType:"json",
			data: $("#main_form").serializeArray(),
			success:function(result){
				plm.endWait();
				if(result.SUCCESS != null && result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				pageReload();
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'������ʾ', message:"����ʧ�ܣ�", icon:'1'});
			}
		});
	}
	
	function gatherAffectobjects() {
		plm.showMessage({title:'ϵͳ��ʾ', message:"���ڽ����С�����", icon:'2'});
	}

	// ѡ���û�
	function addUsers(){
		plm.startWait();
		//ddmValidation("<%=oid%>","<%=Operate.MODIFY%>","addUsersCallBack");
		addUsersCallBack("<%=oid%>");
	}
	
  	function addUsersCallBack(orderOid){
  		// ��֤
		<%=ValidateHelper.buildCheck()%>
			
  		clearDistributeObjectsIid();
		//��ߵ�����ҳ��
		var data = parent.frames("leftTree").getSelections();
		if(data.length == 0){
			plm.endWait();
			plm.showMessage({title:'������ʾ', message:"��ѡ����Ҫ�ַ�������", icon:'1'});
			return;
		}
		setDistributeObjectsIid(data);
		var configObj = 
			{ 
				IsModal : "true",
				SelectMode : "multiple",
				returnType : "arrayObj",
				scope : "<%=ConstUtil.VISTYPE_ALL%>"
			};
		plm.endWait();
		var retObj = ddm.tools.selectUser(configObj);
		if(retObj){
			doBindRole(retObj, "<%=ConstUtil.DISINFOTYPE.USER%>");
	  	}
  	}
  	
  	function addOutSignUsers(){
		var linkData = parent.frames("leftTree").getSelections();
		if(linkData.length == 0){
			plm.endWait();
			plm.showMessage({title:'������ʾ', message:"��ѡ����Ҫ�ַ�������", icon:'1'});
			return;
		}
		setDistributeObjectsIid(linkData);
		var linkOids = $("#distributeOrderObjectLinkOids").val();
  		plm.openWindow("<%=contextPath%>/ddm/public/selectoutorg.jsp?linkOids="+linkOids,500,500,'outSignUser');
	}
      	
  	//ѡ����֯
  	function addOrg(){
  		plm.startWait();
		//ddmValidation("<%=oid%>","<%=Operate.MODIFY%>","addOrgCallBack");
		addOrgCallBack("<%=oid%>");
	}
	
    function addOrgCallBack(orderOid){
  		// ��֤
		<%=ValidateHelper.buildCheck()%>
			
  		clearDistributeObjectsIid();
		//��ߵ�����ҳ��
		var data = parent.frames("leftTree").getSelections();
		if(data.length == 0){
			plm.endWait();
			plm.showMessage({title:'������ʾ', message:"��ѡ����Ҫ�ַ�������", icon:'1'});
			return;
		}
		setDistributeObjectsIid(data);
		var configObj = 
  		{ 
  			IsModal : "true",
  			SelectMode : "multiple",
  			returnType : "arrayObj",
  			scope : "<%=ConstUtil.VISTYPE_ALL%>"
  		};
		plm.endWait();
	    var retObj = ddm.tools.selectOrg(configObj);
		if(retObj){
			doBindRole(retObj, "<%=ConstUtil.DISINFOTYPE.ORG%>");
		} 
    }
    
    function clearDistributeObjectsIid() {
		$("#distributeOrderObjectLinkOids").val("");
    }
    
    function setDistributeObjectsIid(selections) {
    	var oids = "";
    	for(var i = 0; i < selections.length; i++){
			var oid = selections[i]["DISTRIBUTEORDEROBJECTLINKOID"];
			if(oids != ""){
				oids += ",";
			}
			oids += oid;
		}
		$("#distributeOrderObjectLinkOids").val(oids);
    }
  	
 	//ѡ����/��֯���ص�
    function doBindRole(reObj, type){
    	plm.startWait();
    	var arrIid = reObj.arrIID;
    	var arrDisMediaType = reObj.arrDisMediaType;
    	var arrDisInfoNum = reObj.arrDisInfoNum;
    	var arrNote = reObj.arrNote;
    	var iids = "";
    	var disMediaTypes = "";
    	var disInfoNums = "";
    	var notes = "";
    	var distributeObjectIids = "";
    	for(var i = 0; i < arrIid.length; i++){
    		if(iids != ""){
    			iids += ",";
    			disMediaTypes += ",";
    			disInfoNums += ",";
    			notes += ",";
    		}
    		iids += arrIid[i];
    		disMediaTypes += arrDisMediaType[i];
    		disInfoNums += arrDisInfoNum[i];
    		if (arrNote[i] == "") {
    			arrNote[i] = "null";
    		}
    		notes += arrNote[i];
    	}
		$("#type").val(type);
		$("#iids").val(iids);
		$("#disMediaTypes").val(disMediaTypes);
		$("#disInfoNums").val(disInfoNums);
		$("#notes").val(notes);
		$("#disType").val("1");

		$.ajax({
				type: "post",
				url: "<%=addPrincipalUrl%>",
				dataType: "json",
				data: $("#main_form").serializeArray(),
				success: function(result){
					plm.endWait();
					if(result.SUCCESS != null && result.SUCCESS == "false"){
						plm.showAjaxError(result);
						return;
					}
					reload();
			    },
			    error:function(){
			    	plm.endWait();
					plm.showMessage({title:'������ʾ', message:"����ʧ�ܣ�", icon:'1'});
			    }
			});	
    }
    
    // ɾ���ַ���Ϣ
    function deleteDistributeInfo(){
		plm.startWait();
		//ddmValidation("<%=oid%>","<%=Operate.MODIFY%>","deleteDistributeInfoCallBack");
		deleteDistributeInfoCallBack("<%=oid%>");
	}
    
	function deleteDistributeInfoCallBack(orderOid){
		var table = pt.ui.get("<%=gridId%>");
		var selections = table.getSelections();
		if (selections.length == 0) {
			plm.endWait();
			plm.showMessage({title:'������ʾ', message:"������ѡ��1����������!", icon:'1'});
			return;
		}
		var oids = "";
		var life = new Array();
		for(var i=0;i<selections.length;i++){
			oids += selections[i].OIDS + ",";
			life.add(selections[i].LIFECYCLE_STATE);
		}
		oids = oids.substring(0, oids.length - 1);

		if(life.length != 0){
			for(var j = 0; j < life.length;j++){
				if("�ѷ���" == life[j]){
					plm.endWait();
					plm.showMessage({title:'������ʾ', message:"����ɾ���ѷ��͵ķַ���Ϣ!", icon:'1'});
					return;
				}
			}
		}

		if (!plm.confirm("ȷ��ɾ����?")) {
			plm.endWait();
			return;
		}
		var url = "<%=contextPath%>/ddm/distribute/distributeInfoHandle!deleteDistributeInfo.action?oid=<%=oid%>";
		
		$("#distributeInfoOids").val(oids);
		$.ajax({
			url:url,
			type:"post",
			dataType:"json",
			data: $("#main_form").serializeArray(),
			success:function(result){
				plm.endWait();
				if(result.SUCCESS != null && result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				reload();
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'������ʾ', message:"����ʧ�ܣ�", icon:'1'});
			}
		});
	}
    
    function updateDisUrgent(value){
    	plm.startWait();
    	$("#updateDisUrgentValue").val(value);
		//ddmValidation("<%=oid%>","<%=Operate.MODIFY%>","updateDisUrgentCallBack");
		updateDisUrgentCallBack("<%=oid%>");
	}
	
    function updateDisUrgentCallBack(orderOid){
    	var value = $("#updateDisUrgentValue").val();
    	parent.frames("leftTree").checkAll();
    	var data = parent.frames("leftTree").getSelections();
		if(data.length == 0){
			plm.endWait();
			return;
		}
		var linkOids = "";
    	for(var i = 0; i < data.length; i++){
			var oid = data[i]["DISTRIBUTEORDEROBJECTLINKOID"];
			if(linkOids != ""){
				linkOids += ",";
			}
			linkOids += oid;
		}
    	var url = "<%=contextPath%>/ddm/distribute/distributeObjectHandle!updateDistributeObjectLink.action";
    	
		$.ajax({
			url:url,
			type:"post",
			dataType:"json",
			data: "<%=KeyS.OIDS%>="+linkOids+"&disUrgent="+value,
			success:function(result){
				plm.endWait();
				if(result.SUCCESS != null && result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				reload();
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'������ʾ', message:"����ʧ�ܣ�", icon:'1'});
			}
		});
    }
    function updateTime(){
    	plm.startWait();
		//ddmValidation("<%=oid%>","<%=Operate.MODIFY%>","updateTimeCallBack");
		updateTimeCallBack("<%=oid%>");
	}
	
    function updateTimeCallBack(orderOid){
    	parent.frames("leftTree").checkAll();
    	
    	var data = parent.frames("leftTree").getSelections();
		if(data.length == 0){
			plm.endWait();
			return;
		}
		var linkOids = "";
    	for(var i = 0; i < data.length; i++){
			var oid = data[i]["DISTRIBUTEORDEROBJECTLINKOID"];
			if(linkOids != ""){
				linkOids += ",";
			}
			linkOids += oid;
		}
    	var deadLineDate = $("#deadLineDate").val();
    	var url = "<%=contextPath%>/ddm/distribute/distributeObjectHandle!updateDistributedeadLinkDate.action";
    	
		$.ajax({
			url:url,
			type:"post",
			dataType:"json",
			data: "<%=KeyS.OIDS%>="+linkOids+"&deadLineDate="+deadLineDate,
			success:function(result){
				plm.endWait();
				if(result.SUCCESS != null && result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				reload();
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'������ʾ', message:"����ʧ�ܣ�", icon:'1'});
			}
		});
    }
	
    // ���·ַ���Ϣ
    function updateDistributeInfo(){
    	plm.startWait();
		//ddmValidation("<%=oid%>","<%=Operate.MODIFY%>","updateDistributeInfoCallBack");
    	updateDistributeInfoCallBack("<%=oid%>");
	}
	
  	function updateDistributeInfoCallBack(Orderoid){
    	var style = $("#disStyle0").attr("checked")?0:1;
    	var urgent= $("#disUrgent0").attr("checked")?0:1;
    	var deadDate = $("#deadLineDate").val();
    	plm.endWait();
  		window.location.href = "<%=contextPath%>/ddm/distributeinfo/distributeInfo_content_edit.jsp?oid=<%=oid%>&disUrgent="+urgent+"&deadLineDate="+deadDate+"&disStyle="+style;
  	}
    
    // ���¼���
	function reload(){
		var hideIcon = $(".hideIcon").attr("class");
		if(hideIcon == "hideIcon"){
			 divSelected($(".hideIcon"));
		}
		var table = pt.ui.get("<%=gridId%>");
		table.reload();
	}
 
	//���·ַ���ʽ
	function updateDisstyle(value){
		plm.startWait();
		$("#updateDisstylValue").val(value);
		//ddmValidation("<%=oid%>","<%=Operate.MODIFY%>","updateDisstyleCallBack");
		updateDisstyleCallBack("<%=oid%>");
	}
	
	function updateDisstyleCallBack(orderOid){
		var value = $("#updateDisstylValue").val();
    	parent.frames("leftTree").checkAll();
    	var data = parent.frames("leftTree").getSelections();
		if(data.length == 0){
			plm.endWait();
			return;
		}
		var linkOids = "";
    	for(var i = 0; i < data.length; i++){
			var oid = data[i]["DISTRIBUTEORDEROBJECTLINKOID"];
			if(linkOids != ""){
				linkOids += ",";
			}
			linkOids += oid;
		}
    	var url = "<%=contextPath%>/ddm/distribute/distributeObjectHandle!updateDistributeObjectLinkByDisStyle.action";
    	
		$.ajax({
			url:url,
			type:"post",
			dataType:"json",
			data: "<%=KeyS.OIDS%>="+linkOids+"&disStyle="+value,
			success:function(result){
				plm.endWait();
				if(result.SUCCESS != null && result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				reload();
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'������ʾ', message:"����ʧ�ܣ�", icon:'1'});
			}
		});
	}

	function tableReload(){
		var table = pt.ui.get("<%=gridId%>");
		table.reload();
	}
</script>
</html>