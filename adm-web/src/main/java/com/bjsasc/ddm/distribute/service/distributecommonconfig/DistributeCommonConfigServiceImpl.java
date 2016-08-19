package com.bjsasc.ddm.distribute.service.distributecommonconfig;
import java.util.List;

import com.bjsasc.avidm.core.persist.DTPersistUtil;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.ddm.distribute.model.distributecommonconfig.DistributeCommonConfig;

/**
 * ���Ź���ͨ�����÷���ʵ���ࡣ
 * 
 * @author zhangguoqiang 2014-7-11
 */
public class DistributeCommonConfigServiceImpl implements DistributeCommonConfigService {
	/**
	 * ���ط��Ź���ͨ�ò�������
	 * @return
	 *     distributeCommonConfig ���Ź���ͨ�ò������ö���
	 */
	public DistributeCommonConfig newDistributeCommonConfig(){
		DistributeCommonConfig distributeCommonConfig = (DistributeCommonConfig) PersistUtil.createObject(DistributeCommonConfig.CLASSID);
		return distributeCommonConfig;
	}
	/**
	 * ��ӷ��Ź���ͨ�ò�������
	 * @param
	 *     distributeCommonConfig ���Ź���ͨ�ò������ö���
	 */
	public void addDistributeCommonConfig(DistributeCommonConfig distributeCommonConfig){
		Helper.getPersistService().save(distributeCommonConfig);
	}
	
	/**
	 * ��ѯ���з��Ź���ͨ�ò�������
	 */
	public List<DistributeCommonConfig> findAllDistributeCommonConfig(){
		String hql = "from DistributeCommonConfig";
		return Helper.getPersistService().find(hql);
	}
	
	/**
	 * ����innerId��ѯ���Ź���ͨ�ò�������
	 * @param
	 *     innerId ���Ź���ͨ�ò������ö����innerId
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
	 * ����configId��ѯ���Ź���ͨ�ò�������
	 * @param
	 *     configId ���Ź���ͨ�ò������ö����configId
	 * @return
	 *     ���Ź���ͨ�ò������ö���
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
	 * ����configId��ѯ���Ź���ͨ�ò������õ�����ֵ
	 * ����ֵΪ�յ�ʱ�򷵻�Ĭ������ֵ
	 * @param
	 *     configId ���Ź���ͨ�ò������ö����configId
	 * @return
	 *     configValue ���Ź���ͨ�ò������ö����configValue
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
	 * ɾ�����Ź���ͨ�ò�������
	 * @param
	 *     distributeCommonConfig ���Ź���ͨ�ò������ö���
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
	 * �޸ķ��Ź���ͨ�ò�������
	 * @param
	 *     distributeCommonConfig ���Ź���ͨ�ò������ö���
	 */
	public void updateDistributeCommonConfig(DistributeCommonConfig distributeCommonConfig){
		Helper.getPersistService().update(distributeCommonConfig);
	}
	
	/**
	 * ��֤ ���Ź���ͨ�ò������ö��� ���ñ�� �Ƿ��Ѵ���
	 * @param
	 *     configID ���Ź���ͨ�ò������ö��� ���ñ��
	 * @return
	 *     true �Ѵ���
	 *     false ������
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
	 * �޸�����ʱ����ֵ
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
