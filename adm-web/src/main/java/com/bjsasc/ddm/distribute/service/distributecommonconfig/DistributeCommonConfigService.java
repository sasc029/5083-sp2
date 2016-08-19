package com.bjsasc.ddm.distribute.service.distributecommonconfig;

import java.util.List;

import com.bjsasc.ddm.distribute.model.distributecommonconfig.DistributeCommonConfig;

/**
 * 发放管理通用配置服务接口。
 * 
 * @author zhangguoqiang 2014-7-11
 */
public interface DistributeCommonConfigService {
	/**
	 * 返回发放管理通用参数配置
	 * @return
	 *     distributeCommonConfig 发放管理通用参数配置对象
	 */
	public DistributeCommonConfig newDistributeCommonConfig();
	/**
	 * 添加发放管理通用参数配置
	 * @param
	 *     distributeCommonConfig 发放管理通用参数配置对象
	 */
	public void addDistributeCommonConfig(DistributeCommonConfig distributeCommonConfig);
	
	/**
	 * 查询所有发放管理通用参数配置
	 */
	public List<DistributeCommonConfig> findAllDistributeCommonConfig();
	
	/**
	 * 根据innerId查询发放管理通用参数配置
	 * @param
	 *     innerId 发放管理通用参数配置对象的innerId
	 */
	public DistributeCommonConfig findDistributeCommonConfigById(String innerId);
	
	/**
	 * 根据configId查询发放管理通用参数配置
	 * @param
	 *     configId 发放管理通用参数配置对象的configId
	 */
	public DistributeCommonConfig findDistributeCommonConfigByConfigId(String configId);
	
	/**
	 * 根据configId查询发放管理通用参数配置的配置值
	 * @param
	 *     configId 发放管理通用参数配置对象的configId
	 * @return
	 *     configValue 发放管理通用参数配置对象的configValue
	 */
	public String getConfigValueByConfigId(String configId);
	
	/**
	 * 删除发放管理通用参数配置
	 * @param
	 *     distributeCommonConfig 发放管理通用参数配置对象
	 */
	public void deleteDistributeCommonConfig(DistributeCommonConfig distributeCommonConfig);
	/**
	 * 删除多个配置
	 * @param ids 配置主键数组
	 */
	public void deleteDistributeCommonConfig(String[] ids);
	/**
	 * 修改发放管理通用参数配置
	 * @param
	 *     distributeCommonConfig 发放管理通用参数配置对象
	 */
	public void updateDistributeCommonConfig(DistributeCommonConfig distributeCommonConfig);
	
	/**
	 * 验证 发放管理通用参数配置对象 配置编号 是否已存在
	 * @param
	 *     configID 发放管理通用参数配置对象 配置编号
	 * @return
	 *     true 已存在
	 *     false 不存在
	 */
	public boolean checkDistributeCommonConfigOfConfigID(String configID);
	
	/**
	 * 
	 * 修改所选择字段值
	 * @param columnValue--字段名称
	 * @param columnResult--字段值
	 * @param InnerId--ID条件
	 */
	public void updateDistributeCommonConfigByInnerId(String columnValue,String columnResult,String InnerId);
}
