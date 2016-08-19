package com.bjsasc.ddm.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.bjsasc.platform.spring.loader.PlatformConfigFileLoader;

/**
 * ����ģ��������Spring������Ϣ��
 * 
 * @author gengancong 2013-2-22
 */
public class DdmSpringLoader extends PlatformConfigFileLoader {
	
	private static final String FILE_PATH = System.getProperty("AVIDM_HOME")
			+ File.separator + "springconfig" + File.separator;

	private static String[] fileNames = new String[] {};

	static {
		loadConfig();
	}

	/**
	 * �Զ���ȡspringconfigĿ¼������ddmǰ׺��xml�ļ�
	 */
	private static void loadConfig() {
		try {
			List<String> fileNameList = new ArrayList<String>();

			File folder = new File(FILE_PATH);
			String[] files = folder.list();
			for (String file : files) {
				if (file.startsWith("ddm") && file.endsWith(".xml")) {
					// ǰ׺Ϊddm��xml�ļ�
					fileNameList.add(FILE_PATH + file);
				}
			}

			fileNames = new String[fileNameList.size()];
			int i = 0;
			for (String fileName : fileNameList) {
				fileNames[i++] = fileName;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String[] getConfigFileLocation() {
		return fileNames;
	}
}
