package com.bjsasc.ddm.distribute.aop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.system.access.AccessControlHelper;

/**
 * 发放管理模块根据权限过滤查询结果的过滤器
 * 
 * @author gaolingjie, 2013-4-1
 */
public class SearchResultInterceptor {

	private static final Logger LOG = Logger.getLogger(SearchResultInterceptor.class);

	/**
	 * 根据权限模型中用户对应产品的权限，过滤查询结果
	 * 
	 * @param pjp
	 * @return 过滤结果
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public Object filterSearchResult(ProceedingJoinPoint pjp) throws Exception {
		LOG.debug("发放管理模块，过滤查询结果开始......");

		// 拦截器最终的返回值
		Object resultObject = null;

		try {
			// 获得函数执行后返回的结果
			Object methodExecuteRetValue = pjp.proceed();

			if (methodExecuteRetValue instanceof List) {
				// 根据用户是否拥有访问数据的权限来过滤方法执行后返回的数据
				List resultList = getObjectsByAccessPermission((List) methodExecuteRetValue);
				resultObject = resultList;
			} else {
				resultObject = methodExecuteRetValue;
			}

		} catch (Throwable e) {
			LOG.error("根据权限过滤数据错误：" + e.getMessage());
			throw new Exception("根据权限过滤数据错误：" + e.getMessage(), e);
		}

		LOG.debug("发放管理模块，过滤查询结果结束......");
		return resultObject;
	}

	/**
	 * 获取当前用户可以访问的数据对象
	 * 
	 * @param srcList
	 *            方法执行后的返回值
	 * @return 根据用户访问权限过滤后的返回值
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List getObjectsByAccessPermission(List srcList) {
		if (null == srcList) {
			return null;
		}

		List retList = new ArrayList();
		for (Iterator it = srcList.iterator(); it.hasNext();) {
			Object obj = it.next();
			// 检查当前用户是否有访问对象的权限
			if (obj instanceof Domained && AccessControlHelper.getService().canAccess((Domained) obj)) {
				retList.add(obj);
			}
		}

		return retList;
	}
}
