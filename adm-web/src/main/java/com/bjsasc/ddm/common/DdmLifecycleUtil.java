package com.bjsasc.ddm.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import com.bjsasc.ddm.distribute.model.distributelifecycle.DistributeDataLifecycleModel;
import com.bjsasc.ddm.distribute.model.distributelifecycle.DistributeLifecycleState;
import com.bjsasc.plm.core.util.ConfigFileUtil;
import com.bjsasc.plm.core.util.XmlFileUtil;

/**
 * 生命周期状态工具类
 * 
 * @author gaolingjie, 2013-3-7
 */
public class DdmLifecycleUtil {
	private DdmLifecycleUtil(){}

	private static final Logger LOG = Logger.getLogger(DdmLifecycleUtil.class);

	// 是否在工作流中改变生命周期状态，默认为false
	private static boolean updateStateInWorkflow = false;

	// 发放管理模块配置信息根目录
	private static final String DDM_HOME_PATH = ConfigFileUtil.PLM_HOME_PATH
			+ File.separator + "ddm";

	// 发放管理模块生命周期配置路径
	private static final String LIFECYCLE_CONFIG_FILE_PAHT = DDM_HOME_PATH
			+ File.separator + "ddmlifecycle" + File.separator
			+ "ddmlifecycles.xml";

	/**
	 * 发放管理对象生命周期集合，key 为生命周期对象类的全名称， value为生命周期模型
	 */
	private static Map<String, DistributeDataLifecycleModel> lifecycleModelMap = new HashMap<String, DistributeDataLifecycleModel>();

	// 读取配置
	static {
		loadConfig();
	}

	/**
	 * 读取配置
	 */
	public static void loadConfig() {
		// 获取xml文件的Document对象
		Document xmlDoc = XmlFileUtil
				.loadByFilePath(LIFECYCLE_CONFIG_FILE_PAHT);
		LOG.debug("读取发放管理生命周期模板，文件路径：" + LIFECYCLE_CONFIG_FILE_PAHT);
		// 获取根节点
		Element rootEle = xmlDoc.getRootElement();

		readUpdateStateInWorkflowConfig(rootEle);

		List<Element> ddmObjectElements = rootEle
				.elements(DdmLifecycleConfigConst.DDM_OBJCECT_NODE_CONST);
		for (Element ddmObjectEle : ddmObjectElements) {
			String className = ddmObjectEle
					.attributeValue(DdmLifecycleConfigConst.CLASS_ATTR_CONST);
			DistributeDataLifecycleModel lifecycleModel = readLifecycleModel(ddmObjectEle);
			lifecycleModelMap.put(className, lifecycleModel);
		}
	}

	/**
	 * 是否在工作流中修改生命周期状态
	 * 
	 * @return true 在工作流中修改状态 ， false 不在工作流中修改状态
	 */
	public static boolean isUpdateStateInWorkflow() {
		LOG.debug("获取updateStateInWorkflow：" + updateStateInWorkflow);
		return updateStateInWorkflow;
	}

	/**
	 * 获取生命周期模型组成的map，map的key值为对象所属类的全名称，value值为DistributeDataLifecycleModel对象
	 * 
	 * @return 生命周期模型组成的map
	 */
	public static Map<String, DistributeDataLifecycleModel> getLifecycleModelMap() {
		return lifecycleModelMap;
	}

	/**
	 * 
	 * @param targetClass
	 * @return
	 */
	public static DistributeDataLifecycleModel getLifecycleModel(
			String targetClass) {
		LOG.debug("读取发放管理生命周期模板，模板标识为：" + targetClass);
		DistributeDataLifecycleModel lifecycleModel = lifecycleModelMap
				.get(targetClass);
		LOG.debug("获取生命周期模型：" + lifecycleModel);
		return lifecycleModel;
	}

	/**
	 * 读取updateStateInWorkflow属性的值
	 * 
	 * @param rootElement
	 */
	private static void readUpdateStateInWorkflowConfig(Element rootElement) {
		String attrValue = rootElement
				.attributeValue(DdmLifecycleConfigConst.UPDATE_STATE_IN_WORKFLOW_ATTR_CONST);
		if (null != attrValue && "true".equals(attrValue.trim())) {
			updateStateInWorkflow = true;
		}
		LOG.debug("读取生命周期配置文件："
				+ DdmLifecycleConfigConst.UPDATE_STATE_IN_WORKFLOW_ATTR_CONST
				+ "=" + updateStateInWorkflow);
	}

	/**
	 * 获取生命周期模型
	 * 
	 * @param ddmObjectElement
	 * @return
	 */
	private static DistributeDataLifecycleModel readLifecycleModel(
			Element ddmObjectElement) {

		List<DistributeLifecycleState> stateList = readLifecycleState(ddmObjectElement);

		DistributeDataLifecycleModel lifecycleModel = new DistributeDataLifecycleModel(
				stateList);
		LOG.debug("读取发放管理生命周期模型：" + lifecycleModel);
		return lifecycleModel;

	}

	/**
	 * 读取对象 的生命周期状态
	 * 
	 * @param ddmObjectElement
	 *            发放管理对象节点
	 * @return 生命周期集合
	 */
	private static List<DistributeLifecycleState> readLifecycleState(
			Element ddmObjectElement) {
		List<DistributeLifecycleState> stateList = new ArrayList<DistributeLifecycleState>();

		List<Element> stateElementList = ddmObjectElement
				.elements(DdmLifecycleConfigConst.STATE_NODE_CONST);

		for (Element stateElement : stateElementList) {
			String name = stateElement
					.attributeValue(DdmLifecycleConfigConst.NAME_ATTR_CONST);
			String nextStateId = stateElement
					.attributeValue(DdmLifecycleConfigConst.NEXT_STATE_NAME_ATTR_CONST);
			String previousStateId = stateElement
					.attributeValue(DdmLifecycleConfigConst.PREVIOUS_STATE_NAME);
			String rejectiveStateId = stateElement
					.attributeValue(DdmLifecycleConfigConst.REJECTIVE_STATE_NAME_ATTR_CONST);
			DistributeLifecycleState lifecycleState = new DistributeLifecycleState(
					name, nextStateId, previousStateId, rejectiveStateId);
			stateList.add(lifecycleState);
		}

		return stateList;
	}
}
