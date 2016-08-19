package com.bjsasc.ddm.distribute.service.distributesendorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.ddm.common.ConstLifeCycle;
import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.model.recdespapertask.RecDesPaperTask;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.code.action.CodeApplyAction;
import com.bjsasc.plm.core.code.CodemanHelper;
import com.bjsasc.plm.core.code.instance.CodeRuleBindInfo;
import com.bjsasc.plm.core.part.Part;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.suit.ATSuit;
import com.bjsasc.plm.core.suit.ATSuitMemberLink;
import com.bjsasc.plm.core.suit.Suited;
import com.bjsasc.plm.core.system.principal.Principal;
import com.bjsasc.plm.core.system.principal.OrganizationHelper;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.system.principal.UserHelper;
import com.bjsasc.plm.core.type.TypeHelper;
import com.bjsasc.plm.core.util.DateTimeUtil;
import com.bjsasc.plm.core.util.ExcelUtil;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.core.util.JsonUtil;
import com.cascc.avidm.util.SplitString;
import com.cascc.platform.aa.AAProvider;
import com.cascc.platform.cache.CacheUtil;

/**
 * �ڷ������ⷢ��ʵ���ࡣ
 * 
 * @author yanjia 2013-3-21
 */
@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
public class DistributeSendOrderServiceImpl implements DistributeSendOrderService {
	String parms = "";

	@Override
	public List<DistributePaperTask> getAllDistributePaperTask(Map<String, String> map) {
		List<DistributePaperTask> taskList = new ArrayList<DistributePaperTask>();

		String orderBy = " order by T.MODIFYTIME, f.MODIFYTIME desc";
		String mainSql = getSql(map);
		
		String sql1 = mainSql.replace("{0}", "t.*,f.*");
		sql1 = sql1.replace("{1}", orderBy);
		List<DistributePaperTask> paperList;
		if (parms != "") {
			String[] parm = parms.split(",");
			paperList = Helper.getPersistService().query(sql1, DistributePaperTask.class, parm);
		} else {
			paperList = Helper.getPersistService().query(sql1, DistributePaperTask.class);
		}

		String sql2 = mainSql.replace("{0}", "f.*,t.*");
		sql2 = sql2.replace("{1}", orderBy);
		List<DistributeInfo> infoList;
		if (parms != "") {
			String[] parm = parms.split(",");
			infoList = Helper.getPersistService().query(sql2, DistributeInfo.class, parm);
		} else {
			infoList = Helper.getPersistService().query(sql2, DistributeInfo.class);
		}

		List keyList = new ArrayList();
		for (int i = 0; i < infoList.size(); i++) {
			DistributePaperTask distributePaperTask = paperList.get(i);
			DistributePaperTask task = distributePaperTask.cloneDisTask();
			DistributeInfo distributeInfo = infoList.get(i);
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

	private String getSql(Map<String, String> map) {
		parms = "";
		String creatorIID = map.get("creator");
		String disInfoIID = map.get("disInfo");
		String flag = map.get("flag");
		String disInfoName = map.get("disInfoName");
		String oid=map.get("oid");
		String sendtype=map.get("sendType");
		String notsend=ConstLifeCycle.LC_NOT_DISTRIBUT.getName();//���ŵ���δ�ַ���
		String sending = ConstUtil.LC_DISTRIBUTING.getName();//ֽ���������������Ƿ��͵�
		String sent = ConstUtil.LC_DISTRIBUTED.getName();//ֽ�����������������ѷ��͵�
		String querySendDateFrom = map.get("querySendDateFrom");
		String querySendDateTo = map.get("querySendDateTo");
		String infosendtype=null;
		long sendDateFrom = 0L;
		long sendDateTo = 0L;
		sendDateFrom = DateTimeUtil.getLongTime(querySendDateFrom);
		sendDateTo = DateTimeUtil.getLongTime(querySendDateTo) + 86399000L;

		Principal principal = null;
		if (!"".equals(disInfoIID)) {
			if ("1".equals(flag)) {
				principal = OrganizationHelper.getService().getOrganization(disInfoIID);
				if(sendtype.equals("0")){
					infosendtype="0";
				}else{
					infosendtype="1";
				}
			} else if ("2".equals(flag)) {
				principal = UserHelper.getService().getUser(disInfoIID);
				infosendtype=null;
			}
		}

		StringBuffer sbSql = new StringBuffer();
		Principal creator = null;
		sbSql.append("select DISTINCT {0} from DDM_DIS_PAPERTASK t,DDM_DIS_INFO f,DDM_DIS_TASKINFOLINK l");
		sbSql.append(" where t.innerId = l.fromObjectId and t.classId = l.fromObjectClassId");
		sbSql.append(" and l.toObjectId = f.innerId and l.toObjectClassId = f.classId");
		sbSql.append(" and f.DISMEDIATYPE = '0'");
		if (StringUtil.isNull(querySendDateFrom) && StringUtil.isNull(querySendDateTo)) {
			sbSql.append(" and t.stateName =?");
			parms += sending + ",";
			sbSql.append(" and f.stateName=?");
			parms += notsend+",";
		} else {
			sbSql.append(" and t.stateName in ('" + sending + "','" + sent + "')");
		}
		String taskName = map.get("taskName");
		if (taskName != null && taskName.length() > 0) {
			sbSql.append(" and t.name like '%" + taskName + "%'");
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
	    		if (valList.size() > 0 && valList != null) {
	    			sbSql.append(")");
	    		}
    		}
    	}

		if (creatorIID != null && creatorIID.length() > 0) {
			creator = UserHelper.getService().getUser(creatorIID);
			sbSql.append(" and t.createById =? and t.createByClassId = ?");
			parms += creator.getInnerId() + "," + creator.getClassId() + ",";
		}
		if ("".equals(disInfoIID) && !"".equals(disInfoName)) {

			sbSql.append(" and f.disinfoname = ?");
			parms += disInfoName + ",";
		}
		if (disInfoIID != null && disInfoIID.length() > 0) {
			sbSql.append(" and f.disInfoId = ? and f.infoClassId = ?");
			parms += principal.getInnerId() + "," + principal.getClassId() + ",";
		}
		if(oid!=null&&oid.length()>0){
			sbSql.append(" and f.disInfoId = ? and f.infoClassId = ?");
			String innerId = Helper.getInnerId(oid);
			String classId = Helper.getClassId(oid);
			parms += innerId + "," + classId + ",";
		}
		if(!StringUtil.isNull(infosendtype)){
			sbSql.append(" and f.sendtype = ?");
			parms += infosendtype + ",";
		}
		String queryCreateDateFrom = map.get("queryCreateDateFrom");
		String queryCreateDateTo = map.get("queryCreateDateTo");
		long dateFrom = 0L;
		long dateTo = 0L;
		dateFrom = DateTimeUtil.getLongTime(queryCreateDateFrom);
		dateTo = DateTimeUtil.getLongTime(queryCreateDateTo) + 86399000L;
		if (StringUtil.isNull(querySendDateFrom) && StringUtil.isNull(querySendDateTo)) {
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
		} else {
			if (!StringUtil.isNull(querySendDateFrom) && querySendDateTo.length() == 0) {
				sbSql.append(" and f.sendTime >= ?");
				parms += sendDateFrom + ",";
			} else if (!StringUtil.isNull(querySendDateTo) && querySendDateFrom.length() == 0) {
				sbSql.append(" and f.sendTime <= ?");
				parms += sendDateTo + ",";
			} else if (!StringUtil.isNull(querySendDateFrom) && !StringUtil.isNull(querySendDateTo)) {
				sbSql.append(" and f.sendTime between ? and ?");
				parms += sendDateFrom + "," + sendDateTo + ",";
			}
		}
		sbSql.append(" {1} ");
		return sbSql.toString();
	}

	public void updateDistributeInsideCycles(Map<String, String> map, String taskOids) {
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		List<String> oidList = SplitString.string2List(taskOids, ",");
		for (String taskOid : oidList) {
			String taskInnerId = Helper.getInnerId(taskOid);
			String taskClassId = Helper.getClassId(taskOid);
			String whereSql = " and t.innerId = ? and t.classId = ? order by T.MODIFYTIME, f.MODIFYTIME ";
			String mainSql = getSql(map);

			String sql1 = mainSql.replace("{0}", "f.*,t.*");
			sql1 = sql1.replace("{1}", whereSql);
			List<DistributeInfo> disInfoList;
			if (parms != "") {
				parms += taskInnerId + "," + taskClassId + ",";
				String[] parm = parms.split(",");
				disInfoList = Helper.getPersistService().query(sql1, DistributeInfo.class, parm);
			} else {
				disInfoList = Helper.getPersistService().query(sql1, DistributeInfo.class, taskInnerId, taskClassId);
			}

			boolean infoFlag = true;
			List<DistributeInfo> infoList1 = new ArrayList<DistributeInfo>();
			for (DistributeInfo info : disInfoList) {
				Persistable obj = Helper.getPersistService().getObject(
						Helper.getOid(info.getClassId(), info.getInnerId()));
				DistributeInfo dis = (DistributeInfo) obj;
				//�ַ���Ϣ������������
				if (ConstUtil.LC_NOT_DISTRIBUT.getName().equals(dis.getLifeCycleInfo().getStateName())) {
					life.promoteLifeCycle(dis);
					dis.setSendTime(System.currentTimeMillis());
					infoList1.add(dis);
				}
			}
			if (infoList1 != null && infoList1.size() > 0) {
				Helper.getPersistService().update(infoList1);
			}

			String sql = "select DISTINCT DISOBJ.* from DDM_DIS_TASKINFOLINK t,DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink,DDM_DIS_OBJECT disObj,DDM_DIS_PAPERTASK task"
					+ " where disInfo.innerId = t.toObjectId and disInfo.classId = t.toObjectClassId"
					+ " and objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
					+ " and disObj.innerId = objLink.toObjectId and disObj.classId = objLink.toObjectClassId"
					+ " and t.fromObjectId = task.innerId and t.fromObjectClassId = task.classId and"
					+ " t.fromObjectId = ? and t.fromObjectClassId = ?";
			List<DistributeObject> objList = Helper.getPersistService().query(sql, DistributeObject.class, taskInnerId,
					taskClassId);
			String sent = ConstUtil.LC_DISTRIBUTED.getName();
			for (DistributeObject object : objList) {
				String infoSql = "SELECT DISTINCT INFO.* FROM DDM_DIS_TASKINFOLINK INFOLINK,DDM_DIS_INFO INFO,DDM_DIS_ORDEROBJLINK LINK,DDM_DIS_OBJECT OBJ WHERE INFO.DISORDEROBJLINKID = LINK.INNERID AND INFO.DISORDEROBJLINKCLASSID = LINK.CLASSID AND LINK.TOOBJECTID = OBJ.INNERID AND LINK.TOOBJECTCLASSID = OBJ.CLASSID AND OBJ.INNERID = ? AND OBJ.CLASSID = ? AND INFO.CLASSID || ':' || INFO.INNERID = INFOLINK.TOOBJECTCLASSID || ':' || INFOLINK.TOOBJECTID "
						+ " AND INFOLINK.FROMOBJECTCLASSID || ':' || INFOLINK.FROMOBJECTID = '" + taskOid + "'";
				//�����ö�Ӧ�ַ������µ����зַ���Ϣ
				List<DistributeInfo> infoList = Helper.getPersistService().query(infoSql, DistributeInfo.class,
						object.getInnerId(), object.getClassId());
				for (DistributeInfo info : infoList) {
					if (!sent.equals(info.getLifeCycleInfo().getStateName())) {
						//�ַ����ݶ�Ӧ�ķַ���Ϣδȫ������
						infoFlag = false;
						break;
					}
				}
				//�ַ�����������������(�ַ���Ϣȫ������)
				if (infoFlag == true) {
					String hql = "from DistributeOrderObjectLink t "
							+ "where t.toObjectRef.innerId=? and t.toObjectRef.classId=? ";

					List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, object.getInnerId(),
							object.getClassId());
					List<DistributeOrderObjectLink> linkList = new ArrayList<DistributeOrderObjectLink>();
					for (DistributeOrderObjectLink link : links) {
						if (ConstUtil.LC_DISTRIBUTING.getName().equals(link.getLifeCycleInfo().getStateName())) {
							life.promoteLifeCycle(link);
							linkList.add(link);
						}
					}
					if (linkList != null && linkList.size() > 0 ) {
						Helper.getPersistService().update(linkList);
					}
				}
			}

			if (infoFlag == true) {
				//���ַ���Ϣȫ����������������
				Persistable obj = Helper.getPersistService().getObject(taskOid);
				DistributePaperTask disTask = (DistributePaperTask) obj;
				life.promoteLifeCycle(disTask);
				Helper.getPersistService().update(disTask);

				String orderSql = "select MAX(ORD.INNERID) AS INNERID, MAX(ORD.CLASSID) AS CLASSID from DDM_DIS_TASKINFOLINK t,DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink,DDM_DIS_ORDER ORD"
						+ " where disInfo.innerId = t.toObjectId and disInfo.classId = t.toObjectClassId"
						+ " and objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
						+ " and ORD.innerId = objLink.fromObjectId and ORD.classId = objLink.fromObjectClassId"
						+ " and t.fromObjectId =? and t.fromObjectClassId = ?";

				List<Map<String, Object>> list = PersistHelper.getService().query(orderSql, taskInnerId, taskClassId);
				Map<String, Object> mapTemp = list.get(0);
				String orderInnerId = mapTemp.get("INNERID").toString();
				String orderClassId = mapTemp.get("CLASSID").toString();
				Persistable orderObj = Helper.getPersistService().getObject(Helper.getOid(orderClassId, orderInnerId));
				DistributeOrder disOrder = (DistributeOrder) orderObj;
				//���ŵ�������������
				boolean temp = SessionHelper.getService().isCheckPermission();
				SessionHelper.getService().setCheckPermission(false);
				life.promoteLifeCycle(disOrder);
				SessionHelper.getService().setCheckPermission(temp);
				Helper.getPersistService().update(disOrder);
			}
		}
	}

	public List<DistributeInfo> getDistributeDestroyDetails(Map<String, String> map, Object taskOids) {
		List<String> oidList = SplitString.string2List((String) taskOids, ",");
		List<DistributeInfo> infoList = new ArrayList<DistributeInfo>();
		for (String taskOid : oidList) {
			String taskInnerId = Helper.getInnerId(taskOid);
			String taskClassId = Helper.getClassId(taskOid);
			String sql = " and t.innerId = ? and t.classId = ? order by T.MODIFYTIME, f.MODIFYTIME desc";
			String mainSql = getSql(map);

			String sql1 = mainSql.replace("{0}", "f.*,t.*");
			sql1 = sql1.replace("{1}", sql);
			List<DistributeInfo> disInfoList;
			if (parms != "") {
				parms += ConstUtil.LC_DISTRIBUTED.getName() + "," + taskInnerId + "," + taskClassId + ",";
				String[] parm = parms.split(",");
				disInfoList = Helper.getPersistService().query(sql1, DistributeInfo.class, parm);
			} else {
				disInfoList = Helper.getPersistService().query(sql1, DistributeInfo.class, ConstUtil.LC_DISTRIBUTED.getName(),
						taskInnerId, taskClassId);
			}
			for (DistributeInfo info : disInfoList) {
				String objSql = "SELECT MAX(OBJ.ID) AS OBJECTID,MAX(OBJ.NAME) AS OBJECTNAME FROM DDM_DIS_ORDEROBJLINK LINK,DDM_DIS_OBJECT OBJ,DDM_DIS_INFO INFO WHERE OBJ.INNERID = LINK.TOOBJECTID AND OBJ.CLASSID = LINK.TOOBJECTCLASSID AND LINK.INNERID = INFO.DISORDEROBJLINKID AND LINK.CLASSID = INFO.DISORDEROBJLINKCLASSID AND INFO.INNERID = '"
						+ info.getInnerId() + "' AND INFO.CLASSID = '" + info.getClassId() + "'";
				List<Map<String, Object>> list = PersistHelper.getService().query(objSql);
				Map<String, Object> mapTemp = list.get(0);
				DistributeInfo disInfo = info.cloneDisInfo();
				disInfo.setNumber(mapTemp.get("OBJECTID").toString());
				disInfo.setName(mapTemp.get("OBJECTNAME").toString());
				infoList.add(disInfo);
			}
		}
		return infoList;
	}

	public List<DistributeObject> getDistributePaperTaskPropertyList(Map<String, String> map, String oid) {
		List<DistributeObject> objectList = new ArrayList<DistributeObject>();
		String disPaperTaskInnerId = Helper.getInnerId(oid);
		String disPaperTaskClassId = Helper.getClassId(oid);
		String principals = "";
		//String creatorIID = map.get("creator");
		String disInfoIID = "";
		String flag = "";
		String prioid="";
		String infoClassId="";
		String infoInnerId="";
		if (map != null) {
			prioid=(String)map.get("oid");
			if(!StringUtil.isNull(prioid)){
				infoInnerId=Helper.getInnerId(prioid);
				infoClassId=Helper.getClassId(prioid);
			}else{
				disInfoIID = map.get("disInfo");
			}
			flag = map.get("flag");
		}
		Principal principal = null;
		if (StringUtil.isNull(prioid)) {
		if ("1".equals(flag)) {
			principal = OrganizationHelper.getService().getOrganization(disInfoIID);
		} else if ("2".equals(flag)) {
			principal = UserHelper.getService().getUser(disInfoIID);
		}
		}
		//Principal creator = null;
		StringBuffer sbSql = new StringBuffer();
		if(disPaperTaskClassId.equals(RecDesPaperTask.CLASSID)){
			sbSql.append("select DISTINCT objLink.* "
					+ "from DDM_DIS_TASKINFOLINK t,"
					+ "DDM_RECDES_INFO disInfo,"
					+ "DDM_DIS_ORDEROBJLINK objLink "
					+ "where disInfo.innerId = t.toObjectId "
					+ "and disInfo.classId = t.toObjectClassId "
					+ "and objLink.innerId = disInfo.disOrderObjectLinkId "
					+ "and objLink.classId = disInfo.disOrderObjectLinkClassId "
					+ "and t.fromObjectId = ? "
					+ "and t.fromObjectClassId = ?");
		}else if(disPaperTaskClassId.equals(DistributePaperTask.CLASSID)){
			sbSql.append("select DISTINCT objLink.* from DDM_DIS_TASKINFOLINK t,DDM_DIS_INFO disInfo,DDM_DIS_ORDEROBJLINK objLink"
					+ " where disInfo.innerId = t.toObjectId and disInfo.classId = t.toObjectClassId"
					+ " and objLink.innerId = disInfo.disOrderObjLinkId and objLink.classId = disInfo.disOrderObjLinkClassId"
					+ " and t.fromObjectId = ? and t.fromObjectClassId = ?");
		}
		if (disInfoIID != null && disInfoIID.length() > 0) {
			sbSql.append(" and disInfo.disInfoId =? and disInfo.infoClassId = ?");
			principals += principal.getInnerId() + "," + principal.getClassId() + ",";
		}
		if(prioid!=null&&prioid.length()>0){
			sbSql.append(" and disInfo.disInfoId =? and disInfo.infoClassId = ?");
			principals +=  infoInnerId+ "," + infoClassId + ",";
		}
		String linkSql = sbSql.toString();
		List<DistributeOrderObjectLink> linkList;
		String disPaperTasks = disPaperTaskInnerId + "," + disPaperTaskClassId + ",";
		if (principals != "") {
			principals = disPaperTasks + principals;

			String[] princ = principals.split(",");
			linkList = PersistHelper.getService().query(linkSql, DistributeOrderObjectLink.class, princ);
		} else {
			linkList = PersistHelper.getService().query(linkSql, DistributeOrderObjectLink.class, disPaperTaskInnerId,
					disPaperTaskClassId);
		}

		for (DistributeOrderObjectLink link : linkList) {
			DistributeObject obj = (DistributeObject) link.getTo();
			obj.setDistributeOrderObjectLink(link);
			objectList.add(obj);
		}
		return objectList;
	}

	public HSSFWorkbook getExcelObject(Map mapObject) {
		// ����������Ϣ
		Map map = creatData(mapObject);

		// Sheetҳ��
		String sheetName = ConstUtil.SEND_SHEET_NAME;

		// ģ��·��
		String templateFullName = ConstUtil.EXCEL_TEMPLATE_PATH + ConstUtil.EXCEL_TEMPLATE_SENDORDER;
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
			setHeadData(ew, map);
			// ����ͷ��Ԫ��ϲ����и�����
			headerMergeHeight(ew);

			// ��ϸ�п�����ֵ�趨����Ԫ��ϲ����и�����
			iDetailRowEnd = copySetMergeDetail(ew, iDetailRowStart, map);
			iFootCopyRowStart = iDetailRowEnd + 1;

			String sendType = StringUtil.getString(map.get("sendType"));
			// ����β�п���
			if ("0".equals(sendType)) {
				// �ڷ�������β
				ew.copyRows(iFootRowStart - 1, iFootRowEnd - 1, iFootCopyRowStart - 1);
			} else {
				// �ⷢ������β
				ew.copyRows(iFootRowStart - 1, iFootRowEnd - 2, iFootCopyRowStart - 1);
				ew.createRow(iFootCopyRowStart + 2);
				ew.createCell(iFootCopyRowStart + 2, 1);
				ew.setCellStyle(iFootCopyRowStart + 2, 1, 300, false, false, false);
			}

			// ����βֵ�趨
			setFootData(ew, iFootCopyRowStart, map);

			// ����β��Ԫ��ϲ����и�����
			footMergeHeight(ew, iFootCopyRowStart, map);

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
	 * ����������Ϣ��
	 * 
	 * @param map Map
	 * @return Map
	 * 			key:disOrder value:DistributeOrder
	 * 			key:disPaperTask value:DistributePaperTask
	 * 	 		key:detaildisObjList value:List<DistributeObject>
	 * 	 		key:detaildisInfoList value:List<List<DistributeInfo>>
	 *  	 	key:infoName value:String
	 *   	 	key:sendType value:String
	 *   		key:oid value:String
	 */
	private Map creatData(Map map) {
		String oids = StringUtil.getString(map.get("oids"));
		String taskOid[] = oids.split(",");
		String disInfoId = (String) map.get("disInfo");
		String prioid=(String)map.get("oid");
		String flag=(String)map.get("flag");
		String infoClassId="";
		// ��ȡ������λ
		String disInfoName=(String)map.get("disInfoName");
		if(!StringUtil.isNull(prioid)){
		disInfoId=Helper.getInnerId(prioid);
		infoClassId=Helper.getClassId(prioid);
		}
		Principal principal = null;
		if ("".equals(prioid)) {
			if ("1".equals(flag)) {
				principal = OrganizationHelper.getService().getOrganization(disInfoId);
			}else if ("2".equals(flag)) {
				principal = UserHelper.getService().getUser(disInfoId);
			}
			disInfoId=principal.getInnerId();
			infoClassId=principal.getClassId();
		}
		List<DistributeObject> detaildisObjList = new ArrayList<DistributeObject>();
		List<DistributeInfo> detaildisInfoList = new ArrayList<DistributeInfo>();
		Map mapObject = new HashMap();
		mapObject.putAll(map);
		for (String oid : taskOid) {
			String innerIdTemp = Helper.getInnerId(oid);
			String classIdTemp = Helper.getClassId(oid);

			// ���ŵ���ַ�����link:DDM_DIS_ORDEROBJLINK
			String sql = "select DISTINCT objLink.* " + " from DDM_DIS_TASKINFOLINK taskInfoLink, "
					+ " DDM_DIS_INFO disInfo, DDM_DIS_ORDEROBJLINK objLink " + " where taskInfoLink.FROMOBJECTID =? "
					+ " and taskInfoLink.FROMOBJECTCLASSID =? " + " and taskInfoLink.TOOBJECTID =disInfo.INNERID "
					+ " and taskInfoLink.TOOBJECTCLASSID = disInfo.CLASSID "
					+ " and objLink.INNERID = disInfo.DISORDEROBJLINKID "
					+ " and objLink.CLASSID = disInfo.DISORDEROBJLINKCLASSID " + " and disInfo.disInfoId =? "
					+ " and disInfo.infoClassId =?  order by objLink.ISMASTER desc";

			List<DistributeOrderObjectLink> list = PersistHelper.getService().query(sql,
					DistributeOrderObjectLink.class, innerIdTemp, classIdTemp, disInfoId, infoClassId);

			ATSuit suit = null;
			String suitInfoKey = "";
			int i = 0;
			for (DistributeOrderObjectLink disOrdObjLink : list) {				
				// �ַ����ݶ���:DDM_DIS_OBJECT
				DistributeObject disObj = (DistributeObject) disOrdObjLink.getTo();

			    Persistable dataObj = Helper.getPersistService().getObject(disObj.getDataClassId() + ":" + disObj.getDataInnerId());
			    //�������������ӹ���excel���
				if (dataObj instanceof Part) {
					i++;
					continue;
				}
				// �ַ���ϢDDM_DIS_INFO
				sql = "select DISTINCT disInfo.* from DDM_DIS_INFO disInfo " + " where disInfo.DISORDEROBJLINKID = ? "
						+ "and disInfo.DISORDEROBJLINKCLASSID = ? " + " and disInfo.disInfoId = ? "
						+ " and disInfo.infoClassId = ? " + " and disInfo.DISMEDIATYPE = '0'";

				List<DistributeInfo> disInfoList = Helper.getPersistService().query(sql, DistributeInfo.class,
						disOrdObjLink.getInnerId(), disOrdObjLink.getClassId(), disInfoId, infoClassId);
				String infoMapKey = "";
				for (DistributeInfo tmpInfo : disInfoList) {
					String disinfoName = tmpInfo.getDisInfoName();
					long disInfoNum = tmpInfo.getDisInfoNum();
					String note = tmpInfo.getNote();
					infoMapKey += disinfoName + "," + disInfoNum + "," + note + ";";
				}
				if (i == 0) {
					//�׶�������⴦��
					//���׳��ηַ������ײ���ʱ���ӹ��������͵��н�չʾ�׶�����Ŀ��Ϣ�����ж�����ĳ���ڶ�����������ַ���Ϣʱ��Ҳ��Ҫ�������ַ������ڶ�����Ϊ��Ŀ��Ϣչʾ
					if ("ATSuit".equals(disObj.getDataClassId())) {
						suit = (ATSuit)Helper.getPersistService().getObject(disObj.getDataClassId() + ":" + disObj.getDataInnerId());
						DistributeOrder disOrder = (DistributeOrder) disOrdObjLink.getFrom();
						// �ǳ��ηַ�
						if ("0".equals(disOrder.getOrderType()) && !"A".equals(suit.getIterationInfo().getVersionNo())) {
							suit = null;
						} else {
							//���׳��ηַ������ײ���ʱ
							suitInfoKey = infoMapKey;
						}
					}
				} else {
					if (suit != null) {
						Persistable obj = Helper.getPersistService().getObject(disObj.getDataClassId() + ":" + disObj.getDataInnerId());
						if (obj instanceof Suited) {
							ATSuitMemberLink link = ATSuitMemberLink.getlink(suit, (Suited)obj);
							if (link != null && infoMapKey.equals(suitInfoKey)) {
								i++;
								continue;
							}
						}
					}
				}
				for (DistributeInfo infoObj : disInfoList) {
					detaildisObjList.add(disObj);
					detaildisInfoList.add(infoObj);
				}
				i++;
				}
		}

		mapObject.put("detaildisObjList", detaildisObjList);
		mapObject.put("detaildisInfoList", detaildisInfoList);
		return mapObject;
	}

	/**
	 * ����ͷֵ�趨��
	 * 
	 * @param ew ExcelUtil
	 * @param map Map
	 * @throws Exception
	 */
	private void setHeadData(ExcelUtil ew, Map map) throws Exception {

		// ������
		// [������λ]valueֵ(��:3,��:4)
		// ��ȡ������λ
		String disInfoName = (String) map.get("disInfoName");
		ew.setCell(3, 4, disInfoName);

		// [No.]valueֵ(��:3,��:13)
		String sendType = StringUtil.getString(map.get("sendType"));
		if (ConstUtil.C_S_ZERO.equals(sendType)) {
			// TODO
			ew.setCell(3, 13, "");
		} else {
			ew.setCell(3, 13, getSeriaNo());
		}
	}

	/**
	 * ����ͷ��Ԫ��ϲ����и����á�
	 * 
	 * @param ew ExcelUtil
	 */
	private void headerMergeHeight(ExcelUtil ew) {
		// [�����ӹ����뵥]��Ԫ��ϲ�(�У�1~2���У�1~14)
		ew.mergeCells(1, 2, 1, 15);

		// [������λ]]��Ԫ��ϲ�(�У�3���У�2~3)
		ew.mergeRows(3, 2, 3);
		// [������λ]��Ӧvalueֵ��Ԫ��ϲ�(�У�3���У�4~7)
		ew.mergeRows(3, 4, 7);
		// [No.]��Ӧvalueֵ��Ԫ��ϲ�(�У�3���У�13~15)
		ew.mergeRows(3, 13, 15);

		// [���]��Ԫ��ϲ�(�У�4���У�3~4)
		ew.mergeRows(4, 3, 4);
		// [����]��Ԫ��ϲ�(�У�4���У�5~6)
		ew.mergeRows(4, 5, 6);
		// [��ע]��Ԫ��ϲ�(�У�4���У�14~15)
		ew.mergeRows(4, 14, 15);

		// �и�����
		ew.setRowHeight(1, 300);
		ew.setRowHeight(2, 300);
		ew.setRowHeight(3, 300);
		ew.setRowHeight(4, 300);
	}

	/**
	 * ������ϸ��Copy��ֵ�趨�͵�Ԫ��ϲ����и����á�
	 * 
	 * @param ew ExcelUtil
	 * @param iDetailRowStart int
	 * @param map Map
	 * @return int
	 * @throws Exception
	 */
	private int copySetMergeDetail(ExcelUtil ew, int iDetailRowStart, Map map) throws Exception {
		List<DistributeObject> detaildisObjList = (List<DistributeObject>) map.get("detaildisObjList");
		List<DistributeInfo> detaildisInfoList = (List<DistributeInfo>) map.get("detaildisInfoList");
		int iSize = detaildisObjList.size();

		DistributeObject disObj;
		DistributeInfo disInfo;
		
		// Copy��ϸ��λ��
		int iDtailRowTemp = iDetailRowStart;
		if (iSize == 0) {
			ew.copyRows(iDtailRowTemp - 1, iDtailRowTemp - 1, iDtailRowTemp - 1);

			// [���]��Ԫ��ϲ�(�У�4���У�3~4)
			ew.mergeRows(iDtailRowTemp, 3, 4);
			// [����]��Ԫ��ϲ�(�У�4���У�5~6)
			ew.mergeRows(iDtailRowTemp, 5, 6);
			// [��ע]��Ԫ��ϲ�(�У�4���У�14~15)
			ew.mergeRows(iDtailRowTemp, 14, 15);

			// �и�����
			ew.setRowHeight(iDtailRowTemp - 1, 300);
		} else {
			// Copy�п�ʼλ�ã�ģ���п�ʼλ�ã�ģ���н���λ�ã�Copy�ܴ���
			ew.blockLoop(iDtailRowTemp - 1, iDetailRowStart - 1, iDetailRowStart - 1, iSize);
			for (int i = 0; i < iSize; i++) {
				// �и�����
				ew.setRowHeight(iDtailRowTemp + i, 300);

				disObj = detaildisObjList.get(i);

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

				disInfo = detaildisInfoList.get(i);
				
				// [����]valueֵ�趨(��:iDtailRowTemp+i,��:12)
				String type=TypeHelper.getService().getType(disObj.getDataClassId()).getName();
				ew.setCell(iDtailRowTemp + i, 12, type);
				
				// [����]valueֵ�趨(��:iDtailRowTemp+i,��:13)
				ew.setCell(iDtailRowTemp + i, 13, String.valueOf(disInfo.getDisInfoNum()));
				// [��ע]valueֵ�趨(��:iDtailRowTemp+i,��:14)
				ew.setCell(iDtailRowTemp + i, 14, disInfo.getNote());

				// [���]��Ԫ��ϲ�(�У�iDtailRowTemp+i���У�3~4)
				ew.mergeRows(iDtailRowTemp + i, 3, 4);
				// [����]��Ԫ��ϲ�(�У�iDtailRowTemp+i���У�5~6)
				ew.mergeRows(iDtailRowTemp + i, 5, 6);
				// [��ע]��Ԫ��ϲ�(�У�iDtailRowTemp+i���У�14~15)
				ew.mergeRows(iDtailRowTemp + i, 14, 15);

				// ���õ�Ԫ���Զ�����
				String maxLength = StringUtil.maxValue(disObj.getNumber(), disObj.getName(), disInfo.getNote(),
						disObj.getCode());
				if(maxLength.getBytes().length > 16){
					ew.setRowHeight(iDtailRowTemp + i, maxLength, 8);
				}else{
				if(type.getBytes().length > 4){
					ew.setCellHeightStyle(iDtailRowTemp+i, 12, type, 2);
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
	 * @param ew  ExcelUtil
	 * @param rowIndex int
	 * @param map Map
	 * @throws Exception
	 */
	private void setFootData(ExcelUtil ew, int rowIndex, Map map) throws Exception {
		// ��rowIndex��
		User currentUser = SessionHelper.getService().getUser();
		// [������λ]��Ӧvalueֵ�趨(�У�Copy����β�п�ʼλ�ã��У�4~7)
		ew.setCell(rowIndex, 4, currentUser.getOrganizationName());
		// [���յ�λ]��Ӧvalueֵ�趨(�У�Copy����β�п�ʼλ�ã��У�10~12)
		ew.setCell(rowIndex, 10, "");

		// ��rowIndex+1��
		// [������]��Ӧvalueֵ�趨(�У�Copy����β�п�ʼλ��+1���У�4~7)
		ew.setCell(rowIndex + 1, 4, currentUser.getName());
		// [������]��Ӧvalueֵ�趨(�У�Copy����β�п�ʼλ��+1���У�10~12)
		ew.setCell(rowIndex + 1, 10, "");

		// ��rowIndex+2��
		String sendType = StringUtil.getString(map.get("sendType"));
		if (ConstUtil.C_S_ZERO.equals(sendType)) {
			// [����]��Ӧvalueֵ�趨(�У�Copy����β�п�ʼλ��+2���У�4~7)
			ew.setCell(rowIndex + 2, 4, DateTimeUtil.getCurrentDate());
			// [����]��Ӧvalueֵ�趨(�У�Copy����β�п�ʼλ��+2���У�10~12)
			ew.setCell(rowIndex + 2, 10, "");
		} else {
			// �ⷢ����ִ��ʾ[��������յ��ļ���һ�����ڸ���ǩ�ּĻ�һ��]valueֵ�趨(�У�Copy����β�п�ʼλ��+2���У�2~14)
			ew.setCell(rowIndex + 2, 1, ConstUtil.SEND_ORDER_MESSAGE);
		}
	}

	/**
	 * ����β��Ԫ��ϲ����и����á�
	 * 
	 * @param ew ExcelUtil
	 * @param rowIndex int
	 */
	private void footMergeHeight(ExcelUtil ew, int rowIndex, Map map) {
		//  ��rowIndex��
		// [������λ]��Ԫ��ϲ�(�У�Copy����β�п�ʼλ�ã��У�2~3)
		ew.mergeRows(rowIndex, 2, 3);
		// ��Ӧvalueֵ��Ԫ��ϲ�(�У�Copy����β�п�ʼλ�ã��У�4~7)
		ew.mergeRows(rowIndex, 4, 7);
		// [���յ�λ]��Ԫ��ϲ�(�У�Copy����β�п�ʼλ�ã��У�8~9)
		ew.mergeRows(rowIndex, 8, 9);
		// ��Ӧvalueֵ��Ԫ��ϲ�(�У�Copy����β�п�ʼλ�ã��У�10~15)
		ew.mergeRows(rowIndex, 10, 15);

		//  ��rowIndex+1��
		// [������]��Ԫ��ϲ�(�У�Copy����β�п�ʼλ��+1���У�2~3)
		ew.mergeRows(rowIndex + 1, 2, 3);
		// ��Ӧvalueֵ��Ԫ��ϲ�(�У�Copy����β�п�ʼλ��+1���У�4~7)
		ew.mergeRows(rowIndex + 1, 4, 7);
		// [������]��Ԫ��ϲ�(�У�Copy����β�п�ʼλ��+1���У�8~9)
		ew.mergeRows(rowIndex + 1, 8, 9);
		// ��Ӧvalueֵ��Ԫ��ϲ�(�У�Copy����β�п�ʼλ��+1���У�10~15)
		ew.mergeRows(rowIndex + 1, 10, 15);

		//  ��rowIndex+2��
		String sendType = StringUtil.getString(map.get("sendType"));
		if ("0".equals(sendType)) {
			// [����]��Ԫ��ϲ�(�У�Copy����β�п�ʼλ��+2���У�2~3)
			ew.mergeRows(rowIndex + 2, 2, 3);
			// ��Ӧvalueֵ��Ԫ��ϲ�(�У�Copy����β�п�ʼλ��+2���У�4~7)
			ew.mergeRows(rowIndex + 2, 4, 7);
			// [����]��Ԫ��ϲ�(�У�Copy����β�п�ʼλ��+2���У�8~9)
			ew.mergeRows(rowIndex + 2, 8, 9);
			// ��Ӧvalueֵ��Ԫ��ϲ�(�У�Copy����β�п�ʼλ��+2���У�10~15)
			ew.mergeRows(rowIndex + 2, 10, 15);
		} else {
			// [��������յ��ļ���һ�����ڸ���ǩ�ּĻ�һ��]��Ԫ��ϲ�(�У�1~1���У�2~15)
			ew.mergeRows(rowIndex + 2, 1, 16);
		}

		// �и�����
		ew.setRowHeight(rowIndex, 300);
		ew.setRowHeight(rowIndex + 1, 300);
		ew.setRowHeight(rowIndex + 2, 300);
	}

	/**
	 * �����п�
	 * 
	 * @param ew ExcelUtil
	 */
	private void setColumnWidth(ExcelUtil ew) {
		// ��:1,���Ϊ300
		ew.setColumnWidth(1, 300);
		// ��:2,���Ϊ1500
		ew.setColumnWidth(2, 1500);
		// ��:3~6,���Ϊ2000
		ew.setColumnWidth(3, 2000);
		ew.setColumnWidth(4, 2000);
		ew.setColumnWidth(5, 2000);
		ew.setColumnWidth(6, 2000);
		// ��:7,���Ϊ3000
		ew.setColumnWidth(7, 3000);
		// ��:8,���Ϊ2000
		ew.setColumnWidth(8, 2000);
		// ��:9,���Ϊ2000
		ew.setColumnWidth(9, 2000);
		// ��:10,���Ϊ2000
		ew.setColumnWidth(10, 2000);
		// ��:11,���Ϊ2000
		ew.setColumnWidth(11, 2000);
		// ��:12,���Ϊ2000
		ew.setColumnWidth(12, 2000);
		// ��:13,���Ϊ2000
		ew.setColumnWidth(13, 2000);
		// ��:14,���Ϊ2000
		ew.setColumnWidth(14, 2000);
		// ��:15,���Ϊ3000
		ew.setColumnWidth(15, 3000);
		// ��:16,���Ϊ300
		ew.setColumnWidth(16, 300);
	}

	/**
	 * ȡ����ˮ�š�
	 */
	private String getSeriaNo() {
		CodeApplyAction codeaction = new CodeApplyAction();
		String seriaNo = "";
		// ���ֽ��������ر������
		List<CodeRuleBindInfo> ruleBindList = codeaction.getCodeRuleBind(
				ConstUtil.DISTRIBUTEPAPERTASK, null);
		CodeRuleBindInfo codeRuleBind = null;
		int sign = 0;
		if (ruleBindList != null && ruleBindList.size() > 0) {
			for (CodeRuleBindInfo ruleBind : ruleBindList) {
				if (ruleBind.isIfDefault()) {// ���Ĭ�ϱ������
					codeRuleBind = ruleBind;
					sign = 1;
				}
			}
		}
		if (sign == 0) {
			return seriaNo;
		} else {
			Map<String, String> map = new HashMap<String, String>();
			Map<String, String> genCode = CodemanHelper.getService()
					.getCodeByParamAndRulbind(map, codeRuleBind);
			seriaNo = genCode.get("NUMBER");
			return seriaNo;
		}
	}

	@Override
	public List<Map<String, Object>> getDisInfoNames(String disInfoType,
			String sendtype) {
		String sending = ConstLifeCycle.LC_DISTRIBUTING.getName();
		String notsend = ConstLifeCycle.LC_NOT_DISTRIBUT.getName();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select DISTINCT f.DISINFOID,f.INFOCLASSID,f.DISINFONAME from DDM_DIS_PAPERTASK t,DDM_DIS_INFO f,DDM_DIS_TASKINFOLINK l ");
		sbSql.append(" where t.innerId = l.fromObjectId and t.classId = l.fromObjectClassId ");
		sbSql.append(" and l.toObjectId = f.innerId and l.toObjectClassId = f.classId ");
		sbSql.append(" and f.disMediaType = '0' and t.stateName =? and f.stateName=? ");
		if (disInfoType.equals("0")) {
			sbSql.append(" and f.DISINFOTYPE='0'");
			if (sendtype.equals("0")) {
				sbSql.append(" and f.sendType='0'");
			} else {
				sbSql.append(" and f.sendType='1'");
			}
		}
		if (disInfoType.equals("1")) {
			sbSql.append(" and f.DISINFOTYPE='1'");
		}
		String sql = sbSql.toString();
		result = PersistHelper.getService().query(sql, sending, notsend);
		return result;
	}

	/**
	 * @author kangyanfei 2014-07-09
	 */
	@Override
	public List<Map<String, Object>> getDisInfoNamesByDisIntfoTypeAndDestroyType(String disInfoType, String destroyType) {
		//�ַ��е�
		String distrbuting = ConstLifeCycle.LC_DISTRIBUTING.getName();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select DISTINCT f.DISINFOID,f.INFOCLASSID,f.DISINFONAME from DDM_RECDES_PAPERTASK t,DDM_RECDES_INFO f,DDM_DIS_TASKINFOLINK l ");
		sbSql.append(" where t.innerId = l.fromObjectId and t.classId = l.fromObjectClassId ");
		sbSql.append(" and l.toObjectId = f.innerId and l.toObjectClassId = f.classId ");
		sbSql.append(" and f.disMediaType = '0' and t.stateName =? ");
		//Ϊ��λ
		if (disInfoType.equals("0")) {
			sbSql.append(" and f.DISINFOTYPE='0'");
		}
		//��Ա
		if (disInfoType.equals("1")) {
			sbSql.append(" and f.DISINFOTYPE='1' ");
		}
		//������Ϣ����Ҫ���շ���>0��
		if("0".equals(destroyType)){
			sbSql.append(" and f.NEEDRECOVERNUM > 0");
		}
		//������Ϣ����Ҫ���ٷ���>0��
		if("1".equals(destroyType)){
			sbSql.append(" and f.NEEDDESTROYNUM > 0");
		}
		String sql = sbSql.toString();
		result = PersistHelper.getService().query(sql, distrbuting);

		return result;
	}
	
}