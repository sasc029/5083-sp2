package com.bjsasc.ddm.distribute.model.distributeorderobjectlink;

import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleInfo;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.context.model.ContextInfo;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.type.ATLink;
import com.bjsasc.plm.type.restrict.select.Select;
import com.bjsasc.plm.type.restrict.select.SelectOption;

/**
 * 发放单与分发数据link数据模型。
 * 
 * @author gengancong 2013-2-22
 */
@SuppressWarnings("deprecation")
public class DistributeOrderObjectLink extends ATLink implements LifeCycleManaged {

	/** serialVersionUID */
	private static final long serialVersionUID = -310906813258347462L;

	/**
	 * 构造方法。
	 */
	public DistributeOrderObjectLink() {
		setClassId(CLASSID);
	}

	/** CLASSID */
	public static final String CLASSID = DistributeOrderObjectLink.class.getSimpleName();

	/** 是否在这个发放单内是父节点（0：不是，1：是） */
	private String isParent;

	/** 是否在这个发放单内是主对象（0：不是，1：是） */
	private String isMaster;

	/** 生命周期属性信息对象 */
	private LifeCycleInfo lifeCycleInfo = new LifeCycleInfo();
	
	/** 上下文属性信息对象 */
	private ContextInfo contextInfo;
	
	/** 分发信息完工期限 */
	private long disDeadLine;
	
	/** 紧急程度（0为普通，1为加急） */
	private String disUrgent;

	/** 分发方式（0为正式分发，1为一次性分发）*/
	private String disStyle;

	public ContextInfo getContextInfo() {
		return contextInfo;
	}

	public void setContextInfo(ContextInfo contextInfo) {
		this.contextInfo = contextInfo;
	}

	/**
	 * @return isParent
	 */
	public String getIsParent() {
		return isParent;
	}

	/**
	 * @param isParent 要设置的 isParent
	 */
	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	/**
	 * @return lifeCycleInfo
	 */
	public LifeCycleInfo getLifeCycleInfo() {
		return lifeCycleInfo;
	}

	/**
	 * @param lifeCycleInfo 要设置的 lifeCycleInfo
	 */
	public void setLifeCycleInfo(LifeCycleInfo lifeCycleInfo) {
		this.lifeCycleInfo = lifeCycleInfo;
	}

	public String getIsMaster() {
		return isMaster;
	}

	public void setIsMaster(String isMaster) {
		this.isMaster = isMaster;
	}

	public long getDisDeadLine() {
		return disDeadLine;
	}

	public void setDisDeadLine(long disDeadLine) {
		this.disDeadLine = disDeadLine;
	}

	public String getDisUrgent() {
		return disUrgent;
	}

	public void setDisUrgent(String disUrgent) {
		this.disUrgent = disUrgent;
	}
	
	/**
	 * @return disUrgentName
	 */
	public String getDisUrgentName() {
		Select select = Helper.getRestrictManager().getSelect("disUrgent");
		SelectOption selectOption = select.getOptionMap().get(disUrgent);
		return selectOption.getText();
	}

	/**
	 * @return disStyle
	 */
	public String getDisStyle() {
		return disStyle;
	}

	/**
	 * @param disStyle 要设置的 disStyle
	 */
	public void setDisStyle(String disStyle) {
		this.disStyle = disStyle;
	}

}
