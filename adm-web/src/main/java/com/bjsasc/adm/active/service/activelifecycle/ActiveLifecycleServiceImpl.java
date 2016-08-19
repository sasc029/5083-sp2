package com.bjsasc.adm.active.service.activelifecycle;

/**
 * 现行数据管理模块自定义生命周期服务类
 * 
 * @author yanjia, 2013-6-25
 */
public class ActiveLifecycleServiceImpl extends ActiveDefaultLifecycleServiceImpl {
	//
	//	@Override
	//	public void promoteLifeCycle(LifeCycleManaged target) {
	//
	//		/*// 获取当前生命周期状态名称
	//		String currentStateName = target.getLifeCycleInfo().getStateName();
	//
	//		// 下一节点生命周期状态名称
	//		String nextStateName = null;
	//
	//		ActiveLifecycleModel lifecycleModel = AdmLifeCycleLoad.getLifecycleModel(target.getClass().getName());
	//
	//		if (null != lifecycleModel && null != lifecycleModel.getActiveLifeCycleStateByName(currentStateName)) {
	//			nextStateName = lifecycleModel.getActiveLifeCycleStateByName(currentStateName).getNextStateName();
	//		}
	//
	//		// 调用父类方法，更新生命周期状态
	//		if (!StringUtil.isNull(nextStateName)) {
	//			super.updateLifeCycleByStateName(target, nextStateName);
	//		}*/
	//	}
	//
	//	@Override
	//	public void demoteLifeCycle(LifeCycleManaged target) {
	//		/*// 获取当前生命周期状态名称
	//		String currentStateName = target.getLifeCycleInfo().getStateName();
	//
	//		// 上一节点生命周期状态名称
	//		String previousStateName = null;
	//
	//		ActiveLifecycleModel lifecycleModel = AdmLifeCycleLoad.getLifecycleModel(target.getClass().getName());
	//
	//		if (null != lifecycleModel && null != lifecycleModel.getActiveLifeCycleStateByName(currentStateName)) {
	//			previousStateName = lifecycleModel.getActiveLifeCycleStateByName(currentStateName).getPreviousStateName();
	//		}
	//
	//		// 调用父类方法，更新生命周期状态
	//		if (!StringUtil.isNull(previousStateName)) {
	//			super.updateLifeCycleByStateName(target, previousStateName);
	//		}*/
	//	}
	//
	//	@Override
	//	public void rejectLifeCycle(LifeCycleManaged target) {
	//		/*// 获取当前生命周期状态名称
	//		String currentStateName = target.getLifeCycleInfo().getStateName();
	//
	//		// "拒绝"节点生命周期状态名称
	//		String rejectiveStateName = null;
	//
	//		ActiveLifecycleModel lifecycleModel = AdmLifeCycleLoad.getLifecycleModel(target.getClass().getName());
	//
	//		if (null != lifecycleModel && null != lifecycleModel.getActiveLifeCycleStateByName(currentStateName)) {
	//			rejectiveStateName = lifecycleModel.getActiveLifeCycleStateByName(currentStateName).getRejectiveStateName();
	//		}
	//
	//		// 调用父类方法，更新生命周期状态
	//		if (!StringUtil.isNull(rejectiveStateName)) {
	//			super.updateLifeCycleByStateName(target, rejectiveStateName);
	//		}*/
	//	}
}
