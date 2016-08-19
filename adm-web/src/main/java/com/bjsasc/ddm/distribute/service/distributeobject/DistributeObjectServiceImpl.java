package com.bjsasc.ddm.distribute.service.distributeobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.adm.active.model.ActiveBase;
import com.bjsasc.adm.active.model.activedocument.ActiveDocument;
import com.bjsasc.adm.active.model.activeorder.ActiveOrder;
import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.common.FileLoadUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.disinfoistrack.DisInfoIsTrack;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.model.recdespapertask.RecDesPaperTask;
import com.bjsasc.ddm.distribute.model.returnreason.ReturnReason;
import com.bjsasc.ddm.distribute.service.disinfoistrack.DisInfoIsTrackService;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService;
import com.bjsasc.platform.filecomponent.model.PtFileItemBean;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.approve.ApproveOrder;
import com.bjsasc.plm.core.attachment.AttachHelper;
import com.bjsasc.plm.core.attachment.FileHolder;
import com.bjsasc.plm.core.baseline.model.ManagedBaseline;
import com.bjsasc.plm.core.cad.CADDocument;
import com.bjsasc.plm.core.change.ECO;
import com.bjsasc.plm.core.change.TNO;
import com.bjsasc.plm.core.change.Variance;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.doc.Document;
import com.bjsasc.plm.core.doc.DocumentMaster;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.option.OptionHelper;
import com.bjsasc.plm.core.option.OptionValue;
import com.bjsasc.plm.core.part.Part;
import com.bjsasc.plm.core.part.PartHelper;
import com.bjsasc.plm.core.part.PartMaster;
import com.bjsasc.plm.core.part.link.PartDecribeLink;
import com.bjsasc.plm.core.part.link.PartUsageLink;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.suit.ATSuit;
import com.bjsasc.plm.core.type.ATLink;
import com.bjsasc.plm.core.util.DateTimeUtil;
import com.bjsasc.plm.core.vc.VersionControlHelper;
import com.bjsasc.plm.core.vc.model.Iterated;
import com.bjsasc.plm.core.vc.model.Mastered;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.cascc.avidm.util.SplitString;

/**
 * 分发对象服务实现类。
 * 
 * @author gengancong 2013-2-22
 */
@SuppressWarnings({ "deprecation", "unchecked" })
public class DistributeObjectServiceImpl implements DistributeObjectService {

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService#getDistributeObjectsByDistributeOrderInnerId(java.lang.String)
	 */
	@Override
	public List<DistributeObject> getDistributeObjectsByDistributeOrderOid(String distributeOrderOid) {
		String innerId = Helper.getInnerId(distributeOrderOid);
		String classId = Helper.getClassId(distributeOrderOid);
		String hql = "from DistributeOrderObjectLink t where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? order by t.isMaster desc";

		List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, innerId, classId);

		List<DistributeObject> list = new ArrayList<DistributeObject>();

		if (links != null && links.size() > 0) {
			for (DistributeOrderObjectLink link : links) {
				DistributeObject distributeObject = (DistributeObject) link.getTo();
				if (distributeObject != null) {
					distributeObject.setDistributeOrderObjectLink(link);
					list.add(distributeObject);
				}
			}
		}
		return list;
	}

	@Override
	public List<DistributeObject> getDistributeObjectsByDistributeElecTaskOid(String distributeElecTaskOid) {
		String sql = "select link.* from ((select DISTINCT l.* from DDM_DIS_ELECTASK e, DDM_DIS_TASKDOMAINLINK t, DDM_DIS_INFO f, DDM_DIS_ORDEROBJLINK l"
				+ " where t.fromObjectClassId || ':' || t.fromObjectId = e.classId || ':' || e.innerId"
				+ " and t.toObjectClassId || ':' || t.toObjectId = f.classId || ':' || f.innerId"
				+ " and f.disOrderObjLinkClassId || ':' || f.disOrderObjLinkId = l.classId || ':' || l.innerId"
				+ " and e.classId || ':' || e.innerId = ?) union all ("
				+ "select DISTINCT l.* from DDM_DIS_ELECTASK e, DDM_DIS_TASKINFOLINK t, DDM_DIS_INFO f, DDM_DIS_ORDEROBJLINK l"
				+ " where t.fromObjectClassId || ':' || t.fromObjectId = e.classId || ':' || e.innerId"
				+ " and t.toObjectClassId || ':' || t.toObjectId = f.classId || ':' || f.innerId"
				+ " and f.disOrderObjLinkClassId || ':' || f.disOrderObjLinkId = l.classId || ':' || l.innerId"
				+ " and e.classId || ':' || e.innerId = ?)) link order by link.ismaster desc";

		List<DistributeOrderObjectLink> links = Helper.getPersistService().query(sql, DistributeOrderObjectLink.class,
				distributeElecTaskOid, distributeElecTaskOid);

		List<DistributeObject> list = new ArrayList<DistributeObject>();

		if (links != null && links.size() > 0) {
			for (DistributeOrderObjectLink link : links) {
				DistributeObject distributeObject = (DistributeObject) link.getTo();
				if (distributeObject != null) {
					distributeObject.setDistributeOrderObjectLink(link);
					list.add(distributeObject);
				}
			}
		}
		return list;
	}

	/**
	 * 根据纸质签收任务OID取得相关分发数据。
	 * @param distributeOrderOid String
	 * @return List
	 * @author zhangguoqiang 2014-09-11
	 */
	@Override
	public List<DistributeObject> getDistributeObjectsByDistributePaperSignTaskOid(String distributePaperSignTaskOid) {
		String sql = "select DISTINCT l.* from DDM_DIS_PAPERSIGNTASK e, DDM_DIS_TASKINFOLINK t, DDM_DIS_INFO f, DDM_DIS_ORDEROBJLINK l"
				+ " where t.fromObjectClassId || ':' || t.fromObjectId = e.classId || ':' || e.innerId"
				+ " and t.toObjectClassId || ':' || t.toObjectId = f.classId || ':' || f.innerId"
				+ " and f.disOrderObjLinkClassId || ':' || f.disOrderObjLinkId = l.classId || ':' || l.innerId"
				+ " and e.classId || ':' || e.innerId = ? order by l.ismaster desc";

		List<DistributeOrderObjectLink> links = Helper.getPersistService().query(sql, DistributeOrderObjectLink.class,
				distributePaperSignTaskOid);

		List<DistributeObject> list = new ArrayList<DistributeObject>();

		if (links != null && links.size() > 0) {
			for (DistributeOrderObjectLink link : links) {
				DistributeObject distributeObject = (DistributeObject) link.getTo();
				if (distributeObject != null) {
					distributeObject.setDistributeOrderObjectLink(link);
					list.add(distributeObject);
				}
			}
		}
		return list;
	}

	public List<DistributeObject> getDistributeObjectsByDistributePaper(String distributePaperOid) {
		String innerId = Helper.getInnerId(distributePaperOid);
		String classId = Helper.getClassId(distributePaperOid);
		String sql = "";
		if(classId.equals(DistributePaperTask.CLASSID)){
			sql = "SELECT distinct  C.* FROM DDM_DIS_INFO A, DDM_DIS_TASKINFOLINK B, DDM_DIS_ORDEROBJLINK C "
					+ "WHERE A.CLASSID = B.TOOBJECTCLASSID AND A.INNERID = B.TOOBJECTID "
					+ "AND A.DISORDEROBJLINKID = C.INNERID AND A.DISORDEROBJLINKCLASSID = C.CLASSID "
					+ " AND B.FROMOBJECTID = ? AND B.FROMOBJECTCLASSID = ?  ORDER BY C.ISMASTER DESC";
		}else if(classId.equals(RecDesPaperTask.CLASSID)){
			sql = "SELECT distinct  C.* "
					+ "FROM DDM_RECDES_INFO A, "
					+ "DDM_DIS_TASKINFOLINK B, "
					+ "DDM_DIS_ORDEROBJLINK C "
					+ "WHERE A.CLASSID = B.TOOBJECTCLASSID "
					+ "AND A.INNERID = B.TOOBJECTID "
					+ "AND A.DISORDEROBJECTLINKID = C.INNERID "
					+ "AND A.DISORDEROBJECTLINKCLASSID = C.CLASSID  "
					+ "AND B.FROMOBJECTID = ? "
					+ "AND B.FROMOBJECTCLASSID = ?  "
					+ "ORDER BY C.ISMASTER DESC";
		}
		List<DistributeOrderObjectLink> linkList = PersistHelper.getService().query(sql,
				DistributeOrderObjectLink.class, innerId, classId);
		List<DistributeObject> objList = new ArrayList<DistributeObject>();
		for (DistributeOrderObjectLink link : linkList) {
			DistributeObject disObject = (DistributeObject) link.getTo();
			disObject.setDistributeOrderObjectLink(link);
			objList.add(disObject);
		}
		return objList;
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService#getDistributeObjectsByDataOid(java.lang.String)
	 */
	@Override
	public List<DistributeObject> getDistributeObjectsByDataOid(String dataOid) {
		String sql = "SELECT * FROM DDM_DIS_OBJECT A WHERE A.DATACLASSID || ':' || A.DATAINNERID = ?";
		List<DistributeObject> list = PersistHelper.getService().query(sql, DistributeObject.class, dataOid);
		return list;
	}
	
	@Override
	public List<DistributeObject> getDistributeObjectsByDataIid(String dataIid) {
		String sql = "SELECT * FROM DDM_DIS_OBJECT A WHERE A.DATAINNERID = ?";
		List<DistributeObject> list = PersistHelper.getService().query(sql, DistributeObject.class, dataIid);
		return list;
	}
	
	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService#createDistributeObject(com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject)
	 */
	@Override
	public void createDistributeObject(DistributeObject disObject) {
		Helper.getPersistService().save(disObject);
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService#createDistributeOrderObjectLink(com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder,com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject,java.lang.String)
	 */
	@Override
	public String createDistributeOrderObjectLink(DistributeOrder disOrder, DistributeObject disObject, String isMaster) {
		DistributeOrderObjectLinkService service = DistributeHelper.getDistributeOrderObjectLinkService();
		DistributeOrderObjectLink linkDisObject = service.newDistributeOrderObjectLink();

		linkDisObject.setFromObject(disOrder);
		linkDisObject.setToObject(disObject);

		String dataInnerId = disObject.getDataInnerId();
		String dataClassId = disObject.getDataClassId();
		String dataOid = Helper.getOid(dataClassId, dataInnerId);
		Persistable dataObj = Helper.getPersistService().getObject(dataOid);
		// 是否在这个发放单内是父节点（0：不是，1：是）
		// 分发数据对象的数据源对象不是Part，都不是父节点，所以默认设置0
		if (dataObj instanceof Part) {
			linkDisObject.setIsParent("1");
		} else {
			linkDisObject.setIsParent("0");
		}

		linkDisObject.setDisDeadLine(System.currentTimeMillis() + 3 * 24 * 3600 * 1000);
		linkDisObject.setDisUrgent("0");
		linkDisObject.setDisStyle("0");

		// 不是从发放单开始建立分发数据对象的，都不是主对象，所以默认设置0
		// 是否在这个发放单内是主对象（0：不是，1：是
		linkDisObject.setIsMaster(isMaster);
		// 生命周期服务
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		// 生命周期测试
		life.initLifecycle(linkDisObject);

		service.createDistributeOrderObjectLink(linkDisObject);

		return linkDisObject.getClassId() + ":" + linkDisObject.getInnerId();
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService#updateDistributeObject(com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject)
	 */
	@Override
	public void updateDistributeObject(DistributeObject disObject) {
		Helper.getPersistService().update(disObject);
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService#updateDistributeObject(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateDistributeObject(String oid, String dataInnerId, String dataClassId, String dataFrom) {
		Persistable obj = Helper.getPersistService().getObject(oid);
		DistributeObject disObject = (DistributeObject) obj;

		disObject.setDataInnerId(dataInnerId);
		disObject.setDataClassId(dataClassId);
		disObject.setDataFrom(dataFrom);
		disObject.setNumber("aaaaa");
		disObject.setName("bbbbb");
		disObject.setNote("ccccc");

		updateDistributeObject(disObject);
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService#newDistributeObject()
	 */
	@Override
	public DistributeObject newDistributeObject() {
		DistributeObject disObject = (DistributeObject) PersistUtil.createObject(DistributeObject.CLASSID);
		disObject.setClassId(DistributeObject.CLASSID);
		return disObject;
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService#deleteDistributeObject(com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject)
	 */
	@Override
	public void deleteDistributeObject(DistributeObject disObject) {
		Helper.getPersistService().delete(disObject);
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService#deleteDistributeObjectByOids(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteDistributeObjectByOids(String distributeOrderOid, String oids) {

		List<String> oidList = SplitString.string2List(oids, ",");

		for (String oid : oidList) {
			deleteDistributeObjectByOid(distributeOrderOid, oid);
		}
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService#deleteDistributeObjectByOid(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteDistributeObjectByOid(String distributeOrderOid, String distributeObjectLinkOid) {

		String distributeOrderInnerId = Helper.getPersistService().getInnerId(distributeOrderOid);
		String distributeOrderClassId = Helper.getPersistService().getClassId(distributeOrderOid);

		Persistable object = Helper.getPersistService().getObject(distributeObjectLinkOid);
		DistributeOrderObjectLink disObj = (DistributeOrderObjectLink) object;
		DistributeObject disObject = (DistributeObject) disObj.getTo();
		String distributeObjectInnerId = disObject.getInnerId();
		String distributeObjectClassId = disObject.getClassId();

		String hql = "from DistributeOrderObjectLink t "
				+ "where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? "
				+ "  and t.toObjectRef.innerId=?   and t.toObjectRef.classId=? ";

		List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, distributeOrderInnerId,
				distributeOrderClassId, distributeObjectInnerId, distributeObjectClassId);

		List<DistributeInfo> distributeInfolist = new ArrayList<DistributeInfo>();
		hql = "from DistributeInfo t where t.disOrderObjLinkId=? and t.disOrderObjLinkClassId=?";
		List<DisInfoIsTrack> disInfoIsTracklist = new ArrayList<DisInfoIsTrack>();
		DisInfoIsTrackService service = DistributeHelper.getDisInfoIsTrackService();
		if (links != null && links.size() > 0) {
			for (DistributeOrderObjectLink link : links) {
				String innerId = link.getInnerId();
				String classId = link.getClassId();
				List<DistributeInfo> infos = PersistHelper.getService().find(hql, innerId, classId);
				distributeInfolist.addAll(infos);
				for (DistributeInfo disInfo : infos) {
					List<DisInfoIsTrack> infoIsTrackList = service.getDisInfoIsTrackByDisInfoOid(disInfo.getOid());
					disInfoIsTracklist.addAll(infoIsTrackList);
				}
			}
		}

		hql = "from DistributeOrderObjectLink t " + "where t.fromObjectRef.innerId!=? and t.fromObjectRef.classId=? "
				+ "  and t.toObjectRef.innerId=?   and t.toObjectRef.classId=? ";

		List<DistributeOrderObjectLink> otherLinks = PersistHelper.getService().find(hql, distributeOrderInnerId,
				distributeOrderClassId, distributeObjectInnerId, distributeObjectClassId);
		//判断该分发数据是否存在于其他发放单中
		if (otherLinks == null || otherLinks.isEmpty()) {
			Helper.getPersistService().delete(disObject);
		}

		Helper.getPersistService().delete(links);
		Helper.getPersistService().delete(distributeInfolist);
		Helper.getPersistService().delete(disInfoIsTracklist);
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService#isTransferControlling(java.lang.String)
	 */
	@Override
	public String isTransferControlling(String objOid) {
		Persistable dataObj = Helper.getPersistService().getObject(objOid);

		List<String> transferObjString = SplitString.string2List(FileLoadUtil.transfertype, ",");
		// 通过模型或子模型ID取得根模型ID
		String hardClassId = Helper.getTypeService().getTargetClassId(dataObj.getClassId());
		// 是否可转换
		if (!transferObjString.contains(hardClassId)) {
			String message = ConstUtil.IS_TRANSEFER_NG;
			return "false," + message;
		}

		// 是否有生命周期
		if (!(dataObj instanceof LifeCycleManaged)) {
			String message = ConstUtil.IS_CONTROLING_NG_2;
			return "false," + message;
		}

		// 是否满足首选项配置
		LifeCycleManaged workable = (LifeCycleManaged) dataObj;
		String dataState = workable.getLifeCycleInfo().getStateName();
		String optionValue = ConstUtil.LC_CONTROL.getName();
		if (dataObj instanceof Document) {//文档
			Document obj = (Document) dataObj;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowDocState");
			optionValue = value.getValue();
		} else if (dataObj instanceof CADDocument) {//CAD文档
			CADDocument obj = (CADDocument) dataObj;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowCADDocState");
			optionValue = value.getValue();
		} else if (dataObj instanceof Part) {//部件
			Part obj = (Part) dataObj;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowPartState");
			optionValue = value.getValue();
		} else if (dataObj instanceof ECO) {//更改单
			ECO obj = (ECO) dataObj;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowECOState");
			optionValue = value.getValue();
		} else if (dataObj instanceof TNO) {//技术通知单
			TNO obj = (TNO) dataObj;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowTNOState");
			optionValue = value.getValue();
		} else if (dataObj instanceof Variance) {//超差代料质疑单
			Variance obj = (Variance) dataObj;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowVarianceState");
			optionValue = value.getValue();
		} else if (dataObj instanceof ApproveOrder) {//送审单
			ApproveOrder obj = (ApproveOrder) dataObj;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowApproveOrderState");
			optionValue = value.getValue();
		} else if (dataObj instanceof ActiveDocument) {//现行文件
			ActiveDocument obj = (ActiveDocument) dataObj;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowActiveDocumentState");
			optionValue = value.getValue();
		} else if (dataObj instanceof ActiveOrder) {//现行单据
			ActiveOrder obj = (ActiveOrder) dataObj;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowActiveOrderState");
			optionValue = value.getValue();
		} else if (dataObj instanceof ManagedBaseline) {//基线
			ManagedBaseline obj = (ManagedBaseline) dataObj;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowBaseLineState");
			optionValue = value.getValue();
		} else if (dataObj instanceof ATSuit) {//套对象
			ATSuit obj = (ATSuit) dataObj;
			Context context = obj.getContextInfo().getContext();
			OptionValue value = OptionHelper.getService().getOptionValue(context, "disMange_disObjState_allowSuitState");
			optionValue = value.getValue();
		}
		
		if (optionValue.contains(dataState) || "all".equals(optionValue)) {
			return "true,yes";
		} else {
			String message = ConstUtil.IS_CONTROLING_NG;
			message = message.replace("state", dataState);
			return "false," + message;
		}
	}

	public String deleteDistributeOrder(String distributeOrderOid) {
		String flag = "";
		List<DistributeObject> list = getDistributeObjectsByDistributeOrderOid(distributeOrderOid);
		if (list.isEmpty()) {
			Persistable obj = Helper.getPersistService().getObject(distributeOrderOid);
			DistributeOrder dis = (DistributeOrder) obj;
			Helper.getPersistService().delete(dis);
			flag = "true";
		}
		return flag;
	}

	public void updateDistributeObjectLink(String linkOids, String disUrgent) {
		List<String> oidList = SplitString.string2List(linkOids, ",");
		List<DistributeOrderObjectLink> linkList = new ArrayList<DistributeOrderObjectLink>();
		for (String linkOid : oidList) {
			Persistable obj = Helper.getPersistService().getObject(linkOid);
			DistributeOrderObjectLink dis = (DistributeOrderObjectLink) obj;
			dis.setDisUrgent(disUrgent);
			linkList.add(dis);
		}
		if (linkList != null && linkList.size() > 0) {
			Helper.getPersistService().update(linkList);
		}
	}

	public void updateDistributedeadLinkDate(String linkOids, String deadLineDate) {
		List<String> oidList = SplitString.string2List(linkOids, ",");
		List<DistributeOrderObjectLink> linkList = new ArrayList<DistributeOrderObjectLink>();
		long deadLineLongTime = DateTimeUtil.getLongTime(deadLineDate);
		for (String linkOid : oidList) {
			Persistable obj = Helper.getPersistService().getObject(linkOid);
			DistributeOrderObjectLink dis = (DistributeOrderObjectLink) obj;
			dis.setDisDeadLine(deadLineLongTime);
			linkList.add(dis);
		}
		if (linkList != null && linkList.size() > 0) {
			Helper.getPersistService().update(linkList);
		}

	}

	public List<DistributeObject> getDistributeObjectReturnDetail(String objOid, String taskOid) {
		String sqlObj = "SELECT * FROM DDM_DIS_ORDEROBJLINK WHERE TOOBJECTCLASSID || ':' || TOOBJECTID = ?";
		String sqlReturn = "SELECT * FROM DDM_DIS_RETURN WHERE TASKCLASSID || ':' || TASKID = ? AND OBJECTCLASSID || ':' || OBJECTID = ? order by UPDATETIME DESC";
		List<DistributeOrderObjectLink> objList = Helper.getPersistService().query(sqlObj,
				DistributeOrderObjectLink.class, objOid);
		DistributeObject object = (DistributeObject) objList.get(0).getTo();
		List<ReturnReason> retList = Helper.getPersistService().query(sqlReturn, ReturnReason.class, taskOid, objOid);
		List<DistributeObject> objectList = new ArrayList<DistributeObject>();
		for (ReturnReason ret : retList) {
			DistributeObject obj = object.cloneDisObj();
			obj.setReturnReason(ret);
			objectList.add(obj);
		}
		return objectList;
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService#getDistributeObjectsByOrdOidAndyDataOid(java.lang.String,java.lang.String)
	 */
	@Override
	public List<DistributeObject> getDistributeObjectsByOrdOidAndDataOid(String ordOid, String dataOid) {
		String sql = "SELECT DISTINCT disObj.* FROM DDM_DIS_ORDER disOrd, DDM_DIS_OBJECT disObj, DDM_DIS_ORDEROBJLINK disLink"
				+ " WHERE disOrd.CLASSID || ':' || disOrd.INNERID = ? "
				+ " AND disLink.FROMOBJECTID = disOrd.INNERID "
				+ " AND disLink.FROMOBJECTCLASSID = disOrd.CLASSID "
				+ " AND disLink.TOOBJECTID = disObj.INNERID "
				+ " AND disLink.TOOBJECTCLASSID = disObj.CLASSID "
				+ " AND disObj.DATACLASSID || ':' || disObj.DATAINNERID = ?";
		List<DistributeObject> list = PersistHelper.getService().query(sql, DistributeObject.class, ordOid, dataOid);
		return list;
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService#getMasterLink(java.lang.String)
	 */
	@Override
	public boolean getMasterLink(String disOrdOid) {
		// 发放单对象
		String sql = "SELECT DISTINCT disLink.* FROM DDM_DIS_ORDER disOrd, DDM_DIS_ORDEROBJLINK disLink"
				+ " WHERE disOrd.CLASSID || ':' || disOrd.INNERID = ? " + " AND disLink.FROMOBJECTID = disOrd.INNERID "
				+ " AND disLink.FROMOBJECTCLASSID = disOrd.CLASSID " + " AND disLink.ISMASTER='1' ";
		List<DistributeOrderObjectLink> resultList = Helper.getPersistService().query(sql,
				DistributeOrderObjectLink.class, disOrdOid);
		if (resultList == null || resultList.isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public void updateDistributeObjectLinkByDisStyle(String linkOids, String disStyle) {
		List<String> oidList = SplitString.string2List(linkOids, ",");
		List<DistributeOrderObjectLink> linkList = new ArrayList<DistributeOrderObjectLink>();
		for (String linkOid : oidList) {
			Persistable obj = Helper.getPersistService().getObject(linkOid);
			DistributeOrderObjectLink dis = (DistributeOrderObjectLink) obj;
			dis.setDisStyle(disStyle);
			linkList.add(dis);
		}
		if (linkList != null && linkList.size() > 0) {
			Helper.getPersistService().update(linkList);
		}
	}

	@Override
	public List<Persistable> getActiveBaseDataSourceByDisOrdOid(String distributeOrderOid) {
		String sql = "SELECT OBJ.DATACLASSID || ':' || OBJ.DATAINNERID AS DATAOID FROM DDM_DIS_ORDEROBJLINK LINK, DDM_DIS_OBJECT OBJ "
				+ "WHERE LINK .FROMOBJECTCLASSID || ':' || LINK .FROMOBJECTID = ? "
				+ "AND LINK .TOOBJECTCLASSID || ':' || LINK .TOOBJECTID = OBJ.CLASSID || ':' || OBJ.INNERID";
		List<Map<String, Object>> list = Helper.getPersistService().query(sql, distributeOrderOid);
		List<Persistable> resultList = new ArrayList<Persistable>();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				Persistable dataObj = Helper.getPersistService().getObject((String) map.get("DATAOID"));
				if (dataObj instanceof ActiveBase) {
					resultList.add(dataObj);
				}
			}
		}
		return resultList;
	}

	public List<Persistable> getAllElecTaskDataRootObject(String taskOid) {
		String sql = "select obj.* from DDM_DIS_INFO info, DDM_DIS_TASKINFOLINK infoLink, DDM_DIS_ORDEROBJLINK objLink, DDM_DIS_OBJECT obj"
				+ " where infoLink.toObjectClassId || ':' || infoLink.toObjectId = info.classId || ':' || info.innerId"
				+ " and info.disOrderObjLinkClassId || ':' || info.disOrderObjLinkId = objLink.classId || ':' || objLink.innerId"
				+ " and objLink.toObjectClassId || ':' || objLink.toObjectId = obj.classId || ':' || obj.innerId"
				+ " and infoLink.fromObjectClassId || ':' || infoLink.fromObjectId = ?";
		List<DistributeObject> objList = Helper.getPersistService().query(sql, DistributeObject.class, taskOid);
		List<Persistable> list = new ArrayList<Persistable>();
		for (DistributeObject obj : objList) {
			Persistable object = Helper.getPersistService().getObject(obj.getDataClassId(), obj.getDataInnerId());
			//getLastestPartUsageLinksByTo
			if (object instanceof Part) {
				Part objPart = (Part) object;
				PartMaster master = (PartMaster) objPart.getMasterRef().getObject();
				List<PartUsageLink> listUsageLink = PartHelper.getService().getLastestPartUsageLinksByTo(master);
				if (listUsageLink.size() == 0) {
					list.add(objPart);
				}
			}
			if (object instanceof Document) {
				Document objDoc = (Document) object;
				List<PartDecribeLink> listDecLink = PartHelper.getService().getLastestPartDecribeLinkByTo(objDoc);
				if (listDecLink.size() == 0) {
					list.add(objDoc);
				}
			}
		}
		return list;
	}

	public List<Persistable> getAllElecTaskDataObject(String taskOid) {
		String sql = "select obj.* from DDM_DIS_INFO info, DDM_DIS_TASKINFOLINK infoLink, DDM_DIS_ORDEROBJLINK objLink, DDM_DIS_OBJECT obj"
				+ " where infoLink.toObjectClassId || ':' || infoLink.toObjectId = info.classId || ':' || info.innerId"
				+ " and info.disOrderObjLinkClassId || ':' || info.disOrderObjLinkId = objLink.classId || ':' || objLink.innerId"
				+ " and objLink.toObjectClassId || ':' || objLink.toObjectId = obj.classId || ':' || obj.innerId"
				+ " and infoLink.fromObjectClassId || ':' || infoLink.fromObjectId = ?";
		List<DistributeObject> objList = Helper.getPersistService().query(sql, DistributeObject.class, taskOid);
		List<Persistable> list = new ArrayList<Persistable>();
		for (DistributeObject obj : objList) {
			Persistable object = Helper.getPersistService().getObject(obj.getDataClassId(), obj.getDataInnerId());
			if (object instanceof Part || object instanceof Document) {
				list.add(object);
			}
		}
		return list;
	}

	public void addDistributeDataObjectLink(String partOid, String objectOids) {
		List<String> objOidList = SplitString.string2List(objectOids, ",");
		Part part = (Part) Helper.getPersistService().getObject(partOid);
		List<Object> objList = new ArrayList<Object>();
		for (String objOid : objOidList) {
			Persistable obj = Helper.getPersistService().getObject(objOid);
			if (obj instanceof Document) {
				PartDecribeLink workinglink = PartHelper.getService().newPartDecribeLink(part, (Document) obj);
				objList.add(workinglink);
			} else if (obj instanceof Part) {
				Part objPart = (Part) obj;
				PartMaster master = (PartMaster) objPart.getMasterRef().getObject();
				PartUsageLink link = PartHelper.getService().newPartUsageLink(part, master);
				objList.add(link);
			}
		}
		if (objList.size() > 0) {
			Helper.getPersistService().save(objList);
		}
	}
	
	public List<DistributeObject> getDistributeObjectList(DistributeOrder order){
		String sql = "SELECT T.* FROM DDM_DIS_ORDEROBJLINK L,DDM_DIS_OBJECT T"
				+ " WHERE L.TOOBJECTCLASSID || ':' || L.TOOBJECTID = T.CLASSID || ':' || T.INNERID"
				+ " AND L.FROMOBJECTCLASSID || ':' || L.FROMOBJECTID = ?";
		List<DistributeObject> objList = Helper.getPersistService().query(sql, DistributeObject.class, order.getOid());
		
		return objList;
	}
	
	public List<DistributeOrderObjectLink> historyDistributeObjectList(String dataoid){
		
		String sql = "select LK.CLASSID|| ':' ||  LK.INNERID AS LKOID,  OBJ.NAME AS OBJNAME,ODE.NAME AS ORDERNAME  FROM DDM_DIS_OBJECT  OBJ ,DDM_DIS_ORDEROBJLINK  LK ,DDM_DIS_ORDER  ODE WHERE LK.FROMOBJECTID=ODE.INNERID AND LK.FROMOBJECTCLASSID=ODE.CLASSID AND LK.TOOBJECTID=OBJ.INNERID AND LK.TOOBJECTCLASSID=OBJ.CLASSID AND OBJ.DATACLASSID || ':' || OBJ.DATAINNERID=?  ORDER BY ODE.UPDATETIME DESC";
		
		List<Map<String, Object>> list = Helper.getPersistService().query(sql, dataoid);
		List<DistributeOrderObjectLink> linkList = new ArrayList<DistributeOrderObjectLink>();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				
				Persistable dataObj = Helper.getPersistService().getObject((String) map.get("LKOID"));
				DistributeOrderObjectLink dis = (DistributeOrderObjectLink) dataObj;
				dis.setDisUrgent((String)map.get("OBJNAME"));
				dis.setDisStyle((String)map.get("ORDERNAME"));
				
				linkList.add(dis);
			}
		}
		return linkList;
		
		
	}
	
	public void getAllDistributeObject(String distributeOrderOid) {
		List<DistributeObject> listDis = getDistributeObjectsByDistributeOrderOid(distributeOrderOid);
		// 格式化显示数据
		GridDataUtil.prepareRowObjects(listDis, ConstUtil.SPOT_LISTDISTRIBUTEOBJECTS);

	}
	
	public void setMaster(String distributeOrderOid, String distributeObjectOid) {
		String orderId = Helper.getInnerId(distributeOrderOid);
		String orderCd = Helper.getClassId(distributeOrderOid);
		String objectId = Helper.getInnerId(distributeObjectOid);
		String objectCd = Helper.getClassId(distributeObjectOid);
		String sql = "update DDM_DIS_ORDEROBJLINK set ISMASTER='0' where FROMOBJECTID=? and FROMOBJECTCLASSID=? and ISMASTER='1'";
		Helper.getPersistService().bulkUpdateBySql(sql, orderId, orderCd);
		sql = "update DDM_DIS_ORDEROBJLINK set ISMASTER='1' where FROMOBJECTID=? and FROMOBJECTCLASSID=? and TOOBJECTID=? and TOOBJECTCLASSID=?";
		Helper.getPersistService().bulkUpdateBySql(sql, orderId, orderCd, objectId, objectCd);
		DistributeOrder disOrder = (DistributeOrder)PersistHelper.getService().getObject(orderCd, orderId);
		DistributeObject disObject = (DistributeObject)PersistHelper.getService().getObject(objectCd, objectId);
		disOrder.setMasterDataClassID(disObject.getDataClassId());
		PersistHelper.getService().update(disOrder);
	}
	
	/**
	 * 获取要发放的对象的版本号
	 * @return finalVersion
	 */
	public String getCurrentDisObjectVersion(String dataOid){
		
		Persistable persit = Helper.getPersistService().getObject(dataOid);
		String finalVersion="";
		//以下是为了获取补发单的下个个号
		if (persit instanceof Iterated) {
			Iterated it= (Iterated) persit;
			Mastered master= it.getMaster();
			if (master!=null) {
				//获取版本列表
				List<Iterated> itList= Helper.getVersionService().allIterationsOf(master);
				for (int i = 0; i < itList.size(); i++) {
					Iterated iter=itList.get(i);
					String version= iter.getIterationInfo().getFullVersionNo();
					if(i==0){
						finalVersion=version;
					}
				}
			} 
		}
		return finalVersion;
	}
	/**
	 * @author kangyanfei
	 */
	@Override
	public List<DistributeOrderObjectLink> historyDistributeObjectList(
			String oid, String orderType) {
		String sql = "select LK.CLASSID|| ':' ||  LK.INNERID AS LKOID,  OBJ.NAME AS OBJNAME,ODE.NAME AS ORDERNAME  FROM DDM_DIS_OBJECT  OBJ ,DDM_DIS_ORDEROBJLINK  LK ,DDM_DIS_ORDER  ODE WHERE LK.FROMOBJECTID=ODE.INNERID AND LK.FROMOBJECTCLASSID=ODE.CLASSID AND LK.TOOBJECTID=OBJ.INNERID AND LK.TOOBJECTCLASSID=OBJ.CLASSID AND OBJ.DATACLASSID || ':' || OBJ.DATAINNERID=? AND ODE.ORDERTYPE=? ORDER BY OBJ.UPDATETIME DESC";

		List<Map<String, Object>> list = Helper.getPersistService().query(sql, oid, orderType);
		List<DistributeOrderObjectLink> linkList = new ArrayList<DistributeOrderObjectLink>();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				
				Persistable dataObj = Helper.getPersistService().getObject((String) map.get("LKOID"));
				DistributeOrderObjectLink dis = (DistributeOrderObjectLink) dataObj;
				dis.setDisUrgent((String)map.get("OBJNAME"));
				dis.setDisStyle((String)map.get("ORDERNAME"));
				
				linkList.add(dis);
			}
		}
		return linkList;
		
	}

	@Override
	public Map<FileHolder, List<PtFileItemBean>> getDocumentAndFileByOrder(
			String oid) {
		Map<FileHolder, List<PtFileItemBean>> result = new HashMap<FileHolder, List<PtFileItemBean>>();
		List<DistributeObject> disObjList = getDistributeObjectsByDistributeOrderOid(oid);
		//循环disobj获取document,CADDocument
		for(DistributeObject disObj : disObjList){
			String dataClassId = disObj.getDataClassId();
			String dataInnerId = disObj.getDataInnerId();
			Persistable pt = PersistHelper.getService().getObject(dataClassId, dataInnerId);
			if(pt instanceof Document || pt instanceof CADDocument){
				List<PtFileItemBean> listBean = null;
				listBean = AttachHelper.getAttachService().getAllFile((FileHolder) pt);
				result.put((FileHolder)pt, listBean);
			}
		}
		return result;
	}

	@Override
	public List<DistributeObject> getDistributeObjByInfoOid(List<String> InfoOidList,String oid) {
		List<DistributeObject> objs = new ArrayList<DistributeObject>();
		String innerId = Helper.getInnerId(oid);
		if(InfoOidList!=null&&InfoOidList.size()>0){
			String str =SplitString.list2Str(InfoOidList, ",");
			String sql="select  t.* from ("
				 +" select k1.*"
					+" from (select i.*"
							+" from DDM_DIS_INFO i , DDM_DIS_ORDEROBJLINK k2 where k2.fromobjectid = ?"
							+ "and i.DISORDEROBJLINKID = k2.innerid and i.disinfoname in("+str+")"
						 +"  ) a, "
					+" DDM_DIS_ORDEROBJLINK k1"
					+" where a.DISORDEROBJLINKID = k1.innerid) b ,"
					+" DDM_DIS_OBJECT t where b.toobjectid=t.innerid ";
			objs = Helper.getPersistService().query(sql, DistributeObject.class,innerId);
		}
		return objs;
	}
	
	/* (non-Javadoc)
	 * @see com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService#getLastestPartLinkDocument(com.bjsasc.plm.core.part.Part, boolean)
	 */
	public List<Document> getLastestPartLinkDocument(Part part, boolean isDescripeDoc){
		List<Document> list = new ArrayList<Document>();
		List<? extends ATLink> links = null;
		if(isDescripeDoc){
			links = PartHelper.getService().getLastestPartDecribeLinkByFrom(part);
		}else{
			links = PartHelper.getService().getPartReferenceLinkByFrom(part);
		}
		
		if(links != null && links.size() > 0){
			for(ATLink link : links){
				Object obj = link.getToObject();
				Document doc = null;
				if(obj instanceof Document){
					doc = (Document) obj;
				}else if(obj instanceof DocumentMaster){
					doc = (Document) VersionControlHelper.getService().allIterationsOf((DocumentMaster) obj).get(0);
				}
				if(doc != null)
					list.add(doc);
			}
		}
		return list;
	}
	
}