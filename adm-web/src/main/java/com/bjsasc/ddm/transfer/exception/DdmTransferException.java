package com.bjsasc.ddm.transfer.exception;

import com.bjsasc.plm.core.util.PlmException;

/**
 * 数据转换接口异常类
 * 
 * @author gaolingjie, 2013-2-20
 */
public class DdmTransferException extends PlmException {

	private static final long serialVersionUID = 1277927894838026903L;

	
	public DdmTransferException(Throwable cause) {
		super("发放管理模块数据转换出现异常：" + cause.getMessage(), cause);
	}
	
	public DdmTransferException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
