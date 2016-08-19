package com.bjsasc.ddm.distribute.service.distributeobjecttype;

import java.util.List;
import java.util.Map;

import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.system.principal.User;

/**
 * 对象模型配置服务接口。
 * 
 * @author gengancong 2014-5-5
 */
public interface DistributeObjectTypeService {
	
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
	 * @param oids 角色/成员OIDS
	 */
	public void deleteTypeData(String contextOid, String oids);
	
	/**
	 * 添加数据。
	 * 
	 * @param contextOid 上下文OID
	 * @param listTypeMap 提交数据
	 */
	public void addTypeData(String contextOid, List<Map<String, String>> listTypeMap);

	
	/**
	 * 编辑数据。
	 * 
	 * @param contextOid 上下文OID
	 * @param listMap 提交数据
	 * @param listTypeMap 提交数据
	 */
	public void editTypeData(String contextOid, List<Map<String, String>> listMap, List<Map<String, String>> listTypeMap);

	/**
	 * 通过用户信息，用户所在角色，上下文信息取得用户可操作的类型。 
	 * 1，判断指定上下文是否已经定义了配置信息 
	 * 2，获取上下文中包含该承担者的角色
	 * 3，获取发放管理对象类型配置信息
	 * 
	 * @param context 上下文
	 * @param user 用户
	 */
	public Map<String, List<String>> getContextUserType(Context context, User user);
	
	/**
	 * 获取所有配置的类型，用来缓存
	 * @return
	 */
	public Map<String, List<String>> getAllType();
	
}
