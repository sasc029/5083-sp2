package com.bjsasc.ddm.distribute.service.recdesinfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.recdesinfo.RecDesInfo;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService;
import com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService;
import com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.util.StringUtil;
import com.cascc.avidm.util.SplitString;

public class RecDesInfoServiceImpl implements RecDesInfoService {

	/** 生命周期服务 */
	private final DistributeLifecycleService lifeService = DistributeHelper.getDistributeLifecycleService();
	/** 发放单与分发数据信息服务 */
	private final DistributeOrderObjectLinkService disLinkService = DistributeHelper
			.getDistributeOrderObjectLinkService();
	/** 分发数据服务 */
	private final DistributeObjectService disObjService = DistributeHelper.getDistributeObjectService(); 
	/** 发放单服务 */
	private final DistributeOrderService distributeOrderService = DistributeHelper.getDistributeOrderService(); 

	@Override
	public List<RecDesInfo> getRecDesInfosByOrderObjectLinkList(List<DistributeOrderObjectLink> listOrderObjectLink) {
		List<RecDesInfo> list = new ArrayList<RecDesInfo>();
		Map<String, RecDesInfo> oidMap = new HashMap<String, RecDesInfo>();
		
		String resDesInfohql = "from RecDesInfo t where t.disOrderObjectLinkId=? and t.disOrderObjectLinkClassId=?";
		if (listOrderObjectLink != null && listOrderObjectLink.size() > 0) {
			for (DistributeOrderObjectLink link : listOrderObjectLink) {
				String innerId = link.getInnerId();
				String classId = link.getClassId();
				List<RecDesInfo> infos = PersistHelper.getService().find(resDesInfohql, innerId, classId);
				for (RecDesInfo info : infos) {
					if (list.contains(info)) {
						oidMap.get(info.getKey()).addOid(info.getOid());
						continue;
					}
					info.addOid(info.getOid());
					oidMap.put(info.getKey(), info);
					list.add(info);
				}
			}
		}
		return list;
	}

	@Override
	public List<RecDesInfo> getRecDesInfosByDistributeOrderInnerId(String innerId) {
		String hql = "from DistributeOrderObjectLink t where t.fromObjectRef.innerId=?";

		List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, innerId);

		List<RecDesInfo> list = new ArrayList<RecDesInfo>();

		hql = "from RecDesInfo t where t.disOrderObjectLinkId=?";
		if (links != null && links.size() > 0) {
			for (DistributeOrderObjectLink link : links) {
				innerId = link.getInnerId();
				List<RecDesInfo> infos = PersistHelper.getService().find(hql, innerId);
				list.addAll(infos);
			}
		}
		return list;
	}
	
	/*
	 * （非 Javadoc）
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#getAllRecInfo(String distributeOid)
	 */
	@Override
	public List<RecDesInfo> getAllRecInfo(String distributeOrderOid, List<String> linkOidList) {
		//画面展示数据集合
		List<RecDesInfo> list = new ArrayList<RecDesInfo>();
		//去重标示符，合并数据做判断
		List<String> keyList = new ArrayList<String>();
		List<DistributeOrderObjectLink> linksAll = new ArrayList<DistributeOrderObjectLink>();

		//未选择发放数据，根据发放单获得发放linkId
		if (linkOidList == null || linkOidList.isEmpty()) {
			String hql = "from DistributeOrderObjectLink t "
					+ "where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? ";
			String distributeOrderInnerId = Helper.getInnerId(distributeOrderOid);
			String distributeOrderClassId = Helper.getClassId(distributeOrderOid);

			List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, distributeOrderInnerId,
					distributeOrderClassId);
			if(links != null && !links.isEmpty()){
				linksAll.addAll(links);
			}
		} else {
			String hql = "from DistributeOrderObjectLink t " + "where t.innerId=? and t.classId=? ";
			for (String linkOid : linkOidList) {
				String innerId = Helper.getInnerId(linkOid);
				String classId = Helper.getClassId(linkOid);

				List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, innerId, classId);
				linksAll.addAll(links);
			}
		}
		String hql = "from RecDesInfo r where r.disOrderObjectLinkId=? and r.disOrderObjectLinkClassId=?";
		if (linksAll != null && linksAll.size() > 0) {
			for (DistributeOrderObjectLink link : linksAll) {
				String disOrderObjectLinkId = link.getInnerId();
				String disOrderObjectLinkClassId = link.getClassId();

				//查询发放数据都发给了那些单位和人员
				List<RecDesInfo> infos = PersistHelper.getService().find(hql, disOrderObjectLinkId, disOrderObjectLinkClassId);
				for (RecDesInfo info : infos) {

					//如果存在相同的（发放份数，销毁份数，需要销毁份数，备注），进行数据合并
					if (keyList.contains(info.getKey())) {
						int index = keyList.indexOf(info.getKey());
						list.get(index).addOid(info.getOid());
						continue;
					}
					info.addOid(info.getOid());
					info.setDisDeadLine(link.getDisDeadLine());
					info.setDisUrgent(link.getDisUrgent());
					//添加不不需要合并的回收数据
					list.add(info);
					keyList.add(info.getKey());
				}
			}
		}
		return list;
	}
	
	/*
	 * （非 Javadoc）
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#getAllDesInfo(String distributeOid)
	 */
	@Override
	public List<RecDesInfo> getAllDesInfo(String distributeOrderOid, List<String> linkOidList) {
		//画面展示数据集合
		List<RecDesInfo> list = new ArrayList<RecDesInfo>();
		//去重标示符，合并数据做判断
		List<String> keyList = new ArrayList<String>();
		List<DistributeOrderObjectLink> linksAll = new ArrayList<DistributeOrderObjectLink>();

		//未选择发放数据，根据发放单获得发放linkId
		if (linkOidList == null || linkOidList.isEmpty()) {
			String hql = "from DistributeOrderObjectLink t "
					+ "where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? ";
			String distributeOrderInnerId = Helper.getInnerId(distributeOrderOid);
			String distributeOrderClassId = Helper.getClassId(distributeOrderOid);

			List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, distributeOrderInnerId,
					distributeOrderClassId);
			if(links != null && !links.isEmpty()){
				linksAll.addAll(links);
			}
		} else {
			String hql = "from DistributeOrderObjectLink t " + "where t.innerId=? and t.classId=? ";
			for (String linkOid : linkOidList) {
				String innerId = Helper.getInnerId(linkOid);
				String classId = Helper.getClassId(linkOid);

				List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, innerId, classId);
				linksAll.addAll(links);
			}
		}
		String hql = "from RecDesInfo r where r.disOrderObjectLinkId=? and r.disOrderObjectLinkClassId=?";
		if (linksAll != null && linksAll.size() > 0) {
			//循环合并销毁信息
			for (DistributeOrderObjectLink link : linksAll) {
				String disOrderObjectLinkId = link.getInnerId();
				String disOrderObjectLinkClassId = link.getClassId();

				//查询发放数据都发给了那些单位和人员
				List<RecDesInfo> infos = PersistHelper.getService().find(hql, disOrderObjectLinkId, disOrderObjectLinkClassId);
				for (RecDesInfo info : infos) {
					//如果存在相同的（发放份数，销毁份数，需要销毁份数，备注），进行数据合并
					if (keyList.contains(info.getKey2())) {
						int index = keyList.indexOf(info.getKey2());
						list.get(index).addOid(info.getOid());
						continue;
					}
					info.addOid(info.getOid());
					info.setDisDeadLine(link.getDisDeadLine());
					info.setDisUrgent(link.getDisUrgent());
					//添加不不需要合并的回收数据
					list.add(info);
					keyList.add(info.getKey2());
				}
			}
		}
		return list;
	}

	/*
	 * （非 Javadoc）
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#addResDistributeOrder(String orderOidsStr,
			String number, String name, String orderType, String note)
	 */
	@Override
	public DistributeOrder addResDistributeOrder(String orderOidsStr,
			String number, String name, String orderType, String note) {
		
		List<DistributeObject> arrayList=new ArrayList<DistributeObject>();
		//将其他发放单的发放数据统一到一个Set集合里
		List<String> orderOidList = SplitString.string2List(orderOidsStr, ",");
		//根据发放单OID获得发放数据
		List<DistributeObject> disObjectList = disObjService
				.getDistributeObjectsByDistributeOrderOid(orderOidList.get(0));
		for (DistributeObject distributeObject : disObjectList) {

			List<DistributeOrderObjectLink> link = disLinkService
					.getDistributeOrderObjectLinkListByDistributeObjectOid(Helper
							.getOid(distributeObject));

			// 将主对象标识放进distributeObject的 DisplayName属性里面
			distributeObject.setDisplayName(link.get(0).getIsMaster());
			arrayList.add(distributeObject);
		}

		//获得发放单
		DistributeOrder disOrder = (DistributeOrder) PersistHelper.getService().getObject(orderOidList.get(0));
		//创建新的回收单
		DistributeOrder recOrder = distributeOrderService.newDistributeOrder();

		//初始化InnerId
		disOrder.setInnerId(recOrder.getInnerId());
		//编号
		disOrder.setNumber(number);
		//名称
		disOrder.setName(name);
		//发放单类型
		disOrder.setOrderType(orderType);
		//备注
		disOrder.setNote(note);
		//初始化生命周期
		lifeService.initLifecycle(disOrder);
		
		disOrder.setContextInfo(arrayList.get(0).getContextInfo());
		// 设置回收单域信息
		disOrder.setDomainInfo(arrayList.get(0).getDomainInfo());
		
		distributeOrderService.createDistributeOrder(disOrder);
		
		//创建与回收单关联的DistributeOrderObjectLink
		for (DistributeObject distributeObject : arrayList) {
			//回收单关联的数据OID
			disObjService.createDistributeOrderObjectLink(recOrder, distributeObject, distributeObject.getDisplayName());

		}
		
		return disOrder;
		
	}

	/*
	 * （非 Javadoc）
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#addDesDistributeOrder(String orderOidsStr,
			String number, String name, String orderType, String note)
	 */
	@Override
	public DistributeOrder addDesDistributeOrder(String orderOidsStr,
			String number, String name, String orderType, String note) {

		List<DistributeObject> arrayList=new ArrayList<DistributeObject>();
		
		//将其他发放单的发放数据统一到一个Set集合里
		List<String> orderOidList = SplitString.string2List(orderOidsStr, ",");

		//获取发放单
		List<DistributeObject> disObjectList = disObjService
				.getDistributeObjectsByDistributeOrderOid(orderOidList.get(0));
		for (DistributeObject distributeObject : disObjectList) {

			List<DistributeOrderObjectLink> link = disLinkService
					.getDistributeOrderObjectLinkListByDistributeObjectOid(Helper
							.getOid(distributeObject));
			// 将主对象标识放进distributeObject的 DisplayName属性里面
			distributeObject.setDisplayName(link.get(0).getIsMaster());
			arrayList.add(distributeObject);
		}
		
		//获得发放单
		DistributeOrder disOrder = (DistributeOrder) PersistHelper.getService().getObject(orderOidList.get(0));
		//创建销毁单
		DistributeOrder decOrder = distributeOrderService.newDistributeOrder();

		//初始化InnerId
		disOrder.setInnerId(decOrder.getInnerId());
		//编号
		disOrder.setNumber(number);
		//名称
		disOrder.setName(name);
		//发放单类型
		disOrder.setOrderType(orderType);
		//备注
		disOrder.setNote(note);
		
		lifeService.initLifecycle(disOrder);
		
		disOrder.setContextInfo(arrayList.get(0).getContextInfo());
		// 设置销毁单域信息
		disOrder.setDomainInfo(arrayList.get(0).getDomainInfo());
		
		distributeOrderService.createDistributeOrder(disOrder);
		
		//创建与销毁单关联的DistributeOrderObjectLink
		for (DistributeObject distributeObject : arrayList) {
			disObjService.createDistributeOrderObjectLink(disOrder, distributeObject, distributeObject.getDisplayName());
		}
		
		return disOrder;
		
	}

	/*
	 * （非 Javadoc）
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#getAllNeddRecInfosByDistributeOrderOid(String distributeOrderOid, List<String> linkOids)
	 */
	@Override
	public List<Map<String, Object>> getAllNeddRecInfosByDistributeOrderOid(
			String distributeOrderOid, List<String> linkOids) {
		//需要回收的发放信息容器
		List<Map<String, Object>> resultListDisInfos = new ArrayList<Map<String,Object>>();
		//去重标记符集合
		List<String> keylist = new ArrayList<String>();
		if(linkOids.size() == 0) {
			//查询发放单所对应的DistributeOrderObjectLink
			List<DistributeOrderObjectLink> links = getObjectLinkByDistributeOrderOid(distributeOrderOid);

			linkOids = new ArrayList<String>();

			for(DistributeOrderObjectLink link : links){
				//拼oid
				String linkOid = link.getClassId()+":"+link.getInnerId();
				//添加到集合里
				linkOids.add(linkOid);
			}
		}

		for(String linkOid : linkOids){
			//link的InnerId
			String linkInnerId = Helper.getInnerId(linkOid);
			//link的classId
			String linkClassId = Helper.getClassId(linkOid);
			String sql = " SELECT C.DISINFONAME, C.DISINFOID, C.DISINFOTYPE, C.DISORDEROBJLINKID, C.DISORDEROBJLINKCLASSID, "
					+ " sum(C.DISINFONUM) DISINFONUM, sum(C.RECOVERNUM) RECOVERNUM, C.DISMEDIATYPE, B.ID, B.NAME "
					+ " FROM  DDM_DIS_ORDEROBJLINK A, DDM_DIS_OBJECT B, DDM_DIS_INFO C, "
					+ " (SELECT DISTINCT D.INNERID INNERID, D.CLASSID CLASSID ,D.TOOBJECTCLASSID TOOBJECTCLASSID,D.TOOBJECTID TOOBJECTID "
					+ " FROM DDM_DIS_ORDEROBJLINK D WHERE D.INNERID =? AND D.CLASSID =?) E"
					+ " WHERE A.TOOBJECTCLASSID = B.CLASSID AND A.TOOBJECTID = B.INNERID "
					+ " AND A.CLASSID = C.DISORDEROBJLINKCLASSID AND A.INNERID = C.DISORDEROBJLINKID "
					+ " AND C.DISMEDIATYPE = ? "
					+ " AND A.INNERID <> E.INNERID AND A.CLASSID = E.CLASSID AND A.TOOBJECTCLASSID=E.TOOBJECTCLASSID AND A.TOOBJECTID = E.TOOBJECTID"
					+ " GROUP BY C.DISINFONAME, C.DISINFOID, C.DISINFOTYPE, "
					+ " C.DISORDEROBJLINKID, C.DISORDEROBJLINKCLASSID, C.DISMEDIATYPE, B.ID, B.NAME ";
			//只显示纸质分发信息
			String disMediaType = ConstUtil.C_DISMEDIATYPE_0;
			//执行查询
			List<Map<String, Object>> listDisInfos = Helper.getPersistService().query(sql, linkInnerId, linkClassId, disMediaType);
			//循环保存符合条件的发放信息
			for(Map<String, Object> listdisInfo : listDisInfos) {
				// 发放单linkId
				String linkId = (String) listdisInfo.get("DISORDEROBJLINKID");
				// 发放单linkClass
				String linkClass = (String) listdisInfo.get("DISORDEROBJLINKCLASSID");
				// 分发信息IID（人员或组织的内部标识）
				String listDisInfo = (String) listdisInfo.get("DISINFOID");
				// 分发信息名称（单位/人员）
				String listtDisName = (String) listdisInfo.get("DISINFONAME");
				// 发放份数
				long listDisInfoNum = ((BigDecimal)listdisInfo.get("DISINFONUM")).longValue();
				// 回收份数
				long listRecoverNum = ((BigDecimal)listdisInfo.get("RECOVERNUM")).longValue();
				//发放类型
				long listDisMediaType = ((BigDecimal)listdisInfo.get("DISMEDIATYPE")).longValue();
				//分发信息类型（0为单位，1为人员）
				long disInfoType = ((BigDecimal)listdisInfo.get("DISINFOTYPE")).longValue();

				//判断是否已经存在已经相同的数量的发放数据，进行合并
				String stringFlag = recInfoCombine(listDisInfo, listtDisName, listDisInfoNum, listRecoverNum, listDisMediaType, disInfoType);

				//发信息IID（人员或组织的内部标识）、分发信息名称（单位/人员）、发放份数、回收份数、发放类型 。进行合并
				if(keylist.contains(stringFlag)) {
					//相同的合并标示符索引
					int index = keylist.indexOf(stringFlag);
					String disInfoObjLinkId = (String) resultListDisInfos.get(index).get("DISORDEROBJLINKID");
					String disInfoObjLinkClassId = (String) resultListDisInfos.get(index).get("DISORDEROBJLINKCLASSID");
					disInfoObjLinkId = disInfoObjLinkId + ";" + linkId;
					disInfoObjLinkClassId = disInfoObjLinkClassId + ";" + linkClass;

					//发信息IID（人员或组织的内部标识）、分发信息名称（单位/人员）拼接
					resultListDisInfos.get(index).put("DISORDEROBJLINKID", disInfoObjLinkId);
					resultListDisInfos.get(index).put("DISORDEROBJLINKCLASSID", disInfoObjLinkClassId);
					//清空发放数据名称
					resultListDisInfos.get(index).put("ID", "");
					resultListDisInfos.get(index).put("NAME", "");

					continue;
				}
				//添加合并判断标记符
				keylist.add(stringFlag);
				//判断添加回收信息列表
				resultListDisInfos.add(listdisInfo);

			}
		}

		
		return resultListDisInfos;
	}

	/*
	 * （非 Javadoc）
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#getAllNeddDesInfosByDistributeOrderOid(String distributeOrderOid, List<String> linkOids)
	 */
	@Override
	public List<Map<String, Object>> getAllNeddDesInfosByDistributeOrderOid(
			String distributeOrderOid, List<String> linkOids) {
		//需要销毁的发放信息容器
		List<Map<String, Object>> resultListDisInfos = new ArrayList<Map<String,Object>>();
		//去重标记符集合
		List<String> keylist = new ArrayList<String>();

		if(linkOids.size() == 0) {
			//查询发放单所对应的DistributeOrderObjectLink
			List<DistributeOrderObjectLink> links =  getObjectLinkByDistributeOrderOid(distributeOrderOid);

			linkOids = new ArrayList<String>();

			for(DistributeOrderObjectLink link : links){
				//拼oid
				String linkOid = link.getClassId()+":"+link.getInnerId();
				//添加到集合里
				linkOids.add(linkOid);
			}
		}

		for(String linkOid : linkOids){
			//link的InnerId
			String linkInnerId = Helper.getInnerId(linkOid);
			//link的classId
			String linkClassId = Helper.getClassId(linkOid);
			String sql = " SELECT C.DISINFONAME, C.DISINFOID,C.DISINFOTYPE, C.DISORDEROBJLINKID, C.DISORDEROBJLINKCLASSID, "
					+ " sum(C.DISINFONUM) DISINFONUM, sum(C.RECOVERNUM) RECOVERNUM, sum(C.DESTROYNUM) DESTROYNUM, C.DISMEDIATYPE, B.ID, B.NAME "
					+ " FROM  DDM_DIS_ORDEROBJLINK A, DDM_DIS_OBJECT B, DDM_DIS_INFO C, "
					+ " (SELECT DISTINCT D.INNERID INNERID, D.CLASSID CLASSID ,D.TOOBJECTCLASSID TOOBJECTCLASSID,D.TOOBJECTID TOOBJECTID "
					+ " FROM DDM_DIS_ORDEROBJLINK D WHERE D.INNERID =? AND D.CLASSID =?) E"
					+ " WHERE A.TOOBJECTCLASSID = B.CLASSID AND A.TOOBJECTID = B.INNERID "
					+ " AND A.CLASSID = C.DISORDEROBJLINKCLASSID AND A.INNERID = C.DISORDEROBJLINKID "
					+ " AND  C.DISMEDIATYPE = ? "
					+ " AND A.INNERID <> E.INNERID AND A.CLASSID = E.CLASSID AND A.TOOBJECTCLASSID=E.TOOBJECTCLASSID AND A.TOOBJECTID = E.TOOBJECTID"
					+ " GROUP BY C.DISINFONAME, C.DISINFOID,C.DISINFOTYPE, "
					+ " C.DISORDEROBJLINKID, C.DISORDEROBJLINKCLASSID, C.DISMEDIATYPE, B.ID, B.NAME ";
			//只显示纸质分发信息
			String disMediaType = ConstUtil.C_DISMEDIATYPE_0;
			//执行查询
			List<Map<String, Object>> listDisInfos = Helper.getPersistService().query(sql, linkInnerId, linkClassId, disMediaType);
			//循环保存符合条件的发放信息
			for(Map<String, Object> listdisInfo : listDisInfos) {

				// 发放单linkId
				String linkId = (String) listdisInfo.get("DISORDEROBJLINKID");
				// 发放单linkClass
				String linkClass = (String) listdisInfo.get("DISORDEROBJLINKCLASSID");
				// 分发信息IID（人员或组织的内部标识）
				String listDisInfo = (String) listdisInfo.get("DISINFOID");
				// 分发信息名称（单位/人员）
				String listtDisName = (String) listdisInfo.get("DISINFONAME");
				// 发放份数
				long listDisInfoNum = ((BigDecimal)listdisInfo.get("DISINFONUM")).longValue();
				// 销毁份数
				long listDestroyNum = ((BigDecimal)listdisInfo.get("DESTROYNUM")).longValue();
				//发放类型
				long listDisMediaType = ((BigDecimal)listdisInfo.get("DISMEDIATYPE")).longValue();
				//分发信息类型（0为单位，1为人员）
				long disInfoType = ((BigDecimal)listdisInfo.get("DISINFOTYPE")).longValue();

				//判断是否已经存在已经相同的数量的发放数据，进行合并
				String stringFlag = desInfoCombine(listDisInfo, listtDisName, listDisInfoNum, listDestroyNum, listDisMediaType, disInfoType);
				//发信息IID（人员或组织的内部标识）、分发信息名称（单位/人员）、发放份数、销毁份数、发放类型 。进行合并
				if(keylist.contains(stringFlag)) {
					//相同的合并标示符索引
					int index = keylist.indexOf(stringFlag);
					String disInfoObjLinkId = (String) resultListDisInfos.get(index).get("DISORDEROBJLINKID");
					String disInfoObjLinkClassId = (String) resultListDisInfos.get(index).get("DISORDEROBJLINKCLASSID");
					disInfoObjLinkId = disInfoObjLinkId + ";" + linkId;
					disInfoObjLinkClassId = disInfoObjLinkClassId + ";" + linkClass;

					//发信息IID（人员或组织的内部标识）、分发信息名称（单位/人员）拼接
					resultListDisInfos.get(index).put("DISORDEROBJLINKID", disInfoObjLinkId);
					resultListDisInfos.get(index).put("DISORDEROBJLINKCLASSID", disInfoObjLinkClassId);
					//清空发放数据名称
					resultListDisInfos.get(index).put("ID", "");
					resultListDisInfos.get(index).put("NAME", "");
					continue;
				}

				keylist.add(stringFlag);
				resultListDisInfos.add(listdisInfo);

			}

		}
		return resultListDisInfos;

	}

	/*
	 * （非 Javadoc）
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#addNeddRecInfos(String disInfoNames, String disInfoIds,
			String disOrderObjLinkIds,
			String disOrderObjLinkClassIds, String disMediaTypes,
			String disInfoNums, String needDestroyNums, String distributeOrderOid, String notes, String disInfoTypes)
	 */
	@Override
	public String addNeddRecInfos(String disInfoNames, String disInfoIds,
			String disOrderObjLinkIds, String disOrderObjLinkClassIds, String disMediaTypes,
			String disInfoNums, String needRecoverNums, 
			String recoverNums,String distributeOrderOid, String notes, String disInfoTypes) {
		//保存回收信息集合
		List<RecDesInfo> saveRecDesInfos = new ArrayList<RecDesInfo>();
		//更新回收信息集合
		List<RecDesInfo> updateRecDesInfos = new ArrayList<RecDesInfo>();
		//存放错误消息
		StringBuffer result = new StringBuffer();
		//分发信息名称（单位/人员）集合
		List<String> disInfoNameArr = SplitString.string2List(disInfoNames, ",");
		//分发信息IID（人员或组织的内部标识）
		List<String> disInfoIdArr = SplitString.string2List(disInfoIds, ",");
		//分发信息类型（0为单位，1为人员）集合
		List<String> disInfoTypeArr = SplitString.string2List(disInfoTypes, ",");
		//发放单与分发数据LINK内部标识
		List<String> disOrderObjLinkIdArr = SplitString.string2List(disOrderObjLinkIds, ",");
		//发放单与分发数据LINK类标识
		List<String> disOrderObjLinkClassIdArr = SplitString.string2List(disOrderObjLinkClassIds, ",");
		//分发介质类型（0为纸质，1为电子，2为跨域）
		List<String> disMediaTypeArr = SplitString.string2List(disMediaTypes, ",");
		//分发份数
		List<String> disInfoNumArr = SplitString.string2List(disInfoNums, ",");
		//已销毁份数集合
		List<String> recoverNumArr = SplitString.string2List(recoverNums, ",");
		//需要回收份数
		List<String> needRecoverNumArr = SplitString.string2List(needRecoverNums, ",");
		//备注
		List<String> noteArr = SplitString.string2List(notes, ",");

		for(int i = 0; i < disOrderObjLinkIdArr.size(); i++){
			//分发信息IID（人员或组织的内部标识）
			String disInfoId = disInfoIdArr.get(i);
			//发放单与分发数据LINK内部标识
			String disOrderObjLinkId = disOrderObjLinkIdArr.get(i);
			//发放单与分发数据LINK类标识
			String disOrderObjLinkClassId = disOrderObjLinkClassIdArr.get(i);
			//分分发信息名称（单位/人员）
			String disInfoName = disInfoNameArr.get(i);
			//分发信息类型（0为单位，1为人员）
			String disInfoType = "";
			if(ConstUtil.C_DISINFOTYPE_0.equals(disInfoTypeArr.get(i))){
				disInfoType = ConstUtil.C_DISINFOTYPE_USER;
			}else if(ConstUtil.C_DISINFOTYPE_1.equals(disInfoTypeArr.get(i))){
				disInfoType = ConstUtil.C_DISINFOTYPE_ORG;
			}
			//分发介质类型（0为纸质，1为电子，2为跨域）
			String disMediaType = "";
			if ((ConstUtil.C_PAPERTASK).equals(disMediaTypeArr.get(i))) {
				disMediaType = ConstUtil.C_DISMEDIATYPE_0;
			} else if ((ConstUtil.C_ELECTASK).equals(disMediaTypeArr.get(i))) {
				disMediaType =  ConstUtil.C_DISMEDIATYPE_1;
			}
			//发放份数
			long disInfoNum = Long.parseLong(disInfoNumArr.get(i));
			//已回收份数
			long recoverNum = Long.parseLong(recoverNumArr.get(i));
			//添加需要回收份数
			long needRecoverNum = Long.parseLong(needRecoverNumArr.get(i));
			//备注
			String note = "";
			if(!StringUtil.isStringEmpty(noteArr.get(i))){
				note = noteArr.get(i);
			}

			//合并数据的LinkId集合
			List<String> ObjLinkIdArr = SplitString.string2List(disOrderObjLinkId, ";");
			//合并数据的LinkClassId集合
			List<String> ObjLinkClassIdArr = SplitString.string2List(disOrderObjLinkClassId, ";");
			for(int j = 0 ; j < ObjLinkIdArr.size(); j++) {
				String linkId = ObjLinkIdArr.get(j);
				String ClassId = ObjLinkClassIdArr.get(j);
				//查询已经添加过的回收信息
				List<Map<String, Object>> listMap = searchDisRecInfoByDisOid(distributeOrderOid, disInfoId,
						linkId, ClassId, disMediaType);
	
				//已经存在改回收信息，做更新操作
				if(listMap.size() > 0){
					//已经添加的回收份数
					long needRecInfoNum = ((BigDecimal)listMap.get(0).get("NEEDRECOVERNUM")).longValue();
					
					long totalNum = needRecInfoNum + needRecoverNum;
					
					//判断发放份数是否大于需要回收份数
					if((disInfoNum - recoverNum) >= totalNum){
						String recDesInfoInnerId = (String) listMap.get(0).get("INNERID");
						String recDesInfoClassId = (String) listMap.get(0).get("CLASSID");
						//根据InnerId和ClassId查询回收信息
						RecDesInfo recDesInfo = (RecDesInfo) Helper.getPersistService().getObject(recDesInfoClassId, recDesInfoInnerId);
						//修改需要回收份数
						recDesInfo.setNeedRecoverNum(totalNum);
						//修改备注
						recDesInfo.setNote(note);
						//保存更新的回收信息到集合里
						updateRecDesInfos.add(recDesInfo);
					}else{
						//合并和单条的只允许添加一条错误提示
						if(j == 0){
							result.append(disInfoName+"-" + disMediaTypeArr.get(i)+"-可回收的剩余份数：" + ( disInfoNum - recoverNum -needRecInfoNum )+"份,");
						}
					}

				}else{//未存在回收信息，做添加操作
					RecDesInfo recDesInfo = setDisRecInfo(disInfoId, disInfoName,
							linkId,
							ClassId, needRecoverNum,
							distributeOrderOid, disMediaType, disInfoNum, recoverNum, note, disInfoType);
					//保存新添加的回收信息到集合里
					saveRecDesInfos.add(recDesInfo);
				}

			}

		}
		//结果消息
		String resultMessage = "";
		String SubString = result.toString();
		if(SubString != null && !"".equals(SubString)){
			resultMessage = SubString.substring(0,SubString.length()-1)+"|超过剩余回收份数";
		}

		//批量保存回收信息
		if(updateRecDesInfos.size() > 0) {
			Helper.getPersistService().update(updateRecDesInfos);
		}
		//批量更新回收信息
		if(saveRecDesInfos.size() > 0) {
			Helper.getPersistService().save(saveRecDesInfos);
		}
	
		return resultMessage;
	}

	/*
	 * （非 Javadoc）
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#addNeddDesInfos(String disInfoNames, String disInfoIds,
			String disOrderObjLinkIds,
			String disOrderObjLinkClassIds, String disMediaTypes,
			String disInfoNums, String needDestroyNums, String distributeOrderOid,
			String recoverNums, String notes, String disInfoTypes)
	 */
	@Override
	public String addNeddDesInfos(String disInfoNames, String disInfoIds,
			String disOrderObjLinkIds,
			String disOrderObjLinkClassIds, String disMediaTypes,
			String disInfoNums, String needDestroyNums, String destroyNums, String distributeOrderOid,
			String recoverNums, String notes, String disInfoTypes) {
		//保存销毁信息集合
		List<RecDesInfo> saveRecDesInfos = new ArrayList<RecDesInfo>();
		//更新销毁信息集合
		List<RecDesInfo> updateRecDesInfos = new ArrayList<RecDesInfo>();
		//存放错误消息
		StringBuffer result = new StringBuffer();
		//分发信息名称（单位/人员）集合
		List<String> disInfoNameArr = SplitString.string2List(disInfoNames, ",");
		//分发信息IID（人员或组织的内部标识）集合
		List<String> disInfoIdArr = SplitString.string2List(disInfoIds, ",");
		//分发信息类型（0为单位，1为人员）集合
		List<String> disInfoTypeArr = SplitString.string2List(disInfoTypes, ",");
		//发放单与分发数据LINK内部标识集合
		List<String> disOrderObjLinkIdArr = SplitString.string2List(disOrderObjLinkIds, ",");
		//发放单与分发数据LINK类标识集合
		List<String> disOrderObjLinkClassIdArr = SplitString.string2List(disOrderObjLinkClassIds, ",");
		//分发介质类型（0为纸质，1为电子，2为跨域）集合
		List<String> disMediaTypeArr = SplitString.string2List(disMediaTypes, ",");
		//分发份数集合
		List<String> disInfoNumArr = SplitString.string2List(disInfoNums, ",");
		//已销毁份数集合
		List<String> destroyNumArr = SplitString.string2List(destroyNums, ",");
		//已回收份数集合
		List<String> recoverNumArr = SplitString.string2List(recoverNums, ",");
		//需要销毁份数集合
		List<String> needDestroyNumArr = SplitString.string2List(needDestroyNums, ",");
		//备注
		List<String> noteArr = SplitString.string2List(notes, ",");

		for(int i = 0; i < disOrderObjLinkIdArr.size(); i++){
			//分发信息IID（人员或组织的内部标识）
			String disInfoId = disInfoIdArr.get(i).trim();
			//发放单与分发数据LINK内部标识
			String disOrderObjLinkId = disOrderObjLinkIdArr.get(i).trim();
			//发放单与分发数据LINK类标识
			String disOrderObjLinkClassId = disOrderObjLinkClassIdArr.get(i).trim();
			//分发信息IID（人员或组织的内部标识）
			String disInfoName = disInfoNameArr.get(i).trim();
			//分发信息类型（0为单位，1为人员）
			String disInfoType = "";
			if(ConstUtil.C_DISINFOTYPE_0.equals(disInfoTypeArr.get(i))){
				disInfoType = ConstUtil.C_DISINFOTYPE_USER;
			}else if(ConstUtil.C_DISINFOTYPE_1.equals(disInfoTypeArr.get(i))){
				disInfoType = ConstUtil.C_DISINFOTYPE_ORG;
			}
			//分发介质类型（0为纸质，1为电子，2为跨域）
			String disMediaType = "";
			if ((ConstUtil.C_PAPERTASK).equals(disMediaTypeArr.get(i))) {
				disMediaType = ConstUtil.C_DISMEDIATYPE_0;
			} else if ((ConstUtil.C_ELECTASK).equals(disMediaTypeArr.get(i))) {
				disMediaType =  ConstUtil.C_DISMEDIATYPE_1;
			}
			//添加份数
			long needDestroyNum = Long.parseLong(needDestroyNumArr.get(i));
			//已销毁的份数
			long destroyNum = Long.parseLong(destroyNumArr.get(i));
			//已回收的份数
			long recoverNum = Long.parseLong(recoverNumArr.get(i));
			//发放份数
			long disInfoNum = Long.parseLong(disInfoNumArr.get(i));
			//备注
			String note = "";
			if(!StringUtil.isStringEmpty(noteArr.get(i))){
				note = noteArr.get(i);
			}

			//合并数据的LinkId集合
			List<String> ObjLinkIdArr = SplitString.string2List(disOrderObjLinkId, ";");
			//合并数据的LinkClassId集合
			List<String> ObjLinkClassIdArr = SplitString.string2List(disOrderObjLinkClassId, ";");
			for(int j = 0 ; j < ObjLinkIdArr.size(); j++) {
				String linkId = ObjLinkIdArr.get(j);
				String ClassId = ObjLinkClassIdArr.get(j);

				//查询已经添加过的回收信息
				List<Map<String, Object>> listMap = searchDisRecInfoByDisOid(
						distributeOrderOid, disInfoId, linkId,
						ClassId, disMediaType);
	
				//已经存在改回收信息，做更新操作
				if(listMap.size() > 0){
					//已经添加的销毁份数
					long needDestroyInfoNum = ((BigDecimal)listMap.get(0).get("NEEDDESTROYNUM")).longValue();
					
					long totalNum = needDestroyInfoNum + needDestroyNum;
					
					//判断发放份数是否大于需要回收份数
					if((disInfoNum -destroyNum) >= totalNum){
						String recDesInfoInnerId = (String) listMap.get(0).get("INNERID");
						String recDesInfoClassId = (String) listMap.get(0).get("CLASSID");
						//根据InnerId和ClassId查询回收信息
						RecDesInfo recDesInfo = (RecDesInfo) Helper.getPersistService().getObject(recDesInfoClassId, recDesInfoInnerId);
						//修改需要回收份数
						recDesInfo.setNeedDestroyNum(totalNum);
						//修改备注
						recDesInfo.setNote(note);
						//添加需要更新回收信息到集合里
						updateRecDesInfos.add(recDesInfo);
					}else{
						//合并和单条的只允许添加一条错误提示
						if(j == 0){
							result.append(disInfoName+"-" + disMediaTypeArr.get(i)+"-可销毁的剩余份数：" + (disInfoNum -destroyNum - needDestroyInfoNum )+"份,");
						}
					}
				}else{//未存在回收信息，做添加操作
						RecDesInfo recDesInfo = setDisDesInfo(disInfoId, disInfoName, linkId,
								ClassId, needDestroyNum,
								distributeOrderOid, disMediaType, recoverNum, destroyNum,disInfoNum, note, disInfoType);
						saveRecDesInfos.add(recDesInfo);
				}

			}

		}
		//结果消息
		String resultMessage = "";
		String SubString = result.toString();
		if(SubString != null && !"".equals(SubString)){
			resultMessage = SubString.substring(0,SubString.length()-1)+"|超过剩余未销毁份数";;
		}

		//批量保存销毁信息
		if(saveRecDesInfos.size() > 0) {
			Helper.getPersistService().save(saveRecDesInfos);
		}
		//批量更新销毁信息
		if(updateRecDesInfos.size() > 0) {
			Helper.getPersistService().update(updateRecDesInfos);
		}

		return resultMessage;

	}

	/**
	 * 回收单查询
	 *
	 * @param disOrderOid
	 *			发放单OID
	 * @param disInfoId
	 *			分发信息IID（人员或组织的内部标识）	
	 * @param disOrderObjLinkId
	 * 			发放单与分发数据LINK内部标识
	 * @param disOrderObjLinkClassId
	 * 			发放单与分发数据LINK类标识
	 * @param disMediaType
	 * 			分发介质类型（0为纸质，1为电子，2为跨域）
	 *
	 * @return	List<Map<String, Object>>
	 *			回收信息
	 */
	public List<Map<String, Object>> searchDisRecInfoByDisOid(
			String disOrderOid, String disInfoId,
			String disOrderObjLinkId, String disOrderObjLinkClassId, String disMediaType) {
		String classId = Helper.getClassId(disOrderOid);
		String innerId = Helper.getInnerId(disOrderOid);

		String sql = " SELECT A .* FROM DDM_RECDES_INFO A, DDM_DIS_ORDER B, DDM_DIS_ORDEROBJLINK C, "
				+ " (SELECT DISTINCT D.TOOBJECTID, D.TOOBJECTCLASSID FROM DDM_DIS_ORDEROBJLINK D WHERE "
				+ " D.CLASSID = ? AND D.INNERID = ?) E WHERE B.CLASSID = ? AND B.INNERID = ? "
				+ " AND B .CLASSID = C.FROMOBJECTCLASSID AND B .INNERID = C.FROMOBJECTID AND "
				+ " C.TOOBJECTCLASSID = E.TOOBJECTCLASSID AND C.TOOBJECTID = E.TOOBJECTID "
				+ " AND A.DISORDEROBJECTLINKCLASSID = C.CLASSID And A.DISORDEROBJECTLINKID = C.INNERID AND A.DISMEDIATYPE = ? "
				+ "  AND A.DISINFOID = ? AND A.STATENAME = ? ";

		List<Map<String, Object>> listDisInfos = null;
		//“未分发”状态的回收销毁信息
		String stateName =  ConstUtil.LC_NOT_DISTRIBUT.getName();;
		//执行查询
		listDisInfos = Helper.getPersistService()
				.query(sql, disOrderObjLinkClassId, disOrderObjLinkId, classId,
						innerId, disMediaType, disInfoId, stateName);

		if(listDisInfos==null || listDisInfos.isEmpty()){
			listDisInfos = new ArrayList<Map<String,Object>>();
		 }
		return listDisInfos;
	}

	/**
	 * 添加回收消息
	 *
	 * @param disInfoId
	 *			分发信息IID（人员或组织的内部标识）
	 * @param disInfoName
	 *			分发信息名称（单位/人员）
	 * @param disOrderObjLinkId
	 *			发放单与分发数据LINK内部标识
	 * @param disOrderObjLinkClassId
	 *			发放单与分发数据LINK类标识
	 * @param needRecoverNum
	 *			回收份数
	 * @param distributeOrderOid
	 *			回收单OID
	 * @param disMediaType
	 *			分发介质类型（0为纸质，1为电子，2为跨域）
	 * @param disInfoNum
	 *			发放份数
	 * @param recoverNum
	 *			已回收份数
	 * @param note
	 *			备注
	 * @param disInfoType
	 *			分发信息类型（0为单位，1为人员）
	 * @return RecDesInfo
	 *			回收销毁信息
	 */
	public RecDesInfo setDisRecInfo(String disInfoId, String disInfoName,
			String disOrderObjLinkId,
			String disOrderObjLinkClassId, long needRecoverNum,
			String distributeOrderOid,String disMediaType, 
			long disInfoNum, long recoverNum, String note, String disInfoType) {

		//创建回收信息对象
		RecDesInfo recDesInfo = (RecDesInfo) PersistUtil.createObject(RecDesInfo.CLASSID);
		// 查询发放单与分发数据link。
		List<Map<String, Object>> listMap = getDistributeOrderobjectlink(disOrderObjLinkClassId, disOrderObjLinkId, distributeOrderOid);

		if(listMap.size() > 0){
			//获取发放单与分发数据LINK类标识
			String disOrderObjectLinkClassId = (String) listMap.get(0).get("CLASSID");
			//获取发放单与分发数据LINK内部标识
			String disOrderObjectLinkId = (String) listMap.get(0).get("INNERID");

			//查询发放信息
			List<Map<String, Object>> disInfo = getDistributeInfo(disInfoId, disInfoName);


			//设置回收信息内部标识
			recDesInfo.setClassId(RecDesInfo.CLASSID);
			//设置需要回收的份数
			recDesInfo.setNeedRecoverNum(needRecoverNum);
			//设置发放单与分发数据LINK内部标识
			recDesInfo.setDisOrderObjectLinkClassId(disOrderObjectLinkClassId);
			//设置发放单与分发数据LINK类标识
			recDesInfo.setDisOrderObjectLinkId(disOrderObjectLinkId);
			//设置外域接收人IID（人员内部标识）
			recDesInfo.setDisInfoId(disInfoId);
			//设置外域接收人名称（人员）
			recDesInfo.setDisInfoName(disInfoName);
			//分发介质类型（0为纸质，1为电子，2为跨域）集合
			recDesInfo.setDisMediaType(disMediaType);
			//分发份数
			recDesInfo.setDisInfoNum(disInfoNum);
			//已回收份数
			recDesInfo.setRecoverNum(recoverNum);
			//备注
			recDesInfo.setNote(note);
			//分发信息类型（0为单位，1为人员）
			recDesInfo.setDisInfoType(disInfoType);
			//分发方式:回收
			recDesInfo.setDisType(ConstUtil.C_DISTYPE_REC);

			if(disInfo != null && disInfo.size() > 0){
				String infoClassId = (String) disInfo.get(0).get("INFOCLASSID");
				//设置分发信息的类标识（人员或者组织的类标识）
				if(infoClassId != null && !"".equals(infoClassId)){
					recDesInfo.setInfoClassId(infoClassId);
					
				}
			}

			//初始化生命周期
			lifeService.initLifecycle(recDesInfo);
			
		}

		return recDesInfo;
	}

	/**
	 * 添加回收消息
	 *
	 * @param disInfoId
	 *			分发信息IID（人员或组织的内部标识）
	 * @param disInfoName
	 *			分发信息名称（单位/人员）
	 * @param disOrderObjLinkId
	 *			发放单与分发数据LINK内部标识
	 * @param disOrderObjLinkClassId
	 *			发放单与分发数据LINK类标识
	 * @param needDestroyNum
	 *			销毁份数
	 * @param distributeOrderOid
	 *			回收单OID
	 * @param disMediaType
	 *			分发介质类型（0为纸质，1为电子，2为跨域）
	 * @param recoverNum
	 *			已回收份数
	 * @param destroyNum
	 *			已销毁份数
	 * @param disInfoNum
	 *			发放份数
	 * @param note
	 *			备注
	 * @param disInfoType
	 *			分发信息类型（0为单位，1为人员）
	 * @return RecDesInfo
	 *			回收销毁信息
	 */
	public RecDesInfo setDisDesInfo(String disInfoId, String disInfoName,
			String disOrderObjLinkId, String disOrderObjLinkClassId, long needDestroyNum,
			String distributeOrderOid,String disMediaType, long recoverNum, 
			long destroyNum, long disInfoNum, String note, String disInfoType) {
		
		//创建销毁信息对象
		RecDesInfo recDesInfo = (RecDesInfo) PersistUtil.createObject(RecDesInfo.CLASSID);
		// 查询发放单与分发数据link。
		List<Map<String, Object>> listMap = getDistributeOrderobjectlink(disOrderObjLinkClassId, disOrderObjLinkId, distributeOrderOid);
		if(listMap.size() > 0){
			//获取发放单与分发数据LINK类标识
			String disOrderObjectLinkClassId = (String) listMap.get(0).get("CLASSID");
			//获取发放单与分发数据LINK内部标识
			String disOrderObjectLinkId = (String) listMap.get(0).get("INNERID");

			//查询发放信息
			List<Map<String, Object>> disInfo = getDistributeInfo(disInfoId, disInfoName);

			//设置回收信息内部标识
			recDesInfo.setClassId(RecDesInfo.CLASSID);
			//设置需要回收的份数
			recDesInfo.setNeedDestroyNum(needDestroyNum);
			//设置发放单与分发数据LINK内部标识
			recDesInfo.setDisOrderObjectLinkClassId(disOrderObjectLinkClassId);
			//设置发放单与分发数据LINK类标识
			recDesInfo.setDisOrderObjectLinkId(disOrderObjectLinkId);
			//设置外域接收人IID（人员内部标识）
			recDesInfo.setDisInfoId(disInfoId);
			//设置外域接收人名称（人员）
			recDesInfo.setDisInfoName(disInfoName);
			//分发介质类型（0为纸质，1为电子，2为跨域）集合
			recDesInfo.setDisMediaType(disMediaType);
			//已回收份数
			recDesInfo.setRecoverNum(recoverNum);
			//发放份数
			recDesInfo.setDisInfoNum(disInfoNum);
			//已销毁份数
			recDesInfo.setDestroyNum(destroyNum);
			//分发信息类型（0为单位，1为人员）
			recDesInfo.setDisInfoType(disInfoType);
			//分发方式:销毁
			recDesInfo.setDisType(ConstUtil.C_DISTYPE_DES);

			if(disInfo != null && disInfo.size() > 0){
				String infoClassId = (String) disInfo.get(0).get("INFOCLASSID");
				//设置分发信息的类标识（人员或者组织的类标识）
				if(infoClassId != null && !"".equals(infoClassId)){
					recDesInfo.setInfoClassId(infoClassId);
					
				}
			}

			//初始化生命周期
			lifeService.initLifecycle(recDesInfo);

		}
		return recDesInfo;
	}


	/**
	 * 查询发放单与分发数据link。
	 * @param disOrderObjLinkClassId
	 *			发放单与分发数据LINK类标识
	 * @param disOrderObjLinkId
	 *			发放单与分发数据LINK内部标识
	 * @param disOrderOid
	 *			回收单OID
	 * @return	List<Map<String, Object>>
	 * 			发放单与分发数据link。
	 */
	public List<Map<String, Object>> getDistributeOrderobjectlink(String disOrderObjLinkClassId, String disOrderObjLinkId, String disOrderOid){
		String classId = Helper.getClassId(disOrderOid);
		String innerId = Helper.getInnerId(disOrderOid);

		String sql = " SELECT DISTINCT A.* FROM DDM_DIS_ORDEROBJLINK A, "
				+ " ( SELECT DISTINCT B.TOOBJECTCLASSID, B.TOOBJECTID FROM DDM_DIS_ORDEROBJLINK B "
				+ " WHERE B.CLASSID = ? AND B.INNERID = ?) C "
				+ " WHERE A.FROMOBJECTCLASSID = ? AND A.FROMOBJECTID = ? "
				+ " AND A.TOOBJECTCLASSID = C.TOOBJECTCLASSID AND A.TOOBJECTID = C.TOOBJECTID ";
		//执行查询
		List<Map<String, Object>> listDisOrderobjectlinks = Helper.getPersistService()
				.query(sql, disOrderObjLinkClassId, disOrderObjLinkId, classId, innerId);
		if(listDisOrderobjectlinks == null || listDisOrderobjectlinks.isEmpty()){
			listDisOrderobjectlinks = new ArrayList<Map<String,Object>>();
		}

		return listDisOrderobjectlinks;
	}

	/*
	 * （非 Javadoc）
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#getRecInfosForEditByOId(String oid)
	 */
	@Override
	public List<RecDesInfo> getRecInfosForEditByOId(String oid) {
		List<RecDesInfo> listRecInfos = new ArrayList<RecDesInfo>();
		//分割oid和oids
		List<String> oidArr = SplitString.string2List(oid, "!");
		for(String oids : oidArr){
			//分割oid和oids集合
			List<String> oidAndOids = SplitString.string2List(oids, ",");

			//第一个值是oid，第二个是oids
			String classId = Helper.getClassId(oidAndOids.get(0));
			String innerId = Helper.getInnerId(oidAndOids.get(0));

			String hql = " from RecDesInfo r where r.innerId=? and r.classId = ?";
			List<RecDesInfo> recInfos = Helper.getPersistService().find(hql, innerId, classId);
			//查询需要修改的回收销毁信息
			if(recInfos.size() > 0){
				RecDesInfo recDesInfo = recInfos.get(0);
				//oids值，如果有，保存到回收销毁信息中
				recDesInfo.setOids(oids);
				listRecInfos.add(recDesInfo);
			}

		}
		return listRecInfos;
	}

	/*
	 * （非 Javadoc）
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#getDesInfosForEditByOId(String oid)
	 */
	@Override
	public List<RecDesInfo> getDesInfosForEditByOId(String oid) {
		List<RecDesInfo> listDesInfos = new ArrayList<RecDesInfo>();
		//分割oid和oids
		List<String> oidArr = SplitString.string2List(oid, "!");
		for(String oids : oidArr){
			//分割oid和oids集合
			List<String> oidAndOids = SplitString.string2List(oids, ",");

			//第一个值是oid，第二个是oids
			String classId = Helper.getClassId(oidAndOids.get(0));
			String innerId = Helper.getInnerId(oidAndOids.get(0));

			String hql = " from RecDesInfo r where r.innerId=? and r.classId = ?";
			List<RecDesInfo> desInfos = Helper.getPersistService().find(hql, innerId, classId);
			//查询需要修改的回收销毁信息
			if(desInfos.size() > 0){
				RecDesInfo recDesInfo = desInfos.get(0);
				//oids值，如果有，保存到回收销毁信息中
				recDesInfo.setOids(oids);
				listDesInfos.add(recDesInfo);
			}

		}
		return listDesInfos;
	}

	/*
	 * （非 Javadoc）
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#updateRecInfos(String recInfoOids, String needRecoverNums,
			String notes, String dismediatypes)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void updateRecInfos(String recInfoOids, String needRecoverNums,
			String notes, String dismediatypes) {
		//更新的回收信息发放单信息集合
		List<RecDesInfo> updateResInfo = new ArrayList<RecDesInfo>();
		//回收信息OID集合
		List<String> recInfoOidArr = SplitString.string2List(recInfoOids, "!");
		//需要回收份数集合
		List<String> needRecoverNumArr = SplitString.string2List(needRecoverNums, ",");
		//备注集合
		List<String> noteArr = SplitString.string2List(notes, ",");
		//发放方式集合
		List<String> dismediatypeArr = SplitString.string2List(dismediatypes, ",");

		for(int i = 0; i < recInfoOidArr.size(); i++){
			List<String> oids = SplitString.string2List(recInfoOidArr.get(i), ",");
			//循环查询所有选择的回收信息（合并和未合并的）
			for(String oid : oids){
				RecDesInfo recDesInfo = (RecDesInfo) Helper.getPersistService().getObject(oid);
				//设置回收份数
				recDesInfo.setNeedRecoverNum(Long.parseLong(needRecoverNumArr.get(i)));
				//设置备注
				String note = "";
				
				if(!StringUtil.isStringEmpty(noteArr.get(i))){
					note = noteArr.get(i);
				}
				
				recDesInfo.setNote(note);
				//设置分发类型
				String dismediatype = "";
				if ((ConstUtil.C_PAPERTASK).equals(dismediatypeArr.get(i))) {
					dismediatype = ConstUtil.C_DISMEDIATYPE_0;
				} else if ((ConstUtil.C_ELECTASK).equals(dismediatypeArr.get(i))) {
					dismediatype = ConstUtil.C_DISMEDIATYPE_1;
				} else if ((ConstUtil.C_OUTSITETASK).equals(dismediatypeArr.get(i))) {
					dismediatype = ConstUtil.C_DISMEDIATYPE_2;
				}
				recDesInfo.setDisMediaType(dismediatype);
				updateResInfo.add(recDesInfo);
			}

		}

		//批量更新
		Helper.getPersistService().update(updateResInfo);
	}

	/*
	 * （非 Javadoc）
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#updateDesInfos(String desInfoOids, String needRecoverNums,
			String notes, String dismediatypes)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void updateDesInfos(String desInfoOids, String needDestroyNums,
			String notes, String dismediatypes) {
		//更新销毁信息集合
		List<RecDesInfo> updateDesInfo = new ArrayList<RecDesInfo>();
		//销毁信息OID集合
		List<String> desInfoOidArr = SplitString.string2List(desInfoOids, "!");
		//需要销毁份数集合
		List<String> needDestroyNumArr = SplitString.string2List(needDestroyNums, ",");
		//备注集合
		List<String> noteArr = SplitString.string2List(notes, ",");
		//发放方式集合
		List<String> dismediatypeArr = SplitString.string2List(dismediatypes, ",");

		for(int i = 0; i < desInfoOidArr.size(); i++){
			List<String> oids = SplitString.string2List(desInfoOidArr.get(i), ",");
			//循环查询所有选择的销毁信息（合并和未合并的）
			for(String oid : oids){
			//销毁信息
			RecDesInfo recDesInfo = (RecDesInfo) Helper.getPersistService().getObject(oid);

			//设置回收份数
			recDesInfo.setNeedDestroyNum(Long.parseLong(needDestroyNumArr.get(i)));
			//设置备注
			String note = "";

			if(!StringUtil.isStringEmpty(noteArr.get(i))){
				note = noteArr.get(i);
			}

			recDesInfo.setNote(note);
			//设置分发类型
			String dismediatype = "";
			if ((ConstUtil.C_PAPERTASK).equals(dismediatypeArr.get(i))) {
				dismediatype = ConstUtil.C_DISMEDIATYPE_0;
			} else if ((ConstUtil.C_ELECTASK).equals(dismediatypeArr.get(i))) {
				dismediatype = ConstUtil.C_DISMEDIATYPE_1;
			} else if ((ConstUtil.C_OUTSITETASK).equals(dismediatypeArr.get(i))) {
				dismediatype = ConstUtil.C_DISMEDIATYPE_2;
			}
			recDesInfo.setDisMediaType(dismediatype);

			updateDesInfo.add(recDesInfo);

			}
		}

		//批量更新
		Helper.getPersistService().update(updateDesInfo);
		
	}

	/*
	 * （非 Javadoc）
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#deleteRecDesInfos(String oids)
	 */
	@Override
	public void deleteRecDesInfos(String oids) {
		//把回收销毁信息OID放到数组里
		List<String> oidArr = SplitString.string2List(oids, ",");
		List<RecDesInfo> recDesInfos = new ArrayList<RecDesInfo>();

		for(String recDesOid : oidArr){
			String classId = Helper.getPersistService().getClassId(recDesOid);
			String innerId = Helper.getPersistService().getInnerId(recDesOid);

			RecDesInfo recDesInfo = new RecDesInfo();

			recDesInfo.setClassId(classId);
			recDesInfo.setInnerId(innerId);

			recDesInfos.add(recDesInfo);
		}

		//批量删除回收销毁信息
		Helper.getPersistService().deleteAll(recDesInfos);

	}
	/**
	 * 查询发放信息
	 * 
	 * @param disInfoID
	 *			 分发信息IID（人员或组织的内部标识）
	 * @param disInfoName
	 *			分发信息名称（单位/人员）
	 * @return List<Map<String, Object>>
	 *			发放信息数据List集合
	 */
	public List<Map<String, Object>> getDistributeInfo(String disInfoID, String disInfoName){
		String sql = " SELECT D.* from DDM_DIS_INFO D " ;
		List<Map<String, Object>> disInfo = new ArrayList<Map<String,Object>>();
		if(null != disInfoID && null != disInfoName && !"".equals(disInfoID) && !"".equals(disInfoName)){
			sql += "WHERE D.DISINFOID = ? AND D.DISINFONAME= ? ";
			//执行查询
			disInfo = Helper.getPersistService().query(sql, disInfoID, disInfoName);
		}
		return disInfo;
	}

	/*
	 * （非 Javadoc）
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#List<Map<String, Object>> checkRecDistributeOrder(String oid)
	 */
	@Override
	public List<Map<String, Object>> checkRecDistributeOrder(String oid) {
		String classId = Helper.getClassId(oid);
		String innerId = Helper.getInnerId(oid);

		String sql = " SELECT F .* FROM (SELECT DISTINCT C.INNERID, C.CLASSID FROM (SELECT DISTINCT "
				+ " A .INNERID,A .CLASSID,A .TOOBJECTCLASSID,A .TOOBJECTID FROM DDM_DIS_ORDEROBJLINK A "
				+ " WHERE A .FROMOBJECTCLASSID = ? AND A .FROMOBJECTID = ? ) B, DDM_DIS_ORDEROBJLINK C, "
				+ " DDM_DIS_ORDER D WHERE C.TOOBJECTCLASSID = B.TOOBJECTCLASSID AND C.TOOBJECTID = B.TOOBJECTID "
				+ " AND C.CLASSID = B.CLASSID AND C.INNERID <> B.INNERID AND C.FROMOBJECTCLASSID = D.CLASSID "
				+ " AND C.FROMOBJECTID = D.INNERID And D.ORDERTYPE = ? ) E, DDM_RECDES_INFO F WHERE "
				+ " F.DISORDEROBJECTLINKCLASSID = E.CLASSID AND F.DISORDEROBJECTLINKID = E.INNERID AND F.STATENAME <> ? ";

		//回收发放单
		String orderType = ConstUtil.C_ORDERTYPE_2;
		//回收“已分发”状态名字
		String stateName = ConstUtil.LC_COMPLETED.getName();

		//执行查询
		List<Map<String, Object>> listRecOrderInfos = Helper.getPersistService()
				.query(sql, classId, innerId, orderType, stateName);
		if(listRecOrderInfos == null || listRecOrderInfos.isEmpty()){
			listRecOrderInfos = new ArrayList<Map<String,Object>>();
		}

		return listRecOrderInfos;
	}

	/*
	 * （非 Javadoc）
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#List<Map<String, Object>> checkDesDistributeOrder(String oid)
	 */
	@Override
	public List<Map<String, Object>> checkDesDistributeOrder(String oid) {
		String classId = Helper.getClassId(oid);
		String innerId = Helper.getInnerId(oid);
		String sql = " SELECT F .* FROM (SELECT DISTINCT C.INNERID, C.CLASSID FROM (SELECT DISTINCT "
				+ " A .INNERID,A .CLASSID,A .TOOBJECTCLASSID,A .TOOBJECTID FROM DDM_DIS_ORDEROBJLINK A "
				+ " WHERE A .FROMOBJECTCLASSID = ? AND A .FROMOBJECTID = ? ) B, DDM_DIS_ORDEROBJLINK C, "
				+ " DDM_DIS_ORDER D WHERE C.TOOBJECTCLASSID = B.TOOBJECTCLASSID AND C.TOOBJECTID = B.TOOBJECTID "
				+ " AND C.CLASSID = B.CLASSID AND C.INNERID <> B.INNERID AND C.FROMOBJECTCLASSID = D.CLASSID "
				+ " AND C.FROMOBJECTID = D.INNERID And D.ORDERTYPE = ? ) E, DDM_RECDES_INFO F WHERE "
				+ " F.DISORDEROBJECTLINKCLASSID = E.CLASSID AND F.DISORDEROBJECTLINKID = E.INNERID AND F.STATENAME <> ? ";

		//销毁发放单
		String orderType = ConstUtil.C_ORDERTYPE_3;
		//销毁“已完成”状态名字
		String stateName = ConstUtil.LC_COMPLETED.getName();
		//执行查询
		List<Map<String, Object>> listDesOrderInfos = Helper.getPersistService()
				.query(sql, classId, innerId, orderType, stateName);
		if(listDesOrderInfos == null || listDesOrderInfos.isEmpty()){
			listDesOrderInfos = new ArrayList<Map<String,Object>>();
		}

		return listDesOrderInfos;
	}

	/**
	 * 查询发放Link信息
	 * 
	 * @param distributeOrderOid
	 *			发放单OID
	 * @return
	 */
	public List<DistributeOrderObjectLink> getObjectLinkByDistributeOrderOid (String distributeOrderOid){
		String hql = "from DistributeOrderObjectLink t "
				+ "where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? ";
		//发放单InnerId
		String distributeOrderInnerId = Helper.getInnerId(distributeOrderOid);
		//发放单ClassId
		String distributeOrderClassId = Helper.getClassId(distributeOrderOid);
		//查询Link信息
		List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, distributeOrderInnerId,
				distributeOrderClassId);
		return links;
	}

	/**
	 * 验证是否给某个单位或人员发放过某个发放数据
	 * 
	 * @param disOrderObjLinkOid
	 *			发放Link信息Oid
	 * @param disInfoId
	 *			分发信息IID（人员或组织的内部标识）
	 * @param disInfoName
	 *			分发信息名称（单位/人员）
	 * @return 是否发放过某个数据
	 */
	public boolean checkDistributedByDisInfoIdAndDisInfoName(String disOrderObjLinkOid, String disInfoId, String disInfoName) {
		//发放Link信息InnerId
		String disOrderObjLinkId = Helper.getInnerId(disOrderObjLinkOid);
		//发放Link信息ClassId
		String disOrderObjLinkClassId = Helper.getClassId(disOrderObjLinkOid);
		String sql = "SELECT DISTINCT B.* FROM DDM_DIS_ORDEROBJLINK A, DDM_DIS_INFO B WHERE A.INNERID = ? "
				+ " AND A.CLASSID = ? AND A.CLASSID = B.DISORDEROBJLINKCLASSID AND "
				+ " A.INNERID = B.DISORDEROBJLINKID AND B.DISINFOID = ? AND B.DISINFONAME = ? AND "
				+ " B.STATENAME = ? ";
		//未分发标记
		String stateName = ConstUtil.LC_NOT_DISTRIBUT.getName();
		//执行查询
		List<Map<String, Object>> listDesOrderInfos = Helper.getPersistService()
				.query(sql, disOrderObjLinkId, disOrderObjLinkClassId, disInfoId, disInfoName, stateName);
		if(listDesOrderInfos.size() > 0){
			return true;
		}
		return false;

	}


	/**
	 * 回收信息需要合并的判断条件拼成字符串
	 *
	 * @param disInfo
	 *			分发信息IID（人员或组织的内部标识）
	 * @param disName
	 *			分发信息名称（单位/人员）
	 * @param disInfoNum
	 *			发放份数
	 * @param recoverNum
	 *			回收份数
	 * @param DisMediaType
	 *			发放类型
	 * @param disInfoType
	 *			分发信息类型（0为单位，1为人员）
	 * @return result
	 *			去重字符串标示符
	 */
	public String recInfoCombine(String disInfo, String disName, long disInfoNum, long recoverNum, long DisMediaType, long disInfoType){
		String result = disInfo + ";"
				+ disName + ";"
				+ disInfoNum + ";"
				+ recoverNum + ";"
				+ DisMediaType + ";"
				+ disInfoType;
		return result;
		
	}

	/**
	 * 销毁信息需要合并的判断条件拼成字符串
	 *
	 * @param disInfo
	 *			分发信息IID（人员或组织的内部标识）
	 * @param disName
	 *			分发信息名称（单位/人员）
	 * @param disInfoNum
	 *			发放份数
	 * @param recoverNum
	 *			回收份数
	 * @param DisMediaType
	 *			发放类型
	 * @param disInfoType
	 *			分发信息类型（0为单位，1为人员）
	 * @return result
	 *			去重字符串标示符
	 */
	public String desInfoCombine(String disInfo, String disName, long disInfoNum, long destroyNum, long DisMediaType, long disInfoType){
		String result = disInfo + ";"
				+ disName + ";"
				+ disInfoNum + ";"
				+ destroyNum + ";"
				+ DisMediaType +";"
				+ disInfoType;
		return result;
		
	}

	/**
	 * 回收销毁信息需要合并的判断条件拼成字符串
	 *
	 * @param disInfo
	 *			分发信息IID（人员或组织的内部标识）
	 * @param disName
	 *			分发信息名称（单位/人员）
	 * @param disInfoNum
	 *			发放份数
	 * @param recoverNum
	 *			回收份数
	 * @param DisMediaType
	 *			发放类型
	 * @param disInfoType
	 *			分发信息类型（0为单位，1为人员）
	 * @param stateId
	 *			状态ID
	 * @param stateName
	 *			状态名称
	 * @return result
	 *			去重字符串标示符
	 */
	public String recDesInfoCombine(String disInfo, String disName, long needRecoverNum, long needDestroyNum, String DisMediaType, String disInfoType, String stateId, String stateName){
		String result = disInfo + ";"
				+ disName + ";"
				+ needRecoverNum + ";"
				+ needDestroyNum + ";"
				+ DisMediaType + ";"
				+ disInfoType
				+ stateId + ";"
				+ stateName;
		return result;
		
	}

	/**
	 * @author sunzongqing
	 */
	public List<RecDesInfo> getpapertaskinfo(String orderObjectLinkOids, String taskOid) {
		String innerId = Helper.getInnerId(taskOid);
		String classId = Helper.getClassId(taskOid);
		List<String> oidsList = SplitString.string2List(orderObjectLinkOids, ",");
		List<DistributeOrderObjectLink> linksAll = new ArrayList<DistributeOrderObjectLink>();
		List<RecDesInfo> list = new ArrayList<RecDesInfo>();
		List<String> keyList = new ArrayList<String>();

		if (oidsList == null || oidsList.isEmpty()) {
			String sql = "SELECT distinct  C.* "
					+ "FROM DDM_RECDES_INFO A, "
					+ "DDM_DIS_TASKINFOLINK B, "
					+ "DDM_DIS_ORDEROBJLINK C "
					+ "WHERE A.CLASSID = B.TOOBJECTCLASSID "
					+ "AND A.INNERID = B.TOOBJECTID "
					+ "AND A.DISORDEROBJECTLINKID = C.INNERID "
					+ "AND A.DISORDEROBJECTLINKCLASSID = C.CLASSID "
					+ "AND B.FROMOBJECTID = ? "
					+ "AND B.FROMOBJECTCLASSID = ? ";

			List<DistributeOrderObjectLink> linkList = PersistHelper.getService().query(sql,
					DistributeOrderObjectLink.class, innerId, classId);
			linksAll.addAll(linkList);
		} else {
			String hql = "from DistributeOrderObjectLink t " 
					+ "where t.innerId=? "
					+ "and t.classId=? ";
			for (String linkOid : oidsList) {
				String innerid = Helper.getPersistService().getInnerId(linkOid);
				String classid = Helper.getPersistService().getClassId(linkOid);

				List<DistributeOrderObjectLink> linkList = PersistHelper.getService().find(hql, innerid, classid);
				linksAll.addAll(linkList);

			}
		}
		String sqlinfo = "select t.* "
				+ "from  ddm_recdes_info t , "
				+ "ddm_dis_taskinfolink b  "
				+ "where t.disorderobjectlinkid=? "
				+ "and disorderobjectlinkclassid=? "
				+ "and b.toobjectid=t.innerid "
				+ "and b.toobjectclassid=t.classid "
				+ "and b.fromobjectclassid='RecDesPaperTask' ";
		if (linksAll != null && linksAll.size() > 0) {
			for (DistributeOrderObjectLink link : linksAll) {
				String objinner = link.getInnerId();
				String objclass = link.getClassId();
				List<RecDesInfo> infos = PersistHelper.getService().query(sqlinfo, RecDesInfo.class, objinner,
						objclass);
				for (RecDesInfo info : infos) {
					String disInfo = info.getDisInfoId();
					String disName = info.getDisInfoName();
					long needRecoverNum = info.getNeedRecoverNum();
					long needDestroyNum = info.getNeedDestroyNum();
					String DisMediaType = info.getDisMediaType();
					String disInfoType = info.getDisInfoType();
					String stateId = info.getLifeCycleInfo().getStateId();
					String stateName = info.getLifeCycleInfo().getStateName();
					String stringFlag = recDesInfoCombine(disInfo, disName, needRecoverNum, 
							needDestroyNum, DisMediaType, disInfoType, stateId, stateName);
					if (keyList.contains(stringFlag)) {
						continue;
					}
					info.addOid(info.getOid());
					keyList.add(stringFlag);
					info.setDisDeadLine(link.getDisDeadLine());
					info.setDisUrgent(link.getDisUrgent());
					list.add(info);
				}
			}
		}
		return list;

	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.recdesinfo.RecDesInfoService#deleteDistributeRecDesInfoByOid(DistributeOrderObjectLink link)
	 */
	@Override
	public void deleteDistributeRecDesInfoByOid(DistributeOrderObjectLink link) {

		String hql = "from RecDesInfo r where r.disOrderObjectLinkId=? and r.disOrderObjectLinkClassId=?";
		//根据分发数据link查询回收销毁信息
		List<RecDesInfo> recDesInfolist = PersistHelper.getService().find(hql, link.getInnerId(), link.getClassId());

		if(recDesInfolist.size() > 0){
			//删除回收销毁分发信息
			Helper.getPersistService().delete(recDesInfolist);
		}
		//删除分发数据link
		Helper.getPersistService().delete(link);
	}

}
