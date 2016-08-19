package com.bjsasc.adm.active.service.activeorderservice;

import java.util.List;
import java.util.Map;

import com.bjsasc.adm.active.model.ActiveObjOrder;
import com.bjsasc.adm.active.model.ActiveOrdered;
import com.bjsasc.adm.active.model.activeorder.ActiveOrder;
import com.bjsasc.adm.active.model.activeorder.ActiveOrderMaster;
import com.bjsasc.adm.active.model.activeorderlink.ActiveOrderLink;
import com.bjsasc.plm.core.persist.model.Persistable;

/**
 * 现行单据服务接口。
 * 
 * @author yanjia 2013-6-3
 */
public interface ActiveOrderService {
	
	/**
	 * 初始化现行单据对象
	 * 
	 * @param classId String
	 * @return ActiveOrder
	 */
	public ActiveOrder newActiveOrder(String classId);
	
	/**
	 * 初始化现行单据对象,指定初始状态
	 * 
	 * @param classId String
	 * @param checkoutState String
	 * @return ActiveOrder
	 */
	public ActiveOrder newActiveOrder(String classId, String checkoutState);

	/**
	 * 创建现行单据主对象
	 * @return String
	 */
	public String createActiveOrderMaster(String number, String name, String secLevel);

	/**
	 * 创建现行单据
	 * @return String
	 */
	public String createActiveOrder(Map<String, String> paramMap);

	/**
	 * 删除现行单据
	 * @return String
	 */
	public String deleteActiveOrder(String oids);

	/**
	 * 更新现行单据
	 * @return String
	 */
	public void updataActiveOrder(Map<String, String> paramMap);

	/**
	 * 更新现行单据
	 * @return String
	 */	
	public void updataActiveOrder(ActiveOrder obj);

	/**
	 * 更新现行单据主对象
	 * @return String
	 */
	public void updataActiveOrderMaster(ActiveOrderMaster masterObj);

	/**
	 * 是否是现行单据数据源
	 * 
	 * @return boolean
	 */
	public boolean isActiveOrdered(Persistable object);

	/**
	 * 删除单据图与现行文件关联Link
	 * 
	 * @return String
	 */
	public void deleteActiveOrderLink(String oid);

	/**
	 * 取得单据与现行文件关联Link
	 * 
	 * @return List<ActiveOrderLink>
	 */
	public List<ActiveOrderLink> getActiveOrderLinkByObject(String objectOid);

	/**
	 * 取得单据与现行文件关联Link-改前对象
	 * 
	 * @return List<ActiveOrderLink>
	 */
	public List<ActiveOrderLink> getActiveOrderLinkByBeforeObject(String objectOid);

	/**
	 * 取得单据与现行文件关联Link-改后对象
	 * 
	 * @return List<ActiveOrderLink>
	 */
	public List<ActiveOrderLink> getActiveOrderLinkByAfterObject(String objectOid);

	/**
	 * 删除单据与现行文件关联Link-改前对象
	 * 
	 * @return List<ActiveOrderLink>
	 */
	public void deleteActiveOrderLinkByBeforeObject(ActiveOrderLink activeLink);

	/**
	 * 删除单据与现行文件关联Link-改后对象
	 * 
	 * @return List<ActiveOrderLink>
	 */
	public void deleteActiveOrderLinkByAfterObject(ActiveOrderLink activeLink);

	/**
	 * 添加改后对象
	 * 
	 */
	public void addAfterItem(ActiveObjOrder activeOrder, ActiveOrdered activeOrdered);

	/**
	 * 添加改后对象
	 * 
	 */
	public void addAfterItems(ActiveObjOrder activeOrder, List<ActiveOrdered> activeOrdereds);

	/**
	 * 根据改前对象Link添加改后对象
	 * 
	 */
	public void addAfterItemByBeforeItem(ActiveOrderLink link, ActiveOrdered activeOrdered);

	/**
	 * 根据改前对象取得Link
	 * 
	 */
	public ActiveOrderLink getLinkByBeforeItem(ActiveObjOrder activeOrder, ActiveOrdered activeOrdered);

	/**
	 * 添加改前对象
	 * 
	 */
	public void addBeforeItem(ActiveObjOrder activeOrder, ActiveOrdered activeOrdered);

	/**
	 * 添加改前对象
	 * 
	 */
	public void addBeforeItems(ActiveObjOrder activeOrder, List<ActiveOrdered> activeOrdereds);

	/**
	 * 查询改后对象
	 * 
	 * @return  List<ActiveOrdered>
	 */
	public List<ActiveOrderLink> getActiveOrderLinks(String activeOrderOid);

	/**
	 * 查询改后对象
	 * 
	 * @return  List<ActiveOrdered>
	 */
	public List<ActiveOrdered> getAfterItems(ActiveObjOrder activeOrder);
	
	/**
	 * 查询改前对象
	 * 
	 * @return  List<ActiveOrdered>
	 */
	public List<ActiveOrdered> getBeforeItems(String activeOrderOid);

	/**
	 * 查询改前对象依据改后对象和现行单据
	 * 
	 * @return  List<ActiveOrdered>
	 */
	List<ActiveOrdered> getBeforeItemsByOrderAndAfterItem(String activeOrderOid, String objectOid);

	/**
	 * 取得现行单据对象LIST
	 * @return List<ActiveOrder>
	 * */
	public List<ActiveOrder> getActiveOrderByNumber(String number);
	
	/**
	 * 修改现行单据对象
	 */
	public void updataActiveOrderObject(String oid, String beforeOids, String afterOids);
}
