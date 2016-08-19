package com.bjsasc.ddm.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.service.distributeconfigparameter.DistributeConfigParameterService;
import com.bjsasc.ddm.distribute.service.distributeobjecttype.DistributeObjectTypeService;

/**
 * 发放管理类型权限配置缓存
 * @author dail
 *
 */
public class DdmTypePermissionCache implements ServletContextListener {

	/**
	 * 用来记录配置的对象类型
	 */
	public static Map<String, List<String>> typeMap = new HashMap<String, List<String>>();
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		loadDataSource();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static List<String> getTypeData(String key){
		List<String> v= typeMap.get(key);
		return v;
	}
	
	public static void loadDataSource(){
		DistributeObjectTypeService dots = DistributeHelper.getDistributeObjectTypeService();
		typeMap = dots.getAllType();
		//用来记录所有的配置项
		DistributeConfigParameterService dps = DistributeHelper.getDistributeConfigParameterService();
		Map<String, List<String>> parMap = dps.getAllParam();
		typeMap.putAll(parMap);
	}
	
	public static void clearTypeMap(){
		typeMap.clear();
	}
}
