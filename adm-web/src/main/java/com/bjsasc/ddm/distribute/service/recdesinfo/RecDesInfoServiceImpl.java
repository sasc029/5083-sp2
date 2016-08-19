package com.bjsasc.ddm.distribute.service.recdesinfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.recdesinfo.RecDesInfo;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService;
import com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService;
import com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.util.StringUtil;
import com.cascc.avidm.util.SplitString;

public class RecDesInfoServiceImpl implements RecDesInfoService {

	/** �������ڷ��� */
	private final DistributeLifecycleService lifeService = DistributeHelper.getDistributeLifecycleService();
	/** ���ŵ���ַ�������Ϣ���� */
	private final DistributeOrderObjectLinkService disLinkService = DistributeHelper
			.getDistributeOrderObjectLinkService();
	/** �ַ����ݷ��� */
	private final DistributeObjectService disObjService = DistributeHelper.getDistributeObjectService(); 
	/** ���ŵ����� */
	private final DistributeOrderService distributeOrderService = DistributeHelper.getDistributeOrderService(); 

	@Override
	public List<RecDesInfo> getRecDesInfosByOrderObjectLinkList(List<DistributeOrderObjectLink> listOrderObjectLink) {
		List<RecDesInfo> list = new ArrayList<RecDesInfo>();
		Map<String, RecDesInfo> oidMap = new HashMap<String, RecDesInfo>();
		
		String resDesInfohql = "from RecDesInfo t where t.disOrderObjectLinkId=? and t.disOrderObjectLinkClassId=?";
		if (listOrderObjectLink != null && listOrderObjectLink.size() > 0) {
			for (DistributeOrderObjectLink link : listOrderObjectLink) {
				String innerId = link.getInnerId();
				String classId = link.getClassId();
				List<RecDesInfo> infos = PersistHelper.getService().find(resDesInfohql, innerId, classId);
				for (RecDesInfo info : infos) {
					if (list.contains(info)) {
						oidMap.get(info.getKey()).addOid(info.getOid());
						continue;
					}
					info.addOid(info.getOid());
					oidMap.put(info.getKey(), info);
					list.add(info);
				}
			}
		}
		return list;
	}

	@Override
	public List<RecDesInfo> getRecDesInfosByDistributeOrderInnerId(String innerId) {
		String hql = "from DistributeOrderObjectLink t where t.fromObjectRef.innerId=?";

		List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, innerId);

		List<RecDesInfo> list = new ArrayList<RecDesInfo>();

		hql = "from RecDesInfo t where t.disOrderObjectLinkId=?";
		if (links != null && links.size() > 0) {
			for (DistributeOrderObjectLink link : links) {
				innerId = link.getInnerId();
				List<RecDesInfo> infos = PersistHelper.getService().find(hql, innerId);
				list.addAll(infos);
			}
		}
		return list;
	}
	
	/*
	 * ���� Javadoc��
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#getAllRecInfo(String distributeOid)
	 */
	@Override
	public List<RecDesInfo> getAllRecInfo(String distributeOrderOid, List<String> linkOidList) {
		//����չʾ���ݼ���
		List<RecDesInfo> list = new ArrayList<RecDesInfo>();
		//ȥ�ر�ʾ�����ϲ��������ж�
		List<String> keyList = new ArrayList<String>();
		List<DistributeOrderObjectLink> linksAll = new ArrayList<DistributeOrderObjectLink>();

		//δѡ�񷢷����ݣ����ݷ��ŵ���÷���linkId
		if (linkOidList == null || linkOidList.isEmpty()) {
			String hql = "from DistributeOrderObjectLink t "
					+ "where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? ";
			String distributeOrderInnerId = Helper.getInnerId(distributeOrderOid);
			String distributeOrderClassId = Helper.getClassId(distributeOrderOid);

			List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, distributeOrderInnerId,
					distributeOrderClassId);
			if(links != null && !links.isEmpty()){
				linksAll.addAll(links);
			}
		} else {
			String hql = "from DistributeOrderObjectLink t " + "where t.innerId=? and t.classId=? ";
			for (String linkOid : linkOidList) {
				String innerId = Helper.getInnerId(linkOid);
				String classId = Helper.getClassId(linkOid);

				List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, innerId, classId);
				linksAll.addAll(links);
			}
		}
		String hql = "from RecDesInfo r where r.disOrderObjectLinkId=? and r.disOrderObjectLinkClassId=?";
		if (linksAll != null && linksAll.size() > 0) {
			for (DistributeOrderObjectLink link : linksAll) {
				String disOrderObjectLinkId = link.getInnerId();
				String disOrderObjectLinkClassId = link.getClassId();

				//��ѯ�������ݶ���������Щ��λ����Ա
				List<RecDesInfo> infos = PersistHelper.getService().find(hql, disOrderObjectLinkId, disOrderObjectLinkClassId);
				for (RecDesInfo info : infos) {

					//���������ͬ�ģ����ŷ��������ٷ�������Ҫ���ٷ�������ע�����������ݺϲ�
					if (keyList.contains(info.getKey())) {
						int index = keyList.indexOf(info.getKey());
						list.get(index).addOid(info.getOid());
						continue;
					}
					info.addOid(info.getOid());
					info.setDisDeadLine(link.getDisDeadLine());
					info.setDisUrgent(link.getDisUrgent());
					//��Ӳ�����Ҫ�ϲ��Ļ�������
					list.add(info);
					keyList.add(info.getKey());
				}
			}
		}
		return list;
	}
	
	/*
	 * ���� Javadoc��
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#getAllDesInfo(String distributeOid)
	 */
	@Override
	public List<RecDesInfo> getAllDesInfo(String distributeOrderOid, List<String> linkOidList) {
		//����չʾ���ݼ���
		List<RecDesInfo> list = new ArrayList<RecDesInfo>();
		//ȥ�ر�ʾ�����ϲ��������ж�
		List<String> keyList = new ArrayList<String>();
		List<DistributeOrderObjectLink> linksAll = new ArrayList<DistributeOrderObjectLink>();

		//δѡ�񷢷����ݣ����ݷ��ŵ���÷���linkId
		if (linkOidList == null || linkOidList.isEmpty()) {
			String hql = "from DistributeOrderObjectLink t "
					+ "where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? ";
			String distributeOrderInnerId = Helper.getInnerId(distributeOrderOid);
			String distributeOrderClassId = Helper.getClassId(distributeOrderOid);

			List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, distributeOrderInnerId,
					distributeOrderClassId);
			if(links != null && !links.isEmpty()){
				linksAll.addAll(links);
			}
		} else {
			String hql = "from DistributeOrderObjectLink t " + "where t.innerId=? and t.classId=? ";
			for (String linkOid : linkOidList) {
				String innerId = Helper.getInnerId(linkOid);
				String classId = Helper.getClassId(linkOid);

				List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, innerId, classId);
				linksAll.addAll(links);
			}
		}
		String hql = "from RecDesInfo r where r.disOrderObjectLinkId=? and r.disOrderObjectLinkClassId=?";
		if (linksAll != null && linksAll.size() > 0) {
			//ѭ���ϲ�������Ϣ
			for (DistributeOrderObjectLink link : linksAll) {
				String disOrderObjectLinkId = link.getInnerId();
				String disOrderObjectLinkClassId = link.getClassId();

				//��ѯ�������ݶ���������Щ��λ����Ա
				List<RecDesInfo> infos = PersistHelper.getService().find(hql, disOrderObjectLinkId, disOrderObjectLinkClassId);
				for (RecDesInfo info : infos) {
					//���������ͬ�ģ����ŷ��������ٷ�������Ҫ���ٷ�������ע�����������ݺϲ�
					if (keyList.contains(info.getKey2())) {
						int index = keyList.indexOf(info.getKey2());
						list.get(index).addOid(info.getOid());
						continue;
					}
					info.addOid(info.getOid());
					info.setDisDeadLine(link.getDisDeadLine());
					info.setDisUrgent(link.getDisUrgent());
					//��Ӳ�����Ҫ�ϲ��Ļ�������
					list.add(info);
					keyList.add(info.getKey2());
				}
			}
		}
		return list;
	}

	/*
	 * ���� Javadoc��
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#addResDistributeOrder(String orderOidsStr,
			String number, String name, String orderType, String note)
	 */
	@Override
	public DistributeOrder addResDistributeOrder(String orderOidsStr,
			String number, String name, String orderType, String note) {
		
		List<DistributeObject> arrayList=new ArrayList<DistributeObject>();
		//���������ŵ��ķ�������ͳһ��һ��Set������
		List<String> orderOidList = SplitString.string2List(orderOidsStr, ",");
		//���ݷ��ŵ�OID��÷�������
		List<DistributeObject> disObjectList = disObjService
				.getDistributeObjectsByDistributeOrderOid(orderOidList.get(0));
		for (DistributeObject distributeObject : disObjectList) {

			List<DistributeOrderObjectLink> link = disLinkService
					.getDistributeOrderObjectLinkListByDistributeObjectOid(Helper
							.getOid(distributeObject));

			// ���������ʶ�Ž�distributeObject�� DisplayName��������
			distributeObject.setDisplayName(link.get(0).getIsMaster());
			arrayList.add(distributeObject);
		}

		//��÷��ŵ�
		DistributeOrder disOrder = (DistributeOrder) PersistHelper.getService().getObject(orderOidList.get(0));
		//�����µĻ��յ�
		DistributeOrder recOrder = distributeOrderService.newDistributeOrder();

		//��ʼ��InnerId
		disOrder.setInnerId(recOrder.getInnerId());
		//���
		disOrder.setNumber(number);
		//����
		disOrder.setName(name);
		//���ŵ�����
		disOrder.setOrderType(orderType);
		//��ע
		disOrder.setNote(note);
		//��ʼ����������
		lifeService.initLifecycle(disOrder);
		
		disOrder.setContextInfo(arrayList.get(0).getContextInfo());
		// ���û��յ�����Ϣ
		disOrder.setDomainInfo(arrayList.get(0).getDomainInfo());
		
		distributeOrderService.createDistributeOrder(disOrder);
		
		//��������յ�������DistributeOrderObjectLink
		for (DistributeObject distributeObject : arrayList) {
			//���յ�����������OID
			disObjService.createDistributeOrderObjectLink(recOrder, distributeObject, distributeObject.getDisplayName());

		}
		
		return disOrder;
		
	}

	/*
	 * ���� Javadoc��
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#addDesDistributeOrder(String orderOidsStr,
			String number, String name, String orderType, String note)
	 */
	@Override
	public DistributeOrder addDesDistributeOrder(String orderOidsStr,
			String number, String name, String orderType, String note) {

		List<DistributeObject> arrayList=new ArrayList<DistributeObject>();
		
		//���������ŵ��ķ�������ͳһ��һ��Set������
		List<String> orderOidList = SplitString.string2List(orderOidsStr, ",");

		//��ȡ���ŵ�
		List<DistributeObject> disObjectList = disObjService
				.getDistributeObjectsByDistributeOrderOid(orderOidList.get(0));
		for (DistributeObject distributeObject : disObjectList) {

			List<DistributeOrderObjectLink> link = disLinkService
					.getDistributeOrderObjectLinkListByDistributeObjectOid(Helper
							.getOid(distributeObject));
			// ���������ʶ�Ž�distributeObject�� DisplayName��������
			distributeObject.setDisplayName(link.get(0).getIsMaster());
			arrayList.add(distributeObject);
		}
		
		//��÷��ŵ�
		DistributeOrder disOrder = (DistributeOrder) PersistHelper.getService().getObject(orderOidList.get(0));
		//�������ٵ�
		DistributeOrder decOrder = distributeOrderService.newDistributeOrder();

		//��ʼ��InnerId
		disOrder.setInnerId(decOrder.getInnerId());
		//���
		disOrder.setNumber(number);
		//����
		disOrder.setName(name);
		//���ŵ�����
		disOrder.setOrderType(orderType);
		//��ע
		disOrder.setNote(note);
		
		lifeService.initLifecycle(disOrder);
		
		disOrder.setContextInfo(arrayList.get(0).getContextInfo());
		// �������ٵ�����Ϣ
		disOrder.setDomainInfo(arrayList.get(0).getDomainInfo());
		
		distributeOrderService.createDistributeOrder(disOrder);
		
		//���������ٵ�������DistributeOrderObjectLink
		for (DistributeObject distributeObject : arrayList) {
			disObjService.createDistributeOrderObjectLink(disOrder, distributeObject, distributeObject.getDisplayName());
		}
		
		return disOrder;
		
	}

	/*
	 * ���� Javadoc��
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#getAllNeddRecInfosByDistributeOrderOid(String distributeOrderOid, List<String> linkOids)
	 */
	@Override
	public List<Map<String, Object>> getAllNeddRecInfosByDistributeOrderOid(
			String distributeOrderOid, List<String> linkOids) {
		//��Ҫ���յķ�����Ϣ����
		List<Map<String, Object>> resultListDisInfos = new ArrayList<Map<String,Object>>();
		//ȥ�ر�Ƿ�����
		List<String> keylist = new ArrayList<String>();
		if(linkOids.size() == 0) {
			//��ѯ���ŵ�����Ӧ��DistributeOrderObjectLink
			List<DistributeOrderObjectLink> links = getObjectLinkByDistributeOrderOid(distributeOrderOid);

			linkOids = new ArrayList<String>();

			for(DistributeOrderObjectLink link : links){
				//ƴoid
				String linkOid = link.getClassId()+":"+link.getInnerId();
				//��ӵ�������
				linkOids.add(linkOid);
			}
		}

		for(String linkOid : linkOids){
			//link��InnerId
			String linkInnerId = Helper.getInnerId(linkOid);
			//link��classId
			String linkClassId = Helper.getClassId(linkOid);
			String sql = " SELECT C.DISINFONAME, C.DISINFOID, C.DISINFOTYPE, C.DISORDEROBJLINKID, C.DISORDEROBJLINKCLASSID, "
					+ " sum(C.DISINFONUM) DISINFONUM, sum(C.RECOVERNUM) RECOVERNUM, C.DISMEDIATYPE, B.ID, B.NAME "
					+ " FROM  DDM_DIS_ORDEROBJLINK A, DDM_DIS_OBJECT B, DDM_DIS_INFO C, "
					+ " (SELECT DISTINCT D.INNERID INNERID, D.CLASSID CLASSID ,D.TOOBJECTCLASSID TOOBJECTCLASSID,D.TOOBJECTID TOOBJECTID "
					+ " FROM DDM_DIS_ORDEROBJLINK D WHERE D.INNERID =? AND D.CLASSID =?) E"
					+ " WHERE A.TOOBJECTCLASSID = B.CLASSID AND A.TOOBJECTID = B.INNERID "
					+ " AND A.CLASSID = C.DISORDEROBJLINKCLASSID AND A.INNERID = C.DISORDEROBJLINKID "
					+ " AND C.DISMEDIATYPE = ? "
					+ " AND A.INNERID <> E.INNERID AND A.CLASSID = E.CLASSID AND A.TOOBJECTCLASSID=E.TOOBJECTCLASSID AND A.TOOBJECTID = E.TOOBJECTID"
					+ " GROUP BY C.DISINFONAME, C.DISINFOID, C.DISINFOTYPE, "
					+ " C.DISORDEROBJLINKID, C.DISORDEROBJLINKCLASSID, C.DISMEDIATYPE, B.ID, B.NAME ";
			//ֻ��ʾֽ�ʷַ���Ϣ
			String disMediaType = ConstUtil.C_DISMEDIATYPE_0;
			//ִ�в�ѯ
			List<Map<String, Object>> listDisInfos = Helper.getPersistService().query(sql, linkInnerId, linkClassId, disMediaType);
			//ѭ��������������ķ�����Ϣ
			for(Map<String, Object> listdisInfo : listDisInfos) {
				// ���ŵ�linkId
				String linkId = (String) listdisInfo.get("DISORDEROBJLINKID");
				// ���ŵ�linkClass
				String linkClass = (String) listdisInfo.get("DISORDEROBJLINKCLASSID");
				// �ַ���ϢIID����Ա����֯���ڲ���ʶ��
				String listDisInfo = (String) listdisInfo.get("DISINFOID");
				// �ַ���Ϣ���ƣ���λ/��Ա��
				String listtDisName = (String) listdisInfo.get("DISINFONAME");
				// ���ŷ���
				long listDisInfoNum = ((BigDecimal)listdisInfo.get("DISINFONUM")).longValue();
				// ���շ���
				long listRecoverNum = ((BigDecimal)listdisInfo.get("RECOVERNUM")).longValue();
				//��������
				long listDisMediaType = ((BigDecimal)listdisInfo.get("DISMEDIATYPE")).longValue();
				//�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
				long disInfoType = ((BigDecimal)listdisInfo.get("DISINFOTYPE")).longValue();

				//�ж��Ƿ��Ѿ������Ѿ���ͬ�������ķ������ݣ����кϲ�
				String stringFlag = recInfoCombine(listDisInfo, listtDisName, listDisInfoNum, listRecoverNum, listDisMediaType, disInfoType);

				//����ϢIID����Ա����֯���ڲ���ʶ�����ַ���Ϣ���ƣ���λ/��Ա�������ŷ��������շ������������� �����кϲ�
				if(keylist.contains(stringFlag)) {
					//��ͬ�ĺϲ���ʾ������
					int index = keylist.indexOf(stringFlag);
					String disInfoObjLinkId = (String) resultListDisInfos.get(index).get("DISORDEROBJLINKID");
					String disInfoObjLinkClassId = (String) resultListDisInfos.get(index).get("DISORDEROBJLINKCLASSID");
					disInfoObjLinkId = disInfoObjLinkId + ";" + linkId;
					disInfoObjLinkClassId = disInfoObjLinkClassId + ";" + linkClass;

					//����ϢIID����Ա����֯���ڲ���ʶ�����ַ���Ϣ���ƣ���λ/��Ա��ƴ��
					resultListDisInfos.get(index).put("DISORDEROBJLINKID", disInfoObjLinkId);
					resultListDisInfos.get(index).put("DISORDEROBJLINKCLASSID", disInfoObjLinkClassId);
					//��շ�����������
					resultListDisInfos.get(index).put("ID", "");
					resultListDisInfos.get(index).put("NAME", "");

					continue;
				}
				//��Ӻϲ��жϱ�Ƿ�
				keylist.add(stringFlag);
				//�ж���ӻ�����Ϣ�б�
				resultListDisInfos.add(listdisInfo);

			}
		}

		
		return resultListDisInfos;
	}

	/*
	 * ���� Javadoc��
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#getAllNeddDesInfosByDistributeOrderOid(String distributeOrderOid, List<String> linkOids)
	 */
	@Override
	public List<Map<String, Object>> getAllNeddDesInfosByDistributeOrderOid(
			String distributeOrderOid, List<String> linkOids) {
		//��Ҫ���ٵķ�����Ϣ����
		List<Map<String, Object>> resultListDisInfos = new ArrayList<Map<String,Object>>();
		//ȥ�ر�Ƿ�����
		List<String> keylist = new ArrayList<String>();

		if(linkOids.size() == 0) {
			//��ѯ���ŵ�����Ӧ��DistributeOrderObjectLink
			List<DistributeOrderObjectLink> links =  getObjectLinkByDistributeOrderOid(distributeOrderOid);

			linkOids = new ArrayList<String>();

			for(DistributeOrderObjectLink link : links){
				//ƴoid
				String linkOid = link.getClassId()+":"+link.getInnerId();
				//��ӵ�������
				linkOids.add(linkOid);
			}
		}

		for(String linkOid : linkOids){
			//link��InnerId
			String linkInnerId = Helper.getInnerId(linkOid);
			//link��classId
			String linkClassId = Helper.getClassId(linkOid);
			String sql = " SELECT C.DISINFONAME, C.DISINFOID,C.DISINFOTYPE, C.DISORDEROBJLINKID, C.DISORDEROBJLINKCLASSID, "
					+ " sum(C.DISINFONUM) DISINFONUM, sum(C.RECOVERNUM) RECOVERNUM, sum(C.DESTROYNUM) DESTROYNUM, C.DISMEDIATYPE, B.ID, B.NAME "
					+ " FROM  DDM_DIS_ORDEROBJLINK A, DDM_DIS_OBJECT B, DDM_DIS_INFO C, "
					+ " (SELECT DISTINCT D.INNERID INNERID, D.CLASSID CLASSID ,D.TOOBJECTCLASSID TOOBJECTCLASSID,D.TOOBJECTID TOOBJECTID "
					+ " FROM DDM_DIS_ORDEROBJLINK D WHERE D.INNERID =? AND D.CLASSID =?) E"
					+ " WHERE A.TOOBJECTCLASSID = B.CLASSID AND A.TOOBJECTID = B.INNERID "
					+ " AND A.CLASSID = C.DISORDEROBJLINKCLASSID AND A.INNERID = C.DISORDEROBJLINKID "
					+ " AND  C.DISMEDIATYPE = ? "
					+ " AND A.INNERID <> E.INNERID AND A.CLASSID = E.CLASSID AND A.TOOBJECTCLASSID=E.TOOBJECTCLASSID AND A.TOOBJECTID = E.TOOBJECTID"
					+ " GROUP BY C.DISINFONAME, C.DISINFOID,C.DISINFOTYPE, "
					+ " C.DISORDEROBJLINKID, C.DISORDEROBJLINKCLASSID, C.DISMEDIATYPE, B.ID, B.NAME ";
			//ֻ��ʾֽ�ʷַ���Ϣ
			String disMediaType = ConstUtil.C_DISMEDIATYPE_0;
			//ִ�в�ѯ
			List<Map<String, Object>> listDisInfos = Helper.getPersistService().query(sql, linkInnerId, linkClassId, disMediaType);
			//ѭ��������������ķ�����Ϣ
			for(Map<String, Object> listdisInfo : listDisInfos) {

				// ���ŵ�linkId
				String linkId = (String) listdisInfo.get("DISORDEROBJLINKID");
				// ���ŵ�linkClass
				String linkClass = (String) listdisInfo.get("DISORDEROBJLINKCLASSID");
				// �ַ���ϢIID����Ա����֯���ڲ���ʶ��
				String listDisInfo = (String) listdisInfo.get("DISINFOID");
				// �ַ���Ϣ���ƣ���λ/��Ա��
				String listtDisName = (String) listdisInfo.get("DISINFONAME");
				// ���ŷ���
				long listDisInfoNum = ((BigDecimal)listdisInfo.get("DISINFONUM")).longValue();
				// ���ٷ���
				long listDestroyNum = ((BigDecimal)listdisInfo.get("DESTROYNUM")).longValue();
				//��������
				long listDisMediaType = ((BigDecimal)listdisInfo.get("DISMEDIATYPE")).longValue();
				//�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
				long disInfoType = ((BigDecimal)listdisInfo.get("DISINFOTYPE")).longValue();

				//�ж��Ƿ��Ѿ������Ѿ���ͬ�������ķ������ݣ����кϲ�
				String stringFlag = desInfoCombine(listDisInfo, listtDisName, listDisInfoNum, listDestroyNum, listDisMediaType, disInfoType);
				//����ϢIID����Ա����֯���ڲ���ʶ�����ַ���Ϣ���ƣ���λ/��Ա�������ŷ��������ٷ������������� �����кϲ�
				if(keylist.contains(stringFlag)) {
					//��ͬ�ĺϲ���ʾ������
					int index = keylist.indexOf(stringFlag);
					String disInfoObjLinkId = (String) resultListDisInfos.get(index).get("DISORDEROBJLINKID");
					String disInfoObjLinkClassId = (String) resultListDisInfos.get(index).get("DISORDEROBJLINKCLASSID");
					disInfoObjLinkId = disInfoObjLinkId + ";" + linkId;
					disInfoObjLinkClassId = disInfoObjLinkClassId + ";" + linkClass;

					//����ϢIID����Ա����֯���ڲ���ʶ�����ַ���Ϣ���ƣ���λ/��Ա��ƴ��
					resultListDisInfos.get(index).put("DISORDEROBJLINKID", disInfoObjLinkId);
					resultListDisInfos.get(index).put("DISORDEROBJLINKCLASSID", disInfoObjLinkClassId);
					//��շ�����������
					resultListDisInfos.get(index).put("ID", "");
					resultListDisInfos.get(index).put("NAME", "");
					continue;
				}

				keylist.add(stringFlag);
				resultListDisInfos.add(listdisInfo);

			}

		}
		return resultListDisInfos;

	}

	/*
	 * ���� Javadoc��
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#addNeddRecInfos(String disInfoNames, String disInfoIds,
			String disOrderObjLinkIds,
			String disOrderObjLinkClassIds, String disMediaTypes,
			String disInfoNums, String needDestroyNums, String distributeOrderOid, String notes, String disInfoTypes)
	 */
	@Override
	public String addNeddRecInfos(String disInfoNames, String disInfoIds,
			String disOrderObjLinkIds, String disOrderObjLinkClassIds, String disMediaTypes,
			String disInfoNums, String needRecoverNums, 
			String recoverNums,String distributeOrderOid, String notes, String disInfoTypes) {
		//���������Ϣ����
		List<RecDesInfo> saveRecDesInfos = new ArrayList<RecDesInfo>();
		//���»�����Ϣ����
		List<RecDesInfo> updateRecDesInfos = new ArrayList<RecDesInfo>();
		//��Ŵ�����Ϣ
		StringBuffer result = new StringBuffer();
		//�ַ���Ϣ���ƣ���λ/��Ա������
		List<String> disInfoNameArr = SplitString.string2List(disInfoNames, ",");
		//�ַ���ϢIID����Ա����֯���ڲ���ʶ��
		List<String> disInfoIdArr = SplitString.string2List(disInfoIds, ",");
		//�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա������
		List<String> disInfoTypeArr = SplitString.string2List(disInfoTypes, ",");
		//���ŵ���ַ�����LINK�ڲ���ʶ
		List<String> disOrderObjLinkIdArr = SplitString.string2List(disOrderObjLinkIds, ",");
		//���ŵ���ַ�����LINK���ʶ
		List<String> disOrderObjLinkClassIdArr = SplitString.string2List(disOrderObjLinkClassIds, ",");
		//�ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
		List<String> disMediaTypeArr = SplitString.string2List(disMediaTypes, ",");
		//�ַ�����
		List<String> disInfoNumArr = SplitString.string2List(disInfoNums, ",");
		//�����ٷ�������
		List<String> recoverNumArr = SplitString.string2List(recoverNums, ",");
		//��Ҫ���շ���
		List<String> needRecoverNumArr = SplitString.string2List(needRecoverNums, ",");
		//��ע
		List<String> noteArr = SplitString.string2List(notes, ",");

		for(int i = 0; i < disOrderObjLinkIdArr.size(); i++){
			//�ַ���ϢIID����Ա����֯���ڲ���ʶ��
			String disInfoId = disInfoIdArr.get(i);
			//���ŵ���ַ�����LINK�ڲ���ʶ
			String disOrderObjLinkId = disOrderObjLinkIdArr.get(i);
			//���ŵ���ַ�����LINK���ʶ
			String disOrderObjLinkClassId = disOrderObjLinkClassIdArr.get(i);
			//�ַַ���Ϣ���ƣ���λ/��Ա��
			String disInfoName = disInfoNameArr.get(i);
			//�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
			String disInfoType = "";
			if(ConstUtil.C_DISINFOTYPE_0.equals(disInfoTypeArr.get(i))){
				disInfoType = ConstUtil.C_DISINFOTYPE_USER;
			}else if(ConstUtil.C_DISINFOTYPE_1.equals(disInfoTypeArr.get(i))){
				disInfoType = ConstUtil.C_DISINFOTYPE_ORG;
			}
			//�ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
			String disMediaType = "";
			if ((ConstUtil.C_PAPERTASK).equals(disMediaTypeArr.get(i))) {
				disMediaType = ConstUtil.C_DISMEDIATYPE_0;
			} else if ((ConstUtil.C_ELECTASK).equals(disMediaTypeArr.get(i))) {
				disMediaType =  ConstUtil.C_DISMEDIATYPE_1;
			}
			//���ŷ���
			long disInfoNum = Long.parseLong(disInfoNumArr.get(i));
			//�ѻ��շ���
			long recoverNum = Long.parseLong(recoverNumArr.get(i));
			//�����Ҫ���շ���
			long needRecoverNum = Long.parseLong(needRecoverNumArr.get(i));
			//��ע
			String note = "";
			if(!StringUtil.isStringEmpty(noteArr.get(i))){
				note = noteArr.get(i);
			}

			//�ϲ����ݵ�LinkId����
			List<String> ObjLinkIdArr = SplitString.string2List(disOrderObjLinkId, ";");
			//�ϲ����ݵ�LinkClassId����
			List<String> ObjLinkClassIdArr = SplitString.string2List(disOrderObjLinkClassId, ";");
			for(int j = 0 ; j < ObjLinkIdArr.size(); j++) {
				String linkId = ObjLinkIdArr.get(j);
				String ClassId = ObjLinkClassIdArr.get(j);
				//��ѯ�Ѿ���ӹ��Ļ�����Ϣ
				List<Map<String, Object>> listMap = searchDisRecInfoByDisOid(distributeOrderOid, disInfoId,
						linkId, ClassId, disMediaType);
	
				//�Ѿ����ڸĻ�����Ϣ�������²���
				if(listMap.size() > 0){
					//�Ѿ���ӵĻ��շ���
					long needRecInfoNum = ((BigDecimal)listMap.get(0).get("NEEDRECOVERNUM")).longValue();
					
					long totalNum = needRecInfoNum + needRecoverNum;
					
					//�жϷ��ŷ����Ƿ������Ҫ���շ���
					if((disInfoNum - recoverNum) >= totalNum){
						String recDesInfoInnerId = (String) listMap.get(0).get("INNERID");
						String recDesInfoClassId = (String) listMap.get(0).get("CLASSID");
						//����InnerId��ClassId��ѯ������Ϣ
						RecDesInfo recDesInfo = (RecDesInfo) Helper.getPersistService().getObject(recDesInfoClassId, recDesInfoInnerId);
						//�޸���Ҫ���շ���
						recDesInfo.setNeedRecoverNum(totalNum);
						//�޸ı�ע
						recDesInfo.setNote(note);
						//������µĻ�����Ϣ��������
						updateRecDesInfos.add(recDesInfo);
					}else{
						//�ϲ��͵�����ֻ�������һ��������ʾ
						if(j == 0){
							result.append(disInfoName+"-" + disMediaTypeArr.get(i)+"-�ɻ��յ�ʣ�������" + ( disInfoNum - recoverNum -needRecInfoNum )+"��,");
						}
					}

				}else{//δ���ڻ�����Ϣ������Ӳ���
					RecDesInfo recDesInfo = setDisRecInfo(disInfoId, disInfoName,
							linkId,
							ClassId, needRecoverNum,
							distributeOrderOid, disMediaType, disInfoNum, recoverNum, note, disInfoType);
					//��������ӵĻ�����Ϣ��������
					saveRecDesInfos.add(recDesInfo);
				}

			}

		}
		//�����Ϣ
		String resultMessage = "";
		String SubString = result.toString();
		if(SubString != null && !"".equals(SubString)){
			resultMessage = SubString.substring(0,SubString.length()-1)+"|����ʣ����շ���";
		}

		//�������������Ϣ
		if(updateRecDesInfos.size() > 0) {
			Helper.getPersistService().update(updateRecDesInfos);
		}
		//�������»�����Ϣ
		if(saveRecDesInfos.size() > 0) {
			Helper.getPersistService().save(saveRecDesInfos);
		}
	
		return resultMessage;
	}

	/*
	 * ���� Javadoc��
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#addNeddDesInfos(String disInfoNames, String disInfoIds,
			String disOrderObjLinkIds,
			String disOrderObjLinkClassIds, String disMediaTypes,
			String disInfoNums, String needDestroyNums, String distributeOrderOid,
			String recoverNums, String notes, String disInfoTypes)
	 */
	@Override
	public String addNeddDesInfos(String disInfoNames, String disInfoIds,
			String disOrderObjLinkIds,
			String disOrderObjLinkClassIds, String disMediaTypes,
			String disInfoNums, String needDestroyNums, String destroyNums, String distributeOrderOid,
			String recoverNums, String notes, String disInfoTypes) {
		//����������Ϣ����
		List<RecDesInfo> saveRecDesInfos = new ArrayList<RecDesInfo>();
		//����������Ϣ����
		List<RecDesInfo> updateRecDesInfos = new ArrayList<RecDesInfo>();
		//��Ŵ�����Ϣ
		StringBuffer result = new StringBuffer();
		//�ַ���Ϣ���ƣ���λ/��Ա������
		List<String> disInfoNameArr = SplitString.string2List(disInfoNames, ",");
		//�ַ���ϢIID����Ա����֯���ڲ���ʶ������
		List<String> disInfoIdArr = SplitString.string2List(disInfoIds, ",");
		//�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա������
		List<String> disInfoTypeArr = SplitString.string2List(disInfoTypes, ",");
		//���ŵ���ַ�����LINK�ڲ���ʶ����
		List<String> disOrderObjLinkIdArr = SplitString.string2List(disOrderObjLinkIds, ",");
		//���ŵ���ַ�����LINK���ʶ����
		List<String> disOrderObjLinkClassIdArr = SplitString.string2List(disOrderObjLinkClassIds, ",");
		//�ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ���򣩼���
		List<String> disMediaTypeArr = SplitString.string2List(disMediaTypes, ",");
		//�ַ���������
		List<String> disInfoNumArr = SplitString.string2List(disInfoNums, ",");
		//�����ٷ�������
		List<String> destroyNumArr = SplitString.string2List(destroyNums, ",");
		//�ѻ��շ�������
		List<String> recoverNumArr = SplitString.string2List(recoverNums, ",");
		//��Ҫ���ٷ�������
		List<String> needDestroyNumArr = SplitString.string2List(needDestroyNums, ",");
		//��ע
		List<String> noteArr = SplitString.string2List(notes, ",");

		for(int i = 0; i < disOrderObjLinkIdArr.size(); i++){
			//�ַ���ϢIID����Ա����֯���ڲ���ʶ��
			String disInfoId = disInfoIdArr.get(i).trim();
			//���ŵ���ַ�����LINK�ڲ���ʶ
			String disOrderObjLinkId = disOrderObjLinkIdArr.get(i).trim();
			//���ŵ���ַ�����LINK���ʶ
			String disOrderObjLinkClassId = disOrderObjLinkClassIdArr.get(i).trim();
			//�ַ���ϢIID����Ա����֯���ڲ���ʶ��
			String disInfoName = disInfoNameArr.get(i).trim();
			//�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
			String disInfoType = "";
			if(ConstUtil.C_DISINFOTYPE_0.equals(disInfoTypeArr.get(i))){
				disInfoType = ConstUtil.C_DISINFOTYPE_USER;
			}else if(ConstUtil.C_DISINFOTYPE_1.equals(disInfoTypeArr.get(i))){
				disInfoType = ConstUtil.C_DISINFOTYPE_ORG;
			}
			//�ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
			String disMediaType = "";
			if ((ConstUtil.C_PAPERTASK).equals(disMediaTypeArr.get(i))) {
				disMediaType = ConstUtil.C_DISMEDIATYPE_0;
			} else if ((ConstUtil.C_ELECTASK).equals(disMediaTypeArr.get(i))) {
				disMediaType =  ConstUtil.C_DISMEDIATYPE_1;
			}
			//��ӷ���
			long needDestroyNum = Long.parseLong(needDestroyNumArr.get(i));
			//�����ٵķ���
			long destroyNum = Long.parseLong(destroyNumArr.get(i));
			//�ѻ��յķ���
			long recoverNum = Long.parseLong(recoverNumArr.get(i));
			//���ŷ���
			long disInfoNum = Long.parseLong(disInfoNumArr.get(i));
			//��ע
			String note = "";
			if(!StringUtil.isStringEmpty(noteArr.get(i))){
				note = noteArr.get(i);
			}

			//�ϲ����ݵ�LinkId����
			List<String> ObjLinkIdArr = SplitString.string2List(disOrderObjLinkId, ";");
			//�ϲ����ݵ�LinkClassId����
			List<String> ObjLinkClassIdArr = SplitString.string2List(disOrderObjLinkClassId, ";");
			for(int j = 0 ; j < ObjLinkIdArr.size(); j++) {
				String linkId = ObjLinkIdArr.get(j);
				String ClassId = ObjLinkClassIdArr.get(j);

				//��ѯ�Ѿ���ӹ��Ļ�����Ϣ
				List<Map<String, Object>> listMap = searchDisRecInfoByDisOid(
						distributeOrderOid, disInfoId, linkId,
						ClassId, disMediaType);
	
				//�Ѿ����ڸĻ�����Ϣ�������²���
				if(listMap.size() > 0){
					//�Ѿ���ӵ����ٷ���
					long needDestroyInfoNum = ((BigDecimal)listMap.get(0).get("NEEDDESTROYNUM")).longValue();
					
					long totalNum = needDestroyInfoNum + needDestroyNum;
					
					//�жϷ��ŷ����Ƿ������Ҫ���շ���
					if((disInfoNum -destroyNum) >= totalNum){
						String recDesInfoInnerId = (String) listMap.get(0).get("INNERID");
						String recDesInfoClassId = (String) listMap.get(0).get("CLASSID");
						//����InnerId��ClassId��ѯ������Ϣ
						RecDesInfo recDesInfo = (RecDesInfo) Helper.getPersistService().getObject(recDesInfoClassId, recDesInfoInnerId);
						//�޸���Ҫ���շ���
						recDesInfo.setNeedDestroyNum(totalNum);
						//�޸ı�ע
						recDesInfo.setNote(note);
						//�����Ҫ���»�����Ϣ��������
						updateRecDesInfos.add(recDesInfo);
					}else{
						//�ϲ��͵�����ֻ�������һ��������ʾ
						if(j == 0){
							result.append(disInfoName+"-" + disMediaTypeArr.get(i)+"-�����ٵ�ʣ�������" + (disInfoNum -destroyNum - needDestroyInfoNum )+"��,");
						}
					}
				}else{//δ���ڻ�����Ϣ������Ӳ���
						RecDesInfo recDesInfo = setDisDesInfo(disInfoId, disInfoName, linkId,
								ClassId, needDestroyNum,
								distributeOrderOid, disMediaType, recoverNum, destroyNum,disInfoNum, note, disInfoType);
						saveRecDesInfos.add(recDesInfo);
				}

			}

		}
		//�����Ϣ
		String resultMessage = "";
		String SubString = result.toString();
		if(SubString != null && !"".equals(SubString)){
			resultMessage = SubString.substring(0,SubString.length()-1)+"|����ʣ��δ���ٷ���";;
		}

		//��������������Ϣ
		if(saveRecDesInfos.size() > 0) {
			Helper.getPersistService().save(saveRecDesInfos);
		}
		//��������������Ϣ
		if(updateRecDesInfos.size() > 0) {
			Helper.getPersistService().update(updateRecDesInfos);
		}

		return resultMessage;

	}

	/**
	 * ���յ���ѯ
	 *
	 * @param disOrderOid
	 *			���ŵ�OID
	 * @param disInfoId
	 *			�ַ���ϢIID����Ա����֯���ڲ���ʶ��	
	 * @param disOrderObjLinkId
	 * 			���ŵ���ַ�����LINK�ڲ���ʶ
	 * @param disOrderObjLinkClassId
	 * 			���ŵ���ַ�����LINK���ʶ
	 * @param disMediaType
	 * 			�ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
	 *
	 * @return	List<Map<String, Object>>
	 *			������Ϣ
	 */
	public List<Map<String, Object>> searchDisRecInfoByDisOid(
			String disOrderOid, String disInfoId,
			String disOrderObjLinkId, String disOrderObjLinkClassId, String disMediaType) {
		String classId = Helper.getClassId(disOrderOid);
		String innerId = Helper.getInnerId(disOrderOid);

		String sql = " SELECT A .* FROM DDM_RECDES_INFO A, DDM_DIS_ORDER B, DDM_DIS_ORDEROBJLINK C, "
				+ " (SELECT DISTINCT D.TOOBJECTID, D.TOOBJECTCLASSID FROM DDM_DIS_ORDEROBJLINK D WHERE "
				+ " D.CLASSID = ? AND D.INNERID = ?) E WHERE B.CLASSID = ? AND B.INNERID = ? "
				+ " AND B .CLASSID = C.FROMOBJECTCLASSID AND B .INNERID = C.FROMOBJECTID AND "
				+ " C.TOOBJECTCLASSID = E.TOOBJECTCLASSID AND C.TOOBJECTID = E.TOOBJECTID "
				+ " AND A.DISORDEROBJECTLINKCLASSID = C.CLASSID And A.DISORDEROBJECTLINKID = C.INNERID AND A.DISMEDIATYPE = ? "
				+ "  AND A.DISINFOID = ? AND A.STATENAME = ? ";

		List<Map<String, Object>> listDisInfos = null;
		//��δ�ַ���״̬�Ļ���������Ϣ
		String stateName =  ConstUtil.LC_NOT_DISTRIBUT.getName();;
		//ִ�в�ѯ
		listDisInfos = Helper.getPersistService()
				.query(sql, disOrderObjLinkClassId, disOrderObjLinkId, classId,
						innerId, disMediaType, disInfoId, stateName);

		if(listDisInfos==null || listDisInfos.isEmpty()){
			listDisInfos = new ArrayList<Map<String,Object>>();
		 }
		return listDisInfos;
	}

	/**
	 * ��ӻ�����Ϣ
	 *
	 * @param disInfoId
	 *			�ַ���ϢIID����Ա����֯���ڲ���ʶ��
	 * @param disInfoName
	 *			�ַ���Ϣ���ƣ���λ/��Ա��
	 * @param disOrderObjLinkId
	 *			���ŵ���ַ�����LINK�ڲ���ʶ
	 * @param disOrderObjLinkClassId
	 *			���ŵ���ַ�����LINK���ʶ
	 * @param needRecoverNum
	 *			���շ���
	 * @param distributeOrderOid
	 *			���յ�OID
	 * @param disMediaType
	 *			�ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
	 * @param disInfoNum
	 *			���ŷ���
	 * @param recoverNum
	 *			�ѻ��շ���
	 * @param note
	 *			��ע
	 * @param disInfoType
	 *			�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
	 * @return RecDesInfo
	 *			����������Ϣ
	 */
	public RecDesInfo setDisRecInfo(String disInfoId, String disInfoName,
			String disOrderObjLinkId,
			String disOrderObjLinkClassId, long needRecoverNum,
			String distributeOrderOid,String disMediaType, 
			long disInfoNum, long recoverNum, String note, String disInfoType) {

		//����������Ϣ����
		RecDesInfo recDesInfo = (RecDesInfo) PersistUtil.createObject(RecDesInfo.CLASSID);
		// ��ѯ���ŵ���ַ�����link��
		List<Map<String, Object>> listMap = getDistributeOrderobjectlink(disOrderObjLinkClassId, disOrderObjLinkId, distributeOrderOid);

		if(listMap.size() > 0){
			//��ȡ���ŵ���ַ�����LINK���ʶ
			String disOrderObjectLinkClassId = (String) listMap.get(0).get("CLASSID");
			//��ȡ���ŵ���ַ�����LINK�ڲ���ʶ
			String disOrderObjectLinkId = (String) listMap.get(0).get("INNERID");

			//��ѯ������Ϣ
			List<Map<String, Object>> disInfo = getDistributeInfo(disInfoId, disInfoName);


			//���û�����Ϣ�ڲ���ʶ
			recDesInfo.setClassId(RecDesInfo.CLASSID);
			//������Ҫ���յķ���
			recDesInfo.setNeedRecoverNum(needRecoverNum);
			//���÷��ŵ���ַ�����LINK�ڲ���ʶ
			recDesInfo.setDisOrderObjectLinkClassId(disOrderObjectLinkClassId);
			//���÷��ŵ���ַ�����LINK���ʶ
			recDesInfo.setDisOrderObjectLinkId(disOrderObjectLinkId);
			//�������������IID����Ա�ڲ���ʶ��
			recDesInfo.setDisInfoId(disInfoId);
			//����������������ƣ���Ա��
			recDesInfo.setDisInfoName(disInfoName);
			//�ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ���򣩼���
			recDesInfo.setDisMediaType(disMediaType);
			//�ַ�����
			recDesInfo.setDisInfoNum(disInfoNum);
			//�ѻ��շ���
			recDesInfo.setRecoverNum(recoverNum);
			//��ע
			recDesInfo.setNote(note);
			//�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
			recDesInfo.setDisInfoType(disInfoType);
			//�ַ���ʽ:����
			recDesInfo.setDisType(ConstUtil.C_DISTYPE_REC);

			if(disInfo != null && disInfo.size() > 0){
				String infoClassId = (String) disInfo.get(0).get("INFOCLASSID");
				//���÷ַ���Ϣ�����ʶ����Ա������֯�����ʶ��
				if(infoClassId != null && !"".equals(infoClassId)){
					recDesInfo.setInfoClassId(infoClassId);
					
				}
			}

			//��ʼ����������
			lifeService.initLifecycle(recDesInfo);
			
		}

		return recDesInfo;
	}

	/**
	 * ��ӻ�����Ϣ
	 *
	 * @param disInfoId
	 *			�ַ���ϢIID����Ա����֯���ڲ���ʶ��
	 * @param disInfoName
	 *			�ַ���Ϣ���ƣ���λ/��Ա��
	 * @param disOrderObjLinkId
	 *			���ŵ���ַ�����LINK�ڲ���ʶ
	 * @param disOrderObjLinkClassId
	 *			���ŵ���ַ�����LINK���ʶ
	 * @param needDestroyNum
	 *			���ٷ���
	 * @param distributeOrderOid
	 *			���յ�OID
	 * @param disMediaType
	 *			�ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
	 * @param recoverNum
	 *			�ѻ��շ���
	 * @param destroyNum
	 *			�����ٷ���
	 * @param disInfoNum
	 *			���ŷ���
	 * @param note
	 *			��ע
	 * @param disInfoType
	 *			�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
	 * @return RecDesInfo
	 *			����������Ϣ
	 */
	public RecDesInfo setDisDesInfo(String disInfoId, String disInfoName,
			String disOrderObjLinkId, String disOrderObjLinkClassId, long needDestroyNum,
			String distributeOrderOid,String disMediaType, long recoverNum, 
			long destroyNum, long disInfoNum, String note, String disInfoType) {
		
		//����������Ϣ����
		RecDesInfo recDesInfo = (RecDesInfo) PersistUtil.createObject(RecDesInfo.CLASSID);
		// ��ѯ���ŵ���ַ�����link��
		List<Map<String, Object>> listMap = getDistributeOrderobjectlink(disOrderObjLinkClassId, disOrderObjLinkId, distributeOrderOid);
		if(listMap.size() > 0){
			//��ȡ���ŵ���ַ�����LINK���ʶ
			String disOrderObjectLinkClassId = (String) listMap.get(0).get("CLASSID");
			//��ȡ���ŵ���ַ�����LINK�ڲ���ʶ
			String disOrderObjectLinkId = (String) listMap.get(0).get("INNERID");

			//��ѯ������Ϣ
			List<Map<String, Object>> disInfo = getDistributeInfo(disInfoId, disInfoName);

			//���û�����Ϣ�ڲ���ʶ
			recDesInfo.setClassId(RecDesInfo.CLASSID);
			//������Ҫ���յķ���
			recDesInfo.setNeedDestroyNum(needDestroyNum);
			//���÷��ŵ���ַ�����LINK�ڲ���ʶ
			recDesInfo.setDisOrderObjectLinkClassId(disOrderObjectLinkClassId);
			//���÷��ŵ���ַ�����LINK���ʶ
			recDesInfo.setDisOrderObjectLinkId(disOrderObjectLinkId);
			//�������������IID����Ա�ڲ���ʶ��
			recDesInfo.setDisInfoId(disInfoId);
			//����������������ƣ���Ա��
			recDesInfo.setDisInfoName(disInfoName);
			//�ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ���򣩼���
			recDesInfo.setDisMediaType(disMediaType);
			//�ѻ��շ���
			recDesInfo.setRecoverNum(recoverNum);
			//���ŷ���
			recDesInfo.setDisInfoNum(disInfoNum);
			//�����ٷ���
			recDesInfo.setDestroyNum(destroyNum);
			//�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
			recDesInfo.setDisInfoType(disInfoType);
			//�ַ���ʽ:����
			recDesInfo.setDisType(ConstUtil.C_DISTYPE_DES);

			if(disInfo != null && disInfo.size() > 0){
				String infoClassId = (String) disInfo.get(0).get("INFOCLASSID");
				//���÷ַ���Ϣ�����ʶ����Ա������֯�����ʶ��
				if(infoClassId != null && !"".equals(infoClassId)){
					recDesInfo.setInfoClassId(infoClassId);
					
				}
			}

			//��ʼ����������
			lifeService.initLifecycle(recDesInfo);

		}
		return recDesInfo;
	}


	/**
	 * ��ѯ���ŵ���ַ�����link��
	 * @param disOrderObjLinkClassId
	 *			���ŵ���ַ�����LINK���ʶ
	 * @param disOrderObjLinkId
	 *			���ŵ���ַ�����LINK�ڲ���ʶ
	 * @param disOrderOid
	 *			���յ�OID
	 * @return	List<Map<String, Object>>
	 * 			���ŵ���ַ�����link��
	 */
	public List<Map<String, Object>> getDistributeOrderobjectlink(String disOrderObjLinkClassId, String disOrderObjLinkId, String disOrderOid){
		String classId = Helper.getClassId(disOrderOid);
		String innerId = Helper.getInnerId(disOrderOid);

		String sql = " SELECT DISTINCT A.* FROM DDM_DIS_ORDEROBJLINK A, "
				+ " ( SELECT DISTINCT B.TOOBJECTCLASSID, B.TOOBJECTID FROM DDM_DIS_ORDEROBJLINK B "
				+ " WHERE B.CLASSID = ? AND B.INNERID = ?) C "
				+ " WHERE A.FROMOBJECTCLASSID = ? AND A.FROMOBJECTID = ? "
				+ " AND A.TOOBJECTCLASSID = C.TOOBJECTCLASSID AND A.TOOBJECTID = C.TOOBJECTID ";
		//ִ�в�ѯ
		List<Map<String, Object>> listDisOrderobjectlinks = Helper.getPersistService()
				.query(sql, disOrderObjLinkClassId, disOrderObjLinkId, classId, innerId);
		if(listDisOrderobjectlinks == null || listDisOrderobjectlinks.isEmpty()){
			listDisOrderobjectlinks = new ArrayList<Map<String,Object>>();
		}

		return listDisOrderobjectlinks;
	}

	/*
	 * ���� Javadoc��
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#getRecInfosForEditByOId(String oid)
	 */
	@Override
	public List<RecDesInfo> getRecInfosForEditByOId(String oid) {
		List<RecDesInfo> listRecInfos = new ArrayList<RecDesInfo>();
		//�ָ�oid��oids
		List<String> oidArr = SplitString.string2List(oid, "!");
		for(String oids : oidArr){
			//�ָ�oid��oids����
			List<String> oidAndOids = SplitString.string2List(oids, ",");

			//��һ��ֵ��oid���ڶ�����oids
			String classId = Helper.getClassId(oidAndOids.get(0));
			String innerId = Helper.getInnerId(oidAndOids.get(0));

			String hql = " from RecDesInfo r where r.innerId=? and r.classId = ?";
			List<RecDesInfo> recInfos = Helper.getPersistService().find(hql, innerId, classId);
			//��ѯ��Ҫ�޸ĵĻ���������Ϣ
			if(recInfos.size() > 0){
				RecDesInfo recDesInfo = recInfos.get(0);
				//oidsֵ������У����浽����������Ϣ��
				recDesInfo.setOids(oids);
				listRecInfos.add(recDesInfo);
			}

		}
		return listRecInfos;
	}

	/*
	 * ���� Javadoc��
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#getDesInfosForEditByOId(String oid)
	 */
	@Override
	public List<RecDesInfo> getDesInfosForEditByOId(String oid) {
		List<RecDesInfo> listDesInfos = new ArrayList<RecDesInfo>();
		//�ָ�oid��oids
		List<String> oidArr = SplitString.string2List(oid, "!");
		for(String oids : oidArr){
			//�ָ�oid��oids����
			List<String> oidAndOids = SplitString.string2List(oids, ",");

			//��һ��ֵ��oid���ڶ�����oids
			String classId = Helper.getClassId(oidAndOids.get(0));
			String innerId = Helper.getInnerId(oidAndOids.get(0));

			String hql = " from RecDesInfo r where r.innerId=? and r.classId = ?";
			List<RecDesInfo> desInfos = Helper.getPersistService().find(hql, innerId, classId);
			//��ѯ��Ҫ�޸ĵĻ���������Ϣ
			if(desInfos.size() > 0){
				RecDesInfo recDesInfo = desInfos.get(0);
				//oidsֵ������У����浽����������Ϣ��
				recDesInfo.setOids(oids);
				listDesInfos.add(recDesInfo);
			}

		}
		return listDesInfos;
	}

	/*
	 * ���� Javadoc��
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#updateRecInfos(String recInfoOids, String needRecoverNums,
			String notes, String dismediatypes)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void updateRecInfos(String recInfoOids, String needRecoverNums,
			String notes, String dismediatypes) {
		//���µĻ�����Ϣ���ŵ���Ϣ����
		List<RecDesInfo> updateResInfo = new ArrayList<RecDesInfo>();
		//������ϢOID����
		List<String> recInfoOidArr = SplitString.string2List(recInfoOids, "!");
		//��Ҫ���շ�������
		List<String> needRecoverNumArr = SplitString.string2List(needRecoverNums, ",");
		//��ע����
		List<String> noteArr = SplitString.string2List(notes, ",");
		//���ŷ�ʽ����
		List<String> dismediatypeArr = SplitString.string2List(dismediatypes, ",");

		for(int i = 0; i < recInfoOidArr.size(); i++){
			List<String> oids = SplitString.string2List(recInfoOidArr.get(i), ",");
			//ѭ����ѯ����ѡ��Ļ�����Ϣ���ϲ���δ�ϲ��ģ�
			for(String oid : oids){
				RecDesInfo recDesInfo = (RecDesInfo) Helper.getPersistService().getObject(oid);
				//���û��շ���
				recDesInfo.setNeedRecoverNum(Long.parseLong(needRecoverNumArr.get(i)));
				//���ñ�ע
				String note = "";
				
				if(!StringUtil.isStringEmpty(noteArr.get(i))){
					note = noteArr.get(i);
				}
				
				recDesInfo.setNote(note);
				//���÷ַ�����
				String dismediatype = "";
				if ((ConstUtil.C_PAPERTASK).equals(dismediatypeArr.get(i))) {
					dismediatype = ConstUtil.C_DISMEDIATYPE_0;
				} else if ((ConstUtil.C_ELECTASK).equals(dismediatypeArr.get(i))) {
					dismediatype = ConstUtil.C_DISMEDIATYPE_1;
				} else if ((ConstUtil.C_OUTSITETASK).equals(dismediatypeArr.get(i))) {
					dismediatype = ConstUtil.C_DISMEDIATYPE_2;
				}
				recDesInfo.setDisMediaType(dismediatype);
				updateResInfo.add(recDesInfo);
			}

		}

		//��������
		Helper.getPersistService().update(updateResInfo);
	}

	/*
	 * ���� Javadoc��
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#updateDesInfos(String desInfoOids, String needRecoverNums,
			String notes, String dismediatypes)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void updateDesInfos(String desInfoOids, String needDestroyNums,
			String notes, String dismediatypes) {
		//����������Ϣ����
		List<RecDesInfo> updateDesInfo = new ArrayList<RecDesInfo>();
		//������ϢOID����
		List<String> desInfoOidArr = SplitString.string2List(desInfoOids, "!");
		//��Ҫ���ٷ�������
		List<String> needDestroyNumArr = SplitString.string2List(needDestroyNums, ",");
		//��ע����
		List<String> noteArr = SplitString.string2List(notes, ",");
		//���ŷ�ʽ����
		List<String> dismediatypeArr = SplitString.string2List(dismediatypes, ",");

		for(int i = 0; i < desInfoOidArr.size(); i++){
			List<String> oids = SplitString.string2List(desInfoOidArr.get(i), ",");
			//ѭ����ѯ����ѡ���������Ϣ���ϲ���δ�ϲ��ģ�
			for(String oid : oids){
			//������Ϣ
			RecDesInfo recDesInfo = (RecDesInfo) Helper.getPersistService().getObject(oid);

			//���û��շ���
			recDesInfo.setNeedDestroyNum(Long.parseLong(needDestroyNumArr.get(i)));
			//���ñ�ע
			String note = "";

			if(!StringUtil.isStringEmpty(noteArr.get(i))){
				note = noteArr.get(i);
			}

			recDesInfo.setNote(note);
			//���÷ַ�����
			String dismediatype = "";
			if ((ConstUtil.C_PAPERTASK).equals(dismediatypeArr.get(i))) {
				dismediatype = ConstUtil.C_DISMEDIATYPE_0;
			} else if ((ConstUtil.C_ELECTASK).equals(dismediatypeArr.get(i))) {
				dismediatype = ConstUtil.C_DISMEDIATYPE_1;
			} else if ((ConstUtil.C_OUTSITETASK).equals(dismediatypeArr.get(i))) {
				dismediatype = ConstUtil.C_DISMEDIATYPE_2;
			}
			recDesInfo.setDisMediaType(dismediatype);

			updateDesInfo.add(recDesInfo);

			}
		}

		//��������
		Helper.getPersistService().update(updateDesInfo);
		
	}

	/*
	 * ���� Javadoc��
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#deleteRecDesInfos(String oids)
	 */
	@Override
	public void deleteRecDesInfos(String oids) {
		//�ѻ���������ϢOID�ŵ�������
		List<String> oidArr = SplitString.string2List(oids, ",");
		List<RecDesInfo> recDesInfos = new ArrayList<RecDesInfo>();

		for(String recDesOid : oidArr){
			String classId = Helper.getPersistService().getClassId(recDesOid);
			String innerId = Helper.getPersistService().getInnerId(recDesOid);

			RecDesInfo recDesInfo = new RecDesInfo();

			recDesInfo.setClassId(classId);
			recDesInfo.setInnerId(innerId);

			recDesInfos.add(recDesInfo);
		}

		//����ɾ������������Ϣ
		Helper.getPersistService().deleteAll(recDesInfos);

	}
	/**
	 * ��ѯ������Ϣ
	 * 
	 * @param disInfoID
	 *			 �ַ���ϢIID����Ա����֯���ڲ���ʶ��
	 * @param disInfoName
	 *			�ַ���Ϣ���ƣ���λ/��Ա��
	 * @return List<Map<String, Object>>
	 *			������Ϣ����List����
	 */
	public List<Map<String, Object>> getDistributeInfo(String disInfoID, String disInfoName){
		String sql = " SELECT D.* from DDM_DIS_INFO D " ;
		List<Map<String, Object>> disInfo = new ArrayList<Map<String,Object>>();
		if(null != disInfoID && null != disInfoName && !"".equals(disInfoID) && !"".equals(disInfoName)){
			sql += "WHERE D.DISINFOID = ? AND D.DISINFONAME= ? ";
			//ִ�в�ѯ
			disInfo = Helper.getPersistService().query(sql, disInfoID, disInfoName);
		}
		return disInfo;
	}

	/*
	 * ���� Javadoc��
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#List<Map<String, Object>> checkRecDistributeOrder(String oid)
	 */
	@Override
	public List<Map<String, Object>> checkRecDistributeOrder(String oid) {
		String classId = Helper.getClassId(oid);
		String innerId = Helper.getInnerId(oid);

		String sql = " SELECT F .* FROM (SELECT DISTINCT C.INNERID, C.CLASSID FROM (SELECT DISTINCT "
				+ " A .INNERID,A .CLASSID,A .TOOBJECTCLASSID,A .TOOBJECTID FROM DDM_DIS_ORDEROBJLINK A "
				+ " WHERE A .FROMOBJECTCLASSID = ? AND A .FROMOBJECTID = ? ) B, DDM_DIS_ORDEROBJLINK C, "
				+ " DDM_DIS_ORDER D WHERE C.TOOBJECTCLASSID = B.TOOBJECTCLASSID AND C.TOOBJECTID = B.TOOBJECTID "
				+ " AND C.CLASSID = B.CLASSID AND C.INNERID <> B.INNERID AND C.FROMOBJECTCLASSID = D.CLASSID "
				+ " AND C.FROMOBJECTID = D.INNERID And D.ORDERTYPE = ? ) E, DDM_RECDES_INFO F WHERE "
				+ " F.DISORDEROBJECTLINKCLASSID = E.CLASSID AND F.DISORDEROBJECTLINKID = E.INNERID AND F.STATENAME <> ? ";

		//���շ��ŵ�
		String orderType = ConstUtil.C_ORDERTYPE_2;
		//���ա��ѷַ���״̬����
		String stateName = ConstUtil.LC_COMPLETED.getName();

		//ִ�в�ѯ
		List<Map<String, Object>> listRecOrderInfos = Helper.getPersistService()
				.query(sql, classId, innerId, orderType, stateName);
		if(listRecOrderInfos == null || listRecOrderInfos.isEmpty()){
			listRecOrderInfos = new ArrayList<Map<String,Object>>();
		}

		return listRecOrderInfos;
	}

	/*
	 * ���� Javadoc��
	 * @see  com.bjsasc.ddm.distribute.service.recDesInfo.RecDesInfoService#List<Map<String, Object>> checkDesDistributeOrder(String oid)
	 */
	@Override
	public List<Map<String, Object>> checkDesDistributeOrder(String oid) {
		String classId = Helper.getClassId(oid);
		String innerId = Helper.getInnerId(oid);
		String sql = " SELECT F .* FROM (SELECT DISTINCT C.INNERID, C.CLASSID FROM (SELECT DISTINCT "
				+ " A .INNERID,A .CLASSID,A .TOOBJECTCLASSID,A .TOOBJECTID FROM DDM_DIS_ORDEROBJLINK A "
				+ " WHERE A .FROMOBJECTCLASSID = ? AND A .FROMOBJECTID = ? ) B, DDM_DIS_ORDEROBJLINK C, "
				+ " DDM_DIS_ORDER D WHERE C.TOOBJECTCLASSID = B.TOOBJECTCLASSID AND C.TOOBJECTID = B.TOOBJECTID "
				+ " AND C.CLASSID = B.CLASSID AND C.INNERID <> B.INNERID AND C.FROMOBJECTCLASSID = D.CLASSID "
				+ " AND C.FROMOBJECTID = D.INNERID And D.ORDERTYPE = ? ) E, DDM_RECDES_INFO F WHERE "
				+ " F.DISORDEROBJECTLINKCLASSID = E.CLASSID AND F.DISORDEROBJECTLINKID = E.INNERID AND F.STATENAME <> ? ";

		//���ٷ��ŵ�
		String orderType = ConstUtil.C_ORDERTYPE_3;
		//���١�����ɡ�״̬����
		String stateName = ConstUtil.LC_COMPLETED.getName();
		//ִ�в�ѯ
		List<Map<String, Object>> listDesOrderInfos = Helper.getPersistService()
				.query(sql, classId, innerId, orderType, stateName);
		if(listDesOrderInfos == null || listDesOrderInfos.isEmpty()){
			listDesOrderInfos = new ArrayList<Map<String,Object>>();
		}

		return listDesOrderInfos;
	}

	/**
	 * ��ѯ����Link��Ϣ
	 * 
	 * @param distributeOrderOid
	 *			���ŵ�OID
	 * @return
	 */
	public List<DistributeOrderObjectLink> getObjectLinkByDistributeOrderOid (String distributeOrderOid){
		String hql = "from DistributeOrderObjectLink t "
				+ "where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? ";
		//���ŵ�InnerId
		String distributeOrderInnerId = Helper.getInnerId(distributeOrderOid);
		//���ŵ�ClassId
		String distributeOrderClassId = Helper.getClassId(distributeOrderOid);
		//��ѯLink��Ϣ
		List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, distributeOrderInnerId,
				distributeOrderClassId);
		return links;
	}

	/**
	 * ��֤�Ƿ��ĳ����λ����Ա���Ź�ĳ����������
	 * 
	 * @param disOrderObjLinkOid
	 *			����Link��ϢOid
	 * @param disInfoId
	 *			�ַ���ϢIID����Ա����֯���ڲ���ʶ��
	 * @param disInfoName
	 *			�ַ���Ϣ���ƣ���λ/��Ա��
	 * @return �Ƿ񷢷Ź�ĳ������
	 */
	public boolean checkDistributedByDisInfoIdAndDisInfoName(String disOrderObjLinkOid, String disInfoId, String disInfoName) {
		//����Link��ϢInnerId
		String disOrderObjLinkId = Helper.getInnerId(disOrderObjLinkOid);
		//����Link��ϢClassId
		String disOrderObjLinkClassId = Helper.getClassId(disOrderObjLinkOid);
		String sql = "SELECT DISTINCT B.* FROM DDM_DIS_ORDEROBJLINK A, DDM_DIS_INFO B WHERE A.INNERID = ? "
				+ " AND A.CLASSID = ? AND A.CLASSID = B.DISORDEROBJLINKCLASSID AND "
				+ " A.INNERID = B.DISORDEROBJLINKID AND B.DISINFOID = ? AND B.DISINFONAME = ? AND "
				+ " B.STATENAME = ? ";
		//δ�ַ����
		String stateName = ConstUtil.LC_NOT_DISTRIBUT.getName();
		//ִ�в�ѯ
		List<Map<String, Object>> listDesOrderInfos = Helper.getPersistService()
				.query(sql, disOrderObjLinkId, disOrderObjLinkClassId, disInfoId, disInfoName, stateName);
		if(listDesOrderInfos.size() > 0){
			return true;
		}
		return false;

	}


	/**
	 * ������Ϣ��Ҫ�ϲ����ж�����ƴ���ַ���
	 *
	 * @param disInfo
	 *			�ַ���ϢIID����Ա����֯���ڲ���ʶ��
	 * @param disName
	 *			�ַ���Ϣ���ƣ���λ/��Ա��
	 * @param disInfoNum
	 *			���ŷ���
	 * @param recoverNum
	 *			���շ���
	 * @param DisMediaType
	 *			��������
	 * @param disInfoType
	 *			�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
	 * @return result
	 *			ȥ���ַ�����ʾ��
	 */
	public String recInfoCombine(String disInfo, String disName, long disInfoNum, long recoverNum, long DisMediaType, long disInfoType){
		String result = disInfo + ";"
				+ disName + ";"
				+ disInfoNum + ";"
				+ recoverNum + ";"
				+ DisMediaType + ";"
				+ disInfoType;
		return result;
		
	}

	/**
	 * ������Ϣ��Ҫ�ϲ����ж�����ƴ���ַ���
	 *
	 * @param disInfo
	 *			�ַ���ϢIID����Ա����֯���ڲ���ʶ��
	 * @param disName
	 *			�ַ���Ϣ���ƣ���λ/��Ա��
	 * @param disInfoNum
	 *			���ŷ���
	 * @param recoverNum
	 *			���շ���
	 * @param DisMediaType
	 *			��������
	 * @param disInfoType
	 *			�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
	 * @return result
	 *			ȥ���ַ�����ʾ��
	 */
	public String desInfoCombine(String disInfo, String disName, long disInfoNum, long destroyNum, long DisMediaType, long disInfoType){
		String result = disInfo + ";"
				+ disName + ";"
				+ disInfoNum + ";"
				+ destroyNum + ";"
				+ DisMediaType +";"
				+ disInfoType;
		return result;
		
	}

	/**
	 * ����������Ϣ��Ҫ�ϲ����ж�����ƴ���ַ���
	 *
	 * @param disInfo
	 *			�ַ���ϢIID����Ա����֯���ڲ���ʶ��
	 * @param disName
	 *			�ַ���Ϣ���ƣ���λ/��Ա��
	 * @param disInfoNum
	 *			���ŷ���
	 * @param recoverNum
	 *			���շ���
	 * @param DisMediaType
	 *			��������
	 * @param disInfoType
	 *			�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
	 * @param stateId
	 *			״̬ID
	 * @param stateName
	 *			״̬����
	 * @return result
	 *			ȥ���ַ�����ʾ��
	 */
	public String recDesInfoCombine(String disInfo, String disName, long needRecoverNum, long needDestroyNum, String DisMediaType, String disInfoType, String stateId, String stateName){
		String result = disInfo + ";"
				+ disName + ";"
				+ needRecoverNum + ";"
				+ needDestroyNum + ";"
				+ DisMediaType + ";"
				+ disInfoType
				+ stateId + ";"
				+ stateName;
		return result;
		
	}

	/**
	 * @author sunzongqing
	 */
	public List<RecDesInfo> getpapertaskinfo(String orderObjectLinkOids, String taskOid) {
		String innerId = Helper.getInnerId(taskOid);
		String classId = Helper.getClassId(taskOid);
		List<String> oidsList = SplitString.string2List(orderObjectLinkOids, ",");
		List<DistributeOrderObjectLink> linksAll = new ArrayList<DistributeOrderObjectLink>();
		List<RecDesInfo> list = new ArrayList<RecDesInfo>();
		List<String> keyList = new ArrayList<String>();

		if (oidsList == null || oidsList.isEmpty()) {
			String sql = "SELECT distinct  C.* "
					+ "FROM DDM_RECDES_INFO A, "
					+ "DDM_DIS_TASKINFOLINK B, "
					+ "DDM_DIS_ORDEROBJLINK C "
					+ "WHERE A.CLASSID = B.TOOBJECTCLASSID "
					+ "AND A.INNERID = B.TOOBJECTID "
					+ "AND A.DISORDEROBJECTLINKID = C.INNERID "
					+ "AND A.DISORDEROBJECTLINKCLASSID = C.CLASSID "
					+ "AND B.FROMOBJECTID = ? "
					+ "AND B.FROMOBJECTCLASSID = ? ";

			List<DistributeOrderObjectLink> linkList = PersistHelper.getService().query(sql,
					DistributeOrderObjectLink.class, innerId, classId);
			linksAll.addAll(linkList);
		} else {
			String hql = "from DistributeOrderObjectLink t " 
					+ "where t.innerId=? "
					+ "and t.classId=? ";
			for (String linkOid : oidsList) {
				String innerid = Helper.getPersistService().getInnerId(linkOid);
				String classid = Helper.getPersistService().getClassId(linkOid);

				List<DistributeOrderObjectLink> linkList = PersistHelper.getService().find(hql, innerid, classid);
				linksAll.addAll(linkList);

			}
		}
		String sqlinfo = "select t.* "
				+ "from  ddm_recdes_info t , "
				+ "ddm_dis_taskinfolink b  "
				+ "where t.disorderobjectlinkid=? "
				+ "and disorderobjectlinkclassid=? "
				+ "and b.toobjectid=t.innerid "
				+ "and b.toobjectclassid=t.classid "
				+ "and b.fromobjectclassid='RecDesPaperTask' ";
		if (linksAll != null && linksAll.size() > 0) {
			for (DistributeOrderObjectLink link : linksAll) {
				String objinner = link.getInnerId();
				String objclass = link.getClassId();
				List<RecDesInfo> infos = PersistHelper.getService().query(sqlinfo, RecDesInfo.class, objinner,
						objclass);
				for (RecDesInfo info : infos) {
					String disInfo = info.getDisInfoId();
					String disName = info.getDisInfoName();
					long needRecoverNum = info.getNeedRecoverNum();
					long needDestroyNum = info.getNeedDestroyNum();
					String DisMediaType = info.getDisMediaType();
					String disInfoType = info.getDisInfoType();
					String stateId = info.getLifeCycleInfo().getStateId();
					String stateName = info.getLifeCycleInfo().getStateName();
					String stringFlag = recDesInfoCombine(disInfo, disName, needRecoverNum, 
							needDestroyNum, DisMediaType, disInfoType, stateId, stateName);
					if (keyList.contains(stringFlag)) {
						continue;
					}
					info.addOid(info.getOid());
					keyList.add(stringFlag);
					info.setDisDeadLine(link.getDisDeadLine());
					info.setDisUrgent(link.getDisUrgent());
					list.add(info);
				}
			}
		}
		return list;

	}

	/* ���� Javadoc��
	 * @see com.bjsasc.ddm.distribute.service.recdesinfo.RecDesInfoService#deleteDistributeRecDesInfoByOid(DistributeOrderObjectLink link)
	 */
	@Override
	public void deleteDistributeRecDesInfoByOid(DistributeOrderObjectLink link) {

		String hql = "from RecDesInfo r where r.disOrderObjectLinkId=? and r.disOrderObjectLinkClassId=?";
		//���ݷַ�����link��ѯ����������Ϣ
		List<RecDesInfo> recDesInfolist = PersistHelper.getService().find(hql, link.getInnerId(), link.getClassId());

		if(recDesInfolist.size() > 0){
			//ɾ���������ٷַ���Ϣ
			Helper.getPersistService().delete(recDesInfolist);
		}
		//ɾ���ַ�����link
		Helper.getPersistService().delete(link);
	}

}
