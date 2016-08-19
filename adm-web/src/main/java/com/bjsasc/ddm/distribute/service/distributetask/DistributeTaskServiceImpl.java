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
 * �ַ��������ʵ���ࡣ
 * 
 * @author gengancong 2013-2-22
 */
@SuppressWarnings({"unchecked","deprecation"})
public class DistributeTaskServiceImpl implements DistributeTaskService {
	
	/*
	 * ���� Javadoc��
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
		//���ַ���Ϣ���ַ����ݼ����ŵ�����Ϊ�����
		for (DistributePaperTask dis:distributePaperTask){
			//ͨ��ֽ�������innerid��classid��ȡ�ַ���Ϣ
			List<DistributeInfo> infos= disInfoService.getDistributeInfosByDistributePaperTaskOID(dis.getOid());
			for (DistributeInfo info : infos) {
				//��ֽ�������µ����зַ���Ϣ����Ϊ�ѷַ�
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
			//��ֽ���������ó��ѷַ�
			life.updateLifeCycleByStateId(dis,ConstUtil.LC_DISTRIBUTED.getId());
			Helper.getPersistService().update(dis);
			Persistable orderObj = Helper.getPersistService().getObject(disOrderOid);
			DistributeOrder disOrder = (DistributeOrder) orderObj;
			//��󽫷��ŵ�����Ϊ�ѷַ�
			life.promoteLifeCycle(disOrder);
			Helper.getPersistService().update(disOrder);
		}
	}

	public void createDistributeTask(String distributeOrderOids, String flag, String modify){
		/*
		 * �����ڱ�����������
		 */
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		//����ַ���Ϣ��־�����ŵ���������ַ���Ϣ��
		boolean outFlag = false;
		//����ֵ��true�Ƿַ�����false�ǻ������ٵ�
		boolean isDisOrRec = true;
		//sql��䣬ѡ��ַ���Ϣ
		String sqlForDis = "SELECT A .* "
				+ "FROM DDM_DIS_INFO A, "
				+ "DDM_DIS_ORDEROBJLINK B "
				+ "WHERE A .DISORDEROBJLINKID = B.INNERID "
				+ "AND A .DISORDEROBJLINKCLASSID = B.CLASSID "
				+ "AND B.FROMOBJECTCLASSID || ':' || B.FROMOBJECTID = ?";
		//sql��䣬ѡ�����������Ϣ
		String sqlForRecDes = "SELECT A .* "
				+ "FROM DDM_RECDES_INFO A, "
				+ "DDM_DIS_ORDEROBJLINK B "
				+ "WHERE A .DISORDEROBJECTLINKID = B.INNERID "
				+ "AND A .DISORDEROBJECTLINKCLASSID = B.CLASSID "
				+ "AND B.FROMOBJECTCLASSID || ':' || B.FROMOBJECTID = ?";
		//sql��䣬����ɸѡdisUrgent���Ƿ�Ӽ���
		String disUrgSql = "SELECT MAX (LINK .DISURGENT) AS DISURGENT "
				+ "FROM DDM_DIS_ORDER ORD, "
				+ "DDM_DIS_ORDEROBJLINK LINK "
				+ "WHERE LINK .fromObjectClassId || ':' || LINK .fromObjectId "
				+ "= ORD .classId || ':' || ORD .innerId "
				+ "AND ORD .classId || ':' || ORD .innerId = ?";
		//�ַ������������ٵ�����OID�б�
		List<String> distributeOrderOidList = SplitString.string2List(distributeOrderOids, ",");
		
		/*
		 * �߼�����ʵ������
		 */
		boolean userSecrityFlag = ContextUtil.isEnableUserSecrityController();

		for(String distributeOrderOid:distributeOrderOidList){
			/*
			 * �����ڱ�����������
			 */
			Persistable object = Helper.getPersistService().getObject(distributeOrderOid);
			//���·��ŵ�����������״̬
			DistributeOrder disOrderObj = (DistributeOrder)object;
			//������Ϣ�б�
			List<DistributeInfo> disInfoList = new ArrayList<DistributeInfo>();
			//��������ķַ���Ϣ�б�
			List<DistributeInfo> outSiteDisInfoList = new ArrayList<DistributeInfo>();
			//����������Ϣ�б�
			List<RecDesInfo> recDesInfoList = new ArrayList<RecDesInfo>();
			//��ȡ���ŵ������ͣ������ж��Ƿַ������ǻ������ٵ�
			String orderType = disOrderObj.getOrderType();
			//ֽ�������ʶ��Ĭ��Ϊfalse
			boolean paperTaskFlag = false;
			//��������ֽ�������ʶ��Ĭ��Ϊfalse
			boolean recDesPaperTaskFlag = false;
			//վ��oid�б�
			List<String> siteList = new ArrayList<String>();
			//�����ݿ��л�ȡָ��oid�ļӼ���Ϣ
			List<Map<String,Object>> linkList = PersistHelper.getService().query(disUrgSql,distributeOrderOid);
			String disUrgent = linkList.get(0).get("DISURGENT").toString();
			//�ж��Ƿ���ֽ������,0Ϊ�ޣ�1Ϊ��
			int havePaper=0;
			/*
			 * �߼�����ʵ������
			 */
			//����order�����жϣ��鿴�Ƿַ������ǻ�����������
			//(�ַ�����isDisOrRecΪtrue��������������isDisOrRecΪfalse)
			if(orderType.equals(ConstUtil.C_ORDERTYPE_0) ||
					orderType.equals(ConstUtil.C_ORDERTYPE_1)){
				isDisOrRec = true;
			}else if(orderType.equals(ConstUtil.C_ORDERTYPE_2) ||
					orderType.equals(ConstUtil.C_ORDERTYPE_3)){
				isDisOrRec = false;
			}
			
			//�жϷ��ŵ��ύ�ˣ�������߹�������ȡ�������������ˣ�������߹�������ȡ��ǰ�û���
			if(StringUtil.isNull(modify)){
				User currentUser = SessionHelper.getService().getUser();
				disOrderObj.getManageInfo().setModifyBy(currentUser);
			}else{
				User user = UserHelper.getService().getUser(modify);
				disOrderObj.getManageInfo().setModifyBy(user);
			}
			Helper.getPersistService().update(disOrderObj);
			
			if("true".equals(flag)){
				//���ŵ������������������ŵ��ύ�������񣬲��߹�������
				life.promoteLifeCycle(disOrderObj);
			}
			//��ӷ����ˣ�����վ�㣨����
			this.setOrderUserInfo(disOrderObj);
			
			//�ַ������ɷַ�����ķ���
			if(isDisOrRec){
				disInfoList = PersistHelper.getService().query(sqlForDis,DistributeInfo.class,distributeOrderOid);
				if(disInfoList.size() == 0 ){
					throw new RuntimeException(disOrderObj.getName()+"û�зַ���Ϣ����ӷַ���Ϣ���ύ����");
				}
				boolean canDisFlag = DistributeHelper.getDistributeInfoService().canDisBySecurityLevel(distributeOrderOid);
				if(!canDisFlag){
					throw new RuntimeException("�ַ����ݵ��ܼ����ڷַ���Ա���ܼ��������ύ����");
				}
				
				//�����ַ���Ϣ����ֽ�����������
				for(DistributeInfo disInfo:disInfoList){
					//���ĵ�����֪ͨ���ķַ����и��ٴ���
					this.addTraceInfoForECOAndTNO(disInfo);
					
					//ֻ������������
					if(ConstUtil.C_TASKTYPE_OUTSIGN.equals(disInfo.getDisMediaType())){
						//��վ��list��һ�����ŵ�����վ�����
						String siteOid = disInfo.getInfoClassId()+":"+disInfo.getDisInfoId();
						if(!siteList.contains(siteOid)){
							siteList.add(siteOid);
						}
						//���ŵ��д�������ַ���Ϣ
						outSiteDisInfoList.add(disInfo);
						outFlag = true;
						continue;
					}
					if(ConstUtil.C_TASKTYPE_PAPER.equals(disInfo.getDisMediaType())){
						//Ϊֽ�������ǩ��ֵΪ��
						paperTaskFlag = true;
						havePaper = 1;
						continue;
					}else if(ConstUtil.C_TASKTYPE_ELEC.equals(disInfo.getDisMediaType())){
						//��������
						createDistributeElecTask(disInfo,distributeOrderOid, disOrderObj, life);
					}
				}
			}
			//�������ٵ����ɻ�����������ķ��� 
			else{
				recDesInfoList = PersistHelper.getService().query(sqlForRecDes,RecDesInfo.class,distributeOrderOid);
				if(recDesInfoList.size() == 0 ){
					throw new RuntimeException(disOrderObj.getName()+"û�л���������Ϣ����ӻ���������Ϣ���ύ����");
				}
				
				//��������������Ϣ����ֽ�����������
				for(RecDesInfo recDesInfo:recDesInfoList){				
					if(ConstUtil.C_TASKTYPE_PAPER.equals(recDesInfo.getDisMediaType())){
						//Ϊ��������ֽ�������ǩ��ֵΪ��
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
				service.setLifeCycleState(lifeCycleManagedDisOrder, state);//�����ŵ��Ķ�������Ϊ"�ѷַ�"״̬
				//life.promoteLifeCycle(disOrderObj);//�����ŵ��Ķ�������Ϊ"�ѷַ�"״̬
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
						//�ַ����ݶ�Ӧ�ķַ���Ϣδȫ������
						infoFlag = false;
						break;
					}
				}
				//�ַ�����������������(�ַ���Ϣȫ������)
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
			//�ַ�ֽ������
			if(paperTaskFlag){
				createDistributePaperTask(disOrderObj, disUrgent, disInfoList);
				Persistable obj = Helper.getPersistService().getObject(distributeOrderOid);
				DistributeOrder dis = (DistributeOrder) obj;
				Context context = dis.getContextInfo().getContext();
				List<String> objects = new ArrayList<String>();
				objects.add(SessionHelper.getService().getUser().getName());//�û���
				objects.add("�ύ");
				objects.add(dis.getNumber());
				objects.add("�ӹ���");
				int level=1;
				String logType="module";
				String objName=dis.getName();
				int objectSecurity=0;
				String moduleSource="���Ź���";
				String objectType="���ŵ�";
				String operation="�ύ���ӹ���";
				String messageId="ddm.log.submit";
				AuditLogHelper.getService().addLog( level, logType, context, dis.getInnerId(), dis.getClassId(), objName, objectSecurity, moduleSource, objectType, operation, messageId, objects);
			}
			
			//��������ֽ������
			if(recDesPaperTaskFlag){
				createRecDesPaperTask(disOrderObj, disUrgent, recDesInfoList,life);
			}
			
		}
	}
	
	/**
	 * ���ݸ����ķַ���Ϣ���жϣ�����ַ�������ECO��TNO��Ϊ�����׷����Ϣ
	 * 
	 * @param disInfo ���ڳ�ȡ������Ϣ�ķַ���Ϣ���ַ����ݣ�׷����Ϣ��
	 * @modify Sun Zongqing
	 */
	private void addTraceInfoForECOAndTNO(DistributeInfo disInfo){
		DistributeOrderObjectLink disOrderObjLink = (DistributeOrderObjectLink)Helper.getPersistService()
				.getObject(disInfo.getDisOrderObjLinkClassId()+":"+disInfo.getDisOrderObjLinkId());
		//�����ַ����󶨵ķַ�����
		DistributeObject disObject = (DistributeObject)disOrderObjLink.getTo();
		//�ַ�������ָ��������ԭ��
		Persistable disObj = Helper.getPersistService().getObject(disObject.getDataClassId()+":"+disObject.getDataInnerId());
		if(disObj instanceof ECO || disObj instanceof TNO){
			//���ĵ�������֪ͨ���ķַ����и��ٴ���
			DisInfoIsTrackService service = DistributeHelper.getDisInfoIsTrackService();
			//ͨ��disInfoOid���׷����Ϣ������list��sizeֻ��1
			List<DisInfoIsTrack> disInfoIsTrackList = service.getDisInfoIsTrackByDisInfoOid(disInfo.getOid());
			if(disInfoIsTrackList.size() !=0){
				//�õ��ı���
				Persistable department = null;
				Site site = null;
				String deptName = "";
				Change change = (Change) disObj;
				int mediaType = Integer.parseInt(disInfo.getDisMediaType());
				
				//���ڷַ�
				if(mediaType == 0||mediaType == 1){
					if(ConstUtil.C_DISINFOTYPE_ORG.equals(disInfo.getDisInfoType())){
						department = Helper.getPersistService().
								getObject(disInfo.getInfoClassId()+":"+disInfo.getDisInfoId());
						deptName = disInfo.getDisInfoName();
					}
					//���ڷַ�����Աʱ�����ӿڴ���Ա��Ҫ������λ�͵�λ��
					else{
						User user = (User)Helper.getPersistService().getObject(disInfo.getInfoClassId()+":"+disInfo.getDisInfoId());
						department = user.getOrganization();
						deptName = user.getOrganizationName();
					}
				}
				//����ַ�ʱ�����ӿڴ���Ա���ڵ�վ���վ������
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
	 * �����ṩ�Ĳ���������ȫ���Եĵ��ӷַ����񣬰����ظ��ַ���������ļ��
	 * 
	 * @param disInfo ��Ҫ�������������ķַ���Ϣ
	 * @param distributeOrderOid �ַ�����oid�����ڼ���Ƿ��и��û��ķַ���Ϣ
	 * @param disOrderObj ��Ҫ������������ķַ��������ڻ�ȡ��š����ơ������������Ϣ��Ȼ��ֵ����������
	 * @param life �ַ������������ڷ������ڸı�ַ���Ϣ����������
	 * @modify zhang guoqiang
	 * @date 2014/10/28
	 */
	private void createDistributeElecTask(DistributeInfo disInfo, String distributeOrderOid, DistributeOrder disOrderObj,DistributeLifecycleService life){
		
		//�Զ��رܵ��������ٴ��ύ
		//ȡ���÷ַ���Ϣ��Ӧ��taskInfoLink�����ȫ����Ϣ
		String hql = "from DistributeTaskInfoLink t "
				+ "where t.toObjectRef.innerId=? "
				+ "and t.toObjectRef.classId=?";
		List<DistributeTaskInfoLink> links = PersistHelper.getService().find(hql, disInfo.getInnerId(),
				disInfo.getClassId());
		if(links == null || links.isEmpty()){
			//sql��䣬���ڻ�ȡ�����������Ϣ
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
			//���ڸ��û��ĵ�����Ϣ
			List<DistributeElecTask> taskList = PersistHelper.getService()
							.query(taskSql, 
									DistributeElecTask.class,
									disInfo.getDisInfoId(),
									disInfo.getInfoClassId(),
									"1",
									ConstUtil.LC_NOT_SIGNED.getName(),
									distributeOrderOid);
			//�������û��ĵ����������(���Link)
			if(taskList.size()>0){
				createDistributeTaskInfoLink(taskList.get(0), disInfo);
				//�ַ���Ϣ��ӷ���ʱ�䣬������������
				disInfo.setSendTime(System.currentTimeMillis());
				if(ConstUtil.LC_NOT_DISTRIBUT.getName().equals(disInfo.getLifeCycleInfo().getStateName())){
					life.promoteLifeCycle(disInfo);
				}
				Helper.getPersistService().update(disInfo);
				setUserPrivilege(disInfo);
			}else{
				//��ȡ��������ַ����񣬲������µĵ�������
				DistributeElecTaskService service = DistributeHelper.getDistributeElecTaskService();
				DistributeElecTask disElecTask = service.newDistributeElecTask();
				//���õ�������ı�š����ƺͱ�ע
				disElecTask.setNumber(disOrderObj.getNumber()+ConstUtil.C_ELECTASK_STR);
				disElecTask.setName(disOrderObj.getName()+ConstUtil.C_ELECTASK_STR);
				disElecTask.setNote("");
				//�����������ӡ����յ�λ/��Ա����������Ϣ�����������ֶ�DISINFONAME,DISINFOID,INFOCLASSID���ӷַ���Ϣ�л�ȡ���ֵ 
				disElecTask.setDisInfoId(disInfo.getDisInfoId());
				disElecTask.setDisInfoName(disInfo.getDisInfoName());
				disElecTask.setInfoClassId(disInfo.getClassId());
				
				//Ϊ�������������������Ϣ
				disElecTask.setContextInfo(disOrderObj.getContextInfo());
				//Ϊ���������������Ϣ
				disElecTask.setDomainInfo(disOrderObj.getDomainInfo());
				//Ϊ����������ӹ�����Ϣ
				ManageInfo manageInfo = new ManageInfo();
				long currentTime = System.currentTimeMillis();
				manageInfo.setCreateBy(disOrderObj.getManageInfo().getModifyBy());
				manageInfo.setModifyBy(disOrderObj.getManageInfo().getModifyBy());
				manageInfo.setCreateTime(currentTime);
				manageInfo.setModifyTime(currentTime);
				disElecTask.setManageInfo(manageInfo);
				//Ϊ��������������������Ϣ
				service.createDistributeElecTask(disElecTask);
				//���·ַ���Ϣ�ķ���ʱ��
				disInfo.setSendTime(System.currentTimeMillis());
				
				if(ConstUtil.LC_NOT_DISTRIBUT.getName().equals(disInfo.getLifeCycleInfo().getStateName())){
					life.promoteLifeCycle(disInfo);
				}
				Helper.getPersistService().update(disInfo);
				
				//�����ַ���Ϣ���������link����
			
				createDistributeTaskInfoLink(disElecTask, disInfo);
				setUserPrivilege(disInfo);
			}
		}
	}

	/**
	 * �����ṩ�Ĳ���������ȫ���Ե�ֽ�ʷַ�����
	 * 
	 * @param disOrderObj ��Ҫ����ֽ������ķַ��������ڻ�ȡ��š����ơ������������Ϣ��Ȼ��ֵ��ֽ������
	 * @param disUrgent �Ӽ�״̬����
	 * @param recDesInfoList ����������Ϣ�б�������ֽ������������(link)
	 * @param life �ṩ�������ڷ���ı���
	 * @return �����ɵ�TaskInfoLink�б�
	 * @author Sun Zongqing
	 */
	private void createRecDesPaperTask(DistributeOrder disOrderObj,String disUrgent,List<RecDesInfo> recDesInfoList,DistributeLifecycleService life){
		RecDesPaperTaskService service = DistributeHelper.getRecDesPaperTaskService();
		//����һ���µ�ֽ������
		RecDesPaperTask paperTask = service.newRecDesPaperTask();
		//Ϊֽ������ֵ��������š����ơ���ע���Ӽ�״������������Ϣ������Ϣ������������Ϣ��
		paperTask.setNumber(disOrderObj.getNumber()+ConstUtil.C_PAPERTASK_STR);
		paperTask.setName(disOrderObj.getName()+ConstUtil.C_PAPERTASK_STR);
		paperTask.setNote("");
		paperTask.setDisUrgent(disUrgent);
		paperTask.setContextInfo(disOrderObj.getContextInfo());
		paperTask.setDomainInfo(disOrderObj.getDomainInfo());
		//Ϊֽ��������ӹ�����Ϣ
		ManageInfo manageInfo = new ManageInfo();
		long currentTime = System.currentTimeMillis();
		manageInfo.setCreateBy(disOrderObj.getManageInfo().getModifyBy());
		manageInfo.setModifyBy(disOrderObj.getManageInfo().getModifyBy());
		manageInfo.setCreateTime(currentTime);
		manageInfo.setModifyTime(currentTime);
		paperTask.setManageInfo(manageInfo);
		//Ϊֽ�������������������Ϣ
		service.setRecDesPaperTaskLifecycle(paperTask);;
		
		for(RecDesInfo recDesInfo:recDesInfoList){
			if(ConstUtil.C_TASKTYPE_PAPER.equals(recDesInfo.getDisMediaType())){
				//�����ַ���Ϣ��ֽ�������link����
				createRecDesTaskInfoLink(paperTask, recDesInfo);
				recDesInfo.setSendTime(System.currentTimeMillis());
				life.promoteLifeCycle(recDesInfo);
			}
		}
	}
	
	/**
	 * �����ṩ�Ĳ���������ȫ���Ե�ֽ�ʷַ�����
	 * 
	 * @param disOrderObj ��Ҫ����ֽ������ķַ��������ڻ�ȡ��š����ơ������������Ϣ��Ȼ��ֵ��ֽ������
	 * @param disUrgent �Ӽ�״̬����
	 * @param disInfoList �ַ���Ϣ�б�������ֽ������������(link)
	 * @return �����ɵ�TaskInfoLink�����б�
	 * @modify Sun Zongqing
	 */
	private void createDistributePaperTask(DistributeOrder disOrderObj,String disUrgent,List<DistributeInfo> disInfoList){
		//�ô��洢�²�����������Ϣ���Ӷ���
		DistributePaperTaskService service = DistributeHelper.getDistributePaperTaskService();
		//����һ���µ�ֽ������
		DistributePaperTask paperTask = service.newDistributePaperTask();
		//Ϊֽ������ֵ��������š����ơ���ע���Ӽ�״������������Ϣ������Ϣ������������Ϣ��
		paperTask.setNumber(disOrderObj.getNumber()+ConstUtil.C_PAPERTASK_STR);
		paperTask.setName(disOrderObj.getName()+ConstUtil.C_PAPERTASK_STR);
		paperTask.setNote("");
		paperTask.setDisUrgent(disUrgent);
		paperTask.setContextInfo(disOrderObj.getContextInfo());
		paperTask.setDomainInfo(disOrderObj.getDomainInfo());
		paperTask.setManageInfo(disOrderObj.getManageInfo());
		//Ϊֽ��������ӹ�����Ϣ
		ManageInfo manageInfo = new ManageInfo();
		long currentTime = System.currentTimeMillis();
		manageInfo.setCreateBy(disOrderObj.getManageInfo().getModifyBy());
		manageInfo.setModifyBy(disOrderObj.getManageInfo().getModifyBy());
		manageInfo.setCreateTime(currentTime);
		manageInfo.setModifyTime(currentTime);
		paperTask.setManageInfo(manageInfo);
		paperTask.setDisOrderCreator(disOrderObj.getCreateByName());
		//Ϊֽ�������������������Ϣ
		service.createDistributePaperTask(paperTask);
		for(DistributeInfo disInfo:disInfoList){
			if(ConstUtil.C_TASKTYPE_PAPER.equals(disInfo.getDisMediaType())){
				createDistributeTaskInfoLink(paperTask, disInfo);
			}
		}
	}
	
	/**
	 * Ϊ�����еķַ����ṩUser��Ϣ
	 * 
	 * @param disOrderObj ��Ҫ���User��Ϣ�ķַ���
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
	 * �����ַ���Ϣ��ֽ������link����
	 * 
	 * @param disTask
	 *            DistributeObject
	 */
	private void createDistributeTaskInfoLink(DistributeTask disTask, DistributeInfo disInfo) {

		DistributeTaskInfoLinkService service = DistributeHelper.getDistributeTaskInfoLinkService();

		DistributeTaskInfoLink linkDisObject = service.newDistributeTaskInfoLink();
		// ֽ�������ڲ���ʶ
		linkDisObject.setFromObject(disTask);
		// �ַ���Ϣ�ڲ���ʶ
		linkDisObject.setToObject(disInfo);
		// �������ͣ�0��ֽ������1����������,3:��������ֽ������,4:�������ٵ�������
		linkDisObject.setTaskType(getTaskType(disTask));
		service.createDistributeTaskInfoLink(linkDisObject);
	}
	
	/**
	 * �����ַ���Ϣ��ֽ������link����
	 * 
	 * @param disTask
	 *            DistributeObject
	 */
	private void createRecDesTaskInfoLink(DistributeTask disTask, RecDesInfo disInfo) {

		DistributeTaskInfoLinkService service = DistributeHelper.getDistributeTaskInfoLinkService();

		DistributeTaskInfoLink linkDisObject = service.newDistributeTaskInfoLink();
		// ֽ�������ڲ���ʶ
		linkDisObject.setFromObject(disTask);
		// �ַ���Ϣ�ڲ���ʶ
		linkDisObject.setToObject(disInfo);
		// �������ͣ�0��ֽ������1����������,3:��������ֽ������,4:�������ٵ�������
		linkDisObject.setTaskType(getTaskType(disTask));
		service.createDistributeTaskInfoLink(linkDisObject);
	}
	
	/**
	 * �����ַ�������ַ���Ϣ������
	 * 
	 * @param disTask ��Ҫ�������ӵ�Task
	 * @param disInfo ��Ҫ�������ӵ�info�б�
	 * @return �²�����TaskInfoLink����
	 * @author Sun Zongqing
	 * @date 2014/7/3
	 */
	private DistributeTaskInfoLink getDistributeTaskInfoLinkObject(DistributeTask disTask, DistributeInfo disInfo) {

		DistributeTaskInfoLinkService service = DistributeHelper.getDistributeTaskInfoLinkService();

		DistributeTaskInfoLink linkDisObject = service.newDistributeTaskInfoLink();
		// ֽ�������ڲ���ʶ
		linkDisObject.setFromObject(disTask);
		// �ַ���Ϣ�ڲ���ʶ
		linkDisObject.setToObject(disInfo);
		// �������ͣ�0��ֽ������1����������,3:��������ֽ������,4:�������ٵ�������
		linkDisObject.setTaskType(getTaskType(disTask));
		return linkDisObject;
	}
	
	/**
	 * �����ṩ���������Ϣ������������Ϣ������
	 * 
	 * @param disTask ��Ҫ�������ӵ�����
	 * @param disInfo ��Ҫ�������ӵ���Ϣ
	 * @return �����ɵ�������Ϣ���Ӷ���
	 * @author Sun Zongqing
	 */
	private DistributeTaskInfoLink getRecDesTaskInfoLinkObject(DistributeTask disTask, RecDesInfo disInfo) {

		DistributeTaskInfoLinkService service = DistributeHelper.getDistributeTaskInfoLinkService();

		DistributeTaskInfoLink linkDisObject = service.newDistributeTaskInfoLink();
		// ֽ�������ڲ���ʶ
		linkDisObject.setFromObject(disTask);
		// �ַ���Ϣ�ڲ���ʶ
		linkDisObject.setToObject(disInfo);
		// �������ͣ�0��ֽ������1����������,3:��������ֽ������,4:�������ٵ�������
		linkDisObject.setTaskType(getTaskType(disTask));
		return linkDisObject;
	}

	
	/**
	 * ȡ�����������
	 * 
	 * @param disObject ��Ҫ��ȡ�������͵��������
	 * @return �õ�����������
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
					// ������������
					life.promoteLifeCycle(dis);
					//�ύ��ӡ�ӹ������¼��־
					Context context= dis.getContextInfo().getContext();
					List<String> objects = new ArrayList<String>();
					objects.add(SessionHelper.getService().getUser().getName());//�û���
					objects.add("�ύ��ӡ�ӹ�����");
					objects.add(dis.getNumber());
					int level=1;
					String logType="module";
					String objName=dis.getName();
					int objectSecurity=0;
					String moduleSource="���Ź���";
					String objectType="�ַ�����";
					String operation="�ύ��ӡ�ӹ�����";
					String messageId="ddm.log.updateDistributeTask";
					AuditLogHelper.getService().addLog( level, logType, context, 
							dis.getInnerId(), dis.getClassId(), objName, objectSecurity, 
							moduleSource, objectType, operation, messageId, objects);
				} else if (LIFECYCLE_OPT.DEMOTE == opt) {
					// �������ڽ���
					life.demoteLifeCycle(dis);
				} else if (LIFECYCLE_OPT.REJECT == opt) {
					// �������ھܾ�
					life.rejectLifeCycle(dis);
				}
				Helper.getPersistService().update(dis);
				
				if(dis.getLifeCycleInfo().getStateName().equals(ConstLifeCycle.LC_DISTRIBUTED.getName())){
					//��������ⷢ����ӡ֮ǰ�Ľڵ��У��������ύ����һ����������״̬Ϊ�ѷַ������������������жϣ����ַ���Ϣ���ַ����ݼ����ŵ�����Ϊ�����
					String innerid = dis.getInnerId();
					String classid = dis.getClassId();
					String sql = "select t.* from  ddm_dis_info t , ddm_dis_taskinfolink b "
							+ " where t.innerid = b.toobjectid and t.classid = b.toobjectclassid "
							+ " and b.fromObjectId = ? and b.fromObjectClassId = ?";
					List<DistributeInfo> infos = PersistHelper.getService().query(sql, DistributeInfo.class, innerid,classid);
					for (DistributeInfo info : infos) {
						//��ֽ�������µ����зַ���Ϣ����Ϊ�ѷַ�
						if (ConstUtil.LC_NOT_DISTRIBUT.getName().equals(info.getLifeCycleInfo().getStateName())) {
							life.promoteLifeCycle(info);
							info.setSendTime(System.currentTimeMillis());
							infoList1.add(info);
						}
					}
					if (infoList1 != null && infoList1.size() > 0) {
						Helper.getPersistService().update(infoList1);
					}
					//��ֽ�������µ����зַ���������Ϊ�ѷַ�
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
					
					//��󽫷��ŵ�����Ϊ�ѷַ�
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
				throw new RuntimeException("DistribtueTaskServiceImpl���У�updateDistributeTask������������ʧ�ܣ�", e);
			}
		}
	}

	private ReturnReason newReturnReason() {
		ReturnReason reason = (ReturnReason) PersistUtil.createObject(ReturnReason.CLASSID);
		return reason;
	}

	/**
	 * Ϊ�ַ���Ϣ�Ľ����߸���ַ���Ϣ�����ķַ����ݵķ���Ȩ��
	 * 
	 * @param disInfo �ַ���Ϣ
	 * @modify zhang guoqiang
	 * @date 2014/10/28
	 */
	private void setUserPrivilege(DistributeInfo disInfo) {
		DistributePrincipalService principalService = DistributeHelper.getDistributePrincipalService();
		List<User> userList = new ArrayList<User>();
		//�жϷַ����ͣ�0Ϊ��λ��1Ϊ��Ա
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
		//���ݷַ���Ϣȡ�÷ַ�����
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

		//ȡ��ҵ���������Ϣ
		Domained domained = (Domained) PersistHelper.getService().getObject(
				disObject.getDataClassId() + ":" + disObject.getDataInnerId());

		//�����λû�ж�������Ա��ʱ��userListΪ�գ�������Ȩ��
		for(User user : userList){
			boolean flag = AccessControlHelper.getService().hasEntityPermission(user, Operate.DOWNLOAD, domained);
			if(flag == false){
				//���÷���Ȩ��
				principalService.setPriviledge(domained,user);
			}
		}
	}
	
	/**
	 * Ϊ����������Ϣ�Ľ����߸������������Ϣ�����ķַ����ݵķ���Ȩ��
	 * 
	 * @param recDesInfo ����������Ϣ
	 * @modify zhang guoqiang
	 * @date 2014/10/28
	 */
	private void setUserPrivilegeForRecDes(RecDesInfo recDesInfo) {
		DistributePrincipalService principalService = DistributeHelper.getDistributePrincipalService();
		List<User> userList = new ArrayList<User>();
		//�жϷַ����ͣ�0Ϊ��λ��1Ϊ��Ա
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
		//���ݻ���������Ϣȡ�÷ַ�����
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
		//ȡ��ҵ���������Ϣ
		Domained domained = (Domained) PersistHelper.getService().getObject(
				disObject.getDataClassId() + ":" + disObject.getDataInnerId());

		//�����λû�ж�������Ա��ʱ��userListΪ�գ�������Ȩ��
		for(User user : userList){

			boolean flag = AccessControlHelper.getService().hasEntityPermission(user, Operate.DOWNLOAD, domained);
			if(flag == false){
				//���÷���Ȩ��
				principalService.setPriviledge(domained,user);
			}
		}
	}

	//���ͷ��������Ϣ
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

			//ȡ����ط��ŵ�
			String orderSql = "select disOrder.* from DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink,DDM_DIS_ORDER disOrder"
					+ " where objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
					+ " and disOrder.innerId = objLink.fromObjectId and disOrder.classId = objLink.fromObjectClassId"
					+ " and disInfo.classId || ':' || disInfo.innerId = ?";
			List<DistributeOrder> orderList = Helper.getPersistService().query(orderSql, DistributeOrder.class, oid);
			DistributeOrder order = orderList.get(0);

			//ȡ�÷ַ���Ϣ����
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

			//���ݷַ���Ϣ���ұ��׷��
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

			// ��ȡϵͳ����ģʽ
			DmcConfigService dmcConfigService = DmcConfigHelper.getService();
			boolean isDcDeployModel = dmcConfigService.getIsDealOnDC();

			List<Object> dataObjList = new ArrayList<Object>();

			if (isDcDeployModel == false) {
				//����ģʽ��ʵ��ַ�
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

				//����ģʽ�ҵ�ǰվ�㲻����������
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
					// �ж��Ƿ���ʵ���ǩģʽ
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

			//flagΪtrue��ʾΪ���ķ��͸����շ�
			if (flag == true) {
				String taskOidSql = "SELECT MAX (ELEC.CLASSID || ':' || ELEC.INNERID) AS OID FROM DDM_DIS_ELECTASK ELEC, DDM_DIS_TASKDOMAINLINK LINK, DDM_DIS_INFO INFO"
						+ " WHERE LINK.FROMOBJECTCLASSID || ':' || LINK.FROMOBJECTID = ELEC.CLASSID || ':' || ELEC.INNERID"
						+ " AND LINK.TOOBJECTCLASSID || ':' || LINK.TOOBJECTID = INFO.CLASSID || ':' || INFO.INNERID"
						+ " AND INFO.CLASSID || ':' || INFO.INNERID = ?";

				List<Map<String, Object>> oidList = PersistHelper.getService().query(taskOidSql, oid);
				String taskOid = oidList.get(0).get("OID").toString();
				DistributeElecTask task = (DistributeElecTask) Helper.getPersistService().getObject(taskOid);
				Site site = SiteHelper.getSiteService().findSiteById(task.getSourceSiteId());
				//�������
				Map<String, String> centerReqParamMap = new HashMap<String, String>();
				reqParamMap.put("flag", "1");
				reqParamMap.put("taskOid", taskOid);
				reqParamMap.put("opt", "PROMOTE");

				//��������ͬ��(����)
				transferService.sendRequest(UUID.getUID(), "����ͬ���������ڷ���������", MessageTypeconstant.REPLY_SEND_DISTRIBUTE,
						null, site, centerReqParamMap, null, TransferConstant.REQTYPE_SYN);
			}
		}
	}

	// ���ͷ��������Ϣ
	public void sendToOutSite(DistributeOrder order,
			List<DistributeInfo> outSiteDisInfoList, List<String> siteList,
			boolean flag) {
		Map<String, String> reqParamMap = new HashMap<String, String>();

		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		//�Ƿ���ڵ��ӷַ�
		String isExistDispatchSite = "false";
		//���ӷַ��ķַ���ϢOIDS
		String allDispatchDisInfoOids = "'";
		
		for (String siteOid : siteList) {
			List<String> idList = SplitString.string2List(siteOid, ":");
			Site targetSite = SiteHelper.getSiteService().findSiteById(
					idList.get(1));

			// TODO��汾�ַ���Ϣ����
			// ��汾���뷽ϵͳ�汾
			String importTargetSiteSystemVersion = "";
			// ͨ�����ҿ�汾���뷽�汾���ã���������ã��͸��ݸ����ð汾���е��������û�����ã�Ĭ��A5
			ImportVersionCfg cfg = ImportVersionCfgHelper
					.getImportVersionCfgService().findCfgByTargetAndType(
							targetSite.getInnerId(),
							BusinessTypeConstant.BUSINESS_TYPE_DISTRIBUTE);
			if (cfg != null) {
				importTargetSiteSystemVersion = cfg.getSystemVersion();
			} else {
				importTargetSiteSystemVersion = DTSiteConstant.DTSITE_APPVERSION_5;
				//throw new RuntimeException("ȡ��ϵͳ����ʱ���õĿ�汾���뷽�汾����ʧ��!վ�����ƣ�"+ targetSite.getDisplayName());
			}
			// ���͵�A3.5
			if (DTSiteConstant.DTSITE_APPVERSION_3_5
					.equalsIgnoreCase(importTargetSiteSystemVersion)) {

				// ϵͳ����ʱ���÷���
				DmcConfigService dmcConfigService = DmcConfigHelper
						.getService();
				// ��ȡ�����Ƿ�ʹ��ʵ��ģʽ
				String isEntityDistributeModel = dmcConfigService
						.getDmcConfigValue(ConstUtil.IS_ENTITY_DISTRIBUTEMODE_YES);

				//ʵ��ַ�
				if ("true".equalsIgnoreCase(isEntityDistributeModel)) {
					// �ַ���Ϣ���� ʵ��ַ�
					String sendMessageType = Message4XTypeConstant.DISTRIBUTE_DISTRIBUTE_A5;
				
//					// ʹ�ò����е�outSiteDisInfo�����ٷ���һ�����ݿ�
//					// �������ŵ��з���վ��
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
						//��վ��OID
						String outSiteOid = info.getInfoClassId()+":"+info.getDisInfoId();
						if(siteOid.equals(outSiteOid)){
							infoOids = infoOids + info.getOid() + "','";
							//���·ַ���Ϣ��״̬Ϊ�ѷַ�
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
	
					
					//ȡ�÷��Ͷ��󣺷ַ�����
					List<DistributeObject> sendForA3DisObjectList = getSendForA3DisObjectList(objList);
	
					String disObjOids = "'";
					for (DistributeObject obj : sendForA3DisObjectList) {
						disObjOids = disObjOids + obj.getOid() + "','";
					}
					disObjOids = disObjOids.substring(0, disObjOids.length() - 2);
					
					// ���ݷַ�����ȡ�÷ַ�������Դ�����Լ��������������link
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
					
					// ���ݷַ�����ȡ�÷ַ�������Դ�����Լ��������������link
					List<Object> sendObjectList = getAllForA3ObjectList(sendForA3DisObjectList);
	
					if (sendObjectList == null || sendObjectList.size() == 0) {
						continue;
					}
	
					Map<String, String> map = new HashMap<String, String>();
					map.put(A3A5DataConvertUtil.ORDERIID, order.getInnerId());
					// �����ַ�����
					File exportFIle = ExportFileHelper.getExportFileService()
							.exportObject(sendDataObjectList,DTSiteConstant.DTSITE_APPVERSION_3_5, map);
					String filePath = exportFIle.getPath();
					TransferObjectService transferService = TransferObjectHelper
							.getTransferService();
					// �������
					Map<String, String> reqParamMapForA3 = new HashMap<String, String>();
					reqParamMapForA3.put("orderIID", order.getInnerId());
					reqParamMapForA3.put("orderObjLinkOids", orderObjLinkOids);
					reqParamMapForA3.put("disObjOids", disObjOids);
					reqParamMapForA3.put("dataObjOids", dataObjOids);
					reqParamMapForA3.put("infoOids", infoOids);
					reqParamMapForA3.put("dispatchFile", filePath);
					// ���ͣ���վ�㷢�ͣ�
					transferService.sendRequest(UUID.getUID(), order.getNumber(),
							sendMessageType, filePath, targetSite, reqParamMapForA3,
							filePath, TransferConstant.REQTYPE_ASYN);
				//���ӷַ�
				} else {
					isExistDispatchSite = "true";
					for (DistributeInfo info : outSiteDisInfoList) {
						//��վ��OID
						String outSiteOid = info.getInfoClassId()+":"+info.getDisInfoId();
						if(siteOid.equals(outSiteOid)){
							allDispatchDisInfoOids = allDispatchDisInfoOids + info.getOid() + "','";
							//���·ַ���Ϣ��״̬Ϊ�ѷַ�
							life.updateLifeCycleByStateId(info,ConstLifeCycle.LC_DISTRIBUTED.getId());
						}
					}
				}
				// ���͵�A5.0
			} else if (DTSiteConstant.DTSITE_APPVERSION_5
					.equalsIgnoreCase(importTargetSiteSystemVersion)) {
				// �������ŵ��з���վ��
				String infoSql = "select info.* from DDM_DIS_INFO info, DDM_DIS_ORDEROBJLINK link"
						+ " where info.disOrderObjLinkClassId || ':' || info.disOrderObjLinkId = link.classId || ':' || link.innerId"
						+ " and link.fromObjectClassId || ':' || link.fromObjectId = ?"
						+ " and infoClassId || ':' || disInfoId = ?";
				List<DistributeInfo> infoList = Helper.getPersistService()
						.query(infoSql, DistributeInfo.class, order.getOid(),
								siteOid);

				String infoOids = "'";
				for (DistributeInfo info : infoList) {
					//���·ַ���Ϣ��״̬Ϊ�ѷַ�
					life.updateLifeCycleByStateId(info,ConstLifeCycle.LC_DISTRIBUTED.getId());
					infoOids = infoOids + info.getOid() + "','";
				}

				infoOids = infoOids.substring(0, infoOids.length() - 2);

				// ���ݷַ���Ϣ���ұ��׷��
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

				// ���ݷַ�����ȡ�÷ַ�������Դ�����Լ��������������link
				List<Object> objectList = getAllObjectList(objList);

				List<Object> list = new ArrayList<Object>();

				// ������зַ����ݣ����ҵ������Լ�֮������link
				list.addAll(objectList);

				// ���distributeOrderObject����
				list.addAll(orderObjLinkList);

				// ��ӱ��׷�ٶ���
				list.addAll(trackList);

				// ��ӷַ���Ϣ����
				list.addAll(infoList);

				// ��ӷ��ŵ�����
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

				// ���ͣ���վ�㷢�ͣ�
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
				throw new RuntimeException("�˰汾�Ŀ�汾������ʱ��û��֧��!�汾��"
						+ importTargetSiteSystemVersion);
			}
		}
		
		//A3.5�����ǵ��ӷַ���ʱ������������վ�㣬��Ҫͳһ�������Ĵ���
		if("true".equals(isExistDispatchSite)){
			// �ַ���Ϣ���� ���ӷַ�
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

			
			//ȡ�÷��Ͷ��󣺷ַ�����
			List<DistributeObject> sendForA3DisObjectList = getSendForA3DisObjectList(objList);

			String disObjOids = "'";
			for (DistributeObject obj : sendForA3DisObjectList) {
				disObjOids = disObjOids + obj.getOid() + "','";
			}
			disObjOids = disObjOids.substring(0, disObjOids.length() - 2);
			
			// ���ݷַ�����ȡ�÷ַ�������Դ�����Լ��������������link
			List<Object> sendDataObjectList = getAllForA3DataObjectList(sendForA3DisObjectList);

			if (sendDataObjectList == null || sendDataObjectList.size() == 0) {
				throw new RuntimeException("�ַ�������Դ�����Լ��������������link���󲻴��ڣ�");
			}

			String dataObjOids = "'";
			for (Object obj : sendDataObjectList) {
				Persistable p = (Persistable) obj;
				dataObjOids = dataObjOids + p.getClassId() + ":" + p.getInnerId() + "','";
			}
			dataObjOids = dataObjOids.substring(0, dataObjOids.length() - 2);
			
//			// ���ݷַ�����ȡ��A3�ܹ���������ݶ����Ӧ��A5�ַ����ݶ���
//			List<Object> sendObjectList = getAllForA3ObjectList(sendForA3DisObjectList);
//
//			if (sendObjectList == null || sendObjectList.size() == 0) {
//				throw new RuntimeException("A3�ܹ���������ݶ����Ӧ��A5�ַ����ݶ��󲻴��ڣ�");
//			}

			Map<String, String> map = new HashMap<String, String>();
			map.put(A3A5DataConvertUtil.ORDERIID, order.getInnerId());
			// �����ַ�����
			File exportFIle = ExportFileHelper.getExportFileService()
					.exportObject(sendDataObjectList,DTSiteConstant.DTSITE_APPVERSION_3_5, map);
			String filePath = exportFIle.getPath();
			TransferObjectService transferService = TransferObjectHelper
					.getTransferService();
			// �������
			Map<String, String> reqParamMapForA3 = new HashMap<String, String>();
			reqParamMapForA3.put("orderIID", order.getInnerId());
			reqParamMapForA3.put("orderObjLinkOids", orderObjLinkOids);
			reqParamMapForA3.put("disObjOids", disObjOids);
			reqParamMapForA3.put("dataObjOids", dataObjOids);
			reqParamMapForA3.put("infoOids", allDispatchDisInfoOids);
			reqParamMapForA3.put("dispatchFile", filePath);
			DCSiteAttribute dcSiteAttr = DCSiteAttributeHelper.getDCSiteAttrService().findLocalDCSite();
			// ȡ������վ�㣨�����ǵ��ӷַ�����Ҫ��ͬ��λ��վ��ͳһ���͸����ģ�
			Site centerSite = dcSiteAttr.getSite();
			// ���ͣ���վ�㷢�ͣ�
			transferService.sendRequest(UUID.getUID(), order.getNumber(),
					sendMessageType, filePath, centerSite, reqParamMapForA3,
					filePath, TransferConstant.REQTYPE_ASYN);
		}
	}

	/**
	 * ���ݷַ�����ȡ��A3�ܹ���������ݶ����Ӧ��A5�ַ����ݶ���
	 * 
	 * @param objList
	 * @return
	 */
	private List<DistributeObject> getSendForA3DisObjectList(List<DistributeObject> objList) {
		// ���͸�A3�ķַ����ݶ����б�
		List<DistributeObject> forSendObjList = new ArrayList<DistributeObject>();
		for (DistributeObject disObject : objList) {
			// ȡ�÷ַ�����������Դ����
			String dataOid = disObject.getDataClassId() + ":" + disObject.getDataInnerId();
			Persistable obj = Helper.getPersistService().getObject(dataOid);
			if (obj instanceof Document || obj instanceof Part || obj instanceof ECO || obj instanceof CADDocument) {
				// ���Է��͵Ķ��󣨸��ĵ����ĵ�������,CADDOCUMENT������������
			} else {
				// A3ֻ�ܴ�����ĵ����ĵ���������CADDOCUMENT����Ķ��󲻷���
				continue;
			}
			forSendObjList.add(disObject);
		}

		return forSendObjList;
	}
	
	/**
	 * ���ݷַ�����ȡ�÷ַ�������Դ�����Լ�����������Ķ���
	 * 
	 * @param objList
	 * @return
	 */
	private List<Object> getAllForA3ObjectList(List<DistributeObject> objList) {

		List<Object> allObjectList = new ArrayList<Object>();
		// ���͸�A3�ķַ����ݶ����б�
		List<DistributeObject> forSendObjList = new ArrayList<DistributeObject>();
		for (DistributeObject disObject : objList) {
			List<Object> dataObjList = new ArrayList<Object>();
			String dataOid = disObject.getDataClassId() + ":" + disObject.getDataInnerId();
			Persistable obj = Helper.getPersistService().getObject(dataOid);
			//String dataClassId = disObject.getDataClassId();
			if (obj instanceof Document || obj instanceof Part || obj instanceof ECO || obj instanceof CADDocument){
				// �ǿ��Է��͵Ķ��󣬼�������
			} else {
				// A3ֻ�ܴ�����ĵ����ĵ�������
				continue;
			}
			// ȡ�÷ַ�����������Դ����
			
			// ������Դ���ڱ��
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
					// ������Դ���ڸ��ĵ�
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
		// ȡ���������ݺ�����֮���link����
		allObjectList = ExportFileHelper.getExportFileService().getAllObject(
				allObjectList);

		return allObjectList;
	}

	/**
	 * ���ݷַ�����ȡ�÷ַ����ݹ�������Ҫ�ַ���
	 * 
	 * @param objList
	 * @return
	 */
	private List<Object> getAllForA3DataObjectList(List<DistributeObject> objList) {

		// ���͸�A3�ķַ����ݶ����б�
		List<Object> dataObjList = new ArrayList<Object>();
		for (DistributeObject disObject : objList) {
			// ȡ�÷ַ�����������Դ����
			String dataOid = disObject.getDataClassId() + ":" + disObject.getDataInnerId();
			Persistable obj = Helper.getPersistService().getObject(dataOid);
			if (obj instanceof Document || obj instanceof Part || obj instanceof ECO || obj instanceof CADDocument) {
				// �ǿ��Է��͵Ķ��󣬼�������
			} else {
				// A3ֻ�ܴ�����ĵ����ĵ�������
				continue;
			}
			// ������Դ���ڱ��
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
					// ������Դ���ڸ��ĵ�
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

		//���ݷַ���Ϣ���ұ��׷��
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

		//���ݷַ�����ȡ�÷ַ�������Դ�����Լ��������������link
		//List<Object> ObjectList = getAllObjectList(objList);

		List<Object> list = new ArrayList<Object>();

		//������зַ����ݶ���
		list.addAll(objList);

		//���distributeOrderObject����
		list.addAll(linkList);

		//��ӱ��׷�ٶ���
		list.addAll(trackList);

		//��ӷַ���Ϣ����
		list.addAll(infoList);

		//��ӷ��ŵ�����
		list.add(order);

		//ȡ����������ַ���Ϣ��һ������ȡ��վ��
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

		//���ͣ���վ�㷢�ͣ�
		File exportFIle = ExportFileHelper.getExportFileService().exportObject(list);
		String filePath = exportFIle.getPath();
		TransferObjectService transferService = TransferObjectHelper.getTransferService();
		transferService.sendRequest(UUID.getUID(), order.getNumber(), MessageTypeconstant.SEND_DISTRIBUTE, filePath, targetSite,
				reqParamMap, filePath, TransferConstant.REQTYPE_ASYN);
	}

	/**
	 * ���ݷַ�����ȡ�÷ַ�������Դ�����Լ�����������Ķ���
	 * 
	 * @param disObject
	 * @return 
	 */
	public List<Object> getAllObjectList(List<DistributeObject> objList) {
		List<Object> allObjectList = new ArrayList<Object>();
		for (DistributeObject disObject : objList) {
			List<Object> dataObjList = new ArrayList<Object>();
			//ȡ�÷ַ�����������Դ����
			String dataOid = disObject.getDataClassId() + ":" + disObject.getDataInnerId();
			Persistable obj = Helper.getPersistService().getObject(dataOid);
			//������Դ���ڱ��
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
					//������Դ���ڸ��ĵ�
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
					//������Դ���ڼ���֪ͨ��
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
					//������Դ���ڳ���������ɵ�
					Variance variance = (Variance) obj;
					//ȡ�����ɵ���Ӧ��������
					List<VariancedInLink> variancedItemList  = Helper.getChangeService().getVariancedItemList(variance);
					for(VariancedInLink variancedInLink : variancedItemList){
						Persistable object = variancedInLink.getFrom();
						if (!dataObjList.contains(object)) {
							//��ӱ������
							dataObjList.add(object);
						}
					}
					//�õ���������볬�����Ĺ�ϵ
					List<VariancedLink> variancedLinkList = Helper.getChangeService().getVariancedLinkList(variance);
					for(VariancedLink variancedLink : variancedLinkList){
						Persistable object = variancedLink.getFrom();
						if (!dataObjList.contains(object)) {
							//��ӱ������
							dataObjList.add(object);
						}
					}
					//���ݳ����ó�������
					List<VarianceReq> varianceReqList = Helper.getChangeService().getVarianceReqList(variance);
					for(VarianceReq varianceReq : varianceReqList){
						List<VariancedLink> linkList = Helper.getChangeService().getVariancedList(varianceReq);
						for(VariancedLink variancedLink : linkList){
							Persistable object = variancedLink.getFrom();
							if (!dataObjList.contains(object)) {
								//��ӱ������
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
		//ȡ���������ݺ�����֮���link����
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

		// ������������״̬
		DistributeOrder disOrderObj = (DistributeOrder) object;

		//���ص���װ������
		List<String> oidsList = new ArrayList<String>();
		//���зַ���Ϣ����
		String distributeInfoOids = "";
		//��������ַ����񼯺�
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
					//��������(disInfoList�е�һ���ַ���Ϣ)
					disElecTask.setNumber(disOrderObj.getNumber() + ConstUtil.C_ELECTASK_STR);
					disElecTask.setName(disOrderObj.getName() + ConstUtil.C_ELECTASK_STR);

					disElecTask.setNote("");
					disElecTask.setElecTaskType("1");

					disElecTask.setContextInfo(disOrderObj.getContextInfo());
					//�������Ϣ
					disElecTask.setDomainInfo(disOrderObj.getDomainInfo());

					//���Դվ����Ϣ
					disElecTask.setSourceSiteId(site.getInnerId());
					disElecTask.setSourceSiteClassId(site.getClassId());
					disElecTask.setSourceSiteName(site.getSiteData().getSiteName());

					//���Ŀ��վ����Ϣ
					disElecTask.setTargetSiteId(info.getDisInfoId());
					disElecTask.setTargetSiteClassId(info.getInfoClassId());
					disElecTask.setTargetSiteName(info.getDisInfoName());
					
					//��Ӵ������޸���
					disElecTask.setManageInfo(disOrderObj.getManageInfo());
					disElecTask.getManageInfo().setCreateTime(System.currentTimeMillis());
					disElecTask.getManageInfo().setModifyTime(System.currentTimeMillis());

					//�������վ��
					if (centerSite != null) {
						disElecTask.setCenterSiteId(centerSite.getInnerId());
						disElecTask.setCenterSiteClassId(centerSite.getClassId());
					}

					// ���������������
					service.createDistributeElecTask(disElecTask);

					// ���·ַ���Ϣ�ķ���ʱ��
					if (ConstUtil.LC_NOT_DISTRIBUT.getName().equals(info.getLifeCycleInfo().getStateName())) {
						info.setSendTime(System.currentTimeMillis());
						life.promoteLifeCycle(info);
					}
					Helper.getPersistService().update(info);

					//������ģʽ��վ��Ϊ��������
					if (centerFlag == true) {
						// ��������ַ���Ϣ���������link����
						createOutDistributeTaskInfoLink(disElecTask, info);
					} else {
						createDistributeTaskInfoLink(disElecTask, info);
					}

				} else {
					//���ɷַ���Ϣ����������link
					//������ģʽ��վ��Ϊ��������
					if (centerFlag == true) {
						// ��������ַ���Ϣ���������link����
						createOutDistributeTaskInfoLink(disElecTask, info);
					} else {
						createDistributeTaskInfoLink(disElecTask, info);
					}
					//�ַ���Ϣ��ӷ���ʱ�䣬������������
					info.setSendTime(System.currentTimeMillis());
					if (ConstUtil.LC_NOT_DISTRIBUT.getName().equals(info.getLifeCycleInfo().getStateName())) {
						life.promoteLifeCycle(info);
					}
					Helper.getPersistService().update(info);
				}
				//Ϊ�ַ�������Ȩ
				DistributeOrderObjectLink disOrderObjLink = (DistributeOrderObjectLink) Helper.getPersistService()
						.getObject(info.getDisOrderObjLinkClassId() + ":" + info.getDisOrderObjLinkId());
				DistributeObject disObject = (DistributeObject) disOrderObjLink.getTo();
				String outSignId = info.getOutSignId();
				if(!"".equals(outSignId) && outSignId != null){
					User receiveUser = UserHelper.getService().getUser(info.getOutSignId());
					userList.add(receiveUser);
				} else {
					//ȡ�ÿ���Э��Ա��ɫ
					DmcRole dmcRole = DmcRoleHelper.getService().findDmcRoleByRoleID(ConstUtil.DIS_CROSS_DOMAIN_ROLE);
					List<DmcRoleUser> roleUsers = new ArrayList<DmcRoleUser>();
					if(dmcRole != null){
						//ȡ�ÿ���Э��Ա
						roleUsers = DmcRoleHelper.getService().findDmcRoleUserByRoleID(dmcRole.getInnerId());
						if (roleUsers != null && !roleUsers.isEmpty()) {
							//����п���Э��Ա
							Iterator<DmcRoleUser> it = roleUsers.iterator();
							while (it.hasNext()) {
								DmcRoleUser user = (DmcRoleUser) it.next();
								String productIIDsql = "SELECT MAX(T.LOCALPRODUCTID) AS PRODUCTID FROM PLM_COLLABORATION_PRODUCT P, PLM_COLLABORATION_TEMPDATA T"
										+ " WHERE T.DMCPRODUCTCLASSID || ':' || T.DMCPRODUCTID = P.CLASSID || ':' || P.INNERID"
										+ " AND P.PRODUCTIID = ?";
								//ȡ�ÿ���Э��Ա�ɿ����Ĳ�ƷID
								List<Map<String, Object>> proList = Helper.getPersistService().query(productIIDsql, user.getProduct().getProductIID());
								String productId = (String) proList.get(0).get("PRODUCTID");
								String objProductId = disObject.getContextInfo().getContextRef().getInnerId();
								//�������Э��Ա��Ӧ�Ĳ�ƷID�Ƿַ����ݵ�������ID
								if(objProductId.equals(productId)){
									//�ж��Ƿ��з��ʷַ����ݵ�Ȩ��
									User receiveUser = UserHelper.getService().getUser(user.getUserIID());
									userList.add(receiveUser);
								}
							}
						}
					}
				}
				for(User user : userList){
					//�ж��Ƿ���ҵ��������Ȩ��
					Domained domained = (Domained) Helper.getPersistService().getObject(disObject.getDataClassId() + ":" + disObject.getDataInnerId());
					boolean dataObjFlag = AccessControlHelper.getService().hasEntityPermission(user, Operate.DOWNLOAD, domained);
					//��û��Ȩ��
					if(dataObjFlag == false){
						//����ҵ��������Ȩ��
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

		// ������������״̬
		DistributeOrder disOrderObj = (DistributeOrder) object;
		
		int i = 0;
		String infoOids = "";
		String disTaskOids = "";
		String distributeInfoOids = "";
		DistributeElecTask elecTask = null;
		//���ص���װ������
		List<String> oidsList = new ArrayList<String>();
		for (String infoOid : infoOidsList) {
			DistributeInfo info = (DistributeInfo) Helper.getPersistService().getObject(infoOid);
			infoOids = infoOids + info.getOid() + ",";
			//�жϷַ���Ϣ
			if(i == 0){
				//������������
				DistributeElecTaskService service = DistributeHelper.getDistributeElecTaskService();
				DistributeElecTask disElecTask = service.newDistributeElecTask();
				disElecTask.setNumber(disOrderObj.getNumber() + ConstUtil.C_ELECTASK_STR);
				disElecTask.setName(disOrderObj.getName() + ConstUtil.C_ELECTASK_STR);

				disElecTask.setNote("");
				disElecTask.setElecTaskType("1");

				disElecTask.setContextInfo(disOrderObj.getContextInfo());
				//�������Ϣ
				disElecTask.setDomainInfo(disOrderObj.getDomainInfo());

				//���Դվ����Ϣ
				disElecTask.setSourceSiteId(site.getInnerId());
				disElecTask.setSourceSiteClassId(site.getClassId());
				disElecTask.setSourceSiteName(site.getSiteData().getSiteName());

				//���Ŀ��վ����Ϣ
				disElecTask.setTargetSiteId(info.getDisInfoId());
				disElecTask.setTargetSiteClassId(info.getInfoClassId());
				disElecTask.setTargetSiteName(info.getDisInfoName());
				
				//��Ӵ������޸���
				disElecTask.setManageInfo(disOrderObj.getManageInfo());
				disElecTask.getManageInfo().setCreateTime(System.currentTimeMillis());
				disElecTask.getManageInfo().setModifyTime(System.currentTimeMillis());

				//�������վ��
				if (centerSite != null) {
					disElecTask.setCenterSiteId(centerSite.getInnerId());
					disElecTask.setCenterSiteClassId(centerSite.getClassId());
				}

				// ���������������
				service.createDistributeElecTask(disElecTask);

				// ���·ַ���Ϣ�ķ���ʱ��
				if (ConstUtil.LC_NOT_DISTRIBUT.getName().equals(info.getLifeCycleInfo().getStateName())) {
					info.setSendTime(System.currentTimeMillis());
					life.promoteLifeCycle(info);
				}
				Helper.getPersistService().update(info);

				createOutDistributeTaskInfoLink(disElecTask, info);
				elecTask = disElecTask;
				disTaskOids = disElecTask.getOid() + ";";
			} else {
				//�����ַ���Ϣ����������link
				//������ģʽ��վ��Ϊ��������
				createOutDistributeTaskInfoLink(elecTask, info);
				
				//�ַ���Ϣ��ӷ���ʱ�䣬������������
				info.setSendTime(System.currentTimeMillis());
				if (ConstUtil.LC_NOT_DISTRIBUT.getName().equals(info.getLifeCycleInfo().getStateName())) {
					life.promoteLifeCycle(info);
				}
				Helper.getPersistService().update(info);
			}
			i = i + 1;
			//�ַ���Ϣ��Ӧ�ķַ�������Ȩ
			DistributeOrderObjectLink disOrderObjLink = (DistributeOrderObjectLink) Helper.getPersistService()
					.getObject(info.getDisOrderObjLinkClassId() + ":" + info.getDisOrderObjLinkId());
			DistributeObject disObject = (DistributeObject) disOrderObjLink.getTo();
			
			//ȡ�ÿ���Э��Ա��ɫ
			DmcRole dmcRole = DmcRoleHelper.getService().findDmcRoleByRoleID(ConstUtil.DIS_CROSS_DOMAIN_ROLE);
			List<DmcRoleUser> roleUsers = new ArrayList<DmcRoleUser>();
			if(dmcRole != null){
				//ȡ�ÿ���Э��Ա
				roleUsers = DmcRoleHelper.getService().findDmcRoleUserByRoleID(dmcRole.getInnerId());
				if (roleUsers != null && !roleUsers.isEmpty()) {
					//����п���Э��Ա
					Iterator<DmcRoleUser> it = roleUsers.iterator();
					while (it.hasNext()) {
						DmcRoleUser user = (DmcRoleUser) it.next();
						String productIIDsql = "SELECT MAX(T.LOCALPRODUCTID) AS PRODUCTID FROM PLM_COLLABORATION_PRODUCT P, PLM_COLLABORATION_TEMPDATA T"
								+ " WHERE T.DMCPRODUCTCLASSID || ':' || T.DMCPRODUCTID = P.CLASSID || ':' || P.INNERID"
								+ " AND P.PRODUCTIID = ?";
						//ȡ�ÿ���Э��Ա�ɿ����Ĳ�ƷID
						List<Map<String, Object>> proList = Helper.getPersistService().query(productIIDsql, user.getProduct().getProductIID());
						String productId = (String) proList.get(0).get("PRODUCTID");
						String objProductId = disObject.getContextInfo().getContextRef().getInnerId();
						//�������Э��Ա��Ӧ�Ĳ�ƷID�Ƿַ����ݵ�������ID
						if(objProductId.equals(productId)){
							//�ж��Ƿ��з��ʷַ����ݵ�Ȩ��
							User receiveUser = UserHelper.getService().getUser(user.getUserIID());

							//�ж��Ƿ���ҵ��������Ȩ��
							Domained domained = (Domained) Helper.getPersistService().getObject(disObject.getDataClassId() + ":" + disObject.getDataInnerId());
							boolean dataObjFlag = AccessControlHelper.getService().hasEntityPermission(receiveUser, Operate.DOWNLOAD, domained);
							//��û��Ȩ��
							if(dataObjFlag == false){
								//����ҵ��������Ȩ��
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
	 * ��������ַ���Ϣ���������link����
	 * 
	 * @param disTask
	 * @param disInfo
	 */
	private void createOutDistributeTaskInfoLink(DistributeTask disTask, DistributeInfo disInfo) {

		DistributeTaskDomainLink taskDomainLink = newDistributeTaskDomainLink();
		// ֽ�������ڲ���ʶ
		taskDomainLink.setFromObject(disTask);
		// �ַ���Ϣ�ڲ���ʶ
		taskDomainLink.setToObject(disInfo);
		// �������ͣ�0��ֽ������1����������
		taskDomainLink.setTaskType("1");
		Helper.getPersistService().save(taskDomainLink);
	}

	public DistributeTaskDomainLink newDistributeTaskDomainLink() {
		DistributeTaskDomainLink disLink = (DistributeTaskDomainLink) PersistUtil
				.createObject(DistributeTaskDomainLink.CLASSID);
		return disLink;
	}

}
