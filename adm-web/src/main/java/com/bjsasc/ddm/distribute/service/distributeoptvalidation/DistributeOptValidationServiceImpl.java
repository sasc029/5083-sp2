package com.bjsasc.ddm.distribute.service.distributeoptvalidation;

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
public class DistributeOptValidationServiceImpl implements DistributeOptValidationService {

	public boolean getDistributeOperateValidation(String operateId, String oids) {
		User currentUser = SessionHelper.getService().getUser();
		List<String> oidList = SplitString.string2List(oids, ",");
		boolean flag = true;
		for (String oid : oidList) {
			Domained domained = (Domained) Helper.getPersistService().getObject(oid);
			flag = AccessControlHelper.getService().hasEntityPermission(currentUser, operateId, domained);
			if(flag == false){
				AccessControlHelper.getService().checkEntityPermission(operateId, domained);
			}
		}
		return flag;
	}
	
	public boolean getCreateDistributeOrderValidation(String operateId, Domained domained) {
		User currentUser = SessionHelper.getService().getUser();
		boolean flag = AccessControlHelper.getService().hasEntityPermission(currentUser, operateId, domained);
		return flag;
	}
}
