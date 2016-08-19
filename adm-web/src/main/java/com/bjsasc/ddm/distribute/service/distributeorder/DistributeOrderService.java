package com.bjsasc.ddm.distribute.service.distributeorder;

import java.util.List;

import com.bjsasc.ddm.common.ConstUtil.LIFECYCLE_OPT;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.plm.core.persist.model.Persistable;

/**
 * 分发单服务接口。
 * 
 * @author gengancong 2013-2-22
 */
public interface DistributeOrderService {

	/**
	 * 获取所有发放单对象
	 * 
	 * @param stateName
	 * @return
	 */
	public List<DistributeOrder> getAllDistributeOrderByAuth(String stateName);
	/**
	 * 获取一种或多种状态下的发放单
	 * 
	 * @param stateArray
	 * @return
	 */
	public List<DistributeOrder> listDiffStatesDistributeOrder(String [] stateArray);
	/**
	 * 发放单已分发页面点击搜索按钮调用
	 * 
	 * @param stateArray
	 * @param keyWords(页面搜索框关键字)
	 * @return
	 */
	public List<DistributeOrder> listSendedDistributeOrderAdderSearchIuput(String [] stateArray,String keyWords);
	/**
	 * 发放单已分发页面第一次加载页面显示当前月的数据
	 * 
	 * @param stateArray
	 * @param time（当前时间的一个月前的时间）
	 * @param currentTime（当前时间）
	 * @return
	 */
	public List<DistributeOrder> listDiffStatesDistributeOrderOnload(String [] stateArray,long time,long currentTime);
	/**
	 * 获取一种或多种状态以外状态的发放单
	 * 
	 * @param notInStateArray
	 * @return
	 */
	public List<DistributeOrder> listNotInStatesDistributeOrder(String [] notInStateArray);

	/**
	 * 取得相关发放单。
	 * 
	 * @param oid Object
	 * @return List
	 */
	public List<DistributeOrder> getRelatedDistributeOrder(Object oid);
	
	
	/**
	 * 取得与纸质任务相关的分发单
	 * 
	 * @param oid
	 * @return 分发单列表
	 * @modify Sun Zongqing
	 * @modifyDate 2014/7/2
	 */
	public List<DistributeOrder> getRelatetaskdDistributeOrder(String oid);
	

	/**
	 * 获取所有被回退发放单对象
	 * 
	 * @param stateName
	 * @return
	 */
	public List<DistributeOrder> getAllDistributeOrderReturnByAuth(String stateName);

	/**
	 * 获取回退信息。
	 * 
	 * @param stateName
	 * @return
	 */
	public List<DistributeOrder> getDistributeOrderReturnDetail(String stateName, String oid);

	/**
	 * 创建发放单对象。
	 * 
	 * @param oid String
	 */
	public DistributeOrder createDistributeOrderAndObject(String number, String name, String orderType, String note,
			String oid, boolean aotuCreateFlag, String starterId);
	
	/**
	 * 文件夹"更多"操作中批量发放专用的创建发放单对象方法。
	 * 
	 * @param oid String
	 */
	public DistributeOrder createDistributeOrderAndObjectMulti(String number,String name, String orderType, String note, 
			String oid, boolean aotuCreateFlag,String starterId);

	/**
	 * 取得发放单与分发数据link对象。
	 * 
	 * @param distributeOrderOids List
	 * @return List
	 */
	public List<DistributeOrderObjectLink> getDistributeOrderObjectLinksByOids(List<String> distributeOrderOids);

	/**
	 * 判断是否为可以创建发放单的对象。
	 * 
	 * @param  target
	 * @return true:可以;false:不可以
	 */
	public boolean isCanCreateDistributeOrder(Persistable target);
	
	/**
	 * 创建发放单对象。
	 * 
	 * @param dis DistributeOrder
	 */
	public void createDistributeOrder(DistributeOrder dis);

	/**
	 * 创建发放单对象。
	 * 
	 * @param id String
	 * @param name String
	 * @param orderType String
	 * @param note String
	 */
	public void createDistributeOrder(String number, String name, String orderType, String note);

	/**
	 * 更新发放单对象。
	 * 
	 * @param dis DistributeOrder
	 */
	public void updateDistributeOrder(DistributeOrder dis);

	/**
	 * 更新发放单对象。
	 * 
	 * @param oid String
	 * @param number String
	 * @param name String
	 * @param orderType String
	 * @param note String
	 */
	public void updateDistributeOrder(String oid, String name, String note);

	/**
	 * 创建发放单对象。
	 * 
	 * @return DistributeOrder
	 */
	public DistributeOrder newDistributeOrder();
	/**
	 * 删除发放单对象。
	 * 
	 * @return DistributeOrder
	 */
	public void deleteDistributeOrderByOid(String distributeOrderOids);

	/**
	 * 修改发放单生命周期。
	 * 
	 * @return DistributeOrder
	 */
	public void updateDisOrderLifeCycle(String oids, LIFECYCLE_OPT opt, String returnReason);

	/**
	 * 发放单生命周期升级
	 * 
	 * @param oid
	 */
	public String updateOrderLifeCycle(String oid);
	
	/**
	 * 取得是否存在电子任务
	 * 
	 * @param orderOid
	 * @return
	 */
	public String getExistDistributeElecTask(String orderOid);

	/**
	 * 根据发放单OID,状态和数据源OID取得发放单
	 * 
	 * @param orderOid
	 * @param dataOid
	 * @return List<DistributeOrder>
	 */
	public List<DistributeOrder> getDistributeOrderByOrdStateAndDataOid(String orderOid,String dataOid);

	/**
	 * 直接入库创建发放单对象。
	 * 
	 * @param oid String
	 */
	public DistributeOrder createDisOrderAndObject(String number, String name, String orderType, String note,
			String oid, boolean aotuCreateFlag);
	
	/**
	 * 补发分发单-选中其他分发单直接进行补发
	 * @param orderOidsStr
	 * @param number
	 * @param name
	 * @param orderType
	 * @param note
	 */
	public DistributeOrder  reissueDistributeOrder (String  orderOidsStr,String number, String name, String orderType, String note);
	
	/**
	 * 取得分发单的默认分发数据类型
	 * 
	 * @param oid 发放单OID
	 * @return 默认分发数据类型
	 */
	public String getDefaultDistributeOrderType(String oid);
	
	/**
	 * 批量往发放单中添加分发数据
	 * @param order
	 * @param disObjOidList
	 */
	public void addDisObjectToDisOrder(DistributeOrder order, List<String> disObjOidList);
}
