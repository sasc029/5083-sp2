package com.bjsasc.ddm.distribute.service.distributeobjecttype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeobjecttype.DistributeObjectType;
import com.bjsasc.ddm.distribute.service.distributeconfigparameter.DistributeConfigParameterService;
import com.bjsasc.platform.objectmodel.business.persist.PTFactor;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.context.ContextHelper;
import com.bjsasc.plm.core.context.model.AppContext;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.model.ContextInfo;
import com.bjsasc.plm.core.context.model.Contexted;
import com.bjsasc.plm.core.context.model.OrgContext;
import com.bjsasc.plm.core.context.team.Team;
import com.bjsasc.plm.core.context.team.TeamHelper;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.principal.Principal;
import com.bjsasc.plm.core.system.principal.Role;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.grid.GridHelper;
import com.cascc.avidm.util.SplitString;

/**
 * 对象模型配置服务实现类。
 * 
 * @author gengancong 2014-5-5
 */
public class DistributeObjectTypeServiceImpl implements
		DistributeObjectTypeService {

	@Override
	public Map<String, List<String>> getContextUserType(Context context,
			User user) {

		String contextOid = Helper.getOid(context);
		String userOid = Helper.getOid(user);

		Team localTeam = null;
		Team shareTeam = null;
		List<Role> roleList = new ArrayList<Role>();
		List<Role> shareRoleList = new ArrayList<Role>();
		if(context instanceof OrgContext){
			OrgContext orgContext = (OrgContext) context;
			List<Team> teams = TeamHelper.getService().getContextTeams(orgContext);
			for(Team team:teams){
				List<Role> orgRoleList = TeamHelper.getService().getPrincipalRoles(user,team);
				roleList.addAll(orgRoleList);
			}
			
		}else if (context instanceof AppContext) {
			AppContext appContext = (AppContext) context;
			// shareTeam = appContext.getSharedTeam();
			localTeam = appContext.getLocalTeam();
			shareTeam = appContext.getSharedTeam();
			roleList = TeamHelper.getService().getPrincipalRoles(user,
					localTeam);
			if(!StringUtil.isNull(shareTeam)){
				shareRoleList = TeamHelper.getService().getPrincipalRoles(user,
						shareTeam);
			}
		}
		String inParam = "'" + userOid + "'";

		for (Role rr : roleList) {
			String rro = Helper.getOid(rr);
			inParam += ",'" + rro + "'";
		}
		
		for(Role share : shareRoleList) {
			String shareo = Helper.getOid(share);
			inParam += ",'" + shareo + "'";
		}

		String paramId = "DistributeObjectType";

		Context oc = getContext(context, paramId);

		String sql = "select * from DDM_DIS_OBJECT_TYPE t "
				+ " where t.contextid=? and t.contextclassid=? "
				+ "and t.dataClassId || ':' || t.dataId in (" + inParam + ") ";

		String innerId = oc.getInnerId();
		String classId = oc.getClassId();
		List<DistributeObjectType> dotList = PersistHelper.getService().query(
				sql, DistributeObjectType.class, innerId, classId);

		Map<String, List<String>> typeMap = new HashMap<String, List<String>>();
		List<String> typeList = new ArrayList<String>();
		for (DistributeObjectType dot : dotList) {
			typeList.add(dot.getTypeId());
		}
		typeMap.put(contextOid + "_" + userOid, typeList);

		return typeMap;
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

	@Override
	public List<Map<String, Object>> findByContextOid(String contextOid) {
		String spot = "ListDistributeObjectTypes";
		String spotInstance = "ListDistributeObjectTypes";
		return findByContextOid(contextOid, spot, spotInstance);
	}

	@Override
	public List<Map<String, Object>> findByContextOid(String contextOid,
			String spot, String spotInstance) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		String innerId = Helper.getInnerId(contextOid);
		String classId = Helper.getClassId(contextOid);

		String sql = "select max(innerid) innerid,max(classid) classid,"
				+ "dataId,dataClassId,'' typeId,'' typeName,"
				+ "max(state) state,max(contextid) contextid,max(contextclassid) contextclassid,"
				+ "max(domainid) domainid,max(domainclassid) domainclassid,"
				+ "max(createtime) createtime,max(modifytime) modifytime,max(createbyid) createbyid,"
				+ "max(createbyclassid) createbyclassid,max(modifybyid) modifybyid,"
				+ "max(modifybyclassid) modifybyclassid,max(updatetime) updatetime,"
				+ "max(updatecount) updatecount from DDM_DIS_OBJECT_TYPE t "
				+ " where t.contextid=? and t.contextclassid=? and t.state != 9"
				+ " group by t.dataId, t.dataClassId order by updatetime desc";

		List<DistributeObjectType> dotList = PersistHelper.getService().query(
				sql, DistributeObjectType.class, innerId, classId);

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
	public void deleteTypeData(String contextOid, String oids) {

		String cid = Helper.getInnerId(contextOid);
		String ccd = Helper.getClassId(contextOid);
		List<String> oidList = SplitString.string2List(oids, "#@#");

		String sql = "delete from DDM_DIS_OBJECT_TYPE where "
				+ " contextid=? and contextclassid=? "
				+ " and dataId=? and dataClassId=?";

		for (String oid : oidList) {
			String did = Helper.getInnerId(oid);
			String dcd = Helper.getClassId(oid);
			Helper.getPersistService().bulkUpdateBySql(sql, cid, ccd, did, dcd);
		}
		if(!exists(contextOid)){
			deleteConfigData(contextOid,"DistributeObjectType");
		}
		//cleanSession(contextOid);
	}

	@Override
	public void editTypeData(String contextOid,
			List<Map<String, String>> listMap,
			List<Map<String, String>> listTypeMap) {

		for (Map<String, String> map : listMap) {
			String oid = map.get("OID");
			deleteTypeData(contextOid, oid);
		}
		addTypeData(contextOid, listTypeMap);

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
			String paramId = "DistributeObjectType";
			if (!cps.exists(contextOid, paramId)) {
				String paramName = "对象模型配置";
				String defaultValue = "ok";
				String currentValue = "ok";
				String state = "0";
				String description = "如果此数据存在说明此上下文中配置有效。";
				cps.addConfigData(contextOid, paramId, paramName, defaultValue,
						currentValue, state, description);
			}

			for (Map<String, String> mm : listTypeMap) {
				String oid = mm.get("OID");
				String typeId = mm.get("DOT_TYPE_ID");
				String typeName = mm.get("DOT_TYPE_NAME");
				Principal principal = (Principal) Helper.getPersistService()
						.getObject(oid);
				String dataId = principal.getInnerId();
				String dataClassId = principal.getClassId();
				if (exists(contextOid, dataId, dataClassId, typeId)) {
					continue;
				}

				String state = "1";
				if (principal instanceof Role) {
					state = "0";
				}
				DistributeObjectType dot = (DistributeObjectType) PersistUtil
						.createObject(DistributeObjectType.CLASSID);
				dot.setClassId(DistributeObjectType.CLASSID);
				dot.setDataId(dataId);
				dot.setDataClassId(dataClassId);
				dot.setTypeId(typeId);
				dot.setTypeName(typeName);
				dot.setState(state);
				dot.setContextInfo(contextInfo);
				dot.setDomainInfo(context.getDomainInfo());
				Helper.getPersistService().save(dot);
			}
			//cleanSession(contextOid);
		}
	}

	/**
	 * 判断数据是否存在
	 * 
	 * @param dataId
	 * @param dataClassId
	 * @param typeId
	 * @return boolean
	 */
	private boolean exists(String contextOid, String dataId,
			String dataClassId, String typeId) {
		String sql = "select * from DDM_DIS_OBJECT_TYPE t "
				+ " where t.contextid=? and t.contextclassid=? "
				+ "and t.dataId=? and t.dataClassId=? and t.typeId=? ";
		String innerId = Helper.getInnerId(contextOid);
		String classId = Helper.getClassId(contextOid);
		List<DistributeObjectType> dotList = PersistHelper.getService().query(
				sql, DistributeObjectType.class, innerId, classId, dataId,
				dataClassId, typeId);
		if (dotList == null || dotList.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * 清空缓存.
	 * 
	 * @param contextOid
	 */
	private void cleanSession(String contextOid) {
		SessionHelper.getService().clear("DDM_CONFIG_OBJECT_TYPE");
	}
	
	/**
	 * 判断数据是否存在
	 * 
	 * @param contextOid
	 * @return boolean
	 */
	private boolean exists(String contextOid) {
		String sql = "select * from DDM_DIS_OBJECT_TYPE t "
				+ " where t.contextid=? and t.contextclassid=? ";
		String innerId = Helper.getInnerId(contextOid);
		String classId = Helper.getClassId(contextOid);
		List<DistributeObjectType> dotList = PersistHelper.getService().query(
				sql, DistributeObjectType.class, innerId, classId);
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
	
	/**
	 * 获取所有配置的类型，按照上下文oid+"_"+用户信息为key，存储在map中
	 * @return
	 */
	public Map<String, List<String>> getAllType(){
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		String hql = "from DistributeObjectType ";
		List<DistributeObjectType> list = Helper.getPersistService().find(hql);
		for(DistributeObjectType disType : list){
			List<String> typeList = new ArrayList<String>();
			String key = "";
			try{
				key = disType.getContextInfo().getContext().getOid()+"_"+disType.getDataOid();
			}catch(Exception e){
				continue;
			}
			typeList = map.get(key);
			if(typeList == null){
				typeList = new ArrayList<String>();
			}
			typeList.add(disType.getTypeId());
			map.put(key, typeList);
		}
		return map;
	}

}
