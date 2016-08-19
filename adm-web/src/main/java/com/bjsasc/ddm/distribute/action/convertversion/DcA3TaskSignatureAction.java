package com.bjsasc.ddm.distribute.action.convertversion;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.bjsasc.plm.Helper;
import com.bjsasc.plm.type.TypeService;
import com.bjsasc.ddm.distribute.action.AbstractAction;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.convertversion.DcA3TaskSignature;
import com.bjsasc.ddm.distribute.service.convertversion.DcA3TaskSignatureService;

/**
 * ��汾����ǩ����Ϣ action
 * @author zhangguoqiang 2015-05-02
 */
public class DcA3TaskSignatureAction extends AbstractAction {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4979992080399513983L;

	//��汾����ǩ����Ϣ  service
	private DcA3TaskSignatureService service = DistributeHelper.getDcA3TaskSignatureService();
	
	
	/**
	 * ���������orderIID��domainIIDȡ�ÿ�汾����ǩ����Ϣ 
	 */
	public String listDcA3TaskSignatureByDomainIID() throws IOException{
		try {
		String orderIID = request.getParameter("orderIID");
		String domainIID = request.getParameter("domainIID");
		List<DcA3TaskSignature> listDis = service.listDcA3TaskSignatureByDomainIID(orderIID,domainIID);
		// ��ʽ����ʾ����
		TypeService typeManager = Helper.getTypeManager();
		for (DcA3TaskSignature target : listDis) {
			if (target == null) {
				continue;
			}
			Map<String, Object> mainMap = typeManager.format(target);

			listMap.add(mainMap);
		}
	} catch (Exception ex) {
		error(ex);
	}
		listToJson();
		return OUTPUTDATA;
	}
}