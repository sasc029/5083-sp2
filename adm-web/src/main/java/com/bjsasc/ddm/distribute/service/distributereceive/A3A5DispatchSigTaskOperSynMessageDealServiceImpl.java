package com.bjsasc.ddm.distribute.service.distributereceive;

import java.util.List;
import java.util.Map;

import com.bjsasc.avidm.core.site.Site;
import com.bjsasc.avidm.core.site.SiteHelper;
import com.bjsasc.avidm.core.transfer.event.TransferEvent;
import com.bjsasc.ddm.common.A3A5DataConvertUtil;
import com.bjsasc.plm.collaboration.adapter.base.MessageDealService;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class A3A5DispatchSigTaskOperSynMessageDealServiceImpl implements MessageDealService {

	public void dealEvent(TransferEvent event) {
		synchronized (A3A5DispatchSigTaskOperSynMessageDealServiceImpl.class){
			try {
				Site selfSite = SiteHelper.getSiteService().findLocalSiteInfo();
				String selfSiteInnerId = selfSite.getInnerId();
				String targetSiteInnerId = event.getTargetSite().getInnerId();
				//如果是目标站点
				if (selfSiteInnerId.equals(targetSiteInnerId)) {

					Map dataMap = A3A5DataConvertUtil.mapToDatamap(event.getReqParamMap());
					
					String orderIID = (String) dataMap.get(A3A5DataConvertUtil.ORDERIID);
					String domainIID = (String) dataMap.get(A3A5DataConvertUtil.DOMAINIID);
					List taskList = (List)dataMap.get("taskList"); //任务集合
					List signInfoList = (List)dataMap.get("signInfoList"); //签署信息集合
					
					A3A5DataConvertUtil.dealA3ReplyMessage(orderIID,domainIID,taskList,signInfoList);
				} else {
					//由于在A3A5的跨版本分发的业务中A5不可能作为中心，所以这个逻辑分支不可能到达
				}
			} catch (Exception e) {
				throw new RuntimeException("A3A5DispatchSigTaskOperSynMessageDealServiceImpl类中，消息反馈处理失败！", e);
			}
		}
	}
}
