package com.bjsasc.ddm.distribute.service.distributelifecycle;

import java.util.List;
import java.util.Map;

/**
 * 生命周期升级服务接口。
 * 
 * @author gengancong 2014-5-15
 */
public interface LifeCycleUpdateService {

	/**
	 * 更新生命周期。
	 * 
	 * @param params List
	 * @param step String
	 * @return List
	 */
	public List<String> updateLifeCycle(List<Map<String, String>> paramList, String step);

}
