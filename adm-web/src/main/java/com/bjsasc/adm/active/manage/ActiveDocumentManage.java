package com.bjsasc.adm.active.manage;

import com.bjsasc.plm.core.type.TypeHelper;

/**
 * �����ļ������ࡣ
 * 
 * @author yanjia 2013-5-15
 */
public class ActiveDocumentManage {
	
	private ActiveDocumentManage() {
		
	}

	/** �Ƿ���ʾ�ܼ�����(ģ�Ͳ�������) */
	public static final String ISSHOWSECLEVEL_PARAM = "isShowSecLevel";

	/**
	 * �Ƿ���ʾ�ܼ�
	 * 
	 * @param classId
	 * @return
	 */
	public static boolean isShowSecLevelAttr(String classId) {
		return getBoolValue(classId, ISSHOWSECLEVEL_PARAM);
	}

	/**
	 * ��ȡģ�Ͳ��������еĲ���ֵ
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
