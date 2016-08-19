<%@page import="java.util.*"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributelifecycle.LifeCycleUpdateService"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	String op = request.getParameter("op");
	String step = request.getParameter("step");

	Map<String, String> result = new HashMap<String, String>();
	try {
		if ("update".equals(op)) {
			LifeCycleUpdateService lcus = DistributeHelper.getLifeCycleUpdateService();
			List<Map<String, String>> paramList = getParamList();
			
			if (step == null) {
				step = "";
			}
			String[] ss = step.split(",");
			List<String> msgs = new ArrayList<String>();
			for (String s : ss) {
				// 生命周期更新
				msgs.addAll(lcus.updateLifeCycle(paramList, s));
			}
	
			StringBuilder mm = new StringBuilder();
			mm.append("<table>");
			for (String msg : msgs) {
				mm.append("<tr><td>").append(msg).append("</td></tr>");
			}
			mm.append("</table>");
			result.put("mm", mm.toString());
			result.put("success", "true");
		}	
	// 错误处理
	} catch (Exception ex) {
		result.put("success", "false");
		result.put("message", ex.getMessage());
	}
	String results = DataUtil.mapToSimpleJson(result);
	out.print(results);
%>

<%! 
List<Map<String, String>> getParamList() {
	List<Map<String, String>> paramList = new ArrayList<Map<String, String>>();
	Map<String, String> pMap = new HashMap<String, String>();
	pMap.put("templateName", "发放单生命周期");
	pMap.put("objectName", "DistributeOrder");
	pMap.put("stateNameFrom", "新建");
	pMap.put("stateNameTo", "新建");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "发放单生命周期");
	pMap.put("objectName", "DistributeOrder");
	pMap.put("stateNameFrom", "调度中");
	pMap.put("stateNameTo", "调度中");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "发放单生命周期");
	pMap.put("objectName", "DistributeOrder");
	pMap.put("stateNameFrom", "审批中");
	pMap.put("stateNameTo", "审批中");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "发放单生命周期");
	pMap.put("objectName", "DistributeOrder");
	pMap.put("stateNameFrom", "审批打回");
	pMap.put("stateNameTo", "审批被退回");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "发放单生命周期");
	pMap.put("objectName", "DistributeOrder");
	pMap.put("stateNameFrom", "审批终止");
	pMap.put("stateNameTo", "审批终止");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "发放单生命周期");
	pMap.put("objectName", "DistributeOrder");
	pMap.put("stateNameFrom", "分发中");
	pMap.put("stateNameTo", "分发中");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "发放单生命周期");
	pMap.put("objectName", "DistributeOrder");
	pMap.put("stateNameFrom", "已退回");
	pMap.put("stateNameTo", "已退回");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "发放单生命周期");
	pMap.put("objectName", "DistributeOrder");
	pMap.put("stateNameFrom", "已分发");
	pMap.put("stateNameTo", "已分发");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "分发信息生命周期");
	pMap.put("objectName", "DistributeInfo");
	pMap.put("stateNameFrom", "未发送");
	pMap.put("stateNameTo", "未分发");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "分发信息生命周期");
	pMap.put("objectName", "DistributeInfo");
	pMap.put("stateNameFrom", "销毁完成");
	pMap.put("stateNameTo", "销毁完成");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "分发信息生命周期");
	pMap.put("objectName", "DistributeInfo");
	pMap.put("stateNameFrom", "已发送");
	pMap.put("stateNameTo", "已分发");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "电子任务生命周期");
	pMap.put("objectName", "DistributeElecTask");
	pMap.put("stateNameFrom", "未签收");
	pMap.put("stateNameTo", "未签收");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "电子任务生命周期");
	pMap.put("objectName", "DistributeElecTask");
	pMap.put("stateNameFrom", "已签收");
	pMap.put("stateNameTo", "已签收");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "电子任务生命周期");
	pMap.put("objectName", "DistributeElecTask");
	pMap.put("stateNameFrom", "已拒绝");
	pMap.put("stateNameTo", "已拒绝");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "电子任务生命周期");
	pMap.put("objectName", "DistributeElecTask");
	pMap.put("stateNameFrom", "完成");
	pMap.put("stateNameTo", "已完成");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "分发对象生命周期");
	pMap.put("objectName", "DistributeOrderObjectLink");
	pMap.put("stateNameFrom", "分发中");
	pMap.put("stateNameTo", "分发中");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "分发对象生命周期");
	pMap.put("objectName", "DistributeOrderObjectLink");
	pMap.put("stateNameFrom", "完成");
	pMap.put("stateNameTo", "已完成");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "纸质任务生命周期");
	pMap.put("objectName", "DistributePaperTask");
	pMap.put("stateNameFrom", "加工中");
	pMap.put("stateNameTo", "加工中");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "纸质任务生命周期");
	pMap.put("objectName", "DistributePaperTask");
	pMap.put("stateNameFrom", "复制被退回");
	pMap.put("stateNameTo", "复制被退回");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "纸质任务生命周期");
	pMap.put("objectName", "DistributePaperTask");
	pMap.put("stateNameFrom", "加工被退回");
	pMap.put("stateNameTo", "加工被退回");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "纸质任务生命周期");
	pMap.put("objectName", "DistributePaperTask");
	pMap.put("stateNameFrom", "复制未签收");
	pMap.put("stateNameTo", "复制未签收");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "纸质任务生命周期");
	pMap.put("objectName", "DistributePaperTask");
	pMap.put("stateNameFrom", "复制中");
	pMap.put("stateNameTo", "复制中");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "纸质任务生命周期");
	pMap.put("objectName", "DistributePaperTask");
	pMap.put("stateNameFrom", "复制完成");
	pMap.put("stateNameTo", "复制完成");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "纸质任务生命周期");
	pMap.put("objectName", "DistributePaperTask");
	pMap.put("stateNameFrom", "发送中");
	pMap.put("stateNameTo", "分发中");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "纸质任务生命周期");
	pMap.put("objectName", "DistributePaperTask");
	pMap.put("stateNameFrom", "销毁完成");
	pMap.put("stateNameTo", "销毁完成");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "纸质任务生命周期");
	pMap.put("objectName", "DistributePaperTask");
	pMap.put("stateNameFrom", "已发送");
	pMap.put("stateNameTo", "已分发");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "现行数据生命周期");
	pMap.put("objectName", "ActiveDocument");
	pMap.put("stateNameFrom", "新建");
	pMap.put("stateNameTo", "新建");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "现行数据生命周期");
	pMap.put("objectName", "ActiveDocument");
	pMap.put("stateNameFrom", "审批中");
	pMap.put("stateNameTo", "审批中");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "现行数据生命周期");
	pMap.put("objectName", "ActiveDocument");
	pMap.put("stateNameFrom", "审批打回");
	pMap.put("stateNameTo", "审批被退回");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "现行数据生命周期");
	pMap.put("objectName", "ActiveDocument");
	pMap.put("stateNameFrom", "审批终止");
	pMap.put("stateNameTo", "审批终止");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "现行数据生命周期");
	pMap.put("objectName", "ActiveDocument");
	pMap.put("stateNameFrom", "受控中");
	pMap.put("stateNameTo", "受控中");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "现行数据生命周期");
	pMap.put("objectName", "ActiveDocument");
	pMap.put("stateNameFrom", "发放中");
	pMap.put("stateNameTo", "分发中");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "现行数据生命周期");
	pMap.put("objectName", "ActiveDocument");
	pMap.put("stateNameFrom", "已发放");
	pMap.put("stateNameTo", "已分发");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "现行数据生命周期");
	pMap.put("objectName", "ActiveDocument");
	pMap.put("stateNameFrom", "补发中");
	pMap.put("stateNameTo", "补发中");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "现行数据生命周期");
	pMap.put("objectName", "ActiveDocument");
	pMap.put("stateNameFrom", "已删除");
	pMap.put("stateNameTo", "已回收");
	paramList.add(pMap);
	
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "现行数据生命周期");
	pMap.put("objectName", "ActiveOrder");
	pMap.put("stateNameFrom", "新建");
	pMap.put("stateNameTo", "新建");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "现行数据生命周期");
	pMap.put("objectName", "ActiveOrder");
	pMap.put("stateNameFrom", "审批中");
	pMap.put("stateNameTo", "审批中");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "现行数据生命周期");
	pMap.put("objectName", "ActiveOrder");
	pMap.put("stateNameFrom", "审批打回");
	pMap.put("stateNameTo", "审批被退回");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "现行数据生命周期");
	pMap.put("objectName", "ActiveOrder");
	pMap.put("stateNameFrom", "审批终止");
	pMap.put("stateNameTo", "审批终止");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "现行数据生命周期");
	pMap.put("objectName", "ActiveOrder");
	pMap.put("stateNameFrom", "受控中");
	pMap.put("stateNameTo", "受控中");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "现行数据生命周期");
	pMap.put("objectName", "ActiveOrder");
	pMap.put("stateNameFrom", "发放中");
	pMap.put("stateNameTo", "分发中");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "现行数据生命周期");
	pMap.put("objectName", "ActiveOrder");
	pMap.put("stateNameFrom", "已发放");
	pMap.put("stateNameTo", "已分发");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "现行数据生命周期");
	pMap.put("objectName", "ActiveOrder");
	pMap.put("stateNameFrom", "补发中");
	pMap.put("stateNameTo", "补发中");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "现行数据生命周期");
	pMap.put("objectName", "ActiveOrder");
	pMap.put("stateNameFrom", "已删除");
	pMap.put("stateNameTo", "已回收");
	paramList.add(pMap);
	return paramList;
}
 
%>