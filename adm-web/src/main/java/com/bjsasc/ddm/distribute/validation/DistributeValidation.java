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
 * 发放按钮验证。
 * 允许添加的文档的状态                       全部和生命周期状态列表    受控中     Document
 * 允许添加的CAD文档的状态              全部和生命周期状态列表    受控中     CADDocument
 * 允许添加的部件的状态	            全部和生命周期状态列表	受控中     Part
 * 允许添加的更改单的状态	            全部和生命周期状态列表	受控中     ECO
 * 允许添加的技术通知单的状态	全部和生命周期状态列表	受控中     TNO
 * 允许添加的超差代料质疑单的状态	全部和生命周期状态列表	受控中     Variance
 * 允许添加的送审单的状态	            全部和生命周期状态列表	受控中     ApproveOrder
 * 允许添加的现行文件的状态	            全部和生命周期状态列表	受控中     ActiveDocument
 * 允许添加的现行单据的状态	            全部和生命周期状态列表	受控中     ActiveOrder
 * 允许添加的现行套的状态	            全部和生命周期状态列表	受控中     ActiveSet
 * 允许添加的基线的状态	            全部和生命周期状态列表	受控中     BaseLine
 * 允许添加的套对象的状态	            全部和生命周期状态列表	受控中     ATSuit
 * 
 * @author gengancong 2013-3-27
 */
public class DistributeValidation implements ValidationFilter {
	public UIState doActionFilter(Action action, UIDataInfo uiData) {
		Persistable object = uiData.getMainObject();
		
		if(checkObject(object)){
			return UIState.ENABLED;//可用
		}
		
		return UIState.DISABLED;//默认不可用
	}	
	public static boolean checkObject(Persistable object){
		if(object instanceof Document){//文档
			Document obj = (Document) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowDocState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof CADDocument){//CAD文档
			CADDocument obj = (CADDocument) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowCADDocState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof Part){//部件
			Part obj = (Part) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowPartState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof ECO){//更改单
			ECO obj = (ECO) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowECOState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof TNO){//技术通知单
			TNO obj = (TNO) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowTNOState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof Variance){//超差代料质疑单
			Variance obj = (Variance) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowVarianceState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof ApproveOrder){//送审单
			ApproveOrder obj = (ApproveOrder) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowApproveOrderState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof ActiveDocument){//现行文件
			ActiveDocument obj = (ActiveDocument) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowActiveDocumentState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof ActiveOrder){//现行单据
			ActiveOrder obj = (ActiveOrder) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowActiveOrderState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof ActiveSet){//现行套
			ActiveSet obj = (ActiveSet) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowActiveSetState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof ManagedBaseline){//基线
			ManagedBaseline obj = (ManagedBaseline) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowBaseLineState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof ATSuit){//套对象
			ATSuit obj = (ATSuit) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowSuitState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object.getClass().getName().endsWith("ProcessFlow")){
			//临时使用字符串匹配，后续需要彻底解耦
			Contexted obj = (Contexted) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowProcessFlowState");
			
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}else if(object instanceof PPCO){//转阶段更改单
			PPCO obj = (PPCO) object;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowPpcoState");
			return checkLifeCycleState((LifeCycleManaged)object,value.getValue());
		}
		else if(object == null){
			return true;//对于"更多"菜单时传入的为null
		}
		
		return false;
	}
	private static boolean checkLifeCycleState(LifeCycleManaged lifeCycle,String state){
		String stateName = lifeCycle.getLifeCycleInfo().getStateName();

		if(state==null){
			return false;
		}
		
		if(state.contains(stateName) || "all".equals(state)){
			if (lifeCycle instanceof Workable) {//当对象可用时判断是否为工作副本
				Iterated workable = (Iterated) lifeCycle;
				String workState = workable.getIterationInfo().getCheckoutState();
				if (VersionControlUtil.CHECKOUTSTATE_WORK.equals(workState)) {
					//工作副本添加至发放
					return false;
				}
			}
			return true;//可用
		}else{
			return false;
		}
	}
}
