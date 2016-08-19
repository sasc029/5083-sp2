package com.bjsasc.ddm.distribute.service.distributelifecycle;

import com.bjsasc.platform.objectmodel.business.lifeCycle.State;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;

/**
 * ���Ź����������ڷ�����ӿ�
 * 
 * @author gaolingjie, 2013-2-28
 */
public interface DistributeLifecycleService {

	/**
	 * ��ʼ�����Ĺ�����Ϣ
	 * 
	 * @param target
	 */
	public void initLifecycle(LifeCycleManaged target);

	/**
	 * ������������״̬
	 * 
	 * @param target
	 *            Ŀ�����
	 * @param lifecycleState
	 *            ��������״̬
	 */
	public void updateLifeCycle(LifeCycleManaged target, State lifecycleState);

	/**
	 * ������������״̬��������������������
	 * 
	 * @param target
	 *            Ŀ�����
	 * @param stateName
	 *            ��������״̬����
	 */
	public void updateLifeCycleByStateName(LifeCycleManaged target,
			String stateName);

	/**
	 * ������������״̬id������������ģ��
	 * 
	 * @param target
	 *            Ŀ�����
	 * @param stateId
	 *            ��������id
	 */
	public void updateLifeCycleByStateId(LifeCycleManaged target, String stateId);

	/**
	 * ������������״̬
	 * 
	 * @param target
	 *            Ŀ�����
	 */
	public void promoteLifeCycle(LifeCycleManaged target);

	/**
	 * ������������״̬
	 * 
	 * @param target
	 *            Ŀ�����
	 */
	public void demoteLifeCycle(LifeCycleManaged target);

	/**
	 * �ܾ���������״̬
	 * 
	 * @param target
	 *            Ŀ�����
	 */
	public void rejectLifeCycle(LifeCycleManaged target);
	
	/**
	 * ������������״̬id������������ģ��(ֻ�����ڷַ��������������������)
	 * 
	 * @param target
	 *            Ŀ�����
	 * @param stateId
	 *            ��������id
	 */
	public void updateLifeCycleByStateIdNew(LifeCycleManaged target, String stateId);
}
