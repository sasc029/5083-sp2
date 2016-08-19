package com.bjsasc.ddm.distribute.service.distributepapersigntask;

import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.common.ConstUtil.LIFECYCLE_OPT;
import com.bjsasc.ddm.distribute.model.distributepapersigntask.DistributePaperSignTask;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributetaskinfolink.DistributeTaskInfoLink;

/**
 * ֽ��ǩ���������ӿڡ�
 * 
 * @author zhangguoqiang 2014-09-11
 */
public interface DistributePaperSignTaskService {

	/**
	 * ��ȡ����ֽ��ǩ������
	 */
	public List<DistributePaperSignTask> getAllDistributePaperSignTask(Object stateName);
	
	/**
	 * ��ȡ����δǩ��ֽ��ǩ������
	 */
	public List<DistributePaperSignTask> getAllNoSignDistributePaperSignTask(Object stateName);
	
	/**
	 * ��ȡ���л���ֽ��ǩ������
	 */
	public List<DistributePaperSignTask> getAllReturnDistributePaperSignTask(Object stateName);

	/**
	 * ȡ��ֽ��ǩ��������Ϣ��
	 * 
	 * @param distributePaperTaskOid
	 * @return
	 */
	public DistributePaperSignTask getDistributePaperSignTaskInfo(String distributePaperTaskOid);
	
	/**
	 * ͨ��ֽ��ǩ������OID��ȡ����طַ�������Ϣ
	 * 
	 * @param distributePaperTaskOid String
	 * @return List
	 */
	public List<DistributeObject> getDistributeObjects(String distributePaperTaskOid);
	
	/**
	 * ��������ֽ��ǩ���������
	 * 
	 * @param disPaperSignTask
	 */
	public void createDistributePaperSignTasks(Map<String, String> oidsMap);

	/**
	 * ����ֽ��ǩ���������
	 * 
	 * @param disPaperSignTask
	 */
	public void createDistributePaperSignTask(DistributePaperSignTask disPaperSignTask);

	/**
	 * ����ֽ��ǩ���������
	 * 
	 * @return DistributePaperSignTask
	 */
	public DistributePaperSignTask newDistributePaperSignTask();

	/**
	 * ͨ���ַ���ID��ȡ�ַ��������ݡ�
	 * 
	 * @param oid String
	 * @return List
	 */
	public List<DistributePaperSignTask> getDistributePaperSignTasksByDistributeOrderOid(String oid);

	/**
	 * ����ֽ��ǩ������
	 * 
	 * @param oid String
	 * @param opt LIFECYCLE_OPT
	 */
	public void updateDistributePaperSignTask(String oids, LIFECYCLE_OPT opt, String returnReason);

	/**
	 * ��ȡֽ��ǩ��������طַ����ݺͷַ���Ϣ
	 * 
	 * @return DistributePaperSignTask
	 */
	public DistributePaperSignTask getDistributePaperSignTaskProperty(String oid);

	/**
	 * ɾ��ֽ��ǩ������
	 * 
	 * @param oid
	 */
	public void deleteDistributePaperSignTask(String oids);
	
	/**
	 * �����ַ���Ϣ��ַ�����link����
	 * 
	 * @return DistributeInfo
	 */
	public DistributeTaskInfoLink newDistributeTaskInfoLink();
	
	/**
	 * ��ǩ��ֽ��ǩ���������
	 * 
	 * @param distributePaperTaskOid
	 */
	public void updateDistributePaperSignTaskLife(String distributePaperTaskOid);
	
	/**
	 * ����ֽ��ǩ������OIDȡ�÷��ŵ�����
	 * 
	 * @param oids
	 */
	public DistributeOrder getDistributeOrderByPaperSignTaskOid(String oids);

}
