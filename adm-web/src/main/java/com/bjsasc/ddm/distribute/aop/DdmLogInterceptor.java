package com.bjsasc.ddm.distribute.aop;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;

import com.bjsasc.ddm.distribute.model.DdmLogged;

public class DdmLogInterceptor {

	private static final Map<String, Logger> LOGCONTAINER = new HashMap<String, Logger>();
	
	/**
	 * 记录发放管理模块action层错误日志
	 * @param pjp
	 * @param ex
	 */
	public void afterActionThrowing(JoinPoint pjp, Throwable ex){
		
		Object target = pjp.getTarget();
		Signature singnature = pjp.getSignature();
		
		Logger log = getLogger(target.getClass());

		StringBuilder logContent = new StringBuilder("执行函数：").append(
				singnature.getName()).append("()出现异常。错误原因：").append(ex.getMessage());
		
		log.error(logContent.toString(), ex);
	}

	/**
	 * 执行函数时记录函数参数信息
	 * 
	 * @param pjp
	 * @throws Throwable 
	 */
	public Object writeMethodParamsLog(ProceedingJoinPoint pjp) throws Throwable {
		Object retValue = null;
		Object target = pjp.getTarget();
		Signature signature = pjp.getSignature();
		Object[] args = pjp.getArgs();

		Logger log = getLogger(target.getClass());

		StringBuilder logContent = new StringBuilder("准备执行函数：").append(
				signature.getName()).append("(");

		if (null != args) {
			for (Object o : args) {
				appendParamLog(logContent, o);
			}
		}

		logContent.append(")");

		// 格式化日志内容，去掉最后的那个","
		String logStr = formatMethodLog(logContent);

		log.debug(logStr);

		try {
			log.debug("执行"+signature.getName()+"开始......");
			//执行被拦截的函数，返回执行结果
			retValue = pjp.proceed();
			log.debug("执行"+signature.getName()+"结束......");
		} catch (Throwable e) {
			log.error("执行"+signature.getName()+"异常！");
			//函数执行错误，异常继续往外抛出，不做任何处理
			throw e;
		}
		return retValue;
	}

	/**
	 * 获取参数的名称和参数的值，并写入日志中
	 * 
	 * @param logContent
	 * @param param
	 */
	private void appendParamLog(StringBuilder logContent, Object param) {
		if (null != param && param instanceof DdmLogged) {
			logContent.append(param.getClass().getName()).append("=")
					.append(((DdmLogged) param).getLogString()).append(",");
		} else if (null != param) {
			logContent.append(param.getClass().getSimpleName()).append("=")
					.append(param).append(",");
		} else {
			logContent.append(param);
		}
	}

	/**
	 * 格式化日志的输出，去掉最后的","。 如：testNoArgs(Integer=13,String=strValue,)
	 * 格式化后为：testNoArgs(Integer=13,String=strValue)
	 * 
	 * @param logContent
	 * @return
	 */
	private String formatMethodLog(StringBuilder logContent) {
		String logStr = null;
		if (logContent.indexOf(",)") != -1) {
			logStr = logContent.substring(0, logContent.lastIndexOf(",)"))
					+ ")";
		} else {
			logStr = logContent.toString();
		}
		return logStr;
	}

	private synchronized <T> Logger getLogger(Class<T> targetClazz) {
		Logger log = LOGCONTAINER.get(targetClazz.getName());
		if (null == log) {
			log = Logger.getLogger(targetClazz);
			LOGCONTAINER.put(targetClazz.getName(), log);
		}
		return log;
	}
}
