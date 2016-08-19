package com.bjsasc.ddm.distribute.service.convertversion;

import java.util.List;

import com.bjsasc.ddm.distribute.model.convertversion.DcA3TaskSignature;

/**
 * 跨版本任务签署信息服务接口。
 * 
 * @author zhangguoqiang 2015-05-01
 */
public interface DcA3TaskSignatureService {
	
	/**
	 * 返回跨版本任务签署信息
	 * @return
	 *     dcA3TaskSignature 跨版本任务签署信息对象
	 */
	public DcA3TaskSignature newDcA3TaskSignature();
	
	/**
	 * 添加跨版本任务签署信息
	 * @param
	 *     dcA3TaskSignature 跨版本任务签署信息对象
	 */
	public void addDcA3TaskSignature(DcA3TaskSignature dcA3TaskSignature);

	/**
	 * 根据taskIID查询跨版本任务签署信息
	 * @param
	 *     taskIID 跨版本任务签署信息对象的taskIID
	 */
	public DcA3TaskSignature getDcA3TaskSignatureByTaskId(String taskIID);

	/**
	 * 根据orderIID查询跨版本任务签署信息的domainIID列表
	 * @param
	 *     orderIID 跨版本任务签署信息对象的orderIID
	 */
	public List<String> getDomainIIDListByOrderIID(String orderIID);

	/**
	 * 根据orderIID,domainIID查询跨版本任务签署信息
	 * @param
	 *     orderIID 跨版本任务签署信息对象的orderIID
	 * @param
	 *     domainIID 跨版本任务签署信息对象的domainIID
	 */
	public List<DcA3TaskSignature> listDcA3TaskSignatureByDomainIID(String orderIID, String domainIID);

	/**
	 * 更新跨版本任务签署信息
	 * @param
	 *     dcA3TaskSignature 跨版本任务签署信息对象
	 */
	public void updateDcA3TaskSignature(DcA3TaskSignature dcA3TaskSignature);
	
	/**
	 * 删除跨版本任务签署信息
	 * @param
	 *     dcA3TaskSignature 跨版本任务签署信息对象
	 */
	public void deleteDcA3TaskSignature(DcA3TaskSignature dcA3TaskSignature);
	
	/**
	 * 删除跨版本任务签署信息列表
	 * @param
	 *     dcA3TaskSignatureList 跨版本任务签署信息对象列表
	 */
	public void deleteDcA3TaskSignatureList(List<DcA3TaskSignature> dcA3TaskSignatureList);

}
