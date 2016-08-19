package com.bjsasc.ddm.common;

import java.util.HashMap;
import java.util.Map;

import com.bjsasc.platform.objectmodel.business.lifeCycle.State;
import com.bjsasc.plm.core.Helper;

/**
 * �������ڳ����ࡣ
 * 
 * @author gengancong 2013-3-12
 */
public abstract class ConstLifeCycle {

	public static Map<String, String> lifeCycleMap = new HashMap<String, String>();

	/** ���Ź���-��������-�½� (����:���ŵ�)*/
	public static final State LC_NEW = new DdmState("new");
	/** ���Ź���-��������-������  (����:���ŵ�)*/
	public static final State LC_SCHEDULING = new DdmState("scheduling");
	/** ���Ź���-��������-δ�ַ�  (����:���ŵ�,�ַ�����,�ַ���Ϣ)*/
	public static final State LC_NOT_DISTRIBUT = new DdmState("not_distribute");
	/** ���Ź���-��������-�ַ���  (����:���ŵ�,�ַ�����)*/
	public static final State LC_DISTRIBUTING = new DdmState("distributing");
	/** ���Ź���-��������-�ѷַ�  (����:���ŵ�,�ַ���Ϣ)*/
	public static final State LC_DISTRIBUTED = new DdmState("distributed");
	/** ���Ź���-��������-�����  (����:���ŵ� ,�ַ�����)*/
	public static final State LC_COMPLETED = new DdmState("completed");
	/** ���Ź���-��������-���˻�  (����:���ŵ�)*/
	public static final State LC_BACKOFF = new DdmState("backoff");
	/** ���Ź���-��������-�ӹ���  (����:ֽ������)*/
	public static final State LC_PROCESSING = new DdmState("processing");
	/** ���Ź���-��������-����δǩ�� (����:ֽ������)*/
	public static final State LC_DUPLICATE_PROCESS_NOT_RECEIVED = new DdmState("duplicate_process_not_received");
	/** ���Ź���-��������-������ (����:ֽ������)*/
	public static final State LC_DUPLICATE_PROCESS_RECEIVED = new DdmState("duplicate_process_received");
	/** ���Ź���-��������-�ӹ����˻� (����:ֽ������)*/
	public static final State LC_PROCESSING_BACKOFF = new DdmState("processing_backoff");
	/** ���Ź���-��������-������� (����:ֽ������)*/
	public static final State LC_DUPLICATED = new DdmState("duplicated");
	/** ���Ź���-��������-������� (����:ֽ������,�ַ���Ϣ)*/
	public static final State LC_DESTROYED = new DdmState("destroyed");
	/** ���Ź���-��������-���Ʊ��˻� (����:ֽ������)*/
	public static final State LC_DUPLICATE_BACKOFF = new DdmState("duplicate_backoff");
	/** ���Ź���-��������-δǩ��  (����:��������)*/
	public static final State LC_NOT_SIGNED = new DdmState("not_sign");
	/** ���Ź���-��������-��ǩ��  (����:��������)*/
	public static final State LC_SIGNED = new DdmState("signed");
	/** ���Ź���-��������-�Ѿܾ�  (����:��������)*/
	public static final State LC_REFUSE_SIGNED = new DdmState("refuse_signed");
	/** ���Ź���-��������-������*/
	public final static State LC_APPROVING = new DdmState("approving");
	/** ���Ź���-��������-������ֹ  (����:���ŵ�)*/
	public static final State LC_APPROVE_TERMINATE = new DdmState("approveterminate");
	/** ���Ź���-��������-�������˻�  (����:���ŵ�)*/
	public static final State LC_APPROVE_REJECT = new DdmState("approvereject");
	/** PLM-��������-�ܿ��� */
	public static final State LC_CONTROL = new DdmState("control");
	
	/** ���Ź���-��������-�ַ�������ֹ  (����:���ŵ�)*/
	public static final State LC_DDM_APPROVE_TERMINATE = new DdmState("ddmApproveterminate");
	/** ���Ź���-��������-�ַ��������˻�  (����:���ŵ�)*/
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
