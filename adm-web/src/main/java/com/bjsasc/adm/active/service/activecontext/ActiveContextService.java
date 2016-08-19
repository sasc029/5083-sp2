package com.bjsasc.adm.active.service.activecontext;

import java.util.List;

import com.bjsasc.adm.active.model.activecontext.ActiveContext;
import com.bjsasc.plm.core.context.ContextService;

/**
 * 现行文件服务接口。
 * 
 * @author gengancong 2013-07-01
 */
public interface ActiveContextService extends ContextService {

	/**
	 * 新建一个 ActiveContext(现行文件上下文)对象 
	 * @return 现行文件上下文对象
	 */
	public ActiveContext newActiveContext();

	/**
	 * 查询现行文件对象
	 * @return ActiveContext
	 */
	public ActiveContext getActiveContext();

	/**
	 * 创建现行文件上下文，如果现行文件上下文已经存在，则不会创建第二次
	 * @param context 待创建的现行文件上下文
	 * @return 现行文件上下文
	 */
	public ActiveContext createActiveContext();

	/**
	 * 查询现行文件对象
	 * @return List<ActiveContext>
	 */
	public List<ActiveContext> getAllActiveContext();
}
