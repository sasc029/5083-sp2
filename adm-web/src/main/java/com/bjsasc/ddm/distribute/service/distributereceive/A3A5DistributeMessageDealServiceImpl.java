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
	// �ַ��������
	private final DistributeObjectService disObjService = DistributeHelper.getDistributeObjectService();
	//�ַ���Ϣ����
	private final DistributeInfoService infoService = DistributeHelper.getDistributeInfoService();
	//�������ڷ���
	private final DistributeLifecycleService lifeService = DistributeHelper.getDistributeLifecycleService();
	
	public void dealEvent(TransferEvent event) {
		synchronized (A3A5DistributeMessageDealServiceImpl.class){
			try {
				Site selfSite = SiteHelper.getSiteService().findLocalSiteInfo();
				String selfSiteInnerId = selfSite.getInnerId();
				String targetSiteInnerId = event.getTargetSite().getInnerId();
				//�����Ŀ��վ��
				if (selfSiteInnerId.equals(targetSiteInnerId)) {

					Map dataMap = A3A5DataConvertUtil.mapToDatamap(event.getReqParamMap());
					
					DataMap dmcOrder = (DataMap) dataMap.get("dmcOrder");
					//��ʱû���õ��������
					DataMap dmcOrderTarget = (DataMap) dataMap.get("dmcOrderTarget");
					List<DataMap> dmcObjectList = (List<DataMap>)dataMap.get("dmcObjectList"); //�ַ����ݼ���
					List<DataMap> dmcDispatchList = (List<DataMap>)dataMap.get("dmcDispatchList"); //ǩ����Ϣ����
					String dmcDispatchFile = (String)dataMap.get("dmcDispatchFile"); //ǩ����Ϣ�����ļ�
					//TODO���ɣ����ŵ����������ݣ�������Ϣ�����������Լ����Link
					boolean isSuccess = false;
					//����ҵ�����A5ϵͳ
					isSuccess = importFileData(dmcOrder, dmcObjectList, dmcDispatchFile);
					if(!isSuccess){
						throw new PlmException("����ҵ�����A5ϵͳʧ�ܣ��ļ�����" + dmcDispatchFile + "�����������ơ�" + dmcOrder.get("orderName") + "��");
					}
					
					DistributeOrder disOrder = createDistributeOrder(dmcOrder);

					// �ַ�����Դ����(��Ҫ����������)
					List<Map<String,Object>> disDataObjMapList =  new ArrayList<Map<String,Object>>();
					//����A3����Ϣȡ�õ����ļ��任��ķַ�ҵ�����ݶ���
					String sourceSiteIID = (dmcOrder.get(A3A5DataConvertUtil.DOMAINIID));
					Site sourceSite = SiteHelper.getSiteService().findSiteById(sourceSiteIID);
					String sourceSiteVersion = sourceSite.getSystemVersion();
					disDataObjMapList = getDisDataObjMapList(sourceSiteVersion, dmcObjectList);
					// ������ض��󷢷ŵ���ַ�����Link
					List<Map<String, String>> linkList = creteOrderObjectLink(disOrder, disDataObjMapList, true);

					// �����ַ���Ϣ
					List<DistributeInfo> disInfoList = createDistributeInfo(disOrder, dmcDispatchList, linkList);
					
					String infoOids = "";
					for(DistributeInfo disInfo : disInfoList){
						infoOids = infoOids + disInfo.getClassId() + ":" + disInfo.getInnerId() + ";";
					}
					
					//�����ַ���Ϣ��Ӧ�ĵ�������
					DistributeTaskService taskService = DistributeHelper.getDistributeTaskService();
					taskService.createOutSiteDistributeTask(disOrder.getOid(), infoOids,
							sourceSite, true, false, null);
					
					//�洢A3���ݺ����ɵ�A5���ŵ���idת����ϵ�����ݿ�(ת������ǩ�������ʱ����õ�A3�ĵ���id���Ҷ�Ӧ��A5���ŵ�id)
					DataConvertConfigHelper.getService().saveConfig(sourceSiteVersion, 
							dmcOrder.get(A3A5DataConvertUtil.ORDERIID), disOrder.getOid(), A4XObjTypeConstant.OBJTYPE_DISTRIBUTEORDER);
					
					// TODO ���շ�������Ϣ������ �������ӷַ���Ϣ
					//�����շ���һ�ν��յ��ַ�������Ϣʱ Ҫ��һ��������Ϣ��ԭ���𷽵�����Ҫ�ı�״̬
					String isModifyOrderFlowState = "true"; 
					Map<String, String> reqParamMap = new HashMap<String, String>();
					reqParamMap.put("orderIID", dmcOrder.get(A3A5DataConvertUtil.ORDERIID));
					reqParamMap.put("isModifyOrderFlowState", isModifyOrderFlowState);
					reqParamMap.put("orderOid", disOrder.getOid());

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
						sendMessageType = Message4XTypeConstant.DISTRIBUTE_DISTRIBUTEREPLY_A5;
					} else {
						sendMessageType = Message4XTypeConstant.DISTRIBUTE_REPLYDISPATCH_A5;
					}
					// ���ͣ���վ�㷢�ͣ�
					transferService.sendRequest(UUID.getUID(), disOrder.getNumber(),
							sendMessageType, null, event.getSourceSite(), reqParamMap,
							null, TransferConstant.REQTYPE_ASYN);
					
//					// �׳��¼� ��汾�ķ��ŵ���ϵͳ�в�ͬ��ͨ��ҵ��ķ��ŵ������Բ����׳��¼�
//					AbstractEvent createDistributeOrderSucessEvent = EventHelper.getService().getEvent("LifeCycle", "createDistributeOrderSucessEvent"); 
//					//����һ���¼�
//					CreateDistributeOrderSucessEvent cdose = (CreateDistributeOrderSucessEvent) createDistributeOrderSucessEvent;
//					cdose.setDisOrder(disOrder);
//					//�����¼�
//					EventHelper.getService().publishEvent("LifeCycle", cdose);
 
				} else {
					//������A3A5�Ŀ�汾�ַ���ҵ����A5��������Ϊ���ģ���������߼���֧�����ܵ���
				}
			} catch (Exception e) {
				throw new RuntimeException("StartDCDispatchMessageDealService���У���Ϣ��������ʧ�ܣ�", e);
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
			throw new PlmException("������ ��"+ siteName +"����Эͬ���������ã���");
		}
		Map<String, Object> valueMappingInfo = new HashMap<String, Object>();
		String folderClassId = Helper.getClassId(tempFolderOid);
		String folderInnerId = Helper.getInnerId(tempFolderOid);
		Folder tmpFolder = (Folder)Helper.getPersistService().getObject(folderClassId, folderInnerId);
		
//		String tempProductClassId = Helper.getClassId(tempProductOid);
//		String tempProductInnerId = Helper.getInnerId(tempProductOid);
//		ProductContext tmpContext = (ProductContext)Helper.getPersistService().getObject(tempProductClassId, tempProductInnerId);
		//ȡ�õ��������Ļ��������ã����ӷַ���Ҫ���ã�
		ProductContext tmpContext = DmcOrderContextHelper.getService().findDmcOrderContextBySiteInnerId(sourceSiteIID).getProduct();
		
		valueMappingInfo.put("Folder", tmpFolder);
		valueMappingInfo.put("Context", tmpContext);
		
		String objType = getObjType(dmcObjectList);

		//����ҵ�����A5ϵͳ
		isSuccess = ImportFileHelper.getImportFileService().importFile(sourceSiteIID, dmcDispatchFile, 
				DTSiteConstant.DTSITE_APPVERSION_3_5, objType, valueMappingInfo);

		return isSuccess;
	}
	
	/**
	 * @param dmcOrder
	 * @return DistributeOrder
	 */
	private DistributeOrder createDistributeOrder(DataMap dmcOrder) {
		// ���ŵ�����
		DistributeOrder disOrder = distributeOrderService.newDistributeOrder();
		// ����A3�ĵ�����Ϣ�������ŵ�
		disOrder.setNumber(dmcOrder.get("orderID"));// ���
		disOrder.setName(dmcOrder.get("orderName"));// ����
		String orderType = ConstUtil.C_ORDERTYPE_0;// �������� 0:���ŵ�
		disOrder.setOrderType(orderType);
		disOrder.setNote(dmcOrder.get("orderDescription"));// ��ע
		
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

		// �ַ�����List����
		int disDataObjMapListSize = disDataObjMapList.size();

		List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
		// ���ŵ�������
		boolean masterFlag = disObjService.getMasterLink(disOrd.getOid());

		// ���ŵ�����
		String sql = "SELECT disOrd.* FROM DDM_DIS_ORDER disOrd "
				+ " WHERE disOrd.CLASSID || ':' || disOrd.INNERID = ? ";
		List<DistributeOrder> orderList = Helper.getPersistService().query(sql,
						DistributeOrder.class, disOrd.getOid());
				
		for (int i = 0; i < disDataObjMapListSize; i++) {
			Map<String,Object> disDataObjMap = disDataObjMapList.get(i);
			Persistable obj = (Persistable)disDataObjMap.get("persist");
			String dataObjOid = Helper.getOid(obj.getClassId(), obj.getInnerId());
			// ���ݵ�ǰ�ַ�����Դ����ȡ�÷ַ����ݶ���List
			List<DistributeObject> disObjList = disObjService.getDistributeObjectsByDataOid(dataObjOid);

			Map<String,String> map = new HashMap<String,String>();
			map.put("DATAOID",dataObjOid);
			map.put("objectIID",(String)disDataObjMap.get("oldObjIId"));
			// ��ǰ�ַ�����Դδ����Flag
			boolean objFlag = disObjList == null || disObjList.isEmpty();
			//�ַ����ݶ���
			DistributeObject disObject;
			// ��ǰ�ַ�����Դδ����
			if (objFlag) {
				// �����ַ����ݶ���
				disObject = getDistributeObject(obj);
				// �ַ����ݶ��󱣴�
				disObjService.createDistributeObject(disObject);
				map.put("FLAG", ConstUtil.C_S_ZERO);
				// ��ǰ�ַ�����Դ�ѷ���
			} else {
				// ȡ�÷ַ����ݶ���
				disObject = disObjList.get(0);
				map.put("FLAG", ConstUtil.C_S_ONE);
			}

			// ���ŵ���ַ�����link OID
			String linkOid;
			// ��ǰ�ַ�����Դ�Ƿַ�����Դ(������)
			if (!masterFlag) {
				//	�������ڶ���ֻ��һ��ʱ�����������ó����ڶ��󣬶������Ǹ��ĵ�����
				if (disDataObjMapListSize == 2 && obj instanceof ECO){
					// ���ŵ���ַ�����link�����뱣��
					linkOid = disObjService.createDistributeOrderObjectLink(disOrd, disObject, ConstUtil.C_S_ZERO);
					map.put("ISMASTER",ConstUtil.C_S_ZERO);
				}else{
					masterFlag = true;
					// ���÷��ŵ���������Ϣ
					disOrd.setContextInfo(disObject.getContextInfo());
					// ���÷��ŵ�����Ϣ
					disOrd.setDomainInfo(disObject.getDomainInfo());
					lifeService.initLifecycle(disOrd);
					//���Ȩ��
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
					// ���ŵ���ַ�����(������)link�����뱣��
					linkOid = disObjService.createDistributeOrderObjectLink(disOrd, disObject, ConstUtil.C_S_ONE);
					map.put("ISMASTER",ConstUtil.C_S_ONE);	
				}
				// ��ǰ�ַ�����Դ���Ƿַ�����Դ(������)
			} else {
				// ���ŵ���ַ�����link�����뱣��
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
//			//û���õ�������
//			String dataOid = linkMap.get("DATAOID");
//			String isMaster = linkMap.get("ISMASTER");
//			//�ַ�����Դ�Ƿ��ѷ���flag
//			String flag = linkMap.get("FLAG");
			objectIID2LinkOidMap.put(objectIID,linkOid);
		}

		for (DataMap dmcDispatchMap : dmcDispatchList) {
			DistributeInfo disInfo = infoService.newDistributeInfo();
			// �ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��2Ϊվ�㣩
			disInfo.setDisInfoType(ConstUtil.C_DISINFOTYPE_SITE);
			//��λ����(0Ϊ�ڲ���λ,1Ϊ�ⲿ��λ)
			disInfo.setSendType(ConstUtil.C_SENDTYPE_1);

			Principal principal = OrganizationHelper.getService().getOrganization(dmcDispatchMap.get("targetDomainIID"));
			// �ַ���Ϣ���ƣ���λ/��Ա��
			disInfo.setDisInfoName(dmcDispatchMap.get("targetDomainName"));
			// �ַ���ϢIID����Ա����֯���ڲ���ʶ��
			disInfo.setDisInfoId(dmcDispatchMap.get("targetDomainIID"));
			// �ַ���Ϣ�����ʶ����Ա������֯�����ʶ��
			disInfo.setInfoClassId(principal.getClassId());
			// �ַ���Ϣ��Ϊ�ѷַ�״̬��ʱ��
			disInfo.setSendTime(0);
			// �ַ�����
			disInfo.setDisInfoNum(Long.valueOf(dmcDispatchMap.get("dispatchNum")));
			// �ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2����
			disInfo.setDisMediaType(ConstUtil.C_DISMEDIATYPE_2);
			// �ַ���ʽ��0Ϊֱ�ӷַ���1Ϊ������2Ϊ�Ƴ���3Ϊת����
			disInfo.setDisType(disOrder.getOrderType());
			// ��ע(A3��û�б�ע)
			disInfo.setNote("");
			String linkOid = objectIID2LinkOidMap.get(dmcDispatchMap.get("objectIID"));
			// ���ŵ���ַ�����LINK�ڲ���ʶ
			disInfo.setDisOrderObjLinkId(Helper.getPersistService().getInnerId(linkOid));
			// ���ŵ���ַ�����LINK���ʶ
			disInfo.setDisOrderObjLinkClassId(Helper.getPersistService().getClassId(linkOid));
			infoService.createDistributeInfo(disInfo);
			disInfoList.add(disInfo);
		}
		return disInfoList;
	}
	
	/**
	 * ȡ��ת����ķַ����ݶ���
	 * 
	 * @param obj Persistable
	 * @return DistributeObject
	 */
	private DistributeObject getDistributeObject(Persistable obj) {
		// ����ת������
		DdmDataTransferService tranService = DdmDataTransferHelper.getDistributeObjectService();
		DistributeObject disObject = tranService.transferToDdmData(obj);
		//������ֱ���õ�A5�е�ת��,�̶�Ϊ"A5",������Ҫ��������һ��.
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
		//����A3����Ϣȡ�õ����ļ��任��ķַ�ҵ�����ݶ���
		for(DataMap dmcObject : dmcObjectList){
			Map<String,Object> disDataObjMap = new HashMap<String,Object>();
       		//����ϵͳ�汾������IID������verIID���������Ͳ�ѯת����Ķ���OID
			String newObjIID = getDisDataObjOid(sourceSiteVersion,dmcObject);
			
			//��Ҫ���ӵķַ�����oidList����
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

   		//����ϵͳ�汾������IID������verIID���������Ͳ�ѯת����Ķ���OID
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
