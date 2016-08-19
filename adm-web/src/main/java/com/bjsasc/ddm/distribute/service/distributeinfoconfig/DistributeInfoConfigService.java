package com.bjsasc.ddm.distribute.service.distributeinfoconfig;

import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.distribute.model.distributeinfoconfig.DistributeInfoConfig;

/**
 * 默认分发信息服务类借口
 * 
 * @author yangqun 2014-05-09
 *
 */
public interface DistributeInfoConfigService {
	
	/**
	 * 获取默认分发信息配置信息列表。
	 * @return List<DistributeInfoConfig>
	 */
	public List<DistributeInfoConfig> getAllDistributeInfoConfig();

	/**
	 * 获取默认分发信息配置信息列表。
	 * @param spot xml配置的spot
	 * @param spotInstance xml配置的spotInstance
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> listDistributeInfoConfig(String spot, String spotInstance);
	
	/**
	 * 添加默认分发信息配置信息列表。
	 * @param distributeInfoConfig 分发信息配置模型
	 */
	
	public void addDistributeInfoConfig(DistributeInfoConfig distributeInfoConfig);
	
	/**
	 * 添加默认分发信息配置信息列表。
	 * 如果如果所添加的用户已存在则不添加，否则添加
	* @param distributeInfoConfig 分发信息配置模型
	 */
	public boolean addDistributeInfoConfig(String type,String iids,String disMediaTypes,String disInfoNums,String notes);
	
	/**
	 * 添加默认分发信息配置信息列表。
	 * @param distributeInfoConfigList 分发信息配置模型
	 */
	public void addDistributeInfoConfigList(List<DistributeInfoConfig> distributeInfoConfigList);
	/**
	 * 删除默认分发信息配置信息列表。
	 * @param distributeInfoConfig 分发信息配置模型
	 */
	public void delDistributeInfoConfig(DistributeInfoConfig distributeInfoConfig);
	
	/**
	 * 修改默认分发信息配置信息列表。
	 * @param distributeInfoConfig 分发信息配置模型
	 */
	public void updateDistributeInfoConfig(DistributeInfoConfig distributeInfoConfig);
	
	/**
	 * 过滤重复的用户或者组织
	 * @param  distributeInfoConfig 分发信息配置模型
	 * @return true/false
	 */
	public boolean filtSameUserOrOrgnization(DistributeInfoConfig distributeInfoConfig);
	
	

}
