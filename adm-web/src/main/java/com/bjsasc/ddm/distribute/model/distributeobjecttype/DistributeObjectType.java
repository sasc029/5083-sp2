package com.bjsasc.ddm.distribute.model.distributeobjecttype;

import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.context.model.ContextInfo;
import com.bjsasc.plm.core.context.model.Contexted;
import com.bjsasc.plm.core.domain.DomainInfo;
import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.system.principal.Principal;
import com.bjsasc.plm.core.type.ATObject;

/**
 * ����ģ������ģ�͡�
 * 
 * @author gengancong 2014-5-4
 */
public class DistributeObjectType extends ATObject implements Domained,
		Contexted, Manageable {

	/** serialVersionUID */
	private static final long serialVersionUID = 4022005412929635230L;

	/** CLASSID */
	public static final String CLASSID = DistributeObjectType.class
			.getSimpleName();

	/** ��ɫ/��Ա�ڲ���ʶ */
	private String dataId;
	/** ��ɫ/��Ա�� */
	private String dataClassId;
	/** ����ID */
	private String typeId;
	/** �������� */
	private String typeName;
	/** ��� */
	private String state;

	/** ������������Ϣ */
	private ContextInfo contextInfo;
	/** ������Ϣ */
	private ManageInfo manageInfo;
	/** ����Ϣ */
	private DomainInfo domainInfo;

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getDataClassId() {
		return dataClassId;
	}

	public void setDataClassId(String dataClassId) {
		this.dataClassId = dataClassId;
	}

	public String getDataName() {
		String oid = getDataOid();
		if (oid == null) {
			return "";
		}
		Persistable object = Helper.getPersistService().getObject(oid);
		if (object instanceof Principal) {
			Principal principal = (Principal) object;
			if (principal != null) {
				return principal.getName();
			}
		}
		return "";
	}

	public String getDataOid() {
		if (dataId != null && dataClassId != null) {
			return dataClassId + ":" + dataId;
		}
		return "";
	}

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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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
