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
	 * ��¼���Ź���ģ��action�������־
	 * @param pjp
	 * @param ex
	 */
	public void afterActionThrowing(JoinPoint pjp, Throwable ex){
		
		Object target = pjp.getTarget();
		Signature singnature = pjp.getSignature();
		
		Logger log = getLogger(target.getClass());

		StringBuilder logContent = new StringBuilder("ִ�к�����").append(
				singnature.getName()).append("()�����쳣������ԭ��").append(ex.getMessage());
		
		log.error(logContent.toString(), ex);
	}

	/**
	 * ִ�к���ʱ��¼����������Ϣ
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

		StringBuilder logContent = new StringBuilder("׼��ִ�к�����").append(
				signature.getName()).append("(");

		if (null != args) {
			for (Object o : args) {
				appendParamLog(logContent, o);
			}
		}

		logContent.append(")");

		// ��ʽ����־���ݣ�ȥ�������Ǹ�","
		String logStr = formatMethodLog(logContent);

		log.debug(logStr);

		try {
			log.debug("ִ��"+signature.getName()+"��ʼ......");
			//ִ�б����صĺ���������ִ�н��
			retValue = pjp.proceed();
			log.debug("ִ��"+signature.getName()+"����......");
		} catch (Throwable e) {
			log.error("ִ��"+signature.getName()+"�쳣��");
			//����ִ�д����쳣���������׳��������κδ���
			throw e;
		}
		return retValue;
	}

	/**
	 * ��ȡ���������ƺͲ�����ֵ����д����־��
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
	 * ��ʽ����־�������ȥ������","�� �磺testNoArgs(Integer=13,String=strValue,)
	 * ��ʽ����Ϊ��testNoArgs(Integer=13,String=strValue)
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
