package com.bjsasc.ddm.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bjsasc.platform.config.model.ConfigFolderBean;
import com.bjsasc.platform.config.model.ConfigParameterBean;
import com.bjsasc.platform.config.util.ConfigServiceUtil;
import com.bjsasc.platform.config.util.ConfigUtil;
import com.bjsasc.platform.objectmodel.util.AssertUtil;

/**
 * ��ȡ������Ϣ������
 * 
 * @author gaolingjie, 2013-3-26
 */
public class DdmSysConfigUtil {
	private DdmSysConfigUtil(){}

	private static final Logger LOG = Logger.getLogger(DdmSysConfigUtil.class);

	/**
	 * �Ƿ��ڹ��������޸���������״̬
	 * 
	 * @return true �ڹ��������޸���������״̬�� false ���ڹ��������޸���������״̬
	 */
	public static boolean changeLifecycleInWorkflow() {
		String configValue = getConfigValue(
				DdmSysConfigConst.PATH_LIFECYCLE_CONFIG,
				DdmSysConfigConst.PARAM_CHANGE_LIFECYCLE_IN_WORKFLOW);
		return "true".equals(configValue) ? true : false;
	}

	/**
	 * �Ƿ��Զ��������ŵ������á�
	 * 
	 * @return true �Զ��������ŵ��� false ���Զ��������ŵ�
	 */
	public static boolean isAutoCreateDistributeOrder() {
		String configValue = getConfigValue(
				DdmSysConfigConst.PATH_DISTRIBUTE_ORDER_CONFIG,
				DdmSysConfigConst.PARAM_AUTO_CREATE_DISTRIBUTE_ORDER);
		return "true".equals(configValue) ? true : false;
	}

	/**
	 * ��ȡ������ĵ�ǰֵ�� ���û���ҵ���Ӧ�����������null
	 * 
	 * @param folderPath
	 *            �����������ļ��е�λ��
	 * @param paramId
	 *            ������id
	 * @return ������ĵ�ǰֵ
	 */
	public static String getConfigValue(String folderPath, String paramId) {
		String configValue = null;

		ConfigParameterBean param = getConfigParam(folderPath, paramId);
		if (null != param) {
			configValue = param.getCurrentValue();
		}
		configValue = (null == configValue ? "" : configValue.trim());

		LOG.debug("�� " + folderPath + " ��ȡ " + paramId + " ���������ã��������ֵΪ��"
				+ configValue);
		return configValue;
	}

	/**
	 * ��ȡ�������ʵ��bean �����������ڣ�����null
	 * 
	 * @param folderPath
	 *            �����������ļ���
	 * @param paramId
	 *            ������id
	 * @return ������ʵ��bean
	 */
	public static ConfigParameterBean getConfigParam(String folderPath,
			String paramId) {
		ConfigParameterBean param = ConfigServiceUtil.getConfigService()
				.getParameter(folderPath, paramId);
		return param;
	}

	/**
	 * Ĭ�Ϸַ���Ϣ����
	 * 
	 * @return
	 */
	public static List<Map<String,String>> getDefaultDisInfo() {
		List<Map<String,String>> configValue = getConfigParam(
				DdmSysConfigConst.PATH_DEFAULT_DISTRIBUTE_INFO);
		return configValue;
	}

	/**
	 * ��ȡ�������ʵ��bean����
	 * 
	 * @param folderPath
	 *            �����������ļ���
	 * @return List<Map<String,String>>
	 *            ������ʵ��bean����
	 */
	public static List<Map<String,String>> getConfigParam(String folderPath) {
		// ����ֵ
		List<Map<String,String>> configValue = new ArrayList<Map<String,String>>();
		// ��ȡĬ�Ϸַ���Ϣ�����ļ����µ�����״̬���ļ���
		List<ConfigFolderBean> folders = ConfigServiceUtil.getConfigService().getFoldersByParentId(folderPath);
		if (!AssertUtil.assertNull(folders)) {
			for (ConfigFolderBean folder : folders) {
				// �����ļ�����Ϣ��ȡ״̬���ơ���ʶ��Ϣ
				List<ConfigParameterBean> params = ConfigServiceUtil.getConfigService().getParametersByFolderId(
						folder.getFolderId(), "1");
				if (!AssertUtil.assertNull(params)) {
					Map<String,String> configValueMap = new HashMap<String,String>();
					for (ConfigParameterBean param : params) {
						// ��ȡ������ǰֵ
						String paramValue = ConfigUtil.getParameterValue(param);
						// ����״̬��־��id��name����
						configValueMap.put(param.getParamId(), paramValue);
					}
					configValue.add(configValueMap);
				}
			}
		}
		return configValue;
	}
	
	/**
	 * �ύ����������ť��ʾ����
	 * @return true ��ʾ'�ύ��������'��ť
	 * 		   false ��ʾ'�ύ����'��ť
	 */
	public static boolean submitToWorkFlow() {
		String configValue = getConfigValue(
				DdmSysConfigConst.PATH_WORKFLOWBUTTON_CONFIG,
				DdmSysConfigConst.PARAM_SUBMITTOWORKFLOW);
		return "true".equals(configValue) ? true : false;
	}
}
