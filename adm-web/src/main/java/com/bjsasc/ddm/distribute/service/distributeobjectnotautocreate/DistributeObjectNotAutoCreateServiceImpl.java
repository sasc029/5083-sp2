package com.bjsasc.ddm.distribute.service.distributeobjectnotautocreate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.axis2.databinding.types.soapencoding.Array;

import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeobjectnotautocreate.DistributeObjectNotAutoCreate;
import com.bjsasc.ddm.distribute.model.distributeobjecttype.DistributeObjectType;
import com.bjsasc.ddm.distribute.service.distributeconfigparameter.DistributeConfigParameterService;
import com.bjsasc.platform.objectmodel.business.persist.PTFactor;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.context.ContextHelper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.model.ContextInfo;
import com.bjsasc.plm.core.context.model.Contexted;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.principal.Principal;
import com.bjsasc.plm.core.system.principal.Role;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.grid.GridHelper;
import com.cascc.avidm.util.SplitString;

public class DistributeObjectNotAutoCreateServiceImpl implements
		DistributeObjectNotAutoCreateService {

	@Override
	public List<Map<String, Object>> findByContextOid(String contextOid) {
		String spot = "ListDistributeObjectNotAutoCreate";
		String spotInstance = "ListDistributeObjectNotAutoCreate";
		return findByContextOid(contextOid, spot, spotInstance);
	}

	@Override
	public List<Map<String, Object>> findByContextOid(String contextOid,
			String spot, String spotInstance) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		String innerId = Helper.getInnerId(contextOid);
		String classId = Helper.getClassId(contextOid);

		String sql = "select * from DDM_DIS_OBJECT_NOTAUTOCREATE t "
				+ " where t.contextid=? and t.contextclassid=? "
				+ " order by t.updatetime desc";

		List<DistributeObjectNotAutoCreate> dotList = PersistHelper.getService().query(
				sql, DistributeObjectNotAutoCreate.class, innerId, classId);

		List<String> keys = GridHelper.getService()
				.getMyLatestGridViewColumnIds(spot, spotInstance);

		for (Object obj : dotList) {
			Map<String, Object> map = Helper.getTypeManager().format(
					(PTFactor) obj, keys, false);
			map.put(KeyS.ACCESS, true);
			resultList.add(map);
		}
		return resultList;
	}

	@Override
	public void deleteTypeData(String contextOid, String typeNames) {
		String cid = Helper.getInnerId(contextOid);
		String ccd = Helper.getClassId(contextOid);
		List<String> typeidList = SplitString.string2List(typeNames, "#@#");

		String sql = "delete from DDM_DIS_OBJECT_NOTAUTOCREATE where "
				+ " contextid=? and contextclassid=? "
				+ " and typeName=? ";

		for (String typeName : typeidList) {
			Helper.getPersistService().bulkUpdateBySql(sql, cid, ccd, typeName);
		}
		
		if(!exists(contextOid)){
			deleteConfigData(contextOid,"DistributeObjectNotAutoCreate");
		}
	}

	@Override
	public void addTypeData(String contextOid,
			List<Map<String, String>> listTypeMap) {
		if (listTypeMap != null) {
			Context context = ContextHelper.getService().getContext(contextOid);
			ContextInfo contextInfo = new ContextInfo();
			contextInfo.setContext(context);

			DistributeConfigParameterService cps = DistributeHelper
					.getDistributeConfigParameterService();
			String paramId = "DistributeObjectNotAutoCreate";
			if (!cps.exists(contextOid, paramId)) {
				String paramName = "对象模型不自动发放配置";
				String defaultValue = "ok";
				String currentValue = "ok";
				String state = "0";
				String description = "如果此数据存在说明此上下文中配置有效。";
				cps.addConfigData(contextOid, paramId, paramName, defaultValue,
						currentValue, state, description);
			}

			for (Map<String, String> mm : listTypeMap) {
				String typeId = mm.get("DOT_TYPE_ID");
				String typeName = mm.get("DOT_TYPE_NAME");
				if (exists(contextOid, typeId)) {
					continue;
				}

				DistributeObjectNotAutoCreate donac = (DistributeObjectNotAutoCreate) PersistUtil
						.createObject(DistributeObjectNotAutoCreate.CLASSID);
				donac.setClassId(DistributeObjectNotAutoCreate.CLASSID);
				donac.setTypeId(typeId);
				donac.setTypeName(typeName);
				donac.setContextInfo(contextInfo);
				donac.setDomainInfo(context.getDomainInfo());
				Helper.getPersistService().save(donac);
			}
		}
	}

	@Override
	public List<String> getContextType(Context context) {
		List<String> typeNames=new ArrayList<String>();
		String paramId = "DistributeObjectNotAutoCreate";

		Context oc = getContext(context, paramId);

		String sql = "select * from DDM_DIS_OBJECT_NOTAUTOCREATE t "
				+ " where t.contextid=? and t.contextclassid=? ";

		String innerId = oc.getInnerId();
		String classId = oc.getClassId();
		List<DistributeObjectNotAutoCreate> listDisObjNac=PersistHelper.getService().query(
				sql, DistributeObjectNotAutoCreate.class,innerId, classId);
		for(DistributeObjectNotAutoCreate disObjNac:listDisObjNac){
			typeNames.add(disObjNac.getTypeName());
		}
		return typeNames;
	}
	
	/**
	 * 指定上下文中没有定义配置信息 取得父上下文继续判断
	 * 
	 * @param context
	 *            上下文
	 * @param paramId
	 *            参数ID
	 * @return 上下文
	 */
	private Context getContext(Context context, String paramId) {
		DistributeConfigParameterService cps = DistributeHelper
				.getDistributeConfigParameterService();
		String contextOid = Helper.getOid(context);
		if (!cps.exists(contextOid, paramId)) {
			if (context instanceof Contexted) {
				Contexted contexted = (Contexted) context;
				Context parentContext = contexted.getContextInfo().getContext();
				return getContext(parentContext, paramId);
			}
		}
		return context;
	}

	
	/**
	 * 判断数据是否存在
	 * 
	 * @param typeId,contextOid
	 * @return boolean
	 */
	private boolean exists(String contextOid, String typeId) {
		String sql = "select * from DDM_DIS_OBJECT_NOTAUTOCREATE t "
				+ " where t.contextid=? and t.contextclassid=? "
				+ "and t.typeId=? ";
		String innerId = Helper.getInnerId(contextOid);
		String classId = Helper.getClassId(contextOid);
		List<DistributeObjectNotAutoCreate> dotList = PersistHelper.getService().query(
				sql, DistributeObjectNotAutoCreate.class, innerId, classId, typeId);
		if (dotList == null || dotList.isEmpty()) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 判断数据是否存在
	 * 
	 * @param contextOid
	 * @return boolean
	 */
	private boolean exists(String contextOid) {
		String sql = "select * from DDM_DIS_OBJECT_NOTAUTOCREATE t "
				+ " where t.contextid=? and t.contextclassid=? ";
		String innerId = Helper.getInnerId(contextOid);
		String classId = Helper.getClassId(contextOid);
		List<DistributeObjectNotAutoCreate> dotList = PersistHelper.getService().query(
				sql, DistributeObjectNotAutoCreate.class, innerId, classId);
		if (dotList == null || dotList.isEmpty()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 删除相关联的参数表
	 * 
	 * @param contextOid
	 * @return boolean
	 */
	private void deleteConfigData(String contextOid,String paramId) {
		String sql = "delete from DDM_DIS_CONFIG_PARAMETER t "
				+ " where t.contextid=? and t.contextclassid=? "
				+ "and t.paramId=? ";
		String innerId = Helper.getInnerId(contextOid);
		String classId = Helper.getClassId(contextOid);
		Helper.getPersistService().bulkUpdateBySql(sql, innerId, classId, paramId);
	}


}
