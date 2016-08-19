package com.bjsasc.adm.active.service.extend;

import org.apache.log4j.Logger;

import com.bjsasc.adm.active.model.activedocument.ActiveDocument;
import com.bjsasc.adm.active.model.activedocument.ActiveDocumentMaster;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleTemplate;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.attachment.AttachHelper;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.util.LogUtil;
import com.bjsasc.plm.core.vc.VersionServiceImpl;
import com.bjsasc.plm.core.vc.model.Versioned;

/**
 * �ĵ��汾ҵ���߼�����ʵ��
 * 
 * @author ţ����, 2011-11-16
 *
 */
public class ActiveDocumentVersionServiceImpl extends VersionServiceImpl {

	private static final Logger LOGGER = Logger.getLogger(ActiveDocumentVersionServiceImpl.class);

	/* (non-Javadoc)
	 * @see com.bjsasc.plm.core.vc.VersionServiceImpl#newVersion(com.bjsasc.plm.core.vc.Versioned, java.lang.String, java.lang.String)
	 */
	public Versioned newVersion(Versioned version, String newVersionNo, String newIterationNo) {

		LOGGER.debug(LogUtil.beginMethod(version, newVersionNo, newIterationNo));

		Versioned reviseDoc = null;
		if (version instanceof ActiveDocument) {
			// �޶�
			boolean isCheck = SessionHelper.getService().isCheckPermission();
			if (isCheck) {
				SessionHelper.getService().setCheckPermission(!isCheck);
			}

			reviseDoc = super.newVersion(version, newVersionNo, newIterationNo);

			LifeCycleTemplate template = Helper.getLifeCycleService().getLifeCycleTemplate((LifeCycleManaged) version);
			Helper.getLifeCycleService().setLifeCycleTemplate((LifeCycleManaged) reviseDoc, template);
			//�޸�master
			ActiveDocumentMaster newMaster = (ActiveDocumentMaster) reviseDoc.getMaster();
			ActiveDocumentMaster oldMaster = (ActiveDocumentMaster) version.getMaster();
			newMaster.setSecurityLevelInfo(oldMaster.getSecurityLevelInfo());
			PersistHelper.getService().modify(newMaster);

			// ��ע
			((ActiveDocument) reviseDoc).setNote(((ActiveDocument) version).getNote());
			// ��Դ
			((ActiveDocument) reviseDoc).setDataSource(((ActiveDocument) version).getDataSource());
			// ����
			((ActiveDocument) reviseDoc).setActiveCode(((ActiveDocument) version).getActiveCode());
			// ҳ��
			((ActiveDocument) reviseDoc).setPages(((ActiveDocument) version).getPages());
			// ����
			((ActiveDocument) reviseDoc).setCount(((ActiveDocument) version).getCount());
			// ����
			((ActiveDocument) reviseDoc).setAuthorName(((ActiveDocument) version).getAuthorName());
			// ���ߵ�λ
			((ActiveDocument) reviseDoc).setAuthorUnit(((ActiveDocument) version).getAuthorUnit());
			// ����ʱ��
			((ActiveDocument) reviseDoc).setAuthorTime(((ActiveDocument) version).getAuthorTime());

			AttachHelper.getAttachService().copyFile((ActiveDocument) version, (ActiveDocument) reviseDoc);
			PersistHelper.getService().modify(reviseDoc);

			SessionHelper.getService().setCheckPermission(isCheck);
		}
		return (Versioned) reviseDoc;
	}

}
