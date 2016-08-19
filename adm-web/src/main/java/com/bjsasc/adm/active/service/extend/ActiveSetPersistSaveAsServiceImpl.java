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
 * ���������Ϊʵ�֣���չPLM��װ��SaveAs�ӿ�
 * 
 * @author ������, 2013-09-24
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
			
			newDoc = AdmHelper.getActiveSetService().newActiveSet(doc.getClassId()); // ��ʼ��һ���°汾�ĵ�����
			newMaster = (ActiveSetMaster)newDoc.getMaster();
			
			ActiveSetMaster docMaster = (ActiveSetMaster) doc.getMaster();
			newMaster.setNumber(number); // �����±�ź�����
			newMaster.setName(name); // �����±�ź�����
			newMaster.setSecurityLevelInfo(docMaster.getSecurityLevelInfo());
			PersistHelper.getService().save(newMaster);

			// ������
			newDoc.setContextInfo(doc.getContextInfo());
			// ����Ϣ
			newDoc.setDomainInfo(doc.getDomainInfo());
			// ��ע
			newDoc.setNote(doc.getNote());
			// ��Դ
			newDoc.setDataSource(doc.getDataSource());
			// �����ļ����
			newDoc.setActiveDocumentNumber(doc.getActiveDocumentNumber());
			// ����
			newDoc.setActiveCode(doc.getActiveCode());
			// ҳ��
			newDoc.setPages(doc.getPages());
			// ����
			newDoc.setCount(doc.getCount());
			// ��������
			ActiveLifecycleService lifeService = AdmHelper
					.getActiveLifecycleService();
			lifeService.initLifecycle(newDoc);
			boolean isCheck = SessionHelper.getService().isCheckPermission();
			if(isCheck){
				SessionHelper.getService().setCheckPermission(!isCheck);
			}
			
			//�ر�Ȩ�޼��
			boolean checkPermission=SessionHelper.getService().isCheckPermission();
			SessionHelper.getService().setCheckPermission(false);
			
			// ������������
			LifeCycleTemplate template = Helper.getLifeCycleService().getLifeCycleTemplate((LifeCycleManaged) doc);
			Helper.getLifeCycleService().setLifeCycleTemplate((LifeCycleManaged) newDoc, template);
			//��Ȩ�޼��
			SessionHelper.getService().setCheckPermission(checkPermission);
			
			SessionHelper.getService().setCheckPermission(isCheck);
		}
		return newDoc;
	}
}
