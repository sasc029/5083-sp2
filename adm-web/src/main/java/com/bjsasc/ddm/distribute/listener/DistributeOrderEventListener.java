package com.bjsasc.ddm.distribute.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService;
import com.bjsasc.ddm.distribute.service.distributetask.DistributeTaskService;
import com.bjsasc.platform.lifecycle.util.LifeCycleManagedUtil;
import com.bjsasc.platform.workflow.api.WfInstanceService;
import com.bjsasc.platform.workflow.api.util.WorkflowProvider;
import com.bjsasc.platform.workflow.api.util.WorkflowResource;
import com.bjsasc.platform.workflow.engine.event.processevent.ProcInstanceCompleteEvent;
import com.bjsasc.platform.workflow.engine.model.instance.ProcessInstance;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.change.ECO;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.option.OptionHelper;
import com.bjsasc.plm.core.option.OptionValue;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.event.SetLifeCycleStateEvent;
import com.cascc.platform.event.AbstractListener;
import com.cascc.platform.event.Event;
import com.cascc.platform.event.Listener;
import com.cascc.platform.util.PlatformException;

/**
 * �������¼�����
 * 
 * @author guowei 2013-11-14
 */
public class DistributeOrderEventListener extends AbstractListener implements Listener {

	/** serialVersionUID */
	private static final long serialVersionUID = 3904416567606079311L;

	public void raiseEvent(Event event) {

		if (event instanceof SetLifeCycleStateEvent) {
			SetLifeCycleStateEvent aEvent = (SetLifeCycleStateEvent) event;
			// �ַ�����ԴOID
			String oid = aEvent.getEventGroupIdentity();
			// ��������������¼���
			if (aEvent.getWorkFlowEvent() instanceof ProcInstanceCompleteEvent) {
				Persistable target = Helper.getPersistService().getObject(oid);
				if(target instanceof DistributeOrder){
					DistributeOrder disOrder=(DistributeOrder)target;
					Context context = disOrder.getContextInfo().getContext();
					OptionValue disOrderValue = OptionHelper.getService().getOptionValue(context,"disMange_disLifeCycleLinkage_disLifeCycleLinkageOption");
					String optionValue = disOrderValue.getValue();
					
					String procInstID = aEvent.getProInstanceId();
					//��ȡ�ӿ�ʵ��
					WfInstanceService wfInstanceService = WorkflowProvider
							.getWfInstanceService();
					ProcessInstance procInst = wfInstanceService.getProcessInstance(
							null, procInstID);
					if(procInst == null){
						Map rangeMap = new HashMap();
						rangeMap.put(WorkflowResource.KEY_RANGE, WorkflowResource.OLD_RANGE);
						procInst = wfInstanceService.getProcessInstance(rangeMap,procInstID);
						if(procInst == null){
							throw new PlatformException("",
									"��[��ǰ]��[��ʷ]���л�ȡָ��������ʵ��ʧ��,procInstID = [" + procInstID + "]");
						}
					}
					// ȡ���������̵�������ID
					String starterId = procInst.getStarterID();
					if (ConstUtil.LC_SCHEDULING.getName().equals(disOrder.getStateName())){
						/** DistributeObjectService */
						DistributeObjectService service = DistributeHelper.getDistributeObjectService();
						List<DistributeObject> listDis = service.getDistributeObjectsByDistributeOrderOid(oid);
						boolean ecoFlag = false;
						for (DistributeObject disObj : listDis) {
							Persistable disObject = (Persistable) disObj.getDistributeData().getObject();
							if (disObject instanceof ECO) {
								ecoFlag = true;
								break;
							}
						}
						// �����ַ�ʱ(��������(0���ŵ���1�������ŵ�))
						if (ConstUtil.C_ORDERTYPE_1.equals(disOrder.getOrderType())) {
							// ȡ�ò����ַ�����
							//Context context = disOrder.getContextInfo().getContext();
							OptionValue value = OptionHelper.getService().getOptionValue(context,
									"disMange_disFlowConfig_againDispatch");

							if ("false".equals(value.getValue())) {
									// �����ַ�����"��"���ύ��ֱ�ӷַ�
									DistributeTaskService taskService = DistributeHelper.getDistributeTaskService();
									//trueΪ��������ʱ���ķ��ŵ���������
									taskService.createDistributeTask(oid, "true", starterId);
									return;
							}
							// �ַ������Ǹ��ĵ�
							if (ecoFlag) {
								// ȡ�ø��ķַ�����
								context = disOrder.getContextInfo().getContext();
								value = OptionHelper.getService().getOptionValue(context,
										"disMange_disFlowConfig_dispatch");

								if ("false".equals(value.getValue())) {
										// �����ַ�����"��"���ύ��ֱ�ӷַ�
										DistributeTaskService taskService = DistributeHelper.getDistributeTaskService();
										//trueΪ��������ʱ���ķ��ŵ���������
										taskService.createDistributeTask(oid, "true", starterId);
								}
							}
						} else {
							if(ConstUtil.C_ORDERTYPE_0.equals(disOrder.getOrderType())){
								if("true".equals(optionValue)){
									DistributeLifecycleService dls = DistributeHelper.getDistributeLifecycleService();
									DistributeObjectService objService = DistributeHelper.getDistributeObjectService();
									List<DistributeObject> resultList = objService.getDistributeObjectsByDistributeOrderOid(Helper.getOid(disOrder));
									for(DistributeObject disObj:resultList){
										// ��ȡ��������ģ��
										LifeCycleManaged lifecycleManaged =(LifeCycleManaged) LifeCycleManagedUtil.getLifeCycleManagedByClassId(disObj.getDataInnerId(),disObj.getDataClassId());
										dls.updateLifeCycleByStateIdNew(lifecycleManaged, ConstUtil.LC_SCHEDULING.getId());
									}
								}
							}
							// �ַ������Ǹ��ĵ�
							if (ecoFlag) {
								// ȡ�ø��ķַ�����
								//Context context = disOrder.getContextInfo().getContext();
								OptionValue value = OptionHelper.getService().getOptionValue(context,
										"disMange_disFlowConfig_dispatch");

								if ("false".equals(value.getValue())) {
										// ���ķַ�����"��"���ύ��ֱ�ӷַ�
										DistributeTaskService taskService = DistributeHelper.getDistributeTaskService();
										//trueΪ��������ʱ���ķ��ŵ���������
										taskService.createDistributeTask(oid, "true", starterId);
								}
							}
						}
					} else if (ConstUtil.LC_DISTRIBUTING.getName().equals(disOrder.getStateName())){
						//��������
						DistributeTaskService taskService = DistributeHelper.getDistributeTaskService();
						taskService.createDistributeTask(oid, null, starterId);
						if(ConstUtil.C_ORDERTYPE_0.equals(disOrder.getOrderType())){
							if("true".equals(optionValue)){
								DistributeObjectService objService = DistributeHelper.getDistributeObjectService();
								List<DistributeObject> resultList = objService.getDistributeObjectsByDistributeOrderOid(Helper.getOid(disOrder));
								DistributeLifecycleService dls = DistributeHelper.getDistributeLifecycleService();
								for(DistributeObject disObj:resultList){
									LifeCycleManaged lifecycleManaged =(LifeCycleManaged) LifeCycleManagedUtil.getLifeCycleManagedByClassId(disObj.getDataInnerId(),disObj.getDataClassId());
									dls.updateLifeCycleByStateIdNew(lifecycleManaged, ConstUtil.LC_DISTRIBUTING.getId());
								}
							}
						}
					}
				}
			}
		}
	}

}
