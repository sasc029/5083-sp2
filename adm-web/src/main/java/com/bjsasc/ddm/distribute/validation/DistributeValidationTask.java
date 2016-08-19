package com.bjsasc.ddm.distribute.validation;

import java.util.ArrayList;
import java.util.List;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.service.distributeelectask.DistributeElecTaskService;
import com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService;
import com.bjsasc.plm.core.foundation.Helper;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.operate.Action;
import com.bjsasc.plm.ui.UIDataInfo;
import com.bjsasc.plm.ui.validation.UIState;
import com.bjsasc.plm.ui.validation.ValidationFilter;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.ddm.distribute.model.distributeelectask.DistributeElecTask;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;

public class DistributeValidationTask implements ValidationFilter {
	private final DistributeElecTaskService disElecTaskService = DistributeHelper.getDistributeElecTaskService();
	private final DistributeInfoService service = DistributeHelper.getDistributeInfoService();
	public UIState doActionFilter(Action action, UIDataInfo uiData) {
		List<DistributeInfo> listDis = new ArrayList<DistributeInfo>();
		Persistable object = uiData.getMainObject();
		String taskOid = object.getClassId() + ":" + object.getInnerId();	
		DistributeElecTask disElecTask = null;
		if("DistributeElecTask".equals(object.getClassId())){
			listDis = service.getDistributeInfosByTaskOid(taskOid, null, null);
			disElecTask = (DistributeElecTask)object;
		}else if("DistributePaperSignTask".equals(object.getClassId())){
			listDis = service.getDistributeInfosByPaperSignTaskOid(taskOid, null, null);	
		}
		String aaUserIID = SessionHelper.getService().getUser().getAaUserInnerId();
		User user = Helper.getUserService().findUser(aaUserIID);
		String plmUserInnerID = user.getInnerId();
		if(aaUserIID != null && listDis.size() > 0){
			if(object != null && object instanceof LifeCycleManaged) {
				LifeCycleManaged workable = (LifeCycleManaged) object;
				String state = workable.getLifeCycleInfo().getStateName();
				if("com.bjsasc.ddm.distributeelectaskoperate.list.sign".equals(action.getId()) 
					|| "com.bjsasc.ddm.papersigntaskoperate.list.sign".equals(action.getId()) 
					|| "com.bjsasc.ddm.distributeelectaskoperate.list.refusesign".equals(action.getId()) 
					|| "com.bjsasc.ddm.papersigntaskoperate.list.refusesign".equals(action.getId())) {
					if(ConstUtil.LC_NOT_SIGNED.getName().equals(state))
					{// ���������ֽ��ǩ������Ĳ�����ť
						//�޸�ΪֻҪ�ܿ����������񣬵�������İ�ť������Ȩ���ж�
						return UIState.ENABLED;
//						if(plmUserInnerID.equals(listDis.get(0).getDisInfoId()) || aaUserIID.equals(listDis.get(0).getDisInfoId())){
//							return UIState.ENABLED;
//						}else{
//							boolean flat = disElecTaskService.isUserlimit();
//							if(flat == true)
//							{// ���������ֽ��ǩ������Ĳ�����ť
//								if(ConstUtil.C_DISMEDIATYPE_2.equals(listDis.get(0).getDisMediaType())){
//									//TODO ����ַ�Э��Ա�ĳ��� ���ڿ���ַ�Э��Ա�ǰ���վ�������õ�������Ҫ����վ����ж�
//									return UIState.ENABLED;
//								}else{
//									//��������Ա�ĳ���
//									String aaOrgName = SessionHelper.getService().getUser().getOrganizationName();
//									if(aaOrgName != null && aaOrgName.equals(listDis.get(0).getDisInfoName())){
//										return UIState.ENABLED;
//									}
//								}
//							}
//						}
					} 
				}else if("com.bjsasc.ddm.distributeforwardoperate.list.forward".equals(action.getId())){

					boolean flat = disElecTaskService.isUserlimit();
					if(ConstUtil.LC_SIGNED.getName().equals(state) && flat == true)
					{// ���������ֽ��ǩ������Ĳ�����ť
						if(ConstUtil.C_DISMEDIATYPE_2.equals(listDis.get(0).getDisMediaType())){
							//����ַ�Э��Ա�ĳ��� ���ڿ���ַ�Э��Ա�ǰ���վ�������õ�������Ҫ����վ����ж�,ͬʱ��Ҫ�жϵ�ǰ�û��Ƿ��ǿ���ַ�Э��Ա
							return UIState.ENABLED;
						}else{
							String aaOrgName = SessionHelper.getService().getUser().getOrganizationName();
							if(aaOrgName != null && aaOrgName.equals(listDis.get(0).getDisInfoName())){
								return UIState.ENABLED;
							}
						}
					}
				}else if("com.bjsasc.ddm.distributeforwardoperate.list.submit".equals(action.getId())
						|| "com.bjsasc.ddm.paperforwardoperate.list.submit".equals(action.getId())){
					if(ConstUtil.LC_SIGNED.getName().equals(state))
					{// ���������ֽ��ǩ������Ĳ�����ť
						//�޸�ΪֻҪ�ܿ����������񣬵�������İ�ť������Ȩ���ж�
						return UIState.ENABLED;
//						if(plmUserInnerID.equals(listDis.get(0).getDisInfoId()) || aaUserIID.equals(listDis.get(0).getDisInfoId())){
//							return UIState.ENABLED;
//						}else{
//							boolean flat = disElecTaskService.isUserlimit();
//							if(flat == true)
//							{// ���������ֽ��ǩ������Ĳ�����ť
//								if(ConstUtil.C_DISMEDIATYPE_2.equals(listDis.get(0).getDisMediaType())){
//									//TODO ����ַ�Э��Ա�ĳ��� ���ڿ���ַ�Э��Ա�ǰ���վ�������õ�������Ҫ����վ����ж�
//									return UIState.ENABLED;
//								}else{
//									String aaOrgName = SessionHelper.getService().getUser().getOrganizationName();
//									if(aaOrgName != null && aaOrgName.equals(listDis.get(0).getDisInfoName())){
//										return UIState.ENABLED;
//									}
//								}
//							}
//						}
					} 
				}
			}
		}		
		return UIState.DISABLED;
	}
}
