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
 * �������¼�����
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
			// ������״̬
			// WorkFlowEvent even = aEvent.getWorkFlow();
			// �ַ�����ԴOID
			String oid = aEvent.getEventGroupIdentity();

			// ��������������¼���
			//if (even == WorkFlowEvent.ActivityComplete) {
			Persistable obj = Helper.getPersistService().getObject(oid);
			if (obj instanceof LifeCycleManaged) {
				LifeCycleManaged life = (LifeCycleManaged) obj;
				List<LifeCycleManaged> updateList = new ArrayList<LifeCycleManaged>();
				List<LifeCycleManaged> allSubObject = getAllSubObject(life);
				String pStateName = life.getLifeCycleInfo().getStateName();
				for (LifeCycleManaged active : allSubObject) {
					String sStateName = active.getLifeCycleInfo().getStateName();

					LOG.debug("OID:[" + active.getClassId() + ":" + active.getInnerId() + "]," + "��������: [" + sStateName
							+ "]");

					if (!pStateName.equals(sStateName)) {
						active.setLifeCycleInfo(life.getLifeCycleInfo());
						updateList.add(active);
					}
				}
				LOG.debug("��������:" + updateList.size());
				if (!updateList.isEmpty()) {
					/** �����ļ����� */
					ActiveDocumentService service = AdmHelper.getActiveDocumentService();
					service.updateActiveLife(updateList);
				}
			}
			//}
		}
	}

	/**
	 * ȡ�������ļ��������ף����е��ݼ���ض���
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
	 * ȡ�������׼������
	 * 
	 * @param aSet ActiveSet
	 * @return List<LifeCycleManaged>
	 */
	private List<LifeCycleManaged> getActiveSeted(ActiveSet aSet) {
		List<LifeCycleManaged> list = new ArrayList<LifeCycleManaged>();
		list.add(aSet);
		/** �����׷��� */
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
	 * ȡ�����е��ݼ�����º����
	 * 
	 * @param aOrder ActiveOrder
	 * @return List<LifeCycleManaged>
	 */
	private List<LifeCycleManaged> getActiveOrdered(ActiveOrder aOrder) {
		List<LifeCycleManaged> list = new ArrayList<LifeCycleManaged>();
		list.add(aOrder);
		/** ���е��ݷ��� */
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
