package com.bjsasc.ddm.distribute.service.duplicateprocess;

import java.util.List;

import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;

/**
 * ���Ƽӹ�����ӿڡ�
 * 
 * @author guowei 2013-3-11
 */
public interface DuplicateProcessService {
	
	/**
	 * ��ȡ����ֽ������
	 * @return
	 */
	public List<DistributePaperTask> getAllDistributePaperTaskByAuth(String stateName);
	
	/**
	 * ��ȡ���л���ֽ������
	 * @return
	 */
	public List<DistributePaperTask> getAllDistributePaperTaskReturnByAuth(String stateName);
	
	/**
	 * ��ȡѡ�е�������Ϣ
	 * @param oids
	 * @return
	 */
	public List<DistributePaperTask> getDuplicateProcessInfo(String oids);
	
	/**
	 * ���������˺͸�ӡ��
	 * @param oids
	 */
	public void updateDuplicateProcessor(String oids,String collator,String contractor);
	
	/**
	 * ¼��ӹ���Ϣ
	 * @param oid
	 * @return
	 */
	public List<DistributeObject> listDuplicateProcessInfo(String oid);
	
	/**
	 * ¼��ӹ���Ϣ��ͬ�⣩
	 * 
	 * @param oids
	 * @param collator
	 * @param contractor
	 */
	public void updateProcessInfo(String collator,String contractor,String oids,String taskOid);
	
	/**
	 * ���¼ӹ���Ϣ����ͬ�⣩
	 * @param oids
	 */
	public void updateDisAgreeInfo(String oids,String returnReason,String taskOid);

}
