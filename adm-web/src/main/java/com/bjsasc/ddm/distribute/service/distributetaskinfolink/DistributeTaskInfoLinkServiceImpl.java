package com.bjsasc.ddm.distribute.service.distributetaskinfolink;

import com.bjsasc.ddm.distribute.model.distributetaskinfolink.DistributeTaskInfoLink;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.Helper;

/**
 * ������ַ���Ϣlink����ʵ���ࡣ
 * 
 * @author gengancong 2013-2-22
 */
@SuppressWarnings({"deprecation","static-access"})
public class DistributeTaskInfoLinkServiceImpl implements DistributeTaskInfoLinkService {

	/* ���� Javadoc��
	 * @see com.bjsasc.ddm.distribute.service.distributetaskinfolink.DistributeTaskInfoLinkService#createDistributeTaskInfoLink(com.bjsasc.ddm.distribute.model.distributetaskinfolink.DistributeTaskInfoLink)
	 */
	@Override
	public void createDistributeTaskInfoLink(DistributeTaskInfoLink disObj) {
		Helper.getPersistService().save(disObj);
	}

	/* ���� Javadoc��
	 * @see com.bjsasc.ddm.distribute.service.distributetaskinfolink.DistributeTaskInfoLinkService#newDistributeTaskInfoLink()
	 */
	@Override
	public DistributeTaskInfoLink newDistributeTaskInfoLink() {
		DistributeTaskInfoLink disObj = (DistributeTaskInfoLink) PersistUtil
				.createObject(DistributeTaskInfoLink.CLASSID);
		disObj.setClassId(disObj.CLASSID);
		return disObj;
	}
}
