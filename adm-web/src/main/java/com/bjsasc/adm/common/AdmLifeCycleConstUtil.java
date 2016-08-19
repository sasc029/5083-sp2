package com.bjsasc.adm.common;

import java.util.HashMap;
import java.util.Map;

import com.bjsasc.platform.objectmodel.business.lifeCycle.State;

/**
 * 生命周期常量类。
 * 
 * @author yanjia 2013-3-12
 */
public class AdmLifeCycleConstUtil {
	
	private AdmLifeCycleConstUtil() {
		
	}

	public static Map<String, String> lifeCycleMap = new HashMap<String, String>();
	/** 现行数据管理-生命周期-新建*/
	public final static State LC_NEW = new AdmState("new", "新建");
	/** 现行数据管理-生命周期-审批中*/
	public final static State LC_APPROVING = new AdmState("approving", "审批中");
	/** 现行数据管理-生命周期-审批终止 */
	public final static State LC_APPROVETERMINATE = new AdmState("approveterminate", "审批终止");
	/** 现行数据管理-生命周期-审批打回 */
	public final static State LC_APPROVEREJECT = new AdmState("approvereject", "审批被退回");	
	/** 现行数据管理-生命周期-受控中 */
	public final static State LC_CONTROLLING = new AdmState("control", "受控中");
	/** 现行数据管理-生命周期-发放中 */
	public final static State LC_PROVIDING = new AdmState("distributing", "分发中");
	/** 现行数据管理-生命周期-已发放 */
	public final static State LC_PROVIDED = new AdmState("distributed", "已分发");
	/** 现行数据管理-生命周期-补发中 */
	public final static State LC_REPROVIDING = new AdmState("reproviding", "补发中");
	/** 现行数据管理-生命周期-已删除 */
	public final static State LC_RECYCLE = new AdmState("recycled", "已回收");
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
