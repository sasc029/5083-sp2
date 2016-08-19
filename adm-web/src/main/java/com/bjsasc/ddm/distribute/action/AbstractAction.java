package com.bjsasc.ddm.distribute.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.bjsasc.ddm.common.CheckPermission;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.service.distributeobjecttype.DistributeObjectTypeService;
import com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService;
import com.bjsasc.platform.i18n.PLU;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.util.Ajax;
import com.bjsasc.ui.json.DataUtil;
import com.bjsasc.plm.core.util.JsonUtil;
import com.cascc.platform.util.PlatformException;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Struts2 Action �����ࡣ
 * 
 * @author gengancong 2013-2-22
 */
public abstract class AbstractAction extends ActionSupport {

	/** serialVersionUID */
	private static final long serialVersionUID = 2748600176244875368L;
	/** OUTPUTDATA */
	protected static final String OUTPUTDATA = "outputData";
	protected static final String INITPAGE = "initPage";
	private static final  Logger LOG = Logger.getLogger(AbstractAction.class);

	/** request */
	protected HttpServletRequest request = ServletActionContext.getRequest();
	/** response */
	protected HttpServletResponse response = ServletActionContext.getResponse();
	/** response */
	protected HttpSession session = request.getSession();
	/** result */
	protected Map<String, String> result = new HashMap<String, String>();
	/** listMap */
	protected List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();

	DistributeObjectTypeService dots = DistributeHelper.getDistributeObjectTypeService();
	DistributeOrderService dos = DistributeHelper.getDistributeOrderService();
	
	/**
	 * ��������
	 */
	protected void mapToSimpleJson() {
		String json = DataUtil.mapToSimpleJson(result);
		ActionContext.getContext().put("data", json);
	}

	/**
	 * ��������
	 */
	protected void listToJson() {
		String json = DataUtil.listToJson(listMap.size(), listMap);
		ActionContext.getContext().put("data", json);
	}

	/**
	 * ��������
	 */
	protected void encode() {
		//String json = DataUtil.encode(listMap);
		String json = JsonUtil.listToJson(listMap);
		ActionContext.getContext().put("data", json);
	}

	/**
	 * ��������
	 */
	protected void success() {
		result.put("success", "true");
	}

	/**
	 * �쳣����
	 * 
	 * @param ex �쳣
	 */
	protected void error(Exception ex) {
		request.setAttribute("javax_servlet_error_exception", ex);
		LOG.error("���Ź���", ex);
	}

	/**
	 * �쳣����
	 * 
	 * @param ex �쳣
	 */
	protected void jsonError(Exception exception) {
		LOG.error("���Ź���", exception);
		//�ز�����
		response.setStatus(HttpServletResponse.SC_OK); 

		exception.printStackTrace();

		result.put(Ajax.SUCCESS, Ajax.FASLE);
		
		if(exception.getMessage() != null){
			if(exception instanceof PlatformException){
				PlatformException e = (PlatformException)exception;
				result.put(Ajax.CODE, e.getUniqueID());
				result.put(Ajax.MESSAGE, PLU.getErrorString(e.getUniqueID(), e.getParas()));
			}else{
				result.put(Ajax.MESSAGE, exception.getMessage());
			}
		}else{
			result.put(Ajax.MESSAGE, "δָ��������Ϣ");
		}
			
		result.put(Ajax.TRACE, Ajax.getTrace(exception));
	}

	/**
	 * ��session��ȡ�ö���
	 * 
	 * @param key String
	 * @return String
	 */
	protected String getAttributeFromSession(String key) {
		return (String) session.getAttribute(key);
	}

	protected void exportExcel(HSSFWorkbook wBook, String filename) {
		// excel�ļ��Ե��ݱ������
		String excelName = "";
		try {
			excelName = java.net.URLEncoder.encode(filename + ".xls", "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		// excel���
		response.setContentType("Application/vnd.ms-excel; charset=UTF-8");
		response.setHeader("Content-disposition", "attachment; filename=" + excelName);
		try {
			OutputStream fileOut = response.getOutputStream();
			wBook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected String getParameter(String key) {
		String parameter = request.getParameter(key);
		String encoding = StringUtil.encoding(parameter);
		return encoding;
	}

	protected User getCurrentUser() {
		// ȡ�õ�ǰ�û���Ϣ��
		User currentUser = SessionHelper.getService().getUser();
		return currentUser;
	}

	protected int getDataSize() {
		if (listMap != null) {
			return listMap.size();
		}
		return 0;
	}
	protected int getDataSize(List<?> obj) {
		if (obj == null || obj.isEmpty()) {
			return 0;
		} 
		return obj.size();
	}
	
	/**
	 * ������֤Ȩ��
	 * @param forCheckObj ���ݶ���
	 * @param spot ������ʾ����
	 * @param spotInstance ������ʾ����
	 * @return Ȩ����֤�����
	 */
	protected List<Object> checkPermission(List listObj, String spot, String spotInstance) {
		List<Object> forCheckObj = CheckPermission.checkPermission(listObj, spot, spotInstance);
		return forCheckObj;
	}
	
	/**
	 * ������֤Ȩ�� & Ĭ�ϸ�ʽ��
	 * @param forCheckObj ���ݶ���
	 * @param spot ������ʾ����
	 * @param spotInstance ������ʾ����
	 * @return Ȩ����֤�����
	 */
	protected List<Map<String, Object>> checkPermissionAF(List listObj, String spot, String spotInstance) {
		List<Map<String, Object>> list = CheckPermission.checkPermissionAF(listObj, spot, spotInstance);
		return list;
	}
}
