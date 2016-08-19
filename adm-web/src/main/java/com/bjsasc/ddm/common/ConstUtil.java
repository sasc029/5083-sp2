package com.bjsasc.ddm.common;

import com.bjsasc.plm.Helper;
import com.bjsasc.plm.type.restrict.select.Select;

/**
 * ���Ź����ó����ࡣ
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
	/** ��������ֽ������oid�洢����*/
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
	/** �ַ���Ϣ�깤���ޣ�DisDeadLine�� Ĭ���ӳ����� */
	public static final int C_DISDEADLINE_DELAY_DAY = 3;
	
	/**����״̬*/
	public static final String LC_LC_SCHEDULING_STATE ="scheduling";
	/**�ӹ�״̬*/
	public static final String LC_PROCESSING_STATE ="processing";
	/**���Ƽӹ�״̬*/
	public static final String LC_DUPLICATED_STATE ="duplicated";
	/**����״̬*/
	public static final String LC_SENDING_STATE ="sending";
	/**�ѷ���״̬*/
	public static final String LC_SENT_STATE ="sent";

	/**�ռ������ʶ0-���ĵ������е�������Ķ���,����A�汾������׶���*/
	public static final int COLLECTFLAG_0 = 0;
	/**�ռ������ʶ1-���ĵ�����*/
	public static final int COLLECTFLAG_1 = 1;
	/**�ռ������ʶ2-���е��ݶ���*/
	public static final int COLLECTFLAG_2 = 2;
	/**�ռ������ʶ3-A�汾������׶���*/
	public static final int COLLECTFLAG_3 = 3;

	public static final String C_S_ZERO = "0";
	public static final String C_S_ONE = "1";
	public static final String C_S_TWO = "2";

	/** �ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա�� */
	public static enum DISINFOTYPE {
		USER, ORG
	};

	/** ��ʾ���� */
	public static enum DISPLAY_TYPE {
		DISPLAY, EDIT
	};
	
	/** �������ڲ��� */
	public static enum LIFECYCLE_OPT {
		PROMOTE, DEMOTE, REJECT
	};

	/** �������ͣ�0��ֽ������1����������2������3����������ֽ������4:�������ٵ�������5:ֽ��ǩ������ */
	public static final String C_TASKTYPE_PAPER = "0";
	/** �������ͣ�0��ֽ������1����������2������3����������ֽ������4:�������ٵ�������5:ֽ��ǩ������ */
	public static final String C_TASKTYPE_ELEC = "1";
	/** �������ͣ�0��ֽ������1����������2������3����������ֽ������4:�������ٵ�������5:ֽ��ǩ������ */
	public static final String C_TASKTYPE_OUTSIGN = "2";
	/** �������ͣ�0��ֽ������1����������2������3����������ֽ������4:�������ٵ�������5:ֽ��ǩ������ */
	public static final String C_TASKYTPE_RECDESPAPER = "3";
	/** �������ͣ�0��ֽ������1����������2������3����������ֽ������4:�������ٵ�������5:ֽ��ǩ������ */
	public static final String C_TASKYTPE_PAPERSIGN = "5";
	
	/** �����������ͣ�0�����ڣ�1������2�������� */
	public static final String ELEC_TASKTYPE_IN = "0";
	/** �����������ͣ�0�����ڣ�1������2�������� */
	public static final String ELEC_TASKTYPE_OUT = "1";
	/** �����������ͣ�0�����ڣ�1������2�������� */
	public static final String ELEC_TASKTYPE_REPLY = "2";

	/** �ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��2Ϊվ�㣩 */
	public static final String C_DISINFOTYPE_USER = "1";
	/** �ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��2Ϊվ�㣩 */
	public static final String C_DISINFOTYPE_ORG = "0";
	/** �ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��2Ϊվ�㣩 */
	public static final String C_DISINFOTYPE_SITE = "2";

	/** �ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա�� */
	public static final String C_DISINFOTYPE_0 = "��Ա";
	/** �ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��2Ϊվ�㣩 */
	public static final String C_DISINFOTYPE_1 = "��λ";

	/** �������ͣ����ŵ���0���ŵ���1�������ŵ���2���շ��ŵ���3���ٷ��ŵ��� */
	public static final String C_ORDERTYPE_3 = "3";
	/** �������ͣ����ŵ���0���ŵ���1�������ŵ���2���յ���3���ٵ��� */
	public static final String C_ORDERTYPE_2 = "2";
	/** �������ͣ����ŵ���0���ŵ���1�������ŵ���2���յ���3���ٵ��� */
	public static final String C_ORDERTYPE_1 = "1";
	/** �������ͣ����ŵ���0���ŵ���1�������ŵ���2���յ���3���ٵ��� */
	public static final String C_ORDERTYPE_0 = "0";

	/** �ַ���ʽ:�ַ���Ϣ��0Ϊֱ�ӷַ���1Ϊ������2Ϊ�Ƴ���3Ϊת����4Ϊ���գ�5Ϊ���٣� */
	public static final String C_DISTYPE_DIS = "0";
	public static final String C_DISTYPE_ADDDIS = "1";
	public static final String C_DISTYPE_REMOVE = "2";
	public static final String C_DISTYPE_TRANSIT = "3";
	public static final String C_DISTYPE_REC = "4";
	public static final String C_DISTYPE_DES = "5";
	
	/**@author kangyanfei 2014-06-30 
	 * �������ͣ����ŵ���0���ŵ���1�������ŵ���2���շ��ŵ���3���ٷ��ŵ��� */
	public static final String C_ORDERNAME_3 = "���ٷ��ŵ�";
	/**@author kangyanfei 2014-06-30 
	 *  �������ͣ����ŵ���0���ŵ���1�������ŵ���2���յ���3���ٵ��� */
	public static final String C_ORDERNAME_2 = "���շ��ŵ�";
	/**@author kangyanfei 2014-06-30 
	 *  �������ͣ����ŵ���0���ŵ���1�������ŵ���2���յ���3���ٵ��� */
	public static final String C_ORDERNAME_1 = "�������ŵ�";
	/**@author kangyanfei 2014-06-30 
	 *  �������ͣ����ŵ���0���ŵ���1�������ŵ���2���յ���3���ٵ��� */
	public static final String C_ORDERNAME_0 = "���ŵ�";

	/** ��λ����(0Ϊ�ڲ���λ,1Ϊ�ⲿ��λ)  */
	public static final String C_SENDTYPE_0="0";
	/** ��λ����(0Ϊ�ڲ���λ,1Ϊ�ⲿ��λ)  */
	public static final String C_SENDTYPE_1="1";
	/** �ⲿ��֯��ʶ*/
	public static final long C_DOMAINREF=10000;
	
	/** ���ŵ� */
	public static final String C_DISTRIBUTEORDER_0 = "-���ŵ�";
	/** �������ŵ� */
	public static final String C_DISTRIBUTEORDER_1 = "-�������ŵ�";
	/** ���յ� */
	public static final String C_DISTRIBUTEORDER_2 = "-���շ��ŵ�";
	/** ���ٵ� */
	public static final String C_DISTRIBUTEORDER_3 = "-���ٷ��ŵ�";
	/** ֽ������ */
	public static final String C_PAPERTASK_STR = "-ֽ������";
	/** �������� */
	public static final String C_ELECTASK_STR = "-��������";
	/** ֽ��ǩ������ */
	public static final String C_PAPERSIGNTASK_STR = "-ֽ��ǩ������";
	/** ���� */
	public static final String C_COMMA = ",";
	/** �������ݷַ����� */
	public static final String OUT_DATA_BASELINE = "-�������ݷַ�����";
	
	/** ���� */
	public static final String C_ELECTASK = "����";
	
	/** ֽ�� */
	public static final String C_PAPERTASK = "ֽ��";
	
	/** ���� */
	public static final String C_OUTSITETASK = "����";
	
	/**@author kangyanfei
	 *  ֽ�� */
	public static final String C_DISMEDIATYPE_0 = "0";
	
	/**@author kangyanfei
	 *  ���� */
	public static final String C_DISMEDIATYPE_1 = "1";
	
	/**@author kangyanfei
	 *  ���� */
	public static final String C_DISMEDIATYPE_2 = "2";
	
	/** ���� */
	public static final String C_ELEC = "C_ELEC";
	
	/** ֽ�� */
	public static final String C_PAPER = "C_PAPER";
	
	/** ֽ��ǩ�� */
	public static final String C_PAPERSIGN = "C_PAPERSIGN";
	
	/** ���ŵ� */
	public static final String C_DISORDER = "C_DISORDER";

	/** ���� */
	public static final Select DISTRIBUTETYPES;

	/** ״̬ */
	public static final Select DISTRIBUTESTATES;
	
	/** ֽ������ */
	public static final String DISTRIBUTEPAPERTASK = "DistributePaperTask";

	/** ���ſ���Э��Ա */
	public static final String DIS_CROSS_DOMAIN_ROLE = "DIS_XTGLY";
	
	/**
	 * �����Ƿ�ʹ������ģʽ
	 */
	public static final String IS_DC_DISTRIBUTEMODE_YES = "Is_Dc_DistributeModel";
	
	/**
	 * �����Ƿ�ʹ��ʵ��ģʽ
	 */
	public static final String IS_ENTITY_DISTRIBUTEMODE_YES = "Is_Entity_DistributeModel";
	
	/**
	 * ����ʱ����-��ʹ�����β���
	 */
	public final static String IS_DEAL_ON_DC_DISTRIBUTE="Is_DealOnDc_Distribute";

	// ************************
	// Excel ���
	// ************************
	/** EXCELģ��·�� */
	public static final String EXCEL_TEMPLATE_PATH = System.getProperty("AVIDM_HOME") + "/plm/ddm/excel/";
	/** �ӹ����Ͳ����ӹ���EXCELģ�� */
	public static final String EXCEL_TEMPLATE_PAPER_TASK = "�ӹ���.xls";
	/** �ڷ������ⷢ��EXCELģ�� */
	public static final String EXCEL_TEMPLATE_SENDORDER = "�ļ����͵�.xls";
	/** ���յ������ٵ�EXCELģ�� */
	public static final String EXCEL_TEMPLATE_DESTROYORDER = "�ļ��������ٵ�.xls";
	/**�����ļ��������루֪ͨ����*/
	public static final String EXCEL_TEMPLATE_COPY="�����ļ��������루֪ͨ����.xls";
	
	/** �ļ��ӹ���*/
	public static final String PROCESS_TITLE_NAME = "�ļ��ӹ���";
	
	public static final String DOCUMENT_COPY="�����ļ��������루֪ͨ����";
	
	/** �ļ����յ�*/
	public static final String REC_TITLE_NAME = "�ļ����յ�";
	/** �ļ����ٵ�*/
	public static final String DES_TITLE_NAME = "�ļ����ٵ�";
	
	/** �ļ����͵�*/
	public static final String SEND_SHEET_NAME = "�ļ����͵�";
	
	/** ��������յ��ļ���һ�����ڸ���ǩ�ּĻ�һ��*/
	public static final String SEND_ORDER_MESSAGE = "��������յ��ļ���һ�����ڸ���ǩ�ּĻ�һ��";
	
	/** ����/���ٷ���*/
	public static final String DESTROY_ORDER_NUMBER = "����/���ٷ���";
	
	/** �ӹ����뵥*/
	public static final String PAPER_TASK_PROCESS = "�ӹ����뵥";
	
	/** �����ӹ����뵥*/
	public static final String PAPER_TASK_REISSUE_PROCESS = "�����ӹ����뵥";

	// ************************
	// message ���
	// ************************
	/** ���ܿ� message*/
	public static final String IS_CONTROLING_NG = "��������״̬Ϊ��state���Ķ���������뷢�ŵ�";
	/** ���ܿ� message*/
	public static final String IS_CONTROLING_NG_2 = "�����������ڶ���������뷢�ŵ�";
	/** �������ŵ����� message*/
	public static final String CREATE_DISTRIBUTEORDER_ERROE = "���ŵ��д��ڲ�������ѡ�����õķַ����󣬲��ܴ������ŵ�!";
	/** ��ת������ message*/
	public static final String IS_TRANSEFER_NG = "������ת������������뷢�ŵ�";

	/** �������Ƴ� */
	public static final String REMOVE_FROM_SUIT = "�������Ƴ�";
	
	public static final String CREATE_REISSUE_DISTRIBUTEORDER_ERROE = "��������������!";
	/** ���յ�������ʾ��Ϣ*/
	public static final String CREATE_REC_DISTRIBUTEORDER_ERROE = "�������յ�����!";
	/** ���ٵ�������ʾ��Ϣ*/
	public static final String CREATE_DES_DISTRIBUTEORDER_ERROE = "�������ٵ�����!";
	
	/** Ĭ�Ϸַ���Ϣ */
	public static final String DEFAULT_DIS_INFO = "Ĭ�Ϸַ���Ϣ";
	
	/** ����Ƿ��Ѵ�ӡ 1Ϊ�Ѵ�ӡ */
	public static final String OBJECT_PRINT_SIGN_YES = "1"; 
	
	/** ����Ƿ��Ѵ�ӡ 0Ϊδ��ӡ */
	public static final String  OBJECT_PRINT_SIGN_NO = "0"; 
	
	static {

		DISTRIBUTETYPES = Helper.getRestrictManager().getSelect("distributeType");
		DISTRIBUTESTATES = Helper.getRestrictManager().getSelect("distributeState");

	}

}
