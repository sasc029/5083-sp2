package com.bjsasc.ddm.distribute.service.recdespapertask;

import java.util.ArrayList;
import java.util.List;

import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.model.distributetaskinfolink.DistributeTaskInfoLink;
import com.bjsasc.ddm.distribute.model.recdespapertask.RecDesPaperTask;
import com.bjsasc.ddm.distribute.model.returnreason.ReturnReason;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;

/**
 * 回收销毁纸质任务服务接口实现类
 * 
 * @author sunzongqing
 */
@SuppressWarnings({ "unchecked" })
public class RecDesPaperTaskServiceImpl implements RecDesPaperTaskService {

	@Override
	public List<RecDesPaperTask> getAllRecDesPaperTaskByAuth(String stateName) {
		String hql="from RecDesPaperTask t "
				+ "where t.lifeCycleInfo.stateName = ? "
				+ "order by t.disUrgent desc, "
				+ "t.manageInfo.modifyTime desc";
		List<RecDesPaperTask> list = Helper.getPersistService().find(hql,stateName);
		return list;
	}

	@Override
	public List<RecDesPaperTask> getAllRecDesPaperTaskReturnByAuth(
			String stateName) {
		String hql="from RecDesPaperTask t "
				+ "where t.lifeCycleInfo.stateName = ? "
				+ "order by t.disUrgent desc, "
				+ " t.manageInfo.modifyTime desc ";
		List<RecDesPaperTask> list = Helper.getPersistService().find(hql,stateName);
		List<RecDesPaperTask> taskList = new ArrayList<RecDesPaperTask>();
		if(list.size()>0){
			for(RecDesPaperTask recDesPaperTask:list){
				String paperTaskInnerId = recDesPaperTask.getInnerId();
				String paperTaskClassId = recDesPaperTask.getClassId();
				String sqlReturn = "select * from DDM_DIS_RETURN "
						+ "where objectId is null "
						+ "and objectClassId is null "
						+ "and stateId = ? "
						+ "and taskId = ? "
						+ "and taskClassId = ? "
						+ "order by updatetime desc";
				List<ReturnReason> returnList = Helper.getPersistService()
						.query(sqlReturn, ReturnReason.class,
								recDesPaperTask.getLifeCycleInfo().getStateId(),
								paperTaskInnerId, paperTaskClassId);
				recDesPaperTask.setReturnReason(returnList.get(0));
				taskList.add(recDesPaperTask);
			}
		}
		return taskList;
	}

	@Override
	public void setRecDesPaperTaskLifecycle(RecDesPaperTask recDesPaperTask) {
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		life.initLifecycle(recDesPaperTask);
		Helper.getPersistService().save(recDesPaperTask);
	}

	@Override
	public void createRecDesPaperTask(String number, String name, String note) {
		RecDesPaperTask task = newRecDesPaperTask();
		task.setNumber(number);
		task.setName(name);
		task.setNote(note);
		
		setRecDesPaperTaskLifecycle(task);
	}

	@Override
	public RecDesPaperTask newRecDesPaperTask() {
		RecDesPaperTask task = (RecDesPaperTask) PersistUtil.createObject(RecDesPaperTask.CLASSID);
		return task;
	}

	@Override
	public List<RecDesPaperTask> getRecDesPaperTaskReturnDetail(String taskOid,
			String stateName) {
		String sqlTask = "SELECT * FROM DDM_RECDES_PAPERTASK "
				+ "WHERE CLASSID || ':' || INNERID = "
				+ "( SELECT MAX (TASKCLASSID || ':' || TASKID) "
				+ "FROM DDM_DIS_RETURN "
				+ "WHERE CLASSID || ':' || INNERID = ? )";
		String sqlReturn = "SELECT * FROM DDM_DIS_RETURN "
				+ "WHERE STATENAME = ? AND TASKCLASSID || ':' || TASKID = "
				+ "(  SELECT MAX (TASKCLASSID || ':' || TASKID) "
				+ "FROM DDM_DIS_RETURN "
				+ " WHERE CLASSID || ':' || INNERID = ? ) "
				+ "AND OBJECTID IS NULL AND OBJECTCLASSID IS NULL "
				+ "order by UPDATETIME DESC";
		List<RecDesPaperTask> taskList = Helper.getPersistService()
				.query(sqlTask,RecDesPaperTask.class,taskOid);
		RecDesPaperTask recDesPaperTask = taskList.get(0);
		List<ReturnReason> returnList = Helper.getPersistService().query(sqlReturn,ReturnReason.class,
				stateName, taskOid);
		List<RecDesPaperTask> paperTaskList = new ArrayList<RecDesPaperTask>();
		for(ReturnReason ret:returnList){
			RecDesPaperTask task = recDesPaperTask.cloneRecDesPaperTask();
			task.setReturnReason(ret);
			paperTaskList.add(task);
		}
		return paperTaskList;
	}

	@Override
	public RecDesPaperTask getRecDesPaperTaskProperty(String oid) {
		Persistable obj = Helper.getPersistService().getObject(oid);
		RecDesPaperTask task = (RecDesPaperTask) obj;
		String recDesPaperTaskInnerId = Helper.getInnerId(oid);
		String recDesPaperTaskClassId = Helper.getClassId(oid);
		String sqlOrder = "select ORD.* from DDM_DIS_TASKINFOLINK t,"
				+ "DDM_RECDES_INFO recDesInfo,"
				+ "DDM_DIS_ORDEROBJLINK objLink,"
				+ "DDM_DIS_ORDER ORD"
				+ " where t.fromObjectId =?"
				+ " and t.fromObjectClassId = ? "
				+ " and recDesInfo.innerId = t.toObjectId"
				+ " and recDesInfo.classId = t.toObjectClassId"
				+ " and objLink.innerId = recDesInfo.disOrderObjectLinkId"
				+ " and objLink.classId = recDesInfo.disOrderObjectLinkClassId"
				+ " and ORD.innerId = objLink.fromObjectId"
				+ " and ORD.classId = objLink.fromObjectClassId";
		List<DistributeOrder> orderList = PersistHelper.getService()
				.query(sqlOrder,
						DistributeOrder.class,
						recDesPaperTaskInnerId,
						recDesPaperTaskClassId);
		if(orderList.size()>0){
			DistributeOrder order = orderList.get(0);
			task.setOrderName(order.getName());
			task.setOrderOid(order.getClassId()+":"+order.getInnerId());
		}
		return task;
	}

	@Override
	public void deleteRecDesPaperTaskProperty(String oid) {
		String recDesTaskInnerId = Helper.getPersistService().getInnerId(oid);
		String recDesTaskClassId = Helper.getPersistService().getClassId(oid);
		
		String hql = "from DistributeTaskInfoLink t "
				+ "where t.fromObjectRef.innerId=? "
				+ "and t.fromObjectRef.classId=? ";
		
		List<DistributeTaskInfoLink> links = PersistHelper.getService()
				.find(hql, recDesTaskInnerId, recDesTaskClassId);
		if(links != null && links.size() >0)
		{
			Helper.getPersistService().delete(links);
		}
		Persistable paperTask = Helper.getPersistService().getObject(oid);
		Helper.getPersistService().delete(paperTask);
	}

	/**
	 * 2014-08-12
	 * @author kangyanfei
	 */
	@Override
	public List<RecDesPaperTask> getRecDesPaperTasksByDistributeOrderOid(
			String oid) {
		String sql = "SELECT distinct A .*, B.TASKTYPE "
				+ "FROM DDM_RECDES_PAPERTASK A, DDM_DIS_TASKINFOLINK B, DDM_RECDES_INFO C, DDM_DIS_ORDEROBJLINK D "
				+ "WHERE A .CLASSID = B.FROMOBJECTCLASSID AND A .INNERID = B.FROMOBJECTID "
				+ "AND B.TOOBJECTCLASSID = C.CLASSID AND B.TOOBJECTID = C.INNERID "
				+ "AND C.DISORDEROBJECTLINKCLASSID = D .CLASSID AND C.DISORDEROBJECTLINKID = D .INNERID"
				+ " AND D .FROMOBJECTCLASSID || ':' || D .FROMOBJECTID = ? " + "ORDER BY A.MODIFYTIME DESC";

		List<RecDesPaperTask> list = PersistHelper.getService().query(sql, RecDesPaperTask.class, oid);
		return list;
	}
}
	
