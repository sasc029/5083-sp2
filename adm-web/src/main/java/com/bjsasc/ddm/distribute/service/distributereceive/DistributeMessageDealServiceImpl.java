package com.bjsasc.ddm.distribute.service.distributereceive;

import java.io.File;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.avidm.core.conversion.GetMapKeyUtil;
import com.bjsasc.avidm.core.extinter.imp.ImportFileHelper;
import com.bjsasc.avidm.core.site.Site;
import com.bjsasc.avidm.core.site.SiteHelper;
import com.bjsasc.avidm.core.transfer.TransferObjectHelper;
import com.bjsasc.avidm.core.transfer.TransferObjectService;
import com.bjsasc.avidm.core.transfer.event.TransferEvent;
import com.bjsasc.avidm.core.transfer.util.TransferConstant;
import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.service.distributetask.DistributeTaskService;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleTemplate;
import com.bjsasc.platform.objectmodel.business.lifeCycle.State;
import com.bjsasc.plm.baseline.BaselineManager;
import com.bjsasc.plm.collaboration.adapter.base.MessageDealService;
import com.bjsasc.plm.collaboration.config.constant.DmcConfigConstant;
import com.bjsasc.plm.collaboration.config.constant.MessageTypeconstant;
import com.bjsasc.plm.collaboration.config.service.DmcBusTempHelper;
import com.bjsasc.plm.collaboration.config.service.DmcConfigHelper;
import com.bjsasc.plm.collaboration.config.service.DmcConfigService;
import com.bjsasc.plm.collaboration.site.constant.DCSiteConstant;
import com.bjsasc.plm.collaboration.site.model.DCSiteAttribute;
import com.bjsasc.plm.collaboration.site.service.DCSiteAttributeHelper;
import com.bjsasc.plm.collaboration.util.ObjectToMapUtil;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.baseline.model.Baseline;
import com.bjsasc.plm.core.baseline.model.Baselineable;
import com.bjsasc.plm.core.baseline.model.Baselined;
import com.bjsasc.plm.core.baseline.model.ManagedBaseline;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.folder.Folder;
import com.bjsasc.plm.core.folder.Foldered;
import com.bjsasc.plm.core.lifecycle.LifeCycleHelper;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.url.Url;
import com.cascc.avidm.login.model.PersonModel;
import com.cascc.platform.aa.AAProvider;
import com.cascc.platform.uuidservice.UUID;

@SuppressWarnings({"unused", "rawtypes", "unchecked" })
public class DistributeMessageDealServiceImpl implements MessageDealService {

	public void dealEvent(TransferEvent event) {
		synchronized (DistributeMessageDealServiceImpl.class) {
		Site selfSite = SiteHelper.getSiteService().findLocalSiteInfo();
		String selfSiteInnerId = selfSite.getInnerId();
		String targetSiteInnerId = event.getTargetSite().getInnerId();
		// ��ȡϵͳ����ģʽ
		DmcConfigService dmcConfigService = DmcConfigHelper.getService();
		boolean isDcDeployModel = dmcConfigService.getIsDealOnDC();
		String orderOids = "";
		String infoOids = "";
		//���������ģʽ
		if (isDcDeployModel == true) {
			boolean isEntitySignature = false;
			String target = event.getTargetSite().getInnerId();
			DCSiteAttribute dcSiteAttr = DCSiteAttributeHelper.getDCSiteAttrService().findDcSiteAttrByDTSiteId(
					selfSite.getInnerId());
			String entitySignature = DmcConfigHelper.getService().getDmcConfigValue(ConstUtil.IS_ENTITY_DISTRIBUTEMODE_YES);
			if(null != entitySignature){
				isEntitySignature = ("true".equalsIgnoreCase(entitySignature));
			}
			
			//�������������
			if(dcSiteAttr != null && "true".equals(dcSiteAttr.getIsSiteControl())){
				if(isEntitySignature == false){
					//���ӷַ������벢���ɵ�������
					List<Map<Object, List<Object>>> list = new ArrayList<Map<Object, List<Object>>>();
					
					//String siteNo = event.getSourceSite().getSiteData().getSiteNo();
					String siteID = event.getSourceSite().getInnerId();
		
					//ȡ��map(�ļ�����Ϣ����������Ϣ������Ϣ)
					Map<String, Object> valueMappingInfo = getValueMappingInfo(event);
		
					String importFile = event.getFilePath();
					File file = new File(importFile);
					
					//����
					list = ImportFileHelper.getImportFileService().importFile(siteID,new File(importFile), valueMappingInfo);
					// �����ݱ��浽���ݿ�
					boolean isSuccess = ImportFileHelper.getImportFileService().saveObject(list,event.getSourceSite());
					DistributeOrder disOrder = null;
					if (isSuccess) {
						//�������ɵ�������
						for (int i = 0; i < list.size(); i++) {
							Map<Object, List<Object>> map = list.get(i);
							Object obj = GetMapKeyUtil.getMapKey(map);
							if (obj instanceof DistributeOrder) {
								disOrder = (DistributeOrder)obj;
								orderOids = disOrder.getOid();
								//ȡ�÷��ŵ�������������״̬
								LifeCycleManaged lifeCycleManaged = (LifeCycleManaged)obj;
//								LifeCycleTemplate lifeCycleTemplate = LifeCycleHelper.getService().getLifeCycleTemplate((LifeCycleManaged)obj);
//		
//								List<State> stateList = LifeCycleHelper.getService().findStates(lifeCycleTemplate);
//								State lifecycleState = null;
//								for (State state : stateList) {
//									if (ConstUtil.LC_DISTRIBUTED.equals(state.getId())) {
//										lifecycleState = state;
//									}
//								}
								State lifecycleState = ConstUtil.LC_DISTRIBUTED;
								//Helper.getLifeCycleService().setLifeCycleTemplate(lifeCycleManaged,lifeCycleTemplate, lifecycleState);
								 LifeCycleHelper.getService().setLifeCycleState(lifeCycleManaged, lifecycleState);
							} else if (obj instanceof DistributeInfo) {
								DistributeInfo info = ((DistributeInfo) obj);
								infoOids = infoOids + info.getClassId() + ":" + info.getInnerId() + ";";
								
							}
						}
					}
					
					//���ɵ�������
					//��ӷַ����ݷ���url
					PersonModel person = SessionHelper.getService().getPersonModel();
					String siteIID = selfSite.getInnerId();
					//��ӻ���ҵ�����
					List<Baselined> baselineList = new ArrayList<Baselined>();
					for (int i = 0; i < list.size(); i++) {
						Map<Object, List<Object>> map = list.get(i);
						Object obj = GetMapKeyUtil.getMapKey(map);
						if (obj instanceof DistributeObject) {
							DistributeObject disObj = (DistributeObject) obj;
							Persistable dataObj = Helper.getPersistService().getObject(disObj.getDataClassId() + ":" + disObj.getDataInnerId());
							if(dataObj instanceof Baselineable){
								baselineList.add((Baselineable)dataObj);
							}
							String domainRef = AAProvider.getAppRightsMgrService()
									.getAADomainApp4TreeDataByAppRef(null, "plm").get(0).getDomainRef();
							String http = DCSiteConstant.TRANS_PROTOCOL;
							String ip = selfSite.getSiteIP() + ":";
							String spot = selfSite.getSitePort();
							String url = http + ip + spot + Url.APP + "/ddm/public/visitObject.jsp?OID="
									+ disObj.getClassId() + ":" + disObj.getInnerId() + "&PT_DOMAIN_IID="
									+ domainRef;
							disObj.setLinkUrl(url);
							Helper.getPersistService().update(disObj);
						}
					}
					//�����������ݷַ�����
					DistributeOrder order = (DistributeOrder)Helper.getPersistService().getObject(orderOids);
					String number = order.getNumber() + ConstUtil.OUT_DATA_BASELINE;
					Baselined baselined = baselineList.get(0);
					Folder folder = null;
					if(baselined instanceof Foldered){
						Foldered foldered = (Foldered)baselined;
						folder =(Folder) PersistHelper.getService().getObject(foldered.getFolderInfo().getFolder().getClassId(), foldered.getFolderInfo().getFolder().getInnerId());
					}
					ManagedBaseline managedBaseline = BaselineManager.getManager().createBaseline(ManagedBaseline.CLASSID, number, number, null, false, false, folder, new HashMap<String, Object>());
					Baseline baseline = BaselineManager.getManager().addToBaseline(baselineList, managedBaseline);
					//���ɵ�������
					DistributeTaskService taskService = DistributeHelper.getDistributeTaskService();
					List<String> oidsList = taskService.createCenterSiteDistributeTask(orderOids, infoOids,
							(Site) event.getSourceSite(), true, true, null);

					//�������ķ�����Ϣ������
					Map<String, String> reqParamMap = new HashMap<String, String>();
					reqParamMap.put("orderOid", orderOids);
					reqParamMap.put("taskOid", oidsList.get(1));
					reqParamMap.put("infosOid", oidsList.get(0));
					reqParamMap.put("flag", "0");
					reqParamMap.put("siteType", "0");

					TransferObjectService transferService = TransferObjectHelper.getTransferService();
					transferService.sendRequest(UUID.getUID(), disOrder.getNumber(),
							MessageTypeconstant.REPLY_SEND_DISTRIBUTE, null, (Site) event.getSourceSite(),
							reqParamMap, null, TransferConstant.REQTYPE_SYN);
				} else {
					//ʵ��ַ���ת��
					TransferObjectHelper.getTransferService().transmit(event.getInnerId());
				}
			} else {
				//����ģʽ������������
				List<Map<Object, List<Object>>> list = new ArrayList<Map<Object, List<Object>>>();
				
				String outSiteId = event.getSourceSite().getInnerId();
	
				//ȡ��map(�ļ�����Ϣ����������Ϣ������Ϣ)
				Map<String, Object> valueMappingInfo = getValueMappingInfo(event);
	
				String importFile = event.getFilePath();
				File file = new File(importFile);
				
				//����
				list = ImportFileHelper.getImportFileService().importFile(outSiteId,new File(importFile), valueMappingInfo);
				// �����ݱ��浽���ݿ�
				boolean isSuccess = ImportFileHelper.getImportFileService().saveObject(list,event.getSourceSite());
				DistributeOrder disOrder = null;
				if (isSuccess) {
					//�������ɵ�������
					for (int i = 0; i < list.size(); i++) {
						Map<Object, List<Object>> map = list.get(i);
						Object obj = GetMapKeyUtil.getMapKey(map);
						if (obj instanceof DistributeOrder) {
							disOrder = (DistributeOrder)obj;
							orderOids = disOrder.getOid();
							//ȡ�÷��ŵ�������������״̬
							LifeCycleManaged lifeCycleManaged = (LifeCycleManaged)obj;
							LifeCycleTemplate lifeCycleTemplate = LifeCycleHelper.getService().getLifeCycleTemplate((LifeCycleManaged)obj);
	
							List<State> stateList = LifeCycleHelper.getService().findStates(lifeCycleTemplate);
							State lifecycleState = null;
							for (State state : stateList) {
								if ("distributed".equals(state.getId())) {
									lifecycleState = state;
								}
							}
							Helper.getLifeCycleService().setLifeCycleTemplate(lifeCycleManaged,
									lifeCycleTemplate, lifecycleState);
						} else if (obj instanceof DistributeInfo) {
							DistributeInfo info = ((DistributeInfo) obj);
							infoOids = infoOids + info.getClassId() + ":" + info.getInnerId() + ";";
							
						}
					}
				}
				//����ӷ���URL��
				try {
					Site site = SiteHelper.getSiteService().findLocalSiteInfo();
					String siteIID = site.getInnerId();
					//��ӻ���ҵ�����
					List<Baselined> baselineList = new ArrayList<Baselined>();
					for (int i = 0; i < list.size(); i++) {
						Map<Object, List<Object>> map = list.get(i);
						Object obj = GetMapKeyUtil.getMapKey(map);
						if (obj instanceof DistributeObject) {
							DistributeObject disObj = (DistributeObject) obj;
							Persistable object = Helper.getPersistService().getObject(disObj.getDataClassId(), disObj.getDataInnerId());
							//���ӷַ�
							if(object == null){
								InetAddress addr = InetAddress.getLocalHost();
								String ip = addr.getHostAddress().toString();
								String port = event.getTargetSite().getSitePort();
								String dataURL = disObj.getLinkUrl();
								int spot = dataURL.indexOf("PT_DOMAIN_IID");
								String domainUrl = dataURL.substring(spot, dataURL.length());
								String[] domainIIDS = domainUrl.split("=");
								String PT_DOMAIN_IID = domainIIDS[1];
								boolean isViewLocalData = false;
								int index = dataURL.indexOf(ip + ":" + port);
								if (index != -1) {
									isViewLocalData = true;
								}
								String strAction = "";
								if ("false".equalsIgnoreCase("false")) { //û��ʹ�ô������������Ҫ��ȡ����url��passport[���޸ģ������û��]
									int iLoc = -1;

									for (int j = 0; j < 3; j++) {
										iLoc = dataURL.indexOf("/", (iLoc + 1));
									}
									if (-1 == iLoc) {
										//����
										throw new RuntimeException("The dataURL is Error�� dataURL = " + dataURL);
									} else {
										strAction = dataURL.substring(0, iLoc);
									}
									dataURL = dataURL.substring(iLoc);
									if (isViewLocalData) {
										strAction = dataURL;
									} else {
										//��Ҫ�ĳ�avidm
//										int j = dataURL.indexOf("avidm");
//										dataURL = dataURL.substring(j + 5);
										String contextStr = Url.APP;
										int j = dataURL.indexOf(contextStr);
										dataURL = dataURL.substring(j + contextStr.length());
										dataURL += "&outUserIID={userIID}&disOrderOid="+disOrder.getOid();
										strAction += Url.APP
												+ "/userloginservlet?LoginFrom=Login4Site&PT_USER_IID={userIID}&PT_DOMAIN_IID="
												+ PT_DOMAIN_IID + "&PT_SITE_IID=" + siteIID + "&TargetURL="
												+ URLEncoder.encode(dataURL, "UTF-8");
										disObj.setAccessUrl(strAction);
										Helper.getPersistService().update(disObj);
									}
								}
							} else {
								//����ģʽʵ��ַ����������ߣ�
								if(object instanceof Baselineable){
									baselineList.add((Baselineable)object);
								}
							}
						}
					}
					//���ɵ�������
					Map dataMap = ObjectToMapUtil.mapToObject(event.getReqParamMap());
					String sourceSiteId = (String) dataMap.get("sourceSiteId");
					Site sourceSite = null;
					Site centerSite = null;
					if(!"".equals(sourceSiteId) && sourceSiteId != null){
						//���ӷַ�
						sourceSite = SiteHelper.getSiteService().findSiteById(sourceSiteId);
						centerSite = (Site)event.getSourceSite();
					} else {
						//ʵ��ַ�
						sourceSite = event.getSourceSite();
						//�����������ݷַ�����
						DistributeOrder order = (DistributeOrder)Helper.getPersistService().getObject(orderOids);
						String number = order.getNumber() + ConstUtil.OUT_DATA_BASELINE;
						Baselined baselined = baselineList.get(0);
						Folder folder = null;
						if(baselined instanceof Foldered){
							Foldered foldered = (Foldered)baselined;
							folder =(Folder) PersistHelper.getService().getObject(foldered.getFolderInfo().getFolder().getClassId(), foldered.getFolderInfo().getFolder().getInnerId());
						}
						ManagedBaseline managedBaseline = BaselineManager.getManager().createBaseline(ManagedBaseline.CLASSID, number, number, null, false, false, folder, new HashMap<String, Object>());
						Baseline baseline = BaselineManager.getManager().addToBaseline(baselineList, managedBaseline);
					}
					DistributeTaskService taskService = DistributeHelper.getDistributeTaskService();
					
					List<String> oidsList = taskService.createOutSiteDistributeTask(orderOids, infoOids,
							sourceSite, true, false, centerSite);

					//���շ����Ͳ���
					Map<String, String> reqParamMap = new HashMap<String, String>();
					reqParamMap.put("orderOid", orderOids);
					reqParamMap.put("taskOid", oidsList.get(1));
					reqParamMap.put("infosOid", oidsList.get(0));
					reqParamMap.put("flag", "0");
					reqParamMap.put("siteType", "1");
					
					TransferObjectService transferService = TransferObjectHelper.getTransferService();
					//���ӷַ�
					if(!"".equals(sourceSiteId) && sourceSiteId != null){
						//������Ϣ����������
						transferService.sendRequest(UUID.getUID(), disOrder.getNumber(),
								MessageTypeconstant.REPLY_SEND_DISTRIBUTE, null, event.getSourceSite(), reqParamMap, null,
								TransferConstant.REQTYPE_SYN);
					}
					
					//������Ϣ������
					transferService.sendRequest(UUID.getUID(), disOrder.getNumber(),
							MessageTypeconstant.REPLY_SEND_DISTRIBUTE, null, sourceSite, reqParamMap, null,
							TransferConstant.REQTYPE_SYN);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					throw new RuntimeException("DistributeMessageDealService���У�����ģʽ��Ϣ����ʧ�ܣ�", e);
				}
			
			}
		} else {
			//����ģʽ�������Ŀ��վ��
			if (selfSiteInnerId.equals(targetSiteInnerId)) {
				String target = event.getTargetSite().getInnerId();
				DCSiteAttribute dcSiteAttr = DCSiteAttributeHelper.getDCSiteAttrService().findDcSiteAttrByDTSiteId(
						selfSite.getInnerId());
				String outSiteId = event.getSourceSite().getInnerId();

				//ȡ��map(�ļ�����Ϣ����������Ϣ������Ϣ)
				Map<String, Object> valueMappingInfo = getValueMappingInfo(event);

				String importFile = event.getFilePath();
				File file = new File(importFile);
				//����
				List<Map<Object, List<Object>>> list = ImportFileHelper.getImportFileService().importFile(outSiteId,
						new File(importFile), valueMappingInfo);
				// �����ݱ��浽���ݿ�
				boolean isSuccess = ImportFileHelper.getImportFileService().saveObject(list,event.getSourceSite());
				DistributeOrder order = null;
				//��ӻ���ҵ�����
				List<Baselined> baselineList = new ArrayList<Baselined>();
				if (isSuccess) {
					//�������ɵ�������
					for (int i = 0; i < list.size(); i++) {
						Map<Object, List<Object>> map = list.get(i);
						Object obj = GetMapKeyUtil.getMapKey(map);
						if (obj instanceof DistributeOrder) {
							order = (DistributeOrder)obj;
							orderOids = order.getOid();
						} else if (obj instanceof DistributeInfo) {
							DistributeInfo info = ((DistributeInfo) obj);
							infoOids = infoOids + info.getClassId() + ":" + info.getInnerId() + ";";
						} else if (obj instanceof DistributeObject) {
							DistributeObject disObj = (DistributeObject) obj;
							Persistable dataObj = Helper.getPersistService().getObject(disObj.getDataClassId() + ":" + disObj.getDataInnerId());
							if(dataObj instanceof Baselineable){
								baselineList.add((Baselineable)dataObj);
							}
						}
					}
				}
				
				//����ģʽ�����շ�������Ϣ������
				Map<String, String> reqParamMap = new HashMap<String, String>();
				reqParamMap.put("orderOid", orderOids);

				//�����������ݷַ�����
				String number = order.getNumber() + ConstUtil.OUT_DATA_BASELINE;
				Baselined baselined = baselineList.get(0);
				Folder folder = null;
				if(baselined instanceof Foldered){
					Foldered foldered = (Foldered)baselined;
					folder =(Folder) PersistHelper.getService().getObject(foldered.getFolderInfo().getFolder().getClassId(), foldered.getFolderInfo().getFolder().getInnerId());
				}
				ManagedBaseline managedBaseline = BaselineManager.getManager().createBaseline(ManagedBaseline.CLASSID, number, number, null, false, false, folder, new HashMap<String, Object>());
				Baseline baseline = BaselineManager.getManager().addToBaseline(baselineList, managedBaseline);
				
				//���ɵ�������
				DistributeTaskService taskService = DistributeHelper.getDistributeTaskService();
				List<String> oidsList = taskService.createOutSiteDistributeTask(orderOids, infoOids,
						(Site) event.getSourceSite(), true, false, null);
				reqParamMap.put("taskOid", oidsList.get(1));
				reqParamMap.put("infosOid", oidsList.get(0));
				reqParamMap.put("flag", "0");
				reqParamMap.put("siteType", "1");

				TransferObjectService transferService = TransferObjectHelper.getTransferService();
				transferService.sendRequest(UUID.getUID(), order.getNumber(), MessageTypeconstant.REPLY_SEND_DISTRIBUTE,
						null, (Site) event.getSourceSite(), reqParamMap, null, TransferConstant.REQTYPE_SYN);
			} else {
				//����ģʽ����Ŀ��վ�㣬ת����
				TransferObjectHelper.getTransferService().transmit(event.getInnerId());
			}
		}
		}
	}

	//ȡ��map(�ļ�����Ϣ����������Ϣ������Ϣ)
	public Map<String, Object> getValueMappingInfo(TransferEvent event) {
		Map<String, Object> valueMappingInfo = new HashMap();
		try {
			Map dataMap = null;
			dataMap = ObjectToMapUtil.mapToObject(event.getReqParamMap());
			String sourceSiteIID = (String) dataMap.get("sourceSiteIID");
			String busType = (String) dataMap.get("busType");
			String sourceProductIID = (String) dataMap.get("productIID");

			String tempFolderOid = DmcBusTempHelper.getService().getTempFolderIID(sourceSiteIID, sourceProductIID,
					busType);
			String tempProductOid = DmcBusTempHelper.getService().getTempProductIID(sourceSiteIID, sourceProductIID,
					busType);

			String folderClassId = Helper.getClassId(tempFolderOid);
			String folderInnerId = Helper.getInnerId(tempFolderOid);
			//��ȡ�ļ�����Ϣ
			Folder tmpFolder = (Folder) Helper.getPersistService().getObject(folderClassId, folderInnerId);

			String tempProductClassId = Helper.getClassId(tempProductOid);
			String tempProductInnerId = Helper.getInnerId(tempProductOid);
			//��ȡ��������Ϣ
			Context tmpContext = (Context) Helper.getPersistService().getObject(tempProductClassId, tempProductInnerId);
			//��ȡ����Ϣ
			Domained domain = (Domained) Helper.getPersistService().getObject(
					tmpFolder.getDomainInfo().getDomainRef().getClassId(),
					tmpFolder.getDomainInfo().getDomainRef().getInnerId());
			valueMappingInfo.put("Domain", domain);
			valueMappingInfo.put("Folder", tmpFolder);
			valueMappingInfo.put("Context", tmpContext);
		} catch (Exception e) {
			throw new RuntimeException("ReplySynDistributeDealServiceȡ��map(�ļ�����Ϣ����������Ϣ������Ϣ)ʧ�ܣ�", e);
		}
		return valueMappingInfo;
	}
}
