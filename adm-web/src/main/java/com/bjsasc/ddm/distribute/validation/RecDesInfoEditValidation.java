package com.bjsasc.ddm.distribute.validation;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.operate.Action;
import com.bjsasc.plm.ui.UIDataInfo;
import com.bjsasc.plm.ui.validation.UIState;
import com.bjsasc.plm.ui.validation.ValidationFilter;
/**
 * 
 * @author kangyanfei
 *
 */
public class RecDesInfoEditValidation implements ValidationFilter {

	@Override
	public UIState doActionFilter(Action action, UIDataInfo uiData) {
		DistributeOrder object = (DistributeOrder)uiData.getMainObject();
		//查询回收销毁单
		Persistable obj = Helper.getPersistService().getObject(object.getClassId(), object.getInnerId());

		if(obj != null && obj instanceof DistributeOrder){
			String stateName = ((DistributeOrder)obj).getStateName();

			//发放单状态为（新建、调度中、审批终止）显示（添加，编辑，删除）按钮。
			if(ConstUtil.LC_NEW.getName().equals(stateName) 
					|| ConstUtil.LC_SCHEDULING.getName().equals(stateName)
					|| ConstUtil.LC_APPROVE_TERMINATE.getName().equals(stateName)
					||ConstUtil.LC_APPROVE_REJECT.getName().equals(stateName)){
				return UIState.ENABLED;
			}
		}

		return UIState.DISABLED;
	}

}
