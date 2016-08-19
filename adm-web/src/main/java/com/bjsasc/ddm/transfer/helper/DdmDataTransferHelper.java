package com.bjsasc.ddm.transfer.helper;

import com.bjsasc.ddm.transfer.service.DdmDataTransferService;
import com.bjsasc.plm.core.util.SpringUtil;

/**
 * 数据转换服务帮助类
 * @author gaolingjie, 2013-3-1
 */
public class DdmDataTransferHelper {
	
	private DdmDataTransferHelper() {
		
	}

	private static DdmDataTransferService dataTransferService;

	/**
	 * 获取数据转换服务
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
