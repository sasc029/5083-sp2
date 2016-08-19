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
 * ��������״̬������
 * 
 * @author gaolingjie, 2013-3-7
 */
public class DdmLifecycleUtil {
	private DdmLifecycleUtil(){}

	private static final Logger LOG = Logger.getLogger(DdmLifecycleUtil.class);

	// �Ƿ��ڹ������иı���������״̬��Ĭ��Ϊfalse
	private static boolean updateStateInWorkflow = false;

	// ���Ź���ģ��������Ϣ��Ŀ¼
	private static final String DDM_HOME_PATH = ConfigFileUtil.PLM_HOME_PATH
			+ File.separator + "ddm";

	// ���Ź���ģ��������������·��
	private static final String LIFECYCLE_CONFIG_FILE_PAHT = DDM_HOME_PATH
			+ File.separator + "ddmlifecycle" + File.separator
			+ "ddmlifecycles.xml";

	/**
	 * ���Ź�������������ڼ��ϣ�key Ϊ�������ڶ������ȫ���ƣ� valueΪ��������ģ��
	 */
	private static Map<String, DistributeDataLifecycleModel> lifecycleModelMap = new HashMap<String, DistributeDataLifecycleModel>();

	// ��ȡ����
	static {
		loadConfig();
	}

	/**
	 * ��ȡ����
	 */
	public static void loadConfig() {
		// ��ȡxml�ļ���Document����
		Document xmlDoc = XmlFileUtil
				.loadByFilePath(LIFECYCLE_CONFIG_FILE_PAHT);
		LOG.debug("��ȡ���Ź�����������ģ�壬�ļ�·����" + LIFECYCLE_CONFIG_FILE_PAHT);
		// ��ȡ���ڵ�
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
	 * �Ƿ��ڹ��������޸���������״̬
	 * 
	 * @return true �ڹ��������޸�״̬ �� false ���ڹ��������޸�״̬
	 */
	public static boolean isUpdateStateInWorkflow() {
		LOG.debug("��ȡupdateStateInWorkflow��" + updateStateInWorkflow);
		return updateStateInWorkflow;
	}

	/**
	 * ��ȡ��������ģ����ɵ�map��map��keyֵΪ�����������ȫ���ƣ�valueֵΪDistributeDataLifecycleModel����
	 * 
	 * @return ��������ģ����ɵ�map
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
		LOG.debug("��ȡ���Ź�����������ģ�壬ģ���ʶΪ��" + targetClass);
		DistributeDataLifecycleModel lifecycleModel = lifecycleModelMap
				.get(targetClass);
		LOG.debug("��ȡ��������ģ�ͣ�" + lifecycleModel);
		return lifecycleModel;
	}

	/**
	 * ��ȡupdateStateInWorkflow���Ե�ֵ
	 * 
	 * @param rootElement
	 */
	private static void readUpdateStateInWorkflowConfig(Element rootElement) {
		String attrValue = rootElement
				.attributeValue(DdmLifecycleConfigConst.UPDATE_STATE_IN_WORKFLOW_ATTR_CONST);
		if (null != attrValue && "true".equals(attrValue.trim())) {
			updateStateInWorkflow = true;
		}
		LOG.debug("��ȡ�������������ļ���"
				+ DdmLifecycleConfigConst.UPDATE_STATE_IN_WORKFLOW_ATTR_CONST
				+ "=" + updateStateInWorkflow);
	}

	/**
	 * ��ȡ��������ģ��
	 * 
	 * @param ddmObjectElement
	 * @return
	 */
	private static DistributeDataLifecycleModel readLifecycleModel(
			Element ddmObjectElement) {

		List<DistributeLifecycleState> stateList = readLifecycleState(ddmObjectElement);

		DistributeDataLifecycleModel lifecycleModel = new DistributeDataLifecycleModel(
				stateList);
		LOG.debug("��ȡ���Ź�����������ģ�ͣ�" + lifecycleModel);
		return lifecycleModel;

	}

	/**
	 * ��ȡ���� ����������״̬
	 * 
	 * @param ddmObjectElement
	 *            ���Ź������ڵ�
	 * @return �������ڼ���
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
