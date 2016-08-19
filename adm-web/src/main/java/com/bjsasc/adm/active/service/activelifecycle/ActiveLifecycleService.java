package com.bjsasc.adm.active.service.activelifecycle;

import com.bjsasc.platform.objectmodel.business.lifeCycle.State;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;

/**
 * �������ݹ����������ڷ�����ӿ�
 * 
 * @author yanjia, 2013-6-25
 */
public interface ActiveLifecycleService {

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
	public void updateLifeCycleByStateName(LifeCycleManaged target, String stateName);

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
}
