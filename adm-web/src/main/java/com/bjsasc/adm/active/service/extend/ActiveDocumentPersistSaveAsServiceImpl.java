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
 * �����ļ����Ϊʵ�֣���չPLM��װ��SaveAs�ӿ�
 * 
 * @author ������, 2013-09-24
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
			// ��������
			if (doc instanceof ActiveDocument) {
				AdmModelService admModelService = AdmHelper.getAdmModelService();
				String folderOid = Helper.getOid(targetFolder);
				String modelFolderOid = admModelService.getModelFolder(folderOid);
				String modelId = admModelService.getModelId(modelFolderOid);
				if (modelId != null && modelId.length() > 0) {
					classId = modelId;
				}
			}
			// ��ʼ��һ���°汾�ĵ�����
			newDoc = AdmHelper.getActiveDocumentService().newActiveDocument(classId); 
			newMaster = (ActiveDocumentMaster) newDoc.getMaster();

			ActiveDocumentMaster docMaster = (ActiveDocumentMaster) doc.getMaster();
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
			// ����
			newDoc.setActiveCode(doc.getActiveCode());
			// ҳ��
			newDoc.setPages(doc.getPages());
			// ����
			newDoc.setCount(doc.getCount());
			// ����
			newDoc.setAuthorName(doc.getAuthorName());
			// ���ߵ�λ
			newDoc.setAuthorUnit(doc.getAuthorUnit());
			// ����ʱ��
			newDoc.setAuthorTime(doc.getAuthorTime());
			// ��������
			ActiveLifecycleService lifeService = AdmHelper
					.getActiveLifecycleService();
			lifeService.initLifecycle(newDoc);
			// TODO
			boolean isCheck = SessionHelper.getService().isCheckPermission();
			if (isCheck) {
				SessionHelper.getService().setCheckPermission(!isCheck);
			}

			// �ر�Ȩ�޼��
			boolean checkPermission = SessionHelper.getService()
					.isCheckPermission();
			SessionHelper.getService().setCheckPermission(false);

			// ������������
			LifeCycleTemplate template = Helper.getLifeCycleService()
					.getLifeCycleTemplate((LifeCycleManaged) doc);
			Helper.getLifeCycleService().setLifeCycleTemplate(
					(LifeCycleManaged) newDoc, template);
			// ��Ȩ�޼��
			SessionHelper.getService().setCheckPermission(checkPermission);

			SessionHelper.getService().setCheckPermission(isCheck);
		}
		return newDoc;
	}
	
	public final Persistable saveAs(Persistable o, String number, String name,
			Persistable srcParent, Persistable targetParent) {
		// 1.�����¶��󣬿����ǵ������󣬱�����ߣ�Ҳ�����������󣬱���DocumentMaster
		// SaveasService s = ServiceFinder.findService(SaveasService.class, o);
		Folder targetFolder = (AbstractFolder) targetParent;
		Persistable newo = saveAs(o, targetFolder, number, name);
		if (newo instanceof Manageable) {
			((Manageable) newo).setManageInfo(null);
			ManageHelper.getService().init((Manageable) newo);
		}
		// ��������Ϣ Ϊ��У��Ȩ��
		if (newo instanceof Domained && targetParent instanceof AbstractFolder) {
			Domained domain = (Domained) newo;
			domain.setDomainInfo(targetFolder.getDomainInfo());
		}
		
		// �������ʵ����Managed�����ô����ˣ��޸��ˣ�Ϊ��ǰ��¼�û�,

		// �����ļ���
		FolderHelper.getService().addToFolder(targetFolder, (Foldered) newo);

		// �������
		PersistHelper.getService().save(newo);

		// ���û����Ӧ��ʵ�֣���ֱ�ӷ��ء�
		if (newo == null) {
			return null;
		}
		// 2.��������������ϵ
		if (newo instanceof Foldered && targetParent instanceof Folder) {
			// ���¶�����ӵ��ļ�����
			FolderHelper.getService().addToFolder((Folder) targetParent,
					(Foldered) newo);
			PersistHelper.getService().update(newo);
		}
		// 3.������ʷ��¼��1.����
		SaveasHistory h1 = HistoryHelper.getService().newSaveAsHistory();
		// ������ʷ��¼
		SaveasHistory h2 = HistoryHelper.getService().newSaveAsHistory();

		// ����FromOID
		h1.setFromOID(Helper.getOid(o));
		h1.setToOID(Helper.getOid(newo));
		h1.setDirection("���浽");

		h2.setFromOID(Helper.getOid(newo));
		h2.setToOID(Helper.getOid(o));
		h2.setDirection("������");
		// ���ñ���ǰ�ı�źͰ汾
		// ���Ϊ�汾����
		if (o instanceof Iterated) {
			// ���Ϊ�汾����
			// ���ñ��
			Mastered m1 = ((Iterated) o).getMaster();
			h1.setTargetClassId(m1.getClassId());
			h1.setTargetInnerId(m1.getInnerId());

			Mastered m2 = ((Iterated) newo).getMaster();
			h2.setTargetClassId(m2.getClassId());
			h2.setTargetInnerId(m2.getInnerId());

			// ���ð汾
			h1.setPreVersion(((Iterated) o).getIterationInfo()
					.getFullVersionNo());
			h1.setSaveasVersion(((Iterated) newo).getIterationInfo()
					.getFullVersionNo());

			h2.setPreVersion(((Iterated) newo).getIterationInfo()
					.getFullVersionNo());
			h2.setSaveasVersion(((Iterated) o).getIterationInfo()
					.getFullVersionNo());

			// ���ñ��
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
			// ����Ŀ�����
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
		// �������Ϊ��ʷ
		Helper.getPersistService().save(h1);
		Helper.getPersistService().save(h2);
		return newo;

	}	

	public void afterSaveas(Persistable pt, SaveasInfo si) {
		// �����ļ���ϵ
		Persistable targe = si.getSource(pt);
		if (targe instanceof ActiveDocument && pt instanceof ActiveDocument) {
			AttachHelper.getAttachService().copyFile((ActiveDocument) targe,
					(ActiveDocument) pt);
		}
	}
}
