package com.bjsasc.ddm.distribute.validation;

import com.bjsasc.avidm.core.site.Site;
import com.bjsasc.avidm.core.site.SiteHelper;
import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeelectask.DistributeElecTask;
import com.bjsasc.ddm.distribute.service.distributeelectask.DistributeElecTaskService;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.operate.Action;
import com.bjsasc.plm.ui.UIDataInfo;
import com.bjsasc.plm.ui.validation.UIState;
import com.bjsasc.plm.ui.validation.ValidationFilter;

/**
 * �ҽӰ�ť��֤��
 * ��ǰվ��ΪĿ��վ��ʱ���ҽӰ�ť��ʾ��
 * 
 * @author guowei 2013-12-04
 */
public class DistributeValidationObject implements ValidationFilter {
	
	@SuppressWarnings("deprecation")
	public UIState doActionFilter(Action action, UIDataInfo uiData) {
		Persistable object = uiData.getMainObject();
		DistributeElecTask task = (DistributeElecTask)object;
		//��ȡ��ǰվ��
		Site selfSite = SiteHelper.getSiteService().findLocalSiteInfo();
		String targetSiteId = task.getTargetSiteId();
		//��ȡ��ǰվ��ID
		String selfSiteInnerId = "";
		if(selfSite!=null){
			selfSiteInnerId = selfSite.getInnerId();
		}
		//�����ǰվ��ΪĿ��վ��(���շ�)
		if(selfSiteInnerId.equals(targetSiteId) && (ConstUtil.ELEC_TASKTYPE_OUT).equals(task.getElecTaskType())){
			//����������������״̬Ϊδǩ�գ�����ʾ��
			if((ConstUtil.LC_NOT_SIGNED.getName()).equals(task.getLifeCycleInfo().getStateName())){
				return UIState.DISABLED;
			}else{
				//�ж��Ƿ�Ϊ���ӷַ�
				DistributeElecTaskService service = DistributeHelper.getDistributeElecTaskService();
				Boolean flag = service.getDistributeElecTaskIsEntity(task);
				if(flag == false){
					//���ӷַ�������ʾ
					return UIState.DISABLED; 
				}else{
					return UIState.ENABLED;
				}
			}
		}else{
			return UIState.DISABLED;
		}
	}
}
