package com.bjsasc.ddm.distribute.service.DistributeInfoOperation;

import java.util.ArrayList;
import java.util.List;




import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.util.StringUtil;
/**
 * 
 * @author kangyanfei 2014-07-15
 *
 */
public class DistributeOperationServiceImpl implements
		DistributeOperationService {

	/* （非 Javadoc）
	 * @see  com.bjsasc.ddm.distribute.service.DistributeOperation#getDistributeObjectsByDisOrder
	(DistributeOrder disOrder)
	 */
	@Override
	public List<DistributeObject> getDistributeObjectsByDisOrder(DistributeOrder disOrder) {
		List<DistributeObject> disObjects = null;
		//判断分发单是否未被初始化。
		if(disOrder != null){
			//查询分发对象
			disObjects = DistributeHelper.getDistributeObjectService().getDistributeObjectsByDistributeOrderOid(disOrder.getOid());
		} else {
			disObjects = new ArrayList<DistributeObject>();
		}
		return disObjects;
	}

	/* （非 Javadoc）
	 * @see  com.bjsasc.ddm.distribute.service.DistributeOperation#DistributeOperationService
	(DistributeOrder disOrderOid,DistributeObject disObjOid, List<DistributeInfo> disInfos)
	 */
	@Override
	public void inserDisInfos(DistributeOrder disOrder,
			DistributeObject disObj, List<DistributeInfo> disInfos) {
		//发放单的ClassId和InnerId
		String disOrderInnerId = disOrder.getInnerId();
		String disOrderClassId = disOrder.getClassId();
		//分发对象的ClassId和InnerId
		String distributeObjectInnerId = disObj.getInnerId();
		String distributeObjectClassId = disObj.getClassId();

		String hql = "from DistributeOrderObjectLink t "
				+ " where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? "
				+ " and t.toObjectRef.innerId=? and t.toObjectRef.classId=? ";

		//存放批量将要保存的分发信息
		List<DistributeInfo> saveDisInfos = new ArrayList<DistributeInfo>();
		//查询发放单和分发数据间Links
		List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, disOrderInnerId,
				disOrderClassId, distributeObjectInnerId, distributeObjectClassId);
		if(links.size() > 0){
			String disOrderObjLinkId = links.get(0).getInnerId();
			String disOrderObjLinkClassId = links.get(0).getClassId();
			DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
			for(DistributeInfo disInfo :disInfos){
				DistributeInfo saveDisInfo = (DistributeInfo) PersistUtil.createObject(DistributeInfo.CLASSID);

				//设置发放单与分发数据LINK类标识 
				saveDisInfo.setDisOrderObjLinkClassId(disOrderObjLinkClassId);
				//设置发放单与分发数据LINK内部标识 
				saveDisInfo.setDisOrderObjLinkId(disOrderObjLinkId);
				//设置分发信息IID（人员或组织的内部标识）
				if(!StringUtil.isStringEmpty(disInfo.getDisInfoId())){
					saveDisInfo.setDisInfoId(disInfo.getDisInfoId());
				}
				//设置分发信息名称（单位/人员）
				if(!StringUtil.isStringEmpty(disInfo.getDisInfoName())){
					saveDisInfo.setDisInfoName(disInfo.getDisInfoName());
				}
				//设置分发信息的类标识（人员或者组织的类标识）
				if(!StringUtil.isStringEmpty(disInfo.getInfoClassId())){
					saveDisInfo.setInfoClassId(disInfo.getInfoClassId());
				}
				//设置外域接收人IID（人员内部标识）
				if(!StringUtil.isStringEmpty(disInfo.getOutSignId())){
					saveDisInfo.setOutSignId(disInfo.getOutSignId());
				}
				//设置 外域接收人名称（人员）
				if(!StringUtil.isStringEmpty(disInfo.getOutSignName())){
					saveDisInfo.setOutSignName(disInfo.getOutSignName());
				}
				//设置外域接收人的类标识（人员类标识）
				if(!StringUtil.isStringEmpty(disInfo.getOutSignClassId())){
					saveDisInfo.setOutSignClassId(disInfo.getOutSignClassId());
				}
				//分发信息类型（0为单位，1为人员）
				if(!StringUtil.isStringEmpty(disInfo.getDisInfoType())){
					saveDisInfo.setDisInfoType(disInfo.getDisInfoType());
				}
				//设置分发份数 
				if(disInfo.getDisInfoNum() > 0){
					saveDisInfo.setDisInfoNum(disInfo.getDisInfoNum());
				}
				//设置回收份数 
				if(disInfo.getRecoverNum() > 0){
					saveDisInfo.setRecoverNum(disInfo.getRecoverNum());
				}
				//设置销毁份数
				if(disInfo.getDestroyNum() > 0){
					saveDisInfo.setDestroyNum(disInfo.getDestroyNum());
				}
				//设置分发介质类型（0为纸质，1为电子，2为跨域）
				if(!StringUtil.isStringEmpty(disInfo.getDisMediaType())){
					saveDisInfo.setDisMediaType(disInfo.getDisMediaType());
				}
				//设置分发方式（0为直接分发，1为补发，2为移除，3为转发）
				if(!StringUtil.isStringEmpty(disInfo.getDisType())){
					saveDisInfo.setDisType(disInfo.getDisType());
				}
				//设置组织类型 (0为内部,1为外部)
				if(!StringUtil.isStringEmpty(disInfo.getSendType())){
					saveDisInfo.setSendType(disInfo.getSendType());
				}
				//设置分发信息变为已分发状态的时间
				if(disInfo.getSendTime() > 0){
					saveDisInfo.setSendTime(disInfo.getSendTime());
				}
				//设置备注
				if(!StringUtil.isStringEmpty(disInfo.getNote())){
					saveDisInfo.setNote(disInfo.getNote());
				}
				//盖章信息
				if(!StringUtil.isStringEmpty(disInfo.getSealInfo())){
					saveDisInfo.setSealInfo(disInfo.getSealInfo());
				}

				// 生命周期初始化
				life.initLifecycle(saveDisInfo);

				//保存到批量保存集合里
				saveDisInfos.add(saveDisInfo);
			}
		}

		//批量保存分发信息
		PersistHelper.getService().save(saveDisInfos);
	}

}
