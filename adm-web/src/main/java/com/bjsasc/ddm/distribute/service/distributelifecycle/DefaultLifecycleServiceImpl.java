package com.bjsasc.ddm.distribute.service.distributelifecycle;

import java.util.List;

import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.service.activelifecycle.ActiveLifecycleService;
import com.bjsasc.adm.common.AdmLifeCycleConstUtil;
import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeworkload.DistributeWorkload;
import com.bjsasc.ddm.distribute.self.DdmSelfDefHelper;
import com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService;
import com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService;
import com.bjsasc.ddm.distribute.service.distributeworkload.DistributeWorkloadService;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleTemplate;
import com.bjsasc.platform.objectmodel.business.lifeCycle.State;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.ContextHelper;
import com.bjsasc.plm.core.context.model.Contexted;
import com.bjsasc.plm.core.lifecycle.LifeCycleHelper;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.util.PlmException;

/**
 * 生命周期管理服务类（该类是默认实现，工作流状态按照工作流模板一步一步往下走，不存在跳跃设置生命周期状态的情况）
 * 
 * @author gaolingjie, 2013-2-28
 */
public class DefaultLifecycleServiceImpl implements DistributeLifecycleService {

	@Override
	public void initLifecycle(LifeCycleManaged target) {

		// 工作量统计
		DistributeWorkloadService workload = DistributeHelper.getDistributeWorkloadService();
		DistributeWorkload newDistributeWorkload = workload.newDistributeWorkload(target);

		Context rootContext = ContextHelper.getService().getRootContext();
		
		if(target instanceof Contexted){
			Contexted contexted = (Contexted)target;
			if(null != contexted.getContextInfo()){
				rootContext = contexted.getContextInfo().getContext();
			}
		}

		DdmSelfDefHelper.getRuleService().init((Persistable) target, rootContext);

		// 工作量统计
		workload.createDistributeWorkload(newDistributeWorkload, target);
	}

	@Override
	public void updateLifeCycle(LifeCycleManaged target, State lifecycleState) {
		LifeCycleHelper.getService().setLifeCycleState(target, lifecycleState);
		//PersistHelper.getService().update(target);
	}

	@Override
	public void updateLifeCycleByStateName(LifeCycleManaged target, String stateName) {
		// 获取生命周期模板
		LifeCycleTemplate lifeCycleTemplate = LifeCycleHelper.getService().getLifeCycleTemplate(target);

		List<State> stateList = LifeCycleHelper.getService().findStates(lifeCycleTemplate);
		State lifecycleState = null;
		for (State state : stateList) {
			if ((state.getName()).equals(stateName)) {
				lifecycleState = state;
			}
		}
		if (null == lifecycleState) {
			throw new PlmException("没有找到名称为：" + stateName + "的生命周期状态,根据生命周期名称设置生命周期模板失败！");
		}

		this.updateLifeCycle(target, lifecycleState);
	}

	@Override
	public void updateLifeCycleByStateId(LifeCycleManaged target, String stateId) {
		// 获取生命周期模板
		LifeCycleTemplate lifeCycleTemplate = LifeCycleHelper.getService().getLifeCycleTemplate(target);

		List<State> stateList = LifeCycleHelper.getService().findStates(lifeCycleTemplate);
		State lifecycleState = null;
		for (State state : stateList) {
			if ((state.getId()).equals(stateId)) {
				lifecycleState = state;
			}
		}
		if (null == lifecycleState) {
			throw new PlmException("没有找到id为：" + stateId + "的生命周期模板,根据生命周期id设置生命周期模板失败！");
		}

		this.updateLifeCycle(target, lifecycleState);
	}
	
	@Override
	public void updateLifeCycleByStateIdNew(LifeCycleManaged target, String stateId) {
		// 获取生命周期模板
		LifeCycleTemplate lifeCycleTemplate = LifeCycleHelper.getService().getLifeCycleTemplate(target);
		List<State> stateList = LifeCycleHelper.getService().findStates(lifeCycleTemplate);
		State lifecycleState = null;
		for (State state : stateList) {
			if ((state.getId()).equals(stateId)) {
				lifecycleState = state;
			}
		}
		if (null == lifecycleState) {
			//throw new PlmException("没有找到id为：" + stateId + "的生命周期模板,根据生命周期id设置生命周期模板失败！");
		}else{
			this.updateLifeCycle(target, lifecycleState);
		}
	}
	
	@Override
	public void promoteLifeCycle(LifeCycleManaged target) {
		promoteLifeCycleActiveBase(target);
		LifeCycleHelper.getService().promote(target);
		//PersistHelper.getService().update(target);
	}

	@Override
	public void demoteLifeCycle(LifeCycleManaged target) {
		LifeCycleHelper.getService().demote(target);
		//PersistHelper.getService().update(target);
	}

	@Override
	public void rejectLifeCycle(LifeCycleManaged target) {

	}

	public void promoteLifeCycleActiveBase(LifeCycleManaged target) {
		if (target instanceof DistributeOrder) {
			DistributeObjectService objService = DistributeHelper.getDistributeObjectService();
			List<Persistable> resultList = objService.getActiveBaseDataSourceByDisOrdOid(Helper.getOid(target));
			if (!resultList.isEmpty()) {
				ActiveLifecycleService activeLifeService = AdmHelper.getActiveLifecycleService();
				// 获取当前生命周期状态名称
				String currentStateName = target.getLifeCycleInfo().getStateName();
				for (Persistable dataObj : resultList) {
					String dataNextStateName = getActiveBaseNextStateName(currentStateName, target, dataObj);
					if (dataNextStateName != null) {
						activeLifeService.updateLifeCycleByStateName((LifeCycleManaged) dataObj, dataNextStateName);
					}
				}
			}
		}
	}

	private String getActiveBaseNextStateName(String orderCurrentStateName, Persistable target, Persistable dataObj) {
		String nextStateName = null;
		String dataCurrentStateName = ((LifeCycleManaged) dataObj).getLifeCycleInfo().getStateName();
		if (ConstUtil.LC_SCHEDULING.getName().equals(orderCurrentStateName)) {
			if (AdmLifeCycleConstUtil.LC_CONTROLLING.getName().equals(dataCurrentStateName)) {
				nextStateName = AdmLifeCycleConstUtil.LC_PROVIDING.getName();
			} else if (AdmLifeCycleConstUtil.LC_PROVIDING.getName().equals(dataCurrentStateName)) {
				nextStateName = AdmLifeCycleConstUtil.LC_REPROVIDING.getName();
			} else if (AdmLifeCycleConstUtil.LC_PROVIDED.getName().equals(dataCurrentStateName)) {
				nextStateName = AdmLifeCycleConstUtil.LC_REPROVIDING.getName();
			}
		} else if (ConstUtil.LC_DISTRIBUTING.getName().equals(orderCurrentStateName)) {
			DistributeOrderService orderService = DistributeHelper.getDistributeOrderService();
			List<DistributeOrder> orderList = orderService.getDistributeOrderByOrdStateAndDataOid(
					Helper.getOid(target), Helper.getOid(dataObj));
			if (orderList != null && !orderList.isEmpty()) {
				return null;
			}
			nextStateName = AdmLifeCycleConstUtil.LC_PROVIDED.getName();
		}
		return nextStateName;
	}
}
