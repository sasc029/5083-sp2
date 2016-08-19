package com.bjsasc.adm.active.action;

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

import com.bjsasc.platform.i18n.PLU;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.util.Ajax;
import com.bjsasc.ui.json.DataUtil;
import com.cascc.platform.util.PlatformException;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Struts2 Action 抽象类。
 * 
 * @author yanjia 2013-5-14
 */
public abstract class AbstractAction extends ActionSupport {

	/** serialVersionUID */
	private static final long serialVersionUID = 3088459021669331603L;

	private static final Logger LOG = Logger.getLogger(AbstractAction.class);

	/** OUTPUTDATA */
	protected static final String OUTPUTDATA = "outputData";
	protected static final String INITPAGE = "initPage";

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

	/**
	 * 输出结果。
	 */
	protected void mapToSimpleJson() {
		String json = DataUtil.mapToSimpleJson(result);
		ActionContext.getContext().put("data", json);
	}

	/**
	 * 输出结果。
	 */
	protected void listToJson() {
		String json = DataUtil.listToJson(listMap.size(), listMap);
		ActionContext.getContext().put("data", json);
	}

	/**
	 * 输出结果。
	 */
	protected void encode() {
		String json = DataUtil.encode(listMap);
		ActionContext.getContext().put("data", json);
	}

	/**
	 * 正常处理。
	 */
	protected void success() {
		result.put("success", "true");
	}

	/**
	 * 异常处理。
	 * 
	 * @param ex
	 *            异常
	 */
	protected void error(Exception ex) {
		request.setAttribute("javax_servlet_error_exception", ex);
		LOG.error("现行数据管理", ex);
	}

	/**
	 * 异常处理。
	 * 
	 * @param ex
	 *            异常
	 */
	protected void jsonError(Exception exception) {
		LOG.error("现行数据管理", exception);
		// 必不可少
		response.setStatus(HttpServletResponse.SC_OK);

		result.put(Ajax.SUCCESS, Ajax.FASLE);

		if (exception.getMessage() != null) {
			if (exception instanceof PlatformException) {
				PlatformException e = (PlatformException) exception;
				result.put(Ajax.CODE, e.getUniqueID());
				result.put(Ajax.MESSAGE,
						PLU.getErrorString(e.getUniqueID(), e.getParas()));
			} else {
				result.put(Ajax.MESSAGE, exception.getMessage());
			}
		} else {
			result.put(Ajax.MESSAGE, "未指定错误信息");
		}

		result.put(Ajax.TRACE, Ajax.getTrace(exception));
	}

	/**
	 * 从session中取得对象。
	 * 
	 * @param key
	 *            String
	 * @return String
	 */
	protected String getAttributeFromSession(String key) {
		return (String) session.getAttribute(key);
	}

	protected void exportExcel(HSSFWorkbook wBook, String filename) {
		// excel文件以单据编号命名
		String excelName = "";
		try {
			excelName = java.net.URLEncoder.encode(filename + ".xls", "UTF-8");
		} catch (UnsupportedEncodingException uee) {
			LOG.error("文件名错误", uee);
		}

		// excel输出
		response.setContentType("Application/vnd.ms-excel; charset=UTF-8");
		response.setHeader("Content-disposition", "attachment; filename="
				+ excelName);
		try {
			OutputStream fileOut = response.getOutputStream();
			wBook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (IOException e) {
			LOG.error("EXCEL输出错误", e);
		}
	}

	protected String getParameter(String key) {
		String parameter = request.getParameter(key);
		String encoding = StringUtil.encoding(parameter);
		return encoding;
	}

	protected User getCurrentUser() {
		// 取得当前用户信息。
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

	public Map<String, String> getParams(String[] keys) {
		Map<String, String> paramMap = new HashMap<String, String>();
		for (String key : keys) {
			String parameter = request.getParameter(key);
			if (parameter == null) {
				parameter = "";
			}
			paramMap.put(key, parameter);
		}
		return paramMap;
	}
}
