package com.bjsasc.ddm.distribute.action.distributedestroyorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.action.AbstractAction;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.model.recdesinfo.RecDesInfo;
import com.bjsasc.ddm.distribute.model.recdespapertask.RecDesPaperTask;
import com.bjsasc.ddm.distribute.service.distributedestroyorder.DistributeDestroyOrderService;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.bjsasc.plm.type.TypeService;
import com.cascc.avidm.util.SplitString;

/**
 * �������ٵ�Actionʵ����
 * 
 * @author guowei 2013-4-10
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DistributeDestroyOrderAction extends AbstractAction {
	/** serialVersionUID */
	private static final long serialVersionUID = -2453469342445459220L;
	private static final Logger LOG = Logger.getLogger(DistributeDestroyOrderAction.class);
	private final DistributeDestroyOrderService service = DistributeHelper.getDistributeDestroyOrderService();
	Map<String, String> map = new HashMap<String, String>();

	/**
	 * ��ʼ��ҳ��
	 * @return
	 */
	public String initDistributeDestroyLaod() {
		initParam();
		try {
			// ������
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SOPT_LISTDISTRIBUTEDESTORYORDERS);
			request.setAttribute("init", "true");
			request.setAttribute("destroyType", request.getParameter("destroyType"));
			
		} catch (Exception ex) {
			error(ex);
		}
		return INITPAGE;
	}

	/**
	 * ���Ź���--���յ�/���ٵ��˵�
	 * ҳ������һ����ʾ��
	 * ȡ��ֽ��������������Ϊ���ѷ��͡������ݡ�
	 * /ddm/distributesendorder/listDistributeDestorySearch.jsp
	 * ȥ�Ļ�������ֽ��������������Ϊ���ַ��С������ݡ�
	 * @return strutsҳ�����
	 * 
	 * @modify Sun Zongqing
	 */
	public String getAllDistributeDestoryTasks() {
		initParam();
		session.setAttribute("DDM_DISTRIBUTE_INSIDE", map);
		try {
			
			String lc = ConstUtil.LC_DISTRIBUTED.getName();
			List<DistributePaperTask> listDis = service.getAllDistributePaperTask(map, lc);
			LOG.debug("ȡ�û������ٵ�ֽ������ " + getDataSize(listDis) + " ������������״̬��" + lc);
			
			String lcRecDes = ConstUtil.LC_DISTRIBUTING.getName();
			List<RecDesPaperTask> listRecDesPaperTask = service.getAllRecDesPaperTask(map, lcRecDes);
			LOG.debug("ȡ�û������ٵ�ֽ������ " + getDataSize(listRecDesPaperTask) + " ������������״̬��" + lcRecDes);

			String spot = ConstUtil.SOPT_LISTDISTRIBUTEDESTORYORDERS;
			List<Map<String, Object>> listDatas = new ArrayList<Map<String, Object>>();
			
			// ������֤Ȩ��
			List<Map<String, Object>> listRecDesDatas = checkPermissionAF(listRecDesPaperTask, spot, spot);
			listDatas.addAll(listRecDesDatas);

			//����service��д�õķ��������ε��������������������ֽ������
			List<DistributePaperTask> disPaperTaskList = service.removeDisTaskConflictWithRecDesTask(listDis, listRecDesPaperTask);
			// ������֤Ȩ��
			List<Map<String, Object>> listPaperTaskDatas = checkPermissionAF(disPaperTaskList, spot, spot);
			listDatas.addAll(listPaperTaskDatas);

			// ������
			GridDataUtil.prepareRowObjects(listDatas, spot);

			result.put("success","true");
			result.put("flag", listDis.size()+"");
			//success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * �����ϸ�Ļ��������������ϸ��Ϣ������������Ϣ�����ݣ�
	 * ��ַ��������ϸ��Ϣ���ַ���Ϣ�����ݣ�
	 * 
	 * @return ��Strust��׽�ķ�����Ϣ
	 * @modify Sun Zongqing
	 */
	public String getDistributeDestroyDetails() {
		try {
			map = (Map<String, String>) session.getAttribute("DDM_DISTRIBUTE_INSIDE");
			String destroyType = map.get("destroyType");
			String taskOid = request.getParameter(KeyS.OIDS);
			List<String> oids = SplitString.string2List(taskOid, ",");
			
			String disOids = "";
			String recDesOids = "";
			//�Ӵ�������oid�У���ַַ���Ϣ�����������Ϣ��oid
			for(String oid:oids){
				if(DistributePaperTask.CLASSID.equals(Helper.getClassId(oid))){
					disOids += (oid+",");
				}else if(RecDesPaperTask.CLASSID.equals(Helper.getClassId(oid))){
					recDesOids += (oid+",");
				}
			}
			if(disOids != ""){
				disOids = disOids.substring(0,disOids.length()-1);
			}else if(recDesOids!=""){
				recDesOids = recDesOids.substring(0,recDesOids.length()-1);
			}
			
			List<DistributeInfo> listDis = service.getDistributeDestroyDetails(map, disOids);
			//��û���������Ϣ
			List<RecDesInfo> listRecDes = service.getRecDesDetails(map, recDesOids);
			//���ε�����������������Ϣ�ķַ���Ϣ
			List<DistributeInfo> listDisAfterRemove = service.removeDisInfoConflictWithRecDesInfo(listDis, listRecDes);
			// ��ʽ����ʾ����
			TypeService typeManager = Helper.getTypeManager();
			
			//��ʽ���ַ���Ϣ
			for (DistributeInfo target : listDisAfterRemove) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mapAll = new HashMap<String, Object>();
				Map<String, Object> mainMap = typeManager.format(target);
				Map<String, Object> objMap = typeManager.format(target.getDistributeObject());
				mapAll.putAll(objMap);
				mainMap.put("NUMBER", objMap.get("NUMBER"));
				mainMap.put("NAME", objMap.get("NAME"));
				mapAll.putAll(mainMap);
				listMap.add(mapAll);
			}
			
			//��ʽ������������Ϣ
			for (RecDesInfo target : listRecDes) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mapAll = new HashMap<String, Object>();
				Map<String, Object> mainMap = typeManager.format(target);
				Map<String, Object> objMap = typeManager.format(target.getDistributeObject());
				mapAll.putAll(objMap);
				mainMap.put("NUMBER", objMap.get("NUMBER"));
				mainMap.put("NAME", objMap.get("NAME"));
				if("0".equals(destroyType)){
					mainMap.put("RECOVERDESTROYNUM", mainMap.get("NEEDRECOVERNUM"));
					mainMap.put("NEEDRECOVERNUM", mainMap.get("NEEDRECOVERNUM"));
				}else if("1".equals(destroyType)){
					mainMap.put("RECOVERDESTROYNUM", mainMap.get("NEEDDESTROYNUM"));
					mainMap.put("NEEDDESTROYNUM", mainMap.get("NEEDDESTROYNUM"));
				}
				mapAll.putAll(mainMap);
				listMap.add(mapAll);
			}
			
			LOG.debug("ȡ�û������ٵ�������Ϣ " + getDataSize() + " ��");
			
			
			
			success();
		} catch (Exception ex) {
			error(ex);
		}
		listToJson();
		return OUTPUTDATA;
	}

	/**
	 * ���ķַ���Ϣ�Ļ���������Ŀ������������ݵ�
	 * �������ڣ��ַ���Ϣ������������Ϣ��ֽ������
	 * ��������ֽ�����񡢻��յ������ٵ���
	 * 
	 * @return ��Strust��׽�ķ�����Ϣ
	 * @modify Sun Zongqing
	 */
	public String updateDestroyNum() {
		try {
			map = (Map<String, String>) session.getAttribute("DDM_DISTRIBUTE_INSIDE");
			String taskOids = request.getParameter("taskOids");
			String oids = request.getParameter(KeyS.OIDS);
			String destroyNums = request.getParameter("destroyNums");
			
			List<String> oidList = SplitString.string2List(oids, ",");
			List<String> numList = SplitString.string2List(destroyNums, ",");
			List<String> taskList = SplitString.string2List(taskOids, ",");
			String disOids = "";
			String recDesOids = "";
			String disNums="";
			String recDesNums="";
			String disTask = "";
			String recDesTask = "";
			//�Ӵ�������oid�У���ַַ���Ϣ�����������Ϣ��oid
			for(int i =0; i < oidList.size();i++){
				if(DistributeInfo.CLASSID.equals(Helper.getClassId(oidList.get(i)))){
					disOids += (oidList.get(i)+",");
					disNums += (numList.get(i)+",");
				}else if(RecDesInfo.CLASSID.equals(Helper.getClassId(oidList.get(i)))){
					recDesOids += (oidList.get(i)+",");
					recDesNums += (numList.get(i)+",");
				}
			}
			for(String oid:taskList){
				if(DistributePaperTask.CLASSID.equals(Helper.getClassId(oid))){
					disTask += (oid+",");
				}else if(RecDesPaperTask.CLASSID.equals(Helper.getClassId(oid))){
					recDesTask += (oid+",");
				}
			}
			
			//�ж�oid�Ƿ�Ϊ��
			if(!StringUtil.isStringEmpty(disOids)){
				disOids = disOids.substring(0,disOids.length()-1);
				disNums = disNums.substring(0,disNums.length()-1);
				disTask = disTask.substring(0,disTask.length()-1);
			}else if(!StringUtil.isStringEmpty(recDesOids)){
				recDesOids = recDesOids.substring(0,recDesOids.length()-1);
				recDesNums = recDesNums.substring(0,recDesNums.length()-1);
				recDesTask = recDesTask.substring(0,recDesTask.length()-1);
			}
			service.updateDestroyNum(map, disTask, disOids, disNums);
			service.updateDestroyNumByRecDesTask(map, recDesTask, recDesOids, recDesNums);

			//����ȡ�����������б�
			String lc = ConstUtil.LC_DISTRIBUTED.getName();
			List<DistributePaperTask> listDis = service.getAllDistributePaperTask(map, lc);
			LOG.debug("ȡ�û������ٵ�ֽ������ " + getDataSize(listDis) + " ������������״̬��" + lc);
			
			String lcRecDes = ConstUtil.LC_DISTRIBUTING.getName();
			List<RecDesPaperTask> listRecDesPaperTask = service.getAllRecDesPaperTask(map, lcRecDes);
			LOG.debug("ȡ�û������ٵ�ֽ������ " + getDataSize(listRecDesPaperTask) + " ������������״̬��" + lcRecDes);

			String spot = ConstUtil.SOPT_LISTDISTRIBUTEDESTORYORDERS;
			List<Map<String, Object>> listDatas = new ArrayList<Map<String, Object>>();
			
			// ������֤Ȩ��
			List<Map<String, Object>> listRecDesDatas = checkPermissionAF(listRecDesPaperTask, spot, spot);
			listDatas.addAll(listRecDesDatas);

			//����service��д�õķ��������ε��������������������ֽ������
			List<DistributePaperTask> disPaperTaskList = service.removeDisTaskConflictWithRecDesTask(listDis, listRecDesPaperTask);
			// ������֤Ȩ��
			List<Map<String, Object>> listPaperTaskDatas = checkPermissionAF(disPaperTaskList, spot, spot);
			listDatas.addAll(listPaperTaskDatas);

			// ������
			GridDataUtil.prepareRowObjects(listDatas, spot);
			
			success();

		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * ȡ�����еĻ�������ֽ������
	 * 
	 * @return ��strust��׽�ķ�����Ϣ
	 * @author Sun Zongqing
	 */
	public String getAllRecDesPaperTask(){
		try{
		String spot = ConstUtil.SPOT_LISTDISTRIBUTETASKS;
		//��������״̬
		String lc = ConstUtil.LC_DISTRIBUTING.getName();
		//���ָ����������״̬�Ļ�������ֽ������
		List<RecDesPaperTask> taskList = service.getAllRecDesPaperTask(lc);
		
		LOG.debug("ȡ�÷��ŵ� " + getDataSize(taskList) + " ������������״̬��" + lc);
		
		// ������֤Ȩ��
		List<Map<String, Object>> listDatas = checkPermissionAF(taskList, spot, spot);
		// ������
		GridDataUtil.prepareRowObjects(listDatas, spot);
		}catch(Exception ex){
			error(ex);
		}
		return "listRecDesPaperTask";
	}
	
	private void initParam() {
		map.put("taskName", request.getParameter("taskName"));
		map.put("disObj", request.getParameter("disObj"));
		map.put("taskCode", getParameter("taskCode"));
		map.put("creator", getParameter("creator"));
		map.put("creatorName", request.getParameter("creatorName"));
		map.put("disInfo", getParameter("disInfo"));
		map.put("infoClassId", getParameter("infoClassId"));
		map.put("disInfoName", request.getParameter("disInfoName"));
		map.put("flag", getParameter("flag"));
		map.put("queryCreateDateFrom", getParameter("queryCreateDateFrom"));
		map.put("queryCreateDateTo", getParameter("queryCreateDateTo"));
		map.put("destroyType", getParameter("destroyType"));
		map.put("oid", getParameter("oid"));
	}

	/**
	 * Excel������á�
	 */
	public void paperTaskExcel() {
		map = (Map<String, String>) session.getAttribute("DDM_DISTRIBUTE_INSIDE");
		String oids = request.getParameter(KeyS.OIDS);
		String destroyType = request.getParameter("destroyType");
		LOG.debug("ִ��paperTaskExcel() ������� OID = " + oids);
		String destroyNums = request.getParameter("destroyNums");
		LOG.debug("paperTaskExcel() �������[����/���ٷ���] : destroyNums = " + destroyNums);
		Map mapObject = new HashMap();
		mapObject.put("destroyNums", destroyNums);

		mapObject.put("oids", oids);
		mapObject.putAll(map);
		try {
			// ȡ�ö���
			HSSFWorkbook wBook = service.getExcelObject(mapObject);
			// Excel�ļ������
			String file_name = "";
			//1:���ٵ���0:���յ�
			if("0".equals(destroyType)){
				file_name = ConstUtil.REC_TITLE_NAME;
			}else{
				file_name = ConstUtil.DES_TITLE_NAME;
			}
			exportExcel(wBook, file_name);
		} catch (Exception ex) {
			LOG.debug("Excel�����쳣", ex);
			throw new RuntimeException(ex);
		}
	}
}
