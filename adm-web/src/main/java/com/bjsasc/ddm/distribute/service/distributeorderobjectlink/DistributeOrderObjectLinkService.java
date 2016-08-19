package com.bjsasc.ddm.distribute.service.distributeorderobjectlink;

import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.plm.core.persist.model.Persistable;

/**
 *  ���ŵ���ַ�����link����ӿڡ�
 * 
 * @author gengancong 2013-2-22
 */
public interface DistributeOrderObjectLinkService {

	/**
	 * ͨ���ַ����ݶ���ͷ��ŵ�����ȡ�÷��ŵ���ַ�����link���ݡ�
	 * 
	 * @param distributeObjectOid String
	 * @param distributeOrderOids String
	 * @return List
	 */
	public List<DistributeOrderObjectLink> getDistributeOrderObjectLinkListByDisObjOidAndDisOrderOids(
			String distributeObjectOid,String distributeOrderOids);
	/**
	 * ͨ���ַ����ݶ���ȡ�÷��ŵ���ַ�����link���ݡ�
	 * 
	 * @param distributeObjectOid String
	 * @return List
	 */
	public List<DistributeOrderObjectLink> getDistributeOrderObjectLinkListByDistributeObjectOid(
			String distributeObjectOid);

	/**
	 * @author kangyanfei
	 * ͨ�����ŵ�oid��ȡ�÷��ŵ���ַ�����link���ݡ�
	 * @param distributeOrderOid
	 * @return
	 */
	public List<DistributeOrderObjectLink> getDistributeOrderObjectLinkListByDistributeOrderOid(
			String distributeOrderOid);

	/**
	 * �������ŵ���ַ�����link����
	 * 
	 * @param disObj DistributeOrderObjectLink
	 */
	public void createDistributeOrderObjectLink(DistributeOrderObjectLink disObj);

	/**
	 * �������ŵ���ַ�����link����
	 * 
	 * @return DistributeOrderObjectLink
	 */
	public DistributeOrderObjectLink newDistributeOrderObjectLink();

	/**
	 * ���ŵ���ַ�����Link
	 * 
	 * @param disOrd disOrd
	 * @param collectRefResList List<Persistable>
	 * @param  aotuCreateFlag boolean
	 * @return List<Map<String,String>>
	 * 				key:FLAG value:String �ѷ��ű�ʶ (����:1��δ����:0)
	 * 				key:LINKOID value:String ���ŵ���ַ�����LinkOID 
	 *              key:DATAOID value:String �ַ�����ԴOID
	 *              key:ISMASTER value:String �������ʶ (��:1������:0)
	 */
	public List<Map<String,String>> creteOrderObjectLink(DistributeOrder disOrd, List<Persistable> collectRefResList,boolean aotuCreateFlag);

	/**
	 * ����Դ�����Ƿ���һ���Էַ���
	 * 
	 * @return boolean
	 */
	public boolean hasOneDisStyle(String dataOid);
	/**
	 * ͨ��ֽ�������oid��ȡDistributeOrderObjectLink��
	 * 
	 * @param distributeObjectOid String
	 * @param distributeOrderOids String
	 * @return List
	 */
	public List<DistributeOrderObjectLink> getDistributeOrderObjectLinkListByDisPaperTaskOID(
			String oid);
}
