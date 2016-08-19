package com.bjsasc.ddm.distribute.validation;

import com.bjsasc.avidm.core.site.Site;
import com.bjsasc.avidm.core.site.SiteHelper;
import com.bjsasc.plm.collaboration.config.constant.DmcConfigConstant;
import com.bjsasc.plm.collaboration.config.service.DmcConfigHelper;
import com.bjsasc.plm.collaboration.config.service.DmcConfigService;
import com.bjsasc.plm.collaboration.site.model.DCSiteAttribute;
import com.bjsasc.plm.collaboration.site.service.DCSiteAttributeHelper;
import com.bjsasc.plm.operate.Action;
import com.bjsasc.plm.ui.UIDataInfo;
import com.bjsasc.plm.ui.validation.UIState;
import com.bjsasc.plm.ui.validation.ValidationFilter;

/**
 * ���ŵ����վ�㰴ť������֤��
 * ��վ��Ϊ��������ʱ������ʾ��������ť��
 * 
 * @author guowei 2013-11-04
 */
public class DistributeValidationSite implements ValidationFilter {
	
	@SuppressWarnings("deprecation")
	public UIState doActionFilter(Action action, UIDataInfo uiData) {
		// ��ȡϵͳ����ģʽ
		DmcConfigService dmcConfigService = DmcConfigHelper.getService();
		boolean isDcDeployModel = dmcConfigService.getIsDealOnDC();
		Site localSite = SiteHelper.getSiteService().findLocalSiteInfo();
		if(localSite!=null){
			DCSiteAttribute dcSiteAttr = DCSiteAttributeHelper.getDCSiteAttrService().findDcSiteAttrByDTSiteId(
					localSite.getInnerId());
			//���������ģʽ
			if (dcSiteAttr!=null && isDcDeployModel == true && "true".equals(dcSiteAttr.getIsSiteControl())) {
				return UIState.DISABLED;
			} else {
				return UIState.ENABLED;
			}
		}else{
			return UIState.ENABLED;
		}
	}
}
