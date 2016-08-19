package com.bjsasc.ddm.distribute.service.convertversion;

import java.util.List;

import com.bjsasc.ddm.distribute.model.convertversion.DcA3SigObjectLink;

/**
 * 跨版本任务签署信息与分发业务对象link服务接口。
 * 
 * @author zhangguoqiang 2015-05-01
 */
public interface DcA3SigObjectLinkService {
	
	/**
	 * 返回跨版本任务签署信息与分发业务对象link
	 * @return
	 *     dcA3SigObjectLink 跨版本任务签署信息与分发业务对象link对象
	 */
	public DcA3SigObjectLink newDcA3SigObjectLink();
	
	/**
	 * 添加跨版本任务签署信息与分发业务对象link
	 * @param
	 *     dcA3SigObjectLink 跨版本任务签署信息与分发业务对象link对象
	 */
	public void addDcA3SigObjectLink(DcA3SigObjectLink dcA3SigObjectLink);
	
	/**
	 * 添加跨版本任务签署信息与分发业务对象link
	 * @param
	 *     dcA3SigObjectLinkList 跨版本任务签署信息与分发业务对象link对象
	 */
	public void addDcA3SigObjectLinkList(List<DcA3SigObjectLink> dcA3SigObjectLinkList);

	/**
	 * 根据taskIID查询跨版本任务签署信息与分发业务对象link
	 * @param
	 *     taskSigIID 跨版本任务签署信息与分发业务对象link对象的innerId
	 */
	public List<DcA3SigObjectLink> getDcA3SigObjectLinkByTaskSigIID(String taskSigIID);
	
	/**
	 * 删除跨版本任务签署信息与分发业务对象link
	 * @param
	 *     dcA3SigObjectLink 跨版本任务签署信息与分发业务对象link对象
	 */
	public void deleteDcA3SigObjectLink(DcA3SigObjectLink dcA3SigObjectLink);
	
	/**
	 * 删除跨版本任务签署信息与分发业务对象link列表
	 * @param
	 *     dcA3SigObjectLinkList 跨版本任务签署信息与分发业务对象link对象列表
	 */
	public void deleteDcA3SigObjectLinkList(List<DcA3SigObjectLink> dcA3SigObjectLinkList);
}
