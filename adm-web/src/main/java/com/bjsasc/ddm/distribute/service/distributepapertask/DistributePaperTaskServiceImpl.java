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
 * 纸质任务接口实现类。
 * 
 * @author guowei 2013-2-22
 * 
 */
@SuppressWarnings({ "deprecation", "unchecked", "rawtypes", "static-access" })
public class DistributePaperTaskServiceImpl implements DistributePaperTaskService {
	/** 现行数据管理模块配置信息根目录 */
	private static final String DDM_HOME_PATH = ConfigFileUtil.PLM_HOME_PATH + File.separator + "ddm";

	/** 现行数据管理模块生命周期配置路径*/
	private static final String LIFECYCLE_CONFIG_FILE_PAHT = DDM_HOME_PATH + File.separator + "custom"
		+ File.separator + "ExportCopyExcel.xml";
	/** BeforeWorkFlowName调度前工作流名称*/
	private static final String DDM_BEFORE_WORKFLOW = "BeforeWorkFlowName";
	/** BeforeActivityName调度前工作流节点名称*/
	private static final String DDM_BEFORE_ACTIVITY = "BeforeActivityName";
	/** AfterWorkFlowName节点名称*/
	private static final String DDM_AFTER_WORKFLOW = "AfterWorkFlowName";
	/** AfterActivityName调度后工作流节点名称*/
	private static final String DDM_AFTER_ACTIVITY = "AfterActivityName";
	/*
	 * （非 Javadoc）
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
	 * （非 Javadoc）
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
	 * （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService#createDistributePaperTask(com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask)
	 */
	@Override
	public void createDistributePaperTask(DistributePaperTask dis) {
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		life.initLifecycle(dis);
		Helper.getPersistService().save(dis);
	}

	/*
	 * （非 Javadoc）
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
	 * （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService#newDistributePaperTask()
	 */
	public DistributePaperTask newDistributePaperTask() {
		//DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();

		DistributePaperTask dis = (DistributePaperTask) PersistUtil.createObject(DistributePaperTask.CLASSID);

		// 生命周期初始化
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
	 * （非 Javadoc）
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
	 * （非 Javadoc）
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
				mainFile = "是";
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
			map.put("OPERATE", "<img src='../images/p_xls.gif' alt='打开文件'/>打开文件");
			mapList.add(map);
		}
		return mapList;
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService#getExcelObject(java.lang.String)
	 */
	@Override
	public HSSFWorkbook getExcelObject(String oid) {

		Map map = creatData(oid);

		// Sheet页命名/标题命名
		String sheetName = getSheetName(map);

		// 模板路径
		String templateFullName = ConstUtil.EXCEL_TEMPLATE_PATH + ConstUtil.EXCEL_TEMPLATE_PAPER_TASK;
		// 模板报表头行开始位置
		int iHeadRowStart = 1;
		// 模板报表头行结束位置
		int iHeadRowEnd = 5;
		// 模板明细行开始位置/Copy明细行开始位置
		int iDetailRowStart = 6;
		// 模板报表尾行开始位置
		int iFootRowStart = 9;
		// 模板报表尾行结束位置
		int iFootRowEnd = 10;
		// Copy报表头行开始位置
		int iHeadCopyRowStart = 1;
		// 明细行结束位置
		int iDetailRowEnd = 0;
		// Copy报表尾行开始位置 = 明细行结束位置 +1
		int iFootCopyRowStart;

		try {
			ExcelUtil ew = new ExcelUtil();
			ew.init(sheetName, templateFullName);

			// 报表头拷贝
			ew.copyRows(iHeadRowStart - 1, iHeadRowEnd - 1, iHeadCopyRowStart - 1);
			// 报表头值设定s
			setHeadData(ew, sheetName, map);
			// 报表头单元格合并
			headerMerge(ew);

			// 明细行拷贝，值设定，单元格合并
			iDetailRowEnd = copySetMergeDetail(ew, iDetailRowStart, map);
			iFootCopyRowStart = iDetailRowEnd;

			// 报表尾行拷贝
			ew.copyRows(iFootRowStart - 1, iFootRowEnd - 1, iFootCopyRowStart - 1);
			// 报表尾值设定
			setFootData(ew, iFootCopyRowStart);
			// 报表尾单元格合并
			footMerge(ew, iFootCopyRowStart);

			// 设置列表宽度
			setColumnWidth(ew);
			// 设置打印版式
			ew.setPrintStyle(false);

			ew.close();
			ew.deleteTemplateSheet();

			return ew.getCurrentWorkbook();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService#getExcelObject(java.lang.String)
	 */
	@Override
	public HSSFWorkbook getExcelObjectForDept7(String oid) {

		Map map = creatData(oid);

		// Sheet页命名/标题命名
		String sheetName = getSheetNameForDept7(map);

		// 模板路径
		String templateFullName = ConstUtil.EXCEL_TEMPLATE_PATH + ConstUtil.EXCEL_TEMPLATE_COPY;
		// 模板报表头行开始位置
		int iHeadRowStart = 1;
		// 模板报表头行结束位置
		int iHeadRowEnd = 6;
		// 模板明细行开始位置/Copy明细行开始位置
		int iDetailRowStart = 7;
		// 模板报表尾行开始位置
		int iFootRowStart = 16;
		// 模板报表尾行结束位置
		int iFootRowEnd = 18;
		// Copy报表头行开始位置
		int iHeadCopyRowStart = 1;
		// 明细行结束位置
		int iDetailRowEnd = 0;
		// Copy报表尾行开始位置 = 明细行结束位置 +1
		int iFootCopyRowStart;

		try {
			ExcelUtil ew = new ExcelUtil();
			ew.init(sheetName, templateFullName);

			// 报表头拷贝
			ew.copyRows(iHeadRowStart - 1, iHeadRowEnd - 1, iHeadCopyRowStart - 1);
			// 报表头值设定s
			setHeadDataForDept7(ew, sheetName, map);
			// 报表头单元格合并
			headerMergeForDept7(ew);

			// 明细行拷贝，值设定，单元格合并
			iDetailRowEnd = copySetMergeDetailForDept7(ew, iDetailRowStart, map);
			iFootCopyRowStart = iDetailRowEnd;

			// 报表尾行拷贝
			ew.copyRows(iFootRowStart - 1, iFootRowEnd - 1, iFootCopyRowStart - 1);
			// 报表尾值设定
			setFootDataForDept7(ew, iFootCopyRowStart);
			// 报表尾单元格合并
			footMergeForDept7(ew, iFootCopyRowStart);

			// 设置列表宽度
			setColumnWidthForDept7(ew);
			//设置行高
			setRowHeight(ew);
			// 设置打印版式
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
	 * 取得Sheet名。
	 * 
	 * @param map Map 
	 * @return String
	 */
	private String getSheetNameForDept7(Map map) {				
		return ConstUtil.DOCUMENT_COPY;				
	}

	/**
	 * 取得Sheet名。
	 * 
	 * @param map Map 
	 * @return String
	 */
	private String getSheetName(Map map) {
		DistributeOrder disOrder = (DistributeOrder) map.get("disOrder");
		if ("0".equals(disOrder.getOrderType())) {
			// 加工申请单Sheet页命名/标题命名
			return ConstUtil.PAPER_TASK_PROCESS;
		} else {
			// 补发加工申请单Sheet页命名/标题命名
			return ConstUtil.PAPER_TASK_REISSUE_PROCESS;
		}
	}

	/**
	 * 创建数据信息。
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

		// 发放单与分发数据link:DDM_DIS_ORDEROBJLINK
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
				// 发放单数据:DDM_DIS_ORDER
				map.put("disOrder", (DistributeOrder) disOrdObjLink.getFrom());

				// 获取分发信息完工期限
				long disDeadLine = disOrdObjLink.getDisDeadLine();
				map.put("disDeadLine", disDeadLine);
			}
			DistributeObject disobj = (DistributeObject) disOrdObjLink.getTo();
			
		    Persistable dataObj = Helper.getPersistService().getObject(disobj.getDataClassId() + ":" + disobj.getDataInnerId());
		    //部件不导出到加工单excel里边
			if (!(dataObj instanceof Part)) {
				// 分发数据对象:DDM_DIS_OBJECT
				detaildisObjList.add((DistributeObject) disOrdObjLink.getTo());

				// 分发信息DDM_DIS_INFO
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
					// 分发数据对象:DDM_DIS_OBJECT
					objList.add((DistributeObject) disOrdObjLink.getTo());
					infoObjMap.put("objList", objList);
					infoObjMap.put("infoList", disInfoList);
					dataMap.put(infoMapKey, infoObjMap);
					//套对象的特殊处理
					//成套初次分发、成套补发时，加工单、发送单中仅展示套对象条目信息，如有对其中某套内对象增加特殊分发信息时，也需要将单独分发的套内对象作为条目信息展示
					if ("ATSuit".equals(disobj.getDataClassId())) {
						suit = (ATSuit)Helper.getPersistService().getObject(disobj.getDataClassId() + ":" + disobj.getDataInnerId());
						DistributeOrder disOrder = (DistributeOrder) map.get("disOrder");
						// 加工申请单,非初次分发
						if ("0".equals(disOrder.getOrderType()) && !"A".equals(suit.getIterationInfo().getVersionNo())) {
							suit = null;
						} else {
							//成套初次分发、成套补发时
							suitInfoKey = infoMapKey;
						}
					}
				} else {
					if (dataMap.containsKey(infoMapKey)) {

						infoObjMap = dataMap.get(infoMapKey);
						//套对象的特殊处理
						//成套初次分发、成套补发时，加工单、发送单中仅展示套对象条目信息，如有对其中某套内对象增加特殊分发信息时，也需要将单独分发的套内对象作为条目信息展示
						if (suit != null && infoMapKey.equals(suitInfoKey)) {
							Persistable obj = Helper.getPersistService().getObject(disobj.getDataClassId() + ":" + disobj.getDataInnerId());
							if (obj instanceof Suited) {
								ATSuitMemberLink link = ATSuitMemberLink.getlink(suit, (Suited)obj);
								if (link == null) {
									objList = (List<DistributeObject>) infoObjMap.get("objList");

									// 分发数据对象:DDM_DIS_OBJECT
									objList.add((DistributeObject) disOrdObjLink.getTo());
								}
							} else {
								objList = (List<DistributeObject>) infoObjMap.get("objList");

								// 分发数据对象:DDM_DIS_OBJECT
								objList.add((DistributeObject) disOrdObjLink.getTo());
							}
							
						} else {
							objList = (List<DistributeObject>) infoObjMap.get("objList");

							// 分发数据对象:DDM_DIS_OBJECT
							objList.add((DistributeObject) disOrdObjLink.getTo());
						}
					} else {
						infoObjMap = new HashMap();
						dataMap.put(infoMapKey, infoObjMap);

						objList = new ArrayList();
						// 分发数据对象:DDM_DIS_OBJECT
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
	 * 报表头值设定。
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

		// 第一行
		// [标题名称]value值(行:1,列:1)
		ew.setCell(1, 1, titleName);

		// 第四行
		// [申请单位]value值(行:4,列:2)
		User applyUser=disOrder.getManageInfo().getCreateBy();
		String applyOrganizationName=applyUser.getOrganizationName();
		ew.setCell(4, 2, applyOrganizationName);
		// [申请人]value值(行:4,列:5)
		ew.setCell(4, 5, disOrder.getManageInfo().getCreateBy().getName());
		// [申请日期]value值(行:4,列:7)
		ew.setCell(4, 7, DateTimeUtil.getDateDisplay(createTime));
		// [要求完成日期]value值(行:4,列:9)
		long disDeadLine = (Long) map.get("disDeadLine");
		ew.setCell(4, 9, DateTimeUtil.getDateDisplay(disDeadLine));
		// [审查人]value值(行:4,列:11)
		ew.setCell(4, 11, "");
		// [调度人]value值(行:4,列:13)
	
		String createName = disPaperTask.getCreateByName();
		ew.setCell(4, 13, createName);
		
		// 设置申请单位单元格自动拉伸
		if (createName != null && createName.getBytes().length > 8) {
			ew.setCellHeightStyle(4, 13, createName, 4);
		} else if (applyOrganizationName != null && applyOrganizationName.getBytes().length > 20) {
			ew.setCellHeightStyle(4, 2, applyOrganizationName, 10);
		}
	}
	
	/**
	 * 报表头值设定。
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
		// 第一行
		// [标题名称]value值
		ew.setCell(1, 1, titleName);
		//第二行[发放单编号]value值
		ew.setCell(2,9,disOrder.getNumber());
		//第三行[型号代号（纸质任务的）上下文ID]
		ew.setCell(3,2,disPaperTask.getContextInfo().getContext().getNumber());
		// [申请单位]value值
		User applyUser=disOrder.getManageInfo().getCreateBy();
		String applyOrganizationName=applyUser.getOrganizationName();
		ew.setCell(5, 1, applyOrganizationName);
		// [申请人]value值
		ew.setCell(5, 2, disOrder.getManageInfo().getCreateBy().getName());
		//申请日期
		ew.setCell(5, 9, DateTimeUtil.getDateDisplay(createTime));
		//要求完成日期
		long disDeadLine = (Long) map.get("disDeadLine");
		ew.setCell(5, 10, DateTimeUtil.getDateDisplay(disDeadLine));
		// [研发中心型号管理处：调度人]value值(行:5,列:6)
		String createName = disPaperTask.getCreateByName();
		ew.setCell(5, 6, createName);
	    //[申请部门负责人]:调度前流程签署信息
		String  beforeSigner = getSigner(disOrder.getOid(),"before");
		ew.setCell(5,4,beforeSigner);
		//[航天工程部（计划人员]:调度后流程签署信息
		String  afterSigner = getSigner(disOrder.getOid(),"after");
		ew.setCell(5,8,afterSigner);
	}
	
	/**
	 * 报表头单元格合并。
	 * 
	 * @param ew ExcelUtil
	 */
	private void headerMergeForDept7(ExcelUtil ew) {
		//标题(行：1，列：1~11)
		ew.mergeRows(1, 1, 11);
		//发放单编号(行：2，列：9~11)
		ew.mergeRows(2, 9, 11);
		//型号代号(行：3，列：2~6)
		ew.mergeRows(3, 2, 6);
		//申请部门负责人(行：4，列：4~5)
		ew.mergeRows(4,4,5);
		//研发中心型号管理处(行：4，列：6~7);
		ew.mergeRows(4,6,7);
		//要求完成时间(行：4，列：10~11)
		ew.mergeRows(4,10,11);
		//要求完成时间value(行：5，列：10~11)
		ew.mergeRows(5,10,11);
		//申请部门负责人value合并(行：5，列：4~5)
		ew.mergeRows(5,4,5);
		//研发中心型号管理处value合并(行：4，列：6~7);
		ew.mergeRows(5,6,7);
	}

	/**
	 * 报表头单元格合并。
	 * 
	 * @param ew ExcelUtil
	 */
	private void headerMerge(ExcelUtil ew) {
		// [补发加工申请单]单元格合并(行：1~2，列：1~14)
		ew.mergeCells(1, 2, 1, 14);

		// [申请单位]单元格合并(行：3，列：2~4)
		ew.mergeRows(3, 2, 4);
		// [申请人]单元格合并(行：3，列：5~6)
		ew.mergeRows(3, 5, 6);
		// [申请日期]单元格合并(行：3，列：7~8)
		ew.mergeRows(3, 7, 8);
		// [要求完成日期]单元格合并(行：3，列：9~10)
		ew.mergeRows(3, 9, 10);
		// [审查人]单元格合并(行：3，列：11~12)
		ew.mergeRows(3, 11, 12);
		// [调度人]单元格合并(行：3，列：13~14)
		ew.mergeRows(3, 13, 14);
		// 相应value值单元格合并
		ew.mergeRows(4, 2, 4);
		ew.mergeRows(4, 5, 6);
		ew.mergeRows(4, 7, 8);
		ew.mergeRows(4, 9, 10);
		ew.mergeRows(4, 11, 12);
		ew.mergeRows(4, 13, 14);

		// [编号]单元格合并(行：5，列：3~4)
		ew.mergeRows(5, 3, 4);
		// [名称]单元格合并(行：5，列：5~6)
		ew.mergeRows(5, 5, 6);
		// [型号]单元格合并(行：5，列：7~8)
		ew.mergeRows(5, 7, 8);
	}
	
	/**
	 * 报表明细行Copy，值设定和单元格合并。
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
		// Copy明细行位置
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
					// 第iDetailRowTemp行
					// [序号]value值设定(行:iDetailRowTemp,列:1)
					ew.setCell(iDetailRowTemp, 1, ++index);
					// [编号]value值设定(行:iDetailRowTemp,列:2)
					ew.setCell(iDetailRowTemp, 2, disObj.getNumber());
					// [名称]value值设定(行:iDetailRowTemp,列:3)
					ew.setCell(iDetailRowTemp, 3, disObj.getName());
					// [密集]value值设定(行:iDetailRowTemp,列:5)
					ew.setCell(iDetailRowTemp, 5, disObj.getSecurityLevel());
					// [阶段]value值设定(行:iDetailRowTemp,列:4)
					ew.setCell(iDetailRowTemp, 4, disObj.getPhase());
					// [页数]value值设定(行:iDetailRowTemp,列:6)
					ew.setCell(iDetailRowTemp, 6, disObj.getPages());
					// [份数]value值设定(行:iDetailRowTemp,列:7)
					ew.setCell(iDetailRowTemp, 7, String.valueOf(sNum));
					// [发往单位value]单元格合并(行：iDetailRowTemp，列：8~9)
					ew.mergeRows(iDetailRowTemp, 8, 9);
					// [发往单位value]单元格合并(行：iDetailRowTemp，列：8~9)
					ew.mergeRows(iDetailRowTemp-1, 8, 9);
					//[签收]单元格合并
					ew.mergeRows(iDetailRowTemp-1,10,11);
					//[签收]单元格value合并
					ew.mergeRows(iDetailRowTemp,10,11);
					// [发往单位]value值设定(行:iDetailRowTemp+2+j,列:3)
					ew.setCell(iDetailRowTemp, 8, disInfo.getDisInfoName());
					// 行高设置
					ew.setRowHeight(iDetailRowTemp, 612);

					ew.copyRows(iDetailRowStart, iDetailRowStart, iDetailRowTemp);
					
					iDetailRowTemp++;
				}
			}
		}
		// Copy明细结束位置
		return iDetailRowTemp--;		
	}

	/**
	 * 报表明细行Copy，值设定和单元格合并。
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
		// Copy明细行位置
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

			// 获取分发总份数
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

				// 第iDetailRowTemp行
				// [序号]value值设定(行:iDetailRowTemp,列:2)
				ew.setCell(iDetailRowTemp, 2, ++index);

				// [编号]value值设定(行:iDetailRowTemp,列:3)
				ew.setCell(iDetailRowTemp, 3, disObj.getNumber());
				// [名称]value值设定(行:iDetailRowTemp,列:5)
				ew.setCell(iDetailRowTemp, 5, disObj.getName());
				// [型号]value值设定(行:iDetailRowTemp,列:7)
				ew.setCell(iDetailRowTemp, 7, disObj.getCode());
				// [密集]value值设定(行:iDetailRowTemp,列:9)
				ew.setCell(iDetailRowTemp, 9, disObj.getSecurityLevel());
				// [阶段]value值设定(行:iDetailRowTemp,列:10)
				ew.setCell(iDetailRowTemp, 10, disObj.getPhase());
				// [阶段]value值设定(行:iDetailRowTemp,列:11)
				ew.setCell(iDetailRowTemp, 11, disObj.getVersion());
				// [页数]value值设定(行:iDetailRowTemp,列:12)
				ew.setCell(iDetailRowTemp, 12, disObj.getPages());
				// [类型]value值设定(行:iDetailRowTemp,列:13)
				String type=TypeHelper.getService().getType(disObj.getDataClassId()).getName();
				ew.setCell(iDetailRowTemp, 13, type);
				// [份数]value值设定(行:iDetailRowTemp,列:14)
				ew.setCell(iDetailRowTemp, 14, String.valueOf(iCount));

				// [编号]单元格合并(行：iDetailRowTemp，列：3~4)
				ew.mergeRows(iDetailRowTemp, 3, 4);
				// [名称]单元格合并(行：iDetailRowTemp，列：5~6)
				ew.mergeRows(iDetailRowTemp, 5, 6);
				// [型号]单元格合并(行：iDetailRowTemp，列：7~8)
				ew.mergeRows(iDetailRowTemp, 7, 8);

				// 设置单元格自动拉伸
				String maxLength = StringUtil.maxValue(disObj.getNumber(), disObj.getName(), disObj.getCode());
				if(maxLength.getBytes().length > 8){
						ew.setCellHeightStyle(iDetailRowTemp, 7, maxLength, 4);
				}else{
				//设置类型自动拉伸
				if(type.getBytes().length > 4){
					ew.setCellHeightStyle(iDetailRowTemp, 13, type, 2);
					}
				}
				// Copy明细结束位置 iDetailRowTemp
				iDetailRowTemp++;
			}

			ew.copyRows(iDetailRowStart, iDetailRowStart, iDetailRowTemp - 1);
			// 第iDetailRowTemp+1行
			// [发往单位]单元格合并(行：iDetailRowTemp+1，列：3~7)
			ew.mergeRows(iDetailRowTemp, 3, 7);
			// [备注]单元格合并单元格合并(行：iDetailRowTemp+1，列：9~14)
			ew.mergeRows(iDetailRowTemp, 9, 14);

			iDetailRowTemp++;

			for (int j = 0; j < disInfolistSize; j++) {
				disInfo = disInfolist.get(j);
				sNum = disInfo.getDisInfoNum();
				// 获取[备注]字节长度
				int noteLength = 0;
				if (disInfo.getNote() != null) {
					noteLength = disInfo.getNote().getBytes().length;
				}

				ew.copyRows(iDetailRowStart + 1, iDetailRowStart + 1, iDetailRowTemp - 1);

				// 第iDetailRowTemp+2+j行
				// [发往单位]value值设定(行:iDetailRowTemp+2+j,列:3)
				ew.setCell(iDetailRowTemp, 3, disInfo.getDisInfoName());
				// [分发份数]value值设定(行:iDetailRowTemp+2+j,列:8)
				ew.setCell(iDetailRowTemp, 8, sNum);
				// [备注]value值设定(行:iDetailRowTemp+2+j,列:9)
				ew.setCell(iDetailRowTemp, 9, disInfo.getNote());

				// [发往单位]相应value值单元格合并(行：iDetailRowTemp+2+j，列：3~7)
				ew.mergeRows(iDetailRowTemp, 3, 7);
				// [备注]相应value值单元格合并单元格合并(行：iDetailRowTemp+2+j，列：9~14)
				ew.mergeRows(iDetailRowTemp, 9, 14);
				// 设置[备注]单元格自动拉伸
				if (noteLength > 32) {
					ew.setCellHeightStyle(iDetailRowTemp, 9, disInfo.getNote(), 16);
				}

				iDetailRowTemp++;
			}
		}
		// Copy明细结束位置
		return iDetailRowTemp--;
	}

	/**
	 * 报表尾值设定。
	 * 
	 * @param ew ExcelUtil
	 * @param rowIndex int
	 * @throws Exception
	 */
	private void setFootData(ExcelUtil ew, int rowIndex) throws Exception {
		// 第rowIndex行
		User currentUser = SessionHelper.getService().getUser();
		// [承办人]相应value值设定(行：Copy报表尾行开始位置，列：5~7)
		ew.setCell(rowIndex, 5, currentUser.getName());
		//  [复制人]相应value值设定(行：Copy报表尾行开始位置，列：10~14)
		ew.setCell(rowIndex, 10, "");

		// 第rowIndex+1行
		// [承办日期]相应value值设定(行：Copy报表尾行开始位置+1，列：5~7)
		ew.setCell(rowIndex + 1, 5, DateTimeUtil.getCurrentDate());
		// [复制日期]相应value值设定(行：Copy报表尾行开始位置+1，列：10~14)
		ew.setCell(rowIndex + 1, 10, "");
	}
	
	
	/**
	 * 报表尾值设定。
	 * 
	 * @param ew ExcelUtil
	 * @param rowIndex int
	 * @throws Exception
	 */
	private void setFootDataForDept7(ExcelUtil ew, int rowIndex) throws Exception {
		        // 第rowIndex行
				User currentUser = SessionHelper.getService().getUser();
				// [收单人]相应value值设定(行：Copy报表尾行开始位置，列：1~2)
				long currentTime = System.currentTimeMillis();
				String printTime= new SimpleDateFormat("        HH时mm分"+"\n"+"     yyyy年MM月dd日").format(new Date(currentTime));
				ew.setCell(rowIndex+1, 1, currentUser.getName()+"   \n"+printTime);
	}
	
	/**
	 * 报表尾单元格合并。
	 * 
	 * @param ew ExcelUtil
	 * @param rowIndex int
	 */
	private void footMergeForDept7(ExcelUtil ew, int rowIndex) {
		// 第rowIndex行
		// [收单人]单元格合并(行：Copy报表尾行开始位置，列：1~2)
		ew.mergeRows(rowIndex, 1, 2);
		//复制承办人 单元格合并(行：Copy报表尾行开始位置，列：4~5)
		ew.mergeRows(rowIndex, 4, 5);
		//复制图接收人  单元格合并(行：Copy报表尾行开始位置，列：6~7)
		ew.mergeRows(rowIndex, 6, 7);
		//底图回收人
		ew.mergeRows(rowIndex, 8, 9);
		//备注
		ew.mergeRows(rowIndex, 10, 11);
		
		// 第rowIndex+1行
		// [收单人]单元格合并(行：Copy报表尾行开始位置，列：1~2)
		ew.mergeRows(rowIndex+1, 1, 2);
		//复制承办人 单元格合并(行：Copy报表尾行开始位置，列：4~5)
		ew.mergeRows(rowIndex+1, 4, 5);
		//复制图接收人  单元格合并(行：Copy报表尾行开始位置，列：6~7)
		ew.mergeRows(rowIndex+1, 6, 7);
		//底图回收人
		ew.mergeRows(rowIndex+1, 8, 9);
		//备注
		ew.mergeRows(rowIndex+1, 10, 11);
        //第rowIndex+2行
		ew.mergeRows(rowIndex+2,1,11);
		
		// 16、17、18行高度设置
		ew.setRowHeight(rowIndex, 612);
		ew.setRowHeight(rowIndex + 1, 1271);
		ew.setRowHeight(rowIndex + 2, 1341);
	}

	/**
	 * 报表尾单元格合并。
	 * 
	 * @param ew ExcelUtil
	 * @param rowIndex int
	 */
	private void footMerge(ExcelUtil ew, int rowIndex) {
		// 第rowIndex行
		// [承办人]单元格合并(行：Copy报表尾行开始位置，列：2~4)
		ew.mergeRows(rowIndex, 2, 4);
		// 相应value值单元格合并(行：Copy报表尾行开始位置，列：5~7)
		ew.mergeRows(rowIndex, 5, 7);
		// [复制人]单元格合并(行：Copy报表尾行开始位置，列：8~9)
		ew.mergeRows(rowIndex, 8, 9);
		// 相应value值单元格合并(行：Copy报表尾行开始位置，列：10~14)
		ew.mergeRows(rowIndex, 10, 14);

		// 第rowIndex+1行
		// [承办日期]单元格合并(行：Copy报表尾行开始位置，列：2~4)
		ew.mergeRows(rowIndex + 1, 2, 4);
		// 相应value值单元格合并(行：Copy报表尾行开始位置+1，列：5~7)
		ew.mergeRows(rowIndex + 1, 5, 7);
		// [复制日期]单元格合并(行：Copy报表尾行开始位置+1，列：8~9)
		ew.mergeRows(rowIndex + 1, 8, 9);
		// 相应value值单元格合并(行：Copy报表尾行开始位置+1，列：10~14)
		ew.mergeRows(rowIndex + 1, 10, 14);
	}

	/**
	 * 设置列宽。
	 * 
	 * @param ew ExcelUtil
	 */
	private void setColumnWidthForDept7(ExcelUtil ew) {
		// 列:1,宽度为300
		ew.setColumnWidth(1, 2114);
		// 列:2,宽度为1200
		ew.setColumnWidth(2, 4195);
		// 列:3~6,宽度为2000
		ew.setColumnWidth(3, 4740);
		ew.setColumnWidth(4, 2932);
		ew.setColumnWidth(5, 2045);
		ew.setColumnWidth(6, 2455);
		// 列:7,宽度为1200
		ew.setColumnWidth(7, 2285);
		// 列:8,宽度为2000
		ew.setColumnWidth(8, 3376);
		// 列:9~12,宽度为2000
		ew.setColumnWidth(9, 3205);
		ew.setColumnWidth(10, 3341);
		ew.setColumnWidth(11, 3477);
	}
	
	/**
	 * 设置列宽。
	 * 
	 * @param ew ExcelUtil
	 */
	private void setColumnWidth(ExcelUtil ew) {
		// 列:1,宽度为300
		ew.setColumnWidth(1, 300);
		// 列:2,宽度为1200
		ew.setColumnWidth(2, 1200);
		// 列:3~6,宽度为2000
		ew.setColumnWidth(3, 2000);
		ew.setColumnWidth(4, 2000);
		ew.setColumnWidth(5, 2000);
		ew.setColumnWidth(6, 2000);
		// 列:7,宽度为1200
		ew.setColumnWidth(7, 1200);
		// 列:8,宽度为2000
		ew.setColumnWidth(8, 2000);
		// 列:9~12,宽度为2000
		ew.setColumnWidth(9, 2000);
		ew.setColumnWidth(10, 2000);
		ew.setColumnWidth(11, 2000);
		ew.setColumnWidth(12, 2000);
		// 列:13,宽度为1200
		ew.setColumnWidth(13, 1200);
		// 列:14,宽度为1200
		ew.setColumnWidth(14, 1200);	
	}
	
	/**
	 * 设置行高。
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
	 * 取得签署信息。
	 * 
	 * @param String oid 送审对象发放单的oid
	 * @param String beforeAfterFlag 取得调度前还是后流程的flag
	 * @return String 签署信息（审批完成节点的执行人）
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
					if(workItem.getActivityType().equalsIgnoreCase(WorkflowWords.ACTIVITY_TYPE_OUTERCOUNTERSIGN)){// 外部会签
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
	 * 读取配置
	 */
	public static Map<String,String> loadXml() {
		Map<String,String> map = new HashMap<String,String>();
		// 获取xml文件的Document对象
		Document xmlDoc = XmlFileUtil.loadByFilePath(LIFECYCLE_CONFIG_FILE_PAHT);
		// 获取根节点
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
