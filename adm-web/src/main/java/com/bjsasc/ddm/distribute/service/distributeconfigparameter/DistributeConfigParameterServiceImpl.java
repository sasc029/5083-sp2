package com.bjsasc.ddm.distribute.service.distributeconfigparameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.distribute.model.distributeconfigparameter.DistributeConfigParameter;
import com.bjsasc.ddm.distribute.model.distributeobjecttype.DistributeObjectType;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.context.ContextHelper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.model.ContextInfo;

/**
 * 发放管理参数配置服务实现类。
 * 
 * @author gengancong 2014-5-5
 */
public class DistributeConfigParameterServiceImpl implements
		DistributeConfigParameterService {

	@Override
	public boolean exists(String contextOid, String paramId) {
		List<Map<String, Object>> dcpList = findByContextOid(contextOid,
				paramId);

		if (dcpList == null || dcpList.isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public void deleteConfigData(String contextOid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addConfigData(String contextOid, String paramId,
			String paramName, String defaultValue, String currentValue,
			String state, String description) {
		Context context = ContextHelper.getService().getContext(contextOid);
		ContextInfo contextInfo = new ContextInfo();
		contextInfo.setContext(context);
		DistributeConfigParameter dot = (DistributeConfigParameter) PersistUtil.createObject(DistributeConfigParameter.CLASSID);
		dot.setClassId(DistributeObjectType.CLASSID);
		dot.setParamId(paramId);
		dot.setParamName(paramName);
		dot.setDefaultValue(defaultValue);
		dot.setCurrentValue(currentValue);
		dot.setState(state);
		dot.setDescription(description);
		dot.setContextInfo(contextInfo);
		dot.setDomainInfo(context.getDomainInfo());
		Helper.getPersistService().save(dot);
	}

	@Override
	public List<Map<String, Object>> findByContextOid(String contextOid,
			String paramId) {
		String sql = "select * from DDM_DIS_CONFIG_PARAMETER t "
				+ " where t.contextid=? and t.contextclassid=? "
				+ "and t.paramId=? ";
		String innerId = Helper.getInnerId(contextOid);
		String classId = Helper.getClassId(contextOid);
		List<Map<String, Object>> dcpList = Helper.getPersistService().query(
				sql, innerId, classId, paramId);
		return dcpList;
	}
	
	public Map<String, List<String>> getAllParam(){
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		String hql = "from DistributeConfigParameter ";
		List<DistributeConfigParameter> list = Helper.getPersistService().find(hql);
		for(DistributeConfigParameter disPar : list){
			List<String> contextList = new ArrayList<String>();
			String key = disPar.getParamId();
			contextList = map.get(key);
			if(contextList == null){
				contextList = new ArrayList<String>();
			}
			String contextOid = "";
			try{
				contextOid = disPar.getContextInfo().getContext().getOid();
			}catch(Exception e){
				continue;
			}
			contextList.add(contextOid);
			map.put(key, contextList);
		}
		return map;
	}

}
