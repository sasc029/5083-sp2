package com.bjsasc.ddm.transfer.exception;

import com.bjsasc.plm.core.util.PlmException;

/**
 * ����ת���ӿ��쳣��
 * 
 * @author gaolingjie, 2013-2-20
 */
public class DdmTransferException extends PlmException {

	private static final long serialVersionUID = 1277927894838026903L;

	
	public DdmTransferException(Throwable cause) {
		super("���Ź���ģ������ת�������쳣��" + cause.getMessage(), cause);
	}
	
	public DdmTransferException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
