package com.bjsasc.adm.active.action.activeoptvalidation;

import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.service.activeoptvalidation.ActiveOptValidationService;
import com.bjsasc.adm.active.action.AbstractAction;
import com.bjsasc.plm.KeyS;

/**
 * 现行数据管理权限验证实现类。
 * 
 * @author guowei 2013-8-10
 */
public class ActiveOptValidationAction extends AbstractAction {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -5846167734804012579L;
	
	/** ActiveOptValidationService */
	private final ActiveOptValidationService service = AdmHelper.getActiveOptValidationService();

	public String activeOperateValidation() {
		try {
			String operateId = request.getParameter("optId");
			String oids = request.getParameter(KeyS.OIDS);
			service.getActiveOperateValidation(operateId, oids);
			
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}
}
