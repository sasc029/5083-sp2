package com.bjsasc.adm.active.service.activefolder;

import java.util.List;

import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.model.activedocument.ActiveDocument;
import com.bjsasc.adm.active.service.admmodelservice.AdmModelService;
import com.bjsasc.plm.common.move.MoveHelper;
import com.bjsasc.plm.common.move.MoveRuleEnum;
import com.bjsasc.plm.common.move.MoveService;
import com.bjsasc.plm.common.move.MoveServiceDefaultImpl;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.folder.Folder;
import com.bjsasc.plm.core.folder.FolderHelper;
import com.bjsasc.plm.core.folder.Foldered;
import com.bjsasc.plm.core.folder.SubFolder;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.vc.VersionControlHelper;
import com.bjsasc.plm.core.vc.model.Versioned;

public class ActiveMoveServiceImpl extends MoveServiceDefaultImpl {

	public Persistable move(Persistable obj, Persistable srcParent, Context srcContext, Persistable targetParent,
			Context targetContext, List<Persistable> objs, List<SubFolder> folders, MoveRuleEnum rule) {
		return move(obj, srcParent, srcContext, targetParent, targetContext, objs, folders);
	}
	
	public Persistable move(Persistable obj, Persistable srcParent, Context srcContext, Persistable targetParent,
			Context targetContext, List<Persistable> objs, List<SubFolder> folders) {
		// O ���е��޶����ƶ���Ŀ��λ��
		if (obj instanceof Versioned) { // �а汾����
			if (srcParent instanceof Folder && targetParent instanceof Folder) { // �ļ���֮���ƶ�
				// ��������
				if (obj instanceof ActiveDocument){
					Folder targetFolder = (Folder)targetParent;
					AdmModelService admModelService = AdmHelper.getAdmModelService();
					String folderOid = Helper.getOid(targetFolder);
					String modelFolderOid = admModelService.getModelFolder(folderOid);
					String modelId = admModelService.getModelId(modelFolderOid);
					if (modelId != null && modelId.length() > 0) {
						obj.setClassId(modelId);
						PersistHelper.getService().update(obj);
					}
				}
				Versioned versioned = (Versioned) obj;
				List<Versioned> versionList = VersionControlHelper.getService().allVersionsOf(versioned);
				for (Versioned version : versionList) {
					Versioned tmpDoc = (Versioned) version;
					// ���ü��а��������ƶ�,�Զ���¼��ʷ
					MoveHelper.getService().moveFromClipboard(tmpDoc, srcParent, srcContext, targetParent,
							targetContext);
				}
			}
		}else{
			MoveHelper.getService().moveFromClipboard(obj, srcParent, srcContext, targetParent,
					targetContext);
		}

		// �ļ����ƶ����µ��ĵ��ƶ��Ĵ���(����)

		return obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bjsasc.plm.common.move.MoveService#moveFromClipboard(com.bjsasc.platform
	 * .objectmodel.business.persist.Persistable,
	 * com.bjsasc.plm.core.persist.Persistable,
	 * com.bjsasc.plm.core.context.Context,
	 * com.bjsasc.plm.core.persist.Persistable,
	 * com.bjsasc.plm.core.context.Context)
	 */
	public Persistable moveFromClipboard(Persistable obj, Persistable srcParent, Context srcContext, Persistable targetParent,
			Context targetContext) {
		if (obj instanceof Foldered && srcParent instanceof Folder && targetParent instanceof Folder) {
			// "����-ճ��"ͬһ�����ĵĴ���,����С�汾�޸��ļ���λ��
			FolderHelper.getService().move((Foldered) obj, (Folder) srcParent, (Folder) targetParent);
		}
		return obj;
	}

}
