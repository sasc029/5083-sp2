package com.bjsasc.adm.active.validation;

import java.util.List;

import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.model.activedocument.ActiveDocument;
import com.bjsasc.adm.active.model.activeorderlink.ActiveOrderLink;
import com.bjsasc.adm.common.AdmLifeCycleConstUtil;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.operate.Action;
import com.bjsasc.plm.ui.UIDataInfo;
import com.bjsasc.plm.ui.validation.UIState;
import com.bjsasc.plm.ui.validation.ValidationFilter;

/**
 * ֱ����ⰴť��֤��
 * �����ļ������е��ݣ��������½���������ֹ״̬��ֱ�����
 * 
 * @author xuhuiling 2013-10-31
 */
public class PutIntoStorageValidation implements ValidationFilter {

	public UIState doActionFilter(Action action, UIDataInfo uiData) {
		Persistable object = uiData.getMainObject();
		if (object instanceof LifeCycleManaged) {
			LifeCycleManaged lifeCycle = (LifeCycleManaged) object;
			String stateName = lifeCycle.getLifeCycleInfo().getStateName();

			if (AdmLifeCycleConstUtil.LC_NEW.getName().equals(stateName)
					|| AdmLifeCycleConstUtil.LC_APPROVETERMINATE.getName().equals(stateName)) {
				if (object instanceof ActiveDocument) {
					List<ActiveOrderLink> resultBeforeList = AdmHelper.getActiveOrderService().getActiveOrderLinkByBeforeObject(object.getClassId()+":"+object.getInnerId());//��ǰ����
					List<ActiveOrderLink> resultAfterList = AdmHelper.getActiveOrderService().getActiveOrderLinkByAfterObject(object.getClassId()+":"+object.getInnerId());//�ĺ����
					if(resultBeforeList.size() !=0 || resultAfterList.size() !=0){
						return UIState.DISABLED;//�����ļ����������е�����ʱ�����ļ�ֱ����ⲻ����
					}
				}

				return UIState.ENABLED;//����
			} else {
				return UIState.DISABLED;//Ĭ�ϲ�����
			}
		}
		return UIState.DISABLED;//Ĭ�ϲ�����
	}
}
