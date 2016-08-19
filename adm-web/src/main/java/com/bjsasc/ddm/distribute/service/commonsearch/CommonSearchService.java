package com.bjsasc.ddm.distribute.service.commonsearch;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * �ۺϲ�ѯ����ӿڡ�
 * 
 * @author gengancong 2013-2-22
 */
public interface CommonSearchService {

	/**
	 * Excel�������ɡ�
	 * 
	 * @param result List<Map<String, Object>>
	 * @return HSSFWorkbook
	 */
	public HSSFWorkbook getExcelObject(List<Map<String, Object>> result);

}
