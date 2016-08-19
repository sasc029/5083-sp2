package com.bjsasc.ddm.distribute.service.distributedestroyorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.model.recdesinfo.RecDesInfo;
import com.bjsasc.ddm.distribute.model.recdespapertask.RecDesPaperTask;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.part.Part;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.principal.Organization;
import com.bjsasc.plm.core.system.principal.OrganizationHelper;
import com.bjsasc.plm.core.system.principal.Principal;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.system.principal.UserHelper;
import com.bjsasc.plm.core.type.TypeHelper;
import com.bjsasc.plm.core.util.DateTimeUtil;
import com.bjsasc.plm.core.util.ExcelUtil;
import com.bjsasc.plm.core.util.JsonUtil;
import com.bjsasc.plm.core.util.StringUtil;
import com.cascc.avidm.util.SplitString;

/**
 * ���յ������ٵ�ʵ���ࡣ
 * 
 * @author yanjia 2013-3-21
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DistributeDestroyOrderServiceImpl implements DistributeDestroyOrderService {
	String parms = "";

	public List<RecDesPaperTask> getAllRecDesPaperTask(String lc){
		//������л�������ֽ�������sql���
		String sql="SELECT T.* FROM DDM_RECDES_PAPERTASK T WHERE STATENAME = ? ORDER BY T.CREATETIME DESC";
		List<RecDesPaperTask> taskList = new ArrayList<RecDesPaperTask>();
		taskList = Helper.getPersistService().query(sql, RecDesPaperTask.class,lc);
		return taskList;
	}
	
	public List<DistributePaperTask> getAllDistributePaperTask(Map<String,String> map, Object stateName){
		List<DistributePaperTask> taskList = new ArrayList<DistributePaperTask>();

		String orderBy = " order by T.MODIFYTIME, f.MODIFYTIME desc";
		String mainSql = getSql(map);

		String sql1 = mainSql.replace("{0}", "t.*,f.*,b.*");
		sql1 = sql1.replace("{1}", orderBy);
		List<DistributePaperTask> paperList;
		String localParms = parms;
		if (localParms != "") {
			localParms += stateName + ",";
			String[] parm = localParms.split(",");
			paperList = Helper.getPersistService().query(sql1, DistributePaperTask.class, parm);
		} else {
			paperList = Helper.getPersistService().query(sql1, DistributePaperTask.class, stateName);
		}
		String sql2 = mainSql.replace("{0}", "f.*,t.*,b.*");
		sql2 = sql2.replace("{1}", orderBy);
		List<DistributeInfo> infoList;
		if (localParms != "") {
			String[] parm = localParms.split(",");
			infoList = Helper.getPersistService().query(sql2, DistributeInfo.class, parm);
		} else {
			infoList = Helper.getPersistService().query(sql2, DistributeInfo.class, stateName);
		}
		List keyList = new ArrayList();
		for (int i = 0; i < infoList.size(); i++) {
			DistributePaperTask distributePaperTask = paperList.get(i);
			DistributePaperTask task = distributePaperTask.cloneDisTask();
			DistributeInfo distributeInfo = infoList.get(i);
			//�ڽ��л�������������ʱ�����������Ŀ���ڷַ�������������ʾ����Ϣ
			if(distributeInfo.getDisInfoNum() == distributeInfo.getRecoverNum() && map.get("destroyType").equals("0")){
				continue;
			}
			
			task.setDisInfoName(distributeInfo.getDisInfoName());
			task.setDistributeInfoClassId(distributeInfo.getClassId());
			task.setDistributeInfoId(distributeInfo.getInnerId());

			String key = task.getInnerId() + task.getDisInfoName();
			if (keyList.contains(key)) {
				continue;
			}
			taskList.add(task);
			keyList.add(key);
		}
		return taskList;
	}
	
	@Override
	public List<DistributeInfo> getDistributeDestroyDetails(Map<String, String> map, Object taskOids) {
		if(StringUtil.isStringEmpty((String)taskOids)){
			return null;
		}
		List<String> oidList = SplitString.string2List((String) taskOids, ",");
		List<DistributeInfo> infoList = new ArrayList<DistributeInfo>();
		for (String taskOid : oidList) {
			Persistable obj = Helper.getPersistService().getObject(taskOid);
			DistributePaperTask dis = (DistributePaperTask) obj;
			String taskInnerId = Helper.getInnerId(taskOid);
			String taskClassId = Helper.getClassId(taskOid);
			String sql = " and t.innerId = ? and t.classId = ? order by T.MODIFYTIME, f.MODIFYTIME ";
			String mainSql = getSql(map);

			String sql1 = mainSql.replace("{0}", "f.*,t.*");
			sql1 = sql1.replace("{1}", sql);
			List<DistributeInfo> disInfoList;
			String localParms = parms;
			if (localParms != "") {
				localParms += ConstUtil.LC_DISTRIBUTED.getName() + "," + taskInnerId + "," + taskClassId + ",";
				String[] parm = localParms.split(",");

				disInfoList = Helper.getPersistService().query(sql1, DistributeInfo.class, parm);
			} else {
				disInfoList = Helper.getPersistService().query(sql1, DistributeInfo.class, ConstUtil.LC_DISTRIBUTED.getName(),
						taskInnerId, taskClassId);
			}
			for (DistributeInfo info : disInfoList) {
				//�жϻ�����Ŀ�Ƿ��Ѿ����ڷַ�����
				if(info.getDisInfoNum() == info.getRecoverNum() && map.get("destroyType").equals("0")){
					continue;
				}
				String objSql = "SELECT DISTINCT OBJ.* FROM DDM_DIS_ORDEROBJLINK LINK,DDM_DIS_OBJECT OBJ,DDM_DIS_INFO INFO WHERE OBJ.INNERID = LINK.TOOBJECTID AND OBJ.CLASSID = LINK.TOOBJECTCLASSID AND LINK.INNERID = INFO.DISORDEROBJLINKID AND LINK.CLASSID = INFO.DISORDEROBJLINKCLASSID AND INFO.INNERID = ? AND INFO.CLASSID =?";
				List<DistributeObject> list = PersistHelper.getService().query(objSql, DistributeObject.class,
						info.getInnerId(), info.getClassId());
				DistributeInfo disInfo = info.cloneDisInfo();
				disInfo.setDistributeObject(list.get(0));
				infoList.add(disInfo);
				disInfo.setTaskName(dis.getName());
			}
		}
		return infoList;
	}
	
	public List<RecDesPaperTask> getAllRecDesPaperTask(Map<String, String> map, Object stateName) {
		List<RecDesPaperTask> taskList = new ArrayList<RecDesPaperTask>();

		String orderBy = " order by T.MODIFYTIME, f.MODIFYTIME desc";
		String mainSql = getSqlForRecDes(map);
		String destroyType = map.get("destroyType");
		String sql1 = mainSql.replace("{0}", "t.*,f.*,b.*");
		sql1 = sql1.replace("{1}", orderBy);
		List<RecDesPaperTask> paperList;
		String localParms = parms;
		if (localParms != "") {
			localParms += stateName + ",";
			String[] parm = localParms.split(",");
			paperList = Helper.getPersistService().query(sql1, RecDesPaperTask.class, parm);
		} else {
			paperList = Helper.getPersistService().query(sql1, RecDesPaperTask.class, stateName);
		}
		String sql2 = mainSql.replace("{0}", "f.*,t.*,b.*");
		sql2 = sql2.replace("{1}", orderBy);
		List<RecDesInfo> infoList;
		if (localParms != "") {
			String[] parm = localParms.split(",");
			infoList = Helper.getPersistService().query(sql2, RecDesInfo.class, parm);
		} else {
			infoList = Helper.getPersistService().query(sql2, RecDesInfo.class, stateName);
		}
		List keyList = new ArrayList();
		for (int i = 0; i < infoList.size(); i++) {
			RecDesPaperTask recDesPaperTask = paperList.get(i);
			RecDesPaperTask task = recDesPaperTask.cloneRecDesPaperTask();
			RecDesInfo recDesInfo = infoList.get(i);
			if(destroyType.equals("0")&&recDesInfo.getNeedRecoverNum() == 0){
				continue;
			}else if(destroyType.equals("1")&&recDesInfo.getNeedDestroyNum() == 0){
				continue;
			}
			task.setDisInfoName(recDesInfo.getDisInfoName());
			task.setRecDesInfoClassId(recDesInfo.getClassId());
			task.setRecDesInfoId(recDesInfo.getInnerId());

			String key = task.getInnerId() + task.getDisInfoName();
			if (keyList.contains(key)) {
				continue;
			}
			taskList.add(task);
			keyList.add(key);
		}
		return taskList;
	}

	public List<DistributePaperTask> removeDisTaskConflictWithRecDesTask(List<DistributePaperTask> dis, List<RecDesPaperTask> rec){
		if(dis == null || dis.size() == 0 ){
			return new ArrayList<DistributePaperTask>();
		}else if(rec == null || rec.size() == 0){
			return dis;
		}
		List<DistributePaperTask> disList = new ArrayList<DistributePaperTask>();
		disList.addAll(dis);
		//sql��䣬���ڲ�ѯ�Ƿ�ǰ�ַ�������������ݣ��������˻������ٵ���
		String sql1 = "SELECT DISTINCT OBJ.* FROM "
				+ "DDM_DIS_OBJECT OBJ, "
				+ "DDM_DIS_INFO DINFO, "
				+ "DDM_DIS_ORDEROBJLINK OLINK, "
				+ "DDM_DIS_TASKINFOLINK TLINK, "
				+ "DDM_DIS_PAPERTASK DTASK "
				+ "WHERE DTASK.INNERID = ? "
				+ "AND DTASK.CLASSID = ? "
				+ "AND OBJ.INNERID = OLINK.TOOBJECTID "
				+ "AND OBJ.CLASSID = OLINK.TOOBJECTCLASSID "
				+ "AND OLINK.INNERID = DINFO.DISORDEROBJLINKID "
				+ "AND OLINK.CLASSID = DINFO.DISORDEROBJLINKCLASSID "
				+ "AND DINFO.INNERID = TLINK.TOOBJECTID "
				+ "AND DINFO.CLASSID = TLINK.TOOBJECTCLASSID "
				+ "AND TLINK.FROMOBJECTID = DTASK.INNERID "
				+ "AND TLINK.FROMOBJECTCLASSID = DTASK.CLASSID ";
		
		String sql2 = "SELECT DISTINCT OBJ.* FROM "
				+ "DDM_DIS_OBJECT OBJ, "
				+ "DDM_DIS_ORDEROBJLINK OLINK, "
				+ "DDM_DIS_TASKINFOLINK TLINK, "
				+ "DDM_RECDES_INFO RDINFO, "
				+ "DDM_RECDES_PAPERTASK RDTASK "
				+ "WHERE RDTASK.INNERID = ? "
				+ "AND RDTASK.CLASSID = ? "
				+ "AND OBJ.INNERID = OLINK.TOOBJECTID "
				+ "AND OBJ.CLASSID = OLINK.TOOBJECTCLASSID "
				+ "AND OLINK.INNERID = RDINFO.DISORDEROBJECTLINKID "
				+ "AND OLINK.CLASSID = RDINFO.DISORDEROBJECTLINKCLASSID "
				+ "AND RDINFO.INNERID = TLINK.TOOBJECTID "
				+ "AND RDINFO.CLASSID = TLINK.TOOBJECTCLASSID "
				+ "AND TLINK.FROMOBJECTID = RDTASK.INNERID "
				+ "AND TLINK.FROMOBJECTCLASSID = RDTASK.CLASSID ";
		
		for(DistributePaperTask dTask:dis){
			for(RecDesPaperTask rdTask:rec){
				
				List<DistributeObject> obj1 = Helper.getPersistService()
						.query(sql1,DistributeObject.class,
								dTask.getInnerId(),
								dTask.getClassId());
				List<DistributeObject> obj2 = Helper.getPersistService()
						.query(sql2,DistributeObject.class,
								rdTask.getInnerId(),
								rdTask.getClassId());
				//��������д�����ͬ�����ݣ����չʾ�б����޳�
				if(obj1.get(0).getInnerId().equals(obj2.get(0).getInnerId())){
					disList.remove(dTask);
					break;
				}
			}
		}
		
		return disList;
	}
	
	/**
	 * �����ṩ��map������Ϊ�������ٵ��Ĳ�ѯ�����һ��sql��ѯ���
	 * 
	 * @param map ��Ҫ�����Ļ�������ֽ���������Ϣ
	 * @return ���ɵĻ�������ֽ�������sql��ѯ���
	 * 
	 * @author Sun Zongqing 2014-06-16
	 */
	private String getSqlForRecDes(Map<String, String> map) {
		StringBuffer sbSql = new StringBuffer();
		//�˴�����ǰ�ķַ���Ϣ��Ϊ����������Ϣ����ֽ������(0)�ĳɻ�����������(3)
		sbSql.append("select DISTINCT {0} from DDM_RECDES_PAPERTASK t,DDM_RECDES_INFO f,DDM_DIS_TASKINFOLINK l,ddm_dis_orderobjlink k, ddm_dis_object b");
		sbSql.append(" where t.innerId = l.fromObjectId and t.classId = l.fromObjectClassId and  b.innerid=k.TOOBJECTID and  k.innerid=f.DISORDEROBJECTLINKID ");
		sbSql.append(" and l.toObjectId = f.innerId and l.toObjectClassId = f.classId");
		sbSql.append(" and f.DISMEDIATYPE = '0'");
		sbSql.append(getSubSql(map));
		return sbSql.toString();
	}
	
	/**
	 * �����ṩ��map������Ϊ�����״̬��ֽ���������һ��sql��ѯ���
	 * 
	 * @param map �ṩ��ѯ��Ϣ��map����
	 * @return String ���ɵ�sql���
	 * @modify Sun Zongqing
	 */
	private String getSql(Map<String,String> map){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select DISTINCT {0} from DDM_DIS_PAPERTASK t,DDM_DIS_INFO f,DDM_DIS_TASKINFOLINK l,ddm_dis_orderobjlink k, ddm_dis_object b");
		sbSql.append(" where t.innerId = l.fromObjectId and t.classId = l.fromObjectClassId and  b.innerid=k.TOOBJECTID and  k.innerid=f.DISORDEROBJLINKID ");
		sbSql.append(" and l.toObjectId = f.innerId and l.toObjectClassId = f.classId");
		sbSql.append(" and f.DISMEDIATYPE = '0'");
		sbSql.append(this.getSubSql(map));
		return sbSql.toString();
	}

	/**
	 * �����ṩ��map����������sql��ѯ���Ĺ�ͬ����
	 * 
	 * @param map �ṩ��ѯ�����Ĳ�ѯ���
	 * @return String ���ɵ��Ӳ�ѯ���
	 * @author Sun Zongqing
	 */
	private String getSubSql(Map<String,String> map){
		parms = "";
		String creatorIID = map.get("creator");
		String disInfoIID = map.get("disInfo");
		String flag = map.get("flag");
		String oid = map.get("oid");

		Principal principal = null;

		if (!"".equals(disInfoIID)) {
			if ("1".equals(flag)) {
				principal = OrganizationHelper.getService().getOrganization(disInfoIID);
			} else if ("2".equals(flag)) {
				principal = UserHelper.getService().getUser(disInfoIID);
			}
		}


		StringBuffer sbSql = new StringBuffer();
		Principal creator = null;
		
		String taskName = map.get("taskName");
		if (taskName != null && taskName.length() > 0) {
			sbSql.append(" and t.name like '%" + taskName + "%'");
		}
		String disObj = map.get("disObj");
		if (disObj != null && disObj.length() > 0) {
			sbSql.append(" and (b.name like '%" + disObj + "%'");
			sbSql.append(" or b.id like '%" + disObj + "%' )");
		}
	

		//������Χ�����Ĵ���
    	Map<String,Object> mapTaskCode=JsonUtil.toMap(map.get("taskCode"));
    	//������Χ����������������
    	String strContainerScope=mapTaskCode.get("containerScope").toString();
    	String strFavoriteScope=mapTaskCode.get("favoriteScope").toString();
    	//���������� ����Ҫ׷���κ�����
    	//���ղص�������
    	if(!strFavoriteScope.equals("") && strContainerScope.equals("")){
    		if (!StringUtil.isNull(strFavoriteScope)) {
				List<String> valList = SplitString.string2List(strFavoriteScope, ",");
	    		boolean isFirst=true;
	    		for (String val : valList) {
					String innerId = Helper.getInnerId(val);
					String classId = Helper.getClassId(val);
					if(isFirst){
						sbSql.append(" and (( t.contextId = ? and t.contextClassId = ?) ");
						parms += innerId + "," + classId + ",";
						isFirst=false;
					}
					else{
						sbSql.append(" or ( t.contextId = ? and t.contextClassId = ?) ");
						parms += innerId + "," + classId + ",";
					}
				}
	    		if (valList != null && valList.size() > 0  ) {
	    			sbSql.append(")");
	    		}
    		}
    	}

		if (creatorIID != null && creatorIID.length() > 0) {
			creator = UserHelper.getService().getUser(creatorIID);
			sbSql.append(" and t.createById = ? and t.createByClassId = ?");
			parms += creator.getInnerId() + "," + creator.getClassId() + ",";
		}

		if (disInfoIID != null && disInfoIID.length() > 0) {
			sbSql.append(" and f.disInfoId = ? and f.infoClassId = ?");
			parms += principal.getInnerId() + "," + principal.getClassId() + ",";
		}else if(oid != null && oid.length() > 0){
			sbSql.append(" and f.disInfoId = ? and f.infoClassId = ?");
			String innerId = Helper.getInnerId(oid);
			String classId = Helper.getClassId(oid);
			parms += innerId + "," + classId + ",";
		}

		String queryCreateDateFrom = map.get("queryCreateDateFrom");
		String queryCreateDateTo = map.get("queryCreateDateTo");
		long dateFrom = 0L;
		long dateTo = 0L;
		dateFrom = DateTimeUtil.getLongTime(queryCreateDateFrom);
		dateTo = DateTimeUtil.getLongTime(queryCreateDateTo) + 86399000L;
		if (!StringUtil.isNull(queryCreateDateFrom) && queryCreateDateTo.length() == 0) {
			sbSql.append(" and t.createTime >= ?");
			parms += dateFrom + ",";
		} else if (!StringUtil.isNull(queryCreateDateTo) && queryCreateDateFrom.length() == 0) {
			sbSql.append(" and t.createTime <= ?");
			parms += dateTo + ",";
		} else if (!StringUtil.isNull(queryCreateDateFrom) && !StringUtil.isNull(queryCreateDateTo)) {
			sbSql.append(" and t.createTime between ? and ?");
			parms += dateFrom + "," + dateTo + ",";
		}
		sbSql.append(" and f.stateName = ?");
		sbSql.append(" {1} ");
		return sbSql.toString();
	}
	
	public List<RecDesInfo> getRecDesDetails(Map<String, String> map, Object taskOids) {
		if(StringUtil.isStringEmpty((String)taskOids)){
			return new ArrayList<RecDesInfo>();
		}
		List<String> oidList = SplitString.string2List((String) taskOids, ",");
		List<RecDesInfo> infoList = new ArrayList<RecDesInfo>();
		for (String taskOid : oidList) {
			Persistable obj = Helper.getPersistService().getObject(taskOid);
			RecDesPaperTask dis = (RecDesPaperTask) obj;
			String taskInnerId = Helper.getInnerId(taskOid);
			String taskClassId = Helper.getClassId(taskOid);
			String sql = " and t.innerId = ? and t.classId = ? order by T.MODIFYTIME, f.MODIFYTIME ";
			String mainSql = getSqlForRecDes(map);

			String sql1 = mainSql.replace("{0}", "f.*,t.*");
			sql1 = sql1.replace("{1}", sql);
			List<RecDesInfo> recDesInfoList;
			String localParms = parms;
			if (localParms != "") {
				localParms += ConstUtil.LC_DISTRIBUTING.getName() + "," + taskInnerId + "," + taskClassId + ",";
				String[] parm = localParms.split(",");
				recDesInfoList = Helper.getPersistService().query(sql1, RecDesInfo.class, parm);
			} else {
				recDesInfoList = Helper.getPersistService().query(sql1, RecDesInfo.class, ConstUtil.LC_DISTRIBUTING.getName(),
						taskInnerId, taskClassId);
			}
			for (RecDesInfo info : recDesInfoList) {
				String objSql = "SELECT DISTINCT OBJ.* FROM "
						+ "DDM_DIS_ORDEROBJLINK LINK,"
						+ "DDM_DIS_OBJECT OBJ,"
						+ "DDM_RECDES_INFO INFO "
						+ "WHERE OBJ.INNERID = LINK.TOOBJECTID "
						+ "AND OBJ.CLASSID = LINK.TOOBJECTCLASSID "
						+ "AND LINK.INNERID = INFO.DISORDEROBJECTLINKID "
						+ "AND LINK.CLASSID = INFO.DISORDEROBJECTLINKCLASSID "
						+ "AND INFO.INNERID = ? "
						+ "AND INFO.CLASSID =?";
				List<DistributeObject> list = PersistHelper.getService().query(objSql, DistributeObject.class,
						info.getInnerId(), info.getClassId());
				RecDesInfo recDesInfo = info.cloneRecDesInfo();
				recDesInfo.setDistributeObject(list.get(0));
				infoList.add(recDesInfo);
				recDesInfo.setTaskName(dis.getName());
			}
		}
		return infoList;
	}
	
	public void updateDestroyNumByRecDesTask(Map<String, String> map, String taskOids, String oids, String destroyNums) {
		if(StringUtil.isStringEmpty(oids)){
			return;
		}
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		String destroyType = map.get("destroyType");
		boolean flagForDis = false;
		boolean flagForRecDes = false;
		//���������������Ϣ��صķַ���Ϣ����������
		flagForDis = updateDisInfobyRecDesInfoOids(oids, destroyNums, destroyType, life);
		//���»���������Ϣ����������
		flagForRecDes = updateRecDesInfo(oids, destroyType, life);
		
		if (flagForDis) {
			List<String> infoOidList = SplitString.string2List((String) oids, ",");
			List<DistributePaperTask> disTaskList = new ArrayList<DistributePaperTask>();
			for (String infoOid : infoOidList) {
				DistributeObject obj = getDistributeObjectByRecDesInfoOid(infoOid);
				List<DistributePaperTask> disTask = getDistributePaperTaskByObjectOid(obj.getOid());
				setDistributePaperTaskDestroyState(disTask, disTaskList, life);
			}
			if (disTaskList.size() != 0 || disTaskList != null) {
				Helper.getPersistService().update(disTaskList);
			}
		}
		if(flagForRecDes){
			updateRecDesPaperTaskLifeCycle(taskOids, life);
		}
		updateDistributeOrderByRecDesInfoOid(oids, life);
	}

	public void updateDestroyNum(Map<String, String> map, String taskOids, String oids, String destroyNums) {
		if(StringUtil.isStringEmpty(oids)){
			return;
		}
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		List<String> oidList = SplitString.string2List((String) oids, ",");
		List<String> numList = SplitString.string2List((String) destroyNums, ",");
		String destroyType = map.get("destroyType");
		boolean flag = false;
		List<DistributeInfo> InfoList = new ArrayList<DistributeInfo>();
		for (int i = 0; i < oidList.size(); i++) {
			String oid = oidList.get(i);
			Persistable obj = Helper.getPersistService().getObject(oid);
			DistributeInfo dis = (DistributeInfo) obj;
			if ("0".equals(destroyType)) {
				dis.setRecoverNum(Integer.parseInt(numList.get(i))
						+ Integer.parseInt(String.valueOf(dis.getRecoverNum())));
				InfoList.add(dis);
			} else if ("1".equals(destroyType)) {
				if (dis.getDisInfoNum() == Integer.parseInt(numList.get(i))
						+ Integer.parseInt(String.valueOf(dis.getDestroyNum()))) {
					life.rejectLifeCycle(dis);
					flag = true;
				}
				dis.setDestroyNum(Integer.parseInt(numList.get(i))
						+ Integer.parseInt(String.valueOf(dis.getDestroyNum())));
				InfoList.add(dis);
			}
		}
		if (InfoList != null && InfoList.size() > 0  ) {
			Helper.getPersistService().update(InfoList);
		}
		if (flag == true) {
			List<String> taskOidList = SplitString.string2List((String) taskOids, ",");
			List<DistributePaperTask> disTaskList = new ArrayList<DistributePaperTask>();
			for (String taskOid : taskOidList) {
				boolean infoUpdateFlag = true;
				Persistable obj = Helper.getPersistService().getObject(taskOid);
				DistributePaperTask disTask = (DistributePaperTask) obj;
				String infoSql = "select f.* from DDM_DIS_INFO f,DDM_DIS_TASKINFOLINK l,DDM_DIS_PAPERTASK t"
						+ " where l.fromObjectId = t.innerId and l.fromObjectClassId = t.classId"
						+ " and l.toObjectId = f.innerId and l.toObjectClassId = f.classId"
						+ " and t.classId || ':' || t.innerId = ?";
				List<DistributeInfo> infoList = PersistHelper.getService()
						.query(infoSql, DistributeInfo.class, taskOid);
				for (DistributeInfo info : infoList) {
					if (!(ConstUtil.LC_DESTROYED.getName()).equals(info.getLifeCycleInfo().getStateName())
							&& ("0").equals(info.getDisMediaType())) {
						infoUpdateFlag = false;
						break;
					}
				}
				if (infoUpdateFlag == true) {
					life.rejectLifeCycle(disTask);
					disTaskList.add(disTask);
				}
			}
			if (disTaskList != null && disTaskList.size() > 0) {
				Helper.getPersistService().update(disTaskList);
			}

		}
	}
	
	public List<DistributeInfo> removeDisInfoConflictWithRecDesInfo(List<DistributeInfo> dis, List<RecDesInfo> recDes){
		if(dis == null || dis.size() == 0){
			return new ArrayList<DistributeInfo>();
		}else if(recDes == null || recDes.size() == 0){
			return dis;
		}
		List<DistributeInfo> disList = new ArrayList<DistributeInfo>();
		disList.addAll(dis);
		
		for(DistributeInfo dInfo:dis){
			for(RecDesInfo rdInfo:recDes){
				if(dInfo.getDistributeObject().getOid().equals(rdInfo.getDistributeObject().getOid())){
					disList.remove(dInfo);
//					break;
				}
			}
		}
		
		return disList;
	}
	
	/**
	 * �����ṩ�Ĳ������ж��Ƿ�����ֽ���������������Ϊ��������ɡ�
	 * 
	 * @param disTask ��Ҫ����Ƿ���Ҫ�����������ڵ�ֽ������
	 * @param disTaskList ���ڴ洢��Щ�Ѿ��ı���������״̬��ֽ������
	 * @param life ��������״̬����
	 * @modify Sun Zongqing
	 */
	private void setDistributePaperTaskDestroyState(List<DistributePaperTask> disTask,List<DistributePaperTask> disTaskList,DistributeLifecycleService life){
		//ֽ�������е����зַ���Ϣ���Ƿ��Ѿ�ȫ��������ɵı�ʶ
		boolean infoUpdateFlag = true;
		for(DistributePaperTask task:disTask){
			String infoSql = "select f.* from "
					+ "DDM_DIS_INFO f,"
					+ "DDM_DIS_TASKINFOLINK l,"
					+ "DDM_DIS_PAPERTASK t "
					+ "where l.fromObjectId = t.innerId "
					+ "and l.fromObjectClassId = t.classId "
					+ "and l.toObjectId = f.innerId "
					+ "and l.toObjectClassId = f.classId "
					+ "and t.classId = ? "
					+ "and t.innerId = ? ";
			List<DistributeInfo> infoList = PersistHelper.getService()
					.query(infoSql, DistributeInfo.class, task.getClassId(),task.getInnerId());
			//ѭ�����ֽ�ʷַ������е����зַ���Ϣ�ǲ��Ƕ��Ѿ�������������״̬
			for (DistributeInfo info : infoList) {
				if (!(ConstUtil.LC_DESTROYED.getName()).equals(info.getLifeCycleInfo().getStateName())
						&& ("0").equals(info.getDisMediaType())) {
					infoUpdateFlag = false;
					break;
				}
			}
			//���ݱ�ʶ�������Ƿ����ֽ���������������״̬�Լ����ݿ�����
			if (infoUpdateFlag == true) {
					life.rejectLifeCycle(task);
					disTaskList.add(task);
			}
		}
	}
	
	/**
	 * �����ṩ�Ļ�������ֽ�����񣬸����������ڲ���
	 * 
	 * @param taskOids ��Ҫ�����������ڵĻ�������ֽ������oid
	 * @param life �ṩ�����������ڲ����ķ���
	 * @author Sun Zongqing
	 */
	private void updateRecDesPaperTaskLifeCycle(String taskOids, DistributeLifecycleService life){
		//������������oid�б�
		List<String> taskOidList = SplitString.string2List(taskOids, ",");
		//���ڴ洢��������ֽ������
		List<RecDesPaperTask> taskList = new ArrayList<RecDesPaperTask>();
		
		for(String oid:taskOidList){
			boolean infoUpdateFlag = true;
			Persistable obj = Helper.getPersistService().getObject(oid);
			RecDesPaperTask task = (RecDesPaperTask)obj;
			String recDesInfoSql = "select f.* from "
					+ "DDM_RECDES_INFO f,"
					+ "DDM_DIS_TASKINFOLINK l,"
					+ "DDM_RECDES_PAPERTASK t "
					+ "where l.fromObjectId = t.innerId "
					+ "and l.fromObjectClassId = t.classId "
					+ "and l.toObjectId = f.innerId "
					+ "and l.toObjectClassId = f.classId "
					+ "and t.classId || ':' || t.innerId = ?";
			List<RecDesInfo> infoList = PersistHelper.getService()
					.query(recDesInfoSql,RecDesInfo.class,oid);
			for(RecDesInfo info:infoList){
				if(!(ConstUtil.LC_COMPLETED.getName()).equals(info.getLifeCycleInfo().getStateName())
						&&("0").equals(info.getDisMediaType())){
					infoUpdateFlag = false;
					break;
				}
			}
			if(infoUpdateFlag == true){
				life.promoteLifeCycle(task);
				taskList.add(task);
			}
		}
		
		if(taskList.size()!=0 || taskList != null){
			Helper.getPersistService().update(taskList);
		}
	}
	
	/**
	 * ���ݸ��������ݵ�oid����ȡ���ݶ�Ӧ��ֽ�ʷַ�����
	 * 
	 * @param oid ��Ҫ����ֽ�������DistributeObject��oid
	 * @return List<distributePaperTask> ���ݸ�����oid��ѯ����ֽ�ʷַ������б�
	 * @author Sun Zongqing
	 */
	private List<DistributePaperTask> getDistributePaperTaskByObjectOid(String oid){
		//�洢�ַ�ֽ��������б�
		List<DistributePaperTask> taskList = new ArrayList<DistributePaperTask>();
		//sql��䣬���ڲ�ѯDistributePaperTask
		String sql = "SELECT P.* FROM "
				+ "DDM_DIS_PAPERTASK P,"
				+ "DDM_DIS_OBJECT O,"
				+ "DDM_DIS_INFO I,"
				+ "DDM_DIS_ORDEROBJLINK L,"
				+ "DDM_DIS_TASKINFOLINK T "
				+ "WHERE O.INNERID = ? "
				+ "AND O.CLASSID = ? "
				+ "AND P.STATENAME = ?"
				+ "AND O.INNERID = L.TOOBJECTID "
				+ "AND O.CLASSID = L.TOOBJECTCLASSID "
				+ "AND L.INNERID = I.DISORDEROBJLINKID "
				+ "AND L.CLASSID = I.DISORDEROBJLINKCLASSID "
				+ "AND I.INNERID = T.TOOBJECTID "
				+ "AND I.CLASSID = T.TOOBJECTCLASSID "
				+ "AND T.FROMOBJECTID = P.INNERID "
				+ "AND T.FROMOBJECTCLASSID = P.CLASSID ";
		
		taskList = PersistHelper.getService().query(sql,
						DistributePaperTask.class,
						Helper.getInnerId(oid),
						Helper.getClassId(oid),
						ConstUtil.LC_DISTRIBUTED.getName());
		return taskList;
	}
	
	/**
	 * ���ݸ����Ĳ��������������������Ϣ��صķַ���Ϣ�Ļ���������Ŀ���������Ƿ���
	 * �ַ���Ϣ��������Ŀ�Ѿ����ڷַ���Ŀ
	 * 
	 * @param oids ����������Ϣ��oids����','�ָ�
	 * @param destroyNums ÿһ������������Ϣ��Ӧ�Ļ���/������Ŀ����','�ָ�
	 * @param destroyType �������ٵ����ͣ�0Ϊ���գ�1Ϊ����
	 * @return ֽ�������������״̬��ȷ���Ƿ���Ҫ���ַ���ֽ���������ó�'�������'״̬
	 * @author Sun Zongqing
	 */
	private boolean updateDisInfobyRecDesInfoOids(String oids, String destroyNums,String destroyType, DistributeLifecycleService life){
		//�Ƿ���ֽ��������Ҫ��������������״̬���жϱ�ʶ
		boolean flag = false;
		//���ٷ���
		List<String> desNum = SplitString.string2List(destroyNums, ",");
		//����������oid�ֽ�,��Ϊ����������Ϣ��oid�б�
		List<String> oidList = SplitString.string2List(oids, ",");
		//���ڴ洢DistributeInfo������
		List<DistributeInfo> infoList = new ArrayList<DistributeInfo>();
		//���ڴ洢��Ҫ���µ�DistributeInfo������
		List<DistributeInfo> updateList = new ArrayList<DistributeInfo>();
		
		for(int i = 0; i<oidList.size();i++){
			//ͨ������������Ϣ��oid��ȡ����������Ϣ��Ӧ������
			DistributeObject obj = getDistributeObjectByRecDesInfoOid(oidList.get(i));
			//��ȡ����������Ϣ��������oid���Ա��ȡ������Ϣ��disInfo��Ϣ
			RecDesInfo recDesInfo = (RecDesInfo) Helper.getPersistService().getObject(oidList.get(i));
			//ͨ��DistributeObject��ȡ���ݶ�Ӧ�ķַ���Ϣ
			infoList = getDistributeInfoByDistributeObject(obj,recDesInfo);
			//�õ��û�������ֽ�������Ӧ�Ļ���/������Ŀ
			long destroy = Integer.parseInt(desNum.get(i));
			//���ķַ���Ϣ�еĻ���/������Ŀ
			for(DistributeInfo info:infoList){
				if("0".equals(destroyType)){
					destroy -= (info.getDisInfoNum()-info.getRecoverNum());
					if(destroy >0){
						info.setRecoverNum(info.getDisInfoNum());
						updateList.add(info);
						continue;
					}else if(destroy == 0){
						info.setRecoverNum(info.getDisInfoNum());
						updateList.add(info);
						break;
					}else if(destroy < 0){
						destroy += (info.getDisInfoNum()-info.getRecoverNum());
						info.setRecoverNum(destroy+info.getRecoverNum());
						updateList.add(info);
						break;
					}
				}else if("1".equals(destroyType)){
					destroy -= (info.getDisInfoNum()-info.getDestroyNum());
					if(destroy > 0){
						info.setDestroyNum(info.getDisInfoNum());
						flag = true;
						life.rejectLifeCycle(info);
						updateList.add(info);
						continue;
					}else if(destroy == 0){
						info.setDestroyNum(info.getDisInfoNum());
						flag = true;
						life.rejectLifeCycle(info);
						updateList.add(info);
						break;
					}else if(destroy < 0){
						destroy += (info.getDisInfoNum()-info.getDestroyNum());
						info.setDestroyNum(info.getDestroyNum()+destroy);
						updateList.add(info);
						break;
					}
				}
			}
			//�������·ַ���Ϣ
			if(updateList.size() != 0 || updateList != null){
				Helper.getPersistService().update(updateList);
			}
		}
		return flag;
	}
	
	/**
	 * �����ṩ�Ĳ��������»���������Ϣ����������
	 * 
	 * @param oids ��Ҫ��������������״̬�Ļ���������Ϣoid
	 * @param destroyType �������ٵ�����
	 * @param life �������ڷ���ӿ�
	 * @return boolean �Ƿ���Ҫ���л�������ֽ����������������ڼ��ı�ʶ
	 * @author Sun Zongqing
	 */
	private boolean updateRecDesInfo(String oids, String destroyType, DistributeLifecycleService life){
		boolean flag = false;
		//����������oid�ֽ�,��Ϊ����������Ϣ��oid�б�
		List<String> oidList = SplitString.string2List((String) oids, ",");
		if("0".equals(destroyType)){
			for(String oid:oidList){
				Persistable obj = Helper.getPersistService().getObject(oid);
				RecDesInfo info = (RecDesInfo)obj;
				life.promoteLifeCycle(info);
			}
			flag = true;
		}else if("1".equals(destroyType)){
			for(String oid:oidList){
				Persistable obj = Helper.getPersistService().getObject(oid);
				RecDesInfo info = (RecDesInfo)obj;
				life.promoteLifeCycle(info);
			}
			flag = true;
		}
		
		return flag;
	}
	
	/**
	 * ͨ������������Ϣ��oid����ȡ���Ӧ������
	 * 
	 * @param oid ��ҪѰ�ҹ����ַ����ݵĻ���������Ϣ
	 * @return DistributeObject ����������Ϣ��Ӧ������
	 * @author Sun Zongqing
	 */
	private DistributeObject getDistributeObjectByRecDesInfoOid(String oid){
		//���ڴ洢DistributeObject������
		List<DistributeObject> objList = new ArrayList<DistributeObject>();
		//sql��䣬������ȡDistributeObject����
		String sqlForOBJ = "SELECT DISTINCT OBJ.* FROM "
						+ "DDM_DIS_OBJECT OBJ,"
						+ "DDM_RECDES_INFO RDINFO,"
						+ "DDM_DIS_ORDEROBJLINK OBJLINK "
						+ "WHERE RDINFO.INNERID =? "
				 		+ "AND RDINFO.CLASSID =? "
						+ "AND RDINFO.DISORDEROBJECTLINKID = OBJLINK.INNERID "
						+ "AND RDINFO.DISORDEROBJECTLINKCLASSID = OBJLINK.CLASSID "
						+ "AND OBJLINK.TOOBJECTID = OBJ.INNERID "
						+ "AND OBJLINK.TOOBJECTCLASSID = OBJ.CLASSID ";
		//ͨ��PersistHelper��ȡ����������Ϣ��Ӧ������
		objList = PersistHelper.getService()
				.query(sqlForOBJ,
						DistributeObject.class,
						Helper.getInnerId(oid),
						Helper.getClassId(oid));
		DistributeObject obj = objList.get(0);
		
		return obj;
	}
	
	/**
	 * �����ṩ�Ļ���������Ϣ��oid�����ö�Ӧ�ķַ�����������
	 * 
	 * @param oids ����������Ϣ��oid
	 * @param life �ṩ�������ڷ���Ĳ���
	 * @author Sun Zongqing
	 */
	private void updateDistributeOrderByRecDesInfoOid(String oids, DistributeLifecycleService life){
		//��ȡ����/���ٵ���sql���
		String sqlForOrder="SELECT DISTINCT O.* FROM "
				+ "DDM_DIS_ORDER O, "
				+ "DDM_DIS_ORDEROBJLINK L, "
				+ "DDM_RECDES_INFO I "
				+ "WHERE O.INNERID = L.FROMOBJECTID "
				+ "AND O.CLASSID = L.FROMOBJECTCLASSID "
				+ "AND L.INNERID = I.DISORDEROBJECTLINKID "
				+ "AND L.CLASSID = I.DISORDEROBJECTLINKCLASSID "
				+ "AND I.INNERID = ? "
				+ "AND I.CLASSID = ? ";
		//��ȡ����/���ٵ��Ļ���������Ϣ
		String sqlForInfo="SELECT DISTINCT I.* FROM "
				+ "DDM_DIS_ORDER O, "
				+ "DDM_DIS_ORDEROBJLINK L, "
				+ "DDM_RECDES_INFO I "
				+ "WHERE O.INNERID = L.FROMOBJECTID "
				+ "AND O.CLASSID = L.FROMOBJECTCLASSID "
				+ "AND L.INNERID = I.DISORDEROBJECTLINKID "
				+ "AND L.CLASSID = I.DISORDEROBJECTLINKCLASSID "
				+ "AND O.INNERID = ? "
				+ "AND O.CLASSID = ? ";
		
		//��ȡ����/���ٵ��ķ�������link
		String sqlForDisLink="SELECT DISTINCT L.* FROM "
				+ "DDM_DIS_ORDER O, "
				+ "DDM_DIS_ORDEROBJLINK L "
				+ "WHERE O.INNERID = L.FROMOBJECTID "
				+ "AND O.CLASSID = L.FROMOBJECTCLASSID "
				+ "AND O.INNERID = ? "
				+ "AND O.CLASSID = ? ";
		
		List<String> oidList = SplitString.string2List(oids, ",");
		List<DistributeOrder> orderList = new ArrayList<DistributeOrder>();
		List<DistributeOrderObjectLink> disOrdObjLink = new ArrayList<DistributeOrderObjectLink>();
		for(String oid:oidList){
			List<DistributeOrder> order = Helper.getPersistService().query(sqlForOrder,
							DistributeOrder.class,
							Helper.getInnerId(oid),
							Helper.getClassId(oid));
			List<RecDesInfo> infoList = Helper.getPersistService().query(sqlForInfo,
					RecDesInfo.class,
					order.get(0).getInnerId(),
					order.get(0).getClassId());
			
			boolean flag = true;
			//ѭ����⣬�Ƿ����/���ٵ���������л���������Ϣ�����ѷַ����
			for(RecDesInfo info:infoList){
				if(!info.getLifeCycleInfo().getStateName().equals(ConstUtil.LC_COMPLETED.getName())){
					flag = false;
					break;
				}
			}
			
			if(flag){
				//���ݷ��ŵ�����ѯ��������Link��Ϣ
				List<DistributeOrderObjectLink> linkList = Helper.getPersistService().query(sqlForDisLink,
						DistributeOrderObjectLink.class,
						order.get(0).getInnerId(),
						order.get(0).getClassId());
				for(DistributeOrderObjectLink link : linkList ){
					life.promoteLifeCycle(link);
					disOrdObjLink.add(link);
				}

				life.promoteLifeCycle(order.get(0));
				orderList.add(order.get(0));
			}
		}
		
		if(orderList.size() != 0 && orderList != null){
			//���·�������Link��������
			Helper.getPersistService().update(disOrdObjLink);
			Helper.getPersistService().update(orderList);
		}
	}
	
	/**
	 * ͨ�������ķַ����ݣ���ȡ��ص�ֽ������ַ���Ϣ
	 * 
	 * @param obj ��Ҫ��ȡ��طַ���Ϣ�ķַ�����
	 * @param info �����ṩ�ַ���λ/��ԱID��NAME�Ļ���������Ϣ
	 * @return List<DistributeInfo> ������ص����зַ���Ϣ
	 * @author Sun Zongqing
	 */
	private List<DistributeInfo> getDistributeInfoByDistributeObject(DistributeObject obj, RecDesInfo info){
		//���ڴ洢DistributeInfo������
		List<DistributeInfo> infoList = new ArrayList<DistributeInfo>();
		//sql��䣬������ȡDistributeInfo����,��Ϊ�ѷַ���ֽ������
		String sqlForInfo = "SELECT I.* FROM "
						+ "DDM_DIS_INFO I, "
						+ "DDM_DIS_OBJECT O, "
						+ "DDM_DIS_ORDEROBJLINK L "
						+ "WHERE O.INNERID = ? "
						+ "AND O.CLASSID = ? "
						+ "AND I.STATENAME = ? "
						+ "AND I.DISINFOID = ? "
						+ "AND I.DISINFONAME = ? "
						+ "AND L.TOOBJECTID = O.INNERID "
						+ "AND L.TOOBJECTCLASSID = O.CLASSID "
						+ "AND I.DISORDEROBJLINKID = L.INNERID "
						+ "AND I.DISORDEROBJLINKCLASSID = L.CLASSID "
						+ "AND I.DISMEDIATYPE = '0' "
						+ "ORDER BY I.SENDTIME ASC";
		infoList = PersistHelper.getService()
				.query(sqlForInfo,
						DistributeInfo.class,
						obj.getInnerId(),
						obj.getClassId(),
						ConstUtil.LC_DISTRIBUTED.getName(),
						info.getDisInfoId(),
						info.getDisInfoName());
		
		return infoList;
	}
	
	public HSSFWorkbook getExcelObject(Map mapObject) {
		// ����������Ϣ
		Map map = creatData(mapObject);

		// Sheetҳ��
		String sheetName = getSheetName(map);
		// ������
		String titleName = getTitleName(map);

		// ģ��·��
		String templateFullName = ConstUtil.EXCEL_TEMPLATE_PATH + ConstUtil.EXCEL_TEMPLATE_DESTROYORDER;
		// ģ�屨��ͷ�п�ʼλ��
		int iHeadRowStart = 1;
		// ģ�屨��ͷ�н���λ��
		int iHeadRowEnd = 4;
		// ģ����ϸ�п�ʼλ��/Copy��ϸ�п�ʼλ��
		int iDetailRowStart = 5;
		// ģ�屨��β�п�ʼλ��
		int iFootRowStart = 6;
		// ģ�屨��β�н���λ��
		int iFootRowEnd = 8;

		// Copy����ͷ�п�ʼλ��
		int iHeadCopyRowStart = 1;
		// ��ϸ�н���λ��
		int iDetailRowEnd;
		// Copy����β�п�ʼλ�� = ��ϸ�н���λ�� +1
		int iFootCopyRowStart;

		try {
			ExcelUtil ew = new ExcelUtil();
			ew.init(sheetName, templateFullName);

			// ����ͷ����
			ew.copyRows(iHeadRowStart - 1, iHeadRowEnd - 1, iHeadCopyRowStart - 1);
			// ����ͷֵ�趨
			setHeadData(ew, titleName, map);
			// ����ͷ��Ԫ��ϲ����и�����
			headerMergeHeight(ew);

			// ��ϸ�п�����ֵ�趨����Ԫ��ϲ����и�����
			iDetailRowEnd = copySetMergeDetail(ew, iDetailRowStart, map);
			iFootCopyRowStart = iDetailRowEnd + 1;

			// ����β�п���
			ew.copyRows(iFootRowStart - 1, iFootRowEnd - 1, iFootCopyRowStart - 1);
			// ����βֵ�趨
			setFootData(ew, iFootCopyRowStart);
			// ����β��Ԫ��ϲ����и�����
			footMergeHeight(ew, iFootCopyRowStart);

			// �����б���
			setColumnWidth(ew);
			// ���ô�ӡ��ʽ
			ew.setPrintStyle(true);

			ew.close();
			ew.deleteTemplateSheet();

			return ew.getCurrentWorkbook();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ȡ�ñ�����
	 * @param map Map 
	 * @return String
	 */
	private String getTitleName(Map map) {
		String destroyType = StringUtil.getString(map.get("destroyType"));
		//1:���ٵ���0:���յ�
		if ("0".equals(destroyType)) {
			return ConstUtil.REC_TITLE_NAME;
		} else {
			return ConstUtil.DES_TITLE_NAME;
		}
	}
	
	/**
	 * ȡ��Sheet��
	 * @param map Map 
	 * @return String
	 */
	private String getSheetName(Map map) {
		String destroyType = StringUtil.getString(map.get("destroyType"));
		//1:���ٵ���0:���յ�
		if ("0".equals(destroyType)) {
			return ConstUtil.REC_TITLE_NAME;
		} else {
			return ConstUtil.DES_TITLE_NAME;
		}
	}

	/**
	 * ����������Ϣ��
	 * 
	 * @param map
	 *            Map
	 * @return Map
	 * @modify Sun Zongqing 
	 * @modifyTime 2014-06-25
	 */
	private Map creatData(Map map) {
		Map<String, Object> mapList = new HashMap<String, Object>();
		String oids = (String) map.get("oids");

		String[] disInfoOid = oids.split(",");
		List<DistributeInfo> infoList = new ArrayList<DistributeInfo>();
		List<RecDesInfo> rdInfoList = new ArrayList<RecDesInfo>();
		List<DistributeObject> objList = new ArrayList<DistributeObject>();
		String destroyNumStr = (String) map.get("destroyNums");
		List<String> destroyNums = SplitString.string2List(destroyNumStr, ",");
		Map<String,String> objNumsLinkMap = new HashMap<String,String>();
		
		//Ϊ�˴�ӡ����������Ϣ���ĸ���
		List<String> disOids = new ArrayList<String>();
		List<String> recDesOids = new ArrayList<String>();
		List<String> disDestroyNums = new ArrayList<String>();
		List<String> recDesDestroyNums = new ArrayList<String>();
		for(int i = 0; i < disInfoOid.length;i++){
			if(Helper.getClassId(disInfoOid[i]).equals(DistributeInfo.CLASSID)){
				disOids.add(disInfoOid[i]);
				disDestroyNums.add(destroyNums.get(i));
			}else if(Helper.getClassId(disInfoOid[i]).equals(RecDesInfo.CLASSID)){
				recDesOids.add(disInfoOid[i]);
				recDesDestroyNums.add(destroyNums.get(i));
			}
		}
		//��ȡ�ַ���Ϣ�ķ���
		for (int i = 0; i < disOids.size(); i++) {
			Persistable obj = Helper.getPersistService().getObject(disOids.get(i));
			DistributeInfo disInfo = (DistributeInfo) obj;
			infoList.add(disInfo);
			String destroyNumber = disDestroyNums.get(i);

			String classId = Helper.getClassId(disOids.get(i));
			String innerId = Helper.getInnerId(disOids.get(i));

			// ��ȡObject��Ϣ
			String sql = "SELECT Obj.* FROM "
					+ "DDM_DIS_OBJECT Obj, "
					+ "DDM_DIS_ORDEROBJLINK ObjLink, "
					+ "DDM_DIS_INFO Info "
					+ "WHERE Info.INNERID = ?  " 
					+ "AND Info.CLASSID = ?  "
					+ "AND Info.DISORDEROBJLINKID = ObjLink.INNERID  "
					+ "AND Info.DISORDEROBJLINKCLASSID = ObjLink.CLASSID  " 
					+ "AND ObjLink.TOOBJECTID = Obj.INNERID  "
					+ "AND ObjLink.TOOBJECTCLASSID = Obj.CLASSID";
			List<DistributeObject> objListTemp = PersistHelper.getService().query(sql, DistributeObject.class, innerId,
					classId);
			objList.add(objListTemp.get(0));
			String objInnerId =  objListTemp.get(0).getInnerId();
			objNumsLinkMap.put(objInnerId, destroyNumber);			
		}
		//����������Ϣ�ķ��� by Sun Zongqing 
		for (int i = 0; i < recDesOids.size(); i++) {
			Persistable obj = Helper.getPersistService().getObject(recDesOids.get(i));
			RecDesInfo disInfo = (RecDesInfo) obj;
			rdInfoList.add(disInfo);
			String destroyNumber = recDesDestroyNums.get(i);

			String classId = Helper.getClassId(recDesOids.get(i));
			String innerId = Helper.getInnerId(recDesOids.get(i));

			// ��ȡObject��Ϣ
			String sql = "SELECT Obj.* FROM "
					+ "DDM_DIS_OBJECT Obj, "
					+ "DDM_DIS_ORDEROBJLINK ObjLink, "
					+ "DDM_RECDES_INFO Info "
					+ "WHERE Info.INNERID = ?  " 
					+ "AND Info.CLASSID = ?  "
					+ "AND Info.DISORDEROBJECTLINKID = ObjLink.INNERID  "
					+ "AND Info.DISORDEROBJECTLINKCLASSID = ObjLink.CLASSID  " 
					+ "AND ObjLink.TOOBJECTID = Obj.INNERID  "
					+ "AND ObjLink.TOOBJECTCLASSID = Obj.CLASSID";
			List<DistributeObject> objListTemp = PersistHelper.getService().query(sql, DistributeObject.class, innerId,
					classId);
			objList.add(objListTemp.get(0));
			String objInnerId =  objListTemp.get(0).getInnerId();
			objNumsLinkMap.put(objInnerId, destroyNumber);	
		}
		
		//�ϲ�����������Ϣ���ַ���Ϣ����
		List<DistributeInfo> infoList2 = mergeDistributeAndRecDesInfo(infoList, rdInfoList);
		
		// ��ȡ������λ
		String flag = StringUtil.getString(map.get("flag"));
		String disInfoId = StringUtil.getString(map.get("disInfo"));
		String oid = StringUtil.getString(map.get("oid"));
		String disInfoName = "";
		if(!StringUtil.isStringEmpty(disInfoId)){
			Principal principal = null;
			if (ConstUtil.C_S_ONE.equals(flag)) {
				principal = OrganizationHelper.getService().getOrganization(disInfoId);
			} else {
				principal = UserHelper.getService().getUser(disInfoId);
			}
			disInfoName = principal.getName();
		}else if(!StringUtil.isStringEmpty(oid)){
			Persistable userOrOrg = Helper.getPersistService().getObject(oid);
			if(ConstUtil.C_S_ONE.equals(flag)){
				disInfoName = ((Organization)userOrOrg).getName();
			} else {
				disInfoName = ((User)userOrOrg).getName();
			}
			
		}
		
		List<DistributeObject> objDestList = new ArrayList<DistributeObject>();
		for(int i=0; i<objList.size(); i++){
		    Persistable dataObj = Helper.getPersistService().getObject(objList.get(i).getDataClassId() + ":" + objList.get(i).getDataInnerId());
		    //�������������������ٵ�excel���
			if (!(dataObj instanceof Part)) {
				objDestList.add(objList.get(i));
			}
		}
		map.put("disInfoName", disInfoName);

		mapList.put("INFO", infoList2);
		mapList.put("OBJ", objDestList);
		mapList.put("NUMS", objNumsLinkMap);
		mapList.putAll(map);

		return mapList;
	}

	/**
	 * �������л���������Ϣ��ת��Ϊ�ַ���Ϣ����������еķַ���Ϣ�б�ϲ�,
	 * Ŀǰ��Ҫ�Ĳ�����DisInfoNum��Note
	 * 
	 * @param dis ��Ҫ�ϲ��ķַ���Ϣ�б�
	 * @param rec ��Ҫ���ϲ��Ļ���������Ϣ�б�
	 * @return �ϲ���ķַ���Ϣ�б�
	 * @author Sun Zongqing
	 * @createTime 2014-06-25
	 */
	private List<DistributeInfo> mergeDistributeAndRecDesInfo(List<DistributeInfo> dis, List<RecDesInfo> rec){
		List<DistributeInfo> disList = new ArrayList<DistributeInfo>();
		disList.addAll(dis);
		for(RecDesInfo rdInfo:rec){
			DistributeInfo disInfo = new DistributeInfo();
			disInfo.setDisInfoNum(rdInfo.getDisInfoNum());
			disInfo.setNote(rdInfo.getNote());
			
			disList.add(disInfo);
		}
		return disList;
	}
	
	/**
	 * ����ͷֵ�趨��
	 * 
	 * @param ew ExcelUtil
	 * @param titleName
	 * @param map Map
	 * @throws Exception
	 */
	private void setHeadData(ExcelUtil ew, String titleName, Map map) throws Exception {
		// ��һ��
		// [����]������(��:1,��:1)
		ew.setCell(1, 1, titleName);

		// ������
		// [������λ]valueֵ(��:3,��:4)
		String disInfoName = (String) map.get("disInfoName");
		ew.setCell(3, 4, disInfoName);
		// TODO
		// [No.]valueֵ(��:3,��:13)
		ew.setCell(3, 13, "");
	}

	/**
	 * ����ͷ��Ԫ��ϲ����и����á�
	 * 
	 * @param ew ExcelUtil
	 * @throws Exception 
	 */
	private void headerMergeHeight(ExcelUtil ew) throws Exception {
		// ���ⵥԪ��ϲ�(�У�1~2���У�1~17)
		ew.mergeCells(1, 2, 1, 17);

		// [������λ]��Ӧvalueֵ��Ԫ��ϲ�(�У�3���У�4~7)
		ew.mergeRows(3, 4, 7);
		// [No.]��Ӧvalueֵ��Ԫ��ϲ�(�У�3���У�13~16)
		ew.mergeRows(3, 13, 16);

		// [���]��Ԫ��ϲ�(�У�4���У�3~4)
		ew.mergeRows(4, 3, 4);
		// [����]��Ԫ��ϲ�(�У�4���У�5~6)
		ew.mergeRows(4, 5, 6);
		// [��ע]��Ԫ��ϲ�(�У�4���У�15~16)
		ew.mergeRows(4, 15, 16);

		// �и�����
		ew.setRowHeight(1, 300);
		ew.setRowHeight(2, 300);
		ew.setRowHeight(3, 300);
		// ��:4,��:14�и�����
		ew.setRowHeight(4, ConstUtil.DESTROY_ORDER_NUMBER, 4);
	}

	/**
	 * ������ϸ��Copy��ֵ�趨�͵�Ԫ��ϲ����и����á�
	 * 
	 * @param ew  ExcelUtil
	 * @param iDetailRowStart int
	 * @param map Map
	 * @return int
	 * @throws Exception
	 */
	private int copySetMergeDetail(ExcelUtil ew, int iDetailRowStart, Map map) throws Exception {
		List<DistributeInfo> detaildisInfoList = (List<DistributeInfo>) map.get("INFO");
		List<DistributeObject> detaildisObjList = (List<DistributeObject>) map.get("OBJ");
		Map<String,String> objNumsLinkMap = (Map<String,String>)map.get("NUMS");
		int iSize = detaildisObjList.size();
		DistributeInfo disInfo;
		DistributeObject disObj;

		// Copy��ϸ��λ��
		int iDtailRowTemp = iDetailRowStart;
		if (iSize == 0) {
			ew.copyRows(iDtailRowTemp - 1, iDtailRowTemp - 1, iDtailRowTemp - 1);

			// [���]��Ԫ��ϲ�(�У�4���У�3~4)
			ew.mergeRows(iDtailRowTemp, 3, 4);
			// [����]��Ԫ��ϲ�(�У�4���У�5~6)
			ew.mergeRows(iDtailRowTemp, 5, 6);
			// [��ע]��Ԫ��ϲ�(�У�4���У�15~16)
			ew.mergeRows(iDtailRowTemp, 15, 16);

			// �и�����
			ew.setRowHeight(iDtailRowTemp, 300);
		} else {
			// Copy�п�ʼλ�ã�ģ���п�ʼλ�ã�ģ���н���λ�ã�Copy�ܴ���
			ew.blockLoop(iDtailRowTemp - 1, iDetailRowStart - 1, iDetailRowStart - 1, iSize);
			for (int i = 0; i < iSize; i++) {
				disObj = detaildisObjList.get(i);

			    Persistable dataObj = Helper.getPersistService().getObject(disObj.getDataClassId() + ":" + disObj.getDataInnerId());
			    //�������������������ٵ�excel���
				if (!(dataObj instanceof Part)) {
					disInfo = detaildisInfoList.get(i);

					// �и�����
					ew.setRowHeight(iDtailRowTemp + i, 300);

					// [���]valueֵ�趨(��:iDtailRowTemp+i,��:2)
					ew.setCell(iDtailRowTemp + i, 2, i + 1);
					// [���]valueֵ�趨(��:iDtailRowTemp+i,��:3)
					ew.setCell(iDtailRowTemp + i, 3, disObj.getNumber());

					// [����]valueֵ�趨(��:iDtailRowTemp+i,��:5)
					ew.setCell(iDtailRowTemp + i, 5, disObj.getName());

					// TODO
					// [�ͺ�]valueֵ�趨(��:iDtailRowTemp+i,��:7)
					ew.setCell(iDtailRowTemp + i, 7, disObj.getCode());
					// [�ܼ�]valueֵ�趨(��:iDtailRowTemp+i,��:8)
					ew.setCell(iDtailRowTemp + i, 8, disObj.getSecurityLevel());
					// [�׶�]valueֵ�趨(��:iDtailRowTemp+i,��:9)
					ew.setCell(iDtailRowTemp + i, 9, disObj.getPhase());
					// [�汾]valueֵ�趨(��:iDtailRowTemp+i,��:10)
					ew.setCell(iDtailRowTemp + i, 10, disObj.getVersion());
					// [ҳ��]valueֵ�趨(��:iDtailRowTemp+i,��:11)
					ew.setCell(iDtailRowTemp + i, 11, disObj.getPages());
					// [����]valueֵ�趨(��:iDtailRowTemp+i,��:12)
					String type=TypeHelper.getService().getType(disObj.getDataClassId()).getName();
					ew.setCell(iDtailRowTemp + i, 12, type);
					// [����]valueֵ�趨(��:iDtailRowTemp+i,��:13)
					ew.setCell(iDtailRowTemp + i, 13, disInfo.getDisInfoNum());
					
					// TODO
					// [����/���ٷ���]ֵ�趨(��:iDtailRowTemp+i,��:14)

					ew.setCell(iDtailRowTemp + i, 14, objNumsLinkMap.get(disObj.getInnerId()));
					// [��ע]valueֵ�趨(��:iDtailRowTemp+i,��:14)
					ew.setCell(iDtailRowTemp + i, 15, disInfo.getNote());

					// [���]��Ԫ��ϲ�(�У�iDtailRowTemp+i���У�3~4)
					ew.mergeRows(iDtailRowTemp + i, 3, 4);
					// [����]��Ԫ��ϲ�(�У�iDtailRowTemp+i���У�5~6)
					ew.mergeRows(iDtailRowTemp + i, 5, 6);
					// [��ע]��Ԫ��ϲ�(�У�iDtailRowTemp+i���У�15~16)
					ew.mergeRows(iDtailRowTemp + i, 15, 16);

					// ���õ�Ԫ���Զ�����
					String maxLength = StringUtil.maxValue(disObj.getNumber(), disObj.getName(), disInfo.getNote(),
							disObj.getCode());
					if(maxLength.getBytes().length > 18){
						ew.setCellHeightStyle(iDtailRowTemp + i, 3, maxLength, 9);
					}else{
					if(type.getBytes().length > 4){
						ew.setCellHeightStyle(iDtailRowTemp+i, 12, type, 2);
					}
					}
				}

			}
			// Copy��ϸ����λ��iDtailRowTemp+iSize-1
			iDtailRowTemp += iSize - 1;
		}

		// Copy��ϸ����λ��
		return iDtailRowTemp;
	}

	/**
	 * ����βֵ�趨��
	 * 
	 * @param ew
	 * @param rowIndex
	 * @param mapParam Map
	 * @throws Exception
	 */
	private void setFootData(ExcelUtil ew, int rowIndex) throws Exception {
		// ��rowIndex��
		User currentUser = SessionHelper.getService().getUser();
		// [���뵥λ������(����)]��Ӧvalueֵ�趨(�У�Copy����β�п�ʼλ��+1���У�2~4)
		String orgName=currentUser.getOrganizationName();
		if(orgName==null){
			ew.setCell(rowIndex + 1, 2," " + currentUser.getName() + " "
							+ DateTimeUtil.getCurrentDate());
		}else{
			ew.setCell(rowIndex + 1, 2,
				currentUser.getOrganizationName() + " " + currentUser.getName() + " "
						+ DateTimeUtil.getCurrentDate());
		}
		// TODO
		// [������������(����)]��Ӧvalueֵ�趨(�У�Copy����β�п�ʼλ��+1���У�5~7)
		ew.setCell(rowIndex + 1, 5, "");
		// [���ܲ�������(����)]��Ӧvalueֵ�趨(�У�Copy����β�п�ʼλ��+1���У�8~11)
		ew.setCell(rowIndex + 1, 7, "");

		// [������]��Ӧvalueֵ�趨(�У�Copy����β�п�ʼλ�ã��У�15~16)
		ew.setCell(rowIndex, 15, "");
		// [�����]��Ӧvalueֵ�趨(�У�Copy����β�п�ʼλ��+1���У�15~16)
		ew.setCell(rowIndex + 1, 15, "");
		// [�������]��Ӧvalueֵ�趨(�У�Copy����β�п�ʼλ��+2���У�15~16)
		ew.setCell(rowIndex + 2, 15, "");
	}

	/**
	 * ����β��Ԫ��ϲ����и����á�
	 * 
	 * @param ew ExcelUtil
	 * @param rowIndex int
	 *           
	 */
	private void footMergeHeight(ExcelUtil ew, int rowIndex) {
		// [���뵥λ������(����)]��Ԫ��ϲ�(�У�Copy����β�п�ʼλ�ã��У�2~4)
		ew.mergeRows(rowIndex, 2, 4);
		// ��Ӧvalueֵ��Ԫ��ϲ�(�У�Copy����β�п�ʼλ��+1~Copy����β�п�ʼλ��+2���У�2~4)
		ew.mergeCells(rowIndex + 1, rowIndex + 2, 2, 4);
		// [������������(����)]��Ԫ��ϲ�(�У�Copy����β�п�ʼλ�ã��У�5~7)
		ew.mergeRows(rowIndex, 5, 7);
		// ��Ӧvalueֵ��Ԫ��ϲ�(�У�Copy����β�п�ʼλ��+1~Copy����β�п�ʼλ��+2���У�5~7)
		ew.mergeCells(rowIndex + 1, rowIndex + 2, 5, 7);
		// [���ܲ�������(����)]��Ԫ��ϲ�(�У�Copy����β�п�ʼλ�ã��У�8~13)
		ew.mergeRows(rowIndex, 8, 13);
		// ��Ӧvalueֵ��Ԫ��ϲ�(�У�Copy����β�п�ʼλ��+1~Copy����β�п�ʼλ��+2���У�8~13)
		ew.mergeCells(rowIndex + 1, rowIndex + 2, 8, 13);

		// [������]��Ӧvalueֵ��Ԫ��ϲ�(�У�Copy����β�п�ʼλ�ã��У�15~16)
		ew.mergeRows(rowIndex, 15, 16);
		// [�����]��Ӧvalueֵ��Ԫ��ϲ�(�У�Copy����β�п�ʼλ��+1���У�15~16)
		ew.mergeRows(rowIndex + 1, 15, 16);
		// [�������]��Ӧvalueֵ��Ԫ��ϲ�(�У�Copy����β�п�ʼλ��+2���У�15~16)
		ew.mergeRows(rowIndex + 2, 15, 16);

		// �и�����
		ew.setRowHeight(rowIndex, 300);
		ew.setRowHeight(rowIndex + 1, 300);
		ew.setRowHeight(rowIndex + 2, 300);
	}

	/**
	 * �����п�
	 * 
	 * @param ew  ExcelUtil
	 */
	private void setColumnWidth(ExcelUtil ew) {
		// ��:1,���Ϊ300
		ew.setColumnWidth(1, 300);
		// ��:2,���Ϊ1500
		ew.setColumnWidth(2, 1000);
		// ��:3~4,���Ϊ2100
		ew.setColumnWidth(3, 2500);
		ew.setColumnWidth(4, 2500);
		// ��:5~6,���Ϊ2100
		ew.setColumnWidth(5, 2100);
		ew.setColumnWidth(6, 2100);
		// ��:7,���Ϊ3000
		ew.setColumnWidth(7, 3000);
		// ��:8~13���Ϊ1500
		ew.setColumnWidth(8, 1500);
		ew.setColumnWidth(9, 1500);
		ew.setColumnWidth(10, 1500);
		ew.setColumnWidth(11, 1500);
		ew.setColumnWidth(12, 1500);
		ew.setColumnWidth(13, 1500);
		// ��:14~16,���Ϊ2500
		ew.setColumnWidth(14, 2500);
		ew.setColumnWidth(15, 2500);
		ew.setColumnWidth(16, 2500);
		// ��:17,���Ϊ300
		ew.setColumnWidth(17, 300);
	}
}