package com.bjsasc.ddm.distribute.service.distributepapertask;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.Element;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.model.distributetaskinfolink.DistributeTaskInfoLink;
import com.bjsasc.ddm.distribute.model.returnreason.ReturnReason;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.platform.filecomponent.model.PtFileItemBean;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.platform.webframework.util.BeanUtil;
import com.bjsasc.platform.workflow.WorkflowWords;
import com.bjsasc.platform.workflow.engine.model.instance.ProcessInstance;
import com.bjsasc.platform.workflow.engine.model.instance.WorkItem;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.client.docserverclient.datatype.ApproveInfo;
import com.bjsasc.plm.core.attachment.AttachHelper;
import com.bjsasc.plm.core.attachment.AttachTypeEnum;
import com.bjsasc.plm.core.attachment.FileHolder;
import com.bjsasc.plm.core.part.Part;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.suit.ATSuit;
import com.bjsasc.plm.core.suit.ATSuitMemberLink;
import com.bjsasc.plm.core.suit.Suited;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.type.TypeHelper;
import com.bjsasc.plm.core.util.ConfigFileUtil;
import com.bjsasc.plm.core.util.DateTimeUtil;
import com.bjsasc.plm.core.util.ExcelUtil;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.core.util.XmlFileUtil;
import com.bjsasc.plm.core.workflow.WorkFlowUtil;
import com.bjsasc.plm.sign.SignUtil;
import com.bjsasc.plm.sign.SignUtil;

/**
 * ֽ������ӿ�ʵ���ࡣ
 * 
 * @author guowei 2013-2-22
 * 
 */
@SuppressWarnings({ "deprecation", "unchecked", "rawtypes", "static-access" })
public class DistributePaperTaskServiceImpl implements DistributePaperTaskService {
	/** �������ݹ���ģ��������Ϣ��Ŀ¼ */
	private static final String DDM_HOME_PATH = ConfigFileUtil.PLM_HOME_PATH + File.separator + "ddm";

	/** �������ݹ���ģ��������������·��*/
	private static final String LIFECYCLE_CONFIG_FILE_PAHT = DDM_HOME_PATH + File.separator + "custom"
		+ File.separator + "ExportCopyExcel.xml";
	/** BeforeWorkFlowName����ǰ����������*/
	private static final String DDM_BEFORE_WORKFLOW = "BeforeWorkFlowName";
	/** BeforeActivityName����ǰ�������ڵ�����*/
	private static final String DDM_BEFORE_ACTIVITY = "BeforeActivityName";
	/** AfterWorkFlowName�ڵ�����*/
	private static final String DDM_AFTER_WORKFLOW = "AfterWorkFlowName";
	/** AfterActivityName���Ⱥ������ڵ�����*/
	private static final String DDM_AFTER_ACTIVITY = "AfterActivityName";
	/*
	 * ���� Javadoc��
	 * @see com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService#getAllDistributePaperTask()
	 */
	@Override
	public List<DistributePaperTask> getAllDistributePaperTaskByAuth(String stateName) {
		String hql = "from DistributePaperTask t where t.lifeCycleInfo.stateName = ? order by t.disUrgent desc, t.manageInfo.modifyTime desc ";
		List<DistributePaperTask> list = Helper.getPersistService().find(hql, stateName);
		return list;
	}
	
	
	@Override
	public List<DistributePaperTask> getSearchProcessingDistributePaperTaskByAuth(String stateName,String keyWords) {
		 String sql=" select * from  DDM_DIS_PAPERTASK t where (t.id like ? or t.name like ?) and  t.statename!=?";
		List<DistributePaperTask> paperList = PersistHelper.getService().query(sql, DistributePaperTask.class,
				keyWords, keyWords,stateName);
		return paperList;
	}
	@Override
	public List<DistributePaperTask> getSearchProcessingDistributePaperTaskByTime(long timeOfOneMonthBefore,String stateName,long currentTime) {
		String sql=" select * from  DDM_DIS_PAPERTASK y where y.statename!=? "
				+ "and y.createtime >=? and y.createtime<=?";
		List<DistributePaperTask> paperList = PersistHelper.getService().query(sql, DistributePaperTask.class,stateName,timeOfOneMonthBefore,currentTime);
		return paperList;
	}

	@Override
	public List<DistributePaperTask> getAllDistributePaperTaskReturnByAuth(String stateName) {
		String hql = "from DistributePaperTask t where t.lifeCycleInfo.stateName = ? order by t.disUrgent desc,  t.manageInfo.modifyTime desc ";
		List<DistributePaperTask> list = Helper.getPersistService().find(hql, stateName);
		List<DistributePaperTask> taskList = new ArrayList<DistributePaperTask>();
		if (list.size() > 0) {
			for (DistributePaperTask disPaperTask : list) {
				String paperTaskInnerId = disPaperTask.getInnerId();
				String paperTaskClassId = disPaperTask.getClassId();
				String sqlReturn = "select * from DDM_DIS_RETURN where objectId is null and objectClassId is null and stateId = ? and taskId = ? and taskClassId = ? order by updatetime desc";
				List<ReturnReason> retList = Helper.getPersistService().query(sqlReturn, ReturnReason.class,
						disPaperTask.getLifeCycleInfo().getStateId(), paperTaskInnerId, paperTaskClassId);
				disPaperTask.setReturnReason(retList.get(0));
				taskList.add(disPaperTask);
			}
		}
		return taskList;
	}

	/*
	 * ���� Javadoc��
	 * @see com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService#getDistributePaperTaskProperty(java.lang.String)
	 */
	public DistributePaperTask getDistributePaperTaskProperty(String oid) {
		Persistable obj = Helper.getPersistService().getObject(oid);
		DistributePaperTask dis = (DistributePaperTask) obj;
		String disPaperTaskInnerId = Helper.getInnerId(oid);
		String disPaperTaskClassId = Helper.getClassId(oid);
		String sqlOrder = "select ORD.* from DDM_DIS_TASKINFOLINK t,DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink,DDM_DIS_ORDER ORD"
				+ " where t.fromObjectId =? and t.fromObjectClassId = ? "
				+ " and disInfo.innerId = t.toObjectId and disInfo.classId = t.toObjectClassId"
				+ " and objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
				+ " and ORD.innerId = objLink.fromObjectId and ORD.classId = objLink.fromObjectClassId";

		List<DistributeOrder> orderList = PersistHelper.getService().query(sqlOrder, DistributeOrder.class,
				disPaperTaskInnerId, disPaperTaskClassId);
		if (orderList.size() > 0) {
			DistributeOrder order = orderList.get(0);
			dis.setOrderName(order.getName());
			dis.setOrderOid(order.getClassId() + ":" + order.getInnerId());
		}
		return dis;
	}

	/*
	 * ���� Javadoc��
	 * @see com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService#createDistributePaperTask(com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask)
	 */
	@Override
	public void createDistributePaperTask(DistributePaperTask dis) {
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		life.initLifecycle(dis);
		Helper.getPersistService().save(dis);
	}

	/*
	 * ���� Javadoc��
	 * @see com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService#createDistributePaperTask(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void createDistributePaperTask(String number, String name, String note) {
		DistributePaperTask dis = newDistributePaperTask();
		dis.setNumber(number);
		dis.setName(name);
		dis.setNote(note);

		createDistributePaperTask(dis);
	}

	/*
	 * ���� Javadoc��
	 * @see com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService#newDistributePaperTask()
	 */
	public DistributePaperTask newDistributePaperTask() {
		//DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();

		DistributePaperTask dis = (DistributePaperTask) PersistUtil.createObject(DistributePaperTask.CLASSID);

		// �������ڳ�ʼ��
		//life.initLifecycle(dis);
		return dis;
	}

	public List<DistributePaperTask> getDistributeTaskReturnDetail(String returnOid, String stateName) {
		String sqlTask = "SELECT * FROM DDM_DIS_PAPERTASK WHERE CLASSID || ':' || INNERID = ("
				+ " SELECT MAX (TASKCLASSID || ':' || TASKID) FROM DDM_DIS_RETURN "
				+ "WHERE CLASSID || ':' || INNERID = ? )";
		String sqlReturn = "SELECT * FROM DDM_DIS_RETURN WHERE STATENAME = ? AND TASKCLASSID || ':' || TASKID = ( "
				+ " SELECT MAX (TASKCLASSID || ':' || TASKID) FROM DDM_DIS_RETURN "
				+ " WHERE CLASSID || ':' || INNERID = ? ) AND OBJECTID IS NULL AND OBJECTCLASSID IS NULL "
				+ "order by UPDATETIME DESC";
		List<DistributePaperTask> taskList = Helper.getPersistService().query(sqlTask, DistributePaperTask.class,
				returnOid);
		DistributePaperTask distributePaperTask = taskList.get(0);
		List<ReturnReason> retList = Helper.getPersistService().query(sqlReturn, ReturnReason.class, stateName,
				returnOid);
		List<DistributePaperTask> paperTaskList = new ArrayList<DistributePaperTask>();
		for (ReturnReason ret : retList) {
			DistributePaperTask task = distributePaperTask.cloneDisTask();
			task.setReturnReason(ret);
			paperTaskList.add(task);
		}
		return paperTaskList;
	}

	/*
	 * ���� Javadoc��
	 * @see com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService#deleteDistributePaperTaskProperty(java.lang.String)
	 */
	public void deleteDistributePaperTaskProperty(String oid) {
		String disPropertyTaskInnerId = Helper.getPersistService().getInnerId(oid);
		String disPropertyTaskClassId = Helper.getPersistService().getClassId(oid);

		String hql = "from DistributeTaskInfoLink t "
				+ "where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? ";

		List<DistributeTaskInfoLink> links = PersistHelper.getService().find(hql, disPropertyTaskInnerId,
				disPropertyTaskClassId);
		if (links != null && links.size() > 0) {
			Helper.getPersistService().delete(links);
		}
		Persistable paperTask = Helper.getPersistService().getObject(oid);
		Helper.getPersistService().delete(paperTask);
	}

	/*
	 * ���� Javadoc��
	 * @see com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService#listDistributeObjectFiles(java.lang.String)
	 */
	public List<Map<String, Object>> listDistributeObjectFiles(String oid, String contextPath) {
		List mapList = new ArrayList();
		String sql = "select dataInnerId as DATAINNERID,dataClassId as DATACLASSID from DDM_DIS_OBJECT where classId || ':' || innerId =?";
		List<Map<String, Object>> listMap = Helper.getPersistService().query(sql, oid);
		Persistable pt = null;
		if (listMap.size() > 0) {
			String classId = listMap.get(0).get("DATACLASSID").toString();
			String innerId = listMap.get(0).get("DATAINNERID").toString();
			pt = PersistHelper.getService().getByClassId(classId, innerId);
		}
		List<PtFileItemBean> list = null;
		if (pt instanceof FileHolder) {
			list = AttachHelper.getAttachService().getAllFile((FileHolder) pt);
		}
		for (PtFileItemBean bean : list) {
			String mainFile = "";
			boolean isMainFile = bean.getFileObjType().equals(AttachTypeEnum.MAIN.getValue());
			if (isMainFile) {
				mainFile = "��";
			}
			Map map = BeanUtil.beanToMap(bean);
			map.put("MAINFILE", mainFile);
			String fileSizeString = "";
			if (map.get("fileSize") != "0") {
				DecimalFormat df = new DecimalFormat("#.00");
				long fileSize = Long.valueOf((String) map.get("fileSize"));
				if (fileSize < 1024) {
					fileSizeString = df.format((double) fileSize) + "<strong>B</strong>";
				} else if (fileSize < 1048576) {
					fileSizeString = df.format((double) fileSize / 1024) + "<strong>K</strong>";
				} else if (fileSize < 1073741824) {
					fileSizeString = df.format((double) fileSize / 1048576) + "<strong>M</strong>";
				} else {
					fileSizeString = df.format((double) fileSize / 1073741824) + "<strong>G</strong>";
				}
			}
			map.put("FILENAME", map.get("fileName"));
			map.put("FILEEXTENDNAME", "." + (String) map.get("fileExtendName"));
			map.put("FILESIZE", fileSizeString);
			map.put("OPERATE", "<img src='../images/p_xls.gif' alt='���ļ�'/>���ļ�");
			mapList.add(map);
		}
		return mapList;
	}

	/* ���� Javadoc��
	 * @see com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService#getExcelObject(java.lang.String)
	 */
	@Override
	public HSSFWorkbook getExcelObject(String oid) {

		Map map = creatData(oid);

		// Sheetҳ����/��������
		String sheetName = getSheetName(map);

		// ģ��·��
		String templateFullName = ConstUtil.EXCEL_TEMPLATE_PATH + ConstUtil.EXCEL_TEMPLATE_PAPER_TASK;
		// ģ�屨��ͷ�п�ʼλ��
		int iHeadRowStart = 1;
		// ģ�屨��ͷ�н���λ��
		int iHeadRowEnd = 5;
		// ģ����ϸ�п�ʼλ��/Copy��ϸ�п�ʼλ��
		int iDetailRowStart = 6;
		// ģ�屨��β�п�ʼλ��
		int iFootRowStart = 9;
		// ģ�屨��β�н���λ��
		int iFootRowEnd = 10;
		// Copy����ͷ�п�ʼλ��
		int iHeadCopyRowStart = 1;
		// ��ϸ�н���λ��
		int iDetailRowEnd = 0;
		// Copy����β�п�ʼλ�� = ��ϸ�н���λ�� +1
		int iFootCopyRowStart;

		try {
			ExcelUtil ew = new ExcelUtil();
			ew.init(sheetName, templateFullName);

			// ����ͷ����
			ew.copyRows(iHeadRowStart - 1, iHeadRowEnd - 1, iHeadCopyRowStart - 1);
			// ����ͷֵ�趨s
			setHeadData(ew, sheetName, map);
			// ����ͷ��Ԫ��ϲ�
			headerMerge(ew);

			// ��ϸ�п�����ֵ�趨����Ԫ��ϲ�
			iDetailRowEnd = copySetMergeDetail(ew, iDetailRowStart, map);
			iFootCopyRowStart = iDetailRowEnd;

			// ����β�п���
			ew.copyRows(iFootRowStart - 1, iFootRowEnd - 1, iFootCopyRowStart - 1);
			// ����βֵ�趨
			setFootData(ew, iFootCopyRowStart);
			// ����β��Ԫ��ϲ�
			footMerge(ew, iFootCopyRowStart);

			// �����б���
			setColumnWidth(ew);
			// ���ô�ӡ��ʽ
			ew.setPrintStyle(false);

			ew.close();
			ew.deleteTemplateSheet();

			return ew.getCurrentWorkbook();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/* ���� Javadoc��
	 * @see com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService#getExcelObject(java.lang.String)
	 */
	@Override
	public HSSFWorkbook getExcelObjectForDept7(String oid) {

		Map map = creatData(oid);

		// Sheetҳ����/��������
		String sheetName = getSheetNameForDept7(map);

		// ģ��·��
		String templateFullName = ConstUtil.EXCEL_TEMPLATE_PATH + ConstUtil.EXCEL_TEMPLATE_COPY;
		// ģ�屨��ͷ�п�ʼλ��
		int iHeadRowStart = 1;
		// ģ�屨��ͷ�н���λ��
		int iHeadRowEnd = 6;
		// ģ����ϸ�п�ʼλ��/Copy��ϸ�п�ʼλ��
		int iDetailRowStart = 7;
		// ģ�屨��β�п�ʼλ��
		int iFootRowStart = 16;
		// ģ�屨��β�н���λ��
		int iFootRowEnd = 18;
		// Copy����ͷ�п�ʼλ��
		int iHeadCopyRowStart = 1;
		// ��ϸ�н���λ��
		int iDetailRowEnd = 0;
		// Copy����β�п�ʼλ�� = ��ϸ�н���λ�� +1
		int iFootCopyRowStart;

		try {
			ExcelUtil ew = new ExcelUtil();
			ew.init(sheetName, templateFullName);

			// ����ͷ����
			ew.copyRows(iHeadRowStart - 1, iHeadRowEnd - 1, iHeadCopyRowStart - 1);
			// ����ͷֵ�趨s
			setHeadDataForDept7(ew, sheetName, map);
			// ����ͷ��Ԫ��ϲ�
			headerMergeForDept7(ew);

			// ��ϸ�п�����ֵ�趨����Ԫ��ϲ�
			iDetailRowEnd = copySetMergeDetailForDept7(ew, iDetailRowStart, map);
			iFootCopyRowStart = iDetailRowEnd;

			// ����β�п���
			ew.copyRows(iFootRowStart - 1, iFootRowEnd - 1, iFootCopyRowStart - 1);
			// ����βֵ�趨
			setFootDataForDept7(ew, iFootCopyRowStart);
			// ����β��Ԫ��ϲ�
			footMergeForDept7(ew, iFootCopyRowStart);

			// �����б���
			setColumnWidthForDept7(ew);
			//�����и�
			setRowHeight(ew);
			// ���ô�ӡ��ʽ
			ew.setPrintStyle(false);

			ew.close();
			ew.deleteTemplateSheet();

			return ew.getCurrentWorkbook();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ȡ��Sheet����
	 * 
	 * @param map Map 
	 * @return String
	 */
	private String getSheetNameForDept7(Map map) {				
		return ConstUtil.DOCUMENT_COPY;				
	}

	/**
	 * ȡ��Sheet����
	 * 
	 * @param map Map 
	 * @return String
	 */
	private String getSheetName(Map map) {
		DistributeOrder disOrder = (DistributeOrder) map.get("disOrder");
		if ("0".equals(disOrder.getOrderType())) {
			// �ӹ����뵥Sheetҳ����/��������
			return ConstUtil.PAPER_TASK_PROCESS;
		} else {
			// �����ӹ����뵥Sheetҳ����/��������
			return ConstUtil.PAPER_TASK_REISSUE_PROCESS;
		}
	}

	/**
	 * ����������Ϣ��
	 * 
	 * @param oid String
	 * @return Map
	 * 			key:disOrder value:DistributeOrder
	 * 			key:disPaperTask value:DistributePaperTask
	 * 	 		key:detaildisObjList value:List<DistributeObject>
	 * 	 		key:detaildisInfoList value:List<List<DistributeInfo>>
	 */
	private Map creatData(String oid) {
		String classIdTemp;
		String innerIdTemp;
		String sql;

		Persistable paperTaskObj = Helper.getPersistService().getObject(oid);
		DistributePaperTask disPaperTask = (DistributePaperTask) paperTaskObj;
		innerIdTemp = Helper.getPersistService().getInnerId(oid);
		classIdTemp = Helper.getPersistService().getClassId(oid);

		// ���ŵ���ַ�����link:DDM_DIS_ORDEROBJLINK
		sql = "select DISTINCT objLink.* " + " from DDM_DIS_TASKINFOLINK taskInfoLink, "
				+ " DDM_DIS_INFO disInfo, DDM_DIS_ORDEROBJLINK objLink " + " where taskInfoLink.FROMOBJECTID = ? "
				+ " and taskInfoLink.FROMOBJECTCLASSID = ? " + " and taskInfoLink.TOOBJECTID =disInfo.INNERID "
				+ " and taskInfoLink.TOOBJECTCLASSID = disInfo.CLASSID "
				+ " and objLink.INNERID = disInfo.DISORDEROBJLINKID "
				+ " and objLink.CLASSID = disInfo.DISORDEROBJLINKCLASSID order by objLink.ISMASTER desc";

		List<DistributeOrderObjectLink> list = Helper.getPersistService().query(sql, DistributeOrderObjectLink.class,
				innerIdTemp, classIdTemp);

		List<DistributeObject> detaildisObjList = new ArrayList<DistributeObject>();
		List<List<DistributeInfo>> detaildisInfoList = new ArrayList<List<DistributeInfo>>();

		List<DistributeObject> objList = new ArrayList<DistributeObject>();
		Map<String, Object> infoObjMap = new HashMap<String, Object>();
		Map<String, Map> dataMap = new java.util.LinkedHashMap<String, Map>();

		Map map = new HashMap();

		ATSuit suit = null;
		String suitInfoKey = "";
		for (int i = 0; i < list.size(); i++) {
			DistributeOrderObjectLink disOrdObjLink = list.get(i);
			if (i == 0) {
				// ���ŵ�����:DDM_DIS_ORDER
				map.put("disOrder", (DistributeOrder) disOrdObjLink.getFrom());

				// ��ȡ�ַ���Ϣ�깤����
				long disDeadLine = disOrdObjLink.getDisDeadLine();
				map.put("disDeadLine", disDeadLine);
			}
			DistributeObject disobj = (DistributeObject) disOrdObjLink.getTo();
			
		    Persistable dataObj = Helper.getPersistService().getObject(disobj.getDataClassId() + ":" + disobj.getDataInnerId());
		    //�������������ӹ���excel���
			if (!(dataObj instanceof Part)) {
				// �ַ����ݶ���:DDM_DIS_OBJECT
				detaildisObjList.add((DistributeObject) disOrdObjLink.getTo());

				// �ַ���ϢDDM_DIS_INFO
				sql = "select DISTINCT disInfo.* from DDM_DIS_INFO disInfo " + " where disInfo.DISORDEROBJLINKID = ?"
						+ "and disInfo.DISORDEROBJLINKCLASSID = ? "
						+ "and disInfo.DISMEDIATYPE = '0' order by disInfo.MODIFYTIME desc";

				List<DistributeInfo> disInfoList = Helper.getPersistService().query(sql, DistributeInfo.class,
						disOrdObjLink.getInnerId(), disOrdObjLink.getClassId());

				String infoMapKey = "";
				for (DistributeInfo tmpInfo : disInfoList) {
					String disInfoName = tmpInfo.getDisInfoName();
					long disInfoNum = tmpInfo.getDisInfoNum();
					String note = tmpInfo.getNote();
					infoMapKey += disInfoName + "," + disInfoNum + "," + note + ";";
				}

				if (i == 0) {
					// �ַ����ݶ���:DDM_DIS_OBJECT
					objList.add((DistributeObject) disOrdObjLink.getTo());
					infoObjMap.put("objList", objList);
					infoObjMap.put("infoList", disInfoList);
					dataMap.put(infoMapKey, infoObjMap);
					//�׶�������⴦��
					//���׳��ηַ������ײ���ʱ���ӹ��������͵��н�չʾ�׶�����Ŀ��Ϣ�����ж�����ĳ���ڶ�����������ַ���Ϣʱ��Ҳ��Ҫ�������ַ������ڶ�����Ϊ��Ŀ��Ϣչʾ
					if ("ATSuit".equals(disobj.getDataClassId())) {
						suit = (ATSuit)Helper.getPersistService().getObject(disobj.getDataClassId() + ":" + disobj.getDataInnerId());
						DistributeOrder disOrder = (DistributeOrder) map.get("disOrder");
						// �ӹ����뵥,�ǳ��ηַ�
						if ("0".equals(disOrder.getOrderType()) && !"A".equals(suit.getIterationInfo().getVersionNo())) {
							suit = null;
						} else {
							//���׳��ηַ������ײ���ʱ
							suitInfoKey = infoMapKey;
						}
					}
				} else {
					if (dataMap.containsKey(infoMapKey)) {

						infoObjMap = dataMap.get(infoMapKey);
						//�׶�������⴦��
						//���׳��ηַ������ײ���ʱ���ӹ��������͵��н�չʾ�׶�����Ŀ��Ϣ�����ж�����ĳ���ڶ�����������ַ���Ϣʱ��Ҳ��Ҫ�������ַ������ڶ�����Ϊ��Ŀ��Ϣչʾ
						if (suit != null && infoMapKey.equals(suitInfoKey)) {
							Persistable obj = Helper.getPersistService().getObject(disobj.getDataClassId() + ":" + disobj.getDataInnerId());
							if (obj instanceof Suited) {
								ATSuitMemberLink link = ATSuitMemberLink.getlink(suit, (Suited)obj);
								if (link == null) {
									objList = (List<DistributeObject>) infoObjMap.get("objList");

									// �ַ����ݶ���:DDM_DIS_OBJECT
									objList.add((DistributeObject) disOrdObjLink.getTo());
								}
							} else {
								objList = (List<DistributeObject>) infoObjMap.get("objList");

								// �ַ����ݶ���:DDM_DIS_OBJECT
								objList.add((DistributeObject) disOrdObjLink.getTo());
							}
							
						} else {
							objList = (List<DistributeObject>) infoObjMap.get("objList");

							// �ַ����ݶ���:DDM_DIS_OBJECT
							objList.add((DistributeObject) disOrdObjLink.getTo());
						}
					} else {
						infoObjMap = new HashMap();
						dataMap.put(infoMapKey, infoObjMap);

						objList = new ArrayList();
						// �ַ����ݶ���:DDM_DIS_OBJECT
						objList.add((DistributeObject) disOrdObjLink.getTo());
						infoObjMap.put("objList", objList);
						infoObjMap.put("infoList", disInfoList);
					}
				}
			}
		}

		map.put("dataMap", dataMap);
		map.put("disPaperTask", disPaperTask);
		map.put("detaildisObjList", detaildisObjList);
		map.put("detaildisInfoList", detaildisInfoList);

		return map;
	}

	/**
	 * ����ͷֵ�趨��
	 * 
	 * @param ew ExcelUtil
	 * @param titleName String
	 * @param map Map
	 * @throws Exception
	 */
	private void setHeadData(ExcelUtil ew, String titleName, Map map) throws Exception {
		DistributeOrder disOrder = (DistributeOrder) map.get("disOrder");
		DistributePaperTask disPaperTask = (DistributePaperTask) map.get("disPaperTask");
		long createTime = disOrder.getManageInfo().getCreateTime();

		// ��һ��
		// [��������]valueֵ(��:1,��:1)
		ew.setCell(1, 1, titleName);

		// ������
		// [���뵥λ]valueֵ(��:4,��:2)
		User applyUser=disOrder.getManageInfo().getCreateBy();
		String applyOrganizationName=applyUser.getOrganizationName();
		ew.setCell(4, 2, applyOrganizationName);
		// [������]valueֵ(��:4,��:5)
		ew.setCell(4, 5, disOrder.getManageInfo().getCreateBy().getName());
		// [��������]valueֵ(��:4,��:7)
		ew.setCell(4, 7, DateTimeUtil.getDateDisplay(createTime));
		// [Ҫ���������]valueֵ(��:4,��:9)
		long disDeadLine = (Long) map.get("disDeadLine");
		ew.setCell(4, 9, DateTimeUtil.getDateDisplay(disDeadLine));
		// [�����]valueֵ(��:4,��:11)
		ew.setCell(4, 11, "");
		// [������]valueֵ(��:4,��:13)
	
		String createName = disPaperTask.getCreateByName();
		ew.setCell(4, 13, createName);
		
		// �������뵥λ��Ԫ���Զ�����
		if (createName != null && createName.getBytes().length > 8) {
			ew.setCellHeightStyle(4, 13, createName, 4);
		} else if (applyOrganizationName != null && applyOrganizationName.getBytes().length > 20) {
			ew.setCellHeightStyle(4, 2, applyOrganizationName, 10);
		}
	}
	
	/**
	 * ����ͷֵ�趨��
	 * 
	 * @param ew ExcelUtil
	 * @param titleName String
	 * @param map Map
	 * @throws Exception
	 */
	private void setHeadDataForDept7(ExcelUtil ew, String titleName, Map map) throws Exception{
		DistributeOrder disOrder = (DistributeOrder) map.get("disOrder");
		DistributePaperTask disPaperTask = (DistributePaperTask) map.get("disPaperTask");
		long createTime = disOrder.getManageInfo().getCreateTime();
		// ��һ��
		// [��������]valueֵ
		ew.setCell(1, 1, titleName);
		//�ڶ���[���ŵ����]valueֵ
		ew.setCell(2,9,disOrder.getNumber());
		//������[�ͺŴ��ţ�ֽ������ģ�������ID]
		ew.setCell(3,2,disPaperTask.getContextInfo().getContext().getNumber());
		// [���뵥λ]valueֵ
		User applyUser=disOrder.getManageInfo().getCreateBy();
		String applyOrganizationName=applyUser.getOrganizationName();
		ew.setCell(5, 1, applyOrganizationName);
		// [������]valueֵ
		ew.setCell(5, 2, disOrder.getManageInfo().getCreateBy().getName());
		//��������
		ew.setCell(5, 9, DateTimeUtil.getDateDisplay(createTime));
		//Ҫ���������
		long disDeadLine = (Long) map.get("disDeadLine");
		ew.setCell(5, 10, DateTimeUtil.getDateDisplay(disDeadLine));
		// [�з������ͺŹ�����������]valueֵ(��:5,��:6)
		String createName = disPaperTask.getCreateByName();
		ew.setCell(5, 6, createName);
	    //[���벿�Ÿ�����]:����ǰ����ǩ����Ϣ
		String  beforeSigner = getSigner(disOrder.getOid(),"before");
		ew.setCell(5,4,beforeSigner);
		//[���칤�̲����ƻ���Ա]:���Ⱥ�����ǩ����Ϣ
		String  afterSigner = getSigner(disOrder.getOid(),"after");
		ew.setCell(5,8,afterSigner);
	}
	
	/**
	 * ����ͷ��Ԫ��ϲ���
	 * 
	 * @param ew ExcelUtil
	 */
	private void headerMergeForDept7(ExcelUtil ew) {
		//����(�У�1���У�1~11)
		ew.mergeRows(1, 1, 11);
		//���ŵ����(�У�2���У�9~11)
		ew.mergeRows(2, 9, 11);
		//�ͺŴ���(�У�3���У�2~6)
		ew.mergeRows(3, 2, 6);
		//���벿�Ÿ�����(�У�4���У�4~5)
		ew.mergeRows(4,4,5);
		//�з������ͺŹ���(�У�4���У�6~7);
		ew.mergeRows(4,6,7);
		//Ҫ�����ʱ��(�У�4���У�10~11)
		ew.mergeRows(4,10,11);
		//Ҫ�����ʱ��value(�У�5���У�10~11)
		ew.mergeRows(5,10,11);
		//���벿�Ÿ�����value�ϲ�(�У�5���У�4~5)
		ew.mergeRows(5,4,5);
		//�з������ͺŹ���value�ϲ�(�У�4���У�6~7);
		ew.mergeRows(5,6,7);
	}

	/**
	 * ����ͷ��Ԫ��ϲ���
	 * 
	 * @param ew ExcelUtil
	 */
	private void headerMerge(ExcelUtil ew) {
		// [�����ӹ����뵥]��Ԫ��ϲ�(�У�1~2���У�1~14)
		ew.mergeCells(1, 2, 1, 14);

		// [���뵥λ]��Ԫ��ϲ�(�У�3���У�2~4)
		ew.mergeRows(3, 2, 4);
		// [������]��Ԫ��ϲ�(�У�3���У�5~6)
		ew.mergeRows(3, 5, 6);
		// [��������]��Ԫ��ϲ�(�У�3���У�7~8)
		ew.mergeRows(3, 7, 8);
		// [Ҫ���������]��Ԫ��ϲ�(�У�3���У�9~10)
		ew.mergeRows(3, 9, 10);
		// [�����]��Ԫ��ϲ�(�У�3���У�11~12)
		ew.mergeRows(3, 11, 12);
		// [������]��Ԫ��ϲ�(�У�3���У�13~14)
		ew.mergeRows(3, 13, 14);
		// ��Ӧvalueֵ��Ԫ��ϲ�
		ew.mergeRows(4, 2, 4);
		ew.mergeRows(4, 5, 6);
		ew.mergeRows(4, 7, 8);
		ew.mergeRows(4, 9, 10);
		ew.mergeRows(4, 11, 12);
		ew.mergeRows(4, 13, 14);

		// [���]��Ԫ��ϲ�(�У�5���У�3~4)
		ew.mergeRows(5, 3, 4);
		// [����]��Ԫ��ϲ�(�У�5���У�5~6)
		ew.mergeRows(5, 5, 6);
		// [�ͺ�]��Ԫ��ϲ�(�У�5���У�7~8)
		ew.mergeRows(5, 7, 8);
	}
	
	/**
	 * ������ϸ��Copy��ֵ�趨�͵�Ԫ��ϲ���
	 * 
	 * @param ew ExcelUtil
	 * @param iDetailRowStart int 
	 * @param map Map
	 * @return int 
	 * @throws Exception
	 */
	private int copySetMergeDetailForDept7(ExcelUtil ew, int iDetailRowStart, Map map) throws Exception{


		Map<String, Map> dataMap = (Map<String, Map>) map.get("dataMap");

		long sNum;
		// Copy��ϸ��λ��
		int iDetailRowTemp = iDetailRowStart;
		int disInfolistSize;
		DistributeInfo disInfo;
		DistributeObject disObj;

		int index = 0;
		for (Map map0 : dataMap.values()) {
			List<DistributeObject> detaildisObjList = (List<DistributeObject>) map0.get("objList");
			List<DistributeInfo> disInfolist = (List<DistributeInfo>) map0.get("infoList");
			int iSize = detaildisObjList.size();
			disInfolistSize = disInfolist.size();

			ew.copyRows(iDetailRowStart - 1, iDetailRowStart - 1, iDetailRowTemp - 1);
			
			for (int i = 0; i < iSize; i++) {
				for (int j = 0; j < disInfolistSize; j++) {
					disObj = detaildisObjList.get(i);
					disInfolistSize = disInfolist.size();
					disInfo = disInfolist.get(j);
					sNum = disInfo.getDisInfoNum();
					// ��iDetailRowTemp��
					// [���]valueֵ�趨(��:iDetailRowTemp,��:1)
					ew.setCell(iDetailRowTemp, 1, ++index);
					// [���]valueֵ�趨(��:iDetailRowTemp,��:2)
					ew.setCell(iDetailRowTemp, 2, disObj.getNumber());
					// [����]valueֵ�趨(��:iDetailRowTemp,��:3)
					ew.setCell(iDetailRowTemp, 3, disObj.getName());
					// [�ܼ�]valueֵ�趨(��:iDetailRowTemp,��:5)
					ew.setCell(iDetailRowTemp, 5, disObj.getSecurityLevel());
					// [�׶�]valueֵ�趨(��:iDetailRowTemp,��:4)
					ew.setCell(iDetailRowTemp, 4, disObj.getPhase());
					// [ҳ��]valueֵ�趨(��:iDetailRowTemp,��:6)
					ew.setCell(iDetailRowTemp, 6, disObj.getPages());
					// [����]valueֵ�趨(��:iDetailRowTemp,��:7)
					ew.setCell(iDetailRowTemp, 7, String.valueOf(sNum));
					// [������λvalue]��Ԫ��ϲ�(�У�iDetailRowTemp���У�8~9)
					ew.mergeRows(iDetailRowTemp, 8, 9);
					// [������λvalue]��Ԫ��ϲ�(�У�iDetailRowTemp���У�8~9)
					ew.mergeRows(iDetailRowTemp-1, 8, 9);
					//[ǩ��]��Ԫ��ϲ�
					ew.mergeRows(iDetailRowTemp-1,10,11);
					//[ǩ��]��Ԫ��value�ϲ�
					ew.mergeRows(iDetailRowTemp,10,11);
					// [������λ]valueֵ�趨(��:iDetailRowTemp+2+j,��:3)
					ew.setCell(iDetailRowTemp, 8, disInfo.getDisInfoName());
					// �и�����
					ew.setRowHeight(iDetailRowTemp, 612);

					ew.copyRows(iDetailRowStart, iDetailRowStart, iDetailRowTemp);
					
					iDetailRowTemp++;
				}
			}
		}
		// Copy��ϸ����λ��
		return iDetailRowTemp--;		
	}

	/**
	 * ������ϸ��Copy��ֵ�趨�͵�Ԫ��ϲ���
	 * 
	 * @param ew ExcelUtil
	 * @param iDetailRowStart int 
	 * @param map Map
	 * @return int 
	 * @throws Exception
	 */
	private int copySetMergeDetail(ExcelUtil ew, int iDetailRowStart, Map map) throws Exception {

		Map<String, Map> dataMap = (Map<String, Map>) map.get("dataMap");

		int iCount;
		long sNum;
		// Copy��ϸ��λ��
		int iDetailRowTemp = iDetailRowStart;
		int disInfolistSize;
		DistributeInfo disInfo;
		DistributeObject disObj;

		int index = 0;
		for (Map map0 : dataMap.values()) {
			List<DistributeObject> detaildisObjList = (List<DistributeObject>) map0.get("objList");
			List<DistributeInfo> disInfolist = (List<DistributeInfo>) map0.get("infoList");
			int iSize = detaildisObjList.size();
			disInfolistSize = disInfolist.size();

			// ��ȡ�ַ��ܷ���
			iCount = 0;
			for (int s = 0; s < disInfolistSize; s++) {
				disInfo = disInfolist.get(s);
				sNum = disInfo.getDisInfoNum();
				iCount += sNum;
			}

			for (int i = 0; i < iSize; i++) {
				disObj = detaildisObjList.get(i);
				disInfolistSize = disInfolist.size();

				ew.copyRows(iDetailRowStart - 1, iDetailRowStart - 1, iDetailRowTemp - 1);

				// ��iDetailRowTemp��
				// [���]valueֵ�趨(��:iDetailRowTemp,��:2)
				ew.setCell(iDetailRowTemp, 2, ++index);

				// [���]valueֵ�趨(��:iDetailRowTemp,��:3)
				ew.setCell(iDetailRowTemp, 3, disObj.getNumber());
				// [����]valueֵ�趨(��:iDetailRowTemp,��:5)
				ew.setCell(iDetailRowTemp, 5, disObj.getName());
				// [�ͺ�]valueֵ�趨(��:iDetailRowTemp,��:7)
				ew.setCell(iDetailRowTemp, 7, disObj.getCode());
				// [�ܼ�]valueֵ�趨(��:iDetailRowTemp,��:9)
				ew.setCell(iDetailRowTemp, 9, disObj.getSecurityLevel());
				// [�׶�]valueֵ�趨(��:iDetailRowTemp,��:10)
				ew.setCell(iDetailRowTemp, 10, disObj.getPhase());
				// [�׶�]valueֵ�趨(��:iDetailRowTemp,��:11)
				ew.setCell(iDetailRowTemp, 11, disObj.getVersion());
				// [ҳ��]valueֵ�趨(��:iDetailRowTemp,��:12)
				ew.setCell(iDetailRowTemp, 12, disObj.getPages());
				// [����]valueֵ�趨(��:iDetailRowTemp,��:13)
				String type=TypeHelper.getService().getType(disObj.getDataClassId()).getName();
				ew.setCell(iDetailRowTemp, 13, type);
				// [����]valueֵ�趨(��:iDetailRowTemp,��:14)
				ew.setCell(iDetailRowTemp, 14, String.valueOf(iCount));

				// [���]��Ԫ��ϲ�(�У�iDetailRowTemp���У�3~4)
				ew.mergeRows(iDetailRowTemp, 3, 4);
				// [����]��Ԫ��ϲ�(�У�iDetailRowTemp���У�5~6)
				ew.mergeRows(iDetailRowTemp, 5, 6);
				// [�ͺ�]��Ԫ��ϲ�(�У�iDetailRowTemp���У�7~8)
				ew.mergeRows(iDetailRowTemp, 7, 8);

				// ���õ�Ԫ���Զ�����
				String maxLength = StringUtil.maxValue(disObj.getNumber(), disObj.getName(), disObj.getCode());
				if(maxLength.getBytes().length > 8){
						ew.setCellHeightStyle(iDetailRowTemp, 7, maxLength, 4);
				}else{
				//���������Զ�����
				if(type.getBytes().length > 4){
					ew.setCellHeightStyle(iDetailRowTemp, 13, type, 2);
					}
				}
				// Copy��ϸ����λ�� iDetailRowTemp
				iDetailRowTemp++;
			}

			ew.copyRows(iDetailRowStart, iDetailRowStart, iDetailRowTemp - 1);
			// ��iDetailRowTemp+1��
			// [������λ]��Ԫ��ϲ�(�У�iDetailRowTemp+1���У�3~7)
			ew.mergeRows(iDetailRowTemp, 3, 7);
			// [��ע]��Ԫ��ϲ���Ԫ��ϲ�(�У�iDetailRowTemp+1���У�9~14)
			ew.mergeRows(iDetailRowTemp, 9, 14);

			iDetailRowTemp++;

			for (int j = 0; j < disInfolistSize; j++) {
				disInfo = disInfolist.get(j);
				sNum = disInfo.getDisInfoNum();
				// ��ȡ[��ע]�ֽڳ���
				int noteLength = 0;
				if (disInfo.getNote() != null) {
					noteLength = disInfo.getNote().getBytes().length;
				}

				ew.copyRows(iDetailRowStart + 1, iDetailRowStart + 1, iDetailRowTemp - 1);

				// ��iDetailRowTemp+2+j��
				// [������λ]valueֵ�趨(��:iDetailRowTemp+2+j,��:3)
				ew.setCell(iDetailRowTemp, 3, disInfo.getDisInfoName());
				// [�ַ�����]valueֵ�趨(��:iDetailRowTemp+2+j,��:8)
				ew.setCell(iDetailRowTemp, 8, sNum);
				// [��ע]valueֵ�趨(��:iDetailRowTemp+2+j,��:9)
				ew.setCell(iDetailRowTemp, 9, disInfo.getNote());

				// [������λ]��Ӧvalueֵ��Ԫ��ϲ�(�У�iDetailRowTemp+2+j���У�3~7)
				ew.mergeRows(iDetailRowTemp, 3, 7);
				// [��ע]��Ӧvalueֵ��Ԫ��ϲ���Ԫ��ϲ�(�У�iDetailRowTemp+2+j���У�9~14)
				ew.mergeRows(iDetailRowTemp, 9, 14);
				// ����[��ע]��Ԫ���Զ�����
				if (noteLength > 32) {
					ew.setCellHeightStyle(iDetailRowTemp, 9, disInfo.getNote(), 16);
				}

				iDetailRowTemp++;
			}
		}
		// Copy��ϸ����λ��
		return iDetailRowTemp--;
	}

	/**
	 * ����βֵ�趨��
	 * 
	 * @param ew ExcelUtil
	 * @param rowIndex int
	 * @throws Exception
	 */
	private void setFootData(ExcelUtil ew, int rowIndex) throws Exception {
		// ��rowIndex��
		User currentUser = SessionHelper.getService().getUser();
		// [�а���]��Ӧvalueֵ�趨(�У�Copy����β�п�ʼλ�ã��У�5~7)
		ew.setCell(rowIndex, 5, currentUser.getName());
		//  [������]��Ӧvalueֵ�趨(�У�Copy����β�п�ʼλ�ã��У�10~14)
		ew.setCell(rowIndex, 10, "");

		// ��rowIndex+1��
		// [�а�����]��Ӧvalueֵ�趨(�У�Copy����β�п�ʼλ��+1���У�5~7)
		ew.setCell(rowIndex + 1, 5, DateTimeUtil.getCurrentDate());
		// [��������]��Ӧvalueֵ�趨(�У�Copy����β�п�ʼλ��+1���У�10~14)
		ew.setCell(rowIndex + 1, 10, "");
	}
	
	
	/**
	 * ����βֵ�趨��
	 * 
	 * @param ew ExcelUtil
	 * @param rowIndex int
	 * @throws Exception
	 */
	private void setFootDataForDept7(ExcelUtil ew, int rowIndex) throws Exception {
		        // ��rowIndex��
				User currentUser = SessionHelper.getService().getUser();
				// [�յ���]��Ӧvalueֵ�趨(�У�Copy����β�п�ʼλ�ã��У�1~2)
				long currentTime = System.currentTimeMillis();
				String printTime= new SimpleDateFormat("        HHʱmm��"+"\n"+"     yyyy��MM��dd��").format(new Date(currentTime));
				ew.setCell(rowIndex+1, 1, currentUser.getName()+"   \n"+printTime);
	}
	
	/**
	 * ����β��Ԫ��ϲ���
	 * 
	 * @param ew ExcelUtil
	 * @param rowIndex int
	 */
	private void footMergeForDept7(ExcelUtil ew, int rowIndex) {
		// ��rowIndex��
		// [�յ���]��Ԫ��ϲ�(�У�Copy����β�п�ʼλ�ã��У�1~2)
		ew.mergeRows(rowIndex, 1, 2);
		//���Ƴа��� ��Ԫ��ϲ�(�У�Copy����β�п�ʼλ�ã��У�4~5)
		ew.mergeRows(rowIndex, 4, 5);
		//����ͼ������  ��Ԫ��ϲ�(�У�Copy����β�п�ʼλ�ã��У�6~7)
		ew.mergeRows(rowIndex, 6, 7);
		//��ͼ������
		ew.mergeRows(rowIndex, 8, 9);
		//��ע
		ew.mergeRows(rowIndex, 10, 11);
		
		// ��rowIndex+1��
		// [�յ���]��Ԫ��ϲ�(�У�Copy����β�п�ʼλ�ã��У�1~2)
		ew.mergeRows(rowIndex+1, 1, 2);
		//���Ƴа��� ��Ԫ��ϲ�(�У�Copy����β�п�ʼλ�ã��У�4~5)
		ew.mergeRows(rowIndex+1, 4, 5);
		//����ͼ������  ��Ԫ��ϲ�(�У�Copy����β�п�ʼλ�ã��У�6~7)
		ew.mergeRows(rowIndex+1, 6, 7);
		//��ͼ������
		ew.mergeRows(rowIndex+1, 8, 9);
		//��ע
		ew.mergeRows(rowIndex+1, 10, 11);
        //��rowIndex+2��
		ew.mergeRows(rowIndex+2,1,11);
		
		// 16��17��18�и߶�����
		ew.setRowHeight(rowIndex, 612);
		ew.setRowHeight(rowIndex + 1, 1271);
		ew.setRowHeight(rowIndex + 2, 1341);
	}

	/**
	 * ����β��Ԫ��ϲ���
	 * 
	 * @param ew ExcelUtil
	 * @param rowIndex int
	 */
	private void footMerge(ExcelUtil ew, int rowIndex) {
		// ��rowIndex��
		// [�а���]��Ԫ��ϲ�(�У�Copy����β�п�ʼλ�ã��У�2~4)
		ew.mergeRows(rowIndex, 2, 4);
		// ��Ӧvalueֵ��Ԫ��ϲ�(�У�Copy����β�п�ʼλ�ã��У�5~7)
		ew.mergeRows(rowIndex, 5, 7);
		// [������]��Ԫ��ϲ�(�У�Copy����β�п�ʼλ�ã��У�8~9)
		ew.mergeRows(rowIndex, 8, 9);
		// ��Ӧvalueֵ��Ԫ��ϲ�(�У�Copy����β�п�ʼλ�ã��У�10~14)
		ew.mergeRows(rowIndex, 10, 14);

		// ��rowIndex+1��
		// [�а�����]��Ԫ��ϲ�(�У�Copy����β�п�ʼλ�ã��У�2~4)
		ew.mergeRows(rowIndex + 1, 2, 4);
		// ��Ӧvalueֵ��Ԫ��ϲ�(�У�Copy����β�п�ʼλ��+1���У�5~7)
		ew.mergeRows(rowIndex + 1, 5, 7);
		// [��������]��Ԫ��ϲ�(�У�Copy����β�п�ʼλ��+1���У�8~9)
		ew.mergeRows(rowIndex + 1, 8, 9);
		// ��Ӧvalueֵ��Ԫ��ϲ�(�У�Copy����β�п�ʼλ��+1���У�10~14)
		ew.mergeRows(rowIndex + 1, 10, 14);
	}

	/**
	 * �����п�
	 * 
	 * @param ew ExcelUtil
	 */
	private void setColumnWidthForDept7(ExcelUtil ew) {
		// ��:1,���Ϊ300
		ew.setColumnWidth(1, 2114);
		// ��:2,���Ϊ1200
		ew.setColumnWidth(2, 4195);
		// ��:3~6,���Ϊ2000
		ew.setColumnWidth(3, 4740);
		ew.setColumnWidth(4, 2932);
		ew.setColumnWidth(5, 2045);
		ew.setColumnWidth(6, 2455);
		// ��:7,���Ϊ1200
		ew.setColumnWidth(7, 2285);
		// ��:8,���Ϊ2000
		ew.setColumnWidth(8, 3376);
		// ��:9~12,���Ϊ2000
		ew.setColumnWidth(9, 3205);
		ew.setColumnWidth(10, 3341);
		ew.setColumnWidth(11, 3477);
	}
	
	/**
	 * �����п�
	 * 
	 * @param ew ExcelUtil
	 */
	private void setColumnWidth(ExcelUtil ew) {
		// ��:1,���Ϊ300
		ew.setColumnWidth(1, 300);
		// ��:2,���Ϊ1200
		ew.setColumnWidth(2, 1200);
		// ��:3~6,���Ϊ2000
		ew.setColumnWidth(3, 2000);
		ew.setColumnWidth(4, 2000);
		ew.setColumnWidth(5, 2000);
		ew.setColumnWidth(6, 2000);
		// ��:7,���Ϊ1200
		ew.setColumnWidth(7, 1200);
		// ��:8,���Ϊ2000
		ew.setColumnWidth(8, 2000);
		// ��:9~12,���Ϊ2000
		ew.setColumnWidth(9, 2000);
		ew.setColumnWidth(10, 2000);
		ew.setColumnWidth(11, 2000);
		ew.setColumnWidth(12, 2000);
		// ��:13,���Ϊ1200
		ew.setColumnWidth(13, 1200);
		// ��:14,���Ϊ1200
		ew.setColumnWidth(14, 1200);	
	}
	
	/**
	 * �����иߡ�
	 * 
	 * @param ew ExcelUtil
	 */
	private void setRowHeight (ExcelUtil ew) throws Exception{
		ew.setRowHeight(1,759); 
		ew.setRowHeight(2,423);
		ew.setRowHeight(3,512);
		ew.setRowHeight(4,635);
		ew.setRowHeight(5,706);
		ew.setRowHeight(6,612);
	}
	
	/**
	 * ȡ��ǩ����Ϣ��
	 * 
	 * @param String oid ������󷢷ŵ���oid
	 * @param String beforeAfterFlag ȡ�õ���ǰ���Ǻ����̵�flag
	 * @return String ǩ����Ϣ��������ɽڵ��ִ���ˣ�
	 */
	private String getSigner(String oid, String beforeAfterFlag){
		String signer = "";	
		List<ProcessInstance> processInstanceList = WorkFlowUtil.getProcessInstancesByApprove(oid);
		Map map=loadXml();
		Map processInstancesUsedFlagMap =  new HashMap<String,String>();
		for (ProcessInstance processInstance : processInstanceList) {
			if(processInstancesUsedFlagMap.containsKey(processInstance.getId())){
				continue;
			}else{
				processInstancesUsedFlagMap.put(processInstance.getId(),processInstance.getId());
			}
			List<WorkItem>  workItemList = WorkFlowUtil.getWorkItemsByProcessInstanceId(processInstance.getId(), -1);
			String processName=processInstance.getProcDefinitionName();
			String activityName = "";
			if("before".equalsIgnoreCase(beforeAfterFlag)){
				if(!StringUtil.contains(processName,(String) map.get("before"))){
					continue;
				}
				activityName = (String) map.get("beforeActivityName");
			}else if("after".equalsIgnoreCase(beforeAfterFlag)){
				if(!StringUtil.contains(processName,(String) map.get("after"))){
					continue;
				}
				activityName = (String) map.get("afterActivityName");
			}else{
				break;
			}
			
			for(WorkItem workItem : workItemList){
				if (workItem.getActivityName().equalsIgnoreCase(activityName)){
					if(workItem.getActivityType().equalsIgnoreCase(WorkflowWords.ACTIVITY_TYPE_OUTERCOUNTERSIGN)){// �ⲿ��ǩ
						List<ApproveInfo> approveInfoList = SignUtil.getOutSignApproveInfo(workItem, false, Helper.getPersistService().getObject(oid));
						if (approveInfoList.size() > 0) {
							for (ApproveInfo ai : approveInfoList) {
								String msg = ai.getOperator().getMsg();
								signer += "," + msg;
							}
						}
					}else{
						signer += "," + workItem.getExecutorName();
					}
				}
			}
		}
		if(signer.length() > 0){
			signer = signer.substring(1, signer.length());
		}
		return signer;
	}
	
	/**
	 * ��ȡ����
	 */
	public static Map<String,String> loadXml() {
		Map<String,String> map = new HashMap<String,String>();
		// ��ȡxml�ļ���Document����
		Document xmlDoc = XmlFileUtil.loadByFilePath(LIFECYCLE_CONFIG_FILE_PAHT);
		// ��ȡ���ڵ�
		Element rootEle = xmlDoc.getRootElement();

		String beforeWorkFlowName = rootEle.element(DDM_BEFORE_WORKFLOW).getText();
		map.put("before", beforeWorkFlowName);
		String beforeActivityName = rootEle.element(DDM_BEFORE_ACTIVITY).getText();
		map.put("beforeActivityName", beforeActivityName);

		String afterWorkFlowName = rootEle.element(DDM_AFTER_WORKFLOW).getText();
		map.put("after", afterWorkFlowName);	
		String afterActivityName = rootEle.element(DDM_AFTER_ACTIVITY).getText();
		map.put("afterActivityName", afterActivityName);	
		return map;
	}	
}
