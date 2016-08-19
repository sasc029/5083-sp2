package com.bjsasc.ddm.distribute.service.distributereceive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.avidm.core.extinter.imp.ImportFileHelper;
import com.bjsasc.avidm.core.site.Site;
import com.bjsasc.avidm.core.site.SiteHelper;
import com.bjsasc.avidm.core.site.constant.DTSiteConstant;
import com.bjsasc.avidm.core.transfer.TransferObjectHelper;
import com.bjsasc.avidm.core.transfer.TransferObjectService;
import com.bjsasc.avidm.core.transfer.event.TransferEvent;
import com.bjsasc.avidm.core.transfer.util.TransferConstant;
import com.bjsasc.ddm.common.A3A5DataConvertUtil;
import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService;
import com.bjsasc.ddm.distribute.service.distributeoptvalidation.DistributeOptValidationService;
import com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService;
import com.bjsasc.ddm.distribute.service.distributetask.DistributeTaskService;
import com.bjsasc.ddm.transfer.helper.DdmDataTransferHelper;
import com.bjsasc.ddm.transfer.service.DdmDataTransferService;
import com.bjsasc.plm.collaboration.a4x.model.DataMap;
import com.bjsasc.plm.collaboration.a4x.util.Message4XTypeConstant;
import com.bjsasc.plm.collaboration.adapter.base.MessageDealService;
import com.bjsasc.plm.collaboration.config.service.DataConvertConfigHelper;
import com.bjsasc.plm.collaboration.config.service.DmcBusTempHelper;
import com.bjsasc.plm.collaboration.config.service.DmcConfigHelper;
import com.bjsasc.plm.collaboration.config.service.DmcConfigService;
import com.bjsasc.plm.collaboration.config.service.DmcOrderContextHelper;
import com.bjsasc.plm.collaboration.util.A4XObjTypeConstant;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.change.ECO;
import com.bjsasc.plm.core.context.model.ProductContext;
import com.bjsasc.plm.core.folder.Folder;
import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.access.AccessControlHelper;
import com.bjsasc.plm.core.system.access.operate.Operate;
import com.bjsasc.plm.core.system.principal.OrganizationHelper;
import com.bjsasc.plm.core.system.principal.Principal;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.system.principal.UserHelper;
import com.bjsasc.plm.core.util.PlmException;
import com.cascc.platform.uuidservice.UUID;

@SuppressWarnings({ "rawtypes" })
public class A3A5DistributeMessageDealServiceImpl implements MessageDealService {

	/** DistributeOrderService */
	private final DistributeOrderService distributeOrderService = DistributeHelper.getDistributeOrderService();
	// 分发对象服务
	private final DistributeObjectService disObjService = DistributeHelper.getDistributeObjectService();
	//分发信息服务
	private final DistributeInfoService infoService = DistributeHelper.getDistributeInfoService();
	//生命周期服务
	private final DistributeLifecycleService lifeService = DistributeHelper.getDistributeLifecycleService();
	
	public void dealEvent(TransferEvent event) {
		synchronized (A3A5DistributeMessageDealServiceImpl.class){
			try {
				Site selfSite = SiteHelper.getSiteService().findLocalSiteInfo();
				String selfSiteInnerId = selfSite.getInnerId();
				String targetSiteInnerId = event.getTargetSite().getInnerId();
				//如果是目标站点
				if (selfSiteInnerId.equals(targetSiteInnerId)) {

					Map dataMap = A3A5DataConvertUtil.mapToDatamap(event.getReqParamMap());
					
					DataMap dmcOrder = (DataMap) dataMap.get("dmcOrder");
					//暂时没有用到这个参数
					DataMap dmcOrderTarget = (DataMap) dataMap.get("dmcOrderTarget");
					List<DataMap> dmcObjectList = (List<DataMap>)dataMap.get("dmcObjectList"); //分发数据集合
					List<DataMap> dmcDispatchList = (List<DataMap>)dataMap.get("dmcDispatchList"); //签署信息集合
					String dmcDispatchFile = (String)dataMap.get("dmcDispatchFile"); //签署信息导出文件
					//TODO生成，发放单，发放数据，发放信息，电子任务以及相关Link
					boolean isSuccess = false;
					//导入业务对象到A5系统
					isSuccess = importFileData(dmcOrder, dmcObjectList, dmcDispatchFile);
					if(!isSuccess){
						throw new PlmException("导入业务对象到A5系统失败！文件名【" + dmcDispatchFile + "】，单据名称【" + dmcOrder.get("orderName") + "】");
					}
					
					DistributeOrder disOrder = createDistributeOrder(dmcOrder);

					// 分发数据源对象(需要设置主对象)
					List<Map<String,Object>> disDataObjMapList =  new ArrayList<Map<String,Object>>();
					//根据A3的信息取得导入文件变换后的分发业务数据对象
					String sourceSiteIID = (dmcOrder.get(A3A5DataConvertUtil.DOMAINIID));
					Site sourceSite = SiteHelper.getSiteService().findSiteById(sourceSiteIID);
					String sourceSiteVersion = sourceSite.getSystemVersion();
					disDataObjMapList = getDisDataObjMapList(sourceSiteVersion, dmcObjectList);
					// 创建相关对象发放单与分发数据Link
					List<Map<String, String>> linkList = creteOrderObjectLink(disOrder, disDataObjMapList, true);

					// 创建分发信息
					List<DistributeInfo> disInfoList = createDistributeInfo(disOrder, dmcDispatchList, linkList);
					
					String infoOids = "";
					for(DistributeInfo disInfo : disInfoList){
						infoOids = infoOids + disInfo.getClassId() + ":" + disInfo.getInnerId() + ";";
					}
					
					//创建分发信息对应的电子任务
					DistributeTaskService taskService = DistributeHelper.getDistributeTaskService();
					taskService.createOutSiteDistributeTask(disOrder.getOid(), infoOids,
							sourceSite, true, false, null);
					
					//存储A3单据和生成的A5发放单的id转换关系到数据库(转发任务，签署任务的时候会用到A3的单据id查找对应的A5发放单id)
					DataConvertConfigHelper.getService().saveConfig(sourceSiteVersion, 
							dmcOrder.get(A3A5DataConvertUtil.ORDERIID), disOrder.getOid(), A4XObjTypeConstant.OBJTYPE_DISTRIBUTEORDER);
					
					// TODO 接收方反馈消息给发起方 反馈电子分发消息
					//当接收方第一次接收到分发请求消息时 要发一条反馈消息给原发起方单据需要改变状态
					String isModifyOrderFlowState = "true"; 
					Map<String, String> reqParamMap = new HashMap<String, String>();
					reqParamMap.put("orderIID", dmcOrder.get(A3A5DataConvertUtil.ORDERIID));
					reqParamMap.put("isModifyOrderFlowState", isModifyOrderFlowState);
					reqParamMap.put("orderOid", disOrder.getOid());

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
						sendMessageType = Message4XTypeConstant.DISTRIBUTE_DISTRIBUTEREPLY_A5;
					} else {
						sendMessageType = Message4XTypeConstant.DISTRIBUTE_REPLYDISPATCH_A5;
					}
					// 发送（按站点发送）
					transferService.sendRequest(UUID.getUID(), disOrder.getNumber(),
							sendMessageType, null, event.getSourceSite(), reqParamMap,
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
				throw new RuntimeException("StartDCDispatchMessageDealService类中，消息反馈处理失败！", e);
			}
		}
	}

	/**
	 * @param dmcOrder
	 * @param dmcObjectList
	 * @param dmcDispatchFile
	 * @return boolean
	 */
	private boolean importFileData(DataMap dmcOrder, List dmcObjectList, String dmcDispatchFile) {
		boolean isSuccess = false;
		String sourceSiteIID = (dmcOrder.get(A3A5DataConvertUtil.DOMAINIID));
		String sourceProductIID = dmcOrder.get(A3A5DataConvertUtil.PRODUCTIID);
		String busType = dmcOrder.get("typeNo");
		String tempFolderOid = DmcBusTempHelper.getService().getTempFolderIID(sourceSiteIID, sourceProductIID, busType);
		String tempProductOid = DmcBusTempHelper.getService().getTempProductIID(sourceSiteIID, sourceProductIID, busType);
		if(tempFolderOid == null){
			Site sourceSite = SiteHelper.getSiteService().findSiteById(sourceSiteIID);
			String siteName = sourceSite.getSiteData().getSiteName();
			throw new PlmException("请配置 【"+ siteName +"】的协同缓存区配置！！");
		}
		Map<String, Object> valueMappingInfo = new HashMap<String, Object>();
		String folderClassId = Helper.getClassId(tempFolderOid);
		String folderInnerId = Helper.getInnerId(tempFolderOid);
		Folder tmpFolder = (Folder)Helper.getPersistService().getObject(folderClassId, folderInnerId);
		
//		String tempProductClassId = Helper.getClassId(tempProductOid);
//		String tempProductInnerId = Helper.getInnerId(tempProductOid);
//		ProductContext tmpContext = (ProductContext)Helper.getPersistService().getObject(tempProductClassId, tempProductInnerId);
		//取得单据上下文缓存区配置（电子分发需要配置）
		ProductContext tmpContext = DmcOrderContextHelper.getService().findDmcOrderContextBySiteInnerId(sourceSiteIID).getProduct();
		
		valueMappingInfo.put("Folder", tmpFolder);
		valueMappingInfo.put("Context", tmpContext);
		
		String objType = getObjType(dmcObjectList);

		//导入业务对象到A5系统
		isSuccess = ImportFileHelper.getImportFileService().importFile(sourceSiteIID, dmcDispatchFile, 
				DTSiteConstant.DTSITE_APPVERSION_3_5, objType, valueMappingInfo);

		return isSuccess;
	}
	
	/**
	 * @param dmcOrder
	 * @return DistributeOrder
	 */
	private DistributeOrder createDistributeOrder(DataMap dmcOrder) {
		// 发放单对象
		DistributeOrder disOrder = distributeOrderService.newDistributeOrder();
		// 根据A3的单据信息创建发放单
		disOrder.setNumber(dmcOrder.get("orderID"));// 编号
		disOrder.setName(dmcOrder.get("orderName"));// 名称
		String orderType = ConstUtil.C_ORDERTYPE_0;// 单据类型 0:发放单
		disOrder.setOrderType(orderType);
		disOrder.setNote(dmcOrder.get("orderDescription"));// 备注
		
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
		return disOrder;
	}
	
	/**
	 * @param disOrd
	 * @param disDataObjMapList
	 * @param autoCreateFlag
	 * @return List<Map<String,String>>
	 */
	private List<Map<String,String>> creteOrderObjectLink(DistributeOrder disOrd, 
			List<Map<String,Object>> disDataObjMapList, boolean autoCreateFlag) {

		// 分发对象List长度
		int disDataObjMapListSize = disDataObjMapList.size();

		List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
		// 发放单主对象
		boolean masterFlag = disObjService.getMasterLink(disOrd.getOid());

		// 发放单对象
		String sql = "SELECT disOrd.* FROM DDM_DIS_ORDER disOrd "
				+ " WHERE disOrd.CLASSID || ':' || disOrd.INNERID = ? ";
		List<DistributeOrder> orderList = Helper.getPersistService().query(sql,
						DistributeOrder.class, disOrd.getOid());
				
		for (int i = 0; i < disDataObjMapListSize; i++) {
			Map<String,Object> disDataObjMap = disDataObjMapList.get(i);
			Persistable obj = (Persistable)disDataObjMap.get("persist");
			String dataObjOid = Helper.getOid(obj.getClassId(), obj.getInnerId());
			// 根据当前分发数据源对象取得分发数据对象List
			List<DistributeObject> disObjList = disObjService.getDistributeObjectsByDataOid(dataObjOid);

			Map<String,String> map = new HashMap<String,String>();
			map.put("DATAOID",dataObjOid);
			map.put("objectIID",(String)disDataObjMap.get("oldObjIId"));
			// 当前分发数据源未发放Flag
			boolean objFlag = disObjList == null || disObjList.isEmpty();
			//分发数据对象
			DistributeObject disObject;
			// 当前分发数据源未发放
			if (objFlag) {
				// 创建分发数据对象
				disObject = getDistributeObject(obj);
				// 分发数据对象保存
				disObjService.createDistributeObject(disObject);
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
				//	当更改内对象只有一个时，主对象设置成其内对象，而不再是更改单本身。
				if (disDataObjMapListSize == 2 && obj instanceof ECO){
					// 发放单与分发数据link表创建与保存
					linkOid = disObjService.createDistributeOrderObjectLink(disOrd, disObject, ConstUtil.C_S_ZERO);
					map.put("ISMASTER",ConstUtil.C_S_ZERO);
				}else{
					masterFlag = true;
					// 设置发放单上下文信息
					disOrd.setContextInfo(disObject.getContextInfo());
					// 设置发放单域信息
					disOrd.setDomainInfo(disObject.getDomainInfo());
					lifeService.initLifecycle(disOrd);
					//检查权限
					DistributeOptValidationService validationService = DistributeHelper.getDistributeOptValidationService();
					boolean flag = validationService.getCreateDistributeOrderValidation(Operate.CREATE, disOrd);
					if(flag == false){
						AccessControlHelper.getService().checkEntityPermission(Operate.CREATE, disOrd);
					}
				
					if (orderList==null || orderList.isEmpty()){
						Helper.getPersistService().save(disOrd);
					} else {
						Helper.getPersistService().update(disOrd);
					}

					if (autoCreateFlag) {
						DistributeLifecycleService lifeService = DistributeHelper.getDistributeLifecycleService();
						lifeService.promoteLifeCycle(disOrd);
						Helper.getPersistService().update(disOrd);
					}
					// 发放单与分发数据(主对象)link表创建与保存
					linkOid = disObjService.createDistributeOrderObjectLink(disOrd, disObject, ConstUtil.C_S_ONE);
					map.put("ISMASTER",ConstUtil.C_S_ONE);	
				}
				// 当前分发数据源不是分发数据源(主对象)
			} else {
				// 发放单与分发数据link表创建与保存
				linkOid = disObjService.createDistributeOrderObjectLink(disOrd, disObject, ConstUtil.C_S_ZERO);
				map.put("ISMASTER",ConstUtil.C_S_ZERO);
			}
			map.put("LINKOID",linkOid);

			listMap.add(map);
		}
		return listMap;
	}


	private List<DistributeInfo> createDistributeInfo(DistributeOrder disOrder, List<DataMap> dmcDispatchList,List<Map<String, String>> linkList) {
		List<DistributeInfo> disInfoList = new ArrayList<DistributeInfo>();
		Map<String, String> objectIID2LinkOidMap = new HashMap<String, String>();
		for (Map<String, String> linkMap : linkList) {
			String linkOid = linkMap.get("LINKOID");
			String objectIID = linkMap.get("objectIID");
//			//没有用到的内容
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

			Principal principal = OrganizationHelper.getService().getOrganization(dmcDispatchMap.get("targetDomainIID"));
			// 分发信息名称（单位/人员）
			disInfo.setDisInfoName(dmcDispatchMap.get("targetDomainName"));
			// 分发信息IID（人员或组织的内部标识）
			disInfo.setDisInfoId(dmcDispatchMap.get("targetDomainIID"));
			// 分发信息的类标识（人员或者组织的类标识）
			disInfo.setInfoClassId(principal.getClassId());
			// 分发信息变为已分发状态的时间
			disInfo.setSendTime(0);
			// 分发份数
			disInfo.setDisInfoNum(Long.valueOf(dmcDispatchMap.get("dispatchNum")));
			// 分发介质类型（0为纸质，1为电子，2跨域）
			disInfo.setDisMediaType(ConstUtil.C_DISMEDIATYPE_2);
			// 分发方式（0为直接分发，1为补发，2为移除，3为转发）
			disInfo.setDisType(disOrder.getOrderType());
			// 备注(A3中没有备注)
			disInfo.setNote("");
			String linkOid = objectIID2LinkOidMap.get(dmcDispatchMap.get("objectIID"));
			// 发放单与分发数据LINK内部标识
			disInfo.setDisOrderObjLinkId(Helper.getPersistService().getInnerId(linkOid));
			// 发放单与分发数据LINK类标识
			disInfo.setDisOrderObjLinkClassId(Helper.getPersistService().getClassId(linkOid));
			infoService.createDistributeInfo(disInfo);
			disInfoList.add(disInfo);
		}
		return disInfoList;
	}
	
	/**
	 * 取得转换后的分发数据对象。
	 * 
	 * @param obj Persistable
	 * @return DistributeObject
	 */
	private DistributeObject getDistributeObject(Persistable obj) {
		// 数据转换服务
		DdmDataTransferService tranService = DdmDataTransferHelper.getDistributeObjectService();
		DistributeObject disObject = tranService.transferToDdmData(obj);
		//由于是直接用的A5中的转换,固定为"A5",所以需要重新设置一下.
		disObject.setDataFrom(DTSiteConstant.DTSITE_APPVERSION_3_5);
		return disObject;
	}
	
	/**
	 * @param sourceSiteVersion
	 * @param dmcObjectList
	 * @return List<Map<String,Object>>
	 */
	private List<Map<String,Object>> getDisDataObjMapList(String sourceSiteVersion, List<DataMap> dmcObjectList){
		List<Map<String,Object>> disDataObjMapList = new ArrayList<Map<String,Object>> ();
		//根据A3的信息取得导入文件变换后的分发业务数据对象
		for(DataMap dmcObject : dmcObjectList){
			Map<String,Object> disDataObjMap = new HashMap<String,Object>();
       		//根据系统版本，对象IID，对象verIID，对象类型查询转换后的对象OID
			String newObjIID = getDisDataObjOid(sourceSiteVersion,dmcObject);
			
			//需要增加的分发数据oidList构成
			Persistable persist = Helper.getPersistService().getObject(newObjIID);
			disDataObjMap.put("persist", persist);
			disDataObjMap.put("oldObjIId", dmcObject.get("objectIID"));
			disDataObjMap.put("newObjIID", newObjIID);
		}
		return disDataObjMapList;
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
	
	/**
	 * @param dmcObjectList
	 * @return String
	 */
	private String getObjType(List dmcObjectList){
		if(dmcObjectList.size()==1 && (A3A5DataConvertUtil.DMCOBJECT_TYPE_DOC.equals(((DataMap)dmcObjectList.get(0)).get("type")))){
			return A4XObjTypeConstant.OBJTYPE_A35_DOC;
		}else if(isChangeAndDoc(dmcObjectList)){
			return A4XObjTypeConstant.OBJTYPE_A35_CHANGE_AND_DOC;
		}else if(isContainPart(dmcObjectList)){
			return A4XObjTypeConstant.OBJTYPE_A35_SEND_AND_PART;
		}
		return null;
	}
	
	/**
	 * @param dmcObjectList
	 * @return boolean
	 */
	private boolean isContainPart(List dmcObjectList){
		boolean isSendAndPartAndDoc = false;
		for(int i=0; i<dmcObjectList.size(); i++){
			DataMap tmp = (DataMap)dmcObjectList.get(i);
			if("com.cascc.avidm.prodstruct.model.itemmgr.ItemVo".equals(tmp.get("objectType")) 
				|| "com.cascc.avidm.prodstruct.model.itemmgr.VerVo".equals(tmp.get("objectType"))){
				isSendAndPartAndDoc = true;
				break;
			}
		}
		return isSendAndPartAndDoc;
	}
	
	/**
	 * @param dmcObjectList
	 * @return boolean
	 */
	private boolean isChangeAndDoc(List dmcObjectList){
		boolean isChangeAndDoc = true;
		for(int i=0; i<dmcObjectList.size(); i++){
			DataMap tmp = (DataMap)dmcObjectList.get(i);
			if(!("com.cascc.avidm.docman.model.DmDocument".equals(tmp.get("objectType")) 
				|| "com.bjsasc.avidm.changeman.web.form.ChangeOrderForm".equals(tmp.get("objectType")))){
				isChangeAndDoc = false;
			}
		}
		return isChangeAndDoc;
	}
}
