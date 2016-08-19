package com.bjsasc.ddm.distribute.service.distributecommonconfig;

import java.util.List;

import com.bjsasc.ddm.distribute.model.distributecommonconfig.DistributeCommonConfig;

/**
 * ���Ź���ͨ�����÷���ӿڡ�
 * 
 * @author zhangguoqiang 2014-7-11
 */
public interface DistributeCommonConfigService {
	/**
	 * ���ط��Ź���ͨ�ò�������
	 * @return
	 *     distributeCommonConfig ���Ź���ͨ�ò������ö���
	 */
	public DistributeCommonConfig newDistributeCommonConfig();
	/**
	 * ��ӷ��Ź���ͨ�ò�������
	 * @param
	 *     distributeCommonConfig ���Ź���ͨ�ò������ö���
	 */
	public void addDistributeCommonConfig(DistributeCommonConfig distributeCommonConfig);
	
	/**
	 * ��ѯ���з��Ź���ͨ�ò�������
	 */
	public List<DistributeCommonConfig> findAllDistributeCommonConfig();
	
	/**
	 * ����innerId��ѯ���Ź���ͨ�ò�������
	 * @param
	 *     innerId ���Ź���ͨ�ò������ö����innerId
	 */
	public DistributeCommonConfig findDistributeCommonConfigById(String innerId);
	
	/**
	 * ����configId��ѯ���Ź���ͨ�ò�������
	 * @param
	 *     configId ���Ź���ͨ�ò������ö����configId
	 */
	public DistributeCommonConfig findDistributeCommonConfigByConfigId(String configId);
	
	/**
	 * ����configId��ѯ���Ź���ͨ�ò������õ�����ֵ
	 * @param
	 *     configId ���Ź���ͨ�ò������ö����configId
	 * @return
	 *     configValue ���Ź���ͨ�ò������ö����configValue
	 */
	public String getConfigValueByConfigId(String configId);
	
	/**
	 * ɾ�����Ź���ͨ�ò�������
	 * @param
	 *     distributeCommonConfig ���Ź���ͨ�ò������ö���
	 */
	public void deleteDistributeCommonConfig(DistributeCommonConfig distributeCommonConfig);
	/**
	 * ɾ���������
	 * @param ids ������������
	 */
	public void deleteDistributeCommonConfig(String[] ids);
	/**
	 * �޸ķ��Ź���ͨ�ò�������
	 * @param
	 *     distributeCommonConfig ���Ź���ͨ�ò������ö���
	 */
	public void updateDistributeCommonConfig(DistributeCommonConfig distributeCommonConfig);
	
	/**
	 * ��֤ ���Ź���ͨ�ò������ö��� ���ñ�� �Ƿ��Ѵ���
	 * @param
	 *     configID ���Ź���ͨ�ò������ö��� ���ñ��
	 * @return
	 *     true �Ѵ���
	 *     false ������
	 */
	public boolean checkDistributeCommonConfigOfConfigID(String configID);
	
	/**
	 * 
	 * �޸���ѡ���ֶ�ֵ
	 * @param columnValue--�ֶ�����
	 * @param columnResult--�ֶ�ֵ
	 * @param InnerId--ID����
	 */
	public void updateDistributeCommonConfigByInnerId(String columnValue,String columnResult,String InnerId);
}
