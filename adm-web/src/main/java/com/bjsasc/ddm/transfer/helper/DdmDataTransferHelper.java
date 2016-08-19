package com.bjsasc.ddm.transfer.helper;

import com.bjsasc.ddm.transfer.service.DdmDataTransferService;
import com.bjsasc.plm.core.util.SpringUtil;

/**
 * ����ת�����������
 * @author gaolingjie, 2013-3-1
 */
public class DdmDataTransferHelper {
	
	private DdmDataTransferHelper() {
		
	}

	private static DdmDataTransferService dataTransferService;

	/**
	 * ��ȡ����ת������
	 * 
	 * @return DistributeObjectService
	 */
	public static DdmDataTransferService getDistributeObjectService() {
		if (dataTransferService == null) {
			dataTransferService = (DdmDataTransferService) SpringUtil
					.getBean("ddm_datatransfer_service");
		}
		return dataTransferService;
	}
}
