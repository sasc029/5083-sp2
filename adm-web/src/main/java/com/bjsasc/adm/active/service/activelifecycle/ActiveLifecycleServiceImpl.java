package com.bjsasc.adm.active.service.activelifecycle;

/**
 * �������ݹ���ģ���Զ����������ڷ�����
 * 
 * @author yanjia, 2013-6-25
 */
public class ActiveLifecycleServiceImpl extends ActiveDefaultLifecycleServiceImpl {
	//
	//	@Override
	//	public void promoteLifeCycle(LifeCycleManaged target) {
	//
	//		/*// ��ȡ��ǰ��������״̬����
	//		String currentStateName = target.getLifeCycleInfo().getStateName();
	//
	//		// ��һ�ڵ���������״̬����
	//		String nextStateName = null;
	//
	//		ActiveLifecycleModel lifecycleModel = AdmLifeCycleLoad.getLifecycleModel(target.getClass().getName());
	//
	//		if (null != lifecycleModel && null != lifecycleModel.getActiveLifeCycleStateByName(currentStateName)) {
	//			nextStateName = lifecycleModel.getActiveLifeCycleStateByName(currentStateName).getNextStateName();
	//		}
	//
	//		// ���ø��෽����������������״̬
	//		if (!StringUtil.isNull(nextStateName)) {
	//			super.updateLifeCycleByStateName(target, nextStateName);
	//		}*/
	//	}
	//
	//	@Override
	//	public void demoteLifeCycle(LifeCycleManaged target) {
	//		/*// ��ȡ��ǰ��������״̬����
	//		String currentStateName = target.getLifeCycleInfo().getStateName();
	//
	//		// ��һ�ڵ���������״̬����
	//		String previousStateName = null;
	//
	//		ActiveLifecycleModel lifecycleModel = AdmLifeCycleLoad.getLifecycleModel(target.getClass().getName());
	//
	//		if (null != lifecycleModel && null != lifecycleModel.getActiveLifeCycleStateByName(currentStateName)) {
	//			previousStateName = lifecycleModel.getActiveLifeCycleStateByName(currentStateName).getPreviousStateName();
	//		}
	//
	//		// ���ø��෽����������������״̬
	//		if (!StringUtil.isNull(previousStateName)) {
	//			super.updateLifeCycleByStateName(target, previousStateName);
	//		}*/
	//	}
	//
	//	@Override
	//	public void rejectLifeCycle(LifeCycleManaged target) {
	//		/*// ��ȡ��ǰ��������״̬����
	//		String currentStateName = target.getLifeCycleInfo().getStateName();
	//
	//		// "�ܾ�"�ڵ���������״̬����
	//		String rejectiveStateName = null;
	//
	//		ActiveLifecycleModel lifecycleModel = AdmLifeCycleLoad.getLifecycleModel(target.getClass().getName());
	//
	//		if (null != lifecycleModel && null != lifecycleModel.getActiveLifeCycleStateByName(currentStateName)) {
	//			rejectiveStateName = lifecycleModel.getActiveLifeCycleStateByName(currentStateName).getRejectiveStateName();
	//		}
	//
	//		// ���ø��෽����������������״̬
	//		if (!StringUtil.isNull(rejectiveStateName)) {
	//			super.updateLifeCycleByStateName(target, rejectiveStateName);
	//		}*/
	//	}
}
