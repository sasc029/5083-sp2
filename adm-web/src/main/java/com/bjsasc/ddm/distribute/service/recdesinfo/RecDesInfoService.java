package com.bjsasc.ddm.distribute.service.recdesinfo;

import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.recdesinfo.RecDesInfo;

/**
 * �������ٷ��ŵ�����ӿڡ�
 * 
 * @author kangyanfei 2014-5-28
 */
public interface RecDesInfoService {

	/**
	 * ͨ�����ŵ���ַ�����link�б��ȡ����������Ϣ���ݡ�
	 * 
	 * @param List<DistributeOrderObjectLink> listOrderObjectLink
	 * @return List
	 */
	public List<RecDesInfo> getRecDesInfosByOrderObjectLinkList(List<DistributeOrderObjectLink> listOrderObjectLink);
	
	/**
	 * ͨ�����ŵ�ID��ȡ����/���ٷַ���Ϣ���ݡ�
	 * 
	 * @param innerId String
	 * @return List
	 */
	public List<RecDesInfo> getRecDesInfosByDistributeOrderInnerId(String innerId);
	
	/**
	 * ȡ�����л��յ�
	 * 
	 * @param distributeOid
	 *			���ŵ�Ψһ��ʾ��innerID+ClassID��
	 * @param List<String> linkOidList
	 *			��������LINK
	 *
	 * @return ���л��յ�
	 */
	public List<RecDesInfo> getAllRecInfo(String distributeOid, List<String> linkOidList);

	/**
	 * ȡ���������ٵ�
	 * 
	 * @param distributeOid
	 * 			���ŵ�Ψһ��ʾ��innerID+ClassID�� 
	 * @param List<String> linkOidList
	 * 			��������LINK
	 *
	  *@return �������ٵ�
	 */
	public List<RecDesInfo> getAllDesInfo(String distributeOid, List<String> linkOidList);

	/**
	 * ��ӻ��յ�
	 * 
	 * @param orderOidsStr
	 *			���ŵ�Ψһ��ʾ��innerID+ClassID��
	 * @param number
	 *			 ���
	 * @param name
	 *			����
	 * @param orderType
	 *			�������ͣ�0���ŵ���1�������ŵ���2���յ���3���ٵ���
	 * @param note
	 *			��ע
	 * @return DistributeOrder
	 *			�´����Ļ��յ�
	 */
	public DistributeOrder addResDistributeOrder(String orderOidsStr,
			String number, String name, String orderType, String note);

	/**
	 * ������ٵ�
	 * 
	 * @param orderOidsStr
	 *			���ŵ�Ψһ��ʾ��innerID+ClassID��
	 * @param number
	 *			���
	 * @param name
	 *			����
	 * @param orderType
	 *			�������ͣ�0���ŵ���1�������ŵ���2���յ���3���ٵ���
	 * @param note
	 *			��ע
	 * @return DistributeOrder
	 *			�´��������ٵ�
	 */
	public DistributeOrder addDesDistributeOrder(String orderOidsStr,
			String number, String name, String orderType, String note);

	/**
	 * ȡ�÷��ŵ���Ϣ
	 * @param distributeOrderOid
	 * 			���ŵ�OID
	 * @param linkOidList
	 * 			Link��OIDs����
	 * @return ��Ҫ���յķ��ŵ���Ϣ
	 */
	public List<Map<String, Object>> getAllNeddRecInfosByDistributeOrderOid(String distributeOrderOid, List<String> linkOidList);

	/**
	 * ȡ�÷��ŵ���Ϣ
	 * @param distributeOrderOid
	 * 			���ŵ�OID
	 * @param linkOidList
	 * 			Link��OIDs����
	 * @return ��Ҫ���ٵķ��ŵ���Ϣ
	 */
	public List<Map<String, Object>> getAllNeddDesInfosByDistributeOrderOid(String distributeOrderOid, List<String> linkOidList);

	/**
	 * ��ӻ�����Ϣ
	 * 
	 * @param disInfoNames
	 *			�ַ���Ϣ���ƣ���λ/��Ա��
	 * @param disInfoIds
	 *			�ַ���ϢIID����Ա����֯���ڲ���ʶ��
	 * @param disOrderObjLinkIds
	 *			���ŵ���ַ�����LINK�ڲ���ʶ
	 * @param disOrderObjLinkClassIds
	 *			���ŵ���ַ�����LINK���ʶ
	 * @param disMediaTypes
	 *			�ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
	 * @param disInfoNums
	 *			�ַ�����	
	 * @param needRecoverNums
	 *			��Ҫ���յķ���
	 * @param recoverNums
	 *			�ѻ��յķ���
	 * @param distributeOrderOid
	 *			���յ���OID
	 * @param notes
	 *			��ע
	 * @param disInfoTypes
	 *			�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
	 * @return String
	 *			������Ϣ
	 */
	public String addNeddRecInfos(String disInfoNames, String disInfoIds,
			String disOrderObjLinkIds,
			String disOrderObjLinkClassIds, String disMediaTypes,
			String disInfoNums, String needRecoverNums, String recoverNums, 
			String distributeOrderOid, String notes, String disInfoTypes);	

	/**
	 * ��ӻ�����Ϣ
	 * 
	 * @param disInfoNames
	 *			�ַ���Ϣ���ƣ���λ/��Ա��
	 * @param disInfoIds
	 *			�ַ���ϢIID����Ա����֯���ڲ���ʶ��
	 * @param disOrderObjLinkIds
	 *			���ŵ���ַ�����LINK�ڲ���ʶ
	 * @param disOrderObjLinkClassIds
	 *			���ŵ���ַ�����LINK���ʶ
	 * @param disMediaTypes
	 *			�ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
	 * @param disInfoNums
	 *			�ַ�����
	 * @param needDestroyNums
	 *			��Ҫ���ٵķ���
	 * @param destroyNums
	 *			�Ѿ����ٷ���
	 * @param distributeOrderOid
	 *			���ٵ�OID
	 * @param recoverNums
	 *			���շ���
	 * @param notes
	 *			��ע
	 * @param disInfoTypes
	 *			�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
	 * @return String
	 *			������Ϣ
	 */
	public String addNeddDesInfos(String disInfoNames, String disInfoIds,
			String disOrderObjLinkIds,
			String disOrderObjLinkClassIds, String disMediaTypes,
			String disInfoNums, String needDestroyNums, String destroyNums,String distributeOrderOid,
			String recoverNums, String notes, String disInfoTypes);

	/**
	 * ��ѯ����������Ϣ
	 * 
	 * @param oid
	 *			������ϢOID
	 * @return ist<RecDesInfo>
	 *			����������Ϣ
	 */
	public List<RecDesInfo> getRecInfosForEditByOId(String oid);

	/**
	 * ��ѯ����������Ϣ
	 * 
	 * @param oid
	 *			������ϢOID
	 * @return List<RecDesInfo>
	 *			����������Ϣ
	 */
	public List<RecDesInfo> getDesInfosForEditByOId(String oid);

	/**
	 * ���»�����Ϣ
	 * 
	 * @param recInfoOids
	 *			������ϢOID
	 * @param needRecoverNums
	 *			��Ҫ���շ���
	 * @param notes
	 *			��ע
	 * @param dismediatypes
	 *			�ַ�����
	 */
	public void updateRecInfos(String recInfoOids, String needRecoverNums,
			String notes, String dismediatypes);

	/**
	 * ���»�����Ϣ
	 * 
	 * @param desInfoOids
	 *			������ϢOID
	 * @param needDestroyNums
	 *			��Ҫ���ٷ���
	 * @param notes
	 *			��ע
	 * @param dismediatypes
	 *			�ַ�����
	 */
	public void updateDesInfos(String desInfoOids, String needDestroyNums,
			String notes, String dismediatypes);

	/**
	 * 
	 * @param oids
	 *			����������ϢClassID��InnerID
	 */
	public void deleteRecDesInfos(String oids);

	/**
	 * 
	 * @param oid
	 *			�ѷַ��ķ��ŵ�OID
	 * @return List<Map<String, Object>>
	 *			δ��ɵĻ��շ��ŵ���Ϣ
	 */
	public List<Map<String, Object>> checkRecDistributeOrder(String oid);

	/**
	 * 
	 * @param oid
	 *			�ѷַ��ķ��ŵ�OID
	 * @return List<Map<String, Object>>
	 *			δ��ɵ����ٷ��ŵ���Ϣ
	 */
	public List<Map<String, Object>> checkDesDistributeOrder(String oid);

	/**
	 * ���ݸ�����orderObjectLinkOid�ͻ�������ֽ�������OID����ȡ����صķַ���Ϣ
	 * 
	 * @param orderObjectLinkOids �ַ����ͷַ����ݵ�����
	 * @param taskOid ֽ�ʻ������������oid
	 * @return ��صĻ���������Ϣ�б�
	 * 
	 * @author Sun Zongqing
	 * @date 2014/7/2
	 */
	public List<RecDesInfo> getpapertaskinfo(String orderObjectLinkOids, String taskOid);


	/**
	 * ɾ������������Ϣ
	 * 
	 * @param DistributeOrderObjectLink
	 *			�ַ�����link 
	 */
	public void deleteDistributeRecDesInfoByOid(DistributeOrderObjectLink linkd);

}
