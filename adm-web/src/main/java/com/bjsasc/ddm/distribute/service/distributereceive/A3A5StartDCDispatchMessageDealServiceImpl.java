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
	// �ַ��������
	private final DistributeObjectService disObjService = DistributeHelper.getDistributeObjectService();
	//�ַ���Ϣ����
	private final DistributeInfoService infoService = DistributeHelper.getDistributeInfoService();
	//�������ڷ���
	private final DistributeLifecycleService lifeService = DistributeHelper.getDistributeLifecycleService();
	//��վ��ҵ������ת������
	private static DataConvertConfigService dataConvertCfgService = DataConvertConfigHelper.getService();
	
	public void dealEvent(TransferEvent event) {
		synchronized (A3A5StartDCDispatchMessageDealServiceImpl.class){
			try {
				Site selfSite = SiteHelper.getSiteService().findLocalSiteInfo();
				String selfSiteInnerId = selfSite.getInnerId();
				String targetSiteInnerId = event.getTargetSite().getInnerId();
				//�����Ŀ��վ��
				if (selfSiteInnerId.equals(targetSiteInnerId)) {

					Map dataMap = A3A5DataConvertUtil.mapToDatamap(event.getReqParamMap());
					
					DataMap dmcOrder = (DataMap) dataMap.get("dmcOrder");
					//
					DataMap dmcOrderTarget = (DataMap) dataMap.get("dmcOrderTarget");
					List<DataMap> dmcObjectList = (List<DataMap>)dataMap.get("dmcObjectList"); //���񼯺�
					List<DataMap> dmcDispatchList = (List<DataMap>)dataMap.get("dmcDispatchList"); //ǩ����Ϣ����
					//��ʱû���õ��������
					String dmcDispatchFile = (String)dataMap.get("dmcDispatchFile"); //ǩ����Ϣ�����ļ�
					//TODO���ɣ����ŵ����������ݣ�������Ϣ�����������Լ����Link
					//###---���ӷַ�����Ҫ�����ļ�---###
//					boolean isSuccess = false;
//					//����ҵ�����A5ϵͳ
//					isSuccess = importFileData(dmcOrder, dmcObjectList, dmcDispatchFile);
//					if(!isSuccess){
//						throw new PlmException("����ҵ�����A5ϵͳʧ�ܣ��ļ�����" + dmcDispatchFile + "�����������ơ�" + dmcOrder.get("orderName") + "��");
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
						throw new PlmException("������ ��"+ siteName +"���ķ��Ż���������");
					}
					String tempProductClassId = Helper.getClassId(tempProductOid);
					String tempProductInnerId = Helper.getInnerId(tempProductOid);
//
					ProductContext tmpContext = (ProductContext)Helper.getPersistService().getObject(tempProductClassId, tempProductInnerId);
//					//ȡ�õ��������Ļ��������ã����ӷַ���Ҫ���ã����ڲ�ͬ�ͺŶ�Ӧ��ͬ��Ʒ�Ļ�Ҷ���˵�����ִ���ʽ�����ˣ����Ի��޸Ļ���ǰͨ��
//					ProductContext tmpContext = DmcOrderContextHelper.getService().findDmcOrderContextBySiteInnerId(sourceSiteIID).getProduct();
//					if(tmpContext == null){
//						String siteName = sourceSite.getSiteData().getSiteName();
//						throw new PlmException("������ ��"+ siteName +"���ĵ��������Ļ��������ã���");
//					}
					DistributeOrder disOrder = createOutSiteDistributeOrder(dmcOrder);
// 
//					List<Map<String,Object>> disDataObjMapList =  new ArrayList<Map<String,Object>>();
//					disDataObjMapList = getDisDataObjMapList(sourceSiteVersion, dmcObjectList);
					// ������ض��󷢷ŵ���ַ�����Link
					List<Map<String, String>> linkList = creteOutSiteOrderObjectLink(sourceSiteVersion, disOrder, 
							dmcObjectList, tmpContext);

					// �����ַ���Ϣ
					List<DistributeInfo> disInfoList = createOutSiteDistributeInfo(disOrder, dmcDispatchList, linkList);
//					
					String infoOids = "";
					for(DistributeInfo disInfo : disInfoList){
						infoOids = infoOids + disInfo.getClassId() + ":" + disInfo.getInnerId() + ";";
					}
					//�����ַ���Ϣ��Ӧ�ĵ�������
					List<DistributeElecTask> disElecTaskList = createDistributeTask(
							disOrder, disInfoList, sourceSite, null);
					//�ڽ��յ���Ϣ[���ĵ��ӷַ�������]��ʱ�򣬽��շ�Ӧ��ֻ������һ������
					if(disElecTaskList != null && disElecTaskList.size() == 1){
						// ���·ַ����ݵ�accessURL
						String accessURL = "";
						//AccessUrl����,�����ǵ��ӷַ�������A5�з��ʷַ�������Ҫ����һ�����ӵ�A3ϵͳ����Ӧ�����Url
						String taskIID =  disElecTaskList.get(0).getInnerId();
						String passportURL = dmcOrderTarget.get("passportURL");
						String orderURL = dmcOrder.get("orderURL");
						String targetDomainIID = dmcOrderTarget.get("domainIID");
						accessURL = getAccessURL(orderURL, passportURL, taskIID,
								targetDomainIID, sourceSiteVersion);
						//���·ַ����ݵ�AccessUrl�ֶ�����
						for (Map<String, String> linkMap : linkList) {
							String linkOid = linkMap.get("LINKOID");
							DistributeOrderObjectLink disOrderObjLink = (DistributeOrderObjectLink)Helper.getPersistService().getObject(linkOid);
							DistributeObject disObj =  (DistributeObject)disOrderObjLink.getTo();
							disObj.setAccessUrl(accessURL);
							disObjService.updateDistributeObject(disObj);
						}
					}else{
						Log.debug("�����ڽ��յ���Ϣ[���ĵ��ӷַ�������]��ʱ�򣬽��շ������˶��������Ҫ�鿴���յ���dispatch��targetDomainIID��������");
					}
					
					
					//�洢A3���ݺ����ɵ�A5���ŵ���idת����ϵ�����ݿ�(ת������ǩ�������ʱ����õ�A3�ĵ���id���Ҷ�Ӧ��A5���ŵ�id)
					DataConvertConfigHelper.getService().saveConfig(sourceSiteVersion, 
							dmcOrder.get(A3A5DataConvertUtil.ORDERIID), disOrder.getOid(), A4XObjTypeConstant.OBJTYPE_DISTRIBUTEORDER);
					
					// TODO ���շ�������Ϣ������ �������ӷַ���Ϣ
					//
					String isModifyOrderFlowState = "true"; //�����շ���һ�ν��յ��ַ�������Ϣʱ Ҫ��һ��������Ϣ��ԭ���𷽵�����Ҫ�ı�״̬
					Map<String, String> reqParamMap = new HashMap<String, String>();
					reqParamMap.put("orderIID", dmcOrder.get(A3A5DataConvertUtil.ORDERIID));
					reqParamMap.put("isModifyOrderFlowState", isModifyOrderFlowState);
					reqParamMap.put("orderOid", disOrder.getOid());

					TransferObjectService transferService = TransferObjectHelper.getTransferService();

					// ϵͳ����ʱ���÷���
					DmcConfigService dmcConfigService = DmcConfigHelper.getService();
					// ��ȡ�����Ƿ�ʹ��ʵ��ģʽ
					String isEntityDistributeModel = dmcConfigService.getDmcConfigValue(ConstUtil.IS_ENTITY_DISTRIBUTEMODE_YES);
					// �ַ���Ϣ����
					String sendMessageType = "";
					if ("true".equalsIgnoreCase(isEntityDistributeModel)) {
						sendMessageType = Message4XTypeConstant.DISTRIBUTE_DISTRIBUTEREPLY_A5;
					} else {
						sendMessageType = Message4XTypeConstant.DISTRIBUTE_REPLYDISPATCH_A5;
					}
					// ���ͣ���վ�㷢�ͣ�
					transferService.sendRequest(UUID.getUID(), disOrder.getNumber(),
							sendMessageType, null, sourceSite, reqParamMap,
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
				throw new RuntimeException("A3A5StartDCDispatchMessageDealServiceImpl���У���Ϣ��������ʧ�ܣ�", e);
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
//			throw new PlmException("������ ��"+ siteName +"���Ŀ���վ�㣡��");
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
//		//����ҵ�����A5ϵͳ
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
		// ���ŵ�����
		DistributeOrder disOrder = distributeOrderService.newDistributeOrder();
		// ����A3�ĵ�����Ϣ�������ŵ�
		disOrder.setNumber(dmcOrder.get("orderID"));// ���
		disOrder.setName(dmcOrder.get("orderName"));// ����
		String orderType = ConstUtil.C_ORDERTYPE_0;// �������� 0:���ŵ�
		disOrder.setOrderType(orderType);
		disOrder.setNote(dmcOrder.get("orderDescription"));// ��ע
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
		// ����Ϣ����������Ϣ�ڷ���creteOrderObjectLink������
//		disOrder.setDomainInfo(domainInfo);
//		disOrder.setContextInfo(contextInfo);
		//���ڴ�ʱ����޷�������������Ϣ,�����޷��־û�
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

		// �ַ�����List����
		int dmcObjectListSize = dmcObjectList.size();

		List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
		// ���ŵ�������
		boolean masterFlag = disObjService.getMasterLink(disOrd.getOid());

		// ���ŵ�����
		String sql = "SELECT disOrd.* FROM DDM_DIS_ORDER disOrd "
				+ " WHERE disOrd.CLASSID || ':' || disOrd.INNERID = ? ";
		List<DistributeOrder> orderList = Helper.getPersistService().query(sql,
						DistributeOrder.class, disOrd.getOid());
				
		for (int i = 0; i < dmcObjectListSize; i++) {
			DataMap dmcObject = dmcObjectList.get(i);
			String objectIID = (String)dmcObject.get("objectIID");
			// ���ݵ�ǰ�ַ�����Դ����ȡ�÷ַ����ݶ���List
			List<DistributeObject> disObjList = disObjService.getDistributeObjectsByDataIid(objectIID);

			Map<String,String> map = new HashMap<String,String>();
			map.put("objectIID",objectIID);
			// ��ǰ�ַ�����Դδ����Flag
			boolean objFlag = disObjList == null || disObjList.isEmpty();
			//�ַ����ݶ���
			DistributeObject disObject;
			// ��ǰ�ַ�����Դδ����
			if (objFlag) {
				// �����ַ����ݶ���
				disObject = createDistributeObject(sourceSiteVersion,dmcObject, productContext);

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
//				//	�������ڶ���ֻ��һ��ʱ�����������ó����ڶ��󣬶������Ǹ��ĵ�����
//�ڿ���ַ���û��������������
//				if (dmcObjectListSize == 2 && obj instanceof ECO){
//					// ���ŵ���ַ�����link�����뱣��
//					linkOid = disObjService.createDistributeOrderObjectLink(disOrd, disObject, ConstUtil.C_S_ZERO);
//					map.put("ISMASTER",ConstUtil.C_S_ZERO);
//				}else{
					masterFlag = true;
					// ���÷��ŵ���������Ϣ
					disOrd.setContextInfo(disObject.getContextInfo());
					// ���÷��ŵ�����Ϣ
					disOrd.setDomainInfo(disObject.getDomainInfo());
					lifeService.initLifecycle(disOrd);
//					//���Ȩ��
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
					//���·��ŵ���״̬Ϊ�ѷַ�
					lifeService.updateLifeCycleByStateId(disOrd,ConstLifeCycle.LC_DISTRIBUTED.getId());
//��Ϊ���շ�����Ҫ�������
//					if (autoCreateFlag) {
//						lifeService.promoteLifeCycle(disOrd);
//						Helper.getPersistService().update(disOrd);
//					}
					// ���ŵ���ַ�����(������)link�����뱣��
					linkOid = createDistributeOrderObjectLink(disOrd, disObject, ConstUtil.C_S_ONE);

					map.put("ISMASTER",ConstUtil.C_S_ONE);	
//				}
				// ��ǰ�ַ�����Դ���Ƿַ�����Դ(������)
			} else {
				// ���ŵ���ַ�����link�����뱣��
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
//			//������û���õ�������
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

			Site site = SiteHelper.getSiteService().findSiteById(dmcDispatchMap.get("targetDomainIID"));
			// �ַ���Ϣ���ƣ���λ/��Ա��
			disInfo.setDisInfoName(dmcDispatchMap.get("targetDomainName"));
			// �ַ���ϢIID����Ա����֯���ڲ���ʶ��
			disInfo.setDisInfoId(dmcDispatchMap.get("targetDomainIID"));
			// �ַ���Ϣ�����ʶ����Ա������֯�����ʶ��
			disInfo.setInfoClassId(site.getClassId());
			// �ַ���Ϣ��Ϊ�ѷַ�״̬��ʱ�䣨A5���յ���ʱ�㣬����״̬�϶����ѷַ���
			disInfo.setSendTime(System.currentTimeMillis());
			// �ַ�����
			disInfo.setDisInfoNum(Long.valueOf(dmcDispatchMap.get("dispatchNum")));
			// �ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2����
			disInfo.setDisMediaType(ConstUtil.C_DISMEDIATYPE_2);
			// �ַ���ʽ��0Ϊֱ�ӷַ���1Ϊ������2Ϊ�Ƴ���3Ϊת���� ���������0��1����ĳ��Ͽ��ܴ������⣨��ȷ�ϣ�
			disInfo.setDisType(disOrder.getOrderType());
			// ��ע(A3��û�б�ע)
			disInfo.setNote("");
			String linkOid = objectIID2LinkOidMap.get(dmcDispatchMap.get("objectIID"));
			// ���ŵ���ַ�����LINK�ڲ���ʶ
			disInfo.setDisOrderObjLinkId(Helper.getPersistService().getInnerId(linkOid));
			// ���ŵ���ַ�����LINK���ʶ
			disInfo.setDisOrderObjLinkClassId(Helper.getPersistService().getClassId(linkOid));
			infoService.createDistributeInfo(disInfo);
			//������A5���յķַ���Ϣ����A5���յ���ʱ��״̬��Ӧ�����ѷַ�
			lifeService.updateLifeCycleByStateId(disInfo, ConstLifeCycle.LC_DISTRIBUTED.getId());
			disInfoList.add(disInfo);
		}
		return disInfoList;
	}

	private List<DistributeElecTask> createDistributeTask(DistributeOrder disOrderObj, List<DistributeInfo> disInfoList,
			Site site, Site centerSite) {
		//���ص���װ������
		List<DistributeElecTask> disTaskList = new ArrayList<DistributeElecTask>();
		
		Map<String, List<DistributeInfo>> taskMap = new HashMap<String, List<DistributeInfo>>();
		//�ѷַ���Ϣ���շַ�Ŀ�������
		//��Ȼ�ǰ��շַ�Ŀ������飬���������ķ������շ�ʱ�Ѿ������˹��˴������շ�ֻ���յ���������ݣ�����Ӧ��ֻ������һ������
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

		//��������ַ����񼯺�
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
					//��������(disInfoList�е�һ���ַ���Ϣ)
					disElecTask.setNumber(disOrderObj.getNumber() + ConstUtil.C_ELECTASK_STR);
					disElecTask.setName(disOrderObj.getName() + ConstUtil.C_ELECTASK_STR);

					disElecTask.setNote("");
					disElecTask.setElecTaskType(ConstUtil.ELEC_TASKTYPE_OUT);

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
					disElecTaskService.createDistributeElecTask(disElecTask);

//					//������ģʽ��վ��Ϊ��������
//					if (centerFlag == true) {
//						// ��������ַ���Ϣ���������link����
//						createOutDistributeTaskInfoLink(disElecTask, info);
//					} else {
						createDistributeTaskInfoLink(disElecTask, info);
//					}
					//��Ҫ���ص������б�����Ӧ��Ϊһ������
					disTaskList.add(disElecTask);
				} else {
//					//������ģʽ��վ��Ϊ��������
//					if (centerFlag == true) {
//						// ��������ַ���Ϣ���������link����
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
		linkDisObject.setTaskType(ConstUtil.C_TASKTYPE_ELEC);
		service.createDistributeTaskInfoLink(linkDisObject);
	}

	/**
	 * ȡ��ת����ķַ����ݶ���
	 * 
	 * @param dmcObject DataMap
	 * @return DistributeObject
	 */
	private DistributeObject createDistributeObject(String sourceSiteVersion, DataMap dmcObject, ProductContext productContext) {
		// ����ת������
		DistributeObjectService disObjService = DistributeHelper.getDistributeObjectService();
		DistributeObject disObject = disObjService.newDistributeObject();
		//TODO ��Ҫ���ݽ��յ���objectIID��ѯ��Ӧ��A5�е�innerId
		//��ţ����ȷ����A3��������dmcObjectֻ���ĵ��Ͳ��������ĵ���������ͨ���ĵ������Ĺ����ҵ��Ĳ�������dmcObject���
		if(A3A5DataConvertUtil.DMCOBJECT_TYPE_DOC.equals(dmcObject.get("type"))){
			//���ӷַ�û�����ݵ�ת������Ҫת�����ID
//			String dataInnerId = getDisDataObjOid(sourceSiteVersion, dmcObject);
			String dataInnerId = dmcObject.get("objectIID");
			disObject.setDataInnerId(dataInnerId);
			disObject.setDataClassId(Document.class.getSimpleName());
		}else if(A3A5DataConvertUtil.DMCOBJECT_TYPE_ITEM.equals(dmcObject.get("type"))){
			//���ӷַ�û�����ݵ�ת������Ҫת�����ID
//			String dataInnerId = getDisDataObjOid(sourceSiteVersion, dmcObject);
			String dataInnerId = dmcObject.get("objectIID");
			disObject.setDataInnerId(dataInnerId);
			disObject.setDataClassId(Part.class.getSimpleName());
		}
		disObject.setDataFrom(DTSiteConstant.DTSITE_APPVERSION_3_5);
		disObject.setNumber(dmcObject.get("objectID"));
		disObject.setName(dmcObject.get("objectName"));
		//A3��û�б�ע
		disObject.setNote("");
		// ��ʱ��û���������������޷��趨�����AccessUrl��Ҫ�����������ʱ�����
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
		// ����Ϣ����������Ϣ�ڷ���creteOrderObjectLink������
		disObject.setDomainInfo(productContext.getDomainInfo());
		disObject.setContextInfo(productContext.buildContextInfo());
		
		// �ַ����ݶ��󱣴�
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
		// �Ƿ���������ŵ����Ǹ��ڵ㣨0�����ǣ�1���ǣ�
		// �ַ����ݶ��������Դ������Part�������Ǹ��ڵ㣬����Ĭ������0
		if (dataObj instanceof Part) {
			linkDisObject.setIsParent("1");
		} else {
			linkDisObject.setIsParent("0");
		}

		linkDisObject.setDisDeadLine(System.currentTimeMillis() + 3 * 24 * 3600 * 1000);
		linkDisObject.setDisUrgent("0");
		linkDisObject.setDisStyle("0");

		// ���Ǵӷ��ŵ���ʼ�����ַ����ݶ���ģ�����������������Ĭ������0
		// �Ƿ���������ŵ�����������0�����ǣ�1����
		linkDisObject.setIsMaster(isMaster);
		// �������ڷ���
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		// �������ڲ���
		life.initLifecycle(linkDisObject);
		
		Helper.getPersistService().save(linkDisObject);
		
		//���·ַ�������������״̬Ϊ�����(���������򷢹���������״̬��A5�н��յ�ʱ���Ӧ���������)
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
				//����
				throw new RuntimeException("The dataURL is Error�� dataURL = " + dataURL);
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
}
