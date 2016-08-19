package com.bjsasc.adm.active.self;

import com.bjsasc.plm.core.context.template.TemplateService;
import com.bjsasc.plm.core.util.SpringUtil;

/**
 * 自定义服务类helper
 * 
 * @author gaolingjie, 2013-4-13
 */
public class AdmSelfDefHelper {
	
	private AdmSelfDefHelper() {
		
	}
	

	private static TemplateService templdateSerivce;

	public synchronized static TemplateService getTemplateService() {
		if (null == templdateSerivce) {
			templdateSerivce = (TemplateService) SpringUtil.getBean("adm_template_service");
		}

		return templdateSerivce;
	}
}
