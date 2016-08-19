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
import com.bjsasc.plm.collaboration.a4x.AbstractMessageConvert;
import com.bjsasc.plm.collaboration.a4x.MessageConvert;
import com.bjsasc.plm.collaboration.a4x.model.DataMap;
import com.bjsasc.plm.collaboration.a4x.model.DmcConvertData;
import com.bjsasc.plm.collaboration.a4x.model.ObjectMessage;
import com.bjsasc.plm.collaboration.a4x.model.StoreFile;
import com.bjsasc.plm.collaboration.a4x.model.StoreObject;
import com.bjsasc.ddm.common.A3A5DataConvertUtil;

/**
 * 中心电子分发任务发起
 * 
 * @author zhangguoqiang
 * 
 */
public class A3A5StartDCDispatchMessageConvertImpl extends AbstractMessageConvert
		implements MessageConvert {
	
	private static Logger logger = Logger.getLogger(A3A5StartDCDispatchMessageConvertImpl.class);

	@Override
	public List<DmcConvertData> convertTo4XMessage(TransferObject obj) {
		List<DmcConvertData> datas = new ArrayList<DmcConvertData>();
		//TODO 添加A5到A3的消息数据转换的代码 现有逻辑用不到
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
		TransferObject object = super.createTransferObject(om.getMsgIID(), "中心电子分发任务发起", om.getMsgType(), "", null, sourceSite, targetSite, TransferConstant.REQTYPE_ASYN, reqParamMap);
		transferObjList.add(object);
		return transferObjList;
	}

	 /**
	 * @param dataList
	 * @return Map<String, String>
	 */
	public static Map<String, String> makeMessageMap(List<DataMap> dataList){
			Map<String, String> map = new HashMap<String, String>();
			DataMap dmcOrder = new DataMap();
			DataMap dmcOrderTarget = new DataMap();
			List<DataMap> dmcObjectList = new ArrayList<DataMap>();
			List<DataMap> dmcDispatchList = new ArrayList<DataMap>();
				
			for(Iterator<DataMap> iter =dataList.iterator();iter.hasNext();){
				DataMap dataMap =(DataMap)iter.next();
				if(dataMap.getClassName().equals(A3A5DataConvertUtil.orderClassName)){
					dmcOrder = dataMap;
				}else if(dataMap.getClassName().equals(A3A5DataConvertUtil.orderTargetClassName)){
					dmcOrderTarget =dataMap;
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
				map.put("dmcObjectList", A3A5DataConvertUtil.listDataMapToJson(dmcObjectList));
				map.put("dmcDispatchList", A3A5DataConvertUtil.listDataMapToJson(dmcDispatchList));
			} catch (Exception e) {
				logger.error("A3A5StartDCDispatchMessageConvertImpl对象转换为reqParamMap失败",e);
				throw new RuntimeException("A3A5StartDCDispatchMessageConvertImpl对象转换为reqParamMap失败",e);
			}
			return map;
	 }
}
