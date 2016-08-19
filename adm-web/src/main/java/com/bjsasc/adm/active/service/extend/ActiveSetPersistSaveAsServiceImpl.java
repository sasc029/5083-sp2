package com.bjsasc.adm.active.service.extend;

import org.apache.log4j.Logger;

import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.model.activeset.ActiveSet;
import com.bjsasc.adm.active.model.activeset.ActiveSetMaster;
import com.bjsasc.adm.active.service.activelifecycle.ActiveLifecycleService;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleTemplate;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.saveas.SaveasServiceDefaultImpl;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.util.LogUtil;

/**
 * 现行套另存为实现，扩展PLM封装的SaveAs接口
 * 
 * @author 耿安聪, 2013-09-24
 *
 */
public class ActiveSetPersistSaveAsServiceImpl extends SaveasServiceDefaultImpl {

	private static final Logger LOG = Logger.getLogger(ActiveSetPersistSaveAsServiceImpl.class);
	
	/* (non-Javadoc)
	 * @see com.bjsasc.plm.core.persist.PersistSaveAsServiceImpl#saveAs(com.bjsasc.plm.core.persist.Persistable, com.bjsasc.plm.core.identifier.Identifier)
	 */
	public Persistable saveAs(Persistable obj, String number, String name) {
		
		LOG.debug(LogUtil.beginMethod(obj, number));
		
		ActiveSet doc = null;
		ActiveSetMaster newMaster = null;
		ActiveSet newDoc = null;
		if(obj instanceof ActiveSet) {
			doc = (ActiveSet)obj;
			
			newDoc = AdmHelper.getActiveSetService().newActiveSet(doc.getClassId()); // 初始化一个新版本文档对象
			newMaster = (ActiveSetMaster)newDoc.getMaster();
			
			ActiveSetMaster docMaster = (ActiveSetMaster) doc.getMaster();
			newMaster.setNumber(number); // 设置新编号和名称
			newMaster.setName(name); // 设置新编号和名称
			newMaster.setSecurityLevelInfo(docMaster.getSecurityLevelInfo());
			PersistHelper.getService().save(newMaster);

			// 上下文
			newDoc.setContextInfo(doc.getContextInfo());
			// 域信息
			newDoc.setDomainInfo(doc.getDomainInfo());
			// 备注
			newDoc.setNote(doc.getNote());
			// 来源
			newDoc.setDataSource(doc.getDataSource());
			// 现行文件编号
			newDoc.setActiveDocumentNumber(doc.getActiveDocumentNumber());
			// 代号
			newDoc.setActiveCode(doc.getActiveCode());
			// 页数
			newDoc.setPages(doc.getPages());
			// 份数
			newDoc.setCount(doc.getCount());
			// 生命周期
			ActiveLifecycleService lifeService = AdmHelper
					.getActiveLifecycleService();
			lifeService.initLifecycle(newDoc);
			boolean isCheck = SessionHelper.getService().isCheckPermission();
			if(isCheck){
				SessionHelper.getService().setCheckPermission(!isCheck);
			}
			
			//关闭权限检查
			boolean checkPermission=SessionHelper.getService().isCheckPermission();
			SessionHelper.getService().setCheckPermission(false);
			
			// 设置生命周期
			LifeCycleTemplate template = Helper.getLifeCycleService().getLifeCycleTemplate((LifeCycleManaged) doc);
			Helper.getLifeCycleService().setLifeCycleTemplate((LifeCycleManaged) newDoc, template);
			//打开权限检查
			SessionHelper.getService().setCheckPermission(checkPermission);
			
			SessionHelper.getService().setCheckPermission(isCheck);
		}
		return newDoc;
	}
}
