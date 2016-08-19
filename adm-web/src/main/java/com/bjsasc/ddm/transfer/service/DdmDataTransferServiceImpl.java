package com.bjsasc.ddm.transfer.service;

import java.io.File;

import org.apache.log4j.Logger;

import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.transfer.exception.DdmTransferException;
import com.bjsasc.plm.core.exchange.TransferHelper;
import com.bjsasc.plm.core.util.ConfigFileUtil;

/**
 * 发放管理数据转换服务类
 * 
 * @author gaolingjie, 2013-2-27
 */
public class DdmDataTransferServiceImpl implements DdmDataTransferService {

	// 发放管理数据转换自定义转换视图名称
	private static final String DDM_TRANSFER_VIEW = "ddm_standard_view";

	// 发放管理模块配置信息根目录
	private static final String DDM_HOME_PATH = ConfigFileUtil.PLM_HOME_PATH
			+ File.separator + "ddm";

	// 发放管理数据转换文件配置文件目录
	private static final String TRANSFER_CONFIG_PATH = DDM_HOME_PATH
			+ File.separator + "exchange" + File.separator + "standard";

	private static final Logger LOG = Logger
			.getLogger(DdmDataTransferServiceImpl.class);

	static {
		try {
			loadTransferConfig();
		} catch (Exception e) {
			LOG.error("初始化发放管理数据转换配置配置异常：" + e.getMessage(), e);
		}
	}

	/**
	 * 读取数据转换配置文件
	 */
	public static void loadTransferConfig() {

		LOG.debug("开始读取发放管理数据转换配置文件，配置文件锁在的目录为：" + TRANSFER_CONFIG_PATH);

		File configDir = new File(TRANSFER_CONFIG_PATH);
		File[] files = configDir.listFiles();

		if (null == files || files.length == 0) {
			LOG.error("读取发放管理数据转换配置文件，返回的结果为空！");
		} else {
			LOG.error("读取发放管理数据转换配置文件，获取文件的个数为：" + files.length);
			TransferHelper.getService()
					.loadConfigFile(files, DDM_TRANSFER_VIEW);
		}
	}

	@Override
	public DistributeObject transferToDdmData(Object fromData)
			throws DdmTransferException {
		
		DistributeObject result = null;

		try {
			result = (DistributeObject) TransferHelper.getService().transfer(
					fromData, DDM_TRANSFER_VIEW, fromData.getClass().getName());
		} catch (Exception e) {
			String msg = getExceptionMsg(fromData, e);
			throw new DdmTransferException(msg, e);
		}

		return result;
	}

	/**
	 * 生成异常信息
	 * 
	 * @param fromData
	 *            要转换的数据
	 * @param cause
	 *            异常
	 * @return 异常信息
	 */
	private String getExceptionMsg(Object fromData, Throwable cause) {
		String msg = "发放管理模块数据转换出现异常。";
		if (null != fromData) {
			msg += "数据的类型为：" + fromData.getClass().getName() + "。异常原因："
					+ cause.getMessage();
		} else {
			msg += "异常原因：参数fromData为null！";
		}
		return msg;
	}
}
