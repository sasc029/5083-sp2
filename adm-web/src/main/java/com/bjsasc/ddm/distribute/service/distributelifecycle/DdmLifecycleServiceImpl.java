package com.bjsasc.ddm.distribute.service.distributelifecycle;

import java.util.List;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.common.DdmLifecycleUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributelifecycle.DistributeDataLifecycleModel;
import com.bjsasc.ddm.distribute.model.distributeworkload.DistributeWorkload;
import com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService;
import com.bjsasc.ddm.distribute.service.distributeworkload.DistributeWorkloadService;
import com.bjsasc.platform.lifecycle.util.LifeCycleManagedUtil;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.option.OptionHelper;
import com.bjsasc.plm.core.option.OptionValue;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;

/**
 * 发放管理模块自定义生命周期服务类
 * 
 * @author gaolingjie, 2013-3-11
 */
public class DdmLifecycleServiceImpl extends DefaultLifecycleServiceImpl {
	@Override
	public void promoteLifeCycle(LifeCycleManaged target) {

		// 工作量统计
		DistributeWorkloadService workload = DistributeHelper.getDistributeWorkloadService();
		DistributeWorkload newDistributeWorkload = workload.newDistributeWorkload(target);

		// 获取当前生命周期状态名称
		// modify by gengancong 2014/05/20
		// String currentStateName = target.getLifeCycleInfo().getStateName();
		String currentStateName = Helper.getLifeCycleService().getLifeCycleStateId(target);
		// 下一节点生命周期状态名称
		String nextStateName = null;

		DistributeDataLifecycleModel lifecycleModel = DdmLifecycleUtil.getLifecycleModel(target.getClass().getName());

		if (null != lifecycleModel && null != lifecycleModel.getDdmLifeCycleStateByName(currentStateName)) {
			nextStateName = lifecycleModel.getDdmLifeCycleStateByName(currentStateName).getNextStateName();
		}

		// 调用父类方法，更新生命周期状态
		if (!StringUtil.isNull(nextStateName)) {
			super.promoteLifeCycleActiveBase(target);
			// modify by gengancong 2014/05/20
			// super.updateLifeCycleByStateName(target, nextStateName);
			super.updateLifeCycleByStateId(target, nextStateName);
			if (target instanceof Manageable) {
				User currentUser = SessionHelper.getService().getUser();
				Manageable manageable = (Manageable) target;
				manageable.getManageInfo().setModifyBy(currentUser);
				manageable.getManageInfo().setModifyTime(System.currentTimeMillis());
			}
			DistributeObjectService objService = DistributeHelper.getDistributeObjectService();
			if(target instanceof DistributeOrder ){
				List<DistributeObject> resultList = objService.getDistributeObjectsByDistributeOrderOid(Helper.getOid(target));
				DistributeOrder order=(DistributeOrder)target;
				Context context = Helper.getContextService().getRootContext();
				if(ConstUtil.C_ORDERTYPE_0.equals(order.getOrderType())){
					OptionValue value = OptionHelper.getService().getOptionValue(context,"disMange_disLifeCycleLinkage_disLifeCycleLinkageOption");
					String optionValue = value.getValue();
					for(DistributeObject disObj:resultList){
						if("true".equals(optionValue)){
							LifeCycleManaged lifecycleManaged =(LifeCycleManaged) LifeCycleManagedUtil.getLifeCycleManagedByClassId(disObj.getDataInnerId(),disObj.getDataClassId());
							//super.updateLifeCycleByStateId(lifecycleManaged, nextStateName);
							super.updateLifeCycleByStateIdNew(lifecycleManaged, nextStateName);
							
						}
					}
				}
			}
		}
		// 工作量统计
		workload.createDistributeWorkload(newDistributeWorkload, target);
	}

	@Override
	public void demoteLifeCycle(LifeCycleManaged target) {
		// 工作量统计
		DistributeWorkloadService workload = DistributeHelper.getDistributeWorkloadService();
		DistributeWorkload newDistributeWorkload = workload.newDistributeWorkload(target);

		// 获取当前生命周期状态名称
		// modify by gengancong 2014/05/20
		// String currentStateName = target.getLifeCycleInfo().getStateName();
		String currentStateName = Helper.getLifeCycleService().getLifeCycleStateId(target);

		// 上一节点生命周期状态名称
		String previousStateName = null;

		DistributeDataLifecycleModel lifecycleModel = DdmLifecycleUtil.getLifecycleModel(target.getClass().getName());

		if (null != lifecycleModel && null != lifecycleModel.getDdmLifeCycleStateByName(currentStateName)) {
			previousStateName = lifecycleModel.getDdmLifeCycleStateByName(currentStateName).getPreviousStateName();
		}

		// 调用父类方法，更新生命周期状态
		if (!StringUtil.isNull(previousStateName)) {
			// modify by gengancong 2014/05/20
			//super.updateLifeCycleByStateName(target, previousStateName);
			super.updateLifeCycleByStateId(target, previousStateName);
			if (target instanceof Manageable) {
				User currentUser = SessionHelper.getService().getUser();
				Manageable manageable = (Manageable) target;
				manageable.getManageInfo().setModifyBy(currentUser);
				manageable.getManageInfo().setModifyTime(System.currentTimeMillis());
			}
			
		}
		// 工作量统计
		workload.createDistributeWorkload(newDistributeWorkload, target);
	}

	
	
	public List<DistributeOrderObjectLink> getObjectLinkByDistributeOrderOid (String distributeOrderOid){
		String hql = "from DistributeOrderObjectLink t "
				+ "where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? ";
		//发放单InnerId
		String distributeOrderInnerId = Helper.getInnerId(distributeOrderOid);
		//发放单ClassId
		String distributeOrderClassId = Helper.getClassId(distributeOrderOid);
		//查询Link信息
		List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, distributeOrderInnerId,
				distributeOrderClassId);
		return links;
	}
	
	
	@Override
	public void rejectLifeCycle(LifeCycleManaged target) {
		// 工作量统计
		DistributeWorkloadService workload = DistributeHelper.getDistributeWorkloadService();
		DistributeWorkload newDistributeWorkload = workload.newDistributeWorkload(target);

		// 获取当前生命周期状态名称
		// modify by gengancong 2014/05/20
		// String currentStateName = target.getLifeCycleInfo().getStateName();
		String currentStateName = Helper.getLifeCycleService().getLifeCycleStateId(target);

		// "拒绝"节点生命周期状态名称
		String rejectiveStateName = null;

		DistributeDataLifecycleModel lifecycleModel = DdmLifecycleUtil.getLifecycleModel(target.getClass().getName());

		if (null != lifecycleModel && null != lifecycleModel.getDdmLifeCycleStateByName(currentStateName)) {
			rejectiveStateName = lifecycleModel.getDdmLifeCycleStateByName(currentStateName).getRejectiveStateName();
		}

		// 调用父类方法，更新生命周期状态
		if (!StringUtil.isNull(rejectiveStateName)) {
			// modify by gengancong 2014/05/20
			// super.updateLifeCycleByStateName(target, rejectiveStateName);
			super.updateLifeCycleByStateId(target, rejectiveStateName);
			if (target instanceof Manageable) {
				User currentUser = SessionHelper.getService().getUser();
				Manageable manageable = (Manageable) target;
				manageable.getManageInfo().setModifyBy(currentUser);
				manageable.getManageInfo().setModifyTime(System.currentTimeMillis());
			}
		}
		// 工作量统计
		workload.createDistributeWorkload(newDistributeWorkload, target);
	}
}
