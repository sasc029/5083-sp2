package com.bjsasc.adm.common;

import java.util.HashMap;
import java.util.Map;

import com.bjsasc.platform.objectmodel.business.lifeCycle.State;

/**
 * �������ڳ����ࡣ
 * 
 * @author yanjia 2013-3-12
 */
public class AdmLifeCycleConstUtil {
	
	private AdmLifeCycleConstUtil() {
		
	}

	public static Map<String, String> lifeCycleMap = new HashMap<String, String>();
	/** �������ݹ���-��������-�½�*/
	public final static State LC_NEW = new AdmState("new", "�½�");
	/** �������ݹ���-��������-������*/
	public final static State LC_APPROVING = new AdmState("approving", "������");
	/** �������ݹ���-��������-������ֹ */
	public final static State LC_APPROVETERMINATE = new AdmState("approveterminate", "������ֹ");
	/** �������ݹ���-��������-������� */
	public final static State LC_APPROVEREJECT = new AdmState("approvereject", "�������˻�");	
	/** �������ݹ���-��������-�ܿ��� */
	public final static State LC_CONTROLLING = new AdmState("control", "�ܿ���");
	/** �������ݹ���-��������-������ */
	public final static State LC_PROVIDING = new AdmState("distributing", "�ַ���");
	/** �������ݹ���-��������-�ѷ��� */
	public final static State LC_PROVIDED = new AdmState("distributed", "�ѷַ�");
	/** �������ݹ���-��������-������ */
	public final static State LC_REPROVIDING = new AdmState("reproviding", "������");
	/** �������ݹ���-��������-��ɾ�� */
	public final static State LC_RECYCLE = new AdmState("recycled", "�ѻ���");
	static {
		lifeCycleMap.put(LC_NEW.getId(), LC_NEW.getName());
		lifeCycleMap.put(LC_APPROVING.getId(), LC_APPROVING.getName());
		lifeCycleMap.put(LC_APPROVETERMINATE.getId(), LC_APPROVETERMINATE.getName());
		lifeCycleMap.put(LC_APPROVEREJECT.getId(), LC_APPROVEREJECT.getName());
		lifeCycleMap.put(LC_CONTROLLING.getId(), LC_CONTROLLING.getName());
		lifeCycleMap.put(LC_PROVIDING.getId(), LC_PROVIDING.getName());
		lifeCycleMap.put(LC_PROVIDED.getId(), LC_PROVIDED.getName());
		lifeCycleMap.put(LC_REPROVIDING.getId(), LC_REPROVIDING.getName());
		lifeCycleMap.put(LC_RECYCLE.getId(), LC_RECYCLE.getName());
	}

	private static class AdmState extends State {
		/** serialVersionUID */
		private static final long serialVersionUID = -9079690574003672904L;

		public AdmState(String id, String name) {
			setId(id);
			setName(name);
		}
	}

}
