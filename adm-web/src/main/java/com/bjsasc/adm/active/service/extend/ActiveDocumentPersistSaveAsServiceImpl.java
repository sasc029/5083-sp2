package com.bjsasc.adm.active.service.extend;

import org.apache.log4j.Logger;

import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.model.activedocument.ActiveDocument;
import com.bjsasc.adm.active.model.activedocument.ActiveDocumentMaster;
import com.bjsasc.adm.active.service.activelifecycle.ActiveLifecycleService;
import com.bjsasc.adm.active.service.admmodelservice.AdmModelService;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleTemplate;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.attachment.AttachHelper;
import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.folder.AbstractFolder;
import com.bjsasc.plm.core.folder.Folder;
import com.bjsasc.plm.core.folder.FolderHelper;
import com.bjsasc.plm.core.folder.Foldered;
import com.bjsasc.plm.core.history.HistoryHelper;
import com.bjsasc.plm.core.history.SaveasHistory;
import com.bjsasc.plm.core.identifier.UniqueIdentified;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.managed.ManageHelper;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.saveas.SaveasInfo;
import com.bjsasc.plm.core.saveas.SaveasService;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.util.LogUtil;
import com.bjsasc.plm.core.vc.model.Iterated;
import com.bjsasc.plm.core.vc.model.Mastered;

/**
 * 现行文件另存为实现，扩展PLM封装的SaveAs接口
 * 
 * @author 耿安聪, 2013-09-24
 * 
 */
public class ActiveDocumentPersistSaveAsServiceImpl implements SaveasService {

	private static final Logger LOG = Logger
			.getLogger(ActiveDocumentPersistSaveAsServiceImpl.class);

	public Persistable saveAs(Persistable obj, String number, String name) {
		return null;
	}

	public Persistable saveAs(Persistable obj, Folder targetFolder, String number, String name) {

		LOG.debug(LogUtil.beginMethod(obj, number));

		ActiveDocument doc = null;
		ActiveDocumentMaster newMaster = null;
		ActiveDocument newDoc = null;
		if (obj instanceof ActiveDocument) {
			doc = (ActiveDocument) obj;

			String classId = doc.getClassId();
			// 设置类型
			if (doc instanceof ActiveDocument) {
				AdmModelService admModelService = AdmHelper.getAdmModelService();
				String folderOid = Helper.getOid(targetFolder);
				String modelFolderOid = admModelService.getModelFolder(folderOid);
				String modelId = admModelService.getModelId(modelFolderOid);
				if (modelId != null && modelId.length() > 0) {
					classId = modelId;
				}
			}
			// 初始化一个新版本文档对象
			newDoc = AdmHelper.getActiveDocumentService().newActiveDocument(classId); 
			newMaster = (ActiveDocumentMaster) newDoc.getMaster();

			ActiveDocumentMaster docMaster = (ActiveDocumentMaster) doc.getMaster();
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
			// 代号
			newDoc.setActiveCode(doc.getActiveCode());
			// 页数
			newDoc.setPages(doc.getPages());
			// 份数
			newDoc.setCount(doc.getCount());
			// 作者
			newDoc.setAuthorName(doc.getAuthorName());
			// 作者单位
			newDoc.setAuthorUnit(doc.getAuthorUnit());
			// 创建时间
			newDoc.setAuthorTime(doc.getAuthorTime());
			// 生命周期
			ActiveLifecycleService lifeService = AdmHelper
					.getActiveLifecycleService();
			lifeService.initLifecycle(newDoc);
			// TODO
			boolean isCheck = SessionHelper.getService().isCheckPermission();
			if (isCheck) {
				SessionHelper.getService().setCheckPermission(!isCheck);
			}

			// 关闭权限检查
			boolean checkPermission = SessionHelper.getService()
					.isCheckPermission();
			SessionHelper.getService().setCheckPermission(false);

			// 设置生命周期
			LifeCycleTemplate template = Helper.getLifeCycleService()
					.getLifeCycleTemplate((LifeCycleManaged) doc);
			Helper.getLifeCycleService().setLifeCycleTemplate(
					(LifeCycleManaged) newDoc, template);
			// 打开权限检查
			SessionHelper.getService().setCheckPermission(checkPermission);

			SessionHelper.getService().setCheckPermission(isCheck);
		}
		return newDoc;
	}
	
	public final Persistable saveAs(Persistable o, String number, String name,
			Persistable srcParent, Persistable targetParent) {
		// 1.生成新对象，可能是单个对象，比如基线，也可能是主对象，比如DocumentMaster
		// SaveasService s = ServiceFinder.findService(SaveasService.class, o);
		Folder targetFolder = (AbstractFolder) targetParent;
		Persistable newo = saveAs(o, targetFolder, number, name);
		if (newo instanceof Manageable) {
			((Manageable) newo).setManageInfo(null);
			ManageHelper.getService().init((Manageable) newo);
		}
		// 设置域信息 为了校验权限
		if (newo instanceof Domained && targetParent instanceof AbstractFolder) {
			Domained domain = (Domained) newo;
			domain.setDomainInfo(targetFolder.getDomainInfo());
		}
		
		// 如果对象实现了Managed，设置创建人，修改人，为当前登录用户,

		// 放入文件夹
		FolderHelper.getService().addToFolder(targetFolder, (Foldered) newo);

		// 保存对象
		PersistHelper.getService().save(newo);

		// 如果没有相应的实现，则直接返回。
		if (newo == null) {
			return null;
		}
		// 2.与新容器建立关系
		if (newo instanceof Foldered && targetParent instanceof Folder) {
			// 把新对象添加到文件夹中
			FolderHelper.getService().addToFolder((Folder) targetParent,
					(Foldered) newo);
			PersistHelper.getService().update(newo);
		}
		// 3.建立历史记录，1.正向
		SaveasHistory h1 = HistoryHelper.getService().newSaveAsHistory();
		// 反向历史记录
		SaveasHistory h2 = HistoryHelper.getService().newSaveAsHistory();

		// 设置FromOID
		h1.setFromOID(Helper.getOid(o));
		h1.setToOID(Helper.getOid(newo));
		h1.setDirection("保存到");

		h2.setFromOID(Helper.getOid(newo));
		h2.setToOID(Helper.getOid(o));
		h2.setDirection("保存自");
		// 设置保存前的编号和版本
		// 如果为版本对象
		if (o instanceof Iterated) {
			// 如果为版本对象
			// 设置编号
			Mastered m1 = ((Iterated) o).getMaster();
			h1.setTargetClassId(m1.getClassId());
			h1.setTargetInnerId(m1.getInnerId());

			Mastered m2 = ((Iterated) newo).getMaster();
			h2.setTargetClassId(m2.getClassId());
			h2.setTargetInnerId(m2.getInnerId());

			// 设置版本
			h1.setPreVersion(((Iterated) o).getIterationInfo()
					.getFullVersionNo());
			h1.setSaveasVersion(((Iterated) newo).getIterationInfo()
					.getFullVersionNo());

			h2.setPreVersion(((Iterated) newo).getIterationInfo()
					.getFullVersionNo());
			h2.setSaveasVersion(((Iterated) o).getIterationInfo()
					.getFullVersionNo());

			// 设置编号
			if (m1 instanceof UniqueIdentified) {
				h1.setPreNumber(((UniqueIdentified) m1).getNumber());
				h2.setSaveasNumber(((UniqueIdentified) m1).getNumber());
			} else {
				h1.setPreNumber("");
				h2.setSaveasNumber("");
			}
			h1.setSaveasNumber(number);
			h2.setPreNumber(number);
		} else {
			// 设置目标对象
			h1.setTargetClassId(o.getClassId());
			h1.setTargetInnerId(o.getInnerId());
			h2.setTargetClassId(newo.getClassId());
			h2.setTargetInnerId(newo.getInnerId());

			h1.setPreVersion("");
			h1.setSaveasVersion("");

			h2.setPreVersion("");
			h2.setSaveasVersion("");

			if (o instanceof UniqueIdentified) {
				h1.setPreNumber(((UniqueIdentified) o).getNumber());
				h2.setSaveasNumber(((UniqueIdentified) o).getNumber());
			} else {
				h1.setPreNumber("");
				h2.setSaveasNumber("");
			}
			h1.setSaveasNumber(number);
			h2.setPreNumber(number);
		}
		// 保存另存为历史
		Helper.getPersistService().save(h1);
		Helper.getPersistService().save(h2);
		return newo;

	}	

	public void afterSaveas(Persistable pt, SaveasInfo si) {
		// 处理文件关系
		Persistable targe = si.getSource(pt);
		if (targe instanceof ActiveDocument && pt instanceof ActiveDocument) {
			AttachHelper.getAttachService().copyFile((ActiveDocument) targe,
					(ActiveDocument) pt);
		}
	}
}
