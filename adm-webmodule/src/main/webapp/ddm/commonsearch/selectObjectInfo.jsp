<%@page import="com.bjsasc.platform.config.util.ConfigServiceUtil"%>
<%@page import="com.bjsasc.platform.config.model.ConfigFolderBean"%>
<%@page import="com.bjsasc.platform.objectmodel.business.lifeCycle.State"%>
<%@page import="com.bjsasc.plm.core.context.template.LifeCycleUtil"%>
<%@page import="com.cascc.platform.aa.api.util.AADomainUtil"%>
<%@page import="java.util.Vector"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.type.restrict.select.Select"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.type.restrict.select.SelectOption"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Collection"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%	
     String op=request.getParameter("op");
     if(op.equals("selectState")){
    	 List<Map<String, Object>> appAllStates = new Vector<Map<String, Object>>();
    	 Map<String, Object> stateMap = new HashMap<String, Object>();
    		stateMap.put("value", "rdoDistributeState_all");
    		stateMap.put("text", "所有状态");
    		appAllStates.add(stateMap);
    		//取得发放管理模块所有的状态节点
    		Collection<SelectOption> distributeStates = ConstUtil.DISTRIBUTESTATES.getOptionMap().values();
			for (SelectOption option : distributeStates) {
				    String value = option.getValue();
					String text = option.getText();
    				stateMap = new HashMap<String, Object>();
    				stateMap.put("value", value);
    				stateMap.put("text", text);
    				appAllStates.add(stateMap);
    		} 
    		out.print(DataUtil.encode(appAllStates));
    		out.flush();
     }
%>