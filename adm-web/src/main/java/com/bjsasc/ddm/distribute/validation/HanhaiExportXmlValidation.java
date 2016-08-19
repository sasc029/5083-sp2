package com.bjsasc.ddm.distribute.validation;

import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.service.distributecommonconfig.DistributeCommonConfigService;
import com.bjsasc.plm.operate.Action;
import com.bjsasc.plm.ui.UIDataInfo;
import com.bjsasc.plm.ui.validation.UIState;
import com.bjsasc.plm.ui.validation.ValidationFilter;
/**
 * 
 * @author zhangguoqiang
 *
 */
public class HanhaiExportXmlValidation implements ValidationFilter {

	@Override
	public UIState doActionFilter(Action action, UIDataInfo uiData) {
		
		DistributeCommonConfigService distributeCommonConfigService = DistributeHelper.getDistributeCommonConfigService();
		String is_AutoExportXml = distributeCommonConfigService.getConfigValueByConfigId("Is_AutoExportXml");
		// (true：发放单状态变为分发中的时候自动导出分发数据，分发信息的Xml文件；false：不导出Xml文件)
		if ("true".equalsIgnoreCase(is_AutoExportXml)) {
			return UIState.ENABLED;
		}

		return UIState.DISABLED;
	}

}
