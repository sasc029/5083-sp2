package com.bjsasc.ddm.distribute.model.distributeorder;

import com.bjsasc.ddm.distribute.model.returnreason.ReturnReason;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleInfo;
import com.bjsasc.platform.objectmodel.business.persist.ObjectReference;
import com.bjsasc.platform.objectmodel.business.persist.PTFactor;
import com.bjsasc.plm.core.approve.Approved;
import com.bjsasc.plm.core.context.model.ContextInfo;
import com.bjsasc.plm.core.context.model.Contexted;
import com.bjsasc.plm.core.domain.DomainInfo;
import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.identifier.UniqueIdentified;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.type.ATObject;
import com.bjsasc.plm.core.util.DateTimeUtil;

/**
 * 发放单数据模型。
 * 
 * @author gengancong 2013-2-22
 */
@SuppressWarnings("deprecation")
public class DistributeOrder extends ATObject implements UniqueIdentified, Contexted, Manageable, LifeCycleManaged,
		Domained, Approved {

	/** serialVersionUID */
	private static final long serialVersionUID = -5416508837561844886L;
  
	/**
	 * 构造方法。
	 */
	public DistributeOrder() {

	}

	/** CLASSID */
	public static final String CLASSID = DistributeOrder.class.getSimpleName();

	/** 生命周期属性信息对象 */
	private LifeCycleInfo lifeCycleInfo = new LifeCycleInfo();

	/** 管理信息 */
	private ManageInfo manageInfo;

	/** 域信息 */
	private DomainInfo domainInfo;

	/** 上下文信息 */
	private ContextInfo contextInfo;

	/** 编号 */
	private String number;

	/** 名称 */
	private String name;

	/** 单据类型(0发放单、1补发发放单、2回收单、3销毁单) */
	private String orderType;

	/** 备注 */
	private String note;

	/** 回退原因 */
	private String returnReason;

	/** 回退时间 */
	private long returnTime;
	
	/** 回退人 */
	private String userName;
	
	/** 回退对象 */
	private ReturnReason retReason;
	
	/** 发起人内部标识 */
	private String submitUserId;
	
	/** 发起人类标识 */
	private String submitUserClassId;
	
	/** 发起人名称 */
	private String submitUserName;
	
	/** 发起站点内部标识 */
	private String siteId;
	
	/** 发起站点类标识 */
	private String siteClassId;
	
	/** 发起站点名称 */
	private String siteName;
	
	private String masterDataClassID;

	public ReturnReason getRetReason() {
		return retReason;
	}

	public void setRetReason(ReturnReason retReason) {
		this.retReason = retReason;
	}

	public String getContextName() {
		if (contextInfo != null) {
			ObjectReference contextRef = contextInfo.getContextRef();
			if (contextRef != null) {
				PTFactor object = contextRef.getObject();
				if (object != null) {
					return "";
				}
			}
		}
		return "";
	}

	public String getStateName() {
		if (lifeCycleInfo != null) {
			return lifeCycleInfo.getStateName();
		}
		return "";
	}

	public String getCreateByName() {
		if (manageInfo != null) {
			User user = manageInfo.getCreateBy();
			if (user != null) {
				return user.getName();
			}
		}
		return "";
	}

	public String getCreatTime() {
		return DateTimeUtil.getDateDisplay(manageInfo.getCreateTime());
	}

	public String getOid() {
		return this.getClassId() + ":" + this.getInnerId();
	}

	public LifeCycleInfo getLifeCycleInfo() {
		return lifeCycleInfo;
	}

	public void setLifeCycleInfo(LifeCycleInfo lifeCycleInfo) {
		this.lifeCycleInfo = lifeCycleInfo;
	}

	public ManageInfo getManageInfo() {
		return manageInfo;
	}

	public void setManageInfo(ManageInfo manageInfo) {
		this.manageInfo = manageInfo;
	}

	public ContextInfo getContextInfo() {
		return contextInfo;
	}

	public void setContextInfo(ContextInfo contextInfo) {
		this.contextInfo = contextInfo;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public long getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(long returnTime) {
		this.returnTime = returnTime;
	}

	public DomainInfo getDomainInfo() {
		return domainInfo;
	}

	public void setDomainInfo(DomainInfo domainInfo) {
		this.domainInfo = domainInfo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getSubmitUserId() {
		return submitUserId;
	}

	public void setSubmitUserId(String submitUserId) {
		this.submitUserId = submitUserId;
	}

	public String getSubmitUserClassId() {
		return submitUserClassId;
	}

	public void setSubmitUserClassId(String submitUserClassId) {
		this.submitUserClassId = submitUserClassId;
	}

	public String getSubmitUserName() {
		return submitUserName;
	}

	public void setSubmitUserName(String submitUserName) {
		this.submitUserName = submitUserName;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSiteClassId() {
		return siteClassId;
	}

	public void setSiteClassId(String siteClassId) {
		this.siteClassId = siteClassId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public DistributeOrder cloneDisOrder() {
		DistributeOrder order = new DistributeOrder();
		order.setInnerId(getInnerId());
		order.setClassId(getClassId());
		order.setNumber(getNumber());
		order.setName(getName());
		order.setNote(getNote());
		order.setManageInfo(getManageInfo());
		order.setContextInfo(getContextInfo());
		order.setDomainInfo(getDomainInfo());
		order.setLifeCycleInfo(getLifeCycleInfo());
		return order;
	}

	public String getMasterDataClassID() {
		return masterDataClassID;
	}

	public void setMasterDataClassID(String masterDataClassID) {
		this.masterDataClassID = masterDataClassID;
	}
	
}
