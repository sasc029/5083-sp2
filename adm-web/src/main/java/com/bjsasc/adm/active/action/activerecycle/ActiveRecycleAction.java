package com.bjsasc.adm.active.action.activerecycle;

import java.util.List;

import org.apache.log4j.Logger;

import com.bjsasc.adm.active.action.AbstractAction;
import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.model.activerecycle.ActiveRecycle;
import com.bjsasc.adm.active.service.activerecycleservice.ActiveRecycleService;
import com.bjsasc.adm.active.service.admmodelservice.AdmModelService;
import com.bjsasc.adm.common.AdmLifeCycleConstUtil;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.folder.SubFolder;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.bjsasc.plm.util.Ajax;
import com.cascc.avidm.util.SplitString;

/**
 * ����վActionʵ���ࡣ
 * 
 * @author gengancong 2013-6-3
 */
public class ActiveRecycleAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = 6224514110020121703L;

	/** ����վ���� */
	ActiveRecycleService service = AdmHelper.getActiveRecycleService();

	/** Log���� */
	private static final Logger LOG = Logger.getLogger(ActiveRecycleAction.class);

	/**
	 * ȡ�����л���վ����
	 * 
	 * @return INITPAGE
	 */
	public String getAllRecycle() {
		try {
			List<ActiveRecycle> arList = service.listItems();
			LOG.debug("ȡ�û�������: " + getDataSize(arList) + " ��");
			// ������
			GridDataUtil.prepareRowObjects(arList, "ListActiveRecycles");
		} catch (Exception ex) {
			error(ex);
		}
		return INITPAGE;
	}

	/**
	 * ��Ӷ������վ��
	 */
	public String addRecycle() {
		try {
			String oids = request.getParameter("OIDS");

			boolean hasModelFolder = false, deleteFlag = true;
			AdmModelService modelService = AdmHelper.getAdmModelService();
			List<String> oidList = SplitString.string2List(oids, ",");
			for (String oid : oidList) {
				Persistable object = Helper.getPersistService().getObject(oid);
				if (modelService.isModelFolder(oid)) {
					SubFolder sf = (SubFolder) object;
					hasModelFolder = true;
					result.put(Ajax.SUCCESS, Ajax.FASLE);
					result.put(Ajax.MESSAGE, "ģ�Ͷ�Ӧ���ļ��� [" + sf.getName()
							+ "] �����Ա�ɾ����");
					break;
				}
				if (object instanceof LifeCycleManaged && object != null) {
					LifeCycleManaged lcm = (LifeCycleManaged) object;
					boolean flag = false;
					String stateName = lcm.getLifeCycleInfo().getStateName();
					/** �������ݹ���-��������-������ */
					if (AdmLifeCycleConstUtil.LC_APPROVING.getName().equals(stateName)) {
						flag = true;
					} else
					/** �������ݹ���-��������-�ܿ��� */
					if (AdmLifeCycleConstUtil.LC_CONTROLLING.getName().equals(stateName)) {
						flag = true;
					} else
					/** �������ݹ���-��������-������ */
					if (AdmLifeCycleConstUtil.LC_PROVIDING.getName().equals(stateName)) {
						flag = true;
					} else
					/** �������ݹ���-��������-��ɾ�� */
					if (AdmLifeCycleConstUtil.LC_RECYCLE.getName().equals(stateName)) {
						flag = true;
					}
					if (flag) {
						deleteFlag = false;
						result.put(Ajax.SUCCESS, Ajax.FASLE);
						result.put(Ajax.MESSAGE, "��������״̬Ϊ ["+stateName+"] �����ݲ����Ա�ɾ����");
						break;
					}
				}
			}
			if (!hasModelFolder && deleteFlag) {
				service.addltem(oids);
				success();
			}
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * ɾ������վ��
	 */
	public String deleteRecycle() {
		try {
			String oids = request.getParameter("OIDS");
			service.deleteItems(oids);
			// ������ʾ����
			getAllRecycle();

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * ��ջ���վ��
	 */
	public String clearRecycle() {
		try {
			service.clearItems();
			// ������ʾ����
			getAllRecycle();

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * ��ԭ����վ��
	 */
	public String reductionRecycle() {
		try {
			String oids = request.getParameter("OIDS");
			service.reductionItems(oids);
			// ������ʾ����
			getAllRecycle();

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}
}
