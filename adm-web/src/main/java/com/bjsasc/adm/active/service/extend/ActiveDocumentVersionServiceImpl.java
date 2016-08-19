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
 * 文档版本业务逻辑服务实现
 * 
 * @author 牛建义, 2011-11-16
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
			// 修订
			boolean isCheck = SessionHelper.getService().isCheckPermission();
			if (isCheck) {
				SessionHelper.getService().setCheckPermission(!isCheck);
			}

			reviseDoc = super.newVersion(version, newVersionNo, newIterationNo);

			LifeCycleTemplate template = Helper.getLifeCycleService().getLifeCycleTemplate((LifeCycleManaged) version);
			Helper.getLifeCycleService().setLifeCycleTemplate((LifeCycleManaged) reviseDoc, template);
			//修改master
			ActiveDocumentMaster newMaster = (ActiveDocumentMaster) reviseDoc.getMaster();
			ActiveDocumentMaster oldMaster = (ActiveDocumentMaster) version.getMaster();
			newMaster.setSecurityLevelInfo(oldMaster.getSecurityLevelInfo());
			PersistHelper.getService().modify(newMaster);

			// 备注
			((ActiveDocument) reviseDoc).setNote(((ActiveDocument) version).getNote());
			// 来源
			((ActiveDocument) reviseDoc).setDataSource(((ActiveDocument) version).getDataSource());
			// 代号
			((ActiveDocument) reviseDoc).setActiveCode(((ActiveDocument) version).getActiveCode());
			// 页数
			((ActiveDocument) reviseDoc).setPages(((ActiveDocument) version).getPages());
			// 份数
			((ActiveDocument) reviseDoc).setCount(((ActiveDocument) version).getCount());
			// 作者
			((ActiveDocument) reviseDoc).setAuthorName(((ActiveDocument) version).getAuthorName());
			// 作者单位
			((ActiveDocument) reviseDoc).setAuthorUnit(((ActiveDocument) version).getAuthorUnit());
			// 创建时间
			((ActiveDocument) reviseDoc).setAuthorTime(((ActiveDocument) version).getAuthorTime());

			AttachHelper.getAttachService().copyFile((ActiveDocument) version, (ActiveDocument) reviseDoc);
			PersistHelper.getService().modify(reviseDoc);

			SessionHelper.getService().setCheckPermission(isCheck);
		}
		return (Versioned) reviseDoc;
	}

}
