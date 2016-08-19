package com.bjsasc.ddm.distribute.service.convertversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.bjsasc.avidm.core.site.Site;
import com.bjsasc.avidm.core.site.SiteHelper;
import com.bjsasc.avidm.core.transfer.TransferObject;
import com.bjsasc.avidm.core.transfer.util.TransferConstant;
import com.bjsasc.ddm.common.A3A5DataConvertUtil;
import com.bjsasc.ddm.common.MessageCreateUtil;
import com.bjsasc.ddm.distribute.model.distributeelectask.DistributeElecTask;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.collaboration.a4x.AbstractMessageConvert;
import com.bjsasc.plm.collaboration.a4x.MessageConvert;
import com.bjsasc.plm.collaboration.a4x.model.DataMap;
import com.bjsasc.plm.collaboration.a4x.model.DmcConvertData;
import com.bjsasc.plm.collaboration.a4x.model.ObjectMessage;
import com.bjsasc.plm.collaboration.a4x.model.StoreFile;
import com.bjsasc.plm.collaboration.a4x.model.StoreObject;


/**
 * ���ӷַ��������ͬ����Ϣ
 * @author zhangguoqiang
 *
 */
public class A3A5DispatchSigTaskOperSynMessageConvertImpl extends AbstractMessageConvert
		implements MessageConvert {
	
	private static Logger logger = Logger.getLogger(A3A5DispatchSigTaskOperSynMessageConvertImpl.class);

	@Override
	public List<DmcConvertData> convertTo4XMessage(TransferObject obj) {
		List<DmcConvertData> datas = new ArrayList<DmcConvertData>();
		ObjectMessage om = super.convertTo4XObjectMesage(obj);
		
		Map<String, String> reqParammap = obj.getReqParamMap();
		Site targetSite = obj.getTargetSite();
		Site sourceSite = obj.getSoureSite(); 

		List<DataMap> dataMapList = getDataMapList(reqParammap, sourceSite, targetSite);
		
		List<String> fileList = new ArrayList<String>();
		String filePath = obj.getFilePath();
		if (filePath != null && !"".equals(filePath)) {
			fileList.add(filePath);
		}

		DmcConvertData data = MessageCreateUtil.createMessage(om, dataMapList, fileList);
		datas.add(data);
		
		return datas;
	}

	@Override
	public List<TransferObject> convertToA5Message(ObjectMessage om,
			List<StoreObject> soList, List<StoreFile> soFileList) {
		String targetSiteInnerId = om.getEndIID();
		Site targetSite = SiteHelper.getSiteService().findSiteById(targetSiteInnerId);
		Site sourceSite = SiteHelper.getSiteService().findSiteById(om.getSendDomainIID());
		List<DataMap> dataMapList = getDataMapFromStoreObject(soList);
		Map<String, String> reqParamMap = makeMessageMap(dataMapList);
		List<TransferObject> transferObjList = new ArrayList<TransferObject>();
		TransferObject object = super.createTransferObject(om.getMsgIID(), "���ӷַ��������ͬ����Ϣ", om.getMsgType(), "", null, sourceSite, targetSite, TransferConstant.REQTYPE_ASYN, reqParamMap);
		transferObjList.add(object);
		return transferObjList;
	}
	
	 /**
	 * @param dataList
	 * @return  Map<String, String>
	 */
	private static Map<String, String> makeMessageMap(List<DataMap> dataList){
			Map<String, String> map = new HashMap<String, String>();
			List<DataMap> taskList = new ArrayList<DataMap>();
			List<DataMap> taskSignatureList = new ArrayList<DataMap>();
				
			for(Iterator<DataMap> iter =dataList.iterator();iter.hasNext();){
				DataMap dataMap =(DataMap)iter.next();
				if(dataMap.getClassName().equals(A3A5DataConvertUtil.taskClassName)){
					taskList.add(dataMap);
				}else if(dataMap.getClassName().equals(A3A5DataConvertUtil.taskSignatureClassName)){
					taskSignatureList.add(dataMap);
				}else if(dataMap.getClassName().equals(A3A5DataConvertUtil.dataMapClassName)){
					map.put("orderIID", dataMap.get("orderIID"));
					map.put("domainIID", dataMap.get("domainIID"));
					map.put("isModifyOrderFlowState", dataMap.get("isModifyOrderFlowState"));
				}
			}
			try {
				map.put("taskList", A3A5DataConvertUtil.listDataMapToJson(taskList));
				map.put("signInfoList", A3A5DataConvertUtil.listDataMapToJson(taskSignatureList));
			} catch (Exception e) {
				logger.error("A3A5DispatchSigTaskOperSynMessageConvertImpl����ת��ΪreqParamMapʧ��",e);
				throw new RuntimeException("A3A5DispatchSigTaskOperSynMessageConvertImpl����ת��ΪreqParamMapʧ��",e);
			}
			return map;
	 }
	 
	/**
	 * �����յ�������ת��ΪA3�õ�DataMap����
	 * @param dataMap
	 * @param sourceSite
	 * @param targetSite
	 * @return List<DataMap>
	 */
	private List<DataMap> getDataMapList(Map<String, String> dataMap, Site sourceSite, Site targetSite) {
		String orderIID = (String)dataMap.get("orderIID");
		String domainIID = (String)dataMap.get("domainIID");
		String isModifyOrderFlowState = (String)dataMap.get("isModifyOrderFlowState");
		String orderOid = (String)dataMap.get("orderOid");
		String taskOid = (String)dataMap.get("taskOid");
		String receiverOid = (String)dataMap.get("receiverOid");
		String receiveTime = (String)dataMap.get("receiveTime");
		String opt = (String)dataMap.get("opt");
		String returnReason = (String)dataMap.get("returnReason");

		DistributeOrder disOrder = (DistributeOrder) Helper.getPersistService().getObject(orderOid);
		DistributeElecTask disElecTask = (DistributeElecTask) Helper.getPersistService().getObject(taskOid);
		return distributeToDataMapList(sourceSite, targetSite, orderIID, domainIID, isModifyOrderFlowState,
				receiverOid, receiveTime, opt, returnReason, disOrder, disElecTask);
	}

	private List<DataMap> distributeToDataMapList(Site sourceSite, Site targetSite,  
			String orderIID, String domainIID, String isModifyOrderFlowState,  
			String receiverOid, String receiveTime, String opt, String returnReason,
			DistributeOrder disOrder, DistributeElecTask disElecTask) {
		List<DataMap> dataMapList = new ArrayList<DataMap>();
		try {
			DataMap datamap = new DataMap();
			datamap.setClassName(A3A5DataConvertUtil.dataMapClassName);
			datamap.put("orderIID", orderIID);
			datamap.put("domainIID", domainIID);
			datamap.put("isModifyOrderFlowState", isModifyOrderFlowState);
			dataMapList.add(datamap);
			//�����ŵ��б�ת��ΪA3����Ϣ�õ�DmcObject��DataMap
			dataMapList.add(A3A5DataConvertUtil.makeA3DmcTaskMsgDataMap(sourceSite,targetSite,
					orderIID, disOrder, disElecTask, receiverOid));
			//�����ŵ��б�ת��ΪA3����Ϣ�õ�DmcObject��DataMap
			dataMapList.add(A3A5DataConvertUtil.makeA3DmcTaskSignatureMsgDataMap(sourceSite,
					orderIID, receiverOid, receiveTime, returnReason, opt, disElecTask));

		} catch (Exception e) {
			logger.error("A3A5DispatchSigTaskOperSynMessageConvertImpl����ת��ΪDataMapʧ��",e);
			throw new RuntimeException("A3A5DispatchSigTaskOperSynMessageConvertImpl����ת��ΪDataMapʧ��",e);
		}

		return dataMapList;
	}
}
