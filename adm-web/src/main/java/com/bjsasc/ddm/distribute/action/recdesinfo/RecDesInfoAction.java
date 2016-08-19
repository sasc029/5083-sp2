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
 * �������ٷ��ŵ�Actionʵ���ࡣ
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
	 * ȡ�û�����Ϣͨ��Session�DISTRIBUTE_ORDER_OBJECT_LINK_OIDS��
	 * 
	 * @return JSON����
	 */
	public String getRecInfoByLinkOids() {
		try {

			String linkOidArr = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
			List<String> linkOidList = SplitString.string2List(linkOidArr, ",");

			// ��ȡ���ŵ�OIDS
			String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);

			// ȡ�û��յ����ݡ�
			List<RecDesInfo> recInfoList = service.getAllRecInfo(distributeOrderOid, linkOidList);
			if(recInfoList.size() > 0){
				//����Ĭ�Ͻ����̶�
				String disUrgent = recInfoList.get(0).getDisUrgent();
				if(!StringUtil.isStringEmpty(disUrgent)){
					request.setAttribute("disUrgent", disUrgent);
				}
				//����Ĭ���������
				long disDeadLine = recInfoList.get(0).getDisDeadLine();
				if(disDeadLine > 0){
					request.setAttribute("deadLineDate", DateTimeUtil.getDateDisplay(disDeadLine));
				}
			}

			LOG.debug("ȡ�û��յ���Ϣ " + recInfoList.size() + " ��");

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
	 * ����Session��LinkOids���ݡ�
	 * 
	 * @return JSON����
	 */
	public String setLinkOids() {
		try {
			//�õ���ѡ���distributeOrderObjectLinkOids
			String linkOids = request.getParameter("distributeOrderObjectLinkOids");
			//���浽Session
			session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS, linkOids);
	
			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
		
	}

	/**
	 * ȡ��������Ϣͨ��Session�DISTRIBUTE_ORDER_OBJECT_LINK_OIDS��
	 * 
	 * @return JSON����
	 */
	public String getDesInfoByLinkOids() {
		try {

			String linkOidArr = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
			List<String> linkOidList = SplitString.string2List(linkOidArr, ",");

			// ��ȡ���ŵ�OIDS
			String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);

			// ȡ�����ٵ����ݡ�
			List<RecDesInfo> desInfoList = service.getAllDesInfo(distributeOrderOid, linkOidList);
			if(desInfoList.size() > 0){
				//����Ĭ�Ͻ����̶�
				String disUrgent = desInfoList.get(0).getDisUrgent();
				if(!StringUtil.isStringEmpty(disUrgent)){
					request.setAttribute("disUrgent", disUrgent);
				}
				//����Ĭ���������
				long disDeadLine = desInfoList.get(0).getDisDeadLine();
				if(disDeadLine > 0){
					request.setAttribute("deadLineDate", DateTimeUtil.getDateDisplay(disDeadLine));
				}
			}
			LOG.debug("ȡ��������Ϣ" + desInfoList.size() + " ��");

			TypeService typeManager = Helper.getTypeManager();

			for(RecDesInfo desInfo: desInfoList){
				Map<String, Object> mapValue = new HashMap<String, Object>();
				Map<String, Object> attrValues = typeManager.getAttrValues(desInfo);
				mapValue.putAll(attrValues);
				listMap.add(mapValue);
			}

			// ������
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDESINFO);
			
		} catch (Exception ex) {
			error(ex);
		}
		return "listDes";
	}
	
	/**
	 * ���Ź���--���ŵ�
	 * �ѷַ�--�����յ���
	 * @return strutsҳ�����
	 */
	public String addRecDistributeOrder(){
		
		try {
			String number = request.getParameter("NUMBER");// ���
			String name = request.getParameter("NAME");// ����
			String orderType = request.getParameter("orderType");// ��������
			String note = request.getParameter("NOTE");// ��ע

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
	 * ���Ź���--���ŵ�
	 * �ѷַ�--�����ٵ���
	 * @return strutsҳ�����
	 */
	public String addDesDistributeOrder(){
		
		try {
			String number = request.getParameter("NUMBER");// ���
			String name = request.getParameter("NAME");// ����
			String orderType = request.getParameter("orderType");// ��������
			String note = request.getParameter("NOTE");// ��ע

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
	 * ȡ�÷��ŵ���Ϣ
	 * 
	 * @return ��ӻ�����Ϣ�б�ҳ��
	 */
	public String getAllNeddRecInfos (){
		try {
			//��ȡ���ŵ�OIDS
			String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
			String linkOids = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
			List<String> linkOidList = SplitString.string2List(linkOids, ",");
			//�����Ҫ���յķ�����Ϣ
			List<Map<String, Object>> listRecInfo = service.getAllNeddRecInfosByDistributeOrderOid(distributeOrderOid, linkOidList);
			
			TypeService typeManager = Helper.getTypeManager();
			
			for(Map<String, Object> recInfo:listRecInfo){
				Map<String, Object> mapValue = new HashMap<String, Object>();
				RecDesInfo recDesInfo = new RecDesInfo();
				
				//�ַ���ϢIID����Ա����֯���ڲ���ʶ��
				String disInfoId = (String) recInfo.get("DISINFOID");
				//�ַ���ϢIID����Ա����֯���ڲ���ʶ��
				String disInfoName = (String) recInfo.get("DISINFONAME");
				//�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
				String disInfoType = ((BigDecimal) recInfo.get("DISINFOTYPE")).toString();
				//���ŵ���ַ�����LINK�ڲ���ʶ
				String disOrderObjLinkId = (String) recInfo.get("DISORDEROBJLINKID");
				//���ŵ���ַ�����LINK���ʶ
				String disOrderObjLinkClassId = (String) recInfo.get("DISORDEROBJLINKCLASSID");
				//�ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
				String disMediaType = String.valueOf(((BigDecimal)recInfo.get("DISMEDIATYPE")).longValue());
				//�ѻ��շ���
				long recoverNum = ((BigDecimal)recInfo.get("RECOVERNUM")).longValue();
				//�����Ҫ���շ���
				long needRecoverNum = 0;
				//���ŷ���
				long disInfoNum = ((BigDecimal)recInfo.get("DISINFONUM")).longValue();
				//������������
				String disObjName = (String) recInfo.get("NAME");
				//��������ID
				String disObjId = (String) recInfo.get("ID");
				
				//�������ڲ���ʾ��
				recDesInfo.setClassId(RecDesInfo.CLASSID);
				//������Ҫ���յķ���
				recDesInfo.setNeedRecoverNum(needRecoverNum);
				//���÷��ŵ���ַ�����LINK�ڲ���ʶ
				recDesInfo.setDisOrderObjectLinkClassId(disOrderObjLinkClassId);
				//���÷��ŵ���ַ�����LINK���ʶ
				recDesInfo.setDisOrderObjectLinkId(disOrderObjLinkId);
				//�������������IID����Ա�ڲ���ʶ��
				recDesInfo.setDisInfoId(disInfoId);
				//����������������ƣ���Ա��
				recDesInfo.setDisInfoName(disInfoName);
				//���÷ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ���򣩼���
				recDesInfo.setDisMediaType(disMediaType);
				//���÷ַ�����
				recDesInfo.setDisInfoNum(disInfoNum);
				//�����ѻ��շ���
				recDesInfo.setRecoverNum(recoverNum);
				//���÷�������ID
				recDesInfo.setDisObjId(disObjId);
				//���÷�����������
				recDesInfo.setDisObjName(disObjName);
				//���÷��ű�ע
				recDesInfo.setNote("");
				//���÷ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
				recDesInfo.setDisInfoType(disInfoType);
				//ת��������
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
	 * ȡ�÷��ŵ���Ϣ
	 * 
	 * @return ������Ϣ�б�ҳ��
	 */
	public String getAllNeddDesInfos(){
		try {
			//��ȡ���ŵ�OIDS
			String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
			String linkOids = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
			List<String> linkOidList = SplitString.string2List(linkOids, ",");
			//�����Ҫ���ٵķ�����Ϣ
			List<Map<String, Object>> listDisInfo = service.getAllNeddDesInfosByDistributeOrderOid(distributeOrderOid, linkOidList);

			TypeService typeManager = Helper.getTypeManager();

			for(Map<String, Object> desInfo:listDisInfo){
				Map<String, Object> mapValue = new HashMap<String, Object>();
				RecDesInfo recDesInfo = new RecDesInfo();

				//�ַ���ϢIID����Ա����֯���ڲ���ʶ��
				String disInfoId = (String) desInfo.get("DISINFOID");
				//�ַ���ϢIID����Ա����֯���ڲ���ʶ��
				String disInfoName = (String) desInfo.get("DISINFONAME");
				//�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
				String disInfoType = ((BigDecimal) desInfo.get("DISINFOTYPE")).toString();
				//���ŵ���ַ�����LINK�ڲ���ʶ
				String disOrderObjLinkId = (String) desInfo.get("DISORDEROBJLINKID");
				//���ŵ���ַ�����LINK���ʶ
				String disOrderObjLinkClassId = (String) desInfo.get("DISORDEROBJLINKCLASSID");
				//�ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
				String disMediaType = String.valueOf(((BigDecimal)desInfo.get("DISMEDIATYPE")).longValue());
				//�ѻ��շ���
				long recoverNum = ((BigDecimal)desInfo.get("RECOVERNUM")).longValue();
				//�����Ҫ���շ���
				long needDestroyNum = 0;
				//���ŷ���
				long disInfoNum = ((BigDecimal)desInfo.get("DISINFONUM")).longValue();
				//���ŷ���
				long destroyNum = ((BigDecimal)desInfo.get("DESTROYNUM")).longValue();
				//������������
				String disObjName = (String) desInfo.get("NAME");
				//��������ID
				String disObjId = (String) desInfo.get("ID");

				//�������ڲ���ʾ��
				recDesInfo.setClassId(RecDesInfo.CLASSID);
				//������Ҫ���ٵķ���
				recDesInfo.setNeedRecoverNum(needDestroyNum);
				//���÷��ŵ���ַ�����LINK�ڲ���ʶ
				recDesInfo.setDisOrderObjectLinkClassId(disOrderObjLinkClassId);
				//���÷��ŵ���ַ�����LINK���ʶ
				recDesInfo.setDisOrderObjectLinkId(disOrderObjLinkId);
				//�������������IID����Ա�ڲ���ʶ��
				recDesInfo.setDisInfoId(disInfoId);
				//����������������ƣ���Ա��
				recDesInfo.setDisInfoName(disInfoName);
				//�ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ���򣩼���
				recDesInfo.setDisMediaType(disMediaType);
				//�ַ�����
				recDesInfo.setDisInfoNum(disInfoNum);
				//�ѻ��շ���
				recDesInfo.setRecoverNum(recoverNum);
				//�����ٷ���
				recDesInfo.setDestroyNum(destroyNum);
				//��������ID
				recDesInfo.setDisObjId(disObjId);
				//������������
				recDesInfo.setDisObjName(disObjName);
				//���ű�ע
				recDesInfo.setNote("");
				//���÷ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
				recDesInfo.setDisInfoType(disInfoType);

				//ת��������
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
	 * ��ӻ�����Ϣ
	 * @return JSON����
	 */
	public String addNeddRecInfos(){
		try{
			//�ַ���Ϣ���ƣ���λ/��Ա��
			String disInfoNames = request.getParameter("disInfoNames");
			//�ַ���ϢIID����Ա����֯���ڲ���ʶ��
			String disInfoIds = request.getParameter("disInfoIds");
			//���ŵ���ַ�����LINK�ڲ���ʶ
			String disOrderObjLinkIds = request.getParameter("disOrderObjLinkIds");
			//���ŵ���ַ�����LINK���ʶ
			String disOrderObjLinkClassIds = request.getParameter("disOrderObjLinkClassIds");
			//�ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
			String disMediaTypes = request.getParameter("disMediaTypes");
			//�ַ�����
			String disInfoNums = request.getParameter("disInfoNums");
			//��Ҫ���շ���
			String needRecoverNums = request.getParameter("needRecoverNums");
			//�ѻ��շ���
			String recoverNums = request.getParameter("recoverNums");
			//�ѻ��շ���
			String notes = request.getParameter("notes");
			//�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
			String disInfoTypes = request.getParameter("disInfoTypes");
			//���յ�OID
			String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
			String resultMessge = service.addNeddRecInfos(disInfoNames, disInfoIds, disOrderObjLinkIds,
					disOrderObjLinkClassIds, disMediaTypes,
					disInfoNums, needRecoverNums, recoverNums, distributeOrderOid, notes, disInfoTypes);
			
			//û�д�����Ϣ
			if("".equals(resultMessge)){
				success();
			}else{
				result.put("success", "false");
				result.put("message", resultMessge);
			}
		}catch(Exception ex){
			error(ex);
			result.put("success", "false");
			result.put("message", "�ڶ���󶨵���������ģ����,δ�ҵ���ʼ������ָ����ģ��:����������Ϣ��������");
		}

		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * ���������Ϣ
	 * @return ��ʾ������Ϣҳ��
	 */
	public String addNeddDesInfos(){
		try{
			//�ַ���Ϣ���ƣ���λ/��Ա��
			String disInfoNames = request.getParameter("disInfoNames");
			//�ַ���ϢIID����Ա����֯���ڲ���ʶ��
			String disInfoIds = request.getParameter("disInfoIds");
			//���ŵ���ַ�����LINK�ڲ���ʶ
			String disOrderObjLinkIds = request.getParameter("disOrderObjLinkIds");
			//���ŵ���ַ�����LINK���ʶ
			String disOrderObjLinkClassIds = request.getParameter("disOrderObjLinkClassIds");
			//�ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
			String disMediaTypes = request.getParameter("disMediaTypes");
			//�ַ�����
			String disInfoNums = request.getParameter("disInfoNums");
			//��Ҫ���շ���
			String needDestroyNums = request.getParameter("needDestroyNums");
			//�����ٷ���
			String destroyNums = request.getParameter("destroyNums");
			//�ѻ��շ���
			String recoverNums = request.getParameter("recoverNums");
			//�ѻ��շ���
			String notes = request.getParameter("notes");
			//�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
			String disInfoTypes = request.getParameter("disInfoTypes");
			//���յ�OID
			String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);

			String resultMessge = service.addNeddDesInfos(disInfoNames, disInfoIds, disOrderObjLinkIds,
					disOrderObjLinkClassIds, disMediaTypes,
					disInfoNums, needDestroyNums, destroyNums, distributeOrderOid, recoverNums, notes, disInfoTypes);
			//û�д�����Ϣ
			if("".equals(resultMessge)){
				success();
			}else{
				result.put("success", "false");
				result.put("message", resultMessge);
			}
		}catch(Exception ex){
			error(ex);
			result.put("success", "false");
			result.put("message", "�ڶ���󶨵���������ģ����,δ�ҵ���ʼ������ָ����ģ��:����������Ϣ��������");
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * ȡ�û�����Ϣ���ݡ�
	 */
	private void getRecInfosForEdit() {

		// ��ȡ���ŵ�OIDS
		//String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
		//��������OID
		String oid = request.getParameter("oid");

		//��ѯ������Ϣ
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

		LOG.debug("ȡ����Ҫ�޸Ļ�����Ϣ" + getDataSize() + " ����");

	}

	/**
	 * ȡ��������Ϣ���ݡ�
	 */
	private void getDesInfosForEdit() {

		// ��ȡ���ŵ�OIDS
		//String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
		//��������OID
		String oid = request.getParameter("oid");

		//��ѯ������Ϣ
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

		LOG.debug("ȡ����Ҫ�޸�������Ϣ" + getDataSize() + " ����");

	}

	/**
	 * ȡ�û������ݡ�
	 * 
	 * @return JSON����
	 */
	public String getRecInfos() {
		try {
			String linkOid = request.getParameter("linkOid");
			if (linkOid !=null) {
				session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS, linkOid);
			}
			// ȡ�÷ַ���Ϣ���ݡ�
			getRecInfosForEdit();
			LOG.debug("ȡ�û�����Ϣ" + getDataSize() + " ��,��������Ϊ" + DISPLAY_TYPE.EDIT);

		} catch (Exception ex) {
			error(ex);
		}
		// ������
		listToJson();
		return OUTPUTDATA;
	}
	
	/**
	 * ȡ��������Ϣ���ݡ�
	 * 
	 * @return JSON����
	 */
	public String getDesInfos() {
		try {
			String linkOid = request.getParameter("linkOid");
			if (linkOid !=null) {
				session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS, linkOid);
			}
			// ȡ�÷ַ���Ϣ���ݡ�
			getDesInfosForEdit();
			LOG.debug("ȡ��������Ϣ" + getDataSize() + " ������������Ϊ" + DISPLAY_TYPE.EDIT);
			
		} catch (Exception ex) {
			error(ex);
		}
		// ������
		listToJson();
		return OUTPUTDATA;
	}

	/**
	 * ���»�����Ϣ�����շ�����
	 * 
	 * @return JSON����
	 */
	public String updateRecInfos(){
		try {
			//������ϢOID
			String recInfoOids = request.getParameter("recInfoOids");
			//��Ҫ���շ���
			String needRecoverNums = request.getParameter("needRecoverNums");
			//��ע
			String notes = request.getParameter("notes");
			//�ַ�����
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
	 * ����������Ϣ�����ٷ�����
	 *
	 * @return JSON����
	 */
	public String updateDesInfos(){
		try{
			//������ϢOID
			String desInfoOids = request.getParameter("desInfoOids");
			//��Ҫ���ٷ���
			String needDestroyNums = request.getParameter("needDestroyNums");
			//��ע
			String notes = request.getParameter("notes");
			//�ַ�����
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
	 * ɾ������������Ϣ
	 *
	 * @return JSON����
	 */
	public String deleteRecDesInfos(){
		try{
			//������ϢOID
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
	 * ��֤�Ƿ���δ��ɵĻ��շ��ŵ�
	 * 
	 * @return
	 */
	public String checkRecDistributeOrder(){
		try{
		//�ѷַ��ķ��ŵ�OID
		String oid = request.getParameter("oid");

		//��ѯδ��ɵĻ��շ��ŵ���Ϣ
		List<Map<String, Object>> listRecOrderInfos = service.checkRecDistributeOrder(oid);

		//����δ��ɵĻ��շ��ŵ���Ϣ
		if(listRecOrderInfos.size() > 0){
			result.put("success", "false");
		}else{//������δ��ɵĻ��շ��ŵ���Ϣ
			success();
		}

		}catch(Exception ex){
			error(ex);
		}

		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * ��֤�Ƿ���δ��ɵ����ٷ��ŵ�
	 * 
	 * @return
	 */
	public String checkDesDistributeOrder(){
		try{
		//�ѷַ��ķ��ŵ�OID
		String oid = request.getParameter("oid");

		//��ѯδ��ɵĻ��շ��ŵ���Ϣ
		List<Map<String, Object>> listDesOrderInfos = service.checkDesDistributeOrder(oid);

		//����δ��ɵĻ��շ��ŵ���Ϣ
		if(listDesOrderInfos.size() > 0){
			result.put("success", "false");
		}else{//������δ��ɵĻ��շ��ŵ���Ϣ
			success();
		}

		}catch(Exception ex){
			error(ex);
		}

		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * ���·��ŵ���ַ�����link���깤���ޡ�
	 * 
	 * @return String
	 */
	public String updateDistributedeadLinkDate(){
		try {
			//��ȡ���ŵ�OIDS
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
	 * ���·��ŵ���ַ�����link�Ľ����̶ȡ�
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
