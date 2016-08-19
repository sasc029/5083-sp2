package com.bjsasc.adm.active.service.extend;

import org.apache.log4j.Logger;

import com.bjsasc.adm.active.model.activeorder.ActiveOrder;
import com.bjsasc.adm.active.model.activeorder.ActiveOrderMaster;
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
public class ActiveOrderVersionServiceImpl extends VersionServiceImpl {

	private static final Logger LOGGER = Logger.getLogger(ActiveOrderVersionServiceImpl.class);

	/* (non-Javadoc)
	 * @see com.bjsasc.plm.core.vc.VersionServiceImpl#newVersion(com.bjsasc.plm.core.vc.Versioned, java.lang.String, java.lang.String)
	 */
	public Versioned newVersion(Versioned version, String newVersionNo, String newIterationNo) {

		LOGGER.debug(LogUtil.beginMethod(version, newVersionNo, newIterationNo));

		Versioned reviseDoc = null;
		if (version instanceof ActiveOrder) {
			// 修订
			boolean isCheck = SessionHelper.getService().isCheckPermission();
			if (isCheck) {
				SessionHelper.getService().setCheckPermission(!isCheck);
			}

			reviseDoc = super.newVersion(version, newVersionNo, newIterationNo);

			LifeCycleTemplate template = Helper.getLifeCycleService().getLifeCycleTemplate((LifeCycleManaged) version);
			Helper.getLifeCycleService().setLifeCycleTemplate((LifeCycleManaged) reviseDoc, template);
			//修改master
			ActiveOrderMaster newMaster = (ActiveOrderMaster) reviseDoc.getMaster();
			ActiveOrderMaster oldMaster = (ActiveOrderMaster) version.getMaster();
			newMaster.setSecurityLevelInfo(oldMaster.getSecurityLevelInfo());
			PersistHelper.getService().modify(newMaster);

			// 备注
			((ActiveOrder) reviseDoc).setNote(((ActiveOrder) version).getNote());
			// 来源
			((ActiveOrder) reviseDoc).setDataSource(((ActiveOrder) version).getDataSource());
			// 代号
			((ActiveOrder) reviseDoc).setActiveCode(((ActiveOrder) version).getActiveCode());
			// 页数
			((ActiveOrder) reviseDoc).setPages(((ActiveOrder) version).getPages());
			// 份数
			((ActiveOrder) reviseDoc).setCount(((ActiveOrder) version).getCount());
			// 作者
			((ActiveOrder) reviseDoc).setAuthorName(((ActiveOrder) version).getAuthorName());
			// 作者单位
			((ActiveOrder) reviseDoc).setAuthorUnit(((ActiveOrder) version).getAuthorUnit());
			// 创建时间
			((ActiveOrder) reviseDoc).setAuthorTime(((ActiveOrder) version).getAuthorTime());

			AttachHelper.getAttachService().copyFile((ActiveOrder) version, (ActiveOrder) reviseDoc);
			PersistHelper.getService().modify(reviseDoc);

			SessionHelper.getService().setCheckPermission(isCheck);
		}
		return (Versioned) reviseDoc;
	}

}
