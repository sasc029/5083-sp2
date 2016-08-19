package com.bjsasc.adm.active.service.extend;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.bjsasc.adm.active.model.activeset.ActiveSet;
import com.bjsasc.adm.active.model.activeset.ActiveSetMaster;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.persist.PersistInnerServiceImpl;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.system.access.AccessControlHelper;
import com.bjsasc.plm.core.system.access.operate.Operate;
import com.bjsasc.plm.core.util.LogUtil;
import com.bjsasc.plm.core.vc.model.Iterated;
import com.bjsasc.plm.core.vc.VersionControlHelper;

/**
 * 现行套增删改实现，扩展PLM已封装的实现
 * 
 * @author 耿安聪, 2013-09-25
 *
 */
public class ActiveSetPersistSUDServiceImpl extends PersistInnerServiceImpl {
	
	private static final Logger LOG = Logger.getLogger(ActiveSetPersistSUDServiceImpl.class); 
	
	/*
	 * (non-Javadoc)
	 * @see com.bjsasc.plm.core.persist.PersistInnerServiceImpl#save(java.lang.Object)
	 */
	public Serializable save(Object entity) {
		if(entity instanceof ActiveSet){
			ActiveSetMaster master = (ActiveSetMaster) ((ActiveSet)entity).getMaster();
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
		if(entity instanceof ActiveSet) {
			ActiveSet doc = (ActiveSet) entity;
			ActiveSetMaster master = (ActiveSetMaster) doc.getMaster();
			List<Iterated> list = VersionControlHelper.getService().allIterationsOf(doc.getMaster());
			for(Iterated iterated : list) {
				if (iterated instanceof Domained) {//校验版本分支每个对象的权限
					AccessControlHelper.getService().checkEntityPermission(Operate.DELETE, iterated);
				}
				ActiveSet tmpDoc = (ActiveSet)iterated;
				super.delete(tmpDoc, entitiesTogether); // 删除文档
			}
			super.delete(master, entitiesTogether); // 删除Master对象
		}
	}
	
}
