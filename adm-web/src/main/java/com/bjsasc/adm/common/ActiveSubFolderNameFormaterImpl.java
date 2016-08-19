package com.bjsasc.adm.common;

import java.text.MessageFormat;
import java.util.List;

import com.bjsasc.plm.core.Helper;
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
public class ActiveSubFolderNameFormaterImpl implements FormatService {

	/** formatter */
	private static String formatter = "<a href=\"#\" onclick=\"plm.showUrl(''{0}'');\" title=\"{1}\">{1}</a>";

	@Override
	public Object format(Object target, Attr attr, Formater formater) {
		List<String> sources = attr.getSources();
		Object result = null;
		if (sources.isEmpty()) {
			return result;
		}

		String attrIdPath = sources.get(0);
		Object value = Helper.getTypeService().getAttrValue(attrIdPath, target);
		Object innerId = Helper.getTypeService().getAttrValue("innerId", target);
		Object classId = Helper.getTypeService().getAttrValue("classId", target);
		String oid = classId + ":" + innerId;
		String url = Url.APP + "/plm/common/visit/VisitObject.jsp?OID=" + oid;
		if (value == null || "null".equals(value)) {
			return result;
		} else {
			result = MessageFormat.format(formatter, url, value);
		}
		return result;
	}
}
