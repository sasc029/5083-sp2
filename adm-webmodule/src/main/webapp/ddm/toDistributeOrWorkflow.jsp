<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService"%>
<%@page import="com.bjsasc.plm.core.change.ECO"%>
<%@page import="com.bjsasc.plm.core.option.OptionHelper"%>
<%@page import="com.bjsasc.plm.core.option.OptionValue"%>
<%@page import="com.bjsasc.plm.core.lifecycle.LifeCycleManaged"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@include file="/plm/plm.jsp"%>

<%
	String oid = request.getParameter("OID");
	Persistable object = Helper.getPersistService().getObject(oid);
	DistributeOrder disOrder=(DistributeOrder)object;
	String cycState = "";
	String results = "";
	String orderType=disOrder.getOrderType();
	Map<String, String> result = new HashMap<String, String>();
	if (object != null && object instanceof LifeCycleManaged) {
		LifeCycleManaged workable = (LifeCycleManaged) object;
		cycState = workable.getLifeCycleInfo().getStateName();
	}
	if (!"".equals(cycState) && ConstUtil.LC_NEW.getName().equals(cycState)
			|| ConstUtil.LC_DDM_APPROVE_REJECT.getName().equals(cycState)
			|| ConstUtil.LC_DDM_APPROVE_TERMINATE.getName().equals(cycState)){
				// ȡ���½��������Ƿ��߹���������
				Context context = disOrder.getContextInfo().getContext();
				OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disFlowConfig_checkNew");

				if ("false".equals(value.getValue())) {
					result.put("success", "submitDistribute");
					results = DataUtil.mapToSimpleJson(result);// ����"��"���ύ������
				} else {
					result.put("success", "submitToWorkflow");
					results = DataUtil.mapToSimpleJson(result);// ����"��"���ύ��������
		}
	} else if (!"".equals(cycState) && ConstUtil.LC_SCHEDULING.getName().equals(cycState)
					|| ConstUtil.LC_BACKOFF.getName().equals(cycState)
					|| ConstUtil.LC_APPROVE_TERMINATE.getName().equals(cycState) 
					|| ConstUtil.LC_APPROVE_REJECT.getName().equals(cycState)) {
		// ȡ�ó��ηַ�����
		Context context = disOrder.getContextInfo().getContext();
		OptionValue value = OptionHelper.getService().getOptionValue(
				context, "disMange_disFlowConfig_check");
		if ("false".equals(value.getValue())) {
			// �ַ������ύ��ť���ӷַ���Ϣҳǩת�Ƶ��ַ����Ĳ����б���
			//���ηַ�����"��"���ύ����������
			result.put("success", "submitDis");
			results = DataUtil.mapToSimpleJson(result);
		} else {
			result.put("success", "submitDisToWorkflow");
			results = DataUtil.mapToSimpleJson(result);// ���ηַ�����"��"���ύ��������
		}

	}
	out.print(results);
%>