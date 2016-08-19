package com.bjsasc.ddm.distribute.validation;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.plm.operate.Action;
import com.bjsasc.plm.ui.UIDataInfo;
import com.bjsasc.plm.ui.validation.UIState;
import com.bjsasc.plm.ui.validation.ValidationFilter;
/**
 * �����еķַ����ݵġ���Ǵ�ӡ����ťֻ���ڡ��ӹ��С�״̬ʱ����ʾ
 * @author yangzhenzhou
 *
 */
public class DistributePaperTaskObjectPrintValidation implements ValidationFilter {
	public UIState doActionFilter(Action action, UIDataInfo uiData) {
		DistributePaperTask disPaper = (DistributePaperTask)uiData.getMainObject();
		if(disPaper != null){
			String stateName=disPaper.getLifeCycleInfo().getStateName();
			if(ConstUtil.LC_PROCESSING.getName().equals(stateName)){
				return UIState.ENABLED;
			}
		}
		return UIState.DISABLED;
	}
}
