package com.bjsasc.ddm.distribute.model.distributeobject;

import java.util.List;

import com.bjsasc.avidm.core.site.constant.DTSiteConstant;
import com.bjsasc.ddm.common.LifeCycleUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.duplicateprocess.DuplicateProcess;
import com.bjsasc.ddm.distribute.model.returnreason.ReturnReason;
import com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleInfo;
import com.bjsasc.platform.objectmodel.business.persist.ObjectReference;
import com.bjsasc.platform.objectmodel.business.version.IterationInfo;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.context.model.AbstractContext;
import com.bjsasc.plm.core.context.model.ContextInfo;
import com.bjsasc.plm.core.context.model.Contexted;
import com.bjsasc.plm.core.domain.DomainInfo;
import com.bjsasc.plm.core.identifier.Identified;
import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.system.discipline.DisciplineInfo;
import com.bjsasc.plm.core.system.phase.Phase;
import com.bjsasc.plm.core.system.phase.PhaseInfo;
import com.bjsasc.plm.core.system.phase.Phased;
import com.bjsasc.plm.core.system.principal.Organization;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.system.securitylevel.SecurityLevel;
import com.bjsasc.plm.core.system.securitylevel.SecurityLevelInfo;
import com.bjsasc.plm.core.system.securitylevel.SecurityLeveled;
import com.bjsasc.plm.core.type.ATObject;
import com.bjsasc.plm.core.util.DateTimeUtil;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.core.vc.model.OneOffVersioned;
import com.bjsasc.plm.core.vc.model.Versioned;

/**
 * �ַ���������ģ�͡�
 * 
 * @author gengancong 2013-2-22
 */
@SuppressWarnings({ "deprecation", "static-access" })
public class DistributeObject extends ATObject implements Identified, Manageable, Contexted, Phased,
		SecurityLeveled {

	/** serialVersionUID */
	private static final long serialVersionUID = -7695628512458182574L;

	/** CLASSID */
	public static final String CLASSID = DistributeObject.class.getSimpleName();

	/* =======================
	 * �����������
	 * =======================
	 */
	/** ��������  */
	private LifeCycleInfo lifeCycleInfo = new LifeCycleInfo();
	/** ��������ģ������  */
	private String lifeCycleTemplateName;
	/** ��������״̬����  */
	private String stateName;
	/** ��������ȫ״̬����  */
	private String stateNameAll;

	/* =======================
	 * ���ŵ���ַ�����link���
	 * =======================
	 */
	/** ���ŵ���ַ�����link����ģ��  */
	private DistributeOrderObjectLink distributeOrderObjectLink;
	/** ���ŵ���ַ�����link�� OID */
	private String distributeOrderObjectLinkOid;

	/* =======================
	 * �ַ���Ϣ���
	 * =======================
	 */
	/** �ַ���Ϣ */
	private DistributeInfo distributeInfo;
	/** ���յ�λ/�� */
	private String disInfoName;
	/** ����  */
	private String disInfoNum;
	/** ���ʱ��  */
	private String finishTime;

	/* =======================
	 * �ַ�����Դ���
	 * =======================
	 */
	/** �ַ�����Դ���� */
	private ObjectReference distributeData = new ObjectReference();
	/** �ַ�����Դ�ڲ���ʶ  */
	private String dataInnerId;
	/** �ַ�����Դ����  */
	private String dataClassId;
	/** ������������Ϣ  */
	private ContextInfo contextInfo;
	/** ����Ϣ  */
	private DomainInfo domainInfo;
	/** רҵ��Ϣ*/
	private DisciplineInfo disciplineInfo;
	/** �汾��Ϣ */
	private IterationInfo iterationInfo;
	/** ������Ϣ */
	private ObjectReference departmentRef;
	/** ��������Ϣ */
	private ManageInfo manageInfo;
	/** �׶���Ϣ */
	private PhaseInfo phaseInfo = new PhaseInfo();
	/** �ܼ���Ϣ */
	private SecurityLevelInfo securityLevelInfo;
	/** ���  */
	private String number;
	/** ����  */
	private String name;
	/** ��ע */
	private String note;
	/** �汾�� */
	private String version;
	/** ���� ����*/
	private String department;
	/** ���������� */
	private String createName;
	/** ����ʱ��*/
	private String createTime;
	/** �ַ�����Դ��Դ */
	private String dataFrom;
	/** �׶� */
	private String phase;
	/** �ܼ� */
	private String securityLevel;
	/** רҵ */
	private String discipline;
	/** ���� */
	private String operate = "<img src='../images/p_xls.gif' alt='�鿴ʵ���ļ�'/>";
	//TODO type code ���Զ���
	/** ���� */
	private String type;
	/** �ͺ� */
	private String code;
	/** ���Ƽӹ� */
	private DuplicateProcess duplicateProcess;
	/** ���� */
	private ReturnReason returnReason;
	/** �ַ��������� */
	private String linkUrl;
	/** ���շ����ʷַ����ݵ����� */
	private String accessUrl;
	/** �Ƿ��������� */
	private String isMaster;
	/** ҳ�� */
	private String pages;

	public String getAccessUrl() {
		return accessUrl;
	}

	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public ReturnReason getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(ReturnReason returnReason) {
		this.returnReason = returnReason;
	}

	public DuplicateProcess getDuplicateProcess() {
		return duplicateProcess;
	}

	public void setDuplicateProcess(DuplicateProcess duplicateProcess) {
		this.duplicateProcess = duplicateProcess;
	}
	
	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	/**
	 * ȡ�ð汾��
	 * @return version
	 */
	public String getVersion() {
		loadIterationName();
		return version;
	}

	/**
	 * ���ð汾��
	 * @param version Ҫ���õ� version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * ȡ�ò��� ����
	 * @return departmentName
	 */
	public String getDepartment() {
		loadDepartmentName();
		return department;
	}

	/**
	 * ���ò��� ����
	 * @param department Ҫ���õ� department
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * ȡ�ô���������
	 * @return createName
	 */
	public String getCreateName() {
		loadCreateByName();
		return createName;
	}

	/**
	 * ���ô���������
	 * @param createName Ҫ���õ� createName
	 */
	public void setCreateName(String createName) {
		this.createName = createName;
	}

	/**
	 * ȡ�ô���ʱ��
	 * @return createName
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * ���ô���ʱ��
	 * @param createTime Ҫ���õ� createTime
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * ȡ�÷ַ����ݶ���OID
	 * @return oid
	 */
	public String getOid() {
		return this.CLASSID + ":" + this.getInnerId();
	}

	/**
	 * ������������
	 */
	private void loadLifeCycle() {
		DistributeOrderObjectLink link = null;
		if (StringUtil.isNull(distributeOrderObjectLinkOid)) {
			// ���ŵ���ַ����ݶ���Link����
			DistributeOrderObjectLinkService linkService = DistributeHelper.getDistributeOrderObjectLinkService();
			List<DistributeOrderObjectLink> links = linkService
					.getDistributeOrderObjectLinkListByDistributeObjectOid(getOid());
			if (links == null || links.isEmpty()) {
				return;
			}
			link = links.get(0);
		} else {
			Persistable obj = Helper.getPersistService().getObject(distributeOrderObjectLinkOid);
			link = (DistributeOrderObjectLink) obj;
		}

		LifeCycleInfo linkLife = link.getLifeCycleInfo();
		if (linkLife != null) {
			setLifeCycleInfo(linkLife);
			setStateName(linkLife.getStateName());
			setStateNameAll(LifeCycleUtil.getDisplayState(linkLife));
			setLifeCycleTemplateName(LifeCycleUtil.getDisplayTemplate(linkLife.getLifeCycleTemplate()));
		}
	}

	/**
	 * ȡ����������ģ������
	 * @return lifeCycleTemplateName
	 */
	public String getLifeCycleTemplateName() {
		if (lifeCycleTemplateName == null || lifeCycleTemplateName.length() == 0) {
			loadLifeCycle();
		}
		return lifeCycleTemplateName;
	}

	/**
	 * ������������ģ������
	 * @param lifeCycleTemplateName Ҫ���õ� lifeCycleTemplateName
	 */
	public void setLifeCycleTemplateName(String lifeCycleTemplateName) {
		this.lifeCycleTemplateName = lifeCycleTemplateName;
	}

	/**
	 * ȡ����������״̬����
	 * @return stateName
	 */
	public String getStateName() {
		if (stateName == null || stateName.length() == 0) {
			loadLifeCycle();
		}
		return stateName;
	}

	/**
	 * ������������״̬����
	 * @param stateName Ҫ���õ� stateName
	 */
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	/**
	 * ȡ����������ȫ״̬����
	 * @return stateNameAll
	 */
	public String getStateNameAll() {
		return stateNameAll;
	}

	/**
	 * ������������ȫ״̬����
	 * @param stateNameAll Ҫ���õ� stateNameAll
	 */
	public void setStateNameAll(String stateNameAll) {
		this.stateNameAll = stateNameAll;
	}

	/**
	 * ȡ����������
	 * @return lifeCycleInfo
	 */
	public LifeCycleInfo getLifeCycleInfo() {
		loadLifeCycle();
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
	 *  ȡ�÷ַ�����Դ
	 * @return distributeData
	 */
	public ObjectReference getDistributeData() {
		loadDistributeData();
		return distributeData;
	}

	/**
	 * ���÷ַ�����Դ
	 * @param distributeData Ҫ���õ� distributeData
	 */
	public void setDistributeData(ObjectReference distributeData) {
		this.distributeData = distributeData;
	}

	/**
	 * ���طַ�����Դ
	 */
	private void loadDistributeData() {
		if (dataClassId != null && dataInnerId != null) {
			String oid = this.dataClassId + ":" + this.dataInnerId;
			distributeData.setObject(Helper.getPersistService().getObject(oid));
			distributeData.setClassId(this.dataClassId);
			distributeData.setInnerId(this.dataInnerId);
		}
	}

	/**
	 * ȡ�÷��ŵ��ͷַ�����link
	 * @return distributeOrderObjectLink
	 */
	public DistributeOrderObjectLink getDistributeOrderObjectLink() {
		return distributeOrderObjectLink;
	}

	/**
	 * ���÷��ŵ��ͷַ�����link
	 * @param distributeOrderObjectLink Ҫ���õ� distributeOrderObjectLink
	 */
	public void setDistributeOrderObjectLink(DistributeOrderObjectLink distributeOrderObjectLink) {
		this.distributeOrderObjectLink = distributeOrderObjectLink;
		this.isMaster = distributeOrderObjectLink.getIsMaster();
		setDistributeOrderObjectLinkOid(Helper.getOid(distributeOrderObjectLink));
	}

	/**
	 * ȡ�÷ַ�����ԴinnerId
	 * @return dataInnerId
	 */
	public String getDataInnerId() {
		return dataInnerId;
	}

	/**
	 * ���÷ַ�����ԴinnerId
	 * @param dataInnerId Ҫ���õ� dataInnerId
	 */
	public void setDataInnerId(String dataInnerId) {
		this.dataInnerId = dataInnerId;
	}

	/**
	 * ȡ�÷ַ�����ԴclassId
	 * @return dataClassId
	 */
	public String getDataClassId() {
		return dataClassId;
	}

	/**
	 * ���÷ַ�����ԴclassId
	 * @param dataClassId Ҫ���õ� dataClassId
	 */
	public void setDataClassId(String dataClassId) {
		this.dataClassId = dataClassId;
	}

	/**
	 * ȡ�ñ��
	 * @return number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * ���ñ��
	 * @param number Ҫ���õ� number
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * ȡ������
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * ��������
	 * @param name Ҫ���õ� name
	 */
	public void setName(String name) {
		this.name = name;
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
	 * ���ذ汾��
	 */
	public void loadIterationName() {
		if (iterationInfo != null) {
			String result = "";
			if(DTSiteConstant.DTSITE_APPVERSION_3_5.equals(dataFrom)){
				result = iterationInfo.getVersionNo();
			}else{
				String oid = this.dataClassId + ":" + this.dataInnerId;
				Persistable target = Helper.getPersistService().getObject(oid); 	
				
				if(target instanceof Versioned){
					Versioned version = (Versioned) target;
					result = version.getIterationInfo().getVersionNo() + "."
							+ version.getIterationInfo().getIterationNo();
				}
				if(target  instanceof OneOffVersioned){
					OneOffVersioned oneVersion = (OneOffVersioned) target;
					if(oneVersion.getOneOffVersionInfo() != null){
						result = oneVersion.getIterationInfo().getVersionNo() + "-"
								+ oneVersion.getOneOffVersionInfo().getOneOffVersionNo() + "."
								+ oneVersion.getIterationInfo().getIterationNo();
					}
				}
			}
			this.version = result;
		}
	}

	/**
	 * ȡ�ñ�ע
	 * @return note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * ���ñ�ע
	 * @param note Ҫ���õ� note
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/** 
	 * ȡ�ò�����Ϣ
	 * @return departmentRef
	 */
	public ObjectReference getDepartmentRef() {
		return departmentRef;
	}

	/** 
	 * ���ò�����Ϣ
	 * @param departmentRef Ҫ���õ�departmentRef
	 */
	public void setDepartmentRef(ObjectReference departmentRef) {
		this.departmentRef = departmentRef;
	}

	/**
	 *  ���ز�������
	 */
	public void loadDepartmentName() {
		this.department = "";
		if (departmentRef != null) {
			// TODO:��Ҫ�޸�
			Organization organization = (Organization) departmentRef.getObject();
			if (organization != null) {
				this.department = organization.getName();
			}
		}
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
	 * ȡ�ô���������
	 */
	public void loadCreateByName() {
		if (manageInfo != null) {
			User user = manageInfo.getCreateBy();
			if (user != null) {
				setCreateName(user.getName());
			}
		}
	}

	/** 
	 * ȡ�ô���ʱ��
	 */
	public void loadCreatTime() {
		if (manageInfo != null) {
			setCreateTime(DateTimeUtil.getDateDisplay(manageInfo.getCreateTime()));
		}
	}

	/** 
	 * ȡ�ý׶���Ϣ 
	 * @return phaseInfos
	 */
	public PhaseInfo getPhaseInfo() {
		return phaseInfo;
	}

	/**
	 * ���ý׶���Ϣ
	 * @param phaseInfo Ҫ���õ�phaseInfo
	 */
	public void setPhaseInfo(PhaseInfo phaseInfo) {
		this.phaseInfo = phaseInfo;
	}

	/**
	 * ȡ�ý׶�����
	 * @return String
	 */
	public void loadPhaseName() {
		if (phaseInfo != null) {
			Phase phase = phaseInfo.getPhase();
			if (phase != null) {
				setPhase(phase.getName());
			}
		}
	}

	/** 
	 * ȡ�ý׶�
	 * @return phaseInfos
	 */
	public String getPhase() {
		loadPhaseName();
		return phase;
	}

	/** 
	 * ���ý׶�
	 * @param phase Ҫ���õ�phase
	 */
	public void setPhase(String phase) {
		this.phase = phase;
	}

	/** 
	 * ȡ���ܼ�
	 * @return securityLevel
	 */
	public String getSecurityLevel() {
		loadSecurityLevelName();
		return securityLevel;
	}

	/** 
	 * �����ܼ�
	 * @param securityLevel Ҫ���õ�securityLevel
	 */
	public void setSecurityLevel(String securityLevel) {
		this.securityLevel = securityLevel;
	}

	/**
	 * ȡ���ܼ���Ϣ
	 * @return securityLevelInfo
	 */
	public SecurityLevelInfo getSecurityLevelInfo() {
		return securityLevelInfo;
	}

	/**
	 * �����ܼ���Ϣ
	 * @param securityLevelInfo Ҫ���õ� securityLevelInfo
	 */
	public void setSecurityLevelInfo(SecurityLevelInfo securityLevelInfo) {
		this.securityLevelInfo = securityLevelInfo;
	}

	/**
	 * ȡ���ܼ�����
	 */
	public void loadSecurityLevelName() {
		if (securityLevelInfo != null) {
			SecurityLevel sevurityLevel = securityLevelInfo.getSecurityLevel();
			if (sevurityLevel != null) {
				setSecurityLevel(sevurityLevel.getName());
			}
		}
	}

	/**
	 * ȡ�÷��ŵ���ַ�����link��OID
	 * @return distributeOrderObjectLinkOid
	 */
	public String getDistributeOrderObjectLinkOid() {
		return distributeOrderObjectLinkOid;
	}

	/**
	 * ���÷��ŵ���ַ�����link��OID
	 * @param distributeOrderObjectLinkOid Ҫ���õ� distributeOrderObjectLinkOid
	 */
	public void setDistributeOrderObjectLinkOid(String distributeOrderObjectLinkOid) {
		this.distributeOrderObjectLinkOid = distributeOrderObjectLinkOid;
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
	 * �ַ�����Դ��Դ
	 * @return dataFrom
	 */
	public String getDataFrom() {
		return dataFrom;
	}

	/**
	 * ���÷ַ�����Դ��Դ
	 * @param dataFrom Ҫ���õ� dataFrom
	 */
	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	/** 
	 * ȡ��רҵ��Ϣ
	 * @return disciplineInfo
	*/
	public DisciplineInfo getDisciplineInfo() {
		return disciplineInfo;
	}

	/** 
	 * ����רҵ��Ϣ
	 * @param disciplineInfo Ҫ���õ�disciplineInfo
	*/
	public void setDisciplineInfo(DisciplineInfo disciplineInfo) {
		this.disciplineInfo = disciplineInfo;
	}

	/** 
	 * ȡ��רҵ
	 * @return discipline
	*/
	public String getDiscipline() {
		losdDisciplineName();
		return discipline;
	}

	/** 
	 * ����רҵ
	 * @return discipline
	*/
	private void losdDisciplineName() {
		this.discipline = "";
	}

	/** 
	 * ����רҵ
	 * @param disciplineҪ���õ�discipline
	 */
	public void setDiscipline(String discipline) {
		this.discipline = discipline;
	}

	/**
	 * ȡ������
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * ��������
	 * @param type Ҫ���õ� type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * ȡ���ͺ�
	 * @return code
	 */
	public String getCode() {
		loadCode();
		return code;
	}

	/** 
	 * �����ͺ�
	*/
	private void loadCode() {
		this.code = "";
		if (contextInfo != null) {
			AbstractContext context = (AbstractContext) contextInfo.getContextRef().getObject();
			if (context != null) {
				setCode(context.getName());
			}
		}
	}

	/**
	 * �����ͺ�
	 * @param code Ҫ���õ� code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/* ===============================
	 * �ַ���Ϣ���
	 * =============================== 
	 */
	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public String getDisInfoName() {
		return disInfoName + "(" + getDisInfoNum() + ")";
	}

	public void setDisInfoName(String disInfoName) {
		this.disInfoName = disInfoName;
	}

	public String getDisInfoNum() {
		return disInfoNum;
	}

	public void setDisInfoNum(String disInfoNum) {
		this.disInfoNum = disInfoNum;
	}

	public DistributeInfo getDistributeInfo() {
		return distributeInfo;
	}

	public void setDistributeInfo(DistributeInfo distributeInfo) {
		this.distributeInfo = distributeInfo;
	}
	
	public DistributeObject cloneDisObj() {
		DistributeObject obj = new DistributeObject();
		obj.setInnerId(getInnerId());
		obj.setClassId(getClassId());
		obj.setDataInnerId(getDataInnerId());
		obj.setDataClassId(getDataClassId());
		obj.setDataFrom(getDataFrom());
		obj.setDisciplineInfo(getDisciplineInfo());
		obj.setIterationInfo(getIterationInfo());
		obj.setDepartmentRef(getDepartmentRef());
		obj.setPhaseInfo(getPhaseInfo());
		obj.setSecurityLevelInfo(getSecurityLevelInfo());
		obj.setNumber(getNumber());
		obj.setName(getName());
		obj.setNote(getNote());
		obj.setVersion(getVersion());
		obj.setDepartment(getDepartment());
		obj.setCreateName(getCreateName());
		obj.setCreateTime(getCreateTime());
		obj.setPhase(getPhase());
		obj.setSecurityLevel(getSecurityLevel());
		obj.setDiscipline(getDiscipline());
		obj.setManageInfo(getManageInfo());
		obj.setContextInfo(getContextInfo());
		obj.setDomainInfo(getDomainInfo());
		obj.setLifeCycleInfo(getLifeCycleInfo());
		obj.setPages(getPages());
		return obj;
	}

	public void setPhaseRef(ObjectReference phaseRef) {
		phaseInfo.setPhaseRef(phaseRef);
	}

	public String getIsMaster() {
		return isMaster;
	}

	public void setIsMaster(String isMaster) {
		this.isMaster = isMaster;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}
}
