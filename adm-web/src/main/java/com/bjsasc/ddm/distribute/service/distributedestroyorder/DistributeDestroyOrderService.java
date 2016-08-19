package com.bjsasc.ddm.distribute.service.distributedestroyorder;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.model.recdesinfo.RecDesInfo;
import com.bjsasc.ddm.distribute.model.recdespapertask.RecDesPaperTask;

/**
 * ���յ������ٵ�����ӿڡ�
 * 
 * @author yanjia 2013-3-21
 * 
 */
@SuppressWarnings("rawtypes")
public interface DistributeDestroyOrderService {
	
	/**
	 * ���ݲ�ѯ������ѯ��Ӧ����
	 * 
	 * @param stateName
	 * @return
	 */
	public List<DistributePaperTask> getAllDistributePaperTask(Map<String, String> map,Object stateName);
	
	/**
	 * ����MAP�����ṩ�Ĳ�ѯ�������������õĻ�������ֽ������
	 * 
	 * @param map �洢��ѯ������map
	 * @param stateName ��������ֽ���������������״̬
	 * @return List<RecDesPaperTask> ���ݲ�ѯ�������صĻ�������ֽ������
	 * @author Sun Zongqing
	 */
	public List<RecDesPaperTask> getAllRecDesPaperTask(Map<String, String> map, Object stateName);
	
	/**
	 * ���ݲ�ѯ������ѯ�������ٵ�������ϸ��
	 * 
	 * @param stateName
	 * @return
	 */
	public List<DistributeInfo> getDistributeDestroyDetails(Map<String, String> map,Object taskOids);
	
	/**
	 * ����map�ṩ�Ĳ�ѯ�����ͻ�����������OID����ѯ���������������صĻ���������Ϣ
	 * 
	 * @param map �ṩ��ѯ������map���������ڴ���sql���
	 * @param taskOids �������������OID
	 * @return ���ݲ�ѯ������õĻ���������Ϣ
	 * @author Sun Zongqing
	 */
	public List<RecDesInfo> getRecDesDetails(Map<String, String> map, Object taskOids);
	
	/**
	 * ���»������ٷ���
	 * 
	 * @param oids
	 * @param destroyNums
	 */
	public void updateDestroyNum(Map<String, String> map, String taskOids, String oids, String destroyNums);
	
	/**
	 * ���ݻ������������ṩ�Ĳ��������·ַ���Ϣ�Ļ������ٷ�����ֽ�ʷַ�������������ڡ�
	 * ��������������������ڡ�����������Ϣ���������ڡ�����/���ٵ���������
	 * 
	 * @param map �ṩ��ѯ������map�������ṩdestroyType����
	 * @param taskOids ��Ҫ�����������ڵĻ�������ֽ������
	 * @param oids ��Ҫ�����������ڵĻ���������Ϣ
	 * @param destroyNums ����/���ٵķ���
	 * 
	 * @author Sun Zongqing
	 */
	public void updateDestroyNumByRecDesTask(Map<String, String> map, String taskOids, String oids, String destroyNums);
	
	/**
	 * Excel�������ɡ�
	 * 
	 * @param map Map
	 * @return HSSFWorkbook
	 */
	public HSSFWorkbook getExcelObject(Map mapObject);
	
	/**
	 * �����ṩ�ķַ�ֽ���������������ֽ�����񣬷���һ������δ���ɻ���������Ϣ�ķַ���Ϣ��ֽ�ʷַ������б�
	 * (�÷ַ���Ϣ�����������Ϣ�Ľ�����/��λ��Ϣ��ͬ����Ϊ��ͨ��ͬһ���������ó�)
	 * 
	 * @param dis ֽ�ʷַ������б�
	 * @param rec ����������Ϣ�б�
	 * @return List<DistributePaperTask> �õ����·ַ������б�
	 * @author Sun Zongqing
	 */
	public List<DistributePaperTask> removeDisTaskConflictWithRecDesTask(List<DistributePaperTask> dis, List<RecDesPaperTask> rec);
	
	/**
	 * �����ṩ�ķַ���Ϣ�����������Ϣ���Ƴ�����������������Ϣ�ķַ���Ϣ
	 * 
	 * @param dis �ַ���Ϣ�б�
	 * @param recDes ����������Ϣ�б�
	 * @return �Ƴ�����������Ϣ��ķַ���Ϣ�б�
	 * @author Sun Zongqing
	 */
	public List<DistributeInfo> removeDisInfoConflictWithRecDesInfo(List<DistributeInfo> dis, List<RecDesInfo> recDes);
	
	/**
	 * ȡ�����еĻ�������ֽ������
	 * 
	 * @return List<RecDesPaperTask> �õ������л�������ֽ������
	 * @author Sun Zongqing
	 */
	public List<RecDesPaperTask> getAllRecDesPaperTask(String lc);

}
