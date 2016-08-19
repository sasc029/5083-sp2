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
 * ��������״̬������
 * 
 * @author yanjia, 2013-6-25
 */
@SuppressWarnings("unchecked")
public class AdmLifeCycleLoad {
	
	private AdmLifeCycleLoad() {
		
	}

	private static final Logger LOG = Logger.getLogger(AdmLifeCycleLoad.class);

	/** �������������ļ����ڵ����� */
	public static final String ROOT_NODE_CONST = "admLifecycle";

	/** admObject�ڵ�����*/
	public static final String ADM_OBJCECT_NODE_CONST = "admObject";

	/** admObject�ڵ�-class�������� */
	public static final String CLASS_ATTR_CONST = "class";

	/** state�ڵ����� */
	public static final String STATE_NODE_CONST = "state";

	/** state�ڵ�-name��������*/
	public static final String NAME_ATTR_CONST = "name";

	/** state�ڵ�-rejectiveStateName�������� */
	public static final String REJECTIVE_STATE_NAME_ATTR_CONST = "rejectiveStateName";

	/** state�ڵ�-nextStateName��������*/
	public static final String NEXT_STATE_NAME_ATTR_CONST = "nextStateName";

	/** state�ڵ�-previousStateName��������*/
	public static final String PREVIOUS_STATE_NAME = "previousStateName";

	/** �������ݹ���ģ��������Ϣ��Ŀ¼ */
	private static final String ADM_HOME_PATH = ConfigFileUtil.PLM_HOME_PATH + File.separator + "adm";

	/** �������ݹ���ģ��������������·��*/
	private static final String LIFECYCLE_CONFIG_FILE_PAHT = ADM_HOME_PATH + File.separator + "admlifecycle"
			+ File.separator + "admlifecycles.xml";

	/**
	 * �������ݹ�������������ڼ��ϣ�key Ϊ�������ڶ������ȫ���ƣ� valueΪ��������ģ��
	 */
	private static Map<String, ActiveLifecycleModel> lifecycleModelMap = new HashMap<String, ActiveLifecycleModel>();

	// ��ȡ����
	static {
		loadConfig();
	}

	/**
	 * ��ȡ����
	 */
	public static void loadConfig() {
		// ��ȡxml�ļ���Document����
		Document xmlDoc = XmlFileUtil.loadByFilePath(LIFECYCLE_CONFIG_FILE_PAHT);
		LOG.debug("��ȡ�������ݹ�����������ģ�壬�ļ�·����" + LIFECYCLE_CONFIG_FILE_PAHT);
		// ��ȡ���ڵ�
		Element rootEle = xmlDoc.getRootElement();

		List<Element> admObjectElements = rootEle.elements(ADM_OBJCECT_NODE_CONST);
		for (Element admObjectEle : admObjectElements) {
			String className = admObjectEle.attributeValue(CLASS_ATTR_CONST);
			ActiveLifecycleModel lifecycleModel = readLifecycleModel(admObjectEle);
			lifecycleModelMap.put(className, lifecycleModel);
		}
	}

	/**
	 * ��ȡ��������ģ����ɵ�map��map��keyֵΪ�����������ȫ���ƣ�valueֵΪDistributeDataLifecycleModel����
	 * 
	 * @return ��������ģ����ɵ�map
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
		LOG.debug("��ȡ�������ݹ�����������ģ�壬ģ���ʶΪ��" + targetClass);
		ActiveLifecycleModel lifecycleModel = lifecycleModelMap.get(targetClass);
		LOG.debug("��ȡ��������ģ�ͣ�" + lifecycleModel);
		if(lifecycleModel==null){
			try {
				Class<?> temp= Class.forName(targetClass).getSuperclass();
				String className = temp.getName();
				if("java.lang.Object".equals(className)){
					return null;
				}
				return getLifecycleModel(className);
			} catch (ClassNotFoundException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
				return null;
			}
		}
		return lifecycleModel;
	}

	/**
	 * ��ȡ��������ģ��
	 * 
	 * @param ddmObjectElement
	 * @return
	 */
	private static ActiveLifecycleModel readLifecycleModel(Element admObjectElement) {
		List<ActiveLifecycleState> stateList = readLifecycleState(admObjectElement);
		ActiveLifecycleModel lifecycleModel = new ActiveLifecycleModel(stateList);
		LOG.debug("��ȡ�������ݹ�����������ģ�ͣ�" + lifecycleModel);
		return lifecycleModel;
	}

	/**
	 * ��ȡ���� ����������״̬
	 * 
	 * @param admObjectElement �������ݹ������ڵ�
	 * @return �������ڼ���
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
