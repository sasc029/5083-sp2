<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.platform.i18n.*"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%
	String contextPath = request.getContextPath();
	String spot = "DisComConfigDataGrid";
	String gridId = "disComConfigDataGrid";
	String gridTitle = "���Ź���ͨ������";
	String toolbarId = "ddm.distributecommonconfig.toolbar";
	
	String data = request.getParameter(KeyS.DATA);
	if(data == null){
		data = request.getParameter("DATAS");
	}
	GridDataUtil.prepareRowData(data, spot);
%>
<html>
<head>
  <title><%=gridTitle%></title>
<jsp:include page="/platform/ui/Ui-js.jsp"></jsp:include>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" scroll="no" class="body">
<form id="form">
<table height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0">
	<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
		<jsp:param name="spot" value="<%=spot%>"/>
		<jsp:param name="gridId" value="<%=gridId%>"/>
		<jsp:param name="gridTitle" value="<%=gridTitle%>"/>
		<jsp:param name="toolbar_modelId" value="<%=toolbarId%>"/>
		<jsp:param name="operate_container" value="container"/>
	</jsp:include>
</table>
</form>
</body>
<script type="text/javascript">
var container = {};
$(function(){
	var grid = pt.ui.get('disComConfigDataGrid');
	var oldValue = null;
	var columnInnerId = null;
	var configDefaultResult = null;
	grid.on('beforecelledit', function(obj){
		var row = grid.getSelections();
		columnInnerId = row[0]["INNERID"];
		configDefaultResult = row[0]["CONFIGVALUE_R"];
		var record = obj.record;
 		var column = obj.column;
 		oldValue = record[column.id];
	});
	
	grid.on("aftercelledit", function(obj){
		var record = obj.record;
 		var column = obj.column;
 		var flag=column.id;
 		var value = record[column.id];
 		if(value != oldValue){
 	 		//�ж��޸ĺ������ֵ�Ƿ�Ϊ��
 	 		if(flag == "CONFIGVALUE_R"){
 	 			if(value == null || value == ""){
 	 				var msg = "��ȷ��Ҫ�趨����ֵΪ�գ�������Ĭ��ֵΪ��Ч����ֵ��\n";
 	 				if (!plm.confirm(msg)) {
 	 					record["CONFIGVALUE_R"]=configDefaultResult;
 	 					return;
 	 				}
 	 	 		}
 	 		}else{
 	 			return;
 	 		}
 			plm.startWait();
 			$.ajax({
 				type:"POST",
 				url:"<%=contextPath%>/ddm/distribute/distributeCommonConfigHandle!updateSingleEditValue.action",
 				data:"value="+value+"&flag="+flag+"&columnInnerId="+columnInnerId,
 				success:function(result){
 					if(result == 'success'){
 							plm.endWait();
 						}
 				}
 			});
 		}
	});
});

//�Ƿ�����ɾ�� ��ʾ��ʽ��  0�� 1��
function isDel(value, rowdata, rowindex) {
	if(value == '0'){
		return '��';
	}else if(value == '1'){
		return '��';
	}else{
		return '';
	}
}
function contains(array,str){
	for(var i=0;i<array.length;i++){
		if(array[i]==str){
			return true;
		}
		return false;
	}
}
//ɾ����������
function delParamConfig(){
	var grid = pt.ui.get("disComConfigDataGrid");
	var rows = grid.getSelections();
	if(rows==null || rows.length<1){
		plm.showMessage({title:'��ʾ', message:"��ѡ��Ҫɾ��������!", icon:'3'});
		return;
	}
	var ids = new Array();
	var ispers = new Array();
	for(var i=0; i<rows.length; i++){
		ids.add(rows[i]["INNERID"]);
		ispers.add(rows[i]["ISPERMITDELETE_R"]);
	}
	if(confirm("��ȷ��Ҫɾ����������")){
		if(contains(ispers,"<font color='red'>��</font>")){
			plm.showMessage({title:'��ʾ', message:"�������ò�����ɾ��!", icon:'3'});
			return;
		}else{
		plm.startWait();
		$.ajax({
			type: "POST",
			url: "<%=contextPath%>/ddm/distribute/distributeCommonConfigHandle!delDistributeCommonConfig.action",
			data: "ids="+ids,
			success: function(result){
				if(result == 'success'){
					plm.endWait();
					reload();
				}
			}
		});
	}
}
}
function reload(){
	window.location.reload();
}

//�ָ����Ź���ͨ������Ĭ�ϲ���
function recoverParamConfig(){
	var msg = "��ȷ��Ҫ�ָ�Ĭ�ϲ���������\n";
	if (!plm.confirm(msg)) {
		return;
	}
	plm.startWait();
	$.ajax({
		type: "POST",
		url: "<%=contextPath%>/ddm/distribute/distributeCommonConfigHandle!recoverDistributeCommonConfig.action",
		success: function(result){
			if(result == 'success'){
				plm.endWait();				
				reload();
			}
		}
	});
}
</script>
</html>