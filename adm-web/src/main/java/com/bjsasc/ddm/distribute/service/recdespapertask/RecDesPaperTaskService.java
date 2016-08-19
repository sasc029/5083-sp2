package com.bjsasc.ddm.distribute.service.recdespapertask;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.model.recdespapertask.RecDesPaperTask;



/**
 * ��������ֽ������ķ���ӿ�
 * 
 * @author sunzongqing
 */
public interface RecDesPaperTaskService {
	
	/**
	 * ��ȡ����ָ��״̬�Ļ�����������
	 * 
	 * @param stateName ֽ������״̬����
	 * @return List<RecDesPaperTask> ����ѯ״̬�����л�������ֽ������
	 */
	public List<RecDesPaperTask> getAllRecDesPaperTaskByAuth(String stateName);
	
	/**
	 * ��ȡ����ָ��״̬�Ļ��˻�������ֽ������
	 * 
	 * @param stateName ���˻�������ֽ�������״̬����
	 * @return ����ѯ״̬�����л��˻�������ֽ������
	 */
	public List<RecDesPaperTask> getAllRecDesPaperTaskReturnByAuth(String stateName);
	
	/**
	 * ����������Ļ�������ֽ�����񣬷�����Ӧ����������ģ��
	 * 
	 * @param recDesPaperTask ��Ϊ��������Ļ�������ֽ������
	 */
	public void setRecDesPaperTaskLifecycle(RecDesPaperTask recDesPaperTask);
	
	/**
	 * ����������ı�š����ơ���ע��������������ֽ������
	 * 
	 * @param number ��������ֽ������ı��
	 * @param name	��������ֽ�����������
	 * @param note	��������ֽ������ı�ע
	 */
	public void createRecDesPaperTask(String number, String name, String note);
	
	/**
	 * ͨ������PersistUtil��ķ���������һ���µĻ�������ֽ�����񣬷���ʹ�ù��췽������
	 * 
	 * @return RecDesPaperTask �´����Ļ�������ֽ������
	 */
	public RecDesPaperTask newRecDesPaperTask();
	
	/**
	 * ��ȡ���˻�������ֽ��������ϸ�б�
	 * 
	 * @param taskOid �����OID
	 * @param stateName ״̬����
	 * @return list<RecDesPaperTask> ��ϸ���˻�������ֽ�������б�
	 */
	public List<RecDesPaperTask> getRecDesPaperTaskReturnDetail(String taskOid, String stateName);
	
	/**
	 * ͨ��OID��ȡ��������ֽ�������OrderName��OrderOid
	 * 
	 * @param oid ��������ֽ�������OID
	 * @return ���Ӧ�Ļ�������ֽ������
	 */
	public RecDesPaperTask getRecDesPaperTaskProperty(String oid);
	
	/**
	 * ɾ������OID�Ļ�������ֽ������
	 * 
	 * @param oid ��Ҫɾ���Ļ�������ֽ�������OID
	 */
	public void deleteRecDesPaperTaskProperty(String oid);

	/**
	 * 2014-08-12
	 * @author kangyanfei
	 * ͨ���ַ���ID��ȡ�ַ��������ݡ�
	 * 
	 * @param oid String
	 * @return List
	 */
	public List<RecDesPaperTask> getRecDesPaperTasksByDistributeOrderOid(String oid);
}
