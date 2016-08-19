package com.bjsasc.ddm.distribute.service.distributeorderobjectlink;

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
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService;
import com.bjsasc.ddm.distribute.service.distributeoptvalidation.DistributeOptValidationService;
import com.bjsasc.ddm.transfer.helper.DdmDataTransferHelper;
import com.bjsasc.ddm.transfer.service.DdmDataTransferService;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.system.access.AccessControlHelper;
import com.bjsasc.plm.core.system.access.operate.Operate;
import com.bjsasc.plm.core.util.StringUtil;
import com.cascc.avidm.util.SplitString;

/**
 * 发放单与分发数据link服务实现类。
 * 
 * @author gengancong 2013-2-22
 */
@SuppressWarnings({"deprecation","unchecked","static-access"})
public class DistributeOrderObjectLinkServiceImpl implements DistributeOrderObjectLinkService {
	/** 生命周期服务 */
	private final DistributeLifecycleService lifeService = DistributeHelper.getDistributeLifecycleService();
	@Override
	public List<DistributeOrderObjectLink> getDistributeOrderObjectLinkListByDistributeObjectOid(
			String distributeObjectOid) {
		String innerId = Helper.getInnerId(distributeObjectOid);
		String classId = Helper.getClassId(distributeObjectOid);
		String hql = "from DistributeOrderObjectLink t where t.toObjectRef.innerId=? and t.toObjectRef.classId=? ";

		List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, innerId, classId);
		return links;
	}
	
	@Override
	public List<DistributeOrderObjectLink> getDistributeOrderObjectLinkListByDisObjOidAndDisOrderOids(
			String distributeObjectOid,String distributeOrderOids) {
		List<Object> paramList = new ArrayList<Object>();
		String innerId = Helper.getInnerId(distributeObjectOid);
		paramList.add(innerId);
		String classId = Helper.getClassId(distributeObjectOid);
		paramList.add(classId);
		String hql = "from DistributeOrderObjectLink t where t.toObjectRef.innerId=? and t.toObjectRef.classId=? ";
		String strDisOrderWherehql = "";
		if(!StringUtil.isStringEmpty(distributeOrderOids)){
			List<String> disOrderOidList = SplitString.string2List(distributeOrderOids, ",");
			for (String disOrderOid : disOrderOidList) {
				String disOrderInnerId = Helper.getInnerId(disOrderOid);
				String disOrderClassId = Helper.getClassId(disOrderOid);
				strDisOrderWherehql += "(t.fromObjectRef.innerId=? and t.fromObjectRef.classId=?) or";
				paramList.add(disOrderInnerId);
				paramList.add(disOrderClassId);
			}
			strDisOrderWherehql = strDisOrderWherehql.substring(0,strDisOrderWherehql.length() -3 );
		}
		if(!StringUtil.isStringEmpty(strDisOrderWherehql)){
			hql += " and (" + strDisOrderWherehql + ") ";
		}
		List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, paramList.toArray());
		return links;
	}
	
	/**
	 * @author kangyanfei
	 * 2014-08-19
	 */
	@Override
	public List<DistributeOrderObjectLink> getDistributeOrderObjectLinkListByDistributeOrderOid(
			String distributeOrderOid) {
		String innerId = Helper.getInnerId(distributeOrderOid);
		String classId = Helper.getClassId(distributeOrderOid);
		String hql = "from DistributeOrderObjectLink t where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? ";

		List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, innerId, classId);
		return links;
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService#createDistributeOrderObjectLink(com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink)
	 */
	@Override
	public void createDistributeOrderObjectLink(DistributeOrderObjectLink disObj) {
		Helper.getPersistService().save(disObj);
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService#newDistributeOrderObjectLink()
	 */
	@Override
	public DistributeOrderObjectLink newDistributeOrderObjectLink() {
		DistributeOrderObjectLink disObj = (DistributeOrderObjectLink) PersistUtil
				.createObject(DistributeOrderObjectLink.CLASSID);

		disObj.setClassId(disObj.CLASSID);
		return disObj;
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService#creteOrderObjectLink(java.lang.String.java.Util.List)
	 */
	@Override
	public List<Map<String,String>> creteOrderObjectLink(DistributeOrder disOrd, List<Persistable> collectRefResList,boolean aotuCreateFlag) {
		// 分发对象服务
		DistributeObjectService objService = DistributeHelper.getDistributeObjectService();

		// 相关对象List长度
		int collectRefResListSize = collectRefResList.size();

		List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
		// 发放单主对象
		boolean masterFlag = objService.getMasterLink(disOrd.getOid());

		// 发放单对象
		String sql = "SELECT disOrd.* FROM DDM_DIS_ORDER disOrd "
				+ " WHERE disOrd.CLASSID || ':' || disOrd.INNERID = ? ";
		List<DistributeOrder> orderList = Helper.getPersistService().query(sql,
						DistributeOrder.class, disOrd.getOid());
				
		for (int i = 0; i < collectRefResListSize; i++) {
			Persistable obj = collectRefResList.get(i);
			String objOid = Helper.getOid(obj.getClassId(), obj.getInnerId());
			// 根据当前分发数据源对象取得分发数据对象List
			List<DistributeObject> disObjList = objService.getDistributeObjectsByDataOid(objOid);

			Map<String,String> map = new HashMap<String,String>();
			map.put("DATAOID",objOid);
			// 当前分发数据源未发放Flag
			boolean objFlag = disObjList == null || disObjList.isEmpty();
			//分发数据对象
			DistributeObject disObject;
			// 当前分发数据源未发放
			if (objFlag) {
				// 创建分发数据对象
				disObject = getDistributeObject(obj);
				// 分发数据对象保存
				objService.createDistributeObject(disObject);
				map.put("FLAG", ConstUtil.C_S_ZERO);
				// 当前分发数据源已发放
			} else {
				// 取得分发数据对象
				disObject = disObjList.get(0);
				map.put("FLAG", ConstUtil.C_S_ONE);
			}

			// 发放单与分发数据link OID
			String linkOid;
			// 当前分发数据源是分发数据源(主对象)
			int index = objOid.indexOf(":");
			if (!masterFlag) {
				//	当更改内对象只有一个时，主对象设置成其内对象，而不再是更改单本身。
				if (collectRefResListSize == 2 && objOid.substring(0, index).equals("ECO")){
					// 发放单与分发数据link表创建与保存
					linkOid = objService.createDistributeOrderObjectLink(disOrd, disObject, ConstUtil.C_S_ZERO);
					map.put("ISMASTER",ConstUtil.C_S_ZERO);
				}else{
					masterFlag = true;
					// 设置发放单上下文信息
					disOrd.setContextInfo(disObject.getContextInfo());
					// 设置发放单域信息
					disOrd.setDomainInfo(disObject.getDomainInfo());
					lifeService.initLifecycle(disOrd);
					//检查权限
					DistributeOptValidationService validationService = DistributeHelper.getDistributeOptValidationService();
					boolean flag = validationService.getCreateDistributeOrderValidation(Operate.CREATE, disOrd);
					if(flag == false){
						AccessControlHelper.getService().checkEntityPermission(Operate.CREATE, disOrd);
					}
					// 发放单与分发数据(主对象)link表创建与保存
					linkOid = objService.createDistributeOrderObjectLink(disOrd, disObject, ConstUtil.C_S_ONE);
					map.put("ISMASTER",ConstUtil.C_S_ONE);	
					disOrd.setMasterDataClassID(disObject.getDataClassId());
					
					if (orderList==null || orderList.isEmpty()){
						Helper.getPersistService().save(disOrd);
					} else {
						Helper.getPersistService().update(disOrd);
					}

					if (aotuCreateFlag) {
						DistributeLifecycleService lifeService = DistributeHelper.getDistributeLifecycleService();
						lifeService.promoteLifeCycle(disOrd);
						Helper.getPersistService().update(disOrd);
					}
				}
				// 当前分发数据源不是分发数据源(主对象)
			} else {
				// 发放单与分发数据link表创建与保存
				linkOid = objService.createDistributeOrderObjectLink(disOrd, disObject, ConstUtil.C_S_ZERO);
				map.put("ISMASTER",ConstUtil.C_S_ZERO);
			}
			map.put("LINKOID",linkOid);

			listMap.add(map);
		}
		return listMap;
	}

	/**
	 * 取得转换后的分发数据对象。
	 * 
	 * @param obj Persistable
	 * @return DistributeObject
	 */
	private DistributeObject getDistributeObject(Persistable obj) {
		// 数据转换服务
		DdmDataTransferService tranService = DdmDataTransferHelper.getDistributeObjectService();
		DistributeObject disObject = tranService.transferToDdmData(obj);
		return disObject;
	}

	@Override
	public boolean hasOneDisStyle(String dataOid) {
		String sql = "SELECT COUNT (A .INNERID) AS SUM FROM DDM_DIS_OBJECT A, DDM_DIS_ORDEROBJLINK B WHERE A .DATACLASSID || ':' || A .DATAINNERID = ? AND A .CLASSID || ':' || A .INNERID = B.TOOBJECTCLASSID || ':' || B.TOOBJECTID AND B.DISSTYLE = 1";
		List<Map<String, Object>> list = Helper.getPersistService().query(sql, dataOid);
		int count = ((BigDecimal) list.get(0).get("SUM")).intValue();
		if(count>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public List<DistributeOrderObjectLink> getDistributeOrderObjectLinkListByDisPaperTaskOID(
			String oid) {
		String innerId=Helper.getInnerId(oid);
		String classId=Helper.getClassId(oid);
		String disobjsql = "select objLink.* from DDM_DIS_TASKINFOLINK t,DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink"
				+ " where disInfo.innerId = t.toObjectId and disInfo.classId = t.toObjectClassId"
				+ " and objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
				+ " and t.fromObjectId = ? and t.fromObjectClassId = ?";
		List<DistributeOrderObjectLink> objLinkList = Helper.getPersistService().query(disobjsql, DistributeOrderObjectLink.class, innerId,classId);
		return objLinkList;
		
	}
}
