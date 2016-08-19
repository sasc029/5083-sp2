package com.bjsasc.ddm.distribute.action.distributeoptvalidation;

import com.bjsasc.ddm.distribute.action.AbstractAction;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.service.distributeoptvalidation.DistributeOptValidationService;
import com.bjsasc.plm.KeyS;

public class DistributeOptValidationAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = 7613337709541863954L;

	/** DistributeOptValidationService */
	private final DistributeOptValidationService service = DistributeHelper.getDistributeOptValidationService();

	public String distributeOperateValidation() {
		try {
			String operateId = request.getParameter("optId");
			String oids = request.getParameter(KeyS.OIDS);
			service.getDistributeOperateValidation(operateId, oids);
			
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}
}
