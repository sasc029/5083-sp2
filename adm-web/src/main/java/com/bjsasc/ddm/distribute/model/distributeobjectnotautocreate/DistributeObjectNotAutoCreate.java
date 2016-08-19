package com.bjsasc.ddm.distribute.model.distributeobjectnotautocreate;

import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.plm.core.context.model.ContextInfo;
import com.bjsasc.plm.core.context.model.Contexted;
import com.bjsasc.plm.core.domain.DomainInfo;
import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.type.ATObject;

/**
 * ����ģ�����ò��Զ�����ģ�͡�
 * 
 * @author xurui 2014-8-5
 */
public class DistributeObjectNotAutoCreate  extends ATObject implements Domained,
	Contexted, Manageable {

	/** serialVersionUID */
	private static final long serialVersionUID = -3207816659340722062L;
	
	/** CLASSID */
	public static final String CLASSID = DistributeObjectNotAutoCreate.class.getSimpleName();
	
	/** ����ID */
	private String typeId;
	/** �������� */
	private String typeName;

	/** ������������Ϣ */
	private ContextInfo contextInfo;
	/** ������Ϣ */
	private ManageInfo manageInfo;
	/** ����Ϣ */
	private DomainInfo domainInfo;
	
	
	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public ContextInfo getContextInfo() {
		return contextInfo;
	}

	public void setContextInfo(ContextInfo contextInfo) {
		this.contextInfo = contextInfo;
	}

	public ManageInfo getManageInfo() {
		return manageInfo;
	}

	public void setManageInfo(ManageInfo manageInfo) {
		this.manageInfo = manageInfo;
	}

	public DomainInfo getDomainInfo() {
		return domainInfo;
	}

	public void setDomainInfo(DomainInfo domainInfo) {
		this.domainInfo = domainInfo;
	}

}
