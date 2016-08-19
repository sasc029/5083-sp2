package com.bjsasc.ddm.common;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.system.access.operate.register.OperateRegisterUtil;

public class DdmListener implements ServletContextListener{
	
	public void contextInitialized(ServletContextEvent sce) {
		String sql = "select innerId from AAOPERATIONDATA where innerId = 'ddm_create'";
		List<Map<String,Object>> list = Helper.getPersistService().query(sql);
		if(list.size() == 0){
			OperateRegisterUtil.registerAll();
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO 自动生成的方法存根
		
	}
}
