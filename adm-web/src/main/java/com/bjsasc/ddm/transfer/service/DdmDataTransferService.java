package com.bjsasc.ddm.transfer.service;

import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.transfer.exception.DdmTransferException;

/**
 * ���Ź���ģ������ṩ������ת���ӿڷ�����
 * 
 * @author gaolingjie, 2013-2-20
 */
public interface DdmDataTransferService {

	/**
	 * �Ѷ����� �ĵ������������ĵ���ͼ�ĵ��ȣ�ת���ɷַ����ݶ���
	 * 
	 * @param fromData Ҫת��������
	 * @return �ַ�����
	 * @throws DdmTransferException
	 */
	public DistributeObject transferToDdmData(Object fromData) throws DdmTransferException ;

}
