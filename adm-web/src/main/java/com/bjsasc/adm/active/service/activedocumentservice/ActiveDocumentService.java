package com.bjsasc.adm.active.service.activedocumentservice;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.adm.active.model.activedocument.ActiveDocument;
import com.bjsasc.adm.active.model.activedocument.ActiveDocumentMaster;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;

/**
 * �����ļ�����ӿڡ�
 * 
 * @author yanjia 2013-5-13
 */
public interface ActiveDocumentService {
	
	/**
	 * ��ʼ�������ļ�����
	 * 
	 * @param classId String
	 * @return ActiveDocument
	 */
	public ActiveDocument newActiveDocument(String classId);
	
	/**
	 * ��ʼ�������ļ�����,ָ����ʼ״̬
	 * 
	 * @param classId String
	 * @param checkoutState String
	 * @return ActiveDocument
	 */
	public ActiveDocument newActiveDocument(String classId, String checkoutState);

	/**
	 * ȡ�����������ļ�
	 * 
	 * @return List<ActiveDocument>
	 * */
	public List<ActiveDocument> getAllActiveDocument();

	/**
	 * ���������ļ�������
	 * 
	 * @return String
	 */
	public String createActiveDocumentMaster(String number, String name,
			String secLevel);

	/**
	 * ���������ļ�
	 * 
	 * @return String
	 */
	public String createActiveDocument(Map<String, String> paramMap);

	/**
	 * ɾ�������ļ�
	 * 
	 * @return String
	 */
	public String deleteActiveDocument(String oids);

	/**
	 * ֱ����⴦��
	 * 
	 * @return String
	 */
	public String putIntoStorage(String oid);

	/**
	 * ���������ļ�
	 * 
	 * @return String
	 */
	public String updataActiveDocument(Map<String, String> paramMap);

	/**
	 * ���������ļ�
	 * 
	 * @return String
	 */
	public String updataActiveDocument(ActiveDocument obj);

	/**
	 * ���������ļ�������
	 * 
	 * @return String
	 */
	public String updataActiveDocumentMaster(ActiveDocumentMaster masterObj);

	/**
	 * ȡ�������ļ�������
	 * 
	 * @return List<ActiveDocumentMaster>
	 * */
	public List<ActiveDocumentMaster> getAllActiveDocumentMaster();

	public List<ActiveDocument> getAllActiveDocumentquery(
			Map<String, String> map);

	/**
	 * Excel�������ɡ�
	 * 
	 * @param result
	 *            List<Map<String, Object>>
	 * @return HSSFWorkbook
	 */
	public HSSFWorkbook getExcelObject(List<Map<String, Object>> result,
			String changeClassId);

	/**
	 * ȡ�������ļ�����LIST
	 * 
	 * @return List<ActiveDocument>
	 * */
	public List<ActiveDocument> getActiveDocumentByNumber(String number);

	/**
	 * ɾ�����LINK(ACTIVORDERLINK,ACTIVESETLINK)
	 * 
	 * @return List<ActiveDocumentMaster>
	 * */
	public void deleteLink(String oid);

	/**
	 * ������������
	 * 
	 * @param list
	 *            List<LifeCycleManaged>
	 * */
	public void updateActiveLife(List<LifeCycleManaged> list);
}
