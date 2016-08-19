package com.bjsasc.ddm.distribute.aop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.system.access.AccessControlHelper;

/**
 * ���Ź���ģ�����Ȩ�޹��˲�ѯ����Ĺ�����
 * 
 * @author gaolingjie, 2013-4-1
 */
public class SearchResultInterceptor {

	private static final Logger LOG = Logger.getLogger(SearchResultInterceptor.class);

	/**
	 * ����Ȩ��ģ�����û���Ӧ��Ʒ��Ȩ�ޣ����˲�ѯ���
	 * 
	 * @param pjp
	 * @return ���˽��
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public Object filterSearchResult(ProceedingJoinPoint pjp) throws Exception {
		LOG.debug("���Ź���ģ�飬���˲�ѯ�����ʼ......");

		// ���������յķ���ֵ
		Object resultObject = null;

		try {
			// ��ú���ִ�к󷵻صĽ��
			Object methodExecuteRetValue = pjp.proceed();

			if (methodExecuteRetValue instanceof List) {
				// �����û��Ƿ�ӵ�з������ݵ�Ȩ�������˷���ִ�к󷵻ص�����
				List resultList = getObjectsByAccessPermission((List) methodExecuteRetValue);
				resultObject = resultList;
			} else {
				resultObject = methodExecuteRetValue;
			}

		} catch (Throwable e) {
			LOG.error("����Ȩ�޹������ݴ���" + e.getMessage());
			throw new Exception("����Ȩ�޹������ݴ���" + e.getMessage(), e);
		}

		LOG.debug("���Ź���ģ�飬���˲�ѯ�������......");
		return resultObject;
	}

	/**
	 * ��ȡ��ǰ�û����Է��ʵ����ݶ���
	 * 
	 * @param srcList
	 *            ����ִ�к�ķ���ֵ
	 * @return �����û�����Ȩ�޹��˺�ķ���ֵ
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List getObjectsByAccessPermission(List srcList) {
		if (null == srcList) {
			return null;
		}

		List retList = new ArrayList();
		for (Iterator it = srcList.iterator(); it.hasNext();) {
			Object obj = it.next();
			// ��鵱ǰ�û��Ƿ��з��ʶ����Ȩ��
			if (obj instanceof Domained && AccessControlHelper.getService().canAccess((Domained) obj)) {
				retList.add(obj);
			}
		}

		return retList;
	}
}
