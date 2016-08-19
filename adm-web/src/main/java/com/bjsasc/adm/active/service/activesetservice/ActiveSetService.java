package com.bjsasc.adm.active.service.activesetservice;

import java.util.List;
import java.util.Map;

import com.bjsasc.adm.active.model.ActiveObjSet;
import com.bjsasc.adm.active.model.ActiveSeted;
import com.bjsasc.adm.active.model.activeset.ActiveSet;
import com.bjsasc.adm.active.model.activeset.ActiveSetMaster;
import com.bjsasc.adm.active.model.activesetlink.ActiveSetLink;
import com.bjsasc.plm.core.persist.model.Persistable;

/**
 * 现行套服务接口。
 * 
 * @author yanjia 2013-5-13
 */
public interface ActiveSetService {
	
	/**
	 * 初始化现行套对象
	 * 
	 * @param classId String
	 * @return ActiveSet
	 */
	public ActiveSet newActiveSet(String classId);
	
	/**
	 * 初始化现行套对象,指定初始状态
	 * 
	 * @param classId String
	 * @param checkoutState String
	 * @return ActiveSet
	 */
	public ActiveSet newActiveSet(String classId, String checkoutState);
	
	/**
	 * 取得所有现行套
	 * 
	 * @return List<ActiveSet>
	 * */
	public List<ActiveSet> getAllActiveSet();

	/**
	 * 创建现行套主对象
	 * 
	 * @return String
	 */
	public String createActiveSetMaster(String number, String name, String secLevel);

	/**
	 * 创建现行套
	 * 
	 * @return String
	 */
	public String createActiveSet(Map<String, String> paramMap);

	/**
	 * 删除现行套
	 * 
	 * @return String
	 */
	public String deleteActiveSet(String oids);

	/**
	 * 更新现行套
	 * 
	 * @return String
	 */
	public String updataActiveSet(Map<String, String> paramMap);

	/**
	 * 更新现行套
	 * 
	 * @return String
	 */
	public String updataActiveSet(ActiveSet obj);

	/**
	 * 更新现行套主对象
	 * 
	 * @return String
	 */
	public String updataActiveSetMaster(ActiveSetMaster masterObj);

	/**
	 * 取得所有文件主对象
	 * 
	 * @return List<ActiveSetMaster>
	 * */
	public List<ActiveSetMaster> getAllActiveSetMaster();

	/**
	 * 删除套图与现行文件关联Link
	 * 
	 * @return String
	 */
	public String deleteActiveSetLink(String oid);

	/**
	 * 取得套图与现行文件关联Link
	 * 
	 * @return List<ActiveSetLink>
	 */
	public List<ActiveSetLink> getActiveSetLinkByOID(String oids);

	/**
	 * 删除套图与现行文件关联Link
	 * 
	 * @return String
	 */
	public String deleteActiveSetLink(String setOid, String objectOid);

	/**
	 * 取得套图与现行文件关联Link
	 * 
	 * @return List<ActiveSetLink>
	 */
	public List<ActiveSetLink> getActiveSetLinkByObject(String objectOid);

	/**
	 * 是否是现行套数据源
	 * 
	 * @return boolean
	 */
	public boolean isActiveSeted(Persistable object);

	/**
	 * 添加对象到套中
	 * 
	 * */
	public void addToActiveSet(ActiveObjSet activeSet, ActiveSeted activeSeted);

	/**
	 * 添加对象到套中
	 * 
	 * */
	public void addToActiveSet(ActiveObjSet activeSet, List<ActiveSeted> activeSeteds);

	/**
	 * 取得套中对象
	 * 
	 * @return  List<ActiveSeted>
	 */
	public List<ActiveSeted> getActiveItems(ActiveObjSet activeSet);

	/**
	 * 从套图中批量移除对象
	 * 
	 */
	public void removeFromActiveSet(ActiveObjSet activeSet, ActiveSeted activeSeted);

	/**
	 * 从套图中批量移除对象
	 * 
	 */
	public void removeFromActiveSet(ActiveObjSet activeSet, List<ActiveSeted> activeSeteds);

	/**
	 * 从套图中移除LINK对象
	 * 
	 */
	public void removeActiveSetedLink(ActiveSetLink activeSetedLink);

	/**
	 * 删除相关LINK
	 * 
	 */
	public void deleteLink(String oid);

	/**
	 * 取得现行套对象LIST
	 * @return List<ActiveSet>
	 * */
	public List<ActiveSet> getActiveSetByNumber(String number);
}
