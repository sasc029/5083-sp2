package com.bjsasc.adm.active.manage;

import com.bjsasc.plm.core.type.TypeHelper;

/**
 * 现行文件管理类。
 * 
 * @author yanjia 2013-5-15
 */
public class ActiveDocumentManage {
	
	private ActiveDocumentManage() {
		
	}

	/** 是否显示密级属性(模型参数配置) */
	public static final String ISSHOWSECLEVEL_PARAM = "isShowSecLevel";

	/**
	 * 是否显示密级
	 * 
	 * @param classId
	 * @return
	 */
	public static boolean isShowSecLevelAttr(String classId) {
		return getBoolValue(classId, ISSHOWSECLEVEL_PARAM);
	}

	/**
	 * 获取模型参数配置中的布尔值
	 * 
	 * @param classId
	 * @param paramName
	 * @return
	 */
	private static boolean getBoolValue(String classId, String paramName) {
		boolean ret = false;
		try {
			String isShow = TypeHelper.getService().getParamValue(classId, paramName);
			ret = Boolean.valueOf(isShow).booleanValue();
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}
}
