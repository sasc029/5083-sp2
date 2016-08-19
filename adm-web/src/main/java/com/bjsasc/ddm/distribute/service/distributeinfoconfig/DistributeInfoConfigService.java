package com.bjsasc.ddm.distribute.service.distributeinfoconfig;

import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.distribute.model.distributeinfoconfig.DistributeInfoConfig;

/**
 * Ĭ�Ϸַ���Ϣ��������
 * 
 * @author yangqun 2014-05-09
 *
 */
public interface DistributeInfoConfigService {
	
	/**
	 * ��ȡĬ�Ϸַ���Ϣ������Ϣ�б�
	 * @return List<DistributeInfoConfig>
	 */
	public List<DistributeInfoConfig> getAllDistributeInfoConfig();

	/**
	 * ��ȡĬ�Ϸַ���Ϣ������Ϣ�б�
	 * @param spot xml���õ�spot
	 * @param spotInstance xml���õ�spotInstance
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> listDistributeInfoConfig(String spot, String spotInstance);
	
	/**
	 * ���Ĭ�Ϸַ���Ϣ������Ϣ�б�
	 * @param distributeInfoConfig �ַ���Ϣ����ģ��
	 */
	
	public void addDistributeInfoConfig(DistributeInfoConfig distributeInfoConfig);
	
	/**
	 * ���Ĭ�Ϸַ���Ϣ������Ϣ�б�
	 * ����������ӵ��û��Ѵ�������ӣ��������
	* @param distributeInfoConfig �ַ���Ϣ����ģ��
	 */
	public boolean addDistributeInfoConfig(String type,String iids,String disMediaTypes,String disInfoNums,String notes);
	
	/**
	 * ���Ĭ�Ϸַ���Ϣ������Ϣ�б�
	 * @param distributeInfoConfigList �ַ���Ϣ����ģ��
	 */
	public void addDistributeInfoConfigList(List<DistributeInfoConfig> distributeInfoConfigList);
	/**
	 * ɾ��Ĭ�Ϸַ���Ϣ������Ϣ�б�
	 * @param distributeInfoConfig �ַ���Ϣ����ģ��
	 */
	public void delDistributeInfoConfig(DistributeInfoConfig distributeInfoConfig);
	
	/**
	 * �޸�Ĭ�Ϸַ���Ϣ������Ϣ�б�
	 * @param distributeInfoConfig �ַ���Ϣ����ģ��
	 */
	public void updateDistributeInfoConfig(DistributeInfoConfig distributeInfoConfig);
	
	/**
	 * �����ظ����û�������֯
	 * @param  distributeInfoConfig �ַ���Ϣ����ģ��
	 * @return true/false
	 */
	public boolean filtSameUserOrOrgnization(DistributeInfoConfig distributeInfoConfig);
	
	

}
