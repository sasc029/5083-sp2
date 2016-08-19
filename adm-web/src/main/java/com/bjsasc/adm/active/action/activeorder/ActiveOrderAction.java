package com.bjsasc.adm.active.action.activeorder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bjsasc.adm.active.action.AbstractAction;
import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.model.ActiveOrdered;
import com.bjsasc.adm.active.model.activeorder.ActiveOrder;
import com.bjsasc.adm.active.model.activeorderlink.ActiveOrderLink;
import com.bjsasc.adm.active.service.activeorderservice.ActiveOrderService;
import com.bjsasc.adm.common.ConstUtil;
import com.bjsasc.platform.objectmodel.business.version.IterationInfo;
import com.bjsasc.platform.objectmodel.business.version.VersionControlUtil;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.vc.VersionControlHelper;
import com.bjsasc.plm.core.vc.model.Versioned;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.bjsasc.plm.type.TypeManager;
import com.bjsasc.plm.type.TypeService;
import com.cascc.avidm.util.SplitString;

/**
 * 现行单据Action实现类。
 * 
 * @author yanjia 2013-6-3
 */
public class ActiveOrderAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = 7796656227076542082L;

	/** 现行单据服务 */
	ActiveOrderService service = AdmHelper.getActiveOrderService();

	private static final Logger LOG = Logger.getLogger(ActiveOrderAction.class);

	private static final String MESSAGE = "message";

	/**
	 * 创建现行单据。
	 * 
	 * @return JSON对象
	 */
	public String createActiveOrder() {
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
					// 作者
					"AUTHORNAME",
					// 作者单位
					"AUTHORUNIT",
					// 创建时间
					"AUTHORTIME",
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

			// 现行单据保存
			String oid = service.createActiveOrder(paramMap);

			String beforeOids = request.getParameter("beforeOids");
			String afterOids = request.getParameter("afterOids");

			service.updataActiveOrderObject(oid, beforeOids, afterOids);
			
			result.put("innerId", Helper.getInnerId(oid));
			result.put("classId", Helper.getClassId(oid));
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 删除现行单据。
	 * 
	 * @return JSON对象
	 */
	public String deleteActiveOrder() {
		try {
			// 现行单据OIDS
			String oids = request.getParameter(KeyS.OIDS);

			// 删除现行单据
			service.deleteActiveOrder(oids);

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 更新现行单据。
	 * 
	 * @return JSON对象
	 */
	public String updataActiveOrder() {
		try {

			String[] keys = {
					// 现行单据OID
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
					// 作者
					"AUTHORNAME",
					// 作者单位
					"AUTHORUNIT",
					// 创建时间
					"AUTHORTIME",
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

			service.updataActiveOrder(paramMap);

			// 现行单据OID
			String oid = paramMap.get(KeyS.OID);

			String beforeOids = request.getParameter("beforeOids");
			String afterOids = request.getParameter("afterOids");

			service.updataActiveOrderObject(oid, beforeOids, afterOids);

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 更新现行单据的单据对象。
	 * 
	 * @return JSON对象
	 */
	public String updataActiveOrderObject() {
		try {

			// 现行单据OID
			String oid = request.getParameter(KeyS.OID);

			String beforeOids = request.getParameter("beforeOids");
			String afterOids = request.getParameter("afterOids");

			service.updataActiveOrderObject(oid, beforeOids, afterOids);

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 根据OIDS取得现行单据
	 * 
	 * @return JSON对象
	 */
	public void getActiveOrderByOIDS() {
		String oids = request.getParameter(KeyS.OIDS);
		List<String> oidList = SplitString.string2List(oids, ",");
		List<ActiveOrder> list = new ArrayList<ActiveOrder>();
		for (String oid : oidList) {
			ActiveOrder obj = (ActiveOrder) Helper.getPersistService().getObject(oid);
			if (obj == null) {
				continue;
			}
			list.add(obj);
		}
		logPrint(list);
		// 输出结果
		GridDataUtil.prepareRowObjects(list, ConstUtil.SPOT_LISTACTIVEORDER);
	}

	/**
	 * 根据OIDS取得现行单据改前对象
	 * 
	 * @return JSON对象
	 */
	public String getBeforeObject() {
		try {
			String oid = request.getParameter(KeyS.OID);
			List<ActiveOrdered> itemList = service.getBeforeItems(oid);
			TypeService typeManager = Helper.getTypeManager();
			for (ActiveOrdered item : itemList) {
				if (item != null) {
					listMap.add(typeManager.format(item));
				}
			}
			LOG.debug("取得现行单据更改前对象数据: " + getDataSize() + " 条");
			// 输出结果
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTACTIVEORDEROBJECT);
		} catch (Exception ex) {
			jsonError(ex);
		}
		listToJson();
		return OUTPUTDATA;
	}

	/**
	 * 根据OIDS取得现行单据改后对象
	 * 
	 * @return JSON对象
	 */
	public String getAfterObject() {
		try {
			String oid = request.getParameter(KeyS.OID);
			List<ActiveOrderLink> linkList = service.getActiveOrderLinks(oid);
			TypeService typeManager = Helper.getTypeManager();
			
			for (ActiveOrderLink link : linkList) {
				Persistable afterObj = link.getTo();
				String beforeOid = link.getOrderedBeforeOid();
				ActiveOrdered temp = (ActiveOrdered) afterObj;
				if (temp == null) {
					continue;
				}
				Map<String, Object> mapObj = typeManager.format(afterObj);
				Object afterOid = mapObj.get("OID");
				mapObj.put("OID", beforeOid + ";" + afterOid);
				listMap.add(mapObj);
			}
			LOG.debug("取得现行单据更改后对象数据: " + getDataSize() + " 条");
			// 输出结果
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTACTIVEORDEROBJECT);
		} catch (Exception ex) {
			jsonError(ex);
		}
		listToJson();
		return OUTPUTDATA;
	}

	/**
	 * 是否存在单据与现行文件关联Link。
	 * 
	 * @return JSON对象
	 */
	public String hasActiveOrderLink() {
		try {
			// 现行单据OIDS
			String oid = request.getParameter(KeyS.OID);

			// 取得单据与现行文件关联Link
			List<ActiveOrderLink> list = service.getActiveOrderLinkByObject(oid);

			if (list == null || list.isEmpty()) {
				// 可以直接删除该对象
				result.put(MESSAGE, "");
			} else {
				// 确认是否要删除该对象
				result.put(MESSAGE, ConstUtil.MESSAGE_DELETEOBJECTFLAG_1);
			}
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 是否是现行单据数据源。
	 * 
	 * @return JSON对象
	 */
	public String isActiveOrderedObject() {
		try {
			// 现行单据OIDS
			String oid = request.getParameter(KeyS.OID);
			boolean flag = service.isActiveOrdered(Helper.getPersistService().getObject(oid));
			if (flag) {
				// 是现行单据数据源
				result.put(MESSAGE, "");
			} else {
				// 不是现行单据数据源
				result.put(MESSAGE, ConstUtil.MESSAGE_ISACTIVEORDEREDFLAG_0);
			}
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 取得版本对象List
	 * 
	 * @return JSON对象
	 */
	public String getIteratedList() {
		try {
			String oid = request.getParameter(KeyS.OID);
			Persistable obj = Helper.getPersistService().getObject(oid);
			if (obj instanceof Versioned) {
				Versioned versioned = (Versioned) obj;
				List<Versioned> versionedList = VersionControlHelper.getService().allVersionsOf(versioned.getMaster());
				for (Versioned temp : versionedList) {
					if (temp.getInnerId().equals(obj.getInnerId())) {
						break;
					} else {
						Map<String, Object> map = TypeManager.getManager().getAttrValues(temp);
						map.put("BEFOREOID", oid);
						listMap.add(map);
					}
				}
			}
			LOG.debug("取得版本对象数据: " + getDataSize() + " 条");
			// 输出结果
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTVERSIONBROWSE);
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		listToJson();
		return OUTPUTDATA;
	}

	/**
	 * 是否是最新版本。
	 * 
	 * @return JSON对象
	 */
	public String isLastestVersion() {
		try {
			// 现行单据OIDS
			String oid = request.getParameter(KeyS.OID);
			Persistable obj = Helper.getPersistService().getObject(oid);

			Versioned oldVersion = (Versioned) obj;
			List<Versioned> vlist = VersionControlHelper.getService().allVersionsOf(oldVersion.getMaster());
			IterationInfo info = ((Versioned) obj).getIterationInfo();
			// 判断文档对象是否存在最新版本
			String latestNO = vlist.get(0).getIterationInfo().getFullVersionNo();

			// 判断对象是否存在最新版本
			if (latestNO.equals(info.getFullVersionNo()) && info.getLatestInBranch().equals(VersionControlUtil.LATEST)) {
				// 不是存在
				result.put(MESSAGE, ConstUtil.MESSAGE_ISLASTESTVERSIONFLAG_0);
			} else {
				// 存在
				result.put(MESSAGE, "");
			}
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	private void logPrint(List<?> list) {
		LOG.debug("取得现行单据数据: " + getDataSize(list) + " 条");
	}
}
