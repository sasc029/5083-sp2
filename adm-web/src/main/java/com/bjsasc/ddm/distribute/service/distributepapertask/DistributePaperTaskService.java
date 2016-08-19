package com.bjsasc.ddm.distribute.service.distributepapertask;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;

/**
 * ֽ���������ӿڡ�
 * 
 * @author guowei 2013-2-22
 *
 */
public interface DistributePaperTaskService {

	/**
	 * ��ȡ����ֽ������
	 */
	public List<DistributePaperTask> getAllDistributePaperTaskByAuth(String stateName);
	
	/**
	 * ���´�ӡҳ���ʼ����Ĭ����ʾһ�����ڵ�����
	 * @param stateName
	 * @return
	 */
	public List<DistributePaperTask> getSearchProcessingDistributePaperTaskByTime(long time,String stateName,long currentTime );
	/**
	 * ��ӡ�ӹ�����ģ��������������ť
	 * @param stateName
	 * @return
	 */
	public List<DistributePaperTask> getSearchProcessingDistributePaperTaskByAuth(String stateName,String keyWords );
	
	/**
	 * ��ȡ���л���ֽ������
	 */
	public List<DistributePaperTask> getAllDistributePaperTaskReturnByAuth(String stateName);

	/**
	 * ����ֽ���������
	 * 
	 * @param disPaperTask
	 */
	public void createDistributePaperTask(DistributePaperTask disPaperTask);

	/**
	 * ����ֽ���������
	 * 
	 * @param id
	 * @param name
	 * @param note
	 */
	public void createDistributePaperTask(String number, String name, String note);

	/**
	 * ����ֽ���������
	 * 
	 * @return DistributePaperTask
	 */
	public DistributePaperTask newDistributePaperTask();

	/**
	 * ��ȡ������ϸ�б�
	 * @param taskOid
	 * @return
	 */
	public List<DistributePaperTask> getDistributeTaskReturnDetail(String taskOid,String stateName);

	/**
	 * ��ȡֽ��������طַ����ݺͷַ���Ϣ
	 * 
	 * @return DistributePaperTask
	 */
	public DistributePaperTask getDistributePaperTaskProperty(String oid);


	/**
	 * ɾ��ֽ������
	 * 
	 * @param oid
	 */
	public void deleteDistributePaperTaskProperty(String oids);

	/**
	 * ȡ�ø���
	 * 
	 * @param oid
	 * @return
	 */
	public List<Map<String,Object>> listDistributeObjectFiles(String oid,String contextPath);
	
	/**
	 * Excel�������ɡ�
	 * 
	 * @param oid String
	 * @return HSSFWorkbook
	 */
	public HSSFWorkbook getExcelObject(String oid);
	
	/**
	 * Excel�������ɡ�
	 * 
	 * @param oid String
	 * @return HSSFWorkbook
	 */
	public HSSFWorkbook getExcelObjectForDept7(String oid);

}
