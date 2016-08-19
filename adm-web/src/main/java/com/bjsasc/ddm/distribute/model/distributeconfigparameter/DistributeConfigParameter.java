package com.bjsasc.ddm.distribute.model.distributeconfigparameter;

import com.bjsasc.plm.core.context.model.ContextInfo;
import com.bjsasc.plm.core.context.model.Contexted;
import com.bjsasc.plm.core.domain.DomainInfo;
import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.managed.model.ManageInfo;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.type.ATObject;

/**
 * 发放管理参数配置模型。
 * 
 * @author gengancong 2014-5-8
 */
public class DistributeConfigParameter extends ATObject implements Domained,
		Contexted, Manageable {

	/** serialVersionUID */
	private static final long serialVersionUID = -2093509238732507158L;

	/** CLASSID */
	public static final String CLASSID = DistributeConfigParameter.class
			.getSimpleName();

	/** 参数ID */
	private String paramId;
	/** 参数名称 */
	private String paramName;
	/** 默认值 */
	private String defaultValue;
	/** 当前值 */
	private String currentValue;
	/** 说明 */
	private String description;
	/** 类别 */
	private String state;

	/** 所属上下文信息 */
	private ContextInfo contextInfo;
	/** 管理信息 */
	private ManageInfo manageInfo;
	/** 域信息 */
	private DomainInfo domainInfo;

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

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
