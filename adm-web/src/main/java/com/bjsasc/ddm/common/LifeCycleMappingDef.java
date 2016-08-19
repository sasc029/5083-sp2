package com.bjsasc.ddm.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import com.bjsasc.plm.core.util.FileUtil;
import com.bjsasc.plm.core.util.XmlFileUtil;

/**
 * 发放管理生命周期映射定义文件读入类。
 * 
 * @author gengancong 2014-5-20
 */
public class LifeCycleMappingDef {
	public static Map<String, String> LifeCycleMappingDef = new HashMap<String, String>();

	public static Map<String, String> getLifeCycleMappingDef() {
		return LifeCycleMappingDef;
	}

	static {
		reloadConfig();
	}

	public static void reloadConfig() {
		LifeCycleMappingDef.clear();
		String filePattern = "/plm/ddm/ddmlifecycle/ddmLifeCycleMappingDef.xml";
		List<String> filePaths = FileUtil.getFilesByPattern(filePattern);
		if (filePaths != null && filePaths.size() > 0) {
			Document doc = XmlFileUtil.loadByFilePath(filePaths.get(0));
			loadConfig(doc);
		}
	}

	public static void loadConfig(Document doc) {
		Element root = doc.getRootElement();

		for (Iterator<?> it = root.elementIterator("state"); it.hasNext();) {
			Element node = (Element) it.next();

			String id = node.attributeValue("id");
			String value = node.attributeValue("value");
			LifeCycleMappingDef.put(id, value);
		}
	}
}
