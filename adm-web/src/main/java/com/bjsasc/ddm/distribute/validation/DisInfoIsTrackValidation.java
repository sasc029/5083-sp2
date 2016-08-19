package com.bjsasc.ddm.distribute.validation;

import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.change.ECO;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.operate.Action;
import com.bjsasc.plm.ui.UIDataInfo;
import com.bjsasc.plm.ui.validation.UIState;
import com.bjsasc.plm.ui.validation.ValidationFilter;

public class DisInfoIsTrackValidation implements ValidationFilter {
	public UIState doActionFilter(Action action, UIDataInfo uiData) {
		DistributeObject object = (DistributeObject)uiData.getMainObject();
		Persistable obj = Helper.getPersistService().getObject(object.getDataClassId(), object.getDataInnerId());
		if (obj != null && obj instanceof ECO) {
			ECO ecoObj = (ECO)obj;
			if (ecoObj.getIsTrack() == 0) {
				return UIState.DISABLED;
			} else {
				return UIState.ENABLED;
			}
		}
		return UIState.ENABLED;
	}
}
