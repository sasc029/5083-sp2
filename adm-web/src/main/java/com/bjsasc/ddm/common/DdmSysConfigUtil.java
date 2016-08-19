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
 * 读取配置信息工具类
 * 
 * @author gaolingjie, 2013-3-26
 */
public class DdmSysConfigUtil {
	private DdmSysConfigUtil(){}

	private static final Logger LOG = Logger.getLogger(DdmSysConfigUtil.class);

	/**
	 * 是否在工作流中修改生命周期状态
	 * 
	 * @return true 在工作留中修改生命周期状态， false 不在工作流中修改生命周期状态
	 */
	public static boolean changeLifecycleInWorkflow() {
		String configValue = getConfigValue(
				DdmSysConfigConst.PATH_LIFECYCLE_CONFIG,
				DdmSysConfigConst.PARAM_CHANGE_LIFECYCLE_IN_WORKFLOW);
		return "true".equals(configValue) ? true : false;
	}

	/**
	 * 是否自动创建发放单的配置。
	 * 
	 * @return true 自动创建发放单； false 不自动创建发放单
	 */
	public static boolean isAutoCreateDistributeOrder() {
		String configValue = getConfigValue(
				DdmSysConfigConst.PATH_DISTRIBUTE_ORDER_CONFIG,
				DdmSysConfigConst.PARAM_AUTO_CREATE_DISTRIBUTE_ORDER);
		return "true".equals(configValue) ? true : false;
	}

	/**
	 * 获取配置项的当前值， 如果没有找到对应的配置项，返回null
	 * 
	 * @param folderPath
	 *            配置项所述文件夹的位置
	 * @param paramId
	 *            配置项id
	 * @return 配置项的当前值
	 */
	public static String getConfigValue(String folderPath, String paramId) {
		String configValue = null;

		ConfigParameterBean param = getConfigParam(folderPath, paramId);
		if (null != param) {
			configValue = param.getCurrentValue();
		}
		configValue = (null == configValue ? "" : configValue.trim());

		LOG.debug("从 " + folderPath + " 读取 " + paramId + " 参数的配置，配置项的值为："
				+ configValue);
		return configValue;
	}

	/**
	 * 获取配置项的实体bean 如果配置项不存在，返回null
	 * 
	 * @param folderPath
	 *            配置项所属文件夹
	 * @param paramId
	 *            配置项id
	 * @return 配置项实体bean
	 */
	public static ConfigParameterBean getConfigParam(String folderPath,
			String paramId) {
		ConfigParameterBean param = ConfigServiceUtil.getConfigService()
				.getParameter(folderPath, paramId);
		return param;
	}

	/**
	 * 默认分发信息配置
	 * 
	 * @return
	 */
	public static List<Map<String,String>> getDefaultDisInfo() {
		List<Map<String,String>> configValue = getConfigParam(
				DdmSysConfigConst.PATH_DEFAULT_DISTRIBUTE_INFO);
		return configValue;
	}

	/**
	 * 获取配置项的实体bean集合
	 * 
	 * @param folderPath
	 *            配置项所属文件夹
	 * @return List<Map<String,String>>
	 *            配置项实体bean集合
	 */
	public static List<Map<String,String>> getConfigParam(String folderPath) {
		// 返回值
		List<Map<String,String>> configValue = new ArrayList<Map<String,String>>();
		// 获取默认分发信息配置文件夹下的所有状态子文件夹
		List<ConfigFolderBean> folders = ConfigServiceUtil.getConfigService().getFoldersByParentId(folderPath);
		if (!AssertUtil.assertNull(folders)) {
			for (ConfigFolderBean folder : folders) {
				// 根据文件夹信息获取状态名称、标识信息
				List<ConfigParameterBean> params = ConfigServiceUtil.getConfigService().getParametersByFolderId(
						folder.getFolderId(), "1");
				if (!AssertUtil.assertNull(params)) {
					Map<String,String> configValueMap = new HashMap<String,String>();
					for (ConfigParameterBean param : params) {
						// 获取参数当前值
						String paramValue = ConfigUtil.getParameterValue(param);
						// 设置状态标志的id及name属性
						configValueMap.put(param.getParamId(), paramValue);
					}
					configValue.add(configValueMap);
				}
			}
		}
		return configValue;
	}
	
	/**
	 * 提交到工作流按钮显示配置
	 * @return true 显示'提交到工作流'按钮
	 * 		   false 显示'提交发放'按钮
	 */
	public static boolean submitToWorkFlow() {
		String configValue = getConfigValue(
				DdmSysConfigConst.PATH_WORKFLOWBUTTON_CONFIG,
				DdmSysConfigConst.PARAM_SUBMITTOWORKFLOW);
		return "true".equals(configValue) ? true : false;
	}
}
