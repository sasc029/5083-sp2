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
		// (true�����ŵ�״̬��Ϊ�ַ��е�ʱ���Զ������ַ����ݣ��ַ���Ϣ��Xml�ļ���false��������Xml�ļ�)
		if ("true".equalsIgnoreCase(is_AutoExportXml)) {
			return UIState.ENABLED;
		}

		return UIState.DISABLED;
	}

}
