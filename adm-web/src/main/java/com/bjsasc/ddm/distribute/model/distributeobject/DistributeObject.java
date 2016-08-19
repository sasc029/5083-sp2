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
 * 分发对象数据模型。
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
	 * 生命周期相关
	 * =======================
	 */
	/** 生命周期  */
	private LifeCycleInfo lifeCycleInfo = new LifeCycleInfo();
	/** 生命周期模板名称  */
	private String lifeCycleTemplateName;
	/** 生命周期状态名称  */
	private String stateName;
	/** 生命周期全状态名称  */
	private String stateNameAll;

	/* =======================
	 * 发放单与分发数据link相关
	 * =======================
	 */
	/** 发放单与分发数据link数据模型  */
	private DistributeOrderObjectLink distributeOrderObjectLink;
	/** 发放单与分发数据link表 OID */
	private String distributeOrderObjectLinkOid;

	/* =======================
	 * 分发信息相关
	 * =======================
	 */
	/** 分发信息 */
	private DistributeInfo distributeInfo;
	/** 接收单位/人 */
	private String disInfoName;
	/** 份数  */
	private String disInfoNum;
	/** 完成时间  */
	private String finishTime;

	/* =======================
	 * 分发数据源相关
	 * =======================
	 */
	/** 分发数据源对象 */
	private ObjectReference distributeData = new ObjectReference();
	/** 分发数据源内部标识  */
	private String dataInnerId;
	/** 分发数据源类名  */
	private String dataClassId;
	/** 所属上下文信息  */
	private ContextInfo contextInfo;
	/** 域信息  */
	private DomainInfo domainInfo;
	/** 专业信息*/
	private DisciplineInfo disciplineInfo;
	/** 版本信息 */
	private IterationInfo iterationInfo;
	/** 部门信息 */
	private ObjectReference departmentRef;
	/** 管理者信息 */
	private ManageInfo manageInfo;
	/** 阶段信息 */
	private PhaseInfo phaseInfo = new PhaseInfo();
	/** 密集信息 */
	private SecurityLevelInfo securityLevelInfo;
	/** 编号  */
	private String number;
	/** 名称  */
	private String name;
	/** 备注 */
	private String note;
	/** 版本号 */
	private String version;
	/** 部门 名称*/
	private String department;
	/** 创建者名称 */
	private String createName;
	/** 创建时间*/
	private String createTime;
	/** 分发数据源来源 */
	private String dataFrom;
	/** 阶段 */
	private String phase;
	/** 密集 */
	private String securityLevel;
	/** 专业 */
	private String discipline;
	/** 操作 */
	private String operate = "<img src='../images/p_xls.gif' alt='查看实体文件'/>";
	//TODO type code 测试定义
	/** 类型 */
	private String type;
	/** 型号 */
	private String code;
	/** 复制加工 */
	private DuplicateProcess duplicateProcess;
	/** 回退 */
	private ReturnReason returnReason;
	/** 分发数据链接 */
	private String linkUrl;
	/** 接收方访问分发数据的链接 */
	private String accessUrl;
	/** 是否是主对象 */
	private String isMaster;
	/** 页数 */
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
	 * 取得版本号
	 * @return version
	 */
	public String getVersion() {
		loadIterationName();
		return version;
	}

	/**
	 * 设置版本号
	 * @param version 要设置的 version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 取得部门 名称
	 * @return departmentName
	 */
	public String getDepartment() {
		loadDepartmentName();
		return department;
	}

	/**
	 * 设置部门 名称
	 * @param department 要设置的 department
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * 取得创建者名称
	 * @return createName
	 */
	public String getCreateName() {
		loadCreateByName();
		return createName;
	}

	/**
	 * 设置创建者名称
	 * @param createName 要设置的 createName
	 */
	public void setCreateName(String createName) {
		this.createName = createName;
	}

	/**
	 * 取得创建时间
	 * @return createName
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * 设置创建时间
	 * @param createTime 要设置的 createTime
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * 取得分发数据对象OID
	 * @return oid
	 */
	public String getOid() {
		return this.CLASSID + ":" + this.getInnerId();
	}

	/**
	 * 加载生命周期
	 */
	private void loadLifeCycle() {
		DistributeOrderObjectLink link = null;
		if (StringUtil.isNull(distributeOrderObjectLinkOid)) {
			// 发放单与分发数据对象Link服务
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
	 * 取得生命周期模板名称
	 * @return lifeCycleTemplateName
	 */
	public String getLifeCycleTemplateName() {
		if (lifeCycleTemplateName == null || lifeCycleTemplateName.length() == 0) {
			loadLifeCycle();
		}
		return lifeCycleTemplateName;
	}

	/**
	 * 设置生命周期模板名称
	 * @param lifeCycleTemplateName 要设置的 lifeCycleTemplateName
	 */
	public void setLifeCycleTemplateName(String lifeCycleTemplateName) {
		this.lifeCycleTemplateName = lifeCycleTemplateName;
	}

	/**
	 * 取得生命周期状态名称
	 * @return stateName
	 */
	public String getStateName() {
		if (stateName == null || stateName.length() == 0) {
			loadLifeCycle();
		}
		return stateName;
	}

	/**
	 * 设置生命周期状态名称
	 * @param stateName 要设置的 stateName
	 */
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	/**
	 * 取得生命周期全状态名称
	 * @return stateNameAll
	 */
	public String getStateNameAll() {
		return stateNameAll;
	}

	/**
	 * 设置生命周期全状态名称
	 * @param stateNameAll 要设置的 stateNameAll
	 */
	public void setStateNameAll(String stateNameAll) {
		this.stateNameAll = stateNameAll;
	}

	/**
	 * 取得生命周期
	 * @return lifeCycleInfo
	 */
	public LifeCycleInfo getLifeCycleInfo() {
		loadLifeCycle();
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
	 *  取得分发数据源
	 * @return distributeData
	 */
	public ObjectReference getDistributeData() {
		loadDistributeData();
		return distributeData;
	}

	/**
	 * 设置分发数据源
	 * @param distributeData 要设置的 distributeData
	 */
	public void setDistributeData(ObjectReference distributeData) {
		this.distributeData = distributeData;
	}

	/**
	 * 加载分发数据源
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
	 * 取得发放单和分发数据link
	 * @return distributeOrderObjectLink
	 */
	public DistributeOrderObjectLink getDistributeOrderObjectLink() {
		return distributeOrderObjectLink;
	}

	/**
	 * 设置发放单和分发数据link
	 * @param distributeOrderObjectLink 要设置的 distributeOrderObjectLink
	 */
	public void setDistributeOrderObjectLink(DistributeOrderObjectLink distributeOrderObjectLink) {
		this.distributeOrderObjectLink = distributeOrderObjectLink;
		this.isMaster = distributeOrderObjectLink.getIsMaster();
		setDistributeOrderObjectLinkOid(Helper.getOid(distributeOrderObjectLink));
	}

	/**
	 * 取得分发数据源innerId
	 * @return dataInnerId
	 */
	public String getDataInnerId() {
		return dataInnerId;
	}

	/**
	 * 设置分发数据源innerId
	 * @param dataInnerId 要设置的 dataInnerId
	 */
	public void setDataInnerId(String dataInnerId) {
		this.dataInnerId = dataInnerId;
	}

	/**
	 * 取得分发数据源classId
	 * @return dataClassId
	 */
	public String getDataClassId() {
		return dataClassId;
	}

	/**
	 * 设置分发数据源classId
	 * @param dataClassId 要设置的 dataClassId
	 */
	public void setDataClassId(String dataClassId) {
		this.dataClassId = dataClassId;
	}

	/**
	 * 取得编号
	 * @return number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * 设置编号
	 * @param number 要设置的 number
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * 取得名称
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * @param name 要设置的 name
	 */
	public void setName(String name) {
		this.name = name;
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
	 * 加载版本号
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
	 * 取得备注
	 * @return note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * 设置备注
	 * @param note 要设置的 note
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/** 
	 * 取得部门信息
	 * @return departmentRef
	 */
	public ObjectReference getDepartmentRef() {
		return departmentRef;
	}

	/** 
	 * 设置部门信息
	 * @param departmentRef 要设置的departmentRef
	 */
	public void setDepartmentRef(ObjectReference departmentRef) {
		this.departmentRef = departmentRef;
	}

	/**
	 *  加载部门名称
	 */
	public void loadDepartmentName() {
		this.department = "";
		if (departmentRef != null) {
			// TODO:需要修改
			Organization organization = (Organization) departmentRef.getObject();
			if (organization != null) {
				this.department = organization.getName();
			}
		}
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
	 * 取得创建者名称
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
	 * 取得创建时间
	 */
	public void loadCreatTime() {
		if (manageInfo != null) {
			setCreateTime(DateTimeUtil.getDateDisplay(manageInfo.getCreateTime()));
		}
	}

	/** 
	 * 取得阶段信息 
	 * @return phaseInfos
	 */
	public PhaseInfo getPhaseInfo() {
		return phaseInfo;
	}

	/**
	 * 设置阶段信息
	 * @param phaseInfo 要设置的phaseInfo
	 */
	public void setPhaseInfo(PhaseInfo phaseInfo) {
		this.phaseInfo = phaseInfo;
	}

	/**
	 * 取得阶段名称
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
	 * 取得阶段
	 * @return phaseInfos
	 */
	public String getPhase() {
		loadPhaseName();
		return phase;
	}

	/** 
	 * 设置阶段
	 * @param phase 要设置的phase
	 */
	public void setPhase(String phase) {
		this.phase = phase;
	}

	/** 
	 * 取得密集
	 * @return securityLevel
	 */
	public String getSecurityLevel() {
		loadSecurityLevelName();
		return securityLevel;
	}

	/** 
	 * 设置密集
	 * @param securityLevel 要设置的securityLevel
	 */
	public void setSecurityLevel(String securityLevel) {
		this.securityLevel = securityLevel;
	}

	/**
	 * 取得密集信息
	 * @return securityLevelInfo
	 */
	public SecurityLevelInfo getSecurityLevelInfo() {
		return securityLevelInfo;
	}

	/**
	 * 设置密集信息
	 * @param securityLevelInfo 要设置的 securityLevelInfo
	 */
	public void setSecurityLevelInfo(SecurityLevelInfo securityLevelInfo) {
		this.securityLevelInfo = securityLevelInfo;
	}

	/**
	 * 取得密集名称
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
	 * 取得发放单与分发数据link的OID
	 * @return distributeOrderObjectLinkOid
	 */
	public String getDistributeOrderObjectLinkOid() {
		return distributeOrderObjectLinkOid;
	}

	/**
	 * 设置发放单与分发数据link的OID
	 * @param distributeOrderObjectLinkOid 要设置的 distributeOrderObjectLinkOid
	 */
	public void setDistributeOrderObjectLinkOid(String distributeOrderObjectLinkOid) {
		this.distributeOrderObjectLinkOid = distributeOrderObjectLinkOid;
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
	 * 分发数据源来源
	 * @return dataFrom
	 */
	public String getDataFrom() {
		return dataFrom;
	}

	/**
	 * 设置分发数据源来源
	 * @param dataFrom 要设置的 dataFrom
	 */
	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	/** 
	 * 取得专业信息
	 * @return disciplineInfo
	*/
	public DisciplineInfo getDisciplineInfo() {
		return disciplineInfo;
	}

	/** 
	 * 设置专业信息
	 * @param disciplineInfo 要设置的disciplineInfo
	*/
	public void setDisciplineInfo(DisciplineInfo disciplineInfo) {
		this.disciplineInfo = disciplineInfo;
	}

	/** 
	 * 取得专业
	 * @return discipline
	*/
	public String getDiscipline() {
		losdDisciplineName();
		return discipline;
	}

	/** 
	 * 加载专业
	 * @return discipline
	*/
	private void losdDisciplineName() {
		this.discipline = "";
	}

	/** 
	 * 设置专业
	 * @param discipline要设置的discipline
	 */
	public void setDiscipline(String discipline) {
		this.discipline = discipline;
	}

	/**
	 * 取得类型
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置类型
	 * @param type 要设置的 type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 取得型号
	 * @return code
	 */
	public String getCode() {
		loadCode();
		return code;
	}

	/** 
	 * 加载型号
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
	 * 设置型号
	 * @param code 要设置的 code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/* ===============================
	 * 分发信息相关
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
