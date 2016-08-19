package com.bjsasc.adm.active.service.activedocumentservice;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.adm.active.model.activedocument.ActiveDocument;
import com.bjsasc.adm.active.model.activedocument.ActiveDocumentMaster;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;

/**
 * 现行文件服务接口。
 * 
 * @author yanjia 2013-5-13
 */
public interface ActiveDocumentService {
	
	/**
	 * 初始化现行文件对象
	 * 
	 * @param classId String
	 * @return ActiveDocument
	 */
	public ActiveDocument newActiveDocument(String classId);
	
	/**
	 * 初始化现行文件对象,指定初始状态
	 * 
	 * @param classId String
	 * @param checkoutState String
	 * @return ActiveDocument
	 */
	public ActiveDocument newActiveDocument(String classId, String checkoutState);

	/**
	 * 取得所有现行文件
	 * 
	 * @return List<ActiveDocument>
	 * */
	public List<ActiveDocument> getAllActiveDocument();

	/**
	 * 创建现行文件主对象
	 * 
	 * @return String
	 */
	public String createActiveDocumentMaster(String number, String name,
			String secLevel);

	/**
	 * 创建现行文件
	 * 
	 * @return String
	 */
	public String createActiveDocument(Map<String, String> paramMap);

	/**
	 * 删除现行文件
	 * 
	 * @return String
	 */
	public String deleteActiveDocument(String oids);

	/**
	 * 直接入库处理
	 * 
	 * @return String
	 */
	public String putIntoStorage(String oid);

	/**
	 * 更新现行文件
	 * 
	 * @return String
	 */
	public String updataActiveDocument(Map<String, String> paramMap);

	/**
	 * 更新现行文件
	 * 
	 * @return String
	 */
	public String updataActiveDocument(ActiveDocument obj);

	/**
	 * 更新现行文件主对象
	 * 
	 * @return String
	 */
	public String updataActiveDocumentMaster(ActiveDocumentMaster masterObj);

	/**
	 * 取得所有文件主对象
	 * 
	 * @return List<ActiveDocumentMaster>
	 * */
	public List<ActiveDocumentMaster> getAllActiveDocumentMaster();

	public List<ActiveDocument> getAllActiveDocumentquery(
			Map<String, String> map);

	/**
	 * Excel对象生成。
	 * 
	 * @param result
	 *            List<Map<String, Object>>
	 * @return HSSFWorkbook
	 */
	public HSSFWorkbook getExcelObject(List<Map<String, Object>> result,
			String changeClassId);

	/**
	 * 取得现行文件对象LIST
	 * 
	 * @return List<ActiveDocument>
	 * */
	public List<ActiveDocument> getActiveDocumentByNumber(String number);

	/**
	 * 删除相关LINK(ACTIVORDERLINK,ACTIVESETLINK)
	 * 
	 * @return List<ActiveDocumentMaster>
	 * */
	public void deleteLink(String oid);

	/**
	 * 更新现行数据
	 * 
	 * @param list
	 *            List<LifeCycleManaged>
	 * */
	public void updateActiveLife(List<LifeCycleManaged> list);
}
