package com.bjsasc.ddm.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.model.recdespapertask.RecDesPaperTask;
import com.bjsasc.ddm.distribute.service.distributeconfigparameter.DistributeConfigParameterService;
import com.bjsasc.ddm.distribute.service.distributeobjecttype.DistributeObjectTypeService;
import com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService;
import com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService;
import com.bjsasc.ddm.distribute.service.recdespapertask.RecDesPaperTaskService;
import com.bjsasc.platform.objectmodel.business.persist.PTFactor;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.model.Contexted;
import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.option.OptionHelper;
import com.bjsasc.plm.core.option.OptionValue;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.access.AccessControlHelper;
import com.bjsasc.plm.core.system.access.PtPermS;
import com.bjsasc.plm.core.system.access.operate.Operate;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.grid.GridHelper;

/**
 * 批量验证权限类。
 * 
 * @author gengancong 2014-5-12
 */
public class CheckPermission {

	/**
	 * 根据上下文和当前用户取得按文档类型配置信息
	 * 
	 * @param context
	 *            上下文
	 * @param currentUser
	 *            当前用户
	 * @return 按文档类型配置信息
	 */
	private static List<String> getTypeMap(Context context, User currentUser) {
		String paramId = "DistributeObjectType";
		Context cont = getContext(context, paramId);
		String contextOid = Helper.getOid(cont);
		String userOid = Helper.getOid(currentUser);
		String key = contextOid + "_" + userOid;
		List<String> typeList = DdmTypePermissionCache.getTypeData(key);
		if(typeList == null){
			typeList = new ArrayList<String>();
		}
		return typeList;
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
	private static Context getContext(Context context, String paramId) {
		List<String> contextOidList = DdmTypePermissionCache.getTypeData(paramId);
		if(null == contextOidList){
			return context;
		}
		String contextOid = Helper.getOid(context);
		if (!contextOidList.contains(contextOid)) {
			if (context instanceof Contexted) {
				Contexted contexted = (Contexted) context;
				Context parentContext = contexted.getContextInfo().getContext();
				return getContext(parentContext, paramId);
			}
		}
		return context;
	}

	/**
	 * 批量验证权限
	 * 
	 * @param forCheckObj
	 *            数据对象集
	 * @param spot
	 *            画面显示参数
	 * @param spotInstance
	 *            画面显示参数
	 * @return 权限验证后对象集
	 */
	public static List<Object> checkPermission(List listObj, String spot,
			String spotInstance) {

		// 用于校验权限的集合
		List<Object> forCheckObj = new ArrayList<Object>();
		List<Object> rtnObjList = new ArrayList<Object>();
		for (Object obj : listObj) {
			//分发数据，电子任务，纸质签收任务不做策略管理的权限校验
			if (obj instanceof Domained) {
				forCheckObj.add(obj);
			}else{
				rtnObjList.add(obj);
			}
		}

		// 把对象存入缓存，准备没有权限对象的列表
		Map<String, Object> objMap = new HashMap<String, Object>();
		for (Object obj : forCheckObj) {
			objMap.put(Helper.getOid((Persistable) obj), obj);
		}
		List<Object> noAccessObjs = new ArrayList<Object>();
		// 批量校验权限
		List<Map<String, Object>> permRS = AccessControlHelper.getService()
				.hasEntityPermission(SessionHelper.getService().getUser(),
						Operate.ACCESS, null, forCheckObj);

		for (Map<String, Object> temp : permRS) {
			Object obj = temp.get("OBJ");
			String oid = null;
			if (obj instanceof Persistable) {
				Persistable obj2 = (Persistable) obj;
				oid = com.bjsasc.plm.core.Helper.getOid(obj2);
			} else if (obj instanceof PtPermS) {
				PtPermS ptObj = (PtPermS) obj;
				oid = com.bjsasc.plm.core.Helper.getOid(ptObj.getClassId(),
						ptObj.getInnerId());
			}

			// flag>0则说明对象标示为oid的对象具有权限，否则，应该过滤掉
			// 如果没有权限,把该对象加入待移除列表
			if ((Integer) temp.get("FLAG") <= 0) {
				noAccessObjs.add(objMap.get(oid));
			}
		}
		// 移除没有权限的对象
		forCheckObj.removeAll(noAccessObjs);
		rtnObjList.addAll(forCheckObj);
		return rtnObjList;
	}

	/**
	 * 批量验证权限 & 默认格式化
	 * 
	 * @param forCheckObj
	 *            数据对象集
	 * @param spot
	 *            画面显示参数
	 * @param spotInstance
	 *            画面显示参数
	 * @return 权限验证后对象集
	 */
	public static List<Map<String, Object>> checkPermissionAF(List listObj,
			String spot, String spotInstance) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		List<String> keys = GridHelper.getService()
				.getMyLatestGridViewColumnIds(spot, spotInstance);
		// 用于校验权限的集合
		List<Object> forCheckObj = checkPermission(listObj, spot, spotInstance);
		// 验证按对象类型配置的角色权限
		checkDistributeOrderType(forCheckObj);
		for (Object obj : forCheckObj) {

			Map<String, Object> map = Helper.getTypeManager().format(
					(PTFactor) obj, keys, false);
			map.put(KeyS.ACCESS, true);
			list.add(map);
		}
		return list;
	}

	/**
	 * 判断数据所属上下文所定义的类型， 当前用户是否有操作数据的权限。
	 * 
	 * @param listObj
	 *            数据集
	 */
	public static void checkDistributeOrderType(List<Object> listObj) {
		User currentUser = SessionHelper.getService().getUser();
		List<Object> rl = new ArrayList<Object>();
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		Map<String, OptionValue> optionValueMap = new HashMap<String, OptionValue>();
		for (Object object : listObj) {
			//判断按对象类型配置角色权限的开关是否打开，如果没有打开则不做后续检查
			Context context = null;
			String contextInnerId = null;
			if(object instanceof Contexted){
				context = ((Contexted) object).getContextInfo().getContext();
				//相同上下文不再进行开关查询
				contextInnerId = context.getInnerId();
			}
			OptionValue value = null;
			if(StringUtil.isNull(optionValueMap.get(contextInnerId))){
				value = OptionHelper.getService().getOptionValue(context,
						"disMange_disManageConfig_isOpenConfigRoleAuthByObjType");
				optionValueMap.put(contextInnerId, value);
			}else{
				value = (OptionValue)optionValueMap.get(contextInnerId);
			}
			if ("false".equals(value.getValue())) {
				continue;
			}
			
			if (object instanceof Manageable) {
				Manageable m = (Manageable) object;
				String innerId = m.getManageInfo().getCreateByRef().getInnerId();
				String userId = currentUser.getInnerId();
				if (userId != null && userId.equals(innerId)) {
					continue;
				}
			}

			if (object instanceof DistributeOrder) {
				DistributeOrder distributeOrder = (DistributeOrder) object;
				//相同上下文不再进行查询类型配置
//				String contextInnerId=distributeOrder.getContextInfo().getContext().getInnerId();
//				Context context=distributeOrder.getContextInfo().getContext();
				List<String> typeList=new ArrayList<String>();
				if(StringUtil.isNull(map.get(contextInnerId))){
					typeList = getTypeMap(context, currentUser);
					map.put(contextInnerId, typeList);
				}else{
					typeList=(List<String>)map.get(contextInnerId);
				}
				if (!checkType(distributeOrder , typeList)) {
					rl.add(object);
				}
			} else if (object instanceof DistributePaperTask) {
				DistributePaperTask distributePaperTask = (DistributePaperTask) object;

				DistributePaperTaskService dpts = DistributeHelper
						.getDistributePaperTaskService();
				DistributePaperTask dpt = dpts
						.getDistributePaperTaskProperty(Helper
								.getOid(distributePaperTask));
				String orderOid = dpt.getOrderOid();

				DistributeOrder distributeOrder = (DistributeOrder) Helper
						.getPersistService().getObject(orderOid);
				//相同上下文不再进行查询类型配置
//				String contextInnerId=distributeOrder.getContextInfo().getContext().getInnerId();
//				Context context=distributeOrder.getContextInfo().getContext();
				List<String> typeList=new ArrayList<String>();
				if(StringUtil.isNull(map.get(contextInnerId))){
					typeList = getTypeMap(context, currentUser);
					map.put(contextInnerId, typeList);
				}else{
					typeList=(List<String>)map.get(contextInnerId);
				}
				if (!checkType(distributeOrder , typeList)) {
					rl.add(object);
				}
			} else if (object instanceof RecDesPaperTask) {
				RecDesPaperTask recDesPaperTask = (RecDesPaperTask) object;

				RecDesPaperTaskService dpts = DistributeHelper
						.getRecDesPaperTaskService();
				RecDesPaperTask dpt = dpts.getRecDesPaperTaskProperty(Helper
								.getOid(recDesPaperTask));
				String orderOid = dpt.getOrderOid();

				DistributeOrder distributeOrder = (DistributeOrder) Helper
						.getPersistService().getObject(orderOid);

				List<String> typeList=new ArrayList<String>();
				if(StringUtil.isNull(map.get(contextInnerId))){
					typeList = getTypeMap(context, currentUser);
					map.put(contextInnerId, typeList);
				}else{
					typeList=(List<String>)map.get(contextInnerId);
				}
				if (!checkType(distributeOrder , typeList)) {
					rl.add(object);
				}
			}
		}
		listObj.removeAll(rl);
	}

	/**
	 * 判断发放单的默认分发数据的分发类型是否是当前用户可操作的类型。
	 * 
	 * @param distributeOrder
	 *            发放单对象
	 * @param currentUser
	 *            用户
	 * @return boolean true：是可操作对象；false：不是可操作对象。
	 */
	private static boolean checkType(DistributeOrder distributeOrder, List<String> typeList) {
		//DistributeOrderService dos = DistributeHelper.getDistributeOrderService();
		//String distributeOrderOid = Helper.getOid(distributeOrder);
		//String defaultDistributeOrderType = dos.getDefaultDistributeOrderType(distributeOrderOid);
		String defaultDistributeOrderType = distributeOrder.getMasterDataClassID();
		//Context context = distributeOrder.getContextInfo().getContext();
		//List<String> typeList = getTypeMap(context, currentUser);
		if (typeList.contains(defaultDistributeOrderType)) {
			return true;
		}
		return false;
	}

}
