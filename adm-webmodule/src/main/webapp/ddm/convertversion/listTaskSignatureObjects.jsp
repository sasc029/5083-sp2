<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.convertversion.DcA3SigObjectLinkService"%>
<%@page import="com.bjsasc.ddm.distribute.model.convertversion.DcA3SigObjectLink"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String taskSigOid = request.getParameter("taskSigOid");
	String taskSigIID = Helper.getInnerId(taskSigOid);
	String gridId = "taskSignatureObjectGrid";
	String gridTitle = "A3ǩ�������Ϣ";
	String spot = "ListDistributeObjects";
	
	DcA3SigObjectLinkService dcA3SigObjectLinkService = DistributeHelper
			.getDcA3SigObjectLinkService();
	//����taskIIDȡ�ÿ�汾ǩ����Ϣ��ַ�����Link�б�
	List<DcA3SigObjectLink> linkList = dcA3SigObjectLinkService.getDcA3SigObjectLinkByTaskSigIID(taskSigIID);
	//��link��To���󣨷ַ����ݶ��󣩷ŵ�����ʾ�б���
	List<DistributeObject> listDis = new ArrayList<DistributeObject>();
	for(DcA3SigObjectLink link : linkList){
		listDis.add((DistributeObject)link.getTo());
	}
	
	// ��ʽ����ʾ����
	GridDataUtil.prepareRowObjects(listDis, ConstUtil.SPOT_LISTDISTRIBUTEOBJECTS);
	
%>
<html>
	<head>
		<title><%=gridTitle%></title>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
	</head>
	<body>
		<form id="mainForm" name="mainForm" method="POST">
			<table height="100%" width="100%" cellSpacing="0" cellPadding="0"
			     border="0">
				<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
					<jsp:param name="spot" value="<%=spot%>"/>
					<jsp:param name="gridId" value="<%=gridId%>"/>
					<jsp:param name="gridTitle" value=""/>
					<jsp:param name="toolbar_modelId" value=""/>
					<jsp:param name="onLoadSuccess" value="onLoadSuccess()"/>
					<jsp:param name="operate_container" value="container"/>
					<jsp:param name="operate_mainObjectOID" value="" />
				</jsp:include>
				<%-- </td></tr>--%>
		</table>
		</form>
	</body>
</html>
<script type="text/javascript">
	var container = {};
	
    function onLoadSuccess() {
  	  onLoadSuccess_ddm("<%=gridId%>");
      }
    
</script>


