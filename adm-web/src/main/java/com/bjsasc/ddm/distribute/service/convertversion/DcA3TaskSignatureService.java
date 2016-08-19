package com.bjsasc.ddm.distribute.service.convertversion;

import java.util.List;

import com.bjsasc.ddm.distribute.model.convertversion.DcA3TaskSignature;

/**
 * ��汾����ǩ����Ϣ����ӿڡ�
 * 
 * @author zhangguoqiang 2015-05-01
 */
public interface DcA3TaskSignatureService {
	
	/**
	 * ���ؿ�汾����ǩ����Ϣ
	 * @return
	 *     dcA3TaskSignature ��汾����ǩ����Ϣ����
	 */
	public DcA3TaskSignature newDcA3TaskSignature();
	
	/**
	 * ��ӿ�汾����ǩ����Ϣ
	 * @param
	 *     dcA3TaskSignature ��汾����ǩ����Ϣ����
	 */
	public void addDcA3TaskSignature(DcA3TaskSignature dcA3TaskSignature);

	/**
	 * ����taskIID��ѯ��汾����ǩ����Ϣ
	 * @param
	 *     taskIID ��汾����ǩ����Ϣ�����taskIID
	 */
	public DcA3TaskSignature getDcA3TaskSignatureByTaskId(String taskIID);

	/**
	 * ����orderIID��ѯ��汾����ǩ����Ϣ��domainIID�б�
	 * @param
	 *     orderIID ��汾����ǩ����Ϣ�����orderIID
	 */
	public List<String> getDomainIIDListByOrderIID(String orderIID);

	/**
	 * ����orderIID,domainIID��ѯ��汾����ǩ����Ϣ
	 * @param
	 *     orderIID ��汾����ǩ����Ϣ�����orderIID
	 * @param
	 *     domainIID ��汾����ǩ����Ϣ�����domainIID
	 */
	public List<DcA3TaskSignature> listDcA3TaskSignatureByDomainIID(String orderIID, String domainIID);

	/**
	 * ���¿�汾����ǩ����Ϣ
	 * @param
	 *     dcA3TaskSignature ��汾����ǩ����Ϣ����
	 */
	public void updateDcA3TaskSignature(DcA3TaskSignature dcA3TaskSignature);
	
	/**
	 * ɾ����汾����ǩ����Ϣ
	 * @param
	 *     dcA3TaskSignature ��汾����ǩ����Ϣ����
	 */
	public void deleteDcA3TaskSignature(DcA3TaskSignature dcA3TaskSignature);
	
	/**
	 * ɾ����汾����ǩ����Ϣ�б�
	 * @param
	 *     dcA3TaskSignatureList ��汾����ǩ����Ϣ�����б�
	 */
	public void deleteDcA3TaskSignatureList(List<DcA3TaskSignature> dcA3TaskSignatureList);

}
