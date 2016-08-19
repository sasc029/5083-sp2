package com.bjsasc.ddm.distribute.service.distributeelectask;

import java.util.List;

import com.bjsasc.ddm.common.ConstUtil.LIFECYCLE_OPT;
import com.bjsasc.ddm.distribute.model.distributeelectask.DistributeElecTask;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributetaskinfolink.DistributeTaskInfoLink;

/**
 * �����������ӿڡ�
 * 
 * @author gengancong 2013-3-18
 */
public interface DistributeElecTaskService {

	/**
	 * ��ȡ���е�������
	 */
	public List<DistributeElecTask> getAllDistributeElecTask(Object stateName);

	/**
	 * ��ȡ��������Ա�ĵ�������
	 */
	public List<DistributeElecTask> getDistributeElecTaskForSecondSche(String stateName, long from, long to);
	
	/**
	 * ��ȡ����Э��Ա�ĵ�������(Ŀǰֻ����Ѿܾ�������ɵ�״̬,����������������ǰ���߼�)
	 */
	public List<DistributeElecTask> getDistributeElecTaskForCrossDomain(String stateName, long from, long to);
	
	/**
	 * ��ȡ����δǩ�յ�������
	 */
	public List<DistributeElecTask> getAllNoSignDistributeElecTask(Object stateName);
	
	/**
	 * ��ȡ���л��˵�������
	 */
	public List<DistributeElecTask> getAllReturnDistributeElecTask(Object stateName);

	/**
	 * ȡ�õ���������Ϣ��
	 * 
	 * @param distributeElecOid
	 * @return
	 */
	public DistributeElecTask getDistributeElecTaskInfo(String distributeElecOid);

	/**
	 * ͨ����������OID��ȡ����طַ���Ϣ
	 * 
	 * @param distributeElecOid String
	 * @return List
	 */
//	public List<DistributeInfo> getDistributeInfos(String distributeElecOid);
	
	/**
	 * ͨ����������OID��ȡ�����ת����Ϣ
	 * 
	 * @param distributeElecOid String
	 * @return List
	 */
	public List<DistributeInfo> getDistributeForwardInfos(String distributeElecOid);
	
	/**
	 * ȡ�÷ַ����ݶ�Ӧ�ķַ���Ϣ
	 * 
	 * @param orderObjectLinkOids
	 * @return
	 */
	public List<DistributeInfo> getDistributeForwardInfoByOids(String orderObjectLinkOids,String taskOid);

	/**
	 * ͨ����������OID��ȡ����طַ�������Ϣ
	 * 
	 * @param distributeElecOid String
	 * @return List
	 */
	public List<DistributeObject> getDistributeObjects(String distributeElecOid);
	
	/**
	 * ͨ����������OID��ȡ����طַ�������Ϣ
	 * 
	 * @param distributeElecOid String
	 * @return List
	 */
	public List<DistributeObject> getDistributeObject(String distributeElecOid);

	/**
	 * ���������������
	 * 
	 * @param disElecTask
	 */
	public void createDistributeElecTask(DistributeElecTask disElecTask);
	
	/**
	 * ����ת����Ϣ��ע
	 * 
	 * @param oids
	 * @param notes
	 */
	public void updateDistributeInfos(String oids,String notes);
	
	/**
	 * ����ת���ĵ�������
	 * 
	 * @param taskOid
	 * @param oids
	 */
	public void createDistributeForwardElecTask(String taskOid,String oids);

	/**
	 * ���������������
	 * 
	 * @param id
	 * @param name
	 * @param note
	 */
	public void createDistributeElecTask(String number, String name, String note);

	/**
	 * ���������������
	 * 
	 * @return DistributeElecTask
	 */
	public DistributeElecTask newDistributeElecTask();

	/**
	 * ���µ�������
	 * 
	 * @param oid String
	 * @param opt LIFECYCLE_OPT
	 */
	public void updateDistributeElecTask(String oids, LIFECYCLE_OPT opt, String returnReason);

	/**
	 * ��ȡ����������طַ����ݺͷַ���Ϣ
	 * 
	 * @return DistributeElecTask
	 */
	public DistributeElecTask getDistributeElecTaskProperty(String oid);

	/**
	 * ɾ����������
	 * 
	 * @param oid
	 */
	public void deleteDistributeElecTask(String oids);
	
	/**
	 * ����������Ϣ
	 * 
	 * @param linkOids
	 * @param iids
	 */
	public void createDistributeInfos(String linkOids, String iids,String taskOid,String type);
	
	/**
	 * �����ַ���Ϣ����
	 * 
	 * @return DistributeInfo
	 */
	public DistributeInfo newDistributeInfo();
	
	/**
	 * �����ַ���Ϣ��ַ�����link����
	 * 
	 * @return DistributeInfo
	 */
	public DistributeTaskInfoLink newDistributeTaskInfoLink();
	
	/**
	 * ��ǩ�յ����������
	 * 
	 * @param distributeElecOid
	 */
	public void updateDistributeElecTaskLife(String distributeElecOid);
	
	/**
	 * ɾ���ַ���Ϣ
	 * 
	 * @param oid
	 */
	public String deleteDistributeInfos(String oids);


	/**
	 * ȡ��������
	 * 
	 * @param oid
	 */
	public List<DistributeElecTask> getDistributeSonElecTask(String oids);
	
	/**
	 * ȡ�ô��ڵ���ת����Ϣ
	 * 
	 * @param linkOids
	 * @param iids
	 * @param taskOid
	 * @param type
	 * @return
	 */
	public String getExitDistributeElecInfo(String linkOids, String iids,String type);
	
	/**
	 * �ж��û��Ƿ���ת��Ȩ��
	 */
    public 	boolean isUserlimit();
    
	/**
	 * �ж��û��Ƿ��Ƕ�������Ա
	 */
    public 	boolean isSecondScheUser();
    
	/**
	 * �ж��û��Ƿ��ǿ���Э��Ա
	 */
    public 	boolean isCrossDomainUser();
    
	/**
	 * ���������͵���վ��
	 * 
	 * @param oids
	 */
	public void sendDistributeToOutSign(String oids);
	
	/**
	 * ���ݵ�������OIDȡ�÷��ŵ�����
	 * 
	 * @param oids
	 */
	public DistributeOrder getDistributeOrderByElecTaskOid(String oids);

	/**
	 * ��ȡ��վ��ͬ������
	 * 
	 * @return
	 */
	public List<DistributeElecTask> getAllDistributeSynTasks(String orderOid, int siteType);
	
	/**
	 * �ж�����ģʽ�ַ���ʽ�����ӷַ�����ʵ��ַ���
	 * 
	 * @return
	 */
	public Boolean getDistributeElecTaskIsEntity(DistributeElecTask elecTask);

}
