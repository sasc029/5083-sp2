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
 * 挂接按钮验证。
 * 当前站点为目标站点时，挂接按钮显示。
 * 
 * @author guowei 2013-12-04
 */
public class DistributeValidationObject implements ValidationFilter {
	
	@SuppressWarnings("deprecation")
	public UIState doActionFilter(Action action, UIDataInfo uiData) {
		Persistable object = uiData.getMainObject();
		DistributeElecTask task = (DistributeElecTask)object;
		//获取当前站点
		Site selfSite = SiteHelper.getSiteService().findLocalSiteInfo();
		String targetSiteId = task.getTargetSiteId();
		//获取当前站点ID
		String selfSiteInnerId = "";
		if(selfSite!=null){
			selfSiteInnerId = selfSite.getInnerId();
		}
		//如果当前站点为目标站点(接收方)
		if(selfSiteInnerId.equals(targetSiteId) && (ConstUtil.ELEC_TASKTYPE_OUT).equals(task.getElecTaskType())){
			//电子任务生命周期状态为未签收（不显示）
			if((ConstUtil.LC_NOT_SIGNED.getName()).equals(task.getLifeCycleInfo().getStateName())){
				return UIState.DISABLED;
			}else{
				//判断是否为链接分发
				DistributeElecTaskService service = DistributeHelper.getDistributeElecTaskService();
				Boolean flag = service.getDistributeElecTaskIsEntity(task);
				if(flag == false){
					//链接分发，不显示
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
