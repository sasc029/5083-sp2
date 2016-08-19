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
 * 回收单和销毁单实现类。
 * 
 * @author yanjia 2013-3-21
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DistributeDestroyOrderServiceImpl implements DistributeDestroyOrderService {
	String parms = "";

	public List<RecDesPaperTask> getAllRecDesPaperTask(String lc){
		//获得所有回收销毁纸质任务的sql语句
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
			//在进行回收任务搜索的时候，如果回收数目等于分发分数，不再显示该信息
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
				//判断回收数目是否已经等于分发份数
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
		//sql语句，用于查询是否当前分发任务包含的数据，出现在了回收销毁单上
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
				//如果二者中存在相同的数据，则从展示列表中剔除
				if(obj1.get(0).getInnerId().equals(obj2.get(0).getInnerId())){
					disList.remove(dTask);
					break;
				}
			}
		}
		
		return disList;
	}
	
	/**
	 * 根据提供的map参数，为回收销毁单的查询，设计一个sql查询语句
	 * 
	 * @param map 需要搜索的回收销毁纸质任务的信息
	 * @return 生成的回收销毁纸质任务的sql查询语句
	 * 
	 * @author Sun Zongqing 2014-06-16
	 */
	private String getSqlForRecDes(Map<String, String> map) {
		StringBuffer sbSql = new StringBuffer();
		//此处将以前的分发信息改为回收销毁信息，将纸质任务(0)改成回收销毁任务(3)
		sbSql.append("select DISTINCT {0} from DDM_RECDES_PAPERTASK t,DDM_RECDES_INFO f,DDM_DIS_TASKINFOLINK l,ddm_dis_orderobjlink k, ddm_dis_object b");
		sbSql.append(" where t.innerId = l.fromObjectId and t.classId = l.fromObjectClassId and  b.innerid=k.TOOBJECTID and  k.innerid=f.DISORDEROBJECTLINKID ");
		sbSql.append(" and l.toObjectId = f.innerId and l.toObjectClassId = f.classId");
		sbSql.append(" and f.DISMEDIATYPE = '0'");
		sbSql.append(getSubSql(map));
		return sbSql.toString();
	}
	
	/**
	 * 根据提供的map参数，为已完成状态的纸质任务，设计一个sql查询语句
	 * 
	 * @param map 提供查询信息的map参数
	 * @return String 生成的sql语句
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
	 * 根据提供的map参数，创建sql查询语句的共同部分
	 * 
	 * @param map 提供查询参数的查询语句
	 * @return String 生成的子查询语句
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
	

		//搜索范围条件的处理
    	Map<String,Object> mapTaskCode=JsonUtil.toMap(map.get("taskCode"));
    	//搜索范围所包含的搜索对象
    	String strContainerScope=mapTaskCode.get("containerScope").toString();
    	String strFavoriteScope=mapTaskCode.get("favoriteScope").toString();
    	//所有上下文 不需要追加任何条件
    	//我收藏的上下文
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
		//更新与回收销毁信息相关的分发信息的生命周期
		flagForDis = updateDisInfobyRecDesInfoOids(oids, destroyNums, destroyType, life);
		//更新回收销毁信息的生命周期
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
	 * 根据提供的参数，判断是否设置纸质任务的生命周期为‘销毁完成’
	 * 
	 * @param disTask 需要检测是否需要更改生命周期的纸质任务
	 * @param disTaskList 用于存储那些已经改变生命周期状态的纸质任务
	 * @param life 生命周期状态服务
	 * @modify Sun Zongqing
	 */
	private void setDistributePaperTaskDestroyState(List<DistributePaperTask> disTask,List<DistributePaperTask> disTaskList,DistributeLifecycleService life){
		//纸质任务中的所有分发信息，是否已经全部销毁完成的标识
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
			//循环检测纸质分发任务中的所有分发信息是不是都已经变成了销毁完成状态
			for (DistributeInfo info : infoList) {
				if (!(ConstUtil.LC_DESTROYED.getName()).equals(info.getLifeCycleInfo().getStateName())
						&& ("0").equals(info.getDisMediaType())) {
					infoUpdateFlag = false;
					break;
				}
			}
			//根据标识，决定是否更新纸质任务的生命周期状态以及数据库资料
			if (infoUpdateFlag == true) {
					life.rejectLifeCycle(task);
					disTaskList.add(task);
			}
		}
	}
	
	/**
	 * 根据提供的回收销毁纸质任务，更新生命周期操作
	 * 
	 * @param taskOids 需要更新生命周期的回收销毁纸质任务oid
	 * @param life 提供更新生命周期操作的服务
	 * @author Sun Zongqing
	 */
	private void updateRecDesPaperTaskLifeCycle(String taskOids, DistributeLifecycleService life){
		//回收销毁任务oid列表
		List<String> taskOidList = SplitString.string2List(taskOids, ",");
		//用于存储回收销毁纸质任务
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
	 * 根据给定的数据的oid，获取数据对应的纸质分发任务
	 * 
	 * @param oid 需要查找纸质任务的DistributeObject的oid
	 * @return List<distributePaperTask> 根据给定的oid查询到的纸质分发任务列表
	 * @author Sun Zongqing
	 */
	private List<DistributePaperTask> getDistributePaperTaskByObjectOid(String oid){
		//存储分发纸质任务的列表
		List<DistributePaperTask> taskList = new ArrayList<DistributePaperTask>();
		//sql语句，用于查询DistributePaperTask
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
	 * 根据给定的参数，更新与回收销毁信息相关的分发信息的回收销毁数目，并返回是否有
	 * 分发信息的销毁数目已经等于分发数目
	 * 
	 * @param oids 回收销毁信息的oids，以','分隔
	 * @param destroyNums 每一条回收销毁信息对应的回收/销毁数目，以','分隔
	 * @param destroyType 回收销毁的类型，0为回收，1为销毁
	 * @return 纸质任务销毁完成状态，确定是否需要将分发的纸质任务，设置成'销毁完成'状态
	 * @author Sun Zongqing
	 */
	private boolean updateDisInfobyRecDesInfoOids(String oids, String destroyNums,String destroyType, DistributeLifecycleService life){
		//是否有纸质任务需要做更改生命周期状态的判断标识
		boolean flag = false;
		//销毁分数
		List<String> desNum = SplitString.string2List(destroyNums, ",");
		//将传进来的oid分解,此为回收销毁信息的oid列表
		List<String> oidList = SplitString.string2List(oids, ",");
		//用于存储DistributeInfo的例子
		List<DistributeInfo> infoList = new ArrayList<DistributeInfo>();
		//用于存储需要更新的DistributeInfo的例子
		List<DistributeInfo> updateList = new ArrayList<DistributeInfo>();
		
		for(int i = 0; i<oidList.size();i++){
			//通过回收销毁信息的oid获取回收销毁信息对应的数据
			DistributeObject obj = getDistributeObjectByRecDesInfoOid(oidList.get(i));
			//获取回收销毁信息，根据其oid，以便获取发放信息的disInfo信息
			RecDesInfo recDesInfo = (RecDesInfo) Helper.getPersistService().getObject(oidList.get(i));
			//通过DistributeObject获取数据对应的分发信息
			infoList = getDistributeInfoByDistributeObject(obj,recDesInfo);
			//得到该回收销毁纸质任务对应的回收/销毁数目
			long destroy = Integer.parseInt(desNum.get(i));
			//更改分发信息中的回收/销毁数目
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
			//批量更新分发信息
			if(updateList.size() != 0 || updateList != null){
				Helper.getPersistService().update(updateList);
			}
		}
		return flag;
	}
	
	/**
	 * 根据提供的参数，更新回收销毁信息的生命周期
	 * 
	 * @param oids 需要检测更新生命周期状态的回收销毁信息oid
	 * @param destroyType 回收销毁的类型
	 * @param life 生命周期服务接口
	 * @return boolean 是否需要进行回收销毁纸质任务更改生命周期检测的标识
	 * @author Sun Zongqing
	 */
	private boolean updateRecDesInfo(String oids, String destroyType, DistributeLifecycleService life){
		boolean flag = false;
		//将传进来的oid分解,此为回收销毁信息的oid列表
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
	 * 通过回收销毁信息的oid，获取其对应的数据
	 * 
	 * @param oid 需要寻找关联分发数据的回收销毁信息
	 * @return DistributeObject 回收销毁信息对应的数据
	 * @author Sun Zongqing
	 */
	private DistributeObject getDistributeObjectByRecDesInfoOid(String oid){
		//用于存储DistributeObject的例子
		List<DistributeObject> objList = new ArrayList<DistributeObject>();
		//sql语句，用来获取DistributeObject对象
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
		//通过PersistHelper获取回收销毁信息对应的数据
		objList = PersistHelper.getService()
				.query(sqlForOBJ,
						DistributeObject.class,
						Helper.getInnerId(oid),
						Helper.getClassId(oid));
		DistributeObject obj = objList.get(0);
		
		return obj;
	}
	
	/**
	 * 根据提供的回收销毁信息的oid，设置对应的分发单生命周期
	 * 
	 * @param oids 回收销毁信息的oid
	 * @param life 提供生命周期服务的参数
	 * @author Sun Zongqing
	 */
	private void updateDistributeOrderByRecDesInfoOid(String oids, DistributeLifecycleService life){
		//获取回收/销毁单的sql语句
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
		//获取回收/销毁单的回收销毁信息
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
		
		//获取回收/销毁单的发放数据link
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
			//循环检测，是否回收/销毁单下面的所有回收销毁信息，均已分发完成
			for(RecDesInfo info:infoList){
				if(!info.getLifeCycleInfo().getStateName().equals(ConstUtil.LC_COMPLETED.getName())){
					flag = false;
					break;
				}
			}
			
			if(flag){
				//根据发放单，查询发放数据Link信息
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
			//更新发放数据Link生命周期
			Helper.getPersistService().update(disOrdObjLink);
			Helper.getPersistService().update(orderList);
		}
	}
	
	/**
	 * 通过给定的分发数据，获取相关的纸质任务分发信息
	 * 
	 * @param obj 需要获取相关分发信息的分发数据
	 * @param info 用来提供分发单位/人员ID和NAME的回收销毁信息
	 * @return List<DistributeInfo> 数据相关的所有分发信息
	 * @author Sun Zongqing
	 */
	private List<DistributeInfo> getDistributeInfoByDistributeObject(DistributeObject obj, RecDesInfo info){
		//用于存储DistributeInfo的例子
		List<DistributeInfo> infoList = new ArrayList<DistributeInfo>();
		//sql语句，用来获取DistributeInfo对象,需为已分发的纸质任务
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
		// 创建数据信息
		Map map = creatData(mapObject);

		// Sheet页名
		String sheetName = getSheetName(map);
		// 标题名
		String titleName = getTitleName(map);

		// 模板路径
		String templateFullName = ConstUtil.EXCEL_TEMPLATE_PATH + ConstUtil.EXCEL_TEMPLATE_DESTROYORDER;
		// 模板报表头行开始位置
		int iHeadRowStart = 1;
		// 模板报表头行结束位置
		int iHeadRowEnd = 4;
		// 模板明细行开始位置/Copy明细行开始位置
		int iDetailRowStart = 5;
		// 模板报表尾行开始位置
		int iFootRowStart = 6;
		// 模板报表尾行结束位置
		int iFootRowEnd = 8;

		// Copy报表头行开始位置
		int iHeadCopyRowStart = 1;
		// 明细行结束位置
		int iDetailRowEnd;
		// Copy报表尾行开始位置 = 明细行结束位置 +1
		int iFootCopyRowStart;

		try {
			ExcelUtil ew = new ExcelUtil();
			ew.init(sheetName, templateFullName);

			// 报表头拷贝
			ew.copyRows(iHeadRowStart - 1, iHeadRowEnd - 1, iHeadCopyRowStart - 1);
			// 报表头值设定
			setHeadData(ew, titleName, map);
			// 报表头单元格合并和行高设置
			headerMergeHeight(ew);

			// 明细行拷贝，值设定，单元格合并和行高设置
			iDetailRowEnd = copySetMergeDetail(ew, iDetailRowStart, map);
			iFootCopyRowStart = iDetailRowEnd + 1;

			// 报表尾行拷贝
			ew.copyRows(iFootRowStart - 1, iFootRowEnd - 1, iFootCopyRowStart - 1);
			// 报表尾值设定
			setFootData(ew, iFootCopyRowStart);
			// 报表尾单元格合并和行高设置
			footMergeHeight(ew, iFootCopyRowStart);

			// 设置列表宽度
			setColumnWidth(ew);
			// 设置打印版式
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
	 * 取得标题名
	 * @param map Map 
	 * @return String
	 */
	private String getTitleName(Map map) {
		String destroyType = StringUtil.getString(map.get("destroyType"));
		//1:销毁单；0:回收单
		if ("0".equals(destroyType)) {
			return ConstUtil.REC_TITLE_NAME;
		} else {
			return ConstUtil.DES_TITLE_NAME;
		}
	}
	
	/**
	 * 取得Sheet名
	 * @param map Map 
	 * @return String
	 */
	private String getSheetName(Map map) {
		String destroyType = StringUtil.getString(map.get("destroyType"));
		//1:销毁单；0:回收单
		if ("0".equals(destroyType)) {
			return ConstUtil.REC_TITLE_NAME;
		} else {
			return ConstUtil.DES_TITLE_NAME;
		}
	}

	/**
	 * 创建数据信息。
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
		
		//为了打印回收销毁信息做的更改
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
		//获取分发信息的方法
		for (int i = 0; i < disOids.size(); i++) {
			Persistable obj = Helper.getPersistService().getObject(disOids.get(i));
			DistributeInfo disInfo = (DistributeInfo) obj;
			infoList.add(disInfo);
			String destroyNumber = disDestroyNums.get(i);

			String classId = Helper.getClassId(disOids.get(i));
			String innerId = Helper.getInnerId(disOids.get(i));

			// 获取Object信息
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
		//回收销毁信息的方法 by Sun Zongqing 
		for (int i = 0; i < recDesOids.size(); i++) {
			Persistable obj = Helper.getPersistService().getObject(recDesOids.get(i));
			RecDesInfo disInfo = (RecDesInfo) obj;
			rdInfoList.add(disInfo);
			String destroyNumber = recDesDestroyNums.get(i);

			String classId = Helper.getClassId(recDesOids.get(i));
			String innerId = Helper.getInnerId(recDesOids.get(i));

			// 获取Object信息
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
		
		//合并回收销毁信息到分发信息里面
		List<DistributeInfo> infoList2 = mergeDistributeAndRecDesInfo(infoList, rdInfoList);
		
		// 获取发往单位
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
		    //部件不导出到回收销毁单excel里边
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
	 * 将参数中回收销毁信息中转化为分发信息，并与参数中的分发信息列表合并,
	 * 目前需要的参数有DisInfoNum和Note
	 * 
	 * @param dis 需要合并的分发信息列表
	 * @param rec 需要被合并的回收销毁信息列表
	 * @return 合并后的分发信息列表
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
	 * 报表头值设定。
	 * 
	 * @param ew ExcelUtil
	 * @param titleName
	 * @param map Map
	 * @throws Exception
	 */
	private void setHeadData(ExcelUtil ew, String titleName, Map map) throws Exception {
		// 第一行
		// [标题]行内容(行:1,列:1)
		ew.setCell(1, 1, titleName);

		// 第三行
		// [发往单位]value值(行:3,列:4)
		String disInfoName = (String) map.get("disInfoName");
		ew.setCell(3, 4, disInfoName);
		// TODO
		// [No.]value值(行:3,列:13)
		ew.setCell(3, 13, "");
	}

	/**
	 * 报表头单元格合并和行高设置。
	 * 
	 * @param ew ExcelUtil
	 * @throws Exception 
	 */
	private void headerMergeHeight(ExcelUtil ew) throws Exception {
		// 标题单元格合并(行：1~2，列：1~17)
		ew.mergeCells(1, 2, 1, 17);

		// [发往单位]相应value值单元格合并(行：3，列：4~7)
		ew.mergeRows(3, 4, 7);
		// [No.]相应value值单元格合并(行：3，列：13~16)
		ew.mergeRows(3, 13, 16);

		// [编号]单元格合并(行：4，列：3~4)
		ew.mergeRows(4, 3, 4);
		// [名称]单元格合并(行：4，列：5~6)
		ew.mergeRows(4, 5, 6);
		// [备注]单元格合并(行：4，列：15~16)
		ew.mergeRows(4, 15, 16);

		// 行高设置
		ew.setRowHeight(1, 300);
		ew.setRowHeight(2, 300);
		ew.setRowHeight(3, 300);
		// 行:4,列:14行高设置
		ew.setRowHeight(4, ConstUtil.DESTROY_ORDER_NUMBER, 4);
	}

	/**
	 * 报表明细行Copy，值设定和单元格合并和行高设置。
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

		// Copy明细行位置
		int iDtailRowTemp = iDetailRowStart;
		if (iSize == 0) {
			ew.copyRows(iDtailRowTemp - 1, iDtailRowTemp - 1, iDtailRowTemp - 1);

			// [编号]单元格合并(行：4，列：3~4)
			ew.mergeRows(iDtailRowTemp, 3, 4);
			// [名称]单元格合并(行：4，列：5~6)
			ew.mergeRows(iDtailRowTemp, 5, 6);
			// [备注]单元格合并(行：4，列：15~16)
			ew.mergeRows(iDtailRowTemp, 15, 16);

			// 行高设置
			ew.setRowHeight(iDtailRowTemp, 300);
		} else {
			// Copy行开始位置，模板行开始位置，模板行结束位置，Copy总次数
			ew.blockLoop(iDtailRowTemp - 1, iDetailRowStart - 1, iDetailRowStart - 1, iSize);
			for (int i = 0; i < iSize; i++) {
				disObj = detaildisObjList.get(i);

			    Persistable dataObj = Helper.getPersistService().getObject(disObj.getDataClassId() + ":" + disObj.getDataInnerId());
			    //部件不导出到回收销毁单excel里边
				if (!(dataObj instanceof Part)) {
					disInfo = detaildisInfoList.get(i);

					// 行高设置
					ew.setRowHeight(iDtailRowTemp + i, 300);

					// [序号]value值设定(行:iDtailRowTemp+i,列:2)
					ew.setCell(iDtailRowTemp + i, 2, i + 1);
					// [编号]value值设定(行:iDtailRowTemp+i,列:3)
					ew.setCell(iDtailRowTemp + i, 3, disObj.getNumber());

					// [名称]value值设定(行:iDtailRowTemp+i,列:5)
					ew.setCell(iDtailRowTemp + i, 5, disObj.getName());

					// TODO
					// [型号]value值设定(行:iDtailRowTemp+i,列:7)
					ew.setCell(iDtailRowTemp + i, 7, disObj.getCode());
					// [密集]value值设定(行:iDtailRowTemp+i,列:8)
					ew.setCell(iDtailRowTemp + i, 8, disObj.getSecurityLevel());
					// [阶段]value值设定(行:iDtailRowTemp+i,列:9)
					ew.setCell(iDtailRowTemp + i, 9, disObj.getPhase());
					// [版本]value值设定(行:iDtailRowTemp+i,列:10)
					ew.setCell(iDtailRowTemp + i, 10, disObj.getVersion());
					// [页数]value值设定(行:iDtailRowTemp+i,列:11)
					ew.setCell(iDtailRowTemp + i, 11, disObj.getPages());
					// [类型]value值设定(行:iDtailRowTemp+i,列:12)
					String type=TypeHelper.getService().getType(disObj.getDataClassId()).getName();
					ew.setCell(iDtailRowTemp + i, 12, type);
					// [份数]value值设定(行:iDtailRowTemp+i,列:13)
					ew.setCell(iDtailRowTemp + i, 13, disInfo.getDisInfoNum());
					
					// TODO
					// [回收/销毁份数]值设定(行:iDtailRowTemp+i,列:14)

					ew.setCell(iDtailRowTemp + i, 14, objNumsLinkMap.get(disObj.getInnerId()));
					// [备注]value值设定(行:iDtailRowTemp+i,列:14)
					ew.setCell(iDtailRowTemp + i, 15, disInfo.getNote());

					// [编号]单元格合并(行：iDtailRowTemp+i，列：3~4)
					ew.mergeRows(iDtailRowTemp + i, 3, 4);
					// [名称]单元格合并(行：iDtailRowTemp+i，列：5~6)
					ew.mergeRows(iDtailRowTemp + i, 5, 6);
					// [备注]单元格合并(行：iDtailRowTemp+i，列：15~16)
					ew.mergeRows(iDtailRowTemp + i, 15, 16);

					// 设置单元格自动拉伸
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
			// Copy明细结束位置iDtailRowTemp+iSize-1
			iDtailRowTemp += iSize - 1;
		}

		// Copy明细结束位置
		return iDtailRowTemp;
	}

	/**
	 * 报表尾值设定。
	 * 
	 * @param ew
	 * @param rowIndex
	 * @param mapParam Map
	 * @throws Exception
	 */
	private void setFootData(ExcelUtil ew, int rowIndex) throws Exception {
		// 第rowIndex行
		User currentUser = SessionHelper.getService().getUser();
		// [申请单位申请人(日期)]相应value值设定(行：Copy报表尾行开始位置+1，列：2~4)
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
		// [档案部门审批(日期)]相应value值设定(行：Copy报表尾行开始位置+1，列：5~7)
		ew.setCell(rowIndex + 1, 5, "");
		// [主管部门审批(日期)]相应value值设定(行：Copy报表尾行开始位置+1，列：8~11)
		ew.setCell(rowIndex + 1, 7, "");

		// [销毁人]相应value值设定(行：Copy报表尾行开始位置，列：15~16)
		ew.setCell(rowIndex, 15, "");
		// [监毁人]相应value值设定(行：Copy报表尾行开始位置+1，列：15~16)
		ew.setCell(rowIndex + 1, 15, "");
		// [监毁日期]相应value值设定(行：Copy报表尾行开始位置+2，列：15~16)
		ew.setCell(rowIndex + 2, 15, "");
	}

	/**
	 * 报表尾单元格合并和行高设置。
	 * 
	 * @param ew ExcelUtil
	 * @param rowIndex int
	 *           
	 */
	private void footMergeHeight(ExcelUtil ew, int rowIndex) {
		// [申请单位申请人(日期)]单元格合并(行：Copy报表尾行开始位置，列：2~4)
		ew.mergeRows(rowIndex, 2, 4);
		// 相应value值单元格合并(行：Copy报表尾行开始位置+1~Copy报表尾行开始位置+2，列：2~4)
		ew.mergeCells(rowIndex + 1, rowIndex + 2, 2, 4);
		// [档案部门审批(日期)]单元格合并(行：Copy报表尾行开始位置，列：5~7)
		ew.mergeRows(rowIndex, 5, 7);
		// 相应value值单元格合并(行：Copy报表尾行开始位置+1~Copy报表尾行开始位置+2，列：5~7)
		ew.mergeCells(rowIndex + 1, rowIndex + 2, 5, 7);
		// [主管部门审批(日期)]单元格合并(行：Copy报表尾行开始位置，列：8~13)
		ew.mergeRows(rowIndex, 8, 13);
		// 相应value值单元格合并(行：Copy报表尾行开始位置+1~Copy报表尾行开始位置+2，列：8~13)
		ew.mergeCells(rowIndex + 1, rowIndex + 2, 8, 13);

		// [销毁人]相应value值单元格合并(行：Copy报表尾行开始位置，列：15~16)
		ew.mergeRows(rowIndex, 15, 16);
		// [监毁人]相应value值单元格合并(行：Copy报表尾行开始位置+1，列：15~16)
		ew.mergeRows(rowIndex + 1, 15, 16);
		// [监毁日期]相应value值单元格合并(行：Copy报表尾行开始位置+2，列：15~16)
		ew.mergeRows(rowIndex + 2, 15, 16);

		// 行高设置
		ew.setRowHeight(rowIndex, 300);
		ew.setRowHeight(rowIndex + 1, 300);
		ew.setRowHeight(rowIndex + 2, 300);
	}

	/**
	 * 设置列宽。
	 * 
	 * @param ew  ExcelUtil
	 */
	private void setColumnWidth(ExcelUtil ew) {
		// 列:1,宽度为300
		ew.setColumnWidth(1, 300);
		// 列:2,宽度为1500
		ew.setColumnWidth(2, 1000);
		// 列:3~4,宽度为2100
		ew.setColumnWidth(3, 2500);
		ew.setColumnWidth(4, 2500);
		// 列:5~6,宽度为2100
		ew.setColumnWidth(5, 2100);
		ew.setColumnWidth(6, 2100);
		// 列:7,宽度为3000
		ew.setColumnWidth(7, 3000);
		// 列:8~13宽度为1500
		ew.setColumnWidth(8, 1500);
		ew.setColumnWidth(9, 1500);
		ew.setColumnWidth(10, 1500);
		ew.setColumnWidth(11, 1500);
		ew.setColumnWidth(12, 1500);
		ew.setColumnWidth(13, 1500);
		// 列:14~16,宽度为2500
		ew.setColumnWidth(14, 2500);
		ew.setColumnWidth(15, 2500);
		ew.setColumnWidth(16, 2500);
		// 列:17,宽度为300
		ew.setColumnWidth(17, 300);
	}
}