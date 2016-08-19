package com.bjsasc.adm.active.validation;

import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.service.admmodelservice.AdmModelService;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.operate.Action;
import com.bjsasc.plm.ui.UIDataInfo;
import com.bjsasc.plm.ui.validation.UIState;
import com.bjsasc.plm.ui.validation.ValidationFilter;

/**
 * 创建现行文件按钮验证。
 * 只有文件夹或其父文件夹有对应模型时，才可创建现行文件。
 * 
 * @author yanjia 2013-5-22
 */
public class CreateActiveDocumentValidation implements ValidationFilter {
	public UIState doActionFilter(Action action, UIDataInfo uiData) {
		//String folderOid = "SubFolder:dcb12cb211a8b4900e97a240fc7e27f1";
		//String folderOid="Cabinet:b48803b1a164d4a56fd48ab444ef6222";
		String folderOid;
		Persistable object = uiData.getMainObject();
		if(object!=null){	
			folderOid = Helper.getOid(object);
		}else {
			folderOid = "";
		}
		AdmModelService amService = AdmHelper.getAdmModelService();
		String resultOid = amService.getModelFolder(folderOid);
		if (!resultOid.isEmpty()) {
			return UIState.ENABLED;
		}
		return UIState.ENABLED;
	}
}
