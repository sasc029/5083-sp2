package com.bjsasc.ddm.distribute.service.distributeconfigparameter;

import java.util.List;
import java.util.Map;

/**
 * 发放管理参数配置服务接口。
 * 
 * @author gengancong 2014-5-5
 */
public interface DistributeConfigParameterService {
	
	/**
	 * 通过上下文OID取得数据。
	 * 
	 * @param contextOid 上下文OID
	 * @param paramId 参数
	 * @return List
	 */
	public List<Map<String, Object>> findByContextOid(String contextOid, String paramId);
	public boolean exists(String contextOid, String paramId);

	/**
	 * 删除数据。
	 * 
	 * @param contextOid 上下文OID
	 */
	public void deleteConfigData(String contextOid);
	
	/**
	 * 添加数据。
	 * 
	 * @param contextOid 上下文OID
	 * @param listTypeMap 提交数据
	 */
	public void addConfigData(String contextOid, String paramId,
			String paramName, String defaultValue, String currentValue,
			String state, String description);
	
	/**
	 * 获取所有的配置项
	 * @return
	 */
	public Map<String, List<String>> getAllParam();
}
