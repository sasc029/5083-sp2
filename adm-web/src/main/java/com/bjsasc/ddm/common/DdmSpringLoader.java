package com.bjsasc.ddm.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.bjsasc.platform.spring.loader.PlatformConfigFileLoader;

/**
 * 核心模块依赖的Spring配置信息。
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
	 * 自动读取springconfig目录下所有ddm前缀的xml文件
	 */
	private static void loadConfig() {
		try {
			List<String> fileNameList = new ArrayList<String>();

			File folder = new File(FILE_PATH);
			String[] files = folder.list();
			for (String file : files) {
				if (file.startsWith("ddm") && file.endsWith(".xml")) {
					// 前缀为ddm的xml文件
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
