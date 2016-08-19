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
				// �������ڸ���
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
	// ������
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
	pMap.put("templateName", "���ŵ���������");
	pMap.put("objectName", "DistributeOrder");
	pMap.put("stateNameFrom", "�½�");
	pMap.put("stateNameTo", "�½�");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "���ŵ���������");
	pMap.put("objectName", "DistributeOrder");
	pMap.put("stateNameFrom", "������");
	pMap.put("stateNameTo", "������");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "���ŵ���������");
	pMap.put("objectName", "DistributeOrder");
	pMap.put("stateNameFrom", "������");
	pMap.put("stateNameTo", "������");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "���ŵ���������");
	pMap.put("objectName", "DistributeOrder");
	pMap.put("stateNameFrom", "�������");
	pMap.put("stateNameTo", "�������˻�");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "���ŵ���������");
	pMap.put("objectName", "DistributeOrder");
	pMap.put("stateNameFrom", "������ֹ");
	pMap.put("stateNameTo", "������ֹ");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "���ŵ���������");
	pMap.put("objectName", "DistributeOrder");
	pMap.put("stateNameFrom", "�ַ���");
	pMap.put("stateNameTo", "�ַ���");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "���ŵ���������");
	pMap.put("objectName", "DistributeOrder");
	pMap.put("stateNameFrom", "���˻�");
	pMap.put("stateNameTo", "���˻�");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "���ŵ���������");
	pMap.put("objectName", "DistributeOrder");
	pMap.put("stateNameFrom", "�ѷַ�");
	pMap.put("stateNameTo", "�ѷַ�");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "�ַ���Ϣ��������");
	pMap.put("objectName", "DistributeInfo");
	pMap.put("stateNameFrom", "δ����");
	pMap.put("stateNameTo", "δ�ַ�");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "�ַ���Ϣ��������");
	pMap.put("objectName", "DistributeInfo");
	pMap.put("stateNameFrom", "�������");
	pMap.put("stateNameTo", "�������");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "�ַ���Ϣ��������");
	pMap.put("objectName", "DistributeInfo");
	pMap.put("stateNameFrom", "�ѷ���");
	pMap.put("stateNameTo", "�ѷַ�");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "DistributeElecTask");
	pMap.put("stateNameFrom", "δǩ��");
	pMap.put("stateNameTo", "δǩ��");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "DistributeElecTask");
	pMap.put("stateNameFrom", "��ǩ��");
	pMap.put("stateNameTo", "��ǩ��");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "DistributeElecTask");
	pMap.put("stateNameFrom", "�Ѿܾ�");
	pMap.put("stateNameTo", "�Ѿܾ�");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "DistributeElecTask");
	pMap.put("stateNameFrom", "���");
	pMap.put("stateNameTo", "�����");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "�ַ�������������");
	pMap.put("objectName", "DistributeOrderObjectLink");
	pMap.put("stateNameFrom", "�ַ���");
	pMap.put("stateNameTo", "�ַ���");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "�ַ�������������");
	pMap.put("objectName", "DistributeOrderObjectLink");
	pMap.put("stateNameFrom", "���");
	pMap.put("stateNameTo", "�����");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "ֽ��������������");
	pMap.put("objectName", "DistributePaperTask");
	pMap.put("stateNameFrom", "�ӹ���");
	pMap.put("stateNameTo", "�ӹ���");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "ֽ��������������");
	pMap.put("objectName", "DistributePaperTask");
	pMap.put("stateNameFrom", "���Ʊ��˻�");
	pMap.put("stateNameTo", "���Ʊ��˻�");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "ֽ��������������");
	pMap.put("objectName", "DistributePaperTask");
	pMap.put("stateNameFrom", "�ӹ����˻�");
	pMap.put("stateNameTo", "�ӹ����˻�");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "ֽ��������������");
	pMap.put("objectName", "DistributePaperTask");
	pMap.put("stateNameFrom", "����δǩ��");
	pMap.put("stateNameTo", "����δǩ��");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "ֽ��������������");
	pMap.put("objectName", "DistributePaperTask");
	pMap.put("stateNameFrom", "������");
	pMap.put("stateNameTo", "������");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "ֽ��������������");
	pMap.put("objectName", "DistributePaperTask");
	pMap.put("stateNameFrom", "�������");
	pMap.put("stateNameTo", "�������");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "ֽ��������������");
	pMap.put("objectName", "DistributePaperTask");
	pMap.put("stateNameFrom", "������");
	pMap.put("stateNameTo", "�ַ���");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "ֽ��������������");
	pMap.put("objectName", "DistributePaperTask");
	pMap.put("stateNameFrom", "�������");
	pMap.put("stateNameTo", "�������");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "ֽ��������������");
	pMap.put("objectName", "DistributePaperTask");
	pMap.put("stateNameFrom", "�ѷ���");
	pMap.put("stateNameTo", "�ѷַ�");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "ActiveDocument");
	pMap.put("stateNameFrom", "�½�");
	pMap.put("stateNameTo", "�½�");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "ActiveDocument");
	pMap.put("stateNameFrom", "������");
	pMap.put("stateNameTo", "������");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "ActiveDocument");
	pMap.put("stateNameFrom", "�������");
	pMap.put("stateNameTo", "�������˻�");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "ActiveDocument");
	pMap.put("stateNameFrom", "������ֹ");
	pMap.put("stateNameTo", "������ֹ");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "ActiveDocument");
	pMap.put("stateNameFrom", "�ܿ���");
	pMap.put("stateNameTo", "�ܿ���");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "ActiveDocument");
	pMap.put("stateNameFrom", "������");
	pMap.put("stateNameTo", "�ַ���");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "ActiveDocument");
	pMap.put("stateNameFrom", "�ѷ���");
	pMap.put("stateNameTo", "�ѷַ�");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "ActiveDocument");
	pMap.put("stateNameFrom", "������");
	pMap.put("stateNameTo", "������");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "ActiveDocument");
	pMap.put("stateNameFrom", "��ɾ��");
	pMap.put("stateNameTo", "�ѻ���");
	paramList.add(pMap);
	
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "ActiveOrder");
	pMap.put("stateNameFrom", "�½�");
	pMap.put("stateNameTo", "�½�");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "ActiveOrder");
	pMap.put("stateNameFrom", "������");
	pMap.put("stateNameTo", "������");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "ActiveOrder");
	pMap.put("stateNameFrom", "�������");
	pMap.put("stateNameTo", "�������˻�");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "ActiveOrder");
	pMap.put("stateNameFrom", "������ֹ");
	pMap.put("stateNameTo", "������ֹ");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "ActiveOrder");
	pMap.put("stateNameFrom", "�ܿ���");
	pMap.put("stateNameTo", "�ܿ���");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "ActiveOrder");
	pMap.put("stateNameFrom", "������");
	pMap.put("stateNameTo", "�ַ���");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "ActiveOrder");
	pMap.put("stateNameFrom", "�ѷ���");
	pMap.put("stateNameTo", "�ѷַ�");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "ActiveOrder");
	pMap.put("stateNameFrom", "������");
	pMap.put("stateNameTo", "������");
	paramList.add(pMap);
	pMap = new HashMap<String, String>();
	pMap.put("templateName", "����������������");
	pMap.put("objectName", "ActiveOrder");
	pMap.put("stateNameFrom", "��ɾ��");
	pMap.put("stateNameTo", "�ѻ���");
	paramList.add(pMap);
	return paramList;
}
 
%>