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
 * 数据源类型为class时获取数据项类
 * @author
 *
 */
public class AdmLifeCycleStateOptionDataSource implements IOptionDataSource {

	public Map<String, String> getDataSource() {
		Map<String,String> dataMap = new HashMap<String, String>();
		dataMap.put("all", "全部");
		
		Context context = ContextHelper.getService().getRootContext();
		LifeCycleTemplate lifeCycleTemplate = null;
		String templateName = "现行数据生命周期";
		String classId = "ActiveDocument";
		List<LifeCycleTemplate> templates = TemplateHelper.getService().findLifeCycleTemplatesEnabled(context, classId);
		if(templates.size() == 0){
			throw new RuntimeException("未找到该对象类型绑定的且已启用的生命周期模板");
		}
		for(LifeCycleTemplate template:templates){
			if(template.getName().equals(templateName)){
				lifeCycleTemplate = template;
				break;
			}
		}
		if(lifeCycleTemplate == null){
			throw new RuntimeException("在对象绑定的生命周期模板中,未找到初始化规则指定的模板:" + templateName);
		}		
		//在目标模板里找给定的状态
		List<State> templateStates = Helper.getLifeCycleService().findStates(lifeCycleTemplate);

		if(templateStates != null && templateStates.size()>0){
			 for(State temp:templateStates){
				 dataMap.put(temp.getName(), temp.getName());
			 }			 
		 }
		return dataMap;
	}
}
