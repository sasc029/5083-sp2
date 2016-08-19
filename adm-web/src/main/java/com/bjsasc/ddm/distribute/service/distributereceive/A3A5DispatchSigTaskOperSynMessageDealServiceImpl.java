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
				//�����Ŀ��վ��
				if (selfSiteInnerId.equals(targetSiteInnerId)) {

					Map dataMap = A3A5DataConvertUtil.mapToDatamap(event.getReqParamMap());
					
					String orderIID = (String) dataMap.get(A3A5DataConvertUtil.ORDERIID);
					String domainIID = (String) dataMap.get(A3A5DataConvertUtil.DOMAINIID);
					List taskList = (List)dataMap.get("taskList"); //���񼯺�
					List signInfoList = (List)dataMap.get("signInfoList"); //ǩ����Ϣ����
					
					A3A5DataConvertUtil.dealA3ReplyMessage(orderIID,domainIID,taskList,signInfoList);
				} else {
					//������A3A5�Ŀ�汾�ַ���ҵ����A5��������Ϊ���ģ���������߼���֧�����ܵ���
				}
			} catch (Exception e) {
				throw new RuntimeException("A3A5DispatchSigTaskOperSynMessageDealServiceImpl���У���Ϣ��������ʧ�ܣ�", e);
			}
		}
	}
}
