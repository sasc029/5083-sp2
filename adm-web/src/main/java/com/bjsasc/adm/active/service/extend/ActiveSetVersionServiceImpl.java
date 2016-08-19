package com.bjsasc.adm.active.service.extend;

import org.apache.log4j.Logger;

import com.bjsasc.adm.active.model.activeset.ActiveSet;
import com.bjsasc.adm.active.model.activeset.ActiveSetMaster;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleTemplate;
import com.bjsasc.plm.core.Helper;
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
public class ActiveSetVersionServiceImpl extends VersionServiceImpl {

	private static final Logger LOGGER = Logger.getLogger(ActiveSetVersionServiceImpl.class);

	/* (non-Javadoc)
	 * @see com.bjsasc.plm.core.vc.VersionServiceImpl#newVersion(com.bjsasc.plm.core.vc.Versioned, java.lang.String, java.lang.String)
	 */
	public Versioned newVersion(Versioned version, String newVersionNo, String newIterationNo) {

		LOGGER.debug(LogUtil.beginMethod(version, newVersionNo, newIterationNo));

		Versioned reviseDoc = null;
		if (version instanceof ActiveSet) {
			// �޶�
			boolean isCheck = SessionHelper.getService().isCheckPermission();
			if (isCheck) {
				SessionHelper.getService().setCheckPermission(!isCheck);
			}

			reviseDoc = super.newVersion(version, newVersionNo, newIterationNo);

			LifeCycleTemplate template = Helper.getLifeCycleService().getLifeCycleTemplate((LifeCycleManaged) version);
			Helper.getLifeCycleService().setLifeCycleTemplate((LifeCycleManaged) reviseDoc, template);
			//�޸�master
			ActiveSetMaster newMaster = (ActiveSetMaster) reviseDoc.getMaster();
			ActiveSetMaster oldMaster = (ActiveSetMaster) version.getMaster();
			newMaster.setSecurityLevelInfo(oldMaster.getSecurityLevelInfo());
			PersistHelper.getService().modify(newMaster);

			// ��ע
			((ActiveSet) reviseDoc).setNote(((ActiveSet) version).getNote());
			// ��Դ
			((ActiveSet) reviseDoc).setDataSource(((ActiveSet) version).getDataSource());
			// ����
			((ActiveSet) reviseDoc).setActiveCode(((ActiveSet) version).getActiveCode());
			// ҳ��
			((ActiveSet) reviseDoc).setPages(((ActiveSet) version).getPages());
			// ����
			((ActiveSet) reviseDoc).setCount(((ActiveSet) version).getCount());

			PersistHelper.getService().modify(reviseDoc);

			SessionHelper.getService().setCheckPermission(isCheck);
		}
		return (Versioned) reviseDoc;
	}

}
