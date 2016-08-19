package com.bjsasc.adm.common;

import java.text.MessageFormat;
import java.util.List;

import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.type.attr.Attr;
import com.bjsasc.plm.type.formater.FormatService;
import com.bjsasc.plm.type.formater.Formater;
import com.bjsasc.plm.url.Url;

/**
 * 格式化程序。
 * Grid 数据执行 JavaScript 方法。
 * 
 * @author gengancong 2013-2-22
 */
public class JavaScriptOpenFormatImpl implements FormatService {

	/** formatter */
	private static String formatter = "<a href=''#'' onclick=\"plm.OpenWindowAndReload(''{0}'',800,600,''{2}'');\" title=''{1}''>{1}</a>";

	@Override
	public Object format(Object target, Attr attr, Formater formater) {
		List<String> sources = attr.getSources();
		Object result = null;
		if (sources.isEmpty()) {
			return result;
		}

		String attrIdPath = sources.get(0);
		Object value = Helper.getTypeService().getAttrValue(attrIdPath, target);
		Object innerId = Helper.getTypeService().getAttrValue("itemId", target);
		Object classId = Helper.getTypeService().getAttrValue("itemClassId", target);
		Object propertiesUrl = Helper.getTypeService().getAttrValue("itemPropertiesUrl", target);
		String oid = classId + ":" + innerId;
		if (StringUtil.isNull(propertiesUrl)) {
			propertiesUrl = "/plm/common/visit.jsp";
		}
		String url = Url.APP + propertiesUrl + "?OID=" + oid;
		if (value == null || "null".equals(value)) {
			return result;
		} else {
			result = MessageFormat.format(formatter, url, value, innerId);
		}
		return result;
	}
}
