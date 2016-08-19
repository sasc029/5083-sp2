package com.bjsasc.ddm.distribute.service.distributeinfo;

import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.common.ConstUtil.DISPLAY_TYPE;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeinfoconfig.DistributeInfoConfig;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.recdesinfo.RecDesInfo;
import com.bjsasc.plm.core.persist.model.Persistable;

/**
 * 分发信息服务接口。
 * 
 * @author gengancong 2013-2-22
 */
public interface DistributeInfoService {

	/**
	 * 通过发放单ID获取分发信息数据。
	 * 
	 * @param innerId String
	 * @return List
	 */
	public List<DistributeInfo> getDistributeInfosByDistributeOrderInnerId(String innerId);
	
	
	/**
	 * 通过纸质任务的oid获取分发信息数据。
	 * 
	 * @param innerId String
	 * @param classid String
	 * @return List
	 */
	public List<DistributeInfo> getDistributeInfosByDistributePaperTaskOID(String oid);

	/**
	 * 通过发放单OID,分发数据OIDS获取分发信息数据。
	 * 
	 * @param distributeOrderOid String
	 * @param distributeObjectOids List
	 * @param type DISPLAY_TYPE
	 * @return List
	 */
	public List<DistributeInfo> getDistributeInfosByOid(String distributeOrderOid, List<String> distributeObjectOids,
			DISPLAY_TYPE type);

	/**
	 * 通过发放单与分发数据link列表获取分发信息数据。
	 * 
	 * @param List<DistributeOrderObjectLink> listOrderObjectLink
	 * @return List
	 */
	public List<DistributeInfo> getDistributeInfosByOrderObjectLinkList(List<DistributeOrderObjectLink> listOrderObjectLink);

	/**
	 * 通过电子任务OID,分发数据OIDS获取分发信息数据。
	 * 
	 * @param distributeOrderOid String
	 * @param distributeObjectOids List
	 * @param type DISPLAY_TYPE
	 * @return List
	 */
	public List<DistributeInfo> getDistributeInfosByTaskOid(String distributeElecTaskOid, List<String> distributeObjectOids,
			DISPLAY_TYPE type);

	/**
	 * 通过纸质签收任务OID,分发数据OIDS获取分发信息数据。
	 * 
	 * @param distributePaperSignTaskOid String
	 * @param distributeObjectOids List
	 * @param type DISPLAY_TYPE
	 * @return List
	 */
	public List<DistributeInfo> getDistributeInfosByPaperSignTaskOid(String distributePaperSignTaskOid, List<String> distributeObjectOids,
			DISPLAY_TYPE type);

	/**
	 * 通过电子任务OID,分发数据OIDS获取回收销毁信息数据。
	 * 
	 * @param distributeOrderOid String
	 * @param distributeObjectOids List
	 * @param type DISPLAY_TYPE
	 * @return List
	 */
	public List<RecDesInfo> getRecDesInfosByOid(String distributeElecTaskOid, List<String> distributeObjectOids,
			DISPLAY_TYPE type);

	/**
	 * 通过发放单OID,分发数据OID获取分发信息数据。
	 * 
	 * @param distributeOrderOid String
	 * @param distributeObjectOid String
	 * @return List
	 */
	public List<DistributeInfo> getDistributeInfosByOid(String distributeOrderOid, String distributeObjectOid);

	/**
	 * 创建分发信息对象。
	 * 
	 * @param dis DistributeInfo
	 */
	public void createDistributeInfo(DistributeInfo dis);

	/**
	 * 创建分发信息对象。
	 * 
	 * @param type String
	 * @param iids String
	 * @param disMediaTypes String
	 * @param disInfoNums String
	 * @param notes String
	 * @param distributeOrderObjectLinkOids String
	 * @param deleterows String
	 */
	public void createDistributeInfo(String type, String iids, String disMediaTypes, String disInfoNums, String notes,
			String distributeOrderObjectLinkOids, String deleterows);

	/**
	 * 更新分发信息对象。
	 * 
	 * @param oids String
	 * @param disInfoNums String
	 * @param notes String
	 */
	public void updateDistributeInfos(String oids, String disInfoNums, String notes, String dismediatypes);

	/**
	 * 刪除分发信息对象。
	 * 
	 * @param dis DistributeInfo
	 */
	public void deleteDistributeInfo(DistributeInfo dis);

	/**
	 * 刪除分发信息对象。
	 * 
	 * @param oids String
	 */
	public void deleteDistributeInfos(String oids);

	/**
	 * 创建分发信息对象。
	 * 
	 * @return DistributeInfo
	 */
	public DistributeInfo newDistributeInfo();

	/**
	 * 创建和拷贝分发信息对象。
	 * 
	 * @param disInfoOld DistributeInfo
	 * @return DistributeInfo
	 */
	public DistributeInfo createAndCopyDistributeInfo(DistributeInfo disInfoOld, String orderOid, String linkOid);

	/**
	 * 创建默认分发信息。
	 * 
	 * @param  disOrdObjLinkOid String
	 * @param  infoConfigList List<DistributeInfoConfig>
	 *  
	 */
	public void createDefaultDistributeInfo(String disOrdObjLinkOid,List<DistributeInfoConfig> infoConfigList);

	/**
	 * 创建分发信息。
	 * 
	 * @param  disOrdOid String
	 * @param  linkList List<Map<String, String>>
	 * 				key:FLAG value:String 已发放标识 (发放:1，未发放:0)
	 * 				key:LINKOID value:String 发放单与分发数据LinkOID 
	 *              key:DATAOID value:String 分发数据源OID 
	 *              key:ISMASTER value:String 主对象标识 (是:1，不是:0)
	 *  @param collectFlag int 收集对象标识
	 *  @param addDefaultInfoFlag boolean 是否追加默认分发信息标识
	 */
	public void createInfo(String disOrdOid, List<Map<String, String>> linkList,int collectDlag,boolean addDefaultInfoFlag);

	/**
	 * 是否追加默认分发信息。
	 * 
	 * @param dataObject Persistable
	 * @return addDefaultInfoFlag boolean
	 */
	public boolean addDefaultInfo(Persistable dataObject);
	
	
	public List<DistributeInfo>  getpapertaskinfo(String linkOids, String taskOid);
	
	/**
	 * 修改是否跟踪信息。
	 * 
	 * @param oids String
	 * @return isTracks String
	 */
	public void updateDisInfoIsTrack(String oids, String isTracks);
	
	/**
	 * 创建外域分发信息
	 * @param innerIds
	 * @param siteNames
	 * @param userIds
	 * @param linkOids
	 */
	public void createOutSignDisInfo(String innerIds,String siteNames,String userIds,String linkOids,boolean flag);
	
	/**
	 * 通过发放单oid获得分发数据，判断分发数据的最高密级是否高于分发信息为电子人员的密级
	 * @param disOrderOid
	 * @return false：分发数据密级高于分发人员，不能分发
	 */
	public boolean canDisBySecurityLevel(String disOrderOid);
}
