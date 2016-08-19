package com.bjsasc.ddm.common;

import com.bjsasc.plm.core.system.config.ConfigHelper;

/**
 * 发放管理参数配置相关常量
 * 
 * @author gaolingjie, 2013-3-26
 */
public class DdmSysConfigConst {

	/** 默认分隔符 */
	public static final String SEPARATOR = ConfigHelper.PATH_SEPARATOR;

	/** 发放管理模块默认配置路径：/plm/ddm */
	public static final String PATH_DDM_DEFAULT = ConfigHelper.DEFAULT_PATH
			+ SEPARATOR + "ddm";

	/** 默认分发信息配置文件夹路径 */
	public static final String PATH_DEFAULT_DISTRIBUTE_INFO = PATH_DDM_DEFAULT
			+ SEPARATOR + "defaultDistributeInfo";

	/** 发放单配置文件夹路径 */
	public static final String PATH_DISTRIBUTE_ORDER_CONFIG = PATH_DDM_DEFAULT
			+ SEPARATOR + "distributeOrderConfig";
	
	/** '发放到工作流'按钮路径 */
	public static final String PATH_WORKFLOWBUTTON_CONFIG = PATH_DDM_DEFAULT
			+ SEPARATOR + "workFlowButton";

	/** 生命周期配置文件夹路径 */
	public static final String PATH_LIFECYCLE_CONFIG = PATH_DDM_DEFAULT
			+ SEPARATOR + "lifecycleConfig";

	/** 是否在工作流中修改生命周期状态 */
	public static final String PARAM_CHANGE_LIFECYCLE_IN_WORKFLOW = "changeLifecycleInWorkflow";

	/** 是否自动创建发放单 */
	public static final String PARAM_AUTO_CREATE_DISTRIBUTE_ORDER = "autoCreateDistributeOrder";

	// __________________ 自动创建发放单分发信息相关配置 start _________________________
	/** 分发信息名称参数的id */
	public static final String PARAM_DIS_INFO_NAME = "disInfoName";

	/** 分发信息id参数的id */
	public static final String PARAM_DIS_INFO_ID = "disInfoId";

	/** 分发份数参数的id */
	public static final String PARAM_DIS_INFO_NUM = "disInfoNum";

	/** 分发信息类型参数的id */
	public static final String PARAM_DIS_INFO_TYPE = "disInfoType";

	/** 分发方式参数的id */
	public static final String PARAM_DIS_INFO_MEDIA_TYPE = "disMediaType";

	/** 备注参数的id */
	public static final String PARAM_DIS_INFO_NOTE = "note";
	// __________________ 自动创建发放单分发信息相关配置 end _________________________
	
	/** 提交到工作流按钮配置*/
	public static final String PARAM_SUBMITTOWORKFLOW = "submitToWorkFlow";
	
}
