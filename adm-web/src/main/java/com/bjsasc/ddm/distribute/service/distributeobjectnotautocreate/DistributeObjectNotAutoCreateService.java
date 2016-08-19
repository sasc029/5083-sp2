package com.bjsasc.ddm.distribute.service.distributeobjectnotautocreate;

import java.util.List;
import java.util.Map;

import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.system.principal.User;

public interface DistributeObjectNotAutoCreateService {
	/**
	 * ͨ��������OIDȡ�����ݡ�
	 * 
	 * @param contextOid ������OID
	 * @return List
	 */
	public List<Map<String, Object>> findByContextOid(String contextOid);
	public List<Map<String, Object>> findByContextOid(String contextOid, String spot, String spotInstance);

	/**
	 * ɾ�����ݡ�
	 * 
	 * @param contextOid ������OID
	 * @param typeNames
	 */
	public void deleteTypeData(String contextOid, String typeNames);
	
	/**
	 * ������ݡ�
	 * 
	 * @param contextOid ������OID
	 * @param listTypeMap �ύ����
	 */
	public void addTypeData(String contextOid, List<Map<String, String>> listTypeMap);


	/**
	 * �� 
	 * 1���ж�ָ���������Ƿ��Ѿ�������������Ϣ 
	 * 
	 * 
	 * 
	 * @param context ������
	 * @param 
	 */
	public List<String> getContextType(Context context);
}
