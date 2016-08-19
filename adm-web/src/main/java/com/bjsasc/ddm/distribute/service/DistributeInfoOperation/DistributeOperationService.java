package com.bjsasc.ddm.distribute.service.DistributeInfoOperation;

import java.util.List;

import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
/**
 * 
 * @author kangyanfei 2014-07-15
 *
 */
public interface DistributeOperationService {

	/**
	 * ���ݷ��ŵ���ѯ�ַ����ݼ�
	 * @param disOrder
	 * 			���ŵ�oid
	 * @return	List<DistributeObject> 
	 * 			�ַ�����
	 */
	public List<DistributeObject> getDistributeObjectsByDisOrder(DistributeOrder disOrder);

	/**
	 * ��������ַ���Ϣ
	 * @param disOrder
	 * 			���ŵ�
	 * @param disObj
	 * 			�ַ�����
	 * @param disInfos
	 * 			�ַ���Ϣ��
	 */
	public void inserDisInfos(DistributeOrder disOrder,DistributeObject disObj, List<DistributeInfo> disInfos);
}
