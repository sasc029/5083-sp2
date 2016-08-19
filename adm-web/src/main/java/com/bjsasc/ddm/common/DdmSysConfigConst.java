package com.bjsasc.ddm.common;

import com.bjsasc.plm.core.system.config.ConfigHelper;

/**
 * ���Ź������������س���
 * 
 * @author gaolingjie, 2013-3-26
 */
public class DdmSysConfigConst {

	/** Ĭ�Ϸָ��� */
	public static final String SEPARATOR = ConfigHelper.PATH_SEPARATOR;

	/** ���Ź���ģ��Ĭ������·����/plm/ddm */
	public static final String PATH_DDM_DEFAULT = ConfigHelper.DEFAULT_PATH
			+ SEPARATOR + "ddm";

	/** Ĭ�Ϸַ���Ϣ�����ļ���·�� */
	public static final String PATH_DEFAULT_DISTRIBUTE_INFO = PATH_DDM_DEFAULT
			+ SEPARATOR + "defaultDistributeInfo";

	/** ���ŵ������ļ���·�� */
	public static final String PATH_DISTRIBUTE_ORDER_CONFIG = PATH_DDM_DEFAULT
			+ SEPARATOR + "distributeOrderConfig";
	
	/** '���ŵ�������'��ť·�� */
	public static final String PATH_WORKFLOWBUTTON_CONFIG = PATH_DDM_DEFAULT
			+ SEPARATOR + "workFlowButton";

	/** �������������ļ���·�� */
	public static final String PATH_LIFECYCLE_CONFIG = PATH_DDM_DEFAULT
			+ SEPARATOR + "lifecycleConfig";

	/** �Ƿ��ڹ��������޸���������״̬ */
	public static final String PARAM_CHANGE_LIFECYCLE_IN_WORKFLOW = "changeLifecycleInWorkflow";

	/** �Ƿ��Զ��������ŵ� */
	public static final String PARAM_AUTO_CREATE_DISTRIBUTE_ORDER = "autoCreateDistributeOrder";

	// __________________ �Զ��������ŵ��ַ���Ϣ������� start _________________________
	/** �ַ���Ϣ���Ʋ�����id */
	public static final String PARAM_DIS_INFO_NAME = "disInfoName";

	/** �ַ���Ϣid������id */
	public static final String PARAM_DIS_INFO_ID = "disInfoId";

	/** �ַ�����������id */
	public static final String PARAM_DIS_INFO_NUM = "disInfoNum";

	/** �ַ���Ϣ���Ͳ�����id */
	public static final String PARAM_DIS_INFO_TYPE = "disInfoType";

	/** �ַ���ʽ������id */
	public static final String PARAM_DIS_INFO_MEDIA_TYPE = "disMediaType";

	/** ��ע������id */
	public static final String PARAM_DIS_INFO_NOTE = "note";
	// __________________ �Զ��������ŵ��ַ���Ϣ������� end _________________________
	
	/** �ύ����������ť����*/
	public static final String PARAM_SUBMITTOWORKFLOW = "submitToWorkFlow";
	
}
