package com.bjsasc.ddm.distribute.service.distributeorderobjectlink;

import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.plm.core.persist.model.Persistable;

/**
 *  发放单与分发数据link服务接口。
 * 
 * @author gengancong 2013-2-22
 */
public interface DistributeOrderObjectLinkService {

	/**
	 * 通过分发数据对象和发放单对象取得发放单与分发数据link数据。
	 * 
	 * @param distributeObjectOid String
	 * @param distributeOrderOids String
	 * @return List
	 */
	public List<DistributeOrderObjectLink> getDistributeOrderObjectLinkListByDisObjOidAndDisOrderOids(
			String distributeObjectOid,String distributeOrderOids);
	/**
	 * 通过分发数据对象，取得发放单与分发数据link数据。
	 * 
	 * @param distributeObjectOid String
	 * @return List
	 */
	public List<DistributeOrderObjectLink> getDistributeOrderObjectLinkListByDistributeObjectOid(
			String distributeObjectOid);

	/**
	 * @author kangyanfei
	 * 通过发放单oid，取得发放单与分发数据link数据。
	 * @param distributeOrderOid
	 * @return
	 */
	public List<DistributeOrderObjectLink> getDistributeOrderObjectLinkListByDistributeOrderOid(
			String distributeOrderOid);

	/**
	 * 创建发放单与分发数据link对象。
	 * 
	 * @param disObj DistributeOrderObjectLink
	 */
	public void createDistributeOrderObjectLink(DistributeOrderObjectLink disObj);

	/**
	 * 创建发放单与分发数据link对象。
	 * 
	 * @return DistributeOrderObjectLink
	 */
	public DistributeOrderObjectLink newDistributeOrderObjectLink();

	/**
	 * 发放单与分发数据Link
	 * 
	 * @param disOrd disOrd
	 * @param collectRefResList List<Persistable>
	 * @param  aotuCreateFlag boolean
	 * @return List<Map<String,String>>
	 * 				key:FLAG value:String 已发放标识 (发放:1，未发放:0)
	 * 				key:LINKOID value:String 发放单与分发数据LinkOID 
	 *              key:DATAOID value:String 分发数据源OID
	 *              key:ISMASTER value:String 主对象标识 (是:1，不是:0)
	 */
	public List<Map<String,String>> creteOrderObjectLink(DistributeOrder disOrd, List<Persistable> collectRefResList,boolean aotuCreateFlag);

	/**
	 * 数据源对象是否是一次性分发。
	 * 
	 * @return boolean
	 */
	public boolean hasOneDisStyle(String dataOid);
	/**
	 * 通过纸质任务的oid获取DistributeOrderObjectLink。
	 * 
	 * @param distributeObjectOid String
	 * @param distributeOrderOids String
	 * @return List
	 */
	public List<DistributeOrderObjectLink> getDistributeOrderObjectLinkListByDisPaperTaskOID(
			String oid);
}
