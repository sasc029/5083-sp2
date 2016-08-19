package com.bjsasc.adm.active.service.activesetservice;

import java.util.List;
import java.util.Map;

import com.bjsasc.adm.active.model.ActiveObjSet;
import com.bjsasc.adm.active.model.ActiveSeted;
import com.bjsasc.adm.active.model.activeset.ActiveSet;
import com.bjsasc.adm.active.model.activeset.ActiveSetMaster;
import com.bjsasc.adm.active.model.activesetlink.ActiveSetLink;
import com.bjsasc.plm.core.persist.model.Persistable;

/**
 * �����׷���ӿڡ�
 * 
 * @author yanjia 2013-5-13
 */
public interface ActiveSetService {
	
	/**
	 * ��ʼ�������׶���
	 * 
	 * @param classId String
	 * @return ActiveSet
	 */
	public ActiveSet newActiveSet(String classId);
	
	/**
	 * ��ʼ�������׶���,ָ����ʼ״̬
	 * 
	 * @param classId String
	 * @param checkoutState String
	 * @return ActiveSet
	 */
	public ActiveSet newActiveSet(String classId, String checkoutState);
	
	/**
	 * ȡ������������
	 * 
	 * @return List<ActiveSet>
	 * */
	public List<ActiveSet> getAllActiveSet();

	/**
	 * ����������������
	 * 
	 * @return String
	 */
	public String createActiveSetMaster(String number, String name, String secLevel);

	/**
	 * ����������
	 * 
	 * @return String
	 */
	public String createActiveSet(Map<String, String> paramMap);

	/**
	 * ɾ��������
	 * 
	 * @return String
	 */
	public String deleteActiveSet(String oids);

	/**
	 * ����������
	 * 
	 * @return String
	 */
	public String updataActiveSet(Map<String, String> paramMap);

	/**
	 * ����������
	 * 
	 * @return String
	 */
	public String updataActiveSet(ActiveSet obj);

	/**
	 * ����������������
	 * 
	 * @return String
	 */
	public String updataActiveSetMaster(ActiveSetMaster masterObj);

	/**
	 * ȡ�������ļ�������
	 * 
	 * @return List<ActiveSetMaster>
	 * */
	public List<ActiveSetMaster> getAllActiveSetMaster();

	/**
	 * ɾ����ͼ�������ļ�����Link
	 * 
	 * @return String
	 */
	public String deleteActiveSetLink(String oid);

	/**
	 * ȡ����ͼ�������ļ�����Link
	 * 
	 * @return List<ActiveSetLink>
	 */
	public List<ActiveSetLink> getActiveSetLinkByOID(String oids);

	/**
	 * ɾ����ͼ�������ļ�����Link
	 * 
	 * @return String
	 */
	public String deleteActiveSetLink(String setOid, String objectOid);

	/**
	 * ȡ����ͼ�������ļ�����Link
	 * 
	 * @return List<ActiveSetLink>
	 */
	public List<ActiveSetLink> getActiveSetLinkByObject(String objectOid);

	/**
	 * �Ƿ�������������Դ
	 * 
	 * @return boolean
	 */
	public boolean isActiveSeted(Persistable object);

	/**
	 * ��Ӷ�������
	 * 
	 * */
	public void addToActiveSet(ActiveObjSet activeSet, ActiveSeted activeSeted);

	/**
	 * ��Ӷ�������
	 * 
	 * */
	public void addToActiveSet(ActiveObjSet activeSet, List<ActiveSeted> activeSeteds);

	/**
	 * ȡ�����ж���
	 * 
	 * @return  List<ActiveSeted>
	 */
	public List<ActiveSeted> getActiveItems(ActiveObjSet activeSet);

	/**
	 * ����ͼ�������Ƴ�����
	 * 
	 */
	public void removeFromActiveSet(ActiveObjSet activeSet, ActiveSeted activeSeted);

	/**
	 * ����ͼ�������Ƴ�����
	 * 
	 */
	public void removeFromActiveSet(ActiveObjSet activeSet, List<ActiveSeted> activeSeteds);

	/**
	 * ����ͼ���Ƴ�LINK����
	 * 
	 */
	public void removeActiveSetedLink(ActiveSetLink activeSetedLink);

	/**
	 * ɾ�����LINK
	 * 
	 */
	public void deleteLink(String oid);

	/**
	 * ȡ�������׶���LIST
	 * @return List<ActiveSet>
	 * */
	public List<ActiveSet> getActiveSetByNumber(String number);
}
