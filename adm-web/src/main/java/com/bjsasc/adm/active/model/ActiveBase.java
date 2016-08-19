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
 * �����ļ�����ģ�� 
 *@author yanjia 2013-5-13
 */
public abstract class ActiveBase extends ATObject implements Versioned, Contexted, Domained, IterationFoldered,
		Baselineable, Manageable, Lockable, LifeCycleManaged, Approved{

	/** serialVersionUID */
	private static final long serialVersionUID = -8711837298232768986L;
	/** ��������  */
	private LifeCycleInfo lifeCycleInfo;
	/** ��������Ϣ */
	private ManageInfo manageInfo;
	/** ������������Ϣ  */
	private ContextInfo contextInfo;
	/** ����Ϣ  */
	private DomainInfo domainInfo;
	/** �汾��Ϣ */
	private IterationInfo iterationInfo;
	/** ��������Ϣ */
	private ObjectReference masterRef;
	/** �����ļ�����Ϣ  */
	private FolderInfo folderInfo;
	/** �� */
	private Lock lock;

	/**
	 * ȡ����������
	 * @return lifeCycleInfo
	 */
	public LifeCycleInfo getLifeCycleInfo() {
		return lifeCycleInfo;
	}

	/**
	 * ������������
	 * @param lifeCycleInfo Ҫ���õ� lifeCycleInfo
	 */
	public void setLifeCycleInfo(LifeCycleInfo lifeCycleInfo) {
		this.lifeCycleInfo = lifeCycleInfo;
	}

	/** 
	 * ���ù�������Ϣ 
	 * @param manageInfo Ҫ���õ�manageInfo
	 */
	public void setManageInfo(ManageInfo manageInfo) {
		this.manageInfo = manageInfo;
	}

	/** 
	 * ȡ�ù�������Ϣ 
	 * @return manageInfo
	 */
	public ManageInfo getManageInfo() {
		return manageInfo;
	}

	/**
	 * ȡ����������Ϣ
	 * @return contextInfo
	 */
	public ContextInfo getContextInfo() {
		return contextInfo;
	}

	/**
	 * ������������Ϣ
	 * @param contextInfo Ҫ���õ� contextInfo
	 */
	public void setContextInfo(ContextInfo contextInfo) {
		this.contextInfo = contextInfo;
	}

	/**
	 * ȡ������Ϣ
	 * @return domainInfo
	 */
	public DomainInfo getDomainInfo() {
		return domainInfo;
	}

	/**
	 * ��������Ϣ
	 * @param domainInfo Ҫ���õ� domainInfo
	 */
	public void setDomainInfo(DomainInfo domainInfo) {
		this.domainInfo = domainInfo;
	}

	/**
	 * ȡ�ð汾��Ϣ
	 * @return iterationInfo
	 */
	public IterationInfo getIterationInfo() {
		return iterationInfo;
	}

	/**
	 * ���ð汾��Ϣ
	 * @param iterationInfo Ҫ���õ� iterationInfo
	 */
	public void setIterationInfo(IterationInfo iterationInfo) {
		this.iterationInfo = iterationInfo;
	}

	/**
	 * ȡ��������
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
	 * ȡ����������Ϣ
	 * @return masterRef
	 */
	public ObjectReference getMasterRef() {
		return masterRef;
	}

	/**
	 * ����������
	 * @param master
	 */
	public void setMaster(com.bjsasc.platform.objectmodel.business.version.Mastered master) {
		setMasterRef(master == null ? null : ObjectReference.newObjectReference(master));
	}

	/**
	 * ȡ����������Ϣ
	 * @param masterRef
	 */
	public void setMasterRef(ObjectReference masterRef) {
		this.masterRef = masterRef;

	}

	/**
	 * �����ļ�����Ϣ
	 * @return folderInfo
	 */
	public FolderInfo getFolderInfo() {
		return this.folderInfo;
	}

	/**
	 * ���������ļ�����Ϣ
	 * @param folderInfo Ҫ���õ� folderInfo
	 */
	public void setFolderInfo(FolderInfo folderInfo) {
		this.folderInfo = folderInfo;
	}

	/**
	 * ������
	 * @param lock
	 */
	public void setLock(Lock lock) {
		this.lock = lock;
	}

	/**
	 * ��ȡ��
	 * @return
	 */
	public Lock getLock() {
		return lock;
	}
}
