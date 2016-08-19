package com.bjsasc.adm.active.service.modelviewservice;

import java.util.List;
import java.util.Map;

/**
 * 模型视图服务接口。
 * 
 * @author gengancong 2013-6-3
 */
public interface ModelViewService {

	/**
	 * 取得子目录
	 * 
	 * @param attr String
	 * @param keys String
	 * @return List
	 */
	public List<Map<String, Object>> getSubObject(String attr, String keys);

	/**
	 * 取得现行文件
	 * 
	 * @param keys String
	 * @return List
	 */
	public List<Object> getActiveDocuments(String keys);
}
