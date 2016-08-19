package com.bjsasc.adm.active.service.activeorderservice;

import java.util.List;
import java.util.Map;

import com.bjsasc.adm.active.model.ActiveObjOrder;
import com.bjsasc.adm.active.model.ActiveOrdered;
import com.bjsasc.adm.active.model.activeorder.ActiveOrder;
import com.bjsasc.adm.active.model.activeorder.ActiveOrderMaster;
import com.bjsasc.adm.active.model.activeorderlink.ActiveOrderLink;
import com.bjsasc.plm.core.persist.model.Persistable;

/**
 * ���е��ݷ���ӿڡ�
 * 
 * @author yanjia 2013-6-3
 */
public interface ActiveOrderService {
	
	/**
	 * ��ʼ�����е��ݶ���
	 * 
	 * @param classId String
	 * @return ActiveOrder
	 */
	public ActiveOrder newActiveOrder(String classId);
	
	/**
	 * ��ʼ�����е��ݶ���,ָ����ʼ״̬
	 * 
	 * @param classId String
	 * @param checkoutState String
	 * @return ActiveOrder
	 */
	public ActiveOrder newActiveOrder(String classId, String checkoutState);

	/**
	 * �������е���������
	 * @return String
	 */
	public String createActiveOrderMaster(String number, String name, String secLevel);

	/**
	 * �������е���
	 * @return String
	 */
	public String createActiveOrder(Map<String, String> paramMap);

	/**
	 * ɾ�����е���
	 * @return String
	 */
	public String deleteActiveOrder(String oids);

	/**
	 * �������е���
	 * @return String
	 */
	public void updataActiveOrder(Map<String, String> paramMap);

	/**
	 * �������е���
	 * @return String
	 */	
	public void updataActiveOrder(ActiveOrder obj);

	/**
	 * �������е���������
	 * @return String
	 */
	public void updataActiveOrderMaster(ActiveOrderMaster masterObj);

	/**
	 * �Ƿ������е�������Դ
	 * 
	 * @return boolean
	 */
	public boolean isActiveOrdered(Persistable object);

	/**
	 * ɾ������ͼ�������ļ�����Link
	 * 
	 * @return String
	 */
	public void deleteActiveOrderLink(String oid);

	/**
	 * ȡ�õ����������ļ�����Link
	 * 
	 * @return List<ActiveOrderLink>
	 */
	public List<ActiveOrderLink> getActiveOrderLinkByObject(String objectOid);

	/**
	 * ȡ�õ����������ļ�����Link-��ǰ����
	 * 
	 * @return List<ActiveOrderLink>
	 */
	public List<ActiveOrderLink> getActiveOrderLinkByBeforeObject(String objectOid);

	/**
	 * ȡ�õ����������ļ�����Link-�ĺ����
	 * 
	 * @return List<ActiveOrderLink>
	 */
	public List<ActiveOrderLink> getActiveOrderLinkByAfterObject(String objectOid);

	/**
	 * ɾ�������������ļ�����Link-��ǰ����
	 * 
	 * @return List<ActiveOrderLink>
	 */
	public void deleteActiveOrderLinkByBeforeObject(ActiveOrderLink activeLink);

	/**
	 * ɾ�������������ļ�����Link-�ĺ����
	 * 
	 * @return List<ActiveOrderLink>
	 */
	public void deleteActiveOrderLinkByAfterObject(ActiveOrderLink activeLink);

	/**
	 * ��Ӹĺ����
	 * 
	 */
	public void addAfterItem(ActiveObjOrder activeOrder, ActiveOrdered activeOrdered);

	/**
	 * ��Ӹĺ����
	 * 
	 */
	public void addAfterItems(ActiveObjOrder activeOrder, List<ActiveOrdered> activeOrdereds);

	/**
	 * ���ݸ�ǰ����Link��Ӹĺ����
	 * 
	 */
	public void addAfterItemByBeforeItem(ActiveOrderLink link, ActiveOrdered activeOrdered);

	/**
	 * ���ݸ�ǰ����ȡ��Link
	 * 
	 */
	public ActiveOrderLink getLinkByBeforeItem(ActiveObjOrder activeOrder, ActiveOrdered activeOrdered);

	/**
	 * ��Ӹ�ǰ����
	 * 
	 */
	public void addBeforeItem(ActiveObjOrder activeOrder, ActiveOrdered activeOrdered);

	/**
	 * ��Ӹ�ǰ����
	 * 
	 */
	public void addBeforeItems(ActiveObjOrder activeOrder, List<ActiveOrdered> activeOrdereds);

	/**
	 * ��ѯ�ĺ����
	 * 
	 * @return  List<ActiveOrdered>
	 */
	public List<ActiveOrderLink> getActiveOrderLinks(String activeOrderOid);

	/**
	 * ��ѯ�ĺ����
	 * 
	 * @return  List<ActiveOrdered>
	 */
	public List<ActiveOrdered> getAfterItems(ActiveObjOrder activeOrder);
	
	/**
	 * ��ѯ��ǰ����
	 * 
	 * @return  List<ActiveOrdered>
	 */
	public List<ActiveOrdered> getBeforeItems(String activeOrderOid);

	/**
	 * ��ѯ��ǰ�������ݸĺ��������е���
	 * 
	 * @return  List<ActiveOrdered>
	 */
	List<ActiveOrdered> getBeforeItemsByOrderAndAfterItem(String activeOrderOid, String objectOid);

	/**
	 * ȡ�����е��ݶ���LIST
	 * @return List<ActiveOrder>
	 * */
	public List<ActiveOrder> getActiveOrderByNumber(String number);
	
	/**
	 * �޸����е��ݶ���
	 */
	public void updataActiveOrderObject(String oid, String beforeOids, String afterOids);
}
