package com.bjsasc.ddm.distribute.action.duplicateprocess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.action.AbstractAction;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.model.duplicateprocess.DuplicateProcess;
import com.bjsasc.ddm.distribute.service.duplicateprocess.DuplicateProcessService;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.grid.GridHelper;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.bjsasc.plm.type.TypeService;

/**
 * ���Ƽӹ�Actionʵ����
 * 
 * @author guowei 2013-3-04
 * 
 */
public class DuplicateProcessAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = -6531961814059716149L;

	private final DuplicateProcessService service = DistributeHelper.getDuplicateProcessService();

	private static final Logger LOG = Logger.getLogger(DuplicateProcessAction.class);

	/**
	 * ���Ź���--���Ƽӹ��˵�
	 * δǩ������ҳ������һ����ʾ��
	 * ȡ��ֽ��������������Ϊ������δǩ�ա������ݡ�
	 * /ddm/duplicateprocess/listDuplicateNoReceive.jsp
	 * 
	 * @return strutsҳ�����
	 */
	public String getAllDuplicateNoReceiveTask() {
		try {
			String lc = ConstUtil.LC_DUPLICATE_PROCESS_NOT_RECEIVED.getName();
			List<DistributePaperTask> listDis = service.getAllDistributePaperTaskByAuth(lc);
			LOG.debug("ȡ��ֽ������ " + getDataSize(listDis) + " ������������״̬��" + lc);

			String spot = ConstUtil.SPOT_LISTDISTRIBUTEPAPERTASKS;
			
			// ������֤Ȩ��
			List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
			
			// ������
			GridDataUtil.prepareRowObjects(listDatas, spot);

		} catch (Exception ex) {
			error(ex);
		}
		return "noReceivePage";
	}

	/**
	 * ���Ź���--���Ƽӹ��˵�
	 * ��ǩ������ҳ������һ����ʾ��
	 * ȡ��ֽ��������������Ϊ�������С������ݡ�
	 * /ddm/duplicateprocess/listDuplicateReceive.jsp
	 * 
	 * @return strutsҳ�����
	 */
	public String getAllDuplicateReceiveTask() {
		try {
			String lc = ConstUtil.LC_DUPLICATE_PROCESS_RECEIVED.getName();
			List<DistributePaperTask> listDis = service.getAllDistributePaperTaskByAuth(lc);
			LOG.debug("ȡ��ֽ������ " + getDataSize(listDis) + " ������������״̬��" + lc);

			String spot = ConstUtil.SPOT_LISTDISTRIBUTEPAPERTASKS;

			// ������֤Ȩ��
			List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
			
			// ������
			GridDataUtil.prepareRowObjects(listDatas, spot);

		} catch (Exception ex) {
			error(ex);
		}
		return INITPAGE;
	}

	/**
	 * ���Ź���--���Ƽӹ��˵�
	 * ���˻�����ҳ������һ����ʾ��
	 * ȡ��ֽ��������������Ϊ�����Ʊ��˻ء������ݡ�
	 * /ddm/duplicateprocess/listDistributeTaskReturn.jsp
	 * 
	 * @return strutsҳ�����
	 */
	public String getAllDistributeTaskRollBack() {
		try {
			String lc = ConstUtil.LC_DUPLICATE_BACKOFF.getName();
			List<DistributePaperTask> listDis = service.getAllDistributePaperTaskReturnByAuth(lc);
			LOG.debug("ȡ��ֽ������ " + getDataSize(listDis) + " ������������״̬��" + lc);
			
			String spot = ConstUtil.SOPT_LISTDISTRIBUTEPAPERTASKRETURN;
			
			// ������֤Ȩ��
			List<Object> checkList = checkPermission(listDis, spot, spot);
			
			List<String> keys = GridHelper.getService().getMyLatestGridViewColumnIds(spot, spot);
			
			// ��ʽ����ʾ����
			TypeService typeManager = Helper.getTypeManager();
			Map<String, Object> mapAll = null;
			for (Object obj : checkList) {
				if (obj == null) {
					continue;
				}
				mapAll = new HashMap<String, Object>();
				DistributePaperTask target = (DistributePaperTask)obj;
				Map<String, Object> mainMap = typeManager.format(target, keys, false);
				Map<String, Object> returnMap = typeManager.format(target.getReturnReason(), keys, false);
				mapAll.putAll(returnMap);
				mapAll.putAll(mainMap);
				mapAll.put(KeyS.ACCESS, true);
				listMap.add(mapAll);
			}

			GridDataUtil.prepareRowObjectMaps(listMap, spot);

		} catch (Exception ex) {
			error(ex);
		}
		return "distributeTaskReturn";
	}

	@SuppressWarnings("unchecked")
	public String getDuplicateProcessList() {
		listMap = (List<Map<String, Object>>) session.getAttribute("session_DuplicateProcessList");
		listToJson();
		return OUTPUTDATA;
	}

	/**
	 * ȡ����Ӹ��Ƽӹ���Ա�ĸ��Ƽӹ�����
	 * 
	 * @return JSON����
	 */
	public String initDuplicateProcessList() {
		try {
			session.removeAttribute("session_DuplicateProcessList");
			getDuplicateProcessData();

		} catch (Exception ex) {
			error(ex);
		}
		return "initDuplicateProcessList";
	}

	private void getDuplicateProcessData() {
		String oids = request.getParameter(KeyS.OIDS);
		List<DistributePaperTask> listDis = service.getDuplicateProcessInfo(oids);
		// ��ʽ����ʾ����
		TypeService typeManager = Helper.getTypeManager();
		for (DistributePaperTask target : listDis) {
			if (target == null) {
				continue;
			}
			listMap.add(typeManager.format(target));
		}
		LOG.debug("ȡ�üӹ�����" + getDataSize(listDis) + " ��");
		session.setAttribute("session_DuplicateProcessList", listMap);
	}

	@SuppressWarnings("unchecked")
	public String updateDuplicateProcessList() {
		String flag = request.getParameter("flag");
		try {
			String userName = request.getParameter("username");

			String name = userName;
			listMap = (List<Map<String, Object>>) session.getAttribute("session_DuplicateProcessList");

			for (Map<String, Object> map : listMap) {
				if (("1").equals(flag)) {
					map.put("COLLATOR", name);
				} else {
					map.put("CONTRACTOR", name);
				}
			}
			
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * ��������ˣ������ˡ�
	 * 
	 * @return
	 */
	public String updateDuplicateProcessor() {
		try {
			String oids = request.getParameter(KeyS.OIDS);
			String collator = request.getParameter("collator");
			String contractor = request.getParameter("contractor");
			service.updateDuplicateProcessor(oids, collator, contractor);
			
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * ¼��ӹ���Ϣ
	 * 
	 * @return
	 */
	public String initDuplicateProcessInfoList() {
		try {
			String oid = request.getParameter("OID");
			List<DistributeObject> listDis = service.listDuplicateProcessInfo(oid);
			// ��ʽ����ʾ����
			TypeService typeManager = Helper.getTypeManager();
			for (DistributeObject target : listDis) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mapAll = new HashMap<String, Object>();
				Map<String, Object> mainMap = typeManager.format(target);
				if (target.getDuplicateProcess() != null) {
					Map<String, Object> duplicateMap = typeManager.format(target.getDuplicateProcess());
					mapAll.putAll(duplicateMap);
				}
				if (target.getReturnReason() != null) {
					Map<String, Object> returnMap = typeManager.format(target.getReturnReason());
					mapAll.putAll(returnMap);
				}
				mapAll.putAll(mainMap);
				mapAll.put("finishTime", target.getFinishTime());
				listMap.add(mapAll);
			}
			LOG.debug("ȡ��¼��ӹ���Ϣ" + getDataSize() + " ��");
			session.setAttribute("session_processInfoList", listMap);

		} catch (Exception ex) {
			error(ex);
		}
		return "listAddProcessInfo";
	}

	@SuppressWarnings("unchecked")
	public String listDuplicateProcessInfo() {
		listMap = (List<Map<String, Object>>) session.getAttribute("session_processInfoList");
		listToJson();
		return OUTPUTDATA;
	}
	public String listDuplicateProcess() {
		try {
			String oid = request.getParameter("OID");
		List<DistributeObject> listDis = service.listDuplicateProcessInfo(oid);
		// ��ʽ����ʾ����
		TypeService typeManager = Helper.getTypeManager();
		for (DistributeObject target : listDis) {
			if (target == null) {
				continue;
			}
			Map<String, Object> mapAll = new HashMap<String, Object>();
			Map<String, Object> mainMap = typeManager.format(target);
			if (target.getDuplicateProcess() != null) {
				Map<String, Object> duplicateMap = typeManager.format(target.getDuplicateProcess());
				mapAll.putAll(duplicateMap);
			}
			if (target.getReturnReason() != null) {
				Map<String, Object> returnMap = typeManager.format(target.getReturnReason());
				mapAll.putAll(returnMap);
			}
			mapAll.putAll(mainMap);
			mapAll.put("finishTime", target.getFinishTime());
			listMap.add(mapAll);
		}
		LOG.debug("ȡ��¼��ӹ���Ϣ" + getDataSize() + " ��");
	} catch (Exception ex) {
		error(ex);
	}
		listToJson();
		return OUTPUTDATA;
	
	}

	/**
	 * ���¼ӹ���Ϣ
	 * 
	 * @return
	 */
	public String updateProcessInfoList() {
		try {
			String collators = request.getParameter("collator");
			String contractors = request.getParameter("contractor");
			String oids = request.getParameter("OIDS");
			String taskOid = request.getParameter("taskOid");
			service.updateProcessInfo(collators, contractors, oids, taskOid);

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}
	/**
	 * ��֤Ҫ�ύ�������Ƿ�¼�븴�Ƽӹ���Ϣ
	 * 
	 * @return
	 */
	public String checkDuplicateInfo() {
		try {
			String oids = request.getParameter("OIDS");
			String[] oidsArray = oids.split(",");
			String number = "";

			for(String oid:oidsArray){
				List<DistributeObject> listDis = service.listDuplicateProcessInfo(oid);
				for (DistributeObject target : listDis) {
					if (target == null) {
						continue;
					}
					if (target.getDuplicateProcess() != null) {
						DuplicateProcess duplicateInfo = target.getDuplicateProcess();
						if (duplicateInfo.getContractor() == null || duplicateInfo.getCollator() == null) {
							DistributePaperTask paperTask = (DistributePaperTask) Helper.getPersistService().getObject(oid);
							number = number + "\"" + paperTask.getNumber() + "\""  + ",";
						}
					} else {
						DistributePaperTask paperTask = (DistributePaperTask) Helper.getPersistService().getObject(oid);
						number = number + "\"" + paperTask.getNumber() + "\""  + ",";
					}
				}
			}
			if ("".equals(number)) {
				result.put("success", "true");
			} else {
				number = number.substring(0, number.length() - 1);
				result.put("success", number);
			}

		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}
}
