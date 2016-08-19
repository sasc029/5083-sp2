package com.bjsasc.adm.active.service.activefolder;

import java.util.Map;

import com.bjsasc.plm.core.vc.model.Workable;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.baseline.model.Baselined;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.folder.Cabinet;
import com.bjsasc.plm.core.folder.Folder;
import com.bjsasc.plm.core.folder.Foldered;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.workspace.Workspace;
import com.bjsasc.plm.core.workspace.WorkspaceHelper;
import com.bjsasc.plm.navigator.path.Path;
import com.bjsasc.plm.navigator.path.PathBuilder;
import com.bjsasc.plm.navigator.path.PathBuilderHelper;
import com.bjsasc.plm.navigator.path.PathNodeUtil;
import com.bjsasc.plm.vc.VersionControlManager;

/**
 * 文件夹内对象的构造方式
 * @author linjinzhi
 *
 */
public class PathBuilder_ActiveFolderedImpl implements PathBuilder{
	public Path buildPath(Object target, Map<String, Object> params, boolean isTargetIncluded) {
		Path path = new Path();
		
		if(target == null){
			return path;
		}
		
		if(target instanceof Foldered){
			String fromModule = (String)params.get(KeyS.FROM_MODULE);
			String fromContainer = (String)params.get(KeyS.FROM_CONTAINER);
			
			if(!"workspace".equals(fromModule) && target instanceof Baselined && target instanceof Workable){
				boolean isWorkingCopy = false;
				Workable workingCopy = VersionControlManager.getManager().getMyWorkingCopy((Workable)target);
				if(workingCopy != null && workingCopy.getInnerId().equals(((Workable)target).getInnerId())){
					isWorkingCopy = true;
				}
									
				//如果当前对象是当前用户的工作副本
				if(isWorkingCopy){
					Workspace workspace = WorkspaceHelper.getService().getWorkspace((Baselined)target);

					//如果对象所在工作区存在，则优先导航至工作区
					if(workspace != null){
						fromModule = "workspace";
						fromContainer = Helper.getOid(workspace);
					}					
				}				
			}
			
			if(fromModule != null && fromModule.equals("workspace")){
				Workspace workspace = (Workspace)Helper.getPersistService().getObject(fromContainer);
				SessionHelper.getService().put("isInWorkspace", "true");
				SessionHelper.getService().put("workspace", workspace);
				//所在工作区
				path.append(PathBuilderHelper.getManager().buildPath(workspace, params, true));
			}else{				
				Foldered foldered = (Foldered)target;
				Folder folder = foldered.getFolderInfo().getFolder();
				if (folder != null) {
				SessionHelper.getService().put("isInWorkspace", "false");
				SessionHelper.getService().put("workspace", null);
				if(folder instanceof Cabinet){
					Cabinet cabinet = (Cabinet)folder;
					if(Cabinet.CABINET_TYPE_SYSTEM.equals(cabinet.getCabtype())){
						Context folderContext = folder.getContextInfo().getContext();
						path.append(PathBuilderHelper.getManager().buildPath(folderContext, params, true));
					}else{
						path.append(PathBuilderHelper.getManager().buildPath(folder, params, true)); 											
					}
				}else{
					path.append(PathBuilderHelper.getManager().buildPath(folder, params, true));
				}
				}
			}
			
			if(isTargetIncluded){
				path.append(PathNodeUtil.getPathNodeObject((Foldered)target));
			}
		}
		
		return path;
	}
}
