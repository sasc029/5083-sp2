package com.bjsasc.ddm.common;

import java.util.List;

import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleInfo;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleTemplate;
import com.bjsasc.platform.objectmodel.business.lifeCycle.State;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.util.StringUtil;

/**
 * 生命周期共通处理。
 * 
 * @author gengancong 2013-3-26
 */
public class LifeCycleUtil {
    private LifeCycleUtil(){};
	public static String getDisplayTemplate(String templateIID) {
		String display = "";

		LifeCycleTemplate template = Helper.getLifeCycleService().getLifeCycleTemplate(templateIID);
		if (template == null) {
			return display;
		}

		display = template.getName();

		return display;
	}

	/**
	 * 取得生命周期模板名称。
	 * 
	 * @param link DistributeOrderObjectLink
	 * @return String
	 */
	public static String getLifeCycleTemplate(DistributeOrderObjectLink link) {
		String result = "";

		if (link == null) {
			return result;
		}
		LifeCycleInfo lifeCycleInfo = link.getLifeCycleInfo();
		if (lifeCycleInfo == null) {
			return result;
		}
		String lifeCycleTemplate = lifeCycleInfo.getLifeCycleTemplate();
		if (StringUtil.isNull(lifeCycleTemplate)) {
			return result;
		}

		LifeCycleTemplate template = Helper.getLifeCycleService().getLifeCycleTemplate(lifeCycleTemplate);
		if (template == null) {
			return result;
		}

		result = template.getName();

		return result;

	}

	public static String getDisplayState(LifeCycleInfo info) {
		String display = "";
		if (info == null) {
			return display;
		}

		String templateIID = info.getLifeCycleTemplate();
		if (templateIID == null) {
			return display;
		}

		LifeCycleTemplate template = Helper.getLifeCycleService().getLifeCycleTemplate(templateIID);
		if (template == null) {
			return display;
		}

		String stateName = info.getStateName();

		List<State> states = Helper.getLifeCycleService().findStates(template);
		State currentState = null;
		for (State state : states) {
			if (state.getName().equals(stateName)) {
				currentState = state;
				break;
			}
		}

		for (int i = 0; i < states.size(); i++) {
			State state = states.get(i);

			if (state.equals( currentState)) {
				display += "<B>" + state.getName() + "</B>";
			} else {
				display += state.getName();
			}

			if (i < states.size() - 1) {
				display += "&nbsp;-&nbsp;";
			}
		}

		return display;
	}
}
