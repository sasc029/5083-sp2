package com.bjsasc.ddm.distribute.validation;

import com.bjsasc.adm.active.model.activedocument.ActiveDocument;
import com.bjsasc.adm.active.model.activeorder.ActiveOrder;
import com.bjsasc.adm.active.model.activeset.ActiveSet;
import com.bjsasc.platform.objectmodel.business.version.VersionControlUtil;
import com.bjsasc.plm.core.approve.ApproveOrder;
import com.bjsasc.plm.core.baseline.model.ManagedBaseline;
import com.bjsasc.plm.core.cad.CADDocument;
import com.bjsasc.plm.core.change.ECO;
import com.bjsasc.plm.core.change.PPCO;
import com.bjsasc.plm.core.change.TNO;
import com.bjsasc.plm.core.change.Variance;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.doc.Document;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.option.OptionHelper;
import com.bjsasc.plm.core.option.OptionValue;
import com.bjsasc.plm.core.part.Part;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.suit.ATSuit;
import com.bjsasc.plm.core.vc.model.Iterated;
import com.bjsasc.plm.core.vc.model.Workable;
import com.bjsasc.plm.operate.Action;
import com.bjsasc.plm.ui.UIDataInfo;
import com.bjsasc.plm.ui.validation.UIState;
import com.bjsasc.plm.ui.validation.ValidationFilter;
import com.bjsasc.plm.core.context.model.Contexted;

/**
 * ���Ű�ť��֤��
 * ������ӵ��ĵ���״̬                       ȫ������������״̬�б�    �ܿ���     Document
 * ������ӵ�CAD�ĵ���״̬              ȫ������������״̬�б�    �ܿ���     CADDocument
 * ������ӵĲ�����״̬	            ȫ������������״̬�б�	�ܿ���     Part
 * ������ӵĸ��ĵ���״̬	            ȫ������������״̬�б�	�ܿ���     ECO
 * ������ӵļ���֪ͨ����״̬	ȫ������������״̬�б�	�ܿ���     TNO
 * ������ӵĳ���������ɵ���״̬	ȫ������������״̬�б�	�ܿ���     Variance
 * ������ӵ����󵥵�״̬	            ȫ������������״̬�б�	�ܿ���     ApproveOrder
 * ������ӵ������ļ���״̬	            ȫ������������״̬�б�	�ܿ���     ActiveDocument
 * ������ӵ����е��ݵ�״̬	            ȫ������������״̬�б�	�ܿ���     ActiveOrder
 * ������ӵ������׵�״̬	            ȫ������������״̬�б�	�ܿ���     ActiveSet
 * ������ӵĻ��ߵ�״̬	            ȫ������������״̬�б�	�ܿ���     BaseLine
 * ������ӵ��׶����״̬	            ȫ������������״̬�б�	�ܿ���     ATSuit
 * 
 * @author gengancong 2013-3-27
 */
public class DistributeValidation implements ValidationFilter {
	public UIState doActionFilter(Action action, UIDataInfo uiData) {
		Persistable object = uiData.getMainObject();
		
		if(checkObject(object)){
			return UIState.ENABLED;//����
		}
		
		return UIState.DISABLED;//Ĭ�ϲ�����
	}	
	public static boolean checkObject(Persistable object){
		if(object instanceof Document){//�ĵ�
			Document obj = (Document) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowDocState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof CADDocument){//CAD�ĵ�
			CADDocument obj = (CADDocument) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowCADDocState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof Part){//����
			Part obj = (Part) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowPartState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof ECO){//���ĵ�
			ECO obj = (ECO) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowECOState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof TNO){//����֪ͨ��
			TNO obj = (TNO) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowTNOState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof Variance){//����������ɵ�
			Variance obj = (Variance) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowVarianceState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof ApproveOrder){//����
			ApproveOrder obj = (ApproveOrder) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowApproveOrderState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof ActiveDocument){//�����ļ�
			ActiveDocument obj = (ActiveDocument) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowActiveDocumentState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof ActiveOrder){//���е���
			ActiveOrder obj = (ActiveOrder) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowActiveOrderState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof ActiveSet){//������
			ActiveSet obj = (ActiveSet) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowActiveSetState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof ManagedBaseline){//����
			ManagedBaseline obj = (ManagedBaseline) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowBaseLineState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof ATSuit){//�׶���
			ATSuit obj = (ATSuit) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowSuitState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object.getClass().getName().endsWith("ProcessFlow")){
			//��ʱʹ���ַ���ƥ�䣬������Ҫ���׽���
			Contexted obj = (Contexted) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowProcessFlowState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof PPCO){//ת�׶θ��ĵ�
			PPCO obj = (PPCO) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowPpcoState");
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}
		else if(object == null){
			return true;//����"����"�˵�ʱ�����Ϊnull
		}
		
		return false;
	}
	private static boolean checkLifeCycleState(LifeCycleManaged lifeCycle,String state){
		String stateName = lifeCycle.getLifeCycleInfo().getStateName();

		if(state==null){
			return false;
		}
		
		if(state.contains(stateName) || "all".equals(state)){
			if (lifeCycle instanceof Workable) {//���������ʱ�ж��Ƿ�Ϊ��������
				Iterated workable = (Iterated) lifeCycle;
				String workState = workable.getIterationInfo().getCheckoutState();
				if (VersionControlUtil.CHECKOUTSTATE_WORK.equals(workState)) {
					//�����������������
					return false;
				}
			}
			return true;//����
		}else{
			return false;
		}
	}
}
