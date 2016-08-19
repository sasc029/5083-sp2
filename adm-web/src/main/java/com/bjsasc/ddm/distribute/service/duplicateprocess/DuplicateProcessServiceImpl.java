package com.bjsasc.ddm.distribute.service.duplicateprocess;

import java.util.ArrayList;
import java.util.List;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.model.duplicateprocess.DuplicateProcess;
import com.bjsasc.ddm.distribute.model.returnreason.ReturnReason;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.util.DateTimeUtil;
import com.cascc.avidm.util.SplitString;

/**
 * 复制加工接口实现类。
 * 
 * @author guowei 2013-3-11
 */
public class DuplicateProcessServiceImpl implements DuplicateProcessService {

	@Override
	public List<DistributePaperTask> getAllDistributePaperTaskByAuth(String stateName) {
		String hql = "from DistributePaperTask t where t.lifeCycleInfo.stateName = ?  order by t.disUrgent desc, t.manageInfo.modifyTime desc ";
		List<DistributePaperTask> list = Helper.getPersistService().find(hql, stateName);
		if (list.size() > 0) {
			for (DistributePaperTask disPaperTask : list) {
				disPaperTask.setOperate("<img src='../images/write.gif' alt='录入加工信息'/>");
				if (ConstUtil.LC_DUPLICATE_PROCESS_NOT_RECEIVED.getName().equals(stateName)) {
					disPaperTask.setOperate("");
				}
			}
		}
		return list;
	}

	@Override
	public List<DistributePaperTask> getAllDistributePaperTaskReturnByAuth(String stateName) {
		String hql = "from DistributePaperTask t where t.lifeCycleInfo.stateName = ? order by  t.disUrgent desc, t.manageInfo.modifyTime desc ";
		List<DistributePaperTask> list = Helper.getPersistService().find(hql, stateName);
		if (list.size() > 0) {
			for (DistributePaperTask disPaperTask : list) {
				String paperTaskInnerId = disPaperTask.getInnerId();
				String paperTaskClassId = disPaperTask.getClassId();

				//判断为回退画面
				//				if (ConstUtil.LC_DUPLICATE_BACKOFF.getName().equals(stateName)) {
				String sqlReturn = "select * from DDM_DIS_RETURN where stateName = ? and taskId =? and taskClassId = ? order by updatetime desc";
				List<ReturnReason> retList = Helper.getPersistService().query(sqlReturn, ReturnReason.class, stateName,
						paperTaskInnerId, paperTaskClassId);
				disPaperTask.setReturnReason(retList.get(0));
				//				}
			}
		}
		return list;
	}

	@Override
	public List<DistributePaperTask> getDuplicateProcessInfo(String oids) {
		List<String> oidList = SplitString.string2List(oids, ConstUtil.C_COMMA);
		List<DistributePaperTask> list = new ArrayList<DistributePaperTask>();
		for (String oid : oidList) {
			Persistable obj = Helper.getPersistService().getObject(oid);
			DistributePaperTask dis = (DistributePaperTask) obj;
			list.add(dis);
		}
		return list;
	}

	@Override
	public void updateDuplicateProcessor(String oids, String collator, String contractor) {
		//纸质任务oid集合
		List<String> oidList = SplitString.string2List(oids, ConstUtil.C_COMMA);
		for (String oid : oidList) {
			String innerId = Helper.getInnerId(oid);
			String classId = Helper.getClassId(oid);
			String hql = "from DuplicateProcess t where t.disPaperTaskClassId || ':' || t.disPaperTaskInnerId=?";
			List<DuplicateProcess> list = PersistHelper.getService().find(hql, oid);

			if (list.size() == 0) {
				String sql = "select disObj.* from DDM_DIS_TASKINFOLINK t,DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink,DDM_DIS_OBJECT disObj"
						+ " where disInfo.innerId = t.toObjectId and disInfo.classId = t.toObjectClassId"
						+ " and objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
						+ " and disObj.innerId = objLink.toObjectId and disObj.classId = objLink.toObjectClassId"
						+ " and t.fromObjectClassId || ':' || t.fromObjectId = ?";
				List<DistributeObject> dataFromlist = PersistHelper.getService()
						.query(sql, DistributeObject.class, oid);

				if (dataFromlist.size() > 0) {
					List<DuplicateProcess> processList = new ArrayList<DuplicateProcess>();
					for (DistributeObject disObj : dataFromlist) {
						DuplicateProcess dis = newDuplicateProcess();
						dis.setDisObjectInnerId(disObj.getInnerId());
						dis.setDisObjectClassId(disObj.getClassId());
						dis.setDisPaperTaskInnerId(innerId);
						dis.setDisPaperTaskClassId(classId);
						dis.setCollator(collator);
						dis.setContractor(contractor);
						dis.setFinishTime(System.currentTimeMillis());
						processList.add(dis);
					}
					if (processList != null && processList.size() > 0) {
						Helper.getPersistService().save(processList);
					}
				}
			} else {
				List<DuplicateProcess> duplicateList = new ArrayList<DuplicateProcess>();
				for (DuplicateProcess dup : list) {
					dup.setCollator(collator);
					dup.setContractor(contractor);
					dup.setFinishTime(System.currentTimeMillis());
					duplicateList.add(dup);
				}
				if (duplicateList != null && duplicateList.size() > 0) {
					Helper.getPersistService().update(duplicateList);
				}

			}
		}
	}

	private DuplicateProcess newDuplicateProcess() {
		DuplicateProcess dis = (DuplicateProcess) PersistUtil.createObject(DuplicateProcess.CLASSID);
		return dis;
	}

	@Override
	public List<DistributeObject> listDuplicateProcessInfo(String oid) {
		String sql = "select DISTINCT objLink.* from DDM_DIS_TASKINFOLINK t,DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink,DDM_DIS_OBJECT disObj"
				+ " where disInfo.innerId = t.toObjectId and disInfo.classId = t.toObjectClassId"
				+ " and objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
				+ " and disObj.innerId = objLink.toObjectId and disObj.classId = objLink.toObjectClassId"
				+ " and t.fromObjectClassId || ':' || t.fromObjectId = ?";
		List<DistributeOrderObjectLink> linkList = PersistHelper.getService().query(sql,
				DistributeOrderObjectLink.class, oid);
		List<DistributeObject> objList = new ArrayList<DistributeObject>();
		for (DistributeOrderObjectLink link : linkList) {
			DistributeObject obj = (DistributeObject) link.getTo();
			obj.setLifeCycleInfo(link.getLifeCycleInfo());
			String retSql = "select r.* from DDM_DIS_RETURN r where r.objectId = ? and r.objectClassId = ? and r.stateId = ? and r.stateName = ? and r.lifeCycleTemplate = ? and r.taskClassId || ':' || r.taskId = ?"
					+ " order by updatetime desc";
			List<ReturnReason> retList = PersistHelper.getService().query(retSql, ReturnReason.class, obj.getInnerId(),
					obj.getClassId(), obj.getLifeCycleInfo().getStateId(), obj.getLifeCycleInfo().getStateName(),
					obj.getLifeCycleInfo().getLifeCycleTemplate(), oid);
			DistributeObject object = obj.cloneDisObj();
			if (!retList.isEmpty()) {
				object.setReturnReason(retList.get(0));
			}
			String hql = "from DuplicateProcess t where t.disObjectClassId || ':' || t.disObjectInnerId = ? and t.disPaperTaskClassId || ':' || t.disPaperTaskInnerId = ?";
			List<DuplicateProcess> list = PersistHelper.getService().find(hql,
					obj.getClassId() + ":" + obj.getInnerId(), oid);

			if (!list.isEmpty()) {
				//				DuplicateProcess dup = list.get(0);
				for (DuplicateProcess dup : list) {
					if ((dup.getDisObjectInnerId()).equals(object.getInnerId())) {
						if (dup.getFinishTime() == 0) {
							object.setFinishTime("");
						} else {
							object.setFinishTime(DateTimeUtil.getDateDisplay(dup.getFinishTime()));
						}
					}
					object.setDuplicateProcess(dup);
				}

			}

			objList.add(object);
		}
		return objList;

	}

	@Override
	public void updateProcessInfo(String collators, String contractors, String oids, String taskOid) {
		//分发数据oid集合
		List<String> oidList = SplitString.string2List(oids, ConstUtil.C_COMMA);
		List<String> collatorList = SplitString.string2List(collators, ConstUtil.C_COMMA);
		List<String> contractorList = SplitString.string2List(contractors, ConstUtil.C_COMMA);
		String taskInnerId = Helper.getInnerId(taskOid);
		String taskClassId = Helper.getClassId(taskOid);
		for (int i = 0; i < oidList.size(); i++) {
			String objectOid = oidList.get(i);
			String innerId = Helper.getInnerId(objectOid);
			String classId = Helper.getClassId(objectOid);

			String hql = "from DuplicateProcess t where t.disObjectClassId || ':' || t.disObjectInnerId = ? and t.disPaperTaskClassId || ':' || t.disPaperTaskInnerId = ?";
			List<DuplicateProcess> list = PersistHelper.getService().find(hql, objectOid, taskOid);
			if (list.size() > 0) {
				DuplicateProcess duplicateProcess = list.get(0);
				String dupOid = Helper.getPersistService().getOid(duplicateProcess.getClassId(),
						duplicateProcess.getInnerId());
				Persistable obj = Helper.getPersistService().getObject(dupOid);
				DuplicateProcess dis = (DuplicateProcess) obj;
				if (collatorList.size() == 0) {
					dis.setCollator("");
				} else {
					dis.setCollator(collatorList.get(i));
				}
				if (contractorList.size() == 0) {
					dis.setContractor("");
				} else {
					dis.setContractor(contractorList.get(i));
				}
				dis.setFinishTime(System.currentTimeMillis());
				Helper.getPersistService().update(dis);
			} else {
				DuplicateProcess dis = newDuplicateProcess();
				dis.setDisObjectClassId(classId);
				dis.setDisObjectInnerId(innerId);
				if (collatorList.size() == 0) {
					dis.setCollator("");
				} else {
					dis.setCollator(collatorList.get(i));
				}
				if (contractorList.size() == 0) {
					dis.setContractor("");
				} else {
					dis.setContractor(contractorList.get(i));
				}
				dis.setDisPaperTaskClassId(taskClassId);
				dis.setDisPaperTaskInnerId(taskInnerId);
				dis.setFinishTime(System.currentTimeMillis());
				Helper.getPersistService().save(dis);
			}
		}
	}

	@Override
	public void updateDisAgreeInfo(String oids, String returnReason, String taskOid) {
		//分发数据的oid集合
		List<String> oidList = SplitString.string2List(oids, ConstUtil.C_COMMA);
		User currentUser = SessionHelper.getService().getUser();
		for (String oid : oidList) {
			String sql = "select distinct orderlink.* from DDM_DIS_TASKINFOLINK taskLink, DDM_DIS_INFO info, DDM_DIS_ORDEROBJLINK orderlink"
					+ " where taskLink.toObjectId = info.innerId and taskLink.toObjectClassId = info.classId"
					+ " and info.disOrderObjLinkId = orderlink.innerId and info.disOrderObjLinkClassId = orderlink.classId"
					+ " and orderlink.toObjectClassId || ':' || orderlink.toObjectId = ?"
					+ " and taskLink.fromObjectClassId || ':' || taskLink.fromObjectId = ?";
			List<DistributeOrderObjectLink> linkList = PersistHelper.getService().query(sql,
					DistributeOrderObjectLink.class, oid, taskOid);
			for (DistributeOrderObjectLink link : linkList) {
				DistributeObject dis = (DistributeObject) link.getTo();
				dis.setLifeCycleInfo(link.getLifeCycleInfo());
				String innerId = Helper.getInnerId(oid);
				String classId = Helper.getClassId(oid);
				String returnSql = "select * from DDM_DIS_RETURN where objectId = ? and objectClassId = ? and stateId = ? and lifeCycleTemplate = ? and taskClassId || ':' || taskId = ?";
				List<ReturnReason> list = PersistHelper.getService().query(returnSql, ReturnReason.class,
						dis.getInnerId(), dis.getClassId(), dis.getLifeCycleInfo().getStateId(),
						dis.getLifeCycleInfo().getLifeCycleTemplate(), taskOid);
				ReturnReason retReason = newReturnReason();
				retReason.setObjectId(innerId);
				retReason.setObjectClassId(classId);
				retReason.setLifeCycleInfo(dis.getLifeCycleInfo());
				retReason.setReturnReason(returnReason);
				retReason.setReturnCount(list.size() + 1);
				retReason.setUserId(currentUser.getInnerId());
				retReason.setUserClassId(currentUser.getClassId());
				retReason.setUserName(currentUser.getName());
				retReason.setTaskId(Helper.getInnerId(taskOid));
				retReason.setTaskClassId(Helper.getClassId(taskOid));
				Helper.getPersistService().save(retReason);
			}
		}
	}

	private ReturnReason newReturnReason() {
		ReturnReason reason = (ReturnReason) PersistUtil.createObject(ReturnReason.CLASSID);
		return reason;
	}
}
