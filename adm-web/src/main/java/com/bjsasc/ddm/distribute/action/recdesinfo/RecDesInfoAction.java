package com.bjsasc.ddm.distribute.action.recdesinfo;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.common.ConstUtil.DISPLAY_TYPE;
import com.bjsasc.ddm.distribute.action.AbstractAction;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.recdesinfo.RecDesInfo;
import com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService;
import com.bjsasc.ddm.distribute.service.recdesinfo.RecDesInfoService;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.util.DateTimeUtil;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.bjsasc.plm.type.TypeService;
import com.cascc.avidm.util.SplitString;

/**
 * 回收销毁发放单Action实现类。
 * 
 * @author kangyanfei 2013/05/28
 */
public class RecDesInfoAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = -200015769553075807L;

	/** RecDesInfoService */
	private final RecDesInfoService service = DistributeHelper.getRecDesInfoService();
	private static final Logger LOG = Logger.getLogger(RecDesInfoAction.class);
	/** DistributeObjectService */
	DistributeObjectService disObjservice = DistributeHelper.getDistributeObjectService();

	/**
	 * 取得回收信息通过Session里（DISTRIBUTE_ORDER_OBJECT_LINK_OIDS）
	 * 
	 * @return JSON对象
	 */
	public String getRecInfoByLinkOids() {
		try {

			String linkOidArr = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
			List<String> linkOidList = SplitString.string2List(linkOidArr, ",");

			// 获取发放单OIDS
			String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);

			// 取得回收单数据。
			List<RecDesInfo> recInfoList = service.getAllRecInfo(distributeOrderOid, linkOidList);
			if(recInfoList.size() > 0){
				//设置默认紧急程度
				String disUrgent = recInfoList.get(0).getDisUrgent();
				if(!StringUtil.isStringEmpty(disUrgent)){
					request.setAttribute("disUrgent", disUrgent);
				}
				//设置默认完成期限
				long disDeadLine = recInfoList.get(0).getDisDeadLine();
				if(disDeadLine > 0){
					request.setAttribute("deadLineDate", DateTimeUtil.getDateDisplay(disDeadLine));
				}
			}

			LOG.debug("取得回收单信息 " + recInfoList.size() + " 条");

			TypeService typeManager = Helper.getTypeManager();

			for(RecDesInfo recInfo: recInfoList){
				Map<String, Object> mapValue = new HashMap<String, Object>();
				Map<String, Object> attrValues = typeManager.getAttrValues(recInfo);
				mapValue.putAll(attrValues);
				listMap.add(mapValue);
			}
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTRECINFO);
		} catch (Exception ex) {
			error(ex);
		}
		return "listRec";

	}

	/**
	 * 保存Session里LinkOids数据。
	 * 
	 * @return JSON对象
	 */
	public String setLinkOids() {
		try {
			//得到所选择的distributeOrderObjectLinkOids
			String linkOids = request.getParameter("distributeOrderObjectLinkOids");
			//保存到Session
			session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS, linkOids);
	
			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
		
	}

	/**
	 * 取得销毁信息通过Session里（DISTRIBUTE_ORDER_OBJECT_LINK_OIDS）
	 * 
	 * @return JSON对象
	 */
	public String getDesInfoByLinkOids() {
		try {

			String linkOidArr = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
			List<String> linkOidList = SplitString.string2List(linkOidArr, ",");

			// 获取发放单OIDS
			String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);

			// 取得销毁单数据。
			List<RecDesInfo> desInfoList = service.getAllDesInfo(distributeOrderOid, linkOidList);
			if(desInfoList.size() > 0){
				//设置默认紧急程度
				String disUrgent = desInfoList.get(0).getDisUrgent();
				if(!StringUtil.isStringEmpty(disUrgent)){
					request.setAttribute("disUrgent", disUrgent);
				}
				//设置默认完成期限
				long disDeadLine = desInfoList.get(0).getDisDeadLine();
				if(disDeadLine > 0){
					request.setAttribute("deadLineDate", DateTimeUtil.getDateDisplay(disDeadLine));
				}
			}
			LOG.debug("取得销毁信息" + desInfoList.size() + " 条");

			TypeService typeManager = Helper.getTypeManager();

			for(RecDesInfo desInfo: desInfoList){
				Map<String, Object> mapValue = new HashMap<String, Object>();
				Map<String, Object> attrValues = typeManager.getAttrValues(desInfo);
				mapValue.putAll(attrValues);
				listMap.add(mapValue);
			}

			// 输出结果
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDESINFO);
			
		} catch (Exception ex) {
			error(ex);
		}
		return "listDes";
	}
	
	/**
	 * 发放管理--发放单
	 * 已分发--【回收单】
	 * @return struts页面参数
	 */
	public String addRecDistributeOrder(){
		
		try {
			String number = request.getParameter("NUMBER");// 编号
			String name = request.getParameter("NAME");// 名称
			String orderType = request.getParameter("orderType");// 单据类型
			String note = request.getParameter("NOTE");// 备注

			String orderOidsStr = request.getParameter("orderOidsStr");
			DistributeOrder disOrder = service.addResDistributeOrder(
					orderOidsStr, number, name, orderType, note);

			if (disOrder != null) {
				result.put("disOrderOid", disOrder.getOid());
				success();
			} else {
				result.put("success", "message");
				result.put("message",
						ConstUtil.CREATE_REC_DISTRIBUTEORDER_ERROE);
			}

		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
		
	}

	/**
	 * 发放管理--发放单
	 * 已分发--【销毁单】
	 * @return struts页面参数
	 */
	public String addDesDistributeOrder(){
		
		try {
			String number = request.getParameter("NUMBER");// 编号
			String name = request.getParameter("NAME");// 名称
			String orderType = request.getParameter("orderType");// 单据类型
			String note = request.getParameter("NOTE");// 备注

			String orderOidsStr = request.getParameter("orderOidsStr");
			DistributeOrder disOrder = service.addDesDistributeOrder(
					orderOidsStr, number, name, orderType, note);

			if (disOrder != null) {
				result.put("disOrderOid", disOrder.getOid());
				success();
			} else {
				result.put("success", "message");
				result.put("message",
						ConstUtil.CREATE_DES_DISTRIBUTEORDER_ERROE);
			}

		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
		
	}

	/*
	 * 取得发放单信息
	 * 
	 * @return 添加回收信息列表页面
	 */
	public String getAllNeddRecInfos (){
		try {
			//获取发放单OIDS
			String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
			String linkOids = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
			List<String> linkOidList = SplitString.string2List(linkOids, ",");
			//获得需要回收的发放信息
			List<Map<String, Object>> listRecInfo = service.getAllNeddRecInfosByDistributeOrderOid(distributeOrderOid, linkOidList);
			
			TypeService typeManager = Helper.getTypeManager();
			
			for(Map<String, Object> recInfo:listRecInfo){
				Map<String, Object> mapValue = new HashMap<String, Object>();
				RecDesInfo recDesInfo = new RecDesInfo();
				
				//分发信息IID（人员或组织的内部标识）
				String disInfoId = (String) recInfo.get("DISINFOID");
				//分发信息IID（人员或组织的内部标识）
				String disInfoName = (String) recInfo.get("DISINFONAME");
				//分发信息类型（0为单位，1为人员）
				String disInfoType = ((BigDecimal) recInfo.get("DISINFOTYPE")).toString();
				//发放单与分发数据LINK内部标识
				String disOrderObjLinkId = (String) recInfo.get("DISORDEROBJLINKID");
				//发放单与分发数据LINK类标识
				String disOrderObjLinkClassId = (String) recInfo.get("DISORDEROBJLINKCLASSID");
				//分发介质类型（0为纸质，1为电子，2为跨域）
				String disMediaType = String.valueOf(((BigDecimal)recInfo.get("DISMEDIATYPE")).longValue());
				//已回收份数
				long recoverNum = ((BigDecimal)recInfo.get("RECOVERNUM")).longValue();
				//添加需要回收份数
				long needRecoverNum = 0;
				//发放份数
				long disInfoNum = ((BigDecimal)recInfo.get("DISINFONUM")).longValue();
				//发放数据名称
				String disObjName = (String) recInfo.get("NAME");
				//发放数据ID
				String disObjId = (String) recInfo.get("ID");
				
				//设置类内部表示符
				recDesInfo.setClassId(RecDesInfo.CLASSID);
				//设置需要回收的份数
				recDesInfo.setNeedRecoverNum(needRecoverNum);
				//设置发放单与分发数据LINK内部标识
				recDesInfo.setDisOrderObjectLinkClassId(disOrderObjLinkClassId);
				//设置发放单与分发数据LINK类标识
				recDesInfo.setDisOrderObjectLinkId(disOrderObjLinkId);
				//设置外域接收人IID（人员内部标识）
				recDesInfo.setDisInfoId(disInfoId);
				//设置外域接收人名称（人员）
				recDesInfo.setDisInfoName(disInfoName);
				//设置分发介质类型（0为纸质，1为电子，2为跨域）集合
				recDesInfo.setDisMediaType(disMediaType);
				//设置分发份数
				recDesInfo.setDisInfoNum(disInfoNum);
				//设置已回收份数
				recDesInfo.setRecoverNum(recoverNum);
				//设置发放数据ID
				recDesInfo.setDisObjId(disObjId);
				//设置发放数据名称
				recDesInfo.setDisObjName(disObjName);
				//设置发放备注
				recDesInfo.setNote("");
				//设置分发信息类型（0为单位，1为人员）
				recDesInfo.setDisInfoType(disInfoType);
				//转换类属性
				Map<String, Object> attrValues = typeManager.getAttrValues(recDesInfo);
				
				mapValue.putAll(attrValues);
				listMap.add(mapValue);
			}
		} catch (Exception ex) {
			error(ex);
		}
		listToJson();
		return OUTPUTDATA;
	}

	/*
	 * 取得发放单信息
	 * 
	 * @return 销毁信息列表页面
	 */
	public String getAllNeddDesInfos(){
		try {
			//获取发放单OIDS
			String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
			String linkOids = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
			List<String> linkOidList = SplitString.string2List(linkOids, ",");
			//获得需要销毁的发放信息
			List<Map<String, Object>> listDisInfo = service.getAllNeddDesInfosByDistributeOrderOid(distributeOrderOid, linkOidList);

			TypeService typeManager = Helper.getTypeManager();

			for(Map<String, Object> desInfo:listDisInfo){
				Map<String, Object> mapValue = new HashMap<String, Object>();
				RecDesInfo recDesInfo = new RecDesInfo();

				//分发信息IID（人员或组织的内部标识）
				String disInfoId = (String) desInfo.get("DISINFOID");
				//分发信息IID（人员或组织的内部标识）
				String disInfoName = (String) desInfo.get("DISINFONAME");
				//分发信息类型（0为单位，1为人员）
				String disInfoType = ((BigDecimal) desInfo.get("DISINFOTYPE")).toString();
				//发放单与分发数据LINK内部标识
				String disOrderObjLinkId = (String) desInfo.get("DISORDEROBJLINKID");
				//发放单与分发数据LINK类标识
				String disOrderObjLinkClassId = (String) desInfo.get("DISORDEROBJLINKCLASSID");
				//分发介质类型（0为纸质，1为电子，2为跨域）
				String disMediaType = String.valueOf(((BigDecimal)desInfo.get("DISMEDIATYPE")).longValue());
				//已回收份数
				long recoverNum = ((BigDecimal)desInfo.get("RECOVERNUM")).longValue();
				//添加需要回收份数
				long needDestroyNum = 0;
				//发放份数
				long disInfoNum = ((BigDecimal)desInfo.get("DISINFONUM")).longValue();
				//发放份数
				long destroyNum = ((BigDecimal)desInfo.get("DESTROYNUM")).longValue();
				//发放数据名称
				String disObjName = (String) desInfo.get("NAME");
				//发放数据ID
				String disObjId = (String) desInfo.get("ID");

				//设置类内部表示符
				recDesInfo.setClassId(RecDesInfo.CLASSID);
				//设置需要销毁的份数
				recDesInfo.setNeedRecoverNum(needDestroyNum);
				//设置发放单与分发数据LINK内部标识
				recDesInfo.setDisOrderObjectLinkClassId(disOrderObjLinkClassId);
				//设置发放单与分发数据LINK类标识
				recDesInfo.setDisOrderObjectLinkId(disOrderObjLinkId);
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
				//已销毁份数
				recDesInfo.setDestroyNum(destroyNum);
				//发放数据ID
				recDesInfo.setDisObjId(disObjId);
				//发放数据名称
				recDesInfo.setDisObjName(disObjName);
				//发放备注
				recDesInfo.setNote("");
				//设置分发信息类型（0为单位，1为人员）
				recDesInfo.setDisInfoType(disInfoType);

				//转换类属性
				Map<String, Object> attrValues = typeManager.getAttrValues(recDesInfo);

				mapValue.putAll(attrValues);
				listMap.add(mapValue);
			}
		} catch (Exception ex) {
			error(ex);
		}
		listToJson();
		return OUTPUTDATA;
	}

	/**
	 * 添加回收消息
	 * @return JSON对象
	 */
	public String addNeddRecInfos(){
		try{
			//分发信息名称（单位/人员）
			String disInfoNames = request.getParameter("disInfoNames");
			//分发信息IID（人员或组织的内部标识）
			String disInfoIds = request.getParameter("disInfoIds");
			//发放单与分发数据LINK内部标识
			String disOrderObjLinkIds = request.getParameter("disOrderObjLinkIds");
			//发放单与分发数据LINK类标识
			String disOrderObjLinkClassIds = request.getParameter("disOrderObjLinkClassIds");
			//分发介质类型（0为纸质，1为电子，2为跨域）
			String disMediaTypes = request.getParameter("disMediaTypes");
			//分发份数
			String disInfoNums = request.getParameter("disInfoNums");
			//需要回收份数
			String needRecoverNums = request.getParameter("needRecoverNums");
			//已回收份数
			String recoverNums = request.getParameter("recoverNums");
			//已回收份数
			String notes = request.getParameter("notes");
			//分发信息类型（0为单位，1为人员）
			String disInfoTypes = request.getParameter("disInfoTypes");
			//回收单OID
			String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
			String resultMessge = service.addNeddRecInfos(disInfoNames, disInfoIds, disOrderObjLinkIds,
					disOrderObjLinkClassIds, disMediaTypes,
					disInfoNums, needRecoverNums, recoverNums, distributeOrderOid, notes, disInfoTypes);
			
			//没有错误消息
			if("".equals(resultMessge)){
				success();
			}else{
				result.put("success", "false");
				result.put("message", resultMessge);
			}
		}catch(Exception ex){
			error(ex);
			result.put("success", "false");
			result.put("message", "在对象绑定的生命周期模板中,未找到初始化规则指定的模板:回收销毁信息生命周期");
		}

		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 添加销毁信息
	 * @return 显示销毁信息页面
	 */
	public String addNeddDesInfos(){
		try{
			//分发信息名称（单位/人员）
			String disInfoNames = request.getParameter("disInfoNames");
			//分发信息IID（人员或组织的内部标识）
			String disInfoIds = request.getParameter("disInfoIds");
			//发放单与分发数据LINK内部标识
			String disOrderObjLinkIds = request.getParameter("disOrderObjLinkIds");
			//发放单与分发数据LINK类标识
			String disOrderObjLinkClassIds = request.getParameter("disOrderObjLinkClassIds");
			//分发介质类型（0为纸质，1为电子，2为跨域）
			String disMediaTypes = request.getParameter("disMediaTypes");
			//分发份数
			String disInfoNums = request.getParameter("disInfoNums");
			//需要回收份数
			String needDestroyNums = request.getParameter("needDestroyNums");
			//已销毁份数
			String destroyNums = request.getParameter("destroyNums");
			//已回收份数
			String recoverNums = request.getParameter("recoverNums");
			//已回收份数
			String notes = request.getParameter("notes");
			//分发信息类型（0为单位，1为人员）
			String disInfoTypes = request.getParameter("disInfoTypes");
			//回收单OID
			String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);

			String resultMessge = service.addNeddDesInfos(disInfoNames, disInfoIds, disOrderObjLinkIds,
					disOrderObjLinkClassIds, disMediaTypes,
					disInfoNums, needDestroyNums, destroyNums, distributeOrderOid, recoverNums, notes, disInfoTypes);
			//没有错误消息
			if("".equals(resultMessge)){
				success();
			}else{
				result.put("success", "false");
				result.put("message", resultMessge);
			}
		}catch(Exception ex){
			error(ex);
			result.put("success", "false");
			result.put("message", "在对象绑定的生命周期模板中,未找到初始化规则指定的模板:回收销毁信息生命周期");
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 取得回收信息数据。
	 */
	private void getRecInfosForEdit() {

		// 获取发放单OIDS
		//String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
		//回收数据OID
		String oid = request.getParameter("oid");

		//查询回收信息
		List<RecDesInfo> listResInfos = service.getRecInfosForEditByOId(oid);

		TypeService typeManager = Helper.getTypeManager();

		for (RecDesInfo disInfo : listResInfos) {
			Map<String, Object> mapValue = new HashMap<String, Object>();
			Map<String, Object> attrValues = typeManager.getAttrValues(disInfo);
			if(disInfo.getNote() == null){
				attrValues.put("NOTE", "");
			}
			mapValue.putAll(attrValues);
			listMap.add(mapValue);
		}

		if (!listResInfos.isEmpty()) {
			request.setAttribute("disUrgent", listResInfos.get(0).getDisUrgent());
			request.setAttribute("deadLineDate", DateTimeUtil.getDateDisplay(listResInfos.get(0).getDisDeadLine()));
		}

		LOG.debug("取得需要修改回收信息" + getDataSize() + " 条。");

	}

	/**
	 * 取得销毁信息数据。
	 */
	private void getDesInfosForEdit() {

		// 获取发放单OIDS
		//String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
		//回收数据OID
		String oid = request.getParameter("oid");

		//查询回收信息
		List<RecDesInfo> listDesInfos = service.getRecInfosForEditByOId(oid);

		TypeService typeManager = Helper.getTypeManager();

		for (RecDesInfo disInfo : listDesInfos) {
			Map<String, Object> mapValue = new HashMap<String, Object>();
			Map<String, Object> attrValues = typeManager.getAttrValues(disInfo);
			if(disInfo.getNote() == null){
				attrValues.put("NOTE", "");
			}
			mapValue.putAll(attrValues);
			listMap.add(mapValue);
		}

		if (!listDesInfos.isEmpty()) {
			request.setAttribute("disUrgent", listDesInfos.get(0).getDisUrgent());
			request.setAttribute("deadLineDate", DateTimeUtil.getDateDisplay(listDesInfos.get(0).getDisDeadLine()));
		}

		LOG.debug("取得需要修改销毁信息" + getDataSize() + " 条。");

	}

	/**
	 * 取得回收数据。
	 * 
	 * @return JSON对象
	 */
	public String getRecInfos() {
		try {
			String linkOid = request.getParameter("linkOid");
			if (linkOid !=null) {
				session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS, linkOid);
			}
			// 取得分发信息数据。
			getRecInfosForEdit();
			LOG.debug("取得回收信息" + getDataSize() + " 条,回收类型为" + DISPLAY_TYPE.EDIT);

		} catch (Exception ex) {
			error(ex);
		}
		// 输出结果
		listToJson();
		return OUTPUTDATA;
	}
	
	/**
	 * 取得销毁信息数据。
	 * 
	 * @return JSON对象
	 */
	public String getDesInfos() {
		try {
			String linkOid = request.getParameter("linkOid");
			if (linkOid !=null) {
				session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS, linkOid);
			}
			// 取得分发信息数据。
			getDesInfosForEdit();
			LOG.debug("取得销毁信息" + getDataSize() + " 条，回收类型为" + DISPLAY_TYPE.EDIT);
			
		} catch (Exception ex) {
			error(ex);
		}
		// 输出结果
		listToJson();
		return OUTPUTDATA;
	}

	/**
	 * 更新回收信息（回收份数）
	 * 
	 * @return JSON对象
	 */
	public String updateRecInfos(){
		try {
			//回收信息OID
			String recInfoOids = request.getParameter("recInfoOids");
			//需要回收份数
			String needRecoverNums = request.getParameter("needRecoverNums");
			//备注
			String notes = request.getParameter("notes");
			//分发类型
			String dismediatypes = request.getParameter("dismediatypes");

			service.updateRecInfos(recInfoOids, needRecoverNums, notes, dismediatypes);

			success();
		}catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 更新销毁信息（销毁份数）
	 *
	 * @return JSON对象
	 */
	public String updateDesInfos(){
		try{
			//回收信息OID
			String desInfoOids = request.getParameter("desInfoOids");
			//需要销毁份数
			String needDestroyNums = request.getParameter("needDestroyNums");
			//备注
			String notes = request.getParameter("notes");
			//分发类型
			String dismediatypes = request.getParameter("dismediatypes");

			service.updateDesInfos(desInfoOids, needDestroyNums, notes, dismediatypes);

			success();
		}catch(Exception ex){
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 删除回收销毁信息
	 *
	 * @return JSON对象
	 */
	public String deleteRecDesInfos(){
		try{
			//回收信息OID
			String oids = request.getParameter("oids");

			service.deleteRecDesInfos(oids);

			success();
		}catch(Exception ex){
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 验证是否有未完成的回收发放单
	 * 
	 * @return
	 */
	public String checkRecDistributeOrder(){
		try{
		//已分发的发放单OID
		String oid = request.getParameter("oid");

		//查询未完成的回收发放单信息
		List<Map<String, Object>> listRecOrderInfos = service.checkRecDistributeOrder(oid);

		//存在未完成的回收发放单信息
		if(listRecOrderInfos.size() > 0){
			result.put("success", "false");
		}else{//不存在未完成的回收发放单信息
			success();
		}

		}catch(Exception ex){
			error(ex);
		}

		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 验证是否有未完成的销毁发放单
	 * 
	 * @return
	 */
	public String checkDesDistributeOrder(){
		try{
		//已分发的发放单OID
		String oid = request.getParameter("oid");

		//查询未完成的回收发放单信息
		List<Map<String, Object>> listDesOrderInfos = service.checkDesDistributeOrder(oid);

		//存在未完成的回收发放单信息
		if(listDesOrderInfos.size() > 0){
			result.put("success", "false");
		}else{//不存在未完成的回收发放单信息
			success();
		}

		}catch(Exception ex){
			error(ex);
		}

		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 更新发放单与分发数据link的完工期限。
	 * 
	 * @return String
	 */
	public String updateDistributedeadLinkDate(){
		try {
			//获取发放单OIDS
			String linkOids = request.getParameter(KeyS.OIDS);
			String deadLineDate = request.getParameter("deadLineDate");
			disObjservice.updateDistributedeadLinkDate(linkOids, deadLineDate);
			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 更新发放单与分发数据link的紧急程度。
	 * 
	 * @return String
	 */
	public String updateDistributeObjectLink() {
		try {
			String linkOids = request.getParameter(KeyS.OIDS);
			String disUrgent = request.getParameter("disUrgent");
			disObjservice.updateDistributeObjectLink(linkOids, disUrgent);

			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

}
