package com.bjsasc.ddm.distribute.service.distributepapersigntask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributepapersigntask.DistributePaperSignTask;
import com.bjsasc.ddm.distribute.service.distributepapersigntask.DistributePaperSignTaskService;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.query.condition.Condition;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.grid.GridHelper;
import com.bjsasc.plm.system.task.TaskBoxService;
import com.bjsasc.plm.type.TypeService;
import com.bjsasc.plm.type.type.Type;
import com.bjsasc.plm.url.Url;
import com.bjsasc.ddm.common.CheckPermission;

/**
 * @author zhangguoqiang
 *
 */
public class TaskBoxService_DistributePaperSignTaskImpl implements TaskBoxService{
	
	private final DistributePaperSignTaskService disPaperSignTaskService = DistributeHelper.getDistributePaperSignTaskService();

	@Override
	public int countTasks_daiban(List<String> typeIds) {
		String spot = ConstUtil.SPOT_LISTDISTRIBUTETASKBOX;
		String notSign = ConstUtil.LC_NOT_SIGNED.getName();
		String Signed = ConstUtil.LC_SIGNED.getName();
		List<DistributePaperSignTask> disPaperSignTasklistDis = new ArrayList<DistributePaperSignTask>();
		//未签收纸质签收任务
		List<DistributePaperSignTask> disPaperSignTasklistDis_notSign = disPaperSignTaskService.getAllNoSignDistributePaperSignTask(notSign);
		disPaperSignTasklistDis.addAll(disPaperSignTasklistDis_notSign);
		//已签收纸质签收任务
		List<DistributePaperSignTask> disPaperSignTasklistDis_Signed = disPaperSignTaskService.getAllDistributePaperSignTask(Signed);
		disPaperSignTasklistDis.addAll(disPaperSignTasklistDis_Signed);
		// 批量验证权限
		List<Object> disPaperSignTasklistDatas = CheckPermission.checkPermission(disPaperSignTasklistDis, spot, spot);

		return disPaperSignTasklistDatas.size();
	}
	
	@Override
	public int countTasks_yulan(List<String> typeIds) {
		return 0;
	}

	@Override
	public int countTasks_daichuli(List<String> typeIds) {
		return 0;
	}

	@Override
	public List<Map<String, Object>> getTasks_daiban(Map<Type, Condition> typeCondition, List<String> theKeys) {
		List<Map<String, Object>> listDis = new ArrayList<Map<String, Object>>();
		String spot = ConstUtil.SPOT_LISTDISTRIBUTETASKBOX;
		String notSign = ConstUtil.LC_NOT_SIGNED.getName();
		String Signed = ConstUtil.LC_SIGNED.getName();
		List<DistributePaperSignTask> disPaperSignTasklistDis = new ArrayList<DistributePaperSignTask>();
		//未签收纸质签收任务
		List<DistributePaperSignTask> disPaperSignTasklistDis_notSign = disPaperSignTaskService.getAllNoSignDistributePaperSignTask(notSign);
		disPaperSignTasklistDis.addAll(disPaperSignTasklistDis_notSign);
		//已签收纸质签收任务
		List<DistributePaperSignTask> disPaperSignTasklistDis_Signed = disPaperSignTaskService.getAllDistributePaperSignTask(Signed);
		disPaperSignTasklistDis.addAll(disPaperSignTasklistDis_Signed);
		// 批量验证权限
		List<Object> disPaperSignTasklistDatas = CheckPermission.checkPermission(disPaperSignTasklistDis, spot, spot);

		List<String> keys = GridHelper.getService().getMyLatestGridViewColumnIds(spot, spot);
		TypeService typeManager = Helper.getTypeManager();

		for (Object obj : disPaperSignTasklistDatas) {
			if (obj == null) {
				continue;
			}
			DistributePaperSignTask target = (DistributePaperSignTask)obj;

			Map<String, Object> mapAll = new HashMap<String, Object>();
			Map<String, Object> mainMap = typeManager.format(target, keys, false);

			String url = Url.APP + "/ddm/public/visitObject.jsp?OID=" + target.getOid() + "&classId=" + target.getClassId();
			String fNumber = "<a href='#' onclick=\"ddm.tools.showModalDialogReloadTaskBox('"+url+"')\" title='{"+target.getNumber()+"}'>"+target.getNumber()+"</a>";
			String fName = "<a href='#' onclick=\"ddm.tools.showModalDialogReloadTaskBox('"+url+"')\" title='{"+target.getName()+"}'>"+target.getName()+"</a>";
			mainMap.put("NUMBER", fNumber);
			mainMap.put("NAME", fName);
			
			mapAll.putAll(mainMap);
			mapAll.put(KeyS.ACCESS, true);
			listDis.add(mapAll);
		}
		return listDis;
	}
	
	@Override
	public List<Map<String, Object>> getTasks_yulan(Map<Type, Condition> typeCondition, List<String> keys) {
		return null;
	}

	@Override
	public List<Map<String, Object>> getTasks_duban(Map<Type, Condition> typeCondition, List<String> keys) {
		return null;
	}

	@Override
	public List<Map<String, Object>> getTasks_daichuli(Map<Type, Condition> typeCondition, List<String> keys) {
		return null;
	}

	@Override
	public List<Map<String, Object>> getTasks_jiankong(Map<Type, Condition> typeCondition, List<String> keys) {
		return getTasks_duban(typeCondition, keys);
	}

	@Override
	public List<Map<String, Object>> getTasks_history(Map<Type, Condition> typeCondition, List<String> theKeys, long from, long to, String scope) {
		List<Map<String, Object>> listDis = new ArrayList<Map<String, Object>>();
		String spot = ConstUtil.SPOT_LISTDISTRIBUTETASKBOX;
		String completed = ConstUtil.LC_COMPLETED.getName();
		String refuse = ConstUtil.LC_REFUSE_SIGNED.getName();
		
		User currentUser = SessionHelper.getService().getUser();
		String stateName = "'" + completed + "','" + refuse + "'";
		String sql = "SELECT DISTINCT * FROM DDM_DIS_PAPERSIGNTASK "
				+ " WHERE STATENAME in (" + stateName + ") AND RECEIVEBYID = ? AND RECEIVEBYCLASSID = ? "
				+ " AND MODIFYTIME >= ? AND  MODIFYTIME <= ? ORDER BY MODIFYTIME DESC";
		List<DistributePaperSignTask> disPaperSignTasklistDis = Helper.getPersistService().query(sql, DistributePaperSignTask.class, 
				currentUser.getInnerId(), currentUser.getClassId(),from,to);
		// 批量验证权限
		List<Object> disPaperSignTasklistDatas = CheckPermission.checkPermission(disPaperSignTasklistDis, spot, spot);

		List<String> keys = GridHelper.getService().getMyLatestGridViewColumnIds(spot, spot);
		TypeService typeManager = Helper.getTypeManager();

		for (Object obj : disPaperSignTasklistDatas) {
			if (obj == null) {
				continue;
			}
			DistributePaperSignTask target = (DistributePaperSignTask)obj;

			Map<String, Object> mapAll = new HashMap<String, Object>();
			Map<String, Object> mainMap = typeManager.format(target, keys, false);

			String url = Url.APP + "/ddm/public/visitObject.jsp?OID=" + target.getOid() + "&classId=" + target.getClassId();
			String fNumber = "<a href='#' onclick=\"ddm.tools.showModalDialogReloadTaskBox('"+url+"')\" title='{"+target.getNumber()+"}'>"+target.getNumber()+"</a>";
			String fName = "<a href='#' onclick=\"ddm.tools.showModalDialogReloadTaskBox('"+url+"')\" title='{"+target.getName()+"}'>"+target.getName()+"</a>";
			mainMap.put("NUMBER", fNumber);
			mainMap.put("NAME", fName);
			
			mapAll.putAll(mainMap);
			mapAll.put(KeyS.ACCESS, true);
			listDis.add(mapAll);
		}
		return listDis;
	
	
	}

	@Override
	public void abortTasks(List<String> taskOids) {		
	}

	@Override
	public void assignTasks(List<String> taskOids) {
	}

	@Override
	public void startTasks(List<String> taskOids) {
	}

	@Override
	public void refuseTasks(List<String> taskOids) {
	}

	@Override
	public void pauseTasks(List<String> taskOids) {
	}

	@Override
	public void deleteTasks(List<String> taskOids) {
	}	
}
