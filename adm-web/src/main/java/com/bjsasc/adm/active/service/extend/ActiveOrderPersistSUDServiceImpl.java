package com.bjsasc.adm.active.service.extend;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.bjsasc.adm.active.model.activeorder.ActiveOrder;
import com.bjsasc.adm.active.model.activeorder.ActiveOrderMaster;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.attachment.AttachHelper;
import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.persist.PersistInnerServiceImpl;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.system.access.AccessControlHelper;
import com.bjsasc.plm.core.system.access.operate.Operate;
import com.bjsasc.plm.core.util.LogUtil;
import com.bjsasc.plm.core.vc.model.Iterated;
import com.bjsasc.plm.core.vc.VersionControlHelper;

/**
 * ���е�����ɾ��ʵ�֣���չPLM�ѷ�װ��ʵ��
 * 
 * @author ������, 2013-09-25
 *
 */
public class ActiveOrderPersistSUDServiceImpl extends PersistInnerServiceImpl {
	
	private static final Logger LOG = Logger.getLogger(ActiveOrderPersistSUDServiceImpl.class); 
	
	/*
	 * (non-Javadoc)
	 * @see com.bjsasc.plm.core.persist.PersistInnerServiceImpl#save(java.lang.Object)
	 */
	public Serializable save(Object entity) {
		if(entity instanceof ActiveOrder){
			ActiveOrderMaster master = (ActiveOrderMaster) ((ActiveOrder)entity).getMaster();
			Persistable obj = Helper.getPersistService().getObject(master.getClassId(), master.getInnerId());
			if(obj == null){
				Helper.getPersistService().save(master);
			}
			return super.save(entity);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.bjsasc.plm.core.persist.PersistSUDServiceImpl#delete(java.lang.Object)
	 */
	public void delete(Object entity, Collection<?> entitiesTogether) {
		LOG.debug(LogUtil.beginMethod(entity));
		if(entity instanceof ActiveOrder) {
			ActiveOrder doc = (ActiveOrder) entity;
			ActiveOrderMaster master = (ActiveOrderMaster) doc.getMaster();
			List<Iterated> list = VersionControlHelper.getService().allIterationsOf(doc.getMaster());
			for(Iterated iterated : list) {
				if (iterated instanceof Domained) {//У��汾��֧ÿ�������Ȩ��
					AccessControlHelper.getService().checkEntityPermission(Operate.DELETE, iterated);
				}
				ActiveOrder tmpDoc = (ActiveOrder)iterated;
				//ɾ���ļ�
				AttachHelper.getAttachService().deleteAllPtFileItemBean(tmpDoc);
				super.delete(tmpDoc, entitiesTogether); // ɾ���ĵ�
			}
			super.delete(master, entitiesTogether); // ɾ��Master����
		}
	}
	
}
