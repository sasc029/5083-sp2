package com.bjsasc.ddm.common;

import java.util.Map;

import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.principal.User;
import com.cascc.platform.aa.AAProvider;
import com.cascc.platform.aa.api.data.AAUserData;
import com.cascc.platform.aa.api.util.AAMappingType;

public class SessionDataUtil {
	private SessionDataUtil(){};

	/**
	 * 获取当前用户的组织名称
	 * 
	 * @return 用户主要隶属组织名称
	 */
	public static String getUserOrgName() {
		String orgName = "";
		User user = SessionHelper.getService().getUser();
		if (null != user) {
			AAUserData aaUserData = AAProvider.getUserService().getUser(
					SessionHelper.getService().getSessionContext(),
					user.getAaUserInnerId());
			Map<String, String> userInfoMap = AAMappingType.getMap4AAUserData(
					null, aaUserData);
			orgName = userInfoMap.get("USERORGNAME");
		}

		return orgName;
	}
}
