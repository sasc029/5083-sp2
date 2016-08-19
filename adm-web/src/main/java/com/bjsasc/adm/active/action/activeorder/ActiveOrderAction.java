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
 * ���е���Actionʵ���ࡣ
 * 
 * @author yanjia 2013-6-3
 */
public class ActiveOrderAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = 7796656227076542082L;

	/** ���е��ݷ��� */
	ActiveOrderService service = AdmHelper.getActiveOrderService();

	private static final Logger LOG = Logger.getLogger(ActiveOrderAction.class);

	private static final String MESSAGE = "message";

	/**
	 * �������е��ݡ�
	 * 
	 * @return JSON����
	 */
	public String createActiveOrder() {
		try {

			String[] keys = {
					// ����
					"CLASSIDS",
					// ���
					"NUMBER",
					// ����
					"NAME",
					// ��Դ
					"DATASOURCE",
					// ����
					"ACTIVECODE",
					// �����ļ����
					"ACTIVEDOCUMENTNUMBER",
					// ҳ��
					"PAGES",
					// ����
					"COUNT",
					// ����
					"AUTHORNAME",
					// ���ߵ�λ
					"AUTHORUNIT",
					// ����ʱ��
					"AUTHORTIME",
					// ��ע
					"NOTE",
					// ������
					"CONTEXT",
					// �ܼ�
					"SECLEVEL",
					// �ļ���OID
					"folderOid",
					// �汾Flag
					"VERSIONFlAG",
					// �汾
					"VERSION" };
			Map<String, String> paramMap = getParams(keys);

			// ���е��ݱ���
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
	 * ɾ�����е��ݡ�
	 * 
	 * @return JSON����
	 */
	public String deleteActiveOrder() {
		try {
			// ���е���OIDS
			String oids = request.getParameter(KeyS.OIDS);

			// ɾ�����е���
			service.deleteActiveOrder(oids);

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * �������е��ݡ�
	 * 
	 * @return JSON����
	 */
	public String updataActiveOrder() {
		try {

			String[] keys = {
					// ���е���OID
					KeyS.OID,
					// ����
					"CLASSIDS",
					// ���
					"NUMBER",
					// ����
					"NAME",
					// ��Դ
					"DATASOURCE",
					// ����
					"ACTIVECODE",
					// �����ļ����
					"ACTIVEDOCUMENTNUMBER",
					// ҳ��
					"PAGES",
					// ����
					"COUNT",
					// ����
					"AUTHORNAME",
					// ���ߵ�λ
					"AUTHORUNIT",
					// ����ʱ��
					"AUTHORTIME",
					// ��ע
					"NOTE",
					// ������
					"CONTEXT",
					// �ܼ�
					"SECLEVEL",
					// �ļ���OID
					"folderOid",
					// �汾Flag
					"VERSIONFlAG",
					// �汾
					"VERSION" };
			Map<String, String> paramMap = getParams(keys);

			service.updataActiveOrder(paramMap);

			// ���е���OID
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
	 * �������е��ݵĵ��ݶ���
	 * 
	 * @return JSON����
	 */
	public String updataActiveOrderObject() {
		try {

			// ���е���OID
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
	 * ����OIDSȡ�����е���
	 * 
	 * @return JSON����
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
		// ������
		GridDataUtil.prepareRowObjects(list, ConstUtil.SPOT_LISTACTIVEORDER);
	}

	/**
	 * ����OIDSȡ�����е��ݸ�ǰ����
	 * 
	 * @return JSON����
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
			LOG.debug("ȡ�����е��ݸ���ǰ��������: " + getDataSize() + " ��");
			// ������
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTACTIVEORDEROBJECT);
		} catch (Exception ex) {
			jsonError(ex);
		}
		listToJson();
		return OUTPUTDATA;
	}

	/**
	 * ����OIDSȡ�����е��ݸĺ����
	 * 
	 * @return JSON����
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
			LOG.debug("ȡ�����е��ݸ��ĺ��������: " + getDataSize() + " ��");
			// ������
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTACTIVEORDEROBJECT);
		} catch (Exception ex) {
			jsonError(ex);
		}
		listToJson();
		return OUTPUTDATA;
	}

	/**
	 * �Ƿ���ڵ����������ļ�����Link��
	 * 
	 * @return JSON����
	 */
	public String hasActiveOrderLink() {
		try {
			// ���е���OIDS
			String oid = request.getParameter(KeyS.OID);

			// ȡ�õ����������ļ�����Link
			List<ActiveOrderLink> list = service.getActiveOrderLinkByObject(oid);

			if (list == null || list.isEmpty()) {
				// ����ֱ��ɾ���ö���
				result.put(MESSAGE, "");
			} else {
				// ȷ���Ƿ�Ҫɾ���ö���
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
	 * �Ƿ������е�������Դ��
	 * 
	 * @return JSON����
	 */
	public String isActiveOrderedObject() {
		try {
			// ���е���OIDS
			String oid = request.getParameter(KeyS.OID);
			boolean flag = service.isActiveOrdered(Helper.getPersistService().getObject(oid));
			if (flag) {
				// �����е�������Դ
				result.put(MESSAGE, "");
			} else {
				// �������е�������Դ
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
	 * ȡ�ð汾����List
	 * 
	 * @return JSON����
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
			LOG.debug("ȡ�ð汾��������: " + getDataSize() + " ��");
			// ������
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTVERSIONBROWSE);
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		listToJson();
		return OUTPUTDATA;
	}

	/**
	 * �Ƿ������°汾��
	 * 
	 * @return JSON����
	 */
	public String isLastestVersion() {
		try {
			// ���е���OIDS
			String oid = request.getParameter(KeyS.OID);
			Persistable obj = Helper.getPersistService().getObject(oid);

			Versioned oldVersion = (Versioned) obj;
			List<Versioned> vlist = VersionControlHelper.getService().allVersionsOf(oldVersion.getMaster());
			IterationInfo info = ((Versioned) obj).getIterationInfo();
			// �ж��ĵ������Ƿ�������°汾
			String latestNO = vlist.get(0).getIterationInfo().getFullVersionNo();

			// �ж϶����Ƿ�������°汾
			if (latestNO.equals(info.getFullVersionNo()) && info.getLatestInBranch().equals(VersionControlUtil.LATEST)) {
				// ���Ǵ���
				result.put(MESSAGE, ConstUtil.MESSAGE_ISLASTESTVERSIONFLAG_0);
			} else {
				// ����
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
		LOG.debug("ȡ�����е�������: " + getDataSize(list) + " ��");
	}
}
