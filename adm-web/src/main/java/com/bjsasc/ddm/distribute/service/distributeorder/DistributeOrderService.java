package com.bjsasc.ddm.distribute.service.distributeorder;

import java.util.List;

import com.bjsasc.ddm.common.ConstUtil.LIFECYCLE_OPT;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.plm.core.persist.model.Persistable;

/**
 * �ַ�������ӿڡ�
 * 
 * @author gengancong 2013-2-22
 */
public interface DistributeOrderService {

	/**
	 * ��ȡ���з��ŵ�����
	 * 
	 * @param stateName
	 * @return
	 */
	public List<DistributeOrder> getAllDistributeOrderByAuth(String stateName);
	/**
	 * ��ȡһ�ֻ����״̬�µķ��ŵ�
	 * 
	 * @param stateArray
	 * @return
	 */
	public List<DistributeOrder> listDiffStatesDistributeOrder(String [] stateArray);
	/**
	 * ���ŵ��ѷַ�ҳ����������ť����
	 * 
	 * @param stateArray
	 * @param keyWords(ҳ��������ؼ���)
	 * @return
	 */
	public List<DistributeOrder> listSendedDistributeOrderAdderSearchIuput(String [] stateArray,String keyWords);
	/**
	 * ���ŵ��ѷַ�ҳ���һ�μ���ҳ����ʾ��ǰ�µ�����
	 * 
	 * @param stateArray
	 * @param time����ǰʱ���һ����ǰ��ʱ�䣩
	 * @param currentTime����ǰʱ�䣩
	 * @return
	 */
	public List<DistributeOrder> listDiffStatesDistributeOrderOnload(String [] stateArray,long time,long currentTime);
	/**
	 * ��ȡһ�ֻ����״̬����״̬�ķ��ŵ�
	 * 
	 * @param notInStateArray
	 * @return
	 */
	public List<DistributeOrder> listNotInStatesDistributeOrder(String [] notInStateArray);

	/**
	 * ȡ����ط��ŵ���
	 * 
	 * @param oid Object
	 * @return List
	 */
	public List<DistributeOrder> getRelatedDistributeOrder(Object oid);
	
	
	/**
	 * ȡ����ֽ��������صķַ���
	 * 
	 * @param oid
	 * @return �ַ����б�
	 * @modify Sun Zongqing
	 * @modifyDate 2014/7/2
	 */
	public List<DistributeOrder> getRelatetaskdDistributeOrder(String oid);
	

	/**
	 * ��ȡ���б����˷��ŵ�����
	 * 
	 * @param stateName
	 * @return
	 */
	public List<DistributeOrder> getAllDistributeOrderReturnByAuth(String stateName);

	/**
	 * ��ȡ������Ϣ��
	 * 
	 * @param stateName
	 * @return
	 */
	public List<DistributeOrder> getDistributeOrderReturnDetail(String stateName, String oid);

	/**
	 * �������ŵ�����
	 * 
	 * @param oid String
	 */
	public DistributeOrder createDistributeOrderAndObject(String number, String name, String orderType, String note,
			String oid, boolean aotuCreateFlag, String starterId);
	
	/**
	 * �ļ���"����"��������������ר�õĴ������ŵ����󷽷���
	 * 
	 * @param oid String
	 */
	public DistributeOrder createDistributeOrderAndObjectMulti(String number,String name, String orderType, String note, 
			String oid, boolean aotuCreateFlag,String starterId);

	/**
	 * ȡ�÷��ŵ���ַ�����link����
	 * 
	 * @param distributeOrderOids List
	 * @return List
	 */
	public List<DistributeOrderObjectLink> getDistributeOrderObjectLinksByOids(List<String> distributeOrderOids);

	/**
	 * �ж��Ƿ�Ϊ���Դ������ŵ��Ķ���
	 * 
	 * @param  target
	 * @return true:����;false:������
	 */
	public boolean isCanCreateDistributeOrder(Persistable target);
	
	/**
	 * �������ŵ�����
	 * 
	 * @param dis DistributeOrder
	 */
	public void createDistributeOrder(DistributeOrder dis);

	/**
	 * �������ŵ�����
	 * 
	 * @param id String
	 * @param name String
	 * @param orderType String
	 * @param note String
	 */
	public void createDistributeOrder(String number, String name, String orderType, String note);

	/**
	 * ���·��ŵ�����
	 * 
	 * @param dis DistributeOrder
	 */
	public void updateDistributeOrder(DistributeOrder dis);

	/**
	 * ���·��ŵ�����
	 * 
	 * @param oid String
	 * @param number String
	 * @param name String
	 * @param orderType String
	 * @param note String
	 */
	public void updateDistributeOrder(String oid, String name, String note);

	/**
	 * �������ŵ�����
	 * 
	 * @return DistributeOrder
	 */
	public DistributeOrder newDistributeOrder();
	/**
	 * ɾ�����ŵ�����
	 * 
	 * @return DistributeOrder
	 */
	public void deleteDistributeOrderByOid(String distributeOrderOids);

	/**
	 * �޸ķ��ŵ��������ڡ�
	 * 
	 * @return DistributeOrder
	 */
	public void updateDisOrderLifeCycle(String oids, LIFECYCLE_OPT opt, String returnReason);

	/**
	 * ���ŵ�������������
	 * 
	 * @param oid
	 */
	public String updateOrderLifeCycle(String oid);
	
	/**
	 * ȡ���Ƿ���ڵ�������
	 * 
	 * @param orderOid
	 * @return
	 */
	public String getExistDistributeElecTask(String orderOid);

	/**
	 * ���ݷ��ŵ�OID,״̬������ԴOIDȡ�÷��ŵ�
	 * 
	 * @param orderOid
	 * @param dataOid
	 * @return List<DistributeOrder>
	 */
	public List<DistributeOrder> getDistributeOrderByOrdStateAndDataOid(String orderOid,String dataOid);

	/**
	 * ֱ����ⴴ�����ŵ�����
	 * 
	 * @param oid String
	 */
	public DistributeOrder createDisOrderAndObject(String number, String name, String orderType, String note,
			String oid, boolean aotuCreateFlag);
	
	/**
	 * �����ַ���-ѡ�������ַ���ֱ�ӽ��в���
	 * @param orderOidsStr
	 * @param number
	 * @param name
	 * @param orderType
	 * @param note
	 */
	public DistributeOrder  reissueDistributeOrder (String  orderOidsStr,String number, String name, String orderType, String note);
	
	/**
	 * ȡ�÷ַ�����Ĭ�Ϸַ���������
	 * 
	 * @param oid ���ŵ�OID
	 * @return Ĭ�Ϸַ���������
	 */
	public String getDefaultDistributeOrderType(String oid);
	
	/**
	 * ���������ŵ�����ӷַ�����
	 * @param order
	 * @param disObjOidList
	 */
	public void addDisObjectToDisOrder(DistributeOrder order, List<String> disObjOidList);
}
