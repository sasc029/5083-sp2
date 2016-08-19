<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.UIHelper"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>
<%@page import="com.bjsasc.plm.core.util.DateTimeUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@include file="/ddm/public/ddm.jsp" %>

<%@page contentType = "text/html; charset=UTF-8" pageEncoding="GBK"%>
<% 
	String contextPath = request.getContextPath();
    String oid = (String) session.getAttribute(ConstUtil.DISTRIBUTE_ELECTASK_OID);
    
	String classId = DistributeInfo.CLASSID;
	String spot = "ListDistributeInfos";
	String gridId = "distributeInfoGrid";
	String gridTitle = "�ַ���Ϣ";
	String toolbarId = "ddm.distributeforwardinfo.list.toolbar";
	String taskOid = (String)request.getAttribute("taskOid");
	String deleteUrl = contextPath + "/ddm/distribute/distributeElecTaskHandle!deleteDistributeInfos.action";
	String addPrincipalUrl = contextPath + "/ddm/distribute/distributeElecTaskHandle!addDistributeInfos.action";

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
		
		<div id="gridTop" name="gridTop"><div>
	</td></tr><tr><td>
			<table height="100%" width="100%" cellSpacing="0" cellPadding="0"
			border="0">
			<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
				<jsp:param name="spot" value="<%=spot%>" />
				<jsp:param name="gridId" value="<%=gridId%>" />
				<jsp:param name="gridTitle" value="<%=gridTitle%>"/>
        	    <jsp:param name="toolbar_modelId" value="<%=toolbarId%>"/>      
				<jsp:param name="operate_container" value="container"/>
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
</form>
</body>
<script>

	var container = {};
	
	// �ύ����
	function submitForward() {
		var table = pt.ui.get("<%=gridId%>");
		table.selectAll();
		var data = table.getSelections();
		if (data.length == 0) {
			plm.showMessage({title:'������ʾ', message:"��ѡ����Ҫ�ύ�ķַ���Ϣ", icon:'1'});
			return;
		}
		if (!plm.confirm("ȷ���ύ������?")) {
			return;
		}
		var url = "<%=contextPath%>/ddm/distribute/distributeElecTaskHandle!createDistributeForwardElecTask.action";
		var oids = "";
		for(var i=0;i<data.length;i++){
			/* var innerId = data[i].INNERID;
			var classId = data[i].CLASSID;
			var oid = classId + ":" + innerId;
			oids += oid + ","; */
			//oids = data[i]["OIDS"];
			oids += data[i].OID + ",";
		}
		oids = oids.substring(0, oids.length - 1);
		plm.startWait();
		$.ajax({
			url:url,
			type:"post",
			dataType:"json",
			data: "<%=KeyS.OIDS%>="+oids+"&taskOid=<%=oid%>",
			success:function(result){
				plm.endWait();
				if(result.SUCCESS != null && result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}
				//window.location.reload(true);
				parent.parent.close();
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'������ʾ', message:"�ύ����ʧ�ܣ�", icon:'1'});
			}
		});
	}

	// ѡ���û�
  	function addUsers(){
  		// ��֤
		<%=ValidateHelper.buildCheck()%>
			
  		clearDistributeObjectsIid();
		//��ߵ�����ҳ��
		var data = parent.frames("leftTree").getSelections();
		if(data.length == 0){
			plm.showMessage({title:'������ʾ', message:"��ѡ����Ҫ�ַ�������", icon:'1'});
			return;
		}
		setDistributeObjectsIid(data);
		
		var distributeOrderObjectLinkOids = $("#distributeOrderObjectLinkOids").val();
		 var configObj = 
			{ 
				IsModal : "true",
				SelectMode : "multiple",
				returnType : "arrayObj",
				scope : "all"
				
			};
			var retObj = pt.sa.tools.selectUser(configObj);
			if(retObj){
				var arrIid = retObj.arrUserIID;
		    	var iids = "";
		    	for(var i = 0; i < arrIid.length; i++){
		    		if(iids != ""){
		    			iids += ",";
		    		}
		    		iids += arrIid[i];
		    	}
				plm.startWait();
				$.ajax({
						type: "post",
						url: "<%=addPrincipalUrl %>",
						dataType: "json",
						data:"iids="+iids+"&distributeOrderObjectLinkOids="+distributeOrderObjectLinkOids+"&taskOid=<%=oid%>&type=0",
						 success: function(result){
							plm.endWait();
							if(result.SUCCESS != null && result.SUCCESS == "false"){
								plm.showAjaxError(result);
								return;
							}
							if(result.FLAG != "0"){
						       	alert('ѡ�ж���ַ���Ϣ�Ѵ��ڣ������ظ����');
						       	return;
					   	    }
							window.location.reload(true);
					    },
					    error:function(){
					    	plm.endWait();
							plm.showMessage({title:'������ʾ', message:"����ʧ�ܣ�", icon:'1'});
					    }
					});	
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
    	var arrIid = reObj.arrIID;
    	var arrNote = reObj.arrNote;
    	var iids = "";
    	var notes = "";
    	for(var i = 0; i < arrIid.length; i++){
    		if(iids != ""){
    			iids += ",";
    			notes += ",";
    		}
    		iids += arrIid[i];
    		if (arrNote[i] == "") {
    			arrNote[i] = "null";
    		}
    		notes += arrNote[i];
    	}
		$("#type").val(type);
		$("#iids").val(iids);
		$("#notes").val(notes);

		plm.startWait();
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
 	
    function deleteDistributeInfo(){
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		var oids = "";
		if(data.length == 0){
			plm.showMessage({title:'������ʾ', message:"��ѡ����Ҫ�ַ�������", icon:'1'});
			return;
		}
    	for(var i = 0; i < data.length; i++){
    		oids += data[i].OIDS + ",";
    	}
    	oids = oids.substring(0, oids.length - 1);
    	plm.startWait();
    	$.ajax({
			type: "post",
			url: "<%=deleteUrl%>",
			dataType: "json",
			data: "<%= KeyS.OIDS%>="+oids,
			success: function(result){
				plm.endWait();
				if(result.SUCCESS != null &&result.SUCCESS == "false"){
					plm.showAjaxError(result);
					return;
				}else{
					if(result.FLAG == "true"){
						window.location.reload(true);
					}else if(result.FLAG == "false"){
						plm.showMessage({title:'������ʾ', message:"�����ѷ�����Ϣ������ɾ����������ѡ��", icon:'1'});
					}
				}
				
		   },
		   error:function(){
			   plm.endWait();
			   plm.showMessage({title:'������ʾ', message:"�Բ��������������", icon:'1'});
		   }
		});
	}
	
    // ���·ַ���Ϣ
  	function updateForwardInfo(){
  		window.location.href = "<%=contextPath%>/ddm/distributeelectask/distributeInfo_content_edit.jsp?";
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
</script>
</html>