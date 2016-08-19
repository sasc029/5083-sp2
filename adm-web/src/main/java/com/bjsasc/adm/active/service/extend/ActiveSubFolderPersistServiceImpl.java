package com.bjsasc.adm.active.service.extend;

import java.util.Collection;
import java.util.List;

import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.model.Recycledable;
import com.bjsasc.adm.common.AdmLifeCycleConstUtil;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleInfo;
import com.bjsasc.plm.core.folder.FolderHelper;
import com.bjsasc.plm.core.folder.FolderLink;
import com.bjsasc.plm.core.folder.FolderMemberLink;
import com.bjsasc.plm.core.folder.SubFolder;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.PersistInnerServiceImpl;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.util.PlmException;
import com.bjsasc.plm.core.vc.model.ControlBranch;
import com.bjsasc.plm.core.vc.model.Iterated;
import com.bjsasc.plm.core.vc.VersionControlHelper;

/**
 * 现行文件夹删除实现类，扩展PLM已封装的实现
 * 
 * @author 耿安聪, 2013-09-25
 *
 */
public class ActiveSubFolderPersistServiceImpl extends PersistInnerServiceImpl{
	
	private void doBeforeDelete(Object entity){
		if(entity instanceof SubFolder){
			SubFolder sub = (SubFolder)entity;
			List<FolderMemberLink> list = FolderHelper.getService().getFolderMemberLinks(sub);
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					FolderMemberLink link = (FolderMemberLink)list.get(i);
					Persistable obj = (Persistable)link.getFoldered();
					if(link.getObjType()==FolderMemberLink.OBJTYPE_VERSION_Y){
						List<Iterated> iterationlist = VersionControlHelper.getService().iterationsOf((ControlBranch)obj);
						for(Iterated iterated:iterationlist){
							// IterationFoldered iteratedfoldered = (IterationFoldered)iterated;
							try{
								// PersistHelper.getService().delete(iteratedfoldered);
								if (iterated instanceof Recycledable && iterated instanceof LifeCycleManaged) {
									LifeCycleManaged lifeCycle = (LifeCycleManaged) iterated;
									LifeCycleInfo lifeCycleInfo = lifeCycle.getLifeCycleInfo();
									String stateName = lifeCycleInfo.getStateName();
									String recycleStateName = AdmLifeCycleConstUtil.LC_RECYCLE.getName();
									if (recycleStateName.equals(stateName)) {
										continue;
									}
									AdmHelper.getActiveRecycleService().addltem((Recycledable) iterated);
								}
							}catch(Exception e){
								throw new PlmException("plm.folder.delete");
							}
						}
					}
					PersistHelper.getService().delete(link);
					//PersistHelper.getService().delete(obj);
				}
			}			
			List<FolderLink> links = FolderHelper.getService().getChildFolderLinks(sub);
			for(FolderLink link:links){
				PersistHelper.getService().delete(link.getChildFolder());			
			}
			
			FolderLink toParentLink = FolderHelper.getService().getParentFolderLink(sub);
			
			//判断权限
			PersistHelper.getService().update(toParentLink.getParentFolder());
			///
			
			PersistHelper.getService().delete(toParentLink);
			
		}
	}
	
	/* (non-Javadoc)
	 * @see com.bjsasc.plm.core.persist.PersistSUDServiceImpl#delete(java.lang.Object)
	 */
	@Override
	public void delete(Object entity, Collection<?> entitiesTogether) {
		// TODO Auto-generated method stub
		doBeforeDelete(entity);		
		super.delete(entity, entitiesTogether);
	}
}
