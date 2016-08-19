package com.bjsasc.ddm.distribute.validation;

import java.util.List;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService;
import com.bjsasc.plm.core.change.ECO;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.option.OptionHelper;
import com.bjsasc.plm.core.option.OptionValue;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.operate.Action;
import com.bjsasc.plm.ui.UIDataInfo;
import com.bjsasc.plm.ui.validation.UIState;
import com.bjsasc.plm.ui.validation.ValidationFilter;

/**
 * ���ŵ���ť������֤��
 * ֻ����������Ϊ�½���������ֹ�������ܾ����ַ������ܾ����ַ�������ֹ�����ȼ����˵�״̬���ύ��ť�ſ��á�
 * 
 * @author gengancong 2013-3-27
 */
public class DistributeValidationOrder implements ValidationFilter {
	
	public UIState doActionFilter(Action action, UIDataInfo uiData) {
		Persistable object = uiData.getMainObject();
		DistributeOrder disOrder = (DistributeOrder)object;
		
		if (object != null && object instanceof LifeCycleManaged) {
			LifeCycleManaged workable = (LifeCycleManaged) object;
			String state = workable.getLifeCycleInfo().getStateName();
			if ("com.bjsasc.ddm.OrderPropterty.submitToWorkflow".equals(action.getId())) {
				if (ConstUtil.LC_NEW.getName().equals(state) || 
					ConstUtil.LC_SCHEDULING.getName().equals(state) || ConstUtil.LC_BACKOFF.getName().equals(state)) {// �ַ������ύ��ť���ӷַ���Ϣҳǩת�Ƶ��ַ����Ĳ����б���
					return UIState.ENABLED;
				} 
				//�����ܾ���������ֹ���ύʱ��Ҫ���� �������Ƿ������ж�
				if ( ConstUtil.LC_APPROVE_REJECT.getName().equals(state)||ConstUtil.LC_APPROVE_TERMINATE.getName().equals(state)) {
					Context context = disOrder.getContextInfo().getContext();
					OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disFlowConfig_check");
					if ("false".equals(value.getValue())) {
						return UIState.DISABLED;
					} else {
						return UIState.ENABLED;
					}	
				}
				//�ַ������ܾ��ͷַ�������ֹ���ύʱ��Ҫ���� �������Ƿ������ж�
				if ( ConstUtil.LC_DDM_APPROVE_REJECT.getName().equals(state)||ConstUtil.LC_DDM_APPROVE_TERMINATE.getName().equals(state)) {
					Context context = disOrder.getContextInfo().getContext();
					OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disFlowConfig_checkNew");
					if ("false".equals(value.getValue())) {
						return UIState.DISABLED;
					} else {
						return UIState.ENABLED;
					}	
				}
			} else {
				if (ConstUtil.LC_NEW.getName().equals(state) || ConstUtil.LC_SCHEDULING.getName().equals(state) || ConstUtil.LC_DDM_APPROVE_REJECT.getName().equals(state)
						|| ConstUtil.LC_DDM_APPROVE_TERMINATE.getName().equals(state) 
						|| ConstUtil.LC_APPROVE_TERMINATE.getName().equals(state)
						|| ConstUtil.LC_APPROVE_REJECT.getName().equals(state)) {
					return UIState.ENABLED;
				}
			}
		}
		return UIState.DISABLED;
	}
}
