package com.bjsasc.adm.common;

import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.model.activecontext.ActiveContext;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.folder.Cabinet;

/**
 * 现行文件管理参数初始化。
 * 
 * @author gengancong 2013-07-01
 */
public abstract class ActiveInitParameter {

	private static ActiveContext activeContext;

	private static String activeCabinetOid;

	public static ActiveContext getActiveContext() {
		init();
		return activeContext;
	}

	public static String getActiveCabinetOid() {
		init();
		return activeCabinetOid;
	}

	static {
		init();
	}

	public static void init() {
		if (activeContext == null) {
			activeContext = AdmHelper.getActiveContextService().getActiveContext();
		}
		Cabinet cabinet = activeContext.getCabinetDefault();
		activeCabinetOid = Helper.getOid(cabinet);
	}

}
