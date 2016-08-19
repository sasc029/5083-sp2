package com.bjsasc.ddm.transfer.service;

import java.io.File;

import org.apache.log4j.Logger;

import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.transfer.exception.DdmTransferException;
import com.bjsasc.plm.core.exchange.TransferHelper;
import com.bjsasc.plm.core.util.ConfigFileUtil;

/**
 * ���Ź�������ת��������
 * 
 * @author gaolingjie, 2013-2-27
 */
public class DdmDataTransferServiceImpl implements DdmDataTransferService {

	// ���Ź�������ת���Զ���ת����ͼ����
	private static final String DDM_TRANSFER_VIEW = "ddm_standard_view";

	// ���Ź���ģ��������Ϣ��Ŀ¼
	private static final String DDM_HOME_PATH = ConfigFileUtil.PLM_HOME_PATH
			+ File.separator + "ddm";

	// ���Ź�������ת���ļ������ļ�Ŀ¼
	private static final String TRANSFER_CONFIG_PATH = DDM_HOME_PATH
			+ File.separator + "exchange" + File.separator + "standard";

	private static final Logger LOG = Logger
			.getLogger(DdmDataTransferServiceImpl.class);

	static {
		try {
			loadTransferConfig();
		} catch (Exception e) {
			LOG.error("��ʼ�����Ź�������ת�����������쳣��" + e.getMessage(), e);
		}
	}

	/**
	 * ��ȡ����ת�������ļ�
	 */
	public static void loadTransferConfig() {

		LOG.debug("��ʼ��ȡ���Ź�������ת�������ļ��������ļ����ڵ�Ŀ¼Ϊ��" + TRANSFER_CONFIG_PATH);

		File configDir = new File(TRANSFER_CONFIG_PATH);
		File[] files = configDir.listFiles();

		if (null == files || files.length == 0) {
			LOG.error("��ȡ���Ź�������ת�������ļ������صĽ��Ϊ�գ�");
		} else {
			LOG.error("��ȡ���Ź�������ת�������ļ�����ȡ�ļ��ĸ���Ϊ��" + files.length);
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
	 * �����쳣��Ϣ
	 * 
	 * @param fromData
	 *            Ҫת��������
	 * @param cause
	 *            �쳣
	 * @return �쳣��Ϣ
	 */
	private String getExceptionMsg(Object fromData, Throwable cause) {
		String msg = "���Ź���ģ������ת�������쳣��";
		if (null != fromData) {
			msg += "���ݵ�����Ϊ��" + fromData.getClass().getName() + "���쳣ԭ��"
					+ cause.getMessage();
		} else {
			msg += "�쳣ԭ�򣺲���fromDataΪnull��";
		}
		return msg;
	}
}
