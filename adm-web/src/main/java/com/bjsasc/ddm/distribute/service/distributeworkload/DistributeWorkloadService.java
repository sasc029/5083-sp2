package com.bjsasc.ddm.distribute.service.distributeworkload;

import java.util.List;
import com.bjsasc.ddm.distribute.model.distributeworkload.DistributeWorkload;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;

/**
 * ������ͳ�Ʒ���ӿڡ�
 * 
 * @author gengancong 2013-3-21
 */
public interface DistributeWorkloadService {

	/**
	 * ��ȡ������ͳ�����ݡ�
	 * 
	 * @return List
	 */
	public List<DistributeWorkload> getDistributeWorkloads(String where);

	/**
	 * ����������ͳ�ơ�
	 * 
	 * @param dis DistributeWorkload
	 */
	public void createDistributeWorkload(DistributeWorkload dis, LifeCycleManaged object);

	/**
	 * ����������ͳ�ơ�
	 * 
	 * @return DistributeWorkload
	 */
	public DistributeWorkload newDistributeWorkload(LifeCycleManaged object);

}