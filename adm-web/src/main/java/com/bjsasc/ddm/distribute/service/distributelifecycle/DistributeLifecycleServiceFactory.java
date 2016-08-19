package com.bjsasc.ddm.distribute.service.distributelifecycle;

import com.bjsasc.ddm.common.DdmLifecycleUtil;

public class DistributeLifecycleServiceFactory {
	
	private DistributeLifecycleServiceFactory() {
		
	}
	
	public static DistributeLifecycleService getLifecycleService(){
		if(DdmLifecycleUtil.isUpdateStateInWorkflow()){
			return new DefaultLifecycleServiceImpl();
		}
		return new DdmLifecycleServiceImpl();
	}
}
