package com.bjsasc.ddm.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.bjsasc.plm.core.util.ConfigFileUtil;
import com.cascc.avidm.util.SplitString;

/**
 * 读取配置文件名。
 * 
 * @author yanji 2013-5-10
 */
public class FileLoadUtil {
	private FileLoadUtil(){};
	/**  发放管理Log */
	private static final Logger LOG = Logger.getLogger(DdmSysConfigUtil.class);

	/** 发放管理数据转换文件配置文件路径 */
	private static final String TRANSFER_CONFIG_PATH = ConfigFileUtil.PLM_HOME_PATH + File.separator + "ddm"
			+ File.separator + "exchange" + File.separator + "standard";

	/**  发放管理数据转换文件类型 */
	public static String transfertype;

	static {
		transfertype = loadTransferType(TRANSFER_CONFIG_PATH);
	}

	public static String loadTransferType(String path) {
		LOG.debug("配置文件路径 " + path);
		File configDir = new File(path);
		File[] files = configDir.listFiles();
		List<String> typelist = new ArrayList<String>();
		for (File file : files) {
			try {
				InputStream is = new FileInputStream(file);
				SAXReader reader = new SAXReader();
				Document doc = reader.read(is);
				Element rootElement = doc.getRootElement();
				typelist.add(rootElement.attributeValue("type"));
			} catch (Exception e) {
				throw new RuntimeException("文件解析错误：" + file.getAbsolutePath(), e);
			}
		}
		return SplitString.list2Str(typelist, ",");
	}
}
