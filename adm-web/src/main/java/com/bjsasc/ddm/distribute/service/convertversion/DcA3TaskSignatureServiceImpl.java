package com.bjsasc.ddm.distribute.service.convertversion;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.ddm.distribute.model.convertversion.DcA3TaskSignature;

/**
 * 跨版本任务签署信息服务实现类。
 * 
 * @author zhangguoqiang 2015-05-01
 */
public class DcA3TaskSignatureServiceImpl implements DcA3TaskSignatureService {
	
	/**
	 * 返回跨版本任务签署信息
	 * @return
	 *     dcA3TaskSignature 跨版本任务签署信息对象
	 */
	public DcA3TaskSignature newDcA3TaskSignature(){
		DcA3TaskSignature dcA3TaskSignature = (DcA3TaskSignature) PersistUtil.createObject(DcA3TaskSignature.CLASSID);
		return dcA3TaskSignature;
	}
	
	/**
	 * 添加跨版本任务签署信息
	 * @param
	 *     dcA3TaskSignature 跨版本任务签署信息对象
	 */
	public void addDcA3TaskSignature(DcA3TaskSignature dcA3TaskSignature){
		Helper.getPersistService().save(dcA3TaskSignature);
	}
	
	/**
	 * 根据taskIID查询跨版本任务签署信息
	 * @param
	 *     taskIID 跨版本任务签署信息对象内存储的签署对象任务的taskIID
	 */
	public DcA3TaskSignature getDcA3TaskSignatureByTaskId(String taskIID){
		String hql = "from DcA3TaskSignature where taskIID = ?";
		List<?> list = Helper.getPersistService().find(hql, taskIID);
		if(!list.isEmpty()){
			return (DcA3TaskSignature)list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 根据orderIID查询跨版本任务签署信息的domainIID列表
	 * @param
	 *     orderIID 跨版本任务签署信息对象内存储的签署对象任务的orderIID
	 */
	public List<String> getDomainIIDListByOrderIID(String orderIID){
		List<String> domainIIDList = new ArrayList<String>();
		String hql = "from DcA3TaskSignature where orderIID = ?";
		List<?> list = Helper.getPersistService().find(hql, orderIID);
		
		for(Object obj : list){
			DcA3TaskSignature dcA3TaskSignature = (DcA3TaskSignature)obj;
			String domainIID = dcA3TaskSignature.getDomainIID();
			if(!domainIIDList.contains(domainIID)){
				domainIIDList.add(domainIID);
			}
		}
		return domainIIDList;
	}
	
	/**
	 * 根据orderIID,taskIID查询跨版本任务签署信息
	 * @param
	 *     orderIID 跨版本任务签署信息对象内存储的签署对象任务的orderIID
	 * @param
	 *     domainIID 跨版本任务签署信息对象内存储的签署对象任务的domainIID
	 */
	public List<DcA3TaskSignature> listDcA3TaskSignatureByDomainIID(String orderIID, String domainIID){
		String hql = "from DcA3TaskSignature where orderIID = ? and domainIID = ?";
		List<DcA3TaskSignature> sigList = Helper.getPersistService().find(hql, orderIID, domainIID);
		return sigList;
	}
	
	/**
	 * 更新跨版本任务签署信息
	 * @param
	 *     dcA3TaskSignature 跨版本任务签署信息对象
	 */
	public void updateDcA3TaskSignature(DcA3TaskSignature dcA3TaskSignature){
		Helper.getPersistService().update(dcA3TaskSignature);
	}
	
	/**
	 * 删除跨版本任务签署信息
	 * @param
	 *     dcA3TaskSignature 跨版本任务签署信息对象
	 */
	public void deleteDcA3TaskSignature(DcA3TaskSignature dcA3TaskSignature){
		Helper.getPersistService().delete(dcA3TaskSignature);
	}
	
	/**
	 * 删除跨版本任务签署信息列表
	 * @param
	 *     dcA3TaskSignatureList 跨版本任务签署信息对象列表
	 */
	public void deleteDcA3TaskSignatureList(List<DcA3TaskSignature> dcA3TaskSignatureList){
		Helper.getPersistService().delete(dcA3TaskSignatureList);
	}
}
