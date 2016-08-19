<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>

<%@page import="com.bjsasc.ddm.common.ConstUtil"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%@include file="/adm/activepublic/activeError.jsp" %>
<%
	String contextPath = request.getContextPath();
	String spot = "ListActiveRecycles";
	String gridId = "activeRecycleGrid";
	String gridTitle = "����վ";
	String toolbarId = "adm.activerecycle.list.toolbar";
%>
<html>
	<head>
		<title><%=gridTitle%></title>
		<script type="text/javascript"	src="<%=contextPath%>/platform/common/js/ptutil.js" charset="UTF-8"></script>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
	</head>
	<body>
		<form id="mainForm" name="mainForm" method="POST">
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
</html>
<script type="text/javascript">

	var container = {};
	
	// ���¼���
	function reload(){
		var table = pt.ui.get("<%=gridId%>");
		table.reload();
	}
	
	// ���
	function clearActiveRecycle() {
		var url = "<%=contextPath%>/adm/active/ActiveRecycleHandle!clearRecycle.action";
		execute(url, "");
	}
	
	// ɾ��
	function deleteActiveRecycle() {
		var url = "<%=contextPath%>/adm/active/ActiveRecycleHandle!deleteRecycle.action";
		var oids = getOids();
		execute(url, oids);
	}
	
	// ��ԭ
	function reductionActiveRecycle() {
		var url = "<%=contextPath%>/adm/active/ActiveRecycleHandle!reductionRecycle.action";
		var oids = getOids();
		execute(url, oids);
	}
	
	// ȡ��ѡ�����ݵ�OID
	function getOids() {
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		var oids = "";
		for(var i=0;i<data.length;i++){
			oids += data[i].OID + ",";
		}
		return oids;
	}
	
	// ִ�в���
	function execute(url, oids) {
		plm.startWait();
		$.ajax({
			url:url,
			type:"post",
			dataType:"json",
			data:"OIDS=" + oids,
			success:function(result){
				plm.endWait();
				if (result.SUCCESS != null && result.SUCCESS =="false"){
					plm.showAjaxError(result);
				} else {
					reload();
				}
			},
			error:function(){
				plm.endWait();
				plm.showMessage({title:'������ʾ', message:"����ʧ�ܣ�", icon:'1'});
			}
		});
	}
</script>
