<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.plm.collector.CollectorType"%>
<%@page import="com.bjsasc.plm.collector.CollectorScope"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink"%>
<%
	// ǰҳ��ѡ�������(���ŵ���ַ�����Link��OID���ϻ�ַ����ݶ���OID����)
	String data = request.getParameter(KeyS.DATA);
	List<Map<String, String>> list =(List<Map<String, String>>) DataUtil.JsonToList(data);

	// �������
	List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
	data =  DataUtil.listToJson(listMap);

	List<Persistable> listPersistableTemp = new ArrayList<Persistable>();

	for (Map<String, String> map:list){
		String oid = map.get("OID");
		String classId = oid.split(":")[0];
		DistributeObject disObj = null;
		if("DistributeOrderObjectLink".equals(classId)){
			// ���ŵ���ַ�����Link
		    Persistable linkObj = Helper.getPersistService().getObject(oid);
		    DistributeOrderObjectLink objLink = (DistributeOrderObjectLink) linkObj;
		    // �ַ����ݶ���
			Persistable obj = (Persistable) objLink.getTo();
			disObj = (DistributeObject) obj;
		} else {
		    // �ַ����ݶ���
		    Persistable disObject = Helper.getPersistService().getObject(oid);
			disObj = (DistributeObject) disObject;
		}

	    // ����Դ����
		String dataInnerId = disObj.getDataInnerId();
		String dataClassId = disObj.getDataClassId();
		String dataOid = Helper.getOid(dataClassId, dataInnerId);
		Persistable dataObj = Helper.getPersistService().getObject(dataOid);
		if ( dataObj!=null){
			Map<String, Object> mapObject  =  Helper.getTypeManager().getAttrValues(dataObj);
			Map<String, String> objMap=(Map<String, String> )mapObject.get("SOURCE");
			listMap.add(objMap);
		}
	}

	if (listMap.size()>0){
		data =  DataUtil.listToJson(listMap);
	}

	String callback = request.getParameter(KeyS.CALLBACK);
	String contextOid=request.getParameter(KeyS.CONTAINER_OID);
	String distributeOrderOID = request.getParameter("PARAM_DistributeOrderOid");
	String custom_table = request.getParameter("custom_table");
	//data  = URLDecoder.decode(data, "UTF-8");
	CollectorScope scope = CollectorScope.PUBLIC;//������Χ
	CollectorType collectorType = CollectorType.DEFAULTCOLLECTOR;

	//��ͼ���ؼ������ݳ��� 
	String spot = "approveCollector";
	String gridId = "grid_files";
	String tbar = "collector.list.toolbar";
	String pageID = "list_collector_page";
	String parm = "{'DistributeOrderOid':'"+distributeOrderOID+"'}";
	String contextPath = request.getContextPath();
%>
<html>
<head>
<title>���ŵ��ռ���</title>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
</head>
<body>
	<form id="mainForm" name="mainForm" method="POST">
		<input type="hidden" name="<%=KeyS.CALLBACK%>" value="<%=callback%>" />
		<input type="hidden" name="<%=KeyS.CONTAINER_OID%>"
			value="<%=contextOid%>" /> <input type="hidden"
			name="<%=KeyS.DATA%>" value="<%=URLEncoder.encode(data, "UTF-8")%>" />
		<input type="hidden" id="custom_table" name="custom_table"
			value="<%=custom_table%>">

		<table height="99%" width="100%" cellSpacing="0" cellPadding="0"
			border="0">
			<tr class="AvidmActionTitle">
				<td><jsp:include page="/plm/common/actionTitle.jsp">
						<jsp:param name="ACTIONID" value="com.bjsasc.plm.collector.visit" />
					</jsp:include></td>
			</tr>
			<jsp:include page="/plm/collector/collector_with_view.jsp">
				<jsp:param name="spot" value="<%=spot%>" />
				<jsp:param name="scope" value="<%=scope%>" />
				<jsp:param name="gridId" value="<%=gridId%>" />
				<jsp:param name="collectorType" value="<%=collectorType%>" />
				<jsp:param name="toolbar_modelId" value="<%=tbar%>" />
				<jsp:param name="operate_contextOID" value="<%=contextOid%>" />
				<jsp:param name="operate_initData" value="<%=data%>" />
				<jsp:param name="custom_table" value="<%=custom_table%>" />
				<jsp:param name="callback" value="collectorCallBack" />
				<jsp:param name="params" value="<%=parm%>" />
			</jsp:include>
			<tr>
				<td valign="top">
					<div class="pt-formbutton" text="ȷ��" id="saveForm"
						onclick="submitForm()"></div>
					<div class="pt-formbutton" text="ȡ��" id="closebutton"
						onclick="collector.cancelSubmit()"></div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
<script type="text/javascript">
	// ȷ��
	function submitForm(){
		var grid = pt.ui.get("<%=gridId%>");
		var records = grid.getSelections();
  	  	opener.<%=callback%>(records);
  	  	window.close();
	}
	//�ռ����� �ص�
 	function collectorCallBack(result){
 		// �ռ���׷�����ݷ���
 		collector.appendResult(result,"<%=gridId%>");
	}
	//ˢ��ҳ��,��ͼ�ؼ����¼���
	function reload() {
		mainForm.submit();
	}
</script>