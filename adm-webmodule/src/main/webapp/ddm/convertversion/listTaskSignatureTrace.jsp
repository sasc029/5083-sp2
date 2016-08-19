<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.avidm.core.site.Site"%>
<%@page import="com.bjsasc.avidm.core.site.SiteHelper"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.convertversion.DcA3TaskSignatureService"%>
<%@page import="com.bjsasc.ddm.distribute.model.convertversion.DcA3TaskSignature"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String contextPath = request.getContextPath();
	String gridId = "taskSignatureTraceGrid";
	String gridTitle = "A3ǩ����Ϣ����";
	
	DcA3TaskSignatureService dcA3TaskSignatureService = DistributeHelper
			.getDcA3TaskSignatureService();
	// ��ȡ���ŵ�OID
    String orderOid = (String) session.getAttribute(ConstUtil.DISTRIBUTE_ORDER_OID);
	String orderIID = Helper.getInnerId(orderOid);
	List<String> domainIIDList = dcA3TaskSignatureService.getDomainIIDListByOrderIID(orderIID);
%>
<html>
	<head>
		<title><%=gridTitle%></title>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
	</head>
	<body>
		<form id="mainForm" name="mainForm" method="POST">
		<%
		int idx = 0;
		for(String domainIID : domainIIDList){
			Site targetSite = SiteHelper.getSiteService().findSiteById(domainIID);
			String domainName = targetSite.getSiteData().getSiteName();
		    String gridUrl =  contextPath + "/ddm/distribute/dcA3TaskSignatureHandle!listDcA3TaskSignatureByDomainIID.action?orderIID="+orderIID+"&domainIID="+domainIID+"";
		%>
		<table width="100%" cellSpacing="0" cellPadding="0" border="0">
			<tr>
				<td nowrap="nowrap">
					<div class="AvidmMtop5 twoTitle">
						ǩ����<%=domainName%>
					</div>
				</td>
			</tr>
			<tr><td>
				<table class="pt-grid" id="<%=gridId%>_<%=idx%>" width="100%"  height="160" checkbox="false" fit="false" singleSelect="false" rownumbers="true"  url="<%=gridUrl%>" pagination="false">
		         	<thead>
		         		 <tr>
							<th  field="SIGTRACE_DEALUSERNAME" width="80">ǩ����</th>
							<th  field="SIGTRACE_SIGNATURESTATE" width="80">ǩ��״̬</th>
							<th  field="SIGTRACE_STARTTIME" width="140" >���񴴽�ʱ��</th>
							<th  field="SIGTRACE_SIGNATURETIME" width="140">ǩ��ʱ��</th>
				  			<th  field="SIGTRACE_DEALDEVISIONNAME" width="150">ǩ��λ</th>
				  			<th  field="SIGTRACE_ISAGREE" width="60">�Ƿ�ͬ��</th>
							<th  field="SIGTRACE_DEALMIND" width="200">ǩ�����</th>
							<th  field="SIGTRACE_OBJLINK" width="100">ǩ�����</th>
						</tr>
					</thead>
				</table>
			</td></tr>
		</table>
		<%idx++; } %>
		<%if (idx == 0){  %>
				<table class="pt-grid" id="<%=gridId%>" width="100%"  height="160" checkbox="false" fit="false" singleSelect="false" rownumbers="true"  url="" pagination="false">
		         	<thead>
		         		 <tr>
							<th  field="SIGTRACE_DEALUSERNAME" width="80">ǩ����</th>
							<th  field="SIGTRACE_SIGNATURESTATE" width="80">ǩ��״̬</th>
							<th  field="SIGTRACE_STARTTIME" width="140" >���񴴽�ʱ��</th>
							<th  field="SIGTRACE_SIGNATURETIME" width="140">ǩ��ʱ��</th>
				  			<th  field="SIGTRACE_DEALDEVISIONNAME" width="150">ǩ��λ</th>
				  			<th  field="SIGTRACE_ISAGREE" width="60">�Ƿ�ͬ��</th>
							<th  field="SIGTRACE_DEALMIND" width="200">ǩ�����</th>
							<th  field="SIGTRACE_OBJLINK" width="100">ǩ�����</th>
						</tr>
					</thead>
				</table>
		<% } %>
		
		</form>
	</body>
</html>
<script type="text/javascript">
	var container = {};

	function doCustomizeMethod_signatureObjLink(value) {
		var url = '<%=contextPath%>/ddm/convertversion/listTaskSignatureObjects.jsp?taskSigOid=' + value;
		ddm.tools.openWindow(url,900,600,"taskSignatureObjectsOpen");
	}
</script>
