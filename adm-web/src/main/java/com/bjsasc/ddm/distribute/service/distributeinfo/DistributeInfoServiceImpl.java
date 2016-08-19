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
 * �ַ���Ϣ����ʵ���ࡣ
 * 
 * @author gengancong 2013-2-22
 */
@SuppressWarnings({ "unchecked", "static-access" })
public class DistributeInfoServiceImpl implements DistributeInfoService {

	
	DistributeInfoConfigService infoConfigservice = DistributeHelper.getDistributeInfoConfigService();
	/* ���� Javadoc��
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

	/* ���� Javadoc��
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
	 * ��ȡ�������ٵ���Ϣ
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
	 * ͨ��ֽ��ǩ������OID,�ַ�����OIDS��ȡ�ַ���Ϣ���ݡ�
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

	/* ���� Javadoc��
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

	/* ���� Javadoc��
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
				
				//�ж��޸ĺ�ķַ���Ϣ���䷢����λ/��Ա�ͷַ����ͺͱ�ע�Ƿ�ʹ˷ַ������ڴ˷ַ����ڵ����еķַ���Ϣ�ظ�
				String sql = "";
				List<DistributeInfo> list = new ArrayList<DistributeInfo>();
				if(StringUtil.isNull(note)){
					sql = "select * from ddm_dis_info where disOrderObjLinkId=? and disOrderObjLinkClassId=? and disInfoId=? and infoClassId=? and disMediaType=? and note is null and innerid <> ? ";
					list = Helper.getPersistService().query(sql, DistributeInfo.class, disInfo.getDisOrderObjLinkId(), disInfo.getDisOrderObjLinkClassId(), disInfo.getDisInfoId(), disInfo.getInfoClassId(), dismediatype, disInfo.getInnerId());
				}else{
					sql = "select * from ddm_dis_info where disOrderObjLinkId=? and disOrderObjLinkClassId=? and disInfoId=? and infoClassId=? and disMediaType=? and note = ? and innerid <> ? ";
					list = Helper.getPersistService().query(sql, DistributeInfo.class, disInfo.getDisOrderObjLinkId(), disInfo.getDisOrderObjLinkClassId(), disInfo.getDisInfoId(), disInfo.getInfoClassId(), dismediatype, note, disInfo.getInnerId());
				}
				if(list.size()>0){//�����ظ��������¾��еķַ���Ϣ�ķ���������ԭ�޸ĵķַ���Ϣ����ɾ��
					DistributeInfo info = list.get(0);
					if(!info.getDisMediaType().equals(ConstUtil.C_DISMEDIATYPE_1) && !disinfoInnerIDList.contains(disInfo.getInnerId())){
						info.setDisInfoNum(info.getDisInfoNum()+Long.parseLong(disInfoNum));
						disobjList.add(info);
						disinfoInnerIDList.add(info.getInnerId());
					}
					Helper.getPersistService().delete(disInfo);
				}else{//������ظ���������޸ĺ�ķַ���Ϣ
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

	/* ���� Javadoc��
	 * @see com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService#createDistributeInfo(com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo)
	 */
	@Override
	public void createDistributeInfo(DistributeInfo disObject) {
		Helper.getPersistService().save(disObject);

	}

	/* ���� Javadoc��
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
				//�жϷַ���ʽ
				List<DistributeOrder> orderList = Helper.getPersistService().query(sql, DistributeOrder.class, linkOid);
				DistributeOrder order = orderList.get(0);
				String hql = "";
				String note ="";
				
				//notes��Ϊ��
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
							// �ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
							disObject.setDisInfoType(ConstUtil.C_DISINFOTYPE_USER);
						} else if (DISINFOTYPE.valueOf(type) == DISINFOTYPE.ORG) {
							// �ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
							disObject.setDisInfoType(ConstUtil.C_DISINFOTYPE_ORG);
							//��λ����(0Ϊ�ڲ���λ,1Ϊ�ⲿ��λ)
							if(domainref==ConstUtil.C_DOMAINREF){
								disObject.setSendType(ConstUtil.C_SENDTYPE_1);
							}else{
								disObject.setSendType(ConstUtil.C_SENDTYPE_0);
							}
						}
						// �ַ���Ϣ���ƣ���λ/��Ա��
						disObject.setDisInfoName(principal.getName());
						// �ַ���ϢIID����Ա����֯���ڲ���ʶ��
						disObject.setDisInfoId(principal.getInnerId());
						// �ַ���Ϣ�����ʶ����Ա������֯�����ʶ��
						disObject.setInfoClassId(principal.getClassId());
						// �ַ���Ϣ��Ϊ�ѷַ�״̬��ʱ��
						disObject.setSendTime(0);
						// �ַ�����
						disObject.setDisInfoNum(Long.valueOf(disInfoNumList.get(index)));
						// �ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
						disObject.setDisMediaType(disMediaTypeList.get(index));
						// �ַ���ʽ��0Ϊֱ�ӷַ���1Ϊ������2Ϊ�Ƴ���3Ϊת����
						disObject.setDisType(order.getOrderType());
						// ��ע
						if (note != null && !"null".equals(note)) {
							disObject.setNote(note);
						}
						// ���ŵ���ַ�����LINK�ڲ���ʶ
						disObject.setDisOrderObjLinkId(Helper.getPersistService().getInnerId(linkOid));
						// ���ŵ���ַ�����LINK���ʶ
						disObject.setDisOrderObjLinkClassId(Helper.getPersistService().getClassId(linkOid));
						createDistributeInfo(disObject);

						//����Ǹ��ĵ������Ƿ������Ϣ
						DistributeOrderObjectLink linkObj = (DistributeOrderObjectLink) Helper.getPersistService()
								.getObject(linkOid);
						DistributeObject distributeObject = (DistributeObject) linkObj.getTo();
						Persistable disObj = Helper.getPersistService().getObject(
								distributeObject.getDataClassId() + ":" + distributeObject.getDataInnerId());
						if (disObj instanceof ECO) {//���ĵ�
							ECO ecoDis = (ECO) disObj;
							//�����Ƿ������Ϣ
							DisInfoIsTrackService isTrackService = DistributeHelper.getDisInfoIsTrackService();
							isTrackService.createDisInfoIsTrack(disObject, ecoDis.getIsTrack());
						} else if (disObj instanceof TNO) {
							//����֪ͨ��
							TNO tnoDis = (TNO) disObj;
							//�����Ƿ������Ϣ
							DisInfoIsTrackService isTrackService = DistributeHelper.getDisInfoIsTrackService();
							isTrackService.createDisInfoIsTrack(disObject, tnoDis.getIsTrack());
						}

					}
				} else {
					DistributeInfo info = list.get(0);
					if ("0".equals(info.getDisMediaType())) {
						if(DISINFOTYPE.valueOf(type) == DISINFOTYPE.ORG){
						//��λ����(0Ϊ�ڲ���λ,1Ϊ�ⲿ��λ)
						if(domainref==ConstUtil.C_DOMAINREF){
							info.setSendType(ConstUtil.C_SENDTYPE_1);
						}else{
							info.setSendType(ConstUtil.C_SENDTYPE_0);
						}
					}
						info.setDisInfoNum(info.getDisInfoNum() + Long.valueOf(disInfoNumList.get(index)));
						Helper.getPersistService().update(info);
					} else if ("1".equals(info.getDisMediaType())) {
						throw new RuntimeException("�벻Ҫ��ͬһ���ַ����������ͬ�ĵ��ӷַ���Ϣ��");
					}
				}
				i++;
			}
		}
	}

	/* ���� Javadoc��
	 * @see com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService#deleteDistributeInfo(com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo)
	 */
	@Override
	public void deleteDistributeInfo(DistributeInfo disObject) {
		Helper.getPersistService().delete(disObject);
	}

	/* ���� Javadoc��
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

	/* ���� Javadoc��
	 * @see com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService#newDistributeInfo()
	 */
	@Override
	public DistributeInfo newDistributeInfo() {
		DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
		DistributeInfo dis = (DistributeInfo) PersistUtil.createObject(DistributeInfo.CLASSID);

		// �������ڳ�ʼ��
		life.initLifecycle(dis);

		dis.setClassId(dis.CLASSID);
		return dis;
	}

	/* ���� Javadoc��
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
		//�жϷ��ŵ����Ƿ������ͬ�ַ���Ϣ�������ڣ�ֻ���·���
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
			// ����
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
		// Ĭ�Ϸַ���Ϣ����
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
				// �ַ���ϢIID
				String disInfoId = infoConfig.getDisInfoId();
				// �ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
				String disMediaType =infoConfig.getDisMediaType();
				// �ַ�����
				String disInfoNum =String.valueOf(infoConfig.getDisInfoNum());
				// ��ע
				String note = infoConfig.getNote();
				// �����ַ���Ϣ����
				createDistributeInfo(disInfoType, disInfoId, disMediaType, disInfoNum, note, disOrdObjLinkOid, "null");
			}
		}
		
		//List<Map<String, String>> beanList = DdmSysConfigUtil.getDefaultDisInfo();
		/*for (Map<String, String> bean : beanList) {
			// �ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
			String disInfoType = bean.get(DdmSysConfigConst.PARAM_DIS_INFO_TYPE);
			if (ConstUtil.C_S_ZERO.equals(disInfoType)) {
				disInfoType = "ORG";
			} else {
				disInfoType = "USER";
			}
			// �ַ���ϢIID
			String disInfoId = bean.get(DdmSysConfigConst.PARAM_DIS_INFO_ID);
			// �ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
			String disMediaType = bean.get(DdmSysConfigConst.PARAM_DIS_INFO_MEDIA_TYPE);
			// �ַ�����
			String disInfoNum = bean.get(DdmSysConfigConst.PARAM_DIS_INFO_NUM);
			// ��ע
			String note = bean.get(DdmSysConfigConst.PARAM_DIS_INFO_NOTE);
			// �����ַ���Ϣ����
			createDistributeInfo(disInfoType, disInfoId, disMediaType, disInfoNum, note, disOrdObjLinkOid, "null");
		}*/
	}

	/* ���� Javadoc��
	 * @see com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService#createInfo(java.lang.String,java.lang.List,boolean,boolean)
	 */
	@Override
	public void createInfo(String disOrdOid, List<Map<String, String>> linkList, int collectFlag,
			boolean addDefaultInfoFlag) {
		int linkListSize = linkList.size();
		List<DistributeInfoConfig> infoConfigList = new ArrayList<DistributeInfoConfig>();
		// �ռ������Ǹ��ĵ�����
		if (collectFlag == ConstUtil.COLLECTFLAG_1) {
			addECOInfo(disOrdOid, linkList);
			// �ռ����������е��ݶ���
		} else if (collectFlag == ConstUtil.COLLECTFLAG_2) {
			addActiveOrderInfo(disOrdOid, linkList);
			// �ռ�������A�汾������׶���
		} else if (collectFlag == ConstUtil.COLLECTFLAG_3) {
			addSuitInfo(disOrdOid, linkList);
			// �ռ������Ǹ��ĵ�,���е�������Ķ����A�汾���׶���
		} else {
			// ׷��Ĭ�Ϸַ���Ϣ
			if (addDefaultInfoFlag) {
				for (int i = 0; i < linkListSize; i++) {
					Map<String, String> map = linkList.get(i);
					String flag = map.get("FLAG");
					String linkOid = map.get("LINKOID");
					// δ���Ź�����Ҫ׷��Ĭ�Ϸַ�����
					if (ConstUtil.C_S_ZERO.equals(flag)) {
						// Ĭ�Ϸַ���Ϣ�����뱣��
						createDefaultDistributeInfo(linkOid,infoConfigList);
					}
				}
			}
		}
	}

	/**
	 * ���ĵ�׷�ӷַ���Ϣ��
	 * 
	 * @param disOrdOid String
	 * @param linkList  List<Map<String, String>>
	 */
	private void addECOInfo(String disOrdOid, List<Map<String, String>> linkList) {
		// �ַ��������
		DistributeObjectService objService = DistributeHelper.getDistributeObjectService();
		// ���ĵ�����
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
			// δ���Ź�
			if (ConstUtil.C_S_ZERO.equals(flag)) {
				linkOidList.add(linkOid);
				// ����ǰ����
				ChangedLink changeLink = changeSerive.getChangedByChangedAfter((Change) dataObject, obj);
				if (changeLink == null || changeLink.getFrom() == null) {
					// Ĭ�Ϸַ���Ϣ�����뱣��
					createDefaultDistributeInfo(linkOid,infoConfigList);
				} else {
					Persistable changed = changeLink.getFrom();
					boolean tempFlag = isOneDisStyle(Helper.getOid(changed));
					// �Ƿ���һ���Է��Ű汾������׷�ӷַ���Ϣ��û����׷��
					if (!tempFlag) {
						List<DistributeObject> disObjchangedList = objService.getDistributeObjectsByDataOid(Helper
								.getOid(changed));
						// ��ǰ����û�з��Ź����ĺ����׷��Ĭ�Ϸַ���Ϣ
						if (disObjchangedList == null || disObjchangedList.isEmpty()) {
							// Ĭ�Ϸַ���Ϣ�����뱣��
							createDefaultDistributeInfo(linkOid,infoConfigList);
							// ��ǰ���󷢷Ź����ĺ����׷�Ӹ�ǰ����ķַ���Ϣ
						} else {
							// ����ǰ����ķַ���Ϣ���Ƹ����ĺ����
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
					// �������÷��ŵ���ַ�����link��Ϣ
					disInfoNew.setDisOrderObjLinkId(linkInnerId);
					disInfoNew.setDisOrderObjLinkClassId(linkClassId);
					// ����ַ���Ϣ
					createDistributeInfo(disInfoNew);
					//����Ǹ��ĵ������Ƿ������Ϣ
					DistributeOrderObjectLink linkObj = (DistributeOrderObjectLink) Helper.getPersistService()
							.getObject(linkList.get(0).get("LINKOID"));
					DistributeObject distributeObject = (DistributeObject) linkObj.getTo();
					Persistable disObj = Helper.getPersistService().getObject(
							distributeObject.getDataClassId() + ":" + distributeObject.getDataInnerId());
					if (disObj instanceof ECO) {//���ĵ�
						ECO ecoDis = (ECO) disObj;
						//�����Ƿ������Ϣ
						DisInfoIsTrackService isTrackService = DistributeHelper.getDisInfoIsTrackService();
						isTrackService.createDisInfoIsTrack(disInfoNew, ecoDis.getIsTrack());
					}
				}
			}
		}
	}

	/**
	 * ����ǰ����ķַ���Ϣ���Ƹ����ĺ����
	 * 
	 * @param dataObject Persistable
	 * @param disOrdObjLinkOid String
	 */
	private void addRefChangeInfo(Persistable dataObject, String disOrdObjLinkOid, String orderOid) {
		String dataInnerId = dataObject.getInnerId();
		String dataClassId = dataObject.getClassId();
		String linkInnerId = Helper.getInnerId(disOrdObjLinkOid);
		String linkClassId = Helper.getClassId(disOrdObjLinkOid);

		// ��ȡ�ַ���Ϣ
		String sql = "SELECT Info.* FROM DDM_DIS_OBJECT Obj, DDM_DIS_ORDEROBJLINK ObjLink, DDM_DIS_INFO Info "
				+ "WHERE obj.DATAINNERID = ? " + " AND obj.DATACLASSID = ? "
				+ " AND Info.DISORDEROBJLINKID = ObjLink.INNERID "
				+ " AND Info.DISORDEROBJLINKCLASSID = ObjLink.CLASSID " + " AND ObjLink.TOOBJECTID = Obj.INNERID "
				+ " AND ObjLink.TOOBJECTCLASSID = Obj.CLASSID ORDER BY Info.MODIFYTIME ASC";
		List<DistributeInfo> disInfoList = Helper.getPersistService().query(sql, DistributeInfo.class, dataInnerId,
				dataClassId);

		for (DistributeInfo disInfoOld : disInfoList) {
			// �����µķַ���Ϣ���������ɷַ���Ϣ���������
			DistributeInfo disInfoNew = createAndCopyDistributeInfo(disInfoOld, orderOid, disOrdObjLinkOid);
			if (disInfoNew != null) {
				// �������÷��ŵ���ַ�����link��Ϣ
				disInfoNew.setDisOrderObjLinkId(linkInnerId);
				disInfoNew.setDisOrderObjLinkClassId(linkClassId);
				if (disInfoNew != null && ConstUtil.DEFAULT_DIS_INFO.equals(disInfoNew.getNote())) {
					disInfoNew.setNote("");
				}
				// ����ַ���Ϣ
				createDistributeInfo(disInfoNew);
				//����Ǹ��ĵ������Ƿ������Ϣ
				DistributeOrderObjectLink linkObj = (DistributeOrderObjectLink) Helper.getPersistService().getObject(
						disOrdObjLinkOid);
				DistributeObject distributeObject = (DistributeObject) linkObj.getTo();
				Persistable disObj = Helper.getPersistService().getObject(
						distributeObject.getDataClassId() + ":" + distributeObject.getDataInnerId());
				if (disObj instanceof ECO) {//���ĵ�
					ECO ecoDis = (ECO) disObj;
					//�����Ƿ������Ϣ
					DisInfoIsTrackService isTrackService = DistributeHelper.getDisInfoIsTrackService();
					isTrackService.createDisInfoIsTrack(disInfoNew, ecoDis.getIsTrack());
				}
			}
		}
	}

	/* ���� Javadoc��
	 * @see com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService#addDefaultInfo(com.bjsasc.plm.core.persist.Persistable)
	 */
	@Override
	public boolean addDefaultInfo(Persistable dataObject) {
		// �Ƿ�׷��Ĭ�Ϸַ���Ϣ��ʶ
		boolean addDefaultInfoFlag = true;
		//  ����
		if (dataObject instanceof ApproveOrder) {
			addDefaultInfoFlag = true;
			// ���ĵ�
		} else if (dataObject instanceof ECO) {
			addDefaultInfoFlag = true;
			// ����
		} else if (dataObject instanceof Part) {
			addDefaultInfoFlag = true;
			// �ĵ�
		} else if (dataObject instanceof Document) {
			addDefaultInfoFlag = true;
			// �������
		} else if (dataObject instanceof ManagedBaseline) {
			addDefaultInfoFlag = true;
			// ����֪ͨ��
		} else if (dataObject instanceof TNO) {
			addDefaultInfoFlag = true;
			//����������ɵ�
		} else if (dataObject instanceof Variance) {
			addDefaultInfoFlag = true;
			//�����ļ�
		} else if (dataObject instanceof ActiveDocument) {
			addDefaultInfoFlag = true;
			//������
		} else if (dataObject instanceof ActiveSet) {
			addDefaultInfoFlag = true;
			//���е���
		} else if (dataObject instanceof ActiveOrder) {
			addDefaultInfoFlag = true;
			// �׶���
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
		// �ַ��������0
		DistributeOrderObjectLinkService ordObjLinkService = DistributeHelper.getDistributeOrderObjectLinkService();
		boolean isOneDisStyleFlag = false;
		for (Versioned temp : versionedList) {
			// �ַ��������0
			boolean tempFlag = ordObjLinkService.hasOneDisStyle(Helper.getOid(temp));
			if (tempFlag == true) {
				isOneDisStyleFlag = true;
				break;
			}
		}
		return isOneDisStyleFlag;
	}

	/**
	 * ���е���׷�ӷַ���Ϣ��
	 * 
	 * @param disOrdOid String
	 * @param linkList  List<Map<String, String>>
	 */
	private void addActiveOrderInfo(String disOrdOid, List<Map<String, String>> linkList) {
		// �ַ��������
		DistributeObjectService objService = DistributeHelper.getDistributeObjectService();
		// ���е��ݶ���
		String dataObjectOid = linkList.get(0).get("DATAOID");

		List<String> linkOidList = new ArrayList<String>();
		int linkListSize = linkList.size();

		List<DistributeInfoConfig> infoConfigList = new ArrayList<DistributeInfoConfig>();
		for (int i = 1; i < linkListSize; i++) {
			Map<String, String> map = linkList.get(i);
			String flag = map.get("FLAG");
			String linkOid = map.get("LINKOID");
			String dataOid = map.get("DATAOID");
			// δ���Ź�
			if (ConstUtil.C_S_ZERO.equals(flag)) {
				linkOidList.add(linkOid);
				// ����ǰ����
				List<ActiveOrdered> activeOrderedList = AdmHelper.getActiveOrderService()
						.getBeforeItemsByOrderAndAfterItem(dataObjectOid, dataOid);
				if (activeOrderedList == null || activeOrderedList.isEmpty() || activeOrderedList.get(0) == null) {
					// Ĭ�Ϸַ���Ϣ�����뱣��
					createDefaultDistributeInfo(linkOid,infoConfigList);
				} else {
					ActiveOrdered orderBeforeItem = activeOrderedList.get(0);
					boolean tempFlag = isOneDisStyle(Helper.getOid(orderBeforeItem));
					// �Ƿ���һ���Է��Ű汾������׷�ӷַ���Ϣ��û����׷��
					if (!tempFlag) {
						List<DistributeObject> disObjchangedList = objService.getDistributeObjectsByDataOid(Helper
								.getOid(orderBeforeItem));
						if (disObjchangedList == null || disObjchangedList.isEmpty()) {
							// Ĭ�Ϸַ���Ϣ�����뱣��
							createDefaultDistributeInfo(linkOid,infoConfigList);
						} else {
							// ����ǰ����ķַ���Ϣ���Ƹ����ĺ����
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
					// �������÷��ŵ���ַ�����link��Ϣ
					disInfoNew.setDisOrderObjLinkId(linkInnerId);
					disInfoNew.setDisOrderObjLinkClassId(linkClassId);
					// ����ַ���Ϣ
					createDistributeInfo(disInfoNew);
				}
			}
		}
	}

	/* ���� Javadoc��
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
				if ("��".equals(isTrackValue)) {
					isTrackValue = "1";
				} else if ("��".equals(isTrackValue)) {
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
			//ͬһ�����ŵ�
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
					//���ŵ�����Ϊ���
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
							// �ַ���Ϣ���ƣ���λ/��Ա��
							disObject.setDisInfoName(siteNameList.get(index));
							// �ַ���ϢIID����Ա����֯���ڲ���ʶ��
							disObject.setDisInfoId(site.getInnerId());
							// �ַ���Ϣ�����ʶ����Ա������֯�����ʶ��
							disObject.setInfoClassId(site.getClassId());
							//���������IID����Ա�ڲ���ʶ��
							disObject.setOutSignId(user.getInnerId());
							//��������˵����ʶ����Ա���ʶ��
							disObject.setOutSignClassId(user.getClassId());
							//������������ƣ���Ա��
							disObject.setOutSignName(user.getName());
							// �ַ���Ϣ��Ϊ�ѷַ�״̬��ʱ��
							disObject.setSendTime(0);
							// �ַ�����
							disObject.setDisInfoNum(1);
							// �ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
							disObject.setDisMediaType(ConstUtil.C_DISMEDIATYPE_2);
							// �ַ���ʽ��0Ϊֱ�ӷַ���1Ϊ������2Ϊ�Ƴ���3Ϊת����
							disObject.setDisType(order.getOrderType());
							// ���ŵ���ַ�����LINK�ڲ���ʶ
							disObject.setDisOrderObjLinkId(Helper.getPersistService().getInnerId(linkOid));
							// ���ŵ���ַ�����LINK���ʶ
							disObject.setDisOrderObjLinkClassId(Helper.getPersistService().getClassId(linkOid));
							createDistributeInfo(disObject);
							//����Ǹ��ĵ������Ƿ������Ϣ
							DistributeOrderObjectLink linkObj = (DistributeOrderObjectLink) Helper.getPersistService()
									.getObject(linkOid);
							DistributeObject distributeObject = (DistributeObject) linkObj.getTo();
							Persistable disObj = Helper.getPersistService().getObject(
									distributeObject.getDataClassId() + ":" + distributeObject.getDataInnerId());
							if (disObj instanceof ECO) {//���ĵ�
								ECO ecoDis = (ECO) disObj;
								//�����Ƿ������Ϣ
								DisInfoIsTrackService isTrackService = DistributeHelper.getDisInfoIsTrackService();
								isTrackService.createDisInfoIsTrack(disObject, ecoDis.getIsTrack());
							}
							if (disObj instanceof TNO) {
								//����֪ͨ��
								TNO tnoDis = (TNO) disObj;
								//�����Ƿ������Ϣ
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
						// �ַ���Ϣ���ƣ���λ/��Ա��
						disObject.setDisInfoName(siteNameList.get(index));
						// �ַ���ϢIID����Ա����֯���ڲ���ʶ��
						disObject.setDisInfoId(site.getInnerId());
						// �ַ���Ϣ�����ʶ����Ա������֯�����ʶ��
						disObject.setInfoClassId(site.getClassId());
						//���������IID����Ա�ڲ���ʶ��
						disObject.setOutSignId("");
						//��������˵����ʶ����Ա���ʶ��
						disObject.setOutSignClassId("");
						//������������ƣ���Ա��
						disObject.setOutSignName("");
						// �ַ���Ϣ��Ϊ�ѷַ�״̬��ʱ��
						disObject.setSendTime(0);
						// �ַ�����
						disObject.setDisInfoNum(1);
						// �ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
						disObject.setDisMediaType(ConstUtil.C_DISMEDIATYPE_2);
						// �ַ���ʽ��0Ϊֱ�ӷַ���1Ϊ������2Ϊ�Ƴ���3Ϊת����
						disObject.setDisType(order.getOrderType());
						// ���ŵ���ַ�����LINK�ڲ���ʶ
						disObject.setDisOrderObjLinkId(Helper.getPersistService().getInnerId(linkOid));
						// ���ŵ���ַ�����LINK���ʶ
						disObject.setDisOrderObjLinkClassId(Helper.getPersistService().getClassId(linkOid));
						createDistributeInfo(disObject);
						//����Ǹ��ĵ������Ƿ������Ϣ
						DistributeOrderObjectLink linkObj = (DistributeOrderObjectLink) Helper.getPersistService()
								.getObject(linkOid);
						DistributeObject distributeObject = (DistributeObject) linkObj.getTo();
						Persistable disObj = Helper.getPersistService().getObject(
								distributeObject.getDataClassId() + ":" + distributeObject.getDataInnerId());
						if (disObj instanceof ECO) {//���ĵ�
							ECO ecoDis = (ECO) disObj;
							//�����Ƿ������Ϣ
							DisInfoIsTrackService isTrackService = DistributeHelper.getDisInfoIsTrackService();
							isTrackService.createDisInfoIsTrack(disObject, ecoDis.getIsTrack());
						}
						if (disObj instanceof TNO) {
							//����֪ͨ��
							TNO tnoDis = (TNO) disObj;
							//�����Ƿ������Ϣ
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
		//�жϷַ���ʽ
		List<DistributeOrder> orderList = Helper.getPersistService().query(sql, DistributeOrder.class, linkOid);
		DistributeOrder order = orderList.get(0);
		return order;
	}

	/**
	 * A�汾������׶���׷�ӷַ���Ϣ��
	 * 
	 * @param disOrdOid
	 *            String
	 * @param linkList
	 *            List<Map<String, String>>
	 */
	private void addSuitInfo(String disOrdOid, List<Map<String, String>> linkList) {
		// �ַ��������
		DistributeObjectService objService = DistributeHelper.getDistributeObjectService();
		// �׶���
		Persistable dataObject = Helper.getPersistService().getObject(linkList.get(0).get("DATAOID"));
		String disOrdObjLinkOid = linkList.get(0).get("LINKOID");
		String suitFlag = linkList.get(0).get("FLAG");
		ATSuit suit = (ATSuit) dataObject;
		// ȡ����һ���汾���׶����InnerId,ClassId
		String beforeClassId = suit.getIterationInfo().getPredecessorRef().getClassId();
		String beforeInnerId = suit.getIterationInfo().getPredecessorRef().getInnerId();

		// ��һ���汾���׶���
		Persistable beforeObject = Helper.getPersistService().getObject(beforeClassId + ":" + beforeInnerId);
		boolean atsuitFlag = isOneDisStyle(Helper.getOid(beforeObject));
		// ��һ���汾���׶����Ƿ���һ���Է��Ű汾������׷�ӷַ���Ϣ��û����׷��
		if (!atsuitFlag) {
			// δ���Ź�
			if (ConstUtil.C_S_ZERO.equals(suitFlag)) {
				// ��һ���汾���׶���ķַ���Ϣ���Ƹ����ڵ��׶���
				addRefChangeInfo(beforeObject, disOrdObjLinkOid, disOrdOid);
			}

			// �����Ƕ����ڶ���ķַ���Ϣ�Ĵ���
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
				// δ���Ź�
				if (ConstUtil.C_S_ZERO.equals(flag)) {
					linkOidList.add(linkOid);
					if (obj instanceof Changed) {
						// �ҵ����ڶ�������ĸ��ĵ�
						List<ECO> listAfter =  Helper.getChangeService().getRelatedECOList(obj);
						if (listAfter.size() == 0) {
							// Ĭ�Ϸַ���Ϣ�����뱣��
							createDefaultDistributeInfo(linkOid,infoConfigList);
						} else {
							// ���ĵ�����
							Change change = (Change) listAfter.get(0);
							// ����ǰ����
							ChangedLink changeLink = changeSerive.getChangedByChangedAfter(change , obj);
							if (changeLink == null || changeLink.getFrom() == null) {
								// Ĭ�Ϸַ���Ϣ�����뱣��
								createDefaultDistributeInfo(linkOid,infoConfigList);
							} else {
								Persistable changed = changeLink.getFrom();
								boolean suitedFlag = isOneDisStyle(Helper.getOid(changed));
								// ��һ���汾�����ڶ����Ƿ���һ���Է��Ű汾������׷�ӷַ���Ϣ��û����׷��
								if (!suitedFlag) {
									List<DistributeObject> disObjchangedList = objService.getDistributeObjectsByDataOid(Helper
											.getOid(changed));
									// ��ǰ����û�з��Ź����ĺ����׷��Ĭ�Ϸַ���Ϣ
									if (disObjchangedList == null || disObjchangedList.isEmpty()) {
										// Ĭ�Ϸַ���Ϣ�����뱣��
										createDefaultDistributeInfo(linkOid,infoConfigList);
										// ��ǰ���󷢷Ź����ĺ����׷�Ӹ�ǰ����ķַ���Ϣ
									} else {
										// ����ǰ����ķַ���Ϣ���Ƹ����ĺ����
										addRefChangeInfo(changed, linkOid, disOrdOid);
										
										// ���ĺ����ķַ���Ϣ���Ƹ����ĵ�
										addRefEcoInfo(disOrdOid, linkOidList, change);
									}
								}
							}
						}
					} else if(obj instanceof ECO){
						continue;
					} else {
						// Ĭ�Ϸַ���Ϣ�����뱣��
						createDefaultDistributeInfo(linkOid,infoConfigList);
					}

				}
			}
			
		}
	}

	/**
	 * ���ĺ����ķַ���Ϣ���Ƹ����ĵ�
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
		// ��ǰ����û�з��Ź����ĺ����׷��Ĭ�Ϸַ���Ϣ
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
						// �������÷��ŵ���ַ�����link��Ϣ
						disInfoNew.setDisOrderObjLinkId(linkInnerId);
						disInfoNew.setDisOrderObjLinkClassId(linkClassId);
						// ����ַ���Ϣ
						createDistributeInfo(disInfoNew);
						// ����Ǹ��ĵ������Ƿ������Ϣ
						// �����Ƿ������Ϣ
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
			//��ȡ���ŵ������зַ����ݺͷַ���Ϣ���Ա��ܼ���������ʾ
			List<DistributeObject> disObjList = DistributeHelper.getDistributeObjectService().getDistributeObjectsByDistributeOrderOid(disOrderOid);
			int securityLevel = 0;
			int objectBigLevel = 0;
			for(DistributeObject disObj : disObjList){
				//ȡ�����ж��������ܼ�
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
							//���������ܼ������˵��ܼ�������ʾ�����ύ�ַ�
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
