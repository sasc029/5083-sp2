package com.bjsasc.ddm.distribute.service.distributeelectask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.jfree.util.Log;

import com.bjsasc.avidm.core.site.Site;
import com.bjsasc.avidm.core.site.SiteHelper;
import com.bjsasc.avidm.core.site.constant.DTSiteConstant;
import com.bjsasc.avidm.core.transfer.TransferObjectHelper;
import com.bjsasc.avidm.core.transfer.TransferObjectService;
import com.bjsasc.avidm.core.transfer.util.TransferConstant;
import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.common.ConstUtil.LIFECYCLE_OPT;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeelectask.DistributeElecTask;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.distributetask.DistributeTask;
import com.bjsasc.ddm.distribute.model.distributetaskinfolink.DistributeTaskInfoLink;
import com.bjsasc.ddm.distribute.model.returnreason.ReturnReason;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.ddm.distribute.service.distributeprincipal.DistributePrincipalService;
import com.bjsasc.ddm.distribute.service.distributetask.DistributeTaskService;
import com.bjsasc.ddm.distribute.service.distributetaskinfolink.DistributeTaskInfoLinkService;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.collaboration.a4x.util.Message4XTypeConstant;
import com.bjsasc.plm.collaboration.config.constant.MessageTypeconstant;
import com.bjsasc.plm.collaboration.config.model.DmcRole;
import com.bjsasc.plm.collaboration.config.model.DmcRoleUser;
import com.bjsasc.plm.collaboration.config.service.DataConvertConfigHelper;
import com.bjsasc.plm.collaboration.config.service.DmcConfigHelper;
import com.bjsasc.plm.collaboration.config.service.DmcConfigService;
import com.bjsasc.plm.collaboration.config.service.DmcRoleHelper;
import com.bjsasc.plm.collaboration.site.model.DCSiteAttribute;
import com.bjsasc.plm.collaboration.site.service.DCSiteAttributeHelper;
import com.bjsasc.plm.collaboration.util.A4XObjTypeConstant;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.domain.DomainHelper;
import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.security.audit.AuditLogHelper;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.access.AccessControlHelper;
import com.bjsasc.plm.core.system.access.operate.Operate;
import com.bjsasc.plm.core.system.principal.Group;
import com.bjsasc.plm.core.system.principal.GroupHelper;
import com.bjsasc.plm.core.system.principal.Organization;
import com.bjsasc.plm.core.system.principal.OrganizationHelper;
import com.bjsasc.plm.core.system.principal.Principal;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.system.principal.UserHelper;
import com.bjsasc.plm.core.util.StringUtil;
import com.cascc.avidm.util.AvidmConstDefine;
import com.cascc.avidm.util.SplitString;
import com.cascc.platform.uuidservice.UUID;
import com.cascc.platform.aa.AAContext;
import com.cascc.platform.aa.AAProvider;
import com.cascc.platform.aa.API;
import com.cascc.platform.aa.api.data.AAUserData;
import com.cascc.platform.aa.util.mail.AvidmMsgSender;

/**
 * ��������ӿ�ʵ���ࡣ
 * 
 * @author gengancong 2013-3-18
 */
@SuppressWarnings({ "deprecation", "unchecked", "static-access" })
public class DistributeElecTaskServiceImpl implements DistributeElecTaskService {

	/*
	 * ���� Javadoc��
	 * 
	 * @see com.bjsasc.ddm.distribute.service.distributeelectask.
	 * DistributeElecTaskService#getAllDistributeElecTask(java.lang.String)
	 */
	@Override
	public List<DistributeElecTask> getAllNoSignDistributeElecTask(Object stateName) {
		User currentUser = SessionHelper.getService().getUser();
		String sql = "SELECT DISTINCT A .* FROM DDM_DIS_ELECTASK A, DDM_DIS_TASKINFOLINK B, DDM_DIS_INFO C "
				+ " WHERE A .INNERID = B.FROMOBJECTID AND A .CLASSID = B.FROMOBJECTCLASSID "
				+ "AND C.INNERID = B.TOOBJECTID AND C.CLASSID = B.TOOBJECTCLASSID"
				+ " AND A .STATENAME = ? AND C.DISINFOID = ? AND C.INFOCLASSID = ? " + "ORDER BY A .MODIFYTIME DESC";
		List<DistributeElecTask> taskList = Helper.getPersistService().query(sql, DistributeElecTask.class, stateName,
				currentUser.getInnerId(), currentUser.getClassId());
		List<DistributeElecTask> electList = getAllOrganizationElectdistribute(stateName);
		taskList.addAll(electList);
		
		List<DistributeElecTask> outTasklist = new ArrayList<DistributeElecTask>();
		//�жϵ�ǰ�û��Ƿ��ǿ��򷢷�Э��Ա
		boolean isCrossDomainUser = this.isCrossDomainUser();
		boolean isSetCrossDomainUser = this.isSetCrossDomainUser();
		//�����ǰ�û��ǿ��򷢷�Э��Ա,ȡ���������򷢷�Э��Ա��ɵ�����
		if(isSetCrossDomainUser && isCrossDomainUser){
			long from = 0;
			long to = 0;
			String name = stateName.toString();
			outTasklist = this.getDistributeElecTaskForCrossDomain("'"+name+"'", from, to);
		}else if(!isSetCrossDomainUser && currentUser.isAdministrator()){
			outTasklist = this.getDistributeElecTaskForAdmin(stateName.toString());
		}else{//�����ǰ��¼�û�����ͨ�û�����ѯ�Ƿ���ָ���û��Ŀ���ַ�����
			outTasklist = this.getDistributeElecTaskForAAUSER(stateName.toString());
		}
		
		taskList.addAll(outTasklist);
		TimeComparator timeComp = new TimeComparator();
		Collections.sort(outTasklist, timeComp);
		return taskList;
	}

	/**
	 * TimeComparator�Ƚ�����
	 */
	private class TimeComparator implements Comparator<DistributeElecTask> {
		@Override
		public int compare(DistributeElecTask obj1, DistributeElecTask obj2) {
			Long object1 = obj1.getManageInfo().getModifyTime();
			Long object2 = obj2.getManageInfo().getModifyTime();
			if (object1 == null || object2 == null) {
				return 0;
			}
			if (object1 == object2) {
				return 0;
			} else if (object1 > object2) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	public List<DistributeElecTask> getAllDistributeElecTask(Object stateName) {
		User currentUser = SessionHelper.getService().getUser();
		String sql = "SELECT DISTINCT * FROM DDM_DIS_ELECTASK "
				+ " WHERE STATENAME = ? AND RECEIVEBYID = ? AND RECEIVEBYCLASSID = ? ORDER BY MODIFYTIME DESC";
		List<DistributeElecTask> taskList = Helper.getPersistService().query(sql, DistributeElecTask.class, stateName,
				currentUser.getInnerId(), currentUser.getClassId());

		return taskList;
	}

	@Override
	public List<DistributeElecTask> getDistributeElecTaskForSecondSche(String stateName, long from, long to) {

		List<DistributeElecTask> disElecTasklistForSecondSche = new ArrayList<DistributeElecTask>();
		//�жϵ�ǰ�û��Ƿ��Ƕ�������Ա
		boolean isSecondScheUser = this.isSecondScheUser();
		//�����ǰ�û��Ƕ�������Ա,ȡ�õ�ǰ��֯���յĵ�ǰ�û�����Ĳ�Ʒ��ص�����ɵ�����
		if(isSecondScheUser){
			//ȡ�õ�ǰ�û�����֯ID��ClassId
			String aaOrgID = SessionHelper.getService().getUser().getOrganization().getInnerId();
			String aaOrgClassID = SessionHelper.getService().getUser().getOrganization().getClassId();

			//ȡ�����в�Ʒ������
			List<Context> allProductsContext = Helper.getAppContextService().getAllProducts();
			
			//ȡ�õ�ǰ�û�
			User currentUser = SessionHelper.getService().getUser();
			
			//��ȡ��ǰ�û�����Ĳ�Ʒ�������б�
			List<Context> currentUserProductsContext = Helper.getContextService().filterContextsByUserPrincipal(allProductsContext,currentUser);

			//ȡ�õ�ǰ�û�������Ĳ�ƷID
			String productIIDs = "'";
			for(Context context : currentUserProductsContext){
				productIIDs = productIIDs + context.getInnerId() + "','";
			}
			if(currentUserProductsContext.size() > 0){
				productIIDs = productIIDs.substring(0, productIIDs.length() - 2);
			}
			String secondScheSql =  "";
			List<DistributeElecTask> disElecTasklistSecondSche = new ArrayList<DistributeElecTask>();
			if(from == 0){
				secondScheSql = "SELECT DISTINCT TASK.* FROM DDM_DIS_ELECTASK TASK, DDM_DIS_INFO INFO, DDM_DIS_TASKINFOLINK LINK"
						+ " WHERE LINK.FROMOBJECTCLASSID || ':' || LINK.FROMOBJECTID = TASK.CLASSID || ':' || TASK.INNERID"
						+ " AND LINK.TOOBJECTCLASSID || ':' || LINK.TOOBJECTID = INFO.CLASSID || ':' || INFO.INNERID"
						+ " and task.contextId in (" + productIIDs + ") " 
						+ " AND TASK.STATENAME  IN (" + stateName + ")  AND INFO.DISINFOID = ? AND INFO.INFOCLASSID = ? ";
				disElecTasklistSecondSche = Helper.getPersistService().query(secondScheSql, DistributeElecTask.class,
						aaOrgID, aaOrgClassID);
			}else{
				secondScheSql = "SELECT DISTINCT TASK.* FROM DDM_DIS_ELECTASK TASK, DDM_DIS_INFO INFO, DDM_DIS_TASKINFOLINK LINK"
					+ " WHERE LINK.FROMOBJECTCLASSID || ':' || LINK.FROMOBJECTID = TASK.CLASSID || ':' || TASK.INNERID"
					+ " AND LINK.TOOBJECTCLASSID || ':' || LINK.TOOBJECTID = INFO.CLASSID || ':' || INFO.INNERID"
					+ " and task.contextId in (" + productIIDs + ") " 
					+ " AND TASK.STATENAME  IN (" + stateName + ")  AND INFO.DISINFOID = ? AND INFO.INFOCLASSID = ? AND TASK.MODIFYTIME >= ? AND  TASK.MODIFYTIME <= ? ";
				disElecTasklistSecondSche = Helper.getPersistService().query(secondScheSql, DistributeElecTask.class,
						aaOrgID, aaOrgClassID, from, to);
			}
			
			disElecTasklistForSecondSche.addAll(disElecTasklistSecondSche);

		}
		
		return disElecTasklistForSecondSche;
	}

	private List<DistributeElecTask> getDistributeElecTaskForAdmin(String stateName) {
		List<DistributeElecTask> disElecTasklistAdmin = new ArrayList<DistributeElecTask>();
		String taskSql = "select DISTINCT task.* from DDM_DIS_ELECTASK task, DDM_DIS_INFO info,"
				+ " DDM_DIS_TASKINFOLINK link where link.fromObjectClassId || ':' || link.fromObjectId = "
				+ " task.classId || ':' || task.innerId and link.toObjectClassId || ':' || link.toObjectId = "
				+ " info.classId || ':' || info.innerId and task.elecTaskType = '1' "
				+ " and info.infoClassId = ? and task.stateName = ? and task.receiveById is null ";
		disElecTasklistAdmin = Helper.getPersistService().query(taskSql, DistributeElecTask.class, Site.CLASSID, stateName);
		return disElecTasklistAdmin;
	}
	
	private List<DistributeElecTask> getDistributeElecTaskForAAUSER(String stateName) {
		User currentUser = SessionHelper.getService().getUser();
		String aauserInnerID = currentUser.getAaUserInnerId();
		List<DistributeElecTask> disElecTasklistAAUSER = new ArrayList<DistributeElecTask>();
		String taskSql = "select DISTINCT task.* from DDM_DIS_ELECTASK task, DDM_DIS_INFO info,"
				+ " DDM_DIS_TASKINFOLINK link where link.fromObjectClassId || ':' || link.fromObjectId = "
				+ " task.classId || ':' || task.innerId and link.toObjectClassId || ':' || link.toObjectId = "
				+ " info.classId || ':' || info.innerId and task.elecTaskType = '1' "
				+ " and info.infoClassId = ? and task.stateName = ? and task.receiveById is null and info.outSignId = ?";
		disElecTasklistAAUSER = Helper.getPersistService().query(taskSql, DistributeElecTask.class, Site.CLASSID, stateName, aauserInnerID);
		return disElecTasklistAAUSER;
	}
		
	@Override
	public List<DistributeElecTask> getDistributeElecTaskForCrossDomain(String stateName, long from, long to) {

		List<DistributeElecTask> disElecTasklistForCrossDomain = new ArrayList<DistributeElecTask>();
		User currentUser = SessionHelper.getService().getUser();
		//�жϵ�ǰ�û��Ƿ��ǿ��򷢷�Э��Ա
		boolean isCrossDomainUser = this.isCrossDomainUser();
		List<DistributeElecTask> disElecTasklistCrossDomain = new ArrayList<DistributeElecTask>();
		//�����ǰ�û��ǿ��򷢷�Э��Ա,ȡ�õ�ǰվ����յĵ�������
		if(isCrossDomainUser){
			DmcRole dmcRole = DmcRoleHelper.getService().findDmcRoleByRoleID(ConstUtil.DIS_CROSS_DOMAIN_ROLE);
			List<DmcRoleUser> roleUsers = new ArrayList<DmcRoleUser>();
			if (dmcRole != null) {
				roleUsers = DmcRoleHelper.getService().findDmcRoleUserByRoleID(dmcRole.getInnerId());
			}
			List<String> productIIDList = new ArrayList<String>();
			List<String> localProductIDList = new ArrayList<String>();
			String productIIDs = "'";
			if (roleUsers != null && !roleUsers.isEmpty()) {
				Iterator<DmcRoleUser> it = roleUsers.iterator();
				while (it.hasNext()) {
					DmcRoleUser user = (DmcRoleUser) it.next();
					if (currentUser.getAaUserInnerId().equals(user.getUserIID())) {
						productIIDList.add(user.getProduct().getProductIID());
					}
				}

				for (String productIID : productIIDList) {
					String productIIDsql = "SELECT MAX(T.LOCALPRODUCTID) AS PRODUCTID FROM PLM_COLLABORATION_PRODUCT P, PLM_COLLABORATION_TEMPDATA T"
							+ " WHERE T.DMCPRODUCTCLASSID || ':' || T.DMCPRODUCTID = P.CLASSID || ':' || P.INNERID"
							+ " AND P.PRODUCTIID = ?";
					List<Map<String, Object>> proList = Helper.getPersistService().query(productIIDsql, productIID);
					localProductIDList.add((String) proList.get(0).get("PRODUCTID"));
				}

				for (String localProductID : localProductIDList) {
					productIIDs = productIIDs + localProductID + "','";
				}

				if (localProductIDList.size() > 0) {
					productIIDs = productIIDs.substring(0, productIIDs.length() - 2);
				}
			}
			//ȡ�ÿ����������
			Site selfSite = SiteHelper.getSiteService().findLocalSiteInfo();
			DmcConfigService dmcConfigService = DmcConfigHelper.getService();
			boolean isDcDeployModel = dmcConfigService.getIsDealOnDC();
			boolean flag = false;
			//���������ģʽ
			if (selfSite != null && isDcDeployModel == true) {
				DCSiteAttribute dcSiteAttr = DCSiteAttributeHelper.getDCSiteAttrService().findDcSiteAttrByDTSiteId(
						selfSite.getInnerId());
				//�����ǰվ���������������û�ΪЭ��Ա
				if (dcSiteAttr != null && "true".equals(dcSiteAttr.getIsSiteControl())) {
					if(from == 0){
						String taskSql = "select DISTINCT task.* from DDM_DIS_ELECTASK task, DDM_DIS_INFO info, DDM_DIS_TASKDOMAINLINK link"
								+ " where link.fromObjectClassId || ':' || link.fromObjectId = task.classId || ':' || task.innerId"
								+ " and link.toObjectClassId || ':' || link.toObjectId = info.classId || ':' || info.innerId"
								+ " and task.contextId in (" + productIIDs + ") and task.stateName in (" + stateName + ") and elecTaskType = '1' ";
						disElecTasklistCrossDomain = Helper.getPersistService().query(taskSql, DistributeElecTask.class);
					}else{
						String taskSql = "select DISTINCT task.* from DDM_DIS_ELECTASK task, DDM_DIS_INFO info, DDM_DIS_TASKDOMAINLINK link"
								+ " where link.fromObjectClassId || ':' || link.fromObjectId = task.classId || ':' || task.innerId"
								+ " and link.toObjectClassId || ':' || link.toObjectId = info.classId || ':' || info.innerId"
								+ " and task.contextId in (" + productIIDs + ") and task.stateName in (" + stateName + ") and elecTaskType = '1' and task.modifyTime >= ? and task.modifyTime <= ? ";
						disElecTasklistCrossDomain = Helper.getPersistService().query(taskSql, DistributeElecTask.class, from, to);	
					}
					//ϵͳΪ�������� 
					flag = true;
				}
			}

			//ϵͳΪ����������(����ģʽ/����ģʽ�ҷ���������)
			if (selfSite != null && flag == false) {
				if(from == 0){
					String crossDomainSql = "SELECT DISTINCT TASK.* FROM DDM_DIS_ELECTASK TASK, DDM_DIS_INFO INFO, DDM_DIS_TASKINFOLINK LINK"
							+ " WHERE LINK.FROMOBJECTCLASSID || ':' || LINK.FROMOBJECTID = TASK.CLASSID || ':' || TASK.INNERID"
							+ " AND LINK.TOOBJECTCLASSID || ':' || LINK.TOOBJECTID = INFO.CLASSID || ':' || INFO.INNERID"
							+ " and TASK.CONTEXTID in (" + productIIDs + ") " 
							+ " AND TASK.STATENAME  IN (" + stateName + ")  AND INFO.DISINFOID = ? AND INFO.INFOCLASSID = ? AND INFO.outSignId is null ";
						disElecTasklistCrossDomain = Helper.getPersistService().query(crossDomainSql, DistributeElecTask.class,
								selfSite.getInnerId(), selfSite.getClassId());
				}else{
					String crossDomainSql = "SELECT DISTINCT TASK.* FROM DDM_DIS_ELECTASK TASK, DDM_DIS_INFO INFO, DDM_DIS_TASKINFOLINK LINK"
							+ " WHERE LINK.FROMOBJECTCLASSID || ':' || LINK.FROMOBJECTID = TASK.CLASSID || ':' || TASK.INNERID"
							+ " AND LINK.TOOBJECTCLASSID || ':' || LINK.TOOBJECTID = INFO.CLASSID || ':' || INFO.INNERID"
							+ " and TASK.CONTEXTID in (" + productIIDs + ") " 
							+ " AND TASK.STATENAME  IN (" + stateName + ")  AND INFO.DISINFOID = ? AND INFO.INFOCLASSID = ? AND INFO.outSignId is null  AND TASK.MODIFYTIME >= ? AND  TASK.MODIFYTIME <= ?";
						disElecTasklistCrossDomain = Helper.getPersistService().query(crossDomainSql, DistributeElecTask.class,
								selfSite.getInnerId(), selfSite.getClassId(), from, to);
				}
			}

			disElecTasklistForCrossDomain.addAll(disElecTasklistCrossDomain);
		}
		return disElecTasklistForCrossDomain;
	}
	
	@Override
	public List<DistributeElecTask> getAllReturnDistributeElecTask(Object stateName) {
		User currentUser = SessionHelper.getService().getUser();
		String sql = "SELECT DISTINCT * FROM DDM_DIS_ELECTASK "
				+ " WHERE STATENAME = ? AND RECEIVEBYID = ? AND RECEIVEBYCLASSID = ? ORDER BY MODIFYTIME DESC";
		List<DistributeElecTask> taskList = Helper.getPersistService().query(sql, DistributeElecTask.class, stateName,
				currentUser.getInnerId(), currentUser.getClassId());
		List<DistributeElecTask> list = new ArrayList<DistributeElecTask>();
		for (DistributeElecTask task : taskList) {
			String returnReason = task.getReturnReason();
			task.setReturnReason(returnReason);
			list.add(task);
		}
		return list;
	}

	public DistributeElecTask getDistributeElecTaskInfo(String distributeElecOid) {

		Persistable obj = Helper.getPersistService().getObject(distributeElecOid);
		DistributeElecTask disTask = (DistributeElecTask) obj;

		List<DistributeInfo> infos = getDistributeInfos(distributeElecOid);
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
		String sqlOrder = "select disOrder.name as name,disOrder.innerId as innerId,disOrder.classId as classId from ((select ord.* from DDM_DIS_TASKDOMAINLINK t,DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink,DDM_DIS_ORDER ord"
				+ " where disInfo.innerId = t.toObjectId and disInfo.classId = t.toObjectClassId"
				+ " and objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
				+ " and ord.innerId = objLink.fromObjectId and ord.classId = objLink.fromObjectClassId"
				+ " and t.fromObjectClassId || ':' || t.fromObjectId = ?) union all "
				+ "(select ord.* from DDM_DIS_TASKINFOLINK t,DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink,DDM_DIS_ORDER ord"
				+ " where disInfo.innerId = t.toObjectId and disInfo.classId = t.toObjectClassId"
				+ " and objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
				+ " and ord.innerId = objLink.fromObjectId and ord.classId = objLink.fromObjectClassId"
				+ " and t.fromObjectClassId || ':' || t.fromObjectId = ?)) disOrder";

		List<Map<String, Object>> list = PersistHelper.getService().query(sqlOrder, taskOid, taskOid);
		if (!list.isEmpty()) {
			Map<String, Object> map = list.get(0);
			String orderName = map.get("NAME").toString();
			String oid = map.get("CLASSID").toString() + ":" + map.get("INNERID").toString();
			disTask.setOrderName(orderName);
			disTask.setOrderOid(oid);
		}
		return disTask;
	}

	public List<DistributeInfo> getDistributeInfos(String distributeElecOid) {
		String innerId = Helper.getInnerId(distributeElecOid);
		String classId = Helper.getClassId(distributeElecOid);

		String sql = "(SELECT A.* FROM DDM_DIS_INFO A, DDM_DIS_TASKINFOLINK B "
				+ "WHERE A.CLASSID = B.TOOBJECTCLASSID AND A.INNERID = B.TOOBJECTID "
				+ " AND B.FROMOBJECTID = ? AND B.FROMOBJECTCLASSID = ? ) UNION ALL "
				+ "(SELECT A.* FROM DDM_DIS_INFO A, DDM_DIS_TASKDOMAINLINK B "
				+ "WHERE A.CLASSID = B.TOOBJECTCLASSID AND A.INNERID = B.TOOBJECTID "
				+ " AND B.FROMOBJECTID = ? AND B.FROMOBJECTCLASSID = ? )";

		List<DistributeInfo> infos = PersistHelper.getService().query(sql, DistributeInfo.class, innerId, classId,
				innerId, classId);

		return infos;
	}

	public List<DistributeObject> getDistributeObjects(String distributeElecOid) {
		String innerId = Helper.getInnerId(distributeElecOid);
		String classId = Helper.getClassId(distributeElecOid);

		String sql = "(SELECT DISTINCT C.* FROM DDM_DIS_INFO A, DDM_DIS_TASKINFOLINK B, DDM_DIS_ORDEROBJLINK C"
				+ " WHERE A.CLASSID = B.TOOBJECTCLASSID AND A.INNERID = B.TOOBJECTID"
				+ " AND A.DISORDEROBJLINKID = C.INNERID AND A.DISORDEROBJLINKCLASSID = C.CLASSID "
				+ " AND B.FROMOBJECTID = ? AND B.FROMOBJECTCLASSID = ?)" + " UNION ALL ("
				+ "SELECT DISTINCT C.* FROM DDM_DIS_INFO A, DDM_DIS_TASKDOMAINLINK B, DDM_DIS_ORDEROBJLINK C"
				+ " WHERE A.CLASSID = B.TOOBJECTCLASSID AND A.INNERID = B.TOOBJECTID"
				+ " AND A.DISORDEROBJLINKID = C.INNERID AND A.DISORDEROBJLINKCLASSID = C.CLASSID "
				+ " AND B.FROMOBJECTID = ? AND B.FROMOBJECTCLASSID = ?)";

		List<DistributeOrderObjectLink> linkList = PersistHelper.getService().query(sql,
				DistributeOrderObjectLink.class, innerId, classId, innerId, classId);
		List<DistributeObject> objList = new ArrayList<DistributeObject>();
		for (DistributeOrderObjectLink link : linkList) {
			DistributeObject disObject = (DistributeObject) link.getTo();
			disObject.setDistributeOrderObjectLink(link);
			objList.add(disObject);
		}
		return objList;
	}

	public List<DistributeObject> getDistributeObject(String distributeElecOid) {
		String innerId = Helper.getInnerId(distributeElecOid);
		String classId = Helper.getClassId(distributeElecOid);

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

	public void createDistributeInfos(String linkOids, String iids, String taskOid, String type) {
		List<String> iidList = SplitString.string2List(iids, ",");
		List<String> oidsList = SplitString.string2List(linkOids, ",");
		Principal principal = null;
		for (String iid : iidList) {
			if ("0".equals(type)) {
				principal = UserHelper.getService().getUser(iid);
			} else if ("1".equals(type)) {
				principal = OrganizationHelper.getService().getOrganization(iid);
			}
			for (String linkOid : oidsList) {
				String innerId = Helper.getInnerId(linkOid);
				String classId = Helper.getClassId(linkOid);
				String sql = "select t.* from DDM_DIS_INFO t where t.disOrderObjLinkId=? and t.disOrderObjLinkClassId=? and t.disInfoId=? and t.infoClassId=? and t.disType=?";
				List<DistributeInfo> list = PersistHelper.getService().query(sql, DistributeInfo.class, innerId,
						classId, principal.getInnerId(), principal.getClassId(), 3);
				if (list.size() == 0) {
					DistributeInfo info = newDistributeInfo();
					// �ַ���ʽ��0Ϊֱ�ӷַ���1Ϊ������2Ϊ�Ƴ���3Ϊת����
					info.setDisType("3");
					// �ַ�����
					info.setDisInfoNum(1);
					// �ַ���Ϣ���ƣ���λ/��Ա��
					info.setDisInfoName(principal.getName());
					// �ַ���Ϣ�����ʶ����Ա������֯�����ʶ��
					info.setInfoClassId(principal.getClassId());
					// �ַ���ϢIID����Ա����֯���ڲ���ʶ��
					info.setDisInfoId(principal.getInnerId());
					// �ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
					info.setDisMediaType(ConstUtil.C_DISMEDIATYPE_1);
					// �ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
					info.setDisInfoType("1");
					// info.setDisUrgent("0");
					info.setDisOrderObjLinkId(Helper.getInnerId(linkOid));
					info.setDisOrderObjLinkClassId(Helper.getClassId(linkOid));
					Helper.getPersistService().save(info);
				}
			}
		}
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

	public void createDistributeForwardElecTask(String taskOid, String oids) {
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		Persistable taskObj = Helper.getPersistService().getObject(taskOid);
		DistributeElecTask disTask = (DistributeElecTask) taskObj;
		List<String> oidsList = SplitString.string2List(oids, ",");

		String infoId = oidsList.get(0);
		String infoInnerId = Helper.getInnerId(infoId);
		String infoClassId = Helper.getClassId(infoId);
		String orderSql = "SELECT D.*" + " FROM DDM_DIS_INFO F,DDM_DIS_ORDEROBJLINK L,DDM_DIS_ORDER D"
				+ " WHERE L.INNERID = F.DISORDEROBJLINKID AND L.CLASSID = F.DISORDEROBJLINKCLASSID"
				+ " AND L.FROMOBJECTID = D.INNERID AND L.FROMOBJECTCLASSID = D.CLASSID "
				+ "AND F.INNERID = ? AND F.CLASSID = ?";

		List<DistributeOrder> orderList = PersistHelper.getService().query(orderSql, DistributeOrder.class,
				infoInnerId, infoClassId);
		DistributeOrder order = orderList.get(0);

		String objLinkSql = "(SELECT DISTINCT objLink.* FROM DDM_DIS_TASKINFOLINK l, DDM_DIS_INFO f, DDM_DIS_ORDEROBJLINK objLink"
				+ " WHERE f.classId || ':' || f.innerId = l.toObjectClassId || ':' || l.toObjectId "
				+ "AND f.disOrderObjLinkClassId || ':' || f.disOrderObjLinkId = objLink.classId || ':' || objLink.innerId "
				+ "AND l.fromObjectClassId || ':' || l.fromObjectId = ?) union all "
				+ "(SELECT DISTINCT objLink.* FROM DDM_DIS_TASKDOMAINLINK l, DDM_DIS_INFO f, DDM_DIS_ORDEROBJLINK objLink"
				+ " WHERE f.classId || ':' || f.innerId = l.toObjectClassId || ':' || l.toObjectId "
				+ "AND f.disOrderObjLinkClassId || ':' || f.disOrderObjLinkId = objLink.classId || ':' || objLink.innerId "
				+ "AND l.fromObjectClassId || ':' || l.fromObjectId = ?)";
		List<DistributeOrderObjectLink> linkList = Helper.getPersistService().query(objLinkSql,
				DistributeOrderObjectLink.class, taskOid, taskOid);
		String linkOids = "";
		for (DistributeOrderObjectLink link : linkList) {
			linkOids += "'" + link.getClassId() + ":" + link.getInnerId() + "',";
		}

		String infoSql = "SELECT f.* FROM DDM_DIS_INFO f, DDM_DIS_ORDEROBJLINK objLink"
				+ " WHERE f.disOrderObjLinkClassId || ':' || f.disOrderObjLinkId = objLink.classId || ':' || objLink.innerId"
				+ " AND f.DISTYPE = '3' AND objLink.classId || ':' || objLink.innerId in (" + linkOids + "'')";
		List<DistributeInfo> infoList = Helper.getPersistService().query(infoSql, DistributeInfo.class);
		List<DistributeInfo> infoList1 = new ArrayList<DistributeInfo>();
		for (DistributeInfo dis : infoList) {
			String distributeOrderOid = order.getOid();
			String hql = "from DistributeTaskInfoLink t where t.toObjectRef.innerId=? and t.toObjectRef.classId=?";
			List<DistributeTaskInfoLink> links = PersistHelper.getService().find(hql, dis.getInnerId(),
					dis.getClassId());
			//ȡ���û�
			//			User receiveUser = UserHelper.getService().getUserById(dis.getDisInfoId());
			String sqliid = "select aauserinnerid  from  plm_principal_user where innerid=?";
			List<Map<String, Object>> list = Helper.getPersistService().query(sqliid, dis.getDisInfoId());
			User receiveUser = UserHelper.getService().getUser(list.get(0).get("AAUSERINNERID") + "");
			if (links == null || links.isEmpty()) {
				String taskSql = "SELECT T.* FROM DDM_DIS_TASKINFOLINK L, DDM_DIS_ELECTASK T, DDM_DIS_INFO F, DDM_DIS_ORDEROBJLINK LINK"
						+ " WHERE T.INNERID = L.FROMOBJECTID AND T.CLASSID = L.FROMOBJECTCLASSID"
						+ " AND L.TOOBJECTID = F.INNERID AND L.TOOBJECTCLASSID = F.CLASSID"
						+ " AND F.DISORDEROBJLINKID = LINK.INNERID AND F.DISORDEROBJLINKCLASSID = LINK.CLASSID"
						+ " AND F.DISINFOID = ? AND F.INFOCLASSID = ? AND F.DISMEDIATYPE = ? AND T.STATENAME = ? AND LINK.FROMOBJECTCLASSID || ':' || LINK.FROMOBJECTID = ?";
				// ���ڸ��û��ĵ�����Ϣ
				List<DistributeElecTask> taskList = PersistHelper.getService().query(taskSql, DistributeElecTask.class,
						dis.getDisInfoId(), dis.getInfoClassId(), "1", ConstUtil.LC_NOT_SIGNED.getName(),
						distributeOrderOid);
				// �������û��ĵ���������ڣ����link��
				if (taskList.size() > 0) {
					createDistributeTaskInfoLink(taskList.get(0), dis);
					// �ַ���Ϣ��ӷ���ʱ�䣬������������
					dis.setSendTime(System.currentTimeMillis());

					if (ConstUtil.LC_NOT_DISTRIBUT.getName().equals(dis.getLifeCycleInfo().getStateName())) {
						life.promoteLifeCycle(dis);
					}
					infoList1.add(dis);
					setUserPrivilege(dis, receiveUser);
				} else {
					DistributeElecTaskService service = DistributeHelper.getDistributeElecTaskService();
					DistributeElecTask disElecTask = service.newDistributeElecTask();
					disElecTask.setNumber(order.getNumber() + ConstUtil.C_ELECTASK_STR);
					disElecTask.setName(order.getName() + ConstUtil.C_ELECTASK_STR);
					disElecTask.setNote("");
					disElecTask.setContextInfo(order.getContextInfo());
					disElecTask.setDomainInfo(order.getDomainInfo());
					disElecTask.setFromTaskId(disTask.getInnerId());
					disElecTask.setFromTaskClassId(disTask.getClassId());

					// ���������������
					service.createDistributeElecTask(disElecTask);

					// ���·ַ���Ϣ�ķ���ʱ��

					dis.setSendTime(System.currentTimeMillis());
					if (ConstUtil.LC_NOT_DISTRIBUT.getName().equals(dis.getLifeCycleInfo().getStateName())) {
						life.promoteLifeCycle(dis);
					}
					infoList1.add(dis);

					DistributeTaskInfoLink link = newDistributeTaskInfoLink();
					link.setToObject(dis);
					link.setFromObject(disElecTask);
					// �������ͣ�0��ֽ������1����������
					link.setTaskType("1");
					Helper.getPersistService().save(link);
					//Ϊ�ַ�������Ȩ
					setUserPrivilege(dis, receiveUser);
					// ���·ַ���Ϣ�ķ���ʱ��
					// dis.setSendTime(System.currentTimeMillis());

				}
			}
			//ת���ַ������¼��־
			Context context= disTask.getContextInfo().getContext();
			List<String> objects = new ArrayList<String>();
			objects.add(SessionHelper.getService().getUser().getName());//�û���
			objects.add("ת���ַ�����");
			objects.add(disTask.getNumber());
			objects.add(receiveUser.getName());
			int level=1;
			String logType="module";
			String objName=disTask.getName();
			int objectSecurity=0;
			String moduleSource="���Ź���";
			String objectType="��������";
			String operation="ת���ַ�����";
			String messageId="ddm.log.forwardElecTask";
			AuditLogHelper.getService().addLog( level, logType, context, 
					disTask.getInnerId(), disTask.getClassId(), objName, objectSecurity, 
					moduleSource, objectType, operation, messageId, objects);

		}
		if (infoList1.size() > 0 || infoList1 != null) {
			Helper.getPersistService().update(infoList1);
		}

		// ���շ�������Ϣ������ �����ӷַ�ת����Ϣ���ַ���������Ϣ��
		// (ת������ǩ�������ʱ����õ�A3�ĵ���id������A5���ŵ�id���Ҷ�Ӧ��A3�ĵ���id)
		String orderIID = DataConvertConfigHelper.getService().getA4xSendObjIID(
				DTSiteConstant.DTSITE_APPVERSION_3_5, order.getOid(),
				A4XObjTypeConstant.OBJTYPE_DISTRIBUTEORDER);
		if(!StringUtil.isStringEmpty(orderIID)){
			String isModifyOrderFlowState = "false"; //�����շ�ת�������ʱ���𷽲���Ҫ�޸ĵ���״̬
			Map<String, String> reqParamMap = new HashMap<String, String>();
			
			reqParamMap.put("orderIID", orderIID);
			reqParamMap.put("isModifyOrderFlowState", isModifyOrderFlowState);
			reqParamMap.put("orderOid", order.getOid());
	
			TransferObjectService transferService = TransferObjectHelper
					.getTransferService();
	
			// ϵͳ����ʱ���÷���
			DmcConfigService dmcConfigService = DmcConfigHelper
					.getService();
			// ��ȡ�����Ƿ�ʹ��ʵ��ģʽ
			String isEntityDistributeModel = dmcConfigService
					.getDmcConfigValue(ConstUtil.IS_ENTITY_DISTRIBUTEMODE_YES);
			// �ַ���Ϣ����
			String sendMessageType = "";
			if ("true".equalsIgnoreCase(isEntityDistributeModel)) {
				//ʵ��ַ����ַ���������Ϣ
				sendMessageType = Message4XTypeConstant.DISTRIBUTE_DISTRIBUTEREPLY_A5;
			} else {
				//���ӷַ������ӷַ�ת����Ϣ
				sendMessageType = Message4XTypeConstant.DISTRIBUTE_UNPACKAGETASKDATABIND_A5;
			}
	
			Site sourceSite = SiteHelper.getSiteService().findSiteById(disTask.getSourceSiteId());
			// ����
			transferService.sendRequest(UUID.getUID(), order.getNumber(),
					sendMessageType, null, sourceSite, reqParamMap,
					null, TransferConstant.REQTYPE_ASYN);
		}
	}

	public void setUserPrivilege(DistributeInfo disInfo, User user) {
		String sql = "SELECT OBJ.* FROM DDM_DIS_ORDEROBJLINK LINK,DDM_DIS_OBJECT OBJ WHERE"
				+ " OBJ.CLASSID || ':' || OBJ.INNERID = LINK.TOOBJECTCLASSID || ':' || LINK.TOOBJECTID"
				+ " AND LINK.CLASSID || ':' || LINK.INNERID = ?";
		List<DistributeObject> objectList = Helper.getPersistService().query(sql, DistributeObject.class,
				disInfo.getDisOrderObjLinkClassId() + ":" + disInfo.getDisOrderObjLinkId());
		DistributePrincipalService principalService = DistributeHelper.getDistributePrincipalService();
		for (DistributeObject object : objectList) {
			//ȡ��ҵ���������Ϣ
			Domained domained = (Domained) PersistHelper.getService().getObject(
					object.getDataClassId() + ":" + object.getDataInnerId());
			if(domained==null){
				//domained = (Domained)DomainHelper.getService().getDomain(object.getDomainInfo().getDomain().getAaDomainRef());
				//�����domainedΪnull��ʱ�򣬴˶�������Ȩ
				continue;
			}
			boolean flag = AccessControlHelper.getService().hasEntityPermission(user, Operate.ACCESS, domained);
			if (flag == false) {
				//���÷���Ȩ��
				principalService.setPriviledge(domained, user);
			}
		}
	}

	/**
	 * �����ַ���Ϣ���������link����
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
		// �������ͣ�0��ֽ������1����������
		linkDisObject.setTaskType("1");
		service.createDistributeTaskInfoLink(linkDisObject);
	}

	@Override
	public DistributeInfo newDistributeInfo() {
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		DistributeInfo dis = (DistributeInfo) PersistUtil.createObject(DistributeInfo.CLASSID);

		// �������ڳ�ʼ��
		life.initLifecycle(dis);

		dis.setClassId(dis.CLASSID);
		return dis;
	}

	@Override
	public DistributeTaskInfoLink newDistributeTaskInfoLink() {
		DistributeTaskInfoLink disObj = (DistributeTaskInfoLink) PersistUtil
				.createObject(DistributeTaskInfoLink.CLASSID);
		disObj.setClassId(disObj.CLASSID);
		return disObj;
	}

	public List<DistributeInfo> getDistributeForwardInfos(String distributeElecOid) {
		// String innerId = Helper.getInnerId(distributeElecOid);
		// String classId = Helper.getClassId(distributeElecOid);
		User currentUser = SessionHelper.getService().getUser();
		Map<String, DistributeInfo> oidMap = new HashMap<String, DistributeInfo>();

		// String sql1 =
		// "SELECT DISTINCT D.*,C.* FROM DDM_DIS_INFO A, DDM_DIS_TASKINFOLINK B, DDM_DIS_ORDEROBJLINK C, DDM_DIS_OBJECT D "
		// + "WHERE A.CLASSID = B.TOOBJECTCLASSID AND A.INNERID = B.TOOBJECTID "
		// +
		// "AND A.DISORDEROBJLINKID = C.INNERID AND A.DISORDEROBJLINKCLASSID = C.CLASSID "
		// + "AND C.TOOBJECTID = D.INNERID AND C.TOOBJECTCLASSID = D.CLASSID "
		// + " AND B.FROMOBJECTID = ? AND B.FROMOBJECTCLASSID = ? ";
		//
		// List<DistributeObject> objList =
		// PersistHelper.getService().query(sql1, DistributeObject.class,
		// innerId,
		// classId);
		String objLinkSql = "(SELECT DISTINCT objLink.* FROM DDM_DIS_TASKINFOLINK l, DDM_DIS_INFO f, DDM_DIS_ORDEROBJLINK objLink"
				+ " WHERE f.classId || ':' || f.innerId = l.toObjectClassId || ':' || l.toObjectId "
				+ "AND f.disOrderObjLinkClassId || ':' || f.disOrderObjLinkId = objLink.classId || ':' || objLink.innerId "
				+ "AND l.fromObjectClassId || ':' || l.fromObjectId = ?) union all"
				+ "(SELECT DISTINCT objLink.* FROM DDM_DIS_TASKDOMAINLINK l, DDM_DIS_INFO f, DDM_DIS_ORDEROBJLINK objLink"
				+ " WHERE f.classId || ':' || f.innerId = l.toObjectClassId || ':' || l.toObjectId "
				+ "AND f.disOrderObjLinkClassId || ':' || f.disOrderObjLinkId = objLink.classId || ':' || objLink.innerId "
				+ "AND l.fromObjectClassId || ':' || l.fromObjectId = ?)";
		List<DistributeOrderObjectLink> linkList = Helper.getPersistService().query(objLinkSql,
				DistributeOrderObjectLink.class, distributeElecOid, distributeElecOid);
		String linkOids = "";
		for (DistributeOrderObjectLink link : linkList) {
			linkOids += "'" + link.getClassId() + ":" + link.getInnerId() + "',";
		}

		String infoSql = "SELECT f.* FROM DDM_DIS_INFO f, DDM_DIS_ORDEROBJLINK objLink"
				+ " WHERE f.disOrderObjLinkClassId || ':' || f.disOrderObjLinkId = objLink.classId || ':' || objLink.innerId"
				+ " AND f.DISTYPE = '3' AND objLink.classId || ':' || objLink.innerId in (" + linkOids + "'')"
				+ " AND F.CREATEBYID = ? AND F.CREATEBYCLASSID = ?";
		List<DistributeInfo> infoList = Helper.getPersistService().query(infoSql, DistributeInfo.class,
				currentUser.getInnerId(), currentUser.getClassId());

		List<DistributeInfo> list = new ArrayList<DistributeInfo>();
		// for (DistributeObject obj : objList) {
		// String infoSql = "SELECT DISTINCT F.*" +
		// " FROM DDM_DIS_ORDEROBJLINK L,DDM_DIS_INFO F"
		// +
		// " WHERE L.INNERID = F.DISORDEROBJLINKID AND L.CLASSID = F.DISORDEROBJLINKCLASSID"
		// + " AND F.DISTYPE = '3'" +
		// " AND L.TOOBJECTID = ? AND L.TOOBJECTCLASSID = ?"
		// + " AND F.CREATEBYID = ? AND F.CREATEBYCLASSID = ?";
		// List<DistributeInfo> infoList =
		// PersistHelper.getService().query(infoSql, DistributeInfo.class,
		// obj.getInnerId(), obj.getClassId(), currentUser.getInnerId(),
		// currentUser.getClassId());
		for (DistributeInfo info : infoList) {
			if (list.contains(info)) {
				oidMap.get(info.getKey()).addOid(info.getOid());
				continue;
			}
			info.addOid(info.getOid());
			oidMap.put(info.getKey(), info);
			list.add(info);
		}
		// }
		return list;
	}

	public List<DistributeInfo> getDistributeForwardInfoByOids(String orderObjectLinkOids, String taskOid) {
		List<String> oidsList = SplitString.string2List(orderObjectLinkOids, ",");
		List<DistributeInfo> infoList = new ArrayList<DistributeInfo>();
		Map<String, DistributeInfo> oidMap = new HashMap<String, DistributeInfo>();
		if (oidsList == null || oidsList.isEmpty()) {
			List<DistributeInfo> infoTemp = getDistributeForwardInfos(taskOid);
			infoList.addAll(infoTemp);
		} else {
			for (String oid : oidsList) {
				String linkInnerId = Helper.getInnerId(oid);
				String linkClassId = Helper.getClassId(oid);
				User currentUser = SessionHelper.getService().getUser();

				String infoSql = "SELECT DISTINCT F.*" + " FROM DDM_DIS_ORDEROBJLINK L,DDM_DIS_INFO F"
						+ " WHERE L.INNERID = F.DISORDEROBJLINKID AND L.CLASSID = F.DISORDEROBJLINKCLASSID"
						+ " AND F.DISTYPE = '3'" + " AND L.INNERID = ? AND L.CLASSID = ?"
						+ " AND F.CREATEBYID = ? AND F.CREATEBYCLASSID = ?";
				List<DistributeInfo> list = PersistHelper.getService().query(infoSql, DistributeInfo.class,
						linkInnerId, linkClassId, currentUser.getInnerId(), currentUser.getClassId());
				for (DistributeInfo info : list) {
					if (infoList.contains(info)) {
						oidMap.get(info.getKey()).addOid(info.getOid());
						continue;
					}
					info.addOid(info.getOid());
					oidMap.put(info.getKey(), info);
					infoList.add(info);
				}
			}
		}
		return infoList;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see com.bjsasc.ddm.distribute.service.distributeelectask.
	 * DistributeElecTaskService#getDistributeElecTaskProperty(java.lang.String)
	 */
	public DistributeElecTask getDistributeElecTaskProperty(String oid) {
		Persistable obj = Helper.getPersistService().getObject(oid);
		DistributeElecTask dis = (DistributeElecTask) obj;
		String disElecTaskInnerId = Helper.getPersistService().getInnerId(oid);
		String disElecTaskClassId = Helper.getPersistService().getClassId(oid);

		String sql = "select DISOBJ.* from DDM_DIS_TASKINFOLINK t,DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink,DDM_DIS_OBJECT disObj"
				+ " where t.fromObjectId =? and t.fromObjectClassId =?"
				+ " and disInfo.innerId = t.toObjectId and disInfo.classId = t.toObjectClassId"
				+ " and objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
				+ " and disObj.innerId = objLink.toObjectId and disObj.classId = objLink.toObjectClassId";

		List<DistributeObject> list = PersistHelper.getService().query(sql, DistributeObject.class, disElecTaskInnerId,
				disElecTaskClassId);

		String sqlOrder = "select ORD.* from DDM_DIS_TASKINFOLINK t,DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink,DDM_DIS_ORDER ORD"
				+ " where t.fromObjectId = ? and t.fromObjectClassId = ?"
				+ " and disInfo.innerId = t.toObjectId and disInfo.classId = t.toObjectClassId"
				+ " and objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
				+ " and ORD.innerId = objLink.fromObjectId and ORD.classId = objLink.fromObjectClassId";

		List<DistributeOrder> orderList = PersistHelper.getService().query(sqlOrder, DistributeOrder.class,
				disElecTaskInnerId, disElecTaskClassId);

		if (list.size() > 0) {
			dis.setDataFrom(list.get(0).getDataFrom());
		}
		if (orderList.size() > 0) {
			dis.setOrderName(orderList.get(0).getName());
		}
		return dis;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see com.bjsasc.ddm.distribute.service.distributeelectask.
	 * DistributeElecTaskService
	 * #createDistributeElecTask(com.bjsasc.ddm.distribute
	 * .model.distributeelectask.DistributeElecTask)
	 */
	@Override
	public void createDistributeElecTask(DistributeElecTask dis) {
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		// �������ڳ�ʼ��
		life.initLifecycle(dis);
		Helper.getPersistService().save(dis);
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see com.bjsasc.ddm.distribute.service.distributeelectask.
	 * DistributeElecTaskService#createDistributeElecTask(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public void createDistributeElecTask(String number, String name, String note) {
		DistributeElecTask dis = newDistributeElecTask();
		dis.setNumber(number);
		dis.setName(name);
		dis.setNote(note);

		createDistributeElecTask(dis);
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see com.bjsasc.ddm.distribute.service.distributeelectask.
	 * DistributeElecTaskService#newDistributeElecTask()
	 */
	public DistributeElecTask newDistributeElecTask() {
		DistributeElecTask dis = (DistributeElecTask) PersistUtil.createObject(DistributeElecTask.CLASSID);
		return dis;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see com.bjsasc.ddm.distribute.service.distributeelectask.
	 * DistributeElecTaskService#updateDistributeElecTask(java.lang.String,
	 * com.bjsasc.ddm.common.ConstUtil.LIFECYCLE_OPT)
	 */
	public void updateDistributeElecTask(String oids, LIFECYCLE_OPT opt, String returnReason) {
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		List<String> oidList = SplitString.string2List(oids, ",");
		for (String oid : oidList) {
			Persistable obj = Helper.getPersistService().getObject(oid);
			DistributeElecTask dis = (DistributeElecTask) obj;
			try {
				if (LIFECYCLE_OPT.PROMOTE == opt) {
					// ������������
					life.promoteLifeCycle(dis);
					//ǩ�յ��������¼��־
					Context context= dis.getContextInfo().getContext();
					List<String> objects = new ArrayList<String>();
					objects.add(SessionHelper.getService().getUser().getName());//�û���
					objects.add("ǩ��");
					objects.add(dis.getNumber());
					int level=1;
					String logType="module";
					String objName=dis.getName();
					int objectSecurity=0;
					String moduleSource="���Ź���";
					String objectType="��������";
					String operation="ǩ��";
					String messageId="ddm.log.SignFor";
					AuditLogHelper.getService().addLog( level, logType, context, 
							dis.getInnerId(), dis.getClassId(), objName, objectSecurity, 
							moduleSource, objectType, operation, messageId, objects);
				} else if (LIFECYCLE_OPT.REJECT == opt) {
					// �������ھܾ�
					life.rejectLifeCycle(dis);
					//�ܾ�ǩ�յ��������¼��־
					Context context= dis.getContextInfo().getContext();
					List<String> objects = new ArrayList<String>();
					objects.add(SessionHelper.getService().getUser().getName());//�û���
					objects.add("�ܾ�ǩ��");
					objects.add(dis.getNumber());
					int level=1;
					String logType="module";
					String objName=dis.getName();
					int objectSecurity=0;
					String moduleSource="���Ź���";
					String objectType="��������";
					String operation="�ܾ�ǩ��";
					String messageId="ddm.log.RejectSignFor";
					AuditLogHelper.getService().addLog( level, logType, context, 
							dis.getInnerId(), dis.getClassId(), objName, objectSecurity, 
							moduleSource, objectType, operation, messageId, objects);
				}
				User currentUser = SessionHelper.getService().getUser();
				dis.setReceiveBy(currentUser);
				dis.setReceiveByName(currentUser.getName());
				dis.setReceiveTime(System.currentTimeMillis());
				Helper.getPersistService().update(dis);
				if (LIFECYCLE_OPT.REJECT == opt) {
					// ��ӻ�����Ϣ
					ReturnReason retReason = newReturnReason();
					retReason.setTaskId(dis.getInnerId());
					retReason.setTaskClassId(dis.getClassId());
					retReason.setLifeCycleInfo(dis.getLifeCycleInfo());
					retReason.setReturnReason(returnReason);
					retReason.setUserId(currentUser.getInnerId());
					retReason.setUserClassId(currentUser.getClassId());
					retReason.setUserName(currentUser.getName());
					Helper.getPersistService().save(retReason);
					//�ܾ�ǩ�ո������߷����ʼ�
					//ָ�������ߵ�����
					try{
						Map ctx = new HashMap();
						//ʹ�õ�ǰ��¼�ߵ�sessionid����
						HttpServletRequest request = ServletActionContext.getRequest();
						HttpSession session = request.getSession();
						String sSessionId = (String) session.getAttribute(AvidmConstDefine.SESSIONPARA_SESSIONID);
						//ָ������������
						//ͨ���û��ڲ���ʶ����û������ڻ���û�����
						User toUser = dis.getManageInfo().getCreateBy();
						String email=toUser.getEmail();
						DistributeOrder order = getDistributeOrderByElecTaskOid(dis.getOid());
						//��ǰʱ��
						Date date=new Date();
						DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String currentTime=format.format(date);
						String emailBody="�����ȵķַ���"+order.getNumber()+"��"+currentTime+"��"+currentUser.getName()+"���գ�ԭ����:"+returnReason+"�������Ҫ���봴���÷��ŵ��Ĳ����ַ��������в����ַ�.";
						AvidmMsgSender.avSendMessage(ctx, sSessionId, AvidmMsgSender.MSG_TYPE_MAIL,"���ȷַ���������!",emailBody, email,null);
					}catch(Exception e){
						Log.debug("������������ʼ�����ʧ�ܣ�");
					}
				}

				if ("1".equals(dis.getElecTaskType())) {
					//TODO ��汾����
					Site selfSite = SiteHelper.getSiteService().findLocalSiteInfo();
					//ȡ�÷���վ��
					Site sourceSite = SiteHelper.getSiteService().findSiteById(dis.getSourceSiteId());
					// ���͵�A3.5
					if (DTSiteConstant.DTSITE_APPVERSION_3_5
							.equalsIgnoreCase(sourceSite.getSystemVersion())) {
						DistributeOrder order = getDistributeOrderByElecTaskOid(dis.getOid());
						//(ת������ǩ�������ʱ����õ�A5���ŵ�id���Ҷ�Ӧ��A3�ĵ���id)
						String orderIID = DataConvertConfigHelper.getService().getA4xSendObjIID(
								DTSiteConstant.DTSITE_APPVERSION_3_5, order.getOid(),
								A4XObjTypeConstant.OBJTYPE_DISTRIBUTEORDER);
						if(!StringUtil.isStringEmpty(orderIID)){
							String isModifyOrderFlowState = "false"; //�����շ�ǩ�վܾ������ʱ���𷽲���Ҫ�޸ĵ���״̬
							Map<String, String> reqParamMap = new HashMap<String, String>();

							reqParamMap.put("orderIID", orderIID);
							reqParamMap.put("domainIID", selfSite.getInnerId());
							reqParamMap.put("isModifyOrderFlowState", isModifyOrderFlowState);
							reqParamMap.put("orderOid", order.getOid());
							reqParamMap.put("taskOid", oid);
							reqParamMap.put("receiverOid", currentUser.getOid());
							reqParamMap.put("receiveTime", String.valueOf(dis.getReceiveTime()));
							if (LIFECYCLE_OPT.PROMOTE == opt) {
								reqParamMap.put("opt", "PROMOTE");
							} else if (LIFECYCLE_OPT.REJECT == opt) {
								reqParamMap.put("opt", "REJECT");
								reqParamMap.put("returnReason", returnReason);
							}
					
							TransferObjectService transferService = TransferObjectHelper
									.getTransferService();
					
							// ϵͳ����ʱ���÷���
							DmcConfigService dmcConfigService = DmcConfigHelper
									.getService();
							// ��ȡ�����Ƿ�ʹ��ʵ��ģʽ
							String isEntityDistributeModel = dmcConfigService
									.getDmcConfigValue(ConstUtil.IS_ENTITY_DISTRIBUTEMODE_YES);
							// �ַ���Ϣ����
							String sendMessageType = "";
							if ("true".equalsIgnoreCase(isEntityDistributeModel)) {
								//ʵ��ַ����ַ���������Ϣ
								sendMessageType = Message4XTypeConstant.DISTRIBUTE_DISTRIBUTETASKOPERSYN_A5;
							} else {
								//���ӷַ������ӷַ�ת����Ϣ
								sendMessageType = Message4XTypeConstant.DISTRIBUTE_DISPATCHSIGTASKOPERSYN_A5;
							}
					
							// ����
							transferService.sendRequest(UUID.getUID(), dis.getNumber(),
									sendMessageType, null, sourceSite, reqParamMap,
									null, TransferConstant.REQTYPE_ASYN);
						}else{
							Log.debug("ǩ�����������Ϣʱ��A5���ŵ�id���Ҷ�Ӧ��A3�ĵ���idʧ�ܣ���A5���ŵ�Oid��" + order.getOid() + "��");
						}
					}else{
						//�������
						Map<String, String> reqParamMap = new HashMap<String, String>();
						reqParamMap.put("flag", "1");
						reqParamMap.put("taskOid", oid);
						reqParamMap.put("receiveByUserName", currentUser.getName());
						reqParamMap.put("receiveTime", String.valueOf(dis.getReceiveTime()));
						if (LIFECYCLE_OPT.PROMOTE == opt) {
							reqParamMap.put("opt", "PROMOTE");
						} else if (LIFECYCLE_OPT.REJECT == opt) {
							reqParamMap.put("opt", "REJECT");
							reqParamMap.put("userName", currentUser.getName());
							reqParamMap.put("returnReason", returnReason);
						}
	
//						//ȡ�÷���վ��
//						Site sourceSite = SiteHelper.getSiteService().findSiteById(dis.getSourceSiteId());
						//��������ͬ��������
						TransferObjectService transferService = TransferObjectHelper.getTransferService();
						transferService.sendRequest(UUID.getUID(), dis.getNumber(),
								MessageTypeconstant.REPLY_SEND_DISTRIBUTE, null, sourceSite, reqParamMap, null,
								TransferConstant.REQTYPE_SYN);
						//���޸�
						if (!"".equals(dis.getCenterSiteId()) && dis.getCenterSiteId() != null) {
							//ȡ������վ��
							Site centerSite = SiteHelper.getSiteService().findSiteById(dis.getCenterSiteId());
	
							//��������ͬ��������
							transferService.sendRequest(UUID.getUID(), dis.getNumber(),
									MessageTypeconstant.REPLY_SEND_DISTRIBUTE, null, centerSite, reqParamMap, null,
									TransferConstant.REQTYPE_SYN);
						}
					}
				}
			} catch (Exception e) {
				throw new RuntimeException("������������������ʧ�ܣ�", e);
			}
		}
	}

	private ReturnReason newReturnReason() {
		ReturnReason reason = (ReturnReason) PersistUtil.createObject(ReturnReason.CLASSID);
		return reason;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see com.bjsasc.ddm.distribute.service.distributeelectask.
	 * DistributeElecTaskService
	 * #deleteDistributeElecTaskProperty(java.lang.String)
	 */
	public void deleteDistributeElecTask(String oid) {
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

	public void updateDistributeElecTaskLife(String oids) {
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		List<String> oidList = SplitString.string2List(oids, ",");
		List<DistributeElecTask> elecList = new ArrayList<DistributeElecTask>();
		for (String oid : oidList) {
			Persistable obj = Helper.getPersistService().getObject(oid);
			DistributeElecTask dis = (DistributeElecTask) obj;
			life.promoteLifeCycle(dis);
			elecList.add(dis);
			//�ύ���������¼��־
			Context context= dis.getContextInfo().getContext();
			List<String> objects = new ArrayList<String>();
			objects.add(SessionHelper.getService().getUser().getName());//�û���
			objects.add("�ύ�ַ�����");
			objects.add(dis.getNumber());
			int level=1;
			String logType="module";
			String objName=dis.getName();
			int objectSecurity=0;
			String moduleSource="���Ź���";
			String objectType="��������";
			String operation="�ύ�ַ�����";
			String messageId="ddm.log.finishSignFor";
			AuditLogHelper.getService().addLog( level, logType, context, 
					dis.getInnerId(), dis.getClassId(), objName, objectSecurity, 
					moduleSource, objectType, operation, messageId, objects);
			if (ConstUtil.ELEC_TASKTYPE_OUT.equals(dis.getElecTaskType())) {
				Site targetSite = (Site) SiteHelper.getSiteService().findSiteById(dis.getSourceSiteId());

				//�������
				Map<String, String> reqParamMap = new HashMap<String, String>();
				reqParamMap.put("flag", "1");
				reqParamMap.put("taskOid", oid);
				reqParamMap.put("opt", "PROMOTE");

				//��������ͬ��(����)
				TransferObjectService transferService = TransferObjectHelper.getTransferService();
				transferService.sendRequest(UUID.getUID(), dis.getNumber(), MessageTypeconstant.REPLY_SEND_DISTRIBUTE,
						null, (Site) targetSite, reqParamMap, null, TransferConstant.REQTYPE_SYN);

				//���Ϊ���շ���ͬ������
				if (!"".equals(dis.getCenterSiteId()) && dis.getCenterSiteId() != null) {
					Site centerSite = (Site) SiteHelper.getSiteService().findSiteById(dis.getCenterSiteId());
					transferService.sendRequest(UUID.getUID(), dis.getNumber(),
							MessageTypeconstant.REPLY_SEND_DISTRIBUTE, null, centerSite, reqParamMap, null,
							TransferConstant.REQTYPE_SYN);
				}
			}
		}
		if (elecList.size() > 0 || elecList != null) {
			Helper.getPersistService().update(elecList);
		}
	}

	public String deleteDistributeInfos(String oids) {
		List<String> oidList = SplitString.string2List(oids, ",");
		String flag = "";
		List<DistributeInfo> infoList = new ArrayList<DistributeInfo>();
		for (String oid : oidList) {
			String taskLinkSql = "select * from DDM_DIS_TASKINFOLINK where toObjectClassId || ':' || toObjectId =?";
			List<DistributeTaskInfoLink> linkList = PersistHelper.getService().query(taskLinkSql,
					DistributeTaskInfoLink.class, oid);
			if (linkList.isEmpty()) {
				Persistable obj = Helper.getPersistService().getObject(oid);
				DistributeInfo disInfo = (DistributeInfo) obj;
				infoList.add(disInfo);
				flag = "true";
			} else {
				flag = "false";
				break;
			}
		}
		if (infoList.size() > 0 || infoList != null) {
			Helper.getPersistService().delete(infoList);
		}
		return flag;
	}

	public List<DistributeElecTask> getDistributeSonElecTask(String distributeElecOid) {
		String innerId = Helper.getInnerId(distributeElecOid);
		String classId = Helper.getClassId(distributeElecOid);

		String sql = "select *from  ddm_dis_electask where fromtaskid=? and fromtaskclassid=? ";

		List<DistributeElecTask> listElecTasks = PersistHelper.getService().query(sql, DistributeElecTask.class,
				innerId, classId);
		List<DistributeElecTask> list = new ArrayList<DistributeElecTask>();
		for (DistributeElecTask task : listElecTasks) {
			String sqlinfo = "SELECT DISTINCT c .* FROM DDM_DIS_ELECTASK A, DDM_DIS_TASKINFOLINK B, DDM_DIS_INFO C "
					+ " WHERE  B.FROMOBJECTID=? AND B.FROMOBJECTCLASSID=? "
					+ "AND C.INNERID = B.TOOBJECTID AND C.CLASSID = B.TOOBJECTCLASSID";
			List<DistributeInfo> taskinfo = Helper.getPersistService().query(sqlinfo, DistributeInfo.class,
					task.getInnerId(), task.getClassId());
			task.setDisinfo(taskinfo.get(0));
			list.add(task);
		}

		return list;
	}

	public String getExitDistributeElecInfo(String linkOids, String iids, String type) {
		List<String> iidList = SplitString.string2List(iids, ",");
		List<String> oidsList = SplitString.string2List(linkOids, ",");
		Principal principal = null;
		String inWhere = "";
		for (String iid : iidList) {
			if ("0".equals(type)) {
				principal = UserHelper.getService().getUser(iid);
			} else if ("1".equals(type)) {
				principal = OrganizationHelper.getService().getOrganization(iid);
			}
			for (String linkOid : oidsList) {
				inWhere += "'" + Helper.getOid(principal) + ";" + linkOid + "',";
			}
		}
		String sql = "select count(*) CNT from DDM_DIS_INFO info "
				+ "where INFO.INFOCLASSID || ':' || INFO.DISINFOID || ';' "
				+ "|| INFO.DISORDEROBJLINKCLASSID || ':' || INFO.DISORDEROBJLINKID in (" + inWhere
				+ "'') and info.disType='3'";

		List<Map<String, Object>> list = Helper.getPersistService().query(sql);
		Object object = list.get(0).get("CNT");
		String size = object.toString();
		return size;
	}

	@Override
	public boolean isUserlimit() {
		boolean flat = false;
//		User currentUser = SessionHelper.getService().getUser();
//		String sql = "select DISTINCT userref from AAGROUP a,aauserprincipal b where a.innerid=b.principalvalue and a.id='second_sche'";
//		List<Map<String, Object>> list = Helper.getPersistService().query(sql);
//		String userId = currentUser.getAaUserInnerId();
//		for (int i = 0; i < list.size(); i++) {
//			if (userId.equals(list.get(i).get("USERREF"))) {
//				flat = true;
//			}
//		}
//		if (flat == false) {
//			DmcRole dmcRole = DmcRoleHelper.getService().findDmcRoleByRoleID(ConstUtil.DIS_CROSS_DOMAIN_ROLE);
//			if (dmcRole != null) {
//				List<DmcRoleUser> roleUsers = DmcRoleHelper.getService().findDmcRoleUserByRoleID(dmcRole.getInnerId());
//				if (roleUsers != null && !roleUsers.isEmpty()) {
//					Iterator<DmcRoleUser> it = roleUsers.iterator();
//					while (it.hasNext()) {
//						DmcRoleUser user = (DmcRoleUser) it.next();
//						if (currentUser.getAaUserInnerId().equals(user.getUserIID())) {
//							flat = true;
//							continue;
//						}
//					}
//				}
//			}
//		}

		//�жϵ�ǰ�û��Ƿ��Ƕ�������Ա
		boolean isSecondScheUser = this.isSecondScheUser();
		//�жϵ�ǰ�û��Ƿ��ǿ���Э��Ա
		boolean isCrossDomainUser = this.isCrossDomainUser();
		
		if(isSecondScheUser || isCrossDomainUser){
			flat = true;
		}
		return flat;
	}
	
	@Override
	public boolean isSecondScheUser() {
		boolean flat = false;
		//ȡ�õ�ǰ�û�
		User currentUser = SessionHelper.getService().getUser();
		//ȡ�õ�ǰ�û�����֯����
		Organization currentOrganization = currentUser.getOrganization();
		if(currentOrganization == null){
			return false;
		}
		//ȡ�ö���������
		Group secondScheGroup = GroupHelper.getService().getGroupById("second_sche");
		//�жϵ�ǰ�û��Ƿ��ǵ�ǰ��֯�Ķ�������Ա���е��û�
		flat = this.isOrgGroupUser(currentOrganization,secondScheGroup,currentUser);
		return flat;
	}
	
	/**
	 * �жϸ��û��ǲ�����֯������û�
	 * @param org
	 * @param group
	 * @param user
	 * @return
	 */
	private boolean isOrgGroupUser(Organization org,Group group,User user){
		List<User> users = OrganizationHelper.getService().getOrganizationGroupUsers(org, group);
		if(!users.isEmpty()){
			for(User u:users){
				if(u.getAaUserInnerId().equals(user.getAaUserInnerId())){
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean isCrossDomainUser() {
		boolean flat = false;
		User currentUser = SessionHelper.getService().getUser();
		DmcRole dmcRole = DmcRoleHelper.getService().findDmcRoleByRoleID(ConstUtil.DIS_CROSS_DOMAIN_ROLE);
		if (dmcRole != null) {
			List<DmcRoleUser> roleUsers = DmcRoleHelper.getService().findDmcRoleUserByRoleID(dmcRole.getInnerId());
			if (roleUsers != null && !roleUsers.isEmpty()) {
				Iterator<DmcRoleUser> it = roleUsers.iterator();
				while (it.hasNext()) {
					DmcRoleUser user = (DmcRoleUser) it.next();
					if (currentUser.getAaUserInnerId().equals(user.getUserIID())) {
						flat = true;
						break;
					}
				}
			}
		}

		return flat;
	}

	/**
	 * �ж��Ƿ������˿���Э��Ա
	 * @return
	 */
	private boolean isSetCrossDomainUser() {
		boolean flat = false;
		DmcRole dmcRole = DmcRoleHelper.getService().findDmcRoleByRoleID(ConstUtil.DIS_CROSS_DOMAIN_ROLE);
		if (dmcRole != null) {
			List<DmcRoleUser> roleUsers = DmcRoleHelper.getService().findDmcRoleUserByRoleID(dmcRole.getInnerId());
			if (roleUsers != null && !roleUsers.isEmpty()) {
				flat = true;
			}
		}

		return flat;
	}
	
	public List<DistributeElecTask> getAllOrganizationElectdistribute(Object stateName) {
//		User currentUser = SessionHelper.getService().getUser();
//		//String noRec = "δǩ��";
//		String sql = " SELECT DISTINCT A.* FROM DDM_DIS_ELECTASK A, DDM_DIS_TASKINFOLINK B,DDM_DIS_INFO C,AAUSERDATA D,"
//				+ "AAUSERPRINCIPAL S, AAGROUP P, PLM_PRINCIPAL_ORG O  WHERE A.INNERID = B.FROMOBJECTID AND A.CLASSID = B.FROMOBJECTCLASSID "
//				+ "AND C.INNERID = B.TOOBJECTID AND C.CLASSID = B.TOOBJECTCLASSID AND A.STATENAME = ? AND C.INFOCLASSID = 'Organization' "
//				+ "AND O.INNERID = C.DISINFOID AND O.AAORGINNERID = D.USERORG AND D.INNERID = ? AND S.USERREF = ? AND P.INNERID = S.PRINCIPALVALUE AND S.USERREF = D.INNERID AND P.ID='second_sche' ";
//		List<DistributeElecTask> taskList = Helper.getPersistService().query(sql, DistributeElecTask.class, stateName,
//				currentUser.getAaUserInnerId(), currentUser.getAaUserInnerId());
//
//		return taskList;
//		
		//�޸�bug15291 ��Ҫ�ж���Ϊ��������Ա���û�ͬ�ͺŵ��߼���ϵ��ֻ�����ͺ��µ��Ŷ��вſ��Կ�������ͺ��µĵ�������
		long from = 0;
		long to = 0;
		String name = stateName.toString();
		List<DistributeElecTask> disElecTasklistSecondSche = DistributeHelper.getDistributeElecTaskService().getDistributeElecTaskForSecondSche("'"+name+"'",from ,to );
		return disElecTasklistSecondSche;
	}

	public void sendDistributeToOutSign(String elecTaskOids) {
		List<String> elecTaskOidList = SplitString.string2List(elecTaskOids, ",");
		String outSiteInfos = "'";
		//		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		for (String taskOid : elecTaskOidList) {
			DistributeElecTask dis = (DistributeElecTask) Helper.getPersistService().getObject(taskOid);
			if (ConstUtil.ELEC_TASKTYPE_OUT.equals(dis.getElecTaskType())) {
				//				life.promoteLifeCycle(dis);
				//				Helper.getPersistService().update(dis);
				String infoSql = "select info.* from DDM_DIS_ELECTASK elec,DDM_DIS_INFO info,DDM_DIS_TASKDOMAINLINK link"
						+ " where link.fromObjectClassId || ':' || link.fromObjectId = elec.classId || ':' || elec.innerId"
						+ " and link.toObjectClassId || ':' ||��link.toObjectId = info.classId || ':' || info.innerId"
						+ " and elec.classId || ':' || elec.innerId = ?" + " and info.disMediaType = '2'";
				List<DistributeInfo> infoList = Helper.getPersistService()
						.query(infoSql, DistributeInfo.class, taskOid);

				if (infoList.size() > 0) {
					for (DistributeInfo info : infoList) {
						//ȡ����������ַ���Ϣoid����
						outSiteInfos = outSiteInfos + info.getOid() + "','";
					}

					String orderSql = "select distinct disOrder.* from DDM_DIS_ELECTASK task, DDM_DIS_TASKDOMAINLINK link , DDM_DIS_INFO info, DDM_DIS_ORDEROBJLINK objLink, DDM_DIS_ORDER disOrder"
							+ " where link.fromObjectClassId || ':' || link.fromObjectId = task.classId || ':' || task.innerId"
							+ " and link.toObjectClassId || ':' || link.toObjectId = info.classId || ':' || info.innerId"
							+ " and info.disOrderObjLinkClassId || ':' || info.disOrderObjLinkId = objLink.classId || ':' || objLink.innerId"
							+ " and objLink.fromObjectClassId || ':' || objLink.fromObjectId = disOrder.classId || ':' || disOrder.innerId"
							+ " and task.classId || ':' || task.innerId = ?";
					List<DistributeOrder> orderList = Helper.getPersistService().query(orderSql, DistributeOrder.class,
							taskOid);
					//ȡ��������صķ��ŵ�
					DistributeOrder order = orderList.get(0);

					DistributeTaskService service = DistributeHelper.getDistributeTaskService();
					service.sendToReceiveSite(infoList, outSiteInfos, order);
				}
			}
		}
	}

	public DistributeOrder getDistributeOrderByElecTaskOid(String oid) {
		String sql = "(select ORD.* from DDM_DIS_TASKINFOLINK t,DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink,DDM_DIS_ORDER ORD"
				+ " where disInfo.innerId = t.toObjectId and disInfo.classId = t.toObjectClassId"
				+ " and objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
				+ " and ORD.innerId = objLink.fromObjectId and ORD.classId = objLink.fromObjectClassId"
				+ " and t.fromObjectClassId || ':' || t.fromObjectId = ?) union all ("
				+ "select ORD.* from DDM_DIS_TASKDOMAINLINK t,DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink,DDM_DIS_ORDER ORD"
				+ " where disInfo.innerId = t.toObjectId and disInfo.classId = t.toObjectClassId"
				+ " and objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
				+ " and ORD.innerId = objLink.fromObjectId and ORD.classId = objLink.fromObjectClassId"
				+ " and t.fromObjectClassId || ':' || t.fromObjectId = ?)";
		List<DistributeOrder> list = Helper.getPersistService().query(sql, DistributeOrder.class, oid, oid);
		DistributeOrder disOrder = list.get(0);
		return disOrder;
	}

	public List<DistributeElecTask> getAllDistributeSynTasks(String orderOid, int siteType) {
		String sql = "SELECT DISTINCT E.* FROM DDM_DIS_ORDEROBJLINK L, DDM_DIS_INFO F, DDM_DIS_TASKDOMAINLINK T, DDM_DIS_ELECTASK E, DDM_DIS_TASKSYN S"
				+ " WHERE F.DISORDEROBJLINKCLASSID || ':' || F.DISORDEROBJLINKID = L.CLASSID || ':' || L.INNERID"
				+ " AND F.CLASSID || ':' || F.INNERID = T.TOOBJECTCLASSID || ':' || T.TOOBJECTID"
				+ " AND T.FROMOBJECTCLASSID || ':' || T.FROMOBJECTID = E.CLASSID || ':' || E.INNERID"
				+ " AND E.CLASSID || ':' || E.INNERID = S.TASKCLASSID ||��':' || S.TASKID"
				+ " AND L.FROMOBJECTCLASSID || ':' || L.FROMOBJECTID = ?" + " AND S.SITETYPE =?";
		List<DistributeElecTask> list = Helper.getPersistService().query(sql, DistributeElecTask.class, orderOid,
				siteType);
		return list;
	}

	public Boolean getDistributeElecTaskIsEntity(DistributeElecTask elecTask) {
		Boolean flag = true;
		String oid = elecTask.getOid();
		String sql = "SELECT DISTINCT OBJ.* FROM DDM_DIS_ELECTASK ELEC, DDM_DIS_TASKINFOLINK LINK, DDM_DIS_INFO INFO, DDM_DIS_ORDEROBJLINK OBJLINK, DDM_DIS_OBJECT OBJ"
				+ " WHERE LINK.TOOBJECTCLASSID || ':' || LINK.TOOBJECTID = INFO.CLASSID || ':' || INFO.INNERID"
				+ " AND LINK.FROMOBJECTCLASSID || ':' || LINK.FROMOBJECTID = ELEC.CLASSID || ':' || ELEC.INNERID"
				+ " AND INFO.DISORDEROBJLINKCLASSID || ':' || INFO.DISORDEROBJLINKID = OBJLINK.CLASSID || ':' || OBJLINK.INNERID"
				+ " AND OBJLINK.TOOBJECTCLASSID || ':' || OBJLINK.TOOBJECTID = OBJ.CLASSID || ':' || OBJ.INNERID"
				+ " AND ELEC.CLASSID || ':' || ELEC.INNERID = ?";
		List<DistributeObject> objList = Helper.getPersistService().query(sql, DistributeObject.class, oid);
		//��ȡ������طַ����ݣ�ȡ����һ���ַ����ݣ��ж��Ƿ������ҵ��������ӷַ�����ʵ��ַ���
		DistributeObject disObject = objList.get(0);
		Persistable obj = Helper.getPersistService().getObject(disObject.getDataClassId(), disObject.getDataInnerId());
		if (obj == null) {
			flag = false;
		}
		return flag;
	}
}
