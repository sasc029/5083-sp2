package com.bjsasc.ddm.transfer.service;

import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.transfer.exception.DdmTransferException;

/**
 * 发放管理模块对外提供的数据转换接口服务类
 * 
 * @author gaolingjie, 2013-2-20
 */
public interface DdmDataTransferService {

	/**
	 * 把对象（如 文档、部件、更改单、图文档等）转换成分发数据对象
	 * 
	 * @param fromData 要转换的数据
	 * @return 分发对象
	 * @throws DdmTransferException
	 */
	public DistributeObject transferToDdmData(Object fromData) throws DdmTransferException ;

}
