package com.bjsasc.ddm.distribute.validation;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.operate.Action;
import com.bjsasc.plm.ui.UIDataInfo;
import com.bjsasc.plm.ui.validation.UIState;
import com.bjsasc.plm.ui.validation.ValidationFilter;

/**
 * 
 * ���ŵ�ֻ�����½���״̬����ʾ�ύ���Ű�ť
 * 
 * @author gengancong 2013-3-27
 */
public class DistributeInfoEditValidation implements ValidationFilter {
	public UIState doActionFilter(Action action, UIDataInfo uiData) {
		Persistable object = uiData.getMainObject();
		if (object != null && object instanceof LifeCycleManaged) {
			LifeCycleManaged workable = (LifeCycleManaged) object;
			String state = workable.getLifeCycleInfo().getStateName();
			if (ConstUtil.LC_SCHEDULING.getName().equals(state) || ConstUtil.LC_BACKOFF.getName().equals(state)
					|| ConstUtil.LC_NEW.getName().equals(state)
					|| ConstUtil.LC_APPROVE_TERMINATE.getName().equals(state)
					|| ConstUtil.LC_APPROVE_REJECT.getName().equals(state)
					||ConstUtil.LC_DDM_APPROVE_TERMINATE.getName().equals(state)
					||ConstUtil.LC_DDM_APPROVE_REJECT.getName().equals(state)) {
				return UIState.ENABLED;
			}
		}
		return UIState.DISABLED;
	}
}
