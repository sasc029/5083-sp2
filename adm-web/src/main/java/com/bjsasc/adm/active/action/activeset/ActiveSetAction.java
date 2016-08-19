package com.bjsasc.adm.active.action.activeset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bjsasc.adm.active.action.AbstractAction;
import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.model.ActiveObjSet;
import com.bjsasc.adm.active.model.ActiveSeted;
import com.bjsasc.adm.active.model.activeset.ActiveSet;
import com.bjsasc.adm.active.service.activesetservice.ActiveSetService;
import com.bjsasc.adm.common.ConstUtil;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.bjsasc.plm.type.TypeService;
import com.cascc.avidm.util.SplitString;

/**
 * 现行套Action实现类。
 * 
 * @author yanjia 2013-6-4
 */
public class ActiveSetAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = -4131327954360034679L;

	/** 现行套服务 */
	ActiveSetService service = AdmHelper.getActiveSetService();

	private static final Logger LOG = Logger.getLogger(ActiveSetAction.class);
	
	private static final String MESSAGE = "message";

	/**
	 * 取得现行套。
	 * 
	 * @return JSON对象
	 */
	public String getAllActiveSet() {
		try {
			List<ActiveSet> list = service.getAllActiveSet();
			LOG.debug("取得现行套数据: " + getDataSize(list) + " 条");
			// 输出结果
			GridDataUtil.prepareRowObjects(list, ConstUtil.SPOT_LISTACTIVESET);
		} catch (Exception ex) {
			error(ex);
		}
		return INITPAGE;
	}

	/**
	 * 创建现行套。
	 * 
	 * @return JSON对象
	 */
	public String createActiveSet() {
		try {
			String[] keys = {
					// 类型
					"CLASSIDS",
					// 编号
					"NUMBER",
					// 名称
					"NAME",
					// 来源
					"DATASOURCE",
					// 代号
					"ACTIVECODE",
					// 现行文件编号
					"ACTIVEDOCUMENTNUMBER",
					// 页数
					"PAGES",
					// 份数
					"COUNT",
					// 备注
					"NOTE",
					// 上下文
					"CONTEXT",
					// 密集
					"SECLEVEL",
					// 文件夹OID
					"folderOid",
					// 版本Flag
					"VERSIONFlAG",
					// 版本
					"VERSION" };

			Map<String, String> paramMap = getParams(keys);
			
			// 现行套保存
			String oid = service.createActiveSet(paramMap);
			String addOids = request.getParameter("addOids");
			if (addOids != null && addOids.length() > 0) {
				addActiveSetObject(oid, addOids);
			}
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 删除现行套。
	 * 
	 * @return JSON对象
	 */
	public String deleteActiveSet() {
		try {
			// 现行套OIDS
			String oids = request.getParameter(KeyS.OIDS);

			// 删除现行套
			service.deleteActiveSet(oids);

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 更新现行套。
	 * 
	 * @return JSON对象
	 */
	public String updataActiveSet() {
		try {			

			String[] keys = {
					// 现行套OID
					KeyS.OID,
					// 类型
					"CLASSIDS",
					// 编号
					"NUMBER",
					// 名称
					"NAME",
					// 来源
					"DATASOURCE",
					// 代号
					"ACTIVECODE",
					// 现行文件编号
					"ACTIVEDOCUMENTNUMBER",
					// 页数
					"PAGES",
					// 份数
					"COUNT",
					// 备注
					"NOTE",
					// 上下文
					"CONTEXT",
					// 密集
					"SECLEVEL",
					// 文件夹OID
					"folderOid",
					// 版本Flag
					"VERSIONFlAG",
					// 版本
					"VERSION" };

			Map<String, String> paramMap = getParams(keys);
			// 更新现行套
			service.updataActiveSet(paramMap);

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 根据OIDS取得现行套
	 * 
	 * @return JSON对象
	 */
	public void getActiveSetByOIDS() {
		String oids = request.getParameter(KeyS.OIDS);
		List<String> oidList = SplitString.string2List(oids, ",");
		List<ActiveSet> list = new ArrayList<ActiveSet>();
		for (String oid : oidList) {
			ActiveSet obj = (ActiveSet) Helper.getPersistService().getObject(oid);
			if (obj == null) {
				continue;
			}
			list.add(obj);
		}
		LOG.debug("取得现行套数据: " + getDataSize(list) + " 条");
		// 输出结果
		GridDataUtil.prepareRowObjects(list, ConstUtil.SPOT_LISTACTIVESET);
	}

	/**
	 * 根据OIDS取得现行套
	 * 
	 * @return JSON对象
	 */
	public String getActiveSetObject() {
		try {
			String oid = request.getParameter(KeyS.OID);
			ActiveObjSet activeSet = (ActiveObjSet) Helper.getPersistService().getObject(oid);
			List<ActiveSeted> itemList = service.getActiveItems(activeSet);
			TypeService typeManager = Helper.getTypeManager();
			for (ActiveSeted item : itemList) {
				listMap.add(typeManager.format(item));
			}
			LOG.debug("取得现行套数据: " + getDataSize() + " 条");
			// 输出结果
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTACTIVESETOBJECT);
		} catch (Exception ex) {
			jsonError(ex);
		}
		listToJson();
		return OUTPUTDATA;
	}

	/**
	 * 添加现行套数据
	 * 
	 * @return JSON对象
	 */
	public String addActiveSetObject() {
		try {
			String oid = request.getParameter(KeyS.OID);
			String objectOids = request.getParameter(KeyS.OIDS);
			addActiveSetObject(oid, objectOids);
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 添加现行套数据
	 * @param String setOid
	 * @param String objectOids
	 * @return JSON对象
	 */
	private void addActiveSetObject(String setOid, String objectOids) {
		List<String> oids = SplitString.string2List(objectOids, ",");
		ActiveSet obj = (ActiveSet) Helper.getPersistService().getObject(setOid);
		for (String oid : oids) {
			Persistable object = Helper.getPersistService().getObject(oid);
			if (object == null) {
				continue;
			}
			service.addToActiveSet(obj, (ActiveSeted) object);
		}
	}

	/**
	 * 删除现行套数据
	 * 
	 * @return JSON对象
	 */
	public String deleteActiveSetObject() {
		try {
			// 现行套OIDS
			String setOid = request.getParameter(KeyS.OID);
			String objectOids = request.getParameter(KeyS.OIDS);
			List<String> oids = SplitString.string2List(objectOids, ",");
			for (String oid : oids) {
				service.deleteActiveSetLink(setOid, oid);
			}
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 是否存在套图与现行文件关联Link。
	 * 
	 * @return JSON对象
	 */
	public String hasActiveSetLink() {
		try {
			// 现行套OIDS
			// String oid = request.getParameter(KeyS.OID);

			// 取得套图与现行文件关联Link
			// List<ActiveSetLink> list = service.getActiveSetLinkByObject(oid);

			result.put(MESSAGE, "");
//			if (list == null || list.isEmpty()) {
//				// 可以直接删除该对象
//			} else {
//				// 确认是否要删除该对象
//				result.put(MESSAGE, ConstUtil.MESSAGE_DELETEOBJECTFLAG_0);
//			}
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 是否是现行套数据源。
	 * 
	 * @return JSON对象
	 */
	public String isActiveSetedObject() {
		try {
			// 现行套OIDS
			String oid = request.getParameter(KeyS.OID);
			boolean flag = service.isActiveSeted(Helper.getPersistService().getObject(oid));
			if (flag) {
				// 是现行套数据源
				result.put(MESSAGE, "");
			} else {
				// 不是现行套数据源
				result.put(MESSAGE, ConstUtil.MESSAGE_ISACTIVESETEDFLAG_0);
			}
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}
}
