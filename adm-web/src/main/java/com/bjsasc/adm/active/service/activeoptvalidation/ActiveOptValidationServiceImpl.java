package com.bjsasc.adm.active.service.activeoptvalidation;

import java.util.List;

import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.access.AccessControlHelper;
import com.bjsasc.plm.core.system.principal.User;
import com.cascc.avidm.util.SplitString;

/**
 * 操作权限服务实现类
 * 
 * @author guowei 2013-6-9
 */
public class ActiveOptValidationServiceImpl implements ActiveOptValidationService {

	public boolean getActiveOperateValidation(String operateId, String oids) {
		User currentUser = SessionHelper.getService().getUser();
		List<String> oidList = SplitString.string2List(oids, ",");
		boolean flag = true;
		for (String oid : oidList) {			
			String classId = Helper.getClassId(oid);
			if("SubFolder".equals(classId)){
				List<String> operate = SplitString.string2List(operateId, "_");
			   operateId = "plm_"+operate.get(1);
			}
			
			Domained domained = (Domained) Helper.getPersistService().getObject(oid);
			flag = AccessControlHelper.getService().hasEntityPermission(currentUser, operateId, domained);
			if(flag == false){
				AccessControlHelper.getService().checkEntityPermission(operateId, domained);
			}
		}
		return flag;
	}
	
	public boolean getCreateActiveDocumentValidation(String operateId, Domained domained) {
		User currentUser = SessionHelper.getService().getUser();
		boolean flag = AccessControlHelper.getService().hasEntityPermission(currentUser, operateId, domained);
		return flag;
	}
}
