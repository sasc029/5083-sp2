package com.bjsasc.ddm.distribute.service.distributesendorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;

/**
 * �ڷ������ⷢ������ӿڡ�
 * 
 * @author yanjia 2013-03-21
 *
 */
@SuppressWarnings("rawtypes")
public interface DistributeSendOrderService {

	/**
	 * Excel�������ɡ�
	 * 
	 * @param mapObject Map
	 * @return HSSFWorkbook
	 */
	public HSSFWorkbook getExcelObject(Map mapObject);
	
	/**
	 * ���ݲ�ѯ������ѯ��Ӧ����
	 * 
	 * @param stateName
	 * @return
	 */
     public List<DistributePaperTask> getAllDistributePaperTask(Map<String, String> map);
	/**
	 * ���ݱ�Ų�ѯ��Ӧ���������Ϣ��
	 * 
	 * @param stateName
	 * @return
	 */
	public List<DistributeObject> getDistributePaperTaskPropertyList(Map<String, String> map,String oid); 
	/**
	 * ���ݲ�ѯ������ѯ�������ٵ�������ϸ��
	 * 
	 * @param stateName
	 * @return
	 */
	public List<DistributeInfo> getDistributeDestroyDetails(Map<String, String> map,Object taskOid);
	
	/**
	 * ������������״̬����ӡ
	 * 
	 * @param map
	 */
	public void updateDistributeInsideCycles(Map<String, String> map, String taskOid);
	
	/**
	 * ����״̬��ѯ���ⷢ�ŵ��ĵ�λ/��Ա
	 * @param disInfoType 
	 * @return List<Map<String, Object>> listMap
	 */
	public List<Map<String, Object>> getDisInfoNames(String disInfoType,String sendtype);

	/**kangyanfei
	 * ���ݷ�����Ϣ���Ͳ�ѯ�������ٷ��ŵ��ĵ�λ/��Ա
	 * @param disInfoType
	 *			�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
	 * @param destroyType
	 *			����������Ϣ��0������Ϣ��1������Ϣ��
	 * @return List<Map<String, Object>> listMap
	 */
	public List<Map<String, Object>> getDisInfoNamesByDisIntfoTypeAndDestroyType(String disInfoType, String destroyType);

}
