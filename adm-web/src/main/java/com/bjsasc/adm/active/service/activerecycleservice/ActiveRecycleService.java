package com.bjsasc.adm.active.service.activerecycleservice;

import java.util.List;

import com.bjsasc.adm.active.model.Recycledable;
import com.bjsasc.adm.active.model.activerecycle.ActiveRecycle;
import com.bjsasc.plm.core.persist.model.Persistable;

/**
 * ����վ����ӿڡ�
 * 
 * @author gengancong 2013-6-3
 */
public interface ActiveRecycleService {

	/**
	 * ��ѯ����վ����
	 * @return List<ActiveRecycle>
	 */
	public List<ActiveRecycle> listItems();

	/**
	 * ��Ӷ������վ
	 * @param oids
	 */
	public void addltem(String oids);

	/**
	 * ��Ӷ������վ
	 * @param admRecycled
	 */
	public void addltem(Recycledable admRecycled);

	/**
	 * ɾ������
	 * @param recycleOids
	 */
	public void deleteItems(String recycleOids);

	/**
	 * ɾ������վ�����е�����
	 */
	public void clearItems();

	/**
	 * ��ԭ����
	 * @param recycleOids
	 */
	public void reductionItems(String recycleOids);

	/**
	 * ��ԭ����
	 */
	public void reductionItems(List<ActiveRecycle> recycleObjects);

	/**
	 * ��ԭ����
	 */
	public void reductionItems(List<ActiveRecycle> recycleObjects, List<Persistable> targetFolders);
}
