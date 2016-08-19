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
 * ������Actionʵ���ࡣ
 * 
 * @author yanjia 2013-6-4
 */
public class ActiveSetAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = -4131327954360034679L;

	/** �����׷��� */
	ActiveSetService service = AdmHelper.getActiveSetService();

	private static final Logger LOG = Logger.getLogger(ActiveSetAction.class);
	
	private static final String MESSAGE = "message";

	/**
	 * ȡ�������ס�
	 * 
	 * @return JSON����
	 */
	public String getAllActiveSet() {
		try {
			List<ActiveSet> list = service.getAllActiveSet();
			LOG.debug("ȡ������������: " + getDataSize(list) + " ��");
			// ������
			GridDataUtil.prepareRowObjects(list, ConstUtil.SPOT_LISTACTIVESET);
		} catch (Exception ex) {
			error(ex);
		}
		return INITPAGE;
	}

	/**
	 * ���������ס�
	 * 
	 * @return JSON����
	 */
	public String createActiveSet() {
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
			
			// �����ױ���
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
	 * ɾ�������ס�
	 * 
	 * @return JSON����
	 */
	public String deleteActiveSet() {
		try {
			// ������OIDS
			String oids = request.getParameter(KeyS.OIDS);

			// ɾ��������
			service.deleteActiveSet(oids);

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * ���������ס�
	 * 
	 * @return JSON����
	 */
	public String updataActiveSet() {
		try {			

			String[] keys = {
					// ������OID
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
			// ����������
			service.updataActiveSet(paramMap);

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * ����OIDSȡ��������
	 * 
	 * @return JSON����
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
		LOG.debug("ȡ������������: " + getDataSize(list) + " ��");
		// ������
		GridDataUtil.prepareRowObjects(list, ConstUtil.SPOT_LISTACTIVESET);
	}

	/**
	 * ����OIDSȡ��������
	 * 
	 * @return JSON����
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
			LOG.debug("ȡ������������: " + getDataSize() + " ��");
			// ������
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTACTIVESETOBJECT);
		} catch (Exception ex) {
			jsonError(ex);
		}
		listToJson();
		return OUTPUTDATA;
	}

	/**
	 * �������������
	 * 
	 * @return JSON����
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
	 * �������������
	 * @param String setOid
	 * @param String objectOids
	 * @return JSON����
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
	 * ɾ������������
	 * 
	 * @return JSON����
	 */
	public String deleteActiveSetObject() {
		try {
			// ������OIDS
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
	 * �Ƿ������ͼ�������ļ�����Link��
	 * 
	 * @return JSON����
	 */
	public String hasActiveSetLink() {
		try {
			// ������OIDS
			// String oid = request.getParameter(KeyS.OID);

			// ȡ����ͼ�������ļ�����Link
			// List<ActiveSetLink> list = service.getActiveSetLinkByObject(oid);

			result.put(MESSAGE, "");
//			if (list == null || list.isEmpty()) {
//				// ����ֱ��ɾ���ö���
//			} else {
//				// ȷ���Ƿ�Ҫɾ���ö���
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
	 * �Ƿ�������������Դ��
	 * 
	 * @return JSON����
	 */
	public String isActiveSetedObject() {
		try {
			// ������OIDS
			String oid = request.getParameter(KeyS.OID);
			boolean flag = service.isActiveSeted(Helper.getPersistService().getObject(oid));
			if (flag) {
				// ������������Դ
				result.put(MESSAGE, "");
			} else {
				// ��������������Դ
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
