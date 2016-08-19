package com.bjsasc.ddm.distribute.service.convertversion;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.ddm.distribute.model.convertversion.DcA3TaskSignature;

/**
 * ��汾����ǩ����Ϣ����ʵ���ࡣ
 * 
 * @author zhangguoqiang 2015-05-01
 */
public class DcA3TaskSignatureServiceImpl implements DcA3TaskSignatureService {
	
	/**
	 * ���ؿ�汾����ǩ����Ϣ
	 * @return
	 *     dcA3TaskSignature ��汾����ǩ����Ϣ����
	 */
	public DcA3TaskSignature newDcA3TaskSignature(){
		DcA3TaskSignature dcA3TaskSignature = (DcA3TaskSignature) PersistUtil.createObject(DcA3TaskSignature.CLASSID);
		return dcA3TaskSignature;
	}
	
	/**
	 * ��ӿ�汾����ǩ����Ϣ
	 * @param
	 *     dcA3TaskSignature ��汾����ǩ����Ϣ����
	 */
	public void addDcA3TaskSignature(DcA3TaskSignature dcA3TaskSignature){
		Helper.getPersistService().save(dcA3TaskSignature);
	}
	
	/**
	 * ����taskIID��ѯ��汾����ǩ����Ϣ
	 * @param
	 *     taskIID ��汾����ǩ����Ϣ�����ڴ洢��ǩ����������taskIID
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
	 * ����orderIID��ѯ��汾����ǩ����Ϣ��domainIID�б�
	 * @param
	 *     orderIID ��汾����ǩ����Ϣ�����ڴ洢��ǩ����������orderIID
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
	 * ����orderIID,taskIID��ѯ��汾����ǩ����Ϣ
	 * @param
	 *     orderIID ��汾����ǩ����Ϣ�����ڴ洢��ǩ����������orderIID
	 * @param
	 *     domainIID ��汾����ǩ����Ϣ�����ڴ洢��ǩ����������domainIID
	 */
	public List<DcA3TaskSignature> listDcA3TaskSignatureByDomainIID(String orderIID, String domainIID){
		String hql = "from DcA3TaskSignature where orderIID = ? and domainIID = ?";
		List<DcA3TaskSignature> sigList = Helper.getPersistService().find(hql, orderIID, domainIID);
		return sigList;
	}
	
	/**
	 * ���¿�汾����ǩ����Ϣ
	 * @param
	 *     dcA3TaskSignature ��汾����ǩ����Ϣ����
	 */
	public void updateDcA3TaskSignature(DcA3TaskSignature dcA3TaskSignature){
		Helper.getPersistService().update(dcA3TaskSignature);
	}
	
	/**
	 * ɾ����汾����ǩ����Ϣ
	 * @param
	 *     dcA3TaskSignature ��汾����ǩ����Ϣ����
	 */
	public void deleteDcA3TaskSignature(DcA3TaskSignature dcA3TaskSignature){
		Helper.getPersistService().delete(dcA3TaskSignature);
	}
	
	/**
	 * ɾ����汾����ǩ����Ϣ�б�
	 * @param
	 *     dcA3TaskSignatureList ��汾����ǩ����Ϣ�����б�
	 */
	public void deleteDcA3TaskSignatureList(List<DcA3TaskSignature> dcA3TaskSignatureList){
		Helper.getPersistService().delete(dcA3TaskSignatureList);
	}
}
