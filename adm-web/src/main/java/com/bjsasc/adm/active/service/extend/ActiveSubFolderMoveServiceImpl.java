package com.bjsasc.adm.active.service.extend;

import java.util.ArrayList;
import java.util.List;

import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.plm.common.move.MoveServiceDefaultImpl;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.folder.Folder;
import com.bjsasc.plm.core.folder.FolderHelper;
import com.bjsasc.plm.core.folder.FolderLink;
import com.bjsasc.plm.core.folder.SubFolder;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.folder.FolderManagerHelper;

public class ActiveSubFolderMoveServiceImpl extends MoveServiceDefaultImpl {

	public void moveFolders(List<SubFolder> folders, List<Persistable> targets, List<Persistable> objs) {
		multiMoveFolders(folders, targets, objs);
	}

	public Persistable moveFromClipboard(Persistable obj, Persistable srcParent, Context srcContext,
			Persistable targetParent, Context targetContext) {
		// FolderManagerHelper.getService().moveSubFolder((SubFolder)obj, (Folder)targetParent,null,null);
		AdmHelper.getActiveSubFolderManagerService().moveSubFolder((SubFolder) obj, (Folder) targetParent, null, null);
		return null;
	}

	private void multiMoveFolders(List<SubFolder> sourcefolders, List<Persistable> targetfolders, List<Persistable> objs) {
		//获取扁平的文件夹集合
		List<SubFolder> platfolders = new ArrayList<SubFolder>();
		FolderManagerHelper.getService().platTreeFolder(sourcefolders, platfolders);
		for (int i = 0; i < sourcefolders.size(); i++) {
			SubFolder sub = (SubFolder) sourcefolders.get(i);
			FolderLink link = FolderHelper.getService().getParentFolderLink(sub);
			if (link != null) {
				AdmHelper.getActiveSubFolderManagerService().moveSubFolder(sub, (Folder) targetfolders.get(i),
						platfolders, objs);
				// FolderManagerHelper.getService().moveSubFolder(sub, (Folder) targetfolders.get(i),platfolders,objs);
			}
		}
	}
}
