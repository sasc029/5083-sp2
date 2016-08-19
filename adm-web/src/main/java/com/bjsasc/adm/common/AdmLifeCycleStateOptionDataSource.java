package com.bjsasc.adm.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleTemplate;
import com.bjsasc.platform.objectmodel.business.lifeCycle.State;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.context.ContextHelper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.template.TemplateHelper;
import com.bjsasc.plm.core.option.IOptionDataSource;

/**
 * ����Դ����Ϊclassʱ��ȡ��������
 * @author
 *
 */
public class AdmLifeCycleStateOptionDataSource implements IOptionDataSource {

	public Map<String, String> getDataSource() {
		Map<String,String> dataMap = new HashMap<String, String>();
		dataMap.put("all", "ȫ��");
		
		Context context = ContextHelper.getService().getRootContext();
		LifeCycleTemplate lifeCycleTemplate = null;
		String templateName = "����������������";
		String classId = "ActiveDocument";
		List<LifeCycleTemplate> templates = TemplateHelper.getService().findLifeCycleTemplatesEnabled(context, classId);
		if(templates.size() == 0){
			throw new RuntimeException("δ�ҵ��ö������Ͱ󶨵��������õ���������ģ��");
		}
		for(LifeCycleTemplate template:templates){
			if(template.getName().equals(templateName)){
				lifeCycleTemplate = template;
				break;
			}
		}
		if(lifeCycleTemplate == null){
			throw new RuntimeException("�ڶ���󶨵���������ģ����,δ�ҵ���ʼ������ָ����ģ��:" + templateName);
		}		
		//��Ŀ��ģ�����Ҹ�����״̬
		List<State> templateStates = Helper.getLifeCycleService().findStates(lifeCycleTemplate);

		if(templateStates != null && templateStates.size()>0){
			 for(State temp:templateStates){
				 dataMap.put(temp.getName(), temp.getName());
			 }			 
		 }
		return dataMap;
	}
}
