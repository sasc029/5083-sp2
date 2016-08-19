<%@page import="com.bjsasc.adm.active.helper.AdmHelper"%>
<%@page import="com.bjsasc.adm.active.model.query.ActiveQuerySchema"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page language="java" %>
    <%
	List<ActiveQuerySchema> listSchema = AdmHelper.getActiveChangeService().getActiveQuerySchemaList();
	List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
	int defaultSchema=0;
	String selectValue="";
	 for (ActiveQuerySchema schema : listSchema) {
 		Map<String, String> tempMap = new HashMap<String, String>();
            String key=schema.getSchemaName();
			String value =schema.getInnerId();
			String classid=schema.getActivename();
			defaultSchema=schema.getDefaultSchema();
			if(defaultSchema==1){
				selectValue=schema.getInnerId();
				key+="(д╛хо)";
				tempMap.put("text",key);
				tempMap.put("value",value);
				tempMap.put("classid",classid);
			}else{
				tempMap.put("text",key);
				tempMap.put("value",value);
				tempMap.put("classid",classid);
			}
			listMap.add(tempMap);
	}
	String json = DataUtil.listToJson(listMap);
	out.print(json);
	out.flush();
    %>