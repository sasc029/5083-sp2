package com.bjsasc.ddm.distribute.service.convertversion;
import java.util.List;

import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.ddm.distribute.model.convertversion.DcA3SigObjectLink;

/**
 * 跨版本任务签署信息服务实现类。
 * 
 * @author zhangguoqiang 2014-7-11
 */
public class DcA3SigObjectLinkServiceImpl implements DcA3SigObjectLinkService {
	
	/**
	 * 返回跨版本任务签署信息
	 * @return
	 *     dcA3SigObjectLink 跨版本任务签署信息对象
	 */
	public DcA3SigObjectLink newDcA3SigObjectLink(){
		DcA3SigObjectLink dcA3SigObjectLink = (DcA3SigObjectLink) PersistUtil.createObject(DcA3SigObjectLink.CLASSID);
		return dcA3SigObjectLink;
	}
	
	/**
	 * 添加跨版本任务签署信息
	 * @param
	 *     dcA3SigObjectLink 跨版本任务签署信息对象
	 */
	public void addDcA3SigObjectLink(DcA3SigObjectLink dcA3SigObjectLink){
		Helper.getPersistService().save(dcA3SigObjectLink);
	}
	
	/**
	 * 添加跨版本任务签署信息
	 * @param
	 *     dcA3SigObjectLinkList 跨版本任务签署信息对象
	 */
	public void addDcA3SigObjectLinkList(List<DcA3SigObjectLink> dcA3SigObjectLinkList){
		Helper.getPersistService().save(dcA3SigObjectLinkList);
	}
	
	/**
	 * 根据taskIID查询跨版本任务签署信息
	 * @param
	 *     taskSigIID 跨版本任务签署信息对象的innerId
	 */
	public List<DcA3SigObjectLink> getDcA3SigObjectLinkByTaskSigIID(String taskSigIID){
		String hql = "from DcA3SigObjectLink where fromObjectId = ? ";
		List<DcA3SigObjectLink> linkList = Helper.getPersistService().find(hql, taskSigIID);
		return linkList;
	}
	
	/**
	 * 删除跨版本任务签署信息
	 * @param
	 *     dcA3SigObjectLink 跨版本任务签署信息对象
	 */
	public void deleteDcA3SigObjectLink(DcA3SigObjectLink dcA3SigObjectLink){
		Helper.getPersistService().delete(dcA3SigObjectLink);
	}
	
	/**
	 * 删除跨版本任务签署信息列表
	 * @param
	 *     dcA3SigObjectLinkList 跨版本任务签署信息对象列表
	 */
	public void deleteDcA3SigObjectLinkList(List<DcA3SigObjectLink> dcA3SigObjectLinkList){
		Helper.getPersistService().delete(dcA3SigObjectLinkList);
	}
}
