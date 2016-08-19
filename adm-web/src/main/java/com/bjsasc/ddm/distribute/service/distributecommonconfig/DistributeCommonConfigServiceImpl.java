package com.bjsasc.ddm.distribute.service.distributecommonconfig;
import java.util.List;

import com.bjsasc.avidm.core.persist.DTPersistUtil;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.ddm.distribute.model.distributecommonconfig.DistributeCommonConfig;

/**
 * 发放管理通用配置服务实现类。
 * 
 * @author zhangguoqiang 2014-7-11
 */
public class DistributeCommonConfigServiceImpl implements DistributeCommonConfigService {
	/**
	 * 返回发放管理通用参数配置
	 * @return
	 *     distributeCommonConfig 发放管理通用参数配置对象
	 */
	public DistributeCommonConfig newDistributeCommonConfig(){
		DistributeCommonConfig distributeCommonConfig = (DistributeCommonConfig) PersistUtil.createObject(DistributeCommonConfig.CLASSID);
		return distributeCommonConfig;
	}
	/**
	 * 添加发放管理通用参数配置
	 * @param
	 *     distributeCommonConfig 发放管理通用参数配置对象
	 */
	public void addDistributeCommonConfig(DistributeCommonConfig distributeCommonConfig){
		Helper.getPersistService().save(distributeCommonConfig);
	}
	
	/**
	 * 查询所有发放管理通用参数配置
	 */
	public List<DistributeCommonConfig> findAllDistributeCommonConfig(){
		String hql = "from DistributeCommonConfig";
		return Helper.getPersistService().find(hql);
	}
	
	/**
	 * 根据innerId查询发放管理通用参数配置
	 * @param
	 *     innerId 发放管理通用参数配置对象的innerId
	 */
	public DistributeCommonConfig findDistributeCommonConfigById(String innerId){
		String hql = "from DistributeCommonConfig where innerId = ?";
		List list = Helper.getPersistService().find(hql.toString(), innerId);
		if(!list.isEmpty()){
			return (DistributeCommonConfig)list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 根据configId查询发放管理通用参数配置
	 * @param
	 *     configId 发放管理通用参数配置对象的configId
	 * @return
	 *     发放管理通用参数配置对象
	 */
	public DistributeCommonConfig findDistributeCommonConfigByConfigId(String configId){
		String hql = "from DistributeCommonConfig where configID = ?";
		List list = Helper.getPersistService().find(hql, configId);
		if(!list.isEmpty()){
			return (DistributeCommonConfig)list.get(0);
		}else{
			return null;
		}
	}

	/**
	 * 根据configId查询发放管理通用参数配置的配置值
	 * 配置值为空的时候返回默认配置值
	 * @param
	 *     configId 发放管理通用参数配置对象的configId
	 * @return
	 *     configValue 发放管理通用参数配置对象的configValue
	 */
	public String getConfigValueByConfigId(String configId){
		String hql = "from DistributeCommonConfig where configID = ?";
		List list = Helper.getPersistService().find(hql, configId);
		if(!list.isEmpty()){
			DistributeCommonConfig distributeCommonConfig = (DistributeCommonConfig)list.get(0);
			String configValue = distributeCommonConfig.getConfigValue();
			if(configValue != null && !configValue.isEmpty()){
				return configValue;
			}else{
				return distributeCommonConfig.getConfigDefaultValue();
			}
		}else{
			return null;
		}
	}

	/**
	 * 删除发放管理通用参数配置
	 * @param
	 *     distributeCommonConfig 发放管理通用参数配置对象
	 */
	public void deleteDistributeCommonConfig(DistributeCommonConfig distributeCommonConfig){
		Helper.getPersistService().delete(distributeCommonConfig);
	}
	
	public void deleteDistributeCommonConfig(String[] ids){
		for(int i=0; i<ids.length; i++){
			DistributeCommonConfig config = (DistributeCommonConfig)Helper.getPersistService().getObject(DistributeCommonConfig.class.getSimpleName(),ids[i]);
			deleteDistributeCommonConfig(config);
		}
	}
	/**
	 * 修改发放管理通用参数配置
	 * @param
	 *     distributeCommonConfig 发放管理通用参数配置对象
	 */
	public void updateDistributeCommonConfig(DistributeCommonConfig distributeCommonConfig){
		Helper.getPersistService().update(distributeCommonConfig);
	}
	
	/**
	 * 验证 发放管理通用参数配置对象 配置编号 是否已存在
	 * @param
	 *     configID 发放管理通用参数配置对象 配置编号
	 * @return
	 *     true 已存在
	 *     false 不存在
	 */
	public boolean checkDistributeCommonConfigOfConfigID(String configID){
		String hql = "from DistributeCommonConfig where configID = ?";
		List list = Helper.getPersistService().find(hql, configID);
		if(!list.isEmpty()){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 
	 * 修改运行时配置值
	 * 
	 */
	public void updateDistributeCommonConfigByInnerId(String columnValue,
			String columnResult, String innerId) {
		if(columnValue.indexOf("CONFIGVALUE")<0){
			return;
		}
		
		String hql = "from DistributeCommonConfig where innerId = ?";
		@SuppressWarnings("unchecked")
		List<DistributeCommonConfig> list = DTPersistUtil.getService().find(hql,innerId);
		DistributeCommonConfig config = null;
		if(!list.isEmpty()){
			config = (DistributeCommonConfig)list.get(0);
		}
		if(config != null){
			config.setConfigValue(columnResult);
			DTPersistUtil.getService().update(config);
		}
		
	}
}
