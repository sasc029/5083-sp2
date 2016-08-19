package com.bjsasc.ddm.common;

import java.text.MessageFormat;
import java.util.List;

import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.type.attr.Attr;
import com.bjsasc.plm.type.formater.FormatService;
import com.bjsasc.plm.type.formater.Formater;

/**
 * 格式化程序。
 * Grid 数据执行 JavaScript 方法。
 * 
 * @author gengancong 2013-2-22
 */
public class JavaScriptFormatImpl implements FormatService {

	/** formatter */
	private static String formatter = "<a href='#' onclick=\"javascript:doCustomizeMethod_{0}(''{1}'')\">{2}</a>";

	/* （非 Javadoc）
	 * @see com.bjsasc.plm.type.formater.FormatService#format(java.lang.Object, com.bjsasc.plm.type.attr.Attr, com.bjsasc.plm.type.formater.Formater)
	 */
	public Object format(Object target, Attr attr, Formater formater) {
		List<String> sources = attr.getSources();
		Object result = null;
		if (sources.size() == 0) {
			return result;
		}
		String attrIdPath = sources.get(0);
		Object value = Helper.getTypeService().getAttrValue(attrIdPath, target);
		Object innerId = Helper.getTypeService().getAttrValue("innerId", target);
		Object classId = Helper.getTypeService().getAttrValue("classId", target);
		String oid = classId + ":" + innerId;
		if (value == null || "null".equals(value)) {
			return result;
		} else {
			result = MessageFormat.format(formatter, attrIdPath, oid, value);
		}
		return result;
	}
}
