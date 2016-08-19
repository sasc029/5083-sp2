package com.bjsasc.ddm.distribute.model;

/**
 * 发放管理模块记录日志的接口服务
 * 
 * @author gaolingjie, 2013-4-17
 */
public interface DdmLogged {
	/**
	 * 获取打印日志时的输出信息
	 * 
	 * @return 打印日志时的输出信息
	 */
	public String getLogString();
}
