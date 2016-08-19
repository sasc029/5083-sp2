package com.bjsasc.ddm.distribute.service.convertversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bjsasc.avidm.core.site.Site;
import com.bjsasc.avidm.core.site.SiteHelper;
import com.bjsasc.avidm.core.transfer.TransferObject;
import com.bjsasc.avidm.core.transfer.util.TransferConstant;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.collaboration.a4x.AbstractMessageConvert;
import com.bjsasc.plm.collaboration.a4x.MessageConvert;
import com.bjsasc.plm.collaboration.a4x.model.DataMap;
import com.bjsasc.plm.collaboration.a4x.model.DmcConvertData;
import com.bjsasc.plm.collaboration.a4x.model.ObjectMessage;
import com.bjsasc.plm.collaboration.a4x.model.StoreFile;
import com.bjsasc.plm.collaboration.a4x.model.StoreObject;
import com.bjsasc.plm.core.part.Part;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.ddm.common.A3A5DataConvertUtil;
import com.bjsasc.ddm.common.MessageCreateUtil;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;

/**
 * 分发请求消息
 * 
 * @author zhangguoqiang
 * 
 */
public class A3A5DistributeMessageConvertImpl extends AbstractMessageConvert
		implements MessageConvert {
	
	private static Logger logger = Logger.getLogger(A3A5DistributeMessageConvertImpl.class);

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
		TransferObject object = super.createTransferObject(om.getMsgIID(), "分发请求消息", om.getMsgType(), "", null, sourceSite, targetSite, TransferConstant.REQTYPE_ASYN, reqParamMap);
		transferObjList.add(object);
		return transferObjList;
	}

	/**
	 * 将接收到的内容转换为A3用的DataMap数据
	 * @param dataMap
	 * @param sourceSite
	 * @param targetSite
	 * @return List<DataMap>
	 */
	private List<DataMap> getDataMapList(Map<String, String> dataMap, Site sourceSite, Site targetSite) {
		String orderIID = (String)dataMap.get("orderIID");
		String orderObjLinkOids = (String)dataMap.get("orderObjLinkOids");
		String disObjOids = (String)dataMap.get("disObjOids");
		String infoOids = (String)dataMap.get("infoOids");
		String dispatchFile = (String)dataMap.get("dispatchFile");

		DistributeOrder order = (DistributeOrder) Helper.getPersistService().getObject(DistributeOrder.class.getSimpleName(),orderIID);
		
		String linkSql = "select * from DDM_DIS_ORDEROBJLINK "
				+ " where classId || ':' || innerId"
				+ " in (" + orderObjLinkOids + ")";
		List<DistributeOrderObjectLink> orderObjLinkList = Helper
				.getPersistService().query(linkSql, DistributeOrderObjectLink.class);
		
		String infoSql = "select * from DDM_DIS_INFO "
				+ " where classId || ':' || innerId"
				+ " in (" + infoOids + ")";
		List<DistributeInfo> infoList = Helper
				.getPersistService().query(infoSql, DistributeInfo.class);
		
		String disObjSql = "select * from DDM_DIS_OBJECT "
				+ " where classId || ':' || innerId"
				+ " in (" + disObjOids + ")";
		List<DistributeObject> disObjList = Helper
				.getPersistService().query(disObjSql,DistributeObject.class);

		return distributeToDataMapList(sourceSite, targetSite, dispatchFile, order, disObjList, orderObjLinkList, infoList );
	}


	/**
	 * @param sourceSite
	 * @param targetSite
	 * @param dispatchFile
	 * @param order
	 * @param disObjList
	 * @param orderObjLinkList
	 * @param infoList
	 * @return List<DataMap>
	 */
	private List<DataMap> distributeToDataMapList(Site sourceSite, Site targetSite, String dispatchFile, DistributeOrder order, List<DistributeObject> disObjList, 
			List<DistributeOrderObjectLink> orderObjLinkList, List<DistributeInfo> infoList) {
		List<DataMap> dataMapList = new ArrayList<DataMap>();
		List<Object> dataObjList = new ArrayList<Object>();
		try {

			DataMap datamap = new DataMap();
			datamap.setClassName(A3A5DataConvertUtil.dataMapClassName);
			datamap.put("dispatchFile", dispatchFile);
			dataMapList.add(datamap);
			
			//处理发放单列表，转换为A3的消息用的DmcObject的DataMap
			dataMapList.add(A3A5DataConvertUtil.makeA3DmcOrderMsgDataMap(order, sourceSite,A3A5DataConvertUtil.TYPE_NO_DISTRIBUTE));
			
			//取得分发数据对象中所有部件的父部件IIDMap
			Map<String, String> partsParentIIDMap = A3A5DataConvertUtil.getPartsParentIIDMap(disObjList);
			
			//处理发放数据列表，转换为A3的消息用的DmcObject的DataMap
			for (int i = 0; i < disObjList.size(); i++) {
				DistributeObject disObj = disObjList.get(i);
				Persistable dataObj = Helper.getPersistService().getObject(disObj.getDataClassId(), disObj.getDataInnerId());
				dataObjList.add(dataObj);
				dataMapList.add(A3A5DataConvertUtil.makeA3DmcObjectMsgDataMap(order, (Object) dataObj, disObj, partsParentIIDMap, targetSite,A3A5DataConvertUtil.TYPE_NO_DISTRIBUTE));
			}
			//处理发放信息列表，转换为A3的消息用的A3DmcOrderTarget的DataMap和A3DmcDispatchMsg的DataMap
			List<String> disInfoIdList = new ArrayList<String>();
			for (DistributeInfo disInfo : infoList) {
				if(!disInfoIdList.contains(disInfo.getDisInfoId())){
					dataMapList.add(A3A5DataConvertUtil.makeA3DmcOrderTargetMsgDataMap(order, disInfo, targetSite));
				}else{
					disInfoIdList.add(disInfo.getDisInfoId());
				}
				dataMapList.add(A3A5DataConvertUtil.makeA3DmcOrderReceiverMsgDataMap(order, disInfo, targetSite));
				//根据叶红军的调查结果部件Part的时候不需要这个DmcDispatch消息
				Persistable a5persist = A3A5DataConvertUtil.getDisObjectByDisInfo(orderObjLinkList,dataObjList,disInfo);
				if(!(a5persist instanceof Part)){
					dataMapList.add(A3A5DataConvertUtil.makeA3DmcDispatchMsgDataMap(order, orderObjLinkList, dataObjList, disInfo, targetSite));
				}
			}
		} catch (Exception e) {
			logger.error("A3A5DistributeMessageConvertImpl对象转换为DataMap失败",e);
			throw new RuntimeException("A3A5DistributeMessageConvertImpl对象转换为DataMap失败",e);
		}

		return dataMapList;
	}
	
	 /**
	 * @param dataList
	 * @return Map<String, String>
	 */
	public static Map<String, String> makeMessageMap(List<DataMap> dataList){
			Map<String, String> map = new HashMap<String, String>();
			DataMap dmcOrder = new DataMap();
			DataMap dmcOrderTarget = new DataMap();
			List<DataMap> dmcOrderReceiverList = new ArrayList<DataMap>();
			List<DataMap> dmcObjectList = new ArrayList<DataMap>();
			List<DataMap> dmcDispatchList = new ArrayList<DataMap>();
				
			for(Iterator<DataMap> iter =dataList.iterator();iter.hasNext();){
				DataMap dataMap =(DataMap)iter.next();
				if(dataMap.getClassName().equals(A3A5DataConvertUtil.orderClassName)){
					dmcOrder = dataMap;
				}else if(dataMap.getClassName().equals(A3A5DataConvertUtil.orderTargetClassName)){
					dmcOrderTarget = dataMap;
				}else if(dataMap.getClassName().equals(A3A5DataConvertUtil.orderReceiverClassName)){
					dmcOrderReceiverList.add(dataMap);
				}else if(dataMap.getClassName().equals(A3A5DataConvertUtil.dmcObjectClassName)){
					dmcObjectList.add(dataMap);
				}else if(dataMap.getClassName().equals(A3A5DataConvertUtil.dmcDispatchClassName)){
					dmcDispatchList.add(dataMap);
				}else if(dataMap.getClassName().equals(A3A5DataConvertUtil.dataMapClassName)){
					map.put("dispatchFile", dataMap.get("dispatchFile"));
				}
			}
			try {
				map.put("dmcOrder", A3A5DataConvertUtil.dataMapToJson(dmcOrder));
				map.put("dmcOrderTarget", A3A5DataConvertUtil.dataMapToJson(dmcOrderTarget));
				map.put("dmcOrderReceiverList", A3A5DataConvertUtil.listDataMapToJson(dmcOrderReceiverList));
				map.put("dmcObjectList", A3A5DataConvertUtil.listDataMapToJson(dmcObjectList));
				map.put("dmcDispatchList", A3A5DataConvertUtil.listDataMapToJson(dmcDispatchList));
			} catch (Exception e) {
				logger.error("A3A5DistributeMessageConvertImpl对象转换为reqParamMap失败",e);
				throw new RuntimeException("A3A5DistributeMessageConvertImpl对象转换为reqParamMap失败",e);
			}
			return map;
	 }
}
