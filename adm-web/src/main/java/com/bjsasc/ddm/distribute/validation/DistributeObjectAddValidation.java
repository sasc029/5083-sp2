package com.bjsasc.ddm.distribute.validation;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.operate.Action;
import com.bjsasc.plm.ui.UIDataInfo;
import com.bjsasc.plm.ui.validation.UIState;
import com.bjsasc.plm.ui.validation.ValidationFilter;
/**
 * 
 * @author kangyanfei 2014-07-01
 *
 */
public class DistributeObjectAddValidation implements ValidationFilter {

	@Override
	public UIState doActionFilter(Action action, UIDataInfo uiData) {
		DistributeOrder object = (DistributeOrder)uiData.getMainObject();
		//查询回收销毁单
		Persistable obj = Helper.getPersistService().getObject(object.getClassId(), object.getInnerId());

		if(obj != null && obj instanceof DistributeOrder){
			String orderType = ((DistributeOrder)obj).getOrderType();

			//发放单类型为（回收发放单，销毁发放单）不显示（添加）按钮。
			if(ConstUtil.C_ORDERTYPE_3.equals(orderType) 
					|| ConstUtil.C_ORDERTYPE_2.equals(orderType)){
				return UIState.DISABLED;
			}
		}

		return UIState.ENABLED;
	}

}
