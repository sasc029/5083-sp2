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
 * ���Ź�������Ȩ�����û���
 * @author dail
 *
 */
public class DdmTypePermissionCache implements ServletContextListener {

	/**
	 * ������¼���õĶ�������
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
		//������¼���е�������
		DistributeConfigParameterService dps = DistributeHelper.getDistributeConfigParameterService();
		Map<String, List<String>> parMap = dps.getAllParam();
		typeMap.putAll(parMap);
	}
	
	public static void clearTypeMap(){
		typeMap.clear();
	}
}
