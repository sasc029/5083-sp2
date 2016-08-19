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
 * 电子任务接口实现类。
 * 
 * @author gengancong 2013-3-18
 */
@SuppressWarnings({ "deprecation", "unchecked", "static-access" })
public class DistributeElecTaskServiceImpl implements DistributeElecTaskService {

	/*
	 * （非 Javadoc）
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
		//判断当前用户是否是跨域发放协管员
		boolean isCrossDomainUser = this.isCrossDomainUser();
		boolean isSetCrossDomainUser = this.isSetCrossDomainUser();
		//如果当前用户是跨域发放协管员,取得其他跨域发放协管员完成的任务
		if(isSetCrossDomainUser && isCrossDomainUser){
			long from = 0;
			long to = 0;
			String name = stateName.toString();
			outTasklist = this.getDistributeElecTaskForCrossDomain("'"+name+"'", from, to);
		}else if(!isSetCrossDomainUser && currentUser.isAdministrator()){
			outTasklist = this.getDistributeElecTaskForAdmin(stateName.toString());
		}else{//如果当前登录用户是普通用户，查询是否有指定用户的跨域分发任务
			outTasklist = this.getDistributeElecTaskForAAUSER(stateName.toString());
		}
		
		taskList.addAll(outTasklist);
		TimeComparator timeComp = new TimeComparator();
		Collections.sort(outTasklist, timeComp);
		return taskList;
	}

	/**
	 * TimeComparator比较器。
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
		//判断当前用户是否是二级调度员
		boolean isSecondScheUser = this.isSecondScheUser();
		//如果当前用户是二级调度员,取得当前组织接收的当前用户负责的产品相关的已完成的任务
		if(isSecondScheUser){
			//取得当前用户的组织ID，ClassId
			String aaOrgID = SessionHelper.getService().getUser().getOrganization().getInnerId();
			String aaOrgClassID = SessionHelper.getService().getUser().getOrganization().getClassId();

			//取得所有产品上下文
			List<Context> allProductsContext = Helper.getAppContextService().getAllProducts();
			
			//取得当前用户
			User currentUser = SessionHelper.getService().getUser();
			
			//获取当前用户参与的产品上下文列表
			List<Context> currentUserProductsContext = Helper.getContextService().filterContextsByUserPrincipal(allProductsContext,currentUser);

			//取得当前用户所负责的产品ID
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
		//判断当前用户是否是跨域发放协管员
		boolean isCrossDomainUser = this.isCrossDomainUser();
		List<DistributeElecTask> disElecTasklistCrossDomain = new ArrayList<DistributeElecTask>();
		//如果当前用户是跨域发放协管员,取得当前站点接收的电子任务
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
			//取得跨域电子任务
			Site selfSite = SiteHelper.getSiteService().findLocalSiteInfo();
			DmcConfigService dmcConfigService = DmcConfigHelper.getService();
			boolean isDcDeployModel = dmcConfigService.getIsDealOnDC();
			boolean flag = false;
			//如果是中心模式
			if (selfSite != null && isDcDeployModel == true) {
				DCSiteAttribute dcSiteAttr = DCSiteAttributeHelper.getDCSiteAttrService().findDcSiteAttrByDTSiteId(
						selfSite.getInnerId());
				//如果当前站点是数据中心且用户为协管员
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
					//系统为数据中心 
					flag = true;
				}
			}

			//系统为非数据中心(邦联模式/中心模式且非数据中心)
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
					// 分发方式（0为直接分发，1为补发，2为移除，3为转发）
					info.setDisType("3");
					// 分发份数
					info.setDisInfoNum(1);
					// 分发信息名称（单位/人员）
					info.setDisInfoName(principal.getName());
					// 分发信息的类标识（人员或者组织的类标识）
					info.setInfoClassId(principal.getClassId());
					// 分发信息IID（人员或组织的内部标识）
					info.setDisInfoId(principal.getInnerId());
					// 分发介质类型（0为纸质，1为电子，2为跨域）
					info.setDisMediaType(ConstUtil.C_DISMEDIATYPE_1);
					// 分发信息类型（0为单位，1为人员）
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
			//取得用户
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
				// 存在该用户的电子信息
				List<DistributeElecTask> taskList = PersistHelper.getService().query(taskSql, DistributeElecTask.class,
						dis.getDisInfoId(), dis.getInfoClassId(), "1", ConstUtil.LC_NOT_SIGNED.getName(),
						distributeOrderOid);
				// 发给此用户的电子任务存在（添加link）
				if (taskList.size() > 0) {
					createDistributeTaskInfoLink(taskList.get(0), dis);
					// 分发信息添加发送时间，生命周期升级
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

					// 创建电子任务对象。
					service.createDistributeElecTask(disElecTask);

					// 更新分发信息的发送时间

					dis.setSendTime(System.currentTimeMillis());
					if (ConstUtil.LC_NOT_DISTRIBUT.getName().equals(dis.getLifeCycleInfo().getStateName())) {
						life.promoteLifeCycle(dis);
					}
					infoList1.add(dis);

					DistributeTaskInfoLink link = newDistributeTaskInfoLink();
					link.setToObject(dis);
					link.setFromObject(disElecTask);
					// 任务类型（0：纸质任务，1：电子任务）
					link.setTaskType("1");
					Helper.getPersistService().save(link);
					//为分发数据授权
					setUserPrivilege(dis, receiveUser);
					// 更新分发信息的发送时间
					// dis.setSendTime(System.currentTimeMillis());

				}
			}
			//转发分发任务记录日志
			Context context= disTask.getContextInfo().getContext();
			List<String> objects = new ArrayList<String>();
			objects.add(SessionHelper.getService().getUser().getName());//用户名
			objects.add("转发分发任务");
			objects.add(disTask.getNumber());
			objects.add(receiveUser.getName());
			int level=1;
			String logType="module";
			String objName=disTask.getName();
			int objectSecurity=0;
			String moduleSource="发放管理";
			String objectType="电子任务";
			String operation="转发分发任务";
			String messageId="ddm.log.forwardElecTask";
			AuditLogHelper.getService().addLog( level, logType, context, 
					disTask.getInnerId(), disTask.getClassId(), objName, objectSecurity, 
					moduleSource, objectType, operation, messageId, objects);

		}
		if (infoList1.size() > 0 || infoList1 != null) {
			Helper.getPersistService().update(infoList1);
		}

		// 接收方反馈消息给发起方 （电子分发转发消息，分发请求反馈消息）
		// (转发任务，签署任务的时候会用到A3的单据id，根据A5发放单id查找对应的A3的单据id)
		String orderIID = DataConvertConfigHelper.getService().getA4xSendObjIID(
				DTSiteConstant.DTSITE_APPVERSION_3_5, order.getOid(),
				A4XObjTypeConstant.OBJTYPE_DISTRIBUTEORDER);
		if(!StringUtil.isStringEmpty(orderIID)){
			String isModifyOrderFlowState = "false"; //当接收方转发任务的时候发起方不需要修改单据状态
			Map<String, String> reqParamMap = new HashMap<String, String>();
			
			reqParamMap.put("orderIID", orderIID);
			reqParamMap.put("isModifyOrderFlowState", isModifyOrderFlowState);
			reqParamMap.put("orderOid", order.getOid());
	
			TransferObjectService transferService = TransferObjectHelper
					.getTransferService();
	
			// 系统运行时配置服务
			DmcConfigService dmcConfigService = DmcConfigHelper
					.getService();
			// 获取发放是否使用实体模式
			String isEntityDistributeModel = dmcConfigService
					.getDmcConfigValue(ConstUtil.IS_ENTITY_DISTRIBUTEMODE_YES);
			// 分发消息类型
			String sendMessageType = "";
			if ("true".equalsIgnoreCase(isEntityDistributeModel)) {
				//实体分发：分发请求反馈消息
				sendMessageType = Message4XTypeConstant.DISTRIBUTE_DISTRIBUTEREPLY_A5;
			} else {
				//电子分发：电子分发转发消息
				sendMessageType = Message4XTypeConstant.DISTRIBUTE_UNPACKAGETASKDATABIND_A5;
			}
	
			Site sourceSite = SiteHelper.getSiteService().findSiteById(disTask.getSourceSiteId());
			// 发送
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
			//取得业务对象域信息
			Domained domained = (Domained) PersistHelper.getService().getObject(
					object.getDataClassId() + ":" + object.getDataInnerId());
			if(domained==null){
				//domained = (Domained)DomainHelper.getService().getDomain(object.getDomainInfo().getDomain().getAaDomainRef());
				//如果是domained为null的时候，此对象不用授权
				continue;
			}
			boolean flag = AccessControlHelper.getService().hasEntityPermission(user, Operate.ACCESS, domained);
			if (flag == false) {
				//设置访问权限
				principalService.setPriviledge(domained, user);
			}
		}
	}

	/**
	 * 创建分发信息与电子任务link对象。
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
		// 任务类型（0：纸质任务，1：电子任务）
		linkDisObject.setTaskType("1");
		service.createDistributeTaskInfoLink(linkDisObject);
	}

	@Override
	public DistributeInfo newDistributeInfo() {
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		DistributeInfo dis = (DistributeInfo) PersistUtil.createObject(DistributeInfo.CLASSID);

		// 生命周期初始化
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
	 * （非 Javadoc）
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
	 * （非 Javadoc）
	 * 
	 * @see com.bjsasc.ddm.distribute.service.distributeelectask.
	 * DistributeElecTaskService
	 * #createDistributeElecTask(com.bjsasc.ddm.distribute
	 * .model.distributeelectask.DistributeElecTask)
	 */
	@Override
	public void createDistributeElecTask(DistributeElecTask dis) {
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		// 生命周期初始化
		life.initLifecycle(dis);
		Helper.getPersistService().save(dis);
	}

	/*
	 * （非 Javadoc）
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
	 * （非 Javadoc）
	 * 
	 * @see com.bjsasc.ddm.distribute.service.distributeelectask.
	 * DistributeElecTaskService#newDistributeElecTask()
	 */
	public DistributeElecTask newDistributeElecTask() {
		DistributeElecTask dis = (DistributeElecTask) PersistUtil.createObject(DistributeElecTask.CLASSID);
		return dis;
	}

	/*
	 * （非 Javadoc）
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
					// 生命周期升级
					life.promoteLifeCycle(dis);
					//签收电子任务记录日志
					Context context= dis.getContextInfo().getContext();
					List<String> objects = new ArrayList<String>();
					objects.add(SessionHelper.getService().getUser().getName());//用户名
					objects.add("签收");
					objects.add(dis.getNumber());
					int level=1;
					String logType="module";
					String objName=dis.getName();
					int objectSecurity=0;
					String moduleSource="发放管理";
					String objectType="电子任务";
					String operation="签收";
					String messageId="ddm.log.SignFor";
					AuditLogHelper.getService().addLog( level, logType, context, 
							dis.getInnerId(), dis.getClassId(), objName, objectSecurity, 
							moduleSource, objectType, operation, messageId, objects);
				} else if (LIFECYCLE_OPT.REJECT == opt) {
					// 生命周期拒绝
					life.rejectLifeCycle(dis);
					//拒绝签收电子任务记录日志
					Context context= dis.getContextInfo().getContext();
					List<String> objects = new ArrayList<String>();
					objects.add(SessionHelper.getService().getUser().getName());//用户名
					objects.add("拒绝签收");
					objects.add(dis.getNumber());
					int level=1;
					String logType="module";
					String objName=dis.getName();
					int objectSecurity=0;
					String moduleSource="发放管理";
					String objectType="电子任务";
					String operation="拒绝签收";
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
					//拒绝签收给创建者发送邮件
					//指定接受者的邮箱
					try{
						Map ctx = new HashMap();
						//使用当前登录者的sessionid发送
						HttpServletRequest request = ServletActionContext.getRequest();
						HttpSession session = request.getSession();
						String sSessionId = (String) session.getAttribute(AvidmConstDefine.SESSIONPARA_SESSIONID);
						//指定接收者邮箱
						//通过用户内部标识获得用户对象、在获得用户邮箱
						User toUser = dis.getManageInfo().getCreateBy();
						String email=toUser.getEmail();
						DistributeOrder order = getDistributeOrderByElecTaskOid(dis.getOid());
						//当前时间
						Date date=new Date();
						DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String currentTime=format.format(date);
						String emailBody="您调度的分发单"+order.getNumber()+"于"+currentTime+"被"+currentUser.getName()+"拒收，原因是:"+returnReason+"。如果需要，请创建该发放单的补发分发单，进行补发分发.";
						AvidmMsgSender.avSendMessage(ctx, sSessionId, AvidmMsgSender.MSG_TYPE_MAIL,"调度分发单被拒收!",emailBody, email,null);
					}catch(Exception e){
						Log.debug("电子任务拒收邮件发送失败！");
					}
				}

				if ("1".equals(dis.getElecTaskType())) {
					//TODO 跨版本处理
					Site selfSite = SiteHelper.getSiteService().findLocalSiteInfo();
					//取得发起方站点
					Site sourceSite = SiteHelper.getSiteService().findSiteById(dis.getSourceSiteId());
					// 发送到A3.5
					if (DTSiteConstant.DTSITE_APPVERSION_3_5
							.equalsIgnoreCase(sourceSite.getSystemVersion())) {
						DistributeOrder order = getDistributeOrderByElecTaskOid(dis.getOid());
						//(转发任务，签署任务的时候会用到A5发放单id查找对应的A3的单据id)
						String orderIID = DataConvertConfigHelper.getService().getA4xSendObjIID(
								DTSiteConstant.DTSITE_APPVERSION_3_5, order.getOid(),
								A4XObjTypeConstant.OBJTYPE_DISTRIBUTEORDER);
						if(!StringUtil.isStringEmpty(orderIID)){
							String isModifyOrderFlowState = "false"; //当接收方签收拒绝任务的时候发起方不需要修改单据状态
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
					
							// 系统运行时配置服务
							DmcConfigService dmcConfigService = DmcConfigHelper
									.getService();
							// 获取发放是否使用实体模式
							String isEntityDistributeModel = dmcConfigService
									.getDmcConfigValue(ConstUtil.IS_ENTITY_DISTRIBUTEMODE_YES);
							// 分发消息类型
							String sendMessageType = "";
							if ("true".equalsIgnoreCase(isEntityDistributeModel)) {
								//实体分发：分发请求反馈消息
								sendMessageType = Message4XTypeConstant.DISTRIBUTE_DISTRIBUTETASKOPERSYN_A5;
							} else {
								//电子分发：电子分发转发消息
								sendMessageType = Message4XTypeConstant.DISTRIBUTE_DISPATCHSIGTASKOPERSYN_A5;
							}
					
							// 发送
							transferService.sendRequest(UUID.getUID(), dis.getNumber(),
									sendMessageType, null, sourceSite, reqParamMap,
									null, TransferConstant.REQTYPE_ASYN);
						}else{
							Log.debug("签署任务后反馈消息时用A5发放单id查找对应的A3的单据id失败！！A5发放单Oid【" + order.getOid() + "】");
						}
					}else{
						//传输参数
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
	
//						//取得发起方站点
//						Site sourceSite = SiteHelper.getSiteService().findSiteById(dis.getSourceSiteId());
						//生命周期同步给发起方
						TransferObjectService transferService = TransferObjectHelper.getTransferService();
						transferService.sendRequest(UUID.getUID(), dis.getNumber(),
								MessageTypeconstant.REPLY_SEND_DISTRIBUTE, null, sourceSite, reqParamMap, null,
								TransferConstant.REQTYPE_SYN);
						//待修改
						if (!"".equals(dis.getCenterSiteId()) && dis.getCenterSiteId() != null) {
							//取得中心站点
							Site centerSite = SiteHelper.getSiteService().findSiteById(dis.getCenterSiteId());
	
							//生命周期同步给发起方
							transferService.sendRequest(UUID.getUID(), dis.getNumber(),
									MessageTypeconstant.REPLY_SEND_DISTRIBUTE, null, centerSite, reqParamMap, null,
									TransferConstant.REQTYPE_SYN);
						}
					}
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
			//提交电子任务记录日志
			Context context= dis.getContextInfo().getContext();
			List<String> objects = new ArrayList<String>();
			objects.add(SessionHelper.getService().getUser().getName());//用户名
			objects.add("提交分发任务");
			objects.add(dis.getNumber());
			int level=1;
			String logType="module";
			String objName=dis.getName();
			int objectSecurity=0;
			String moduleSource="发放管理";
			String objectType="电子任务";
			String operation="提交分发任务";
			String messageId="ddm.log.finishSignFor";
			AuditLogHelper.getService().addLog( level, logType, context, 
					dis.getInnerId(), dis.getClassId(), objName, objectSecurity, 
					moduleSource, objectType, operation, messageId, objects);
			if (ConstUtil.ELEC_TASKTYPE_OUT.equals(dis.getElecTaskType())) {
				Site targetSite = (Site) SiteHelper.getSiteService().findSiteById(dis.getSourceSiteId());

				//传输参数
				Map<String, String> reqParamMap = new HashMap<String, String>();
				reqParamMap.put("flag", "1");
				reqParamMap.put("taskOid", oid);
				reqParamMap.put("opt", "PROMOTE");

				//生命周期同步(发起方)
				TransferObjectService transferService = TransferObjectHelper.getTransferService();
				transferService.sendRequest(UUID.getUID(), dis.getNumber(), MessageTypeconstant.REPLY_SEND_DISTRIBUTE,
						null, (Site) targetSite, reqParamMap, null, TransferConstant.REQTYPE_SYN);

				//如果为接收方，同步中心
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

		//判断当前用户是否是二级调度员
		boolean isSecondScheUser = this.isSecondScheUser();
		//判断当前用户是否是跨域协管员
		boolean isCrossDomainUser = this.isCrossDomainUser();
		
		if(isSecondScheUser || isCrossDomainUser){
			flat = true;
		}
		return flat;
	}
	
	@Override
	public boolean isSecondScheUser() {
		boolean flat = false;
		//取得当前用户
		User currentUser = SessionHelper.getService().getUser();
		//取得当前用户的组织机构
		Organization currentOrganization = currentUser.getOrganization();
		if(currentOrganization == null){
			return false;
		}
		//取得二级调度组
		Group secondScheGroup = GroupHelper.getService().getGroupById("second_sche");
		//判断当前用户是否是当前组织的二级调度员组中的用户
		flat = this.isOrgGroupUser(currentOrganization,secondScheGroup,currentUser);
		return flat;
	}
	
	/**
	 * 判断该用户是不是组织中组的用户
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
	 * 判断是否配置了跨域协管员
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
//		//String noRec = "未签收";
//		String sql = " SELECT DISTINCT A.* FROM DDM_DIS_ELECTASK A, DDM_DIS_TASKINFOLINK B,DDM_DIS_INFO C,AAUSERDATA D,"
//				+ "AAUSERPRINCIPAL S, AAGROUP P, PLM_PRINCIPAL_ORG O  WHERE A.INNERID = B.FROMOBJECTID AND A.CLASSID = B.FROMOBJECTCLASSID "
//				+ "AND C.INNERID = B.TOOBJECTID AND C.CLASSID = B.TOOBJECTCLASSID AND A.STATENAME = ? AND C.INFOCLASSID = 'Organization' "
//				+ "AND O.INNERID = C.DISINFOID AND O.AAORGINNERID = D.USERORG AND D.INNERID = ? AND S.USERREF = ? AND P.INNERID = S.PRINCIPALVALUE AND S.USERREF = D.INNERID AND P.ID='second_sche' ";
//		List<DistributeElecTask> taskList = Helper.getPersistService().query(sql, DistributeElecTask.class, stateName,
//				currentUser.getAaUserInnerId(), currentUser.getAaUserInnerId());
//
//		return taskList;
//		
		//修改bug15291 需要判断作为二级调度员的用户同型号的逻辑关系，只有在型号下的团队中才可以看到这个型号下的电子任务
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
						+ " and link.toObjectClassId || ':' ||　link.toObjectId = info.classId || ':' || info.innerId"
						+ " and elec.classId || ':' || elec.innerId = ?" + " and info.disMediaType = '2'";
				List<DistributeInfo> infoList = Helper.getPersistService()
						.query(infoSql, DistributeInfo.class, taskOid);

				if (infoList.size() > 0) {
					for (DistributeInfo info : infoList) {
						//取得所有外域分发信息oid集合
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
					//取得任务相关的发放单
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
				+ " AND E.CLASSID || ':' || E.INNERID = S.TASKCLASSID ||　':' || S.TASKID"
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
		//获取任务相关分发数据，取任意一条分发数据，判断是否有相关业务对象（链接分发还是实体分发）
		DistributeObject disObject = objList.get(0);
		Persistable obj = Helper.getPersistService().getObject(disObject.getDataClassId(), disObject.getDataInnerId());
		if (obj == null) {
			flag = false;
		}
		return flag;
	}
}
