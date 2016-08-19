package com.bjsasc.ddm.distribute.service.distributeelectask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeelectask.DistributeElecTask;
import com.bjsasc.ddm.distribute.service.distributeelectask.DistributeElecTaskService;
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
public class TaskBoxService_DistributeElecTaskImpl implements TaskBoxService{
	
	private final DistributeElecTaskService disElecTaskService = DistributeHelper.getDistributeElecTaskService();

	@Override
	public int countTasks_daiban(List<String> typeIds) {
		String spot = ConstUtil.SPOT_LISTDISTRIBUTETASKBOX;
		String notSign = ConstUtil.LC_NOT_SIGNED.getName();
		String Signed = ConstUtil.LC_SIGNED.getName();
		List<DistributeElecTask> disElecTasklistDis = new ArrayList<DistributeElecTask>();
		//δǩ�յ�������
		List<DistributeElecTask> disElecTasklistDis_notSign = disElecTaskService.getAllNoSignDistributeElecTask(notSign);
		disElecTasklistDis.addAll(disElecTasklistDis_notSign);
		//��ǩ�յ�������
		List<DistributeElecTask> disElecTasklistDis_Signed = disElecTaskService.getAllDistributeElecTask(Signed);
		disElecTasklistDis.addAll(disElecTasklistDis_Signed);
		// ������֤Ȩ��
		List<Object> disElecTasklistDatas = CheckPermission.checkPermission(disElecTasklistDis, spot, spot);

		return disElecTasklistDatas.size();
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
		List<DistributeElecTask> disElecTasklistDis = new ArrayList<DistributeElecTask>();
		//δǩ�յ�������
		List<DistributeElecTask> disElecTasklistDis_notSign = disElecTaskService.getAllNoSignDistributeElecTask(notSign);
		disElecTasklistDis.addAll(disElecTasklistDis_notSign);
		//��ǩ�յ�������
		List<DistributeElecTask> disElecTasklistDis_Signed = disElecTaskService.getAllDistributeElecTask(Signed);
		disElecTasklistDis.addAll(disElecTasklistDis_Signed);
		// ������֤Ȩ��
		List<Object> disElecTasklistDatas = CheckPermission.checkPermission(disElecTasklistDis, spot, spot);

		List<String> keys = GridHelper.getService().getMyLatestGridViewColumnIds(spot, spot);
		TypeService typeManager = Helper.getTypeManager();

		boolean flat = disElecTaskService.isUserlimit();

		for (Object obj : disElecTasklistDatas) {
			if (obj == null) {
				continue;
			}
			DistributeElecTask target = (DistributeElecTask)obj;
			if (flat == false && Signed.equals(target.getStateName())) {
				target.setOperate("");
			}

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
		List<DistributeElecTask> disElecTasklistDis = new ArrayList<DistributeElecTask>();
		List<DistributeElecTask> disElecTasklistDisTemp = new ArrayList<DistributeElecTask>();
		String spot = ConstUtil.SPOT_LISTDISTRIBUTETASKBOX;
		//�����
		String completed = ConstUtil.LC_COMPLETED.getName();
		//�Ѿܾ�
		String refuse = ConstUtil.LC_REFUSE_SIGNED.getName();
		
		User currentUser = SessionHelper.getService().getUser();
		String stateName = "'" + completed + "','" + refuse + "'";
		//ȡ�õ�ǰ�û��յ�������
		String sql = "SELECT DISTINCT * FROM DDM_DIS_ELECTASK "
				+ " WHERE STATENAME in (" + stateName + ") AND RECEIVEBYID = ? AND RECEIVEBYCLASSID = ? "
				+ " AND MODIFYTIME >= ? AND  MODIFYTIME <= ? ";
		List<DistributeElecTask> disElecTasklistReceive = Helper.getPersistService().query(sql, DistributeElecTask.class,
				currentUser.getInnerId(), currentUser.getClassId(), from, to);
		
		disElecTasklistDisTemp.addAll(disElecTasklistReceive);

		//�жϵ�ǰ�û��Ƿ��Ƕ�������Ա
		boolean isSecondScheUser = disElecTaskService.isSecondScheUser();
		//�����ǰ�û��Ƕ�������Ա,ȡ��������������Ա��ɵ�����
		if(isSecondScheUser){
			//��ȡ��������Ա������ɵĵ�������
			List<DistributeElecTask> disElecTasklistSecondSche = disElecTaskService.getDistributeElecTaskForSecondSche(stateName, from, to);
			disElecTasklistDisTemp.addAll(disElecTasklistSecondSche);
		}

		//�жϵ�ǰ�û��Ƿ��ǿ��򷢷�Э��Ա
		boolean isCrossDomainUser = disElecTaskService.isCrossDomainUser();
		//�����ǰ�û��ǿ��򷢷�Э��Ա,ȡ���������򷢷�Э��Ա��ɵ�����
		if(isCrossDomainUser){
			//��ȡ���򷢷�Э��Ա������ɵĵ�������
			List<DistributeElecTask> disElecTasklistCrossDomain = disElecTaskService.getDistributeElecTaskForCrossDomain(stateName, from, to);
			disElecTasklistDisTemp.addAll(disElecTasklistCrossDomain);
		}
		
		//�ڶ�������Ա�Ϳ���Э��Ա�������п��ܴ����Լ����յ�������Ҫȥ���ظ�����
		List<String> disElecTaskInnerIdList = new ArrayList<String>();
		for(DistributeElecTask disElecTask : disElecTasklistDisTemp){
			if(!disElecTaskInnerIdList.contains(disElecTask.getInnerId())){
				disElecTaskInnerIdList.add(disElecTask.getInnerId());
				disElecTasklistDis.add(disElecTask);
			}
		}
		
		// ������֤Ȩ��
		List<Object> disElecTasklistDatas = CheckPermission.checkPermission(disElecTasklistDis, spot, spot);
		
		List<String> keys = GridHelper.getService().getMyLatestGridViewColumnIds(spot, spot);
		TypeService typeManager = Helper.getTypeManager();

		for (Object obj : disElecTasklistDatas) {
			if (obj == null) {
				continue;
			}
			DistributeElecTask target = (DistributeElecTask)obj;

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
