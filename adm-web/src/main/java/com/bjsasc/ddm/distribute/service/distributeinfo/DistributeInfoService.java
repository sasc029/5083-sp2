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
 * �ַ���Ϣ����ӿڡ�
 * 
 * @author gengancong 2013-2-22
 */
public interface DistributeInfoService {

	/**
	 * ͨ�����ŵ�ID��ȡ�ַ���Ϣ���ݡ�
	 * 
	 * @param innerId String
	 * @return List
	 */
	public List<DistributeInfo> getDistributeInfosByDistributeOrderInnerId(String innerId);
	
	
	/**
	 * ͨ��ֽ�������oid��ȡ�ַ���Ϣ���ݡ�
	 * 
	 * @param innerId String
	 * @param classid String
	 * @return List
	 */
	public List<DistributeInfo> getDistributeInfosByDistributePaperTaskOID(String oid);

	/**
	 * ͨ�����ŵ�OID,�ַ�����OIDS��ȡ�ַ���Ϣ���ݡ�
	 * 
	 * @param distributeOrderOid String
	 * @param distributeObjectOids List
	 * @param type DISPLAY_TYPE
	 * @return List
	 */
	public List<DistributeInfo> getDistributeInfosByOid(String distributeOrderOid, List<String> distributeObjectOids,
			DISPLAY_TYPE type);

	/**
	 * ͨ�����ŵ���ַ�����link�б��ȡ�ַ���Ϣ���ݡ�
	 * 
	 * @param List<DistributeOrderObjectLink> listOrderObjectLink
	 * @return List
	 */
	public List<DistributeInfo> getDistributeInfosByOrderObjectLinkList(List<DistributeOrderObjectLink> listOrderObjectLink);

	/**
	 * ͨ����������OID,�ַ�����OIDS��ȡ�ַ���Ϣ���ݡ�
	 * 
	 * @param distributeOrderOid String
	 * @param distributeObjectOids List
	 * @param type DISPLAY_TYPE
	 * @return List
	 */
	public List<DistributeInfo> getDistributeInfosByTaskOid(String distributeElecTaskOid, List<String> distributeObjectOids,
			DISPLAY_TYPE type);

	/**
	 * ͨ��ֽ��ǩ������OID,�ַ�����OIDS��ȡ�ַ���Ϣ���ݡ�
	 * 
	 * @param distributePaperSignTaskOid String
	 * @param distributeObjectOids List
	 * @param type DISPLAY_TYPE
	 * @return List
	 */
	public List<DistributeInfo> getDistributeInfosByPaperSignTaskOid(String distributePaperSignTaskOid, List<String> distributeObjectOids,
			DISPLAY_TYPE type);

	/**
	 * ͨ����������OID,�ַ�����OIDS��ȡ����������Ϣ���ݡ�
	 * 
	 * @param distributeOrderOid String
	 * @param distributeObjectOids List
	 * @param type DISPLAY_TYPE
	 * @return List
	 */
	public List<RecDesInfo> getRecDesInfosByOid(String distributeElecTaskOid, List<String> distributeObjectOids,
			DISPLAY_TYPE type);

	/**
	 * ͨ�����ŵ�OID,�ַ�����OID��ȡ�ַ���Ϣ���ݡ�
	 * 
	 * @param distributeOrderOid String
	 * @param distributeObjectOid String
	 * @return List
	 */
	public List<DistributeInfo> getDistributeInfosByOid(String distributeOrderOid, String distributeObjectOid);

	/**
	 * �����ַ���Ϣ����
	 * 
	 * @param dis DistributeInfo
	 */
	public void createDistributeInfo(DistributeInfo dis);

	/**
	 * �����ַ���Ϣ����
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
	 * ���·ַ���Ϣ����
	 * 
	 * @param oids String
	 * @param disInfoNums String
	 * @param notes String
	 */
	public void updateDistributeInfos(String oids, String disInfoNums, String notes, String dismediatypes);

	/**
	 * �h���ַ���Ϣ����
	 * 
	 * @param dis DistributeInfo
	 */
	public void deleteDistributeInfo(DistributeInfo dis);

	/**
	 * �h���ַ���Ϣ����
	 * 
	 * @param oids String
	 */
	public void deleteDistributeInfos(String oids);

	/**
	 * �����ַ���Ϣ����
	 * 
	 * @return DistributeInfo
	 */
	public DistributeInfo newDistributeInfo();

	/**
	 * �����Ϳ����ַ���Ϣ����
	 * 
	 * @param disInfoOld DistributeInfo
	 * @return DistributeInfo
	 */
	public DistributeInfo createAndCopyDistributeInfo(DistributeInfo disInfoOld, String orderOid, String linkOid);

	/**
	 * ����Ĭ�Ϸַ���Ϣ��
	 * 
	 * @param  disOrdObjLinkOid String
	 * @param  infoConfigList List<DistributeInfoConfig>
	 *  
	 */
	public void createDefaultDistributeInfo(String disOrdObjLinkOid,List<DistributeInfoConfig> infoConfigList);

	/**
	 * �����ַ���Ϣ��
	 * 
	 * @param  disOrdOid String
	 * @param  linkList List<Map<String, String>>
	 * 				key:FLAG value:String �ѷ��ű�ʶ (����:1��δ����:0)
	 * 				key:LINKOID value:String ���ŵ���ַ�����LinkOID 
	 *              key:DATAOID value:String �ַ�����ԴOID 
	 *              key:ISMASTER value:String �������ʶ (��:1������:0)
	 *  @param collectFlag int �ռ������ʶ
	 *  @param addDefaultInfoFlag boolean �Ƿ�׷��Ĭ�Ϸַ���Ϣ��ʶ
	 */
	public void createInfo(String disOrdOid, List<Map<String, String>> linkList,int collectDlag,boolean addDefaultInfoFlag);

	/**
	 * �Ƿ�׷��Ĭ�Ϸַ���Ϣ��
	 * 
	 * @param dataObject Persistable
	 * @return addDefaultInfoFlag boolean
	 */
	public boolean addDefaultInfo(Persistable dataObject);
	
	
	public List<DistributeInfo>  getpapertaskinfo(String linkOids, String taskOid);
	
	/**
	 * �޸��Ƿ������Ϣ��
	 * 
	 * @param oids String
	 * @return isTracks String
	 */
	public void updateDisInfoIsTrack(String oids, String isTracks);
	
	/**
	 * ��������ַ���Ϣ
	 * @param innerIds
	 * @param siteNames
	 * @param userIds
	 * @param linkOids
	 */
	public void createOutSignDisInfo(String innerIds,String siteNames,String userIds,String linkOids,boolean flag);
	
	/**
	 * ͨ�����ŵ�oid��÷ַ����ݣ��жϷַ����ݵ�����ܼ��Ƿ���ڷַ���ϢΪ������Ա���ܼ�
	 * @param disOrderOid
	 * @return false���ַ������ܼ����ڷַ���Ա�����ַܷ�
	 */
	public boolean canDisBySecurityLevel(String disOrderOid);
}
