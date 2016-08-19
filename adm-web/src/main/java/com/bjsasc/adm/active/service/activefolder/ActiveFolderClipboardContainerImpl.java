package com.bjsasc.adm.active.service.activefolder;

import org.apache.log4j.Logger;

import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.model.activedocument.ActiveDocument;
import com.bjsasc.adm.active.service.admmodelservice.AdmModelService;
import com.bjsasc.plm.common.clipboard.ClipboardContainerDefaultImpl;
import com.bjsasc.plm.common.clipboard.ClipboardObject;
import com.bjsasc.plm.common.move.MoveHelper;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.context.model.Contexted;
import com.bjsasc.plm.core.folder.Folder;
import com.bjsasc.plm.core.folder.Foldered;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.saveas.SaveasHelper;

public class ActiveFolderClipboardContainerImpl extends ClipboardContainerDefaultImpl {

	private static final Logger LOG = Logger.getLogger(ActiveFolderClipboardContainerImpl.class);
	
	// 拷贝粘贴
	public Persistable pasteFromCopy(ClipboardObject clipboardObj, Persistable targetParent, String number, String name) {
		// 获取另存为的对象,如果为Identified，执行另存，否则不另存
		Persistable obj = clipboardObj.getPasteItem();
		Persistable newObj = null;
		if (obj instanceof Folder) {// 文件夹对象不能copy。
			LOG.debug("文件夹对象不能拷贝。");
		} else {
			Foldered pasteObj = (Foldered)clipboardObj.getPasteItem();
			newObj = SaveasHelper.getService().saveAs(pasteObj, number, name, pasteObj.getFolderInfo().getFolder(), targetParent);
		}
		return newObj;
	}

	// 剪切粘贴
	public Persistable pasteFromCut(ClipboardObject clipboardObj,Persistable targetParent,String spotId) {
		Persistable obj = clipboardObj.getPasteItem();// 粘贴对象
		
		// 设置类型
		if (obj instanceof ActiveDocument && targetParent instanceof Folder){
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
		Foldered pasteObj = (Foldered)obj;
		Contexted ct = (Contexted)targetParent;

		return MoveHelper.getService().moveFromClipboard(pasteObj,pasteObj.getFolderInfo().getFolder(), null, targetParent, ct.getContextInfo().getContext());
	}
}
