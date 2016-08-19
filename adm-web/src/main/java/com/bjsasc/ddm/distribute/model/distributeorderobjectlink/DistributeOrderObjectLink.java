package com.bjsasc.ddm.distribute.model.distributeorderobjectlink;

import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleInfo;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.context.model.ContextInfo;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.type.ATLink;
import com.bjsasc.plm.type.restrict.select.Select;
import com.bjsasc.plm.type.restrict.select.SelectOption;

/**
 * ���ŵ���ַ�����link����ģ�͡�
 * 
 * @author gengancong 2013-2-22
 */
@SuppressWarnings("deprecation")
public class DistributeOrderObjectLink extends ATLink implements LifeCycleManaged {

	/** serialVersionUID */
	private static final long serialVersionUID = -310906813258347462L;

	/**
	 * ���췽����
	 */
	public DistributeOrderObjectLink() {
		setClassId(CLASSID);
	}

	/** CLASSID */
	public static final String CLASSID = DistributeOrderObjectLink.class.getSimpleName();

	/** �Ƿ���������ŵ����Ǹ��ڵ㣨0�����ǣ�1���ǣ� */
	private String isParent;

	/** �Ƿ���������ŵ�����������0�����ǣ�1���ǣ� */
	private String isMaster;

	/** ��������������Ϣ���� */
	private LifeCycleInfo lifeCycleInfo = new LifeCycleInfo();
	
	/** ������������Ϣ���� */
	private ContextInfo contextInfo;
	
	/** �ַ���Ϣ�깤���� */
	private long disDeadLine;
	
	/** �����̶ȣ�0Ϊ��ͨ��1Ϊ�Ӽ��� */
	private String disUrgent;

	/** �ַ���ʽ��0Ϊ��ʽ�ַ���1Ϊһ���Էַ���*/
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
	 * @param isParent Ҫ���õ� isParent
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
	 * @param lifeCycleInfo Ҫ���õ� lifeCycleInfo
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
	 * @param disStyle Ҫ���õ� disStyle
	 */
	public void setDisStyle(String disStyle) {
		this.disStyle = disStyle;
	}

}
