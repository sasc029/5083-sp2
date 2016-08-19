package com.bjsasc.ddm.distribute.service.convertversion;

import java.util.List;

import com.bjsasc.ddm.distribute.model.convertversion.DcA3SigObjectLink;

/**
 * ��汾����ǩ����Ϣ��ַ�ҵ�����link����ӿڡ�
 * 
 * @author zhangguoqiang 2015-05-01
 */
public interface DcA3SigObjectLinkService {
	
	/**
	 * ���ؿ�汾����ǩ����Ϣ��ַ�ҵ�����link
	 * @return
	 *     dcA3SigObjectLink ��汾����ǩ����Ϣ��ַ�ҵ�����link����
	 */
	public DcA3SigObjectLink newDcA3SigObjectLink();
	
	/**
	 * ��ӿ�汾����ǩ����Ϣ��ַ�ҵ�����link
	 * @param
	 *     dcA3SigObjectLink ��汾����ǩ����Ϣ��ַ�ҵ�����link����
	 */
	public void addDcA3SigObjectLink(DcA3SigObjectLink dcA3SigObjectLink);
	
	/**
	 * ��ӿ�汾����ǩ����Ϣ��ַ�ҵ�����link
	 * @param
	 *     dcA3SigObjectLinkList ��汾����ǩ����Ϣ��ַ�ҵ�����link����
	 */
	public void addDcA3SigObjectLinkList(List<DcA3SigObjectLink> dcA3SigObjectLinkList);

	/**
	 * ����taskIID��ѯ��汾����ǩ����Ϣ��ַ�ҵ�����link
	 * @param
	 *     taskSigIID ��汾����ǩ����Ϣ��ַ�ҵ�����link�����innerId
	 */
	public List<DcA3SigObjectLink> getDcA3SigObjectLinkByTaskSigIID(String taskSigIID);
	
	/**
	 * ɾ����汾����ǩ����Ϣ��ַ�ҵ�����link
	 * @param
	 *     dcA3SigObjectLink ��汾����ǩ����Ϣ��ַ�ҵ�����link����
	 */
	public void deleteDcA3SigObjectLink(DcA3SigObjectLink dcA3SigObjectLink);
	
	/**
	 * ɾ����汾����ǩ����Ϣ��ַ�ҵ�����link�б�
	 * @param
	 *     dcA3SigObjectLinkList ��汾����ǩ����Ϣ��ַ�ҵ�����link�����б�
	 */
	public void deleteDcA3SigObjectLinkList(List<DcA3SigObjectLink> dcA3SigObjectLinkList);
}
