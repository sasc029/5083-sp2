package com.bjsasc.ddm.distribute.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService;
import com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService;
import com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService;
import com.bjsasc.ddm.transfer.helper.DdmDataTransferHelper;
import com.bjsasc.ddm.transfer.service.DdmDataTransferService;
import com.bjsasc.platform.workflow.api.WfInstanceService;
import com.bjsasc.platform.workflow.api.util.WorkflowProvider;
import com.bjsasc.platform.workflow.api.util.WorkflowResource;
import com.bjsasc.platform.workflow.engine.event.processevent.ProcInstanceCompleteEvent;
import com.bjsasc.platform.workflow.engine.model.instance.ProcessInstance;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.model.Contexted;
import com.bjsasc.plm.core.option.OptionHelper;
import com.bjsasc.plm.core.option.OptionValue;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.event.SetLifeCycleStateEvent;
import com.cascc.platform.event.AbstractListener;
import com.cascc.platform.event.Event;
import com.cascc.platform.event.Listener;
import com.cascc.platform.util.PlatformException;

/**
 * 工作流事件监听
 * 
 * @author gengancong 2013-3-27
 */
public class WorkflowEventListener extends AbstractListener implements Listener {

	/** serialVersionUID */
	private static final long serialVersionUID = 7157375931569130776L;

	public void raiseEvent(Event event) {

		if (event instanceof SetLifeCycleStateEvent) {
			SetLifeCycleStateEvent aEvent = (SetLifeCycleStateEvent) event;
			// 分发对象源OID
			String oid = aEvent.getEventGroupIdentity();
			// 监听工作流完成事件。
			if (aEvent.getWorkFlowEvent() instanceof ProcInstanceCompleteEvent) {
				Persistable target = Helper.getPersistService().getObject(oid);
				Context context = null;
				if(target instanceof Contexted){
					context = ((Contexted) target).getContextInfo().getContext();
				}
				OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_autoDisConfig_autoCreateDisOrder");
				String autoCreateDistributeOrderFlag = value.getValue();
				// 发放单对象服务
				DistributeOrderService service = DistributeHelper.getDistributeOrderService();
				//判断是业务对象进行工作流时才进行自动创建发放单的操作
				boolean isCanCreateDistributeOrder = false;
				isCanCreateDistributeOrder = service.isCanCreateDistributeOrder(target);
				// 是否自动创建发放单flag(true：是，false：不是)
				if ("true".equals(autoCreateDistributeOrderFlag) && isCanCreateDistributeOrder) {
					String procInstID = aEvent.getProInstanceId();
					//获取接口实例
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
									"在[当前]、[历史]表中获取指定的流程实例失败,procInstID = [" + procInstID + "]");
						}
					}
					// 取得审批流程的启动人ID
					String starterId = procInst.getStarterID();
					// 分发对象服务
					DistributeObjectService disObjService = DistributeHelper.getDistributeObjectService();
					// 分发数据源对象已发放List
					List<DistributeObject> disObjList = disObjService.getDistributeObjectsByDataOid(oid);
					// 分发数据对象
					DistributeObject disObject;
					// 单据类型
					String orderType;
					// 编号
					String number;
					// 名称
					String name;
					// 备注
					String note;
					// 分发数据源对象已发放List为空，则创建分发数据
					if (disObjList == null || disObjList.isEmpty()) {
						// 数据转换服务
						DdmDataTransferService tranService = DdmDataTransferHelper.getDistributeObjectService();
						Persistable obj = Helper.getPersistService().getObject(oid);
						disObject = tranService.transferToDdmData(obj);
						// 单据类型(发放单)
						orderType = ConstUtil.C_ORDERTYPE_0;
						number = disObject.getNumber() + ConstUtil.C_DISTRIBUTEORDER_0;
						name = disObject.getName() + ConstUtil.C_DISTRIBUTEORDER_0;
					} else {
						// 分发数据源对象已发放List不为空，则取得已发放的分发数据
						disObject = disObjList.get(0);
						// 单据类型(补发发放单)
						orderType = ConstUtil.C_ORDERTYPE_1;
						number = disObject.getNumber() + ConstUtil.C_DISTRIBUTEORDER_1;
						name = disObject.getName() + ConstUtil.C_DISTRIBUTEORDER_1;
						/** 发放单与分发数据Link服务 */
						DistributeOrderObjectLinkService linkService = DistributeHelper.getDistributeOrderObjectLinkService();
						int seriaNo = linkService.getDistributeOrderObjectLinkListByDistributeObjectOid(Helper.getOid(disObject))
								.size();
						number = number + "-" + String.valueOf(seriaNo);
						name = name + "-" + String.valueOf(seriaNo);
					}
					note = disObject.getNote();
					
					// 取得是否自动启动分发的配置
					OptionValue autoStartDisValue = OptionHelper.getService().getOptionValue(context, "disMange_autoDisConfig_autoStartDis");
					if ("true".equals(autoStartDisValue.getValue())) {
						//取得当前会话是否需要权限验证
						boolean ischeck=Helper.getSessionService().isCheckPermission();
						//关闭权限验证（自动启动分发需要关闭权限验证）
						Helper.getSessionService().setCheckPermission(false);
						// 创建发放单
						service.createDistributeOrderAndObject(number, name, orderType, note, oid, true, starterId);
						//恢复权限验证
						Helper.getSessionService().setCheckPermission(ischeck);
					} else {
						// 创建发放单
						service.createDistributeOrderAndObject(number, name, orderType, note, oid, false, starterId);
					}
				}
			}

		}
	}

}
