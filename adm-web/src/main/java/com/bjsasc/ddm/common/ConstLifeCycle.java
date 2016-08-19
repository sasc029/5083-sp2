package com.bjsasc.ddm.common;

import java.util.HashMap;
import java.util.Map;

import com.bjsasc.platform.objectmodel.business.lifeCycle.State;
import com.bjsasc.plm.core.Helper;

/**
 * 生命周期常量类。
 * 
 * @author gengancong 2013-3-12
 */
public abstract class ConstLifeCycle {

	public static Map<String, String> lifeCycleMap = new HashMap<String, String>();

	/** 发放管理-生命周期-新建 (对象:发放单)*/
	public static final State LC_NEW = new DdmState("new");
	/** 发放管理-生命周期-调度中  (对象:发放单)*/
	public static final State LC_SCHEDULING = new DdmState("scheduling");
	/** 发放管理-生命周期-未分发  (对象:发放单,分发数据,分发信息)*/
	public static final State LC_NOT_DISTRIBUT = new DdmState("not_distribute");
	/** 发放管理-生命周期-分发中  (对象:发放单,分发数据)*/
	public static final State LC_DISTRIBUTING = new DdmState("distributing");
	/** 发放管理-生命周期-已分发  (对象:发放单,分发信息)*/
	public static final State LC_DISTRIBUTED = new DdmState("distributed");
	/** 发放管理-生命周期-已完成  (对象:发放单 ,分发数据)*/
	public static final State LC_COMPLETED = new DdmState("completed");
	/** 发放管理-生命周期-已退回  (对象:发放单)*/
	public static final State LC_BACKOFF = new DdmState("backoff");
	/** 发放管理-生命周期-加工中  (对象:纸质任务)*/
	public static final State LC_PROCESSING = new DdmState("processing");
	/** 发放管理-生命周期-复制未签收 (对象:纸质任务)*/
	public static final State LC_DUPLICATE_PROCESS_NOT_RECEIVED = new DdmState("duplicate_process_not_received");
	/** 发放管理-生命周期-复制中 (对象:纸质任务)*/
	public static final State LC_DUPLICATE_PROCESS_RECEIVED = new DdmState("duplicate_process_received");
	/** 发放管理-生命周期-加工被退回 (对象:纸质任务)*/
	public static final State LC_PROCESSING_BACKOFF = new DdmState("processing_backoff");
	/** 发放管理-生命周期-复制完成 (对象:纸质任务)*/
	public static final State LC_DUPLICATED = new DdmState("duplicated");
	/** 发放管理-生命周期-销毁完成 (对象:纸质任务,分发信息)*/
	public static final State LC_DESTROYED = new DdmState("destroyed");
	/** 发放管理-生命周期-复制被退回 (对象:纸质任务)*/
	public static final State LC_DUPLICATE_BACKOFF = new DdmState("duplicate_backoff");
	/** 发放管理-生命周期-未签收  (对象:电子任务)*/
	public static final State LC_NOT_SIGNED = new DdmState("not_sign");
	/** 发放管理-生命周期-已签收  (对象:电子任务)*/
	public static final State LC_SIGNED = new DdmState("signed");
	/** 发放管理-生命周期-已拒绝  (对象:电子任务)*/
	public static final State LC_REFUSE_SIGNED = new DdmState("refuse_signed");
	/** 发放管理-生命周期-审批中*/
	public final static State LC_APPROVING = new DdmState("approving");
	/** 发放管理-生命周期-审批终止  (对象:发放单)*/
	public static final State LC_APPROVE_TERMINATE = new DdmState("approveterminate");
	/** 发放管理-生命周期-审批被退回  (对象:发放单)*/
	public static final State LC_APPROVE_REJECT = new DdmState("approvereject");
	/** PLM-生命周期-受控中 */
	public static final State LC_CONTROL = new DdmState("control");
	
	/** 发放管理-生命周期-分发审批终止  (对象:发放单)*/
	public static final State LC_DDM_APPROVE_TERMINATE = new DdmState("ddmApproveterminate");
	/** 发放管理-生命周期-分发审批被退回  (对象:发放单)*/
	public static final State LC_DDM_APPROVE_REJECT = new DdmState("ddmApprovereject");

	static {
		lifeCycleMap.put(LC_NEW.getId(), LC_NEW.getName());
		lifeCycleMap.put(LC_BACKOFF.getId(), LC_BACKOFF.getName());
		lifeCycleMap.put(LC_COMPLETED.getId(), LC_COMPLETED.getName());
		lifeCycleMap.put(LC_DESTROYED.getId(), LC_DESTROYED.getName());
		lifeCycleMap.put(LC_DISTRIBUTING.getId(), LC_DISTRIBUTING.getName());
		lifeCycleMap.put(LC_DUPLICATE_BACKOFF.getId(), LC_DUPLICATE_BACKOFF.getName());
		lifeCycleMap.put(LC_DUPLICATE_PROCESS_NOT_RECEIVED.getId(), LC_DUPLICATE_PROCESS_NOT_RECEIVED.getName());
		lifeCycleMap.put(LC_DUPLICATE_PROCESS_RECEIVED.getId(), LC_DUPLICATE_PROCESS_RECEIVED.getName());
		lifeCycleMap.put(LC_DUPLICATED.getId(), LC_DUPLICATED.getName());
		lifeCycleMap.put(LC_NOT_SIGNED.getId(), LC_NOT_SIGNED.getName());
		lifeCycleMap.put(LC_PROCESSING.getId(), LC_PROCESSING.getName());
		lifeCycleMap.put(LC_PROCESSING_BACKOFF.getId(), LC_PROCESSING_BACKOFF.getName());
		lifeCycleMap.put(LC_REFUSE_SIGNED.getId(), LC_REFUSE_SIGNED.getName());
		lifeCycleMap.put(LC_SCHEDULING.getId(), LC_SCHEDULING.getName());
		lifeCycleMap.put(LC_SIGNED.getId(), LC_SIGNED.getName());
		lifeCycleMap.put(LC_NOT_DISTRIBUT.getId(), LC_NOT_DISTRIBUT.getName());
		lifeCycleMap.put(LC_DISTRIBUTED.getId(), LC_DISTRIBUTED.getName());
		lifeCycleMap.put(LC_APPROVING.getId(), LC_APPROVING.getName());
		lifeCycleMap.put(LC_APPROVE_TERMINATE.getId(), LC_APPROVE_TERMINATE.getName());
		lifeCycleMap.put(LC_CONTROL.getId(), LC_CONTROL.getName());
		lifeCycleMap.put(LC_APPROVE_REJECT.getId(), LC_APPROVE_REJECT.getName());
		lifeCycleMap.put(LC_DDM_APPROVE_TERMINATE.getId(), LC_DDM_APPROVE_TERMINATE.getName());
		lifeCycleMap.put(LC_DDM_APPROVE_REJECT.getId(), LC_DDM_APPROVE_REJECT.getName());
	}

	private static class DdmState extends State {

		/** serialVersionUID */
		private static final long serialVersionUID = -652100848043111073L;

		public DdmState(String id) {
			setId(id);
			Map<String, String> lcMap = LifeCycleMappingDef.getLifeCycleMappingDef();
			String stateId = lcMap.get(id);
			State state = Helper.getLifeCycleService().getState(stateId);
			setName(state.getName());
		}
	}
}
