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
 * ���Ź���ģ���Զ����������ڷ�����
 * 
 * @author gaolingjie, 2013-3-11
 */
public class DdmLifecycleServiceImpl extends DefaultLifecycleServiceImpl {
	@Override
	public void promoteLifeCycle(LifeCycleManaged target) {

		// ������ͳ��
		DistributeWorkloadService workload = DistributeHelper.getDistributeWorkloadService();
		DistributeWorkload newDistributeWorkload = workload.newDistributeWorkload(target);

		// ��ȡ��ǰ��������״̬����
		// modify by gengancong 2014/05/20
		// String currentStateName = target.getLifeCycleInfo().getStateName();
		String currentStateName = Helper.getLifeCycleService().getLifeCycleStateId(target);
		// ��һ�ڵ���������״̬����
		String nextStateName = null;

		DistributeDataLifecycleModel lifecycleModel = DdmLifecycleUtil.getLifecycleModel(target.getClass().getName());

		if (null != lifecycleModel && null != lifecycleModel.getDdmLifeCycleStateByName(currentStateName)) {
			nextStateName = lifecycleModel.getDdmLifeCycleStateByName(currentStateName).getNextStateName();
		}

		// ���ø��෽����������������״̬
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
		// ������ͳ��
		workload.createDistributeWorkload(newDistributeWorkload, target);
	}

	@Override
	public void demoteLifeCycle(LifeCycleManaged target) {
		// ������ͳ��
		DistributeWorkloadService workload = DistributeHelper.getDistributeWorkloadService();
		DistributeWorkload newDistributeWorkload = workload.newDistributeWorkload(target);

		// ��ȡ��ǰ��������״̬����
		// modify by gengancong 2014/05/20
		// String currentStateName = target.getLifeCycleInfo().getStateName();
		String currentStateName = Helper.getLifeCycleService().getLifeCycleStateId(target);

		// ��һ�ڵ���������״̬����
		String previousStateName = null;

		DistributeDataLifecycleModel lifecycleModel = DdmLifecycleUtil.getLifecycleModel(target.getClass().getName());

		if (null != lifecycleModel && null != lifecycleModel.getDdmLifeCycleStateByName(currentStateName)) {
			previousStateName = lifecycleModel.getDdmLifeCycleStateByName(currentStateName).getPreviousStateName();
		}

		// ���ø��෽����������������״̬
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
		// ������ͳ��
		workload.createDistributeWorkload(newDistributeWorkload, target);
	}

	
	
	public List<DistributeOrderObjectLink> getObjectLinkByDistributeOrderOid (String distributeOrderOid){
		String hql = "from DistributeOrderObjectLink t "
				+ "where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? ";
		//���ŵ�InnerId
		String distributeOrderInnerId = Helper.getInnerId(distributeOrderOid);
		//���ŵ�ClassId
		String distributeOrderClassId = Helper.getClassId(distributeOrderOid);
		//��ѯLink��Ϣ
		List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, distributeOrderInnerId,
				distributeOrderClassId);
		return links;
	}
	
	
	@Override
	public void rejectLifeCycle(LifeCycleManaged target) {
		// ������ͳ��
		DistributeWorkloadService workload = DistributeHelper.getDistributeWorkloadService();
		DistributeWorkload newDistributeWorkload = workload.newDistributeWorkload(target);

		// ��ȡ��ǰ��������״̬����
		// modify by gengancong 2014/05/20
		// String currentStateName = target.getLifeCycleInfo().getStateName();
		String currentStateName = Helper.getLifeCycleService().getLifeCycleStateId(target);

		// "�ܾ�"�ڵ���������״̬����
		String rejectiveStateName = null;

		DistributeDataLifecycleModel lifecycleModel = DdmLifecycleUtil.getLifecycleModel(target.getClass().getName());

		if (null != lifecycleModel && null != lifecycleModel.getDdmLifeCycleStateByName(currentStateName)) {
			rejectiveStateName = lifecycleModel.getDdmLifeCycleStateByName(currentStateName).getRejectiveStateName();
		}

		// ���ø��෽����������������״̬
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
		// ������ͳ��
		workload.createDistributeWorkload(newDistributeWorkload, target);
	}
}
