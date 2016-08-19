package com.bjsasc.adm.active.model;

import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleInfo;
import com.bjsasc.platform.objectmodel.business.persist.ObjectReference;
import com.bjsasc.platform.objectmodel.business.version.IterationInfo;
import com.bjsasc.plm.core.approve.Approved;
import com.bjsasc.plm.core.baseline.model.Baselineable;
import com.bjsasc.plm.core.context.model.ContextInfo;
import com.bjsasc.plm.core.context.model.Contexted;
import com.bjsasc.plm.core.domain.DomainInfo;
import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.folder.FolderInfo;
import com.bjsasc.plm.core.folder.IterationFoldered;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.lock.model.Lock;
import com.bjsasc.plm.core.lock.model.Lockable;
import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.type.ATObject;
import com.bjsasc.plm.core.vc.model.Mastered;
import com.bjsasc.plm.core.vc.model.Versioned;

/**
 * 现行文件基类模型 
 *@author yanjia 2013-5-13
 */
public abstract class ActiveBase extends ATObject implements Versioned, Contexted, Domained, IterationFoldered,
		Baselineable, Manageable, Lockable, LifeCycleManaged, Approved{

	/** serialVersionUID */
	private static final long serialVersionUID = -8711837298232768986L;
	/** 生命周期  */
	private LifeCycleInfo lifeCycleInfo;
	/** 管理者信息 */
	private ManageInfo manageInfo;
	/** 所属上下文信息  */
	private ContextInfo contextInfo;
	/** 域信息  */
	private DomainInfo domainInfo;
	/** 版本信息 */
	private IterationInfo iterationInfo;
	/** 主对象信息 */
	private ObjectReference masterRef;
	/** 所属文件夹信息  */
	private FolderInfo folderInfo;
	/** 锁 */
	private Lock lock;

	/**
	 * 取得生命周期
	 * @return lifeCycleInfo
	 */
	public LifeCycleInfo getLifeCycleInfo() {
		return lifeCycleInfo;
	}

	/**
	 * 设置生命周期
	 * @param lifeCycleInfo 要设置的 lifeCycleInfo
	 */
	public void setLifeCycleInfo(LifeCycleInfo lifeCycleInfo) {
		this.lifeCycleInfo = lifeCycleInfo;
	}

	/** 
	 * 设置管理者信息 
	 * @param manageInfo 要设置的manageInfo
	 */
	public void setManageInfo(ManageInfo manageInfo) {
		this.manageInfo = manageInfo;
	}

	/** 
	 * 取得管理者信息 
	 * @return manageInfo
	 */
	public ManageInfo getManageInfo() {
		return manageInfo;
	}

	/**
	 * 取得上下文信息
	 * @return contextInfo
	 */
	public ContextInfo getContextInfo() {
		return contextInfo;
	}

	/**
	 * 设置上下文信息
	 * @param contextInfo 要设置的 contextInfo
	 */
	public void setContextInfo(ContextInfo contextInfo) {
		this.contextInfo = contextInfo;
	}

	/**
	 * 取得域信息
	 * @return domainInfo
	 */
	public DomainInfo getDomainInfo() {
		return domainInfo;
	}

	/**
	 * 设置域信息
	 * @param domainInfo 要设置的 domainInfo
	 */
	public void setDomainInfo(DomainInfo domainInfo) {
		this.domainInfo = domainInfo;
	}

	/**
	 * 取得版本信息
	 * @return iterationInfo
	 */
	public IterationInfo getIterationInfo() {
		return iterationInfo;
	}

	/**
	 * 设置版本信息
	 * @param iterationInfo 要设置的 iterationInfo
	 */
	public void setIterationInfo(IterationInfo iterationInfo) {
		this.iterationInfo = iterationInfo;
	}

	/**
	 * 取得主对象
	 * @return masterRef.getObject()
	 */
	public Mastered getMaster() {
		if (masterRef != null) {
			return (Mastered) masterRef.getObject();
		} else {
			return null;
		}
	}

	/**
	 * 取得主对象信息
	 * @return masterRef
	 */
	public ObjectReference getMasterRef() {
		return masterRef;
	}

	/**
	 * 设置主对象
	 * @param master
	 */
	public void setMaster(com.bjsasc.platform.objectmodel.business.version.Mastered master) {
		setMasterRef(master == null ? null : ObjectReference.newObjectReference(master));
	}

	/**
	 * 取得主对象信息
	 * @param masterRef
	 */
	public void setMasterRef(ObjectReference masterRef) {
		this.masterRef = masterRef;

	}

	/**
	 * 所属文件夹信息
	 * @return folderInfo
	 */
	public FolderInfo getFolderInfo() {
		return this.folderInfo;
	}

	/**
	 * 设置所属文件夹信息
	 * @param folderInfo 要设置的 folderInfo
	 */
	public void setFolderInfo(FolderInfo folderInfo) {
		this.folderInfo = folderInfo;
	}

	/**
	 * 设置锁
	 * @param lock
	 */
	public void setLock(Lock lock) {
		this.lock = lock;
	}

	/**
	 * 获取锁
	 * @return
	 */
	public Lock getLock() {
		return lock;
	}
}
