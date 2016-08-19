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
		//��ѯ�������ٵ�
		Persistable obj = Helper.getPersistService().getObject(object.getClassId(), object.getInnerId());

		if(obj != null && obj instanceof DistributeOrder){
			String orderType = ((DistributeOrder)obj).getOrderType();

			//���ŵ�����Ϊ�����շ��ŵ������ٷ��ŵ�������ʾ����ӣ���ť��
			if(ConstUtil.C_ORDERTYPE_3.equals(orderType) 
					|| ConstUtil.C_ORDERTYPE_2.equals(orderType)){
				return UIState.DISABLED;
			}
		}

		return UIState.ENABLED;
	}

}
