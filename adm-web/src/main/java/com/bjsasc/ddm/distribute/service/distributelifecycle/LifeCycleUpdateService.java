package com.bjsasc.ddm.distribute.service.distributelifecycle;

import java.util.List;
import java.util.Map;

/**
 * ����������������ӿڡ�
 * 
 * @author gengancong 2014-5-15
 */
public interface LifeCycleUpdateService {

	/**
	 * �����������ڡ�
	 * 
	 * @param params List
	 * @param step String
	 * @return List
	 */
	public List<String> updateLifeCycle(List<Map<String, String>> paramList, String step);

}
