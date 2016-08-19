package com.bjsasc.ddm.distribute.service.distributeobjectnotautocreate;

import java.util.List;
import java.util.Map;

import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.system.principal.User;

public interface DistributeObjectNotAutoCreateService {
	/**
	 * 通过上下文OID取得数据。
	 * 
	 * @param contextOid 上下文OID
	 * @return List
	 */
	public List<Map<String, Object>> findByContextOid(String contextOid);
	public List<Map<String, Object>> findByContextOid(String contextOid, String spot, String spotInstance);

	/**
	 * 删除数据。
	 * 
	 * @param contextOid 上下文OID
	 * @param typeNames
	 */
	public void deleteTypeData(String contextOid, String typeNames);
	
	/**
	 * 添加数据。
	 * 
	 * @param contextOid 上下文OID
	 * @param listTypeMap 提交数据
	 */
	public void addTypeData(String contextOid, List<Map<String, String>> listTypeMap);


	/**
	 * 。 
	 * 1，判断指定上下文是否已经定义了配置信息 
	 * 
	 * 
	 * 
	 * @param context 上下文
	 * @param 
	 */
	public List<String> getContextType(Context context);
}
