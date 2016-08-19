package com.bjsasc.ddm.distribute.service.distributeconfigparameter;

import java.util.List;
import java.util.Map;

/**
 * ���Ź���������÷���ӿڡ�
 * 
 * @author gengancong 2014-5-5
 */
public interface DistributeConfigParameterService {
	
	/**
	 * ͨ��������OIDȡ�����ݡ�
	 * 
	 * @param contextOid ������OID
	 * @param paramId ����
	 * @return List
	 */
	public List<Map<String, Object>> findByContextOid(String contextOid, String paramId);
	public boolean exists(String contextOid, String paramId);

	/**
	 * ɾ�����ݡ�
	 * 
	 * @param contextOid ������OID
	 */
	public void deleteConfigData(String contextOid);
	
	/**
	 * ������ݡ�
	 * 
	 * @param contextOid ������OID
	 * @param listTypeMap �ύ����
	 */
	public void addConfigData(String contextOid, String paramId,
			String paramName, String defaultValue, String currentValue,
			String state, String description);
	
	/**
	 * ��ȡ���е�������
	 * @return
	 */
	public Map<String, List<String>> getAllParam();
}
