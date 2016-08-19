package com.bjsasc.ddm.distribute.service.convertversion;
import java.util.List;

import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.ddm.distribute.model.convertversion.DcA3SigObjectLink;

/**
 * ��汾����ǩ����Ϣ����ʵ���ࡣ
 * 
 * @author zhangguoqiang 2014-7-11
 */
public class DcA3SigObjectLinkServiceImpl implements DcA3SigObjectLinkService {
	
	/**
	 * ���ؿ�汾����ǩ����Ϣ
	 * @return
	 *     dcA3SigObjectLink ��汾����ǩ����Ϣ����
	 */
	public DcA3SigObjectLink newDcA3SigObjectLink(){
		DcA3SigObjectLink dcA3SigObjectLink = (DcA3SigObjectLink) PersistUtil.createObject(DcA3SigObjectLink.CLASSID);
		return dcA3SigObjectLink;
	}
	
	/**
	 * ��ӿ�汾����ǩ����Ϣ
	 * @param
	 *     dcA3SigObjectLink ��汾����ǩ����Ϣ����
	 */
	public void addDcA3SigObjectLink(DcA3SigObjectLink dcA3SigObjectLink){
		Helper.getPersistService().save(dcA3SigObjectLink);
	}
	
	/**
	 * ��ӿ�汾����ǩ����Ϣ
	 * @param
	 *     dcA3SigObjectLinkList ��汾����ǩ����Ϣ����
	 */
	public void addDcA3SigObjectLinkList(List<DcA3SigObjectLink> dcA3SigObjectLinkList){
		Helper.getPersistService().save(dcA3SigObjectLinkList);
	}
	
	/**
	 * ����taskIID��ѯ��汾����ǩ����Ϣ
	 * @param
	 *     taskSigIID ��汾����ǩ����Ϣ�����innerId
	 */
	public List<DcA3SigObjectLink> getDcA3SigObjectLinkByTaskSigIID(String taskSigIID){
		String hql = "from DcA3SigObjectLink where fromObjectId = ? ";
		List<DcA3SigObjectLink> linkList = Helper.getPersistService().find(hql, taskSigIID);
		return linkList;
	}
	
	/**
	 * ɾ����汾����ǩ����Ϣ
	 * @param
	 *     dcA3SigObjectLink ��汾����ǩ����Ϣ����
	 */
	public void deleteDcA3SigObjectLink(DcA3SigObjectLink dcA3SigObjectLink){
		Helper.getPersistService().delete(dcA3SigObjectLink);
	}
	
	/**
	 * ɾ����汾����ǩ����Ϣ�б�
	 * @param
	 *     dcA3SigObjectLinkList ��汾����ǩ����Ϣ�����б�
	 */
	public void deleteDcA3SigObjectLinkList(List<DcA3SigObjectLink> dcA3SigObjectLinkList){
		Helper.getPersistService().delete(dcA3SigObjectLinkList);
	}
}
