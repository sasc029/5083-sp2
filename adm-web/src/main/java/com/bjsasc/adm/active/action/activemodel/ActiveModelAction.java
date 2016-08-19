package com.bjsasc.adm.active.action.activemodel;

import com.bjsasc.adm.active.action.AbstractAction;
import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.service.admmodelservice.AdmModelService;
import com.bjsasc.adm.common.ActiveInitParameter;

/**
 * �����ļ�ģ��Actionʵ���ࡣ
 * 
 * @author yanjia 2013-5-15
 */
public class ActiveModelAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = -2059368990982210463L;

	/** �����ļ�ģ�ͷ��� */
	AdmModelService service = AdmHelper.getAdmModelService();

	//private Logger log = Logger.getLogger(this.getClass());

	/**
	 * ģ��ת���ļ���
	 * 
	 * @return
	 */
	public String getActiveDocumentFolder() {
		try {
			String spot = "ActiveDocument";
			String tempOid = ActiveInitParameter.getActiveCabinetOid();
			service.synchronizeModel(spot, tempOid);
			
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}
}
