package com.bjsasc.ddm.distribute.service.distributereceive;

//import java.io.File;
import java.io.UnsupportedEncodingException;
//import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jfree.util.Log;





//import com.bjsasc.avidm.core.extinter.exp.ExportFileHelper;
//import com.bjsasc.avidm.core.extinter.imp.ImportFileHelper;
import com.bjsasc.avidm.core.site.Site;
import com.bjsasc.avidm.core.site.SiteHelper;
import com.bjsasc.avidm.core.site.constant.DTSiteConstant;
//import com.bjsasc.avidm.core.site.constant.DTSiteConstant;
import com.bjsasc.avidm.core.transfer.TransferObjectHelper;
import com.bjsasc.avidm.core.transfer.TransferObjectService;
import com.bjsasc.avidm.core.transfer.event.TransferEvent;
import com.bjsasc.avidm.core.transfer.util.TransferConstant;
import com.bjsasc.ddm.common.A3A5DataConvertUtil;
import com.bjsasc.ddm.common.ConstLifeCycle;
import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeelectask.DistributeElecTask;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.distributetask.DistributeTask;
import com.bjsasc.ddm.distribute.model.distributetaskinfolink.DistributeTaskInfoLink;
import com.bjsasc.ddm.distribute.service.distributeelectask.DistributeElecTaskService;
import com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService;
//import com.bjsasc.ddm.distribute.service.distributeoptvalidation.DistributeOptValidationService;
import com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService;
import com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService;
//import com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService;
//import com.bjsasc.ddm.distribute.service.distributeprincipal.DistributePrincipalService;
//import com.bjsasc.ddm.distribute.service.distributetask.DistributeTaskService;
import com.bjsasc.ddm.distribute.service.distributetaskinfolink.DistributeTaskInfoLinkService;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.platform.objectmodel.business.version.IterationInfo;
//import com.bjsasc.ddm.transfer.helper.DdmDataTransferHelper;
//import com.bjsasc.ddm.transfer.service.DdmDataTransferService;
//import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.collaboration.a4x.model.DataMap;
import com.bjsasc.plm.collaboration.a4x.util.Message4XTypeConstant;
import com.bjsasc.plm.collaboration.adapter.base.MessageDealService;
//import com.bjsasc.plm.collaboration.config.model.DmcRole;
//import com.bjsasc.plm.collaboration.config.model.DmcRoleUser;
import com.bjsasc.plm.collaboration.config.service.DataConvertConfigHelper;
import com.bjsasc.plm.collaboration.config.service.DataConvertConfigService;
import com.bjsasc.plm.collaboration.config.service.DmcBusTempHelper;
import com.bjsasc.plm.collaboration.config.service.DmcConfigHelper;
import com.bjsasc.plm.collaboration.config.service.DmcConfigService;
import com.bjsasc.plm.collaboration.config.service.DmcOrderContextHelper;
//import com.bjsasc.plm.collaboration.config.service.DmcRoleHelper;
import com.bjsasc.plm.collaboration.util.A4XObjTypeConstant;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.context.model.ProductContext;
//import com.bjsasc.plm.core.change.ECO;
import com.bjsasc.plm.core.doc.Document;
//import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.part.Part;
import com.bjsasc.plm.core.persist.model.Persistable;
//import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.session.SessionHelper;
//import com.bjsasc.plm.core.system.access.AccessControlHelper;
//import com.bjsasc.plm.core.system.access.operate.Operate;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.system.principal.UserHelper;
import com.bjsasc.plm.core.util.PlmException;
import com.bjsasc.plm.core.vc.model.Versioned;
import com.cascc.avidm.login.model.PersonModel;
//import com.cascc.avidm.util.SplitString;
//import com.bjsasc.plm.core.util.PlmException;
import com.cascc.platform.uuidservice.UUID;

@SuppressWarnings({ "rawtypes" })
public class A3A5StartDCDispatchMessageDealServiceImpl implements MessageDealService {

	/** DistributeOrderService */
	private final DistributeOrderService distributeOrderService = DistributeHelper.getDistributeOrderService();
	// 分发对象服务
	private final DistributeObjectService disObjService = DistributeHelper.getDistributeObjectService();
	//分发信息服务
	private final DistributeInfoService infoService = DistributeHelper.getDistributeInfoService();
	//生命周期服务
	private final DistributeLifecycleService lifeService = DistributeHelper.getDistributeLifecycleService();
	//跨站点业务类型转换服务
	private static DataConvertConfigService dataConvertCfgService = DataConvertConfigHelper.getService();
	
	public void dealEvent(TransferEvent event) {
		synchronized (A3A5StartDCDispatchMessageDealServiceImpl.class){
			try {
				Site selfSite = SiteHelper.getSiteService().findLocalSiteInfo();
				String selfSiteInnerId = selfSite.getInnerId();
				String targetSiteInnerId = event.getTargetSite().getInnerId();
				//如果是目标站点
				if (selfSiteInnerId.equals(targetSiteInnerId)) {

					Map dataMap = A3A5DataConvertUtil.mapToDatamap(event.getReqParamMap());
					
					DataMap dmcOrder = (DataMap) dataMap.get("dmcOrder");
					//
					DataMap dmcOrderTarget = (DataMap) dataMap.get("dmcOrderTarget");
					List<DataMap> dmcObjectList = (List<DataMap>)dataMap.get("dmcObjectList"); //任务集合
					List<DataMap> dmcDispatchList = (List<DataMap>)dataMap.get("dmcDispatchList"); //签署信息集合
					//暂时没有用到这个参数
					String dmcDispatchFile = (String)dataMap.get("dmcDispatchFile"); //签署信息导出文件
					//TODO生成，发放单，发放数据，发放信息，电子任务以及相关Link
					//###---电子分发不需要导入文件---###
//					boolean isSuccess = false;
//					//导入业务对象到A5系统
//					isSuccess = importFileData(dmcOrder, dmcObjectList, dmcDispatchFile);
//					if(!isSuccess){
//						throw new PlmException("导入业务对象到A5系统失败！文件名【" + dmcDispatchFile + "】，单据名称【" + dmcOrder.get("orderName") + "】");
//					}

					String sourceSiteIID = (dmcOrder.get(A3A5DataConvertUtil.DOMAINIID));
					Site sourceSite = SiteHelper.getSiteService().findSiteById(sourceSiteIID);
					String sourceSiteVersion = sourceSite.getSystemVersion();
					String sourceProductIID = dmcOrder.get(A3A5DataConvertUtil.PRODUCTIID);
					String busType = dmcOrder.get("typeNo");
//					String tempFolderOid = DmcBusTempHelper.getService().getTempFolderIID(sourceSiteIID, sourceProductIID, busType);
					String tempProductOid = DmcBusTempHelper.getService().getTempProductIID(sourceSiteIID, sourceProductIID, busType);
					String siteName = sourceSite.getSiteData().getSiteName();
					if(tempProductOid == null){
						throw new PlmException("请配置 【"+ siteName +"】的发放缓存区！！");
					}
					String tempProductClassId = Helper.getClassId(tempProductOid);
					String tempProductInnerId = Helper.getInnerId(tempProductOid);
//
					ProductContext tmpContext = (ProductContext)Helper.getPersistService().getObject(tempProductClassId, tempProductInnerId);
//					//取得单据上下文缓存区配置（电子分发需要配置）由于不同型号对应不同产品的话叶红军说的这种处理方式处理不了，所以还修改回以前通过
//					ProductContext tmpContext = DmcOrderContextHelper.getService().findDmcOrderContextBySiteInnerId(sourceSiteIID).getProduct();
//					if(tmpContext == null){
//						String siteName = sourceSite.getSiteData().getSiteName();
//						throw new PlmException("请配置 【"+ siteName +"】的单据上下文缓存区配置！！");
//					}
					DistributeOrder disOrder = createOutSiteDistributeOrder(dmcOrder);
// 
//					List<Map<String,Object>> disDataObjMapList =  new ArrayList<Map<String,Object>>();
//					disDataObjMapList = getDisDataObjMapList(sourceSiteVersion, dmcObjectList);
					// 创建相关对象发放单与分发数据Link
					List<Map<String, String>> linkList = creteOutSiteOrderObjectLink(sourceSiteVersion, disOrder, 
							dmcObjectList, tmpContext);

					// 创建分发信息
					List<DistributeInfo> disInfoList = createOutSiteDistributeInfo(disOrder, dmcDispatchList, linkList);
//					
					String infoOids = "";
					for(DistributeInfo disInfo : disInfoList){
						infoOids = infoOids + disInfo.getClassId() + ":" + disInfo.getInnerId() + ";";
					}
					//创建分发信息对应的电子任务
					List<DistributeElecTask> disElecTaskList = createDistributeTask(
							disOrder, disInfoList, sourceSite, null);
					//在接收到消息[中心电子分发任务发起]的时候，接收方应该只会生成一个任务
					if(disElecTaskList != null && disElecTaskList.size() == 1){
						// 更新分发数据的accessURL
						String accessURL = "";
						//AccessUrl处理,由于是电子分发所以在A5中访问分发对象需要创建一个链接到A3系统的相应对象的Url
						String taskIID =  disElecTaskList.get(0).getInnerId();
						String passportURL = dmcOrderTarget.get("passportURL");
						String orderURL = dmcOrder.get("orderURL");
						String targetDomainIID = dmcOrderTarget.get("domainIID");
						accessURL = getAccessURL(orderURL, passportURL, taskIID,
								targetDomainIID, sourceSiteVersion);
						//更新分发数据的AccessUrl字段内容
						for (Map<String, String> linkMap : linkList) {
							String linkOid = linkMap.get("LINKOID");
							DistributeOrderObjectLink disOrderObjLink = (DistributeOrderObjectLink)Helper.getPersistService().getObject(linkOid);
							DistributeObject disObj =  (DistributeObject)disOrderObjLink.getTo();
							disObj.setAccessUrl(accessURL);
							disObjService.updateDistributeObject(disObj);
						}
					}else{
						Log.debug("由于在接收到消息[中心电子分发任务发起]的时候，接收方生成了多个任务，需要查看接收到的dispatch的targetDomainIID！！！！");
					}
					
					
					//存储A3单据和生成的A5发放单的id转换关系到数据库(转发任务，签署任务的时候会用到A3的单据id查找对应的A5发放单id)
					DataConvertConfigHelper.getService().saveConfig(sourceSiteVersion, 
							dmcOrder.get(A3A5DataConvertUtil.ORDERIID), disOrder.getOid(), A4XObjTypeConstant.OBJTYPE_DISTRIBUTEORDER);
					
					// TODO 接收方反馈消息给发起方 反馈电子分发消息
					//
					String isModifyOrderFlowState = "true"; //当接收方第一次接收到分发请求消息时 要发一条反馈消息给原发起方单据需要改变状态
					Map<String, String> reqParamMap = new HashMap<String, String>();
					reqParamMap.put("orderIID", dmcOrder.get(A3A5DataConvertUtil.ORDERIID));
					reqParamMap.put("isModifyOrderFlowState", isModifyOrderFlowState);
					reqParamMap.put("orderOid", disOrder.getOid());

					TransferObjectService transferService = TransferObjectHelper.getTransferService();

					// 系统运行时配置服务
					DmcConfigService dmcConfigService = DmcConfigHelper.getService();
					// 获取发放是否使用实体模式
					String isEntityDistributeModel = dmcConfigService.getDmcConfigValue(ConstUtil.IS_ENTITY_DISTRIBUTEMODE_YES);
					// 分发消息类型
					String sendMessageType = "";
					if ("true".equalsIgnoreCase(isEntityDistributeModel)) {
						sendMessageType = Message4XTypeConstant.DISTRIBUTE_DISTRIBUTEREPLY_A5;
					} else {
						sendMessageType = Message4XTypeConstant.DISTRIBUTE_REPLYDISPATCH_A5;
					}
					// 发送（按站点发送）
					transferService.sendRequest(UUID.getUID(), disOrder.getNumber(),
							sendMessageType, null, sourceSite, reqParamMap,
							null, TransferConstant.REQTYPE_ASYN);
					
//					// 抛出事件 跨版本的发放单在系统中不同于通常业务的发放单，所以不用抛出事件
//					AbstractEvent createDistributeOrderSucessEvent = EventHelper.getService().getEvent("LifeCycle", "createDistributeOrderSucessEvent"); 
//					//定义一个事件
//					CreateDistributeOrderSucessEvent cdose = (CreateDistributeOrderSucessEvent) createDistributeOrderSucessEvent;
//					cdose.setDisOrder(disOrder);
//					//发布事件
//					EventHelper.getService().publishEvent("LifeCycle", cdose);
 
				} else {
					//由于在A3A5的跨版本分发的业务中A5不可能作为中心，所以这个逻辑分支不可能到达
				}
			} catch (Exception e) {
				throw new RuntimeException("A3A5StartDCDispatchMessageDealServiceImpl类中，消息反馈处理失败！", e);
			}
		}
	}

//	private boolean importFileData(DataMap dmcOrder, List dmcObjectList, String dmcDispatchFile) {
//		boolean isSuccess = false;
//		String sourceSiteIID = (dmcOrder.get(A3A5DataConvertUtil.DOMAINIID));
//		String sourceProductIID = dmcOrder.get(A3A5DataConvertUtil.PRODUCTIID);
//		String busType = dmcOrder.get("typeNo");
//		String tempFolderOid = DmcBusTempHelper.getService().getTempFolderIID(sourceSiteIID, sourceProductIID, busType);
//		String tempProductOid = DmcBusTempHelper.getService().getTempProductIID(sourceSiteIID, sourceProductIID, busType);
//		if(tempFolderOid == null){
//			Site sourceSite = SiteHelper.getSiteService().findSiteById(sourceSiteIID);
//			String siteName = sourceSite.getSiteData().getSiteName();
//			throw new PlmException("请配置 【"+ siteName +"】的跨域站点！！");
//		}
//		Map<String, Object> valueMappingInfo = new HashMap<String, Object>();
//		String folderClassId = Helper.getClassId(tempFolderOid);
//		String folderInnerId = Helper.getInnerId(tempFolderOid);
//		Folder tmpFolder = (Folder)Helper.getPersistService().getObject(folderClassId, folderInnerId);
//		
//		String tempProductClassId = Helper.getClassId(tempProductOid);
//		String tempProductInnerId = Helper.getInnerId(tempProductOid);
//		ProductContext tmpContext = (ProductContext)Helper.getPersistService().getObject(tempProductClassId, tempProductInnerId);
//		valueMappingInfo.put("Folder", tmpFolder);
//		valueMappingInfo.put("Context", tmpContext);
//		
//		String objType = getObjType(dmcObjectList);
//
//		//导入业务对象到A5系统
//		isSuccess = ImportFileHelper.getImportFileService().importFile(sourceSiteIID, dmcDispatchFile, 
//				DTSiteConstant.DTSITE_APPVERSION_3_5, objType, valueMappingInfo);
//
//		return isSuccess;
//	}
	
	/**
	 * @param dmcOrder
	 * @return DistributeOrder
	 */
	private DistributeOrder createOutSiteDistributeOrder(DataMap dmcOrder) {
		// 发放单对象
		DistributeOrder disOrder = distributeOrderService.newDistributeOrder();
		// 根据A3的单据信息创建发放单
		disOrder.setNumber(dmcOrder.get("orderID"));// 编号
		disOrder.setName(dmcOrder.get("orderName"));// 名称
		String orderType = ConstUtil.C_ORDERTYPE_0;// 单据类型 0:发放单
		disOrder.setOrderType(orderType);
		disOrder.setNote(dmcOrder.get("orderDescription"));// 备注
		disOrder.setSiteId(dmcOrder.get("domainIID"));
		disOrder.setSiteName(dmcOrder.get("domainName"));
		String creatorIID = dmcOrder.get(A3A5DataConvertUtil.CREATORIID);
		if (creatorIID != null) {
			User user = UserHelper.getService().getUser(creatorIID);
			ManageInfo manageInfo = new ManageInfo();
			long currentTime = System.currentTimeMillis();
			manageInfo.setCreateBy(user);
			manageInfo.setModifyBy(user);
			manageInfo.setCreateTime(currentTime);
			manageInfo.setModifyTime(currentTime);
			disOrder.setManageInfo(manageInfo);
		}
		// 域信息和上下文信息在方法creteOrderObjectLink中设置
//		disOrder.setDomainInfo(domainInfo);
//		disOrder.setContextInfo(contextInfo);
		//由于此时间点无法设置上下文信息,所以无法持久化
//		Helper.getPersistService().save(disOrder);
		return disOrder;
	}
	
	/**
	 * @param disOrd
	 * @param disDataObjMapList
	 * @param autoCreateFlag
	 * @return List<Map<String,String>>
	 */
	private List<Map<String,String>> creteOutSiteOrderObjectLink(String sourceSiteVersion, DistributeOrder disOrd, 
			List<DataMap> dmcObjectList, ProductContext productContext) {

		// 分发对象List长度
		int dmcObjectListSize = dmcObjectList.size();

		List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
		// 发放单主对象
		boolean masterFlag = disObjService.getMasterLink(disOrd.getOid());

		// 发放单对象
		String sql = "SELECT disOrd.* FROM DDM_DIS_ORDER disOrd "
				+ " WHERE disOrd.CLASSID || ':' || disOrd.INNERID = ? ";
		List<DistributeOrder> orderList = Helper.getPersistService().query(sql,
						DistributeOrder.class, disOrd.getOid());
				
		for (int i = 0; i < dmcObjectListSize; i++) {
			DataMap dmcObject = dmcObjectList.get(i);
			String objectIID = (String)dmcObject.get("objectIID");
			// 根据当前分发数据源对象取得分发数据对象List
			List<DistributeObject> disObjList = disObjService.getDistributeObjectsByDataIid(objectIID);

			Map<String,String> map = new HashMap<String,String>();
			map.put("objectIID",objectIID);
			// 当前分发数据源未发放Flag
			boolean objFlag = disObjList == null || disObjList.isEmpty();
			//分发数据对象
			DistributeObject disObject;
			// 当前分发数据源未发放
			if (objFlag) {
				// 创建分发数据对象
				disObject = createDistributeObject(sourceSiteVersion,dmcObject, productContext);

				map.put("FLAG", ConstUtil.C_S_ZERO);
				// 当前分发数据源已发放
			} else {
				// 取得分发数据对象
				disObject = disObjList.get(0);
				map.put("FLAG", ConstUtil.C_S_ONE);
			}

			// 发放单与分发数据link OID
			String linkOid;
			// 当前分发数据源是分发数据源(主对象)
			if (!masterFlag) {
//				//	当更改内对象只有一个时，主对象设置成其内对象，而不再是更改单本身。
//在跨域分发中没有这个特殊的需求
//				if (dmcObjectListSize == 2 && obj instanceof ECO){
//					// 发放单与分发数据link表创建与保存
//					linkOid = disObjService.createDistributeOrderObjectLink(disOrd, disObject, ConstUtil.C_S_ZERO);
//					map.put("ISMASTER",ConstUtil.C_S_ZERO);
//				}else{
					masterFlag = true;
					// 设置发放单上下文信息
					disOrd.setContextInfo(disObject.getContextInfo());
					// 设置发放单域信息
					disOrd.setDomainInfo(disObject.getDomainInfo());
					lifeService.initLifecycle(disOrd);
//					//检查权限
//					DistributeOptValidationService validationService = DistributeHelper.getDistributeOptValidationService();
//					boolean flag = validationService.getCreateDistributeOrderValidation(Operate.CREATE, disOrd);
//					if(flag == false){
//						AccessControlHelper.getService().checkEntityPermission(Operate.CREATE, disOrd);
//					}

					if (orderList==null || orderList.isEmpty()){
						Helper.getPersistService().save(disOrd);
					} else {
						Helper.getPersistService().update(disOrd);
					}
					//更新发放单的状态为已分发
					lifeService.updateLifeCycleByStateId(disOrd,ConstLifeCycle.LC_DISTRIBUTED.getId());
//作为接收方不需要这个处理
//					if (autoCreateFlag) {
//						lifeService.promoteLifeCycle(disOrd);
//						Helper.getPersistService().update(disOrd);
//					}
					// 发放单与分发数据(主对象)link表创建与保存
					linkOid = createDistributeOrderObjectLink(disOrd, disObject, ConstUtil.C_S_ONE);

					map.put("ISMASTER",ConstUtil.C_S_ONE);	
//				}
				// 当前分发数据源不是分发数据源(主对象)
			} else {
				// 发放单与分发数据link表创建与保存
				linkOid = createDistributeOrderObjectLink(disOrd, disObject, ConstUtil.C_S_ZERO);
				map.put("ISMASTER",ConstUtil.C_S_ZERO);
			}
			map.put("LINKOID",linkOid);

			listMap.add(map);
		}
		return listMap;
	}

	/**
	 * @param disOrder
	 * @param dmcDispatchList
	 * @param linkList
	 * @return List<DistributeInfo>
	 */
	private List<DistributeInfo> createOutSiteDistributeInfo(DistributeOrder disOrder, List<DataMap> dmcDispatchList,List<Map<String, String>> linkList) {
		List<DistributeInfo> disInfoList = new ArrayList<DistributeInfo>();
		Map<String, String> objectIID2LinkOidMap = new HashMap<String, String>();
		for (Map<String, String> linkMap : linkList) {
			String linkOid = linkMap.get("LINKOID");
			String objectIID = linkMap.get("objectIID");
//			//有三个没有用到的内容
//			String dataOid = linkMap.get("DATAOID");
//			String isMaster = linkMap.get("ISMASTER");
//			//分发数据源是否已发放flag
//			String flag = linkMap.get("FLAG");
			objectIID2LinkOidMap.put(objectIID,linkOid);
		}

		for (DataMap dmcDispatchMap : dmcDispatchList) {
			DistributeInfo disInfo = infoService.newDistributeInfo();
			// 分发信息类型（0为单位，1为人员，2为站点）
			disInfo.setDisInfoType(ConstUtil.C_DISINFOTYPE_SITE);
			//单位类型(0为内部单位,1为外部单位)
			disInfo.setSendType(ConstUtil.C_SENDTYPE_1);

			Site site = SiteHelper.getSiteService().findSiteById(dmcDispatchMap.get("targetDomainIID"));
			// 分发信息名称（单位/人员）
			disInfo.setDisInfoName(dmcDispatchMap.get("targetDomainName"));
			// 分发信息IID（人员或组织的内部标识）
			disInfo.setDisInfoId(dmcDispatchMap.get("targetDomainIID"));
			// 分发信息的类标识（人员或者组织的类标识）
			disInfo.setInfoClassId(site.getClassId());
			// 分发信息变为已分发状态的时间（A5接收到的时点，发起方状态肯定是已分发）
			disInfo.setSendTime(System.currentTimeMillis());
			// 分发份数
			disInfo.setDisInfoNum(Long.valueOf(dmcDispatchMap.get("dispatchNum")));
			// 分发介质类型（0为纸质，1为电子，2跨域）
			disInfo.setDisMediaType(ConstUtil.C_DISMEDIATYPE_2);
			// 分发方式（0为直接分发，1为补发，2为移除，3为转发） 这个设置在0，1以外的场合可能存在问题（待确认）
			disInfo.setDisType(disOrder.getOrderType());
			// 备注(A3中没有备注)
			disInfo.setNote("");
			String linkOid = objectIID2LinkOidMap.get(dmcDispatchMap.get("objectIID"));
			// 发放单与分发数据LINK内部标识
			disInfo.setDisOrderObjLinkId(Helper.getPersistService().getInnerId(linkOid));
			// 发放单与分发数据LINK类标识
			disInfo.setDisOrderObjLinkClassId(Helper.getPersistService().getClassId(linkOid));
			infoService.createDistributeInfo(disInfo);
			//由于是A5接收的分发信息，在A5接收到的时候状态就应该是已分发
			lifeService.updateLifeCycleByStateId(disInfo, ConstLifeCycle.LC_DISTRIBUTED.getId());
			disInfoList.add(disInfo);
		}
		return disInfoList;
	}

	private List<DistributeElecTask> createDistributeTask(DistributeOrder disOrderObj, List<DistributeInfo> disInfoList,
			Site site, Site centerSite) {
		//返回的组装的数据
		List<DistributeElecTask> disTaskList = new ArrayList<DistributeElecTask>();
		
		Map<String, List<DistributeInfo>> taskMap = new HashMap<String, List<DistributeInfo>>();
		//把分发信息按照分发目标域分组
		//虽然是按照分发目标域分组，但由于中心发到接收方时已经做过了过滤处理，接收方只会收到本域的内容，所以应该只会生成一个任务
		for (DistributeInfo disInfo : disInfoList) {
			String targetDomainIID = disInfo.getDisInfoId();
			List<DistributeInfo> domainDisInfoList = taskMap.get(targetDomainIID);
			if (domainDisInfoList == null) {
				domainDisInfoList = new ArrayList<DistributeInfo>();
				taskMap.put(targetDomainIID, domainDisInfoList);
			}
			domainDisInfoList.add(disInfo);
		}

		DistributeElecTaskService disElecTaskService = DistributeHelper.getDistributeElecTaskService();

		//生成外域分发任务集合
		String disTaskOids = "";
		for (List<DistributeInfo> domainDisInfoList : taskMap.values()) {
			int i = 0;
			DistributeElecTask disElecTask = disElecTaskService.newDistributeElecTask();
			String infoOids = "";
			disTaskOids = disTaskOids + disElecTask.getOid() + ";";
			for (DistributeInfo info : domainDisInfoList) {
				i = i + 1;
				infoOids = infoOids + info.getOid() + ",";
				if (i == 1) {
					//生成任务(disInfoList中第一个分发信息)
					disElecTask.setNumber(disOrderObj.getNumber() + ConstUtil.C_ELECTASK_STR);
					disElecTask.setName(disOrderObj.getName() + ConstUtil.C_ELECTASK_STR);

					disElecTask.setNote("");
					disElecTask.setElecTaskType(ConstUtil.ELEC_TASKTYPE_OUT);

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
					disElecTaskService.createDistributeElecTask(disElecTask);

//					//是中心模式且站点为数据中心
//					if (centerFlag == true) {
//						// 创建外域分发信息与电子任务link对象。
//						createOutDistributeTaskInfoLink(disElecTask, info);
//					} else {
						createDistributeTaskInfoLink(disElecTask, info);
//					}
					//需要返回的任务列表，正常应该为一个任务
					disTaskList.add(disElecTask);
				} else {
//					//是中心模式且站点为数据中心
//					if (centerFlag == true) {
//						// 创建外域分发信息与电子任务link对象。
//						createOutDistributeTaskInfoLink(disElecTask, info);
//					} else {
						createDistributeTaskInfoLink(disElecTask, info);
//					}	
				}
			}
		}

		return disTaskList;

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
		linkDisObject.setTaskType(ConstUtil.C_TASKTYPE_ELEC);
		service.createDistributeTaskInfoLink(linkDisObject);
	}

	/**
	 * 取得转换后的分发数据对象。
	 * 
	 * @param dmcObject DataMap
	 * @return DistributeObject
	 */
	private DistributeObject createDistributeObject(String sourceSiteVersion, DataMap dmcObject, ProductContext productContext) {
		// 数据转换服务
		DistributeObjectService disObjService = DistributeHelper.getDistributeObjectService();
		DistributeObject disObject = disObjService.newDistributeObject();
		//TODO 需要根据接收到的objectIID查询对应的A5中的innerId
		//跟牛建义确认了A3发过来的dmcObject只有文档和部件。更改单和送审单是通过文档部件的关联找到的不出现在dmcObject里边
		if(A3A5DataConvertUtil.DMCOBJECT_TYPE_DOC.equals(dmcObject.get("type"))){
			//电子分发没有数据的转换不需要转换这个ID
//			String dataInnerId = getDisDataObjOid(sourceSiteVersion, dmcObject);
			String dataInnerId = dmcObject.get("objectIID");
			disObject.setDataInnerId(dataInnerId);
			disObject.setDataClassId(Document.class.getSimpleName());
		}else if(A3A5DataConvertUtil.DMCOBJECT_TYPE_ITEM.equals(dmcObject.get("type"))){
			//电子分发没有数据的转换不需要转换这个ID
//			String dataInnerId = getDisDataObjOid(sourceSiteVersion, dmcObject);
			String dataInnerId = dmcObject.get("objectIID");
			disObject.setDataInnerId(dataInnerId);
			disObject.setDataClassId(Part.class.getSimpleName());
		}
		disObject.setDataFrom(DTSiteConstant.DTSITE_APPVERSION_3_5);
		disObject.setNumber(dmcObject.get("objectID"));
		disObject.setName(dmcObject.get("objectName"));
		//A3中没有备注
		disObject.setNote("");
		// 此时还没有生成任务，所以无法设定，这个AccessUrl需要在生成任务的时候更新
		disObject.setAccessUrl("");
		//disObject.getIterationInfo().setVersionNo(dmcObject.get("versionNo"));

		//disObject.setVersion(dmcObject.get("versionNo"));.
		disObject.setStateName(dmcObject.get("objectState")); 

		String creatorIID = dmcObject.get(A3A5DataConvertUtil.CREATORIID);
		if (creatorIID != null) {
			User user = UserHelper.getService().getUser(creatorIID);
			ManageInfo manageInfo = new ManageInfo();
			long currentTime = System.currentTimeMillis();
			manageInfo.setCreateBy(user);
			manageInfo.setModifyBy(user);
			manageInfo.setCreateTime(currentTime);
			manageInfo.setModifyTime(currentTime);
			disObject.setManageInfo(manageInfo);
			disObject.setCreateName(dmcObject.get("creatorName"));
		}
		IterationInfo iterationInfo = new IterationInfo();
		iterationInfo.setVersionNo(dmcObject.get("versionNo"));
		iterationInfo.setIterationNo("X");
		iterationInfo.setVersionLevel(1);
		iterationInfo.setVersionSortNo("0001");
		iterationInfo.setLatestInBranch("1");
		iterationInfo.setCheckoutState("checkin");
		disObject.setIterationInfo(iterationInfo);
		// 域信息和上下文信息在方法creteOrderObjectLink中设置
		disObject.setDomainInfo(productContext.getDomainInfo());
		disObject.setContextInfo(productContext.buildContextInfo());
		
		// 分发数据对象保存
		disObjService.createDistributeObject(disObject);
		
		return disObject;
	}
	
	private String createDistributeOrderObjectLink(DistributeOrder disOrder, DistributeObject disObject, String isMaster) {
		DistributeOrderObjectLinkService service = DistributeHelper.getDistributeOrderObjectLinkService();
		DistributeOrderObjectLink linkDisObject = service.newDistributeOrderObjectLink();

		linkDisObject.setFromObject(disOrder);
		linkDisObject.setToObject(disObject);

		String dataInnerId = disObject.getDataInnerId();
		String dataClassId = disObject.getDataClassId();
		String dataOid = Helper.getOid(dataClassId, dataInnerId);
		Persistable dataObj = Helper.getPersistService().getObject(dataOid);
		// 是否在这个发放单内是父节点（0：不是，1：是）
		// 分发数据对象的数据源对象不是Part，都不是父节点，所以默认设置0
		if (dataObj instanceof Part) {
			linkDisObject.setIsParent("1");
		} else {
			linkDisObject.setIsParent("0");
		}

		linkDisObject.setDisDeadLine(System.currentTimeMillis() + 3 * 24 * 3600 * 1000);
		linkDisObject.setDisUrgent("0");
		linkDisObject.setDisStyle("0");

		// 不是从发放单开始建立分发数据对象的，都不是主对象，所以默认设置0
		// 是否在这个发放单内是主对象（0：不是，1：是
		linkDisObject.setIsMaster(isMaster);
		// 生命周期服务
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		// 生命周期测试
		life.initLifecycle(linkDisObject);
		
		Helper.getPersistService().save(linkDisObject);
		
		//更新分发数据生命周期状态为已完成(由于是外域发过来的数据状态在A5中接收的时候就应该是已完成)
		life.updateLifeCycleByStateId(linkDisObject,ConstLifeCycle.LC_COMPLETED.getId());

		return linkDisObject.getClassId() + ":" + linkDisObject.getInnerId();
	}
	/**
	 * @param orderURL
	 * @param passPortURL
	 * @param taskIID
	 * @param targetDomainIID
	 * @param systemVersion
	 * @return String
	 */
	private String getAccessURL(String orderURL, String passPortURL, String taskIID,
			String targetDomainIID, String systemVersion){
		String accessURL = "";
		String strAction = "";
//		try {
			String dataURL = orderURL;
//			PersonModel person = SessionHelper.getService().getPersonModel();
			dataURL += "&outusername={outusername}";
			dataURL += "&outuserrealname={outuserrealname}";
			dataURL +="&targetDomainIID="+targetDomainIID;
			dataURL +="&taskIID="+taskIID;
			int iLoc = -1;
			for (int i = 0; i < 3; i++) {
				iLoc = dataURL.indexOf("/", (iLoc + 1));
			}
			if (-1 == iLoc) {
				//报错
				throw new RuntimeException("The dataURL is Error！ dataURL = " + dataURL);
			} else {
				strAction = dataURL.substring(0, iLoc);
			}
			dataURL = dataURL.substring(iLoc);
			String contextStr = dataURL.substring(0,dataURL.indexOf("/",1));
			strAction += contextStr+"/userloginservlet";
			//String realName = URLEncoder.encode(person.getUserName(),"UTF-8");
			strAction += "?IsPassportLogin=true";
			strAction += "&realName={realName}";
			strAction += "&TargetURL="+dataURL;
			strAction += "&userName="+passPortURL;
//		} catch (UnsupportedEncodingException e) {
//			// catch block
//			Log.debug("UnsupportedEncodingException" + e);
//		}
		accessURL = strAction;
		return accessURL;
	}
	
	/**
	 * @param sourceSiteVersion
	 * @param dmcObject
	 * @return String
	 */
	private String getDisDataObjOid(String sourceSiteVersion, DataMap dmcObject){
		String type = dmcObject.get("type");
		String oldObjIId = dmcObject.get("objectIID");
		String oldObjVerIID = dmcObject.get("objectVerIID");

   		//根据系统版本，对象IID，对象verIID，对象类型查询转换后的对象OID
		String newObjIID = null;
		if(type.equals(A3A5DataConvertUtil.DMCOBJECT_TYPE_DOC)){
			newObjIID = DataConvertConfigHelper.getService().getA5ConvertObjIID(sourceSiteVersion, oldObjIId, oldObjVerIID, A4XObjTypeConstant.OBJTYPE_DMDOCUMENT);
		}else if(type.equals(A3A5DataConvertUtil.DMCOBJECT_TYPE_ITEM)){
			newObjIID = DataConvertConfigHelper.getService().getA5ConvertObjIID(sourceSiteVersion, oldObjVerIID, A4XObjTypeConstant.OBJTYPE_ITEM);
			if(newObjIID == null){
				newObjIID = DataConvertConfigHelper.getService().getA5ConvertObjIID(sourceSiteVersion, oldObjIId, A4XObjTypeConstant.OBJTYPE_ITEM);
			}
		}else{
			newObjIID = DataConvertConfigHelper.getService().getA5ConvertObjIID(sourceSiteVersion, oldObjIId, A4XObjTypeConstant.OBJTYPE_APPROVEORDER);
			if(newObjIID == null){
				newObjIID = DataConvertConfigHelper.getService().getA5ConvertObjIID(sourceSiteVersion, oldObjIId, A4XObjTypeConstant.OBJTYPE_ECO);
			}
		}
		return newObjIID;
	}
}
