package com.bjsasc.adm.active.validation;

import java.util.HashMap;
import java.util.Map;

import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.access.AccessControlHelper;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.operate.Action;
import com.bjsasc.plm.ui.UIDataInfo;
import com.bjsasc.plm.ui.validation.UIState;
import com.bjsasc.plm.ui.validation.ValidationFilter;

/**
 * 现行文件的编辑按钮验证。
 * 
 * @author gengancong 2013-8-28
 */
public class ActiveValidation implements ValidationFilter {

	private static Map<String, String> actionMap = new HashMap<String, String>();

	static {
		actionMap.put("com.bjsasc.adm.activedata.delete", "plm_delete");
		actionMap.put("com.bjsasc.adm.activedocument.edit", "plm_modify");
		actionMap.put("com.bjsasc.adm.activeorder.edit", "plm_modify");
		actionMap.put("com.bjsasc.adm.activeset.edit", "plm_modify");
		actionMap.put("com.bjsasc.plm.newVersion.adm", "plm_revise");
		actionMap.put("com.bjsasc.plm.copy.adm", "access");
		actionMap.put("com.bjsasc.plm.saveas.adm", "plm_create");
		actionMap.put("com.bjsasc.plm.move.adm", "plm_move_into_domain");
		actionMap.put("com.bjsasc.plm.rename.adm", "plm_rename");
		actionMap.put("com.bjsasc.plm.reasignLifeCycleTemplate.adm", "plm_set_state");
		actionMap.put("com.bjsasc.plm.ApproveOrder.approve.adm", "plm_modify");
		actionMap.put("com.bjsasc.adm.activeset.create", "plm_create");
		actionMap.put("com.bjsasc.adm.activeorder.create", "plm_create");
		actionMap.put("com.bjsasc.plm.domain.author.adm", "setPermission");
		actionMap.put("adm.storage.operate", "plm_create");
		actionMap.put("ddm.distribute.operate", "plm_create");
		// actionMap.put("", "");
	}

	@Override
	public UIState doActionFilter(Action action, UIDataInfo uiData) {

		Persistable object = uiData.getMainObject();

		if (object instanceof LifeCycleManaged && object != null) {

			boolean flag = true;
			String operateId = actionMap.get(action.getId());
			if (object instanceof Domained && operateId != null && operateId.length() > 0) {
				User currentUser = SessionHelper.getService().getUser();
				Domained domained = (Domained) object;
				flag = AccessControlHelper.getService().hasEntityPermission(currentUser, operateId, domained);				
			}
			if (flag == false) {
				return UIState.DISABLED;
			}
			return UIState.ENABLED;
		}
		return UIState.ENABLED;
	}
}
