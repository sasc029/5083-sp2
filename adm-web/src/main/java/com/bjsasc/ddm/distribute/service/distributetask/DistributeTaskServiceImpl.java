package com.bjsasc.ddm.distribute.service.distributetask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bjsasc.avidm.core.extinter.exp.ExportFileHelper;
import com.bjsasc.avidm.core.site.Site;
import com.bjsasc.avidm.core.site.SiteHelper;
import com.bjsasc.avidm.core.site.constant.DTSiteConstant;
import com.bjsasc.avidm.core.transfer.TransferObjectHelper;
import com.bjsasc.avidm.core.transfer.TransferObjectService;
import com.bjsasc.avidm.core.transfer.util.TransferConstant;
import com.bjsasc.ddm.common.A3A5DataConvertUtil;
import com.bjsasc.ddm.common.ConstLifeCycle;
import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.common.ConstUtil.LIFECYCLE_OPT;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.disinfoistrack.DisInfoIsTrack;
import com.bjsasc.ddm.distribute.model.distributeelectask.DistributeElecTask;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.model.distributetask.DistributeTask;
import com.bjsasc.ddm.distribute.model.distributetaskdomainlink.DistributeTaskDomainLink;
import com.bjsasc.ddm.distribute.model.distributetaskinfolink.DistributeTaskInfoLink;
import com.bjsasc.ddm.distribute.model.distributetasksite.DistributeTaskSite;
import com.bjsasc.ddm.distribute.model.recdesinfo.RecDesInfo;
import com.bjsasc.ddm.distribute.model.recdespapertask.RecDesPaperTask;
import com.bjsasc.ddm.distribute.model.returnreason.ReturnReason;
import com.bjsasc.ddm.distribute.service.disinfoistrack.DisInfoIsTrackService;
import com.bjsasc.ddm.distribute.service.distributeelectask.DistributeElecTaskService;
import com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService;
import com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService;
import com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService;
import com.bjsasc.ddm.distribute.service.distributeprincipal.DistributePrincipalService;
import com.bjsasc.ddm.distribute.service.distributetaskinfolink.DistributeTaskInfoLinkService;
import com.bjsasc.ddm.distribute.service.recdespapertask.RecDesPaperTaskService;
import com.bjsasc.platform.objectmodel.business.lifeCycle.State;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.collaboration.a4x.util.Message4XTypeConstant;
import com.bjsasc.plm.collaboration.config.constant.BusinessTypeConstant;
import com.bjsasc.plm.collaboration.config.constant.MessageTypeconstant;
import com.bjsasc.plm.collaboration.config.model.DmcRole;
import com.bjsasc.plm.collaboration.config.model.DmcRoleUser;
import com.bjsasc.plm.collaboration.config.model.ImportVersionCfg;
import com.bjsasc.plm.collaboration.config.service.DmcConfigHelper;
import com.bjsasc.plm.collaboration.config.service.DmcConfigService;
import com.bjsasc.plm.collaboration.config.service.DmcRoleHelper;
import com.bjsasc.plm.collaboration.config.service.ImportVersionCfgHelper;
import com.bjsasc.plm.collaboration.site.model.DCSiteAttribute;
import com.bjsasc.plm.collaboration.site.service.DCSiteAttributeHelper;
import com.bjsasc.plm.core.cad.CADDocument;
import com.bjsasc.plm.core.change.Change;
import com.bjsasc.plm.core.change.ChangedItem;
import com.bjsasc.plm.core.change.ECO;
import com.bjsasc.plm.core.change.TNO;
import com.bjsasc.plm.core.change.Variance;
import com.bjsasc.plm.core.change.VarianceReq;
import com.bjsasc.plm.core.change.WaivedItem;
import com.bjsasc.plm.core.change.link.ChangedLink;
import com.bjsasc.plm.core.change.link.IncludeInLink;
import com.bjsasc.plm.core.change.link.VariancedInLink;
import com.bjsasc.plm.core.change.link.VariancedLink;
import com.bjsasc.plm.core.change.link.WaivedInLink;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.model.Contexted;
import com.bjsasc.plm.core.context.util.ContextUtil;
import com.bjsasc.plm.core.doc.Document;
import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.lifecycle.LifeCycleHelper;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.lifecycle.LifeCycleService;
import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.part.Part;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.security.audit.AuditLogHelper;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.access.AccessControlHelper;
import com.bjsasc.plm.core.system.access.operate.Operate;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.system.principal.UserHelper;
import com.bjsasc.plm.core.util.StringUtil;
import com.cascc.avidm.util.SplitString;
import com.cascc.platform.uuidservice.UUID;

/**
 * 分发任务服务实现类。
 * 
 * @author gengancong 2013-2-22
 */
@SuppressWarnings({"unchecked","deprecation"})
public class DistributeTaskServiceImpl implements DistributeTaskService {
	
	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * com.bjsasc.ddm.distribute.service.distributetask.DistributeTaskService
	 * #getDistributeTasksByDistributeOrderOid(java.lang.String)
	 */
	@Override
	public List<DistributePaperTask> getDistributePaperTasksByDistributeOrderOid(String oid) {
		String sql = "SELECT distinct A .*, B.TASKTYPE "
				+ "FROM DDM_DIS_PAPERTASK A, DDM_DIS_TASKINFOLINK B, DDM_DIS_INFO C, DDM_DIS_ORDEROBJLINK D "
				+ "WHERE A .CLASSID = B.FROMOBJECTCLASSID AND A .INNERID = B.FROMOBJECTID "
				+ "AND B.TOOBJECTCLASSID = C.CLASSID AND B.TOOBJECTID = C.INNERID "
				+ "AND C.DISORDEROBJLINKCLASSID = D .CLASSID AND C.DISORDEROBJLINKID = D .INNERID"
				+ " AND D .FROMOBJECTCLASSID || ':' || D .FROMOBJECTID = ? " + "ORDER BY A.MODIFYTIME DESC";

		List<DistributePaperTask> list = PersistHelper.getService().query(sql, DistributePaperTask.class, oid);
		List<DistributePaperTask> taskList = new ArrayList<DistributePaperTask>();
		if (list.size() > 0) {
			for (DistributePaperTask disPaperTask : list) {
				String stateName = disPaperTask.getLifeCycleInfo().getStateName();
				if(ConstUtil.LC_PROCESSING_BACKOFF.getName().equals(stateName)
						|| ConstUtil.LC_DUPLICATE_BACKOFF.getName().equals(stateName)){
					String paperTaskInnerId = disPaperTask.getInnerId();
					String paperTaskClassId = disPaperTask.getClassId();
					String sqlReturn = "select * from DDM_DIS_RETURN where objectId is null and objectClassId is null and stateId = ? and taskId = ? and taskClassId = ? order by updatetime desc";
					List<ReturnReason> retList = Helper.getPersistService().query(sqlReturn, ReturnReason.class,
							disPaperTask.getLifeCycleInfo().getStateId(), paperTaskInnerId, paperTaskClassId);
					disPaperTask.setReturnReason(retList.get(0));
					taskList.add(disPaperTask);
				}else{
					taskList.add(disPaperTask);
				}
			}
		}
		return taskList;
	}

	@Override
	public List<DistributeElecTask> getDistributeElecTasksByDistributeOrderOid(String oid) {
		String sql = "SELECT distinct A .*, B.TASKTYPE "
				+ "FROM DDM_DIS_ELECTASK A, DDM_DIS_TASKINFOLINK B, DDM_DIS_INFO C, DDM_DIS_ORDEROBJLINK D "
				+ "WHERE A .CLASSID = B.FROMOBJECTCLASSID AND A .INNERID = B.FROMOBJECTID "
				+ "AND B.TOOBJECTCLASSID = C.CLASSID AND B.TOOBJECTID = C.INNERID "
				+ "AND C.DISORDEROBJLINKCLASSID = D .CLASSID AND C.DISORDEROBJLINKID = D .INNERID"
				+ " AND D .FROMOBJECTCLASSID || ':' || D .FROMOBJECTID = ? " + "ORDER BY A.MODIFYTIME DESC";

		List<DistributeElecTask> list = PersistHelper.getService().query(sql, DistributeElecTask.class, oid);
		List<DistributeElecTask> taskList = new ArrayList<DistributeElecTask>();
		if (list.size() > 0) {
			for (DistributeElecTask disElecTask : list) {
					String elecTaskInnerId = disElecTask.getInnerId();
					String elecTaskClassId = disElecTask.getClassId();
					String sqlDisInfo = "select * from DDM_DIS_TASKINFOLINK L, DDM_DIS_INFO I"
							+ " where L.TOOBJECTID = I.INNERID and L.FROMOBJECTID = ? and L.FROMOBJECTCLASSID = ?";
					List<DistributeInfo> disInfoList = Helper.getPersistService().query(sqlDisInfo, DistributeInfo.class,
							elecTaskInnerId, elecTaskClassId);
					disElecTask.setDisInfoName(disInfoList.get(0).getDisInfoName());
					taskList.add(disElecTask);
				}
			}
		return taskList;
		}
	
	/* (non-Javadoc)
	 * @see com.bjsasc.ddm.distribute.service.distributetask.DistributeTaskService#setAllSended(java.lang.String)
	 */
	public void setAllSended(String disOrderOid){
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		//DistributePaperTask dis = (DistributePaperTask) obj;
		DistributeTaskService taskService = DistributeHelper.getDistributeTaskService();
		List<DistributePaperTask> distributePaperTask =taskService.getDistributePaperTasksByDistributeOrderOid(disOrderOid);//getDistributePaperTasksByDistributeOrderOid(disOrderOid);
		List<DistributeInfo> infoList1 = new ArrayList<DistributeInfo>();
		DistributeInfoService disInfoService = DistributeHelper.getDistributeInfoService();
		DistributeOrderObjectLinkService disOrderObjLinkService = DistributeHelper.getDistributeOrderObjectLinkService();//.getDistributeInfoService();
		//将分发信息及分发数据及发放单均变为已完成
		for (DistributePaperTask dis:distributePaperTask){
			//通过纸质任务的innerid和classid获取分发信息
			List<DistributeInfo> infos= disInfoService.getDistributeInfosByDistributePaperTaskOID(dis.getOid());
			for (DistributeInfo info : infos) {
				//将纸质任务下的所有分发信息设置为已分发
				if (ConstUtil.LC_NOT_DISTRIBUT.getName().equals(info.getLifeCycleInfo().getStateName())) {
					life.promoteLifeCycle(info);
					info.setSendTime(System.currentTimeMillis());
					infoList1.add(info);
				}
			}
			if (infoList1 != null && infoList1.size() > 0) {
				Helper.getPersistService().update(infoList1);
			}
			List<DistributeOrderObjectLink> objLinkList = 
					disOrderObjLinkService.getDistributeOrderObjectLinkListByDisPaperTaskOID(dis.getOid());
			List<DistributeOrderObjectLink> linkList = new ArrayList<DistributeOrderObjectLink>();
			for (DistributeOrderObjectLink objLink : objLinkList) {
				if (ConstUtil.LC_DISTRIBUTING.getName().equals(objLink.getLifeCycleInfo().getStateName())) {
					life.promoteLifeCycle(objLink);
					linkList.add(objLink);
				}
			}
			if (linkList != null || linkList.size() != 0) {
				Helper.getPersistService().update(linkList);
			}
			//将纸质任务设置成已分发
			life.updateLifeCycleByStateId(dis,ConstUtil.LC_DISTRIBUTED.getId());
			Helper.getPersistService().update(dis);
			Persistable orderObj = Helper.getPersistService().getObject(disOrderOid);
			DistributeOrder disOrder = (DistributeOrder) orderObj;
			//最后将发放单设置为已分发
			life.promoteLifeCycle(disOrder);
			Helper.getPersistService().update(disOrder);
		}
	}

	public void createDistributeTask(String distributeOrderOids, String flag, String modify){
		/*
		 * 方法内变量命名区域
		 */
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		//外域分发信息标志（发放单中无外域分发信息）
		boolean outFlag = false;
		//布尔值，true是分发单，false是回收销毁单
		boolean isDisOrRec = true;
		//sql语句，选择分发信息
		String sqlForDis = "SELECT A .* "
				+ "FROM DDM_DIS_INFO A, "
				+ "DDM_DIS_ORDEROBJLINK B "
				+ "WHERE A .DISORDEROBJLINKID = B.INNERID "
				+ "AND A .DISORDEROBJLINKCLASSID = B.CLASSID "
				+ "AND B.FROMOBJECTCLASSID || ':' || B.FROMOBJECTID = ?";
		//sql语句，选择回收销毁信息
		String sqlForRecDes = "SELECT A .* "
				+ "FROM DDM_RECDES_INFO A, "
				+ "DDM_DIS_ORDEROBJLINK B "
				+ "WHERE A .DISORDEROBJECTLINKID = B.INNERID "
				+ "AND A .DISORDEROBJECTLINKCLASSID = B.CLASSID "
				+ "AND B.FROMOBJECTCLASSID || ':' || B.FROMOBJECTID = ?";
		//sql语句，用于筛选disUrgent（是否加急）
		String disUrgSql = "SELECT MAX (LINK .DISURGENT) AS DISURGENT "
				+ "FROM DDM_DIS_ORDER ORD, "
				+ "DDM_DIS_ORDEROBJLINK LINK "
				+ "WHERE LINK .fromObjectClassId || ':' || LINK .fromObjectId "
				+ "= ORD .classId || ':' || ORD .innerId "
				+ "AND ORD .classId || ':' || ORD .innerId = ?";
		//分发单（回收销毁单）的OID列表
		List<String> distributeOrderOidList = SplitString.string2List(distributeOrderOids, ",");
		
		/*
		 * 逻辑方法实现区域
		 */
		boolean userSecrityFlag = ContextUtil.isEnableUserSecrityController();

		for(String distributeOrderOid:distributeOrderOidList){
			/*
			 * 方法内变量命名区域
			 */
			Persistable object = Helper.getPersistService().getObject(distributeOrderOid);
			//更新发放单的生命周期状态
			DistributeOrder disOrderObj = (DistributeOrder)object;
			//发放信息列表
			List<DistributeInfo> disInfoList = new ArrayList<DistributeInfo>();
			//发往外域的分发信息列表
			List<DistributeInfo> outSiteDisInfoList = new ArrayList<DistributeInfo>();
			//回收销毁信息列表
			List<RecDesInfo> recDesInfoList = new ArrayList<RecDesInfo>();
			//提取发放单的类型，用于判断是分发单还是回收销毁单
			String orderType = disOrderObj.getOrderType();
			//纸质任务标识，默认为false
			boolean paperTaskFlag = false;
			//回收销毁纸质任务标识，默认为false
			boolean recDesPaperTaskFlag = false;
			//站点oid列表
			List<String> siteList = new ArrayList<String>();
			//从数据库中获取指定oid的加急信息
			List<Map<String,Object>> linkList = PersistHelper.getService().query(disUrgSql,distributeOrderOid);
			String disUrgent = linkList.get(0).get("DISURGENT").toString();
			//判断是否有纸质任务,0为无，1为有
			int havePaper=0;
			/*
			 * 逻辑方法实现区域
			 */
			//进行order类型判断，查看是分发任务还是回收销毁任务。
			//(分发任务，isDisOrRec为true；回收销毁任务，isDisOrRec为false)
			if(orderType.equals(ConstUtil.C_ORDERTYPE_0) ||
					orderType.equals(ConstUtil.C_ORDERTYPE_1)){
				isDisOrRec = true;
			}else if(orderType.equals(ConstUtil.C_ORDERTYPE_2) ||
					orderType.equals(ConstUtil.C_ORDERTYPE_3)){
				isDisOrRec = false;
			}
			
			//判断发放单提交人，如果是走工作流则取工作流的启动人，如果不走工作流则取当前用户。
			if(StringUtil.isNull(modify)){
				User currentUser = SessionHelper.getService().getUser();
				disOrderObj.getManageInfo().setModifyBy(currentUser);
			}else{
				User user = UserHelper.getService().getUser(modify);
				disOrderObj.getManageInfo().setModifyBy(user);
			}
			Helper.getPersistService().update(disOrderObj);
			
			if("true".equals(flag)){
				//发放单生命周期升级（发放单提交生成任务，不走工作流）
				life.promoteLifeCycle(disOrderObj);
			}
			//添加发起人，发起站点（跨域）
			this.setOrderUserInfo(disOrderObj);
			
			//分发单生成分发任务的方法
			if(isDisOrRec){
				disInfoList = PersistHelper.getService().query(sqlForDis,DistributeInfo.class,distributeOrderOid);
				if(disInfoList.size() == 0 ){
					throw new RuntimeException(disOrderObj.getName()+"没有分发信息，添加分发信息后提交发放");
				}
				boolean canDisFlag = DistributeHelper.getDistributeInfoService().canDisBySecurityLevel(distributeOrderOid);
				if(!canDisFlag){
					throw new RuntimeException("分发数据的密级高于分发人员的密级，不能提交发放");
				}
				
				//遍历分发信息，给纸质任务做标记
				for(DistributeInfo disInfo:disInfoList){
					//更改单或技术通知单的分发进行跟踪处理
					this.addTraceInfoForECOAndTNO(disInfo);
					
					//只创建域内任务
					if(ConstUtil.C_TASKTYPE_OUTSIGN.equals(disInfo.getDisMediaType())){
						//外站点list（一个发放单发往站点个数
						String siteOid = disInfo.getInfoClassId()+":"+disInfo.getDisInfoId();
						if(!siteList.contains(siteOid)){
							siteList.add(siteOid);
						}
						//发放单中存在外域分发信息
						outSiteDisInfoList.add(disInfo);
						outFlag = true;
						continue;
					}
					if(ConstUtil.C_TASKTYPE_PAPER.equals(disInfo.getDisMediaType())){
						//为纸质任务标签赋值为真
						paperTaskFlag = true;
						havePaper = 1;
						continue;
					}else if(ConstUtil.C_TASKTYPE_ELEC.equals(disInfo.getDisMediaType())){
						//电子任务
						createDistributeElecTask(disInfo,distributeOrderOid, disOrderObj, life);
					}
				}
			}
			//回收销毁单生成回收销毁任务的方法 
			else{
				recDesInfoList = PersistHelper.getService().query(sqlForRecDes,RecDesInfo.class,distributeOrderOid);
				if(recDesInfoList.size() == 0 ){
					throw new RuntimeException(disOrderObj.getName()+"没有回收销毁信息，添加回收销毁信息后提交发放");
				}
				
				//遍历回收销毁信息，给纸质任务做标记
				for(RecDesInfo recDesInfo:recDesInfoList){				
					if(ConstUtil.C_TASKTYPE_PAPER.equals(recDesInfo.getDisMediaType())){
						//为回收销毁纸质任务标签赋值为真
						recDesPaperTaskFlag = true;
						havePaper = 1;
						continue;
					}
				}
			}

			if(outFlag == true){
				sendToOutSite(disOrderObj, outSiteDisInfoList, siteList,false);
			}
			if(havePaper == 0){
				LifeCycleService service = LifeCycleHelper.getService();
				State state = ConstUtil.LC_DISTRIBUTED;
				LifeCycleManaged lifeCycleManagedDisOrder = (LifeCycleManaged) disOrderObj;
				service.setLifeCycleState(lifeCycleManagedDisOrder, state);//将发放单的对象设置为"已分发"状态
				//life.promoteLifeCycle(disOrderObj);//将发放单的对象设置为"已分发"状态
			}
			DistributeObjectService disObjService = DistributeHelper.getDistributeObjectService();
			DistributeInfoService disInfoService = DistributeHelper.getDistributeInfoService();
			List<DistributeObject> listDis = disObjService.getDistributeObjectsByDistributeOrderOid(distributeOrderOid);
			boolean infoFlag = true;
			String sent = ConstUtil.LC_DISTRIBUTED.getName();
			for (DistributeObject disObject : listDis) {
				List<DistributeInfo> infoList = disInfoService.getDistributeInfosByOid(disOrderObj.getClassId()+":"+disOrderObj.getInnerId(),disObject.getClassId()+":"+disObject.getInnerId());
				for (DistributeInfo info : infoList) {
					if (!sent.equals(info.getLifeCycleInfo().getStateName())) {
						//分发数据对应的分发信息未全部发送
						infoFlag = false;
						break;
					}
				}
				//分发数据生命周期升级(分发信息全部发送)
				if (infoFlag == true) {
					String hql = "from DistributeOrderObjectLink t "
							+ "where t.toObjectRef.innerId=? and t.toObjectRef.classId=? and t.fromObjectRef.innerId=? ";
					
					List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, disObject.getInnerId(),
							disObject.getClassId(), disOrderObj.getInnerId());
					List<DistributeOrderObjectLink> linkList2 = new ArrayList<DistributeOrderObjectLink>();
					for (DistributeOrderObjectLink link : links) {
						if (ConstUtil.LC_DISTRIBUTING.getName().equals(link.getLifeCycleInfo().getStateName())) {
							life.promoteLifeCycle(link);
							linkList2.add(link);
						}
					}
					if (linkList2.size() != 0 || linkList2 == null) {
						Helper.getPersistService().update(linkList2);
					}
				}
				infoFlag = true;
			}
			//分发纸质任务
			if(paperTaskFlag){
				createDistributePaperTask(disOrderObj, disUrgent, disInfoList);
				Persistable obj = Helper.getPersistService().getObject(distributeOrderOid);
				DistributeOrder dis = (DistributeOrder) obj;
				Context context = dis.getContextInfo().getContext();
				List<String> objects = new ArrayList<String>();
				objects.add(SessionHelper.getService().getUser().getName());//用户名
				objects.add("提交");
				objects.add(dis.getNumber());
				objects.add("加工单");
				int level=1;
				String logType="module";
				String objName=dis.getName();
				int objectSecurity=0;
				String moduleSource="发放管理";
				String objectType="发放单";
				String operation="提交到加工单";
				String messageId="ddm.log.submit";
				AuditLogHelper.getService().addLog( level, logType, context, dis.getInnerId(), dis.getClassId(), objName, objectSecurity, moduleSource, objectType, operation, messageId, objects);
			}
			
			//回收销毁纸质任务
			if(recDesPaperTaskFlag){
				createRecDesPaperTask(disOrderObj, disUrgent, recDesInfoList,life);
			}
			
		}
	}
	
	/**
	 * 根据给定的分发信息，判断，如果分发数据是ECO或TNO，为其添加追踪信息
	 * 
	 * @param disInfo 用于抽取各种信息的分发信息（分发数据，追踪信息）
	 * @modify Sun Zongqing
	 */
	private void addTraceInfoForECOAndTNO(DistributeInfo disInfo){
		DistributeOrderObjectLink disOrderObjLink = (DistributeOrderObjectLink)Helper.getPersistService()
				.getObject(disInfo.getDisOrderObjLinkClassId()+":"+disInfo.getDisOrderObjLinkId());
		//获得与分发单绑定的分发数据
		DistributeObject disObject = (DistributeObject)disOrderObjLink.getTo();
		//分发数据所指代的数据原型
		Persistable disObj = Helper.getPersistService().getObject(disObject.getDataClassId()+":"+disObject.getDataInnerId());
		if(disObj instanceof ECO || disObj instanceof TNO){
			//更改单货技术通知单的分发进行跟踪处理
			DisInfoIsTrackService service = DistributeHelper.getDisInfoIsTrackService();
			//通过disInfoOid获得追踪信息，返回list的size只有1
			List<DisInfoIsTrack> disInfoIsTrackList = service.getDisInfoIsTrackByDisInfoOid(disInfo.getOid());
			if(disInfoIsTrackList.size() !=0){
				//用到的变量
				Persistable department = null;
				Site site = null;
				String deptName = "";
				Change change = (Change) disObj;
				int mediaType = Integer.parseInt(disInfo.getDisMediaType());
				
				//域内分发
				if(mediaType == 0||mediaType == 1){
					if(ConstUtil.C_DISINFOTYPE_ORG.equals(disInfo.getDisInfoType())){
						department = Helper.getPersistService().
								getObject(disInfo.getInfoClassId()+":"+disInfo.getDisInfoId());
						deptName = disInfo.getDisInfoName();
					}
					//域内分发给人员时，给接口传人员主要隶属单位和单位名
					else{
						User user = (User)Helper.getPersistService().getObject(disInfo.getInfoClassId()+":"+disInfo.getDisInfoId());
						department = user.getOrganization();
						deptName = user.getOrganizationName();
					}
				}
				//跨域分发时，给接口传人员所在的站点和站点名称
				else if(mediaType == 2){
					site = (Site) SiteHelper
							.getSiteService()
							.findSiteById(disInfo.getDisInfoId());
					deptName = disInfo.getDisInfoName();
				}
				int isTrack = disInfoIsTrackList.get(0).getIstrack();
				Helper.getChangeTraceService().addTraceInfo(change, department, site, deptName, isTrack, mediaType);
			}
		}
	}
	
	/**
	 * 根据提供的参数，创建全属性的电子分发任务，包括重复分发电子任务的检测
	 * 
	 * @param disInfo 需要与电子任务关联的分发信息
	 * @param distributeOrderOid 分发单的oid，用于检测是否有该用户的分发信息
	 * @param disOrderObj 需要创建电子任务的分发单，用于获取编号、名称、域和上下文信息，然后赋值给电子任务
	 * @param life 分发任务生命周期服务，用于改变分发信息的生命周期
	 * @modify zhang guoqiang
	 * @date 2014/10/28
	 */
	private void createDistributeElecTask(DistributeInfo disInfo, String distributeOrderOid, DistributeOrder disOrderObj,DistributeLifecycleService life){
		
		//自动回避电子任务被再次提交
		//取出该分发信息对应的taskInfoLink里面的全部信息
		String hql = "from DistributeTaskInfoLink t "
				+ "where t.toObjectRef.innerId=? "
				+ "and t.toObjectRef.classId=?";
		List<DistributeTaskInfoLink> links = PersistHelper.getService().find(hql, disInfo.getInnerId(),
				disInfo.getClassId());
		if(links == null || links.isEmpty()){
			//sql语句，用于获取电子任务的信息
			String taskSql = "SELECT T.* FROM "
					+ "DDM_DIS_TASKINFOLINK L, "
					+ "DDM_DIS_ELECTASK T, "
					+ "DDM_DIS_INFO F, "
					+ "DDM_DIS_ORDEROBJLINK LINK "
					+ "WHERE T.INNERID = L.FROMOBJECTID "
					+ "AND T.CLASSID = L.FROMOBJECTCLASSID "
					+ "AND L.TOOBJECTID = F.INNERID "
					+ "AND L.TOOBJECTCLASSID = F.CLASSID "
					+ "AND F.DISORDEROBJLINKID = LINK.INNERID "
					+ "AND F.DISORDEROBJLINKCLASSID = LINK.CLASSID "
					+ "AND F.DISINFOID = ? "
					+ "AND F.INFOCLASSID = ? "
					+ "AND F.DISMEDIATYPE = ? "
					+ "AND T.STATENAME = ? "
					+ "AND LINK.FROMOBJECTCLASSID || ':' || LINK.FROMOBJECTID = ?";
			//存在该用户的电子信息
			List<DistributeElecTask> taskList = PersistHelper.getService()
							.query(taskSql, 
									DistributeElecTask.class,
									disInfo.getDisInfoId(),
									disInfo.getInfoClassId(),
									"1",
									ConstUtil.LC_NOT_SIGNED.getName(),
									distributeOrderOid);
			//发给此用户的电子任务存在(添加Link)
			if(taskList.size()>0){
				createDistributeTaskInfoLink(taskList.get(0), disInfo);
				//分发信息添加发送时间，生命周期升级
				disInfo.setSendTime(System.currentTimeMillis());
				if(ConstUtil.LC_NOT_DISTRIBUT.getName().equals(disInfo.getLifeCycleInfo().getStateName())){
					life.promoteLifeCycle(disInfo);
				}
				Helper.getPersistService().update(disInfo);
				setUserPrivilege(disInfo);
			}else{
				//获取电子任务分发服务，并创建新的电子任务
				DistributeElecTaskService service = DistributeHelper.getDistributeElecTaskService();
				DistributeElecTask disElecTask = service.newDistributeElecTask();
				//设置电子任务的编号、名称和备注
				disElecTask.setNumber(disOrderObj.getNumber()+ConstUtil.C_ELECTASK_STR);
				disElecTask.setName(disOrderObj.getName()+ConstUtil.C_ELECTASK_STR);
				disElecTask.setNote("");
				//电子任务增加“接收单位/人员”的属性信息，增加三个字段DISINFONAME,DISINFOID,INFOCLASSID，从分发信息中获取这个值 
				disElecTask.setDisInfoId(disInfo.getDisInfoId());
				disElecTask.setDisInfoName(disInfo.getDisInfoName());
				disElecTask.setInfoClassId(disInfo.getClassId());
				
				//为电子任务添加上下文信息
				disElecTask.setContextInfo(disOrderObj.getContextInfo());
				//为电子任务添加域信息
				disElecTask.setDomainInfo(disOrderObj.getDomainInfo());
				//为电子任务添加管理信息
				ManageInfo manageInfo = new ManageInfo();
				long currentTime = System.currentTimeMillis();
				manageInfo.setCreateBy(disOrderObj.getManageInfo().getModifyBy());
				manageInfo.setModifyBy(disOrderObj.getManageInfo().getModifyBy());
				manageInfo.setCreateTime(currentTime);
				manageInfo.setModifyTime(currentTime);
				disElecTask.setManageInfo(manageInfo);
				//为电子任务赋予生命周期信息
				service.createDistributeElecTask(disElecTask);
				//更新分发信息的发送时间
				disInfo.setSendTime(System.currentTimeMillis());
				
				if(ConstUtil.LC_NOT_DISTRIBUT.getName().equals(disInfo.getLifeCycleInfo().getStateName())){
					life.promoteLifeCycle(disInfo);
				}
				Helper.getPersistService().update(disInfo);
				
				//创建分发信息与电子任务link对象。
			
				createDistributeTaskInfoLink(disElecTask, disInfo);
				setUserPrivilege(disInfo);
			}
		}
	}

	/**
	 * 根据提供的参数，创建全属性的纸质分发任务
	 * 
	 * @param disOrderObj 需要创建纸质任务的分发单，用于获取编号、名称、域和上下文信息，然后赋值给纸质任务
	 * @param disUrgent 加急状态参数
	 * @param recDesInfoList 回收销毁信息列表，用于与纸质任务建立连接(link)
	 * @param life 提供生命周期服务的变量
	 * @return 新生成的TaskInfoLink列表
	 * @author Sun Zongqing
	 */
	private void createRecDesPaperTask(DistributeOrder disOrderObj,String disUrgent,List<RecDesInfo> recDesInfoList,DistributeLifecycleService life){
		RecDesPaperTaskService service = DistributeHelper.getRecDesPaperTaskService();
		//创建一个新的纸质任务
		RecDesPaperTask paperTask = service.newRecDesPaperTask();
		//为纸质任务赋值，包括编号、名称、备注、加急状况、上下文信息、域信息和生命周期信息。
		paperTask.setNumber(disOrderObj.getNumber()+ConstUtil.C_PAPERTASK_STR);
		paperTask.setName(disOrderObj.getName()+ConstUtil.C_PAPERTASK_STR);
		paperTask.setNote("");
		paperTask.setDisUrgent(disUrgent);
		paperTask.setContextInfo(disOrderObj.getContextInfo());
		paperTask.setDomainInfo(disOrderObj.getDomainInfo());
		//为纸质任务添加管理信息
		ManageInfo manageInfo = new ManageInfo();
		long currentTime = System.currentTimeMillis();
		manageInfo.setCreateBy(disOrderObj.getManageInfo().getModifyBy());
		manageInfo.setModifyBy(disOrderObj.getManageInfo().getModifyBy());
		manageInfo.setCreateTime(currentTime);
		manageInfo.setModifyTime(currentTime);
		paperTask.setManageInfo(manageInfo);
		//为纸质任务添加生命周期信息
		service.setRecDesPaperTaskLifecycle(paperTask);;
		
		for(RecDesInfo recDesInfo:recDesInfoList){
			if(ConstUtil.C_TASKTYPE_PAPER.equals(recDesInfo.getDisMediaType())){
				//创建分发信息与纸质任务的link对象
				createRecDesTaskInfoLink(paperTask, recDesInfo);
				recDesInfo.setSendTime(System.currentTimeMillis());
				life.promoteLifeCycle(recDesInfo);
			}
		}
	}
	
	/**
	 * 根据提供的参数，创建全属性的纸质分发任务
	 * 
	 * @param disOrderObj 需要创建纸质任务的分发单，用于获取编号、名称、域和上下文信息，然后赋值给纸质任务
	 * @param disUrgent 加急状态参数
	 * @param disInfoList 分发信息列表，用于与纸质任务建立连接(link)
	 * @return 新生成的TaskInfoLink对象列表
	 * @modify Sun Zongqing
	 */
	private void createDistributePaperTask(DistributeOrder disOrderObj,String disUrgent,List<DistributeInfo> disInfoList){
		//用处存储新产生的任务信息连接对象
		DistributePaperTaskService service = DistributeHelper.getDistributePaperTaskService();
		//创建一个新的纸质任务
		DistributePaperTask paperTask = service.newDistributePaperTask();
		//为纸质任务赋值，包括编号、名称、备注、加急状况、上下文信息、域信息和生命周期信息。
		paperTask.setNumber(disOrderObj.getNumber()+ConstUtil.C_PAPERTASK_STR);
		paperTask.setName(disOrderObj.getName()+ConstUtil.C_PAPERTASK_STR);
		paperTask.setNote("");
		paperTask.setDisUrgent(disUrgent);
		paperTask.setContextInfo(disOrderObj.getContextInfo());
		paperTask.setDomainInfo(disOrderObj.getDomainInfo());
		paperTask.setManageInfo(disOrderObj.getManageInfo());
		//为纸质任务添加管理信息
		ManageInfo manageInfo = new ManageInfo();
		long currentTime = System.currentTimeMillis();
		manageInfo.setCreateBy(disOrderObj.getManageInfo().getModifyBy());
		manageInfo.setModifyBy(disOrderObj.getManageInfo().getModifyBy());
		manageInfo.setCreateTime(currentTime);
		manageInfo.setModifyTime(currentTime);
		paperTask.setManageInfo(manageInfo);
		paperTask.setDisOrderCreator(disOrderObj.getCreateByName());
		//为纸质任务添加生命周期信息
		service.createDistributePaperTask(paperTask);
		for(DistributeInfo disInfo:disInfoList){
			if(ConstUtil.C_TASKTYPE_PAPER.equals(disInfo.getDisMediaType())){
				createDistributeTaskInfoLink(paperTask, disInfo);
			}
		}
	}
	
	/**
	 * 为参数中的分发单提供User信息
	 * 
	 * @param disOrderObj 需要添加User信息的分发单
	 * @modify Sun Zongqing
	 */
	private void setOrderUserInfo(DistributeOrder disOrderObj){
		Site localSite = SiteHelper.getSiteService().findLocalSiteInfo();
		if(localSite!=null){
			User currentUser = SessionHelper.getService().getUser();
			disOrderObj.setSubmitUserId(currentUser.getInnerId());
			disOrderObj.setSubmitUserClassId(currentUser.getClassId());
			disOrderObj.setSubmitUserName(currentUser.getName());
			disOrderObj.setSiteId(localSite.getInnerId());
			disOrderObj.setSiteName(localSite.getSiteData().getSiteName());
			disOrderObj.setSiteClassId(localSite.getClassId());
			Helper.getPersistService().update(disOrderObj);
		}
		
	}
	
	/**
	 * 创建分发信息与纸质任务link对象。
	 * 
	 * @param disTask
	 *            DistributeObject
	 */
	private void createDistributeTaskInfoLink(DistributeTask disTask, DistributeInfo disInfo) {

		DistributeTaskInfoLinkService service = DistributeHelper.getDistributeTaskInfoLinkService();

		DistributeTaskInfoLink linkDisObject = service.newDistributeTaskInfoLink();
		// 纸质任务内部标识
		linkDisObject.setFromObject(disTask);
		// 分发信息内部标识
		linkDisObject.setToObject(disInfo);
		// 任务类型（0：纸质任务，1：电子任务,3:回收销毁纸质任务,4:回收销毁电子任务）
		linkDisObject.setTaskType(getTaskType(disTask));
		service.createDistributeTaskInfoLink(linkDisObject);
	}
	
	/**
	 * 创建分发信息与纸质任务link对象。
	 * 
	 * @param disTask
	 *            DistributeObject
	 */
	private void createRecDesTaskInfoLink(DistributeTask disTask, RecDesInfo disInfo) {

		DistributeTaskInfoLinkService service = DistributeHelper.getDistributeTaskInfoLinkService();

		DistributeTaskInfoLink linkDisObject = service.newDistributeTaskInfoLink();
		// 纸质任务内部标识
		linkDisObject.setFromObject(disTask);
		// 分发信息内部标识
		linkDisObject.setToObject(disInfo);
		// 任务类型（0：纸质任务，1：电子任务,3:回收销毁纸质任务,4:回收销毁电子任务）
		linkDisObject.setTaskType(getTaskType(disTask));
		service.createDistributeTaskInfoLink(linkDisObject);
	}
	
	/**
	 * 建立分发任务与分发信息的链接
	 * 
	 * @param disTask 需要建立连接的Task
	 * @param disInfo 需要建立连接的info列表
	 * @return 新产生的TaskInfoLink对象
	 * @author Sun Zongqing
	 * @date 2014/7/3
	 */
	private DistributeTaskInfoLink getDistributeTaskInfoLinkObject(DistributeTask disTask, DistributeInfo disInfo) {

		DistributeTaskInfoLinkService service = DistributeHelper.getDistributeTaskInfoLinkService();

		DistributeTaskInfoLink linkDisObject = service.newDistributeTaskInfoLink();
		// 纸质任务内部标识
		linkDisObject.setFromObject(disTask);
		// 分发信息内部标识
		linkDisObject.setToObject(disInfo);
		// 任务类型（0：纸质任务，1：电子任务,3:回收销毁纸质任务,4:回收销毁电子任务）
		linkDisObject.setTaskType(getTaskType(disTask));
		return linkDisObject;
	}
	
	/**
	 * 根据提供的任务和信息，创建任务信息的连接
	 * 
	 * @param disTask 需要创建连接的任务
	 * @param disInfo 需要创建连接的信息
	 * @return 新生成的任务信息链接对象
	 * @author Sun Zongqing
	 */
	private DistributeTaskInfoLink getRecDesTaskInfoLinkObject(DistributeTask disTask, RecDesInfo disInfo) {

		DistributeTaskInfoLinkService service = DistributeHelper.getDistributeTaskInfoLinkService();

		DistributeTaskInfoLink linkDisObject = service.newDistributeTaskInfoLink();
		// 纸质任务内部标识
		linkDisObject.setFromObject(disTask);
		// 分发信息内部标识
		linkDisObject.setToObject(disInfo);
		// 任务类型（0：纸质任务，1：电子任务,3:回收销毁纸质任务,4:回收销毁电子任务）
		linkDisObject.setTaskType(getTaskType(disTask));
		return linkDisObject;
	}

	
	/**
	 * 取得任务的类型
	 * 
	 * @param disObject 需要获取任务类型的任务参数
	 * @return 得到的任务类型
	 * @modify	Sun Zongqing
	 */
	private String getTaskType(DistributeTask disObject) {
		String taskType = "0";
		if (disObject instanceof DistributePaperTask) {
			taskType = "0";
		} else if (disObject instanceof DistributeElecTask) {
			taskType = "1";
		}else if(disObject instanceof RecDesPaperTask){
			taskType = "3";
		}
		return taskType;
	}

	public void updateDistributeTask(String oids, LIFECYCLE_OPT opt, String returnReason) {
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		List<String> oidList = SplitString.string2List(oids, ",");
		User currentUser = SessionHelper.getService().getUser();
		List<DistributeInfo> infoList1 = new ArrayList<DistributeInfo>();
		for (String oid : oidList) {
			Persistable obj = Helper.getPersistService().getObject(oid);
			DistributePaperTask dis = (DistributePaperTask) obj;
			try {
				if (LIFECYCLE_OPT.PROMOTE == opt) {
					// 生命周期升级
					life.promoteLifeCycle(dis);
					//提交打印加工任务记录日志
					Context context= dis.getContextInfo().getContext();
					List<String> objects = new ArrayList<String>();
					objects.add(SessionHelper.getService().getUser().getName());//用户名
					objects.add("提交打印加工任务");
					objects.add(dis.getNumber());
					int level=1;
					String logType="module";
					String objName=dis.getName();
					int objectSecurity=0;
					String moduleSource="发放管理";
					String objectType="分发任务";
					String operation="提交打印加工任务";
					String messageId="ddm.log.updateDistributeTask";
					AuditLogHelper.getService().addLog( level, logType, context, 
							dis.getInnerId(), dis.getClassId(), objName, objectSecurity, 
							moduleSource, objectType, operation, messageId, objects);
				} else if (LIFECYCLE_OPT.DEMOTE == opt) {
					// 生命周期降级
					life.demoteLifeCycle(dis);
				} else if (LIFECYCLE_OPT.REJECT == opt) {
					// 生命周期拒绝
					life.rejectLifeCycle(dis);
				}
				Helper.getPersistService().update(dis);
				
				if(dis.getLifeCycleInfo().getStateName().equals(ConstLifeCycle.LC_DISTRIBUTED.getName())){
					//如果在内外发单打印之前的节点中，设置了提交的下一个生命周期状态为已分发的情况，在这里进行判断，将分发信息及分发数据及发放单均变为已完成
					String innerid = dis.getInnerId();
					String classid = dis.getClassId();
					String sql = "select t.* from  ddm_dis_info t , ddm_dis_taskinfolink b "
							+ " where t.innerid = b.toobjectid and t.classid = b.toobjectclassid "
							+ " and b.fromObjectId = ? and b.fromObjectClassId = ?";
					List<DistributeInfo> infos = PersistHelper.getService().query(sql, DistributeInfo.class, innerid,classid);
					for (DistributeInfo info : infos) {
						//将纸质任务下的所有分发信息设置为已分发
						if (ConstUtil.LC_NOT_DISTRIBUT.getName().equals(info.getLifeCycleInfo().getStateName())) {
							life.promoteLifeCycle(info);
							info.setSendTime(System.currentTimeMillis());
							infoList1.add(info);
						}
					}
					if (infoList1 != null && infoList1.size() > 0) {
						Helper.getPersistService().update(infoList1);
					}
					//将纸质任务下的所有分发数据设置为已分发
					String disobjsql = "select objLink.* from DDM_DIS_TASKINFOLINK t,DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink"
							+ " where disInfo.innerId = t.toObjectId and disInfo.classId = t.toObjectClassId"
							+ " and objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
							+ " and t.fromObjectId = ? and t.fromObjectClassId = ?";
					List<DistributeOrderObjectLink> objLinkList = Helper.getPersistService().query(disobjsql, DistributeOrderObjectLink.class, innerid,classid);
					List<DistributeOrderObjectLink> linkList = new ArrayList<DistributeOrderObjectLink>();
					for (DistributeOrderObjectLink objLink : objLinkList) {
						if (ConstUtil.LC_DISTRIBUTING.getName().equals(objLink.getLifeCycleInfo().getStateName())) {
							life.promoteLifeCycle(objLink);
							linkList.add(objLink);
						}
					}
					if (linkList != null || linkList.size() != 0) {
						Helper.getPersistService().update(linkList);
					}
					
					//最后将发放单设置为已分发
					String orderSql = "select MAX(ORD.INNERID) AS INNERID, MAX(ORD.CLASSID) AS CLASSID from DDM_DIS_TASKINFOLINK t,DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink,DDM_DIS_ORDER ORD"
							+ " where disInfo.innerId = t.toObjectId and disInfo.classId = t.toObjectClassId"
							+ " and objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
							+ " and ORD.innerId = objLink.fromObjectId and ORD.classId = objLink.fromObjectClassId"
							+ " and t.fromObjectId =? and t.fromObjectClassId = ?";
					List<Map<String, Object>> list = PersistHelper.getService().query(orderSql, innerid, classid);
					Map<String, Object> mapTemp = list.get(0);
					String orderInnerId = mapTemp.get("INNERID").toString();
					String orderClassId = mapTemp.get("CLASSID").toString();
					Persistable orderObj = Helper.getPersistService().getObject(Helper.getOid(orderClassId, orderInnerId));
					DistributeOrder disOrder = (DistributeOrder) orderObj;
					boolean temp = SessionHelper.getService().isCheckPermission();
					SessionHelper.getService().setCheckPermission(false);
					life.promoteLifeCycle(disOrder);
					SessionHelper.getService().setCheckPermission(temp);
					Helper.getPersistService().update(disOrder);
				}
					
				if (LIFECYCLE_OPT.REJECT == opt) {
					String innerId = Helper.getPersistService().getInnerId(oid);
					String classId = Helper.getPersistService().getClassId(oid);
					int i = 1;
					String hql = " from ReturnReason r where r.taskId = ? and r.taskClassId = ? and r.lifeCycleInfo.stateId = ?";
					List<ReturnReason> list = Helper.getPersistService().find(hql, innerId, classId,
							dis.getLifeCycleInfo().getStateId());
					if (list.size() > 0) {
						i = list.size() + 1;
					}
					ReturnReason retReason = newReturnReason();
					retReason.setTaskId(innerId);
					retReason.setTaskClassId(classId);
					retReason.setLifeCycleInfo(dis.getLifeCycleInfo());
					retReason.setReturnReason(returnReason);
					retReason.setReturnCount(i);
					retReason.setUserId(currentUser.getInnerId());
					retReason.setUserClassId(currentUser.getClassId());
					retReason.setUserName(currentUser.getName());
					Helper.getPersistService().save(retReason);
				}
			} catch (Exception e) {
				throw new RuntimeException("DistribtueTaskServiceImpl类中，updateDistributeTask生命周期升级失败！", e);
			}
		}
	}

	private ReturnReason newReturnReason() {
		ReturnReason reason = (ReturnReason) PersistUtil.createObject(ReturnReason.CLASSID);
		return reason;
	}

	/**
	 * 为分发信息的接收者赋予分发信息关联的分发数据的访问权限
	 * 
	 * @param disInfo 分发信息
	 * @modify zhang guoqiang
	 * @date 2014/10/28
	 */
	private void setUserPrivilege(DistributeInfo disInfo) {
		DistributePrincipalService principalService = DistributeHelper.getDistributePrincipalService();
		List<User> userList = new ArrayList<User>();
		//判断分发类型，0为单位，1为人员
		if("1".equals(disInfo.getDisInfoType())){
			String sqliid = "select aauserinnerid  from  "
					+ "plm_principal_user "
					+ "where innerid=?";
			List<Map<String,Object>> list = Helper.getPersistService().query(sqliid,disInfo.getDisInfoId());
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
			List<Map<String,Object>> list = Helper.getPersistService().query(sqluser,disInfo.getDisInfoId());
			for(Map<String,Object> map :list){
				User receiveUser = UserHelper.getService().getUser(map.get("AAUSERINNERID")+"");
				userList.add(receiveUser);
			}
		}
		//根据分发信息取得分发数据
		String objSql = "SELECT DISTINCT OBJ.* FROM "
				+ "DDM_DIS_ORDEROBJLINK LINK,"
				+ "DDM_DIS_OBJECT OBJ,"
				+ "DDM_DIS_INFO INFO "
				+ "WHERE OBJ.INNERID = LINK.TOOBJECTID "
				+ "AND OBJ.CLASSID = LINK.TOOBJECTCLASSID "
				+ "AND LINK.INNERID = INFO.DISORDEROBJLINKID "
				+ "AND LINK.CLASSID = INFO.DISORDEROBJLINKCLASSID "
				+ "AND INFO.INNERID = ? "
				+ "AND INFO.CLASSID =?";
		List<DistributeObject> disObjectlist = PersistHelper.getService().query(objSql, DistributeObject.class,
				disInfo.getInnerId(), disInfo.getClassId());
		DistributeObject disObject = disObjectlist.get(0);

		//取得业务对象域信息
		Domained domained = (Domained) PersistHelper.getService().getObject(
				disObject.getDataClassId() + ":" + disObject.getDataInnerId());

		//如果单位没有二级调度员的时候userList为空，不设置权限
		for(User user : userList){
			boolean flag = AccessControlHelper.getService().hasEntityPermission(user, Operate.DOWNLOAD, domained);
			if(flag == false){
				//设置访问权限
				principalService.setPriviledge(domained,user);
			}
		}
	}
	
	/**
	 * 为回收销毁信息的接收者赋予回收销毁信息关联的分发数据的访问权限
	 * 
	 * @param recDesInfo 回收销毁信息
	 * @modify zhang guoqiang
	 * @date 2014/10/28
	 */
	private void setUserPrivilegeForRecDes(RecDesInfo recDesInfo) {
		DistributePrincipalService principalService = DistributeHelper.getDistributePrincipalService();
		List<User> userList = new ArrayList<User>();
		//判断分发类型，0为单位，1为人员
		if("1".equals(recDesInfo.getDisInfoType())){
			String sqliid = "select aauserinnerid  from  "
					+ "plm_principal_user "
					+ "where innerid=?";
			List<Map<String,Object>> list = Helper.getPersistService().query(sqliid,recDesInfo.getDisInfoId());
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
			List<Map<String,Object>> list = Helper.getPersistService().query(sqluser,recDesInfo.getDisInfoId());
			for(Map<String,Object> map :list){
				User receiveUser = UserHelper.getService().getUser(map.get("AAUSERINNERID")+"");
				userList.add(receiveUser);
			}
		}
		//根据回收销毁信息取得分发数据
		String objSql = "SELECT DISTINCT OBJ.* FROM "
				+ "DDM_DIS_ORDEROBJLINK LINK,"
				+ "DDM_DIS_OBJECT OBJ,"
				+ "DDM_RECDES_INFO INFO "
				+ "WHERE OBJ.INNERID = LINK.TOOBJECTID "
				+ "AND OBJ.CLASSID = LINK.TOOBJECTCLASSID "
				+ "AND LINK.INNERID = INFO.DISORDEROBJECTLINKID "
				+ "AND LINK.CLASSID = INFO.DISORDEROBJECTLINKCLASSID "
				+ "AND INFO.INNERID = ? "
				+ "AND INFO.CLASSID =?";
		List<DistributeObject> disObjectlist = PersistHelper.getService().query(objSql, DistributeObject.class,
				recDesInfo.getInnerId(), recDesInfo.getClassId());
		
		DistributeObject disObject = disObjectlist.get(0);
		//取得业务对象域信息
		Domained domained = (Domained) PersistHelper.getService().getObject(
				disObject.getDataClassId() + ":" + disObject.getDataInnerId());

		//如果单位没有二级调度员的时候userList为空，不设置权限
		for(User user : userList){

			boolean flag = AccessControlHelper.getService().hasEntityPermission(user, Operate.DOWNLOAD, domained);
			if(flag == false){
				//设置访问权限
				principalService.setPriviledge(domained,user);
			}
		}
	}

	//发送发放相关信息
	public void sendToOutSign(String outSignInfoOids, boolean flag) {
		List<String> oidsList = SplitString.string2List(outSignInfoOids, ",");
		List<DistributeOrderObjectLink> linkList = new ArrayList<DistributeOrderObjectLink>();
		List<DistributeOrder> disOrderList = new ArrayList<DistributeOrder>();
		Map<String, String> reqParamMap = new HashMap<String, String>();
		for (String oid : oidsList) {
			List<Object> list = new ArrayList<Object>();
			String linkSql = "select link.* from DDM_DIS_INFO info,DDM_DIS_ORDEROBJLINK link"
					+ " where info.disOrderObjLinkClassId || ':' || info.disOrderObjLinkId = link.classId || ':' || link.innerId"
					+ " and info.classId || ':' || info.innerId = ?";
			List<DistributeOrderObjectLink> orderObjLinkList = Helper.getPersistService().query(linkSql,
					DistributeOrderObjectLink.class, oid);
			DistributeOrderObjectLink link = orderObjLinkList.get(0);

			//取得相关发放单
			String orderSql = "select disOrder.* from DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink,DDM_DIS_ORDER disOrder"
					+ " where objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
					+ " and disOrder.innerId = objLink.fromObjectId and disOrder.classId = objLink.fromObjectClassId"
					+ " and disInfo.classId || ':' || disInfo.innerId = ?";
			List<DistributeOrder> orderList = Helper.getPersistService().query(orderSql, DistributeOrder.class, oid);
			DistributeOrder order = orderList.get(0);

			//取得分发信息对象
			DistributeInfo info = (DistributeInfo) Helper.getPersistService().getObject(oid);
			String parms = "";
			StringBuffer sbf = new StringBuffer();
			sbf.append("select obj.* from DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink,DDM_DIS_ORDER disOrder,DDM_DIS_OBJECT obj"
					+ " where objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
					+ " and disOrder.innerId = objLink.fromObjectId and disOrder.classId = objLink.fromObjectClassId"
					+ " and obj.innerId = objLink.toObjectId and obj.classId = objLink.toObjectClassId"
					+ " and disOrder.classId || ':' || disOrder.innerId = ?"
					+ " and disInfo.disInfoId = ? and disInfo.infoClassId = ? and disInfo.disMediaType = ?");
			if (!"".equals(info.getOutSignId()) && info.getOutSignId() != null) {
				sbf.append(" and disInfo.outSignId = ? and disInfo.outSignClassId =?");
				parms += info.getOutSignId() + "," + info.getOutSignClassId() + ",";
			}
			List<DistributeObject> disObjList;
			if (parms != "") {
				String[] parm = parms.split(",");
				disObjList = Helper.getPersistService().query(sbf.toString(), DistributeObject.class, order.getOid(),
						info.getDisInfoId(), info.getInfoClassId(), info.getDisMediaType(), parm);
			} else {
				disObjList = Helper.getPersistService().query(sbf.toString(), DistributeObject.class, order.getOid(),
						info.getDisInfoId(), info.getInfoClassId(), info.getDisMediaType());
			}
			if (order instanceof Contexted) {
				Contexted context = (Contexted) order;
				reqParamMap.put("productIID", context.getContextInfo().getContextRef().getInnerId());
			}

			String sourceSiteIID = "";
			Site sourceSite = SiteHelper.getSiteService().findLocalSiteInfo();
			if(sourceSite!=null){
				sourceSiteIID = sourceSite.getInnerId();
			}
			reqParamMap.put("sourceSiteIID", sourceSiteIID);
			reqParamMap.put("busType", BusinessTypeConstant.BUSINESS_TYPE_DISTRIBUTE);

			if (!linkList.contains(link)) {
				linkList.add(link);
				list.add(link);
			}

			//根据分发信息查找变更追踪
			String trackSql = "select * from DDM_DIS_ISTRACK where infoClassId || ':' || disInfoId = ?";
			List<DisInfoIsTrack> trackList = Helper.getPersistService().query(trackSql, DisInfoIsTrack.class, oid);
			list.addAll(trackList);

			list.add(info);

			if (!disOrderList.contains(order)) {
				disOrderList.add(order);
				list.add(order);
			}

			if (flag == true) {
				reqParamMap.put("sourceSiteId", order.getSiteId());
			}

			String sql = "select * from DTS_TRANSFER_SITE where CLASSID || ':' || INNERID = ?";
			List<Site> siteList = Helper.getPersistService().query(sql, Site.class,
					info.getInfoClassId() + ":" + info.getDisInfoId());
			Site targetSite = siteList.get(0);

			// 获取系统部署模式
			DmcConfigService dmcConfigService = DmcConfigHelper.getService();
			boolean isDcDeployModel = dmcConfigService.getIsDealOnDC();

			List<Object> dataObjList = new ArrayList<Object>();

			if (isDcDeployModel == false) {
				//邦联模式，实体分发
				for (DistributeObject object : disObjList) {
					String dataOid = object.getDataClassId() + ":" + object.getDataInnerId();
					Persistable obj = Helper.getPersistService().getObject(dataOid);
					if (obj instanceof Change) {
						List<Object> changeBeforeList = new ArrayList<Object>();
						Change change = (Change) obj;
						List<ChangedLink> changeLinkList = Helper.getChangeService().getChangedLinkList(change);
						for (ChangedLink changedLink : changeLinkList) {
							Persistable beforeObj = changedLink.getFrom();
							changeBeforeList.add(beforeObj);
						}
						dataObjList.addAll(changeBeforeList);
						if (obj instanceof ECO) {
							ECO eco = (ECO) obj;
							List<IncludeInLink> includeLinkList = Helper.getChangeService().getChangedItemList(eco);
							for (IncludeInLink include : includeLinkList) {
								ChangedItem changedItem = (ChangedItem) include.getFrom();
								dataObjList.add(changedItem);
							}
							dataObjList.addAll(includeLinkList);
						} else if (obj instanceof TNO) {
							TNO tno = (TNO) obj;
							List<WaivedInLink> waivedInLinkList = Helper.getChangeService().getWaivedItemList(tno);
							for (WaivedInLink waivedInLink : waivedInLinkList) {
								WaivedItem waivedItem = (WaivedItem) waivedInLink.getFrom();
								dataObjList.add(waivedItem);
							}
							dataObjList.addAll(waivedInLinkList);
						}
					}
					dataObjList.add(obj);
				}
				dataObjList.addAll(disObjList);
				List<Object> objectList = ExportFileHelper.getExportFileService().getAllObject(dataObjList);
				list.addAll(objectList);
			} else if (isDcDeployModel == true) {
				Site selfSite = SiteHelper.getSiteService().findLocalSiteInfo();
				DCSiteAttribute dcSiteAttr = DCSiteAttributeHelper.getDCSiteAttrService().findDcSiteAttrByDTSiteId(
						selfSite.getInnerId());

				//中心模式且当前站点不是数据中心
				if (dcSiteAttr != null && "false".equals(dcSiteAttr.getIsSiteControl())) {
					for (DistributeObject object : disObjList) {
						String dataOid = object.getDataClassId() + ":" + object.getDataInnerId();
						Persistable obj = Helper.getPersistService().getObject(dataOid);
						if (obj instanceof Change) {
							List<Object> changeBeforeList = new ArrayList<Object>();
							Change change = (Change) obj;
							List<ChangedLink> changeLinkList = Helper.getChangeService().getChangedLinkList(change);
							for (ChangedLink changedLink : changeLinkList) {
								Persistable beforeObj = changedLink.getFrom();
								changeBeforeList.add(beforeObj);
							}
							dataObjList.addAll(changeBeforeList);
							if (obj instanceof ECO) {
								ECO eco = (ECO) obj;
								List<IncludeInLink> includeLinkList = Helper.getChangeService().getChangedItemList(eco);
								for (IncludeInLink include : includeLinkList) {
									ChangedItem changedItem = (ChangedItem) include.getFrom();
									dataObjList.add(changedItem);
								}
								dataObjList.addAll(includeLinkList);
							} else if (obj instanceof TNO) {
								TNO tno = (TNO) obj;
								List<WaivedInLink> waivedInLinkList = Helper.getChangeService().getWaivedItemList(tno);
								for (WaivedInLink waivedInLink : waivedInLinkList) {
									WaivedItem waivedItem = (WaivedItem) waivedInLink.getFrom();
									dataObjList.add(waivedItem);
								}
								dataObjList.addAll(waivedInLinkList);
							}
						}
						dataObjList.add(obj);
					}
					dataObjList.addAll(disObjList);
					List<Object> objectList = ExportFileHelper.getExportFileService().getAllObject(dataObjList);
					list.addAll(objectList);
				} else {
					// 判断是否是实体会签模式
					boolean isEntitySignature = false;
					String entitySignature = DmcConfigHelper.getService().getDmcConfigValue(ConstUtil.IS_ENTITY_DISTRIBUTEMODE_YES);
					if(null != entitySignature){
						isEntitySignature = ("true".equalsIgnoreCase(entitySignature));
					}
					if (isEntitySignature) {
						for (DistributeObject object : disObjList) {
							String dataOid = object.getDataClassId() + ":" + object.getDataInnerId();
							Persistable obj = Helper.getPersistService().getObject(dataOid);
							if (obj instanceof Change) {
								List<Object> changeBeforeList = new ArrayList<Object>();
								Change change = (Change) obj;
								List<ChangedLink> changeLinkList = Helper.getChangeService().getChangedLinkList(change);
								for (ChangedLink changedLink : changeLinkList) {
									Persistable beforeObj = changedLink.getFrom();
									changeBeforeList.add(beforeObj);
								}
								dataObjList.addAll(changeBeforeList);
								if (obj instanceof ECO) {
									ECO eco = (ECO) obj;
									List<IncludeInLink> includeLinkList = Helper.getChangeService().getChangedItemList(
											eco);
									for (IncludeInLink include : includeLinkList) {
										ChangedItem changedItem = (ChangedItem) include.getFrom();
										dataObjList.add(changedItem);
									}
									dataObjList.addAll(includeLinkList);
								} else if (obj instanceof TNO) {
									TNO tno = (TNO) obj;
									List<WaivedInLink> waivedInLinkList = Helper.getChangeService().getWaivedItemList(
											tno);
									for (WaivedInLink waivedInLink : waivedInLinkList) {
										WaivedItem waivedItem = (WaivedItem) waivedInLink.getFrom();
										dataObjList.add(waivedItem);
									}
									dataObjList.addAll(waivedInLinkList);
								}
							}
							dataObjList.add(obj);
						}
						dataObjList.addAll(disObjList);
						List<Object> objectList = ExportFileHelper.getExportFileService().getAllObject(dataObjList);
						list.addAll(objectList);
					}
				}
			}

			File exportFIle = ExportFileHelper.getExportFileService().exportObject(list);
			String filePath = exportFIle.getPath();
			TransferObjectService transferService = TransferObjectHelper.getTransferService();
			transferService.sendRequest(UUID.getUID(), "distribute", MessageTypeconstant.SEND_DISTRIBUTE, filePath,
					targetSite, reqParamMap, filePath, TransferConstant.REQTYPE_ASYN);

			//flag为true表示为中心发送给接收方
			if (flag == true) {
				String taskOidSql = "SELECT MAX (ELEC.CLASSID || ':' || ELEC.INNERID) AS OID FROM DDM_DIS_ELECTASK ELEC, DDM_DIS_TASKDOMAINLINK LINK, DDM_DIS_INFO INFO"
						+ " WHERE LINK.FROMOBJECTCLASSID || ':' || LINK.FROMOBJECTID = ELEC.CLASSID || ':' || ELEC.INNERID"
						+ " AND LINK.TOOBJECTCLASSID || ':' || LINK.TOOBJECTID = INFO.CLASSID || ':' || INFO.INNERID"
						+ " AND INFO.CLASSID || ':' || INFO.INNERID = ?";

				List<Map<String, Object>> oidList = PersistHelper.getService().query(taskOidSql, oid);
				String taskOid = oidList.get(0).get("OID").toString();
				DistributeElecTask task = (DistributeElecTask) Helper.getPersistService().getObject(taskOid);
				Site site = SiteHelper.getSiteService().findSiteById(task.getSourceSiteId());
				//传输参数
				Map<String, String> centerReqParamMap = new HashMap<String, String>();
				reqParamMap.put("flag", "1");
				reqParamMap.put("taskOid", taskOid);
				reqParamMap.put("opt", "PROMOTE");

				//生命周期同步(发起方)
				transferService.sendRequest(UUID.getUID(), "中心同步生命周期反馈给发起方", MessageTypeconstant.REPLY_SEND_DISTRIBUTE,
						null, site, centerReqParamMap, null, TransferConstant.REQTYPE_SYN);
			}
		}
	}

	// 发送发放相关信息
	public void sendToOutSite(DistributeOrder order,
			List<DistributeInfo> outSiteDisInfoList, List<String> siteList,
			boolean flag) {
		Map<String, String> reqParamMap = new HashMap<String, String>();

		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		//是否存在电子分发
		String isExistDispatchSite = "false";
		//电子分发的分发信息OIDS
		String allDispatchDisInfoOids = "'";
		
		for (String siteOid : siteList) {
			List<String> idList = SplitString.string2List(siteOid, ":");
			Site targetSite = SiteHelper.getSiteService().findSiteById(
					idList.get(1));

			// TODO跨版本分发信息处理
			// 跨版本导入方系统版本
			String importTargetSiteSystemVersion = "";
			// 通过查找跨版本导入方版本配置，如果有配置，就根据该配置版本进行导出，如果没有配置，默认A5
			ImportVersionCfg cfg = ImportVersionCfgHelper
					.getImportVersionCfgService().findCfgByTargetAndType(
							targetSite.getInnerId(),
							BusinessTypeConstant.BUSINESS_TYPE_DISTRIBUTE);
			if (cfg != null) {
				importTargetSiteSystemVersion = cfg.getSystemVersion();
			} else {
				importTargetSiteSystemVersion = DTSiteConstant.DTSITE_APPVERSION_5;
				//throw new RuntimeException("取得系统运行时配置的跨版本导入方版本配置失败!站点名称："+ targetSite.getDisplayName());
			}
			// 发送到A3.5
			if (DTSiteConstant.DTSITE_APPVERSION_3_5
					.equalsIgnoreCase(importTargetSiteSystemVersion)) {

				// 系统运行时配置服务
				DmcConfigService dmcConfigService = DmcConfigHelper
						.getService();
				// 获取发放是否使用实体模式
				String isEntityDistributeModel = dmcConfigService
						.getDmcConfigValue(ConstUtil.IS_ENTITY_DISTRIBUTEMODE_YES);

				//实体分发
				if ("true".equalsIgnoreCase(isEntityDistributeModel)) {
					// 分发消息类型 实体分发
					String sendMessageType = Message4XTypeConstant.DISTRIBUTE_DISTRIBUTE_A5;
				
//					// 使用参数中的outSiteDisInfo可以少访问一次数据库
//					// 遍历发放单中发送站点
//					String infoSql = "select info.* from DDM_DIS_INFO info, DDM_DIS_ORDEROBJLINK link"
//							+ " where info.disOrderObjLinkClassId || ':' || info.disOrderObjLinkId = link.classId || ':' || link.innerId"
//							+ " and link.fromObjectClassId || ':' || link.fromObjectId = ?"
//							+ " and infoClassId || ':' || disInfoId = ?";
//					List<DistributeInfo> infoList = Helper.getPersistService()
//							.query(infoSql, DistributeInfo.class, order.getOid(),
//									siteOid);
//	
//					String infoOids = "'";
//					for (DistributeInfo info : infoList) {
//						infoOids = infoOids + info.getOid() + "','";
//					}
//					infoOids = infoOids.substring(0, infoOids.length() - 2);
	
					String infoOids = "'";
					for (DistributeInfo info : outSiteDisInfoList) {
						//外站点OID
						String outSiteOid = info.getInfoClassId()+":"+info.getDisInfoId();
						if(siteOid.equals(outSiteOid)){
							infoOids = infoOids + info.getOid() + "','";
							//更新分发信息的状态为已分发
							life.updateLifeCycleByStateId(info,ConstLifeCycle.LC_DISTRIBUTED.getId());
						}
					}
					infoOids = infoOids.substring(0, infoOids.length() - 2);
					
					String linkSql = "select link.* from DDM_DIS_INFO info, DDM_DIS_ORDEROBJLINK link"
							+ " where info.disOrderObjLinkClassId || ':' || info.disOrderObjLinkId = link.classId || ':' || link.innerId"
							+ " and info.classId || ':' || info.innerId in ("
							+ infoOids + ")";
					List<DistributeOrderObjectLink> orderObjLinkList = Helper
							.getPersistService().query(linkSql,
									DistributeOrderObjectLink.class);
					
	
					String orderObjLinkOids = "'";
					for (DistributeOrderObjectLink orderObjLink : orderObjLinkList) {
						orderObjLinkOids = orderObjLinkOids + orderObjLink.getClassId() + ":" + orderObjLink.getInnerId() + "','";
					}
					orderObjLinkOids = orderObjLinkOids.substring(0, orderObjLinkOids.length() - 2);
					
					String objSql = "select distinct obj.* from DDM_DIS_INFO info, DDM_DIS_ORDEROBJLINK link, DDM_DIS_OBJECT obj"
							+ " where info.disOrderObjLinkClassId || ':' || info.disOrderObjLinkId = link.classId || ':' || link.innerId"
							+ " and link.toObjectClassId || ':' || link.toObjectId = obj.classId || ':' || obj.innerId"
							+ " and info.classId || ':' || info.innerId in ("
							+ infoOids + ")";
					List<DistributeObject> objList = Helper.getPersistService()
							.query(objSql, DistributeObject.class);
	
					
					//取得发送对象：分发数据
					List<DistributeObject> sendForA3DisObjectList = getSendForA3DisObjectList(objList);
	
					String disObjOids = "'";
					for (DistributeObject obj : sendForA3DisObjectList) {
						disObjOids = disObjOids + obj.getOid() + "','";
					}
					disObjOids = disObjOids.substring(0, disObjOids.length() - 2);
					
					// 根据分发数据取得分发数据来源对象以及所有相关联对象link
					List<Object> sendDataObjectList = getAllForA3DataObjectList(sendForA3DisObjectList);
	
					if (sendDataObjectList == null || sendDataObjectList.size() == 0) {
						continue;
					}
	
					String dataObjOids = "'";
					for (Object obj : sendDataObjectList) {
						Persistable p = (Persistable) obj;
						dataObjOids = dataObjOids + p.getClassId() + ":" + p.getInnerId() + "','";
					}
					dataObjOids = dataObjOids.substring(0, dataObjOids.length() - 2);
					
					// 根据分发数据取得分发数据来源对象以及所有相关联对象link
					List<Object> sendObjectList = getAllForA3ObjectList(sendForA3DisObjectList);
	
					if (sendObjectList == null || sendObjectList.size() == 0) {
						continue;
					}
	
					Map<String, String> map = new HashMap<String, String>();
					map.put(A3A5DataConvertUtil.ORDERIID, order.getInnerId());
					// 导出分发对象
					File exportFIle = ExportFileHelper.getExportFileService()
							.exportObject(sendDataObjectList,DTSiteConstant.DTSITE_APPVERSION_3_5, map);
					String filePath = exportFIle.getPath();
					TransferObjectService transferService = TransferObjectHelper
							.getTransferService();
					// 请求参数
					Map<String, String> reqParamMapForA3 = new HashMap<String, String>();
					reqParamMapForA3.put("orderIID", order.getInnerId());
					reqParamMapForA3.put("orderObjLinkOids", orderObjLinkOids);
					reqParamMapForA3.put("disObjOids", disObjOids);
					reqParamMapForA3.put("dataObjOids", dataObjOids);
					reqParamMapForA3.put("infoOids", infoOids);
					reqParamMapForA3.put("dispatchFile", filePath);
					// 发送（按站点发送）
					transferService.sendRequest(UUID.getUID(), order.getNumber(),
							sendMessageType, filePath, targetSite, reqParamMapForA3,
							filePath, TransferConstant.REQTYPE_ASYN);
				//电子分发
				} else {
					isExistDispatchSite = "true";
					for (DistributeInfo info : outSiteDisInfoList) {
						//外站点OID
						String outSiteOid = info.getInfoClassId()+":"+info.getDisInfoId();
						if(siteOid.equals(outSiteOid)){
							allDispatchDisInfoOids = allDispatchDisInfoOids + info.getOid() + "','";
							//更新分发信息的状态为已分发
							life.updateLifeCycleByStateId(info,ConstLifeCycle.LC_DISTRIBUTED.getId());
						}
					}
				}
				// 发送到A5.0
			} else if (DTSiteConstant.DTSITE_APPVERSION_5
					.equalsIgnoreCase(importTargetSiteSystemVersion)) {
				// 遍历发放单中发送站点
				String infoSql = "select info.* from DDM_DIS_INFO info, DDM_DIS_ORDEROBJLINK link"
						+ " where info.disOrderObjLinkClassId || ':' || info.disOrderObjLinkId = link.classId || ':' || link.innerId"
						+ " and link.fromObjectClassId || ':' || link.fromObjectId = ?"
						+ " and infoClassId || ':' || disInfoId = ?";
				List<DistributeInfo> infoList = Helper.getPersistService()
						.query(infoSql, DistributeInfo.class, order.getOid(),
								siteOid);

				String infoOids = "'";
				for (DistributeInfo info : infoList) {
					//更新分发信息的状态为已分发
					life.updateLifeCycleByStateId(info,ConstLifeCycle.LC_DISTRIBUTED.getId());
					infoOids = infoOids + info.getOid() + "','";
				}

				infoOids = infoOids.substring(0, infoOids.length() - 2);

				// 根据分发信息查找变更追踪
				String trackSql = "select * from DDM_DIS_ISTRACK where infoClassId || ':' || disInfoId in("
						+ infoOids + ")";
				List<DisInfoIsTrack> trackList = Helper.getPersistService()
						.query(trackSql, DisInfoIsTrack.class);

				String linkSql = "select link.* from DDM_DIS_INFO info, DDM_DIS_ORDEROBJLINK link"
						+ " where info.disOrderObjLinkClassId || ':' || info.disOrderObjLinkId = link.classId || ':' || link.innerId"
						+ " and info.classId || ':' || info.innerId in ("
						+ infoOids + ")";
				List<DistributeOrderObjectLink> orderObjLinkList = Helper
						.getPersistService().query(linkSql,
								DistributeOrderObjectLink.class);

				String objSql = "select distinct obj.* from DDM_DIS_INFO info, DDM_DIS_ORDEROBJLINK link, DDM_DIS_OBJECT obj"
						+ " where info.disOrderObjLinkClassId || ':' || info.disOrderObjLinkId = link.classId || ':' || link.innerId"
						+ " and link.toObjectClassId || ':' || link.toObjectId = obj.classId || ':' || obj.innerId"
						+ " and info.classId || ':' || info.innerId in ("
						+ infoOids + ")";
				List<DistributeObject> objList = Helper.getPersistService()
						.query(objSql, DistributeObject.class);

				// 根据分发数据取得分发数据来源对象以及所有相关联对象link
				List<Object> objectList = getAllObjectList(objList);

				List<Object> list = new ArrayList<Object>();

				// 添加所有分发数据，相关业务对象以及之间所有link
				list.addAll(objectList);

				// 添加distributeOrderObject对象
				list.addAll(orderObjLinkList);

				// 添加变更追踪对象
				list.addAll(trackList);

				// 添加分发信息对象
				list.addAll(infoList);

				// 添加发放单对象
				list.add(order);

				if (order instanceof Contexted) {
					Contexted context = (Contexted) order;
					reqParamMap.put("productIID", context.getContextInfo()
							.getContextRef().getInnerId());
				}

				String sourceSiteIID = SiteHelper.getSiteService()
						.findLocalSiteInfo().getInnerId();
				reqParamMap.put("sourceSiteIID", sourceSiteIID);
				reqParamMap.put("busType",
						BusinessTypeConstant.BUSINESS_TYPE_DISTRIBUTE);

				// 发送（按站点发送）
				File exportFIle = ExportFileHelper.getExportFileService()
						.exportObject(list);
				String filePath = exportFIle.getPath();
				TransferObjectService transferService = TransferObjectHelper
						.getTransferService();
				transferService.sendRequest(UUID.getUID(), order.getNumber(),
						MessageTypeconstant.SEND_DISTRIBUTE, filePath,
						targetSite, reqParamMap, filePath,
						TransferConstant.REQTYPE_ASYN);

			} else {
				throw new RuntimeException("此版本的跨版本发放暂时还没有支持!版本："
						+ importTargetSiteSystemVersion);
			}
		}
		
		//A3.5并且是电子分发的时候，如果发往多个站点，需要统一发到中心处理
		if("true".equals(isExistDispatchSite)){
			// 分发消息类型 电子分发
			String sendMessageType = Message4XTypeConstant.DISTRIBUTE_DISPATCH_A5;

			allDispatchDisInfoOids = allDispatchDisInfoOids.substring(0, allDispatchDisInfoOids.length() - 2);

			String linkSql = "select link.* from DDM_DIS_INFO info, DDM_DIS_ORDEROBJLINK link"
					+ " where info.disOrderObjLinkClassId || ':' || info.disOrderObjLinkId = link.classId || ':' || link.innerId"
					+ " and info.classId || ':' || info.innerId in ("
					+ allDispatchDisInfoOids + ")";
			List<DistributeOrderObjectLink> orderObjLinkList = Helper
					.getPersistService().query(linkSql,
							DistributeOrderObjectLink.class);
			

			String orderObjLinkOids = "'";
			for (DistributeOrderObjectLink orderObjLink : orderObjLinkList) {
				orderObjLinkOids = orderObjLinkOids + orderObjLink.getClassId() + ":" + orderObjLink.getInnerId() + "','";
			}
			orderObjLinkOids = orderObjLinkOids.substring(0, orderObjLinkOids.length() - 2);
			
			String objSql = "select distinct obj.* from DDM_DIS_INFO info, DDM_DIS_ORDEROBJLINK link, DDM_DIS_OBJECT obj"
					+ " where info.disOrderObjLinkClassId || ':' || info.disOrderObjLinkId = link.classId || ':' || link.innerId"
					+ " and link.toObjectClassId || ':' || link.toObjectId = obj.classId || ':' || obj.innerId"
					+ " and info.classId || ':' || info.innerId in ("
					+ allDispatchDisInfoOids + ")";
			List<DistributeObject> objList = Helper.getPersistService()
					.query(objSql, DistributeObject.class);

			
			//取得发送对象：分发数据
			List<DistributeObject> sendForA3DisObjectList = getSendForA3DisObjectList(objList);

			String disObjOids = "'";
			for (DistributeObject obj : sendForA3DisObjectList) {
				disObjOids = disObjOids + obj.getOid() + "','";
			}
			disObjOids = disObjOids.substring(0, disObjOids.length() - 2);
			
			// 根据分发数据取得分发数据来源对象以及所有相关联对象link
			List<Object> sendDataObjectList = getAllForA3DataObjectList(sendForA3DisObjectList);

			if (sendDataObjectList == null || sendDataObjectList.size() == 0) {
				throw new RuntimeException("分发数据来源对象以及所有相关联对象link对象不存在！");
			}

			String dataObjOids = "'";
			for (Object obj : sendDataObjectList) {
				Persistable p = (Persistable) obj;
				dataObjOids = dataObjOids + p.getClassId() + ":" + p.getInnerId() + "','";
			}
			dataObjOids = dataObjOids.substring(0, dataObjOids.length() - 2);
			
//			// 根据分发数据取得A3能够处理的数据对象对应的A5分发数据对象
//			List<Object> sendObjectList = getAllForA3ObjectList(sendForA3DisObjectList);
//
//			if (sendObjectList == null || sendObjectList.size() == 0) {
//				throw new RuntimeException("A3能够处理的数据对象对应的A5分发数据对象不存在！");
//			}

			Map<String, String> map = new HashMap<String, String>();
			map.put(A3A5DataConvertUtil.ORDERIID, order.getInnerId());
			// 导出分发对象
			File exportFIle = ExportFileHelper.getExportFileService()
					.exportObject(sendDataObjectList,DTSiteConstant.DTSITE_APPVERSION_3_5, map);
			String filePath = exportFIle.getPath();
			TransferObjectService transferService = TransferObjectHelper
					.getTransferService();
			// 请求参数
			Map<String, String> reqParamMapForA3 = new HashMap<String, String>();
			reqParamMapForA3.put("orderIID", order.getInnerId());
			reqParamMapForA3.put("orderObjLinkOids", orderObjLinkOids);
			reqParamMapForA3.put("disObjOids", disObjOids);
			reqParamMapForA3.put("dataObjOids", dataObjOids);
			reqParamMapForA3.put("infoOids", allDispatchDisInfoOids);
			reqParamMapForA3.put("dispatchFile", filePath);
			DCSiteAttribute dcSiteAttr = DCSiteAttributeHelper.getDCSiteAttrService().findLocalDCSite();
			// 取得中心站点（由于是电子分发所以要不同单位或站点统一发送给中心）
			Site centerSite = dcSiteAttr.getSite();
			// 发送（按站点发送）
			transferService.sendRequest(UUID.getUID(), order.getNumber(),
					sendMessageType, filePath, centerSite, reqParamMapForA3,
					filePath, TransferConstant.REQTYPE_ASYN);
		}
	}

	/**
	 * 根据分发数据取得A3能够处理的数据对象对应的A5分发数据对象
	 * 
	 * @param objList
	 * @return
	 */
	private List<DistributeObject> getSendForA3DisObjectList(List<DistributeObject> objList) {
		// 发送给A3的分发数据对象列表
		List<DistributeObject> forSendObjList = new ArrayList<DistributeObject>();
		for (DistributeObject disObject : objList) {
			// 取得分发数据数据来源对象
			String dataOid = disObject.getDataClassId() + ":" + disObject.getDataInnerId();
			Persistable obj = Helper.getPersistService().getObject(dataOid);
			if (obj instanceof Document || obj instanceof Part || obj instanceof ECO || obj instanceof CADDocument) {
				// 可以发送的对象（更改单，文档，部件,CADDOCUMENT），继续处理
			} else {
				// A3只能处理更改单，文档，部件，CADDOCUMENT以外的对象不发送
				continue;
			}
			forSendObjList.add(disObject);
		}

		return forSendObjList;
	}
	
	/**
	 * 根据分发数据取得分发数据来源对象以及所有相关联的对象
	 * 
	 * @param objList
	 * @return
	 */
	private List<Object> getAllForA3ObjectList(List<DistributeObject> objList) {

		List<Object> allObjectList = new ArrayList<Object>();
		// 发送给A3的分发数据对象列表
		List<DistributeObject> forSendObjList = new ArrayList<DistributeObject>();
		for (DistributeObject disObject : objList) {
			List<Object> dataObjList = new ArrayList<Object>();
			String dataOid = disObject.getDataClassId() + ":" + disObject.getDataInnerId();
			Persistable obj = Helper.getPersistService().getObject(dataOid);
			//String dataClassId = disObject.getDataClassId();
			if (obj instanceof Document || obj instanceof Part || obj instanceof ECO || obj instanceof CADDocument){
				// 是可以发送的对象，继续处理
			} else {
				// A3只能处理更改单，文档，部件
				continue;
			}
			// 取得分发数据数据来源对象
			
			// 数据来源属于变更
			if (obj instanceof Change) {
				List<Object> changeBeforeList = new ArrayList<Object>();
				Change change = (Change) obj;
				List<ChangedLink> changeLinkList = Helper.getChangeService()
						.getChangedLinkList(change);
				for (ChangedLink changedLink : changeLinkList) {
					if (changedLink.getFrom() != null) {
						Persistable beforeObj = changedLink.getFrom();
						changeBeforeList.add(beforeObj);
					}
				}
				dataObjList.addAll(changeBeforeList);
				if (obj instanceof ECO) {
					// 数据来源属于更改单
					ECO eco = (ECO) obj;
					List<IncludeInLink> includeLinkList = Helper
							.getChangeService().getChangedItemList(eco);
					for (IncludeInLink include : includeLinkList) {
						Persistable object = include.getFrom();
						if (object != null) {
							ChangedItem changedItem = (ChangedItem) object;
							dataObjList.add(changedItem);
						}
					}
					dataObjList.addAll(includeLinkList);
				}
			}
			dataObjList.add(obj);
			allObjectList.addAll(dataObjList);
			forSendObjList.add(disObject);
		}
		allObjectList.addAll(forSendObjList);
		// 取得所有数据和数据之间的link对象
		allObjectList = ExportFileHelper.getExportFileService().getAllObject(
				allObjectList);

		return allObjectList;
	}

	/**
	 * 根据分发数据取得分发数据关联的需要分发的
	 * 
	 * @param objList
	 * @return
	 */
	private List<Object> getAllForA3DataObjectList(List<DistributeObject> objList) {

		// 发送给A3的分发数据对象列表
		List<Object> dataObjList = new ArrayList<Object>();
		for (DistributeObject disObject : objList) {
			// 取得分发数据数据来源对象
			String dataOid = disObject.getDataClassId() + ":" + disObject.getDataInnerId();
			Persistable obj = Helper.getPersistService().getObject(dataOid);
			if (obj instanceof Document || obj instanceof Part || obj instanceof ECO || obj instanceof CADDocument) {
				// 是可以发送的对象，继续处理
			} else {
				// A3只能处理更改单，文档，部件
				continue;
			}
			// 数据来源属于变更
			if (obj instanceof Change) {
				List<Object> changeBeforeList = new ArrayList<Object>();
				Change change = (Change) obj;
				List<ChangedLink> changeLinkList = Helper.getChangeService()
						.getChangedLinkList(change);
				for (ChangedLink changedLink : changeLinkList) {
					if (changedLink.getFrom() != null) {
						Persistable beforeObj = changedLink.getFrom();
						changeBeforeList.add(beforeObj);
					}
				}
				dataObjList.addAll(changeBeforeList);
				if (obj instanceof ECO) {
					// 数据来源属于更改单
					ECO eco = (ECO) obj;
					List<IncludeInLink> includeLinkList = Helper
							.getChangeService().getChangedItemList(eco);
					for (IncludeInLink include : includeLinkList) {
						Persistable object = include.getFrom();
						if (object != null) {
							ChangedItem changedItem = (ChangedItem) object;
							dataObjList.add(changedItem);
						}
					}
					dataObjList.addAll(includeLinkList);
				}
			}
			dataObjList.add(obj);
		}

		return dataObjList;
	}

	public void sendToReceiveSite(List<DistributeInfo> infoList, String outSiteInfos, DistributeOrder order) {
		String disInfos = outSiteInfos.substring(0, outSiteInfos.length() - 2);

		//根据分发信息查找变更追踪
		String trackSql = "select * from DDM_DIS_ISTRACK where infoClassId || ':' || disInfoId in(" + disInfos + ")";
		List<DisInfoIsTrack> trackList = Helper.getPersistService().query(trackSql, DisInfoIsTrack.class);

		String orderObjLinkSql = "select link.* from DDM_DIS_ORDEROBJLINK link, DDM_DIS_INFO info"
				+ " where info.disOrderObjLinkClassId || ':' || info.disOrderObjLinkId = link.classId || ':' || link.innerId"
				+ " and info.classId || ':' || info.innerId in (" + disInfos + ")";
		List<DistributeOrderObjectLink> linkList = Helper.getPersistService().query(orderObjLinkSql,
				DistributeOrderObjectLink.class);

		String objSql = "select distinct obj.* from DDM_DIS_ORDEROBJLINK link, DDM_DIS_OBJECT obj, DDM_DIS_INFO info"
				+ " where link.toObjectClassId || ':' || link.toObjectId = obj.classId || ':' || obj.innerId"
				+ " and info.disOrderObjLinkClassId || ':' || info.disOrderObjLinkId = link.classId || ':' || link.innerId"
				+ " and info.classId || ':' || info.innerId in (" + disInfos + ")";
		List<DistributeObject> objList = Helper.getPersistService().query(objSql, DistributeObject.class);

		//根据分发数据取得分发数据来源对象以及所有相关联对象link
		//List<Object> ObjectList = getAllObjectList(objList);

		List<Object> list = new ArrayList<Object>();

		//添加所有分发数据对象
		list.addAll(objList);

		//添加distributeOrderObject对象
		list.addAll(linkList);

		//添加变更追踪对象
		list.addAll(trackList);

		//添加分发信息对象
		list.addAll(infoList);

		//添加发放单对象
		list.add(order);

		//取得所有外域分发信息中一个，并取得站点
		DistributeInfo info = infoList.get(0);
		String siteOid = info.getInfoClassId() + ":" + info.getDisInfoId();

		List<String> idList = SplitString.string2List(siteOid, ":");
		Site targetSite = SiteHelper.getSiteService().findSiteById(idList.get(1));

		Map<String, String> reqParamMap = new HashMap<String, String>();

		if (order instanceof Contexted) {
			Contexted context = (Contexted) order;
			reqParamMap.put("productIID", context.getContextInfo().getContextRef().getInnerId());
		}
		reqParamMap.put("sourceSiteId", order.getSiteId());
		String sourceSiteIID = SiteHelper.getSiteService().findLocalSiteInfo().getInnerId();
		reqParamMap.put("sourceSiteIID", sourceSiteIID);
		reqParamMap.put("busType", BusinessTypeConstant.BUSINESS_TYPE_DISTRIBUTE);

		//发送（按站点发送）
		File exportFIle = ExportFileHelper.getExportFileService().exportObject(list);
		String filePath = exportFIle.getPath();
		TransferObjectService transferService = TransferObjectHelper.getTransferService();
		transferService.sendRequest(UUID.getUID(), order.getNumber(), MessageTypeconstant.SEND_DISTRIBUTE, filePath, targetSite,
				reqParamMap, filePath, TransferConstant.REQTYPE_ASYN);
	}

	/**
	 * 根据分发数据取得分发数据来源对象以及所有相关联的对象
	 * 
	 * @param disObject
	 * @return 
	 */
	public List<Object> getAllObjectList(List<DistributeObject> objList) {
		List<Object> allObjectList = new ArrayList<Object>();
		for (DistributeObject disObject : objList) {
			List<Object> dataObjList = new ArrayList<Object>();
			//取得分发数据数据来源对象
			String dataOid = disObject.getDataClassId() + ":" + disObject.getDataInnerId();
			Persistable obj = Helper.getPersistService().getObject(dataOid);
			//数据来源属于变更
			if (obj instanceof Change) {
				List<Object> changeBeforeList = new ArrayList<Object>();
				Change change = (Change) obj;
				List<ChangedLink> changeLinkList = Helper.getChangeService().getChangedLinkList(change);
				for (ChangedLink changedLink : changeLinkList) {
					if (changedLink.getFrom() != null) {
						Persistable beforeObj = changedLink.getFrom();
						changeBeforeList.add(beforeObj);
					}
				}
				dataObjList.addAll(changeBeforeList);
				if (obj instanceof ECO) {
					//数据来源属于更改单
					ECO eco = (ECO) obj;
					List<IncludeInLink> includeLinkList = Helper.getChangeService().getChangedItemList(eco);
					for (IncludeInLink include : includeLinkList) {
						Persistable object = include.getFrom();
						if (object != null) {
							ChangedItem changedItem = (ChangedItem) object;
							dataObjList.add(changedItem);
						}
					}
					dataObjList.addAll(includeLinkList);
				} else if (obj instanceof TNO) {
					//数据来源属于技术通知单
					TNO tno = (TNO) obj;
					List<WaivedInLink> waivedInLinkList = Helper.getChangeService().getWaivedItemList(tno);
					for (WaivedInLink waivedInLink : waivedInLinkList) {
						Persistable object = waivedInLink.getFrom();
						if (object != null) {
							WaivedItem waivedItem = (WaivedItem) object;
							dataObjList.add(waivedItem);
						}
					}
					dataObjList.addAll(waivedInLinkList);
				} else if (obj instanceof Variance){
					//数据来源属于超差代料质疑单
					Variance variance = (Variance) obj;
					//取得质疑单对应的质疑项
					List<VariancedInLink> variancedItemList  = Helper.getChangeService().getVariancedItemList(variance);
					for(VariancedInLink variancedInLink : variancedItemList){
						Persistable object = variancedInLink.getFrom();
						if (!dataObjList.contains(object)) {
							//添加变更对象
							dataObjList.add(object);
						}
					}
					//得到变更对象与超差对象的关系
					List<VariancedLink> variancedLinkList = Helper.getChangeService().getVariancedLinkList(variance);
					for(VariancedLink variancedLink : variancedLinkList){
						Persistable object = variancedLink.getFrom();
						if (!dataObjList.contains(object)) {
							//添加变更对象
							dataObjList.add(object);
						}
					}
					//根据超差单获得超差申请
					List<VarianceReq> varianceReqList = Helper.getChangeService().getVarianceReqList(variance);
					for(VarianceReq varianceReq : varianceReqList){
						List<VariancedLink> linkList = Helper.getChangeService().getVariancedList(varianceReq);
						for(VariancedLink variancedLink : linkList){
							Persistable object = variancedLink.getFrom();
							if (!dataObjList.contains(object)) {
								//添加变更对象
								dataObjList.add(object);
							}
						}
						dataObjList.addAll(linkList);
					}
					dataObjList.addAll(variancedItemList);
					dataObjList.addAll(variancedLinkList);
					dataObjList.addAll(varianceReqList);
				}
			}
			dataObjList.add(obj);
			allObjectList.addAll(dataObjList);
		}
		allObjectList.addAll(objList);
		//取得所有数据和数据之间的link对象
		allObjectList = ExportFileHelper.getExportFileService().getAllObject(allObjectList);

		return allObjectList;
	}

	public List<String> createOutSiteDistributeTask(String distributeOrderOid, String disInfoOids, Site site,
			boolean flag, boolean centerFlag, Site centerSite) {
		DistributePrincipalService principalService = DistributeHelper.getDistributePrincipalService();
		List<String> infoOidsList = SplitString.string2List(disInfoOids, ";");

		Map<String, List<DistributeInfo>> taskMap = new HashMap<String, List<DistributeInfo>>();
		for (String infoOid : infoOidsList) {
			DistributeInfo info = (DistributeInfo) Helper.getPersistService().getObject(infoOid);

			String outUserOid = info.getOutSignClassId() + ":" + info.getOutSignId();
			List<DistributeInfo> disInfoList = taskMap.get(outUserOid);
			if (disInfoList == null) {

				disInfoList = new ArrayList<DistributeInfo>();
				taskMap.put(outUserOid, disInfoList);
			}

			disInfoList.add(info);
		}

		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();

		Persistable object = Helper.getPersistService().getObject(distributeOrderOid);

		// 更新生命周期状态
		DistributeOrder disOrderObj = (DistributeOrder) object;

		//返回的组装的数据
		List<String> oidsList = new ArrayList<String>();
		//所有分发信息集合
		String distributeInfoOids = "";
		//生成外域分发任务集合
		String disTaskOids = "";
		for (List<DistributeInfo> disInfoList : taskMap.values()) {
			int i = 0;
			DistributeElecTaskService service = DistributeHelper.getDistributeElecTaskService();
			DistributeElecTask disElecTask = service.newDistributeElecTask();
			String infoOids = "";
			disTaskOids = disTaskOids + disElecTask.getOid() + ";";
			List<User> userList = new ArrayList<User>();
			for (DistributeInfo info : disInfoList) {
				i = i + 1;
				infoOids = infoOids + info.getOid() + ",";
				if (i == 1) {
					//生成任务(disInfoList中第一个分发信息)
					disElecTask.setNumber(disOrderObj.getNumber() + ConstUtil.C_ELECTASK_STR);
					disElecTask.setName(disOrderObj.getName() + ConstUtil.C_ELECTASK_STR);

					disElecTask.setNote("");
					disElecTask.setElecTaskType("1");

					disElecTask.setContextInfo(disOrderObj.getContextInfo());
					//添加域信息
					disElecTask.setDomainInfo(disOrderObj.getDomainInfo());

					//添加源站点信息
					disElecTask.setSourceSiteId(site.getInnerId());
					disElecTask.setSourceSiteClassId(site.getClassId());
					disElecTask.setSourceSiteName(site.getSiteData().getSiteName());

					//添加目标站点信息
					disElecTask.setTargetSiteId(info.getDisInfoId());
					disElecTask.setTargetSiteClassId(info.getInfoClassId());
					disElecTask.setTargetSiteName(info.getDisInfoName());
					
					//添加创建人修改人
					disElecTask.setManageInfo(disOrderObj.getManageInfo());
					disElecTask.getManageInfo().setCreateTime(System.currentTimeMillis());
					disElecTask.getManageInfo().setModifyTime(System.currentTimeMillis());

					//添加中心站点
					if (centerSite != null) {
						disElecTask.setCenterSiteId(centerSite.getInnerId());
						disElecTask.setCenterSiteClassId(centerSite.getClassId());
					}

					// 创建电子任务对象。
					service.createDistributeElecTask(disElecTask);

					// 更新分发信息的发送时间
					if (ConstUtil.LC_NOT_DISTRIBUT.getName().equals(info.getLifeCycleInfo().getStateName())) {
						info.setSendTime(System.currentTimeMillis());
						life.promoteLifeCycle(info);
					}
					Helper.getPersistService().update(info);

					//是中心模式且站点为数据中心
					if (centerFlag == true) {
						// 创建外域分发信息与电子任务link对象。
						createOutDistributeTaskInfoLink(disElecTask, info);
					} else {
						createDistributeTaskInfoLink(disElecTask, info);
					}

				} else {
					//生成分发信息与电子任务的link
					//是中心模式且站点为数据中心
					if (centerFlag == true) {
						// 创建外域分发信息与电子任务link对象。
						createOutDistributeTaskInfoLink(disElecTask, info);
					} else {
						createDistributeTaskInfoLink(disElecTask, info);
					}
					//分发信息添加发送时间，生命周期升级
					info.setSendTime(System.currentTimeMillis());
					if (ConstUtil.LC_NOT_DISTRIBUT.getName().equals(info.getLifeCycleInfo().getStateName())) {
						life.promoteLifeCycle(info);
					}
					Helper.getPersistService().update(info);
				}
				//为分发数据授权
				DistributeOrderObjectLink disOrderObjLink = (DistributeOrderObjectLink) Helper.getPersistService()
						.getObject(info.getDisOrderObjLinkClassId() + ":" + info.getDisOrderObjLinkId());
				DistributeObject disObject = (DistributeObject) disOrderObjLink.getTo();
				String outSignId = info.getOutSignId();
				if(!"".equals(outSignId) && outSignId != null){
					User receiveUser = UserHelper.getService().getUser(info.getOutSignId());
					userList.add(receiveUser);
				} else {
					//取得跨域协管员角色
					DmcRole dmcRole = DmcRoleHelper.getService().findDmcRoleByRoleID(ConstUtil.DIS_CROSS_DOMAIN_ROLE);
					List<DmcRoleUser> roleUsers = new ArrayList<DmcRoleUser>();
					if(dmcRole != null){
						//取得跨域协管员
						roleUsers = DmcRoleHelper.getService().findDmcRoleUserByRoleID(dmcRole.getInnerId());
						if (roleUsers != null && !roleUsers.isEmpty()) {
							//如果有跨域协管员
							Iterator<DmcRoleUser> it = roleUsers.iterator();
							while (it.hasNext()) {
								DmcRoleUser user = (DmcRoleUser) it.next();
								String productIIDsql = "SELECT MAX(T.LOCALPRODUCTID) AS PRODUCTID FROM PLM_COLLABORATION_PRODUCT P, PLM_COLLABORATION_TEMPDATA T"
										+ " WHERE T.DMCPRODUCTCLASSID || ':' || T.DMCPRODUCTID = P.CLASSID || ':' || P.INNERID"
										+ " AND P.PRODUCTIID = ?";
								//取得跨域协管员可看到的产品ID
								List<Map<String, Object>> proList = Helper.getPersistService().query(productIIDsql, user.getProduct().getProductIID());
								String productId = (String) proList.get(0).get("PRODUCTID");
								String objProductId = disObject.getContextInfo().getContextRef().getInnerId();
								//如果跨域协管员对应的产品ID是分发数据的上下文ID
								if(objProductId.equals(productId)){
									//判断是否有访问分发数据的权限
									User receiveUser = UserHelper.getService().getUser(user.getUserIID());
									userList.add(receiveUser);
								}
							}
						}
					}
				}
				for(User user : userList){
					//判断是否有业务对象浏览权限
					Domained domained = (Domained) Helper.getPersistService().getObject(disObject.getDataClassId() + ":" + disObject.getDataInnerId());
					boolean dataObjFlag = AccessControlHelper.getService().hasEntityPermission(user, Operate.DOWNLOAD, domained);
					//若没有权限
					if(dataObjFlag == false){
						//设置业务对象访问权限
						principalService.setPriviledge(domained, user);
					}
				}
			}
			distributeInfoOids = distributeInfoOids + infoOids + ";";
		}
		oidsList.add(distributeInfoOids);
		oidsList.add(disTaskOids);

		return oidsList;

	}
	
	public List<String> createCenterSiteDistributeTask(String distributeOrderOid, String disInfoOids, Site site,
			boolean flag, boolean centerFlag, Site centerSite) {
		List<String> infoOidsList = SplitString.string2List(disInfoOids, ";");
		
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();

		Persistable object = Helper.getPersistService().getObject(distributeOrderOid);
		
		DistributePrincipalService principalService = DistributeHelper.getDistributePrincipalService();

		// 更新生命周期状态
		DistributeOrder disOrderObj = (DistributeOrder) object;
		
		int i = 0;
		String infoOids = "";
		String disTaskOids = "";
		String distributeInfoOids = "";
		DistributeElecTask elecTask = null;
		//返回的组装的数据
		List<String> oidsList = new ArrayList<String>();
		for (String infoOid : infoOidsList) {
			DistributeInfo info = (DistributeInfo) Helper.getPersistService().getObject(infoOid);
			infoOids = infoOids + info.getOid() + ",";
			//判断分发信息
			if(i == 0){
				//创建电子任务
				DistributeElecTaskService service = DistributeHelper.getDistributeElecTaskService();
				DistributeElecTask disElecTask = service.newDistributeElecTask();
				disElecTask.setNumber(disOrderObj.getNumber() + ConstUtil.C_ELECTASK_STR);
				disElecTask.setName(disOrderObj.getName() + ConstUtil.C_ELECTASK_STR);

				disElecTask.setNote("");
				disElecTask.setElecTaskType("1");

				disElecTask.setContextInfo(disOrderObj.getContextInfo());
				//添加域信息
				disElecTask.setDomainInfo(disOrderObj.getDomainInfo());

				//添加源站点信息
				disElecTask.setSourceSiteId(site.getInnerId());
				disElecTask.setSourceSiteClassId(site.getClassId());
				disElecTask.setSourceSiteName(site.getSiteData().getSiteName());

				//添加目标站点信息
				disElecTask.setTargetSiteId(info.getDisInfoId());
				disElecTask.setTargetSiteClassId(info.getInfoClassId());
				disElecTask.setTargetSiteName(info.getDisInfoName());
				
				//添加创建人修改人
				disElecTask.setManageInfo(disOrderObj.getManageInfo());
				disElecTask.getManageInfo().setCreateTime(System.currentTimeMillis());
				disElecTask.getManageInfo().setModifyTime(System.currentTimeMillis());

				//添加中心站点
				if (centerSite != null) {
					disElecTask.setCenterSiteId(centerSite.getInnerId());
					disElecTask.setCenterSiteClassId(centerSite.getClassId());
				}

				// 创建电子任务对象。
				service.createDistributeElecTask(disElecTask);

				// 更新分发信息的发送时间
				if (ConstUtil.LC_NOT_DISTRIBUT.getName().equals(info.getLifeCycleInfo().getStateName())) {
					info.setSendTime(System.currentTimeMillis());
					life.promoteLifeCycle(info);
				}
				Helper.getPersistService().update(info);

				createOutDistributeTaskInfoLink(disElecTask, info);
				elecTask = disElecTask;
				disTaskOids = disElecTask.getOid() + ";";
			} else {
				//创建分发信息与电子任务的link
				//是中心模式且站点为数据中心
				createOutDistributeTaskInfoLink(elecTask, info);
				
				//分发信息添加发送时间，生命周期升级
				info.setSendTime(System.currentTimeMillis());
				if (ConstUtil.LC_NOT_DISTRIBUT.getName().equals(info.getLifeCycleInfo().getStateName())) {
					life.promoteLifeCycle(info);
				}
				Helper.getPersistService().update(info);
			}
			i = i + 1;
			//分发信息对应的分发数据授权
			DistributeOrderObjectLink disOrderObjLink = (DistributeOrderObjectLink) Helper.getPersistService()
					.getObject(info.getDisOrderObjLinkClassId() + ":" + info.getDisOrderObjLinkId());
			DistributeObject disObject = (DistributeObject) disOrderObjLink.getTo();
			
			//取得跨域协管员角色
			DmcRole dmcRole = DmcRoleHelper.getService().findDmcRoleByRoleID(ConstUtil.DIS_CROSS_DOMAIN_ROLE);
			List<DmcRoleUser> roleUsers = new ArrayList<DmcRoleUser>();
			if(dmcRole != null){
				//取得跨域协管员
				roleUsers = DmcRoleHelper.getService().findDmcRoleUserByRoleID(dmcRole.getInnerId());
				if (roleUsers != null && !roleUsers.isEmpty()) {
					//如果有跨域协管员
					Iterator<DmcRoleUser> it = roleUsers.iterator();
					while (it.hasNext()) {
						DmcRoleUser user = (DmcRoleUser) it.next();
						String productIIDsql = "SELECT MAX(T.LOCALPRODUCTID) AS PRODUCTID FROM PLM_COLLABORATION_PRODUCT P, PLM_COLLABORATION_TEMPDATA T"
								+ " WHERE T.DMCPRODUCTCLASSID || ':' || T.DMCPRODUCTID = P.CLASSID || ':' || P.INNERID"
								+ " AND P.PRODUCTIID = ?";
						//取得跨域协管员可看到的产品ID
						List<Map<String, Object>> proList = Helper.getPersistService().query(productIIDsql, user.getProduct().getProductIID());
						String productId = (String) proList.get(0).get("PRODUCTID");
						String objProductId = disObject.getContextInfo().getContextRef().getInnerId();
						//如果跨域协管员对应的产品ID是分发数据的上下文ID
						if(objProductId.equals(productId)){
							//判断是否有访问分发数据的权限
							User receiveUser = UserHelper.getService().getUser(user.getUserIID());

							//判断是否有业务对象浏览权限
							Domained domained = (Domained) Helper.getPersistService().getObject(disObject.getDataClassId() + ":" + disObject.getDataInnerId());
							boolean dataObjFlag = AccessControlHelper.getService().hasEntityPermission(receiveUser, Operate.DOWNLOAD, domained);
							//若没有权限
							if(dataObjFlag == false){
								//设置业务对象访问权限
								principalService.setPriviledge(domained, receiveUser);
							}
						}
					}
				}
			}
		}
		distributeInfoOids = infoOids + ";";
		oidsList.add(distributeInfoOids);
		oidsList.add(disTaskOids);

		return oidsList;

	}

	public DistributeTaskSite newDistributeTaskSite() {
		DistributeTaskSite dis = (DistributeTaskSite) PersistUtil.createObject(DistributeTaskSite.CLASSID);
		return dis;
	}

	/**
	 * 创建外域分发信息与电子任务link对象。
	 * 
	 * @param disTask
	 * @param disInfo
	 */
	private void createOutDistributeTaskInfoLink(DistributeTask disTask, DistributeInfo disInfo) {

		DistributeTaskDomainLink taskDomainLink = newDistributeTaskDomainLink();
		// 纸质任务内部标识
		taskDomainLink.setFromObject(disTask);
		// 分发信息内部标识
		taskDomainLink.setToObject(disInfo);
		// 任务类型（0：纸质任务，1：电子任务）
		taskDomainLink.setTaskType("1");
		Helper.getPersistService().save(taskDomainLink);
	}

	public DistributeTaskDomainLink newDistributeTaskDomainLink() {
		DistributeTaskDomainLink disLink = (DistributeTaskDomainLink) PersistUtil
				.createObject(DistributeTaskDomainLink.CLASSID);
		return disLink;
	}

}
