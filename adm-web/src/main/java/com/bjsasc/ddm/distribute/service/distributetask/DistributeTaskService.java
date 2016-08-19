package com.bjsasc.ddm.distribute.service.distributetask;

import java.util.List;

import com.bjsasc.avidm.core.site.Site;
import com.bjsasc.ddm.common.ConstUtil.LIFECYCLE_OPT;
import com.bjsasc.ddm.distribute.model.distributeelectask.DistributeElecTask;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;

/**
 * �ַ��������ӿڡ�
 * 
 * @author gengancong 2013-2-22
 */
public interface DistributeTaskService {

	/**
	 * ͨ���ַ���ID��ȡ�ַ��������ݡ�
	 * 
	 * @param oid String
	 * @return List
	 */
	public List<DistributePaperTask> getDistributePaperTasksByDistributeOrderOid(String oid);

	/**
	 * ͨ���ַ���ID��ȡ�ַ��������ݡ�
	 * 
	 * @param oid String
	 * @return List
	 */
	public List<DistributeElecTask> getDistributeElecTasksByDistributeOrderOid(String oid);

	/**
	 * �����ַ�����
	 * 
	 * @param distributeOrderOids �ַ�����oid����������зַ����ͻ������ٵ�
	 * @param flag ��ǩ��true��ʾ���߹�����
	 * @modify Sun Zongqing
	 */
	public void createDistributeTask(String distributeOrderOids, String flag,String modify);
	/**
	 * ������ѡ������ã������е���ʱ���ַ������ַ����񡢷ַ���Ϣ�Լ��ַ��������ó��ѷַ�״̬
	 * 
	 * @param distributeOrderOids �ַ�����oid
	 */
	public void setAllSended(String distributeOrderOids);

	/**
	 * �޸���������
	 * 
	 * @param oids
	 * @param opt
	 * @param returnReason
	 */
	public void updateDistributeTask(String oids, LIFECYCLE_OPT opt, String returnReason);
	
	/**
	 * ���������Ϣ
	 * 
	 * @param outSignInfoOids
	 */
	public void sendToOutSign(String outSignInfoOids, boolean flag);
	
	/**
	 * �������ķ��ͷ��������Ϣ�����շ�
	 * 
	 * @param outSiteInfos
	 */
	public void sendToReceiveSite(List<DistributeInfo> disInfoList, String outSiteInfos, DistributeOrder order);
	
	/**
	 * ������վ������
	 * 
	 * @param orderOids
	 * @param isDcDeployModel
	 */
	public List<String> createOutSiteDistributeTask(String orderOid, String disInfoOids, Site site, boolean flag,
			boolean centerFlag, Site centerSite);
	
	/**
	 * ��������վ������
	 * 
	 * @param orderOids
	 * @param isDcDeployModel
	 */
	public List<String> createCenterSiteDistributeTask(String orderOid, String disInfoOids, Site site, boolean flag,
			boolean centerFlag, Site centerSite);
}