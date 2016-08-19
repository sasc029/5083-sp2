package com.bjsasc.ddm.distribute.service.recdesinfo;

import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.recdesinfo.RecDesInfo;

/**
 * 回收销毁发放单服务接口。
 * 
 * @author kangyanfei 2014-5-28
 */
public interface RecDesInfoService {

	/**
	 * 通过发放单与分发数据link列表获取回收销毁信息数据。
	 * 
	 * @param List<DistributeOrderObjectLink> listOrderObjectLink
	 * @return List
	 */
	public List<RecDesInfo> getRecDesInfosByOrderObjectLinkList(List<DistributeOrderObjectLink> listOrderObjectLink);
	
	/**
	 * 通过发放单ID获取回收/销毁分发信息数据。
	 * 
	 * @param innerId String
	 * @return List
	 */
	public List<RecDesInfo> getRecDesInfosByDistributeOrderInnerId(String innerId);
	
	/**
	 * 取得所有回收单
	 * 
	 * @param distributeOid
	 *			发放单唯一标示（innerID+ClassID）
	 * @param List<String> linkOidList
	 *			发放数据LINK
	 *
	 * @return 所有回收单
	 */
	public List<RecDesInfo> getAllRecInfo(String distributeOid, List<String> linkOidList);

	/**
	 * 取得所有销毁单
	 * 
	 * @param distributeOid
	 * 			发放单唯一标示（innerID+ClassID） 
	 * @param List<String> linkOidList
	 * 			发放数据LINK
	 *
	  *@return 所有销毁单
	 */
	public List<RecDesInfo> getAllDesInfo(String distributeOid, List<String> linkOidList);

	/**
	 * 添加回收单
	 * 
	 * @param orderOidsStr
	 *			发放单唯一标示（innerID+ClassID）
	 * @param number
	 *			 编号
	 * @param name
	 *			名称
	 * @param orderType
	 *			单据类型（0发放单，1补发发放单，2回收单，3销毁单）
	 * @param note
	 *			备注
	 * @return DistributeOrder
	 *			新创建的回收单
	 */
	public DistributeOrder addResDistributeOrder(String orderOidsStr,
			String number, String name, String orderType, String note);

	/**
	 * 添加销毁单
	 * 
	 * @param orderOidsStr
	 *			发放单唯一标示（innerID+ClassID）
	 * @param number
	 *			编号
	 * @param name
	 *			名称
	 * @param orderType
	 *			单据类型（0发放单，1补发发放单，2回收单，3销毁单）
	 * @param note
	 *			备注
	 * @return DistributeOrder
	 *			新创建的销毁单
	 */
	public DistributeOrder addDesDistributeOrder(String orderOidsStr,
			String number, String name, String orderType, String note);

	/**
	 * 取得发放单信息
	 * @param distributeOrderOid
	 * 			发放单OID
	 * @param linkOidList
	 * 			Link的OIDs集合
	 * @return 需要回收的发放单信息
	 */
	public List<Map<String, Object>> getAllNeddRecInfosByDistributeOrderOid(String distributeOrderOid, List<String> linkOidList);

	/**
	 * 取得发放单信息
	 * @param distributeOrderOid
	 * 			发放单OID
	 * @param linkOidList
	 * 			Link的OIDs集合
	 * @return 需要销毁的发放单信息
	 */
	public List<Map<String, Object>> getAllNeddDesInfosByDistributeOrderOid(String distributeOrderOid, List<String> linkOidList);

	/**
	 * 添加回收信息
	 * 
	 * @param disInfoNames
	 *			分发信息名称（单位/人员）
	 * @param disInfoIds
	 *			分发信息IID（人员或组织的内部标识）
	 * @param disOrderObjLinkIds
	 *			发放单与分发数据LINK内部标识
	 * @param disOrderObjLinkClassIds
	 *			发放单与分发数据LINK类标识
	 * @param disMediaTypes
	 *			分发介质类型（0为纸质，1为电子，2为跨域）
	 * @param disInfoNums
	 *			分发份数	
	 * @param needRecoverNums
	 *			需要回收的份数
	 * @param recoverNums
	 *			已回收的份数
	 * @param distributeOrderOid
	 *			回收单的OID
	 * @param notes
	 *			备注
	 * @param disInfoTypes
	 *			分发信息类型（0为单位，1为人员）
	 * @return String
	 *			错误消息
	 */
	public String addNeddRecInfos(String disInfoNames, String disInfoIds,
			String disOrderObjLinkIds,
			String disOrderObjLinkClassIds, String disMediaTypes,
			String disInfoNums, String needRecoverNums, String recoverNums, 
			String distributeOrderOid, String notes, String disInfoTypes);	

	/**
	 * 添加回收信息
	 * 
	 * @param disInfoNames
	 *			分发信息名称（单位/人员）
	 * @param disInfoIds
	 *			分发信息IID（人员或组织的内部标识）
	 * @param disOrderObjLinkIds
	 *			发放单与分发数据LINK内部标识
	 * @param disOrderObjLinkClassIds
	 *			发放单与分发数据LINK类标识
	 * @param disMediaTypes
	 *			分发介质类型（0为纸质，1为电子，2为跨域）
	 * @param disInfoNums
	 *			分发份数
	 * @param needDestroyNums
	 *			需要销毁的份数
	 * @param destroyNums
	 *			已经销毁份数
	 * @param distributeOrderOid
	 *			销毁的OID
	 * @param recoverNums
	 *			回收份数
	 * @param notes
	 *			备注
	 * @param disInfoTypes
	 *			分发信息类型（0为单位，1为人员）
	 * @return String
	 *			错误消息
	 */
	public String addNeddDesInfos(String disInfoNames, String disInfoIds,
			String disOrderObjLinkIds,
			String disOrderObjLinkClassIds, String disMediaTypes,
			String disInfoNums, String needDestroyNums, String destroyNums,String distributeOrderOid,
			String recoverNums, String notes, String disInfoTypes);

	/**
	 * 查询回收销毁信息
	 * 
	 * @param oid
	 *			回收信息OID
	 * @return ist<RecDesInfo>
	 *			回收销毁信息
	 */
	public List<RecDesInfo> getRecInfosForEditByOId(String oid);

	/**
	 * 查询回收销毁信息
	 * 
	 * @param oid
	 *			销毁信息OID
	 * @return List<RecDesInfo>
	 *			回收销毁信息
	 */
	public List<RecDesInfo> getDesInfosForEditByOId(String oid);

	/**
	 * 更新回收信息
	 * 
	 * @param recInfoOids
	 *			回收信息OID
	 * @param needRecoverNums
	 *			需要回收份数
	 * @param notes
	 *			备注
	 * @param dismediatypes
	 *			分发类型
	 */
	public void updateRecInfos(String recInfoOids, String needRecoverNums,
			String notes, String dismediatypes);

	/**
	 * 更新回收信息
	 * 
	 * @param desInfoOids
	 *			销毁信息OID
	 * @param needDestroyNums
	 *			需要销毁份数
	 * @param notes
	 *			备注
	 * @param dismediatypes
	 *			分发类型
	 */
	public void updateDesInfos(String desInfoOids, String needDestroyNums,
			String notes, String dismediatypes);

	/**
	 * 
	 * @param oids
	 *			回收销毁信息ClassID：InnerID
	 */
	public void deleteRecDesInfos(String oids);

	/**
	 * 
	 * @param oid
	 *			已分发的发放单OID
	 * @return List<Map<String, Object>>
	 *			未完成的回收发放单信息
	 */
	public List<Map<String, Object>> checkRecDistributeOrder(String oid);

	/**
	 * 
	 * @param oid
	 *			已分发的发放单OID
	 * @return List<Map<String, Object>>
	 *			未完成的销毁发放单信息
	 */
	public List<Map<String, Object>> checkDesDistributeOrder(String oid);

	/**
	 * 根据给定的orderObjectLinkOid和回收销毁纸质任务的OID，获取其相关的分发信息
	 * 
	 * @param orderObjectLinkOids 分发单和分发数据的链接
	 * @param taskOid 纸质回收销毁任务的oid
	 * @return 相关的回收销毁信息列表
	 * 
	 * @author Sun Zongqing
	 * @date 2014/7/2
	 */
	public List<RecDesInfo> getpapertaskinfo(String orderObjectLinkOids, String taskOid);


	/**
	 * 删除回收销毁信息
	 * 
	 * @param DistributeOrderObjectLink
	 *			分发数据link 
	 */
	public void deleteDistributeRecDesInfoByOid(DistributeOrderObjectLink linkd);

}
