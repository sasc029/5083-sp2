package com.bjsasc.adm.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import com.bjsasc.adm.active.model.activelifecycle.ActiveLifecycleModel;
import com.bjsasc.adm.active.model.activelifecycle.ActiveLifecycleState;
import com.bjsasc.plm.core.util.ConfigFileUtil;
import com.bjsasc.plm.core.util.XmlFileUtil;

/**
 * 生命周期状态工具类
 * 
 * @author yanjia, 2013-6-25
 */
@SuppressWarnings("unchecked")
public class AdmLifeCycleLoad {
	
	private AdmLifeCycleLoad() {
		
	}

	private static final Logger LOG = Logger.getLogger(AdmLifeCycleLoad.class);

	/** 生命周期配置文件根节点名称 */
	public static final String ROOT_NODE_CONST = "admLifecycle";

	/** admObject节点名称*/
	public static final String ADM_OBJCECT_NODE_CONST = "admObject";

	/** admObject节点-class属性名称 */
	public static final String CLASS_ATTR_CONST = "class";

	/** state节点名称 */
	public static final String STATE_NODE_CONST = "state";

	/** state节点-name属性名称*/
	public static final String NAME_ATTR_CONST = "name";

	/** state节点-rejectiveStateName属性名称 */
	public static final String REJECTIVE_STATE_NAME_ATTR_CONST = "rejectiveStateName";

	/** state节点-nextStateName属性名称*/
	public static final String NEXT_STATE_NAME_ATTR_CONST = "nextStateName";

	/** state节点-previousStateName属性名称*/
	public static final String PREVIOUS_STATE_NAME = "previousStateName";

	/** 现行数据管理模块配置信息根目录 */
	private static final String ADM_HOME_PATH = ConfigFileUtil.PLM_HOME_PATH + File.separator + "adm";

	/** 现行数据管理模块生命周期配置路径*/
	private static final String LIFECYCLE_CONFIG_FILE_PAHT = ADM_HOME_PATH + File.separator + "admlifecycle"
			+ File.separator + "admlifecycles.xml";

	/**
	 * 现行数据管理对象生命周期集合，key 为生命周期对象类的全名称， value为生命周期模型
	 */
	private static Map<String, ActiveLifecycleModel> lifecycleModelMap = new HashMap<String, ActiveLifecycleModel>();

	// 读取配置
	static {
		loadConfig();
	}

	/**
	 * 读取配置
	 */
	public static void loadConfig() {
		// 获取xml文件的Document对象
		Document xmlDoc = XmlFileUtil.loadByFilePath(LIFECYCLE_CONFIG_FILE_PAHT);
		LOG.debug("读取现行数据管理生命周期模板，文件路径：" + LIFECYCLE_CONFIG_FILE_PAHT);
		// 获取根节点
		Element rootEle = xmlDoc.getRootElement();

		List<Element> admObjectElements = rootEle.elements(ADM_OBJCECT_NODE_CONST);
		for (Element admObjectEle : admObjectElements) {
			String className = admObjectEle.attributeValue(CLASS_ATTR_CONST);
			ActiveLifecycleModel lifecycleModel = readLifecycleModel(admObjectEle);
			lifecycleModelMap.put(className, lifecycleModel);
		}
	}

	/**
	 * 获取生命周期模型组成的map，map的key值为对象所属类的全名称，value值为DistributeDataLifecycleModel对象
	 * 
	 * @return 生命周期模型组成的map
	 */
	public static Map<String, ActiveLifecycleModel> getLifecycleModelMap() {
		return lifecycleModelMap;
	}

	/**
	 * 
	 * @param targetClass
	 * @return
	 */
	public static ActiveLifecycleModel getLifecycleModel(String targetClass) {
		LOG.debug("读取现行数据管理生命周期模板，模板标识为：" + targetClass);
		ActiveLifecycleModel lifecycleModel = lifecycleModelMap.get(targetClass);
		LOG.debug("获取生命周期模型：" + lifecycleModel);
		if(lifecycleModel==null){
			try {
				Class<?> temp= Class.forName(targetClass).getSuperclass();
				String className = temp.getName();
				if("java.lang.Object".equals(className)){
					return null;
				}
				return getLifecycleModel(className);
			} catch (ClassNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				return null;
			}
		}
		return lifecycleModel;
	}

	/**
	 * 获取生命周期模型
	 * 
	 * @param ddmObjectElement
	 * @return
	 */
	private static ActiveLifecycleModel readLifecycleModel(Element admObjectElement) {
		List<ActiveLifecycleState> stateList = readLifecycleState(admObjectElement);
		ActiveLifecycleModel lifecycleModel = new ActiveLifecycleModel(stateList);
		LOG.debug("读取现行数据管理生命周期模型：" + lifecycleModel);
		return lifecycleModel;
	}

	/**
	 * 读取对象 的生命周期状态
	 * 
	 * @param admObjectElement 现行数据管理对象节点
	 * @return 生命周期集合
	 */

	private static List<ActiveLifecycleState> readLifecycleState(Element admObjectElement) {
		List<ActiveLifecycleState> stateList = new ArrayList<ActiveLifecycleState>();
		List<Element> stateElementList = admObjectElement.elements(STATE_NODE_CONST);
		for (Element stateElement : stateElementList) {
			String name = stateElement.attributeValue(NAME_ATTR_CONST);
			String nextStateId = stateElement.attributeValue(NEXT_STATE_NAME_ATTR_CONST);
			String previousStateId = stateElement.attributeValue(PREVIOUS_STATE_NAME);
			String rejectiveStateId = stateElement.attributeValue(REJECTIVE_STATE_NAME_ATTR_CONST);
			ActiveLifecycleState lifecycleState = new ActiveLifecycleState(name, nextStateId, previousStateId,
					rejectiveStateId);
			stateList.add(lifecycleState);
		}
		return stateList;
	}
}
