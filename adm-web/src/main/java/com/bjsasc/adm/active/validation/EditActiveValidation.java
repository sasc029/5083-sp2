package com.bjsasc.adm.active.validation;

import com.bjsasc.adm.common.AdmLifeCycleConstUtil;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.operate.Action;
import com.bjsasc.plm.ui.UIDataInfo;
import com.bjsasc.plm.ui.validation.UIState;
import com.bjsasc.plm.ui.validation.ValidationFilter;

/**
 * 现行文件的编辑按钮验证。
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
			/** 现行数据管理-生命周期-审批中 */
			if (AdmLifeCycleConstUtil.LC_APPROVING.getName().equals(stateName)) {
				flag = true;
			} else
			/** 现行数据管理-生命周期-受控中 */
			if (AdmLifeCycleConstUtil.LC_CONTROLLING.getName().equals(stateName)) {
				flag = true;
			} else
			/** 现行数据管理-生命周期-发放中 */
			if (AdmLifeCycleConstUtil.LC_PROVIDING.getName().equals(stateName)) {
				flag = true;
			} else
			/** 现行数据管理-生命周期-已删除 */
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
