package com.bjsasc.ddm.distribute.service.distributeorder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bjsasc.adm.active.model.activedocument.ActiveDocument;
import com.bjsasc.adm.active.model.activeorder.ActiveOrder;
import com.bjsasc.adm.active.service.PutIntoStorageService;
import com.bjsasc.ddm.common.CollectReferenceObject;
import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.common.ConstUtil.LIFECYCLE_OPT;
import com.bjsasc.ddm.distribute.event.CreateDistributeOrderSucessEvent;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeelectask.DistributeElecTask;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.distributepapersigntask.DistributePaperSignTask;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.model.recdespapertask.RecDesPaperTask;
import com.bjsasc.ddm.distribute.model.returnreason.ReturnReason;
import com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService;
import com.bjsasc.ddm.distribute.service.distributeobjectnotautocreate.DistributeObjectNotAutoCreateService;
import com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService;
import com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService;
import com.bjsasc.ddm.distribute.service.distributetask.DistributeTaskService;
import com.bjsasc.ddm.distribute.service.recdesinfo.RecDesInfoService;
import com.bjsasc.ddm.transfer.helper.DdmDataTransferHelper;
import com.bjsasc.ddm.transfer.service.DdmDataTransferService;
import com.bjsasc.platform.objectmodel.business.lifeCycle.State;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.approve.ApproveOrder;
import com.bjsasc.plm.core.baseline.model.ManagedBaseline;
import com.bjsasc.plm.core.cad.CADDocument;
import com.bjsasc.plm.core.change.ECO;
import com.bjsasc.plm.core.change.PPCO;
import com.bjsasc.plm.core.change.TNO;
import com.bjsasc.plm.core.change.Variance;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.doc.Document;
import com.bjsasc.plm.core.event.EventHelper;
import com.bjsasc.plm.core.lifecycle.LifeCycleHelper;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.lifecycle.LifeCycleService;
import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.option.OptionHelper;
import com.bjsasc.plm.core.option.OptionValue;
import com.bjsasc.plm.core.part.Part;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.security.audit.AuditLogHelper;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.suit.ATSuit;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.system.principal.UserHelper;
import com.bjsasc.plm.core.type.TypeHelper;
import com.cascc.avidm.util.SplitString;
import com.cascc.platform.event.AbstractEvent;

/**
 * 发放单服务实现类。
 * 
 * @author gengancong 2013-2-22
 */
@SuppressWarnings({ "deprecation", "unchecked", "static-access" })
public class DistributeOrderServiceImpl implements DistributeOrderService, PutIntoStorageService {
	/** 生命周期服务 */
	private final DistributeLifecycleService lifeService = DistributeHelper.getDistributeLifecycleService();
	/** 分发信息服务 */
	private final DistributeInfoService infoService = DistributeHelper.getDistributeInfoService();
	/** 发放单与分发数据信息服务 */
	private final DistributeOrderObjectLinkService disLinkService = DistributeHelper
			.getDistributeOrderObjectLinkService();
	/** 分发数据服务 */
	private final DistributeObjectService disObjService = DistributeHelper.getDistributeObjectService(); 

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService#getAllDistributeOrder()
	 */
	@Override
	public List<DistributeOrder> getAllDistributeOrderByAuth(String stateName) {
		String hql = "from DistributeOrder t where t.lifeCycleInfo.stateName = ? order by t.manageInfo.modifyTime desc ";
		List<DistributeOrder> list = Helper.getPersistService().find(hql, stateName);
		return list;
	}
	
	public List<DistributeOrder> listDiffStatesDistributeOrder(String [] stateArray){
		
		String sql = "select t.* from DDM_DIS_ORDER t where t.stateName in(";
		String paramStr="";
		for (int i = 0; i < stateArray.length; i++) {
			paramStr+="'"+stateArray[i]+"',";
		}
		sql+=paramStr.substring(0,paramStr.length()-1);
		sql+=")  order by t.updateTime desc";
		
		List<DistributeOrder> list = PersistHelper.getService().query(sql, DistributeOrder.class);
		return list;
	}
	
	public List<DistributeOrder> listSendedDistributeOrderAdderSearchIuput(String [] stateArray,String keywords){
		keywords="%"+keywords+"%";
		String sql = "select t.* from DDM_DIS_ORDER t where t.stateName in(";
		String paramStr="";
		for (int i = 0; i < stateArray.length; i++) {
			paramStr+="'"+stateArray[i]+"',";
		}
		sql+=paramStr.substring(0,paramStr.length()-1);
		sql+=") and( t.id like ? or t.name like ?)  order by t.updateTime desc";
		List<DistributeOrder> list = PersistHelper.getService().query(sql, DistributeOrder.class,keywords,keywords);
		return list;
	}
	

	public List<DistributeOrder> listDiffStatesDistributeOrderOnload(String [] stateArray,long time,long currentTime){
		String sql = "select t.* from DDM_DIS_ORDER t where t.stateName in(";
		String paramStr="";
		for (int i = 0; i < stateArray.length; i++) {
			paramStr+="'"+stateArray[i]+"',";
		}
		sql+=paramStr.substring(0,paramStr.length()-1);
		sql+=") and createtime >=? and createtime<=?  order by t.updateTime desc";
		
		List<DistributeOrder> list = PersistHelper.getService().query(sql, DistributeOrder.class,time,currentTime);
		return list;
	}
	
	public List<DistributeOrder> listNotInStatesDistributeOrder(String [] notInStateArray){
		String sql = "select t.* from DDM_DIS_ORDER t where t.stateName not in(";
		String paramStr="";
		for (int i = 0; i < notInStateArray.length; i++) {
			paramStr+="'"+notInStateArray[i]+"',";
		}
		sql+=paramStr.substring(0,paramStr.length()-1);
		sql+=") order by t.updateTime desc";
		List<DistributeOrder> list = PersistHelper.getService().query(sql, DistributeOrder.class);
		return list;
	}

	public List<DistributeOrder> getRelatedDistributeOrder(Object oid) {
		String sql = " SELECT DISTINCT A .* FROM DDM_DIS_ORDER A, DDM_DIS_ORDEROBJLINK B, DDM_DIS_OBJECT C "
				+ " WHERE B.FROMOBJECTID = A .INNERID AND B.FROMOBJECTCLASSID = A .CLASSID "
				+ " AND B.TOOBJECTID = C.INNERID AND B.TOOBJECTCLASSID = C.CLASSID "
				+ " AND C.DATACLASSID || ':' || C.DATAINNERID = ? ORDER BY A.MODIFYTIME DESC";

		List<DistributeOrder> list = Helper.getPersistService().query(sql, DistributeOrder.class, oid);
		return list;
	}

	public List<DistributeOrder> getRelatetaskdDistributeOrder(String oid) {
		String innerId = Helper.getInnerId(oid);
		String classId = Helper.getClassId(oid);
		String sql="";
		if(classId.equals(DistributePaperTask.CLASSID) || classId.equals(DistributeElecTask.CLASSID)
				 || classId.equals(DistributePaperSignTask.CLASSID)){
			sql = "(select distinct a.* "
					+ "from ddm_dis_order a,"
					+ "ddm_dis_orderobjlink b,"
					+ "ddm_dis_info c,"
					+ "ddm_dis_taskinfolink d "
					+ "where d.fromobjectid=? "
					+ "and d.fromobjectclassid=? "
					+ "and d.toobjectid=c.innerid "
					+ "and d.toobjectclassid=c.classid "
					+ "and c.disorderobjlinkid=b.innerid  "
					+ "and c.disorderobjlinkclassid=b.classid "
					+ "and b.fromobjectid=a.innerid "
					+ "and b.fromobjectclassid=a.classid) "
					+ "UNION ALL"
					+ "(select distinct a.* from "
					+ "ddm_dis_order a,"
					+ "ddm_dis_orderobjlink b,"
					+ "ddm_dis_info c,"
					+ "ddm_dis_taskdomainlink d "
					+ "where d.fromobjectid=? "
					+ "and d.fromobjectclassid=? "
					+ "and d.toobjectid=c.innerid   "
					+ "and d.toobjectclassid=c.classid "
					+ "and c.disorderobjlinkid=b.innerid  "
					+ "and c.disorderobjlinkclassid=b.classid "
					+ "and b.fromobjectid=a.innerid "
					+ "and b.fromobjectclassid=a.classid)";
		}else if(classId.equals(RecDesPaperTask.CLASSID)){
			sql = "(select distinct a.* "
					+ "from ddm_dis_order a,"
					+ "ddm_dis_orderobjlink b,"
					+ "ddm_recdes_info c,"
					+ "ddm_dis_taskinfolink d "
					+ "where d.fromobjectid=? "
					+ "and d.fromobjectclassid=? "
					+ "and d.toobjectid=c.innerid "
					+ "and d.toobjectclassid=c.classid "
					+ "and c.disorderobjectlinkid=b.innerid  "
					+ "and c.disorderobjectlinkclassid=b.classid "
					+ "and b.fromobjectid=a.innerid "
					+ "and b.fromobjectclassid=a.classid) "
					+ "UNION ALL"
					+ "(select distinct a.* from "
					+ "ddm_dis_order a,"
					+ "ddm_dis_orderobjlink b,"
					+ "ddm_recdes_info c,"
					+ "ddm_dis_taskdomainlink d "
					+ "where d.fromobjectid=? "
					+ "and d.fromobjectclassid=? "
					+ "and d.toobjectid=c.innerid   "
					+ "and d.toobjectclassid=c.classid "
					+ "and c.disorderobjectlinkid=b.innerid  "
					+ "and c.disorderobjectlinkclassid=b.classid "
					+ "and b.fromobjectid=a.innerid "
					+ "and b.fromobjectclassid=a.classid)";
		}
		

		List<DistributeOrder> list = PersistHelper.getService().query(sql, DistributeOrder.class, innerId, classId,
				innerId, classId);
		return list;
	}

	/*
	 * （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService#getAllDistributeOrderReturn(java.lang.String)
	 */
	@Override
	public List<DistributeOrder> getAllDistributeOrderReturnByAuth(String stateName) {
		String orderSql = "select t.* from DDM_DIS_ORDER t where t.stateName =? order by t.updateTime desc";
		List<DistributeOrder> orderList = Helper.getPersistService().query(orderSql, DistributeOrder.class, stateName);
		List<DistributeOrder> list = new ArrayList<DistributeOrder>();
		for (DistributeOrder order : orderList) {
			String sql = "select * from DDM_DIS_RETURN where taskId =? and taskClassId = ? order by updateTime desc";
			List<ReturnReason> retList = Helper.getPersistService().query(sql, ReturnReason.class, order.getInnerId(),
					order.getClassId());
			if (retList.size() > 0) {
				order.setRetReason(retList.get(0));
			}
			list.add(order);
		}
		return orderList;
	}

	/*
	 * （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService#getDistributeOrderReturnDetail(java.lang.String, java.lang.String)
	 */
	@Override
	public List<DistributeOrder> getDistributeOrderReturnDetail(String stateName, String returnOid) {
		String sqlTask = "SELECT * FROM DDM_DIS_ORDER WHERE CLASSID || ':' || INNERID = ("
				+ " SELECT MAX (TASKCLASSID || ':' || TASKID) FROM DDM_DIS_RETURN "
				+ "WHERE CLASSID || ':' || INNERID = ? )";
		String sqlReturn = "SELECT * FROM DDM_DIS_RETURN WHERE TASKCLASSID || ':' || TASKID = ( "
				+ " SELECT MAX (TASKCLASSID || ':' || TASKID) FROM DDM_DIS_RETURN "
				+ " WHERE CLASSID || ':' || INNERID = ? ) " + "order by UPDATETIME DESC";
		List<DistributeOrder> ordList = Helper.getPersistService().query(sqlTask, DistributeOrder.class, returnOid);
		DistributeOrder order = ordList.get(0);
		List<ReturnReason> retList = Helper.getPersistService().query(sqlReturn, ReturnReason.class, returnOid);
		List<DistributeOrder> orderList = new ArrayList<DistributeOrder>();
		for (ReturnReason ret : retList) {
			DistributeOrder disOrder = order.cloneDisOrder();
			disOrder.setRetReason(ret);
			orderList.add(disOrder);
		}
		return orderList;

	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService#isCanCreateDistributeOrder(com.bjsasc.plm.core.persist.model.Persistable)
	 */
	public boolean isCanCreateDistributeOrder(Persistable target) {
		boolean flag = false;
		if(target instanceof Document){//文档
			flag = true;
		}else if(target instanceof CADDocument){//CAD文档
			flag = true;
		}else if(target instanceof Part){//部件
			flag = true;
		}else if(target instanceof ECO){//更改单
			flag = true;
		}else if(target instanceof TNO){//技术通知单
			flag = true;
		}else if(target instanceof Variance){//超差代料质疑单
			flag = true;
		}else if(target instanceof ApproveOrder){//送审单
			flag = true;
		}else if(target instanceof ATSuit){//套对象
			flag = true;
		}else if(target instanceof ManagedBaseline){//基线
			flag = true;
		}else if(target instanceof ActiveDocument){//现行文档
			flag = true;
		}else if(target instanceof ActiveOrder){//现行单据
			flag = true;
		}else if(target instanceof PPCO){//转阶段更改单
			flag = true;
		}
		return flag;
		
	}
	
	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService#createDistributeOrder(com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder)
	 */
	@Override
	public void createDistributeOrder(DistributeOrder dis) {
		Helper.getPersistService().save(dis);
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService#createDistributeOrder(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void createDistributeOrder(String number, String name, String orderType, String note) {
		DistributeOrder dis = newDistributeOrder();
		dis.setNumber(number);
		dis.setName(name);
		dis.setOrderType(orderType);
		dis.setNote(note);

		createDistributeOrder(dis);
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService#updateDistributeOrder(com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder)
	 */
	@Override
	public void updateDistributeOrder(DistributeOrder dis) {
		Helper.getPersistService().update(dis);
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService#updateDistributeOrder(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateDistributeOrder(String oid, String name, String note) {
		Persistable obj = Helper.getPersistService().getObject(oid);
		DistributeOrder dis = (DistributeOrder) obj;

		dis.setName(name);
		dis.setNote(note);
		updateDistributeOrder(dis);
		//编辑发放单记录日志
		Context context= dis.getContextInfo().getContext();
		List<String> objects = new ArrayList<String>();
		objects.add(SessionHelper.getService().getUser().getName());//用户名
		//objects.add("签收");
		objects.add(dis.getNumber());
		int level=1;
		String logType="module";
		String objName=dis.getName();
		int objectSecurity=0;
		String moduleSource="发放管理";
		String objectType="发放单";
		String operation="编辑业务对象";
		String messageId="ddm.log.editDistributeOrder";
		AuditLogHelper.getService().addLog( level, logType, context, 
				dis.getInnerId(), dis.getClassId(), objName, objectSecurity, 
				moduleSource, objectType, operation, messageId, objects);
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService#getDistributeOrderObjectLinksByOids(java.util.List)
	 */
	@Override
	public List<DistributeOrderObjectLink> getDistributeOrderObjectLinksByOids(List<String> distributeOrderOids) {

		List<DistributeOrderObjectLink> listAll = new ArrayList<DistributeOrderObjectLink>();
		for (String distributeOrderOid : distributeOrderOids) {

			String distributeOrderInnerId = Helper.getPersistService().getInnerId(distributeOrderOid);
			String distributeOrderClassId = Helper.getPersistService().getClassId(distributeOrderOid);

			String hql = "from DistributeOrderObjectLink t "
					+ "where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? ";

			List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, distributeOrderInnerId,
					distributeOrderClassId);

			hql = "from DistributeInfo t where t.disOrderObjLinkId=? and t.disOrderObjLinkClassId=?";
			if (links != null && links.size() > 0) {
				listAll.addAll(links);
			}
		}
		return listAll;
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService#newDistributeOrder()
	 */
	public DistributeOrder newDistributeOrder() {
		DistributeOrder dis = (DistributeOrder) PersistUtil.createObject(DistributeOrder.CLASSID);
		dis.setClassId(dis.CLASSID);
		return dis;
	}

	/*
	 * （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService#deleteDistributeOrderByOid(java.lang.String)
	 */
	@Override
	public void deleteDistributeOrderByOid(String distributeOrderOids) {
		List<String> oidList = SplitString.string2List(distributeOrderOids, ",");
		List<Persistable> perList = new ArrayList<Persistable>();

		for (String oid : oidList) {
			String distributeOrderInnerId = Helper.getPersistService().getInnerId(oid);
			String distributeOrderClassId = Helper.getPersistService().getClassId(oid);

			String hql = "from DistributeOrderObjectLink t "
					+ "where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? ";

			List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, distributeOrderInnerId,
					distributeOrderClassId);
			if (links != null && links.size() > 0) {
				DistributeObjectService objService = DistributeHelper.getDistributeObjectService();
				RecDesInfoService recDesInfoService = DistributeHelper.getRecDesInfoService();

				for (DistributeOrderObjectLink link : links) {

					//获取发放单
					DistributeOrder order = (DistributeOrder) link.getFrom();
					//判断发放单类型，进行删除操作
					if(ConstUtil.C_ORDERTYPE_0.equals(order.getOrderType()) || ConstUtil.C_ORDERTYPE_1.equals(order.getOrderType())){
						objService.deleteDistributeObjectByOid(oid, Helper.getOid(link.getClassId(), link.getInnerId()));
					} else if(ConstUtil.C_ORDERTYPE_2.equals(order.getOrderType()) || ConstUtil.C_ORDERTYPE_3.equals(order.getOrderType())) {
						//删除回收销毁单分发信息
						recDesInfoService.deleteDistributeRecDesInfoByOid( link);
					}
				}
			}
			Persistable order = Helper.getPersistService().getObject(oid);
			perList.add(order);

		}
		if (perList != null && perList.size() > 0) {
			Helper.getPersistService().delete(perList);
		}

	}

	/*
	 * （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService#updateDisOrderLifeCycle(java.lang.String，LIFECYCLE_OPT,java.lang.String)
	 */
	@Override
	public void updateDisOrderLifeCycle(String oids, LIFECYCLE_OPT opt, String returnReason) {
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		List<String> oidList = SplitString.string2List(oids, ",");
		User currentUser = SessionHelper.getService().getUser();
		for (String oid : oidList) {
			String innerId = Helper.getPersistService().getInnerId(oid);
			String classId = Helper.getPersistService().getClassId(oid);
			String sqlDisOrd = "select DISTINCT disOrd.* "
					+ " from DDM_DIS_TASKINFOLINK taskInfoLink, "
					+ " DDM_DIS_INFO disInfo, DDM_DIS_ORDEROBJLINK objLink, DDM_DIS_OBJECT disObj, DDM_DIS_ORDER disOrd "
					+ " where taskInfoLink.FROMOBJECTID = ? " + " and taskInfoLink.FROMOBJECTCLASSID = ? "
					+ " and taskInfoLink.TOOBJECTID =disInfo.INNERID "
					+ " and taskInfoLink.TOOBJECTCLASSID = disInfo.CLASSID "
					+ " and objLink.INNERID = disInfo.DISORDEROBJLINKID "
					+ " and objLink.CLASSID = disInfo.DISORDEROBJLINKCLASSID "
					+ " and objLink.FROMOBJECTID = disOrd.INNERID "
					+ " and objLink.FROMOBJECTCLASSID = disOrd.CLASSID ";

			List<DistributeOrder> orderList = Helper.getPersistService().query(sqlDisOrd, DistributeOrder.class,
					innerId, classId);
			String orderOid = Helper.getPersistService().getOid(orderList.get(0).getClassId(),
					orderList.get(0).getInnerId());
			Persistable obj = Helper.getPersistService().getObject(orderOid);
			DistributeOrder dis = (DistributeOrder) obj;
			try {
				if (LIFECYCLE_OPT.PROMOTE == opt) {
					// 生命周期升级
					life.promoteLifeCycle(dis);
				} else if (LIFECYCLE_OPT.DEMOTE == opt) {
					// 生命周期降级
					life.demoteLifeCycle(dis);
					
				} else if (LIFECYCLE_OPT.REJECT == opt) {
					// 生命周期拒绝
					life.rejectLifeCycle(dis);
					//退回打印加工任务记录日志
					Context context= dis.getContextInfo().getContext();
					List<String> objects = new ArrayList<String>();
					objects.add(SessionHelper.getService().getUser().getName());//用户名
					objects.add("退回打印加工任务");
					objects.add(dis.getNumber());
					int level=1;
					String logType="module";
					String objName=dis.getName();
					int objectSecurity=0;
					String moduleSource="发放管理";
					String objectType="分发任务";
					String operation="退回打印加工任务";
					String messageId="ddm.log.returnDistributeTask";
					AuditLogHelper.getService().addLog( level, logType, context, 
							dis.getInnerId(), dis.getClassId(), objName, objectSecurity, 
							moduleSource, objectType, operation, messageId, objects);
				}
				Helper.getPersistService().update(dis);
				if (LIFECYCLE_OPT.REJECT == opt) {
					DistributePaperTaskService taskService = DistributeHelper.getDistributePaperTaskService();
					taskService.deleteDistributePaperTaskProperty(oid);
					//添加回退信息
					int i = 1;
					String hql = " from ReturnReason r where r.taskId = ? and r.taskClassId = ?";
					List<ReturnReason> list = Helper.getPersistService().find(hql, orderList.get(0).getInnerId(),
							orderList.get(0).getClassId());
					if (list.size() > 0) {
						i = list.size() + 1;
					}
					ReturnReason retReason = newReturnReason();
					retReason.setTaskId(dis.getInnerId());
					retReason.setTaskClassId(dis.getClassId());
					retReason.setLifeCycleInfo(dis.getLifeCycleInfo());
					retReason.setReturnReason(returnReason);
					retReason.setReturnCount(i);
					retReason.setUserId(currentUser.getInnerId());
					retReason.setUserClassId(currentUser.getClassId());
					retReason.setUserName(currentUser.getName());
					Helper.getPersistService().save(retReason);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建回退理由对象。
	 * 
	 * @return ReturnReason
	 */
	private ReturnReason newReturnReason() {
		ReturnReason reason = (ReturnReason) PersistUtil.createObject(ReturnReason.CLASSID);
		return reason;
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService#createDistributeOrderAndObject(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)
	 */
	@Override
	public DistributeOrder createDistributeOrderAndObject(String number, String name, String orderType, String note,
			String oid, boolean aotuCreateFlag, String starterId) {

		// 发放单对象
		DistributeOrder disOrder = newDistributeOrder();
		// 系统自动创建的发放单，创建人应为审批流程的启动人
		if (starterId != null) {
			User user = UserHelper.getService().getUser(starterId);
			ManageInfo manageInfo = new ManageInfo();
			long currentTime = System.currentTimeMillis();
			manageInfo.setCreateBy(user);
			manageInfo.setModifyBy(user);
			manageInfo.setCreateTime(currentTime);
			manageInfo.setModifyTime(currentTime);
			disOrder.setManageInfo(manageInfo);
		}
		disOrder.setNumber(number);
		disOrder.setName(name);
		disOrder.setOrderType(orderType);
		disOrder.setNote(note);
		//lifeService.initLifecycle(disOrder);

		// 分发数据源对象(主对象)
		Persistable dataObject = Helper.getPersistService().getObject(oid);

		// 取得收集相关对象List
		List<Persistable> collectRefResList = CollectReferenceObject.collectRefObject(dataObject);
		List<Persistable> collectRefResListNew=new ArrayList<Persistable>();
		int flag=0;
		if (collectRefResList == null || collectRefResList.isEmpty()) {
			return null;
		}else{
			//按对象类型配置不自动发放
			if (starterId != null) {
			for(Persistable persistable : collectRefResList){
				DdmDataTransferService tranService = DdmDataTransferHelper.getDistributeObjectService();
				DistributeObject disObj= tranService.transferToDdmData(persistable);
				String type=TypeHelper.getService().getType(disObj.getDataClassId()).getName();
				Context contextOfType = disObj.getContextInfo().getContext();
				DistributeObjectNotAutoCreateService  disObjNACservice=(DistributeObjectNotAutoCreateService)DistributeHelper.getDistributeObjectNotAutoCreateService();
				List<String> typeNames=disObjNACservice.getContextType(contextOfType);
				for(String tName:typeNames){
					if(tName.equals(type)){
						flag=1;
						break;
					}	
				}
				if(flag!=1){
					collectRefResListNew.add(persistable);//如果配置中没有该类型，则添加
				}
				flag=0;  //重新设置成0
			}
			if(collectRefResListNew==null||collectRefResListNew.isEmpty()){ //如果没有符合的，则直接返回
					return null;
			}
		  }else{     //如果不是通过自动发放，则直接赋值，不进行判断
			  collectRefResListNew = collectRefResList;
		  }
	   }
		// 收集对象是更改单标识
		int collectFlag = CollectReferenceObject.getIsECOCollect();
		// 创建相关崔对象发放单与分发数据Link
		List<Map<String, String>> linkList = disLinkService.creteOrderObjectLink(disOrder, collectRefResListNew,
				aotuCreateFlag);

		// 是否追加默认分发信息标识
		//boolean addDefaultInfoFlag = infoService.addDefaultInfo(dataObject);目前需求是所有对象第一次分发都要进行默认分发信息追加
		
		// 创建分发信息
		//infoService.createInfo(disOrder.getOid(), linkList, collectFlag, addDefaultInfoFlag);
		infoService.createInfo(disOrder.getOid(), linkList, collectFlag, true);

		// 收集对象是更改单对象
		if (aotuCreateFlag && collectFlag == ConstUtil.COLLECTFLAG_1) {
			// 取得更改分发配置
			Context context = disOrder.getContextInfo().getContext();
			OptionValue dispatchValue = OptionHelper.getService().getOptionValue(context,
					"disMange_disFlowConfig_dispatch");
			// 更改分发配置"否"则提交后直接分发
			if ("false".equals(dispatchValue.getValue())) {
				DistributeTaskService taskService = DistributeHelper.getDistributeTaskService();
				//true为创建任务时更改发放单生命周期
				taskService.createDistributeTask(disOrder.getOid(), "true", null);
			}
		}
		AbstractEvent createDistributeOrderSucessEvent = EventHelper.getService().getEvent("LifeCycle", "createDistributeOrderSucessEvent"); 
		//定义一个事件
		CreateDistributeOrderSucessEvent cdose = (CreateDistributeOrderSucessEvent) createDistributeOrderSucessEvent;
		cdose.setDisOrder(disOrder);
		//发布事件
		EventHelper.getService().publishEvent("LifeCycle", cdose);

		return disOrder;
	}
	
	@Override
	public DistributeOrder createDistributeOrderAndObjectMulti(String number,String name, String orderType, String note, 
			String oids, boolean aotuCreateFlag,String starterId){

		List<String> oidList = SplitString.string2List(oids, ",");

		// 发放单对象
		DistributeOrder disOrder = newDistributeOrder();
		disOrder.setNumber(number);
		disOrder.setName(name);
		disOrder.setOrderType(orderType);
		disOrder.setNote(note);
		
		addDisObjectToDisOrder(disOrder,oidList);
		
		AbstractEvent createDistributeOrderSucessEvent = EventHelper.getService().getEvent("LifeCycle", "createDistributeOrderSucessEvent"); 
		//定义一个事件
		CreateDistributeOrderSucessEvent cdose = (CreateDistributeOrderSucessEvent) createDistributeOrderSucessEvent;
		cdose.setDisOrder(disOrder);
		//发布事件
		EventHelper.getService().publishEvent("LifeCycle", cdose);
		return disOrder;
	}


	public String updateOrderLifeCycle(String oid) {
		String flag = "dataFalse";

		String distributeOrderInnerId = Helper.getPersistService().getInnerId(oid);
		String distributeOrderClassId = Helper.getPersistService().getClassId(oid);

		String hql = "from DistributeOrderObjectLink t "
				+ "where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? ";

		List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, distributeOrderInnerId,
				distributeOrderClassId);
		if (!links.isEmpty()) {
			flag = "true";

			DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();

			Persistable obj = Helper.getPersistService().getObject(oid);
			DistributeOrder dis = (DistributeOrder) obj;

			// 补发分发时(单据类型(0发放单、1补发发放单))
			if (ConstUtil.C_ORDERTYPE_1.equals(dis.getOrderType())) {
				// 取得补发分发配置
				Context context = dis.getContextInfo().getContext();
				OptionValue value = OptionHelper.getService().getOptionValue(context,
						"disMange_disFlowConfig_againDispatch");
				OptionValue paperProcessingValue = OptionHelper.getService().getOptionValue(context,
						"disMange_disFlowConfig_whetherPaperProcessing");
				if ("false".equals(value.getValue())) {
					/*String sql = "SELECT A .* FROM DDM_DIS_INFO A, DDM_DIS_ORDEROBJLINK B "
							+ "WHERE A .DISORDEROBJLINKID = B.INNERID AND A .DISORDEROBJLINKCLASSID = B.CLASSID "
							+ "AND B.FROMOBJECTCLASSID || ':' || B.FROMOBJECTID = ?";*/
					//List<DistributeInfo> disInfoList = PersistHelper.getService().query(sql, DistributeInfo.class, oid);
					DistributeInfoService disInfoService = DistributeHelper.getDistributeInfoService();
					List<DistributeInfo> disInfoList= disInfoService.getDistributeInfosByDistributeOrderInnerId(distributeOrderInnerId);
					if (disInfoList.size() == 0) {
						// 补发发放单直接提交到分发中时,若分发信息为空，则不能提交
						flag = "infoFalse";
					} else {
						// 更新生命周期状态
						life.promoteLifeCycle(dis);
						Helper.getPersistService().update(dis);
						// 补发分发配置"否"则提交后直接分发
						DistributeTaskService taskService = DistributeHelper.getDistributeTaskService();
						//true为创建任务时更改发放单生命周期
						taskService.createDistributeTask(oid, "true", null);
						// 补发发放单是否进行纸质处理(根据首选项的配置决定是否开启)
						if ("false".equals(paperProcessingValue.getValue())) {
							taskService.setAllSended(oid);
						}
					}
				} else {
					// 补发分发配置"是"则提交后进行调度
					// 更新生命周期状态
					life.promoteLifeCycle(dis);
					Helper.getPersistService().update(dis);
				}
			} else {
				// 更新生命周期状态
				life.promoteLifeCycle(dis);
				Helper.getPersistService().update(dis);
				/** DistributeObjectService */
				DistributeObjectService service = DistributeHelper.getDistributeObjectService();
				List<DistributeObject> listDis = service.getDistributeObjectsByDistributeOrderOid(oid);
				boolean ecoFlag = false;
				for (DistributeObject disObj : listDis) {
					Persistable disObject = (Persistable) disObj.getDistributeData().getObject();
					if (disObject instanceof ECO) {
						ecoFlag = true;
						break;
					}
				}
				// 分发对象是更改单
				if (ecoFlag) {
					// 取得更改分发配置
					Context context = dis.getContextInfo().getContext();
					OptionValue value = OptionHelper.getService().getOptionValue(context,
							"disMange_disFlowConfig_dispatch");
					if ("false".equals(value.getValue())) {
						// 更改分发配置"否"则提交后直接分发
						DistributeTaskService taskService = DistributeHelper.getDistributeTaskService();
						//true为创建任务时更改发放单生命周期
						taskService.createDistributeTask(oid, "true", null);
					}
				}

			}
		}
		Persistable obj = Helper.getPersistService().getObject(oid);
		DistributeOrder dis = (DistributeOrder) obj;
		Context context = dis.getContextInfo().getContext();
		List<String> objects = new ArrayList<String>();
		objects.add(SessionHelper.getService().getUser().getName());//用户名
		objects.add("提交");
		objects.add(dis.getNumber());
		objects.add("调度中");
		int level=1;
		String logType="module";
		String objName=dis.getName();
		int objectSecurity=0;
		String moduleSource="发放管理";
		String objectType="发放单";
		String operation="提交到调度";
		String messageId="ddm.log.submit";
		AuditLogHelper.getService().addLog( level, logType, context, distributeOrderInnerId, distributeOrderClassId, objName, objectSecurity, moduleSource, objectType, operation, messageId, objects);
		return flag;
	}

	public String getExistDistributeElecTask(String orderOid) {
		String existFlag = "false";
		String sql = "select elec.*"
				+ " from DDM_DIS_ORDEROBJLINK link,DDM_DIS_INFO info,DDM_DIS_TASKINFOLINK taskInfo,DDM_DIS_ELECTASK elec"
				+ " where link.classId || ':' || link.innerId = info.disOrderObjLinkClassId || ':' || info.disOrderObjLinkId"
				+ " and info.classId || ':' || info.innerId = taskInfo.toObjectClassId || ':' || taskInfo.toObjectId"
				+ " and elec.classId || ':' || elec.innerId = taskInfo.fromObjectClassId || ':' || taskInfo.fromObjectId"
				+ " and link.fromObjectClassId || ':' || link.fromObjectId = ?";
		List<DistributeElecTask> list = Helper.getPersistService().query(sql, DistributeElecTask.class, orderOid);
		if (!list.isEmpty()) {
			existFlag = "true";
		}
		return existFlag;
	}

	@Override
	public List<DistributeOrder> getDistributeOrderByOrdStateAndDataOid(String orderOid, String dataOid) {
		String state = ConstUtil.LC_DISTRIBUTED.getName();
		String sql = " SELECT DISTINCT A .* FROM DDM_DIS_ORDER A, DDM_DIS_ORDEROBJLINK B, DDM_DIS_OBJECT C "
				+ " WHERE A .CLASSID || ':' || A .INNERID !=? AND A.STATENAME !=?"
				+ " AND B.FROMOBJECTCLASSID || ':' || B.FROMOBJECTID=A .CLASSID || ':' || A .INNERID "
				+ " AND B.TOOBJECTCLASSID || ':' || B.TOOBJECTID=C.CLASSID || ':' || C.INNERID"
				+ " AND C.DATACLASSID || ':' || C.DATAINNERID = ? ";
		List<DistributeOrder> list = Helper.getPersistService().query(sql, DistributeOrder.class, orderOid, state,
				dataOid);
		return list;
	}

	/** 直接入库处理 */
	public String putIntoStorage(String oid) {

		// 分发对象服务
		DistributeObjectService disObjService = DistributeHelper.getDistributeObjectService();
		List<DistributeObject> disObjList = disObjService.getDistributeObjectsByDataOid(oid);
		DistributeObject disObject;
		String orderType;
		String number;
		String name;
		String flag = "fail";
		if (disObjList == null || disObjList.isEmpty()) {
			//"发放单"
			orderType = ConstUtil.C_ORDERTYPE_0;
			// 数据转换服务
			DdmDataTransferService tranService = DdmDataTransferHelper.getDistributeObjectService();
			Persistable obj = Helper.getPersistService().getObject(oid);
			disObject = tranService.transferToDdmData(obj);
			number = disObject.getNumber() + ConstUtil.C_DISTRIBUTEORDER_0;
			name = disObject.getName() + ConstUtil.C_DISTRIBUTEORDER_0;
		} else {
			return "putError";//已经入库
		}
		DistributeOrder disOrder = createDisOrderAndObject(number, name, orderType, "", oid, false);
		if (null != disOrder) {
			LifeCycleService service = LifeCycleHelper.getService();
			State state = ConstUtil.LC_DISTRIBUTED;
			LifeCycleManaged lifeCycleManagedDisOrder = (LifeCycleManaged) disOrder;
			LifeCycleManaged lifeCycleManage = service.setLifeCycleState(lifeCycleManagedDisOrder, state);//将发放单的对象设置为"已分发"状态
			if (null != lifeCycleManage) {
				flag = "success";//success
			} else {
				flag = "fail";//fail
			}
		} else {
			flag = "createError";//现行套中无受控或已发放分发对象
		}
		return flag;
	}

	@Override
	public DistributeOrder createDisOrderAndObject(String number, String name, String orderType, String note,
			String oid, boolean aotuCreateFlag) {

		// 发放单对象
		DistributeOrder disOrder = newDistributeOrder();

		disOrder.setNumber(number);
		disOrder.setName(name);
		disOrder.setOrderType(orderType);
		disOrder.setNote(note);
		//lifeService.initLifecycle(disOrder);

		// 分发数据源对象(主对象)
		Persistable dataObject = Helper.getPersistService().getObject(oid);

		// 取得收集相关对象List
		List<Persistable> collectRefResList = CollectReferenceObject.collectRefObject(dataObject);

		if (collectRefResList == null || collectRefResList.isEmpty()) {
			return null;
		}
		// 创建相关崔对象发放单与分发数据Link
		List<Map<String, String>> linkList = disLinkService.creteOrderObjectLink(disOrder, collectRefResList,
				aotuCreateFlag);
		for (Map<String, String> link : linkList) {
			String linkOid = link.get("LINKOID").toString();
			LifeCycleManaged lifeCycleManagedDisOrder = (LifeCycleManaged) Helper.getPersistService()
					.getObject(linkOid);
			LifeCycleService service = LifeCycleHelper.getService();
			State state = ConstUtil.LC_COMPLETED;
			LifeCycleManaged lifeCycleManage = service.setLifeCycleState(lifeCycleManagedDisOrder, state);//将分发对象设置为"完成"状态
			if (null == lifeCycleManage) {
				return null;
			}
		}

		return disOrder;
	}
	/**
	 * 补发分发单-选中其他分发单直接进行补发
	 * @return DistributeOrder
	 */
	
	public DistributeOrder  reissueDistributeOrder (String  orderOidsStr,String number, String name, String orderType, String note){
		
		List<DistributeObject> arrayList=new ArrayList<DistributeObject>();
		
		//将其他发放单的发放数据统一到一个Set集合里
		List<String> orderOidList = SplitString.string2List(orderOidsStr, ",");
		for (String orderOid : orderOidList) {
			
			List<DistributeObject> disObjectList= disObjService.getDistributeObjectsByDistributeOrderOid(orderOid);	
			for (DistributeObject distributeObject : disObjectList) {
				
				Persistable persist = Helper.getPersistService().getObject(Helper.getOid(distributeObject.getDataClassId(), distributeObject.getDataInnerId()));
				//更改单不加入补发单中
				if (persist instanceof ECO) {
					continue;
				}
				List<DistributeOrderObjectLink> link=disLinkService.getDistributeOrderObjectLinkListByDistributeObjectOid(Helper.getOid(distributeObject));
				//将主对象标识放进distributeObject的 DisplayName属性里面
				distributeObject.setDisplayName(link.get(0).getIsMaster());
				arrayList.add(distributeObject);
			}
		}
		//创建发放单
		
		DistributeOrder disOrder = newDistributeOrder();
		
		disOrder.setNumber(number);
		disOrder.setName(name);
		disOrder.setOrderType(orderType);
		disOrder.setNote(note);
		
		lifeService.initLifecycle(disOrder);
		
		disOrder.setContextInfo(arrayList.get(0).getContextInfo());
		// 设置发放单域信息
		disOrder.setDomainInfo(arrayList.get(0).getDomainInfo());
		
		createDistributeOrder(disOrder);
		
		
		//创建与发放单关联的DistributeOrderObjectLink
		for (DistributeObject distributeObject : arrayList) {
			disObjService.createDistributeOrderObjectLink(disOrder, distributeObject, distributeObject.getDisplayName());
		}
		
		return disOrder;
		
	}
	
	public String getDefaultDistributeOrderType(String oid) {
		String classId = "";
		String distributeOrderInnerId = Helper.getPersistService().getInnerId(oid);
		String distributeOrderClassId = Helper.getPersistService().getClassId(oid);
		String hql = "from DistributeOrderObjectLink t "
				+ "where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? and t.isMaster = '1'";

		List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, distributeOrderInnerId,
				distributeOrderClassId);
		if(!links.isEmpty()){
			DistributeOrderObjectLink dol = links.get(0);
			Persistable to = dol.getTo();
			if (to instanceof DistributeObject) {
				DistributeObject distributeObject = (DistributeObject)to;
				if (distributeObject != null) {
					classId = distributeObject.getDataClassId();
				}
			}
		}
		return classId;
	}

	@Override
	public void addDisObjectToDisOrder(DistributeOrder order, List<String> disObjOidList) {
		for (String dataOid : disObjOidList) {
			List<Persistable> collectNewList = new ArrayList<Persistable>();
			//先判断是否已在发放单中存在
			List<DistributeObject> disObjList = disObjService.getDistributeObjectsByOrdOidAndDataOid(order.getOid(),dataOid);
			if (disObjList != null && disObjList.size() > 0) {
				continue;
			}
			Persistable dataObject = Helper.getPersistService().getObject(dataOid);
			List<Persistable> collectRefResList = CollectReferenceObject.collectRefObject(dataObject);
			int collectFlag = CollectReferenceObject.getIsECOCollect();
			for(Persistable obj : collectRefResList){
				List<DistributeObject> dObjList = disObjService.getDistributeObjectsByOrdOidAndDataOid(order.getOid(),Helper.getOid(obj));
				if (dObjList != null && dObjList.size() > 0) {
					continue;
				}else{
					collectNewList.add(obj);
				}
			}
			// 创建相关分发对象发放单与分发数据Link
			List<Map<String, String>> linkList = disLinkService.creteOrderObjectLink(order, collectNewList, false);
			// 创建分发信息
			infoService.createInfo(order.getOid(), linkList, collectFlag, true);
		}
	}
}
