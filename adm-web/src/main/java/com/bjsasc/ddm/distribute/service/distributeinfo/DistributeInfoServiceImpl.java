package com.bjsasc.ddm.distribute.service.distributeinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.model.ActiveOrdered;
import com.bjsasc.adm.active.model.activedocument.ActiveDocument;
import com.bjsasc.adm.active.model.activeorder.ActiveOrder;
import com.bjsasc.adm.active.model.activeset.ActiveSet;
import com.bjsasc.avidm.core.site.Site;
import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.common.ConstUtil.DISINFOTYPE;
import com.bjsasc.ddm.common.ConstUtil.DISPLAY_TYPE;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.disinfoistrack.DisInfoIsTrack;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeinfoconfig.DistributeInfoConfig;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.recdesinfo.RecDesInfo;
import com.bjsasc.ddm.distribute.service.disinfoistrack.DisInfoIsTrackService;
import com.bjsasc.ddm.distribute.service.distributeinfoconfig.DistributeInfoConfigService;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService;
import com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.approve.ApproveOrder;
import com.bjsasc.plm.core.baseline.model.ManagedBaseline;
import com.bjsasc.plm.core.change.Change;
import com.bjsasc.plm.core.change.ChangeService;
import com.bjsasc.plm.core.change.Changed;
import com.bjsasc.plm.core.change.ECO;
import com.bjsasc.plm.core.change.TNO;
import com.bjsasc.plm.core.change.Variance;
import com.bjsasc.plm.core.change.link.ChangedLink;
import com.bjsasc.plm.core.context.util.ContextUtil;
import com.bjsasc.plm.core.doc.Document;
import com.bjsasc.plm.core.part.Part;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.suit.ATSuit;
import com.bjsasc.plm.core.suit.Suit;
import com.bjsasc.plm.core.system.principal.OrganizationHelper;
import com.bjsasc.plm.core.system.principal.Principal;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.system.principal.UserHelper;
import com.bjsasc.plm.core.system.securitylevel.ContextUserHelper;
import com.bjsasc.plm.core.system.securitylevel.SecurityLevel;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.core.vc.VersionControlHelper;
import com.bjsasc.plm.core.vc.model.Versioned;
import com.cascc.avidm.util.SplitString;
import com.cascc.platform.aa.AAProvider;
import com.cascc.platform.aa.api.data.AADivisionData;
import com.cascc.platform.aa.api.data.AAUserData;

/**
 * 分发信息服务实现类。
 * 
 * @author gengancong 2013-2-22
 */
@SuppressWarnings({ "unchecked", "static-access" })
public class DistributeInfoServiceImpl implements DistributeInfoService {

	
	DistributeInfoConfigService infoConfigservice = DistributeHelper.getDistributeInfoConfigService();
	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService#getDistributeInfosByDistributeOrderInnerId(java.lang.String)
	 */
	@Override
	public List<DistributeInfo> getDistributeInfosByDistributeOrderInnerId(String innerId) {

		String hql = "from DistributeOrderObjectLink t where t.fromObjectRef.innerId=?";

		List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, innerId);

		List<DistributeInfo> list = new ArrayList<DistributeInfo>();

		hql = "from DistributeInfo t where t.disOrderObjLinkId=?";
		if (links != null && links.size() > 0) {
			for (DistributeOrderObjectLink link : links) {
				innerId = link.getInnerId();
				List<DistributeInfo> infos = PersistHelper.getService().find(hql, innerId);
				list.addAll(infos);
			}
		}
		return list;
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService#getDistributeInfosByOid(java.lang.String, java.util.List)
	 */
	@Override
	public List<DistributeInfo> getDistributeInfosByOid(String distributeOrderOid, List<String> linkOidList,
			DISPLAY_TYPE type) {
		List<DistributeInfo> list = new ArrayList<DistributeInfo>();
		Map<String, DistributeInfo> oidMap = new HashMap<String, DistributeInfo>();
		List<DistributeOrderObjectLink> linksAll = new ArrayList<DistributeOrderObjectLink>();

		if (linkOidList == null || linkOidList.isEmpty()) {
			String hql = "from DistributeOrderObjectLink t "
					+ "where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? ";
			String distributeOrderInnerId = Helper.getInnerId(distributeOrderOid);
			String distributeOrderClassId = Helper.getClassId(distributeOrderOid);

			List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, distributeOrderInnerId,
					distributeOrderClassId);
			linksAll.addAll(links);
		} else {
			String hql = "from DistributeOrderObjectLink t " + "where t.innerId=? and t.classId=? ";
			for (String linkOid : linkOidList) {
				String innerId = Helper.getInnerId(linkOid);
				String classId = Helper.getClassId(linkOid);

				List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, innerId, classId);
				linksAll.addAll(links);
			}
		}
		String hql = "from DistributeInfo t where t.disOrderObjLinkId=? and t.disOrderObjLinkClassId=?";
		if (linksAll != null && linksAll.size() > 0) {
			for (DistributeOrderObjectLink link : linksAll) {
				String innerId = link.getInnerId();
				String classId = link.getClassId();
				List<DistributeInfo> infos = PersistHelper.getService().find(hql, innerId, classId);
				for (DistributeInfo info : infos) {
					if (list.contains(info)) {
						oidMap.get(info.getKey()).addOid(info.getOid());
						continue;
					}
					info.addOid(info.getOid());
					oidMap.put(info.getKey(), info);
					info.setDisDeadLine(link.getDisDeadLine());
					info.setDisUrgent(link.getDisUrgent());
					info.setDisStyle(link.getDisStyle());
					list.add(info);
				}
			}
		}
		return list;
	}
	

	@Override
	public List<DistributeInfo> getDistributeInfosByOrderObjectLinkList(List<DistributeOrderObjectLink> listOrderObjectLink) {
		List<DistributeInfo> list = new ArrayList<DistributeInfo>();
		Map<String, DistributeInfo> oidMap = new HashMap<String, DistributeInfo>();
		
		String disInfohql = "from DistributeInfo t where t.disOrderObjLinkId=? and t.disOrderObjLinkClassId=?";
		if (listOrderObjectLink != null && listOrderObjectLink.size() > 0) {
			for (DistributeOrderObjectLink link : listOrderObjectLink) {
				String innerId = link.getInnerId();
				String classId = link.getClassId();
				List<DistributeInfo> infos = PersistHelper.getService().find(disInfohql, innerId, classId);
				for (DistributeInfo info : infos) {
					if (list.contains(info)) {
						oidMap.get(info.getKey()).addOid(info.getOid());
						continue;
					}
					info.addOid(info.getOid());
					oidMap.put(info.getKey(), info);
//					info.setDisDeadLine(link.getDisDeadLine());
//					info.setDisUrgent(link.getDisUrgent());
//					info.setDisStyle(link.getDisStyle());
					list.add(info);
				}
			}
		}
		return list;
	}

	/**
	 * 获取回收销毁单信息
	 * @param distributeOrderOid
	 * @param linkOidList
	 * @param type
	 * @return
	 */
	public List<RecDesInfo> getRecDesInfosByOid(String distributeOrderOid, List<String> linkOidList,
			DISPLAY_TYPE type) {
		List<RecDesInfo> list = new ArrayList<RecDesInfo>();
		Map<String, RecDesInfo> oidMap = new HashMap<String, RecDesInfo>();
		List<DistributeOrderObjectLink> linksAll = new ArrayList<DistributeOrderObjectLink>();

		if (linkOidList == null || linkOidList.isEmpty()) {
			String hql = "from DistributeOrderObjectLink t "
					+ "where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? ";
			String distributeOrderInnerId = Helper.getInnerId(distributeOrderOid);
			String distributeOrderClassId = Helper.getClassId(distributeOrderOid);

			List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, distributeOrderInnerId,
					distributeOrderClassId);
			linksAll.addAll(links);
		} else {
			String hql = "from DistributeOrderObjectLink t " + "where t.innerId=? and t.classId=? ";
			for (String linkOid : linkOidList) {
				String innerId = Helper.getInnerId(linkOid);
				String classId = Helper.getClassId(linkOid);

				List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, innerId, classId);
				linksAll.addAll(links);
			}
		}
		String hql = "from RecDesInfo t where t.disOrderObjectLinkId=? and t.disOrderObjectLinkClassId=?";
		if (linksAll != null && linksAll.size() > 0) {
			for (DistributeOrderObjectLink link : linksAll) {
				String innerId = link.getInnerId();
				String classId = link.getClassId();
				List<RecDesInfo> infos = PersistHelper.getService().find(hql, innerId, classId);
				for (RecDesInfo info : infos) {
					if (list.contains(info)) {
						oidMap.get(info.getKey()).addOid(info.getOid());
						continue;
					}
					info.addOid(info.getOid());
					oidMap.put(info.getKey(), info);
					info.setDisDeadLine(link.getDisDeadLine());
					info.setDisUrgent(link.getDisUrgent());
					//info.setDisStyle(link.getDisStyle());
					list.add(info);
				}
			}
		}
		return list;
	}
	
	@Override
	public List<DistributeInfo> getDistributeInfosByTaskOid(String distributeElecTaskOid, List<String> linkOidList,
			DISPLAY_TYPE type) {
		List<DistributeInfo> list = new ArrayList<DistributeInfo>();
		Map<String, DistributeInfo> oidMap = new HashMap<String, DistributeInfo>();
		List<DistributeOrderObjectLink> linksAll = new ArrayList<DistributeOrderObjectLink>();

		if (linkOidList == null || linkOidList.isEmpty()) {
			String sql = "(select l.* from DDM_DIS_ELECTASK e, DDM_DIS_TASKDOMAINLINK t, DDM_DIS_INFO f, DDM_DIS_ORDEROBJLINK l"
					+ " where t.fromObjectClassId || ':' || t.fromObjectId = e.classId || ':' || e.innerId"
					+ " and t.toObjectClassId || ':' || t.toObjectId = f.classId || ':' || f.innerId"
					+ " and f.disOrderObjLinkClassId || ':' || f.disOrderObjLinkId = l.classId || ':' || l.innerId"
					+ " and e.classId || ':' || e.innerId = ?) union all ("
					+ "select l.* from DDM_DIS_ELECTASK e, DDM_DIS_TASKINFOLINK t, DDM_DIS_INFO f, DDM_DIS_ORDEROBJLINK l"
					+ " where t.fromObjectClassId || ':' || t.fromObjectId = e.classId || ':' || e.innerId"
					+ " and t.toObjectClassId || ':' || t.toObjectId = f.classId || ':' || f.innerId"
					+ " and f.disOrderObjLinkClassId || ':' || f.disOrderObjLinkId = l.classId || ':' || l.innerId"
					+ " and e.classId || ':' || e.innerId = ?)";

			List<DistributeOrderObjectLink> links = Helper.getPersistService().query(sql,
					DistributeOrderObjectLink.class, distributeElecTaskOid, distributeElecTaskOid);
			linksAll.addAll(links);
		} else {
			String hql = "from DistributeOrderObjectLink t " + "where t.innerId=? and t.classId=? ";
			for (String linkOid : linkOidList) {
				String innerId = Helper.getInnerId(linkOid);
				String classId = Helper.getClassId(linkOid);

				List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, innerId, classId);
				linksAll.addAll(links);
			}
		}
		String sql = "(select F.* from DDM_DIS_INFO f,DDM_DIS_ELECTASK e, DDM_DIS_TASKINFOLINK t"
				+ " where t.fromObjectClassId || ':' || t.fromObjectId = e.classId || ':' || e.innerId"
				+ " and t.toObjectClassId || ':' || t.toObjectId = f.classId || ':' || f.innerId"
				+ " and f.disOrderObjLinkId = ? and f.disOrderObjLinkClassId = ?"
				+ " and e.classId || ':' || e.innerId = ?) union all ("
				+ "select F.* from DDM_DIS_INFO f,DDM_DIS_ELECTASK e, DDM_DIS_TASKDOMAINLINK t"
				+ " where t.fromObjectClassId || ':' || t.fromObjectId = e.classId || ':' || e.innerId"
				+ " and t.toObjectClassId || ':' || t.toObjectId = f.classId || ':' || f.innerId"
				+ " and f.disOrderObjLinkId = ? and f.disOrderObjLinkClassId = ?"
				+ " and e.classId || ':' || e.innerId = ?)";
		if (linksAll != null && linksAll.size() > 0) {
			for (DistributeOrderObjectLink link : linksAll) {
				String innerId = link.getInnerId();
				String classId = link.getClassId();
				List<DistributeInfo> infos = Helper.getPersistService().query(sql, DistributeInfo.class, innerId,
						classId, distributeElecTaskOid, innerId, classId, distributeElecTaskOid);
				for (DistributeInfo info : infos) {
					if (list.contains(info)) {
						oidMap.get(info.getKey()).addOid(info.getOid());
						continue;
					}
					info.addOid(info.getOid());
					oidMap.put(info.getKey(), info);
					info.setDisDeadLine(link.getDisDeadLine());
					info.setDisUrgent(link.getDisUrgent());
					info.setDisStyle(link.getDisStyle());
					list.add(info);
				}
			}
		}
		return list;
	}

	/**
	 * 通过纸质签收任务OID,分发数据OIDS获取分发信息数据。
	 * @param distributePaperSignTaskOid String
	 * @param distributeObjectOids List
	 * @param type DISPLAY_TYPE
	 * @return List
	 * @author zhangguoqiang 2014-09-11
	 */
	@Override
	public List<DistributeInfo> getDistributeInfosByPaperSignTaskOid(String distributePaperSignTaskOid, List<String> linkOidList,
			DISPLAY_TYPE type) {
		List<DistributeInfo> list = new ArrayList<DistributeInfo>();
		Map<String, DistributeInfo> oidMap = new HashMap<String, DistributeInfo>();
		List<DistributeOrderObjectLink> linksAll = new ArrayList<DistributeOrderObjectLink>();

		if (linkOidList == null || linkOidList.isEmpty()) {
			String sql ="select l.* from DDM_DIS_PAPERSIGNTASK e, DDM_DIS_TASKINFOLINK t, DDM_DIS_INFO f, DDM_DIS_ORDEROBJLINK l"
					+ " where t.fromObjectClassId || ':' || t.fromObjectId = e.classId || ':' || e.innerId"
					+ " and t.toObjectClassId || ':' || t.toObjectId = f.classId || ':' || f.innerId"
					+ " and f.disOrderObjLinkClassId || ':' || f.disOrderObjLinkId = l.classId || ':' || l.innerId"
					+ " and e.classId || ':' || e.innerId = ?";

			List<DistributeOrderObjectLink> links = Helper.getPersistService().query(sql,
					DistributeOrderObjectLink.class, distributePaperSignTaskOid);
			linksAll.addAll(links);
		} else {
			String hql = "from DistributeOrderObjectLink t " + "where t.innerId=? and t.classId=? ";
			for (String linkOid : linkOidList) {
				String innerId = Helper.getInnerId(linkOid);
				String classId = Helper.getClassId(linkOid);

				List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, innerId, classId);
				linksAll.addAll(links);
			}
		}
		String sql = "select F.* from DDM_DIS_INFO f,DDM_DIS_PAPERSIGNTASK e, DDM_DIS_TASKINFOLINK t"
				+ " where t.fromObjectClassId || ':' || t.fromObjectId = e.classId || ':' || e.innerId"
				+ " and t.toObjectClassId || ':' || t.toObjectId = f.classId || ':' || f.innerId"
				+ " and f.disOrderObjLinkId = ? and f.disOrderObjLinkClassId = ?"
				+ " and e.classId || ':' || e.innerId = ?";
		if (linksAll != null && linksAll.size() > 0) {
			for (DistributeOrderObjectLink link : linksAll) {
				String innerId = link.getInnerId();
				String classId = link.getClassId();
				List<DistributeInfo> infos = Helper.getPersistService().query(sql, DistributeInfo.class, innerId,
						classId, distributePaperSignTaskOid);
				for (DistributeInfo info : infos) {
					if (list.contains(info)) {
						oidMap.get(info.getKey()).addOid(info.getOid());
						continue;
					}
					info.addOid(info.getOid());
					oidMap.put(info.getKey(), info);
					info.setDisDeadLine(link.getDisDeadLine());
					info.setDisUrgent(link.getDisUrgent());
					info.setDisStyle(link.getDisStyle());
					list.add(info);
				}
			}
		}
		return list;
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService#getDistributeInfosByOid(java.lang.String, java.lang.String)
	 */
	@Override
	public List<DistributeInfo> getDistributeInfosByOid(String distributeOrderOid, String distributeObjectOid) {

		String distributeOrderInnerId = Helper.getInnerId(distributeOrderOid);
		String distributeOrderClassId = Helper.getClassId(distributeOrderOid);
		String distributeObjectInnerId = Helper.getInnerId(distributeObjectOid);
		String distributeObjectClassId = Helper.getClassId(distributeObjectOid);

		String hql = "from DistributeOrderObjectLink t "
				+ "where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? "
				+ "  and t.toObjectRef.innerId=?   and t.toObjectRef.classId=? ";

		List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, distributeOrderInnerId,
				distributeOrderClassId, distributeObjectInnerId, distributeObjectClassId);

		List<DistributeInfo> list = new ArrayList<DistributeInfo>();

		hql = "from DistributeInfo t where t.disOrderObjLinkId=? and t.disOrderObjLinkClassId=?";
		if (links != null && links.size() > 0) {
			for (DistributeOrderObjectLink link : links) {
				String innerId = link.getInnerId();
				String classId = link.getClassId();
				List<DistributeInfo> infos = PersistHelper.getService().find(hql, innerId, classId);
				list.addAll(infos);
			}
		}
		return list;
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService#updateDistributeInfos(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateDistributeInfos(String oids, String disInfoNums, String notes, String dismediatypes) {
		List<String> oidList = SplitString.string2List(oids, ";");
		List<String> disInfoNumList = SplitString.string2List(disInfoNums, ",");
		List<String> noteList = SplitString.string2List(notes, ",");
		List<String> dismediatypeList = SplitString.string2List(dismediatypes, ",");

		int length = oidList.size();
		List<DistributeInfo> disobjList = new ArrayList<DistributeInfo>();
		List<String> disinfoInnerIDList = new ArrayList<String>();
		for (int index = 0; index < length; index++) {
			String tmpOids = oidList.get(index);
			String disInfoNum = disInfoNumList.get(index);
			String note = noteList.get(index);
			String dismediatype = dismediatypeList.get(index);
			if ((ConstUtil.C_PAPERTASK).equals(dismediatype)) {
				dismediatype = ConstUtil.C_DISMEDIATYPE_0;
			} else if ((ConstUtil.C_ELECTASK).equals(dismediatype)) {
				dismediatype = ConstUtil.C_DISMEDIATYPE_1;
			} else if ((ConstUtil.C_OUTSITETASK).equals(dismediatype)) {
				dismediatype = ConstUtil.C_DISMEDIATYPE_2;
			}

			List<String> tmpOidList = SplitString.string2List(tmpOids, ",");

			for (int x = 0; x < tmpOidList.size(); x++) {
				String oid = tmpOidList.get(x);
				Persistable obj = Helper.getPersistService().getObject(oid);
				DistributeInfo disInfo = (DistributeInfo) obj;
				disInfo.setDisInfoNum(Long.parseLong(disInfoNum));
				if (StringUtil.isNull(note)) {
					note = "";
				}
				disInfo.setNote(note);
				disInfo.setDisMediaType(dismediatype);
				
				//判断修改后的分发信息，其发往单位/人员和分发类型和备注是否和此分发数据在此分发单内的已有的分发信息重复
				String sql = "";
				List<DistributeInfo> list = new ArrayList<DistributeInfo>();
				if(StringUtil.isNull(note)){
					sql = "select * from ddm_dis_info where disOrderObjLinkId=? and disOrderObjLinkClassId=? and disInfoId=? and infoClassId=? and disMediaType=? and note is null and innerid <> ? ";
					list = Helper.getPersistService().query(sql, DistributeInfo.class, disInfo.getDisOrderObjLinkId(), disInfo.getDisOrderObjLinkClassId(), disInfo.getDisInfoId(), disInfo.getInfoClassId(), dismediatype, disInfo.getInnerId());
				}else{
					sql = "select * from ddm_dis_info where disOrderObjLinkId=? and disOrderObjLinkClassId=? and disInfoId=? and infoClassId=? and disMediaType=? and note = ? and innerid <> ? ";
					list = Helper.getPersistService().query(sql, DistributeInfo.class, disInfo.getDisOrderObjLinkId(), disInfo.getDisOrderObjLinkClassId(), disInfo.getDisInfoId(), disInfo.getInfoClassId(), dismediatype, note, disInfo.getInnerId());
				}
				if(list.size()>0){//如有重复，曾更新旧有的分发信息的份数，并将原修改的分发信息对象删除
					DistributeInfo info = list.get(0);
					if(!info.getDisMediaType().equals(ConstUtil.C_DISMEDIATYPE_1) && !disinfoInnerIDList.contains(disInfo.getInnerId())){
						info.setDisInfoNum(info.getDisInfoNum()+Long.parseLong(disInfoNum));
						disobjList.add(info);
						disinfoInnerIDList.add(info.getInnerId());
					}
					Helper.getPersistService().delete(disInfo);
				}else{//如果不重复，则更新修改后的分发信息
					if(!disinfoInnerIDList.contains(disInfo.getInnerId())){
						disobjList.add(disInfo);
						disinfoInnerIDList.add(disInfo.getInnerId());
					}
				}
			}
		}
		if (disobjList != null && disobjList.size() > 0) {
			Helper.getPersistService().update(disobjList);
		}

	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService#createDistributeInfo(com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo)
	 */
	@Override
	public void createDistributeInfo(DistributeInfo disObject) {
		Helper.getPersistService().save(disObject);

	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService#createDistributeInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void createDistributeInfo(String type, String iids, String disMediaTypes, String disInfoNums, String notes,
			String distributeOrderObjectLinkOids, String deleterows) {
		List<String> iidList = SplitString.string2List(iids, ",");
		List<String> disMediaTypeList = SplitString.string2List(disMediaTypes, ",");
		List<String> disInfoNumList = SplitString.string2List(disInfoNums, ",");
		List<String> noteList = SplitString.string2List(notes, ",");
		List<String> distributeOrderObjectLinkOidList = SplitString.string2List(distributeOrderObjectLinkOids, ",");
		List<String> deleterowsList = new ArrayList<String>();
		if (!"null".equals(deleterows)) {
			deleterowsList = SplitString.string2List(deleterows, ",");
		}
		List<DistributeInfo> infoList = new ArrayList<DistributeInfo>();
		int length = iidList.size();
		for (int index = 0; index < length; index++) {
			Principal principal = null;
			int domainref=0;
			if (DISINFOTYPE.valueOf(type) == DISINFOTYPE.USER) {
				principal = UserHelper.getService().getUser(iidList.get(index));
			} else if (DISINFOTYPE.valueOf(type) == DISINFOTYPE.ORG) {
				principal = OrganizationHelper.getService().getOrganization(iidList.get(index));			
				AADivisionData ad=AAProvider.getDivisionService().getDivision(null, iidList.get(index));
				domainref=ad.getDomainRef();
			}
			int i = 0;
			for (String linkOid : distributeOrderObjectLinkOidList) {
				String innerId = Helper.getInnerId(linkOid);
				String classId = Helper.getClassId(linkOid);

				String sql = "select d.* from DDM_DIS_ORDEROBJLINK l,DDM_DIS_ORDER d where d.innerId = l.fromObjectId and d.classId = l.fromObjectClassId and l.classId || ':' || l.innerId = ?";
				//判断分发方式
				List<DistributeOrder> orderList = Helper.getPersistService().query(sql, DistributeOrder.class, linkOid);
				DistributeOrder order = orderList.get(0);
				String hql = "";
				String note ="";
				
				//notes不为空
				if (!StringUtil.isNull(notes)) {
					note=noteList.get(index);
				}
				List<DistributeInfo> list = new ArrayList<DistributeInfo>();
				if (StringUtil.isNull(note)) {
					if (deleterowsList.size() != 0 && deleterowsList.get(i).equals("true")){
						hql = "from DistributeInfo t where t.disOrderObjLinkId=? and t.disOrderObjLinkClassId=? and t.disInfoId=? and t.infoClassId=? and t.disMediaType=? and t.note = ? ";
						list = PersistHelper.getService().find(hql, innerId, classId, principal.getInnerId(),
								principal.getClassId(), disMediaTypeList.get(index), ConstUtil.REMOVE_FROM_SUIT);
						note = ConstUtil.REMOVE_FROM_SUIT;
					} else {
						hql = "from DistributeInfo t where t.disOrderObjLinkId=? and t.disOrderObjLinkClassId=? and t.disInfoId=? and t.infoClassId=? and t.disMediaType=? and t.note is null";
						list = PersistHelper.getService().find(hql, innerId, classId, principal.getInnerId(),
								principal.getClassId(), disMediaTypeList.get(index));
					}
				} else {
					hql = "from DistributeInfo t where t.disOrderObjLinkId=? and t.disOrderObjLinkClassId=? and t.disInfoId=? and t.infoClassId=? and t.disMediaType=? and t.note = ? ";
					list = PersistHelper.getService().find(hql, innerId, classId, principal.getInnerId(),
							principal.getClassId(), disMediaTypeList.get(index), note);
				}
				if ("1".equals(disMediaTypeList.get(index))) {
					String elecInfoSql = "select * from DDM_DIS_INFO where disMediaType = '1' and disInfoId = ? and infoClassId = ? and disOrderObjLinkId = ? and disOrderObjLinkClassId = ?";
					infoList = Helper.getPersistService().query(elecInfoSql, DistributeInfo.class,
							principal.getInnerId(), principal.getClassId(), innerId, classId);
				}
				if (list.size() == 0) {
					if (infoList.size() == 0) {
						DistributeInfo disObject = newDistributeInfo();
						if (DISINFOTYPE.valueOf(type) == DISINFOTYPE.USER) {
							// 分发信息类型（0为单位，1为人员）
							disObject.setDisInfoType(ConstUtil.C_DISINFOTYPE_USER);
						} else if (DISINFOTYPE.valueOf(type) == DISINFOTYPE.ORG) {
							// 分发信息类型（0为单位，1为人员）
							disObject.setDisInfoType(ConstUtil.C_DISINFOTYPE_ORG);
							//单位类型(0为内部单位,1为外部单位)
							if(domainref==ConstUtil.C_DOMAINREF){
								disObject.setSendType(ConstUtil.C_SENDTYPE_1);
							}else{
								disObject.setSendType(ConstUtil.C_SENDTYPE_0);
							}
						}
						// 分发信息名称（单位/人员）
						disObject.setDisInfoName(principal.getName());
						// 分发信息IID（人员或组织的内部标识）
						disObject.setDisInfoId(principal.getInnerId());
						// 分发信息的类标识（人员或者组织的类标识）
						disObject.setInfoClassId(principal.getClassId());
						// 分发信息变为已分发状态的时间
						disObject.setSendTime(0);
						// 分发份数
						disObject.setDisInfoNum(Long.valueOf(disInfoNumList.get(index)));
						// 分发介质类型（0为纸质，1为电子，2为跨域）
						disObject.setDisMediaType(disMediaTypeList.get(index));
						// 分发方式（0为直接分发，1为补发，2为移除，3为转发）
						disObject.setDisType(order.getOrderType());
						// 备注
						if (note != null && !"null".equals(note)) {
							disObject.setNote(note);
						}
						// 发放单与分发数据LINK内部标识
						disObject.setDisOrderObjLinkId(Helper.getPersistService().getInnerId(linkOid));
						// 发放单与分发数据LINK类标识
						disObject.setDisOrderObjLinkClassId(Helper.getPersistService().getClassId(linkOid));
						createDistributeInfo(disObject);

						//如果是更改单创建是否跟踪信息
						DistributeOrderObjectLink linkObj = (DistributeOrderObjectLink) Helper.getPersistService()
								.getObject(linkOid);
						DistributeObject distributeObject = (DistributeObject) linkObj.getTo();
						Persistable disObj = Helper.getPersistService().getObject(
								distributeObject.getDataClassId() + ":" + distributeObject.getDataInnerId());
						if (disObj instanceof ECO) {//更改单
							ECO ecoDis = (ECO) disObj;
							//创建是否跟踪信息
							DisInfoIsTrackService isTrackService = DistributeHelper.getDisInfoIsTrackService();
							isTrackService.createDisInfoIsTrack(disObject, ecoDis.getIsTrack());
						} else if (disObj instanceof TNO) {
							//技术通知单
							TNO tnoDis = (TNO) disObj;
							//创建是否跟踪信息
							DisInfoIsTrackService isTrackService = DistributeHelper.getDisInfoIsTrackService();
							isTrackService.createDisInfoIsTrack(disObject, tnoDis.getIsTrack());
						}

					}
				} else {
					DistributeInfo info = list.get(0);
					if ("0".equals(info.getDisMediaType())) {
						if(DISINFOTYPE.valueOf(type) == DISINFOTYPE.ORG){
						//单位类型(0为内部单位,1为外部单位)
						if(domainref==ConstUtil.C_DOMAINREF){
							info.setSendType(ConstUtil.C_SENDTYPE_1);
						}else{
							info.setSendType(ConstUtil.C_SENDTYPE_0);
						}
					}
						info.setDisInfoNum(info.getDisInfoNum() + Long.valueOf(disInfoNumList.get(index)));
						Helper.getPersistService().update(info);
					} else if ("1".equals(info.getDisMediaType())) {
						throw new RuntimeException("请不要给同一个分发数据添加相同的电子分发信息！");
					}
				}
				i++;
			}
		}
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService#deleteDistributeInfo(com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo)
	 */
	@Override
	public void deleteDistributeInfo(DistributeInfo disObject) {
		Helper.getPersistService().delete(disObject);
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService#deleteDistributeInfos(java.util.List)
	 */
	@Override
	public void deleteDistributeInfos(String oids) {
		List<Persistable> PerList = new ArrayList<Persistable>();
		List<DisInfoIsTrack> TrackList = new ArrayList<DisInfoIsTrack>();
		List<String> oidList = SplitString.string2List(oids, ",");
		for (String oid : oidList) {
			Persistable obj = Helper.getPersistService().getObject(oid);
			PerList.add(obj);
			//Helper.getPersistService().delete(obj);
			DistributeInfo disInfoObj = (DistributeInfo) obj;
			DisInfoIsTrackService service = DistributeHelper.getDisInfoIsTrackService();
			List<DisInfoIsTrack> disInfoIsTrackList = service.getDisInfoIsTrackByDisInfoOid(disInfoObj.getOid());
			if (disInfoIsTrackList.size() != 0) {
				//Helper.getPersistService().delete(disInfoIsTrackList.get(0));
				TrackList.add(disInfoIsTrackList.get(0));
			}
		}
		if (PerList != null && PerList.size() > 0) {
			Helper.getPersistService().delete(PerList);
		}
		if (TrackList != null && TrackList.size() > 0) {
			Helper.getPersistService().delete(TrackList);
		}
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService#newDistributeInfo()
	 */
	@Override
	public DistributeInfo newDistributeInfo() {
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		DistributeInfo dis = (DistributeInfo) PersistUtil.createObject(DistributeInfo.CLASSID);

		// 生命周期初始化
		life.initLifecycle(dis);

		dis.setClassId(dis.CLASSID);
		return dis;
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService#createAndCopyDistributeInfo(com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo)
	 */
	@Override
	public DistributeInfo createAndCopyDistributeInfo(DistributeInfo disInfoOld, String orderOid, String linkOid) {
		long disInfoNum = disInfoOld.getDisInfoNum();
		long destroyNum = disInfoOld.getDestroyNum();
		long num = disInfoNum - destroyNum;
		if (num <= 0) {
			return null;
		}
		//判断发放单中是否存在相同分发信息，若存在，只更新份数
		StringBuffer infoSql = new StringBuffer();
		infoSql.append("SELECT INFO.* FROM DDM_DIS_INFO INFO, DDM_DIS_ORDEROBJLINK OBJLINK"
				+ " WHERE INFO.DISORDEROBJLINKCLASSID || ':' || INFO.DISORDEROBJLINKID = OBJLINK.CLASSID || ':' || OBJLINK.INNERID"
				+ " AND OBJLINK.CLASSID || ':' || OBJLINK.INNERID = ?"
				+ " AND OBJLINK.FROMOBJECTCLASSID || ':' || OBJLINK.FROMOBJECTID = ?"
				+ " AND INFO.DISMEDIATYPE = ?"
				+ " AND INFO.DISINFOID = ?");
		if(!"".equals(disInfoOld.getNote()) && disInfoOld.getNote()!=null){
			infoSql.append(" AND INFO.NOTE = '" + disInfoOld.getNote() + "'");
		} else {
			infoSql.append(" AND INFO.NOTE IS NULL");
		}
		if (ConstUtil.C_TASKTYPE_OUTSIGN.equals(disInfoOld.getDisMediaType())){
			// 外域
			if(!"".equals(disInfoOld.getOutSignId()) && disInfoOld.getOutSignId() != null){
				infoSql.append(" AND INFO.OUTSIGNID = '" + disInfoOld.getOutSignId() + "'");
			} else {
				infoSql.append(" AND INFO.OUTSIGNID IS NULL");
			}
		}
		List<DistributeInfo> list = Helper.getPersistService().query(
				infoSql.toString(), DistributeInfo.class, linkOid, orderOid,
				disInfoOld.getDisMediaType(), disInfoOld.getDisInfoId());
		if(list.size() > 0){
			DistributeInfo info = list.get(0);
			if(ConstUtil.C_TASKTYPE_PAPER.equals(disInfoOld.getDisMediaType())){
				info.setDisInfoNum(info.getDisInfoNum() + disInfoOld.getDisInfoNum());
			}
			Helper.getPersistService().update(info);
			return null;
		} else {
			DistributeInfo disInfoNew = newDistributeInfo();
			disInfoNew.setDisInfoName(disInfoOld.getDisInfoName());
			disInfoNew.setDisInfoId(disInfoOld.getDisInfoId());
			disInfoNew.setInfoClassId(disInfoOld.getInfoClassId());
			disInfoNew.setDisInfoType(disInfoOld.getDisInfoType());
			disInfoNew.setSendType(disInfoOld.getSendType());
			disInfoNew.setRecoverNum(disInfoOld.getRecoverNum());
			disInfoNew.setDisMediaType(disInfoOld.getDisMediaType());
			disInfoNew.setDisType("0");
			disInfoNew.setNote(disInfoOld.getNote());
			disInfoNew.setDisInfoNum(num);
			disInfoNew.setDestroyNum(0);
			disInfoNew.setDisInfoName(disInfoOld.getDisInfoName());
			disInfoNew.setManageInfo(disInfoOld.getManageInfo());
			disInfoNew.setOutSignId(disInfoOld.getOutSignId());
			disInfoNew.setOutSignClassId(disInfoOld.getOutSignClassId());
			disInfoNew.setOutSignName(disInfoOld.getOutSignName());
			return disInfoNew;
		}
	}

	/* (non-Javadoc)
	 * @see com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService#createDefaultDistributeInfo(java.lang.String, java.util.List)
	 */
	@Override
	public void createDefaultDistributeInfo(String disOrdObjLinkOid,List<DistributeInfoConfig> infoConfigList) {
		// 默认分发信息配置
		if(infoConfigList == null || infoConfigList.size() == 0){
			infoConfigList.addAll(infoConfigservice.getAllDistributeInfoConfig());
		}
		if(null != infoConfigList && infoConfigList.size() > 0){
			for (int i = 0; i < infoConfigList.size(); i++) {
				DistributeInfoConfig infoConfig=infoConfigList.get(i);
				String disInfoType = infoConfig.getDisInfoType();
				if (ConstUtil.C_S_ZERO.equals(disInfoType)) {
					disInfoType = "ORG";
				} else {
					disInfoType = "USER";
				}
				// 分发信息IID
				String disInfoId = infoConfig.getDisInfoId();
				// 分发介质类型（0为纸质，1为电子，2为跨域）
				String disMediaType =infoConfig.getDisMediaType();
				// 分发份数
				String disInfoNum =String.valueOf(infoConfig.getDisInfoNum());
				// 备注
				String note = infoConfig.getNote();
				// 创建分发信息对象
				createDistributeInfo(disInfoType, disInfoId, disMediaType, disInfoNum, note, disOrdObjLinkOid, "null");
			}
		}
		
		//List<Map<String, String>> beanList = DdmSysConfigUtil.getDefaultDisInfo();
		/*for (Map<String, String> bean : beanList) {
			// 分发信息类型（0为单位，1为人员）
			String disInfoType = bean.get(DdmSysConfigConst.PARAM_DIS_INFO_TYPE);
			if (ConstUtil.C_S_ZERO.equals(disInfoType)) {
				disInfoType = "ORG";
			} else {
				disInfoType = "USER";
			}
			// 分发信息IID
			String disInfoId = bean.get(DdmSysConfigConst.PARAM_DIS_INFO_ID);
			// 分发介质类型（0为纸质，1为电子，2为跨域）
			String disMediaType = bean.get(DdmSysConfigConst.PARAM_DIS_INFO_MEDIA_TYPE);
			// 分发份数
			String disInfoNum = bean.get(DdmSysConfigConst.PARAM_DIS_INFO_NUM);
			// 备注
			String note = bean.get(DdmSysConfigConst.PARAM_DIS_INFO_NOTE);
			// 创建分发信息对象
			createDistributeInfo(disInfoType, disInfoId, disMediaType, disInfoNum, note, disOrdObjLinkOid, "null");
		}*/
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService#createInfo(java.lang.String,java.lang.List,boolean,boolean)
	 */
	@Override
	public void createInfo(String disOrdOid, List<Map<String, String>> linkList, int collectFlag,
			boolean addDefaultInfoFlag) {
		int linkListSize = linkList.size();
		List<DistributeInfoConfig> infoConfigList = new ArrayList<DistributeInfoConfig>();
		// 收集对象是更改单对象
		if (collectFlag == ConstUtil.COLLECTFLAG_1) {
			addECOInfo(disOrdOid, linkList);
			// 收集对象是现行单据对象
		} else if (collectFlag == ConstUtil.COLLECTFLAG_2) {
			addActiveOrderInfo(disOrdOid, linkList);
			// 收集对象是A版本以外的套对象
		} else if (collectFlag == ConstUtil.COLLECTFLAG_3) {
			addSuitInfo(disOrdOid, linkList);
			// 收集对象是更改单,现行单据以外的对象和A版本的套对象
		} else {
			// 追加默认分发信息
			if (addDefaultInfoFlag) {
				for (int i = 0; i < linkListSize; i++) {
					Map<String, String> map = linkList.get(i);
					String flag = map.get("FLAG");
					String linkOid = map.get("LINKOID");
					// 未发放过且需要追加默认分发数据
					if (ConstUtil.C_S_ZERO.equals(flag)) {
						// 默认分发信息创建与保存
						createDefaultDistributeInfo(linkOid,infoConfigList);
					}
				}
			}
		}
	}

	/**
	 * 更改单追加分发信息。
	 * 
	 * @param disOrdOid String
	 * @param linkList  List<Map<String, String>>
	 */
	private void addECOInfo(String disOrdOid, List<Map<String, String>> linkList) {
		// 分发对象服务
		DistributeObjectService objService = DistributeHelper.getDistributeObjectService();
		// 更改单对象
		Persistable dataObject = Helper.getPersistService().getObject(linkList.get(0).get("DATAOID"));
		ChangeService changeSerive = Helper.getChangeService();
		List<String> linkOidList = new ArrayList<String>();
		int linkListSize = linkList.size();

		List<DistributeInfoConfig> infoConfigList = new ArrayList<DistributeInfoConfig>();
		for (int i = 1; i < linkListSize; i++) {
			Map<String, String> map = linkList.get(i);
			String flag = map.get("FLAG");
			String linkOid = map.get("LINKOID");
			String dataOid = map.get("DATAOID");
			Persistable obj = Helper.getPersistService().getObject(dataOid);
			// 未发放过
			if (ConstUtil.C_S_ZERO.equals(flag)) {
				linkOidList.add(linkOid);
				// 更改前对象
				ChangedLink changeLink = changeSerive.getChangedByChangedAfter((Change) dataObject, obj);
				if (changeLink == null || changeLink.getFrom() == null) {
					// 默认分发信息创建与保存
					createDefaultDistributeInfo(linkOid,infoConfigList);
				} else {
					Persistable changed = changeLink.getFrom();
					boolean tempFlag = isOneDisStyle(Helper.getOid(changed));
					// 是否有一次性发放版本，有则不追加分发信息，没有则追加
					if (!tempFlag) {
						List<DistributeObject> disObjchangedList = objService.getDistributeObjectsByDataOid(Helper
								.getOid(changed));
						// 改前对象没有发放过，改后对象追加默认分发信息
						if (disObjchangedList == null || disObjchangedList.isEmpty()) {
							// 默认分发信息创建与保存
							createDefaultDistributeInfo(linkOid,infoConfigList);
							// 改前对象发放过，改后对象追加改前对象的分发信息
						} else {
							// 更改前对象的分发信息复制给更改后对象
							addRefChangeInfo(changed, linkOid, disOrdOid);
						}
					}
				}
			}
		}
		if (ConstUtil.C_S_ZERO.equals(linkList.get(0).get("FLAG"))) {
			List<DistributeInfo> infoList = getDistributeInfosByOid(disOrdOid, linkOidList, DISPLAY_TYPE.DISPLAY);
			String linkOid = linkList.get(0).get("LINKOID");
			String linkInnerId = Helper.getInnerId(linkOid);
			String linkClassId = Helper.getClassId(linkOid);
			for (DistributeInfo infoOld : infoList) {
				DistributeInfo disInfoNew = createAndCopyDistributeInfo(infoOld,disOrdOid,linkOid);
				if (disInfoNew != null) {
					// 重新设置发放单与分发数据link信息
					disInfoNew.setDisOrderObjLinkId(linkInnerId);
					disInfoNew.setDisOrderObjLinkClassId(linkClassId);
					// 保存分发信息
					createDistributeInfo(disInfoNew);
					//如果是更改单创建是否跟踪信息
					DistributeOrderObjectLink linkObj = (DistributeOrderObjectLink) Helper.getPersistService()
							.getObject(linkList.get(0).get("LINKOID"));
					DistributeObject distributeObject = (DistributeObject) linkObj.getTo();
					Persistable disObj = Helper.getPersistService().getObject(
							distributeObject.getDataClassId() + ":" + distributeObject.getDataInnerId());
					if (disObj instanceof ECO) {//更改单
						ECO ecoDis = (ECO) disObj;
						//创建是否跟踪信息
						DisInfoIsTrackService isTrackService = DistributeHelper.getDisInfoIsTrackService();
						isTrackService.createDisInfoIsTrack(disInfoNew, ecoDis.getIsTrack());
					}
				}
			}
		}
	}

	/**
	 * 更改前对象的分发信息复制给更改后对象。
	 * 
	 * @param dataObject Persistable
	 * @param disOrdObjLinkOid String
	 */
	private void addRefChangeInfo(Persistable dataObject, String disOrdObjLinkOid, String orderOid) {
		String dataInnerId = dataObject.getInnerId();
		String dataClassId = dataObject.getClassId();
		String linkInnerId = Helper.getInnerId(disOrdObjLinkOid);
		String linkClassId = Helper.getClassId(disOrdObjLinkOid);

		// 获取分发信息
		String sql = "SELECT Info.* FROM DDM_DIS_OBJECT Obj, DDM_DIS_ORDEROBJLINK ObjLink, DDM_DIS_INFO Info "
				+ "WHERE obj.DATAINNERID = ? " + " AND obj.DATACLASSID = ? "
				+ " AND Info.DISORDEROBJLINKID = ObjLink.INNERID "
				+ " AND Info.DISORDEROBJLINKCLASSID = ObjLink.CLASSID " + " AND ObjLink.TOOBJECTID = Obj.INNERID "
				+ " AND ObjLink.TOOBJECTCLASSID = Obj.CLASSID ORDER BY Info.MODIFYTIME ASC";
		List<DistributeInfo> disInfoList = Helper.getPersistService().query(sql, DistributeInfo.class, dataInnerId,
				dataClassId);

		for (DistributeInfo disInfoOld : disInfoList) {
			// 创建新的分发信息，并拷贝旧分发信息的相关数据
			DistributeInfo disInfoNew = createAndCopyDistributeInfo(disInfoOld, orderOid, disOrdObjLinkOid);
			if (disInfoNew != null) {
				// 重新设置发放单与分发数据link信息
				disInfoNew.setDisOrderObjLinkId(linkInnerId);
				disInfoNew.setDisOrderObjLinkClassId(linkClassId);
				if (disInfoNew != null && ConstUtil.DEFAULT_DIS_INFO.equals(disInfoNew.getNote())) {
					disInfoNew.setNote("");
				}
				// 保存分发信息
				createDistributeInfo(disInfoNew);
				//如果是更改单创建是否跟踪信息
				DistributeOrderObjectLink linkObj = (DistributeOrderObjectLink) Helper.getPersistService().getObject(
						disOrdObjLinkOid);
				DistributeObject distributeObject = (DistributeObject) linkObj.getTo();
				Persistable disObj = Helper.getPersistService().getObject(
						distributeObject.getDataClassId() + ":" + distributeObject.getDataInnerId());
				if (disObj instanceof ECO) {//更改单
					ECO ecoDis = (ECO) disObj;
					//创建是否跟踪信息
					DisInfoIsTrackService isTrackService = DistributeHelper.getDisInfoIsTrackService();
					isTrackService.createDisInfoIsTrack(disInfoNew, ecoDis.getIsTrack());
				}
			}
		}
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService#addDefaultInfo(com.bjsasc.plm.core.persist.Persistable)
	 */
	@Override
	public boolean addDefaultInfo(Persistable dataObject) {
		// 是否追加默认分发信息标识
		boolean addDefaultInfoFlag = true;
		//  送审单
		if (dataObject instanceof ApproveOrder) {
			addDefaultInfoFlag = true;
			// 更改单
		} else if (dataObject instanceof ECO) {
			addDefaultInfoFlag = true;
			// 部件
		} else if (dataObject instanceof Part) {
			addDefaultInfoFlag = true;
			// 文档
		} else if (dataObject instanceof Document) {
			addDefaultInfoFlag = true;
			// 管理基线
		} else if (dataObject instanceof ManagedBaseline) {
			addDefaultInfoFlag = true;
			// 技术通知单
		} else if (dataObject instanceof TNO) {
			addDefaultInfoFlag = true;
			//超差代料质疑单
		} else if (dataObject instanceof Variance) {
			addDefaultInfoFlag = true;
			//现行文件
		} else if (dataObject instanceof ActiveDocument) {
			addDefaultInfoFlag = true;
			//现行套
		} else if (dataObject instanceof ActiveSet) {
			addDefaultInfoFlag = true;
			//现行单据
		} else if (dataObject instanceof ActiveOrder) {
			addDefaultInfoFlag = true;
			// 套对象
		} else if (dataObject instanceof Suit) {
			addDefaultInfoFlag = true;
		}
		return addDefaultInfoFlag;
	}

	public List<DistributeInfo> getpapertaskinfo(String orderObjectLinkOids, String taskOid) {
		String innerId = Helper.getInnerId(taskOid);
		String classId = Helper.getClassId(taskOid);
		List<String> oidsList = SplitString.string2List(orderObjectLinkOids, ",");
		Map<String, DistributeInfo> oidMap = new HashMap<String, DistributeInfo>();
		List<DistributeOrderObjectLink> linksAll = new ArrayList<DistributeOrderObjectLink>();
		List<DistributeInfo> list = new ArrayList<DistributeInfo>();

		if (oidsList == null || oidsList.isEmpty()) {
			String sql = "SELECT distinct  C.* FROM DDM_DIS_INFO A, DDM_DIS_TASKINFOLINK B, DDM_DIS_ORDEROBJLINK C "
					+ "WHERE A.CLASSID = B.TOOBJECTCLASSID AND A.INNERID = B.TOOBJECTID "
					+ "AND A.DISORDEROBJLINKID = C.INNERID AND A.DISORDEROBJLINKCLASSID = C.CLASSID "
					+ " AND B.FROMOBJECTID = ? AND B.FROMOBJECTCLASSID = ? ";

			List<DistributeOrderObjectLink> linkList = PersistHelper.getService().query(sql,
					DistributeOrderObjectLink.class, innerId, classId);
			linksAll.addAll(linkList);
		} else {
			String hql = "from DistributeOrderObjectLink t " + "where t.innerId=? and t.classId=? ";
			for (String linkOid : oidsList) {
				String innerid = Helper.getPersistService().getInnerId(linkOid);
				String classid = Helper.getPersistService().getClassId(linkOid);

				List<DistributeOrderObjectLink> linkList = PersistHelper.getService().find(hql, innerid, classid);
				linksAll.addAll(linkList);

			}
		}
		String sqlinfo = "select t.* from  ddm_dis_info t , ddm_dis_taskinfolink b "
				+ "  where t.disorderobjlinkid=? and disorderobjlinkclassid=? "
				+ "  and b.toobjectid=t.innerid and b.toobjectclassid=t.classid "
				+ "    and b.fromobjectclassid='DistributePaperTask' ";
		if (linksAll != null && linksAll.size() > 0) {
			for (DistributeOrderObjectLink link : linksAll) {
				String objinner = link.getInnerId();
				String objclass = link.getClassId();
				List<DistributeInfo> infos = PersistHelper.getService().query(sqlinfo, DistributeInfo.class, objinner,
						objclass);
				for (DistributeInfo info : infos) {
					if (list.contains(info)) {
						oidMap.get(info.getKey()).addOid(info.getOid());
						continue;
					}
					info.addOid(info.getOid());
					oidMap.put(info.getKey(), info);
					info.setDisDeadLine(link.getDisDeadLine());
					info.setDisUrgent(link.getDisUrgent());
					info.setDisStyle(link.getDisStyle());
					list.add(info);
				}
			}
		}
		return list;

	}

	private boolean isOneDisStyle(String oid) {
		Persistable obj = Helper.getPersistService().getObject(oid);
		Versioned versioned = (Versioned) obj;
		List<Versioned> versionedList = VersionControlHelper.getService().allVersionsOf(versioned.getMaster());
		// 分发对象服务0
		DistributeOrderObjectLinkService ordObjLinkService = DistributeHelper.getDistributeOrderObjectLinkService();
		boolean isOneDisStyleFlag = false;
		for (Versioned temp : versionedList) {
			// 分发对象服务0
			boolean tempFlag = ordObjLinkService.hasOneDisStyle(Helper.getOid(temp));
			if (tempFlag == true) {
				isOneDisStyleFlag = true;
				break;
			}
		}
		return isOneDisStyleFlag;
	}

	/**
	 * 现行单据追加分发信息。
	 * 
	 * @param disOrdOid String
	 * @param linkList  List<Map<String, String>>
	 */
	private void addActiveOrderInfo(String disOrdOid, List<Map<String, String>> linkList) {
		// 分发对象服务
		DistributeObjectService objService = DistributeHelper.getDistributeObjectService();
		// 现行单据对象
		String dataObjectOid = linkList.get(0).get("DATAOID");

		List<String> linkOidList = new ArrayList<String>();
		int linkListSize = linkList.size();

		List<DistributeInfoConfig> infoConfigList = new ArrayList<DistributeInfoConfig>();
		for (int i = 1; i < linkListSize; i++) {
			Map<String, String> map = linkList.get(i);
			String flag = map.get("FLAG");
			String linkOid = map.get("LINKOID");
			String dataOid = map.get("DATAOID");
			// 未发放过
			if (ConstUtil.C_S_ZERO.equals(flag)) {
				linkOidList.add(linkOid);
				// 更改前对象
				List<ActiveOrdered> activeOrderedList = AdmHelper.getActiveOrderService()
						.getBeforeItemsByOrderAndAfterItem(dataObjectOid, dataOid);
				if (activeOrderedList == null || activeOrderedList.isEmpty() || activeOrderedList.get(0) == null) {
					// 默认分发信息创建与保存
					createDefaultDistributeInfo(linkOid,infoConfigList);
				} else {
					ActiveOrdered orderBeforeItem = activeOrderedList.get(0);
					boolean tempFlag = isOneDisStyle(Helper.getOid(orderBeforeItem));
					// 是否有一次性发放版本，有则不追加分发信息，没有则追加
					if (!tempFlag) {
						List<DistributeObject> disObjchangedList = objService.getDistributeObjectsByDataOid(Helper
								.getOid(orderBeforeItem));
						if (disObjchangedList == null || disObjchangedList.isEmpty()) {
							// 默认分发信息创建与保存
							createDefaultDistributeInfo(linkOid,infoConfigList);
						} else {
							// 更改前对象的分发信息复制给更改后对象
							addRefChangeInfo(orderBeforeItem, linkOid, disOrdOid);
						}
					}
				}
			}
		}
		if (ConstUtil.C_S_ZERO.equals(linkList.get(0).get("FLAG"))) {
			List<DistributeInfo> infoList = getDistributeInfosByOid(disOrdOid, linkOidList, DISPLAY_TYPE.DISPLAY);
			String linkInnerId = Helper.getInnerId(linkList.get(0).get("LINKOID"));
			String linkClassId = Helper.getClassId(linkList.get(0).get("LINKOID"));
			for (DistributeInfo infoOld : infoList) {
				DistributeInfo disInfoNew = createAndCopyDistributeInfo(infoOld, disOrdOid, linkList.get(0).get("LINKOID"));
				if (disInfoNew != null) {
					// 重新设置发放单与分发数据link信息
					disInfoNew.setDisOrderObjLinkId(linkInnerId);
					disInfoNew.setDisOrderObjLinkClassId(linkClassId);
					// 保存分发信息
					createDistributeInfo(disInfoNew);
				}
			}
		}
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService#updateDistributeInfos(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateDisInfoIsTrack(String oids, String isTracks) {
		List<String> oidList = SplitString.string2List(oids, ";");
		List<String> isTrack = SplitString.string2List(isTracks, ",");
		List<DisInfoIsTrack> trackList = new ArrayList<DisInfoIsTrack>();
		int index = 0;
		for (String infoOid : oidList) {
			DisInfoIsTrackService service = DistributeHelper.getDisInfoIsTrackService();
			List<DisInfoIsTrack> disInfoIsTrackList = service.getDisInfoIsTrackByDisInfoOid(infoOid);
			if (disInfoIsTrackList.size() != 0) {
				DisInfoIsTrack isTrackObj = disInfoIsTrackList.get(0);
				String isTrackValue = isTrack.get(index);
				if ("是".equals(isTrackValue)) {
					isTrackValue = "1";
				} else if ("否".equals(isTrackValue)) {
					isTrackValue = "0";
				}
				isTrackObj.setIstrack(Integer.parseInt(isTrackValue));
				trackList.add(isTrackObj);
			}
			index++;
		}
		if (trackList != null && trackList.size() > 0) {
			Helper.getPersistService().update(trackList);
		}

	}

	public void createOutSignDisInfo(String innerIds, String siteNames, String userIds, String linkOids, boolean flag) {
		List<String> iidList = SplitString.string2List(innerIds, ";");
		List<String> siteNameList = SplitString.string2List(siteNames, ";");
		List<String> userIdsList = SplitString.string2List(userIds, ";");
		List<String> distributeOrderObjectLinkOidList = SplitString.string2List(linkOids, ",");
		int length = iidList.size();
		DistributeOrder order = null;
		if(flag == false){
			//同一个发放单
			String linkOid = distributeOrderObjectLinkOidList.get(0);
			order = getDistributeOrder(Helper.getInnerId(linkOid),Helper.getClassId(linkOid),linkOid);
		}
		for (int index = 0; index < length; index++) {
			String siteSql = "select * from DTS_TRANSFER_SITE where siteDataId = ?";
			List<Site> siteList = Helper.getPersistService().query(siteSql, Site.class, iidList.get(index));
			Site site = siteList.get(0);
			for (String linkOid : distributeOrderObjectLinkOidList) {
				String innerId = Helper.getInnerId(linkOid);
				String classId = Helper.getClassId(linkOid);
				if(flag == true){
					//发放单个数为多个
					order = getDistributeOrder(innerId,classId,linkOid);
				}
				
				if (userIdsList.size() > 0) {
					List<String> userIdList = SplitString.string2List(userIdsList.get(index), ",");
					for (String userId : userIdList) {
						AAUserData user = AAProvider.getUserService().getUser(null, userId);
						String hql = "from DistributeInfo t where t.disOrderObjLinkId=? and t.disOrderObjLinkClassId=? and t.outSignId=? and t.outSignClassId=? and t.disMediaType='2' and t.note is null";
						List<DistributeInfo> list = PersistHelper.getService().find(hql, innerId, classId,
								user.getInnerId(), user.getClassId());
						if (list.size() == 0) {
							DistributeInfo disObject = newDistributeInfo();
							disObject.setDisInfoType(ConstUtil.C_DISINFOTYPE_SITE);
							// 分发信息名称（单位/人员）
							disObject.setDisInfoName(siteNameList.get(index));
							// 分发信息IID（人员或组织的内部标识）
							disObject.setDisInfoId(site.getInnerId());
							// 分发信息的类标识（人员或者组织的类标识）
							disObject.setInfoClassId(site.getClassId());
							//外域接收人IID（人员内部标识）
							disObject.setOutSignId(user.getInnerId());
							//外域接收人的类标识（人员类标识）
							disObject.setOutSignClassId(user.getClassId());
							//外域接收人名称（人员）
							disObject.setOutSignName(user.getName());
							// 分发信息变为已分发状态的时间
							disObject.setSendTime(0);
							// 分发份数
							disObject.setDisInfoNum(1);
							// 分发介质类型（0为纸质，1为电子，2为跨域）
							disObject.setDisMediaType(ConstUtil.C_DISMEDIATYPE_2);
							// 分发方式（0为直接分发，1为补发，2为移除，3为转发）
							disObject.setDisType(order.getOrderType());
							// 发放单与分发数据LINK内部标识
							disObject.setDisOrderObjLinkId(Helper.getPersistService().getInnerId(linkOid));
							// 发放单与分发数据LINK类标识
							disObject.setDisOrderObjLinkClassId(Helper.getPersistService().getClassId(linkOid));
							createDistributeInfo(disObject);
							//如果是更改单创建是否跟踪信息
							DistributeOrderObjectLink linkObj = (DistributeOrderObjectLink) Helper.getPersistService()
									.getObject(linkOid);
							DistributeObject distributeObject = (DistributeObject) linkObj.getTo();
							Persistable disObj = Helper.getPersistService().getObject(
									distributeObject.getDataClassId() + ":" + distributeObject.getDataInnerId());
							if (disObj instanceof ECO) {//更改单
								ECO ecoDis = (ECO) disObj;
								//创建是否跟踪信息
								DisInfoIsTrackService isTrackService = DistributeHelper.getDisInfoIsTrackService();
								isTrackService.createDisInfoIsTrack(disObject, ecoDis.getIsTrack());
							}
							if (disObj instanceof TNO) {
								//技术通知单
								TNO tnoDis = (TNO) disObj;
								//创建是否跟踪信息
								DisInfoIsTrackService isTrackService = DistributeHelper.getDisInfoIsTrackService();
								isTrackService.createDisInfoIsTrack(disObject, tnoDis.getIsTrack());
							}
						} else {
							DistributeInfo info = list.get(0);
							info.setDisInfoNum(info.getDisInfoNum() + 1);
							Helper.getPersistService().update(info);
						}
					}
				} else {
					String hql = "from DistributeInfo t where t.disOrderObjLinkId=? and t.disOrderObjLinkClassId=? and t.infoClassId || ':' ||t.disInfoId =? and t.disMediaType='2' and t.note is null and t.outSignId is null";
					List<DistributeInfo> list = PersistHelper.getService().find(hql, innerId, classId,
							site.getClassId() + ":" + site.getInnerId());
					if (list.size() == 0) {
						DistributeInfo disObject = newDistributeInfo();
						disObject.setDisInfoType(ConstUtil.C_DISINFOTYPE_SITE);
						// 分发信息名称（单位/人员）
						disObject.setDisInfoName(siteNameList.get(index));
						// 分发信息IID（人员或组织的内部标识）
						disObject.setDisInfoId(site.getInnerId());
						// 分发信息的类标识（人员或者组织的类标识）
						disObject.setInfoClassId(site.getClassId());
						//外域接收人IID（人员内部标识）
						disObject.setOutSignId("");
						//外域接收人的类标识（人员类标识）
						disObject.setOutSignClassId("");
						//外域接收人名称（人员）
						disObject.setOutSignName("");
						// 分发信息变为已分发状态的时间
						disObject.setSendTime(0);
						// 分发份数
						disObject.setDisInfoNum(1);
						// 分发介质类型（0为纸质，1为电子，2为跨域）
						disObject.setDisMediaType(ConstUtil.C_DISMEDIATYPE_2);
						// 分发方式（0为直接分发，1为补发，2为移除，3为转发）
						disObject.setDisType(order.getOrderType());
						// 发放单与分发数据LINK内部标识
						disObject.setDisOrderObjLinkId(Helper.getPersistService().getInnerId(linkOid));
						// 发放单与分发数据LINK类标识
						disObject.setDisOrderObjLinkClassId(Helper.getPersistService().getClassId(linkOid));
						createDistributeInfo(disObject);
						//如果是更改单创建是否跟踪信息
						DistributeOrderObjectLink linkObj = (DistributeOrderObjectLink) Helper.getPersistService()
								.getObject(linkOid);
						DistributeObject distributeObject = (DistributeObject) linkObj.getTo();
						Persistable disObj = Helper.getPersistService().getObject(
								distributeObject.getDataClassId() + ":" + distributeObject.getDataInnerId());
						if (disObj instanceof ECO) {//更改单
							ECO ecoDis = (ECO) disObj;
							//创建是否跟踪信息
							DisInfoIsTrackService isTrackService = DistributeHelper.getDisInfoIsTrackService();
							isTrackService.createDisInfoIsTrack(disObject, ecoDis.getIsTrack());
						}
						if (disObj instanceof TNO) {
							//技术通知单
							TNO tnoDis = (TNO) disObj;
							//创建是否跟踪信息
							DisInfoIsTrackService isTrackService = DistributeHelper.getDisInfoIsTrackService();
							isTrackService.createDisInfoIsTrack(disObject, tnoDis.getIsTrack());
						}
					} else {
						DistributeInfo info = list.get(0);
						info.setDisInfoNum(info.getDisInfoNum() + 1);
						Helper.getPersistService().update(info);
					}
				}
			}
		}
	}
	
	public DistributeOrder getDistributeOrder(String innerId, String classId, String linkOid){
		String sql = "select d.* from DDM_DIS_ORDEROBJLINK l,DDM_DIS_ORDER d where d.innerId = l.fromObjectId and d.classId = l.fromObjectClassId and l.classId || ':' || l.innerId = ?";
		//判断分发方式
		List<DistributeOrder> orderList = Helper.getPersistService().query(sql, DistributeOrder.class, linkOid);
		DistributeOrder order = orderList.get(0);
		return order;
	}

	/**
	 * A版本以外的套对象追加分发信息。
	 * 
	 * @param disOrdOid
	 *            String
	 * @param linkList
	 *            List<Map<String, String>>
	 */
	private void addSuitInfo(String disOrdOid, List<Map<String, String>> linkList) {
		// 分发对象服务
		DistributeObjectService objService = DistributeHelper.getDistributeObjectService();
		// 套对象
		Persistable dataObject = Helper.getPersistService().getObject(linkList.get(0).get("DATAOID"));
		String disOrdObjLinkOid = linkList.get(0).get("LINKOID");
		String suitFlag = linkList.get(0).get("FLAG");
		ATSuit suit = (ATSuit) dataObject;
		// 取得上一个版本的套对象的InnerId,ClassId
		String beforeClassId = suit.getIterationInfo().getPredecessorRef().getClassId();
		String beforeInnerId = suit.getIterationInfo().getPredecessorRef().getInnerId();

		// 上一个版本的套对象
		Persistable beforeObject = Helper.getPersistService().getObject(beforeClassId + ":" + beforeInnerId);
		boolean atsuitFlag = isOneDisStyle(Helper.getOid(beforeObject));
		// 上一个版本的套对象是否有一次性发放版本，有则不追加分发信息，没有则追加
		if (!atsuitFlag) {
			// 未发放过
			if (ConstUtil.C_S_ZERO.equals(suitFlag)) {
				// 上一个版本的套对象的分发信息复制给现在的套对象
				addRefChangeInfo(beforeObject, disOrdObjLinkOid, disOrdOid);
			}

			// 以下是对套内对象的分发信息的处理
			ChangeService changeSerive = Helper.getChangeService();
			int linkListSize = linkList.size();

			List<DistributeInfoConfig> infoConfigList = new ArrayList<DistributeInfoConfig>();
			for (int i = 1; i < linkListSize; i++) {
				List<String> linkOidList = new ArrayList<String>();
				Map<String, String> map = linkList.get(i);
				String flag = map.get("FLAG");
				String linkOid = map.get("LINKOID");
				String dataOid = map.get("DATAOID");
				Persistable obj = Helper.getPersistService().getObject(dataOid);
				// 未发放过
				if (ConstUtil.C_S_ZERO.equals(flag)) {
					linkOidList.add(linkOid);
					if (obj instanceof Changed) {
						// 找到套内对象关联的更改单
						List<ECO> listAfter =  Helper.getChangeService().getRelatedECOList(obj);
						if (listAfter.size() == 0) {
							// 默认分发信息创建与保存
							createDefaultDistributeInfo(linkOid,infoConfigList);
						} else {
							// 更改单对象
							Change change = (Change) listAfter.get(0);
							// 更改前对象
							ChangedLink changeLink = changeSerive.getChangedByChangedAfter(change , obj);
							if (changeLink == null || changeLink.getFrom() == null) {
								// 默认分发信息创建与保存
								createDefaultDistributeInfo(linkOid,infoConfigList);
							} else {
								Persistable changed = changeLink.getFrom();
								boolean suitedFlag = isOneDisStyle(Helper.getOid(changed));
								// 上一个版本的套内对象是否有一次性发放版本，有则不追加分发信息，没有则追加
								if (!suitedFlag) {
									List<DistributeObject> disObjchangedList = objService.getDistributeObjectsByDataOid(Helper
											.getOid(changed));
									// 改前对象没有发放过，改后对象追加默认分发信息
									if (disObjchangedList == null || disObjchangedList.isEmpty()) {
										// 默认分发信息创建与保存
										createDefaultDistributeInfo(linkOid,infoConfigList);
										// 改前对象发放过，改后对象追加改前对象的分发信息
									} else {
										// 更改前对象的分发信息复制给更改后对象
										addRefChangeInfo(changed, linkOid, disOrdOid);
										
										// 更改后对象的分发信息复制给更改单
										addRefEcoInfo(disOrdOid, linkOidList, change);
									}
								}
							}
						}
					} else if(obj instanceof ECO){
						continue;
					} else {
						// 默认分发信息创建与保存
						createDefaultDistributeInfo(linkOid,infoConfigList);
					}

				}
			}
			
		}
	}

	/**
	 * 更改后对象的分发信息复制给更改单
	 * 
	 * @param disOrdOid
	 *            String
	 * @param linkOidList
	 *            List<String>
	 * @param change
	 *            Change
	 */
	private void addRefEcoInfo(String disOrdOid, List<String> linkOidList, Change change) {
		List<DistributeInfo> infoList = getDistributeInfosByOid(disOrdOid,
				linkOidList, DISPLAY_TYPE.DISPLAY);
		
		String disOrdInnerId = Helper.getPersistService().getInnerId(disOrdOid);
		String disOrdClassId = Helper.getPersistService().getClassId(disOrdOid);
		ECO eco = (ECO) change;
		DistributeObjectService objService = DistributeHelper.getDistributeObjectService();
		List<DistributeObject> disObjList = objService.getDistributeObjectsByDataOid(Helper.getOid(eco));
		// 改前对象没有发放过，改后对象追加默认分发信息
		if (disObjList != null && !disObjList.isEmpty()) {
			String disObjInnerId = disObjList.get(0).getInnerId();
			String disObjClassId = disObjList.get(0).getClassId();
			String hql = "from DistributeOrderObjectLink t "
					+ "where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? "
					+ "and t.toObjectRef.innerId=? and t.toObjectRef.classId=? ";

			List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, disOrdInnerId,
					disOrdClassId, disObjInnerId, disObjClassId);
			if (links.size() != 0) {
				String linkInnerId = links.get(0).getInnerId();
				String linkClassId = links.get(0).getClassId();
				for (DistributeInfo infoOld : infoList) {
					DistributeInfo disInfoNew = createAndCopyDistributeInfo(infoOld, disOrdOid, linkClassId + ":" + linkInnerId);
					if (disInfoNew != null) {
						// 重新设置发放单与分发数据link信息
						disInfoNew.setDisOrderObjLinkId(linkInnerId);
						disInfoNew.setDisOrderObjLinkClassId(linkClassId);
						// 保存分发信息
						createDistributeInfo(disInfoNew);
						// 如果是更改单创建是否跟踪信息
						// 创建是否跟踪信息
						DisInfoIsTrackService isTrackService = DistributeHelper.getDisInfoIsTrackService();
						isTrackService.createDisInfoIsTrack(disInfoNew,eco.getIsTrack());
					}
				}
			}
		}
	}

	@Override
	public boolean canDisBySecurityLevel(String disOrderOid) {
		boolean userSecrityFlag = ContextUtil.isEnableUserSecrityController();
		boolean secrityCanDisFlag = true;
		if(userSecrityFlag){
			//获取发放单的所有分发数据和分发信息，对比密级，弹出提示
			List<DistributeObject> disObjList = DistributeHelper.getDistributeObjectService().getDistributeObjectsByDistributeOrderOid(disOrderOid);
			int securityLevel = 0;
			int objectBigLevel = 0;
			for(DistributeObject disObj : disObjList){
				//取得所有对象的最高密级
				if(null!=disObj.getSecurityLevelInfo()){
					securityLevel = disObj.getSecurityLevelInfo().getSecurityLevel().getLevel();
					if(objectBigLevel>=securityLevel){
						continue;
					}else{
						objectBigLevel = securityLevel;
					}
				}
			}
			if(objectBigLevel!=0){
				List<DistributeInfo> list = getDistributeInfosByDistributeOrderInnerId(Helper.getInnerId(disOrderOid));
				for(DistributeInfo disInfo:list){
					if(ConstUtil.C_TASKTYPE_ELEC.equals(disInfo.getDisMediaType()) && User.CLASSID.equals(disInfo.getInfoClassId())){
						User user = (User)PersistHelper.getService().getObject(disInfo.getInfoClassId(), disInfo.getDisInfoId());
						SecurityLevel userSecurityLevel = ContextUserHelper.getUserSecurityService().getUserSecurityLevel(user, user.getContextInfo().getContext());
						if(objectBigLevel>userSecurityLevel.getLevel()){
							//如果对象的密级高于人的密级，则提示不能提交分发
							secrityCanDisFlag = false;
						}
					}
				}
			}
		}
		return secrityCanDisFlag;
	}

	@Override
	public List<DistributeInfo> getDistributeInfosByDistributePaperTaskOID(
			String oid) {
		String innerId=Helper.getInnerId(oid);
		String classId=Helper.getClassId(oid);
		//String hql = "from DistributeOrderObjectLink t where t.fromObjectRef.innerId=?";
		String sql = "select t.* from  ddm_dis_info t , ddm_dis_taskinfolink b "
				+ " where t.innerid = b.toobjectid and t.classid = b.toobjectclassid "
				+ " and b.fromObjectId = ? and b.fromObjectClassId = ?";
		List<DistributeInfo> infos = PersistHelper.getService().query(sql, DistributeInfo.class, innerId,classId);
		return infos;
	
	}
}
