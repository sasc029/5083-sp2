package com.bjsasc.adm.active.service.activerecycleservice;

import java.util.List;

import com.bjsasc.adm.active.model.Recycledable;
import com.bjsasc.adm.active.model.activerecycle.ActiveRecycle;
import com.bjsasc.plm.core.persist.model.Persistable;

/**
 * 回收站服务接口。
 * 
 * @author gengancong 2013-6-3
 */
public interface ActiveRecycleService {

	/**
	 * 查询回收站对象
	 * @return List<ActiveRecycle>
	 */
	public List<ActiveRecycle> listItems();

	/**
	 * 添加对象回收站
	 * @param oids
	 */
	public void addltem(String oids);

	/**
	 * 添加对象回收站
	 * @param admRecycled
	 */
	public void addltem(Recycledable admRecycled);

	/**
	 * 删除对象
	 * @param recycleOids
	 */
	public void deleteItems(String recycleOids);

	/**
	 * 删除回收站中所有的数据
	 */
	public void clearItems();

	/**
	 * 还原对象
	 * @param recycleOids
	 */
	public void reductionItems(String recycleOids);

	/**
	 * 还原对象
	 */
	public void reductionItems(List<ActiveRecycle> recycleObjects);

	/**
	 * 还原对象
	 */
	public void reductionItems(List<ActiveRecycle> recycleObjects, List<Persistable> targetFolders);
}
