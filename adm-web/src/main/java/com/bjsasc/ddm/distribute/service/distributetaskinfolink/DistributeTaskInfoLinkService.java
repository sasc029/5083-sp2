package com.bjsasc.ddm.distribute.service.distributetaskinfolink;

import com.bjsasc.ddm.distribute.model.distributetaskinfolink.DistributeTaskInfoLink;

/**
 *  ������ַ���Ϣlink����ӿڡ�
 * 
 * @author gengancong 2013-2-22
 */
public interface DistributeTaskInfoLinkService {

	/**
	 * ����������ַ���Ϣlink����
	 * 
	 * @param disObj DistributeTaskInfoLink
	 */
	public void createDistributeTaskInfoLink(DistributeTaskInfoLink disObj);

	/**
	 * ����������ַ���Ϣlink����
	 * 
	 * @return DistributeTaskInfoLink
	 */
	public DistributeTaskInfoLink newDistributeTaskInfoLink();
}
