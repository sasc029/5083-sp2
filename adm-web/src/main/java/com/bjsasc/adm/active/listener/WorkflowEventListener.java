package com.bjsasc.adm.active.listener;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.model.ActiveOrdered;
import com.bjsasc.adm.active.model.ActiveSeted;
import com.bjsasc.adm.active.model.activedocument.ActiveDocument;
import com.bjsasc.adm.active.model.activeorder.ActiveOrder;
import com.bjsasc.adm.active.model.activeset.ActiveSet;
import com.bjsasc.adm.active.service.activedocumentservice.ActiveDocumentService;
import com.bjsasc.adm.active.service.activeorderservice.ActiveOrderService;
import com.bjsasc.adm.active.service.activesetservice.ActiveSetService;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.event.SetLifeCycleStateEvent;
import com.cascc.platform.event.AbstractListener;
import com.cascc.platform.event.Event;
import com.cascc.platform.event.Listener;

/**
 * 工作流事件监听
 * 
 * @author gengancong 2013-7-10
 */
public class WorkflowEventListener extends AbstractListener implements Listener {

	/** serialVersionUID */
	private static final long serialVersionUID = 7157375931569130776L;

	private static final Logger LOG = Logger.getLogger(WorkflowEventListener.class);

	public void raiseEvent(Event event) {
		if (event instanceof SetLifeCycleStateEvent) {
			SetLifeCycleStateEvent aEvent = (SetLifeCycleStateEvent) event;
			// 工作流状态
			// WorkFlowEvent even = aEvent.getWorkFlow();
			// 分发对象源OID
			String oid = aEvent.getEventGroupIdentity();

			// 监听工作流完成事件。
			//if (even == WorkFlowEvent.ActivityComplete) {
			Persistable obj = Helper.getPersistService().getObject(oid);
			if (obj instanceof LifeCycleManaged) {
				LifeCycleManaged life = (LifeCycleManaged) obj;
				List<LifeCycleManaged> updateList = new ArrayList<LifeCycleManaged>();
				List<LifeCycleManaged> allSubObject = getAllSubObject(life);
				String pStateName = life.getLifeCycleInfo().getStateName();
				for (LifeCycleManaged active : allSubObject) {
					String sStateName = active.getLifeCycleInfo().getStateName();

					LOG.debug("OID:[" + active.getClassId() + ":" + active.getInnerId() + "]," + "生命周期: [" + sStateName
							+ "]");

					if (!pStateName.equals(sStateName)) {
						active.setLifeCycleInfo(life.getLifeCycleInfo());
						updateList.add(active);
					}
				}
				LOG.debug("更新数据:" + updateList.size());
				if (!updateList.isEmpty()) {
					/** 现行文件服务 */
					ActiveDocumentService service = AdmHelper.getActiveDocumentService();
					service.updateActiveLife(updateList);
				}
			}
			//}
		}
	}

	/**
	 * 取得现行文件，现行套，现行单据及相关对象。
	 * 
	 * @param object LifeCycleManaged
	 * @return List<LifeCycleManaged>
	 */
	private List<LifeCycleManaged> getAllSubObject(LifeCycleManaged object) {
		List<LifeCycleManaged> list = new ArrayList<LifeCycleManaged>();
		if (object instanceof ActiveDocument) {
			list.add(object);
		} else if (object instanceof ActiveSet) {
			list.addAll(getActiveSeted((ActiveSet) object));
		} else if (object instanceof ActiveOrder) {
			list.addAll(getActiveOrdered((ActiveOrder) object));
		}
		return list;
	}

	/**
	 * 取得现行套及其对象。
	 * 
	 * @param aSet ActiveSet
	 * @return List<LifeCycleManaged>
	 */
	private List<LifeCycleManaged> getActiveSeted(ActiveSet aSet) {
		List<LifeCycleManaged> list = new ArrayList<LifeCycleManaged>();
		list.add(aSet);
		/** 现行套服务 */
		ActiveSetService service = AdmHelper.getActiveSetService();
		List<ActiveSeted> activeItems = service.getActiveItems(aSet);

		for (ActiveSeted aSeted : activeItems) {
			if (aSeted instanceof ActiveDocument) {
				list.add((ActiveDocument) aSeted);
			} else if (aSeted instanceof ActiveOrder) {
				list.addAll(getActiveOrdered((ActiveOrder) aSeted));
			}
		}
		return list;
	}

	/**
	 * 取得现行单据及其更新后对象。
	 * 
	 * @param aOrder ActiveOrder
	 * @return List<LifeCycleManaged>
	 */
	private List<LifeCycleManaged> getActiveOrdered(ActiveOrder aOrder) {
		List<LifeCycleManaged> list = new ArrayList<LifeCycleManaged>();
		list.add(aOrder);
		/** 现行单据服务 */
		ActiveOrderService service = AdmHelper.getActiveOrderService();
		List<ActiveOrdered> afterItems = service.getAfterItems(aOrder);

		for (ActiveOrdered aOrdered : afterItems) {
			if (aOrdered instanceof ActiveDocument) {
				list.add((ActiveDocument) aOrdered);
			} else if (aOrdered instanceof ActiveSet) {
				list.addAll(getActiveSeted((ActiveSet) aOrdered));
			}
		}
		return list;
	}
}
