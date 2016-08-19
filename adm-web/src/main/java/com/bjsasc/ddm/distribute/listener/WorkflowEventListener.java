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
 * �������¼�����
 * 
 * @author gengancong 2013-3-27
 */
public class WorkflowEventListener extends AbstractListener implements Listener {

	/** serialVersionUID */
	private static final long serialVersionUID = 7157375931569130776L;

	public void raiseEvent(Event event) {

		if (event instanceof SetLifeCycleStateEvent) {
			SetLifeCycleStateEvent aEvent = (SetLifeCycleStateEvent) event;
			// �ַ�����ԴOID
			String oid = aEvent.getEventGroupIdentity();
			// ��������������¼���
			if (aEvent.getWorkFlowEvent() instanceof ProcInstanceCompleteEvent) {
				Persistable target = Helper.getPersistService().getObject(oid);
				Context context = null;
				if(target instanceof Contexted){
					context = ((Contexted) target).getContextInfo().getContext();
				}
				OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_autoDisConfig_autoCreateDisOrder");
				String autoCreateDistributeOrderFlag = value.getValue();
				// ���ŵ��������
				DistributeOrderService service = DistributeHelper.getDistributeOrderService();
				//�ж���ҵ�������й�����ʱ�Ž����Զ��������ŵ��Ĳ���
				boolean isCanCreateDistributeOrder = false;
				isCanCreateDistributeOrder = service.isCanCreateDistributeOrder(target);
				// �Ƿ��Զ��������ŵ�flag(true���ǣ�false������)
				if ("true".equals(autoCreateDistributeOrderFlag) && isCanCreateDistributeOrder) {
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
					// �ַ��������
					DistributeObjectService disObjService = DistributeHelper.getDistributeObjectService();
					// �ַ�����Դ�����ѷ���List
					List<DistributeObject> disObjList = disObjService.getDistributeObjectsByDataOid(oid);
					// �ַ����ݶ���
					DistributeObject disObject;
					// ��������
					String orderType;
					// ���
					String number;
					// ����
					String name;
					// ��ע
					String note;
					// �ַ�����Դ�����ѷ���ListΪ�գ��򴴽��ַ�����
					if (disObjList == null || disObjList.isEmpty()) {
						// ����ת������
						DdmDataTransferService tranService = DdmDataTransferHelper.getDistributeObjectService();
						Persistable obj = Helper.getPersistService().getObject(oid);
						disObject = tranService.transferToDdmData(obj);
						// ��������(���ŵ�)
						orderType = ConstUtil.C_ORDERTYPE_0;
						number = disObject.getNumber() + ConstUtil.C_DISTRIBUTEORDER_0;
						name = disObject.getName() + ConstUtil.C_DISTRIBUTEORDER_0;
					} else {
						// �ַ�����Դ�����ѷ���List��Ϊ�գ���ȡ���ѷ��ŵķַ�����
						disObject = disObjList.get(0);
						// ��������(�������ŵ�)
						orderType = ConstUtil.C_ORDERTYPE_1;
						number = disObject.getNumber() + ConstUtil.C_DISTRIBUTEORDER_1;
						name = disObject.getName() + ConstUtil.C_DISTRIBUTEORDER_1;
						/** ���ŵ���ַ�����Link���� */
						DistributeOrderObjectLinkService linkService = DistributeHelper.getDistributeOrderObjectLinkService();
						int seriaNo = linkService.getDistributeOrderObjectLinkListByDistributeObjectOid(Helper.getOid(disObject))
								.size();
						number = number + "-" + String.valueOf(seriaNo);
						name = name + "-" + String.valueOf(seriaNo);
					}
					note = disObject.getNote();
					
					// ȡ���Ƿ��Զ������ַ�������
					OptionValue autoStartDisValue = OptionHelper.getService().getOptionValue(context, "disMange_autoDisConfig_autoStartDis");
					if ("true".equals(autoStartDisValue.getValue())) {
						//ȡ�õ�ǰ�Ự�Ƿ���ҪȨ����֤
						boolean ischeck=Helper.getSessionService().isCheckPermission();
						//�ر�Ȩ����֤���Զ������ַ���Ҫ�ر�Ȩ����֤��
						Helper.getSessionService().setCheckPermission(false);
						// �������ŵ�
						service.createDistributeOrderAndObject(number, name, orderType, note, oid, true, starterId);
						//�ָ�Ȩ����֤
						Helper.getSessionService().setCheckPermission(ischeck);
					} else {
						// �������ŵ�
						service.createDistributeOrderAndObject(number, name, orderType, note, oid, false, starterId);
					}
				}
			}

		}
	}

}
