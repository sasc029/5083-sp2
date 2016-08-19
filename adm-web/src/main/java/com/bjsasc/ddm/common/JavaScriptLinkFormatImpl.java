package com.bjsasc.ddm.common;

import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.List;

import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.type.attr.Attr;
import com.bjsasc.plm.type.formater.FormatService;
import com.bjsasc.plm.type.formater.Formater;
import com.bjsasc.plm.url.Url;
import com.cascc.avidm.login.model.PersonModel;

/**
 * 格式化程序。
 * Grid 数据执行 JavaScript 方法。
 * 
 * @author gengancong 2013-2-22
 */
public class JavaScriptLinkFormatImpl implements FormatService {

	/** formatter */

//	private static String formatter = "<a href=''#'' onclick=\"plm.showUrl(''{0}'');\" title=''{1}''>{1}</a>";

	//private static String formatter = "<a href=''#'' onclick=\"plm.OpenWindowAndReload(''{0}'',800,600,''{2}'');\" title=''{1}''>{1}</a>";
	//detail画面不能用ddm的js
//	private static String formatter = "<a href=''#'' onclick=\"ddm.tools.openWindow(''{0}'',800,600);\" title=''{1}''>{1}</a>";

// 	private static String formatter = "<a href=''#'' onclick=\"window.showModalDialog(''{0}'', ''self'', ''dialogHeight: 600px; dialogWidth: 800px;  center: Yes; help: no; resizable: NO; status: no; scroll: Yes'');\" title=''{1}''>{1}</a>";
	private static String formatter = "<a href=''#'' onclick=\"ddm.tools.showModalDialogReload(''{0}'')\" title=''{1}''>{1}</a>";

	/* （非 Javadoc）
	 * @see com.bjsasc.plm.type.formater.FormatService#format(java.lang.Object, com.bjsasc.plm.type.attr.Attr, com.bjsasc.plm.type.formater.Formater) 
	 */
	@SuppressWarnings("deprecation")
	public Object format(Object target, Attr attr, Formater formater) {
		List<String> sources = attr.getSources();
		Object result = null;
		if (sources.size() == 0) {
			return result;
		}
		//String domainRef = AAProvider.getAppRightsMgrService().getAADomainApp4TreeDataByAppRef(null, "plm").get(0).getDomainRef();
		String attrIdPath = sources.get(0);
		Object value = Helper.getTypeService().getAttrValue(attrIdPath, target);
		Object innerId = Helper.getTypeService().getAttrValue("innerId", target);
		Object classId = Helper.getTypeService().getAttrValue("classId", target);
		String oid = classId + ":" + innerId;
		String url = Url.APP + "/ddm/public/visitObject.jsp?OID=" + oid + "&classId=" + classId;
		if (value == null || "null".equals(value)) {
			return result;
		} else if(target instanceof DistributeObject){
			String dataClassId = ((DistributeObject) target).getDataClassId();
			String dataInnerId = ((DistributeObject) target).getDataInnerId();
			Persistable obj = Helper.getPersistService().getObject(dataClassId + ":" + dataInnerId);
			if (!"".equals(obj) && obj != null){
				result = MessageFormat.format(formatter, url, value, innerId);
			} else {
				PersonModel person = SessionHelper.getService().getPersonModel();
				String userIID = person.getUserIID();
				String accessUrl = URLDecoder.decode(((DistributeObject) target).getAccessUrl());
				accessUrl = accessUrl.replace("{userIID}", userIID);
				result = MessageFormat.format(formatter, accessUrl, value, innerId);
			}
		} else {
			result = MessageFormat.format(formatter, url, value, innerId);
		}
		return result;
	}
}
