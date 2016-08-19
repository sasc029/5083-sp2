package com.bjsasc.adm.active.service.admmodelservice;

import java.util.Map;

/**
 * 现行文件模型服务接口。
 * 
 * @author yanjia 2013-5-13
 */
public interface AdmModelService {

	/**
	 * 同步，重读模型
	 * 
	 * @param spot
	 * @param tempOid
	 */
	public void synchronizeModel(String spot, String tempOid);

	/**
	 * 取得创建现行文件必要参数
	 * 
	 * @param requestFolderOid String 文件夹OID
	 * @return  result Map<String, String>
	 * 					类型  key:CLASSID value:String
	 * 					文件夹OID  key:FOLDEROID value:String
	 * 					文件夹PATH  key:FOLDERPATH value:String
	 * 					上下文OID  key:CONTEXTOID value:String
	 * 				    上下文NAME  key:CONTEXTNAME value:String
	 */
	public Map<String, String> getCreateActiveDocumentParam(String requestFolderOid);

	/**
	 * 递归取得模型
	 * 
	 * @param folderOid String
	 * @return  String
	 */
	public String getModelFolder(String folderOid);

	/**
	 * 取得文件夹相匹配的模型ID
	 * 
	 * @param folderOid String
	 * @return  String
	 */
	public String getModelId(String folderOid);

	/**
	 * 取得文件夹相匹配的模型名称
	 * 
	 * @param folderOid String
	 * @return  String
	 */
	public String getModelName(String folderOid);

	/**
	 * 取得模型对应文件信息
	 * 
	 * @param folderOid String
	 * @return  result Map<String, String>
	 * 					文件夹OID  key:FOLDEROID value:String
	 * 					文件夹PATH  key:FOLDERPATH value:String
	 */
	public Map<String, String> getFolderByModel(String classId);
	
	/**
	 * 文件夹是否有匹配模型
	 * 
	 * @param folderOid String
	 * @return  boolean
	 */
	public boolean isModelFolder(String folderOid);
}
