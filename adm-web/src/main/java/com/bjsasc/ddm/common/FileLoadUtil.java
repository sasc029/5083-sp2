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
 * ��ȡ�����ļ�����
 * 
 * @author yanji 2013-5-10
 */
public class FileLoadUtil {
	private FileLoadUtil(){};
	/**  ���Ź���Log */
	private static final Logger LOG = Logger.getLogger(DdmSysConfigUtil.class);

	/** ���Ź�������ת���ļ������ļ�·�� */
	private static final String TRANSFER_CONFIG_PATH = ConfigFileUtil.PLM_HOME_PATH + File.separator + "ddm"
			+ File.separator + "exchange" + File.separator + "standard";

	/**  ���Ź�������ת���ļ����� */
	public static String transfertype;

	static {
		transfertype = loadTransferType(TRANSFER_CONFIG_PATH);
	}

	public static String loadTransferType(String path) {
		LOG.debug("�����ļ�·�� " + path);
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
				throw new RuntimeException("�ļ���������" + file.getAbsolutePath(), e);
			}
		}
		return SplitString.list2Str(typelist, ",");
	}
}
