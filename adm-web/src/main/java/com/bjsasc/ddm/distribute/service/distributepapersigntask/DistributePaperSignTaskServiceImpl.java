package com.bjsasc.ddm.distribute.service.distributepapersigntask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.common.ConstUtil.LIFECYCLE_OPT;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributepapersigntask.DistributePaperSignTask;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.distributetask.DistributeTask;
import com.bjsasc.ddm.distribute.model.distributetaskinfolink.DistributeTaskInfoLink;
import com.bjsasc.ddm.distribute.model.returnreason.ReturnReason;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.ddm.distribute.service.distributeprincipal.DistributePrincipalService;
import com.bjsasc.ddm.distribute.service.distributetaskinfolink.DistributeTaskInfoLinkService;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.privilege.acl.ACLData;
import com.bjsasc.plm.core.privilege.acl.ACLDataHelper;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.access.AccessControlHelper;
import com.bjsasc.plm.core.system.access.operate.Operate;
import com.bjsasc.plm.core.system.principal.OrganizationHelper;
import com.bjsasc.plm.core.system.principal.Principal;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.system.principal.UserHelper;
import com.bjsasc.plm.core.util.StringUtil;
import com.cascc.avidm.util.SplitString;

/**
 * 纸质签收任务接口实现类。
 * 
 * @author zhangguoqiang 2014-09-11
 */
@SuppressWarnings({ "deprecation", "unchecked"})
public class DistributePaperSignTaskServiceImpl implements DistributePaperSignTaskService {

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.bjsasc.ddm.distribute.service.distributepapersigntask.
	 * DistributePaperSignTaskService#getAllDistributePaperSignTask(java.lang.String)
	 */
	@Override
	public List<DistributePaperSignTask> getAllNoSignDistributePaperSignTask(Object stateName) {
		User currentUser = SessionHelper.getService().getUser();
		String sql = "SELECT DISTINCT A .* FROM DDM_DIS_PAPERSIGNTASK A, DDM_DIS_TASKINFOLINK B, DDM_DIS_INFO C "
				+ " WHERE A .INNERID = B.FROMOBJECTID AND A .CLASSID = B.FROMOBJECTCLASSID "
				+ "AND C.INNERID = B.TOOBJECTID AND C.CLASSID = B.TOOBJECTCLASSID"
				+ " AND A .STATENAME = ? AND C.DISINFOID = ? AND C.INFOCLASSID = ? " + "ORDER BY A .MODIFYTIME DESC";
		List<DistributePaperSignTask> taskList = Helper.getPersistService().query(sql, DistributePaperSignTask.class, stateName,
				currentUser.getInnerId(), currentUser.getClassId());
		List<DistributePaperSignTask> electList = getAllOrganizationPaperSigndistribute(stateName);
		taskList.addAll(electList);

		return taskList;
	}

	public List<DistributePaperSignTask> getAllDistributePaperSignTask(Object stateName) {
		User currentUser = SessionHelper.getService().getUser();
		String sql = "SELECT DISTINCT * FROM DDM_DIS_PAPERSIGNTASK "
				+ " WHERE STATENAME = ? AND RECEIVEBYID = ? AND RECEIVEBYCLASSID = ? ORDER BY MODIFYTIME DESC";
		List<DistributePaperSignTask> taskList = Helper.getPersistService().query(sql, DistributePaperSignTask.class, stateName,
				currentUser.getInnerId(), currentUser.getClassId());

		return taskList;
	}

	@Override
	public List<DistributePaperSignTask> getAllReturnDistributePaperSignTask(Object stateName) {
		User currentUser = SessionHelper.getService().getUser();
		String sql = "SELECT DISTINCT * FROM DDM_DIS_PAPERSIGNTASK "
				+ " WHERE STATENAME = ? AND RECEIVEBYID = ? AND RECEIVEBYCLASSID = ? ORDER BY MODIFYTIME DESC";
		List<DistributePaperSignTask> taskList = Helper.getPersistService().query(sql, DistributePaperSignTask.class, stateName,
				currentUser.getInnerId(), currentUser.getClassId());
		List<DistributePaperSignTask> list = new ArrayList<DistributePaperSignTask>();
		for (DistributePaperSignTask task : taskList) {
			String returnReason = task.getReturnReason();
			task.setReturnReason(returnReason);
			list.add(task);
		}
		return list;
	}

	public DistributePaperSignTask getDistributePaperSignTaskInfo(String distributePaperSignOid) {

		Persistable obj = Helper.getPersistService().getObject(distributePaperSignOid);
		DistributePaperSignTask disTask = (DistributePaperSignTask) obj;

		List<DistributeInfo> infos = getDistributeInfos(distributePaperSignOid);
		if (infos != null) {
			DistributeInfo distributeInfo = infos.get(0);
			if (distributeInfo != null) {
				long sendTime = distributeInfo.getSendTime();
				if (sendTime > 0) {
					disTask.setSendTime(sendTime);
				}
			}
		}
		String taskOid = disTask.getClassId() + ":" + disTask.getInnerId();
		String sqlOrder = "select disOrder.name as name,disOrder.innerId as innerId,disOrder.classId as classId from "
				+ "(select ord.* from DDM_DIS_TASKINFOLINK t,DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink,DDM_DIS_ORDER ord"
				+ " where disInfo.innerId = t.toObjectId and disInfo.classId = t.toObjectClassId"
				+ " and objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
				+ " and ord.innerId = objLink.fromObjectId and ord.classId = objLink.fromObjectClassId"
				+ " and t.fromObjectClassId || ':' || t.fromObjectId = ?) disOrder";

		List<Map<String, Object>> list = PersistHelper.getService().query(sqlOrder, taskOid);
		if (!list.isEmpty()) {
			Map<String, Object> map = list.get(0);
			String orderName = map.get("NAME").toString();
			String oid = map.get("CLASSID").toString() + ":" + map.get("INNERID").toString();
			disTask.setOrderName(orderName);
			disTask.setOrderOid(oid);
		}
		return disTask;
	}

	public List<DistributeInfo> getDistributeInfos(String distributePaperSignOid) {
		String innerId = Helper.getInnerId(distributePaperSignOid);
		String classId = Helper.getClassId(distributePaperSignOid);

		String sql = "SELECT A.* FROM DDM_DIS_INFO A, DDM_DIS_TASKINFOLINK B "
				+ "WHERE A.CLASSID = B.TOOBJECTCLASSID AND A.INNERID = B.TOOBJECTID "
				+ " AND B.FROMOBJECTID = ? AND B.FROMOBJECTCLASSID = ? ";

		List<DistributeInfo> infos = PersistHelper.getService().query(sql, DistributeInfo.class, innerId, classId);

		return infos;
	}

	public List<DistributeObject> getDistributeObjects(String distributePaperSignOid) {
		String innerId = Helper.getInnerId(distributePaperSignOid);
		String classId = Helper.getClassId(distributePaperSignOid);

		String sql = "SELECT DISTINCT C.* FROM DDM_DIS_INFO A, DDM_DIS_TASKINFOLINK B, DDM_DIS_ORDEROBJLINK C"
				+ " WHERE A.CLASSID = B.TOOBJECTCLASSID AND A.INNERID = B.TOOBJECTID"
				+ " AND A.DISORDEROBJLINKID = C.INNERID AND A.DISORDEROBJLINKCLASSID = C.CLASSID "
				+ " AND B.FROMOBJECTID = ? AND B.FROMOBJECTCLASSID = ?";

		List<DistributeOrderObjectLink> linkList = PersistHelper.getService().query(sql,
				DistributeOrderObjectLink.class, innerId, classId);
		List<DistributeObject> objList = new ArrayList<DistributeObject>();
		for (DistributeOrderObjectLink link : linkList) {
			DistributeObject disObject = (DistributeObject) link.getTo();
			disObject.setDistributeOrderObjectLink(link);
			objList.add(disObject);
		}
		return objList;
	}

	public List<DistributeObject> getDistributeObject(String distributePaperSignOid) {
		String innerId = Helper.getInnerId(distributePaperSignOid);
		String classId = Helper.getClassId(distributePaperSignOid);

		String sql = "SELECT DISTINCT {0} FROM DDM_DIS_INFO A, DDM_DIS_TASKINFOLINK B, DDM_DIS_ORDEROBJLINK C, DDM_DIS_OBJECT D "
				+ "WHERE A.CLASSID = B.TOOBJECTCLASSID AND A.INNERID = B.TOOBJECTID "
				+ "AND A.DISORDEROBJLINKID = C.INNERID AND A.DISORDEROBJLINKCLASSID = C.CLASSID "
				+ "AND C.TOOBJECTID = D.INNERID AND C.TOOBJECTCLASSID = D.CLASSID "
				+ " AND B.FROMOBJECTID = ? AND B.FROMOBJECTCLASSID = ?";

		String sql1 = sql.replace("{0}", "D.*,C.*");
		List<DistributeObject> objectList = PersistHelper.getService().query(sql1, DistributeObject.class, innerId,
				classId);

		sql1 = sql.replace("{0}", "C.*,D.*");
		List<DistributeOrderObjectLink> links = PersistHelper.getService().query(sql1, DistributeOrderObjectLink.class,
				innerId, classId);
		List<DistributeObject> objList = new ArrayList<DistributeObject>();
		for (int i = 0; i < objectList.size(); i++) {
			DistributeObject object = objectList.get(i);
			object.setDistributeOrderObjectLinkOid(links.get(i).getClassId() + ":" + links.get(i).getInnerId());
			objList.add(object);
		}

		return objList;
	}

	public void updateDistributeInfos(String oids, String notes) {
		List<String> oidsList = SplitString.string2List(oids, ";");
		List<String> notesList = SplitString.string2List(notes, ",");
		int length = oidsList.size();
		List<DistributeInfo> InfoList = new ArrayList<DistributeInfo>();
		for (int index = 0; index < length; index++) {
			String tmpOids = oidsList.get(index);
			String note = notesList.get(index);
			List<String> tmpOidList = SplitString.string2List(tmpOids, ",");

			for (int x = 0; x < tmpOidList.size(); x++) {
				String oid = tmpOidList.get(x);
				Persistable obj = Helper.getPersistService().getObject(oid);
				DistributeInfo disInfo = (DistributeInfo) obj;
				if (StringUtil.isNull(note)) {
					note = "";
				}
				disInfo.setNote(note);
				InfoList.add(disInfo);

			}
		}
		if (InfoList.size() > 0 || InfoList != null) {
			Helper.getPersistService().update(InfoList);
		}
	}

	/**
	 * 创建分发信息与纸质签收任务link对象。
	 * 
	 * @param disTask
	 *            DistributeObject
	 */
	private DistributeTaskInfoLink createDistributeTaskInfoLink(DistributeTask disTask, DistributeInfo disInfo) {

		DistributeTaskInfoLinkService service = DistributeHelper.getDistributeTaskInfoLinkService();

		DistributeTaskInfoLink linkDisObject = service.newDistributeTaskInfoLink();
		// 纸质任务内部标识
		linkDisObject.setFromObject(disTask);
		// 分发信息内部标识
		linkDisObject.setToObject(disInfo);
		// 任务类型（5：纸质签收任务）
		linkDisObject.setTaskType(ConstUtil.C_TASKYTPE_PAPERSIGN);
		
		return linkDisObject;
	}

	@Override
	public DistributeTaskInfoLink newDistributeTaskInfoLink() {
		DistributeTaskInfoLink disObj = (DistributeTaskInfoLink) PersistUtil
				.createObject(DistributeTaskInfoLink.CLASSID);
		return disObj;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.bjsasc.ddm.distribute.service.distributepapersigntask.
	 * DistributePaperSignTaskService#getDistributePaperSignTaskProperty(java.lang.String)
	 */
	public DistributePaperSignTask getDistributePaperSignTaskProperty(String oid) {
		Persistable obj = Helper.getPersistService().getObject(oid);
		DistributePaperSignTask dis = (DistributePaperSignTask) obj;
		String disPaperSignTaskInnerId = Helper.getPersistService().getInnerId(oid);
		String disPaperSignTaskClassId = Helper.getPersistService().getClassId(oid);

		String sql = "select DISOBJ.* from DDM_DIS_TASKINFOLINK t,DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink,DDM_DIS_OBJECT disObj"
				+ " where t.fromObjectId =? and t.fromObjectClassId =?"
				+ " and disInfo.innerId = t.toObjectId and disInfo.classId = t.toObjectClassId"
				+ " and objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
				+ " and disObj.innerId = objLink.toObjectId and disObj.classId = objLink.toObjectClassId";

		List<DistributeObject> list = PersistHelper.getService().query(sql, DistributeObject.class, disPaperSignTaskInnerId,
				disPaperSignTaskClassId);

		String sqlOrder = "select ORD.* from DDM_DIS_TASKINFOLINK t,DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink,DDM_DIS_ORDER ORD"
				+ " where t.fromObjectId = ? and t.fromObjectClassId = ?"
				+ " and disInfo.innerId = t.toObjectId and disInfo.classId = t.toObjectClassId"
				+ " and objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
				+ " and ORD.innerId = objLink.fromObjectId and ORD.classId = objLink.fromObjectClassId";

		List<DistributeOrder> orderList = PersistHelper.getService().query(sqlOrder, DistributeOrder.class,
				disPaperSignTaskInnerId, disPaperSignTaskClassId);

		if (list.size() > 0) {
			dis.setDataFrom(list.get(0).getDataFrom());
		}
		if (orderList.size() > 0) {
			dis.setOrderName(orderList.get(0).getName());
		}
		return dis;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.bjsasc.ddm.distribute.service.distributepapersigntask.
	 * DistributePaperSignTaskService
	 * #createDistributePaperSignTask(com.bjsasc.ddm.distribute
	 * .model.distributepapersigntask.DistributePaperSignTask)
	 */
	public void createDistributePaperSignTask(DistributePaperSignTask dis) {
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		// 生命周期初始化
		life.initLifecycle(dis);
		Helper.getPersistService().save(dis);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.bjsasc.ddm.distribute.service.distributepapersigntask.
	 * DistributePaperSignTaskService#newDistributePaperSignTask()
	 */
	public DistributePaperSignTask newDistributePaperSignTask() {
		DistributePaperSignTask dis = (DistributePaperSignTask) PersistUtil.createObject(DistributePaperSignTask.CLASSID);
		return dis;
	}


	/* (非 Javadoc)
	 * @see com.bjsasc.ddm.distribute.service.distributepapersigntask.
	 * DistributePaperSignTaskService#createDistributePaperSignTasks(java.util.Map)
	 */
	public void createDistributePaperSignTasks(Map<String, String> map) {
		
		String oids = StringUtil.getString(map.get("oids"));
		String taskOid[] = oids.split(",");
		String disInfoId = (String) map.get("disInfo");
		String prioid=(String)map.get("oid");
		String flag=(String)map.get("flag");
		String infoClassId="";
		
		List<User> userList = new ArrayList<User>();
		//存放权限数据的列表
		List<ACLData> allAclList = new ArrayList<ACLData>();

		if(!StringUtil.isNull(prioid)){
			disInfoId=Helper.getInnerId(prioid);
			infoClassId=Helper.getClassId(prioid);
		}
		Principal principal = null;
		if ("".equals(prioid)) {
			if ("1".equals(flag)) {//单位
				principal = OrganizationHelper.getService().getOrganization(disInfoId);
			}else if ("2".equals(flag)) {//人员
				principal = UserHelper.getService().getUser(disInfoId);
			}
			disInfoId=principal.getInnerId();
			infoClassId=principal.getClassId();
		}
		//用于存放需要存入数据库的TaskInfoLink对象
		List<DistributeTaskInfoLink> taskInfoLinkList = new ArrayList<DistributeTaskInfoLink>();

		for (String oid : taskOid) {
			String innerIdTemp = Helper.getInnerId(oid);
			String classIdTemp = Helper.getClassId(oid);

			// 发放单与分发数据link:DDM_DIS_ORDEROBJLINK
			String sql = "select DISTINCT objLink.* " + " from DDM_DIS_TASKINFOLINK taskInfoLink, "
					+ " DDM_DIS_INFO disInfo, DDM_DIS_ORDEROBJLINK objLink " + " where taskInfoLink.FROMOBJECTID =? "
					+ " and taskInfoLink.FROMOBJECTCLASSID =? " + " and taskInfoLink.TOOBJECTID =disInfo.INNERID "
					+ " and taskInfoLink.TOOBJECTCLASSID = disInfo.CLASSID "
					+ " and objLink.INNERID = disInfo.DISORDEROBJLINKID "
					+ " and objLink.CLASSID = disInfo.DISORDEROBJLINKCLASSID " + " and disInfo.disInfoId =? "
					+ " and disInfo.infoClassId =?  order by objLink.ISMASTER desc";

			List<DistributeOrderObjectLink> disOrdObjLinkList = PersistHelper.getService().query(sql,
					DistributeOrderObjectLink.class, innerIdTemp, classIdTemp, disInfoId, infoClassId);

			//针对同一个接收人的多个分发数据只创建一个纸质签收任务
			boolean disPaperSignTask_CreateFlag = true;
			DistributePaperSignTask disPaperSignTask = null;
			
			for (DistributeOrderObjectLink disOrdObjLink : disOrdObjLinkList) {
				// 发放单对象:DDM_DIS_ORDER
				DistributeOrder disOrder = (DistributeOrder) disOrdObjLink.getFrom();
				// 纸质任务的分发信息DDM_DIS_INFO
				sql = "select DISTINCT disInfo.* from DDM_DIS_INFO disInfo " + " where disInfo.DISORDEROBJLINKID = ? "
						+ "and disInfo.DISORDEROBJLINKCLASSID = ? " + " and disInfo.disInfoId = ? "
						+ " and disInfo.infoClassId = ? " + " and disInfo.DISMEDIATYPE = '" + ConstUtil.C_TASKTYPE_PAPER + "'";

				List<DistributeInfo> disInfoList = Helper.getPersistService().query(sql, DistributeInfo.class,
						disOrdObjLink.getInnerId(), disOrdObjLink.getClassId(), disInfoId, infoClassId);

				//针对同一个接收人/单位的多个分发数据的分发信息，分发类型是一样的
				DistributeInfo infoObj = disInfoList.get(0);
				
				//判断当前的发放信息是否已经创建了纸质签收任务，如果已经创建了则不再创建纸质签收任务。避免重复创建。
				String hql = "from DistributeTaskInfoLink t where t.toObjectRef.innerId=? and t.toObjectRef.classId=? and t.taskType=?";
				List<DistributeTaskInfoLink> links = PersistHelper.getService().find(hql, infoObj.getInnerId(),
						infoObj.getClassId(),ConstUtil.C_TASKYTPE_PAPERSIGN);
				if (links != null && links.size() > 0) {
					continue;
				}
				
				//创建纸质签收任务对象
				if(disPaperSignTask_CreateFlag){
					
					//判断分发类型，0为单位，1为人员
					if("1".equals(infoObj.getDisInfoType())){
						String sqliid = "select aauserinnerid  from  "
								+ "plm_principal_user "
								+ "where innerid=?";
						List<Map<String,Object>> list = Helper.getPersistService().query(sqliid,infoObj.getDisInfoId());
						User receiveUser = UserHelper.getService().getUser(list.get(0).get("AAUSERINNERID")+"");
						userList.add(receiveUser);
					}else{
						String sqluser = "SELECT DISTINCT t.aauserinnerid  "
								+ "from  plm_principal_user t ,"
								+ "plm_principal_org o, "
								+ "aagroup p,"
								+ "aauserdata d  ,"
								+ "aauserprincipal s  "
								+ "where p.id='second_sche' "
								+ "and o.innerid=? "
								+ "and o.aaorginnerid=d.userorg "
								+ "and s.userref=d.innerid  "
								+ "and p.innerid=s.principalvalue "
								+ "and d.innerid=t.aauserinnerid ";
						List<Map<String,Object>> list = Helper.getPersistService().query(sqluser,infoObj.getDisInfoId());
						for(Map<String,Object> aaMap :list){
							User receiveUser = UserHelper.getService().getUser(aaMap.get("AAUSERINNERID")+"");
							userList.add(receiveUser);
						}
					}
					
					//取得纸质任务对象
					Persistable taskobj = Helper.getPersistService().getObject(oid);
					DistributePaperTask disPaperTask = (DistributePaperTask) taskobj;
					
					disPaperSignTask = newDistributePaperSignTask();
					//设置纸质签收任务的编号、名称和备注
					disPaperSignTask.setNumber(disOrder.getNumber() + ConstUtil.C_PAPERSIGNTASK_STR);
					disPaperSignTask.setName(disOrder.getName() + ConstUtil.C_PAPERSIGNTASK_STR);
					disPaperSignTask.setNote("");
					//为纸质签收任务添加任务类型
					disPaperSignTask.setTaskType(ConstUtil.C_TASKYTPE_PAPERSIGN);
					//为纸质签收任务添加紧急度
					disPaperSignTask.setDisUrgent(disPaperTask.getDisUrgent());
					//为纸质签收任务添加上下文信息
					disPaperSignTask.setContextInfo(disOrder.getContextInfo());
					//为纸质签收任务添加域信息
					disPaperSignTask.setDomainInfo(disOrder.getDomainInfo());
					//设置生命周期信息并创建纸质签收任务对象
					createDistributePaperSignTask(disPaperSignTask);
					allAclList.addAll(getAllObjUserPrivilege(disPaperSignTask,userList));
					disPaperSignTask_CreateFlag = false;
				}
				//创建分发信息与纸质签收任务link对象
				taskInfoLinkList.add(createDistributeTaskInfoLink(disPaperSignTask,infoObj));
			}
		}
		
		//集中保存新建的link，提高任务效率
		Helper.getPersistService().save(taskInfoLinkList);
		//集中存储权限数据
		Helper.getPersistService().save(allAclList);
		ACLDataHelper.getService().afterUpdateAcls(allAclList);
		
	}

	/**
	 * 取得需要为用户赋予数据访问权限的列表
	 * 
	 * @param disTask 需要赋予权限的分发电子任务对象
	 * @param userList 需要赋予权限的用户
	 * @return 获得的权限列表
	 * @author zhangguoqiang 2014-09-11
	 */
	private List<ACLData> getAllObjUserPrivilege(DistributePaperSignTask disTask, List<User> userList) {
		DistributePrincipalService principalService = DistributeHelper.getDistributePrincipalService();
		//一个存储权限数据的列表
		List<ACLData> aclList = new ArrayList<ACLData>();
		String sql1 = "SELECT DISTINCT D.* "
				+ "FROM DDM_DIS_INFO A, "
				+ "DDM_DIS_TASKINFOLINK B, "
				+ "DDM_DIS_ORDEROBJLINK C, "
				+ "DDM_DIS_OBJECT D "
				+ "WHERE A.CLASSID = B.TOOBJECTCLASSID   "
				+ "AND A.INNERID = B.TOOBJECTID "
				+ "AND A.DISORDEROBJLINKID = C.INNERID "
				+ "AND A.DISORDEROBJLINKCLASSID = C.CLASSID "
				+ "AND C.TOOBJECTID = D.INNERID AND C.TOOBJECTCLASSID = D.CLASSID  "
				+ "AND B.FROMOBJECTID = ? "
				+ "AND B.FROMOBJECTCLASSID = ? ";

		List<DistributeObject> objList = PersistHelper.getService().query(sql1, DistributeObject.class, disTask.getInnerId(),
				disTask.getClassId());
		
		for (DistributeObject object : objList) {
			//取得业务对象域信息
			Domained domained = (Domained) PersistHelper.getService().getObject(
					object.getDataClassId() + ":" + object.getDataInnerId());

			for(User user : userList){
				boolean flag = AccessControlHelper.getService().hasEntityPermission(user, Operate.ACCESS, domained);
				if (flag == false) {
					//设置访问权限
					aclList.addAll(principalService.getPriviledgeData(domained,user));
				}
			}
		}
		return aclList;
	}
	
	@Override
	public List<DistributePaperSignTask> getDistributePaperSignTasksByDistributeOrderOid(String oid) {
		String sql = "SELECT distinct A .*, B.TASKTYPE "
				+ "FROM DDM_DIS_PAPERSIGNTASK A, DDM_DIS_TASKINFOLINK B, DDM_DIS_INFO C, DDM_DIS_ORDEROBJLINK D "
				+ "WHERE A .CLASSID = B.FROMOBJECTCLASSID AND A .INNERID = B.FROMOBJECTID "
				+ "AND B.TOOBJECTCLASSID = C.CLASSID AND B.TOOBJECTID = C.INNERID "
				+ "AND C.DISORDEROBJLINKCLASSID = D .CLASSID AND C.DISORDEROBJLINKID = D .INNERID"
				+ " AND D .FROMOBJECTCLASSID || ':' || D .FROMOBJECTID = ? " + "ORDER BY A.MODIFYTIME DESC";

		List<DistributePaperSignTask> list = PersistHelper.getService().query(sql, DistributePaperSignTask.class, oid);
		return list;
	}
	
	/*
	 * （非 Javadoc）
	 * 
	 * @see com.bjsasc.ddm.distribute.service.distributepapersigntask.
	 * DistributePaperSignTaskService#updateDistributePaperSignTask(java.lang.String,
	 * com.bjsasc.ddm.common.ConstUtil.LIFECYCLE_OPT)
	 */
	public void updateDistributePaperSignTask(String oids, LIFECYCLE_OPT opt, String returnReason) {
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		List<String> oidList = SplitString.string2List(oids, ",");
		for (String oid : oidList) {
			Persistable obj = Helper.getPersistService().getObject(oid);
			DistributePaperSignTask dis = (DistributePaperSignTask) obj;
			try {
				if (LIFECYCLE_OPT.PROMOTE == opt) {
					// 生命周期升级
					life.promoteLifeCycle(dis);
				} else if (LIFECYCLE_OPT.REJECT == opt) {
					// 生命周期拒绝
					life.rejectLifeCycle(dis);
				}
				User currentUser = SessionHelper.getService().getUser();
				dis.setReceiveBy(currentUser);
				dis.setReceiveByName(currentUser.getName());
				dis.setReceiveTime(System.currentTimeMillis());
				Helper.getPersistService().update(dis);
				if (LIFECYCLE_OPT.REJECT == opt) {
					// 添加回退信息
					ReturnReason retReason = newReturnReason();
					retReason.setTaskId(dis.getInnerId());
					retReason.setTaskClassId(dis.getClassId());
					retReason.setLifeCycleInfo(dis.getLifeCycleInfo());
					retReason.setReturnReason(returnReason);
					retReason.setUserId(currentUser.getInnerId());
					retReason.setUserClassId(currentUser.getClassId());
					retReason.setUserName(currentUser.getName());
					Helper.getPersistService().save(retReason);
				}
			} catch (Exception e) {
				throw new RuntimeException("生命周期升级并反馈失败！", e);
			}
		}
	}

	private ReturnReason newReturnReason() {
		ReturnReason reason = (ReturnReason) PersistUtil.createObject(ReturnReason.CLASSID);
		return reason;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.bjsasc.ddm.distribute.service.distributepapersigntask.
	 * DistributePaperSignTaskService
	 * #deleteDistributePaperSignTaskProperty(java.lang.String)
	 */
	public void deleteDistributePaperSignTask(String oid) {
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

	public void updateDistributePaperSignTaskLife(String oids) {
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		List<String> oidList = SplitString.string2List(oids, ",");
		List<DistributePaperSignTask> paperSignList = new ArrayList<DistributePaperSignTask>();
		for (String oid : oidList) {
			Persistable obj = Helper.getPersistService().getObject(oid);
			DistributePaperSignTask dis = (DistributePaperSignTask) obj;
			life.promoteLifeCycle(dis);
			paperSignList.add(dis);
		}
		if (paperSignList.size() > 0 || paperSignList != null) {
			Helper.getPersistService().update(paperSignList);
		}
	}

	public List<DistributePaperSignTask> getAllOrganizationPaperSigndistribute(Object stateName) {
		User currentUser = SessionHelper.getService().getUser();
		//String noRec = "未签收";
		String sql = " SELECT DISTINCT A.* FROM DDM_DIS_PAPERSIGNTASK A, DDM_DIS_TASKINFOLINK B,DDM_DIS_INFO C,AAUSERDATA D,"
				+ "AAUSERPRINCIPAL S, AAGROUP P, PLM_PRINCIPAL_ORG O  WHERE A.INNERID = B.FROMOBJECTID AND A.CLASSID = B.FROMOBJECTCLASSID "
				+ "AND C.INNERID = B.TOOBJECTID AND C.CLASSID = B.TOOBJECTCLASSID AND A.STATENAME = ? AND C.INFOCLASSID = 'Organization' "
				+ "AND O.INNERID = C.DISINFOID AND O.AAORGINNERID = D.USERORG AND D.INNERID = ? AND S.USERREF = ? AND P.INNERID = S.PRINCIPALVALUE AND S.USERREF = D.INNERID AND P.ID='second_sche' ";
		List<DistributePaperSignTask> taskList = Helper.getPersistService().query(sql, DistributePaperSignTask.class, stateName,
				currentUser.getAaUserInnerId(), currentUser.getAaUserInnerId());
		return taskList;
	}

	public DistributeOrder getDistributeOrderByPaperSignTaskOid(String oid) {
		String sql = "select ORD.* from DDM_DIS_TASKINFOLINK t,DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink,DDM_DIS_ORDER ORD"
				+ " where disInfo.innerId = t.toObjectId and disInfo.classId = t.toObjectClassId"
				+ " and objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
				+ " and ORD.innerId = objLink.fromObjectId and ORD.classId = objLink.fromObjectClassId"
				+ " and t.fromObjectClassId || ':' || t.fromObjectId = ?";
		List<DistributeOrder> list = Helper.getPersistService().query(sql, DistributeOrder.class, oid);
		DistributeOrder disOrder = list.get(0);
		return disOrder;
	}

}
