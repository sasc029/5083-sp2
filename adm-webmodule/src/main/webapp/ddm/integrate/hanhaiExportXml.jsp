<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributecommonconfig.DistributeCommonConfigService"%>
<%@page import="com.bjsasc.ddm.distribute.service.integrate.IntegrateService"%>
<%@page import="com.cascc.avidm.util.SplitString"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@include file="/plm/plm.jsp"%>

<%
	String results = "";
	Map<String, String> result = new HashMap<String, String>();
	
	String contextPath = request.getContextPath();
	String oids = request.getParameter(KeyS.OIDS);
	String callback = request.getParameter(KeyS.CALLBACK);
	List<String> oidList = SplitString.string2List(oids, ",");
	
	DistributeCommonConfigService distributeCommonConfigService = DistributeHelper.getDistributeCommonConfigService();
	String is_AutoExportXml = distributeCommonConfigService.getConfigValueByConfigId("Is_AutoExportXml");
	// 是否开启瀚海之星Xml文件导出功能
	
    result.put("success", "false");
	// (true：发放单状态变为分发中的时候自动导出分发数据，分发信息的Xml文件；false：不导出Xml文件)
	if ("true".equalsIgnoreCase(is_AutoExportXml)) {
		for (String oid : oidList) {
	        IntegrateService integrateService = DistributeHelper.getIntegrateService();
        	integrateService.exportXml(oid);
        	result.put("success", "true");
	    }
	}

    results = DataUtil.mapToSimpleJson(result);
	out.print(results);
%>
