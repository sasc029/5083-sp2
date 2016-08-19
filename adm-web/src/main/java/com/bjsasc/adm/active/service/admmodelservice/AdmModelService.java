package com.bjsasc.adm.active.service.admmodelservice;

import java.util.Map;

/**
 * �����ļ�ģ�ͷ���ӿڡ�
 * 
 * @author yanjia 2013-5-13
 */
public interface AdmModelService {

	/**
	 * ͬ�����ض�ģ��
	 * 
	 * @param spot
	 * @param tempOid
	 */
	public void synchronizeModel(String spot, String tempOid);

	/**
	 * ȡ�ô��������ļ���Ҫ����
	 * 
	 * @param requestFolderOid String �ļ���OID
	 * @return  result Map<String, String>
	 * 					����  key:CLASSID value:String
	 * 					�ļ���OID  key:FOLDEROID value:String
	 * 					�ļ���PATH  key:FOLDERPATH value:String
	 * 					������OID  key:CONTEXTOID value:String
	 * 				    ������NAME  key:CONTEXTNAME value:String
	 */
	public Map<String, String> getCreateActiveDocumentParam(String requestFolderOid);

	/**
	 * �ݹ�ȡ��ģ��
	 * 
	 * @param folderOid String
	 * @return  String
	 */
	public String getModelFolder(String folderOid);

	/**
	 * ȡ���ļ�����ƥ���ģ��ID
	 * 
	 * @param folderOid String
	 * @return  String
	 */
	public String getModelId(String folderOid);

	/**
	 * ȡ���ļ�����ƥ���ģ������
	 * 
	 * @param folderOid String
	 * @return  String
	 */
	public String getModelName(String folderOid);

	/**
	 * ȡ��ģ�Ͷ�Ӧ�ļ���Ϣ
	 * 
	 * @param folderOid String
	 * @return  result Map<String, String>
	 * 					�ļ���OID  key:FOLDEROID value:String
	 * 					�ļ���PATH  key:FOLDERPATH value:String
	 */
	public Map<String, String> getFolderByModel(String classId);
	
	/**
	 * �ļ����Ƿ���ƥ��ģ��
	 * 
	 * @param folderOid String
	 * @return  boolean
	 */
	public boolean isModelFolder(String folderOid);
}
