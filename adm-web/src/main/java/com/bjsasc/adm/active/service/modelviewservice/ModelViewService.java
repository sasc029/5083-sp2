package com.bjsasc.adm.active.service.modelviewservice;

import java.util.List;
import java.util.Map;

/**
 * ģ����ͼ����ӿڡ�
 * 
 * @author gengancong 2013-6-3
 */
public interface ModelViewService {

	/**
	 * ȡ����Ŀ¼
	 * 
	 * @param attr String
	 * @param keys String
	 * @return List
	 */
	public List<Map<String, Object>> getSubObject(String attr, String keys);

	/**
	 * ȡ�������ļ�
	 * 
	 * @param keys String
	 * @return List
	 */
	public List<Object> getActiveDocuments(String keys);
}
