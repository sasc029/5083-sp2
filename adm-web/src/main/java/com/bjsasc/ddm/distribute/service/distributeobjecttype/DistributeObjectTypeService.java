package com.bjsasc.ddm.distribute.service.distributeobjecttype;

import java.util.List;
import java.util.Map;

import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.system.principal.User;

/**
 * ����ģ�����÷���ӿڡ�
 * 
 * @author gengancong 2014-5-5
 */
public interface DistributeObjectTypeService {
	
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
	 * @param oids ��ɫ/��ԱOIDS
	 */
	public void deleteTypeData(String contextOid, String oids);
	
	/**
	 * ������ݡ�
	 * 
	 * @param contextOid ������OID
	 * @param listTypeMap �ύ����
	 */
	public void addTypeData(String contextOid, List<Map<String, String>> listTypeMap);

	
	/**
	 * �༭���ݡ�
	 * 
	 * @param contextOid ������OID
	 * @param listMap �ύ����
	 * @param listTypeMap �ύ����
	 */
	public void editTypeData(String contextOid, List<Map<String, String>> listMap, List<Map<String, String>> listTypeMap);

	/**
	 * ͨ���û���Ϣ���û����ڽ�ɫ����������Ϣȡ���û��ɲ��������͡� 
	 * 1���ж�ָ���������Ƿ��Ѿ�������������Ϣ 
	 * 2����ȡ�������а����óе��ߵĽ�ɫ
	 * 3����ȡ���Ź����������������Ϣ
	 * 
	 * @param context ������
	 * @param user �û�
	 */
	public Map<String, List<String>> getContextUserType(Context context, User user);
	
	/**
	 * ��ȡ�������õ����ͣ���������
	 * @return
	 */
	public Map<String, List<String>> getAllType();
	
}
