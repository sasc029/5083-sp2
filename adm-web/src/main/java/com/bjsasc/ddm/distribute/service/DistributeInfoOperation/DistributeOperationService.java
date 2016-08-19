package com.bjsasc.ddm.distribute.service.DistributeInfoOperation;

import java.util.List;

import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
/**
 * 
 * @author kangyanfei 2014-07-15
 *
 */
public interface DistributeOperationService {

	/**
	 * 根据发放单查询分发数据集
	 * @param disOrder
	 * 			发放单oid
	 * @return	List<DistributeObject> 
	 * 			分发对象
	 */
	public List<DistributeObject> getDistributeObjectsByDisOrder(DistributeOrder disOrder);

	/**
	 * 批量插入分发信息
	 * @param disOrder
	 * 			发放单
	 * @param disObj
	 * 			分发数据
	 * @param disInfos
	 * 			分发信息集
	 */
	public void inserDisInfos(DistributeOrder disOrder,DistributeObject disObj, List<DistributeInfo> disInfos);
}
