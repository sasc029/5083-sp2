<%@page import="java.util.*" %>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.core.system.access.operate.Operate"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%@include file="/ddm/public/ddm.jsp" %>

<%
	String contextPath = request.getContextPath();
	//String spot = "ListDistributeOrders";
	String gridId = "distributeOrderGrid";
	String toolbarId = "ddm.distributeorderSending.list.toolbar";
	
	String spot = "ListDistributeSendOrders";

 	String flag = request.getParameter("flag");
 	if ("clean".equals(flag)) {
 		GridDataUtil.clearBuf(spot);
 	}
 	
	Map<String,Object> params = new HashMap<String, Object>();
 	params.put("OID", "");
 	GridDataUtil.prepareRowQueryParams(params,  spot);
 	
%>
<html>
	<head>
		<script type="text/javascript"	src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
		<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
	</head>
	<body>
	
		  <form id="mainForm" name="mainForm" method="POST">
			<table  height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0" >
				<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">	
				    <jsp:param name="spot" value="<%=spot%>"/>
					<jsp:param name="toolbar_modelId" value="<%=toolbarId%>"/>
					<jsp:param name="gridId" value="<%=gridId%>"/>
					<jsp:param name="onLoadSuccess" value="onLoadSuccess()"/>
					<jsp:param name="operate_container" value="container"/>
				</jsp:include>
			</table> 
			</form>
	</body>
</html>
<script type="text/javascript">

function onLoadSuccess() {
	onLoadSuccess_ddm("<%=gridId%>");
}

var container = {};

//ɾ���ַ��еķ��ŵ�
function deleteDistributeOrder(){
	
	var table = pt.ui.get("<%=gridId%>");
	var data = table.getSelections();
	if(data.length == 0){
		plm.showMessage({title:'������ʾ', message:"������ѡ��һ����������", icon:'1'});
		return;
	}
	var oids = "";
	var warnOids="";
	for(var i=0;i<data.length;i++){
		
		if (data[i].SOURCE.STATENAME!='�½�'&& data[i].SOURCE.STATENAME!='������'&& data[i].SOURCE.STATENAME!='���˻�'&&data[i].SOURCE.STATENAME!='�ַ��������˻�'
			&&data[i].SOURCE.STATENAME!='�ַ�������ֹ'&&data[i].SOURCE.STATENAME!='������ֹ'&&data[i].SOURCE.STATENAME!='�������˻�') {
			warnOids+=data[i]["NAME"].substring(data[i]["NAME"].indexOf("le='{")+5,data[i]["NAME"].indexOf("}'>")) + ",";
			continue;
		}
		oids += data[i].OID + ",";
	}
	if (oids!="") {
		oids = oids.substring(0, oids.length - 1);
		var msg = "��ȷ��Ҫɾ����\n";
		if (!plm.confirm(msg)) {
			return;
		}
		plm.startWait();
		exectueDelete(data,oids,warnOids);
	}else {
		plm.showMessage({title:'������ʾ', message:"��ǰ��ѡ��ȫ�����ŵ����ɲ���<br>��ţ�"+warnOids.substring(0, warnOids.length - 1), icon:'1'});
		return;
	}
	
}

function exectueDelete(data,oids,warnOids){
	
	$.ajax({
		url:"<%=contextPath%>/ddm/distribute/distributeOrderHandle!deleteDistributeOrder.action",
		type:"post",
		dataType:"json",
		data:"<%=KeyS.OIDS%>="+oids,
		success:function(result){
			plm.endWait();
			if (result.SUCCESS != null && result.SUCCESS =="false"){
				plm.showAjaxError(result);
			} else {
				if(result.ERROROIDS == ""){
					if (warnOids!=""){
						alert("���棺��ǰ��ѡ�Ĳ��ַ��ŵ�û�б�ɾ��\n��ţ�" +warnOids.substring(0, warnOids.length - 1));
						reload();
					}else{
						tableReload("ɾ���ɹ�");
					};
				}else{
					var errorOids=result.ERROROIDS.split(",");
					for(var j=0;j<errorOids.length;j++){
						for(var i=0;i<data.length;i++){
							if (data[i].OID == errorOids[j]) {
								warnOids+=data[i]["NAME"].substring(data[i]["NAME"].indexOf("le='{")+5,data[i]["NAME"].indexOf("}'>")) + ",";
								continue;
							}
						}
					}
					if (warnOids!=""){
						alert("��ǰ��ѡ�ķ��ŵ�û�б�ɾ��\n��ţ�" +warnOids.substring(0, warnOids.length - 1));
						reload();
					}
				}
			}
		},
		error:function(){
			plm.endWait();
			plm.showMessage({title:'������ʾ', message:"ɾ�����ŵ�����ʧ�ܣ�", icon:'1'});
		}
	});
}

//ˢ��dategrid
function tableReload(text){
	messager.showTipMessager({'content':text});
	reload();
}

function reload(){
	window.location.reload(true);
}
//����ˢ�·���
function reloadOrder(){
	window.location.href = "<%=contextPath%>/ddm/distributeorder/listdistributesending.jsp?flag=clean";
}

</script>
