package com.bjsasc.adm.active.service.activefolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.adm.common.AdmLifeCycleConstUtil;
import com.bjsasc.platform.objectmodel.business.persist.PTFactor;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.folder.Folder;
import com.bjsasc.plm.core.folder.FolderHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.query.condition.Condition;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.access.AccessControlHelper;
import com.bjsasc.plm.core.system.access.PtPermS;
import com.bjsasc.plm.core.system.access.operate.Operate;
import com.bjsasc.plm.core.system.config.ConfigHelper;
import com.bjsasc.plm.core.vc.model.Iterated;
import com.bjsasc.plm.folder.extend.FolderQueryIterated;
import com.bjsasc.plm.folder.extend.FolderQueryManaged;
import com.bjsasc.plm.folder.extend.FolderQuerySubFolder;
import com.bjsasc.plm.grid.GridHelper;
import com.bjsasc.plm.grid.data.GridDataService;
import com.bjsasc.plm.type.type.Type;

/**
 * 文件夹内容表格数据查询服务
 * 
 * @author linjinzhi
 * 
 */
public class GridDataService_ListActiveFolderContentsImpl implements GridDataService {

	// 根据参数配置确定在获取文件夹内容列表时是否验证权限
	private static final String CHECKACCESSINFOLDER = ConfigHelper.getService().getParameter("checkAccessInFolder",
			"/plm/system");

	private static final String LC_RECYCLE_NAME = AdmLifeCycleConstUtil.LC_RECYCLE.getName();

	public List<Map<String, Object>> getRows(String spot, String spotInstance, Map<Type, Condition> typeCondition,
			Map<String, Object> params) {

		List<String> keys = GridHelper.getService().getMyLatestGridViewColumnIds(spot, spotInstance);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		/**
		 * 查询文件夹下所有的对象，未进行权限的过滤
		 */
		List<Object> forCheckObj = getObjectByTypeConditionAndParam(typeCondition, params);

		for (Object obj : forCheckObj) {

			Map<String, Object> map = Helper.getTypeManager().format((PTFactor) obj, keys, false);
			// 过滤已删除的数据。
			if (LC_RECYCLE_NAME.equals(map.get("LIFECYCLE_STATE"))) {
				continue;
			}
			map.put(KeyS.ACCESS, true);
			list.add(map);
		}
		return list;
	}

	/**
	 * 根据类型和参数返回所有查询到的文件夹下的对象,并进行权限过滤，该输出可以同时适用于工作区对象列表
	 * 
	 * @author litianli
	 * @date 2013年10月15日 下午2:05:13
	 * @param typeCondition
	 * @param params
	 * @return
	 * 
	 */
	public static List<Object> getObjectByTypeConditionAndParam(Map<Type, Condition> typeCondition,
			Map<String, Object> params) {
		// 用于校验权限的集合
		List<Object> forCheckObj = new ArrayList<Object>();

		// 目标文件夹
		Folder folder = (Folder) params.get("folder");

		// 根据Folder获取上下文，然后获取首选项中的值
		Context context = folder.getContextInfo().getContext();
		boolean isShowNewestBranch = FolderHelper.getService().isShowNewestBranch(context);

		for (Type type : typeCondition.keySet()) {
			String targetClassName = type.getId();
			String targetAlias = type.getAlias();

			// 目标类
			Class<?> target = Helper.getTypeService().getTargetClass(targetClassName);

			// 版本对象执行连表查询，无版本对象直接在目标表上执行查询
			List result = new ArrayList();

			if (Iterated.class.isAssignableFrom(target)) {
				String masterClassName = targetClassName + "Master";
				Type master = Helper.getTypeManager().getType(masterClassName);
				String masterAlias = master.getAlias();

				// 如果目标类具有版本机制，则连接主对象表
				result = FolderQueryIterated.query(folder, targetClassName, targetAlias, masterClassName, masterAlias,
						typeCondition.get(type), isShowNewestBranch);
			} else if (Folder.class.isAssignableFrom(target)) {
				// 查询子文件夹
				result = FolderQuerySubFolder.query(folder, targetAlias, typeCondition.get(type));
			} else {
				// 查询普通对象
				result = FolderQueryManaged.query(folder, targetClassName, targetAlias, typeCondition.get(type));
			}
			forCheckObj.addAll(result);
		}
		// 把对象存入缓存，准备没有权限对象的列表
		Map<String, Object> objMap = new HashMap<String, Object>();
		for (Object obj : forCheckObj) {
			objMap.put(Helper.getOid((Persistable) obj), obj);
		}
		List<Object> noAccessObjs = new ArrayList<Object>();
		if (!"false".equals(CHECKACCESSINFOLDER)) {
			// 批量校验权限
			List<Map<String, Object>> permRS = AccessControlHelper.getService().hasEntityPermission(
					SessionHelper.getService().getUser(), Operate.ACCESS, context, forCheckObj);

			for (Map<String, Object> temp : permRS) {
				Object obj = temp.get("OBJ");
				String oid = null;
				if (obj instanceof Persistable) {
					Persistable obj2 = (Persistable) obj;
					oid = com.bjsasc.plm.core.Helper.getOid(obj2);
				} else if (obj instanceof PtPermS) {
					PtPermS ptObj = (PtPermS) obj;
					oid = com.bjsasc.plm.core.Helper.getOid(ptObj.getClassId(), ptObj.getInnerId());
				}

				// flag>0则说明对象标示为oid的对象具有权限，否则，应该过滤掉
				// 如果没有权限,把该对象加入待移除列表
				if ((Integer) temp.get("FLAG") <= 0) {
					noAccessObjs.add(objMap.get(oid));
				}
			}
		}
		// 移除没有权限的对象
		forCheckObj.removeAll(noAccessObjs);
		return forCheckObj;
	}
}
