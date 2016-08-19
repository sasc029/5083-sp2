package com.bjsasc.adm.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.bjsasc.platform.objectmodel.managed.model.ClassAttrDef;
import com.bjsasc.plm.core.util.XmlFile;
import com.bjsasc.plm.core.util.XmlFileUtil;

/**
 * 现行文件模型加载。
 * 
 * @author gengancong 2013-07-01
 */
public class ActiveModelUtil {
	
	private ActiveModelUtil() {
		
	}

	public static List<ClassAttrDef> activeModels = new ArrayList<ClassAttrDef>();

	static {
		reloadConfig();
	}

	public static void reloadConfig() {
		activeModels.clear();

		List<XmlFile> xmlFiles = XmlFileUtil.loadByConfigPath("model");
		for (XmlFile xmlFile : xmlFiles) {
			Document doc = xmlFile.getDoc();
			loadConfig(doc, xmlFile.getFile().getPath());
		}
	}

	public static void loadConfig(Document doc, String filePath) {
		Element root = doc.getRootElement();

		for (Iterator<?> it = root.elementIterator("attr"); it.hasNext();) {
			Element node = (Element) it.next();
			ClassAttrDef attr = loadAttr(node);
			activeModels.add(attr);
		}
	}

	private static ClassAttrDef loadAttr(Element node) {
		String attrId = node.attributeValue("id");
		String attrName = node.attributeValue("name");
		String dataType = node.attributeValue("type");
		ClassAttrDef attr = new ClassAttrDef();
		attr.setAttrId(attrId);
		attr.setAttrName(attrName);
		attr.setDataType(dataType);
		return attr;
	}
}
