package com.bjsasc.adm.active.validation;

import com.bjsasc.adm.common.AdmLifeCycleConstUtil;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.operate.Action;
import com.bjsasc.plm.ui.UIDataInfo;
import com.bjsasc.plm.ui.validation.UIState;
import com.bjsasc.plm.ui.validation.ValidationFilter;

/**
 * �����ļ��ı༭��ť��֤��
 * 
 * @author gengancong 2013-8-28
 */
public class EditActiveValidation implements ValidationFilter {

	@Override
	public UIState doActionFilter(Action action, UIDataInfo uiData) {

		Persistable object = uiData.getMainObject();

		if (object instanceof LifeCycleManaged && object != null) {

			LifeCycleManaged lcm = (LifeCycleManaged) object;

			String stateName = lcm.getLifeCycleInfo().getStateName();
			boolean flag = false;
			/** �������ݹ���-��������-������ */
			if (AdmLifeCycleConstUtil.LC_APPROVING.getName().equals(stateName)) {
				flag = true;
			} else
			/** �������ݹ���-��������-�ܿ��� */
			if (AdmLifeCycleConstUtil.LC_CONTROLLING.getName().equals(stateName)) {
				flag = true;
			} else
			/** �������ݹ���-��������-������ */
			if (AdmLifeCycleConstUtil.LC_PROVIDING.getName().equals(stateName)) {
				flag = true;
			} else
			/** �������ݹ���-��������-��ɾ�� */
			if (AdmLifeCycleConstUtil.LC_RECYCLE.getName().equals(stateName)) {
				flag = true;
			}
			if (flag) {
				return UIState.DISABLED;
			}
			return UIState.ENABLED;
		}
		return UIState.ENABLED;
	}
}
