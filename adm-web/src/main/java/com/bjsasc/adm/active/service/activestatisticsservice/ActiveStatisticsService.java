package com.bjsasc.adm.active.service.activestatisticsservice;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 统计与输出服务接口。
 * 
 * @author guowei 2013-5-30
 */
public interface ActiveStatisticsService {

	/**
	 * 取得模型id
	 * 
	 * @param oids
	 * @return
	 */
	public String getModelId(String oids);

	/**
	 * 取得模型相关属性
	 * 
	 * @param oids
	 * @return
	 */
	public List<Map<String, Object>> getClassAttrList(String oids);

	/**
	 * 取得统计数据
	 * 
	 * @param folderOids
	 * @param file
	 * @param stMap
	 * @param selList
	 * @param optList
	 * @return
	 */
	public List<Map<String, Object>> getActiveDocumentlist(String folderOids, String file,
			List<Map<String, Object>> stList, List<Map<String, String>> selList, List<Map<String, Object>> optList);

	public HSSFWorkbook getExcelObject(List<Map<String, Object>> listname, List<Map<String, Object>> listStatistic);
	
	public String  getOids(String folderOids);
}
