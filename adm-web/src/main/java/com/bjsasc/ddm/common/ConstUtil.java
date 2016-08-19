package com.bjsasc.ddm.common;

import com.bjsasc.plm.Helper;
import com.bjsasc.plm.type.restrict.select.Select;

/**
 * 发放管理用常量类。
 * 
 * @author gengancong 2013-2-22
 */
public class ConstUtil extends ConstLifeCycle {
	/** SPOT_LISTDISTRIBUTETASKS */
	public static final String SPOT_LISTDISTRIBUTETASKS = "ListDistributeTasks";
	/** SPOT_LISTDISTRIBUTEINFOS */
	public static final String SPOT_LISTDISTRIBUTEINFOS = "ListDistributeInfos";
	/** SPOT_LISTDISTRIBUTEORDERS */
	public static final String SPOT_LISTDISTRIBUTEORDERS = "ListDistributeOrders";
	/** SPOT_LISTDISTRIBUTEOBJECTS */
	public static final String SPOT_LISTDISTRIBUTEOBJECTS = "ListDistributeObjects";
	/** SPOT_LISTDISTRIBUTETSKPROPERTYS */
	public static final String SPOT_LISTDISTRIBUTETSKPROPERTYS = "ListDistributeTaskPropertys";
	/** SPOT_LISTDISTRIBUTEPAPERTASKS */
	public static final String SPOT_LISTDISTRIBUTEPAPERTASKS = "ListDistributePaperTasks";
	/** SPOT_LISTDISTRIBUTEPAPERTASKS */
	public static final String SPOT_LISTDISTRIBUTEPAPERTASKSPRINT = "ListDistributePaperTasksPrint";
	/** SPOT_LISTDISTRIBUTEDUPLICATESUBMIT */
	public static final String SPOT_LISTDISTRIBUTEDUPLICATESUBMIT = "ListDistributeDuplicateSubmit";
	/** SPOT_LISTDISTRIBUTEELECTASKS */
	public static final String SPOT_LISTDISTRIBUTEELECTASKSNOTSIGNED = "ListDistributeElecTasks_notSigned";
	public static final String SPOT_LISTDISTRIBUTEELECTASKSSIGNED = "ListDistributeElecTasks_signed";
	public static final String SPOT_LISTDISTRIBUTEELECTASKSCOMPLETED = "ListDistributeElecTasks_completed";
	public static final String SPOT_LISTDISTRIBUTEELECTASKSREFUSESIGNED = "ListDistributeElecTasks_refuseSigned";
	/** SPOT_LISTDISTRIBUTECOMMONSEARCH */
	public static final String SPOT_LISTDISTRIBUTECOMMONSEARCH = "distributeCommonSearch";
	/** SPOT_LISTDISTRIBUTERETURNDETAIL */
	public static final String SPOT_LISTDISTRIBUTERETURNDETAIL = "ListDistributeReturnDetail";
	/** SOPT_LISTDISTRIBUTERETURN */
	public static final String SOPT_LISTDISTRIBUTERETURN = "ListDistributeReturn";
	/** SOPT_LISTDISTRIBUTERETURN */
	public static final String SOPT_LISTDISTRIBUTEWORKLOAD = "ListDistributeWorkloads";
	/** SOPT_LISTDISTRIBUTEINSIDEORDERS */
	public static final String SOPT_LISTDISTRIBUTEINSIDEORDERS = "ListDistributeInsideOrders";
	/** SOPT_LISTDISTRIBUTEDESTORYORDERS */
	public static final String SOPT_LISTDISTRIBUTEDESTORYORDERS = "ListDistributeDestoryOrders";
	/** SOPT_LISTDISTRIBUTEDESTORYDETAILS */
	public static final String SOPT_LISTDISTRIBUTEDESTORYDETAILS = "ListDistributeDestoryDetails";
	/** SOPT_LISTDISTRIBUTEPAPERTASKRETURN */
	public static final String SOPT_LISTDISTRIBUTEPAPERTASKRETURN = "ListDistributePaperTaskReturn";
	/** 回收销毁纸质任务oid存储常量*/
	public static final String SPOT_RECDESPAPERTASK_OID = "RECDESPAPERTASK_OID";
	/**SPOT_LISTDUPLICATEPROCESS */
	public static final String SPOT_LISTDUPLICATEPROCESS = "listduplicateprocess";
	/** SOPT_LISTDISTRIBUTEPAPERTASKRETURNDETAIL */
	public static final String SOPT_LISTDISTRIBUTEPAPERTASKRETURNDETAIL = "ListDistributePaperTaskReturnDetail";
	/** SOPT_LISTDISTRIBUTEOBJECTRETURNDETAIL */
	public static final String SOPT_LISTDISTRIBUTEOBJECTRETURNDETAIL = "ListDistributeObjectReturnDetail";
	/** SOPT_LISTDISTRIBUTEELECOBJECTS */
	public static final String SOPT_LISTDISTRIBUTEELECOBJECTS = "ListDistributeElecObjects";
	/** SOPT_LISTDISTRIBUTEPAPERSIGNOBJECTS */
	public static final String SOPT_LISTDISTRIBUTEPAPERSIGNOBJECTS = "ListDistributePaperSignObjects";
	/** SOPT_LISTDISTRIBUTEFORWARDELECTASKS */
	public static final String SOPT_LISTDISTRIBUTEFORWARDELECTASKS = "ListDistributeForwardElecTasks";
	/** SPOT_LISTDISTRIBUTECOMMONSEARCH */
	public static final String SPOT_LISTDISTRIBUTESONPAPERTASKS = "ListDistributesonPaperTasks";

	public static final String SPOT_LISTDISTRIBUTEELECTASKS = "ListDistributeElectasks";
	/** SPOT_LISTDISTRIBUTEDATAOBJECT */
	public static final String SPOT_LISTDISTRIBUTEDATAOBJECT = "ListDistributeDataObject";
	/** SPOT_LISTDISTRIBUTESYNTASKS */
	public static final String SPOT_LISTDISTRIBUTESYNTASKS = "ListDistributeSynTasks";
	/** @author kangyanfei
	 * SPOT_LISTRECINFO */
	public static final String SPOT_LISTRECINFO = "ListRecInfos";
	/** 
	 * @author kangyanfei
	 * SPOT_LISTDESINFO */
	public static final String SPOT_LISTDESINFO = "ListDesInfos";
	/** 
	 * @author kangyanfei
	 * SPOT_LISTRECDESINFO */
	public static final String SPOT_LISTRECDESINFO = "ListRecDesInfos";
	/** 
	 * @author kangyanfei
	 * SPOT_LISTNEEDRECDESINFONUM */
	public static final String SPOT_LISTNEEDRECDESINFONUM = "ListNeedRecDisInfoNum";
	/** 
	 * @author kangyanfei
	 * SPOT_LISTNEEDDESDISINFONUM */
	public static final String SPOT_LISTNEEDDESDISINFONUM = "ListNeedDesDisInfoNum";
	/** 
	 * @author zhangguoqiang
	 * SPOT_LISTNEEDDESDISINFONUM */
	public static final String SPOT_LISTDISTRIBUTETASKBOX = "ListDistributeTaskBox";

	/** VISTYPE_ALL */
	public static final String VISTYPE_ALL = "all";

	public static final String DISTRIBUTE_ORDER_OID = "DISTRIBUTE_ORDER_OID";

	public static final String DISTRIBUTE_ORDER_OBJECT_LINK_OIDS = "DISTRIBUTE_ORDER_OBJECT_LINK_OIDS";

	public static final String DISTRIBUTE_PAPERTASK_OID = "DISTRIBUTE_PAPERTASK_OID";
	
	public static final String DISTRIBUTE_ELECTASK_OID = "DISTRIBUTE_ELECTASK_OID";

	public static final String DISTRIBUTE_PAPERSIGNTASK_OID = "DISTRIBUTE_PAPERSIGNTASK_OID";
	
	public static final String SPOT_DISTRIBUTEPAPERTASK_OID = "DISTRIBUTEPAPERTASK_OID";
	/** 分发信息完工期限（DisDeadLine） 默认延迟天数 */
	public static final int C_DISDEADLINE_DELAY_DAY = 3;
	
	/**调度状态*/
	public static final String LC_LC_SCHEDULING_STATE ="scheduling";
	/**加工状态*/
	public static final String LC_PROCESSING_STATE ="processing";
	/**复制加工状态*/
	public static final String LC_DUPLICATED_STATE ="duplicated";
	/**发送状态*/
	public static final String LC_SENDING_STATE ="sending";
	/**已发送状态*/
	public static final String LC_SENT_STATE ="sent";

	/**收集对象标识0-更改单和现行单据以外的对象,不含A版本以外的套对象*/
	public static final int COLLECTFLAG_0 = 0;
	/**收集对象标识1-更改单对象*/
	public static final int COLLECTFLAG_1 = 1;
	/**收集对象标识2-现行单据对象*/
	public static final int COLLECTFLAG_2 = 2;
	/**收集对象标识3-A版本以外的套对象*/
	public static final int COLLECTFLAG_3 = 3;

	public static final String C_S_ZERO = "0";
	public static final String C_S_ONE = "1";
	public static final String C_S_TWO = "2";

	/** 分发信息类型（0为单位，1为人员） */
	public static enum DISINFOTYPE {
		USER, ORG
	};

	/** 显示类型 */
	public static enum DISPLAY_TYPE {
		DISPLAY, EDIT
	};
	
	/** 生命周期操作 */
	public static enum LIFECYCLE_OPT {
		PROMOTE, DEMOTE, REJECT
	};

	/** 任务类型（0：纸质任务，1：电子任务，2：跨域，3：回收销毁纸质任务，4:回收销毁电子任务，5:纸质签收任务） */
	public static final String C_TASKTYPE_PAPER = "0";
	/** 任务类型（0：纸质任务，1：电子任务，2：跨域，3：回收销毁纸质任务，4:回收销毁电子任务，5:纸质签收任务） */
	public static final String C_TASKTYPE_ELEC = "1";
	/** 任务类型（0：纸质任务，1：电子任务，2：跨域，3：回收销毁纸质任务，4:回收销毁电子任务，5:纸质签收任务） */
	public static final String C_TASKTYPE_OUTSIGN = "2";
	/** 任务类型（0：纸质任务，1：电子任务，2：跨域，3：回收销毁纸质任务，4:回收销毁电子任务，5:纸质签收任务） */
	public static final String C_TASKYTPE_RECDESPAPER = "3";
	/** 任务类型（0：纸质任务，1：电子任务，2：跨域，3：回收销毁纸质任务，4:回收销毁电子任务，5:纸质签收任务） */
	public static final String C_TASKYTPE_PAPERSIGN = "5";
	
	/** 电子任务类型（0：域内，1：外域，2：反馈） */
	public static final String ELEC_TASKTYPE_IN = "0";
	/** 电子任务类型（0：域内，1：外域，2：反馈） */
	public static final String ELEC_TASKTYPE_OUT = "1";
	/** 电子任务类型（0：域内，1：外域，2：反馈） */
	public static final String ELEC_TASKTYPE_REPLY = "2";

	/** 分发信息类型（0为单位，1为人员，2为站点） */
	public static final String C_DISINFOTYPE_USER = "1";
	/** 分发信息类型（0为单位，1为人员，2为站点） */
	public static final String C_DISINFOTYPE_ORG = "0";
	/** 分发信息类型（0为单位，1为人员，2为站点） */
	public static final String C_DISINFOTYPE_SITE = "2";

	/** 分发信息类型（0为单位，1为人员） */
	public static final String C_DISINFOTYPE_0 = "人员";
	/** 分发信息类型（0为单位，1为人员，2为站点） */
	public static final String C_DISINFOTYPE_1 = "单位";

	/** 单据类型：发放单（0发放单，1补发发放单，2回收发放单，3销毁发放单） */
	public static final String C_ORDERTYPE_3 = "3";
	/** 单据类型：发放单（0发放单，1补发发放单，2回收单，3销毁单） */
	public static final String C_ORDERTYPE_2 = "2";
	/** 单据类型：发放单（0发放单，1补发发放单，2回收单，3销毁单） */
	public static final String C_ORDERTYPE_1 = "1";
	/** 单据类型：发放单（0发放单，1补发发放单，2回收单，3销毁单） */
	public static final String C_ORDERTYPE_0 = "0";

	/** 分发方式:分发信息（0为直接分发，1为补发，2为移除，3为转发，4为回收，5为销毁） */
	public static final String C_DISTYPE_DIS = "0";
	public static final String C_DISTYPE_ADDDIS = "1";
	public static final String C_DISTYPE_REMOVE = "2";
	public static final String C_DISTYPE_TRANSIT = "3";
	public static final String C_DISTYPE_REC = "4";
	public static final String C_DISTYPE_DES = "5";
	
	/**@author kangyanfei 2014-06-30 
	 * 单据类型：发放单（0发放单，1补发发放单，2回收发放单，3销毁发放单） */
	public static final String C_ORDERNAME_3 = "销毁发放单";
	/**@author kangyanfei 2014-06-30 
	 *  单据类型：发放单（0发放单，1补发发放单，2回收单，3销毁单） */
	public static final String C_ORDERNAME_2 = "回收发放单";
	/**@author kangyanfei 2014-06-30 
	 *  单据类型：发放单（0发放单，1补发发放单，2回收单，3销毁单） */
	public static final String C_ORDERNAME_1 = "补发发放单";
	/**@author kangyanfei 2014-06-30 
	 *  单据类型：发放单（0发放单，1补发发放单，2回收单，3销毁单） */
	public static final String C_ORDERNAME_0 = "发放单";

	/** 单位类型(0为内部单位,1为外部单位)  */
	public static final String C_SENDTYPE_0="0";
	/** 单位类型(0为内部单位,1为外部单位)  */
	public static final String C_SENDTYPE_1="1";
	/** 外部组织标识*/
	public static final long C_DOMAINREF=10000;
	
	/** 发放单 */
	public static final String C_DISTRIBUTEORDER_0 = "-发放单";
	/** 补发发放单 */
	public static final String C_DISTRIBUTEORDER_1 = "-补发发放单";
	/** 回收单 */
	public static final String C_DISTRIBUTEORDER_2 = "-回收发放单";
	/** 销毁单 */
	public static final String C_DISTRIBUTEORDER_3 = "-销毁发放单";
	/** 纸质任务 */
	public static final String C_PAPERTASK_STR = "-纸质任务";
	/** 电子任务 */
	public static final String C_ELECTASK_STR = "-电子任务";
	/** 纸质签收任务 */
	public static final String C_PAPERSIGNTASK_STR = "-纸质签收任务";
	/** 逗号 */
	public static final String C_COMMA = ",";
	/** 外来数据分发基线 */
	public static final String OUT_DATA_BASELINE = "-外来数据分发基线";
	
	/** 电子 */
	public static final String C_ELECTASK = "电子";
	
	/** 纸质 */
	public static final String C_PAPERTASK = "纸质";
	
	/** 跨域 */
	public static final String C_OUTSITETASK = "跨域";
	
	/**@author kangyanfei
	 *  纸质 */
	public static final String C_DISMEDIATYPE_0 = "0";
	
	/**@author kangyanfei
	 *  电子 */
	public static final String C_DISMEDIATYPE_1 = "1";
	
	/**@author kangyanfei
	 *  跨域 */
	public static final String C_DISMEDIATYPE_2 = "2";
	
	/** 电子 */
	public static final String C_ELEC = "C_ELEC";
	
	/** 纸质 */
	public static final String C_PAPER = "C_PAPER";
	
	/** 纸质签收 */
	public static final String C_PAPERSIGN = "C_PAPERSIGN";
	
	/** 发放单 */
	public static final String C_DISORDER = "C_DISORDER";

	/** 类型 */
	public static final Select DISTRIBUTETYPES;

	/** 状态 */
	public static final Select DISTRIBUTESTATES;
	
	/** 纸质任务 */
	public static final String DISTRIBUTEPAPERTASK = "DistributePaperTask";

	/** 发放跨域协管员 */
	public static final String DIS_CROSS_DOMAIN_ROLE = "DIS_XTGLY";
	
	/**
	 * 发放是否使用中心模式
	 */
	public static final String IS_DC_DISTRIBUTEMODE_YES = "Is_Dc_DistributeModel";
	
	/**
	 * 发放是否使用实体模式
	 */
	public static final String IS_ENTITY_DISTRIBUTEMODE_YES = "Is_Entity_DistributeModel";
	
	/**
	 * 运行时配置-否使用星形部署
	 */
	public final static String IS_DEAL_ON_DC_DISTRIBUTE="Is_DealOnDc_Distribute";

	// ************************
	// Excel 相关
	// ************************
	/** EXCEL模板路径 */
	public static final String EXCEL_TEMPLATE_PATH = System.getProperty("AVIDM_HOME") + "/plm/ddm/excel/";
	/** 加工单和补发加工单EXCEL模板 */
	public static final String EXCEL_TEMPLATE_PAPER_TASK = "加工单.xls";
	/** 内发单和外发单EXCEL模板 */
	public static final String EXCEL_TEMPLATE_SENDORDER = "文件发送单.xls";
	/** 回收单和销毁单EXCEL模板 */
	public static final String EXCEL_TEMPLATE_DESTROYORDER = "文件作废销毁单.xls";
	/**技术文件复制申请（通知）单*/
	public static final String EXCEL_TEMPLATE_COPY="技术文件复制申请（通知）单.xls";
	
	/** 文件加工单*/
	public static final String PROCESS_TITLE_NAME = "文件加工单";
	
	public static final String DOCUMENT_COPY="技术文件复制申请（通知）单";
	
	/** 文件回收单*/
	public static final String REC_TITLE_NAME = "文件回收单";
	/** 文件销毁单*/
	public static final String DES_TITLE_NAME = "文件销毁单";
	
	/** 文件发送单*/
	public static final String SEND_SHEET_NAME = "文件发送单";
	
	/** 请务必在收到文件后一个月内盖章签字寄回一联*/
	public static final String SEND_ORDER_MESSAGE = "请务必在收到文件后一个月内盖章签字寄回一联";
	
	/** 回收/销毁份数*/
	public static final String DESTROY_ORDER_NUMBER = "回收/销毁份数";
	
	/** 加工申请单*/
	public static final String PAPER_TASK_PROCESS = "加工申请单";
	
	/** 补发加工申请单*/
	public static final String PAPER_TASK_REISSUE_PROCESS = "补发加工申请单";

	// ************************
	// message 相关
	// ************************
	/** 非受控 message*/
	public static final String IS_CONTROLING_NG = "生命周期状态为【state】的对象不允许加入发放单";
	/** 非受控 message*/
	public static final String IS_CONTROLING_NG_2 = "不是生命周期对象不允许加入发放单";
	/** 创建发放单错误 message*/
	public static final String CREATE_DISTRIBUTEORDER_ERROE = "发放单中存在不满足首选项配置的分发对象，不能创建发放单!";
	/** 非转换对象 message*/
	public static final String IS_TRANSEFER_NG = "非数据转换对象不允许加入发放单";

	/** 从套中移除 */
	public static final String REMOVE_FROM_SUIT = "从套中移除";
	
	public static final String CREATE_REISSUE_DISTRIBUTEORDER_ERROE = "创建补发单错误!";
	/** 回收单错误提示信息*/
	public static final String CREATE_REC_DISTRIBUTEORDER_ERROE = "创建回收单错误!";
	/** 销毁单错误提示信息*/
	public static final String CREATE_DES_DISTRIBUTEORDER_ERROE = "创建销毁单错误!";
	
	/** 默认分发信息 */
	public static final String DEFAULT_DIS_INFO = "默认分发信息";
	
	/** 标记是否已打印 1为已打印 */
	public static final String OBJECT_PRINT_SIGN_YES = "1"; 
	
	/** 标记是否已打印 0为未打印 */
	public static final String  OBJECT_PRINT_SIGN_NO = "0"; 
	
	static {

		DISTRIBUTETYPES = Helper.getRestrictManager().getSelect("distributeType");
		DISTRIBUTESTATES = Helper.getRestrictManager().getSelect("distributeState");

	}

}
