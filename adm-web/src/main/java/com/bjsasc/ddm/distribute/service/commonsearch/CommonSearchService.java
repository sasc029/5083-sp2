package com.bjsasc.ddm.distribute.service.commonsearch;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 综合查询服务接口。
 * 
 * @author gengancong 2013-2-22
 */
public interface CommonSearchService {

	/**
	 * Excel对象生成。
	 * 
	 * @param result List<Map<String, Object>>
	 * @return HSSFWorkbook
	 */
	public HSSFWorkbook getExcelObject(List<Map<String, Object>> result);

}
